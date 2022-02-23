package com.lcg.oa.service;

import com.lcg.oa.dao.NoticeDao;
import com.lcg.oa.entity.Notice;
import com.lcg.oa.utils.MybatisUtils;

import java.util.List;

/**
 * 消息服务
 */
public class NoticeService {
    /**
     * 查询指定员工的系统消息
     *
     * @param receiverId
     * @return 最近100条消息列表
     */
    public List<Notice> getNoticeList(Long receiverId) {
        return (List) MybatisUtils.executeQuery(sqlSession -> {
            NoticeDao noticeDao = sqlSession.getMapper(NoticeDao.class);
            return noticeDao.selectByReceiverId(receiverId);
        });
    }
}
