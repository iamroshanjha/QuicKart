package com.a0quickcartgmail.quickart;



public class User {
    public String userid;
    public String username;
    public String email;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String userid, String username, String email) {
        this.userid=userid;
        this.username = username;
        this.email = email;

    }

    public String getuserid() {
       return userid;
    }

    public String getusername() {
        return username;
    }

    public String getemail() {
       return email;
    }

}
