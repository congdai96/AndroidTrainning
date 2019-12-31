package com.dainc.sessiontest.model;

public class ShuukeiModel {

    String roleName;
    int male;
    int female;
    int ageMax19;
    int ageMin20;
    int notFull;
    int notAge;

    public ShuukeiModel(String roleName, int male, int female, int ageMax19, int ageMin20, int notFull, int notAge) {
        this.roleName = roleName;
        this.male = male;
        this.female = female;
        this.ageMax19 = ageMax19;
        this.ageMin20 = ageMin20;
        this.notFull = notFull;
        this.notAge = notAge;
    }

    public String getRoleName() {
        return roleName;
    }
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
    public int getMale() {
        return male;
    }
    public void setMale(int male) {
        this.male = male;
    }
    public int getFemale() {
        return female;
    }
    public void setFemale(int female) {
        this.female = female;
    }
    public int getAgeMax19() {
        return ageMax19;
    }
    public void setAgeMax19(int ageMax19) {
        this.ageMax19 = ageMax19;
    }
    public int getAgeMin20() {
        return ageMin20;
    }
    public void setAgeMin20(int ageMin20) {
        this.ageMin20 = ageMin20;
    }
    public int getNotFull() {
        return notFull;
    }
    public void setNotFull(int notFull) {
        this.notFull = notFull;
    }
    public int getNotAge() {
        return notAge;
    }
    public void setNotAge(int notAge) {
        this.notAge = notAge;
    }
}
