package com.lcg.oa.service;

import com.lcg.oa.dao.DepartmentDao;
import com.lcg.oa.entity.Department;
import com.lcg.oa.utils.MybatisUtils;

public class DepartmentService {
    public Department selectById(Long departmentId) {
        return (Department) MybatisUtils.executeQuery(sqlSession -> {
            DepartmentDao departmentDao = sqlSession.getMapper(DepartmentDao.class);
            return departmentDao.selectById(departmentId);
        });
    }
}
