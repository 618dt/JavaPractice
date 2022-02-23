package com.lcg.reader.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lcg.reader.entity.*;
import com.lcg.reader.service.BookService;
import com.lcg.reader.service.CategoryService;
import com.lcg.reader.service.EvaluationService;
import com.lcg.reader.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class BookController {
    @Resource
    private CategoryService categoryService;//分类的信息来自此接口
    @Resource
    private BookService bookService;
    @Resource
    private EvaluationService evaluationService;

    @Resource
    private MemberService memberService;

    /**
     * 显示首页
     *
     * @return 数据模型
     */
    @GetMapping("/")
    public ModelAndView showIndex() {
        ModelAndView mav = new ModelAndView("/index");
        //获取分类列表
        List<Category> categoryList = categoryService.selectAll();
        //将分类列表的信息放入mav中，在ftl文件中利用属性名categoryList来设置数据
        mav.addObject("categoryList", categoryList);
        return mav;
    }

    @GetMapping("/books")
    @ResponseBody //直接返回响应的json字符串
    public IPage<Book> selectBook(Long categoryId, String order, Integer p) {
        if (p == null) {
            p = 1;
        }
        IPage<Book> iPage = bookService.paging(categoryId, order, p, 5);//默认每页显示五条数据
        return iPage;
    }

    @GetMapping("/book/{bid}")//路径变量用{}包裹,即将index中book/后的bookId定义为id;
    public ModelAndView showDetail(@PathVariable("bid") Long id, HttpSession session) {
        Member member = (Member) session.getAttribute("loginMember");
        ModelAndView mav = new ModelAndView("/detail");
        if (member != null) {
            //获取会员阅读状态
            MemberReadState memberReadState = memberService.selectMemberReadState(member.getMemberId(), id);
            mav.addObject("memberReadState", memberReadState);
        }
        Book book = bookService.selectById(id);
        List<Evaluation> evaluationList = evaluationService.selectByBookId(id);
        mav.addObject("book", book);
        mav.addObject("evaluationList", evaluationList);
        return mav;
    }
}
