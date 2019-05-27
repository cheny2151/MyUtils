package importDataUtils.excel;

import POIUtils.PoiUtils;
import importDataUtils.mysql.SimplePool;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
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

    public static void main(String[] args) {
        SimplePool simplePool = new SimplePool();
        try (Connection connection = simplePool.getConnection()) {
            List<Entity> entities = PoiUtils.readFormFile(new File(""), Entity.class);
            for (Entity entity : entities) {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
