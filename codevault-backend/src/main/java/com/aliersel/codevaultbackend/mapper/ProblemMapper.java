package com.aliersel.codevaultbackend.mapper;

import com.aliersel.codevaultbackend.entity.Problem;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProblemMapper {
    @Insert("INSERT INTO problem" +
            "(problem_title,problem_content,problem_type,difficulty,status,mastery,company_id,department_id,post_id,user_id) " +
            "VALUES(#{problemTitle}, CAST(#{problemContent} AS JSONB), #{problemType}, #{difficulty}, #{status}, #{mastery}, #{companyID}, #{departmentID}, #{postID}, #{userID})"
    )
    void insert(Problem problem);
}
