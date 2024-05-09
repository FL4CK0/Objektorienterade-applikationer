package org.example;

public class User {
    //private String id;
    private String username;

    public User(String username) {
        // this.id = id;
        this.username = username;
    }
    /*
        public String getId() {
            return id;
        }
    */
    public String getUsername() {
        return username;
    }
}