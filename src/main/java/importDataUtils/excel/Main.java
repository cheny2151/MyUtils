package importDataUtils.excel;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author cheney
 * @date 2019/5/27
 */
public class Main {

    private static String sql;

    static {
        Properties properties = new Properties();
        try {
            InputStream resourceAsStream = Main.class.getClassLoader().getResourceAsStream("db.properties");
            properties.load(resourceAsStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sql = properties.getProperty("sql.import");
    }

}
