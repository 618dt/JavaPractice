package com.lcg.reader.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lcg.reader.entity.Evaluation;

import java.util.List;

public interface EvaluationService {
    /**
     * 按图书编号查询有效短评
     *
     * @param bookId
     * @return
     */
    public List<Evaluation> selectByBookId(Long bookId);

    /**
     * 评论分页信息
     *
     * @param page  页数
     * @param limit 每页行数
     * @return 分页对象
     */
    public IPage<Evaluation> pagingEva(Integer page, Integer limit);

    /**
     * 修改短评状态(禁用短评)
     *
     * @param evaluationId 评论ID
     * @param reason       禁用原因
     */
    public void changeState(Long evaluationId, String reason);
}
