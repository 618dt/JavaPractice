package com.lcg.reader.controller.management;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lcg.reader.entity.Book;
import com.lcg.reader.entity.Evaluation;
import com.lcg.reader.entity.Member;
import com.lcg.reader.entity.User;
import com.lcg.reader.service.BookService;
import com.lcg.reader.service.EvaluationService;
import com.lcg.reader.service.MemberService;
import com.lcg.reader.service.exception.BussinessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/management/evaluation")
public class MEvaluationController {
    @Resource
    private EvaluationService evaluationService;
    @Resource
    private BookService bookService;
    @Resource
    private MemberService memberService;

    @GetMapping("/index.html")
    public ModelAndView showMaEvaluation(HttpSession session) {
        User user = (User) session.getAttribute("loginAdmin");
        if (user == null) {
            return new ModelAndView("/management/login");
        } else {
            ModelAndView mav = new ModelAndView("/management/evaluation");
            mav.addObject(user);
            return mav;
        }
    }

    @GetMapping("/list")
    @ResponseBody
    public Map listEva(Integer page, Integer limit) {
        Map result = new HashMap();
        if (page == 0) {
            page = 1;
        }
        if (limit == 0) {
            limit = 10;
        }
        List<Evaluation> evaluationList = new LinkedList<Evaluation>();
        try {
            IPage<Evaluation> p = evaluationService.pagingEva(page, limit);
            for (Evaluation eva : p.getRecords()) {
                Book book = bookService.selectById(eva.getBookId());
                Member member = memberService.selectById(eva.getMemberId());
                eva.setBook(book);
                eva.setMember(member);
                evaluationList.add(eva);
            }
            result.put("code", "0");
            result.put("msg", "success");
            result.put("data", evaluationList);
            result.put("count", p.getTotal());
        } catch (BussinessException e) {
            e.printStackTrace();
            result.put("code", e.getCode());
            result.put("msg", e.getMsg());
        }
        return result;
    }

    @PostMapping("/disable")
    @ResponseBody
    public Map disableEva(Long evaluationId, String reason) {
        Map result = new HashMap();
        try {
            evaluationService.changeState(evaluationId, reason);
            result.put("code", "0");
            result.put("msg", "success");
        } catch (BussinessException e) {
            e.printStackTrace();
            result.put("code", e.getCode());
            result.put("msg", e.getMsg());
        }

        return result;
    }
}
