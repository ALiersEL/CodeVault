package com.aliersel.codevaultbackend.mapper;

import com.aliersel.codevaultbackend.controller.entity.CategoryWithCounts;
import com.aliersel.codevaultbackend.controller.entity.ProblemWithTags;
import com.aliersel.codevaultbackend.entity.*;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
    @Insert("INSERT INTO \"user\"" +
            "(user_name,password_hash,phone_number,email) " +
            "VALUES(#{userName}, #{passwordHash}, #{phoneNumber}, #{email})")
    Boolean saveUser(User user);

    @Select("SELECT * FROM \"user\" WHERE user_name = #{userName}")
    User findByUserName(String userName);

    @Insert("INSERT INTO company (company_name,user_id) VALUES(#{companyName},#{userID}) RETURNING company_id")
    @Options(useGeneratedKeys = true, keyProperty = "companyID", keyColumn = "company_id")
    void saveCompany(Company company);

    @Insert("INSERT INTO department (department_name,company_id) VALUES(#{departmentName},#{companyID}) RETURNING department_id")
    @Options(useGeneratedKeys = true, keyProperty = "departmentID", keyColumn = "department_id")
    void saveDepartment(Department department);

    @Insert("INSERT INTO post (post_name,department_id) VALUES(#{postName},#{departmentID}) RETURNING post_id")
    @Options(useGeneratedKeys = true, keyProperty = "postID", keyColumn = "post_id")
    void savePost(Post post);

    @Insert("INSERT INTO category (category_name,user_id) VALUES(#{categoryName},#{userID}) RETURNING category_id")
    @Options(useGeneratedKeys = true, keyProperty = "categoryID", keyColumn = "category_id")
    void saveCategory(Category category);

    @Select("SELECT company_id FROM company WHERE company_name = #{companyName} AND user_id = #{userID}")
    Integer findCompanyIDByName(String companyName, Integer userID);

    @Select("SELECT department_id FROM department WHERE department_name = #{departmentName} AND company_id = #{companyID}")
    Integer findDepartmentIDByName(String departmentName, Integer companyID);

    @Select("SELECT problem_id FROM problem WHERE user_id = #{userID}")
    List<Integer> findProblemIDsByUserID(Integer userID);

    @Select("SELECT p.problem_id, p.problem_title, p.problem_type, p.difficulty, p.status, p.last_modified, array_agg(c.category_name) AS tags " +
            "FROM problem AS p " +
            "LEFT JOIN problem_category AS pc " +
            "ON p.problem_id = pc.problem_id " +
            "LEFT JOIN category AS c " +
            "ON pc.category_id = c.category_id " +
            "WHERE p.user_id = #{userID} " +
            "GROUP BY p.problem_id")
    @Results({
            @Result(property = "problemID", column = "problem_id"),
            @Result(property = "problemTitle", column = "problem_title"),
            @Result(property = "problemType", column = "problem_type"),
            @Result(property = "difficulty", column = "difficulty"),
            @Result(property = "status", column = "status"),
            @Result(property = "lastModified", column = "last_modified"),
            @Result(property = "tags", column = "tags", typeHandler = com.aliersel.codevaultbackend.mapper.type_handler.ArrayTypeHandler.class)
    })
    Page<ProblemWithTags> findProblemsByUserID(Integer userID);

    @Select("SELECT c.category_id AS id, c.category_name AS name, COALESCE(pc.count, 0) AS count " +
            "FROM category AS c " +
            "LEFT JOIN ( " +
            "SELECT category_id, COUNT(problem_id) " +
            "OVER (PARTITION BY category_id) " +
            "FROM problem_category " +
            ") AS pc " +
            "ON c.category_id = pc.category_id " +
            "WHERE c.user_id = #{userID}")
    List<CategoryWithCounts> findCategoriesByUserID(Integer userID);

    @Update("UPDATE category SET category_name = #{categoryName} WHERE category_id = #{categoryID}")
    Boolean updateCategory(Integer categoryID, String categoryName);

    @Delete("DELETE FROM category WHERE category_id = #{categoryID}")
    Boolean deleteCategory(Integer categoryID);
}
