package com.StarkIndustries.JwtAuthenticationMark2.Models;

public class PasswordModel {

    private String username;

    private String password;

    private String newPassword;

    public PasswordModel(String username,String password,String newPassword){
        this.username=username;
        this.password=password;
        this.newPassword=newPassword;
    }

    public PasswordModel(String password,String newPassword){
        this.password=password;
        this.newPassword=newPassword;
    }

    public PasswordModel(){

    }

    @Override
    public String toString() {
        return "PasswordModel{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", newPassword='" + newPassword + '\'' +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

}
