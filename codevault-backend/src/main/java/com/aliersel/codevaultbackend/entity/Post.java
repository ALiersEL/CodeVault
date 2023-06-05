package com.aliersel.codevaultbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Post {
    Integer postID;
    String postName;
    Integer departmentID;
}
