package algorithm.hyperLogLog.my;

import java.util.Random;

/**
 * hyperLogLog中桶数组
 *
 * @author cheney
 * @date 2020-01-19
 */
public class SlotArray {

    /**
     * 桶最大数,6位1:63
     */
    private final static long MAX_SIX_BIT = 0x3F;

    /**
     * 一个桶6个bit位
     */
    private final static int SLOT_BIT = 6;

    /**
     * 桶个数
     */
    public final static int SLOT_SIZE = 1 << 14;

    /**
     * ({@link SLOT_BIT} * {@link SLOT_SIZE})/{@link Long#SIZE}
     */
    private final static int ARRAY_LEN = 1536;

    /**
     * 用于存放{@link SLOT_SIZE}个桶的bit
     */
    private long[] slotBits;

    public SlotArray() {
        this.slotBits = new long[ARRAY_LEN];
    }

    public void putValue(int index, int value) {
        if (value > MAX_SIX_BIT) {
            throw new IllegalArgumentException("max value is " + MAX_SIX_BIT);
        }
        int oldVal = getValue(index);
        if (value > oldVal) {
            putVal(index, value);
        }
    }

    public int getValue(int index) {
        if (index < 0 || index >= SLOT_SIZE) {
            throw new IllegalArgumentException("index must [0," + SLOT_SIZE + ") ,but is " + index);
        }
        int preSlotLength = index * SLOT_BIT;
        int preSlotIndex = preSlotLength / Long.SIZE;
        long offset = preSlotLength % Long.SIZE;
        long hitBitArray = slotBits[preSlotIndex];
        // 将offset前6位值设为1，再&long，最后无符号右移offset个偏移位
        int val1 = (int) ((MAX_SIX_BIT << offset & hitBitArray) >>> offset);
        if (offset <= Long.SIZE - SLOT_BIT) {
            return val1;
        } else {
            // 当偏移量大于58时，需要获取下一位long补充
            long nextBitArray = slotBits[preSlotIndex + 1];
            int existsBit = Long.SIZE - (int) offset;
            // 补充高位：取nextBitArray前(6-existsBit)位再左移existsBit位+低位
            return (int) ((MAX_SIX_BIT >>> existsBit & nextBitArray) << existsBit) + val1;
        }
    }

    private void putVal(int index, int value) {
        int preSlotLength = index * SLOT_BIT;
        int preSlotIndex = preSlotLength / Long.SIZE;
        long offset = preSlotLength % Long.SIZE;
        long hitBitArray = slotBits[preSlotIndex];
        if (offset <= Long.SIZE - SLOT_BIT) {
            /* (MAX_SIX_BIT << offset | hitBitArray) : 将offset前6位值设为1；
             * ~(~vl << offset) : 值二进制位取反左移offer位后反转回去 */
            slotBits[preSlotIndex] = (MAX_SIX_BIT << offset | hitBitArray) & ~(~(long) value << offset);
        } else {
            // 当偏移量大于58时，需要将额外的值存放到下一位long
            long nextBitArray = slotBits[preSlotIndex + 1];
            int existsBit = Long.SIZE - (int) offset;
            long bit0 = (1 << existsBit) - 1;
            // 将vl低existsBit位赋值到当前long的高existsBit位
            slotBits[preSlotIndex] = (bit0 << offset | hitBitArray) & ~(~(bit0 & (long) value) << offset);
            long bit1 = MAX_SIX_BIT ^ bit0;
            // 将vl高(6-existsBit位)赋值到下一个long的低(6-existsBit位)
            slotBits[preSlotIndex + 1] = (bit1 >>> existsBit | nextBitArray) & ((long) value & bit1) >>> existsBit;
        }
    }

    public static void main(String[] args) {
        SlotArray slotArray = new SlotArray();
        Random random = new Random();
        for (int i = 0; i < 10000; i++) {
            int index = random.nextInt(SLOT_SIZE);
            int value = random.nextInt(64);
            System.out.println("put" + value + " to index " + index);
            slotArray.putValue(index, value);
            int value1 = slotArray.getValue(index);
            System.out.println(value1);
        }
    }

}
