package com.aliersel.codevaultbackend.service.impl;

import com.aliersel.codevaultbackend.entity.Problem;
import com.aliersel.codevaultbackend.mapper.ProblemMapper;
import com.aliersel.codevaultbackend.service.intf.ProblemService;
import com.aliersel.codevaultbackend.utils.Result;
import com.aliersel.codevaultbackend.utils.ResultUtil;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProblemServiceImpl implements ProblemService {
    @Autowired
    private ProblemMapper problemMapper;
    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Override
    public Result<Integer> addProblem(Problem problem) {
        try {
            problemMapper.addProblem(problem);
            return ResultUtil.success(problem.getProblemID());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(500, "添加失败");
        }
    }

    @Override
    public Result findByProblemID(Integer problemID) {
        return ResultUtil.success(problemMapper.findByProblemID(problemID));
    }

    @Override
    public Result addCompanies(Integer problemID, List<Integer> companyIDs) {
        try(SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH)) {
            ProblemMapper problemMapper = sqlSession.getMapper(ProblemMapper.class);
            for(Integer companyID: companyIDs) {
                problemMapper.addCompany(problemID, companyID);
            }
            sqlSession.commit();
            return ResultUtil.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(500, "添加失败");
        }
    }

    @Override
    public Result addDepartments(Integer problemID, List<Integer> departmentIDs) {
        try(SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH)) {
            ProblemMapper problemMapper = sqlSession.getMapper(ProblemMapper.class);
            for(Integer departmentID: departmentIDs) {
                problemMapper.addDepartment(problemID, departmentID);
            }
            sqlSession.commit();
            return ResultUtil.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(500, "添加失败");
        }
    }

    @Override
    public Result addPosts(Integer problemID, List<Integer> postIDs) {
        try(SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH)) {
            ProblemMapper problemMapper = sqlSession.getMapper(ProblemMapper.class);
            for(Integer postID: postIDs) {
                problemMapper.addPost(problemID, postID);
            }
            sqlSession.commit();
            return ResultUtil.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(500, "添加失败");
        }
    }

    @Override
    public Result addCategory(Integer problemID, Integer categoryID) {
        try {
            problemMapper.addCategory(problemID, categoryID);
            return ResultUtil.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(500, "添加失败");
        }
    }

    @Override
    public Result addCategories(Integer problemID, List<Integer> categoryIDs) {
        try(SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH)) {
            ProblemMapper problemMapper = sqlSession.getMapper(ProblemMapper.class);
            for(Integer categoryID: categoryIDs) {
                problemMapper.addCategory(problemID, categoryID);
            }
            sqlSession.commit();
            return ResultUtil.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(500, "添加失败");
        }
    }
}
