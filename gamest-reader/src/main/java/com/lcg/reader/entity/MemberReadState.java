package com.lcg.reader.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

@TableName("member_read_state")
public class MemberReadState {
    /*这里的一个小坑，由于忘记设置了主键注解，导致后面在更新阅读状态的时候发现只有没有任何阅读状态的时候
     * 点击阅读状态会生效；但当切换阅读状态的时候发现请求报500错误，一开始以为是前台的问题，但想着增加阅读状态没问题
     * 只有修改阅读状态才有问题，以为是后台修改语句的问题，所以检查了后台的业务逻辑，却发现修改语句没有问题，然后慢慢的
     * 查看日志发现updateById的sql语句是where null = ?才发现可能是忘记了主键的设置*/
    @TableId(type = IdType.AUTO)
    private Long rsId;
    private Long bookId;
    private Long memberId;
    private Integer readState;
    private Date createTime;

    public Long getRsId() {
        return rsId;
    }

    public void setRsId(Long rsId) {
        this.rsId = rsId;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Integer getReadState() {
        return readState;
    }

    public void setReadState(Integer readState) {
        this.readState = readState;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
