package com.lcg.oa.controller;

import com.alibaba.fastjson.JSON;
import com.lcg.oa.entity.Notice;
import com.lcg.oa.entity.User;
import com.lcg.oa.service.NoticeService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "NoticeServlet", urlPatterns = "/notice/list")
public class NoticeServlet extends HttpServlet {
    private NoticeService noticeService = new NoticeService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("login_user");//提取登录用户信息
        List<Notice> noticeList = noticeService.getNoticeList(user.getEmployeeId());
        Map result = new HashMap<>();
        result.put("code", "0");
        result.put("msg", "");
        result.put("count", noticeList.size());//记录总数
        result.put("data", noticeList);//完整的数据
        String json = JSON.toJSONString(result);
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(json);
    }
}
