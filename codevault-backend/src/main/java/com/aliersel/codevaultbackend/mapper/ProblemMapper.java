package com.aliersel.codevaultbackend.mapper;

import com.aliersel.codevaultbackend.entity.Problem;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

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

    @Select("SELECT * FROM problem WHERE problem_id = #{problemID}")
    Problem findByProblemID(Integer problemID);

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
}
