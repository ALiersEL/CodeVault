package com.aliersel.codevaultbackend.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.aliersel.codevaultbackend.entity.Problem;
import com.aliersel.codevaultbackend.security.JwtTokenProvider;
import com.aliersel.codevaultbackend.service.intf.ProblemService;
import com.aliersel.codevaultbackend.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/problem")
public class ProblemController {
    @Autowired
    private ProblemService problemService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/add")
    // 从header中获取token，从token中获取userID，从requestBody中获取problem
    public Result add(@RequestHeader("Authorization") String token, @RequestBody String problemRequest) {
        Integer userID = jwtTokenProvider.getUserid(token.split(" ")[1].trim());
        System.out.println("success");
        System.out.println(problemRequest);
        JSONObject json = JSON.parseObject(problemRequest);
        Problem problem = new Problem();
        problem.setProblemTitle(json.getString("problemTitle"));
        problem.setProblemContent(json.getString("problemContent"));
        problem.setStatus(json.getBoolean("status"));
        problem.setMastery(json.getInteger("mastery"));
        // to be continued
        problem.setUserID(userID);
        return problemService.add(problem);
    }
}
