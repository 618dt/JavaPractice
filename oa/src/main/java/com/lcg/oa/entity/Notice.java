package com.lcg.oa.entity;

import java.util.Date;

public class Notice {
    private Long noticeId;
    private Long receiverId;
    private String content;
    private Date createTime;

    public Notice() {//保留默认构造方法，符合javabean要求

    }

    //通过此构造方法简化消息创建过程
    public Notice(Long receiverId, String content) {
        this.receiverId = receiverId;
        this.content = content;
        this.createTime = new Date();
    }

    public Long getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(Long noticeId) {
        this.noticeId = noticeId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
