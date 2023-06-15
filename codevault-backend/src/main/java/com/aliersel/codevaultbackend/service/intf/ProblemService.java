package com.aliersel.codevaultbackend.service.intf;

import com.aliersel.codevaultbackend.entity.Code;
import com.aliersel.codevaultbackend.entity.Note;
import com.aliersel.codevaultbackend.entity.Problem;
import com.aliersel.codevaultbackend.utils.Result;

import java.util.List;

public interface ProblemService {
    Result<Integer> addProblem(Problem problem);
    Result findByProblemID(Integer problemID);
    Result deleteByProblemID(Integer problemID);
    Result addCompanies(Integer problemID, List<Integer> companyIDs);
    Result addDepartments(Integer problemID, List<Integer> departmentIDs);
    Result addPosts(Integer problemID, List<Integer> postIDs);
    Result addCategory(Integer problemID, Integer categoryID);
    Result addCategories(Integer problemID, List<Integer> categoryIDs);
    Result addCode(Code code, Integer problemID);
    Result findCodesByProblemID(Integer problemID);
    Result findCodeByCodeID(Integer codeID);
    Result addNote(Note note, Integer problemID);
    Result findNotesByProblemID(Integer problemID);
    Result findNoteByNoteID(Integer noteID);
}
