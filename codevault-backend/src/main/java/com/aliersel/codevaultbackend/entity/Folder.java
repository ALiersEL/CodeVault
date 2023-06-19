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
public class Folder {
    private Integer folderID;
    private String folderName;
    private String folderPath;
    private Integer parentFolderID;
    OffsetDateTime dateAdded;
    OffsetDateTime lastModified;
    private Integer userID;
}
