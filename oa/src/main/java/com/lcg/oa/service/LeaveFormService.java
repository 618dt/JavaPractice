package com.lcg.oa.service;

import com.lcg.oa.dao.EmployeeDao;
import com.lcg.oa.dao.LeaveFormDao;
import com.lcg.oa.dao.NoticeDao;
import com.lcg.oa.dao.ProcessFlowDao;
import com.lcg.oa.entity.Employee;
import com.lcg.oa.entity.LeaveForm;
import com.lcg.oa.entity.Notice;
import com.lcg.oa.entity.ProcessFlow;
import com.lcg.oa.service.exception.BussinessException;
import com.lcg.oa.utils.MybatisUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 请假单流程服务
 */
public class LeaveFormService {
    /**
     * 创建请假单
     *
     * @param form 前端输入的请假单数据
     * @return 持久化后的请假单对象
     */
    public LeaveForm createLeaveForm(LeaveForm form) {
        LeaveForm saveForm = (LeaveForm) MybatisUtils.executeUpdate(sqlSession -> {
            //1.持久化form表单数据,8级以下员工表单状态为processing,8级(总经理)状态为approved
            EmployeeDao employeeDao = sqlSession.getMapper(EmployeeDao.class);
            Employee employee = employeeDao.selectById(form.getEmployeeId());
            if (employee.getLevel() == 8) {
                form.setState("approved");
            } else {
                form.setState("processing");
            }
            LeaveFormDao leaveFormDao = sqlSession.getMapper(LeaveFormDao.class);
            leaveFormDao.insert(form);
            //2.增加第一条流程数据,说明表单已提交,状态为complete
            ProcessFlowDao processFlowDao = sqlSession.getMapper(ProcessFlowDao.class);
            ProcessFlow flow1 = new ProcessFlow();
            flow1.setFormId(form.getFormId());
            flow1.setOperatorId(employee.getEmployeeId());
            flow1.setAction("apply");//申请
            flow1.setCreateTime(new Date());
            flow1.setOrderNo(1);
            flow1.setState("complete");
            flow1.setIsLast(0);
            processFlowDao.insert(flow1);
            //3.分情况创建其余流程数据
            //3.1 7级以下员工,生成部门经理审批任务,请假时间大于36小时,还需生成总经理审批任务
            //设置日期格式，用来消息通知
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH时");
            NoticeDao noticeDao = sqlSession.getMapper(NoticeDao.class);//在if块外获取noticeDao对象
            if (employee.getLevel() < 7) {
                Employee dmanger = employeeDao.selectLeader(employee);
                ProcessFlow flow2 = new ProcessFlow();
                flow2.setFormId(form.getFormId());
                flow2.setOperatorId(dmanger.getEmployeeId());
                flow2.setAction("audit");//审批
                flow2.setCreateTime(new Date());
                flow2.setOrderNo(2);
                flow2.setState("process");
                long diff = form.getEndTime().getTime() - form.getStartTime().getTime();
                float hours = diff / (1000 * 60 * 60) * 1f;
                if (hours >= BussinessConstants.MANAGER_AUDIT_HOURS) {
                    flow2.setIsLast(0);
                    processFlowDao.insert(flow2);
                    //时间大于指定时间，处理总经理的审批流程
                    Employee manger = employeeDao.selectLeader(dmanger);
                    ProcessFlow flow3 = new ProcessFlow();
                    flow3.setFormId(form.getFormId());
                    flow3.setOperatorId(manger.getEmployeeId());
                    flow3.setAction("audit");
                    flow3.setCreateTime(new Date());
                    flow3.setState("ready");
                    flow3.setOrderNo(3);
                    flow3.setIsLast(1);
                    processFlowDao.insert(flow3);
                } else {
                    flow2.setIsLast(1);
                    processFlowDao.insert(flow2);
                }
                //String.format用于输出指定格式的字符串
                String noticeContent = String.format("您的请假申请[%s-%s]已提交，请等待上级审批."
                        , sdf.format(form.getStartTime()), sdf.format(form.getEndTime()));
                noticeDao.insert(new Notice(employee.getEmployeeId(), noticeContent));//存放数据库
                //通知部门经理审批
                noticeContent = String.format("%s-%s提起请假申请[%s-%s],请尽快审批"
                        , employee.getTitle(), employee.getName(), sdf.format(form.getStartTime())
                        , sdf.format(form.getEndTime()));
                noticeDao.insert(new Notice(dmanger.getEmployeeId(), noticeContent));
            } else if (employee.getLevel() == 7) {//部门经理
                //3.2 7级员工,生成总经理审批任务
                Employee manger = employeeDao.selectLeader(employee);
                ProcessFlow flow = new ProcessFlow();
                flow.setFormId(form.getFormId());
                flow.setOperatorId(manger.getEmployeeId());
                flow.setAction("audit");
                flow.setCreateTime(new Date());
                flow.setState("process");
                flow.setOrderNo(2);
                flow.setIsLast(1);
                processFlowDao.insert(flow);
                //请假单申请消息
                String noticeContent = String.format("您的请假申请[%s-%s]已提交，请等待上级审批."
                        , sdf.format(form.getStartTime()), sdf.format(form.getEndTime()));
                noticeDao.insert(new Notice(employee.getEmployeeId(), noticeContent));
                //通知总经理审批
                noticeContent = String.format("%s-%s提起请假申请[%s-%s],请尽快审批"
                        , employee.getTitle(), employee.getName(), sdf.format(form.getStartTime())
                        , sdf.format(form.getEndTime()));
                noticeDao.insert(new Notice(manger.getEmployeeId(), noticeContent));
            } else if (employee.getLevel() == 8) {
                //3.3 8级员工,生成总经理审批任务,系统自动通过
                ProcessFlow flow = new ProcessFlow();
                flow.setFormId(form.getFormId());
                flow.setOperatorId(employee.getEmployeeId());
                flow.setAction("audit");
                flow.setResult("approved");
                flow.setReason("自动通过");
                flow.setCreateTime(new Date());
                flow.setAuditTime(new Date());
                flow.setState("complete");
                flow.setOrderNo(2);
                flow.setIsLast(1);
                processFlowDao.insert(flow);
                //请假单申请消息
                String noticeContent = String.format("您的请假申请[%s-%s]已自动批准通过."
                        , sdf.format(form.getStartTime()), sdf.format(form.getEndTime()));
                noticeDao.insert(new Notice(employee.getEmployeeId(), noticeContent));
            }
            return form;
        });
        return saveForm;
    }

    /**
     * 获取指定任务状态及指定经办人对应的请假单列表
     *
     * @param pfState    ProcessFlow任务状态
     * @param operatorId 经办人编号
     * @return 请假单及相关数据列表
     */
    public List<Map> getLeaveFormList(String pfState, Long operatorId) {
        return (List<Map>) MybatisUtils.executeQuery(sqlSession -> {
            LeaveFormDao dao = sqlSession.getMapper(LeaveFormDao.class);
            List<Map> formList = dao.selectByParams(pfState, operatorId);
            return formList;
        });
    }

    /**
     * 审核请假单
     *
     * @param formId     表单编号
     * @param operatorId 经办人(当前登录员工)
     * @param result     审批结果
     * @param reason     审批意见
     */
    public void audit(Long formId, Long operatorId, String result, String reason) {
        MybatisUtils.executeUpdate(sqlSession -> {
            //1.无论同意/驳回，当前任务（即状态为process）状态变更为complete
            ProcessFlowDao processFlowDao = sqlSession.getMapper(ProcessFlowDao.class);
            List<ProcessFlow> flowList = processFlowDao.selectByFormId(formId);
            if (flowList.size() == 0) {
                throw new BussinessException("PF001", "无效的审批流程");
            }
            //获取当前任务ProcessFlow对象
             //这里采用λ表达式对List进行筛选；p是传入参数；collect表示返回新的List
            List<ProcessFlow> processList = flowList.stream().filter(p ->
                    p.getOperatorId() == operatorId &&
                            p.getState().equals("process")).collect(Collectors.toList());
            ProcessFlow process = null;
            if (processList.size() == 0) {
                throw new BussinessException("PF002", "未找到待处理任务");
            } else {
                process = processList.get(0);
                process.setState("complete");
                process.setResult(result);
                process.setReason(reason);
                process.setAuditTime(new Date());
                processFlowDao.update(process);
            }
            //2.如果当前任务是最后一个节点，代表流程结束，更新请假单状态为对应的approve/refused
            LeaveFormDao leaveFormDao = sqlSession.getMapper(LeaveFormDao.class);
            LeaveForm form = leaveFormDao.selectById(formId);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH时");
            EmployeeDao employeeDao = sqlSession.getMapper(EmployeeDao.class);
            Employee employee = employeeDao.selectById(form.getEmployeeId());//表单提交人信息
            Employee operator = employeeDao.selectById(operatorId);//任务经办人信息
            NoticeDao noticeDao = sqlSession.getMapper(NoticeDao.class);
            if (process.getIsLast() == 1) {
                form.setState(result);//approved|refused
                leaveFormDao.update(form);
                String strResult = null;
                if (result.equals("approved")) {
                    strResult = "批准";
                } else if (result.equals("refused")) {
                    strResult = "驳回";
                }
                String noticeContent = String.format("您的请假申请[%s-%s]%s-%s已%s,审批意见:%S,审批流程已结束"
                        , sdf.format(form.getStartTime()), sdf.format(form.getEndTime())
                        , operator.getTitle(), operator.getName(), strResult, reason);
                noticeDao.insert(new Notice(form.getEmployeeId(), noticeContent));//发给表单提交人的通知
                //发给审批人的通知
                noticeContent = String.format("%s-%s提起请假申请[%s-%s]您已%s,审批意见:%s,审批流程已结束",
                        employee.getTitle(), employee.getName()
                        , sdf.format(form.getStartTime()), sdf.format(form.getEndTime()),
                        strResult, reason);
                noticeDao.insert(new Notice(operator.getEmployeeId(), noticeContent));
            } else {
                //readyList包含所有后续任务节点； filter即过滤掉不符合的节点(例如状态为取消、未完成等)
                List<ProcessFlow> readyList = flowList.stream().filter(p -> p.getState().equals("ready")).collect(Collectors.toList());
                //3.如果当前任务不是最后一个节点且审批通过，那下一个节点的状态：ready->process
                if (result.equals("approved")) {
                    ProcessFlow readyProcess = readyList.get(0);
                    readyProcess.setState("process");
                    processFlowDao.update(readyProcess);
                    //消息1: 通知表单提交人,部门经理已经审批通过,交由上级继续审批
                    String noticeContent1 = String.format("您的请假申请[%s-%s]%s%s已批准,审批意见:%s ,请继续等待上级审批",
                            sdf.format(form.getStartTime()), sdf.format(form.getEndTime()),
                            operator.getTitle(), operator.getName(), reason);
                    noticeDao.insert(new Notice(form.getEmployeeId(), noticeContent1));

                    //消息2: 通知总经理有新的审批任务
                    String noticeContent2 = String.format("%s-%s提起请假申请[%s-%s],请尽快审批",
                            employee.getTitle(), employee.getName(), sdf.format(form.getStartTime()), sdf.format(form.getEndTime()));
                    noticeDao.insert(new Notice(readyProcess.getOperatorId(), noticeContent2));

                    //消息3: 通知部门经理(当前经办人),员工的申请单你已批准,交由上级继续审批
                    String noticeContent3 = String.format("%s-%s提起请假申请[%s-%s]您已批准,审批意见:%s,申请转至上级领导继续审批",
                            employee.getTitle(), employee.getName(), sdf.format(form.getStartTime()), sdf.format(form.getEndTime()), reason);
                    noticeDao.insert(new Notice(operator.getEmployeeId(), noticeContent3));
                } else if (result.equals("refused")) {
                    //4.如果当前任务不是最后一个节点且审批驳回，则后续所有任务状态变为cancel，请假单状态变为refused
                    for (ProcessFlow p : readyList) {//遍历后续所有任务
                        p.setState("cancel");
                        processFlowDao.update(p);
                    }
                    form.setState("refused");
                    leaveFormDao.update(form);
                    //消息1: 通知申请人表单已被驳回
                    String noticeContent1 = String.format("您的请假申请[%s-%s]%s%s已驳回,审批意见:%s,审批流程已结束",
                            sdf.format(form.getStartTime()), sdf.format(form.getEndTime()),
                            operator.getTitle(), operator.getName(), reason);
                    noticeDao.insert(new Notice(form.getEmployeeId(), noticeContent1));

                    //消息2: 通知经办人表单"您已驳回"
                    String noticeContent2 = String.format("%s-%s提起请假申请[%s-%s]您已驳回,审批意见:%s,审批流程已结束",
                            employee.getTitle(), employee.getName(), sdf.format(form.getStartTime()), sdf.format(form.getEndTime()), reason);
                    noticeDao.insert(new Notice(operator.getEmployeeId(), noticeContent2));
                }
            }
            return null;
        });
    }
}

