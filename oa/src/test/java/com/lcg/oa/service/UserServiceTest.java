package com.lcg.oa.service;

import com.lcg.oa.entity.Node;
import com.lcg.oa.entity.User;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class UserServiceTest {
    private UserService userService = new UserService();

    @Test
    public void checkLogin1() {
        userService.checkLogin("uu", "1234");
    }

    @Test
    public void checkLogin2() {
        userService.checkLogin("m8", "1234");
    }

    @Test
    public void checkLogin3() {
        User user = userService.checkLogin("m8", "lf123");
        System.out.println(user);
    }

    @Test
    public void selectNodeByUserId() {
        List<Node> nodeList = userService.selectNodeByUserId(2l);//l表示长整型
        System.out.println(nodeList);
    }
}