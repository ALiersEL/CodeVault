package com.aliersel.codevaultbackend.mapper;

import com.aliersel.codevaultbackend.entity.Category;
import com.aliersel.codevaultbackend.entity.Code;
import com.aliersel.codevaultbackend.entity.Note;
import com.aliersel.codevaultbackend.entity.Problem;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ProblemMapper {
    @Insert("INSERT INTO problem" +
            "(problem_title,problem_content,problem_type,difficulty,status,mastery,user_id) " +
            "VALUES(#{problemTitle}, #{problemContent}, #{problemType}, #{difficulty}, #{status}, #{mastery}, #{userID})" +
            "RETURNING problem_id"
    )
    @Options(useGeneratedKeys = true, keyProperty = "problemID", keyColumn = "problem_id")
    void addProblem(Problem problem);

    @Select("SELECT * " +
            "FROM problem " +
            "WHERE problem_id = #{problemID}")
    Problem findByProblemID(Integer problemID);

    @Delete("DELETE FROM problem " +
            "WHERE problem_id = #{problemID}")
    Boolean deleteByProblemID(Integer problemID);

    @Insert("INSERT INTO problem_company" +
            "(problem_id,company_id) " +
            "VALUES(#{problemID}, #{companyID})"
    )
    Boolean addCompany(Integer problemID, Integer companyID);

    @Insert("INSERT INTO problem_department" +
            "(problem_id,department_id) " +
            "VALUES(#{problemID}, #{departmentID})"
    )
    Boolean addDepartment(Integer problemID, Integer departmentID);

    @Insert("INSERT INTO problem_post" +
            "(problem_id,post_id) " +
            "VALUES(#{problemID}, #{postID})"
    )
    Boolean addPost(Integer problemID, Integer postID);

    @Insert("INSERT INTO problem_category" +
            "(problem_id,category_id) " +
            "VALUES(#{problemID}, #{categoryID})")
    Boolean addCategory(Integer problemID, Integer categoryID);

    @Select("SELECT * " +
            "FROM problem_category " +
            "WHERE problem_id = #{problemID} " +
            "INNER JOIN category ON problem_category.category_id = category.category_id")
    List<Category> findCategoriesByProblemID(Integer problemID);

    @Insert("INSERT INTO code" +
            "(code_text,code_language,problem_id) " +
            "VALUES(#{codeText}, #{codeLanguage}, #{problemID})")
    Boolean addCode(Code code, Integer problemID);

    @Select("SELECT * " +
            "FROM code " +
            "WHERE problem_id = #{problemID}")
    List<Code> findCodesByProblemID(Integer problemID);

    @Select("SELECT * " +
            "FROM code " +
            "WHERE code_id = #{codeID}")
    Code findCodeByCodeID(Integer codeID);

    @Insert("INSERT INTO note" +
            "(note_text,problem_id) " +
            "VALUES(#{noteText}, #{problemID})")
    Boolean addNote(Note note, Integer problemID);

    @Select("SELECT * " +
            "FROM note " +
            "WHERE problem_id = #{problemID}")
    List<Note> findNotesByProblemID(Integer problemID);

    @Select("SELECT * " +
            "FROM note " +
            "WHERE note_id = #{noteID}")
    Note findNoteByNoteID(Integer noteID);
}
