package com.lcg.reader.service.impl;

import com.lcg.reader.entity.Category;
import com.lcg.reader.mapper.CategoryMapper;
import com.lcg.reader.service.CategoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.Assert.*;

/*IOC容器初始化的代码*/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class CategoryServiceImplTest {
    @Resource
    private CategoryService category;

    @Test
    public void selectAll() {
        List<Category> list = category.selectAll();
        System.out.println(list);
    }
}