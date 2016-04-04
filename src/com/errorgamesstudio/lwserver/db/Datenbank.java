package com.errorgamesstudio.lwserver.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.UUID;

import com.errorgamesstudio.lwserver.lw.Category;
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
	
	public static Category[] getCategories()
	{
		try
		{
			Statement statement = connection.createStatement();
			java.sql.ResultSet result = statement.executeQuery("SELECT * FROM categories");
			ArrayList<Category> cats = new ArrayList<Category>();
			while(result.next())
			{
				Category cat = new Category();
				cat.setCategoryID(result.getInt(1));
				cat.setCategoryName(result.getString(2));
				cats.add(cat);
			}
			Category[] temp = new Category[cats.size()];
			
			return cats.toArray(temp);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
		
	}
	
	public static String loadJokes(int categoryID, String order, int currentAmount, String sessionID)
	{
		try
		{
			int userID = -1;
			
			// First geht userID from sessionID if sessionID is not null or ""
			if(sessionID != null && !sessionID.equals(""))
			{
				Statement statement = connection.createStatement();
				ResultSet result = statement.executeQuery("select idUser from user where sessionID = '" + sessionID + "'");
				if(result.next())
				{
					userID = result.getInt(1);
				}
				else 
					userID = -1;
			}
			
			
			
			Statement statement = connection.createStatement();
			java.sql.ResultSet result = null;
			if(order.trim().equals("topToday"))
			{
				result = statement.executeQuery("select jokes.*, user.Username from jokes, user where jokes.categoryID = " +categoryID+" order by jokeHype desc limit "+currentAmount+", "+currentAmount+20);
				System.out.println("ORDER: " + order);

				// Deprecated
				//result = statement.executeQuery("select temp4.jokeID, temp4.userID, categoryID, jokeText, jokesVotes, jokeHype, jokeDate, Username, voted, favorit from (select temp2.jokeID, temp2.userID, categoryID, jokeText, jokesVotes, jokeHype, jokeDate, Username, voted from (select temp.jokeID, temp.userID, temp.categoryID, temp.jokeText, temp.jokesVotes, temp.jokeHype, temp.jokeDate, user.Username from (select * from jokes inner join categories on jokes.categoryID = categories.idCategories where CategoryName = '" + category + "') AS temp inner join user on temp.userID = user.idUser order by jokeHype desc LIMIT " + first + ","+ amount +") AS temp2 left join (select * from jokevotes where userID = 5) as temp3 on temp3.jokeID = temp2.jokeID) AS temp4 left join (select * from favorites where userID = " + userID + ") AS temp5 on temp4.jokeID = temp5.jokeID;");
			}
			else if(order.trim().equals("topAllTime"))
			{
				result = statement.executeQuery("select jokes.*, user.Username from jokes, user where jokes.categoryID = " +categoryID+" order by jokesvotes desc limit "+currentAmount+", "+currentAmount+20);
				
				//Deprecated
				//result = statement.executeQuery("select temp4.jokeID, temp4.userID, categoryID, jokeText, jokesVotes, jokeHype, jokeDate, Username, voted, favorit from (select temp2.jokeID, temp2.userID, categoryID, jokeText, jokesVotes, jokeHype, jokeDate, Username, voted from (select temp.jokeID, temp.userID, temp.categoryID, temp.jokeText, temp.jokesVotes, temp.jokeHype, temp.jokeDate, user.Username from (select * from jokes inner join categories on jokes.categoryID = categories.idCategories where CategoryName = '" + category + "') AS temp inner join user on temp.userID = user.idUser order by jokesVotes desc LIMIT " + first + ","+ amount +") AS temp2 left join (select * from jokevotes where userID = 5) as temp3 on temp3.jokeID = temp2.jokeID) AS temp4 left join (select * from favorites where userID = " + userID + ") AS temp5 on temp4.jokeID = temp5.jokeID;");
			}
			else if(order.trim().equals("newest"))
			{
				result = statement.executeQuery("select jokes.*, user.Username from jokes, user where jokes.categoryID = " +categoryID+" order by jokeDate desc limit "+currentAmount+", "+currentAmount+20);
				
				//Deprecated
				//result = statement.executeQuery("select temp4.jokeID, temp4.userID, categoryID, jokeText, jokesVotes, jokeHype, jokeDate, Username, voted, favorit from (select temp2.jokeID, temp2.userID, categoryID, jokeText, jokesVotes, jokeHype, jokeDate, Username, voted from (select temp.jokeID, temp.userID, temp.categoryID, temp.jokeText, temp.jokesVotes, temp.jokeHype, temp.jokeDate, user.Username from (select * from jokes inner join categories on jokes.categoryID = categories.idCategories where CategoryName = '" + category + "') AS temp inner join user on temp.userID = user.idUser order by jokeDate desc LIMIT " + first + ","+ amount +") AS temp2 left join (select * from jokevotes where userID = 5) as temp3 on temp3.jokeID = temp2.jokeID) AS temp4 left join (select * from favorites where userID = " + userID + ") AS temp5 on temp4.jokeID = temp5.jokeID;");
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
				// Not clean work. Hope I dont need next two somewhere else
				temp.category = "";
				temp.categoryType = -1;
				//Shit above
				
				//Workaround incoming
				Statement votedStatement = connection.createStatement();
				java.sql.ResultSet votedResult = votedStatement.executeQuery("SELECT voted from jokevotes where jokeID ="+temp.getJokeId() + " and userID ="+ userID);
				if(votedResult.next())
				{
					temp.voted = votedResult.getBoolean(1);
				}
				else
				{
					temp.voted = false;
				}
				
				Statement favStatement = connection.createStatement();
				java.sql.ResultSet favResult = votedStatement.executeQuery("SELECT favorit from favorites where jokeID ="+temp.getJokeId() + " and userID ="+ userID);
				if(favResult.next())
				{
					temp.favorit = favResult.getBoolean(1);
				}
				else
				{
					temp.favorit = false;
				}

				
								
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
			}
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
	
	public static boolean rateJoke(String sessionID, int jokeID, boolean newVote)
	{
		// First get userID from sessionID
		int userID = getUserIDFromSessionID(sessionID);
		if(userID == -1)
			return false;
		
		
		java.sql.Statement statement;
		boolean result = false;
		String voted = "0";
		if(newVote)
		{
			voted = "1";
		}
		
		try
		{
			statement = connection.createStatement();
			result = statement.execute("INSERT INTO jokevotes (jokeID, userID, voted) VALUES ('" + jokeID + "','" + userID +"'," + voted + ");");
		}
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			
			
			try
			{
				statement = connection.createStatement();
				result = statement.execute("update jokevotes set voted =" + voted + ", date=curdate() where jokeID = " + jokeID + " and userID = " + userID);
				return true;			
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
				return false;
			}
		}
		return true;
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

	private static int getUserIDFromSessionID(String sessionID)
	{
		try
		{
			int userID = -1;
			
			// First geht userID from sessionID if sessionID is not null or ""
			if(sessionID != null && !sessionID.equals(""))
			{
				Statement statement = connection.createStatement();
				ResultSet result = statement.executeQuery("select idUser from user where sessionID = '" + sessionID + "'");
				if(result.next())
				{
					userID = result.getInt(1);
				}
				else 
					userID = -1;
			}
			return userID;
		}
		catch(Exception e)
		{
		
			e.printStackTrace();
		}
		return -1;
	}
}
