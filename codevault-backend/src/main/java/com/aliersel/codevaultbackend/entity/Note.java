package com.aliersel.codevaultbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Note {
    private Integer noteID;
    private String noteText;
    private OffsetDateTime lastModified;
    private Integer problemID;
}
