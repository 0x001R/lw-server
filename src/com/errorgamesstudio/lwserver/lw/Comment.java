package com.errorgamesstudio.lwserver.lw;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Comment implements Serializable{
    private String commentText;
    private String user;
    private int votes;
    private Date date;
    private int commentId;
    private ArrayList<Comment> childComments;


    public int getCommentId() {
        return commentId;
    }

    public ArrayList<Comment> getChildComments() {
        return childComments;
    }

    public boolean addChildComment(Comment comment) {

        if(commentId == comment.getCommentId())
        {
            childComments.add(comment);
            return true;
        }
        else
        {
            for(Comment cc : childComments)
            {
                if(cc.addChildComment(comment))
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
        }
        return false;

    }
}