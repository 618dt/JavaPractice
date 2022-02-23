package com.lcg.reader.service;

import com.lcg.reader.entity.Category;

import java.util.List;

//有这个service的接口，就要创建其实现类
public interface CategoryService {
    public List<Category> selectAll();
}
