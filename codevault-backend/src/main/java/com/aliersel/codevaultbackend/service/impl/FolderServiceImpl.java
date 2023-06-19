package com.aliersel.codevaultbackend.service.impl;

import com.aliersel.codevaultbackend.controller.entity.FileWithTypes;
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
            return ResultUtil.error(500, "获取失败");
        }
    }

    @Override
    public Result addFolderByPath(Integer userID, String parentPath, String folderName) {
        try {
            String folderPath = parentPath + "/" + folderName;
            System.out.println(folderPath);
            return ResultUtil.success(folderMapper.saveFolder(userID, folderPath, parentPath, folderName));
        } catch (Exception e) {
            return ResultUtil.error(500, "添加失败");
        }
    }

    @Override
    public Result deleteFolder(Integer userID, Integer folderID) {
        try {
            System.out.println();
            folderMapper.deleteFolder(userID, folderID);
            return ResultUtil.success();
        } catch (Exception e) {
            return ResultUtil.error(500, "删除失败");
        }
    }
}
