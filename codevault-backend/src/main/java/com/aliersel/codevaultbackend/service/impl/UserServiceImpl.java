package com.aliersel.codevaultbackend.service.impl;

import com.aliersel.codevaultbackend.controller.api.CategoryWithCounts;
import com.aliersel.codevaultbackend.controller.api.CompanyWithCounts;
import com.aliersel.codevaultbackend.controller.api.ProblemWithTags;
import com.aliersel.codevaultbackend.entity.*;
import com.aliersel.codevaultbackend.mapper.CompanyMapper;
import com.aliersel.codevaultbackend.mapper.UserMapper;
import com.aliersel.codevaultbackend.security.JwtTokenProvider;
import com.aliersel.codevaultbackend.service.intf.UserService;
import com.aliersel.codevaultbackend.utils.Result;
import com.aliersel.codevaultbackend.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private CompanyMapper companyMapper;

    @Override
    public Result register(User user) {
        if(userMapper.findByUserName(user.getUserName()) != null){
            return ResultUtil.error(400,"User already exists");
        }
        try {
            String hash = user.getPasswordHash();
            // 使用Bcrypt加密
            user.setPasswordHash(passwordEncoder.encode(hash));
            userMapper.saveUser(user);
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
//        System.out.println(realUser);
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
            userMapper.saveCompany(company);
            return ResultUtil.success(company.getCompanyID());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(400, "Add company failed");
        }
    }

    @Override
    public Result updateCompany(Company company) {
        try {
            userMapper.updateCompany(company);
            return ResultUtil.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(400, "Update company failed");
        }
    }

    @Override
    public Result deleteCompany(Integer companyID) {
        try {
            userMapper.deleteCompany(companyID);
            return ResultUtil.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(400, "Delete company failed");
        }
    }

    @Override
    public Result<Integer> addDepartment(Department department) {
        try {
            userMapper.saveDepartment(department);
            return ResultUtil.success(department.getDepartmentID());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(400, "Add department failed");
        }
    }

    @Override
    public Result<Integer> addPost(Post post) {
        try {
            userMapper.savePost(post);
            return ResultUtil.success(post.getPostID());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(400, "Add post failed");
        }
    }

    @Override
    public Result<Integer> addCategory(Category category) {
        try {
            userMapper.saveCategory(category);
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

    @Override
    public Result<Integer> getProblemCountByUserID(Integer userID) {
        try {
            return ResultUtil.success(userMapper.countProblemsByUserID(userID));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(400, "Get problem count failed");
        }
    }
    @Override
    public Result<List<ProblemWithTags>> getProblemsByUserID(Integer userID, Integer problemTitleSort, Integer difficultySort, Integer masterySort, Integer lastModifiedSort, Integer offset, Integer pageSize) {
        try {
            return ResultUtil.success(userMapper.findProblemsByUserID(userID, problemTitleSort, difficultySort, masterySort, lastModifiedSort, offset, pageSize));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(400, "Get problems failed");
        }
    }

    @Override
    public Result<List<ProblemWithTags>> getFilteredProblemsByUserID(Integer userID, Integer type, Integer difficulty, Boolean status, List<Integer> tagIDs, String keyword, Integer problemTitleSort, Integer difficultySort, Integer masterySort, Integer lastModifiedSort, Integer offset, Integer pageSize) {
        try {
            return ResultUtil.success(userMapper.findFilteredProblemsByUserID(userID, type, difficulty, status, tagIDs, keyword, problemTitleSort, difficultySort, masterySort, lastModifiedSort, offset, pageSize));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(400, "Get problems failed");
        }
    }

    @Override
    public Result<List<CategoryWithCounts>> getCategoriesByUserID(Integer userID) {
        try {
            return ResultUtil.success(userMapper.findCategoriesByUserID(userID));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(400, "Get categories failed");
        }
    }

    @Override
    public Result<Integer> addCategory(Integer userID, String categoryName) {
        try {
            Category category = new Category();
            category.setUserID(userID);
            category.setCategoryName(categoryName);
            userMapper.saveCategory(category);
            return ResultUtil.success(category.getCategoryID());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(400, "Add category failed");
        }
    }

    @Override
    public Result updateCategory(Integer categoryID, String categoryName) {
        try {
            userMapper.updateCategory(categoryID, categoryName);
            return ResultUtil.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(400, "Update category failed");
        }
    }

    @Override
    public Result deleteCategory(Integer categoryID) {
        try {
            userMapper.deleteCategory(categoryID);
            return ResultUtil.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(400, "Delete category failed");
        }
    }

    @Override
    public Result<List<CompanyWithCounts>> getCompaniesByUserID(Integer userID) {
        try {
            return ResultUtil.success(companyMapper.findCompaniesByUserID(userID));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(400, "Get companies failed");
        }
    }

    @Override
    public Result<List<Department>> getDepartmentsByCompanyID(Integer companyID) {
        try {
            return ResultUtil.success(companyMapper.findDepartmentsByCompanyID(companyID));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(400, "Get departments failed");
        }
    }

    @Override
    public Result<List<Post>> getPostsByDepartmentID(Integer departmentID) {
        try {
            return ResultUtil.success(companyMapper.findPostsByDepartmentID(departmentID));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(400, "Get posts failed");
        }
    }
}
