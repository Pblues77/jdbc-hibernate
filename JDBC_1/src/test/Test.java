package test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import database.JDBCUtil;

public class Test {
	public static void main(String[] args) {
		// B1: ket noi
		Connection connection = JDBCUtil.getConnection();
		// B2: Xu ly du lieu
		try {
			// Tao doi tuong statement
			Statement st = connection.createStatement();
			// Query
			String q_ins_1 = "INSERT INTO persons(person_id, last_name, first_name, gender, dob, income)"
					+ "VALUES(1,\"Le\", \"Ba Trong\", \"Nam\",\"2003-7-7\",10000000.0);";
			int check = st.executeUpdate(q_ins_1);
			// Check ket qua
			System.out.println(check > 0 ? "updated rows: " + check : "failed");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// B5: Ngat ket noi
		JDBCUtil.closeConnection(connection);
	}
}
