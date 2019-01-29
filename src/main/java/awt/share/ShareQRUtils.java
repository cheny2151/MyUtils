package awt.share;

import awt.qrcode.QRCodeUtils;
import com.google.zxing.WriterException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;
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

    //通用间隙
    private static final int DEFAULT_GAP = 40;

    //背景宽度
    private final static int BG_WIDTH = 900;

    //背景高度
    private final static int BG_HEIGHT = 1500;

    //二维码高度
    private static final int QR_HEIGHT = 280;

    //二维码宽
    private static final int QR_WIDTH = 280;

    //商品信息图片高度
    private static final int PRODUCT_INFO_HEIGHT = 300;

    //商品信息图片宽度
    private static final int PRODUCT_INFO_WIDTH = BG_WIDTH - QR_WIDTH - DEFAULT_GAP;

    //头部图片高度
    private static final int HEAD_HEIGHT = 250;

    //头部图片宽度
    private static final int HEAD_WIDTH = BG_WIDTH * 19 / 20;

    private static final Color bgColor = new Color(251, 251, 251, 255);

    public static final String FONT_NAME = "Default";

    public static BufferedImage createShare(URL productUrl, URL headUrl, String nickname
            , String title, String productName, String currentPrice, String economize
            , String qrCodeContent) throws IOException, WriterException {

        BufferedImage bg = new BufferedImage(BG_WIDTH, BG_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics bgGraphics = bg.getGraphics();
        //背景
        bgGraphics.setColor(bgColor);
        bgGraphics.fillRect(0, 0, BG_WIDTH, BG_HEIGHT);

        //头部
        BufferedImage head = createHead(headUrl, nickname, title);
        bgGraphics.drawImage(head, 0, 0, head.getWidth(), head.getHeight(), null);

        //商品图片
        if (productUrl != null) {
            BufferedImage productImg = ImageIO.read(productUrl);
            productImg = getScaledImageByOne(productImg, 700, 2);
            productImg = round(productImg, 50, 50);
            bgGraphics.drawImage(productImg, (BG_WIDTH - productImg.getWidth()) / 2, HEAD_HEIGHT, productImg.getWidth(), productImg.getHeight(), null);
        }

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
     * 绘制头部图片
     *
     * @param headUrl  头像URL
     * @param nickname 昵称
     * @param title    标题文案
     * @return
     */
    private static BufferedImage createHead(URL headUrl, String nickname, String title) throws IOException {

        BufferedImage headImage = new BufferedImage(HEAD_WIDTH, HEAD_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics headGraphics = headImage.getGraphics();
        headGraphics.setColor(bgColor);
        headGraphics.fillRect(0, 0, headImage.getWidth(), headImage.getHeight());

        int x = DEFAULT_GAP;
        int y = DEFAULT_GAP;
        //头像
        if (headUrl != null) {
            BufferedImage userHeadImage = ImageIO.read(headUrl);
            userHeadImage = convertRoundedImage(userHeadImage, userHeadImage.getWidth());
            headGraphics.drawImage(userHeadImage, DEFAULT_GAP, DEFAULT_GAP, null);

            x = x + DEFAULT_GAP + userHeadImage.getWidth();
        }

        //昵称
        int fontSize = 40;
        Font font = new Font(FONT_NAME, Font.PLAIN, fontSize);
        headGraphics.setFont(font);
        FontMetrics fm = headGraphics.getFontMetrics();
        headGraphics.setColor(new Color(0, 0, 0));
        headGraphics.drawString(nickname, x, (y = y + fm.getHeight()));

        //标题文案
        fontSize = 30;
        font = new Font(FONT_NAME, Font.PLAIN, fontSize);
        headGraphics.setFont(font);
        fm = headGraphics.getFontMetrics();
        int titleWidth = fm.stringWidth(title);
        headGraphics.setColor(new Color(0, 0, 0));
        if (titleWidth + x > HEAD_WIDTH) {
            int sub = (HEAD_WIDTH - x) / (int) fontSize;
            String title1 = title.substring(0, sub);
            String title2 = title.substring(sub);
            headGraphics.drawString(title1, x, (y = y + fm.getHeight() + (DEFAULT_GAP / 2)));
            headGraphics.drawString(title2, x, y + fm.getHeight());
        } else {
            headGraphics.drawString(title, x, y + fm.getHeight() + (DEFAULT_GAP / 2));
        }
        headGraphics.dispose();

        return headImage;

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

        int fontSize = 40;
        int y = fontSize;
        int gap = (fontSize + 20);

        BufferedImage productInfoImage = new BufferedImage(PRODUCT_INFO_WIDTH, PRODUCT_INFO_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics infoGraphics = productInfoImage.getGraphics();
        infoGraphics.setColor(bgColor);
        infoGraphics.fillRect(0, 0, productInfoImage.getWidth(), productInfoImage.getHeight());
        Font font = new Font(FONT_NAME, Font.PLAIN, fontSize);
        infoGraphics.setFont(font);
        FontMetrics fontMetrics = infoGraphics.getFontMetrics();

        //商品信息
        int i = fontMetrics.stringWidth(productName);
        infoGraphics.setColor(new Color(0, 0, 0));
        if (i > PRODUCT_INFO_WIDTH) {
            int sub = PRODUCT_INFO_WIDTH / fontSize;
            String productName1 = productName.substring(0, sub);
            infoGraphics.drawString(productName1, DEFAULT_GAP * 2, y = (y + 10));
            String productName2 = productName.substring(sub);
            if (fontMetrics.stringWidth(productName2) > PRODUCT_INFO_WIDTH) {
                productName2 = productName2.substring(0, (PRODUCT_INFO_WIDTH / fontSize) - 1) + "...";
            }
            infoGraphics.drawString(productName2, DEFAULT_GAP * 2, y = (y + fontMetrics.getHeight() + 5));
        } else {
            infoGraphics.drawString(productName, DEFAULT_GAP * 2, y = (y + 10));
        }

        fontSize = 33;
        font = new Font(FONT_NAME, Font.PLAIN, fontSize);
        infoGraphics.setFont(font);
        fontMetrics = infoGraphics.getFontMetrics();
        int x = DEFAULT_GAP * 2;
        //当前价
        int len1 = fontMetrics.stringWidth("当前价");
        infoGraphics.setColor(new Color(0, 0, 0));
        infoGraphics.drawString("当前价", x, y = (y + gap));
        currentPrice = currentPrice + "元";
        infoGraphics.setColor(new Color(255, 0, 0));
        infoGraphics.drawString(currentPrice, len1 + x + DEFAULT_GAP, y);

        //拍中可省
        int len2 = fontMetrics.stringWidth("拍中可省");
        infoGraphics.setColor(new Color(0, 0, 0));
        infoGraphics.drawString("拍中可省", x, y = (y + gap));
        economize = economize + "元";
        infoGraphics.setColor(new Color(255, 0, 0));
        infoGraphics.drawString(economize, len2 + x + DEFAULT_GAP, y);

        infoGraphics.dispose();
        return productInfoImage;
    }

    public static void imageToFile(BufferedImage image, File file) throws IOException {
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

    /**
     * 方形转为圆形
     *
     * @param img    the img
     * @param radius the radius 半径
     * @return the buffered image
     */
    public static BufferedImage convertRoundedImage(BufferedImage img, int radius) {
        BufferedImage result = new BufferedImage(radius, radius, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = result.createGraphics();
        //在适当的位置画图
        g.drawImage(img, (radius - img.getWidth(null)) / 2, (radius - img.getHeight(null)) / 2, null);

        //圆角
        RoundRectangle2D round = new RoundRectangle2D.Double(0, 0, radius, radius, radius * 2, radius * 2);
        Area clear = new Area(new Rectangle(0, 0, radius, radius));
        clear.subtract(new Area(round));
        g.setComposite(AlphaComposite.Clear);

        //抗锯齿
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.fill(clear);
        g.dispose();

        return result;
    }


    private static int getOtherLen(int len, int oLen, int oOtherLen) {
        BigDecimal scale = new BigDecimal(len).divide(new BigDecimal(oLen), 2, RoundingMode.HALF_DOWN);
        return scale.multiply(new BigDecimal(oOtherLen)).intValue();
    }

    /**
     * 绘制圆角
     *
     * @param image
     * @param arcw
     * @param arch
     * @return
     */
    private static BufferedImage round(BufferedImage image, int arcw, int arch) {
        int width = image.getWidth();
        int height = image.getHeight();
        Graphics2D graphics = image.createGraphics();
        //前两个指定矩形的左上角的坐标
        //第3，4个指定矩形的宽度和高度
        //最后两个指定圆角弧形的宽度和高度
        RoundRectangle2D.Double round = new RoundRectangle2D.Double(0, 0, width, height, arcw, arch);
        graphics.draw(round);
        Area clear = new Area(new Rectangle(0, 0, width, height));
        clear.subtract(new Area(round));
        graphics.setComposite(AlphaComposite.Clear);

        //抗锯齿
        graphics.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_DEFAULT);
        graphics.fill(clear);
        graphics.dispose();
        return image;
    }

    private static BufferedImage createQRWithWord(String content) throws WriterException {
        BufferedImage qrCode = QRCodeUtils.createQRCode(content
                , QR_HEIGHT, QR_WIDTH);
        BufferedImage qrWithWord = new BufferedImage(qrCode.getWidth(), qrCode.getHeight() + 50, BufferedImage.TYPE_INT_RGB);
        Graphics qrGraphics = qrWithWord.getGraphics();
        qrGraphics.setColor(bgColor);
        qrGraphics.fillRect(0, 0, qrWithWord.getWidth(), qrWithWord.getHeight());
        qrGraphics.drawImage(qrCode, 0, 0, null);
        // 普通字体
        Font font = new Font(FONT_NAME, Font.PLAIN, 30);
        qrGraphics.setFont(font);
        qrGraphics.setColor(new Color(68, 68, 68));
        qrGraphics.drawString("长按识别查看商品", 20, qrWithWord.getHeight() - 25);
        qrGraphics.dispose();

        return qrWithWord;
    }


}
