package com.ourbuaa.buaahelper;

/**
 * Created by Croxx on 2017/5/25.
 */

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable(tableName = "user")
public class DBUserBean {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String username;
    @DatabaseField
    private String access_token;
    @DatabaseField
    private  int state;
    @DatabaseField
    private int number;
    @DatabaseField
    private String name;
    @DatabaseField
    private int department;
    @DatabaseField
    private String department_name;
    @DatabaseField
    private String email;
    @DatabaseField
    private int phone;

    public DBUserBean(){

    }

    public DBUserBean(String username,String access_token){
        this.username = username;
        this.access_token = access_token;
        this.state=0;
    }

    public int getId(){return id;}
    public String getUsername(){return username;}
    public String getAccess_token(){return access_token;}
    public int getState(){return state;}
    public void ban(){state=1;}

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }
}
