package com.lcg.oa.service;

import com.lcg.oa.dao.RbacDao;
import com.lcg.oa.dao.UserDao;
import com.lcg.oa.entity.Node;
import com.lcg.oa.entity.User;
import com.lcg.oa.service.exception.BussinessException;
import com.lcg.oa.utils.MD5Utils;

import java.util.List;

public class UserService {
    private UserDao userDao = new UserDao();//记得实例化，否则出现空指针异常
    private RbacDao rbacDao = new RbacDao();

    /**
     * 根据前台输入进行登录校验
     *
     * @param username 前台输入的用户名
     * @param password 前台输入的密码
     * @return 校验通过后, 包含对应用户数据的User实体类
     * @throws BussinessException L001-用户名不存在,L002-密码错误
     */
    public User checkLogin(String username, String password) {
        User user = userDao.selectByUsername(username);
        if (user == null) {
            //抛出用户不存在异常
            throw new BussinessException("L001", "用户名不存在");
        }
        //对前台输入的密码加盐混淆后生成MD5，与保存在数据库中的MD5密码进行比对
        String md5 = MD5Utils.md5Digest(password, user.getSalt());
        if (!md5.equals(user.getPassword())) {
            throw new BussinessException("L002", "密码错误正确密码为：" + user.getPassword());
        }
        return user;
    }

    public List<Node> selectNodeByUserId(Long userId) {
        List<Node> nodeList = rbacDao.selectNodeByUserId(userId);
        return nodeList;
    }
}
