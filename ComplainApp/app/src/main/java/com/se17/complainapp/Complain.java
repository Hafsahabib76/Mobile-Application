package com.se17.complainapp;

public class Complain {

    private int id;
    private String category;
    private String severity;
    private String description;
    private byte[] complainImage;

    public Complain() {
    }

    public Complain(int id, String category, String severity, String description, byte[] complainImage) {
        this.id = id;
        this.category = category;
        this.severity = severity;
        this.description = description;
        this.complainImage = complainImage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getComplainImage() {
        return complainImage;
    }

    public void setComplainImage(byte[] complainImage) {
        this.complainImage = complainImage;
    }
}
