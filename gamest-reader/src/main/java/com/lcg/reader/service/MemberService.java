package com.lcg.reader.service;

import com.lcg.reader.entity.Evaluation;
import com.lcg.reader.entity.Member;
import com.lcg.reader.entity.MemberReadState;

public interface MemberService {
    /**
     * 会员注册,创建新的会员
     *
     * @param username 用户名
     * @param password 密码
     * @param nickname 昵称
     * @return 新的会员对象
     */
    public Member createMember(String username, String password, String nickname);

    /**
     * 登录检查
     *
     * @param username 用户名
     * @param password 用户密码
     * @return 登录对象
     */
    public Member checkLogin(String username, String password);

    /**
     * 获取阅读状态
     *
     * @param memberId
     * @param bookId
     * @return
     */
    public MemberReadState selectMemberReadState(Long memberId, Long bookId);

    /**
     * 更新阅读状态
     *
     * @param memberId
     * @param bookId
     * @param readState
     * @return
     */
    public MemberReadState updateMemberReadState(Long memberId, Long bookId, Integer readState);

    /**
     * 评论
     *
     * @param memberId 用户Id
     * @param bookId   图书id
     * @param score    评分
     * @param content  评论内容
     * @return 短评对象
     */
    public Evaluation evaluate(Long memberId, Long bookId, Integer score, String content);

    /**
     * 短评点赞
     *
     * @param evaluationId 短评编号
     * @return 短评对象
     */
    public Evaluation enjoy(Long evaluationId);

    public Member selectById(Long evaluationId);

}
