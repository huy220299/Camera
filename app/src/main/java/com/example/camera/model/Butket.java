package com.example.camera.model;

import java.io.Serializable;

public class Butket implements Serializable {
    private String name;
    private String firstImageContainedPath;
    private boolean isOpened;

    public Butket() {
    }

    public Butket(String name, String firstImageContainedPath) {
        this.name = name;
        this.firstImageContainedPath = firstImageContainedPath;
        isOpened=false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstImageContainedPath() {
        return firstImageContainedPath;
    }

    public void setFirstImageContainedPath(String firstImageContainedPath) {
        this.firstImageContainedPath = firstImageContainedPath;
    }

    public boolean isOpened() {
        return isOpened;
    }

    public void setOpened(boolean opened) {
        isOpened = opened;
    }
}