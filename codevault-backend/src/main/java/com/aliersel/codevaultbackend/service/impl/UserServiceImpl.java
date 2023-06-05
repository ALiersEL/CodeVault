package com.aliersel.codevaultbackend.service.impl;

import com.aliersel.codevaultbackend.entity.*;
import com.aliersel.codevaultbackend.mapper.UserMapper;
import com.aliersel.codevaultbackend.security.JwtTokenProvider;
import com.aliersel.codevaultbackend.service.intf.UserService;
import com.aliersel.codevaultbackend.utils.Result;
import com.aliersel.codevaultbackend.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public Result register(User user) {
        if(userMapper.findByUserName(user.getUserName()) != null){
            return ResultUtil.error(400,"User already exists");
        }
        try {
            String hash = user.getPasswordHash();
            // 使用Bcrypt加密
            user.setPasswordHash(passwordEncoder.encode(hash));
            userMapper.addUser(user);
            return ResultUtil.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(400, "Register failed");
        }
    }

    @Override
    public Result login(User user) {
//        System.out.println(user);
        User realUser = userMapper.findByUserName(user.getUserName());
        System.out.println(realUser);
        if (realUser == null) {
            return ResultUtil.error(400, "User not found");
        }
        // System.out.println(realUser);
        if (!passwordEncoder.matches(user.getPasswordHash(), realUser.getPasswordHash())) {
            return ResultUtil.error(400, "Password is wrong");
        }
        try {
            // 生成并返回JWT
            HashMap<String, Object> map = new HashMap<>();
            String token = jwtTokenProvider.createToken(user.getUserName(), realUser.getUserID(), Collections.singletonList(realUser.getRole()));
            map.put("expires", jwtTokenProvider.getExpireTime(token));
            map.put("token", token);
            // System.out.println(map);
            return ResultUtil.success(map);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Login failed");
            return ResultUtil.error(400, "Login failed");
        }
    }

    @Override
    public Result logout(User user) {
        return ResultUtil.success();
    }

    @Override
    public Result<Integer> addCompany(Company company) {
        try {
            userMapper.addCompany(company);
            return ResultUtil.success(company.getCompanyID());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(400, "Add company failed");
        }
    }

    @Override
    public Result<Integer> addDepartment(Department department) {
        try {
            userMapper.addDepartment(department);
            return ResultUtil.success(department.getDepartmentID());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(400, "Add department failed");
        }
    }

    @Override
    public Result<Integer> addPost(Post post) {
        try {
            userMapper.addPost(post);
            return ResultUtil.success(post.getPostID());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(400, "Add post failed");
        }
    }

    @Override
    public Result<Integer> addCategory(Category category) {
        try {
            userMapper.addCategory(category);
            return ResultUtil.success(category.getCategoryID());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(400, "Add category failed");
        }
    }

    @Override
    public Result<Integer> findCompanyID(String companyName, Integer userID) {
        try {
            return ResultUtil.success(userMapper.findCompanyIDByName(companyName, userID));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(400, "Find company ID failed");
        }
    }

    @Override
    public Result<Integer> findDepartmentID(String departmentName, Integer companyID) {
        try {
            return ResultUtil.success(userMapper.findDepartmentIDByName(departmentName, companyID));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(400, "Find department ID failed");
        }
    }
}
