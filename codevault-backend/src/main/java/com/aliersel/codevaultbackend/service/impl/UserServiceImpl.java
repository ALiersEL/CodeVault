package com.aliersel.codevaultbackend.service.impl;

import com.aliersel.codevaultbackend.entity.User;
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
            userMapper.insert(user);
            return ResultUtil.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(400, "Register failed");
        }
    }

    @Override
    public Result login(User user) {
        System.out.println(user);
        User realUser = userMapper.findByUserName(user.getUserName());
        if (realUser == null) {
            return ResultUtil.error(400, "User not found");
        }
        // System.out.println(realUser);
        if (!passwordEncoder.matches(user.getPasswordHash(), realUser.getPasswordHash())) {
            return ResultUtil.error(400, "Password is wrong");
        }
        try {
            // 生成并返回JWT
            HashMap<String, String> map = new HashMap<>();
            map.put("token", jwtTokenProvider.createToken(user.getUserName(), realUser.getUserID(), Collections.singletonList(realUser.getRole())));
            // System.out.println(map);
            return ResultUtil.success(map);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(400, "Login failed");
        }
    }
}
