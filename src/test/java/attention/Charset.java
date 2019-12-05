package attention;

import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

/**
 * @author cheney
 * @date 2019-12-05
 */
public class Charset {

    public static void main(String[] args) {
        String test = "test";
        byte[] bytes = test.getBytes(StandardCharsets.UTF_8);
        System.out.println(bytesToHexString(bytes));
    }

    public static String bytesToHexString(byte... src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (byte b : src) {
            int v = b & 0xFF;
            String hv = Integer.toHexString(v);
            System.out.println(Integer.toBinaryString(v));
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

}
