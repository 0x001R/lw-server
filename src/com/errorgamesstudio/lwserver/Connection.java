package com.errorgamesstudio.lwserver;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;

public class Connection
{
	public static void main(String args[])
	{
		new Connection();
	}
	
	InputStream inputStream;
    OutputStream outputStream;
    String placeholder = "%&§";


    public Connection()
    {
        try
        {
            Socket socket = new Socket("192.168.178.22", 6969);
            outputStream = socket.getOutputStream();
            inputStream = socket.getInputStream();

            PrintStream out = new PrintStream(outputStream);
            
            out.print("POST"+ placeholder + "_" + placeholder + "Wetter kaputt, kommt Wasser raus!");
            out.flush();
            socket.close();
        }
        catch(Exception x)
        {

        }
    }
}
