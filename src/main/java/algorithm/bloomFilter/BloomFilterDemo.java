package algorithm.bloomFilter;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.openjdk.jol.info.ClassLayout;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;

/**
 * @author cheney
 * @date 2020-05-08
 */
@SuppressWarnings("UnstableApiUsage")
public class BloomFilterDemo {

    /**
     * 默认预计个数：100W
     */
    private final static int DEFAULT_EXPECTED_INSERTIONS = 0xF4240;

    private BloomFilter<String> bloomFilter;

    private int expectedInsertions;

    public BloomFilterDemo() {
        this.bloomFilter = BloomFilter.create(Funnels.stringFunnel(StandardCharsets.UTF_8), DEFAULT_EXPECTED_INSERTIONS, 0.0001);
    }

    public BloomFilterDemo(int expectedInsertions) {
        this.bloomFilter = com.google.common.hash.BloomFilter.create(Funnels.stringFunnel(StandardCharsets.UTF_8), expectedInsertions, 0.01);
    }

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        com.google.common.hash.BloomFilter<CharSequence> instance = com.google.common.hash.BloomFilter.create(Funnels.stringFunnel(StandardCharsets.UTF_8), DEFAULT_EXPECTED_INSERTIONS, 0.00001);
        // 获取BloomFilter.bits.data.array占用内存大小
        Field bits = com.google.common.hash.BloomFilter.class.getDeclaredField("bits");
        bits.setAccessible(true);
        Object o = bits.get(instance);
        Field data = o.getClass().getDeclaredField("data");
        data.setAccessible(true);
        Object o1 = data.get(o);
        Field array = o1.getClass().getDeclaredField("array");
        array.setAccessible(true);
        Object o2 = array.get(o1);
        ClassLayout classLayout = ClassLayout.parseInstance(o2);
        System.out.println(classLayout.toPrintable());
    }


}
