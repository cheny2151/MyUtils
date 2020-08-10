package scan;

import DesignPattern.TypeSwitchChain.BaseTypeSwitch;
import expression.cheney.test.Main;
import scan.filter.ScanFilter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * @author cheney
 * @date 2019-12-27
 */
public class TestMain {

    public static void main(String[] args) throws ScanException {
        test0();
        System.out.println("--------------");
        test1();
        System.out.println("--------------");
//        test2();
    }

    public static void test0() throws ScanException {
        ScanFilter scanFilter = new ScanFilter();
        scanFilter.setSuperClass(BaseTypeSwitch.class);
        PathScan pathScan = new PathScan(scanFilter);
        List<Class<?>> classes = pathScan.scanClass("DesignPattern");
        classes.forEach(clazz -> System.out.println(clazz.getSimpleName()));
    }

    public static void test1() throws ScanException {
        PathScan pathScan = new PathScan();
        List<Class<?>> classes = pathScan.scanClass("expression.cheney");
        classes.forEach(clazz -> System.out.println(clazz.getSimpleName()));
    }

    public static void test2() throws ScanException {
        PathScan pathScan = new PathScan();
        Class<? extends Main> aClass = Main.class;
        URL resource = aClass.getResource("../func");
        System.out.println(resource.getFile());
        List<Class<?>> classes = pathScan.scanClass(resource);
        classes.forEach(System.out::println);
    }

    public static void test3(String path) throws IOException {
        Enumeration<URL> scan = Thread.currentThread().getContextClassLoader().getResources(path);
        ArrayList<URL> list = Collections.list(scan);
        for (URL url : list) {
            System.out.println("url:" + url);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }
    }

}
