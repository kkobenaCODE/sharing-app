package com.example.sharingapp;

import java.util.UUID;

public class Contact extends Observable {
    private String username;
    private String email;
    private String id;

    public Contact(String id, String username, String email) {
        if (id == null){
            setId();
        } else {
            updateId(id);
        }
        this.email = email;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        notifyObservers();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyObservers();
    }

    public String getId() {
        return id;
    }

    public void setId() {
        this.id = UUID.randomUUID().toString();
        notifyObservers();
    }

    public void updateId(String id){
        this.id = id;
        notifyObservers();
    }
}
