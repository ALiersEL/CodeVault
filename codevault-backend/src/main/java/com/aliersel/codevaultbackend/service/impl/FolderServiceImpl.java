package com.aliersel.codevaultbackend.service.impl;

import com.aliersel.codevaultbackend.controller.api.FileWithTypes;
import com.aliersel.codevaultbackend.entity.Folder;
import com.aliersel.codevaultbackend.mapper.FolderMapper;
import com.aliersel.codevaultbackend.service.intf.FolderService;
import com.aliersel.codevaultbackend.utils.Result;
import com.aliersel.codevaultbackend.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FolderServiceImpl implements FolderService {
    @Autowired
    private FolderMapper folderMapper;

    @Override
    public Result<List<FileWithTypes>> getFilesByFolderPath(Integer userID, String folderPath) {
        try {
            List<FileWithTypes> files = folderMapper.findFilesByFolderPath(userID, folderPath);
            return ResultUtil.success(files);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(500, "获取失败");
        }
    }

    @Override
    public Result<List<FileWithTypes>> getFilesByFolderID(Integer userID, Integer folderID) {
        try {
            List<FileWithTypes> files = folderMapper.findFilesByFolderID(userID, folderID);
            return ResultUtil.success(files);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(500, "获取失败");
        }
    }

    @Override
    public Result<List<Folder>> getFoldersByFolderID(Integer folderID) {
        try {
            List<Folder> folders = folderMapper.findFoldersByFolderID(folderID);
            return ResultUtil.success(folders);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(500, "获取失败");
        }
    }

    @Override
    public Result addFolderByPath(Integer userID, String parentPath, String folderName) {
        try {
            String folderPath = parentPath + "/" + folderName;
            System.out.println(userID);
            System.out.println(folderPath);
            System.out.println(parentPath);
            System.out.println(folderName);
            return ResultUtil.success(folderMapper.saveFolder(userID, folderPath, parentPath, folderName));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(500, "添加失败");
        }
    }

    @Override
    public Result deleteFolder(Integer userID, Integer folderID) {
        try {
            folderMapper.deleteFolder(userID, folderID);
            return ResultUtil.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(500, "删除失败");
        }
    }

    @Override
    public Result<Integer> getFolderIDByFolderPath(Integer userID, String folderPath) {
        try {
            return ResultUtil.success(folderMapper.findFolderIDByFolderPath(userID, folderPath));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(500, "查找失败");
        }
    }

    @Override
    public Result renameFolder(Integer folderID, String newName, String newPath) {
        try {
            folderMapper.updateFolderName(folderID, newName, newPath);
            return ResultUtil.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(500, "重命名失败");
        }
    }

    @Override
    public Result moveFolder(Integer folderID, Integer targetFolderID) {
        try {
            folderMapper.updateFolderParent(folderID, targetFolderID);
            return ResultUtil.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(500, "移动失败");
        }
    }

    @Override
    public Result<Integer> getRootFolderIDByUserID(Integer userID) {
        try {
            return ResultUtil.success(folderMapper.findFolderIDByFolderPath(userID, "~"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(500, "查找失败");
        }
    }
}
