package com.aliersel.codevaultbackend.service.impl;

import com.aliersel.codevaultbackend.entity.User;
import com.aliersel.codevaultbackend.mapper.UserMapper;
import com.aliersel.codevaultbackend.service.intf.UserService;
import com.aliersel.codevaultbackend.utils.Result;
import com.aliersel.codevaultbackend.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Result register(User user) {
        if(userMapper.selectByUserName(user.getUserName()) != null){
            return ResultUtil.error(400,"User already exists");
        }
        String hash = user.getPasswordHash();
        // 使用Bcrypt加密
        user.setPasswordHash(passwordEncoder.encode(hash));
        userMapper.insert(user);
        return ResultUtil.success();
    }

    @Override
    public Result login(User user) {
        System.out.println(user);
        User realUser = userMapper.selectByUserName(user.getUserName());
        if (realUser == null) {
            return ResultUtil.error(400, "User not found");
        }
        System.out.println(realUser);
        if (!passwordEncoder.matches(user.getPasswordHash(), realUser.getPasswordHash())) {
            return ResultUtil.error(400, "Password is wrong");
        }
        return ResultUtil.success();
    }
}
