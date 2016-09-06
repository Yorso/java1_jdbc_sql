package com.jorge.client;

import java.util.ArrayList;
import java.util.List;

import com.jorge.pojo.Book;
import com.jorge.pojo.Chapter;
import com.jorge.pojo.Publisher;
import com.jorge.service.BookStoreService;

public class BookStoreClient {

	public static void main(String[] args){
		BookStoreService bookStoreservice = new BookStoreService();

		// Persisting object graph
		System.out.println("Main method: Setting publisher info in Publisher object");
		Publisher publisher = new Publisher("MAN5", "Manning Publication"); // Setting publisher info in Publisher object
		
		System.out.println("Main method: Setting book info in Book object");
		Book book = new Book("28374950984238", "Java persistence with Hibernate, second edition", publisher); // Setting book info in Book object
		
		// Adding chapter info to a chapter list
		List<Chapter> chapters = new ArrayList<Chapter>();
		Chapter chapter1 = new Chapter("Introducing JPA and Hibernate", 1);
		System.out.println("Main method: Adding chapter 1 to chapter list");
		chapters.add(chapter1);
		Chapter chapter2 = new Chapter("Domain Models and Metadata", 2);
		System.out.println("Main method: Adding chapter 2 to chapter list");
		chapters.add(chapter2);
		
		System.out.println("Main method: Setting chapter list to book");
		book.setChapters(chapters); // Setting chapter list to book
		
		System.out.println("Main method: Calling writer method");
		bookStoreservice.persistObjectGraph(book); // Calling writer method to write in DB

		// Retrieving object graph
		/*
		System.out.println("Main method: Getting info from reader method");
		Book book = bookStoreservice.retrieveObjectGraph("28374950984237"); // Getting info from reader method
		System.out.println(book); // Displying result
		*/
	}

}
