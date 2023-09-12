package database;

import java.sql.*;

/*
 * Mysql on linux:
 * sudo systemctl enable/disable/status/start/stop mysql
 * sudo service mysql enable/disable/status/start/stop mysql
 */
public class JDBCUtil {
	public static Connection getConnection() {
		Connection conn = null;

		try {
			// Register MySQL Driver with DriverManager
			DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());

			String url = "jdbc:mysql://localhost:3306/jdbc_2";
			String username = "root";
			String password = "chelseafc";

			// Tạo kết nối
			conn = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	}

	public static void closeConnection(Connection conn) {
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void printInfo(Connection conn) {
		try {
			if (conn == null)
				System.out.println("Connection is null");
			else if(conn.isClosed()) {
				System.out.println("Connection is closed");
			}else {
				DatabaseMetaData metadata = conn.getMetaData();
				System.out.println(metadata.toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}