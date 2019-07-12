package scan;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 扫描包下得所有class
 *
 * @author cheney
 * @date 2019/6/26
 */
@Slf4j
public class PathScan {

    private final static String scanPath = "POIUtils";

    private final static String CLASS_EXTENSION = ".class";

    private final static String ROOT_PATH = ".";

    public static List<Class> scanClass(String scanPath) {
        String resourcePath = ROOT_PATH.equals(scanPath) ? ROOT_PATH : scanPath.replaceAll("\\.", "/");

        URL resource = PathScan.class.getClassLoader().getResource(resourcePath);
        if (resource == null) {
            throw new IllegalArgumentException("目录" + scanPath + "不存在");
        }

        ArrayList<Class> result = new ArrayList<>();
        if ("file".equals(resource.getProtocol())) {
            String file = resource.getFile();
            scanClassInFile(scanPath, new File(file), result, true);
        }

        return result;
    }

    private static void scanClassInFile(String parentPath, File file, ArrayList<Class> result, boolean root) {
        if (file.isFile()) {
            String className = file.getName().substring(0, file.getName().length() - 6);
            try {
                String fullClassName = parentPath + "." + className;
                result.add(Class.forName(fullClassName));
            } catch (ClassNotFoundException e) {
                log.error("can not find class name:{}", className);
            }
        } else if (file.isDirectory()) {
            File[] childFiles = file.listFiles((childFile) ->
                    childFile.isDirectory() || childFile.getName().endsWith(CLASS_EXTENSION)
            );
            if (childFiles == null || childFiles.length == 0) {
                return;
            }
            String nextScanPath = getNextScanPath(parentPath, file, root);
            for (File child : childFiles) {
                scanClassInFile(nextScanPath, child, result, false);
            }
        }
    }

    private static String getNextScanPath(String parentPath, File file, boolean root) {
        if (ROOT_PATH.equals(parentPath)) {
            // 根目录
            return "";
        } else if (root) {
            // 首次进入递归
            return parentPath;
        } else if ("".equals(parentPath)) {
            // 为空
            return file.getName();
        }
        return parentPath + "." + file.getName();
    }

    public static void main(String[] args) {
        List<Class> classes = scanClass(PathScan.scanPath);
        classes.forEach(clazz -> System.out.println(clazz.getSimpleName()));
    }


}
