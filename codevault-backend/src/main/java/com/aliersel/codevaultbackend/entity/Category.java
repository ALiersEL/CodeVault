package com.aliersel.codevaultbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Category {
    Integer categoryID;
    String categoryName;
    Integer userID;
}
