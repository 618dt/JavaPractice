/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80021
 Source Host           : localhost:3306
 Source Schema         : lcg-oa

 Target Server Type    : MySQL
 Target Server Version : 80021
 File Encoding         : 65001

 Date: 23/02/2022 21:04:10
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for adm_department
-- ----------------------------
DROP TABLE IF EXISTS `adm_department`;
CREATE TABLE `adm_department`  (
  `department_id` bigint(0) NOT NULL AUTO_INCREMENT,
  `department_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`department_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of adm_department
-- ----------------------------
INSERT INTO `adm_department` VALUES (1, '总裁办');
INSERT INTO `adm_department` VALUES (2, '研发部');
INSERT INTO `adm_department` VALUES (3, '市场部');

-- ----------------------------
-- Table structure for adm_employee
-- ----------------------------
DROP TABLE IF EXISTS `adm_employee`;
CREATE TABLE `adm_employee`  (
  `employee_id` bigint(0) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `department_id` bigint(0) NOT NULL,
  `title` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `level` int(0) NOT NULL,
  PRIMARY KEY (`employee_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of adm_employee
-- ----------------------------
INSERT INTO `adm_employee` VALUES (1, '礼父', 1, '总经理', 8);
INSERT INTO `adm_employee` VALUES (2, '右磊', 2, '部门经理', 7);
INSERT INTO `adm_employee` VALUES (3, '刘莉莉', 2, '高级研发工程师', 6);
INSERT INTO `adm_employee` VALUES (4, '宋彩妮', 2, '研发工程师', 5);
INSERT INTO `adm_employee` VALUES (5, '欧阳致远', 2, '初级研发工程师', 4);
INSERT INTO `adm_employee` VALUES (6, '关浩', 3, '部门经理', 7);
INSERT INTO `adm_employee` VALUES (7, '方传输', 3, '大客户经理', 6);
INSERT INTO `adm_employee` VALUES (8, '柳国兴', 3, '客户经理', 5);
INSERT INTO `adm_employee` VALUES (9, '王峰', 3, '客户经理', 4);
INSERT INTO `adm_employee` VALUES (10, '曹亿彪', 3, '见习客户经理', 3);

-- ----------------------------
-- Table structure for adm_leave_form
-- ----------------------------
DROP TABLE IF EXISTS `adm_leave_form`;
CREATE TABLE `adm_leave_form`  (
  `form_id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '请假单编号',
  `employee_id` bigint(0) NOT NULL COMMENT '员工编号',
  `form_type` int(0) NOT NULL COMMENT '请假类型 1-事假 2-病假 3-工伤假 4-婚假 5-产假 6-丧假',
  `start_time` datetime(0) NOT NULL COMMENT '请假起始时间',
  `end_time` datetime(0) NOT NULL COMMENT '请假结束时间',
  `reason` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '请假事由',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `state` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'processing-正在审批 approved-审批通过 refused-审批被驳回',
  PRIMARY KEY (`form_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 39 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of adm_leave_form
-- ----------------------------
INSERT INTO `adm_leave_form` VALUES (34, 5, 1, '2021-08-24 00:00:00', '2021-08-27 00:00:00', '外出学习', '2021-08-24 23:46:37', 'refused');
INSERT INTO `adm_leave_form` VALUES (35, 4, 4, '2021-08-25 00:00:00', '2021-09-10 00:00:00', '回家结婚', '2021-08-24 23:50:54', 'approved');
INSERT INTO `adm_leave_form` VALUES (36, 3, 1, '2021-08-17 00:00:00', '2021-08-31 00:00:00', '项目出差', '2021-08-25 00:22:37', 'approved');
INSERT INTO `adm_leave_form` VALUES (37, 1, 1, '2021-09-01 00:00:00', '2021-09-30 00:00:00', '出国学习', '2021-08-25 00:25:42', 'approved');
INSERT INTO `adm_leave_form` VALUES (38, 1, 4, '2021-12-01 00:00:00', '2022-01-06 00:00:00', '儿子结婚', '2021-12-14 17:25:18', 'approved');
INSERT INTO `adm_leave_form` VALUES (39, 3, 1, '2021-12-28 00:00:00', '2021-12-30 00:00:00', '55555', '2021-12-14 17:27:39', 'refused');

-- ----------------------------
-- Table structure for adm_process_flow
-- ----------------------------
DROP TABLE IF EXISTS `adm_process_flow`;
CREATE TABLE `adm_process_flow`  (
  `process_id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '处理任务编号',
  `form_id` bigint(0) NOT NULL COMMENT '表单编号',
  `operator_id` bigint(0) NOT NULL COMMENT '经办人编号',
  `action` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'apply-申请 audit-审批',
  `result` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'approved-同意 refused-驳回',
  `reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '审批意见',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `audit_time` datetime(0) NULL DEFAULT NULL COMMENT '审批时间',
  `order_no` int(0) NOT NULL COMMENT '任务序号',
  `state` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'reday-准备 process-正在处理 complete-处理完成 cancel-取消',
  `is_last` int(0) NOT NULL COMMENT '是否最后节点 ，0-否 1-是',
  PRIMARY KEY (`process_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 96 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of adm_process_flow
-- ----------------------------
INSERT INTO `adm_process_flow` VALUES (82, 34, 5, 'apply', NULL, NULL, '2021-08-24 23:46:37', NULL, 1, 'complete', 0);
INSERT INTO `adm_process_flow` VALUES (83, 34, 2, 'audit', 'refused', '培训是在下个月', '2021-08-24 23:46:37', '2021-08-25 00:01:13', 2, 'complete', 0);
INSERT INTO `adm_process_flow` VALUES (84, 34, 1, 'audit', NULL, NULL, '2021-08-24 23:46:37', NULL, 3, 'cancel', 1);
INSERT INTO `adm_process_flow` VALUES (85, 35, 4, 'apply', NULL, NULL, '2021-08-24 23:50:54', NULL, 1, 'complete', 0);
INSERT INTO `adm_process_flow` VALUES (86, 35, 2, 'audit', 'approved', '同意，新婚快乐', '2021-08-24 23:50:54', '2021-08-25 00:02:05', 2, 'complete', 0);
INSERT INTO `adm_process_flow` VALUES (87, 35, 1, 'audit', 'approved', '同意，恭喜恭喜', '2021-08-24 23:50:54', '2021-08-25 00:09:00', 3, 'complete', 1);
INSERT INTO `adm_process_flow` VALUES (88, 36, 3, 'apply', NULL, NULL, '2021-08-25 00:22:37', NULL, 1, 'complete', 0);
INSERT INTO `adm_process_flow` VALUES (89, 36, 2, 'audit', 'approved', '好的', '2021-08-25 00:22:37', '2021-08-25 00:23:17', 2, 'complete', 0);
INSERT INTO `adm_process_flow` VALUES (90, 36, 1, 'audit', 'approved', '同意', '2021-08-25 00:22:37', '2021-08-25 00:23:52', 3, 'complete', 1);
INSERT INTO `adm_process_flow` VALUES (91, 37, 1, 'apply', NULL, NULL, '2021-08-25 00:25:42', NULL, 1, 'complete', 0);
INSERT INTO `adm_process_flow` VALUES (92, 37, 1, 'audit', 'approved', '自动通过', '2021-08-25 00:25:42', '2021-08-25 00:25:42', 2, 'complete', 1);
INSERT INTO `adm_process_flow` VALUES (93, 38, 1, 'apply', NULL, NULL, '2021-12-14 17:25:19', NULL, 1, 'complete', 0);
INSERT INTO `adm_process_flow` VALUES (94, 38, 1, 'audit', 'approved', '自动通过', '2021-12-14 17:25:19', '2021-12-14 17:25:19', 2, 'complete', 1);
INSERT INTO `adm_process_flow` VALUES (95, 39, 3, 'apply', NULL, NULL, '2021-12-14 17:27:39', NULL, 1, 'complete', 0);
INSERT INTO `adm_process_flow` VALUES (96, 39, 2, 'audit', 'refused', '原因不详', '2021-12-14 17:27:39', '2021-12-14 17:29:03', 2, 'complete', 1);

-- ----------------------------
-- Table structure for sys_node
-- ----------------------------
DROP TABLE IF EXISTS `sys_node`;
CREATE TABLE `sys_node`  (
  `node_id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '节点编号',
  `node_type` int(0) NOT NULL COMMENT '节点类型 1-模块 2-功能',
  `node_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '节点名称',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '功能地址',
  `node_code` int(0) NOT NULL COMMENT '节点编码，用于排序',
  `parent_id` bigint(0) NULL DEFAULT NULL COMMENT '上级节点编号',
  PRIMARY KEY (`node_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_node
-- ----------------------------
INSERT INTO `sys_node` VALUES (1, 1, '行政审批', NULL, 1000000, NULL);
INSERT INTO `sys_node` VALUES (2, 2, '通知公告', '/forward/notice', 1000001, 1);
INSERT INTO `sys_node` VALUES (3, 2, '请假申请', '/forward/form', 1000002, 1);
INSERT INTO `sys_node` VALUES (4, 2, '请假审批', '/forward/audit', 1000003, 1);

-- ----------------------------
-- Table structure for sys_notice
-- ----------------------------
DROP TABLE IF EXISTS `sys_notice`;
CREATE TABLE `sys_notice`  (
  `notice_id` bigint(0) NOT NULL AUTO_INCREMENT,
  `receiver_id` bigint(0) NOT NULL,
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `create_time` datetime(0) NOT NULL,
  PRIMARY KEY (`notice_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_notice
-- ----------------------------
INSERT INTO `sys_notice` VALUES (1, 5, '您的请假申请[2021-08-24-00时-2021-08-27-00时]已提交，请等待上级审批.', '2021-08-24 23:46:37');
INSERT INTO `sys_notice` VALUES (2, 2, '初级研发工程师-欧阳致远提起请假申请[2021-08-24-00时-2021-08-27-00时],请尽快审批', '2021-08-24 23:46:37');
INSERT INTO `sys_notice` VALUES (3, 4, '您的请假申请[2021-08-25-00时-2021-09-10-00时]已提交，请等待上级审批.', '2021-08-24 23:50:54');
INSERT INTO `sys_notice` VALUES (4, 2, '研发工程师-宋彩妮提起请假申请[2021-08-25-00时-2021-09-10-00时],请尽快审批', '2021-08-24 23:50:54');
INSERT INTO `sys_notice` VALUES (5, 5, '您的请假申请[2021-08-24-00时-2021-08-27-00时]部门经理右磊已驳回,审批意见:培训是在下个月,审批流程已结束', '2021-08-25 00:01:13');
INSERT INTO `sys_notice` VALUES (6, 2, '初级研发工程师-欧阳致远提起请假申请[2021-08-24-00时-2021-08-27-00时]您已驳回,审批意见:培训是在下个月,审批流程已结束', '2021-08-25 00:01:13');
INSERT INTO `sys_notice` VALUES (7, 4, '您的请假申请[2021-08-25-00时-2021-09-10-00时]部门经理右磊已批准,审批意见:同意，新婚快乐 ,请继续等待上级审批', '2021-08-25 00:02:05');
INSERT INTO `sys_notice` VALUES (8, 1, '研发工程师-宋彩妮提起请假申请[2021-08-25-00时-2021-09-10-00时],请尽快审批', '2021-08-25 00:02:05');
INSERT INTO `sys_notice` VALUES (9, 2, '研发工程师-宋彩妮提起请假申请[2021-08-25-00时-2021-09-10-00时]您已批准,审批意见:同意，新婚快乐,申请转至上级领导继续审批', '2021-08-25 00:02:05');
INSERT INTO `sys_notice` VALUES (10, 4, '您的请假申请[2021-08-25-00时-2021-09-10-00时]总经理礼父已null,审批意见:同意，恭喜恭喜,审批流程已结束', '2021-08-25 00:09:00');
INSERT INTO `sys_notice` VALUES (11, 1, '研发工程师-宋彩妮提起请假申请[2021-08-25-00时-2021-09-10-00时]您已null,审批意见:同意，恭喜恭喜,审批流程已结束', '2021-08-25 00:09:00');
INSERT INTO `sys_notice` VALUES (12, 3, '您的请假申请[2021-08-17-00时-2021-08-31-00时]已提交，请等待上级审批.', '2021-08-25 00:22:37');
INSERT INTO `sys_notice` VALUES (13, 2, '高级研发工程师-刘莉莉提起请假申请[2021-08-17-00时-2021-08-31-00时],请尽快审批', '2021-08-25 00:22:37');
INSERT INTO `sys_notice` VALUES (14, 3, '您的请假申请[2021-08-17-00时-2021-08-31-00时]部门经理右磊已批准,审批意见:好的 ,请继续等待上级审批', '2021-08-25 00:23:17');
INSERT INTO `sys_notice` VALUES (15, 1, '高级研发工程师-刘莉莉提起请假申请[2021-08-17-00时-2021-08-31-00时],请尽快审批', '2021-08-25 00:23:17');
INSERT INTO `sys_notice` VALUES (16, 2, '高级研发工程师-刘莉莉提起请假申请[2021-08-17-00时-2021-08-31-00时]您已批准,审批意见:好的,申请转至上级领导继续审批', '2021-08-25 00:23:17');
INSERT INTO `sys_notice` VALUES (17, 3, '您的请假申请[2021-08-17-00时-2021-08-31-00时]总经理-礼父已批准,审批意见:同意,审批流程已结束', '2021-08-25 00:23:52');
INSERT INTO `sys_notice` VALUES (18, 1, '高级研发工程师-刘莉莉提起请假申请[2021-08-17-00时-2021-08-31-00时]您已批准,审批意见:同意,审批流程已结束', '2021-08-25 00:23:52');
INSERT INTO `sys_notice` VALUES (19, 1, '您的请假申请[2021-09-01-00时-2021-09-30-00时]已自动批准通过.', '2021-08-25 00:25:42');
INSERT INTO `sys_notice` VALUES (20, 1, '您的请假申请[2021-12-01-00时-2022-01-06-00时]已自动批准通过.', '2021-12-14 17:25:19');
INSERT INTO `sys_notice` VALUES (21, 3, '您的请假申请[2021-12-28-00时-2021-12-30-00时]已提交，请等待上级审批.', '2021-12-14 17:27:39');
INSERT INTO `sys_notice` VALUES (22, 2, '高级研发工程师-刘莉莉提起请假申请[2021-12-28-00时-2021-12-30-00时],请尽快审批', '2021-12-14 17:27:39');
INSERT INTO `sys_notice` VALUES (23, 3, '您的请假申请[2021-12-28-00时-2021-12-30-00时]部门经理-右磊已驳回,审批意见:原因不详,审批流程已结束', '2021-12-14 17:29:03');
INSERT INTO `sys_notice` VALUES (24, 2, '高级研发工程师-刘莉莉提起请假申请[2021-12-28-00时-2021-12-30-00时]您已驳回,审批意见:原因不详,审批流程已结束', '2021-12-14 17:29:03');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `role_id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '角色编号',
  `role_description` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色描述',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '业务岗角色');
INSERT INTO `sys_role` VALUES (2, '管理岗角色');

-- ----------------------------
-- Table structure for sys_role_node
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_node`;
CREATE TABLE `sys_role_node`  (
  `rn_id` bigint(0) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(0) NOT NULL,
  `node_id` bigint(0) NOT NULL,
  PRIMARY KEY (`rn_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_node
-- ----------------------------
INSERT INTO `sys_role_node` VALUES (1, 1, 1);
INSERT INTO `sys_role_node` VALUES (2, 1, 2);
INSERT INTO `sys_role_node` VALUES (3, 1, 3);
INSERT INTO `sys_role_node` VALUES (4, 2, 1);
INSERT INTO `sys_role_node` VALUES (5, 2, 2);
INSERT INTO `sys_role_node` VALUES (6, 2, 3);
INSERT INTO `sys_role_node` VALUES (7, 2, 4);

-- ----------------------------
-- Table structure for sys_role_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_user`;
CREATE TABLE `sys_role_user`  (
  `ru_id` bigint(0) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(0) NOT NULL,
  `user_id` int(0) NOT NULL,
  PRIMARY KEY (`ru_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_user
-- ----------------------------
INSERT INTO `sys_role_user` VALUES (1, 2, 1);
INSERT INTO `sys_role_user` VALUES (2, 2, 2);
INSERT INTO `sys_role_user` VALUES (3, 1, 3);
INSERT INTO `sys_role_user` VALUES (4, 1, 4);
INSERT INTO `sys_role_user` VALUES (5, 1, 5);
INSERT INTO `sys_role_user` VALUES (6, 2, 6);
INSERT INTO `sys_role_user` VALUES (7, 1, 7);
INSERT INTO `sys_role_user` VALUES (8, 1, 8);
INSERT INTO `sys_role_user` VALUES (9, 1, 9);
INSERT INTO `sys_role_user` VALUES (10, 1, 10);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `user_id` bigint(0) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `employee_id` bigint(0) NOT NULL,
  `salt` int(0) NOT NULL,
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'm8', '868816a5f21d00bef493e895824a4f51', 1, 182);
INSERT INTO `sys_user` VALUES (2, 't7', '713c2c9be62332b9ff9abbf4eb62e26d', 2, 183);
INSERT INTO `sys_user` VALUES (3, 't6', 'c671ea0b7bd6f440e765d99cc0f699aa', 3, 184);
INSERT INTO `sys_user` VALUES (4, 't5', '7b8a86eabcb10b1a94941d47baea917a', 4, 185);
INSERT INTO `sys_user` VALUES (5, 't4', '3698917e0c4e9f81aa0d030c3603286b', 5, 186);
INSERT INTO `sys_user` VALUES (6, 's7', 'b7a947d1e1b67ba9595f245cc78ae5d4', 6, 187);
INSERT INTO `sys_user` VALUES (7, 's6', 'f57e762e3fb7e1e3ec8ec4db6a1248e1', 7, 188);
INSERT INTO `sys_user` VALUES (8, 's5', 'cdc4e2723c762749d14d627a4b77306a', 8, 181);
INSERT INTO `sys_user` VALUES (9, 's4', 'b1ea83100514cb02b365f3a80a14ead4', 9, 180);
INSERT INTO `sys_user` VALUES (10, 's3', 'dcfa022748271dccf5532c834e98ad08', 10, 189);

SET FOREIGN_KEY_CHECKS = 1;
