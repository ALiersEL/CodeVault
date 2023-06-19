package com.aliersel.codevaultbackend.controller;

import com.alibaba.fastjson2.JSONObject;
import com.aliersel.codevaultbackend.entity.User;
import com.aliersel.codevaultbackend.security.JwtTokenProvider;
import com.aliersel.codevaultbackend.service.intf.UserService;
import com.aliersel.codevaultbackend.utils.Result;
import com.aliersel.codevaultbackend.utils.ResultUtil;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        return userService.register(user);
    }

    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        return userService.login(user);
    }

    @PostMapping("/logout")
    public Result logout(@RequestBody User user) {
        return userService.logout(user);
    }

    @GetMapping("/problemset")
    public Result getProblems(@RequestHeader("Authorization") String token, @RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = "3") int pageSize) {
        try {
            Integer userID = jwtTokenProvider.getUserid(token.split(" ")[1].trim());
            PageHelper.startPage(pageNo, pageSize);
            return userService.getProblemsByUserID(userID);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(500, "服务器内部错误");
        }
    }

    @GetMapping("/tags")
    public Result getCategories(@RequestHeader("Authorization") String token) {
        try {
            Integer userID = jwtTokenProvider.getUserid(token.split(" ")[1].trim());
            return userService.getCategoriesByUserID(userID);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(500, "服务器内部错误");
        }
    }

    @PostMapping("/tags")
    public Result<Integer> addCategory(@RequestHeader("Authorization") String token, @RequestBody JSONObject jsonObject) {
        try {
            Integer userID = jwtTokenProvider.getUserid(token.split(" ")[1].trim());
            String tagName = jsonObject.getString("name");
            return userService.addCategory(userID, tagName);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(500, "服务器内部错误");
        }
    }

    @PutMapping("/tags/{tagID}")
    public Result updateCategory(@PathVariable Integer tagID, @RequestBody JSONObject jsonObject) {
        try {
            String tagName = jsonObject.getString("name");
            return userService.updateCategory(tagID, tagName);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(500, "服务器内部错误");
        }
    }

    @DeleteMapping("/tags/{tagID}")
    public Result deleteTag(@PathVariable Integer tagID) {
        try {
            return userService.deleteCategory(tagID);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(500, "服务器内部错误");
        }
    }


}
