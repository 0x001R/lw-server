package com.errorgamesstudio.lwserver.lw;

import java.io.Serializable;

public class User implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8834109452659776861L;
	private int userID;
	private String sessionID;
	private String username;
	
	
	public User(int userID, String sessionID, String username)
	{
		this.setUserID(userID);
		this.setSessionID(sessionID);
		this.setUsername(username);
	}


	/**
	 * @return the userID
	 */
	public int getUserID() {
		return userID;
	}


	/**
	 * @param userID the userID to set
	 */
	public void setUserID(int userID) {
		this.userID = userID;
	}


	/**
	 * @return the sessionID
	 */
	public String getSessionID() {
		return sessionID;
	}


	/**
	 * @param sessionID the sessionID to set
	 */
	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}


	/**
	 * @return the name
	 */
	public String getUsername() {
		return username;
	}


	/**
	 * @param name the name to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
}
