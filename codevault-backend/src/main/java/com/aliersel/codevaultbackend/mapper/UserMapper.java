package com.aliersel.codevaultbackend.mapper;

import com.aliersel.codevaultbackend.controller.api.CategoryWithCounts;
import com.aliersel.codevaultbackend.controller.api.ProblemWithTags;
import com.aliersel.codevaultbackend.entity.*;
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

    @Update("UPDATE company SET company_name = #{companyName} WHERE company_id = #{companyID}")
    Boolean updateCompany(Company company);

    @Delete("DELETE FROM company WHERE company_id = #{companyID}")
    Boolean deleteCompany(Integer companyID);

    @Select("SELECT company_id FROM company WHERE company_name = #{companyName} AND user_id = #{userID}")
    Integer findCompanyIDByName(String companyName, Integer userID);

    @Select("SELECT department_id FROM department WHERE department_name = #{departmentName} AND company_id = #{companyID}")
    Integer findDepartmentIDByName(String departmentName, Integer companyID);

    @Select("SELECT problem_id FROM problem WHERE user_id = #{userID}")
    List<Integer> findProblemIDsByUserID(Integer userID);

    @Select("SELECT COUNT(1) FROM problem WHERE user_id = #{userID}")
    Integer countProblemsByUserID(Integer userID);

    // 0: 无序，1：升序，2：降序
    @Select("<script> " +
            "SELECT p.problem_id, p.problem_title, p.problem_type, p.difficulty, p.status, p.last_modified, array_agg(c.category_name) AS tags, p.mastery " +
            "FROM problem AS p " +
            "LEFT JOIN problem_category AS pc " +
            "ON p.problem_id = pc.problem_id " +
            "LEFT JOIN category AS c " +
            "ON pc.category_id = c.category_id " +
            "WHERE p.user_id = #{userID} " +
            "GROUP BY p.problem_id " +
            "<if test='problemTitleSort == 1'>ORDER BY p.problem_title ASC</if> " +
            "<if test='problemTitleSort == 2'>ORDER BY p.problem_title DESC</if> " +
            "<if test='difficultySort == 1'>ORDER BY p.difficulty ASC</if> " +
            "<if test='difficultySort == 2'>ORDER BY p.difficulty DESC</if> " +
            "<if test='masterySort == 1'>ORDER BY p.mastery ASC</if> " +
            "<if test='masterySort == 2'>ORDER BY p.mastery DESC</if> " +
            "<if test='lastModifiedSort == 1'>ORDER BY p.last_modified ASC</if> " +
            "<if test='lastModifiedSort == 2'>ORDER BY p.last_modified DESC</if> " +
            "LIMIT #{pageSize} OFFSET #{offset} " +
            "</script>"
    )
    @Results({
            @Result(property = "problemID", column = "problem_id"),
            @Result(property = "problemTitle", column = "problem_title"),
            @Result(property = "problemType", column = "problem_type"),
            @Result(property = "difficulty", column = "difficulty"),
            @Result(property = "status", column = "status"),
            @Result(property = "lastModified", column = "last_modified"),
            @Result(property = "tags", column = "tags", typeHandler = com.aliersel.codevaultbackend.mapper.type_handler.ArrayTypeHandler.class),
            @Result(property = "mastery", column = "mastery")
    })
    List<ProblemWithTags> findProblemsByUserID(Integer userID, Integer problemTitleSort, Integer difficultySort, Integer masterySort, Integer lastModifiedSort, Integer offset, Integer pageSize);

    @Select("<script> " +
            "SELECT p.problem_id, p.problem_title, p.problem_type, p.difficulty, p.status, p.last_modified, " +
            "array_agg(c.category_name) AS tags, p.mastery " +
            "FROM problem AS p " +
            "LEFT JOIN problem_category AS pc " +
            "ON p.problem_id = pc.problem_id " +
            "LEFT JOIN category AS c " +
            "ON pc.category_id = c.category_id " +
            "WHERE p.user_id = #{userID} " +
            "<if test='type != null'>AND p.problem_type = #{type}</if> " +
            "<if test='difficulty != null'>AND p.difficulty = #{difficulty}</if> " +
            "<if test='status != null'>AND p.status = #{status}</if> " +
            "<if test='tagIDs != null and tagIDs.size() > 0'>AND c.category_id IN " +
            "<foreach item='item' collection='tagIDs' open='(' separator=',' close=')'> " +
            "#{item} " +
            "</foreach> " +
            "</if> " +
            "<if test='keyword != null'>AND (to_tsvector('zhcnsearch', p.problem_title || ' ' || p.problem_content) @@ to_tsquery('zhcnsearch', #{keyword}) " +
            "OR to_tsvector('english', p.problem_title || ' ' || p.problem_content) @@ to_tsquery('english', #{keyword}))</if> " +
            "GROUP BY p.problem_id " +
            "<if test='problemTitleSort == 1'>ORDER BY p.problem_title ASC</if> " +
            "<if test='problemTitleSort == 2'>ORDER BY p.problem_title DESC</if> " +
            "<if test='difficultySort == 1'>ORDER BY p.difficulty ASC</if> " +
            "<if test='difficultySort == 2'>ORDER BY p.difficulty DESC</if> " +
            "<if test='masterySort == 1'>ORDER BY p.mastery ASC</if> " +
            "<if test='masterySort == 2'>ORDER BY p.mastery DESC</if> " +
            "<if test='lastModifiedSort == 1'>ORDER BY p.last_modified ASC</if> " +
            "<if test='lastModifiedSort == 2'>ORDER BY p.last_modified DESC</if> " +
            "LIMIT #{pageSize} OFFSET #{offset} " +
            "</script>")
    @Results({
            @Result(property = "problemID", column = "problem_id"),
            @Result(property = "problemTitle", column = "problem_title"),
            @Result(property = "problemType", column = "problem_type"),
            @Result(property = "difficulty", column = "difficulty"),
            @Result(property = "status", column = "status"),
            @Result(property = "lastModified", column = "last_modified"),
            @Result(property = "tags", column = "tags", typeHandler = com.aliersel.codevaultbackend.mapper.type_handler.ArrayTypeHandler.class),
            @Result(property = "mastery", column = "mastery")
    })
    List<ProblemWithTags> findFilteredProblemsByUserID(Integer userID, Integer type, Integer difficulty, Boolean status, List<Integer> tagIDs, String keyword, Integer problemTitleSort, Integer difficultySort, Integer masterySort, Integer lastModifiedSort, Integer offset, Integer pageSize);

    @Select("SELECT c.category_id AS id, c.category_name AS name, COALESCE(pc.count, 0) AS count " +
            "FROM category AS c " +
            "LEFT JOIN ( " +
            "SELECT category_id, COUNT(problem_id) AS count " +
            "FROM problem_category " +
            "GROUP BY category_id " +
            ") AS pc " +
            "ON c.category_id = pc.category_id " +
            "WHERE c.user_id = #{userID}")
    List<CategoryWithCounts> findCategoriesByUserID(Integer userID);

    @Update("UPDATE category SET category_name = #{categoryName} WHERE category_id = #{categoryID}")
    Boolean updateCategory(Integer categoryID, String categoryName);

    @Delete("DELETE FROM category WHERE category_id = #{categoryID}")
    Boolean deleteCategory(Integer categoryID);
}
