package com.lcg.reader.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lcg.reader.entity.Book;
import com.lcg.reader.service.BookService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class BookServiceImplTest {
    @Resource
    private BookService bookService;

    @Test
    public void paging() {
        IPage<Book> iPage = bookService.paging(2l, "quantity", 2, 5);
        List<Book> records = iPage.getRecords();//获取分页对象iPage的数据
        for (Book b : records) {
            System.out.println("ID：" + b.getBookId() + "书名:" + b.getBookName());
        }
        System.out.println("总页数" + iPage.getPages());
    }
}