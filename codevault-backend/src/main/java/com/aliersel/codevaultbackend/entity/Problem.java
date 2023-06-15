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
    private Integer problemID;
    private String problemTitle;
    private String problemContent;
    private Integer problemType;
    private Integer difficulty;
    private Boolean status;
    private Integer mastery;
    private OffsetDateTime dateAdded;
    private OffsetDateTime lastModified;
    private Integer userID;
    private Integer folderID;
}
