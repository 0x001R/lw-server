package com.errorgamesstudio.lwserver.lw;

import java.io.Serializable;

public class User implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8834109452659776861L;
	private int userID;
	private String sessionID;
	private String name;
	
	
	public User(int userID, String sessionID, String name)
	{
		this.setUserID(userID);
		this.setSessionID(sessionID);
		this.setName(name);
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
	public String getName() {
		return name;
	}


	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
}
