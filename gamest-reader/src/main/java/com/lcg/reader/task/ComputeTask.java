package com.lcg.reader.task;

import com.lcg.reader.service.BookService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 完成自动计算任务
 */
@Component //组件注解，当一个类不好确定是service还是controller时增加该注解让spring进行管理
public class ComputeTask {
    @Resource
    private BookService bookService;

    //任务调度,从每分钟从0秒执行一次
    @Scheduled(cron = "0 * * * * ?")
    public void updateEvaluation() {
        bookService.updateEvaluation();
        System.out.println("已更新所有图书评分");
    }
}
