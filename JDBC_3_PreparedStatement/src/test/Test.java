package test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import dao.BookDAO;
import dao.DAOInterface;
import database.JDBCUtil;
import model.Book;

public class Test {
	/*
	 * ubuntu: sudo mysql -u root -p
	 create database jdbc_2;
	 use jdbc_2;
	 create table Book(
	 	id varchar(20) NOT NULL,
	 	name varchar(50),
	 	price double(10, 3),
	 	publishingYear int,
    	PRIMARY KEY (id)
	 );
	 */
	public static void main(String[] args) {
		DAOInterface<Book> bookDAO = BookDAO.getInstance();
		//insert
		bookDAO.insert(new Book("1", "A", 200000.0, 2003));
		bookDAO.insert(new Book("2", "B", 300000.0, 2010));
		bookDAO.insert(new Book("3", "C", 150000.0, 2015));
		//update
		//delete
		//selectAll
		System.out.println(bookDAO.selectAll());
	}
}
