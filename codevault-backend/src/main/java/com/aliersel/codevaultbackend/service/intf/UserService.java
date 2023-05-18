package com.aliersel.codevaultbackend.service.intf;

import com.aliersel.codevaultbackend.entity.User;
import com.aliersel.codevaultbackend.utils.Result;

public interface UserService {
    Result register(User user);
    Result login(User user);
}
