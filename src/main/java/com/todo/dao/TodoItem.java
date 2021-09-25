package com.todo.dao;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TodoItem {
    private String title;
    private String desc;
    private String current_date;
    private String category;
    private String due_date;

    public TodoItem(String title, String desc, String category, String due_date){
        this.title=title;
        this.desc=desc;
        this.category = category;
        SimpleDateFormat curDate = new SimpleDateFormat("yyyy/MM/dd kk:mm:ss");
        this.current_date= curDate.format(new Date());
        this.due_date = due_date.substring(0, 4) + "/" + due_date.substring(4, 6) + "/" + due_date.substring(6, 8);
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCurrent_date() {
        return current_date;
    }

    public String toSaveString() {
        return category + "##" + title + "##" + desc + "##" + due_date + "##" + current_date + "\n";
    }

    public void setCurrent_date(String current_date) { this.current_date = current_date; }

    @Override
    public String toString() { return " [" + category + "] " + title + " - " + desc + " - " + due_date + " - " + current_date; }

    public String getCategory() {
        return category;
    }
}