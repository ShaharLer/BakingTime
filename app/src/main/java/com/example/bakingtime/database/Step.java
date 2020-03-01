package com.example.bakingtime.database;

public class Step {

    private int id;
    private String shortDescription;
    private String description;
    private String videoUrl;

    public Step(int id, String shortDescription, String description, String videoUrl) {
        this.id = id;
        this.shortDescription = shortDescription;
        this.description= description;
        this.videoUrl = videoUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
