package java8;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class TestForStream {

    @Test
    public void test() {
        ArrayList<String> strings = new ArrayList<>();
        strings.add("231423");
        strings.add("2343");
        strings.add("35ew");
        strings.add("ewr");
        strings.add("23trw");
        strings.add("32532");
        strings.add("34");
        List<Integer> collect = strings.stream().map(String::length).limit(3).collect(Collectors.toList());
        System.out.println(collect);
    }

    @Test
    public void test2(){
        long l = System.currentTimeMillis();
        ArrayList<List<String>> lists = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            ArrayList<String> strings = new ArrayList<>();
            for (int j = i * 20; j < (i + 1) * 20; j++) {
                strings.add(j + "");
            }
            lists.add(strings);
        }
        List<String> collect = lists.stream().flatMap(Collection::stream).collect(Collectors.toList());
        System.out.println(collect);
        System.out.println(System.currentTimeMillis()-l);
        System.out.println("");
    }
}
