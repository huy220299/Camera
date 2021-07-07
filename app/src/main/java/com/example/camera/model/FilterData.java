package com.example.camera.model;

import java.io.Serializable;

public class FilterData implements Serializable {
 
    String name;
    String rule;
    int imageId;
 
    public FilterData(String name, String rule) {
        this.name = name;
        this.rule = rule;
    }
 
    public String getName() {
        return name;
    }
 
    public void setName(String name) {
        this.name = name;
    }
 
    public String getRule() {
        return rule;
    }
 
    public void setRule(String rule) {
        this.rule = rule;
    }
 

}