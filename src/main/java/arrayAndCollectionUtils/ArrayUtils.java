package arrayAndCollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ArrayUtils {

    private ArrayUtils() {
    }

    /**
     * 拼接id 逗号隔开
     *
     * @param array
     * @return
     */
    public static String concat(Object[] array) {
        StringBuilder stringBuffer = new StringBuilder();
        for (Object i : array) {
            stringBuffer.append("\"").append(i.toString()).append("\",");
        }
        return stringBuffer.subSequence(0, stringBuffer.length() - 1).toString();
    }

    /**
     * long[] to String[]
     */
    public static Collection asStringList(long[] longs) {
        List<String> strings = new ArrayList<>(longs.length);
        for (long aLong : longs) {
            strings.add(String.valueOf(aLong));
        }
        return strings;
    }

    /**
     * Object[] to String[]
     */
    public static Collection asStringList(Object[] array) {
        List<String> strings = new ArrayList<>(array.length);
        for (Object obj : array) {
            strings.add(String.valueOf(obj.toString()));
        }
        return strings;
    }

    /**
     * remove Long[] null to long[]
     */
    public static long[] romoveNullElement(Long[] array) {
        long[] newArray = new long[array.length];
        int nullCount = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] == null) {
                nullCount++;
            } else {
                newArray[i - nullCount] = array[i];
            }
        }
        for (long i : newArray) {
            System.out.println(i);
        }
        if (nullCount > 0) {
            long[] newArray_2 = new long[array.length - nullCount];
            System.arraycopy(newArray, 0, newArray_2, 0, array.length - nullCount);
            return newArray_2;
        }
        return newArray;
    }

}
