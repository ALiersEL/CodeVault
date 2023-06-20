package com.aliersel.codevaultbackend.mapper;

import com.aliersel.codevaultbackend.controller.entity.CompanyWithCounts;
import com.aliersel.codevaultbackend.entity.Company;
import com.aliersel.codevaultbackend.entity.Department;
import com.aliersel.codevaultbackend.entity.Post;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CompanyMapper {
    @Select("SELECT c.company_id AS id, c.company_name AS name, COALESCE(COUNT(pc.problem_id) + COUNT(pd.problem_id) + COUNT(pp.problem_id), 0) AS count " +
            "FROM company AS c " +
            "LEFT JOIN department  AS d ON c.company_id = d.company_id " +
            "LEFT JOIN post AS p ON d.department_id = p.department_id " +
            "LEFT JOIN problem_post AS pp ON p.post_id = pp.post_id " +
            "LEFT JOIN problem_department AS pd ON d.department_id = pd.department_id " +
            "LEFT JOIN problem_company AS pc ON c.company_id = pc.company_id " +
            "WHERE pc.company_id = #{companyID} " +
            "OR pd.department_id IN (SELECT department_id FROM department WHERE company_id = #{companyID}) " +
            "OR pp.post_id IN (SELECT post_id FROM post WHERE department_id IN (SELECT department_id FROM department WHERE company_id = #{companyID})) " +
            "GROUP BY c.company_id")
    List<CompanyWithCounts> findCompaniesByUserID(Integer userID);

    @Select("SELECT * FROM department WHERE company_id = #{companyID}")
    List<Department> findDepartmentsByCompanyID(Integer companyID);

    @Select("SELECT * FROM post WHERE department_id = #{departmentID}")
    List<Post> findPostsByDepartmentID(Integer departmentID);
}
