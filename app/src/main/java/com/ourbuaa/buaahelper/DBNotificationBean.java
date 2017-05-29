package com.ourbuaa.buaahelper;

/**
 * Created by Croxx on 2017/5/25.
 */


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "notification")
public class DBNotificationBean {
    @DatabaseField(id = true, uniqueCombo = true)
    private int id;
    @DatabaseField
    private long updated_at;
    @DatabaseField
    private String title;
    @DatabaseField
    private String author;
    @DatabaseField
    private int department;
    @DatabaseField
    private String content;
    @DatabaseField
    private String files;
    @DatabaseField
    private int star;
    @DatabaseField
    private int read;
    @DatabaseField
    private int delete;
    @DatabaseField(uniqueCombo = true)
    private String owner;
    @DatabaseField
    private int important;

    public DBNotificationBean() {

    }


    public DBNotificationBean(int id, String owner, long updated_at, String title, String author, int department, String content, String files) {
        this.id = id;
        this.updated_at = updated_at;
        this.title = title;
        this.author = author;
        this.department = department;
        this.content = content;
        this.files = files;
        this.star = 0;

        this.read = 0;
        this.delete = 0;
        this.owner = owner;

        this.important = 0;
    }

    public int getId() {
        return id;
    }

    public long getUpdated_at() {
        return updated_at;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getDepartment() {
        return department;
    }

    public String getContent() {
        return content;
    }

    public String getFiles() {
        return files;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public int getRead() {
        return read;
    }

    public void setRead(int read) {
        this.read = read;
    }

    public int getDelete() {
        return delete;
    }

    public void setDelete(int delete) {
        this.delete = delete;
    }

    public int getImportant() {
        return important;
    }

    public void setImportant(int important) {
        this.important = important;
    }
}
