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
    private String department_name;
    @DatabaseField
    private String department_avatar;
    @DatabaseField
    private long start_time;
    @DatabaseField
    private long finish_time;
    @DatabaseField
    private String excerpt;
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
    @DatabaseField
    private int full;
    @DatabaseField
    private long version;

    public DBNotificationBean() {

    }

    public DBNotificationBean(int id,long version,String owner){
        this.id=id;
        this.version=version;
        this.full=1;
        this.owner=owner;
    }
/*

    public DBNotificationBean(int id, String owner, long updated_at,long version, String title, String author, int department, String content, String files) {
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

        this.full = 0;
        this.version = version;
        this.important = 0;
    }

    public DBNotificationBean(int id,String owner,long updated_at,long version){
        this.id=id;
        this.updated_at=updated_at;
        this.version=version;
    }
    */

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(long updated_at) {
        this.updated_at = updated_at;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getDepartment() {
        return department;
    }

    public void setDepartment(int department) {
        this.department = department;
    }

    public String getDepartment_name() {
        return department_name;
    }

    public void setDepartment_name(String department_name) {
        this.department_name = department_name;
    }

    public String getDepartment_avatar() {
        return department_avatar;
    }

    public void setDepartment_avatar(String department_avatar) {
        this.department_avatar = department_avatar;
    }

    public long getStart_time() {
        return start_time;
    }

    public void setStart_time(long start_time) {
        this.start_time = start_time;
    }

    public long getFinish_time() {
        return finish_time;
    }

    public void setFinish_time(long finish_time) {
        this.finish_time = finish_time;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFiles() {
        return files;
    }

    public void setFiles(String files) {
        this.files = files;
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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getImportant() {
        return important;
    }

    public void setImportant(int important) {
        this.important = important;
    }

    public int getFull() {
        return full;
    }

    public void setFull(int full) {
        this.full = full;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }
}
