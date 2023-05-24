package com.aliersel.codevaultbackend.controller;

import com.aliersel.codevaultbackend.entity.Problem;
import com.aliersel.codevaultbackend.service.intf.ProblemService;
import com.aliersel.codevaultbackend.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/problems")
public class ProblemController {
    @Autowired
    private ProblemService problemService;

    @PostMapping("/add")
    public Result add(@RequestBody Problem problem) {
        return problemService.add(problem);
    }
}
