package com.errorgamesstudio.lwserver.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.UUID;

import com.errorgamesstudio.lwserver.lw.Joke;
import com.errorgamesstudio.lwserver.lw.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Datenbank
{
	static Connection connection = null;
	
	public static void start()
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			connection = DriverManager.getConnection("jdbc:mysql://localhost/lw-datenbank?user=lwserver&password=1234");
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
	
	public static String loadJokes(String category, int categoryTyp, int first, int amount, int userID)
	{
		try
		{
			Statement statement = connection.createStatement();
			java.sql.ResultSet result;
			if(categoryTyp == 0)
			{
				result = statement.executeQuery("select temp4.jokeID, temp4.userID, categoryID, jokeText, jokesVotes, jokeHype, jokeDate, Username, voted, favorit from (select temp2.jokeID, temp2.userID, categoryID, jokeText, jokesVotes, jokeHype, jokeDate, Username, voted from (select temp.jokeID, temp.userID, temp.categoryID, temp.jokeText, temp.jokesVotes, temp.jokeHype, temp.jokeDate, user.Username from (select * from jokes inner join categories on jokes.categoryID = categories.idCategories where CategoryName = '" + category + "') AS temp inner join user on temp.userID = user.idUser order by jokeHype desc LIMIT " + first + ","+ amount +") AS temp2 left join (select * from jokevotes where userID = 5) as temp3 on temp3.jokeID = temp2.jokeID) AS temp4 left join (select * from favorites where userID = " + userID + ") AS temp5 on temp4.jokeID = temp5.jokeID;");
			}
			else if(categoryTyp == 1)
			{
				result = statement.executeQuery("select temp4.jokeID, temp4.userID, categoryID, jokeText, jokesVotes, jokeHype, jokeDate, Username, voted, favorit from (select temp2.jokeID, temp2.userID, categoryID, jokeText, jokesVotes, jokeHype, jokeDate, Username, voted from (select temp.jokeID, temp.userID, temp.categoryID, temp.jokeText, temp.jokesVotes, temp.jokeHype, temp.jokeDate, user.Username from (select * from jokes inner join categories on jokes.categoryID = categories.idCategories where CategoryName = '" + category + "') AS temp inner join user on temp.userID = user.idUser order by jokesVotes desc LIMIT " + first + ","+ amount +") AS temp2 left join (select * from jokevotes where userID = 5) as temp3 on temp3.jokeID = temp2.jokeID) AS temp4 left join (select * from favorites where userID = " + userID + ") AS temp5 on temp4.jokeID = temp5.jokeID;");
			}
			else
			{
				result = statement.executeQuery("select temp4.jokeID, temp4.userID, categoryID, jokeText, jokesVotes, jokeHype, jokeDate, Username, voted, favorit from (select temp2.jokeID, temp2.userID, categoryID, jokeText, jokesVotes, jokeHype, jokeDate, Username, voted from (select temp.jokeID, temp.userID, temp.categoryID, temp.jokeText, temp.jokesVotes, temp.jokeHype, temp.jokeDate, user.Username from (select * from jokes inner join categories on jokes.categoryID = categories.idCategories where CategoryName = '" + category + "') AS temp inner join user on temp.userID = user.idUser order by jokeDate desc LIMIT " + first + ","+ amount +") AS temp2 left join (select * from jokevotes where userID = 5) as temp3 on temp3.jokeID = temp2.jokeID) AS temp4 left join (select * from favorites where userID = " + userID + ") AS temp5 on temp4.jokeID = temp5.jokeID;");
			}
			ArrayList<Joke> jokes = new ArrayList<Joke>();
			while(result.next())
			{
				Joke temp = new Joke();
				
				temp.jokeId = result.getInt(1);	
				temp.jokeText = result.getString(4);
				temp.votes = result.getInt(5);
				temp.hype = result.getInt(6);
				temp.date = result.getDate(7);
				temp.username = result.getString(8);
				temp.category = category;
				temp.categoryType = categoryTyp;
				temp.voted = result.getBoolean(9);
				temp.favorit = result.getBoolean(10);
				
				Statement voteStatement = connection.createStatement();
				java.sql.ResultSet voteResult = voteStatement.executeQuery("SELECT count(userID) AS numberofvotes FROM jokevotes where jokeID = " + temp.jokeId + " AND voted = 1;");
				if(voteResult.next())
				{
					temp.votes = voteResult.getInt(1);
				}
				else
				{
					temp.votes = 0;
				}
				
				jokes.add(temp);
				
				
				System.out.println("Es gibt Witze");
			}
			System.out.println("oder doch nicht");
			Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
			return gson.toJson(jokes.toArray());
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
	
	public static User login(String username, String password)
	{
		java.sql.Statement statment;
		try
		{
			statment = connection.createStatement();
			java.sql.ResultSet result = statment.executeQuery("SELECT * FROM user WHERE Username = '"+ username + "' AND Userpassword = '" + password + "'");
			String sessionID = generateSessionId();
			
			if(result.next())
			{
				User user = new User(result.getInt(1), sessionID, username);
				statment.execute("UPDATE user SET sessionID ='" + user.getSessionID() + "' WHERE idUser=" + user.getUserID());
				return user;
			}
			else
			{
				return new User(-1, null, null);
			}
			
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return new User(-1, null, null);
	}
	
	public static String generateSessionId()
	{
		UUID uuid = UUID.randomUUID();
		try{
			java.sql.Statement statment = connection.createStatement();
			java.sql.ResultSet result = statment.executeQuery("SELECT sessionID FROM user WHERE sessionID = '" + uuid.toString() + "'");
			if(result.next())
			{
				return generateSessionId();
			}
			else
			{
				return uuid.toString();
			}
		}
		catch(Exception e){
			
		}
		
		return "";
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
	
	public static void rateJoke(int userID, int jokeID)
	{
		java.sql.Statement statement;
		boolean result;
		
		try
		{
			statement = connection.createStatement();
			result = statement.execute("INSERT INTO jokevotes (jokeID, userID) VALUES ('" + jokeID + "','" + userID +"');");
		}
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void unrateJoke(int userID, int jokeID)
	{
		java.sql.Statement statement;
		boolean result;
		
		try
		{
			statement = connection.createStatement();
			result = statement.execute("DELETE FROM jokevotes where userID = " + userID + " AND jokeID = " + jokeID);
		}
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void favoritJoke(int jokeID, int userID)
	{
		java.sql.Statement statement;
		boolean result;
		
		try
		{
			statement = connection.createStatement();
			result = statement.execute("INSERT INTO favorites (jokeID, userID) VALUES ('" + jokeID + "','" + userID +"');");
		}
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void unfavoritJoke(int jokeID, int userID)
	{
		java.sql.Statement statement;
		boolean result;
		
		try
		{
			statement = connection.createStatement();
			result = statement.execute("DELETE FROM favorites where userID = " + userID + " AND jokeID = " + jokeID);
		}
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
