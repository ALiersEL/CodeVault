package com.aliersel.codevaultbackend.mapper;

import com.aliersel.codevaultbackend.entity.*;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {
    @Insert("INSERT INTO \"user\"" +
            "(user_name,password_hash,phone_number,email) " +
            "VALUES(#{userName}, #{passwordHash}, #{phoneNumber}, #{email})")
    Boolean addUser(User user);

    @Select("SELECT * FROM \"user\" WHERE user_name = #{userName}")
    User findByUserName(String userName);

    @Insert("INSERT INTO company (company_name,user_id) VALUES(#{companyName},#{userID}) RETURNING company_id")
    @Options(useGeneratedKeys = true, keyProperty = "companyID", keyColumn = "company_id")
    void addCompany(Company company);

    @Insert("INSERT INTO department (department_name,company_id) VALUES(#{departmentName},#{companyID}) RETURNING department_id")
    @Options(useGeneratedKeys = true, keyProperty = "departmentID", keyColumn = "department_id")
    void addDepartment(Department department);

    @Insert("INSERT INTO post (post_name,department_id) VALUES(#{postName},#{departmentID}) RETURNING post_id")
    @Options(useGeneratedKeys = true, keyProperty = "postID", keyColumn = "post_id")
    void addPost(Post post);

    @Insert("INSERT INTO category (category_name,user_id) VALUES(#{categoryName},#{userID}) RETURNING category_id")
    @Options(useGeneratedKeys = true, keyProperty = "categoryID", keyColumn = "category_id")
    void addCategory(Category category);

    @Select("SELECT company_id FROM company WHERE company_name = #{companyName} AND user_id = #{userID}")
    Integer findCompanyIDByName(String companyName, Integer userID);

    @Select("SELECT department_id FROM department WHERE department_name = #{departmentName} AND company_id = #{companyID}")
    Integer findDepartmentIDByName(String departmentName, Integer companyID);

}
