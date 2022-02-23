package com.lcg.reader.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lcg.reader.entity.Category;
import com.lcg.reader.mapper.CategoryMapper;
import com.lcg.reader.service.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service("categoryService") //该类是一个Service类，因此要增加注解，且beanId一般和接口保持一致
//默认该类的所有方法为只读，所以不使用事务，如果在该类中创建了需要写入的方法，则在方法上额外注解即可
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    @Resource //查询的方法由Mapper接口来完成，因此给Mapper接口对象注入数据
    private CategoryMapper categoryMapper;

    public List<Category> selectAll() {
        List<Category> list = categoryMapper.selectList(new QueryWrapper<Category>());
        return list;
    }
}
