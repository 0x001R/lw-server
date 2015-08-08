package com.errorgamesstudio.lwserver.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

public class Server implements Runnable
{
	boolean isRunning;
	ServerSocket serverSocket;
	ArrayList<Client> clients;
	Thread serverThread;
	
	public Server()
	{
		clients = new ArrayList<Client>();
		
		try
		{
			serverSocket = new ServerSocket(6969);
		} 
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		serverThread = new Thread(this);
	}
	
	
	@Override
	public void run()
	{
		isRunning = true;
		
		while(isRunning)
		{
			isRunning = !serverThread.isInterrupted();
			try
			{
				Socket client = serverSocket.accept();
				clients.add(new Client(client));
				System.out.println("Client connected");
			} 
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		try
		{
			serverSocket.close();
		} 
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void serverStop()
	{
		System.out.println("Server wird gestoppt");
		
		serverThread.interrupt();
	}
	
	@SuppressWarnings("deprecation")
	public void serverKill()
	{
		System.out.println("Server wird gekillt");

		serverThread.destroy();
	}
	
	public void serverStart()
	{
		System.out.println("Startet Server auf Port: " + serverSocket.getLocalPort());
		serverThread.start();
	}

}
