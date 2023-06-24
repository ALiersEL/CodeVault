package com.aliersel.codevaultbackend.controller.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.OffsetDateTime;

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
    private Integer mastery;
}
