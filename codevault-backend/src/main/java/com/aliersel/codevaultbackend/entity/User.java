package com.aliersel.codevaultbackend.entity;

import java.time.OffsetDateTime;

public class User {
    Integer userID;
    String userName;
    String passwordHash;
    OffsetDateTime dateRegistered;
    String phoneNumber;
    String email;
    Integer acEasy;
    Integer acMedium;
    Integer acHard;
    Integer totalEasy;
    Integer totalMedium;
    Integer totalHard;

    public User() {
    }

    public User(Integer userID, String userName, String passwordHash, OffsetDateTime dateRegistered, String phoneNumber, String email, Integer acEasy, Integer acMedium, Integer acHard, Integer totalEasy, Integer totalMedium, Integer totalHard) {
        this.userID = userID;
        this.userName = userName;
        this.passwordHash = passwordHash;
        this.dateRegistered = dateRegistered;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.acEasy = acEasy;
        this.acMedium = acMedium;
        this.acHard = acHard;
        this.totalEasy = totalEasy;
        this.totalMedium = totalMedium;
        this.totalHard = totalHard;
    }

    /**
     * 获取
     * @return userID
     */
    public Integer getUserID() {
        return userID;
    }

    /**
     * 设置
     * @param userID
     */
    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    /**
     * 获取
     * @return userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 设置
     * @param userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * 获取
     * @return passwordHash
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * 设置
     * @param passwordHash
     */
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    /**
     * 获取
     * @return dateRegistered
     */
    public OffsetDateTime getDateRegistered() {
        return dateRegistered;
    }

    /**
     * 设置
     * @param dateRegistered
     */
    public void setDateRegistered(OffsetDateTime dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    /**
     * 获取
     * @return phoneNumber
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * 设置
     * @param phoneNumber
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * 获取
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * 设置
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 获取
     * @return acEasy
     */
    public Integer getAcEasy() {
        return acEasy;
    }

    /**
     * 设置
     * @param acEasy
     */
    public void setAcEasy(Integer acEasy) {
        this.acEasy = acEasy;
    }

    /**
     * 获取
     * @return acMedium
     */
    public Integer getAcMedium() {
        return acMedium;
    }

    /**
     * 设置
     * @param acMedium
     */
    public void setAcMedium(Integer acMedium) {
        this.acMedium = acMedium;
    }

    /**
     * 获取
     * @return acHard
     */
    public Integer getAcHard() {
        return acHard;
    }

    /**
     * 设置
     * @param acHard
     */
    public void setAcHard(Integer acHard) {
        this.acHard = acHard;
    }

    /**
     * 获取
     * @return totalEasy
     */
    public Integer getTotalEasy() {
        return totalEasy;
    }

    /**
     * 设置
     * @param totalEasy
     */
    public void setTotalEasy(Integer totalEasy) {
        this.totalEasy = totalEasy;
    }

    /**
     * 获取
     * @return totalMedium
     */
    public Integer getTotalMedium() {
        return totalMedium;
    }

    /**
     * 设置
     * @param totalMedium
     */
    public void setTotalMedium(Integer totalMedium) {
        this.totalMedium = totalMedium;
    }

    /**
     * 获取
     * @return totalHard
     */
    public Integer getTotalHard() {
        return totalHard;
    }

    /**
     * 设置
     * @param totalHard
     */
    public void setTotalHard(Integer totalHard) {
        this.totalHard = totalHard;
    }

    public String toString() {
        return "User{userID = " + userID + ", userName = " + userName + ", passwordHash = " + passwordHash + ", dateRegistered = " + dateRegistered + ", phoneNumber = " + phoneNumber + ", email = " + email + ", acEasy = " + acEasy + ", acMedium = " + acMedium + ", acHard = " + acHard + ", totalEasy = " + totalEasy + ", totalMedium = " + totalMedium + ", totalHard = " + totalHard + "}";
    }
}
