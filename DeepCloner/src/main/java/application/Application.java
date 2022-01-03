package application;

import java.util.ArrayList;

import cloner.Cloner;

public class Application {
	public static void main(String[] args) throws IllegalAccessException {
		ArrayList<String> favoriteBooks = new ArrayList<>();
		favoriteBooks.add("Book1");
		favoriteBooks.add("Book2");
		Man man = new Man("Name", 50, favoriteBooks);
		Man clonedMan = new Cloner().deepClone(man);
	}
}
