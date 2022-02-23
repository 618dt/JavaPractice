package com.lcg.reader.service;

import com.lcg.reader.entity.User;
import com.lcg.reader.mapper.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

public interface UserService {
    public User checkLogin(String username, String password);
}
