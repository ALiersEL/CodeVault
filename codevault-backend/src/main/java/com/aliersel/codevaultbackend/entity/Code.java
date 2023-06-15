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
public class Code {
    private Integer codeID;
    private String codeText;
    private OffsetDateTime lastModified;
    private Integer codeLanguage;
    private Integer problemID;
}
