package com.aliersel.codevaultbackend.service.impl;

import com.aliersel.codevaultbackend.entity.User;
import com.aliersel.codevaultbackend.mapper.UserMapper;
import com.aliersel.codevaultbackend.service.intf.UserService;
import com.aliersel.codevaultbackend.utils.Result;
import com.aliersel.codevaultbackend.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;
    @Override
    public Result register(User user) {
        if(userMapper.selectByUserName(user.getUserName()) != null){
            return ResultUtil.error(400,"User already exists");
        }
        userMapper.insert(user);
        return ResultUtil.success();
    }

    @Override
    public Result login(User user) {
        System.out.println(user);
        User user1 = userMapper.selectByUserName(user.getUserName());
        if (user1 == null) {
            return ResultUtil.error(400, "User not found");
        }
        System.out.println(user1);
        if (!user1.getPasswordHash().equals(user.getPasswordHash())) {
            return ResultUtil.error(400, "Password is wrong");
        }
        return ResultUtil.success();
    }
}
