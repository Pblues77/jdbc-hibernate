package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import database.JDBCUtil;
import model.Book;

public class BookDAO implements DAOInterface<Book> {
	private static final BookDAO instance = new BookDAO();;

	public static final BookDAO getInstance() {
		return instance;
	}

	@Override
	public int insert(Book t) {
		int rowsCheck = 0;
		try {
			// B1:Ket noi
			Connection conn = JDBCUtil.getConnection();
			// B2: Statement
			String sql = "INSERT INTO Book(id, name, price, publishingYear)" + "VALUES(?,?,?,?);";
			PreparedStatement st = conn.prepareStatement(sql);

			st.setString(1, t.getId());
			st.setString(2, t.getName());
			st.setDouble(3, t.getPrice());
			st.setInt(4, t.getPublishingYear());
			// B3:
			rowsCheck = st.executeUpdate(sql);
			// B4:
			System.out.println("Query: " + sql);
			System.out.println("Row updated: " + rowsCheck);
			// B5: Ngat ket noi
			JDBCUtil.closeConnection(conn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rowsCheck;
	}

	@Override
	public int update(Book t) {
		int rowsCheck = 0;
		try {
			// B1:Ket noi
			Connection conn = JDBCUtil.getConnection();
			// B2: Statement
			String sql = "UPDATE Book" + " SET" + " name='?'" + ",price=?,publishingYear=? WHERE id='?';";
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, t.getName());
			st.setDouble(2, t.getPrice());
			st.setInt(3, t.getPublishingYear());
			st.setString(4, t.getId());
			// B3:
			rowsCheck = st.executeUpdate(sql);
			// B4:
			System.out.println("Query: " + sql);
			System.out.println("Row updated: " + rowsCheck);
			// B5: Ngat ket noi
			JDBCUtil.closeConnection(conn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rowsCheck;
	}

	@Override
	public int delete(Book t) {
		int rowsCheck = 0;
		try {
			// B1:Ket noi
			Connection conn = JDBCUtil.getConnection();
			// B2: Statement
			String sql = "DELETE FROM Book" + " WHERE id='?';";
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, t.getId());
			// B3:
			rowsCheck = st.executeUpdate(sql);
			// B4:
			System.out.println("Query: " + sql);
			System.out.println("Row updated: " + rowsCheck);
			// B5: Ngat ket noi
			JDBCUtil.closeConnection(conn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rowsCheck;
	}

	@Override
	public ArrayList<Book> selectAll() {
		ArrayList<Book> re = new ArrayList<Book>();
		try {
			// B1:Ket noi
			Connection conn = JDBCUtil.getConnection();
			// B2: Statement
			Statement st = conn.createStatement();
			// B3: query
			String sql = "SELECT * FROM Book";
			ResultSet rs = st.executeQuery(sql);
			// B4:
			System.out.println("Query: " + sql);
			while (rs.next()) {
				String id = rs.getString("id");
				String name = rs.getString("name");
				Double price = rs.getDouble("price");
				int publishingYear = rs.getInt("publishingYear");
				Book book = new Book(id, name, price, publishingYear);
				re.add(book);
			}
			// B5: Ngat ket noi
			JDBCUtil.closeConnection(conn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return re;
	}

	/*
	 * Nếu condition là 1=1 ? => chuyển sang dùng PreparedStatement
	 */
	@Override
	public Book selectById(Book t) {
		Book re = null;
		try {
			// B1:Ket noi
			Connection conn = JDBCUtil.getConnection();
			// B2: Statement
			String sql = "SELECT * FROM Book" + " WHERE id='?'";
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, t.getId());
			// B3:
			ResultSet rs = st.executeQuery(sql);
			// B4:
			System.out.println("Query: " + sql);
			while (rs.next()) {
				String id = rs.getString("id");
				String name = rs.getString("name");
				Double price = rs.getDouble("price");
				int publishingYear = rs.getInt("publishingYear");
				re = new Book(id, name, price, publishingYear);

			}
			// B5: Ngat ket noi
			JDBCUtil.closeConnection(conn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return re;
	}

	@Override
	public ArrayList<Book> selectByCondition(String condition) {
		ArrayList<Book> re = new ArrayList<Book>();
		try {
			// B1:Ket noi
			Connection conn = JDBCUtil.getConnection();
			// B2: Statement
			Statement st = conn.createStatement();
			// B3: query
			String sql = "SELECT * FROM Book WHERE " + condition;
			ResultSet rs = st.executeQuery(sql);
			// B4:
			System.out.println("Query: " + sql);
			while (rs.next()) {
				String id = rs.getString("id");
				String name = rs.getString("name");
				Double price = rs.getDouble("price");
				int publishingYear = rs.getInt("publishingYear");
				Book book = new Book(id, name, price, publishingYear);
				re.add(book);
			}
			// B5: Ngat ket noi
			JDBCUtil.closeConnection(conn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return re;
	}

}
