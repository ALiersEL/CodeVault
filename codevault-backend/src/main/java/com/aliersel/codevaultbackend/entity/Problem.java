package com.aliersel.codevaultbackend.entity;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
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
}
