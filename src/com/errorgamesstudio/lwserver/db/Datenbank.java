package com.errorgamesstudio.lwserver.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

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
			Statement statment = connection.createStatement();
			java.sql.ResultSet result = statment.executeQuery("SELECT CategoryName FROM categories");
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
