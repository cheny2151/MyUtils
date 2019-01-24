package awt.qrcode;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author cheney
 */
public class QRCodeUtils {

    private final static int DEFAULT_HEIGHT = 300;
    private final static int DEFAULT_WIDTH = 300;

    private final static Map<EncodeHintType, Object> ENCODE_HINTS;
    private final static Map<DecodeHintType, Object> DECODE_HINTS;

    static {
        ENCODE_HINTS = new HashMap<>();
        ENCODE_HINTS.put(EncodeHintType.CHARACTER_SET, "utf-8");
        //边框
        ENCODE_HINTS.put(EncodeHintType.MARGIN, 2);
        ENCODE_HINTS.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);

        DECODE_HINTS = new HashMap<>();
        DECODE_HINTS.put(DecodeHintType.CHARACTER_SET, "utf-8");
    }

    /**
     * 生成二维码
     *
     * @param content 二维码内容
     * @param height  高度
     * @param width   宽度
     * @return bufferedImage
     * @throws WriterException
     */
    public static BufferedImage createQRCode(String content, int height, int width) throws WriterException {

        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, height, width, ENCODE_HINTS);

        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

    public static BufferedImage createQRCode(String content) throws WriterException {
        return createQRCode(content, DEFAULT_HEIGHT, DEFAULT_WIDTH);
    }

    /**
     * 解析二维码
     *
     * @param inputStream 二维码输入流
     * @return 二维码内容
     * @throws Exception
     */
    public static String decodeQR(InputStream inputStream) throws Exception {
        BufferedImage bufferedImage = ImageIO.read(inputStream);
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(bufferedImage)));
        Result decode = new MultiFormatReader().decode(binaryBitmap, DECODE_HINTS);
        return decode.getText();
    }

}
