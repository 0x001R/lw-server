package com.errorgamesstudio.lwserver.lw;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Comment implements Serializable{
    public String commentText;
    public String username;
    public int votes;
    public Date date;
    public int commentID;
    public int userID;
    public int jokeID;
    public int parent;
}