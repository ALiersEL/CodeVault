package com.aliersel.codevaultbackend.controller;

import com.alibaba.fastjson2.JSONObject;
import com.aliersel.codevaultbackend.constant.enums.CodeLanguage;
import com.aliersel.codevaultbackend.controller.api.Source;
import com.aliersel.codevaultbackend.entity.*;
import com.aliersel.codevaultbackend.security.JwtTokenProvider;
import com.aliersel.codevaultbackend.service.intf.FolderService;
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
    private FolderService folderService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping
    // 从header中获取token，从token中获取userID，从requestBody中获取problem
    public Result addProblem(@RequestHeader("Authorization") String token, @RequestBody JSONObject problemRequest) {
        try {
            Integer userID = jwtTokenProvider.getUserid(token.split(" ")[1].trim());
            // 如果folderPath为空，则默认为根目录，
            // 否则根据folderPath获取folderID
            Integer folderID = 1;
            if (problemRequest.getString("folderPath") != null) {
                String folderPath = problemRequest.getString("folderPath");
                folderID = folderService.getFolderIDByFolderPath(userID, folderPath).getData();
                System.out.println(folderID);
            }
            JSONObject jsonObject = problemService.processProblemRequest(token, problemRequest,folderID).getData();
            Problem problem = jsonObject.getObject("problem", Problem.class);
            List<String> labelList = jsonObject.getObject("labelList", List.class);
            List<Integer> valueList = jsonObject.getObject("valueList", List.class);
            Map<Integer, Set<Integer>> objectMap = jsonObject.getObject("objectMap", Map.class);
            // insert into tables based on objectMap
            Integer problemID = problemService.addProblem(problem).getData();
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
            return ResultUtil.success(problemID);
        }catch (Exception e) {
            return ResultUtil.error(3, "新增失败");
        }
    }

    @PutMapping("/{ProblemID}")
    public Result updateProblem(@RequestHeader("Authorization") String token, @RequestBody JSONObject problemRequest) {
        try{
            Integer folderID = 1;
            JSONObject jsonObject = problemService.processProblemRequest(token, problemRequest, folderID).getData();
            Integer userID = jsonObject.getInteger("userID");
            Problem problem = jsonObject.getObject("problem", Problem.class);
            List<String> labelList = jsonObject.getObject("labelList", List.class);
            List<Integer> valueList = jsonObject.getObject("valueList", List.class);
            Map<Integer, Set<Integer>> objectMap = jsonObject.getObject("objectMap", Map.class);
            // insert into tables based on objectMap
            problemService.updateProblem(problem);
            Integer problemID = problem.getProblemID();

            List<Integer> oldCompanyIDList = problemService.findCompanyIDsByProblemID(problemID).getData();
            List<Integer> oldDepartmentIDList = problemService.findDepartmentIDsByProblemID(problemID).getData();
            List<Integer> oldPostIDList = problemService.findPostIDsByProblemID(problemID).getData();

            for (Map.Entry<Integer, Set<Integer>> entry : objectMap.entrySet()) {
                Integer sourceType = entry.getKey();
                List<Integer> sourceIDs = new ArrayList<>(entry.getValue());
                switch (sourceType) {
                    case 1:
                        List<Integer> deleteCompanyIDList = new ArrayList<>(oldCompanyIDList);
                        deleteCompanyIDList.removeAll(sourceIDs);
                        List<Integer> addCompanyIDList = new ArrayList<>(sourceIDs);
                        addCompanyIDList.removeAll(oldCompanyIDList);
                        problemService.deleteCompanies(problemID, deleteCompanyIDList);
                        problemService.addCompanies(problemID, addCompanyIDList);
                        break;
                    case 2:
                        List<Integer> deleteDepartmentIDList = new ArrayList<>(oldDepartmentIDList);
                        deleteDepartmentIDList.removeAll(sourceIDs);
                        List<Integer> addDepartmentIDList = new ArrayList<>(sourceIDs);
                        addDepartmentIDList.removeAll(oldDepartmentIDList);
                        problemService.deleteDepartments(problemID, deleteDepartmentIDList);
                        problemService.addDepartments(problemID, addDepartmentIDList);
                        break;
                    case 3:
                        List<Integer> deletePostIDList = new ArrayList<>(oldPostIDList);
                        deletePostIDList.removeAll(sourceIDs);
                        List<Integer> addPostIDList = new ArrayList<>(sourceIDs);
                        addPostIDList.removeAll(oldPostIDList);
                        problemService.deletePosts(problemID, deletePostIDList);
                        problemService.addPosts(problemID, addPostIDList);
                        break;
                }
            }

            // 比较valueList和原有的idList
            // (原有的idList - valueList) -> 删除
            // (valueList - 原有的idList) -> 新增
            List<Integer> oldCategoryIDList =  problemService.findCategoryIDsByProblemID(problemID).getData();
            List<Integer> deleteCategoryIDList = new ArrayList<>(oldCategoryIDList);
            deleteCategoryIDList.removeAll(valueList);
            List<Integer> addCategoryIDList = new ArrayList<>(valueList);
            addCategoryIDList.removeAll(oldCategoryIDList);
            problemService.deleteCategories(problemID, deleteCategoryIDList);
            problemService.addCategories(problemID, addCategoryIDList);
            // user的新标签
            for (String label: labelList) {
                Category category = new Category();
                category.setCategoryName(label);
                category.setUserID(userID);
                Integer categoryID = userService.addCategory(category).getData();
                problemService.addCategory(problemID, categoryID);
            }
            return ResultUtil.success(problemID);
        }catch(Exception e) {
            return ResultUtil.error(3, "更新失败");
        }
    }

    @GetMapping("/{ProblemID}")
    public Result getProblem(@PathVariable Integer ProblemID) {
        // 返回Problem, 相关的类别和公司
        try{
            Problem problem = problemService.findByProblemID(ProblemID).getData();
            List<Category> categories = problemService.findCategoriesByProblemID(ProblemID).getData();
            List<Source> sources = problemService.findSourcesByProblemID(ProblemID).getData();
            System.out.println(sources);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("problem", problem);
            jsonObject.put("categories", categories);
            jsonObject.put("sources", sources);
            return ResultUtil.success(jsonObject);
        } catch (Exception e) {
            return ResultUtil.error(3, "ProblemID不存在");
        }
    }

    @DeleteMapping("/{ProblemID}")
    public Result delete(@PathVariable Integer ProblemID) {
        try {
            return problemService.deleteByProblemID(ProblemID);
        } catch (Exception e) {
            return ResultUtil.error(3, "删除失败");
        }
    }

    @PostMapping("/{ProblemID}/codes")
    public Result addCode(@PathVariable Integer ProblemID, @RequestBody JSONObject jsonObject) {
        Code code = new Code();
        code.setCodeText(jsonObject.getString("code"));
        // 将language转成大写后，用CodeLanguage枚举类获取language的数值
        code.setCodeLanguage(CodeLanguage.valueOf(jsonObject.getString("language").toUpperCase()).getValue());
        code.setProblemID(ProblemID);
        System.out.println(code);
        return problemService.addCode(code);
    }

    @GetMapping("/{ProblemID}/codes")
    public Result<List<Code>> getCodes(@PathVariable Integer ProblemID) {
        return problemService.findCodesByProblemID(ProblemID);
    }

    @GetMapping("/codes/{CodeID}")
    public Result<Code> getCode(@PathVariable Integer CodeID) {
        return problemService.findCodeByCodeID(CodeID);
    }

    @PostMapping("/{ProblemID}/notes")
    public Result addNote(@PathVariable Integer ProblemID, @RequestBody Note note) {
        note.setProblemID(ProblemID);
        System.out.println(note);
        return problemService.addNote(note);
    }

    @GetMapping("/{ProblemID}/notes")
    public Result<List<Note>> getNotes(@PathVariable Integer ProblemID) {
        return problemService.findNotesByProblemID(ProblemID);
    }

    @GetMapping("/notes/{NoteID}")
    public Result<Note> getNote(@PathVariable Integer NoteID) {
        System.out.println(NoteID);
        return problemService.findNoteByNoteID(NoteID);
    }

    @PutMapping("/{ProblemID}/name")
    public Result renameProblem(@PathVariable Integer ProblemID, @RequestBody JSONObject jsonObject) {
        String newName = jsonObject.getString("newName");
        return problemService.renameProblem(ProblemID, newName);
    }

    @PostMapping("/{ProblemID}/move")
    public Result moveProblem(@PathVariable Integer ProblemID, @RequestBody JSONObject jsonObject) {
        Integer targetFolderID = jsonObject.getInteger("targetFolderID");
        return problemService.moveProblem(ProblemID, targetFolderID);
    }
}
