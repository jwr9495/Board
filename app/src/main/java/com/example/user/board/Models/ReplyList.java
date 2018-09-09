package com.example.user.board.Models;

public class ReplyList {

    //String Seq;
    String REPLY;
    String DATE;
    public ReplyList()
    {

    }

    public ReplyList(String reply, String date) {
        super();
        this.REPLY = reply;
        this.DATE = date;
    }


    public String getREPLY() {
        return REPLY;
    }

    public void setREPLY(String reply) {
        this.REPLY = reply;
    }

    public String getDATE() {
        return DATE;
    }

    public void setDATE(String date) {
        this.DATE = date;
    }
}
