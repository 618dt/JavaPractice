package com.lcg.reader.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lcg.reader.entity.User;
import com.lcg.reader.mapper.UserMapper;
import com.lcg.reader.service.UserService;
import com.lcg.reader.service.exception.BussinessException;
import com.lcg.reader.utils.MD5Utils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public User checkLogin(String username, String password) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<User>();
        userQueryWrapper.eq("username", username);
        User user = userMapper.selectOne(userQueryWrapper);
        if (user == null) {
            throw new BussinessException("M02", "用户不存在");
        }
        int salt = user.getSalt();
        String md5 = MD5Utils.md5Digest(password, salt);
        if (!md5.equals(user.getPassword())) {
            throw new BussinessException("M03", "密码错误");
        }
        return user;
    }
}
