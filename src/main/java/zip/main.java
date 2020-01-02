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
        ZipFile zipFile = new ZipFile(path, Charset.forName("gbk"));
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        ZipEntry zipEntry;
        while (entries.hasMoreElements()) {
            zipEntry = entries.nextElement();
            if (!zipEntry.getName().contains("汇总")) {
                InputStream inputStream = zipFile.getInputStream(zipEntry);
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("gbk")));
                String line;
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                }
                br.close();
            }
        }
        zipFile.close();
    }

}
