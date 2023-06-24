package com.aliersel.codevaultbackend.controller.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Source {
    private String companyName;
    private Integer companyID;
    private String departmentName;
    private Integer departmentID;
    private String postName;
    private Integer postID;
}
