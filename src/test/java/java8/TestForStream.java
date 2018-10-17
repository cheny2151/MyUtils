package java8;

import DesignPattern.observer.single.Student;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TestForStream {

    @Test
    public void test(){
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
    }

}
