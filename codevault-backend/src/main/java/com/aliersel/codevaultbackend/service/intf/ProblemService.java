package com.aliersel.codevaultbackend.service.intf;

import com.alibaba.fastjson2.JSONObject;
import com.aliersel.codevaultbackend.controller.entity.Source;
import com.aliersel.codevaultbackend.entity.Category;
import com.aliersel.codevaultbackend.entity.Code;
import com.aliersel.codevaultbackend.entity.Note;
import com.aliersel.codevaultbackend.entity.Problem;
import com.aliersel.codevaultbackend.utils.Result;

import java.util.List;

public interface ProblemService {
    Result<Integer> addProblem(Problem problem);
    Result<JSONObject> processProblemRequest(String token, JSONObject problemRequest, Integer folderID);
    Result updateProblem(Problem problem);
    Result<Problem> findByProblemID(Integer problemID);
    Result deleteByProblemID(Integer problemID);
    Result addCompanies(Integer problemID, List<Integer> companyIDs);
    Result deleteCompanies(Integer problemID, List<Integer> companyIDs);
    Result<List<Integer>> findCompanyIDsByProblemID(Integer problemID);
    Result addDepartments(Integer problemID, List<Integer> departmentIDs);
    Result deleteDepartments(Integer problemID, List<Integer> departmentIDs);
    Result<List<Integer>> findDepartmentIDsByProblemID(Integer problemID);
    Result addPosts(Integer problemID, List<Integer> postIDs);
    Result deletePosts(Integer problemID, List<Integer> postIDs);
    Result<List<Integer>> findPostIDsByProblemID(Integer problemID);
    Result<List<Source>> findSourcesByProblemID(Integer problemID);
    Result addCategory(Integer problemID, Integer categoryID);
    Result addCategories(Integer problemID, List<Integer> categoryIDs);
    Result deleteCategories(Integer problemID, List<Integer> categoryIDs);
    Result<List<Category>> findCategoriesByProblemID(Integer problemID);
    Result<List<Integer>> findCategoryIDsByProblemID(Integer problemID);
    Result addCode(Code code);
    Result findCodesByProblemID(Integer problemID);
    Result findCodeByCodeID(Integer codeID);
    Result addNote(Note note);
    Result findNotesByProblemID(Integer problemID);
    Result findNoteByNoteID(Integer noteID);
}
