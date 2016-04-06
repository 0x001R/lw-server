package com.errorgamesstudio.lwserver.lw;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;

public class Joke  implements Serializable {

    public int jokeId;
    public String jokeText;
    public String username;
    public int votes;
    public Date date;
    public int hype;
    public int categoryID;
    public int categoryType;
    public ArrayList<Comment> comments;
    public boolean voted;
    public boolean favorit;
    
    public Joke()
    {
    	
    }

    public Joke(int jokeId, String username, int votes, String jokeText, Date date, int hype, int categoryID, int categoryType, boolean voted, boolean favorit)
    {
        this.jokeId = jokeId;
        this.username = username;
        this.votes = votes;
        this.jokeText  = jokeText;
        this.date = date;
        this.hype = hype;
        this.categoryID = categoryID;
        this.categoryType = categoryType;
        this.voted = voted;
        this.favorit = favorit;
    }

    

    public int getJokeId()
    {
        return jokeId;
    }

    public String getJokeText() {
        return jokeText;
    }

    public String getUsername() {
        return username;
    }

    public int getVotes() {
        return votes;
    }

    public Date getDate() {
        return date;
    }

    public int getHype() {
        return hype;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public int getCategoryType() {
        return categoryType;
    }

    public boolean isVoted()
    {
        return voted;
    }

    public void vote(boolean voted)
    {
        this.voted = voted;
    }

    public boolean isFavorit()
    {
        return favorit;
    }

    public void setFavorit(boolean favorit)
    {
        this.favorit = favorit;
    }
}
