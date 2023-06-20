package com.aliersel.codevaultbackend.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.aliersel.codevaultbackend.controller.entity.Source;
import com.aliersel.codevaultbackend.entity.*;
import com.aliersel.codevaultbackend.mapper.ProblemMapper;
import com.aliersel.codevaultbackend.security.JwtTokenProvider;
import com.aliersel.codevaultbackend.service.intf.ProblemService;
import com.aliersel.codevaultbackend.service.intf.UserService;
import com.aliersel.codevaultbackend.utils.Result;
import com.aliersel.codevaultbackend.utils.ResultUtil;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProblemServiceImpl implements ProblemService {
    @Autowired
    private ProblemMapper problemMapper;
    @Autowired
    private SqlSessionFactory sqlSessionFactory;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private UserService userService;

    @Override
    public Result<Integer> addProblem(Problem problem) {
        try {
            problemMapper.saveProblem(problem);
            return ResultUtil.success(problem.getProblemID());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(500, "添加失败");
        }
    }

    @Override
    public Result<JSONObject> processProblemRequest(String token, JSONObject problemRequest,Integer folderID) {
        try {
            Integer userID = jwtTokenProvider.getUserid(token.split(" ")[1].trim());
            System.out.println(problemRequest);
            // 获得tags中value为"-1"的元素的label作为labelList, 其余value不为"-1"的元素的value作为valueList
            JSONArray tags = problemRequest.getJSONArray("tags");
            List<String> labelList = new ArrayList<>(); // 用于存放新增的标签，只需存name
            List<Integer> valueList = new ArrayList<>(); // 用于存放已有的标签，需存id
            for (int i = 0; i < tags.size(); i++) {
                JSONObject jsonObject = tags.getJSONObject(i);
                String label = jsonObject.getString("label");
                Integer value = jsonObject.getInteger("value");
                if (value == -1) {
                    labelList.add(label);
                } else {
                    valueList.add(value);
                }
            }

            JSONArray sources = problemRequest.getJSONArray("sources");
            // sourceID和sourceType组成的set，以便插入problem_company等表
            Map<Integer, Set<Integer>> objectMap = new HashMap<>();
            Set<String> companyNames = new HashSet<>();
            Set<String> companyDepartmentPairs = new HashSet<>();
            Set<String> companyDepartmentPostTriples = new HashSet<>();

            for (int i = 0; i < sources.size(); i++) {
                JSONObject jsonObject = sources.getJSONObject(i);
                Integer sourceType = null ;
                Integer sourceID = null;
                String sourcePath = "";

                JSONObject company = jsonObject.getJSONObject("company");
                if (!company.toString().equals("{}")) {
                    // Process company
                    String companyName = company.getString("companyName");
                    Integer companyID = company.getInteger("companyID");
                    sourcePath += companyName;
                    if (companyID == -1 && !companyNames.contains(sourcePath)) {
                        companyNames.add(sourcePath);
                        Company company1 = new Company();
                        company1.setCompanyName(companyName);
                        company1.setUserID(userID);
                        companyID = userService.addCompany(company1).getData();
                    }
                    sourceType = 1;
                    sourceID = companyID;

                    JSONObject department = jsonObject.getJSONObject("department");
                    if (!department.toString().equals("{}")) {
                        // Process department
                        String departmentName = department.getString("departmentName");
                        Integer departmentID = department.getInteger("departmentID");
                        sourcePath += "/" + departmentName;
                        if (departmentID == -1 && !companyDepartmentPairs.contains(sourcePath)) {
                            companyDepartmentPairs.add(sourcePath);
                            if (companyID == -1) {
                                companyID = userService.findCompanyID(companyName, userID).getData();
                            }
                            Department department1 = new Department();
                            department1.setDepartmentName(departmentName);
                            department1.setCompanyID(companyID);
                            departmentID = userService.addDepartment(department1).getData();
                        }
                        sourceType = 2;
                        sourceID = departmentID;

                        JSONObject post = jsonObject.getJSONObject("post");
                        if (!post.toString().equals("{}")) {
                            // Process post
                            String postName = post.getString("postName");
                            Integer postID = post.getInteger("postID");
                            sourcePath += "/" + postName;
                            if (postID == -1 && !companyDepartmentPostTriples.contains(sourcePath)) {
                                companyDepartmentPostTriples.add(sourcePath);
                                if (departmentID == -1) {
                                    departmentID = userService.findDepartmentID(departmentName, companyID).getData();
                                }
                                Post post1 = new Post();
                                post1.setPostName(postName);
                                post1.setDepartmentID(departmentID);
                                postID = userService.addPost(post1).getData();
                            }
                            sourceType = 3;
                            sourceID = postID;
                        }
                    }
                }

                // 如果sourceType不为null
                if (sourceType != null){ // 检查sourceType是否已存在于objectMap中
                    Set<Integer> sourceIDs = objectMap.computeIfAbsent(sourceType, k -> new HashSet<>());
                    // sourceType不存在，创建对应的Set并插入sourceID
                    // 将sourceID插入到对应的Set中
                    sourceIDs.add(sourceID);
                }
            }
//            System.out.println("objectMap: " + objectMap);

            Problem problem = new Problem();
            problem.setProblemID(problemRequest.getInteger("problemID"));
            problem.setProblemTitle(problemRequest.getString("problemTitle"));
            problem.setProblemContent(problemRequest.getString("problemContent"));
            problem.setStatus(problemRequest.getBoolean("status"));
            problem.setMastery(problemRequest.getInteger("mastery"));
            problem.setDifficulty(problemRequest.getInteger("difficulty"));
            problem.setProblemType(problemRequest.getInteger("problemType"));
            problem.setUserID(userID);
            problem.setFolderID(folderID);
//            System.out.println(problem);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userID", userID);
            jsonObject.put("problem", problem);
            jsonObject.put("labelList", labelList);
            jsonObject.put("valueList", valueList);
            jsonObject.put("objectMap", objectMap);
            return ResultUtil.success(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(500, "处理失败");
        }
    }

    @Override
    public Result updateProblem(Problem problem) {
        try {
            problemMapper.updateProblem(problem);
            return ResultUtil.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(500, "更新失败");
        }
    }

    @Override
    public Result<Problem> findByProblemID(Integer problemID) {
        try {
            List<Category> categories = problemMapper.findCategoriesByProblemID(problemID);
            return ResultUtil.success(problemMapper.findByProblemID(problemID));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(500, "查询失败");
        }
    }

    @Override
    public Result deleteByProblemID(Integer problemID) {
        try {
            problemMapper.deleteByProblemID(problemID);
            return ResultUtil.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(500, "删除失败");
        }
    }

    @Override
    public Result addCompanies(Integer problemID, List<Integer> companyIDs) {
        try(SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH)) {
            ProblemMapper problemMapper = sqlSession.getMapper(ProblemMapper.class);
            for(Integer companyID: companyIDs) {
                problemMapper.saveCompany(problemID, companyID);
            }
            sqlSession.commit();
            return ResultUtil.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(500, "添加失败");
        }
    }

    @Override
    public Result deleteCompanies(Integer problemID, List<Integer> companyIDs) {
        try(SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH)) {
            ProblemMapper problemMapper = sqlSession.getMapper(ProblemMapper.class);
            for(Integer companyID: companyIDs) {
                problemMapper.deleteCompany(problemID, companyID);
            }
            sqlSession.commit();
            return ResultUtil.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(500, "删除失败");
        }
    }

    @Override
    public Result<List<Integer>> findCompanyIDsByProblemID(Integer problemID) {
        try {
            return ResultUtil.success(problemMapper.findCompanyIDsByProblemID(problemID));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(500, "查询失败");
        }
    }

    @Override
    public Result addDepartments(Integer problemID, List<Integer> departmentIDs) {
        try(SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH)) {
            ProblemMapper problemMapper = sqlSession.getMapper(ProblemMapper.class);
            for(Integer departmentID: departmentIDs) {
                problemMapper.saveDepartment(problemID, departmentID);
            }
            sqlSession.commit();
            return ResultUtil.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(500, "添加失败");
        }
    }

    @Override
    public Result deleteDepartments(Integer problemID, List<Integer> departmentIDs) {
        try(SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH)) {
            ProblemMapper problemMapper = sqlSession.getMapper(ProblemMapper.class);
            for(Integer departmentID: departmentIDs) {
                problemMapper.deleteDepartment(problemID, departmentID);
            }
            sqlSession.commit();
            return ResultUtil.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(500, "删除失败");
        }
    }

    @Override
    public Result<List<Integer>> findDepartmentIDsByProblemID(Integer problemID) {
        try {
            return ResultUtil.success(problemMapper.findDepartmentIDsByProblemID(problemID));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(500, "查询失败");
        }
    }

    @Override
    public Result addPosts(Integer problemID, List<Integer> postIDs) {
        try(SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH)) {
            ProblemMapper problemMapper = sqlSession.getMapper(ProblemMapper.class);
            for(Integer postID: postIDs) {
                problemMapper.savePost(problemID, postID);
            }
            sqlSession.commit();
            return ResultUtil.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(500, "添加失败");
        }
    }

    @Override
    public Result deletePosts(Integer problemID, List<Integer> postIDs) {
        try(SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH)) {
            ProblemMapper problemMapper = sqlSession.getMapper(ProblemMapper.class);
            for(Integer postID: postIDs) {
                problemMapper.deletePost(problemID, postID);
            }
            sqlSession.commit();
            return ResultUtil.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(500, "删除失败");
        }
    }

    @Override
    public Result<List<Integer>> findPostIDsByProblemID(Integer problemID) {
        try {
            return ResultUtil.success(problemMapper.findPostIDsByProblemID(problemID));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(500, "查询失败");
        }
    }

    @Override
    public Result<List<Source>> findSourcesByProblemID(Integer problemID) {
        try {
            List<Source> sources = problemMapper.findSourcesByProblemID(problemID);
            return ResultUtil.success(sources);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(500, "查询失败");
        }
    }

    @Override
    public Result addCategory(Integer problemID, Integer categoryID) {
        try {
            problemMapper.saveCategory(problemID, categoryID);
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
                problemMapper.saveCategory(problemID, categoryID);
            }
            sqlSession.commit();
            return ResultUtil.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(500, "添加失败");
        }
    }

    @Override
    public Result deleteCategories(Integer problemID, List<Integer> categoryIDs) {
        try(SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH)) {
            ProblemMapper problemMapper = sqlSession.getMapper(ProblemMapper.class);
            for(Integer categoryID: categoryIDs) {
                problemMapper.deleteCategory(problemID, categoryID);
            }
            sqlSession.commit();
            return ResultUtil.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(500, "删除失败");
        }
    }

    @Override
    public Result<List<Category>> findCategoriesByProblemID(Integer problemID) {
        try {
            return ResultUtil.success(problemMapper.findCategoriesByProblemID(problemID));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(500, "查询失败");
        }
    }

    @Override
    public Result<List<Integer>> findCategoryIDsByProblemID(Integer problemID) {
        try {
            return ResultUtil.success(problemMapper.findCategoryIDsByProblemID(problemID));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(500, "查询失败");
        }
    }

    @Override
    public Result addCode(Code code) {
        try {
            problemMapper.saveCode(code);
            return ResultUtil.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(500, "添加失败");
        }
    }

    @Override
    public Result findCodesByProblemID(Integer problemID) {
        try {
            return ResultUtil.success(problemMapper.findCodesByProblemID(problemID));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(500, "查询失败");
        }
    }

    @Override
    public Result findCodeByCodeID(Integer codeID) {
        try {
            return ResultUtil.success(problemMapper.findCodeByCodeID(codeID));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(500, "查询失败");
        }
    }

    @Override
    public Result addNote(Note note) {
        try {
            problemMapper.saveNote(note);
            return ResultUtil.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(500, "添加失败");
        }
    }

    @Override
    public Result findNotesByProblemID(Integer problemID) {
        try {
            return ResultUtil.success(problemMapper.findNotesByProblemID(problemID));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(500, "查询失败");
        }
    }

    @Override
    public Result findNoteByNoteID(Integer noteID) {
        try {
            return ResultUtil.success(problemMapper.findNoteByNoteID(noteID));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(500, "查询失败");
        }
    }
}
