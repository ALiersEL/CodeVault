package com.aliersel.codevaultbackend.service.intf;

import com.aliersel.codevaultbackend.entity.*;
import com.aliersel.codevaultbackend.utils.Result;

public interface UserService {
    Result register(User user);
    Result login(User user);
    Result logout(User user);
    Result<Integer> addCompany(Company company);
    Result<Integer> addDepartment(Department department);
    Result<Integer> addPost(Post post);
    Result<Integer> addCategory(Category category);
    Result<Integer> findCompanyID(String companyName, Integer userID);
    Result<Integer> findDepartmentID(String departmentName, Integer companyID);
    Result getProblemsByUserID(Integer userID);
}
