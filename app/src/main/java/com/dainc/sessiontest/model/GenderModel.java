package com.dainc.sessiontest.model;

import java.io.Serializable;

public class GenderModel implements Serializable {
    private int genderId;
    private String genderName;

    public GenderModel(int genderId, String genderName) {
        this.genderId = genderId;
        this.genderName = genderName;
    }

    public int getGenderId() {
        return genderId;
    }

    public void setGenderId(int genderId) {
        this.genderId = genderId;
    }

    public String getGenderName() {
        return genderName;
    }

    public void setGenderName(String genderName) {
        this.genderName = genderName;
    }

    @Override
    public String toString() {
        return this.genderName;
    }
}
