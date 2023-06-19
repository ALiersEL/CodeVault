package com.aliersel.codevaultbackend.service.intf;

import com.aliersel.codevaultbackend.controller.entity.CategoryWithCounts;
import com.aliersel.codevaultbackend.controller.entity.ProblemWithTags;
import com.aliersel.codevaultbackend.entity.*;
import com.aliersel.codevaultbackend.utils.Result;
import com.github.pagehelper.Page;

import java.util.List;

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
    Result<Page<ProblemWithTags>> getProblemsByUserID(Integer userID);
    Result<List<CategoryWithCounts>> getCategoriesByUserID(Integer userID);
    Result<Integer> addCategory(Integer userID, String tagName);
    Result updateCategory(Integer tagID, String tagName);
    Result deleteCategory(Integer tagID);
}
