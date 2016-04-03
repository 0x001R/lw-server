package com.errorgamesstudio.lwserver.lw;

import java.io.Serializable;

public class Category implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1221990000635723586L;
	private int categoryID;
	private String categoryName;
	/**
	 * @return the categoryID
	 */
	public int getCategoryID() {
		return categoryID;
	}
	/**
	 * @param categoryID the categoryID to set
	 */
	public void setCategoryID(int categoryID) {
		this.categoryID = categoryID;
	}
	/**
	 * @return the categoryName
	 */
	public String getCategoryName() {
		return categoryName;
	}
	/**
	 * @param categoryName the categoryName to set
	 */
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	
	
}
