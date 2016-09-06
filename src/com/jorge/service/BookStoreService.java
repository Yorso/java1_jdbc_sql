package com.jorge.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.jorge.pojo.Book;
import com.jorge.pojo.Chapter;
import com.jorge.pojo.Publisher;

public class BookStoreService {

	private Connection connection = null;
	
	// Writing info to DB
	public void persistObjectGraph(Book book){
		try{
			// Connecting to DB
			System.out.println("persistObjectGraph method: connecting to DB");
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bookstore", "user1", "user1pass");
			
			// Updating publisher
			System.out.println("persistObjectGraph method: updating publisher");
			PreparedStatement stmt = connection.prepareStatement("INSERT INTO publisher (code, publisher_name) VALUES (?,?)");
			
			stmt.setString(1, book.getPublisher().getCode());
			stmt.setString(2, book.getPublisher().getName());
			stmt.executeUpdate();
			
			System.out.println("persistObjectGraph method: closing publisher prepared statement");
			stmt.close();
			
			// Updating book
			System.out.println("persistObjectGraph method: updating book");
			stmt = connection.prepareStatement("INSERT INTO book (isbn, book_name, publisher_code) VALUES (?,?,?)");
			
			stmt.setString(1, book.getIsbn());
			stmt.setString(2, book.getName());
			stmt.setString(3, book.getPublisher().getCode());
			stmt.executeUpdate();
			
			System.out.println("persistObjectGraph method: closing book prepared statement");
			stmt.close();
			
			// Updating chapters
			System.out.println("persistObjectGraph method: updating chapters");
			stmt = connection.prepareStatement("INSERT INTO chapter (book_isbn, chapter_num, title) VALUES (?,?,?)");
			
			for(Chapter chapter : book.getChapters()){
				stmt.setString(1, book.getIsbn());
				stmt.setInt(2, chapter.getChapterNumber());
				stmt.setString(3, chapter.getTitle());
				stmt.executeUpdate();
			}
			
			System.out.println("persistObjectGraph method: closing chapter prepared statement");
			stmt.close();
		}
		catch(ClassNotFoundException e){
			System.out.println("persistObjectGraph method: ERROR: " + e.getMessage());
			e.printStackTrace();
		}
		catch(SQLException e){
			System.out.println("persistObjectGraph method: ERROR: " + e.getMessage());
			e.printStackTrace();
		}
		finally{
			try{
				System.out.println("persistObjectGraph method: closing connection");
				connection.close();
			}
			catch(SQLException e){
				System.out.println("persistObjectGraph method: ERROR: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}
	
	// Getting info from DB 
	public Book retrieveObjectGraph(String isbn){
		Book book = null;
		
		try{
			// Connecting to DB
			System.out.println("retrieveObjectGraph method: connecting to DB");
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bookstore", "user1", "user1pass");
			
			// Getting book and publisher info
			System.out.println("retrieveObjectGraph method: getting book and publisher info");
			PreparedStatement stmt = connection.prepareStatement("SELECT * FROM book, publisher WHERE book.publisher_code = publisher.code AND book.isbn = ?");
			stmt.setString(1, isbn);
			
			ResultSet rs = stmt.executeQuery();
			
			book = new Book();
			
			if(rs.next()){
				book.setIsbn(isbn);
				book.setName(rs.getString("book_name"));
				
				Publisher publisher = new Publisher();
				publisher.setCode(rs.getString("code"));
				publisher.setName(rs.getString("publisher_name"));
				
				book.setPublisher(publisher); // Setting publisher to book
			}
			
			System.out.println("retrieveObjectGraph method: closing book/publisher resultset");
			rs.close();
			System.out.println("retrieveObjectGraph method: closing book/publisher prepared statement");
			stmt.close();
			
			// Getting chapter info
			System.out.println("retrieveObjectGraph method: getting chapter info");
			List<Chapter> chapters = new ArrayList<Chapter>();
			stmt = connection.prepareStatement("SELECT * FROM chapter WHERE book_isbn = ?");
			stmt.setString(1, isbn);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				Chapter chapter = new Chapter();
				chapter.setTitle(rs.getString("title"));
				chapter.setChapterNumber(rs.getInt("chapter_num"));
				chapters.add(chapter); // Adding chapter to chapter list
			}
			book.setChapters(chapters); // Setting chapter list to book
			
			System.out.println("retrieveObjectGraph method: closing chapter resultset");
			rs.close();
			System.out.println("retrieveObjectGraph method: closing chapter prepared statement");
			stmt.close();
		}
		catch(ClassNotFoundException e){
			System.out.println("retrieveObjectGraph method: ERROR: " + e.getMessage());
			e.printStackTrace();
		}
		catch(SQLException e){
			System.out.println("retrieveObjectGraph method: ERROR: " + e.getMessage());
			e.printStackTrace();
		}
		finally{
			try{
				System.out.println("retrieveObjectGraph method: closing connection");
				connection.close();
			}
			catch(SQLException e){
				System.out.println("retrieveObjectGraph method: ERROR: " + e.getMessage());
				e.printStackTrace();
			}
		}
		
		System.out.println("retrieveObjectGraph method: return book object");
		return book;
	}

}
