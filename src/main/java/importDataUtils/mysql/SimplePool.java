package importDataUtils.mysql;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author cheney
 * @date 2019/5/27
 */
@Slf4j
public class SimplePool {

    private List<Connection> connections = new ArrayList<>();

    private Properties properties;

    private String url;

    private String username;

    private String password;

    public SimplePool() {
        InputStream inputStream = SimplePool.class.getClassLoader().getResourceAsStream("db.properties");
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            log.error("读取配置失败", e);
            throw new RuntimeException();
        }
        this.properties = properties;
        this.url = properties.getProperty("db.url");
        this.username = properties.getProperty("db.username");
        this.password = properties.getProperty("db.password");
        init();
    }

    private void init() {
        try {
            Class.forName(properties.getProperty("db.driver"));
        } catch (Exception e) {
            log.error("fail to init datasource", e);
            throw new RuntimeException();
        }
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public static void main(String[] args) throws SQLException {
        SimplePool simplePool = new SimplePool();
        PreparedStatement preparedStatement = simplePool.getConnection().prepareStatement("select * from t_hl_grade_cost",
                ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE);
        preparedStatement.setFetchSize(1);
        ResultSet resultSet = preparedStatement.executeQuery();
        System.out.println(resultSet.next());
        System.out.println(resultSet.getString("remark"));
        System.out.println(resultSet.next());
        System.out.println(resultSet.getString("remark"));
        System.out.println(resultSet.last());
        System.out.println(resultSet.getString("remark"));
        System.out.println(resultSet.first());
        System.out.println(resultSet.getString("remark"));

        DatabaseMetaData dbmd=simplePool.getConnection().getMetaData();
        if(dbmd.supportsResultSetType(ResultSet.TYPE_FORWARD_ONLY)){
            System.out.println("*******MySQL支持TYPE_FORWARD_ONLY结果集**********");
        }else{
            System.out.println("*******MySQL不支持TYPE_FORWARD_ONLY结果集**********");
        }
    }

}
