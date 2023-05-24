package com.aliersel.codevaultbackend.service.impl;

import com.aliersel.codevaultbackend.entity.Problem;
import com.aliersel.codevaultbackend.mapper.ProblemMapper;
import com.aliersel.codevaultbackend.service.intf.ProblemService;
import com.aliersel.codevaultbackend.utils.Result;
import com.aliersel.codevaultbackend.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProblemServiceImpl implements ProblemService {
    @Autowired
    private ProblemMapper problemMapper;

    @Override
    public Result add(Problem problem) {
        problemMapper.insert(problem);
        return ResultUtil.success();
    }


}
