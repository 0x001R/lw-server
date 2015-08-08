package com.errorgamesstudio.lwserver;

import com.errorgamesstudio.lwserver.db.Datenbank;
import com.errorgamesstudio.lwserver.net.Server;

public class Main
{

	public static void main(String[] args)
	{
		Datenbank.start();
		Server server = new Server();
		server.serverStart();		
	}

}
