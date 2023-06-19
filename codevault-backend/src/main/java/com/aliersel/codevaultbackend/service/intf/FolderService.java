package com.aliersel.codevaultbackend.service.intf;

import com.aliersel.codevaultbackend.controller.entity.FileWithTypes;
import com.aliersel.codevaultbackend.utils.Result;
import org.springframework.stereotype.Service;

import java.util.List;

public interface FolderService {
    Result<List<FileWithTypes>> getFilesByFolderPath(Integer userID, String folderPath);
    Result addFolderByPath(Integer userID, String parentPath, String folderName);
    Result deleteFolder(Integer userID, Integer folderID);
}
