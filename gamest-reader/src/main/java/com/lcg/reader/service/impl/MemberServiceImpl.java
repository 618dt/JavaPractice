package com.lcg.reader.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lcg.reader.entity.Evaluation;
import com.lcg.reader.entity.Member;
import com.lcg.reader.entity.MemberReadState;
import com.lcg.reader.mapper.EvaluationMapper;
import com.lcg.reader.mapper.MemberMapper;
import com.lcg.reader.mapper.MemberReadStateMapper;
import com.lcg.reader.service.MemberService;
import com.lcg.reader.service.exception.BussinessException;
import com.lcg.reader.utils.MD5Utils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service("memberService")
@Transactional //使用事务
public class MemberServiceImpl implements MemberService {
    @Resource
    private MemberMapper memberMapper;
    @Resource
    private MemberReadStateMapper memberReadStateMapper;
    @Resource
    private EvaluationMapper evaluationMapper;

    /**
     * 会员注册,创建新的会员
     *
     * @param username 用户名
     * @param password 密码
     * @param nickname 昵称
     * @return 新的会员对象
     */
    public Member createMember(String username, String password, String nickname) {
        QueryWrapper<Member> queryWrapper = new QueryWrapper<Member>();
        queryWrapper.eq("username", username);
        List<Member> memberList = memberMapper.selectList(queryWrapper);
        //判断用户名是否已存在
        if (memberList.size() > 0) {
            //抛出我们自定义的异常
            throw new BussinessException("M01", "用户名已存在");
        }
        Member member = new Member();
        member.setUsername(username);
        member.setNickname(nickname);
        //生成小于1000的随机数加上1000相当于随机的4位数用来当作盐值；
        int salt = new Random().nextInt(1000) + 1000;
        String md5 = MD5Utils.md5Digest(password, salt);
        member.setSalt(salt);
        member.setPassword(md5);
        member.setCreateTime(new Date());
        memberMapper.insert(member);
        return member;
    }

    public Member checkLogin(String username, String password) {
        QueryWrapper<Member> queryWrapper = new QueryWrapper<Member>();
        queryWrapper.eq("username", username);
        //用户只有一个，按照用户名查询用户表对应的字段,返回对象为用户实体类
        Member member = memberMapper.selectOne(queryWrapper);
        if (member == null) {//说明前台传来的用户名不存在
            throw new BussinessException("M02", "用户不存在");
        }
        int salt = member.getSalt();
        String md5 = MD5Utils.md5Digest(password, salt);
        if (!md5.equals(member.getPassword())) {
            throw new BussinessException("M03", "密码错误");
        }
        return member;
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public MemberReadState selectMemberReadState(Long memberId, Long bookId) {
        QueryWrapper<MemberReadState> queryWrapper = new QueryWrapper<MemberReadState>();
        queryWrapper.eq("member_id", memberId);
        queryWrapper.eq("book_id", bookId);
        MemberReadState memberReadState = memberReadStateMapper.selectOne(queryWrapper);
        return memberReadState;
    }

    /**
     * 修改阅读状态
     *
     * @param memberId
     * @param bookId
     * @param readState
     * @return
     */
    public MemberReadState updateMemberReadState(Long memberId, Long bookId, Integer readState) {
        //首先进行阅读状态是否存在的判断，如果不存在则插入新字段，存在则修改
        QueryWrapper<MemberReadState> queryWrapper = new QueryWrapper<MemberReadState>();
        queryWrapper.eq("book_id", bookId);
        queryWrapper.eq("member_id", memberId);
        MemberReadState memberReadState = memberReadStateMapper.selectOne(queryWrapper);
        if (memberReadState == null) {
            memberReadState = new MemberReadState();//状态是空的，需要进行实例化
            memberReadState.setBookId(bookId);
            memberReadState.setMemberId(memberId);
            memberReadState.setReadState(readState);
            memberReadState.setCreateTime(new Date());
            memberReadStateMapper.insert(memberReadState);
        } else {
            memberReadState.setReadState(readState);
            memberReadState.setCreateTime(new Date());
            memberReadStateMapper.updateById(memberReadState);//根据id进行修改,参数为实体对象;
        }
        return memberReadState;
    }

    /**
     * 新增评论
     *
     * @param memberId 用户Id
     * @param bookId   图书id
     * @param score    评分
     * @param content  评论内容
     * @return
     */
    public Evaluation evaluate(Long memberId, Long bookId, Integer score, String content) {
        Evaluation evaluation = new Evaluation();
        evaluation.setBookId(bookId);
        evaluation.setMemberId(memberId);
        evaluation.setContent(content);
        evaluation.setScore(score);
        evaluation.setCreateTime(new Date());
        evaluation.setState("enable");
        evaluation.setEnjoy(0);//刚新增的评论默认点赞数量为0
        evaluationMapper.insert(evaluation);
        return evaluation;
    }

    public Evaluation enjoy(Long evaluationId) {
        Evaluation eva = evaluationMapper.selectById(evaluationId);
        eva.setEnjoy(eva.getEnjoy() + 1);
        evaluationMapper.updateById(eva);
        return eva;
    }

    public Member selectById(Long evaluationId) {
        Member member = memberMapper.selectById(evaluationId);
        return member;
    }


}
