package algorithm.hyperLogLog.my;

import algorithm.hyperLogLog.MurmurHash;

/**
 * HyperLogLog是用于统计基数的算法，由伯努利实验
 * 1：将value hash后得到64位hash串；
 * 2：取64位hash的低14位作为桶位置，取剩下的50位，从低位往高位数，首次出现1的即为桶的值；
 * 3：与桶原值对比，取最大的存于桶中
 * 4：通过hyperLogLog算法计算基数
 * 5：对小基数进行偏差修正
 *
 * @author cheney
 * @date 2020-01-19
 */
public class HyperLogLog {

    /**
     * 桶取的位数
     */
    private final static int SLOT_DIGIT = 14;

    /**
     * 值取得位数
     */
    private final static int VALUE_DIGIT = 50;

    /**
     * 桶个数
     */
    private final static int SLOT_NUM = 1 << SLOT_DIGIT;

    /**
     * 14位1
     */
    private final static int SLOT_BIT = 0x3FFF;

    /**
     * 桶数据结构实体
     */
    private SlotArray slotArray;

    /**
     * 常量
     */
    private final static double constant = constant((int) (Math.log(SLOT_NUM) / Math.log(2)), SLOT_NUM);

    public HyperLogLog() {
        slotArray = new SlotArray();
    }

    /**
     * 添加基数
     *
     * @param object 基数
     */
    public void pfadd(Object object) {
        if (object == null) {
            throw new NullPointerException();
        }
        long hash = MurmurHash.hash64(object);
        int index = (int) (hash & SLOT_BIT);
        int value = firstTrueBit(hash >>> SLOT_DIGIT);

        slotArray.putValue(index, value);
    }

    /**
     * 统计基数
     *
     * @return 基数
     */
    public long pfcount() {
        int m = SLOT_NUM;
        SlotArray slotArray = this.slotArray;
        double zeroc = 0;
        double dv = 0;
        for (int i = 0; i < SlotArray.SLOT_SIZE; i++) {
            int value = slotArray.getValue(i);
            if (value == 0) {
                zeroc++;
                continue;
            }
            dv += 1.0 / (1 << value);
        }
        // 0值桶为 1/2^0 =1,补充0值
        dv = dv + zeroc;
        dv = constant * (1 / dv);

        if (dv <= 2.5 * m) {
            return Math.round(m * Math.log(m / zeroc));
        } else {
            return Math.round(dv);
        }
    }

    private int firstTrueBit(long value) {
        for (int i = 0; i < VALUE_DIGIT; i++) {
            if ((value >>> i & 1) == 1) {
                return i + 1;
            }
        }
        return 0;
    }

    private static double constant(int p, int slotNum) {
        switch (p) {
            case 4: {
                return 0.673 * slotNum * slotNum;
            }
            case 5: {
                return 0.697 * slotNum * slotNum;
            }
            case 6: {
                return 0.709 * slotNum * slotNum;
            }
            default: {
                return 0.7213 / (1 + 1.079 / slotNum) * slotNum * slotNum;
            }
        }
    }

    public static void main(String[] args) {
        HyperLogLog hyperLogLog = new HyperLogLog();
        //集合中只有下面这些元素
        for (int i = 0; i < 1023800; i++) {
            hyperLogLog.pfadd(i);
        }
        //估算基数
        System.out.println(hyperLogLog.pfcount());
    }
}
