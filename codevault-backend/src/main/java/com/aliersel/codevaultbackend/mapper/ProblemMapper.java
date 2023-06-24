package com.aliersel.codevaultbackend.mapper;

import com.aliersel.codevaultbackend.controller.api.Source;
import com.aliersel.codevaultbackend.entity.*;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ProblemMapper {
    @Insert("INSERT INTO problem " +
            "(problem_title,problem_content,problem_type,difficulty,status,mastery,user_id,folder_id) " +
            "VALUES(#{problemTitle}, #{problemContent}, #{problemType}, #{difficulty}, #{status}, #{mastery}, #{userID}, #{folderID}) " +
            "RETURNING problem_id")
    @Options(useGeneratedKeys = true, keyProperty = "problemID", keyColumn = "problem_id")
    void saveProblem(Problem problem);

    @Update("UPDATE problem " +
            "SET (problem_title,problem_content,problem_type,difficulty,status,mastery,user_id) = " +
            "(#{problemTitle}, #{problemContent}, #{problemType}, #{difficulty}, #{status}, #{mastery}, #{userID}) " +
            "WHERE problem_id = #{problemID}")
    void updateProblem(Problem problem);

    @Select("SELECT * " +
            "FROM problem " +
            "WHERE problem_id = #{problemID}")
    Problem findByProblemID(Integer problemID);

    @Delete("DELETE FROM problem " +
            "WHERE problem_id = #{problemID}")
    Boolean deleteByProblemID(Integer problemID);

    @Insert("INSERT INTO problem_company " +
            "(problem_id,company_id) " +
            "VALUES(#{problemID}, #{companyID})")
    Boolean saveCompany(Integer problemID, Integer companyID);

    @Delete("DELETE FROM problem_company " +
            "WHERE problem_id = #{problemID} " +
            "AND company_id = #{companyID}")
    Boolean deleteCompany(Integer problemID, Integer companyID);

    @Select("SELECT company_id " +
            "FROM problem_company " +
            "WHERE problem_id = #{problemID}")
    List<Integer> findCompanyIDsByProblemID(Integer problemID);

    @Insert("INSERT INTO problem_department " +
            "(problem_id,department_id) " +
            "VALUES(#{problemID}, #{departmentID})")
    Boolean saveDepartment(Integer problemID, Integer departmentID);

    @Delete("DELETE FROM problem_department " +
            "WHERE problem_id = #{problemID} " +
            "AND department_id = #{departmentID}")
    Boolean deleteDepartment(Integer problemID, Integer departmentID);

    @Select("SELECT department_id " +
            "FROM problem_department " +
            "WHERE problem_id = #{problemID}")
    List<Integer> findDepartmentIDsByProblemID(Integer problemID);

    @Insert("INSERT INTO problem_post " +
            "(problem_id,post_id) " +
            "VALUES(#{problemID}, #{postID})")
    Boolean savePost(Integer problemID, Integer postID);

    @Delete("DELETE FROM problem_post " +
            "WHERE problem_id = #{problemID} " +
            "AND post_id = #{postID}")
    Boolean deletePost(Integer problemID, Integer postID);

    @Select("SELECT post_id " +
            "FROM problem_post " +
            "WHERE problem_id = #{problemID}")
    List<Integer> findPostIDsByProblemID(Integer problemID);

    @Select("SELECT company_name, company.company_id, NULL AS department_name, CAST(NULL AS INTEGER) AS department_id, NULL AS post_name, CAST(NULL AS INTEGER) AS post_id " +
            "FROM problem_company " +
            "INNER JOIN company ON problem_company.company_id = company.company_id " +
            "WHERE problem_company.problem_id = #{problemID} " +
            "UNION " +
            "SELECT company_name, company.company_id, department_name, department.department_id, NULL AS post_name , CAST(NULL AS INTEGER) AS post_id " +
            "FROM problem_department " +
            "INNER JOIN department ON problem_department.department_id = department.department_id " +
            "INNER JOIN company ON department.company_id = company.company_id " +
            "WHERE problem_department.problem_id = #{problemID} " +
            "UNION " +
            "SELECT company_name, company.company_id, department_name, department.department_id, post_name, post.post_id " +
            "FROM problem_post " +
            "INNER JOIN post ON problem_post.post_id = post.post_id " +
            "INNER JOIN department ON post.department_id = department.department_id " +
            "INNER JOIN company ON department.company_id = company.company_id " +
            "WHERE problem_post.problem_id = #{problemID}")
    List<Source> findSourcesByProblemID(Integer problemID);

    @Insert("INSERT INTO problem_category " +
            "(problem_id,category_id) " +
            "VALUES(#{problemID}, #{categoryID})")
    Boolean saveCategory(Integer problemID, Integer categoryID);

    @Delete("DELETE FROM problem_category " +
            "WHERE problem_id = #{problemID} AND category_id = #{categoryID}")
    Boolean deleteCategory(Integer problemID, Integer categoryID);

    @Select("SELECT category_name, category.category_id " +
            "FROM problem_category " +
            "INNER JOIN category ON problem_category.category_id = category.category_id " +
            "WHERE problem_category.problem_id = #{problemID}")
    List<Category> findCategoriesByProblemID(Integer problemID);

    @Select("SELECT category_id " +
            "FROM problem_category " +
            "WHERE problem_id = #{problemID}")
    List<Integer> findCategoryIDsByProblemID(Integer problemID);

    @Insert("INSERT INTO code " +
            "(code_text,code_language,problem_id) " +
            "VALUES(#{codeText}, #{codeLanguage}, #{problemID})")
    Boolean saveCode(Code code);

    @Select("SELECT * " +
            "FROM code " +
            "WHERE problem_id = #{problemID}")
    List<Code> findCodesByProblemID(Integer problemID);

    @Select("SELECT * " +
            "FROM code " +
            "WHERE code_id = #{codeID}")
    Code findCodeByCodeID(Integer codeID);

    @Insert("INSERT INTO note " +
            "(note_text,problem_id) " +
            "VALUES(#{noteText}, #{problemID})")
    Boolean saveNote(Note note);

    @Select("SELECT * " +
            "FROM note " +
            "WHERE problem_id = #{problemID}")
    List<Note> findNotesByProblemID(Integer problemID);

    @Select("SELECT * " +
            "FROM note " +
            "WHERE note_id = #{noteID}")
    Note findNoteByNoteID(Integer noteID);

    @Update("UPDATE problem " +
            "SET problem_title = #{newName} " +
            "WHERE problem_id = #{problemID}")
    Boolean updateProblemName(Integer problemID, String newName);

    @Update("UPDATE problem " +
            "SET folder_id = #{targetfolderID} " +
            "WHERE problem_id = #{problemID}")
    Boolean updateProblemFolder(Integer problemID, Integer targetfolderID);
}
