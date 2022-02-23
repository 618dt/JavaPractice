package com.lcg.reader.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lcg.reader.entity.Book;
import com.lcg.reader.entity.Evaluation;
import com.lcg.reader.entity.Member;
import com.lcg.reader.mapper.BookMapper;
import com.lcg.reader.mapper.EvaluationMapper;
import com.lcg.reader.mapper.MemberMapper;
import com.lcg.reader.service.EvaluationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("evaluation")
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class EvaluationServiceImpl implements EvaluationService {
    @Resource
    private EvaluationMapper evaluationMapper;
    @Resource
    private MemberMapper memberMapper;
    @Resource
    private BookMapper bookMapper;

    /**
     * 按图书编号查询有效短评
     *
     * @param bookId
     * @return
     */
    public List<Evaluation> selectByBookId(Long bookId) {
        //获取图书对象
        Book book = bookMapper.selectById(bookId);
        QueryWrapper<Evaluation> queryWrapper = new QueryWrapper<Evaluation>();
        queryWrapper.eq("book_id", bookId);
        queryWrapper.eq("state", "enable");
        queryWrapper.orderByDesc("create_time");
        List<Evaluation> evaluationList = evaluationMapper.selectList(queryWrapper);
        for (Evaluation eva : evaluationList) {
            /*遍历每一个评论列表，然后根据评论列表的memberId在数据库中查询member对象
             * 再把查到的对象赋值给Evaluation类里面关联的对象Member*/
            Member member = memberMapper.selectById(eva.getMemberId());
            eva.setMember(member);
            eva.setBook(book);
        }
        return evaluationList;
    }

    public IPage<Evaluation> pagingEva(Integer page, Integer limit) {
        //构造分页信息
        Page<Evaluation> p = new Page<Evaluation>(page, limit);
        //Mybatis-plus的分页查询,条件设置为空表示全选
        IPage<Evaluation> iPage = evaluationMapper.selectPage(p, null);
/*        for (Evaluation e : iPage.getRecords()) {
            Book book = bookMapper.selectById(e.getBookId());
            Member member = memberMapper.selectById(e.getMemberId());
            e.setBook(book);
            e.setMember(member);
        }*/
        return iPage;
    }

    /**
     * 短评禁用功能
     *
     * @param evaluationId 评论ID
     * @param reason       禁用原因
     */
    public void changeState(Long evaluationId, String reason) {
        //获取原生的评论
        Evaluation rawEvaluation = evaluationMapper.selectById(evaluationId);
        rawEvaluation.setState("disable");//状态为禁用
        rawEvaluation.setDisableReason(reason);
        rawEvaluation.setDisableTime(new Date());
        evaluationMapper.updateById(rawEvaluation);//进行修改
    }
}
