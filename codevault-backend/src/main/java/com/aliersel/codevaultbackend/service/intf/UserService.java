package com.aliersel.codevaultbackend.service.intf;

import com.aliersel.codevaultbackend.controller.api.CategoryWithCounts;
import com.aliersel.codevaultbackend.controller.api.CompanyWithCounts;
import com.aliersel.codevaultbackend.controller.api.ProblemWithTags;
import com.aliersel.codevaultbackend.entity.*;
import com.aliersel.codevaultbackend.utils.Result;

import java.util.List;

public interface UserService {
    Result register(User user);
    Result login(User user);
    Result logout(User user);
    Result<Integer> addCompany(Company company);
    Result updateCompany(Company company);
    Result deleteCompany(Integer companyID);
    Result<Integer> addDepartment(Department department);
    Result<Integer> addPost(Post post);
    Result<Integer> addCategory(Category category);
    Result<Integer> findCompanyID(String companyName, Integer userID);
    Result<Integer> findDepartmentID(String departmentName, Integer companyID);
    Result<Integer> getProblemCountByUserID(Integer userID);
    Result<List<ProblemWithTags>> getProblemsByUserID(Integer userID, Integer problemTitleSort, Integer difficultySort, Integer masterySort, Integer lastModifiedSort, Integer offset, Integer pageSize);
    Result<List<ProblemWithTags>> getFilteredProblemsByUserID(Integer userID, Integer type, Integer difficulty, Boolean status, List<Integer> tagIDs, String keyword, Integer problemTitleSort, Integer difficultySort, Integer masterySort, Integer lastModifiedSort, Integer offset, Integer pageSize);
    Result<List<CategoryWithCounts>> getCategoriesByUserID(Integer userID);
    Result<Integer> addCategory(Integer userID, String tagName);
    Result updateCategory(Integer tagID, String tagName);
    Result deleteCategory(Integer tagID);
    Result<List<CompanyWithCounts>> getCompaniesByUserID(Integer userID);
    Result<List<Department>> getDepartmentsByCompanyID(Integer companyID);
    Result<List<Post>> getPostsByDepartmentID(Integer departmentID);
}
