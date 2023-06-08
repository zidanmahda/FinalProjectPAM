package com.example.finalprojectpam.model;

public class PlaceModel {
    private String name, description, image, key;

    public PlaceModel(){

    }

    public PlaceModel(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public PlaceModel(String image, String name, String description) {
        this.image = image;
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
