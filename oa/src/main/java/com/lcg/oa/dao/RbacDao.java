package com.lcg.oa.dao;

import com.lcg.oa.entity.Node;
import com.lcg.oa.utils.MybatisUtils;

import java.util.List;

public class RbacDao {
    public List<Node> selectNodeByUserId(Long userId) {
        return (List) MybatisUtils.executeQuery(sqlSession -> sqlSession.selectList("rbacmapper.selectNodeByUserId", userId));
    }
}
