package com.example.johanaanesen.imt3673_lab02;

import java.io.Serializable;

public class RssFeedModel implements Serializable{

    public String title;
    public String link;

    public RssFeedModel(String title, String link) {
        this.title = title;
        this.link = link;
    }
}