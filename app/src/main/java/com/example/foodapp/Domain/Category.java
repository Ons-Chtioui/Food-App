package com.example.foodapp.Domain;

public class Category {
    private int id;
    private String ImagePath;
    private String Name;

    public String getName() {
        return Name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Category(int id, String imagePath, String name) {
        this.id = id;
        ImagePath = imagePath;
        Name = name;
    }
    public Category() {}


    public void setName(String name) {
        Name = name;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public int getId() {
        return id;
    }
}
