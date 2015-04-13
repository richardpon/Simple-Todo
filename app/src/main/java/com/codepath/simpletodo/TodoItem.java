package com.codepath.simpletodo;

public class TodoItem {
    private int id;
    private String body;
    private int position;

    public TodoItem(String body, int position) {
        super();
        this.body = body;
        this.position = position;
    }

    public String getBody() {
        return this.body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getPosition() {
        return this.position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
