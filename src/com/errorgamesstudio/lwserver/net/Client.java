package com.errorgamesstudio.lwserver.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import com.errorgamesstudio.lwserver.db.Datenbank;
import com.errorgamesstudio.lwserver.lw.User;
import com.google.gson.Gson;

public class Client implements Runnable
{
	Socket socket;
	InputStream inputStream;
	OutputStream outputStream;
	String placeholder = "%&§";
	String username;
	Thread clientThread = new Thread(this);
	
	public Client(Socket socket)
	{
		this.socket = socket;
		try
		{
			inputStream = this.socket.getInputStream();
			outputStream = this.socket.getOutputStream();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		clientThread.start();
	}
	
	public void run()
	{
		boolean isActive = true;
		BufferedReader inReader = null;
		try 
		{
			inReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
		} 
		catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		PrintWriter outWriter = new PrintWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
		while(isActive)
		{
			String input = "";
			try
			{
				input = inReader.readLine();
			} 
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(input != null)
			{
				System.out.println("Empfangen: " + input);
				String parameter = input.substring(0, 19);
				parameter = parameter.trim();
				 switch(parameter)
				 {
				 	case "LOGIN":
				 	{
				 		String username = input.substring(20,39);
				 		username = username.trim();
				 		String password = input.substring(40,59);
				 		password = password.trim();
				 		System.out.println("Username=" + username);
				 		System.out.println("Password=" + password);
				 		User user = Datenbank.login(username, password);
				 		System.out.println("UserID=" + user.getUserID());
				 		if(user.getUserID() != -1)
				 		{
				 			System.out.println(username + " logged in");
				 			System.out.println(username + "    " + user.getSessionID());
				 			Gson gson = new Gson();
				 			outWriter.println(gson.toJson(user));
				 			//outWriter.println(addSpaces(addSpaces("LOGGEDIN") + addSpaces(user.getUserID() + "") + user.getSessionID()));
				 		}
				 		else
				 		{
				 			System.out.println("Fehler bei der Anmeldung");
				 			outWriter.println(addSpaces("ERROR") + addSpaces("21"));
				 		}
				 		outWriter.flush();
				 		break;
				 	}	 	
				 	
				 	case "POST": // parameter[1] == Witz 
				 	{
				 		String witz = input.substring(20);
				 		witz.replace((char)255, (char)39);
				 		witz = witz.trim();
				 		Datenbank.postNewJoke(username, witz);
				 		break;
				 	}
				 	case "DOWNLOAD": //parameter[1] == category; parameter[2] == parameter(Top/Beliebte etc); parameter[3] == first Joke; parameter[4] == last Joke
				 	{
				 		String category = input.substring(20, 39);
				 		category = category.trim();
				 		String auswahl = input.substring(40, 59);
				 		auswahl = auswahl.trim();
				 		String firstJoke = auswahl.substring(60, 79);
				 		firstJoke = firstJoke.trim();
				 		String lastJoke = input.substring(80, 99);
				 		lastJoke = lastJoke.trim();
				 		Datenbank.getJokes(category, auswahl, Integer.parseInt(firstJoke), Integer.parseInt(lastJoke));
				 		break;
				 	}
				 	case "COMMENT": // parameter[1] == jokeID; username; parameter[2] == commentText
				 	{
				 		String jokeID = input.substring(20, 39);
				 		jokeID = jokeID.trim();
				 		String commentText = input.substring(40, 59);
				 		commentText = commentText.trim();
				 		Datenbank.postComment(Integer.parseInt(jokeID), username, commentText);
				 		break;
				 	}
				 	case "REGISTER":
				 	{
				 		String newUsername = input.substring(20,39);
				 		newUsername = newUsername.trim();
				 		String password = input.substring(40, 59);
				 		password = password.trim();
				 		System.out.println("REGISTRIEREN");
				 		Datenbank.register(newUsername, password);
				 		break;
				 	}
				 	case "REQUEST_CATEGRORIES":
				 	{
				 		new Thread(new Runnable() {
							
							@Override
							public void run() {
								
								Gson gson = new Gson();
								
								outWriter.println(gson.toJson(Datenbank.getCategories()));
					 			outWriter.flush();
							}
						}).start();
				 		break;
				 	}
				 	case "LOAD_JOKES":
				 	{
				 		String sCategoryID = input.substring(20,39).trim();
				 		int categoryID = Integer.valueOf(sCategoryID);
				 		
				 		String order = input.substring(40, 59);
				 		
				 		String sCurrentAmount = input.substring(60, 79).trim();
				 		int currentAmount = Integer.valueOf(sCurrentAmount);
				 		
				 		String sessionID = input.substring(80).trim();
				 		
				 		new Thread(new Runnable() {
							
							@Override
							public void run() {
								String jokes = Datenbank.loadJokes(categoryID, order, currentAmount, sessionID);
								
								if(jokes != null)
								{
									outWriter.println(jokes);
						 			outWriter.flush();
								}
							}
						}).start();
				 		break;
				 	}
				 	case "VOTE":
				 	{
				 		int jokeID = Integer.valueOf(input.substring(20,39).trim());
				 		String sNewVote = input.substring(40, 59).trim();
				 		boolean newVote = sNewVote.equals("true");
				 		String sessionID = input.substring(60);
				 		
				 		Gson gson = new Gson();
						
						outWriter.println(gson.toJson(Datenbank.rateJoke(sessionID, jokeID, newVote)));
			 			outWriter.flush();

				 		
				 		break;
				 	}
				 	case "UNVOTE":
				 	{
				 		int jokeID = Integer.valueOf(input.substring(20,39).trim());
				 		int userID = Integer.valueOf(input.substring(40,59).trim());
				 		
				 		Datenbank.unrateJoke(userID, jokeID);
				 		
				 		break;
				 	}
				 	case "FAVORIT":
				 	{
				 		int jokeID = Integer.valueOf(input.substring(20,39).trim());
				 		int userID = Integer.valueOf(input.substring(40,59).trim());
				 		
				 		Datenbank.favoritJoke(jokeID, userID);
				 		
				 		break;
				 	}
				 	case "UNFAVORIT":
				 	{
				 		int jokeID = Integer.valueOf(input.substring(20,39).trim());
				 		int userID = Integer.valueOf(input.substring(40,59).trim());
				 		
				 		Datenbank.unfavoritJoke(jokeID, userID);
				 		
				 		break;
				 	}
				 	/*
				 	}
				 	}
				 	case "REPORT":
				 	{
				 		Datenbank.report(username, parameter[1]);
				 		break;
				 	}
				 	case "DELETE_JOKE":
				 	{
				 		Datenbank.deleteJoke(username, Integer.parseInt(parameter[1]));
				 		break;
				 	}
				 	case "DELETE_COMMENT":
				 	{
				 		Datenbank.deleteComment(username, Integer.parseInt(parameter[1]));
				 		break;
				 	}
				 	case "RATE_JOKE":
				 	{
				 		Datenbank.rateJoke(username, Integer.parseInt(parameter[1]));
				 		break;
				 	}
				 	case "RATE_COMMENT":
				 	{
				 		Datenbank.rateComment(username, Integer.parseInt(parameter[1]), parameter[2]);
				 		break;
				 	}
				 	case "ABO":
				 	{
				 		Datenbank.abo(username, parameter[1]);
				 		break;
				 	}
				 	case "DEABO":
				 	{
				 		Datenbank.deabo(username, parameter[1]);
				 		break;
				 	}
				 	case "PROFIL":
				 	{
				 		
				 		break;
				 	}
				 	case "SEARCH":
				 	{
				 		
				 		break;
				 	}
				 	case "LOAD_COMMENT":
				 	{
				 		
				 		break;
				 	}
				 	case "CATEGORIES":
				 	{
				 		
				 		break;
				 	}*/
				 }
			}
			else
			{
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	private String addSpaces(String input)
    {
		if(input == null)
		{
			input = "  ";
		}
        for(int i = input.length();i < 20; i++)
        {
            input += " ";
        }
        return input;
    }
}
