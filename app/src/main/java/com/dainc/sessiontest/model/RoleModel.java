package com.dainc.sessiontest.model;

import java.io.Serializable;

public class RoleModel implements Serializable {
    private int authorityId;
    private String authorityName;

    public RoleModel(int authorityId, String authorityName) {
        this.authorityId = authorityId;
        this.authorityName = authorityName;
    }

    public int getAuthorityId() {
        return authorityId;
    }
    public void setAuthorityId(int authorityId) {
        this.authorityId = authorityId;
    }
    public String getAuthorityName() {
        return authorityName;
    }
    public void setAuthorityName(String authorityName) {
        this.authorityName = authorityName;
    }

    @Override
    public String toString() {
        return this.authorityName;
    }
}
