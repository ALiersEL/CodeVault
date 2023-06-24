package com.aliersel.codevaultbackend.service.intf;

import com.aliersel.codevaultbackend.controller.api.FileWithTypes;
import com.aliersel.codevaultbackend.entity.Folder;
import com.aliersel.codevaultbackend.utils.Result;

import java.util.List;

public interface FolderService {
    Result<List<FileWithTypes>> getFilesByFolderPath(Integer userID, String folderPath);
    Result<List<FileWithTypes>> getFilesByFolderID(Integer userID, Integer folderID);
    Result<List<Folder>> getFoldersByFolderID(Integer folderID);
    Result addFolderByPath(Integer userID, String parentPath, String folderName);
    Result deleteFolder(Integer userID, Integer folderID);
    Result<Integer> getFolderIDByFolderPath(Integer userID, String folderPath);
    Result renameFolder(Integer folderID, String newName, String newPath);
    Result moveFolder(Integer folderID, Integer targetFolderID);
    Result<Integer> getRootFolderIDByUserID(Integer userID);
}
