package com.StarkIndustries.JwtAuthenticationMark2.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name="Users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id",nullable = false)
    private int userId;

    @Column(name = "username")
    @NotBlank(message = "Username should not be empty!!")
    @NotNull(message = "Username should not be null!!")
    private String username;

    @Column(name = "password")
    @NotBlank(message = "Password should not be empty!!")
    @NotNull(message = "Password should not be null!!")
    private String password;

    public Users(int userId, String username, String password) {
        this.userId = userId;
        this.username = username;
        this.password = password;
    }

    public Users(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Users() {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public @NotBlank(message = "Username should not be empty!!") @NotNull(message = "Username should not be null!!") String getUsername() {
        return username;
    }

    public void setUsername(@NotBlank(message = "Username should not be empty!!") @NotNull(message = "Username should not be null!!") String username) {
        this.username = username;
    }

    public @NotBlank(message = "Password should not be empty!!") @NotNull(message = "Password should not be null!!") String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank(message = "Password should not be empty!!") @NotNull(message = "Password should not be null!!") String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Users{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
