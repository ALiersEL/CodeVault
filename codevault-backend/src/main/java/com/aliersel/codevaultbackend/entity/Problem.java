package com.aliersel.codevaultbackend.entity;

import java.time.OffsetDateTime;

public class Problem {
    Integer problemID;
    String problemTitle;
    String problemContent;
    Integer problemType;
    Integer difficulty;
    Boolean status;
    Integer mastery;
    OffsetDateTime dateAdded;
    OffsetDateTime lastModified;
    Integer companyID;
    Integer departmentID;
    Integer postID;
    Integer userID;
    Integer folderID;

    public Problem() {
    }

    public Problem(Integer problemID, String problemTitle, String problemContent, Integer problemType, Integer difficulty, Boolean status, Integer mastery, OffsetDateTime dateAdded, OffsetDateTime lastModified, Integer companyID, Integer departmentID, Integer postID, Integer userID, Integer folderID) {
        this.problemID = problemID;
        this.problemTitle = problemTitle;
        this.problemContent = problemContent;
        this.problemType = problemType;
        this.difficulty = difficulty;
        this.status = status;
        this.mastery = mastery;
        this.dateAdded = dateAdded;
        this.lastModified = lastModified;
        this.companyID = companyID;
        this.departmentID = departmentID;
        this.postID = postID;
        this.userID = userID;
        this.folderID = folderID;
    }

    /**
     * 获取
     * @return problemID
     */
    public Integer getProblemID() {
        return problemID;
    }

    /**
     * 设置
     * @param problemID
     */
    public void setProblemID(Integer problemID) {
        this.problemID = problemID;
    }

    /**
     * 获取
     * @return problemTitle
     */
    public String getProblemTitle() {
        return problemTitle;
    }

    /**
     * 设置
     * @param problemTitle
     */
    public void setProblemTitle(String problemTitle) {
        this.problemTitle = problemTitle;
    }

    /**
     * 获取
     * @return problemContent
     */
    public String getProblemContent() {
        return problemContent;
    }

    /**
     * 设置
     * @param problemContent
     */
    public void setProblemContent(String problemContent) {
        this.problemContent = problemContent;
    }

    /**
     * 获取
     * @return problemType
     */
    public Integer getProblemType() {
        return problemType;
    }

    /**
     * 设置
     * @param problemType
     */
    public void setProblemType(Integer problemType) {
        this.problemType = problemType;
    }

    /**
     * 获取
     * @return difficulty
     */
    public Integer getDifficulty() {
        return difficulty;
    }

    /**
     * 设置
     * @param difficulty
     */
    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * 获取
     * @return status
     */
    public Boolean getStatus() {
        return status;
    }

    /**
     * 设置
     * @param status
     */
    public void setStatus(Boolean status) {
        this.status = status;
    }

    /**
     * 获取
     * @return mastery
     */
    public Integer getMastery() {
        return mastery;
    }

    /**
     * 设置
     * @param mastery
     */
    public void setMastery(Integer mastery) {
        this.mastery = mastery;
    }

    /**
     * 获取
     * @return dateAdded
     */
    public OffsetDateTime getDateAdded() {
        return dateAdded;
    }

    /**
     * 设置
     * @param dateAdded
     */
    public void setDateAdded(OffsetDateTime dateAdded) {
        this.dateAdded = dateAdded;
    }

    /**
     * 获取
     * @return lastModified
     */
    public OffsetDateTime getLastModified() {
        return lastModified;
    }

    /**
     * 设置
     * @param lastModified
     */
    public void setLastModified(OffsetDateTime lastModified) {
        this.lastModified = lastModified;
    }

    /**
     * 获取
     * @return companyID
     */
    public Integer getCompanyID() {
        return companyID;
    }

    /**
     * 设置
     * @param companyID
     */
    public void setCompanyID(Integer companyID) {
        this.companyID = companyID;
    }

    /**
     * 获取
     * @return departmentID
     */
    public Integer getDepartmentID() {
        return departmentID;
    }

    /**
     * 设置
     * @param departmentID
     */
    public void setDepartmentID(Integer departmentID) {
        this.departmentID = departmentID;
    }

    /**
     * 获取
     * @return postID
     */
    public Integer getPostID() {
        return postID;
    }

    /**
     * 设置
     * @param postID
     */
    public void setPostID(Integer postID) {
        this.postID = postID;
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
     * @return folderID
     */
    public Integer getFolderID() {
        return folderID;
    }

    /**
     * 设置
     * @param folderID
     */
    public void setFolderID(Integer folderID) {
        this.folderID = folderID;
    }

    public String toString() {
        return "Problem{problemID = " + problemID + ", problemTitle = " + problemTitle + ", problemContent = " + problemContent + ", problemType = " + problemType + ", difficulty = " + difficulty + ", status = " + status + ", mastery = " + mastery + ", dateAdded = " + dateAdded + ", lastModified = " + lastModified + ", companyID = " + companyID + ", departmentID = " + departmentID + ", postID = " + postID + ", userID = " + userID + ", folderID = " + folderID + "}";
    }
}
