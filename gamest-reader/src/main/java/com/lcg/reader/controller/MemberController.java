package com.lcg.reader.controller;

import com.lcg.reader.entity.Evaluation;
import com.lcg.reader.entity.Member;
import com.lcg.reader.service.MemberService;
import com.lcg.reader.service.exception.BussinessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
public class MemberController {
    @Resource
    private MemberService memberService;

    //显示注册页面
    @GetMapping("/register.html")/*这里的后缀可写可不写*/
    public ModelAndView showRegister() {
        return new ModelAndView("/register");
    }

    //显示登录页面
    @GetMapping("/login.html")
    public ModelAndView showLogin() {
        return new ModelAndView("/login");/*这里只能是url，不能加后缀*/
    }

    /*因为无论哪一个web应用框架都是基于底层的Servlet进行实现，所以这里我们
     * 可以直接将原生的HttpServletRequest对象写入参数中进行使用*/
    @PostMapping("/register")//ajax采用post方式提交的
    @ResponseBody //直接进行序列化的响应返回
    public Map register(String vc, String username, String password, String nickname, HttpServletRequest request) {
        //利用底层的Servlet获取当前会话中的kaptchaVerifyCode属性的值(即验证码)
        String verifyCode = (String) request.getSession().getAttribute("kaptchaVerifyCode");
        /*验证码校验,返回result Map对象*/
        Map result = new HashMap();
        //没有写入验证码||后台没有产生验证码||忽略大小写比对验证码,!表示验证码不一致
        if (vc == null || verifyCode == null || !vc.equalsIgnoreCase(verifyCode)) {
            result.put("code", "VC01");
            result.put("msg", "验证码错误");
        } else {
            try {
                //创建用户
                memberService.createMember(username, password, nickname);
                result.put("code", "0");
                result.put("msg", "success");
            } catch (BussinessException e) {
                e.printStackTrace();
                result.put("code", e.getCode());
                result.put("msg", e.getMsg());
            }
        }
        return result;
    }

    /*这里采用session,session比request的范围更广,用来保存页面的用户信息*/
    @ResponseBody
    @PostMapping("/login")
    public Map checkLogin(String username, String password, String vc, HttpSession session) {
        String verifyCode = (String) session.getAttribute("kaptchaVerifyCode");
        Map result = new HashMap();
        if (vc == null || verifyCode == null || !vc.equalsIgnoreCase(verifyCode)) {
            result.put("code", "VC01");
            result.put("msg", "验证码错误");
        } else {
            try {
                //先进行验证码的比对，然后校验登录的用户信息，信息不正确的话会自动抛出异常
                Member member = memberService.checkLogin(username, password);
                //保存登录用户的信息，在首页中进行展现
                session.setAttribute("loginMember", member);
                result.put("code", "0");
                result.put("msg", "success");
            } catch (BussinessException e) {
                e.printStackTrace();
                result.put("code", e.getCode());
                result.put("msg", e.getMsg());
            }
        }
        return result;
    }

    @PostMapping("/update_read_state")
    @ResponseBody
    public Map updateReadState(Long memberId, Long bookId, Integer readState) {
        Map result = new HashMap();
        try {
            memberService.updateMemberReadState(memberId, bookId, readState);
            result.put("code", "0");
            result.put("msg", "success");
        } catch (BussinessException e) {
            e.printStackTrace();
            result.put("code", e.getCode());
            result.put("msg", e.getMsg());
        }
        return result;
    }

    @PostMapping("/evaluate")
    @ResponseBody
    public Map evaluate(Long memberId, Long bookId, Integer score, String content) {
        Map result = new HashMap();
        try {
            /*由于在service方法中我们有一个返回对象Evaluation；如果我们前台客户端需要这个对象的话
             * 也可以将其加入result中返回到前台*/
            //Evaluation eva = memberService.evaluate(memberId, bookId, score, content);
            memberService.evaluate(memberId, bookId, score, content);
            result.put("code", "0");
            result.put("msg", "success");
            //result.put("evaluation",eva);返回前台
        } catch (BussinessException e) {
            e.printStackTrace();
            result.put("code", e.getCode());
            result.put("msg", e.getMsg());
        }
        return result;
    }

    @PostMapping("/enjoy")
    @ResponseBody
    public Map enjoy(Long evaluationId) {
        Map result = new HashMap();
        try {
            Evaluation eva = memberService.enjoy(evaluationId);
            result.put("code", "0");
            result.put("msg", "success");
            result.put("evaluation", eva);//需要将短评对象返回，以获取短评的点赞数
        } catch (BussinessException e) {
            e.printStackTrace();
            result.put("code", e.getCode());
            result.put("msg", e.getMsg());
        }
        return result;
    }
}
