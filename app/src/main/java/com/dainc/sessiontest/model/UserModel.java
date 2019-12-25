package com.dainc.sessiontest.model;

import java.io.Serializable;

public class UserModel implements Serializable {


    private String sNo;
    private String userId;
    private String familyName;
    private String firstName;
    private int age;
    private int genderId;
    private int authorityId;
    private int admin;
    private String password;
    private String authorityName;
    private String genderName;

    public UserModel(String sNo, String userId, String familyName, String firstName, String authorityName,int admin) {
        this.sNo = sNo;
        this.userId = userId;
        this.familyName = familyName;
        this.firstName = firstName;
        this.authorityName = authorityName;
        this.admin = admin;
    }

    public UserModel(String userId, String familyName, String firstName, int age, int genderId, int authorityId, int admin, String password, String authorityName, String genderName) {
        this.userId = userId;
        this.familyName = familyName;
        this.firstName = firstName;
        this.age = age;
        this.genderId = genderId;
        this.authorityId = authorityId;
        this.admin = admin;
        this.password = password;
        this.authorityName = authorityName;
        this.genderName = genderName;
    }

    public String getsNo() {
        return sNo;
    }

    public void setsNo(String sNo) {
        this.sNo = sNo;
    }
    public String getAuthorityName() {
        return authorityName;
    }

    public void setAuthorityName(String authorityName) {
        this.authorityName = authorityName;
    }

    public String getGenderName() {
        return genderName;
    }

    public void setGenderName(String genderName) {
        this.genderName = genderName;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getGenderId() {
        return genderId;
    }

    public void setGenderId(int genderId) {
        this.genderId = genderId;
    }

    public int getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(int authorityId) {
        this.authorityId = authorityId;
    }

    public int getAdmin() {
        return admin;
    }

    public void setAdmin(int admin) {
        this.admin = admin;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
