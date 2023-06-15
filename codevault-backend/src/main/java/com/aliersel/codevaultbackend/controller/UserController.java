package com.aliersel.codevaultbackend.controller;

import com.aliersel.codevaultbackend.entity.User;
import com.aliersel.codevaultbackend.service.intf.UserService;
import com.aliersel.codevaultbackend.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

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
    public Result getProblems(@RequestParam("userID") Integer userID) {
        return userService.getProblemsByUserID(userID);
    }
}
