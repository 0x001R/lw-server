package com.errorgamesstudio.lwserver.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;

import com.errorgamesstudio.lwserver.lw.Joke;
import com.mysql.jdbc.ResultSet;
//import com.mysql.jdbc.Statement;


public class Datenbank
{
	static Connection connection = null;
	
	public static void start()
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			connection = DriverManager.getConnection("jdbc:mysql://localhost/lw-datenbank?user=server&password=1234");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	public static ArrayList<String> getCategories()
	{
		try
		{
			Statement statement = connection.createStatement();
			java.sql.ResultSet result = statement.executeQuery("SELECT CategoryName FROM categories");
			ArrayList<String> catName = new ArrayList<String>();
			while(result.next())
			{
				catName.add(result.getString(1));
			}
			return catName;
		}
		catch(Exception e)
		{
			
		}
		return null;
		
	}
	
	public static ArrayList<Joke> loadJokes(String category, int categoryTyp, int first, int amount)
	{
		try
		{
			Statement statement = connection.createStatement();
			java.sql.ResultSet result;
			categoryTyp = 0;
			if(categoryTyp == 0)
			{
				result = statement.executeQuery("select temp.jokeID, temp.userID, temp.categoryID, temp.jokeText, temp.jokesVotes, temp.jokeHype, temp.jokeDate, user.Username from (select * from jokes inner join categories on jokes.categoryID = categories.idCategories where CategoryName = 'Einzeiler' AND row_count() <= 20 AND row_count() >= 0 order by jokeHype) AS temp inner join user on temp.userID = user.idUser;");
			}
			else if(categoryTyp == 1)
			{
				result = statement.executeQuery("select temp.jokeID, temp.userID, temp.categoryID, temp.jokeText, temp.jokesVotes, temp.jokeHype, temp.jokeDate, user.Username from (select * from jokes inner join categories on jokes.categoryID = categories.idCategories where CategoryName = 'Einzeiler' AND row_count() <= " + (first + amount) +" AND row_count() >= " + first + " order by jokeHype) AS temp inner join user on temp.userID = user.idUser;");
			}
			else
			{
				result = statement.executeQuery("select temp.jokeID, temp.userID, temp.categoryID, temp.jokeText, temp.jokesVotes, temp.jokeHype, temp.jokeDate, user.Username from (select * from jokes inner join categories on jokes.categoryID = categories.idCategories where CategoryName = 'Einzeiler' AND row_count() <= " + (first + amount) +" AND row_count() >= " + first + " order by jokeHype) AS temp inner join user on temp.userID = user.idUser;");
			}
			
			result = statement.executeQuery("select * from jokes");
			
			ArrayList<Joke> jokes = new ArrayList<Joke>();
			while(result.next())
			{
				Joke temp = new Joke();
				
				temp.jokeId = result.getInt(1);	
				temp.jokeText = result.getString(4);
				temp.votes = result.getInt(5);
				temp.hype = result.getInt(6);
				temp.date = result.getDate(7);
				//temp.username = result.getString(8);
				temp.category = category;
				temp.categoryType = categoryTyp;
				
				jokes.add(temp);
				
				System.out.println("Es gibt Witze");
			}
			System.out.println("oder doch nicht");
			return jokes;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static boolean postNewJoke(String user, String jokeText)
	{
		System.out.println(jokeText);
		return false;
	}
	
	public static void getJoke(int jokeID)
	{
		
	}
	
	public static boolean register(String newUsername, String password)
	{
		java.sql.Statement statement;
		int result;
		
		try
		{
			statement = connection.createStatement();
			result = statement.executeUpdate("INSERT INTO user (Username, Userpassword) VALUES ('" + newUsername + "','" + password +"');");
			return true;
		}
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
		return false;
	}
	
	public static boolean login(String username, String password)
	{
		java.sql.Statement statment;
		try
		{
			statment = connection.createStatement();
			java.sql.ResultSet result = statment.executeQuery("SELECT * FROM user WHERE Username = '"+ username + "' AND Userpassword = '" + password + "'");
			return  result.next();
			
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	public static void getJokes(String cat, String parameter, int first, int last)
	{
		
	}
	
	public static void postComment(int jokeID, String username, String commentText)
	{
		
	}
	
	public static void report(String vonUsername, String fuerUsername)
	{
		
	}
	
	public static void deleteJoke(String username, int jokeID)
	{
		
	}
	
	public static void deleteComment(String username, int commentID)
	{
		
	}
	
	public static void rateJoke(String username, int jokeID)
	{
		
	}
	
	public static void rateComment(String username, int commentID, String rating)
	{
		
	}
	
	public static void abo(String username, String aboName)
	{
		
	}
	
	public static void deabo(String username, String deaboName)
	{
		
	}
	
}
