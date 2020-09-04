package jdbc;

import java.sql.*;

/**
 * @author cheney
 * @date 2019-07-31
 */
public class Main {

    public static final String URL = "jdbc:mysql://localhost:3306/lexue?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=GMT%2B8";
    public static final String USER = "root";
    public static final String PASSWORD = "cheney@mysql";

    public static void main(String[] args) throws Exception {
        //1.加载驱动程序
        Class.forName("com.mysql.cj.jdbc.Driver");
        //2. 获得数据库连接
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select * from t_hl_grade_cost");
        ResultSetMetaData metaData = rs.getMetaData();
        int columnIndex = 1;
        System.out.println(metaData.getColumnClassName(columnIndex));
        System.out.println(metaData.getColumnType(columnIndex));
        System.out.println(JDBCType.valueOf(metaData.getColumnType(columnIndex)));
    }

}
