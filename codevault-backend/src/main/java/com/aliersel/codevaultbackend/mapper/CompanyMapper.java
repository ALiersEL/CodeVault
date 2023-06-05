package com.aliersel.codevaultbackend.mapper;

import com.aliersel.codevaultbackend.entity.Company;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CompanyMapper {
    @Select("SELECT * FROM company WHERE user_id = #{userID}")
    List<Company> findByUserID(Integer userID);
}
