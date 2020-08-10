package scan;

import lombok.extern.slf4j.Slf4j;
import scan.filter.ScanFilter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/**
 * 扫描包下得所有class
 *
 * @author cheney
 * @date 2019/6/26
 */
@Slf4j
public class PathScan {

    private final static String CLASS_EXTENSION = ".class";

    public static final String TARGET_CLASSES = "/target/classes/";

    // .class长度
    public static final int CLASS_END_LEN = 6;

    // 空路径
    private final static String EMPTY_PATH = "";

    // 分隔符
    public static final String SEPARATE_CHARACTER = ".";

    // 为root的路径
    private final static String[] ROOT_PATH = new String[]{SEPARATE_CHARACTER, EMPTY_PATH};

    // 过滤器
    private ScanFilter scanFilter;

    public PathScan() {
    }

    public PathScan(ScanFilter scanFilter) {
        this.scanFilter = scanFilter;
    }

    /**
     * 扫描项目中指定包名的所有类
     * 例如 expression.cheney
     * 使用"","."扫描根，即项目中所有类
     *
     * @param scanPath 包路径,以'.'或者'/'为分隔符
     * @return 扫描到的Class
     */
    public List<Class<?>> scanClass(String scanPath) throws ScanException {
        scanPath = SEPARATE_CHARACTER.equals(scanPath) ? EMPTY_PATH : scanPath;
        String resourcePath = scanPath.replaceAll("\\.", "/");

        URL resource = PathScan.class.getClassLoader().getResource(resourcePath);
        if (resource == null) {
            throw new ScanException("目录\"" + scanPath + "\"不存在");
        }

        String protocol = resource.getProtocol();
        if (log.isDebugEnabled()) {
            log.debug("file protocol:{}", protocol);
        }

        ArrayList<Class<?>> results = new ArrayList<>();
        scanPath = scanPath.replaceAll("/", ".");
        if ("file".equals(protocol)) {
            String file = resource.getFile();
            scanClassInFile(scanPath, new File(file), results, new boolean[]{true});
        } else if ("jar".equals(protocol)) {
            scanClassInJar(scanPath, resource, results, new boolean[]{true});
        }

        return results;
    }

    /**
     * 扫描项目中指定URL下的所有类
     *
     * @param resource url资源
     * @return 扫描到的Class
     */
    public List<Class<?>> scanClass(URL resource) throws ScanException {
        ArrayList<Class<?>> result = new ArrayList<>();
        if ("file".equals(resource.getProtocol())) {
            String file = resource.getFile();
            if (!file.contains(TARGET_CLASSES)) {
                throw new ScanException("Resource is not a class path");
            }
            String[] classPaths = file.split(TARGET_CLASSES);
            String scanPath = classPaths.length == 1 ? EMPTY_PATH : classPaths[1].replace("/", ".");
            scanClassInFile(scanPath, new File(file), result, new boolean[]{true});
        }

        return result;
    }

    /**
     * 从本地File中扫描所有类
     *
     * @param parentPath 上级目录,以','为分隔符
     * @param file       文件
     * @param result     扫描结果集
     * @param first      是否首次调用
     */
    private void scanClassInFile(String parentPath, File file, List<Class<?>> result, boolean[] first) {
        if (file.isFile()) {
            String ClassFileName = parentPath + file.getName();
            filterClass(ClassFileName, result);
        } else if (file.isDirectory()) {
            File[] childFiles = file.listFiles((childFile) ->
                    childFile.isDirectory() || childFile.getName().endsWith(CLASS_EXTENSION)
            );
            if (childFiles == null || childFiles.length == 0) {
                return;
            }
            String nextScanPath = getNextScanPath(parentPath, file, first);
            for (File child : childFiles) {
                scanClassInFile(nextScanPath, child, result, first);
            }
        }
    }

    /**
     * 获取下个包路径
     *
     * @param parentPath 上级目录,以','为分隔符
     * @param directory  扫描的当前目录
     * @param first      是否首次调用
     * @return 下一个包路径
     */
    private String getNextScanPath(String parentPath, File directory, boolean[] first) {
        if (first[0]) {
            // 首次进入
            first[0] = false;
            if (EMPTY_PATH.equals(parentPath) || parentPath.endsWith(SEPARATE_CHARACTER)) {
                return parentPath;
            }
            return parentPath + SEPARATE_CHARACTER;
        } else {
            return parentPath + directory.getName() + SEPARATE_CHARACTER;
        }
    }

    /**
     * 从本地jar包中扫描所有类
     *
     * @param parentPath 上级目录,以','为分隔符
     * @param url        jar包的url资源
     * @param result     扫描结果集
     * @param first      是否首次调用
     */
    private void scanClassInJar(String parentPath, URL url, List<Class<?>> result, boolean[] first)
            throws ScanException {
        url = extractRealJarUrl(url);
        if (url == null || !isJar(url)) {
            if (log.isDebugEnabled()) {
                log.debug("url:{} not a jar", url);
            }
            return;
        }
        try {
            JarInputStream jarInputStream = new JarInputStream(url.openStream());
            loadResourcesInJar(parentPath, jarInputStream, result);
        } catch (IOException e) {
            throw new ScanException("加载资源异常", e);
        }
    }

    /**
     * 提取有效jar包URL
     *
     * @param url 原url
     * @return jar url
     */
    private URL extractRealJarUrl(URL url) {
        String jarUrl = url.toExternalForm();
        int startIndex = jarUrl.startsWith("jar:") ? 4 : 0;
        jarUrl = jarUrl.substring(startIndex, jarUrl.indexOf(".jar") + 4);
        try {
            return new URL(jarUrl);
        } catch (MalformedURLException e) {
            return null;
        }
    }

    /**
     * 从jarInputStream流中提取有效的类并添加到result中
     *
     * @param parentPath     上级目录,以','为分隔符
     * @param jarInputStream jar文件流
     * @param result         结果合集
     * @throws IOException
     */
    private void loadResourcesInJar(String parentPath, JarInputStream jarInputStream, List<Class<?>> result)
            throws IOException {
        JarEntry nextJarEntry;
        while ((nextJarEntry = jarInputStream.getNextJarEntry()) != null) {
            if (!nextJarEntry.isDirectory()) {
                String ClassFileName = nextJarEntry.getName().replaceAll("/", ".");
                if (ClassFileName.startsWith(parentPath)) {
                    filterClass(ClassFileName, result);
                }
            }
        }
    }

    /**
     * 过滤有效类添加到result中
     *
     * @param ClassFileName 文件名
     * @param result        扫描结果集合
     */
    private void filterClass(String ClassFileName, List<Class<?>> result) {
        Class<?> target;
        try {
            String fullClassName = ClassFileName.substring(0, ClassFileName.length() - CLASS_END_LEN);
            target = loadClass(fullClassName);
        } catch (ClassNotFoundException e) {
            log.error("can not find class name:{}", ClassFileName);
            return;
        }
        if (scanFilter != null) {
            Class<?> superClass = scanFilter.getSuperClass();
            if (superClass != null &&
                    (!superClass.isAssignableFrom(target) ||
                            superClass.equals(target))) {
                return;
            }
            List<Class<? extends Annotation>> hasAnnotations = scanFilter.getHasAnnotations();
            if (hasAnnotations != null && !hasAnnotations.isEmpty()) {
                boolean hasAll = hasAnnotations.stream().allMatch(a -> target.getAnnotation(a) != null);
                if (!hasAll) {
                    return;
                }
            }
        }
        result.add(target);
    }

    private Class<?> loadClass(String fullClassName) throws ClassNotFoundException {
        return Thread.currentThread().getContextClassLoader().loadClass(fullClassName);
    }


    /**
     * jar文件的magic头
     */
    private static final byte[] JAR_MAGIC = {'P', 'K', 3, 4};

    /**
     * 判断url所对应的资源是否为jar包
     */
    protected boolean isJar(URL url) {
        return isJar(url, new byte[JAR_MAGIC.length]);
    }

    protected boolean isJar(URL url, byte[] buffer) {
        InputStream is = null;
        try {
            is = url.openStream();
            is.read(buffer, 0, JAR_MAGIC.length);
            if (Arrays.equals(buffer, JAR_MAGIC)) {
                if (log.isDebugEnabled()) {
                    log.debug("Found JAR: " + url);
                }
                return true;
            }
        } catch (Exception e) {
            // Failure to read the stream means this is not a JAR
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    // Ignore
                }
            }
        }

        return false;
    }

}
