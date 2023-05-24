package com.aliersel.codevaultbackend.mapper;

import com.aliersel.codevaultbackend.entity.Problem;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProblemMapper {
    void insert(Problem problem);
}
