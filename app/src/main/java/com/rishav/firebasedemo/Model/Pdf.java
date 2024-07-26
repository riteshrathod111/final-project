package com.rishav.firebasedemo.Model;

public class Pdf {
    private String id;
    private String name; // Add this field to store PDF name
    private String description;
    private String url;
    private String userId; // Add this field to store user ID or username

    public Pdf() {
        // Default constructor required for calls to DataSnapshot.getValue(Pdf.class)
    }

    public Pdf(String id, String name, String description, String url, String userId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.url = url;
        this.userId = userId;
    }

    // Getters and setters for all fields
    // ...

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
