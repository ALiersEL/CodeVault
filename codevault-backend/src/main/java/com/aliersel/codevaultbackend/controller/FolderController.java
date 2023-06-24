package com.aliersel.codevaultbackend.controller;

import com.alibaba.fastjson2.JSONObject;
import com.aliersel.codevaultbackend.controller.api.FileWithTypes;
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
@RequestMapping("/folders")
public class FolderController {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private FolderService folderService;

    @GetMapping
    public Result<List<FileWithTypes>> getFolderContent(@RequestHeader("Authorization") String token,
                                                        @RequestParam(value = "path", required = false ) String path,
                                                        @RequestParam(value = "folderID", required = false) Integer folderID) {
         try {
             Integer userID = jwtTokenProvider.getUserid(token.split(" ")[1].trim());
             if (path != null) {
                 path = URLDecoder.decode(path, "UTF-8");
                 return folderService.getFilesByFolderPath(userID, path);
             } else if (folderID != null) {
                 System.out.println(folderID);
                 return folderService.getFilesByFolderID(userID, folderID);
             } else {
                 return ResultUtil.error(500, "获取失败");
             }
            } catch (Exception e) {
                return ResultUtil.error(500, "获取失败");
         }
    }

    // 只获取文件夹
    @GetMapping("/simple")
    public Result<List<Folder>> getAllFolderContent(@RequestParam(value = "folderID", required = true) Integer folderID) {
        try {
            System.out.println(folderID);
            System.out.println(folderService.getFoldersByFolderID(folderID));
            return folderService.getFoldersByFolderID(folderID);
        } catch (Exception e) {
            return ResultUtil.error(500, "获取失败");
        }
    }

    @GetMapping("/root")
    public Result<Integer> getRootFolderIDByUserID(@RequestHeader("Authorization") String token) {
        try {
            Integer userID = jwtTokenProvider.getUserid(token.split(" ")[1].trim());
            return folderService.getRootFolderIDByUserID(userID);
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

    @PutMapping("/{folderID}/name")
    public Result renameFolder(@PathVariable("folderID") Integer folderID, @RequestBody JSONObject jsonObject) {
        try {
            String newName = jsonObject.getString("newName");
            String newPath = jsonObject.getString("newPath");
            System.out.println(newName);
            System.out.println(newPath);
            return folderService.renameFolder(folderID, newName, newPath);
        } catch (Exception e) {
            return ResultUtil.error(500, "重命名失败");
        }
    }

    @PostMapping("/{folderID}/move")
    public Result moveFolder(@PathVariable("folderID") Integer folderID, @RequestBody JSONObject jsonObject) {
        try {
            Integer targetFolderID = jsonObject.getInteger("targetFolderID");
            return folderService.moveFolder(folderID, targetFolderID);
        } catch (Exception e) {
            return ResultUtil.error(500, "移动失败");
        }
    }
}
