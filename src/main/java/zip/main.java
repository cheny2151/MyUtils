package zip;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author cheney
 * @date 2020-01-02
 */
public class main {

    @Test
    public void test() throws IOException {
        String path = "D:\\test.zip";
        ZipFile zf = new ZipFile(path, Charset.forName("gbk"));
        Enumeration<? extends ZipEntry> entries = zf.entries();
        ZipEntry ze;
        while (entries.hasMoreElements()) {
            ze = entries.nextElement();
            if (!ze.getName().contains("汇总")) {
                InputStream inputStream = zf.getInputStream(ze);
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("gbk")));
                String line;
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                }
                br.close();
            }
        }
        zf.close();
    }

}
