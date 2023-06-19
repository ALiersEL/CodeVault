package com.aliersel.codevaultbackend.controller.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProblemWithTags {
    private Integer problemID;
    private String problemTitle;
    private Integer problemType;
    private Integer difficulty;
    private Boolean status;
    private OffsetDateTime lastModified;
    private String[] tags;
}
