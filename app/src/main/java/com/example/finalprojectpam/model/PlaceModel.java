package com.example.finalprojectpam.model;

public class PlaceModel {
    private String name, description, image, maps, key;

    public PlaceModel(){

    }

    public PlaceModel(String name, String description, String image, String maps) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.maps = maps;
    }

    public PlaceModel(String name, String description, String maps) {
        this.name = name;
        this.description = description;
        this.maps = maps;
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

    public String getMaps() {
        return maps;
    }

    public void setMaps(String maps) {
        this.maps = maps;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
