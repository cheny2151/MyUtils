package csv;

import com.alibaba.fastjson.JSON;
import com.csvreader.CsvReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @author cheney
 * @date 2019-12-25
 */
public class Demo {

    public static void main(String[] args) throws IOException {
        File file = new File("D://支付宝账单-200217-201906（1）.csv");
        CsvReader csvReader = new CsvReader(new FileInputStream(file), Charset.forName("GBK"));
        while (csvReader.readRecord()) {
            System.out.println(JSON.toJSONString(csvReader.getValues()));
        }
    }

}
