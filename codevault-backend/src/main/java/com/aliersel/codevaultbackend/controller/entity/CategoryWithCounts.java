package com.aliersel.codevaultbackend.controller.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CategoryWithCounts {
    private String name;
    private Integer id;
    private Integer count;
}
