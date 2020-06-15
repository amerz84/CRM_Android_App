package com.example.churchappcapstone.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "logins")
public class LoginEntity {

    @PrimaryKey(autoGenerate = true)
    private int loginId;
    private String loginEmail;
    private String loginPassword;
    private boolean isAdmin;

    public LoginEntity(String loginEmail, String loginPassword, boolean isAdmin) {
        this.loginEmail = loginEmail;
        this.loginPassword = loginPassword;
        this.isAdmin = isAdmin;
    }

    public int getLoginId() {
        return loginId;
    }

    public void setLoginId(int loginId) {
        this.loginId = loginId;
    }

    public String getLoginEmail() {
        return loginEmail;
    }

    public void setLoginEmail(String loginEmail) {
        this.loginEmail = loginEmail;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
