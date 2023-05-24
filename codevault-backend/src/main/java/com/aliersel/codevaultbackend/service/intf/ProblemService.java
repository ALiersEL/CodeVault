package com.aliersel.codevaultbackend.service.intf;

import com.aliersel.codevaultbackend.entity.Problem;
import com.aliersel.codevaultbackend.utils.Result;

public interface ProblemService {
    Result add(Problem problem);
}
