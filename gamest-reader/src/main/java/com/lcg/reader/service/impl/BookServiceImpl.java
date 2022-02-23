package com.lcg.reader.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lcg.reader.entity.Book;
import com.lcg.reader.entity.Evaluation;
import com.lcg.reader.entity.MemberReadState;
import com.lcg.reader.mapper.BookMapper;
import com.lcg.reader.mapper.EvaluationMapper;
import com.lcg.reader.mapper.MemberReadStateMapper;
import com.lcg.reader.service.BookService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service("bookService")
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class BookServiceImpl implements BookService {
    @Resource
    private BookMapper bookMapper;
    @Resource
    private MemberReadStateMapper memberReadStateMapper;
    @Resource
    private EvaluationMapper evaluationMapper;

    public IPage<Book> paging(Long categoryId, String order, Integer page, Integer rows) {
        //分页信息：哪一页，每页的记录数
        Page<Book> p = new Page<Book>(page, rows);
        //条件构造器，为空表示没有条件
        QueryWrapper<Book> queryWrapper = new QueryWrapper<Book>();
        //分类不为空，并且有分类(前台我们将categoryId设置为-1表示没有进行分类)
        if (categoryId != null && categoryId != -1) {
            //设置条件为分类的id ,查询等于指定字段的数据
            queryWrapper.eq("category_id", categoryId);
        }
        if (order != null) {
            if (order.equals("quantity")) {
                //设置条件按照指定的字段进行降序排序
                queryWrapper.orderByDesc("evaluation_quantity");
            } else if (order.equals("score")) {
                queryWrapper.orderByDesc("evaluation_score");
            }
        }
        //参数为分页信息，条件构造器
        IPage<Book> pageObject = bookMapper.selectPage(p, queryWrapper);
        return pageObject;
    }

    public Book selectById(Long bookId) {
        Book book = bookMapper.selectById(bookId);
        return book;
    }

    /*既然只是调用bookMapper提供的方法，为什么不直接在前台调用呢？
     * 这是因为我们基于MVC的按层逐级调用，禁止从controller直接调用mapper的方法来
     * 进行与数据库的操作,中间必须经过service；遵循这样的开发规范，会使程序维护更加轻松*/
    @Transactional
    public void updateEvaluation() {
        bookMapper.updateEvaluation();
    }

    @Transactional
    public Book createBook(Book book) {
        //Mybatis-plus会自动的将自增的主键编号回填到实体类中
        bookMapper.insert(book);
        return book;//此时的book相比参数中的book多了一个主键(图书编号)
    }

    @Transactional
    public Book updateBook(Book book) {
        bookMapper.updateById(book);
        return book;
    }

    @Transactional
    public void deleteBook(Long bookId) {
        bookMapper.deleteById(bookId);
        /*构造条件删除某一本图书的所有阅读状态*/
        QueryWrapper<MemberReadState> mrsQueryWrapper = new QueryWrapper<MemberReadState>();
        mrsQueryWrapper.eq("book_id", bookId);//即删除的where book_id = bookId
        memberReadStateMapper.delete(mrsQueryWrapper);
        QueryWrapper<Evaluation> evaluationQueryWrapper = new QueryWrapper<Evaluation>();
        evaluationQueryWrapper.eq("book_id", bookId);
        evaluationMapper.delete(evaluationQueryWrapper);
    }
}
