package com.example.user.board.Models;

import java.io.Serializable;

public class FreeList implements Serializable {

    //String Seq;
    private long id;
    private String TITLE;
    private String CONTENT;
    private String DATE;


    public FreeList() {

    }

    public FreeList(String title, String content, String date) {
        //super();
        this.TITLE = title;
        this.CONTENT = content;
        this.DATE = date;


    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String title) {
        this.TITLE = title;
    }

    public String getCONTENT() {
        return CONTENT;
    }

    public void setCONTENT(String CONTENT) {
        this.CONTENT = CONTENT;
    }

    public String getDATE() {
        return DATE;
    }

    public void setDATE(String date) {
        this.DATE = date;
    }


    @Override
    public String toString() {
        return "FreeList{" +
                "id=" + id +
                ", TITLE='" + TITLE + '\'' +
                ", CONTENT='" + CONTENT + '\'' +
                ", DATE='" + DATE + '\'' +
                '}';
    }
}
