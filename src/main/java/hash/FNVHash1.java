package hash;

import org.junit.Test;

/**
 * Created by cheny on 2018/1/2.
 */
public class FNVHash1 {

    /**
     * 改进的32位FNV算法1
     * @param data 字符串
     * @return int值
     */
    public static int FNVHash1(String data)
    {
        final int p = 16777619;
        int hash = (int)2166136261L;
        for(int i=0;i<data.length();i++)
            hash = (hash ^ data.charAt(i)) * p;
//        hash += hash << 13;
//        hash ^= hash >> 7;
//        hash += hash << 3;
//        hash ^= hash >> 17;
//        hash += hash << 5;
        return hash;
    }

    @Test
    public void test(){
        int test = FNVHash1("192.168.137.1:8080");
        System.out.println(test);
        System.out.println("192.168.137.1:8080".hashCode());
    }

}
