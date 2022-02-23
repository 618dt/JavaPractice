package com.lcg.reader.controller.management;

import com.lcg.reader.entity.User;
import com.lcg.reader.service.UserService;
import com.lcg.reader.service.exception.BussinessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/management")
public class ManagementController {
    @Resource
    private UserService userService;

    @GetMapping("/index.html")
    public ModelAndView showIndex(HttpSession session) {
        User user = (User) session.getAttribute("loginAdmin");
        if (user == null) {
            return new ModelAndView("/management/login");
        } else {
            ModelAndView mav = new ModelAndView("/management/index");
            mav.addObject(user);
            return mav;
        }
    }

    //处理登录校验
    @PostMapping("/check_login")
    @ResponseBody
    public Map checkLogin(String username, String password, HttpSession session) {
        Map result = new HashMap();
        try {
            User user = userService.checkLogin(username, password);
            //保存管理员登录信息
            session.setAttribute("loginAdmin", user);
            result.put("code", "0");
            result.put("msg", "success");
        } catch (BussinessException e) {
            e.printStackTrace();
            result.put("code", e.getCode());
            result.put("msg", e.getMsg());
        }

        return result;
    }

    /*管理员登出*/
    @GetMapping("/logout")
    public ModelAndView logoutAdmin(HttpServletRequest request) {
        request.getSession().invalidate();//清空当前会话
        //跳转到登录页面
        return new ModelAndView("/management/login");
    }

}
