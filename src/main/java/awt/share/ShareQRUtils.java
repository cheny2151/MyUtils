package awt.share;

import awt.qrcode.QRCodeUtils;
import com.google.zxing.WriterException;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;

/**
 * @author cheney
 */
public class ShareQRUtils {

    private final static int BG_WIDTH = 900;

    private final static int BG_HEIGHT = 1500;

    //二维码高度
    private static final int QR_HEIGHT = 280;

    //二维码宽
    private static final int QR_WIDTH = 280;

    //商品信息图片高度
    private static final int PRODUCT_INFO_HEIGHT = 300;
    //商品信息图片宽度
    private static final int PRODUCT_INFO_WIDTH = BG_WIDTH - QR_WIDTH;


    @Test
    public void test() throws IOException, WriterException {
        BufferedImage read = ImageIO.read(new File("C:\\Users\\cheny\\Pictures\\share_20190124152015.png"));

        imageToFile(createShare(new URL("http://auction-fat.oss-cn-shenzhen.aliyuncs.com/COMMODITY/29668155-0703-40a6-a084-3b3abf998ba0.jpg")
                , "法国拉比红酒法国拉比红酒法国拉比红酒", "200", "50"
                , "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx6882489a7a2331d8&redirect_uri=http://auction-fat.mmdpp.cn/frontend-call/wechats/indexPage&response_type=code&scope=snsapi_userinfo&state=cmVkaXJlY3RVcmxfaHR0cDovL2F1Y3Rpb24tZmF0Lm1tZHBwLmNuL2luZGV4Lmh0bWwjL2xvZ2lu%0AP3BhcmVudFVpZD03NTQzZWQ3MzY4MDE0Nzk4YmU4OTBjYTA4ZGJkNTlhMCZidXNpbmVzc0lkPTEz%0AZjI3N2Y1ZmFhNjRjZjlhZDI0ZTI0MjQ3YWU0YTVi#wechat_redirect")
                , new File("C:\\Users\\cheny\\Pictures\\test.jpg"));
    }

    public static BufferedImage createShare(URL imageUrl, String productName, String currentPrice, String economize, String qrCodeContent) throws IOException, WriterException {

        BufferedImage productImg = ImageIO.read(imageUrl);
        productImg = getScaledImageByOne(productImg, 700, 2);

        BufferedImage bg = new BufferedImage(BG_WIDTH, BG_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics bgGraphics = bg.getGraphics();
        //背景
        bgGraphics.setColor(new Color(255, 255, 255));
        bgGraphics.fillRect(0, 0, BG_WIDTH, BG_HEIGHT);
        //商品
        bgGraphics.drawImage(productImg, 0, 200, productImg.getWidth(), productImg.getHeight(), null);

        //二维码
        BufferedImage qrImage = createQRWithWord(qrCodeContent);
        bgGraphics.drawImage(qrImage, BG_WIDTH - QR_HEIGHT - 20, BG_HEIGHT - QR_WIDTH - 150, null);

        //商品信息
        BufferedImage productInfo = createProductInfo(productName, currentPrice, economize);
        bgGraphics.drawImage(productInfo, 0, BG_HEIGHT - QR_WIDTH - 150, null);

        bgGraphics.dispose();

        return bg;
    }

    /**
     * 绘制商品信息图片
     *
     * @param productName  商品名
     * @param currentPrice 当前价格
     * @param economize    拍中可省
     * @return
     */
    private static BufferedImage createProductInfo(String productName, String currentPrice, String economize) {

        int fontSize = 50;
        int currentHeight = fontSize;
        int gap = fontSize + 40;

        BufferedImage productInfoImage = new BufferedImage(PRODUCT_INFO_WIDTH, PRODUCT_INFO_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics infoGraphics = productInfoImage.getGraphics();
        infoGraphics.setColor(new Color(255, 255, 255));
        infoGraphics.fillRect(0, 0, productInfoImage.getWidth(), productInfoImage.getHeight());
        Font font = new Font("微软雅黑", Font.PLAIN, fontSize);
        infoGraphics.setFont(font);
        FontMetrics fontMetrics = infoGraphics.getFontMetrics();

        //商品图片
        int i = fontMetrics.stringWidth(productName);
        if (i > PRODUCT_INFO_WIDTH) {
            productName = productName.substring(0, (PRODUCT_INFO_WIDTH / fontSize) - 3) + "...";
        }
        infoGraphics.setColor(new Color(0, 0, 0));
        infoGraphics.drawString(productName, 20, currentHeight = (currentHeight + 20));

        fontSize = 38;
        font = new Font("微软雅黑", Font.PLAIN, fontSize);
        infoGraphics.setFont(font);
        fontMetrics = infoGraphics.getFontMetrics();
        //当前价
        int len1 = fontMetrics.stringWidth("当前价");
        infoGraphics.setColor(new Color(0, 0, 0));
        infoGraphics.drawString("当前价", 20, currentHeight = (currentHeight + gap));
        currentPrice = currentPrice + "元";
        infoGraphics.setColor(new Color(255, 0, 0));
        infoGraphics.drawString(currentPrice, len1 + 50, currentHeight);

        //拍中可省
        int len2 = fontMetrics.stringWidth("拍中可省");
        infoGraphics.setColor(new Color(0, 0, 0));
        infoGraphics.drawString("拍中可省", 20, currentHeight = (currentHeight + gap));
        economize = economize + "元";
        infoGraphics.setColor(new Color(255, 0, 0));
        infoGraphics.drawString(economize, len2 + 50, currentHeight);

        infoGraphics.dispose();
        return productInfoImage;
    }

    private static void imageToFile(BufferedImage image, File file) throws IOException {
        ImageIO.write(image, "jpg", file);
    }

    /**
     * 按指定的字节数截取字符串（一个中文字符占3个字节，一个英文字符或数字占1个字节）
     *
     * @param sourceString 源字符串
     * @param cutBytes     要截取的字节数
     * @return
     */
    public static String cutString(String sourceString, int cutBytes) {
        if (sourceString == null || "".equals(sourceString.trim())) {
            return "";
        }
        int lastIndex = 0;
        boolean stopFlag = false;
        int totalBytes = 0;
        for (int i = 0; i < sourceString.length(); i++) {
            String s = Integer.toBinaryString(sourceString.charAt(i));
            if (s.length() > 8) {
                totalBytes += 3;
            } else {
                totalBytes += 1;
            }
            if (!stopFlag) {
                if (totalBytes == cutBytes) {
                    lastIndex = i;
                    stopFlag = true;
                } else if (totalBytes > cutBytes) {
                    lastIndex = i - 1;
                    stopFlag = true;
                }
            }
        }
        if (!stopFlag) {
            return sourceString;
        } else {
            return sourceString.substring(0, lastIndex + 1);
        }
    }

    /**
     * 涂抹矩形
     *
     * @param image  原图片
     * @param x      涂抹起始x轴
     * @param y      涂抹起始y轴
     * @param width  矩形宽度
     * @param height 矩形高度
     * @param color  颜色
     * @return
     */
    public BufferedImage fillRect(BufferedImage image, int x, int y, int width, int height, Color color) {

        //生成块
        BufferedImage rect = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics whiteGraphics = rect.getGraphics();
        whiteGraphics.setColor(color);
        whiteGraphics.fillRect(0, 0, width, height);
        whiteGraphics.dispose();

        //绘制原图片
        int originWidth = image.getWidth();
        int originHeight = image.getHeight();
        BufferedImage bufferedImage = new BufferedImage(originWidth, originHeight, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = bufferedImage.getGraphics();
        graphics.drawImage(image, 0, 0, originWidth, originHeight, null);

        //放置矩形
        graphics.drawImage(rect, x, y, null);
        graphics.dispose();
        return bufferedImage;
    }

    /**
     * 图像等比例缩放
     *
     * @param img     the img
     * @param maxSize the max size
     * @param type    the type
     * @return the scaled image
     */
    private static BufferedImage getScaledImage(BufferedImage img, int maxSize, int type) {
        int w0 = img.getWidth();
        int h0 = img.getHeight();
        int w = w0;
        int h = h0;
        // 头像如果是长方形：
        // 1:高度与宽度的最大值为maxSize进行等比缩放,
        // 2:高度与宽度的最小值为maxSize进行等比缩放
        if (type == 1) {
            w = w0 > h0 ? maxSize : (maxSize * w0 / h0);
            h = w0 > h0 ? (maxSize * h0 / w0) : maxSize;
        } else if (type == 2) {
            w = w0 > h0 ? (maxSize * w0 / h0) : maxSize;
            h = w0 > h0 ? maxSize : (maxSize * h0 / w0);
        }
        Image schedImage = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
        BufferedImage bufferedImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = bufferedImage.createGraphics();
        g.drawImage(schedImage, 0, 0, null);
        return bufferedImage;
    }

    /**
     * 图像按宽度或高度等比例缩放
     *
     * @param img  the img
     * @param len  target len
     * @param type 1:width 2:height
     * @return the scaled image
     */
    private static BufferedImage getScaledImageByOne(BufferedImage img, int len, int type) {
        int w0 = img.getWidth();
        int h0 = img.getHeight();
        int w;
        int h;

        if (1 == type) {
            w = len;
            h = getOtherLen(len, w0, h0);
        } else if (2 == type) {
            h = len;
            w = getOtherLen(len, h0, w0);
        } else {
            throw new IllegalArgumentException("type must be 1 or 2");
        }

        Image schedImage = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
        BufferedImage bufferedImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = bufferedImage.createGraphics();
        g.drawImage(schedImage, 0, 0, null);
        return bufferedImage;
    }


    private static int getOtherLen(int len, int oLen, int oOtherLen) {
        BigDecimal scale = new BigDecimal(len).divide(new BigDecimal(oLen), 2, RoundingMode.HALF_DOWN);
        return scale.multiply(new BigDecimal(oOtherLen)).intValue();
    }

    public static BufferedImage createQRWithWord(String content) throws WriterException {
        BufferedImage qrCode = QRCodeUtils.createQRCode(content
                , QR_HEIGHT, QR_WIDTH);
        BufferedImage qrWithWord = new BufferedImage(qrCode.getWidth(), qrCode.getHeight() + 50, BufferedImage.TYPE_INT_RGB);
        Graphics qrGraphics = qrWithWord.getGraphics();
        qrGraphics.setColor(new Color(255, 255, 255));
        qrGraphics.fillRect(0, 0, qrWithWord.getWidth(), qrWithWord.getHeight());
        qrGraphics.drawImage(qrCode, 0, 0, null);
        // 普通字体
        Font font = new Font("微软雅黑", Font.PLAIN, 30);
        qrGraphics.setFont(font);
        qrGraphics.setColor(new Color(68, 68, 68));
        qrGraphics.drawString("长按识别查看商品", 20, qrWithWord.getHeight() - 25);
        qrGraphics.dispose();

        return qrWithWord;
    }

}
