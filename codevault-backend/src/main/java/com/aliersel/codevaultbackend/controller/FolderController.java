package com.aliersel.codevaultbackend.controller;

import com.alibaba.fastjson2.JSONObject;
import com.aliersel.codevaultbackend.controller.entity.FileWithTypes;
import com.aliersel.codevaultbackend.entity.Folder;
import com.aliersel.codevaultbackend.security.JwtTokenProvider;
import com.aliersel.codevaultbackend.service.intf.FolderService;
import com.aliersel.codevaultbackend.utils.Result;
import com.aliersel.codevaultbackend.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.util.List;

@RestController
@RequestMapping("/folder")
public class FolderController {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private FolderService folderService;

    @GetMapping
    public Result<List<FileWithTypes>> getFolderContent(@RequestHeader("Authorization") String token, @RequestParam(value = "path") String path) {
         try {
             Integer userID = jwtTokenProvider.getUserid(token.split(" ")[1].trim());
             return folderService.getFilesByFolderPath(userID, path);
            } catch (Exception e) {
                return ResultUtil.error(500, "获取失败");
         }
    }

    @GetMapping
    public Result<List<Folder>> getFoldersByFolderID(@RequestHeader("Authorization") String token, @RequestParam(value = "folderID") Integer folderID) {
        try {
            Integer userID = jwtTokenProvider.getUserid(token.split(" ")[1].trim());
            return folderService.getFoldersByFolderID(userID, folderID);
        } catch (Exception e) {
            return ResultUtil.error(500, "获取失败");
        }
    }

    @PostMapping
    public Result addFolderByPath(@RequestHeader("Authorization") String token, @RequestBody JSONObject jsonObject) {
        try {
            Integer userID = jwtTokenProvider.getUserid(token.split(" ")[1].trim());
            String parentPath = jsonObject.getString("parentPath");
            String folderName = jsonObject.getString("folderName");
            return folderService.addFolderByPath(userID, parentPath, folderName);
        } catch (Exception e) {
            return ResultUtil.error(500, "添加失败");
        }
    }

    @DeleteMapping("/{folderID}")
    public Result deleteFolder(@RequestHeader("Authorization") String token, @PathVariable("folderID") Integer folderID) {
        try {
            Integer userID = jwtTokenProvider.getUserid(token.split(" ")[1].trim());
            return folderService.deleteFolder(userID, folderID);
        } catch (Exception e) {
            return ResultUtil.error(500, "删除失败");
        }
    }

}
