package com.lcg.oa.dao;

import com.lcg.oa.entity.Department;

public interface DepartmentDao {
    public Department selectById(Long departmentId);
}
