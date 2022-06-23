package com.example.frenchtoenglishnotebook;

import java.io.Serializable;

public class Sentence implements Serializable {
    private String english;
    private String french;

    public Sentence(String french, String english) {
        this.english = english;
        this.french = french;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public String getFrench() {
        return french;
    }

    public void setFrench(String french) {
        this.french = french;
    }
}
