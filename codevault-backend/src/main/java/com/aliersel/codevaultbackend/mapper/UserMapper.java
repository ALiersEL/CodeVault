package com.aliersel.codevaultbackend.mapper;

import com.aliersel.codevaultbackend.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {
    @Insert("INSERT INTO \"user\"" +
            "(user_name,password_hash,phone_number,email) " +
            "VALUES(#{userName}, #{passwordHash}, #{phoneNumber}, #{email})")
    void insert(User user);

    @Select("SELECT * FROM \"user\" WHERE user_name = #{userName}")
    User findByUserName(String userName);

}
