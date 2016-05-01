package com.rdxcabs.Beans;

/**
 * Created by arung on 17/4/16.
 */
public class UserBean {

    private String username;
    private String password;
    private String fullName;
    private String email;
    private String phoneNumber;

    public UserBean(){

    }

    public UserBean(String username, String password, String fullName, String email, String phoneNumber) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }

    public String getFullName(){
        return fullName;
    }

    public String getEmail(){
        return email;
    }

    public String getPhoneNumber(){
        return phoneNumber;
    }
}
