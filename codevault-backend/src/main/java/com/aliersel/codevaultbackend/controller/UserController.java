package com.aliersel.codevaultbackend.controller;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.aliersel.codevaultbackend.controller.entity.CompanyWithCounts;
import com.aliersel.codevaultbackend.entity.Company;
import com.aliersel.codevaultbackend.entity.User;
import com.aliersel.codevaultbackend.security.JwtTokenProvider;
import com.aliersel.codevaultbackend.service.intf.UserService;
import com.aliersel.codevaultbackend.utils.Result;
import com.aliersel.codevaultbackend.utils.ResultUtil;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

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
    public Result getProblems(@RequestHeader("Authorization") String token, @RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = "10") int pageSize) {
        try {
            Integer userID = jwtTokenProvider.getUserid(token.split(" ")[1].trim());
            PageHelper.startPage(pageNo, pageSize);
            return userService.getProblemsByUserID(userID);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(500, "服务器内部错误");
        }
    }

    @PostMapping("/problemset/filtered")
    public Result getFilteredProblems(@RequestHeader("Authorization") String token, @RequestBody JSONObject jsonObject) {
        try {
            Integer userID = jwtTokenProvider.getUserid(token.split(" ")[1].trim());
            System.out.println(jsonObject);
            Integer type = jsonObject.getInteger("selectedType");
            Integer difficulty = jsonObject.getInteger("selectedDifficulty");
            Boolean status = jsonObject.getBoolean("selectedStatus");
            // selectedTags为空，则tags为null，否则为List<String>
            JSONArray selectedTags = jsonObject.getJSONArray("selectedTags");
            List<Integer> tagIDs = selectedTags == null ? null : selectedTags.toJavaList(Integer.class);
            String keyword = jsonObject.getString("searchKey");
            return ResultUtil.success(userService.getFilteredProblemsByUserID(userID, type, difficulty, status, tagIDs, keyword));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(500, "服务器内部错误");
        }
    }

    @GetMapping("/tags")
    public Result getCategories(@RequestHeader("Authorization") String token) {
        try {
            Integer userID = jwtTokenProvider.getUserid(token.split(" ")[1].trim());
            return userService.getCategoriesByUserID(userID);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(500, "服务器内部错误");
        }
    }

    @PostMapping("/tags")
    public Result<Integer> addCategory(@RequestHeader("Authorization") String token, @RequestBody JSONObject jsonObject) {
        try {
            Integer userID = jwtTokenProvider.getUserid(token.split(" ")[1].trim());
            String tagName = jsonObject.getString("name");
            return userService.addCategory(userID, tagName);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(500, "服务器内部错误");
        }
    }

    @PutMapping("/tags/{tagID}")
    public Result updateCategory(@PathVariable Integer tagID, @RequestBody JSONObject jsonObject) {
        try {
            String tagName = jsonObject.getString("name");
            return userService.updateCategory(tagID, tagName);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(500, "服务器内部错误");
        }
    }

    @DeleteMapping("/tags/{tagID}")
    public Result deleteTag(@PathVariable Integer tagID) {
        try {
            return userService.deleteCategory(tagID);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(500, "服务器内部错误");
        }
    }

    @GetMapping("/companies")
    public Result<List<CompanyWithCounts>> getCompanies(@RequestHeader("Authorization") String token) {
        try {
            Integer userID = jwtTokenProvider.getUserid(token.split(" ")[1].trim());
            return userService.getCompaniesByUserID(userID);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(500, "服务器内部错误");
        }
    }

    @PostMapping("/companies")
    public Result addCompany(@RequestHeader("Authorization") String token, @RequestBody JSONObject jsonObject) {
        try {
            Integer userID = jwtTokenProvider.getUserid(token.split(" ")[1].trim());
            Company company = new Company();
            company.setCompanyName(jsonObject.getString("name"));
            company.setUserID(userID);
            return userService.addCompany(company);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(500, "服务器内部错误");
        }
    }

    @PutMapping("/companies/{companyID}")
    public Result updateCompany(@PathVariable Integer companyID, @RequestBody JSONObject jsonObject) {
        try {
            Company company = new Company();
            company.setCompanyID(companyID);
            company.setCompanyName(jsonObject.getString("name"));
            return userService.updateCompany(company);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(500, "服务器内部错误");
        }
    }

    @DeleteMapping("/companies/{companyID}")
    public Result deleteCompany(@PathVariable Integer companyID) {
        try {
            return userService.deleteCompany(companyID);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(500, "服务器内部错误");
        }
    }

    @GetMapping("/departments")
    public Result getDepartments(@RequestParam Integer companyID) {
        try {
            System.out.println(companyID);
            return userService.getDepartmentsByCompanyID(companyID);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(500, "服务器内部错误");
        }
    }

    @GetMapping("/posts")
    public Result getPosts(@RequestParam Integer departmentID) {
        try {
            return userService.getPostsByDepartmentID(departmentID);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(500, "服务器内部错误");
        }
    }

}
