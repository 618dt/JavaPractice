package com.lcg.oa.service;

import com.lcg.oa.dao.EmployeeDao;
import com.lcg.oa.entity.Employee;
import com.lcg.oa.utils.MybatisUtils;

public class EmployeeService {
    /**
     * 按编号查找员工
     *
     * @param employeeId 员工编号从user处获取
     * @return 员工对象，不存在时返回null
     */
    public Employee selectById(Long employeeId) {
        return (Employee) MybatisUtils.executeQuery(sqlSession -> {
            EmployeeDao employeeDao = sqlSession.getMapper(EmployeeDao.class);
            return employeeDao.selectById(employeeId);
        });
    }
}
