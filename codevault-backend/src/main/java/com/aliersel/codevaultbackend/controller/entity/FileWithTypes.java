package com.aliersel.codevaultbackend.controller.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FileWithTypes {
    private Integer id;
    private String name;
    private String type;
    private OffsetDateTime lastModified;
}
