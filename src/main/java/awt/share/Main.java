package awt.share;

import com.google.zxing.WriterException;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;


/**
 * @author cheney
 */
public class Main {

    @Test
    public void test() throws IOException, WriterException {
        BufferedImage read = ImageIO.read(new File("C:\\Users\\cheny\\Pictures\\share_20190124152015.png"));

        ShareQRUtils.imageToFile(ShareQRUtils.createShare(new URL("http://auction-fat.oss-cn-shenzhen.aliyuncs.com/COMMODITY/29668155-0703-40a6-a084-3b3abf998ba0.jpg")
                , new URL("https://wx.qlogo.cn/mmopen/vi_32/gUhMa7CSLZxG7iaa82t37lNrLoMNbiaOGMZ5DQLfCyHB7TGvKs2dds804Is3kNgzxGIcXDPomDxE3u7ibWpkTMz3g/132"), "崔长葱", "喜拍优品，拍卖新体验，省钱、省钱、省钱省钱省钱省钱省钱省钱省钱省钱！"
                , "法国拉比红酒法国拉比红酒法国拉比红酒法国拉比红酒法国拉比红酒法", "200", "50"
                , "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx6882489a7a2331d8&redirect_uri=http://auction-fat.mmdpp.cn/frontend-call/wechats/indexPage&response_type=code&scope=snsapi_userinfo&state=cmVkaXJlY3RVcmxfaHR0cDovL2F1Y3Rpb24tZmF0Lm1tZHBwLmNuL2luZGV4Lmh0bWwjL2xvZ2lu%0AP3BhcmVudFVpZD03NTQzZWQ3MzY4MDE0Nzk4YmU4OTBjYTA4ZGJkNTlhMCZidXNpbmVzc0lkPTEz%0AZjI3N2Y1ZmFhNjRjZjlhZDI0ZTI0MjQ3YWU0YTVi#wechat_redirect")
                , new File("C:\\Users\\cheny\\Pictures\\test.jpg"));
    }

}
