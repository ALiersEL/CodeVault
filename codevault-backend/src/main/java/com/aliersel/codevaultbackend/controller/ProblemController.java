package com.aliersel.codevaultbackend.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.aliersel.codevaultbackend.entity.*;
import com.aliersel.codevaultbackend.security.JwtTokenProvider;
import com.aliersel.codevaultbackend.service.intf.ProblemService;
import com.aliersel.codevaultbackend.service.intf.UserService;
import com.aliersel.codevaultbackend.utils.Result;
import com.aliersel.codevaultbackend.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/problems")
public class ProblemController {
    @Autowired
    private UserService userService;
    @Autowired
    private ProblemService problemService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/add")
    // 从header中获取token，从token中获取userID，从requestBody中获取problem
    public Result add(@RequestHeader("Authorization") String token, @RequestBody JSONObject problemRequest) {
        Integer userID = jwtTokenProvider.getUserid(token.split(" ")[1].trim());
        System.out.println(problemRequest);
        // 获得tags中value为"-1"的元素的label作为labelList, 其余value不为"-1"的元素的value作为valueList
        JSONArray tags = problemRequest.getJSONArray("tags");
        List<String> labelList = new ArrayList<>(); // 用于存放新增的标签，只需存name
        List<Integer> valueList = new ArrayList<>(); // 用于存放已有的标签，需存id
        for (int i = 0; i < tags.size(); i++) {
            JSONObject jsonObject = tags.getJSONObject(i);
            String label = jsonObject.getString("label");
            Integer value = jsonObject.getInteger("value");
            if (value == -1) {
                labelList.add(label);
            } else {
                valueList.add(value);
            }
        }

        JSONArray sources = problemRequest.getJSONArray("sources");
        // sourceID和sourceType组成的set，以便插入problem_company等表
        Map<Integer, Set<Integer>> objectMap = new HashMap<>();
        Set<String> companyNames = new HashSet<>();
        Set<String> companyDepartmentPairs = new HashSet<>();
        Set<String> companyDepartmentPostTriples = new HashSet<>();

        for (int i = 0; i < sources.size(); i++) {
            JSONObject jsonObject = sources.getJSONObject(i);
            Integer sourceType = null ;
            Integer sourceID = null;
            String sourcePath = "";

            JSONObject company = jsonObject.getJSONObject("company");
            if (!company.toString().equals("{}")) {
                // Process company
                String companyName = company.getString("companyName");
                Integer companyID = company.getInteger("companyID");
                sourcePath += companyName;
                if (companyID == -1 && !companyNames.contains(sourcePath)) {
                    companyNames.add(sourcePath);
                    Company company1 = new Company();
                    company1.setCompanyName(companyName);
                    company1.setUserID(userID);
                    companyID = userService.addCompany(company1).getData();
                }
                sourceType = 1;
                sourceID = companyID;

                JSONObject department = jsonObject.getJSONObject("department");
                if (!department.toString().equals("{}")) {
                    // Process department
                    String departmentName = department.getString("departmentName");
                    Integer departmentID = department.getInteger("departmentID");
                    sourcePath += "/" + departmentName;
                    if (departmentID == -1 && !companyDepartmentPairs.contains(sourcePath)) {
                        companyDepartmentPairs.add(sourcePath);
                        if (companyID == -1) {
                            companyID = userService.findCompanyID(companyName, userID).getData();
                        }
                        Department department1 = new Department();
                        department1.setDepartmentName(departmentName);
                        department1.setCompanyID(companyID);
                        departmentID = userService.addDepartment(department1).getData();
                    }
                    sourceType = 2;
                    sourceID = departmentID;

                    JSONObject post = jsonObject.getJSONObject("post");
                    if (!post.toString().equals("{}")) {
                        // Process post
                        String postName = post.getString("postName");
                        Integer postID = post.getInteger("postID");
                        sourcePath += "/" + postName;
                        if (postID == -1 && !companyDepartmentPostTriples.contains(sourcePath)) {
                            companyDepartmentPostTriples.add(sourcePath);
                            if (departmentID == -1) {
                                departmentID = userService.findDepartmentID(departmentName, companyID).getData();
                            }
                            Post post1 = new Post();
                            post1.setPostName(postName);
                            post1.setDepartmentID(departmentID);
                            postID = userService.addPost(post1).getData();
                        }
                        sourceType = 3;
                        sourceID = postID;
                    }
                }
            }

            // 如果sourceType不为null
            if (sourceType != null){ // 检查sourceType是否已存在于objectMap中
                Set<Integer> sourceIDs = objectMap.computeIfAbsent(sourceType, k -> new HashSet<>());
                // sourceType不存在，创建对应的Set并插入sourceID
                // 将sourceID插入到对应的Set中
                sourceIDs.add(sourceID);
            }
        }
        System.out.println("objectMap: " + objectMap);

        Problem problem = new Problem();
        problem.setProblemTitle(problemRequest.getString("problemTitle"));
        problem.setProblemContent(problemRequest.getString("problemContent"));
        problem.setStatus(problemRequest.getBoolean("status"));
        problem.setMastery(problemRequest.getInteger("mastery"));
        problem.setDifficulty(problemRequest.getInteger("difficulty"));
        problem.setProblemType(problemRequest.getInteger("problemType"));
        problem.setUserID(userID);
        System.out.println(problem);
        Integer problemID = problemService.addProblem(problem).getData();

        System.out.println("problemID: " + problemID);

        // insert into tables based on objectMap
        for (Map.Entry<Integer, Set<Integer>> entry : objectMap.entrySet()) {
            Integer sourceType = entry.getKey();
            List<Integer> sourceIDs = new ArrayList<>(entry.getValue());
            switch (sourceType) {
                case 1:
                    problemService.addCompanies(problemID, sourceIDs);
                    break;
                case 2:
                    problemService.addDepartments(problemID, sourceIDs);
                    break;
                case 3:
                    problemService.addPosts(problemID, sourceIDs);
                    break;
            }
        }

        // insert into problem_category based on labelList and valueList
        problemService.addCategories(problemID, valueList);
        for (String label: labelList) {
            Category category = new Category();
            category.setCategoryName(label);
            category.setUserID(userID);
            Integer categoryID = userService.addCategory(category).getData();
            problemService.addCategory(problemID, categoryID);
        }

        return ResultUtil.success();
    }

    @GetMapping("/{ProblemID}")
    public Result get(@PathVariable Integer ProblemID) {
        return problemService.findByProblemID(ProblemID);
    }
}
