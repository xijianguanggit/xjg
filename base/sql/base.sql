/*
Navicat MySQL Data Transfer

Source Server         : 49.4.4.78
Source Server Version : 50638
Source Host           : 49.4.4.78:3306
Source Database       : base-web

Target Server Type    : MYSQL
Target Server Version : 50638
File Encoding         : 65001

Date: 2018-07-17 13:54:10
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `t_access_log`
-- ----------------------------
DROP TABLE IF EXISTS `t_access_log`;
CREATE TABLE `t_access_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `access_time` datetime NOT NULL COMMENT '访问时间',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `emp_id` bigint(20) NOT NULL COMMENT '人员ID',
  `url` varchar(200) NOT NULL COMMENT '访问的URL',
  `client_ip` varchar(40) NOT NULL COMMENT '客户端IP',
  `user_agent` varchar(200) NOT NULL COMMENT 'useragent',
  `server_ip` varchar(40) NOT NULL COMMENT '服务器端IP',
  `server_port` varchar(5) NOT NULL COMMENT '服务器端端口',
  `result` varchar(50) NOT NULL COMMENT '响应结果',
  `cost` bigint(20) NOT NULL COMMENT '耗时毫秒',
  `request` varchar(10000) DEFAULT NULL,
  `respones` mediumtext,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=909 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_access_log
-- ----------------------------

-- ----------------------------
-- Table structure for `t_assessment`
-- ----------------------------
DROP TABLE IF EXISTS `t_assessment`;
CREATE TABLE `t_assessment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `emp_id` bigint(20) NOT NULL COMMENT '人员ID',
  `start_date` datetime NOT NULL COMMENT '开始日期',
  `name` varchar(50) NOT NULL COMMENT '考核名称',
  `result` varchar(200) NOT NULL COMMENT '考核结果',
  `score` varchar(50) NOT NULL COMMENT '考核成绩',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint(50) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(50) DEFAULT NULL COMMENT '更新人',
  `update_count` tinyint(6) DEFAULT '0' COMMENT '版本号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_assessment
-- ----------------------------

-- ----------------------------
-- Table structure for `t_attence_check`
-- ----------------------------
DROP TABLE IF EXISTS `t_attence_check`;
CREATE TABLE `t_attence_check` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` varchar(50) NOT NULL COMMENT '标题',
  `start_time` datetime NOT NULL COMMENT '开始日期',
  `end_time` datetime NOT NULL COMMENT '结束日期',
  `emp_id` bigint(20) NOT NULL COMMENT '部门ID',
  `create_by` bigint(20) DEFAULT NULL COMMENT '考勤人ID',
  `remark` varchar(100) DEFAULT NULL COMMENT '说明',
  `check_status` bigint(20) DEFAULT NULL COMMENT '审核状态',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(50) DEFAULT NULL COMMENT '更新人',
  `update_count` tinyint(6) DEFAULT '0' COMMENT '版本号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_attence_check
-- ----------------------------

-- ----------------------------
-- Table structure for `t_attence_check_detail`
-- ----------------------------
DROP TABLE IF EXISTS `t_attence_check_detail`;
CREATE TABLE `t_attence_check_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `attence_id` bigint(20) NOT NULL COMMENT '主表ID',
  `user_id` bigint(20) NOT NULL COMMENT '人员ID',
  `commit_day` bigint(20) NOT NULL COMMENT '提交工作日报天数',
  `commit_hours` float(20,2) NOT NULL COMMENT '确认的工作小时数',
  `attence_day` float(20,2) NOT NULL COMMENT '确认出勤天数',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint(50) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(50) DEFAULT NULL COMMENT '更新人',
  `update_count` tinyint(6) DEFAULT '0' COMMENT '版本号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_attence_check_detail
-- ----------------------------

-- ----------------------------
-- Table structure for `t_auth_key`
-- ----------------------------
DROP TABLE IF EXISTS `t_auth_key`;
CREATE TABLE `t_auth_key` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `app_id` varchar(50) DEFAULT NULL,
  `app_key` varchar(50) DEFAULT NULL,
  `app_secret` varchar(50) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `create_by` bigint(20) DEFAULT NULL,
  `is_cancel` smallint(2) DEFAULT '0',
  `cancel_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_auth_key
-- ----------------------------
INSERT INTO `t_auth_key` VALUES ('1', '47', 'crmui', '26db88a4dfc34cebaf196e68761c1294ac4ce265', '2018-07-03 16:15:46', '1', '0', null);

-- ----------------------------
-- Table structure for `t_calendar`
-- ----------------------------
DROP TABLE IF EXISTS `t_calendar`;
CREATE TABLE `t_calendar` (
  `id` bigint(32) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `date` date DEFAULT NULL COMMENT '日期',
  `day_en` varchar(32) DEFAULT NULL COMMENT '星期英文',
  `day_cn` varchar(32) DEFAULT NULL COMMENT '星期中文',
  `workday` varchar(2) DEFAULT NULL COMMENT '是否是工作日，1是工作日，0非工作日',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_calendar
-- ----------------------------

-- ----------------------------
-- Table structure for `t_change_log`
-- ----------------------------
DROP TABLE IF EXISTS `t_change_log`;
CREATE TABLE `t_change_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `flow_id` varchar(32) DEFAULT NULL,
  `url` varchar(50) DEFAULT NULL,
  `proc` varchar(50) DEFAULT NULL,
  `type` varchar(20) DEFAULT NULL,
  `entity_id` varchar(50) DEFAULT NULL,
  `entity_name` varchar(50) DEFAULT NULL,
  `old_content` varchar(4000) DEFAULT NULL,
  `new_content` varchar(4000) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `create_by` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9858 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_change_log
-- ----------------------------

-- ----------------------------
-- Table structure for `t_city`
-- ----------------------------
DROP TABLE IF EXISTS `t_city`;
CREATE TABLE `t_city` (
  `code` varchar(20) NOT NULL COMMENT '编码',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `level` smallint(6) NOT NULL COMMENT '层级',
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_city
-- ----------------------------
INSERT INTO `t_city` VALUES ('110000', '北京市', '1');
INSERT INTO `t_city` VALUES ('110100', '市辖区', '2');
INSERT INTO `t_city` VALUES ('110101', '东城区', '3');
INSERT INTO `t_city` VALUES ('110102', '西城区', '3');
INSERT INTO `t_city` VALUES ('110105', '朝阳区', '3');
INSERT INTO `t_city` VALUES ('110106', '丰台区', '3');
INSERT INTO `t_city` VALUES ('110107', '石景山区', '3');
INSERT INTO `t_city` VALUES ('110108', '海淀区', '3');
INSERT INTO `t_city` VALUES ('110109', '门头沟区', '3');
INSERT INTO `t_city` VALUES ('110111', '房山区', '3');
INSERT INTO `t_city` VALUES ('110112', '通州区', '3');
INSERT INTO `t_city` VALUES ('110113', '顺义区', '3');
INSERT INTO `t_city` VALUES ('110114', '昌平区', '3');
INSERT INTO `t_city` VALUES ('110115', '大兴区', '3');
INSERT INTO `t_city` VALUES ('110116', '怀柔区', '3');
INSERT INTO `t_city` VALUES ('110117', '平谷区', '3');
INSERT INTO `t_city` VALUES ('110118', '密云区', '3');
INSERT INTO `t_city` VALUES ('110119', '延庆区', '3');
INSERT INTO `t_city` VALUES ('120000', '天津市', '1');
INSERT INTO `t_city` VALUES ('120100', '市辖区', '2');
INSERT INTO `t_city` VALUES ('120101', '和平区', '3');
INSERT INTO `t_city` VALUES ('120102', '河东区', '3');
INSERT INTO `t_city` VALUES ('120103', '河西区', '3');
INSERT INTO `t_city` VALUES ('120104', '南开区', '3');
INSERT INTO `t_city` VALUES ('120105', '河北区', '3');
INSERT INTO `t_city` VALUES ('120106', '红桥区', '3');
INSERT INTO `t_city` VALUES ('120110', '东丽区', '3');
INSERT INTO `t_city` VALUES ('120111', '西青区', '3');
INSERT INTO `t_city` VALUES ('120112', '津南区', '3');
INSERT INTO `t_city` VALUES ('120113', '北辰区', '3');
INSERT INTO `t_city` VALUES ('120114', '武清区', '3');
INSERT INTO `t_city` VALUES ('120115', '宝坻区', '3');
INSERT INTO `t_city` VALUES ('120116', '滨海新区', '3');
INSERT INTO `t_city` VALUES ('120117', '宁河区', '3');
INSERT INTO `t_city` VALUES ('120118', '静海区', '3');
INSERT INTO `t_city` VALUES ('120119', '蓟州区', '3');
INSERT INTO `t_city` VALUES ('130000', '河北省', '1');
INSERT INTO `t_city` VALUES ('130100', '石家庄市', '2');
INSERT INTO `t_city` VALUES ('130101', '市辖区', '3');
INSERT INTO `t_city` VALUES ('130102', '长安区', '3');
INSERT INTO `t_city` VALUES ('130104', '桥西区', '3');
INSERT INTO `t_city` VALUES ('130105', '新华区', '3');
INSERT INTO `t_city` VALUES ('130107', '井陉矿区', '3');
INSERT INTO `t_city` VALUES ('130108', '裕华区', '3');
INSERT INTO `t_city` VALUES ('130109', '藁城区', '3');
INSERT INTO `t_city` VALUES ('130110', '鹿泉区', '3');
INSERT INTO `t_city` VALUES ('130111', '栾城区', '3');
INSERT INTO `t_city` VALUES ('130121', '井陉县', '3');
INSERT INTO `t_city` VALUES ('130123', '正定县', '3');
INSERT INTO `t_city` VALUES ('130125', '行唐县', '3');
INSERT INTO `t_city` VALUES ('130126', '灵寿县', '3');
INSERT INTO `t_city` VALUES ('130127', '高邑县', '3');
INSERT INTO `t_city` VALUES ('130128', '深泽县', '3');
INSERT INTO `t_city` VALUES ('130129', '赞皇县', '3');
INSERT INTO `t_city` VALUES ('130130', '无极县', '3');
INSERT INTO `t_city` VALUES ('130131', '平山县', '3');
INSERT INTO `t_city` VALUES ('130132', '元氏县', '3');
INSERT INTO `t_city` VALUES ('130133', '赵县', '3');
INSERT INTO `t_city` VALUES ('130183', '晋州市', '3');
INSERT INTO `t_city` VALUES ('130184', '新乐市', '3');
INSERT INTO `t_city` VALUES ('130200', '唐山市', '2');
INSERT INTO `t_city` VALUES ('130201', '市辖区', '3');
INSERT INTO `t_city` VALUES ('130202', '路南区', '3');
INSERT INTO `t_city` VALUES ('130203', '路北区', '3');
INSERT INTO `t_city` VALUES ('130204', '古冶区', '3');
INSERT INTO `t_city` VALUES ('130205', '开平区', '3');
INSERT INTO `t_city` VALUES ('130207', '丰南区', '3');
INSERT INTO `t_city` VALUES ('130208', '丰润区', '3');
INSERT INTO `t_city` VALUES ('130209', '曹妃甸区', '3');
INSERT INTO `t_city` VALUES ('130223', '滦县', '3');
INSERT INTO `t_city` VALUES ('130224', '滦南县', '3');
INSERT INTO `t_city` VALUES ('130225', '乐亭县', '3');
INSERT INTO `t_city` VALUES ('130227', '迁西县', '3');
INSERT INTO `t_city` VALUES ('130229', '玉田县', '3');
INSERT INTO `t_city` VALUES ('130281', '遵化市', '3');
INSERT INTO `t_city` VALUES ('130283', '迁安市', '3');
INSERT INTO `t_city` VALUES ('130300', '秦皇岛市', '2');
INSERT INTO `t_city` VALUES ('130301', '市辖区', '3');
INSERT INTO `t_city` VALUES ('130302', '海港区', '3');
INSERT INTO `t_city` VALUES ('130303', '山海关区', '3');
INSERT INTO `t_city` VALUES ('130304', '北戴河区', '3');
INSERT INTO `t_city` VALUES ('130306', '抚宁区', '3');
INSERT INTO `t_city` VALUES ('130321', '青龙满族自治县', '3');
INSERT INTO `t_city` VALUES ('130322', '昌黎县', '3');
INSERT INTO `t_city` VALUES ('130324', '卢龙县', '3');
INSERT INTO `t_city` VALUES ('130400', '邯郸市', '2');
INSERT INTO `t_city` VALUES ('130401', '市辖区', '3');
INSERT INTO `t_city` VALUES ('130402', '邯山区', '3');
INSERT INTO `t_city` VALUES ('130403', '丛台区', '3');
INSERT INTO `t_city` VALUES ('130404', '复兴区', '3');
INSERT INTO `t_city` VALUES ('130406', '峰峰矿区', '3');
INSERT INTO `t_city` VALUES ('130421', '邯郸县', '3');
INSERT INTO `t_city` VALUES ('130423', '临漳县', '3');
INSERT INTO `t_city` VALUES ('130424', '成安县', '3');
INSERT INTO `t_city` VALUES ('130425', '大名县', '3');
INSERT INTO `t_city` VALUES ('130426', '涉县', '3');
INSERT INTO `t_city` VALUES ('130427', '磁县', '3');
INSERT INTO `t_city` VALUES ('130428', '肥乡县', '3');
INSERT INTO `t_city` VALUES ('130429', '永年县', '3');
INSERT INTO `t_city` VALUES ('130430', '邱县', '3');
INSERT INTO `t_city` VALUES ('130431', '鸡泽县', '3');
INSERT INTO `t_city` VALUES ('130432', '广平县', '3');
INSERT INTO `t_city` VALUES ('130433', '馆陶县', '3');
INSERT INTO `t_city` VALUES ('130434', '魏县', '3');
INSERT INTO `t_city` VALUES ('130435', '曲周县', '3');
INSERT INTO `t_city` VALUES ('130481', '武安市', '3');
INSERT INTO `t_city` VALUES ('130500', '邢台市', '2');
INSERT INTO `t_city` VALUES ('130501', '市辖区', '3');
INSERT INTO `t_city` VALUES ('130502', '桥东区', '3');
INSERT INTO `t_city` VALUES ('130503', '桥西区', '3');
INSERT INTO `t_city` VALUES ('130521', '邢台县', '3');
INSERT INTO `t_city` VALUES ('130522', '临城县', '3');
INSERT INTO `t_city` VALUES ('130523', '内丘县', '3');
INSERT INTO `t_city` VALUES ('130524', '柏乡县', '3');
INSERT INTO `t_city` VALUES ('130525', '隆尧县', '3');
INSERT INTO `t_city` VALUES ('130526', '任县', '3');
INSERT INTO `t_city` VALUES ('130527', '南和县', '3');
INSERT INTO `t_city` VALUES ('130528', '宁晋县', '3');
INSERT INTO `t_city` VALUES ('130529', '巨鹿县', '3');
INSERT INTO `t_city` VALUES ('130530', '新河县', '3');
INSERT INTO `t_city` VALUES ('130531', '广宗县', '3');
INSERT INTO `t_city` VALUES ('130532', '平乡县', '3');
INSERT INTO `t_city` VALUES ('130533', '威县', '3');
INSERT INTO `t_city` VALUES ('130534', '清河县', '3');
INSERT INTO `t_city` VALUES ('130535', '临西县', '3');
INSERT INTO `t_city` VALUES ('130581', '南宫市', '3');
INSERT INTO `t_city` VALUES ('130582', '沙河市', '3');
INSERT INTO `t_city` VALUES ('130600', '保定市', '2');
INSERT INTO `t_city` VALUES ('130601', '市辖区', '3');
INSERT INTO `t_city` VALUES ('130602', '竞秀区', '3');
INSERT INTO `t_city` VALUES ('130606', '莲池区', '3');
INSERT INTO `t_city` VALUES ('130607', '满城区', '3');
INSERT INTO `t_city` VALUES ('130608', '清苑区', '3');
INSERT INTO `t_city` VALUES ('130609', '徐水区', '3');
INSERT INTO `t_city` VALUES ('130623', '涞水县', '3');
INSERT INTO `t_city` VALUES ('130624', '阜平县', '3');
INSERT INTO `t_city` VALUES ('130626', '定兴县', '3');
INSERT INTO `t_city` VALUES ('130627', '唐县', '3');
INSERT INTO `t_city` VALUES ('130628', '高阳县', '3');
INSERT INTO `t_city` VALUES ('130629', '容城县', '3');
INSERT INTO `t_city` VALUES ('130630', '涞源县', '3');
INSERT INTO `t_city` VALUES ('130631', '望都县', '3');
INSERT INTO `t_city` VALUES ('130632', '安新县', '3');
INSERT INTO `t_city` VALUES ('130633', '易县', '3');
INSERT INTO `t_city` VALUES ('130634', '曲阳县', '3');
INSERT INTO `t_city` VALUES ('130635', '蠡县', '3');
INSERT INTO `t_city` VALUES ('130636', '顺平县', '3');
INSERT INTO `t_city` VALUES ('130637', '博野县', '3');
INSERT INTO `t_city` VALUES ('130638', '雄县', '3');
INSERT INTO `t_city` VALUES ('130681', '涿州市', '3');
INSERT INTO `t_city` VALUES ('130683', '安国市', '3');
INSERT INTO `t_city` VALUES ('130684', '高碑店市', '3');
INSERT INTO `t_city` VALUES ('130700', '张家口市', '2');
INSERT INTO `t_city` VALUES ('130701', '市辖区', '3');
INSERT INTO `t_city` VALUES ('130702', '桥东区', '3');
INSERT INTO `t_city` VALUES ('130703', '桥西区', '3');
INSERT INTO `t_city` VALUES ('130705', '宣化区', '3');
INSERT INTO `t_city` VALUES ('130706', '下花园区', '3');
INSERT INTO `t_city` VALUES ('130708', '万全区', '3');
INSERT INTO `t_city` VALUES ('130709', '崇礼区', '3');
INSERT INTO `t_city` VALUES ('130722', '张北县', '3');
INSERT INTO `t_city` VALUES ('130723', '康保县', '3');
INSERT INTO `t_city` VALUES ('130724', '沽源县', '3');
INSERT INTO `t_city` VALUES ('130725', '尚义县', '3');
INSERT INTO `t_city` VALUES ('130726', '蔚县', '3');
INSERT INTO `t_city` VALUES ('130727', '阳原县', '3');
INSERT INTO `t_city` VALUES ('130728', '怀安县', '3');
INSERT INTO `t_city` VALUES ('130730', '怀来县', '3');
INSERT INTO `t_city` VALUES ('130731', '涿鹿县', '3');
INSERT INTO `t_city` VALUES ('130732', '赤城县', '3');
INSERT INTO `t_city` VALUES ('130800', '承德市', '2');
INSERT INTO `t_city` VALUES ('130801', '市辖区', '3');
INSERT INTO `t_city` VALUES ('130802', '双桥区', '3');
INSERT INTO `t_city` VALUES ('130803', '双滦区', '3');
INSERT INTO `t_city` VALUES ('130804', '鹰手营子矿区', '3');
INSERT INTO `t_city` VALUES ('130821', '承德县', '3');
INSERT INTO `t_city` VALUES ('130822', '兴隆县', '3');
INSERT INTO `t_city` VALUES ('130823', '平泉县', '3');
INSERT INTO `t_city` VALUES ('130824', '滦平县', '3');
INSERT INTO `t_city` VALUES ('130825', '隆化县', '3');
INSERT INTO `t_city` VALUES ('130826', '丰宁满族自治县', '3');
INSERT INTO `t_city` VALUES ('130827', '宽城满族自治县', '3');
INSERT INTO `t_city` VALUES ('130828', '围场满族蒙古族自治县', '3');
INSERT INTO `t_city` VALUES ('130900', '沧州市', '2');
INSERT INTO `t_city` VALUES ('130901', '市辖区', '3');
INSERT INTO `t_city` VALUES ('130902', '新华区', '3');
INSERT INTO `t_city` VALUES ('130903', '运河区', '3');
INSERT INTO `t_city` VALUES ('130921', '沧县', '3');
INSERT INTO `t_city` VALUES ('130922', '青县', '3');
INSERT INTO `t_city` VALUES ('130923', '东光县', '3');
INSERT INTO `t_city` VALUES ('130924', '海兴县', '3');
INSERT INTO `t_city` VALUES ('130925', '盐山县', '3');
INSERT INTO `t_city` VALUES ('130926', '肃宁县', '3');
INSERT INTO `t_city` VALUES ('130927', '南皮县', '3');
INSERT INTO `t_city` VALUES ('130928', '吴桥县', '3');
INSERT INTO `t_city` VALUES ('130929', '献县', '3');
INSERT INTO `t_city` VALUES ('130930', '孟村回族自治县', '3');
INSERT INTO `t_city` VALUES ('130981', '泊头市', '3');
INSERT INTO `t_city` VALUES ('130982', '任丘市', '3');
INSERT INTO `t_city` VALUES ('130983', '黄骅市', '3');
INSERT INTO `t_city` VALUES ('130984', '河间市', '3');
INSERT INTO `t_city` VALUES ('131000', '廊坊市', '2');
INSERT INTO `t_city` VALUES ('131001', '市辖区', '3');
INSERT INTO `t_city` VALUES ('131002', '安次区', '3');
INSERT INTO `t_city` VALUES ('131003', '广阳区', '3');
INSERT INTO `t_city` VALUES ('131022', '固安县', '3');
INSERT INTO `t_city` VALUES ('131023', '永清县', '3');
INSERT INTO `t_city` VALUES ('131024', '香河县', '3');
INSERT INTO `t_city` VALUES ('131025', '大城县', '3');
INSERT INTO `t_city` VALUES ('131026', '文安县', '3');
INSERT INTO `t_city` VALUES ('131028', '大厂回族自治县', '3');
INSERT INTO `t_city` VALUES ('131081', '霸州市', '3');
INSERT INTO `t_city` VALUES ('131082', '三河市', '3');
INSERT INTO `t_city` VALUES ('131100', '衡水市', '2');
INSERT INTO `t_city` VALUES ('131101', '市辖区', '3');
INSERT INTO `t_city` VALUES ('131102', '桃城区', '3');
INSERT INTO `t_city` VALUES ('131103', '冀州区', '3');
INSERT INTO `t_city` VALUES ('131121', '枣强县', '3');
INSERT INTO `t_city` VALUES ('131122', '武邑县', '3');
INSERT INTO `t_city` VALUES ('131123', '武强县', '3');
INSERT INTO `t_city` VALUES ('131124', '饶阳县', '3');
INSERT INTO `t_city` VALUES ('131125', '安平县', '3');
INSERT INTO `t_city` VALUES ('131126', '故城县', '3');
INSERT INTO `t_city` VALUES ('131127', '景县', '3');
INSERT INTO `t_city` VALUES ('131128', '阜城县', '3');
INSERT INTO `t_city` VALUES ('131182', '深州市', '3');
INSERT INTO `t_city` VALUES ('139000', '省直辖县级行政区划', '2');
INSERT INTO `t_city` VALUES ('139001', '定州市', '3');
INSERT INTO `t_city` VALUES ('139002', '辛集市', '3');
INSERT INTO `t_city` VALUES ('140000', '山西省', '1');
INSERT INTO `t_city` VALUES ('140100', '太原市', '2');
INSERT INTO `t_city` VALUES ('140101', '市辖区', '3');
INSERT INTO `t_city` VALUES ('140105', '小店区', '3');
INSERT INTO `t_city` VALUES ('140106', '迎泽区', '3');
INSERT INTO `t_city` VALUES ('140107', '杏花岭区', '3');
INSERT INTO `t_city` VALUES ('140108', '尖草坪区', '3');
INSERT INTO `t_city` VALUES ('140109', '万柏林区', '3');
INSERT INTO `t_city` VALUES ('140110', '晋源区', '3');
INSERT INTO `t_city` VALUES ('140121', '清徐县', '3');
INSERT INTO `t_city` VALUES ('140122', '阳曲县', '3');
INSERT INTO `t_city` VALUES ('140123', '娄烦县', '3');
INSERT INTO `t_city` VALUES ('140181', '古交市', '3');
INSERT INTO `t_city` VALUES ('140200', '大同市', '2');
INSERT INTO `t_city` VALUES ('140201', '市辖区', '3');
INSERT INTO `t_city` VALUES ('140202', '城区', '3');
INSERT INTO `t_city` VALUES ('140203', '矿区', '3');
INSERT INTO `t_city` VALUES ('140211', '南郊区', '3');
INSERT INTO `t_city` VALUES ('140212', '新荣区', '3');
INSERT INTO `t_city` VALUES ('140221', '阳高县', '3');
INSERT INTO `t_city` VALUES ('140222', '天镇县', '3');
INSERT INTO `t_city` VALUES ('140223', '广灵县', '3');
INSERT INTO `t_city` VALUES ('140224', '灵丘县', '3');
INSERT INTO `t_city` VALUES ('140225', '浑源县', '3');
INSERT INTO `t_city` VALUES ('140226', '左云县', '3');
INSERT INTO `t_city` VALUES ('140227', '大同县', '3');
INSERT INTO `t_city` VALUES ('140300', '阳泉市', '2');
INSERT INTO `t_city` VALUES ('140301', '市辖区', '3');
INSERT INTO `t_city` VALUES ('140302', '城区', '3');
INSERT INTO `t_city` VALUES ('140303', '矿区', '3');
INSERT INTO `t_city` VALUES ('140311', '郊区', '3');
INSERT INTO `t_city` VALUES ('140321', '平定县', '3');
INSERT INTO `t_city` VALUES ('140322', '盂县', '3');
INSERT INTO `t_city` VALUES ('140400', '长治市', '2');
INSERT INTO `t_city` VALUES ('140401', '市辖区', '3');
INSERT INTO `t_city` VALUES ('140402', '城区', '3');
INSERT INTO `t_city` VALUES ('140411', '郊区', '3');
INSERT INTO `t_city` VALUES ('140421', '长治县', '3');
INSERT INTO `t_city` VALUES ('140423', '襄垣县', '3');
INSERT INTO `t_city` VALUES ('140424', '屯留县', '3');
INSERT INTO `t_city` VALUES ('140425', '平顺县', '3');
INSERT INTO `t_city` VALUES ('140426', '黎城县', '3');
INSERT INTO `t_city` VALUES ('140427', '壶关县', '3');
INSERT INTO `t_city` VALUES ('140428', '长子县', '3');
INSERT INTO `t_city` VALUES ('140429', '武乡县', '3');
INSERT INTO `t_city` VALUES ('140430', '沁县', '3');
INSERT INTO `t_city` VALUES ('140431', '沁源县', '3');
INSERT INTO `t_city` VALUES ('140481', '潞城市', '3');
INSERT INTO `t_city` VALUES ('140500', '晋城市', '2');
INSERT INTO `t_city` VALUES ('140501', '市辖区', '3');
INSERT INTO `t_city` VALUES ('140502', '城区', '3');
INSERT INTO `t_city` VALUES ('140521', '沁水县', '3');
INSERT INTO `t_city` VALUES ('140522', '阳城县', '3');
INSERT INTO `t_city` VALUES ('140524', '陵川县', '3');
INSERT INTO `t_city` VALUES ('140525', '泽州县', '3');
INSERT INTO `t_city` VALUES ('140581', '高平市', '3');
INSERT INTO `t_city` VALUES ('140600', '朔州市', '2');
INSERT INTO `t_city` VALUES ('140601', '市辖区', '3');
INSERT INTO `t_city` VALUES ('140602', '朔城区', '3');
INSERT INTO `t_city` VALUES ('140603', '平鲁区', '3');
INSERT INTO `t_city` VALUES ('140621', '山阴县', '3');
INSERT INTO `t_city` VALUES ('140622', '应县', '3');
INSERT INTO `t_city` VALUES ('140623', '右玉县', '3');
INSERT INTO `t_city` VALUES ('140624', '怀仁县', '3');
INSERT INTO `t_city` VALUES ('140700', '晋中市', '2');
INSERT INTO `t_city` VALUES ('140701', '市辖区', '3');
INSERT INTO `t_city` VALUES ('140702', '榆次区', '3');
INSERT INTO `t_city` VALUES ('140721', '榆社县', '3');
INSERT INTO `t_city` VALUES ('140722', '左权县', '3');
INSERT INTO `t_city` VALUES ('140723', '和顺县', '3');
INSERT INTO `t_city` VALUES ('140724', '昔阳县', '3');
INSERT INTO `t_city` VALUES ('140725', '寿阳县', '3');
INSERT INTO `t_city` VALUES ('140726', '太谷县', '3');
INSERT INTO `t_city` VALUES ('140727', '祁县', '3');
INSERT INTO `t_city` VALUES ('140728', '平遥县', '3');
INSERT INTO `t_city` VALUES ('140729', '灵石县', '3');
INSERT INTO `t_city` VALUES ('140781', '介休市', '3');
INSERT INTO `t_city` VALUES ('140800', '运城市', '2');
INSERT INTO `t_city` VALUES ('140801', '市辖区', '3');
INSERT INTO `t_city` VALUES ('140802', '盐湖区', '3');
INSERT INTO `t_city` VALUES ('140821', '临猗县', '3');
INSERT INTO `t_city` VALUES ('140822', '万荣县', '3');
INSERT INTO `t_city` VALUES ('140823', '闻喜县', '3');
INSERT INTO `t_city` VALUES ('140824', '稷山县', '3');
INSERT INTO `t_city` VALUES ('140825', '新绛县', '3');
INSERT INTO `t_city` VALUES ('140826', '绛县', '3');
INSERT INTO `t_city` VALUES ('140827', '垣曲县', '3');
INSERT INTO `t_city` VALUES ('140828', '夏县', '3');
INSERT INTO `t_city` VALUES ('140829', '平陆县', '3');
INSERT INTO `t_city` VALUES ('140830', '芮城县', '3');
INSERT INTO `t_city` VALUES ('140881', '永济市', '3');
INSERT INTO `t_city` VALUES ('140882', '河津市', '3');
INSERT INTO `t_city` VALUES ('140900', '忻州市', '2');
INSERT INTO `t_city` VALUES ('140901', '市辖区', '3');
INSERT INTO `t_city` VALUES ('140902', '忻府区', '3');
INSERT INTO `t_city` VALUES ('140921', '定襄县', '3');
INSERT INTO `t_city` VALUES ('140922', '五台县', '3');
INSERT INTO `t_city` VALUES ('140923', '代县', '3');
INSERT INTO `t_city` VALUES ('140924', '繁峙县', '3');
INSERT INTO `t_city` VALUES ('140925', '宁武县', '3');
INSERT INTO `t_city` VALUES ('140926', '静乐县', '3');
INSERT INTO `t_city` VALUES ('140927', '神池县', '3');
INSERT INTO `t_city` VALUES ('140928', '五寨县', '3');
INSERT INTO `t_city` VALUES ('140929', '岢岚县', '3');
INSERT INTO `t_city` VALUES ('140930', '河曲县', '3');
INSERT INTO `t_city` VALUES ('140931', '保德县', '3');
INSERT INTO `t_city` VALUES ('140932', '偏关县', '3');
INSERT INTO `t_city` VALUES ('140981', '原平市', '3');
INSERT INTO `t_city` VALUES ('141000', '临汾市', '2');
INSERT INTO `t_city` VALUES ('141001', '市辖区', '3');
INSERT INTO `t_city` VALUES ('141002', '尧都区', '3');
INSERT INTO `t_city` VALUES ('141021', '曲沃县', '3');
INSERT INTO `t_city` VALUES ('141022', '翼城县', '3');
INSERT INTO `t_city` VALUES ('141023', '襄汾县', '3');
INSERT INTO `t_city` VALUES ('141024', '洪洞县', '3');
INSERT INTO `t_city` VALUES ('141025', '古县', '3');
INSERT INTO `t_city` VALUES ('141026', '安泽县', '3');
INSERT INTO `t_city` VALUES ('141027', '浮山县', '3');
INSERT INTO `t_city` VALUES ('141028', '吉县', '3');
INSERT INTO `t_city` VALUES ('141029', '乡宁县', '3');
INSERT INTO `t_city` VALUES ('141030', '大宁县', '3');
INSERT INTO `t_city` VALUES ('141031', '隰县', '3');
INSERT INTO `t_city` VALUES ('141032', '永和县', '3');
INSERT INTO `t_city` VALUES ('141033', '蒲县', '3');
INSERT INTO `t_city` VALUES ('141034', '汾西县', '3');
INSERT INTO `t_city` VALUES ('141081', '侯马市', '3');
INSERT INTO `t_city` VALUES ('141082', '霍州市', '3');
INSERT INTO `t_city` VALUES ('141100', '吕梁市', '2');
INSERT INTO `t_city` VALUES ('141101', '市辖区', '3');
INSERT INTO `t_city` VALUES ('141102', '离石区', '3');
INSERT INTO `t_city` VALUES ('141121', '文水县', '3');
INSERT INTO `t_city` VALUES ('141122', '交城县', '3');
INSERT INTO `t_city` VALUES ('141123', '兴县', '3');
INSERT INTO `t_city` VALUES ('141124', '临县', '3');
INSERT INTO `t_city` VALUES ('141125', '柳林县', '3');
INSERT INTO `t_city` VALUES ('141126', '石楼县', '3');
INSERT INTO `t_city` VALUES ('141127', '岚县', '3');
INSERT INTO `t_city` VALUES ('141128', '方山县', '3');
INSERT INTO `t_city` VALUES ('141129', '中阳县', '3');
INSERT INTO `t_city` VALUES ('141130', '交口县', '3');
INSERT INTO `t_city` VALUES ('141181', '孝义市', '3');
INSERT INTO `t_city` VALUES ('141182', '汾阳市', '3');
INSERT INTO `t_city` VALUES ('150000', '内蒙古自治区', '1');
INSERT INTO `t_city` VALUES ('150100', '呼和浩特市', '2');
INSERT INTO `t_city` VALUES ('150101', '市辖区', '3');
INSERT INTO `t_city` VALUES ('150102', '新城区', '3');
INSERT INTO `t_city` VALUES ('150103', '回民区', '3');
INSERT INTO `t_city` VALUES ('150104', '玉泉区', '3');
INSERT INTO `t_city` VALUES ('150105', '赛罕区', '3');
INSERT INTO `t_city` VALUES ('150121', '土默特左旗', '3');
INSERT INTO `t_city` VALUES ('150122', '托克托县', '3');
INSERT INTO `t_city` VALUES ('150123', '和林格尔县', '3');
INSERT INTO `t_city` VALUES ('150124', '清水河县', '3');
INSERT INTO `t_city` VALUES ('150125', '武川县', '3');
INSERT INTO `t_city` VALUES ('150200', '包头市', '2');
INSERT INTO `t_city` VALUES ('150201', '市辖区', '3');
INSERT INTO `t_city` VALUES ('150202', '东河区', '3');
INSERT INTO `t_city` VALUES ('150203', '昆都仑区', '3');
INSERT INTO `t_city` VALUES ('150204', '青山区', '3');
INSERT INTO `t_city` VALUES ('150205', '石拐区', '3');
INSERT INTO `t_city` VALUES ('150206', '白云鄂博矿区', '3');
INSERT INTO `t_city` VALUES ('150207', '九原区', '3');
INSERT INTO `t_city` VALUES ('150221', '土默特右旗', '3');
INSERT INTO `t_city` VALUES ('150222', '固阳县', '3');
INSERT INTO `t_city` VALUES ('150223', '达尔罕茂明安联合旗', '3');
INSERT INTO `t_city` VALUES ('150300', '乌海市', '2');
INSERT INTO `t_city` VALUES ('150301', '市辖区', '3');
INSERT INTO `t_city` VALUES ('150302', '海勃湾区', '3');
INSERT INTO `t_city` VALUES ('150303', '海南区', '3');
INSERT INTO `t_city` VALUES ('150304', '乌达区', '3');
INSERT INTO `t_city` VALUES ('150400', '赤峰市', '2');
INSERT INTO `t_city` VALUES ('150401', '市辖区', '3');
INSERT INTO `t_city` VALUES ('150402', '红山区', '3');
INSERT INTO `t_city` VALUES ('150403', '元宝山区', '3');
INSERT INTO `t_city` VALUES ('150404', '松山区', '3');
INSERT INTO `t_city` VALUES ('150421', '阿鲁科尔沁旗', '3');
INSERT INTO `t_city` VALUES ('150422', '巴林左旗', '3');
INSERT INTO `t_city` VALUES ('150423', '巴林右旗', '3');
INSERT INTO `t_city` VALUES ('150424', '林西县', '3');
INSERT INTO `t_city` VALUES ('150425', '克什克腾旗', '3');
INSERT INTO `t_city` VALUES ('150426', '翁牛特旗', '3');
INSERT INTO `t_city` VALUES ('150428', '喀喇沁旗', '3');
INSERT INTO `t_city` VALUES ('150429', '宁城县', '3');
INSERT INTO `t_city` VALUES ('150430', '敖汉旗', '3');
INSERT INTO `t_city` VALUES ('150500', '通辽市', '2');
INSERT INTO `t_city` VALUES ('150501', '市辖区', '3');
INSERT INTO `t_city` VALUES ('150502', '科尔沁区', '3');
INSERT INTO `t_city` VALUES ('150521', '科尔沁左翼中旗', '3');
INSERT INTO `t_city` VALUES ('150522', '科尔沁左翼后旗', '3');
INSERT INTO `t_city` VALUES ('150523', '开鲁县', '3');
INSERT INTO `t_city` VALUES ('150524', '库伦旗', '3');
INSERT INTO `t_city` VALUES ('150525', '奈曼旗', '3');
INSERT INTO `t_city` VALUES ('150526', '扎鲁特旗', '3');
INSERT INTO `t_city` VALUES ('150581', '霍林郭勒市', '3');
INSERT INTO `t_city` VALUES ('150600', '鄂尔多斯市', '2');
INSERT INTO `t_city` VALUES ('150601', '市辖区', '3');
INSERT INTO `t_city` VALUES ('150602', '东胜区', '3');
INSERT INTO `t_city` VALUES ('150603', '康巴什区', '3');
INSERT INTO `t_city` VALUES ('150621', '达拉特旗', '3');
INSERT INTO `t_city` VALUES ('150622', '准格尔旗', '3');
INSERT INTO `t_city` VALUES ('150623', '鄂托克前旗', '3');
INSERT INTO `t_city` VALUES ('150624', '鄂托克旗', '3');
INSERT INTO `t_city` VALUES ('150625', '杭锦旗', '3');
INSERT INTO `t_city` VALUES ('150626', '乌审旗', '3');
INSERT INTO `t_city` VALUES ('150627', '伊金霍洛旗', '3');
INSERT INTO `t_city` VALUES ('150700', '呼伦贝尔市', '2');
INSERT INTO `t_city` VALUES ('150701', '市辖区', '3');
INSERT INTO `t_city` VALUES ('150702', '海拉尔区', '3');
INSERT INTO `t_city` VALUES ('150703', '扎赉诺尔区', '3');
INSERT INTO `t_city` VALUES ('150721', '阿荣旗', '3');
INSERT INTO `t_city` VALUES ('150722', '莫力达瓦达斡尔族自治旗', '3');
INSERT INTO `t_city` VALUES ('150723', '鄂伦春自治旗', '3');
INSERT INTO `t_city` VALUES ('150724', '鄂温克族自治旗', '3');
INSERT INTO `t_city` VALUES ('150725', '陈巴尔虎旗', '3');
INSERT INTO `t_city` VALUES ('150726', '新巴尔虎左旗', '3');
INSERT INTO `t_city` VALUES ('150727', '新巴尔虎右旗', '3');
INSERT INTO `t_city` VALUES ('150781', '满洲里市', '3');
INSERT INTO `t_city` VALUES ('150782', '牙克石市', '3');
INSERT INTO `t_city` VALUES ('150783', '扎兰屯市', '3');
INSERT INTO `t_city` VALUES ('150784', '额尔古纳市', '3');
INSERT INTO `t_city` VALUES ('150785', '根河市', '3');
INSERT INTO `t_city` VALUES ('150800', '巴彦淖尔市', '2');
INSERT INTO `t_city` VALUES ('150801', '市辖区', '3');
INSERT INTO `t_city` VALUES ('150802', '临河区', '3');
INSERT INTO `t_city` VALUES ('150821', '五原县', '3');
INSERT INTO `t_city` VALUES ('150822', '磴口县', '3');
INSERT INTO `t_city` VALUES ('150823', '乌拉特前旗', '3');
INSERT INTO `t_city` VALUES ('150824', '乌拉特中旗', '3');
INSERT INTO `t_city` VALUES ('150825', '乌拉特后旗', '3');
INSERT INTO `t_city` VALUES ('150826', '杭锦后旗', '3');
INSERT INTO `t_city` VALUES ('150900', '乌兰察布市', '2');
INSERT INTO `t_city` VALUES ('150901', '市辖区', '3');
INSERT INTO `t_city` VALUES ('150902', '集宁区', '3');
INSERT INTO `t_city` VALUES ('150921', '卓资县', '3');
INSERT INTO `t_city` VALUES ('150922', '化德县', '3');
INSERT INTO `t_city` VALUES ('150923', '商都县', '3');
INSERT INTO `t_city` VALUES ('150924', '兴和县', '3');
INSERT INTO `t_city` VALUES ('150925', '凉城县', '3');
INSERT INTO `t_city` VALUES ('150926', '察哈尔右翼前旗', '3');
INSERT INTO `t_city` VALUES ('150927', '察哈尔右翼中旗', '3');
INSERT INTO `t_city` VALUES ('150928', '察哈尔右翼后旗', '3');
INSERT INTO `t_city` VALUES ('150929', '四子王旗', '3');
INSERT INTO `t_city` VALUES ('150981', '丰镇市', '3');
INSERT INTO `t_city` VALUES ('152200', '兴安盟', '2');
INSERT INTO `t_city` VALUES ('152201', '乌兰浩特市', '3');
INSERT INTO `t_city` VALUES ('152202', '阿尔山市', '3');
INSERT INTO `t_city` VALUES ('152221', '科尔沁右翼前旗', '3');
INSERT INTO `t_city` VALUES ('152222', '科尔沁右翼中旗', '3');
INSERT INTO `t_city` VALUES ('152223', '扎赉特旗', '3');
INSERT INTO `t_city` VALUES ('152224', '突泉县', '3');
INSERT INTO `t_city` VALUES ('152500', '锡林郭勒盟', '2');
INSERT INTO `t_city` VALUES ('152501', '二连浩特市', '3');
INSERT INTO `t_city` VALUES ('152502', '锡林浩特市', '3');
INSERT INTO `t_city` VALUES ('152522', '阿巴嘎旗', '3');
INSERT INTO `t_city` VALUES ('152523', '苏尼特左旗', '3');
INSERT INTO `t_city` VALUES ('152524', '苏尼特右旗', '3');
INSERT INTO `t_city` VALUES ('152525', '东乌珠穆沁旗', '3');
INSERT INTO `t_city` VALUES ('152526', '西乌珠穆沁旗', '3');
INSERT INTO `t_city` VALUES ('152527', '太仆寺旗', '3');
INSERT INTO `t_city` VALUES ('152528', '镶黄旗', '3');
INSERT INTO `t_city` VALUES ('152529', '正镶白旗', '3');
INSERT INTO `t_city` VALUES ('152530', '正蓝旗', '3');
INSERT INTO `t_city` VALUES ('152531', '多伦县', '3');
INSERT INTO `t_city` VALUES ('152900', '阿拉善盟', '2');
INSERT INTO `t_city` VALUES ('152921', '阿拉善左旗', '3');
INSERT INTO `t_city` VALUES ('152922', '阿拉善右旗', '3');
INSERT INTO `t_city` VALUES ('152923', '额济纳旗', '3');
INSERT INTO `t_city` VALUES ('210000', '辽宁省', '1');
INSERT INTO `t_city` VALUES ('210100', '沈阳市', '2');
INSERT INTO `t_city` VALUES ('210101', '市辖区', '3');
INSERT INTO `t_city` VALUES ('210102', '和平区', '3');
INSERT INTO `t_city` VALUES ('210103', '沈河区', '3');
INSERT INTO `t_city` VALUES ('210104', '大东区', '3');
INSERT INTO `t_city` VALUES ('210105', '皇姑区', '3');
INSERT INTO `t_city` VALUES ('210106', '铁西区', '3');
INSERT INTO `t_city` VALUES ('210111', '苏家屯区', '3');
INSERT INTO `t_city` VALUES ('210112', '浑南区', '3');
INSERT INTO `t_city` VALUES ('210113', '沈北新区', '3');
INSERT INTO `t_city` VALUES ('210114', '于洪区', '3');
INSERT INTO `t_city` VALUES ('210115', '辽中区', '3');
INSERT INTO `t_city` VALUES ('210123', '康平县', '3');
INSERT INTO `t_city` VALUES ('210124', '法库县', '3');
INSERT INTO `t_city` VALUES ('210181', '新民市', '3');
INSERT INTO `t_city` VALUES ('210200', '大连市', '2');
INSERT INTO `t_city` VALUES ('210201', '市辖区', '3');
INSERT INTO `t_city` VALUES ('210202', '中山区', '3');
INSERT INTO `t_city` VALUES ('210203', '西岗区', '3');
INSERT INTO `t_city` VALUES ('210204', '沙河口区', '3');
INSERT INTO `t_city` VALUES ('210211', '甘井子区', '3');
INSERT INTO `t_city` VALUES ('210212', '旅顺口区', '3');
INSERT INTO `t_city` VALUES ('210213', '金州区', '3');
INSERT INTO `t_city` VALUES ('210214', '普兰店区', '3');
INSERT INTO `t_city` VALUES ('210224', '长海县', '3');
INSERT INTO `t_city` VALUES ('210281', '瓦房店市', '3');
INSERT INTO `t_city` VALUES ('210283', '庄河市', '3');
INSERT INTO `t_city` VALUES ('210300', '鞍山市', '2');
INSERT INTO `t_city` VALUES ('210301', '市辖区', '3');
INSERT INTO `t_city` VALUES ('210302', '铁东区', '3');
INSERT INTO `t_city` VALUES ('210303', '铁西区', '3');
INSERT INTO `t_city` VALUES ('210304', '立山区', '3');
INSERT INTO `t_city` VALUES ('210311', '千山区', '3');
INSERT INTO `t_city` VALUES ('210321', '台安县', '3');
INSERT INTO `t_city` VALUES ('210323', '岫岩满族自治县', '3');
INSERT INTO `t_city` VALUES ('210381', '海城市', '3');
INSERT INTO `t_city` VALUES ('210400', '抚顺市', '2');
INSERT INTO `t_city` VALUES ('210401', '市辖区', '3');
INSERT INTO `t_city` VALUES ('210402', '新抚区', '3');
INSERT INTO `t_city` VALUES ('210403', '东洲区', '3');
INSERT INTO `t_city` VALUES ('210404', '望花区', '3');
INSERT INTO `t_city` VALUES ('210411', '顺城区', '3');
INSERT INTO `t_city` VALUES ('210421', '抚顺县', '3');
INSERT INTO `t_city` VALUES ('210422', '新宾满族自治县', '3');
INSERT INTO `t_city` VALUES ('210423', '清原满族自治县', '3');
INSERT INTO `t_city` VALUES ('210500', '本溪市', '2');
INSERT INTO `t_city` VALUES ('210501', '市辖区', '3');
INSERT INTO `t_city` VALUES ('210502', '平山区', '3');
INSERT INTO `t_city` VALUES ('210503', '溪湖区', '3');
INSERT INTO `t_city` VALUES ('210504', '明山区', '3');
INSERT INTO `t_city` VALUES ('210505', '南芬区', '3');
INSERT INTO `t_city` VALUES ('210521', '本溪满族自治县', '3');
INSERT INTO `t_city` VALUES ('210522', '桓仁满族自治县', '3');
INSERT INTO `t_city` VALUES ('210600', '丹东市', '2');
INSERT INTO `t_city` VALUES ('210601', '市辖区', '3');
INSERT INTO `t_city` VALUES ('210602', '元宝区', '3');
INSERT INTO `t_city` VALUES ('210603', '振兴区', '3');
INSERT INTO `t_city` VALUES ('210604', '振安区', '3');
INSERT INTO `t_city` VALUES ('210624', '宽甸满族自治县', '3');
INSERT INTO `t_city` VALUES ('210681', '东港市', '3');
INSERT INTO `t_city` VALUES ('210682', '凤城市', '3');
INSERT INTO `t_city` VALUES ('210700', '锦州市', '2');
INSERT INTO `t_city` VALUES ('210701', '市辖区', '3');
INSERT INTO `t_city` VALUES ('210702', '古塔区', '3');
INSERT INTO `t_city` VALUES ('210703', '凌河区', '3');
INSERT INTO `t_city` VALUES ('210711', '太和区', '3');
INSERT INTO `t_city` VALUES ('210726', '黑山县', '3');
INSERT INTO `t_city` VALUES ('210727', '义县', '3');
INSERT INTO `t_city` VALUES ('210781', '凌海市', '3');
INSERT INTO `t_city` VALUES ('210782', '北镇市', '3');
INSERT INTO `t_city` VALUES ('210800', '营口市', '2');
INSERT INTO `t_city` VALUES ('210801', '市辖区', '3');
INSERT INTO `t_city` VALUES ('210802', '站前区', '3');
INSERT INTO `t_city` VALUES ('210803', '西市区', '3');
INSERT INTO `t_city` VALUES ('210804', '鲅鱼圈区', '3');
INSERT INTO `t_city` VALUES ('210811', '老边区', '3');
INSERT INTO `t_city` VALUES ('210881', '盖州市', '3');
INSERT INTO `t_city` VALUES ('210882', '大石桥市', '3');
INSERT INTO `t_city` VALUES ('210900', '阜新市', '2');
INSERT INTO `t_city` VALUES ('210901', '市辖区', '3');
INSERT INTO `t_city` VALUES ('210902', '海州区', '3');
INSERT INTO `t_city` VALUES ('210903', '新邱区', '3');
INSERT INTO `t_city` VALUES ('210904', '太平区', '3');
INSERT INTO `t_city` VALUES ('210905', '清河门区', '3');
INSERT INTO `t_city` VALUES ('210911', '细河区', '3');
INSERT INTO `t_city` VALUES ('210921', '阜新蒙古族自治县', '3');
INSERT INTO `t_city` VALUES ('210922', '彰武县', '3');
INSERT INTO `t_city` VALUES ('211000', '辽阳市', '2');
INSERT INTO `t_city` VALUES ('211001', '市辖区', '3');
INSERT INTO `t_city` VALUES ('211002', '白塔区', '3');
INSERT INTO `t_city` VALUES ('211003', '文圣区', '3');
INSERT INTO `t_city` VALUES ('211004', '宏伟区', '3');
INSERT INTO `t_city` VALUES ('211005', '弓长岭区', '3');
INSERT INTO `t_city` VALUES ('211011', '太子河区', '3');
INSERT INTO `t_city` VALUES ('211021', '辽阳县', '3');
INSERT INTO `t_city` VALUES ('211081', '灯塔市', '3');
INSERT INTO `t_city` VALUES ('211100', '盘锦市', '2');
INSERT INTO `t_city` VALUES ('211101', '市辖区', '3');
INSERT INTO `t_city` VALUES ('211102', '双台子区', '3');
INSERT INTO `t_city` VALUES ('211103', '兴隆台区', '3');
INSERT INTO `t_city` VALUES ('211104', '大洼区', '3');
INSERT INTO `t_city` VALUES ('211122', '盘山县', '3');
INSERT INTO `t_city` VALUES ('211200', '铁岭市', '2');
INSERT INTO `t_city` VALUES ('211201', '市辖区', '3');
INSERT INTO `t_city` VALUES ('211202', '银州区', '3');
INSERT INTO `t_city` VALUES ('211204', '清河区', '3');
INSERT INTO `t_city` VALUES ('211221', '铁岭县', '3');
INSERT INTO `t_city` VALUES ('211223', '西丰县', '3');
INSERT INTO `t_city` VALUES ('211224', '昌图县', '3');
INSERT INTO `t_city` VALUES ('211281', '调兵山市', '3');
INSERT INTO `t_city` VALUES ('211282', '开原市', '3');
INSERT INTO `t_city` VALUES ('211300', '朝阳市', '2');
INSERT INTO `t_city` VALUES ('211301', '市辖区', '3');
INSERT INTO `t_city` VALUES ('211302', '双塔区', '3');
INSERT INTO `t_city` VALUES ('211303', '龙城区', '3');
INSERT INTO `t_city` VALUES ('211321', '朝阳县', '3');
INSERT INTO `t_city` VALUES ('211322', '建平县', '3');
INSERT INTO `t_city` VALUES ('211324', '喀喇沁左翼蒙古族自治县', '3');
INSERT INTO `t_city` VALUES ('211381', '北票市', '3');
INSERT INTO `t_city` VALUES ('211382', '凌源市', '3');
INSERT INTO `t_city` VALUES ('211400', '葫芦岛市', '2');
INSERT INTO `t_city` VALUES ('211401', '市辖区', '3');
INSERT INTO `t_city` VALUES ('211402', '连山区', '3');
INSERT INTO `t_city` VALUES ('211403', '龙港区', '3');
INSERT INTO `t_city` VALUES ('211404', '南票区', '3');
INSERT INTO `t_city` VALUES ('211421', '绥中县', '3');
INSERT INTO `t_city` VALUES ('211422', '建昌县', '3');
INSERT INTO `t_city` VALUES ('211481', '兴城市', '3');
INSERT INTO `t_city` VALUES ('220000', '吉林省', '1');
INSERT INTO `t_city` VALUES ('220100', '长春市', '2');
INSERT INTO `t_city` VALUES ('220101', '市辖区', '3');
INSERT INTO `t_city` VALUES ('220102', '南关区', '3');
INSERT INTO `t_city` VALUES ('220103', '宽城区', '3');
INSERT INTO `t_city` VALUES ('220104', '朝阳区', '3');
INSERT INTO `t_city` VALUES ('220105', '二道区', '3');
INSERT INTO `t_city` VALUES ('220106', '绿园区', '3');
INSERT INTO `t_city` VALUES ('220112', '双阳区', '3');
INSERT INTO `t_city` VALUES ('220113', '九台区', '3');
INSERT INTO `t_city` VALUES ('220122', '农安县', '3');
INSERT INTO `t_city` VALUES ('220182', '榆树市', '3');
INSERT INTO `t_city` VALUES ('220183', '德惠市', '3');
INSERT INTO `t_city` VALUES ('220200', '吉林市', '2');
INSERT INTO `t_city` VALUES ('220201', '市辖区', '3');
INSERT INTO `t_city` VALUES ('220202', '昌邑区', '3');
INSERT INTO `t_city` VALUES ('220203', '龙潭区', '3');
INSERT INTO `t_city` VALUES ('220204', '船营区', '3');
INSERT INTO `t_city` VALUES ('220211', '丰满区', '3');
INSERT INTO `t_city` VALUES ('220221', '永吉县', '3');
INSERT INTO `t_city` VALUES ('220281', '蛟河市', '3');
INSERT INTO `t_city` VALUES ('220282', '桦甸市', '3');
INSERT INTO `t_city` VALUES ('220283', '舒兰市', '3');
INSERT INTO `t_city` VALUES ('220284', '磐石市', '3');
INSERT INTO `t_city` VALUES ('220300', '四平市', '2');
INSERT INTO `t_city` VALUES ('220301', '市辖区', '3');
INSERT INTO `t_city` VALUES ('220302', '铁西区', '3');
INSERT INTO `t_city` VALUES ('220303', '铁东区', '3');
INSERT INTO `t_city` VALUES ('220322', '梨树县', '3');
INSERT INTO `t_city` VALUES ('220323', '伊通满族自治县', '3');
INSERT INTO `t_city` VALUES ('220381', '公主岭市', '3');
INSERT INTO `t_city` VALUES ('220382', '双辽市', '3');
INSERT INTO `t_city` VALUES ('220400', '辽源市', '2');
INSERT INTO `t_city` VALUES ('220401', '市辖区', '3');
INSERT INTO `t_city` VALUES ('220402', '龙山区', '3');
INSERT INTO `t_city` VALUES ('220403', '西安区', '3');
INSERT INTO `t_city` VALUES ('220421', '东丰县', '3');
INSERT INTO `t_city` VALUES ('220422', '东辽县', '3');
INSERT INTO `t_city` VALUES ('220500', '通化市', '2');
INSERT INTO `t_city` VALUES ('220501', '市辖区', '3');
INSERT INTO `t_city` VALUES ('220502', '东昌区', '3');
INSERT INTO `t_city` VALUES ('220503', '二道江区', '3');
INSERT INTO `t_city` VALUES ('220521', '通化县', '3');
INSERT INTO `t_city` VALUES ('220523', '辉南县', '3');
INSERT INTO `t_city` VALUES ('220524', '柳河县', '3');
INSERT INTO `t_city` VALUES ('220581', '梅河口市', '3');
INSERT INTO `t_city` VALUES ('220582', '集安市', '3');
INSERT INTO `t_city` VALUES ('220600', '白山市', '2');
INSERT INTO `t_city` VALUES ('220601', '市辖区', '3');
INSERT INTO `t_city` VALUES ('220602', '浑江区', '3');
INSERT INTO `t_city` VALUES ('220605', '江源区', '3');
INSERT INTO `t_city` VALUES ('220621', '抚松县', '3');
INSERT INTO `t_city` VALUES ('220622', '靖宇县', '3');
INSERT INTO `t_city` VALUES ('220623', '长白朝鲜族自治县', '3');
INSERT INTO `t_city` VALUES ('220681', '临江市', '3');
INSERT INTO `t_city` VALUES ('220700', '松原市', '2');
INSERT INTO `t_city` VALUES ('220701', '市辖区', '3');
INSERT INTO `t_city` VALUES ('220702', '宁江区', '3');
INSERT INTO `t_city` VALUES ('220721', '前郭尔罗斯蒙古族自治县', '3');
INSERT INTO `t_city` VALUES ('220722', '长岭县', '3');
INSERT INTO `t_city` VALUES ('220723', '乾安县', '3');
INSERT INTO `t_city` VALUES ('220781', '扶余市', '3');
INSERT INTO `t_city` VALUES ('220800', '白城市', '2');
INSERT INTO `t_city` VALUES ('220801', '市辖区', '3');
INSERT INTO `t_city` VALUES ('220802', '洮北区', '3');
INSERT INTO `t_city` VALUES ('220821', '镇赉县', '3');
INSERT INTO `t_city` VALUES ('220822', '通榆县', '3');
INSERT INTO `t_city` VALUES ('220881', '洮南市', '3');
INSERT INTO `t_city` VALUES ('220882', '大安市', '3');
INSERT INTO `t_city` VALUES ('222400', '延边朝鲜族自治州', '2');
INSERT INTO `t_city` VALUES ('222401', '延吉市', '3');
INSERT INTO `t_city` VALUES ('222402', '图们市', '3');
INSERT INTO `t_city` VALUES ('222403', '敦化市', '3');
INSERT INTO `t_city` VALUES ('222404', '珲春市', '3');
INSERT INTO `t_city` VALUES ('222405', '龙井市', '3');
INSERT INTO `t_city` VALUES ('222406', '和龙市', '3');
INSERT INTO `t_city` VALUES ('222424', '汪清县', '3');
INSERT INTO `t_city` VALUES ('222426', '安图县', '3');
INSERT INTO `t_city` VALUES ('230000', '黑龙江省', '1');
INSERT INTO `t_city` VALUES ('230100', '哈尔滨市', '2');
INSERT INTO `t_city` VALUES ('230101', '市辖区', '3');
INSERT INTO `t_city` VALUES ('230102', '道里区', '3');
INSERT INTO `t_city` VALUES ('230103', '南岗区', '3');
INSERT INTO `t_city` VALUES ('230104', '道外区', '3');
INSERT INTO `t_city` VALUES ('230108', '平房区', '3');
INSERT INTO `t_city` VALUES ('230109', '松北区', '3');
INSERT INTO `t_city` VALUES ('230110', '香坊区', '3');
INSERT INTO `t_city` VALUES ('230111', '呼兰区', '3');
INSERT INTO `t_city` VALUES ('230112', '阿城区', '3');
INSERT INTO `t_city` VALUES ('230113', '双城区', '3');
INSERT INTO `t_city` VALUES ('230123', '依兰县', '3');
INSERT INTO `t_city` VALUES ('230124', '方正县', '3');
INSERT INTO `t_city` VALUES ('230125', '宾县', '3');
INSERT INTO `t_city` VALUES ('230126', '巴彦县', '3');
INSERT INTO `t_city` VALUES ('230127', '木兰县', '3');
INSERT INTO `t_city` VALUES ('230128', '通河县', '3');
INSERT INTO `t_city` VALUES ('230129', '延寿县', '3');
INSERT INTO `t_city` VALUES ('230183', '尚志市', '3');
INSERT INTO `t_city` VALUES ('230184', '五常市', '3');
INSERT INTO `t_city` VALUES ('230200', '齐齐哈尔市', '2');
INSERT INTO `t_city` VALUES ('230201', '市辖区', '3');
INSERT INTO `t_city` VALUES ('230202', '龙沙区', '3');
INSERT INTO `t_city` VALUES ('230203', '建华区', '3');
INSERT INTO `t_city` VALUES ('230204', '铁锋区', '3');
INSERT INTO `t_city` VALUES ('230205', '昂昂溪区', '3');
INSERT INTO `t_city` VALUES ('230206', '富拉尔基区', '3');
INSERT INTO `t_city` VALUES ('230207', '碾子山区', '3');
INSERT INTO `t_city` VALUES ('230208', '梅里斯达斡尔族区', '3');
INSERT INTO `t_city` VALUES ('230221', '龙江县', '3');
INSERT INTO `t_city` VALUES ('230223', '依安县', '3');
INSERT INTO `t_city` VALUES ('230224', '泰来县', '3');
INSERT INTO `t_city` VALUES ('230225', '甘南县', '3');
INSERT INTO `t_city` VALUES ('230227', '富裕县', '3');
INSERT INTO `t_city` VALUES ('230229', '克山县', '3');
INSERT INTO `t_city` VALUES ('230230', '克东县', '3');
INSERT INTO `t_city` VALUES ('230231', '拜泉县', '3');
INSERT INTO `t_city` VALUES ('230281', '讷河市', '3');
INSERT INTO `t_city` VALUES ('230300', '鸡西市', '2');
INSERT INTO `t_city` VALUES ('230301', '市辖区', '3');
INSERT INTO `t_city` VALUES ('230302', '鸡冠区', '3');
INSERT INTO `t_city` VALUES ('230303', '恒山区', '3');
INSERT INTO `t_city` VALUES ('230304', '滴道区', '3');
INSERT INTO `t_city` VALUES ('230305', '梨树区', '3');
INSERT INTO `t_city` VALUES ('230306', '城子河区', '3');
INSERT INTO `t_city` VALUES ('230307', '麻山区', '3');
INSERT INTO `t_city` VALUES ('230321', '鸡东县', '3');
INSERT INTO `t_city` VALUES ('230381', '虎林市', '3');
INSERT INTO `t_city` VALUES ('230382', '密山市', '3');
INSERT INTO `t_city` VALUES ('230400', '鹤岗市', '2');
INSERT INTO `t_city` VALUES ('230401', '市辖区', '3');
INSERT INTO `t_city` VALUES ('230402', '向阳区', '3');
INSERT INTO `t_city` VALUES ('230403', '工农区', '3');
INSERT INTO `t_city` VALUES ('230404', '南山区', '3');
INSERT INTO `t_city` VALUES ('230405', '兴安区', '3');
INSERT INTO `t_city` VALUES ('230406', '东山区', '3');
INSERT INTO `t_city` VALUES ('230407', '兴山区', '3');
INSERT INTO `t_city` VALUES ('230421', '萝北县', '3');
INSERT INTO `t_city` VALUES ('230422', '绥滨县', '3');
INSERT INTO `t_city` VALUES ('230500', '双鸭山市', '2');
INSERT INTO `t_city` VALUES ('230501', '市辖区', '3');
INSERT INTO `t_city` VALUES ('230502', '尖山区', '3');
INSERT INTO `t_city` VALUES ('230503', '岭东区', '3');
INSERT INTO `t_city` VALUES ('230505', '四方台区', '3');
INSERT INTO `t_city` VALUES ('230506', '宝山区', '3');
INSERT INTO `t_city` VALUES ('230521', '集贤县', '3');
INSERT INTO `t_city` VALUES ('230522', '友谊县', '3');
INSERT INTO `t_city` VALUES ('230523', '宝清县', '3');
INSERT INTO `t_city` VALUES ('230524', '饶河县', '3');
INSERT INTO `t_city` VALUES ('230600', '大庆市', '2');
INSERT INTO `t_city` VALUES ('230601', '市辖区', '3');
INSERT INTO `t_city` VALUES ('230602', '萨尔图区', '3');
INSERT INTO `t_city` VALUES ('230603', '龙凤区', '3');
INSERT INTO `t_city` VALUES ('230604', '让胡路区', '3');
INSERT INTO `t_city` VALUES ('230605', '红岗区', '3');
INSERT INTO `t_city` VALUES ('230606', '大同区', '3');
INSERT INTO `t_city` VALUES ('230621', '肇州县', '3');
INSERT INTO `t_city` VALUES ('230622', '肇源县', '3');
INSERT INTO `t_city` VALUES ('230623', '林甸县', '3');
INSERT INTO `t_city` VALUES ('230624', '杜尔伯特蒙古族自治县', '3');
INSERT INTO `t_city` VALUES ('230700', '伊春市', '2');
INSERT INTO `t_city` VALUES ('230701', '市辖区', '3');
INSERT INTO `t_city` VALUES ('230702', '伊春区', '3');
INSERT INTO `t_city` VALUES ('230703', '南岔区', '3');
INSERT INTO `t_city` VALUES ('230704', '友好区', '3');
INSERT INTO `t_city` VALUES ('230705', '西林区', '3');
INSERT INTO `t_city` VALUES ('230706', '翠峦区', '3');
INSERT INTO `t_city` VALUES ('230707', '新青区', '3');
INSERT INTO `t_city` VALUES ('230708', '美溪区', '3');
INSERT INTO `t_city` VALUES ('230709', '金山屯区', '3');
INSERT INTO `t_city` VALUES ('230710', '五营区', '3');
INSERT INTO `t_city` VALUES ('230711', '乌马河区', '3');
INSERT INTO `t_city` VALUES ('230712', '汤旺河区', '3');
INSERT INTO `t_city` VALUES ('230713', '带岭区', '3');
INSERT INTO `t_city` VALUES ('230714', '乌伊岭区', '3');
INSERT INTO `t_city` VALUES ('230715', '红星区', '3');
INSERT INTO `t_city` VALUES ('230716', '上甘岭区', '3');
INSERT INTO `t_city` VALUES ('230722', '嘉荫县', '3');
INSERT INTO `t_city` VALUES ('230781', '铁力市', '3');
INSERT INTO `t_city` VALUES ('230800', '佳木斯市', '2');
INSERT INTO `t_city` VALUES ('230801', '市辖区', '3');
INSERT INTO `t_city` VALUES ('230803', '向阳区', '3');
INSERT INTO `t_city` VALUES ('230804', '前进区', '3');
INSERT INTO `t_city` VALUES ('230805', '东风区', '3');
INSERT INTO `t_city` VALUES ('230811', '郊区', '3');
INSERT INTO `t_city` VALUES ('230822', '桦南县', '3');
INSERT INTO `t_city` VALUES ('230826', '桦川县', '3');
INSERT INTO `t_city` VALUES ('230828', '汤原县', '3');
INSERT INTO `t_city` VALUES ('230881', '同江市', '3');
INSERT INTO `t_city` VALUES ('230882', '富锦市', '3');
INSERT INTO `t_city` VALUES ('230883', '抚远市', '3');
INSERT INTO `t_city` VALUES ('230900', '七台河市', '2');
INSERT INTO `t_city` VALUES ('230901', '市辖区', '3');
INSERT INTO `t_city` VALUES ('230902', '新兴区', '3');
INSERT INTO `t_city` VALUES ('230903', '桃山区', '3');
INSERT INTO `t_city` VALUES ('230904', '茄子河区', '3');
INSERT INTO `t_city` VALUES ('230921', '勃利县', '3');
INSERT INTO `t_city` VALUES ('231000', '牡丹江市', '2');
INSERT INTO `t_city` VALUES ('231001', '市辖区', '3');
INSERT INTO `t_city` VALUES ('231002', '东安区', '3');
INSERT INTO `t_city` VALUES ('231003', '阳明区', '3');
INSERT INTO `t_city` VALUES ('231004', '爱民区', '3');
INSERT INTO `t_city` VALUES ('231005', '西安区', '3');
INSERT INTO `t_city` VALUES ('231025', '林口县', '3');
INSERT INTO `t_city` VALUES ('231081', '绥芬河市', '3');
INSERT INTO `t_city` VALUES ('231083', '海林市', '3');
INSERT INTO `t_city` VALUES ('231084', '宁安市', '3');
INSERT INTO `t_city` VALUES ('231085', '穆棱市', '3');
INSERT INTO `t_city` VALUES ('231086', '东宁市', '3');
INSERT INTO `t_city` VALUES ('231100', '黑河市', '2');
INSERT INTO `t_city` VALUES ('231101', '市辖区', '3');
INSERT INTO `t_city` VALUES ('231102', '爱辉区', '3');
INSERT INTO `t_city` VALUES ('231121', '嫩江县', '3');
INSERT INTO `t_city` VALUES ('231123', '逊克县', '3');
INSERT INTO `t_city` VALUES ('231124', '孙吴县', '3');
INSERT INTO `t_city` VALUES ('231181', '北安市', '3');
INSERT INTO `t_city` VALUES ('231182', '五大连池市', '3');
INSERT INTO `t_city` VALUES ('231200', '绥化市', '2');
INSERT INTO `t_city` VALUES ('231201', '市辖区', '3');
INSERT INTO `t_city` VALUES ('231202', '北林区', '3');
INSERT INTO `t_city` VALUES ('231221', '望奎县', '3');
INSERT INTO `t_city` VALUES ('231222', '兰西县', '3');
INSERT INTO `t_city` VALUES ('231223', '青冈县', '3');
INSERT INTO `t_city` VALUES ('231224', '庆安县', '3');
INSERT INTO `t_city` VALUES ('231225', '明水县', '3');
INSERT INTO `t_city` VALUES ('231226', '绥棱县', '3');
INSERT INTO `t_city` VALUES ('231281', '安达市', '3');
INSERT INTO `t_city` VALUES ('231282', '肇东市', '3');
INSERT INTO `t_city` VALUES ('231283', '海伦市', '3');
INSERT INTO `t_city` VALUES ('232700', '大兴安岭地区', '2');
INSERT INTO `t_city` VALUES ('232721', '呼玛县', '3');
INSERT INTO `t_city` VALUES ('232722', '塔河县', '3');
INSERT INTO `t_city` VALUES ('232723', '漠河县', '3');
INSERT INTO `t_city` VALUES ('310000', '上海市', '1');
INSERT INTO `t_city` VALUES ('310100', '市辖区', '2');
INSERT INTO `t_city` VALUES ('310101', '黄浦区', '3');
INSERT INTO `t_city` VALUES ('310104', '徐汇区', '3');
INSERT INTO `t_city` VALUES ('310105', '长宁区', '3');
INSERT INTO `t_city` VALUES ('310106', '静安区', '3');
INSERT INTO `t_city` VALUES ('310107', '普陀区', '3');
INSERT INTO `t_city` VALUES ('310109', '虹口区', '3');
INSERT INTO `t_city` VALUES ('310110', '杨浦区', '3');
INSERT INTO `t_city` VALUES ('310112', '闵行区', '3');
INSERT INTO `t_city` VALUES ('310113', '宝山区', '3');
INSERT INTO `t_city` VALUES ('310114', '嘉定区', '3');
INSERT INTO `t_city` VALUES ('310115', '浦东新区', '3');
INSERT INTO `t_city` VALUES ('310116', '金山区', '3');
INSERT INTO `t_city` VALUES ('310117', '松江区', '3');
INSERT INTO `t_city` VALUES ('310118', '青浦区', '3');
INSERT INTO `t_city` VALUES ('310120', '奉贤区', '3');
INSERT INTO `t_city` VALUES ('310151', '崇明区', '3');
INSERT INTO `t_city` VALUES ('320000', '江苏省', '1');
INSERT INTO `t_city` VALUES ('320100', '南京市', '2');
INSERT INTO `t_city` VALUES ('320101', '市辖区', '3');
INSERT INTO `t_city` VALUES ('320102', '玄武区', '3');
INSERT INTO `t_city` VALUES ('320104', '秦淮区', '3');
INSERT INTO `t_city` VALUES ('320105', '建邺区', '3');
INSERT INTO `t_city` VALUES ('320106', '鼓楼区', '3');
INSERT INTO `t_city` VALUES ('320111', '浦口区', '3');
INSERT INTO `t_city` VALUES ('320113', '栖霞区', '3');
INSERT INTO `t_city` VALUES ('320114', '雨花台区', '3');
INSERT INTO `t_city` VALUES ('320115', '江宁区', '3');
INSERT INTO `t_city` VALUES ('320116', '六合区', '3');
INSERT INTO `t_city` VALUES ('320117', '溧水区', '3');
INSERT INTO `t_city` VALUES ('320118', '高淳区', '3');
INSERT INTO `t_city` VALUES ('320200', '无锡市', '2');
INSERT INTO `t_city` VALUES ('320201', '市辖区', '3');
INSERT INTO `t_city` VALUES ('320205', '锡山区', '3');
INSERT INTO `t_city` VALUES ('320206', '惠山区', '3');
INSERT INTO `t_city` VALUES ('320211', '滨湖区', '3');
INSERT INTO `t_city` VALUES ('320213', '梁溪区', '3');
INSERT INTO `t_city` VALUES ('320214', '新吴区', '3');
INSERT INTO `t_city` VALUES ('320281', '江阴市', '3');
INSERT INTO `t_city` VALUES ('320282', '宜兴市', '3');
INSERT INTO `t_city` VALUES ('320300', '徐州市', '2');
INSERT INTO `t_city` VALUES ('320301', '市辖区', '3');
INSERT INTO `t_city` VALUES ('320302', '鼓楼区', '3');
INSERT INTO `t_city` VALUES ('320303', '云龙区', '3');
INSERT INTO `t_city` VALUES ('320305', '贾汪区', '3');
INSERT INTO `t_city` VALUES ('320311', '泉山区', '3');
INSERT INTO `t_city` VALUES ('320312', '铜山区', '3');
INSERT INTO `t_city` VALUES ('320321', '丰县', '3');
INSERT INTO `t_city` VALUES ('320322', '沛县', '3');
INSERT INTO `t_city` VALUES ('320324', '睢宁县', '3');
INSERT INTO `t_city` VALUES ('320381', '新沂市', '3');
INSERT INTO `t_city` VALUES ('320382', '邳州市', '3');
INSERT INTO `t_city` VALUES ('320400', '常州市', '2');
INSERT INTO `t_city` VALUES ('320401', '市辖区', '3');
INSERT INTO `t_city` VALUES ('320402', '天宁区', '3');
INSERT INTO `t_city` VALUES ('320404', '钟楼区', '3');
INSERT INTO `t_city` VALUES ('320411', '新北区', '3');
INSERT INTO `t_city` VALUES ('320412', '武进区', '3');
INSERT INTO `t_city` VALUES ('320413', '金坛区', '3');
INSERT INTO `t_city` VALUES ('320481', '溧阳市', '3');
INSERT INTO `t_city` VALUES ('320500', '苏州市', '2');
INSERT INTO `t_city` VALUES ('320501', '市辖区', '3');
INSERT INTO `t_city` VALUES ('320505', '虎丘区', '3');
INSERT INTO `t_city` VALUES ('320506', '吴中区', '3');
INSERT INTO `t_city` VALUES ('320507', '相城区', '3');
INSERT INTO `t_city` VALUES ('320508', '姑苏区', '3');
INSERT INTO `t_city` VALUES ('320509', '吴江区', '3');
INSERT INTO `t_city` VALUES ('320581', '常熟市', '3');
INSERT INTO `t_city` VALUES ('320582', '张家港市', '3');
INSERT INTO `t_city` VALUES ('320583', '昆山市', '3');
INSERT INTO `t_city` VALUES ('320585', '太仓市', '3');
INSERT INTO `t_city` VALUES ('320600', '南通市', '2');
INSERT INTO `t_city` VALUES ('320601', '市辖区', '3');
INSERT INTO `t_city` VALUES ('320602', '崇川区', '3');
INSERT INTO `t_city` VALUES ('320611', '港闸区', '3');
INSERT INTO `t_city` VALUES ('320612', '通州区', '3');
INSERT INTO `t_city` VALUES ('320621', '海安县', '3');
INSERT INTO `t_city` VALUES ('320623', '如东县', '3');
INSERT INTO `t_city` VALUES ('320681', '启东市', '3');
INSERT INTO `t_city` VALUES ('320682', '如皋市', '3');
INSERT INTO `t_city` VALUES ('320684', '海门市', '3');
INSERT INTO `t_city` VALUES ('320700', '连云港市', '2');
INSERT INTO `t_city` VALUES ('320701', '市辖区', '3');
INSERT INTO `t_city` VALUES ('320703', '连云区', '3');
INSERT INTO `t_city` VALUES ('320706', '海州区', '3');
INSERT INTO `t_city` VALUES ('320707', '赣榆区', '3');
INSERT INTO `t_city` VALUES ('320722', '东海县', '3');
INSERT INTO `t_city` VALUES ('320723', '灌云县', '3');
INSERT INTO `t_city` VALUES ('320724', '灌南县', '3');
INSERT INTO `t_city` VALUES ('320800', '淮安市', '2');
INSERT INTO `t_city` VALUES ('320801', '市辖区', '3');
INSERT INTO `t_city` VALUES ('320803', '淮安区', '3');
INSERT INTO `t_city` VALUES ('320804', '淮阴区', '3');
INSERT INTO `t_city` VALUES ('320812', '清江浦区', '3');
INSERT INTO `t_city` VALUES ('320813', '洪泽区', '3');
INSERT INTO `t_city` VALUES ('320826', '涟水县', '3');
INSERT INTO `t_city` VALUES ('320830', '盱眙县', '3');
INSERT INTO `t_city` VALUES ('320831', '金湖县', '3');
INSERT INTO `t_city` VALUES ('320900', '盐城市', '2');
INSERT INTO `t_city` VALUES ('320901', '市辖区', '3');
INSERT INTO `t_city` VALUES ('320902', '亭湖区', '3');
INSERT INTO `t_city` VALUES ('320903', '盐都区', '3');
INSERT INTO `t_city` VALUES ('320904', '大丰区', '3');
INSERT INTO `t_city` VALUES ('320921', '响水县', '3');
INSERT INTO `t_city` VALUES ('320922', '滨海县', '3');
INSERT INTO `t_city` VALUES ('320923', '阜宁县', '3');
INSERT INTO `t_city` VALUES ('320924', '射阳县', '3');
INSERT INTO `t_city` VALUES ('320925', '建湖县', '3');
INSERT INTO `t_city` VALUES ('320981', '东台市', '3');
INSERT INTO `t_city` VALUES ('321000', '扬州市', '2');
INSERT INTO `t_city` VALUES ('321001', '市辖区', '3');
INSERT INTO `t_city` VALUES ('321002', '广陵区', '3');
INSERT INTO `t_city` VALUES ('321003', '邗江区', '3');
INSERT INTO `t_city` VALUES ('321012', '江都区', '3');
INSERT INTO `t_city` VALUES ('321023', '宝应县', '3');
INSERT INTO `t_city` VALUES ('321081', '仪征市', '3');
INSERT INTO `t_city` VALUES ('321084', '高邮市', '3');
INSERT INTO `t_city` VALUES ('321100', '镇江市', '2');
INSERT INTO `t_city` VALUES ('321101', '市辖区', '3');
INSERT INTO `t_city` VALUES ('321102', '京口区', '3');
INSERT INTO `t_city` VALUES ('321111', '润州区', '3');
INSERT INTO `t_city` VALUES ('321112', '丹徒区', '3');
INSERT INTO `t_city` VALUES ('321181', '丹阳市', '3');
INSERT INTO `t_city` VALUES ('321182', '扬中市', '3');
INSERT INTO `t_city` VALUES ('321183', '句容市', '3');
INSERT INTO `t_city` VALUES ('321200', '泰州市', '2');
INSERT INTO `t_city` VALUES ('321201', '市辖区', '3');
INSERT INTO `t_city` VALUES ('321202', '海陵区', '3');
INSERT INTO `t_city` VALUES ('321203', '高港区', '3');
INSERT INTO `t_city` VALUES ('321204', '姜堰区', '3');
INSERT INTO `t_city` VALUES ('321281', '兴化市', '3');
INSERT INTO `t_city` VALUES ('321282', '靖江市', '3');
INSERT INTO `t_city` VALUES ('321283', '泰兴市', '3');
INSERT INTO `t_city` VALUES ('321300', '宿迁市', '2');
INSERT INTO `t_city` VALUES ('321301', '市辖区', '3');
INSERT INTO `t_city` VALUES ('321302', '宿城区', '3');
INSERT INTO `t_city` VALUES ('321311', '宿豫区', '3');
INSERT INTO `t_city` VALUES ('321322', '沭阳县', '3');
INSERT INTO `t_city` VALUES ('321323', '泗阳县', '3');
INSERT INTO `t_city` VALUES ('321324', '泗洪县', '3');
INSERT INTO `t_city` VALUES ('330000', '浙江省', '1');
INSERT INTO `t_city` VALUES ('330100', '杭州市', '2');
INSERT INTO `t_city` VALUES ('330101', '市辖区', '3');
INSERT INTO `t_city` VALUES ('330102', '上城区', '3');
INSERT INTO `t_city` VALUES ('330103', '下城区', '3');
INSERT INTO `t_city` VALUES ('330104', '江干区', '3');
INSERT INTO `t_city` VALUES ('330105', '拱墅区', '3');
INSERT INTO `t_city` VALUES ('330106', '西湖区', '3');
INSERT INTO `t_city` VALUES ('330108', '滨江区', '3');
INSERT INTO `t_city` VALUES ('330109', '萧山区', '3');
INSERT INTO `t_city` VALUES ('330110', '余杭区', '3');
INSERT INTO `t_city` VALUES ('330111', '富阳区', '3');
INSERT INTO `t_city` VALUES ('330122', '桐庐县', '3');
INSERT INTO `t_city` VALUES ('330127', '淳安县', '3');
INSERT INTO `t_city` VALUES ('330182', '建德市', '3');
INSERT INTO `t_city` VALUES ('330185', '临安市', '3');
INSERT INTO `t_city` VALUES ('330200', '宁波市', '2');
INSERT INTO `t_city` VALUES ('330201', '市辖区', '3');
INSERT INTO `t_city` VALUES ('330203', '海曙区', '3');
INSERT INTO `t_city` VALUES ('330204', '江东区', '3');
INSERT INTO `t_city` VALUES ('330205', '江北区', '3');
INSERT INTO `t_city` VALUES ('330206', '北仑区', '3');
INSERT INTO `t_city` VALUES ('330211', '镇海区', '3');
INSERT INTO `t_city` VALUES ('330212', '鄞州区', '3');
INSERT INTO `t_city` VALUES ('330225', '象山县', '3');
INSERT INTO `t_city` VALUES ('330226', '宁海县', '3');
INSERT INTO `t_city` VALUES ('330281', '余姚市', '3');
INSERT INTO `t_city` VALUES ('330282', '慈溪市', '3');
INSERT INTO `t_city` VALUES ('330283', '奉化市', '3');
INSERT INTO `t_city` VALUES ('330300', '温州市', '2');
INSERT INTO `t_city` VALUES ('330301', '市辖区', '3');
INSERT INTO `t_city` VALUES ('330302', '鹿城区', '3');
INSERT INTO `t_city` VALUES ('330303', '龙湾区', '3');
INSERT INTO `t_city` VALUES ('330304', '瓯海区', '3');
INSERT INTO `t_city` VALUES ('330305', '洞头区', '3');
INSERT INTO `t_city` VALUES ('330324', '永嘉县', '3');
INSERT INTO `t_city` VALUES ('330326', '平阳县', '3');
INSERT INTO `t_city` VALUES ('330327', '苍南县', '3');
INSERT INTO `t_city` VALUES ('330328', '文成县', '3');
INSERT INTO `t_city` VALUES ('330329', '泰顺县', '3');
INSERT INTO `t_city` VALUES ('330381', '瑞安市', '3');
INSERT INTO `t_city` VALUES ('330382', '乐清市', '3');
INSERT INTO `t_city` VALUES ('330400', '嘉兴市', '2');
INSERT INTO `t_city` VALUES ('330401', '市辖区', '3');
INSERT INTO `t_city` VALUES ('330402', '南湖区', '3');
INSERT INTO `t_city` VALUES ('330411', '秀洲区', '3');
INSERT INTO `t_city` VALUES ('330421', '嘉善县', '3');
INSERT INTO `t_city` VALUES ('330424', '海盐县', '3');
INSERT INTO `t_city` VALUES ('330481', '海宁市', '3');
INSERT INTO `t_city` VALUES ('330482', '平湖市', '3');
INSERT INTO `t_city` VALUES ('330483', '桐乡市', '3');
INSERT INTO `t_city` VALUES ('330500', '湖州市', '2');
INSERT INTO `t_city` VALUES ('330501', '市辖区', '3');
INSERT INTO `t_city` VALUES ('330502', '吴兴区', '3');
INSERT INTO `t_city` VALUES ('330503', '南浔区', '3');
INSERT INTO `t_city` VALUES ('330521', '德清县', '3');
INSERT INTO `t_city` VALUES ('330522', '长兴县', '3');
INSERT INTO `t_city` VALUES ('330523', '安吉县', '3');
INSERT INTO `t_city` VALUES ('330600', '绍兴市', '2');
INSERT INTO `t_city` VALUES ('330601', '市辖区', '3');
INSERT INTO `t_city` VALUES ('330602', '越城区', '3');
INSERT INTO `t_city` VALUES ('330603', '柯桥区', '3');
INSERT INTO `t_city` VALUES ('330604', '上虞区', '3');
INSERT INTO `t_city` VALUES ('330624', '新昌县', '3');
INSERT INTO `t_city` VALUES ('330681', '诸暨市', '3');
INSERT INTO `t_city` VALUES ('330683', '嵊州市', '3');
INSERT INTO `t_city` VALUES ('330700', '金华市', '2');
INSERT INTO `t_city` VALUES ('330701', '市辖区', '3');
INSERT INTO `t_city` VALUES ('330702', '婺城区', '3');
INSERT INTO `t_city` VALUES ('330703', '金东区', '3');
INSERT INTO `t_city` VALUES ('330723', '武义县', '3');
INSERT INTO `t_city` VALUES ('330726', '浦江县', '3');
INSERT INTO `t_city` VALUES ('330727', '磐安县', '3');
INSERT INTO `t_city` VALUES ('330781', '兰溪市', '3');
INSERT INTO `t_city` VALUES ('330782', '义乌市', '3');
INSERT INTO `t_city` VALUES ('330783', '东阳市', '3');
INSERT INTO `t_city` VALUES ('330784', '永康市', '3');
INSERT INTO `t_city` VALUES ('330800', '衢州市', '2');
INSERT INTO `t_city` VALUES ('330801', '市辖区', '3');
INSERT INTO `t_city` VALUES ('330802', '柯城区', '3');
INSERT INTO `t_city` VALUES ('330803', '衢江区', '3');
INSERT INTO `t_city` VALUES ('330822', '常山县', '3');
INSERT INTO `t_city` VALUES ('330824', '开化县', '3');
INSERT INTO `t_city` VALUES ('330825', '龙游县', '3');
INSERT INTO `t_city` VALUES ('330881', '江山市', '3');
INSERT INTO `t_city` VALUES ('330900', '舟山市', '2');
INSERT INTO `t_city` VALUES ('330901', '市辖区', '3');
INSERT INTO `t_city` VALUES ('330902', '定海区', '3');
INSERT INTO `t_city` VALUES ('330903', '普陀区', '3');
INSERT INTO `t_city` VALUES ('330921', '岱山县', '3');
INSERT INTO `t_city` VALUES ('330922', '嵊泗县', '3');
INSERT INTO `t_city` VALUES ('331000', '台州市', '2');
INSERT INTO `t_city` VALUES ('331001', '市辖区', '3');
INSERT INTO `t_city` VALUES ('331002', '椒江区', '3');
INSERT INTO `t_city` VALUES ('331003', '黄岩区', '3');
INSERT INTO `t_city` VALUES ('331004', '路桥区', '3');
INSERT INTO `t_city` VALUES ('331021', '玉环县', '3');
INSERT INTO `t_city` VALUES ('331022', '三门县', '3');
INSERT INTO `t_city` VALUES ('331023', '天台县', '3');
INSERT INTO `t_city` VALUES ('331024', '仙居县', '3');
INSERT INTO `t_city` VALUES ('331081', '温岭市', '3');
INSERT INTO `t_city` VALUES ('331082', '临海市', '3');
INSERT INTO `t_city` VALUES ('331100', '丽水市', '2');
INSERT INTO `t_city` VALUES ('331101', '市辖区', '3');
INSERT INTO `t_city` VALUES ('331102', '莲都区', '3');
INSERT INTO `t_city` VALUES ('331121', '青田县', '3');
INSERT INTO `t_city` VALUES ('331122', '缙云县', '3');
INSERT INTO `t_city` VALUES ('331123', '遂昌县', '3');
INSERT INTO `t_city` VALUES ('331124', '松阳县', '3');
INSERT INTO `t_city` VALUES ('331125', '云和县', '3');
INSERT INTO `t_city` VALUES ('331126', '庆元县', '3');
INSERT INTO `t_city` VALUES ('331127', '景宁畲族自治县', '3');
INSERT INTO `t_city` VALUES ('331181', '龙泉市', '3');
INSERT INTO `t_city` VALUES ('340000', '安徽省', '1');
INSERT INTO `t_city` VALUES ('340100', '合肥市', '2');
INSERT INTO `t_city` VALUES ('340101', '市辖区', '3');
INSERT INTO `t_city` VALUES ('340102', '瑶海区', '3');
INSERT INTO `t_city` VALUES ('340103', '庐阳区', '3');
INSERT INTO `t_city` VALUES ('340104', '蜀山区', '3');
INSERT INTO `t_city` VALUES ('340111', '包河区', '3');
INSERT INTO `t_city` VALUES ('340121', '长丰县', '3');
INSERT INTO `t_city` VALUES ('340122', '肥东县', '3');
INSERT INTO `t_city` VALUES ('340123', '肥西县', '3');
INSERT INTO `t_city` VALUES ('340124', '庐江县', '3');
INSERT INTO `t_city` VALUES ('340181', '巢湖市', '3');
INSERT INTO `t_city` VALUES ('340200', '芜湖市', '2');
INSERT INTO `t_city` VALUES ('340201', '市辖区', '3');
INSERT INTO `t_city` VALUES ('340202', '镜湖区', '3');
INSERT INTO `t_city` VALUES ('340203', '弋江区', '3');
INSERT INTO `t_city` VALUES ('340207', '鸠江区', '3');
INSERT INTO `t_city` VALUES ('340208', '三山区', '3');
INSERT INTO `t_city` VALUES ('340221', '芜湖县', '3');
INSERT INTO `t_city` VALUES ('340222', '繁昌县', '3');
INSERT INTO `t_city` VALUES ('340223', '南陵县', '3');
INSERT INTO `t_city` VALUES ('340225', '无为县', '3');
INSERT INTO `t_city` VALUES ('340300', '蚌埠市', '2');
INSERT INTO `t_city` VALUES ('340301', '市辖区', '3');
INSERT INTO `t_city` VALUES ('340302', '龙子湖区', '3');
INSERT INTO `t_city` VALUES ('340303', '蚌山区', '3');
INSERT INTO `t_city` VALUES ('340304', '禹会区', '3');
INSERT INTO `t_city` VALUES ('340311', '淮上区', '3');
INSERT INTO `t_city` VALUES ('340321', '怀远县', '3');
INSERT INTO `t_city` VALUES ('340322', '五河县', '3');
INSERT INTO `t_city` VALUES ('340323', '固镇县', '3');
INSERT INTO `t_city` VALUES ('340400', '淮南市', '2');
INSERT INTO `t_city` VALUES ('340401', '市辖区', '3');
INSERT INTO `t_city` VALUES ('340402', '大通区', '3');
INSERT INTO `t_city` VALUES ('340403', '田家庵区', '3');
INSERT INTO `t_city` VALUES ('340404', '谢家集区', '3');
INSERT INTO `t_city` VALUES ('340405', '八公山区', '3');
INSERT INTO `t_city` VALUES ('340406', '潘集区', '3');
INSERT INTO `t_city` VALUES ('340421', '凤台县', '3');
INSERT INTO `t_city` VALUES ('340422', '寿县', '3');
INSERT INTO `t_city` VALUES ('340500', '马鞍山市', '2');
INSERT INTO `t_city` VALUES ('340501', '市辖区', '3');
INSERT INTO `t_city` VALUES ('340503', '花山区', '3');
INSERT INTO `t_city` VALUES ('340504', '雨山区', '3');
INSERT INTO `t_city` VALUES ('340506', '博望区', '3');
INSERT INTO `t_city` VALUES ('340521', '当涂县', '3');
INSERT INTO `t_city` VALUES ('340522', '含山县', '3');
INSERT INTO `t_city` VALUES ('340523', '和县', '3');
INSERT INTO `t_city` VALUES ('340600', '淮北市', '2');
INSERT INTO `t_city` VALUES ('340601', '市辖区', '3');
INSERT INTO `t_city` VALUES ('340602', '杜集区', '3');
INSERT INTO `t_city` VALUES ('340603', '相山区', '3');
INSERT INTO `t_city` VALUES ('340604', '烈山区', '3');
INSERT INTO `t_city` VALUES ('340621', '濉溪县', '3');
INSERT INTO `t_city` VALUES ('340700', '铜陵市', '2');
INSERT INTO `t_city` VALUES ('340701', '市辖区', '3');
INSERT INTO `t_city` VALUES ('340705', '铜官区', '3');
INSERT INTO `t_city` VALUES ('340706', '义安区', '3');
INSERT INTO `t_city` VALUES ('340711', '郊区', '3');
INSERT INTO `t_city` VALUES ('340722', '枞阳县', '3');
INSERT INTO `t_city` VALUES ('340800', '安庆市', '2');
INSERT INTO `t_city` VALUES ('340801', '市辖区', '3');
INSERT INTO `t_city` VALUES ('340802', '迎江区', '3');
INSERT INTO `t_city` VALUES ('340803', '大观区', '3');
INSERT INTO `t_city` VALUES ('340811', '宜秀区', '3');
INSERT INTO `t_city` VALUES ('340822', '怀宁县', '3');
INSERT INTO `t_city` VALUES ('340824', '潜山县', '3');
INSERT INTO `t_city` VALUES ('340825', '太湖县', '3');
INSERT INTO `t_city` VALUES ('340826', '宿松县', '3');
INSERT INTO `t_city` VALUES ('340827', '望江县', '3');
INSERT INTO `t_city` VALUES ('340828', '岳西县', '3');
INSERT INTO `t_city` VALUES ('340881', '桐城市', '3');
INSERT INTO `t_city` VALUES ('341000', '黄山市', '2');
INSERT INTO `t_city` VALUES ('341001', '市辖区', '3');
INSERT INTO `t_city` VALUES ('341002', '屯溪区', '3');
INSERT INTO `t_city` VALUES ('341003', '黄山区', '3');
INSERT INTO `t_city` VALUES ('341004', '徽州区', '3');
INSERT INTO `t_city` VALUES ('341021', '歙县', '3');
INSERT INTO `t_city` VALUES ('341022', '休宁县', '3');
INSERT INTO `t_city` VALUES ('341023', '黟县', '3');
INSERT INTO `t_city` VALUES ('341024', '祁门县', '3');
INSERT INTO `t_city` VALUES ('341100', '滁州市', '2');
INSERT INTO `t_city` VALUES ('341101', '市辖区', '3');
INSERT INTO `t_city` VALUES ('341102', '琅琊区', '3');
INSERT INTO `t_city` VALUES ('341103', '南谯区', '3');
INSERT INTO `t_city` VALUES ('341122', '来安县', '3');
INSERT INTO `t_city` VALUES ('341124', '全椒县', '3');
INSERT INTO `t_city` VALUES ('341125', '定远县', '3');
INSERT INTO `t_city` VALUES ('341126', '凤阳县', '3');
INSERT INTO `t_city` VALUES ('341181', '天长市', '3');
INSERT INTO `t_city` VALUES ('341182', '明光市', '3');
INSERT INTO `t_city` VALUES ('341200', '阜阳市', '2');
INSERT INTO `t_city` VALUES ('341201', '市辖区', '3');
INSERT INTO `t_city` VALUES ('341202', '颍州区', '3');
INSERT INTO `t_city` VALUES ('341203', '颍东区', '3');
INSERT INTO `t_city` VALUES ('341204', '颍泉区', '3');
INSERT INTO `t_city` VALUES ('341221', '临泉县', '3');
INSERT INTO `t_city` VALUES ('341222', '太和县', '3');
INSERT INTO `t_city` VALUES ('341225', '阜南县', '3');
INSERT INTO `t_city` VALUES ('341226', '颍上县', '3');
INSERT INTO `t_city` VALUES ('341282', '界首市', '3');
INSERT INTO `t_city` VALUES ('341300', '宿州市', '2');
INSERT INTO `t_city` VALUES ('341301', '市辖区', '3');
INSERT INTO `t_city` VALUES ('341302', '埇桥区', '3');
INSERT INTO `t_city` VALUES ('341321', '砀山县', '3');
INSERT INTO `t_city` VALUES ('341322', '萧县', '3');
INSERT INTO `t_city` VALUES ('341323', '灵璧县', '3');
INSERT INTO `t_city` VALUES ('341324', '泗县', '3');
INSERT INTO `t_city` VALUES ('341500', '六安市', '2');
INSERT INTO `t_city` VALUES ('341501', '市辖区', '3');
INSERT INTO `t_city` VALUES ('341502', '金安区', '3');
INSERT INTO `t_city` VALUES ('341503', '裕安区', '3');
INSERT INTO `t_city` VALUES ('341504', '叶集区', '3');
INSERT INTO `t_city` VALUES ('341522', '霍邱县', '3');
INSERT INTO `t_city` VALUES ('341523', '舒城县', '3');
INSERT INTO `t_city` VALUES ('341524', '金寨县', '3');
INSERT INTO `t_city` VALUES ('341525', '霍山县', '3');
INSERT INTO `t_city` VALUES ('341600', '亳州市', '2');
INSERT INTO `t_city` VALUES ('341601', '市辖区', '3');
INSERT INTO `t_city` VALUES ('341602', '谯城区', '3');
INSERT INTO `t_city` VALUES ('341621', '涡阳县', '3');
INSERT INTO `t_city` VALUES ('341622', '蒙城县', '3');
INSERT INTO `t_city` VALUES ('341623', '利辛县', '3');
INSERT INTO `t_city` VALUES ('341700', '池州市', '2');
INSERT INTO `t_city` VALUES ('341701', '市辖区', '3');
INSERT INTO `t_city` VALUES ('341702', '贵池区', '3');
INSERT INTO `t_city` VALUES ('341721', '东至县', '3');
INSERT INTO `t_city` VALUES ('341722', '石台县', '3');
INSERT INTO `t_city` VALUES ('341723', '青阳县', '3');
INSERT INTO `t_city` VALUES ('341800', '宣城市', '2');
INSERT INTO `t_city` VALUES ('341801', '市辖区', '3');
INSERT INTO `t_city` VALUES ('341802', '宣州区', '3');
INSERT INTO `t_city` VALUES ('341821', '郎溪县', '3');
INSERT INTO `t_city` VALUES ('341822', '广德县', '3');
INSERT INTO `t_city` VALUES ('341823', '泾县', '3');
INSERT INTO `t_city` VALUES ('341824', '绩溪县', '3');
INSERT INTO `t_city` VALUES ('341825', '旌德县', '3');
INSERT INTO `t_city` VALUES ('341881', '宁国市', '3');
INSERT INTO `t_city` VALUES ('350000', '福建省', '1');
INSERT INTO `t_city` VALUES ('350100', '福州市', '2');
INSERT INTO `t_city` VALUES ('350101', '市辖区', '3');
INSERT INTO `t_city` VALUES ('350102', '鼓楼区', '3');
INSERT INTO `t_city` VALUES ('350103', '台江区', '3');
INSERT INTO `t_city` VALUES ('350104', '仓山区', '3');
INSERT INTO `t_city` VALUES ('350105', '马尾区', '3');
INSERT INTO `t_city` VALUES ('350111', '晋安区', '3');
INSERT INTO `t_city` VALUES ('350121', '闽侯县', '3');
INSERT INTO `t_city` VALUES ('350122', '连江县', '3');
INSERT INTO `t_city` VALUES ('350123', '罗源县', '3');
INSERT INTO `t_city` VALUES ('350124', '闽清县', '3');
INSERT INTO `t_city` VALUES ('350125', '永泰县', '3');
INSERT INTO `t_city` VALUES ('350128', '平潭县', '3');
INSERT INTO `t_city` VALUES ('350181', '福清市', '3');
INSERT INTO `t_city` VALUES ('350182', '长乐市', '3');
INSERT INTO `t_city` VALUES ('350200', '厦门市', '2');
INSERT INTO `t_city` VALUES ('350201', '市辖区', '3');
INSERT INTO `t_city` VALUES ('350203', '思明区', '3');
INSERT INTO `t_city` VALUES ('350205', '海沧区', '3');
INSERT INTO `t_city` VALUES ('350206', '湖里区', '3');
INSERT INTO `t_city` VALUES ('350211', '集美区', '3');
INSERT INTO `t_city` VALUES ('350212', '同安区', '3');
INSERT INTO `t_city` VALUES ('350213', '翔安区', '3');
INSERT INTO `t_city` VALUES ('350300', '莆田市', '2');
INSERT INTO `t_city` VALUES ('350301', '市辖区', '3');
INSERT INTO `t_city` VALUES ('350302', '城厢区', '3');
INSERT INTO `t_city` VALUES ('350303', '涵江区', '3');
INSERT INTO `t_city` VALUES ('350304', '荔城区', '3');
INSERT INTO `t_city` VALUES ('350305', '秀屿区', '3');
INSERT INTO `t_city` VALUES ('350322', '仙游县', '3');
INSERT INTO `t_city` VALUES ('350400', '三明市', '2');
INSERT INTO `t_city` VALUES ('350401', '市辖区', '3');
INSERT INTO `t_city` VALUES ('350402', '梅列区', '3');
INSERT INTO `t_city` VALUES ('350403', '三元区', '3');
INSERT INTO `t_city` VALUES ('350421', '明溪县', '3');
INSERT INTO `t_city` VALUES ('350423', '清流县', '3');
INSERT INTO `t_city` VALUES ('350424', '宁化县', '3');
INSERT INTO `t_city` VALUES ('350425', '大田县', '3');
INSERT INTO `t_city` VALUES ('350426', '尤溪县', '3');
INSERT INTO `t_city` VALUES ('350427', '沙县', '3');
INSERT INTO `t_city` VALUES ('350428', '将乐县', '3');
INSERT INTO `t_city` VALUES ('350429', '泰宁县', '3');
INSERT INTO `t_city` VALUES ('350430', '建宁县', '3');
INSERT INTO `t_city` VALUES ('350481', '永安市', '3');
INSERT INTO `t_city` VALUES ('350500', '泉州市', '2');
INSERT INTO `t_city` VALUES ('350501', '市辖区', '3');
INSERT INTO `t_city` VALUES ('350502', '鲤城区', '3');
INSERT INTO `t_city` VALUES ('350503', '丰泽区', '3');
INSERT INTO `t_city` VALUES ('350504', '洛江区', '3');
INSERT INTO `t_city` VALUES ('350505', '泉港区', '3');
INSERT INTO `t_city` VALUES ('350521', '惠安县', '3');
INSERT INTO `t_city` VALUES ('350524', '安溪县', '3');
INSERT INTO `t_city` VALUES ('350525', '永春县', '3');
INSERT INTO `t_city` VALUES ('350526', '德化县', '3');
INSERT INTO `t_city` VALUES ('350527', '金门县', '3');
INSERT INTO `t_city` VALUES ('350581', '石狮市', '3');
INSERT INTO `t_city` VALUES ('350582', '晋江市', '3');
INSERT INTO `t_city` VALUES ('350583', '南安市', '3');
INSERT INTO `t_city` VALUES ('350600', '漳州市', '2');
INSERT INTO `t_city` VALUES ('350601', '市辖区', '3');
INSERT INTO `t_city` VALUES ('350602', '芗城区', '3');
INSERT INTO `t_city` VALUES ('350603', '龙文区', '3');
INSERT INTO `t_city` VALUES ('350622', '云霄县', '3');
INSERT INTO `t_city` VALUES ('350623', '漳浦县', '3');
INSERT INTO `t_city` VALUES ('350624', '诏安县', '3');
INSERT INTO `t_city` VALUES ('350625', '长泰县', '3');
INSERT INTO `t_city` VALUES ('350626', '东山县', '3');
INSERT INTO `t_city` VALUES ('350627', '南靖县', '3');
INSERT INTO `t_city` VALUES ('350628', '平和县', '3');
INSERT INTO `t_city` VALUES ('350629', '华安县', '3');
INSERT INTO `t_city` VALUES ('350681', '龙海市', '3');
INSERT INTO `t_city` VALUES ('350700', '南平市', '2');
INSERT INTO `t_city` VALUES ('350701', '市辖区', '3');
INSERT INTO `t_city` VALUES ('350702', '延平区', '3');
INSERT INTO `t_city` VALUES ('350703', '建阳区', '3');
INSERT INTO `t_city` VALUES ('350721', '顺昌县', '3');
INSERT INTO `t_city` VALUES ('350722', '浦城县', '3');
INSERT INTO `t_city` VALUES ('350723', '光泽县', '3');
INSERT INTO `t_city` VALUES ('350724', '松溪县', '3');
INSERT INTO `t_city` VALUES ('350725', '政和县', '3');
INSERT INTO `t_city` VALUES ('350781', '邵武市', '3');
INSERT INTO `t_city` VALUES ('350782', '武夷山市', '3');
INSERT INTO `t_city` VALUES ('350783', '建瓯市', '3');
INSERT INTO `t_city` VALUES ('350800', '龙岩市', '2');
INSERT INTO `t_city` VALUES ('350801', '市辖区', '3');
INSERT INTO `t_city` VALUES ('350802', '新罗区', '3');
INSERT INTO `t_city` VALUES ('350803', '永定区', '3');
INSERT INTO `t_city` VALUES ('350821', '长汀县', '3');
INSERT INTO `t_city` VALUES ('350823', '上杭县', '3');
INSERT INTO `t_city` VALUES ('350824', '武平县', '3');
INSERT INTO `t_city` VALUES ('350825', '连城县', '3');
INSERT INTO `t_city` VALUES ('350881', '漳平市', '3');
INSERT INTO `t_city` VALUES ('350900', '宁德市', '2');
INSERT INTO `t_city` VALUES ('350901', '市辖区', '3');
INSERT INTO `t_city` VALUES ('350902', '蕉城区', '3');
INSERT INTO `t_city` VALUES ('350921', '霞浦县', '3');
INSERT INTO `t_city` VALUES ('350922', '古田县', '3');
INSERT INTO `t_city` VALUES ('350923', '屏南县', '3');
INSERT INTO `t_city` VALUES ('350924', '寿宁县', '3');
INSERT INTO `t_city` VALUES ('350925', '周宁县', '3');
INSERT INTO `t_city` VALUES ('350926', '柘荣县', '3');
INSERT INTO `t_city` VALUES ('350981', '福安市', '3');
INSERT INTO `t_city` VALUES ('350982', '福鼎市', '3');
INSERT INTO `t_city` VALUES ('360000', '江西省', '1');
INSERT INTO `t_city` VALUES ('360100', '南昌市', '2');
INSERT INTO `t_city` VALUES ('360101', '市辖区', '3');
INSERT INTO `t_city` VALUES ('360102', '东湖区', '3');
INSERT INTO `t_city` VALUES ('360103', '西湖区', '3');
INSERT INTO `t_city` VALUES ('360104', '青云谱区', '3');
INSERT INTO `t_city` VALUES ('360105', '湾里区', '3');
INSERT INTO `t_city` VALUES ('360111', '青山湖区', '3');
INSERT INTO `t_city` VALUES ('360112', '新建区', '3');
INSERT INTO `t_city` VALUES ('360121', '南昌县', '3');
INSERT INTO `t_city` VALUES ('360123', '安义县', '3');
INSERT INTO `t_city` VALUES ('360124', '进贤县', '3');
INSERT INTO `t_city` VALUES ('360200', '景德镇市', '2');
INSERT INTO `t_city` VALUES ('360201', '市辖区', '3');
INSERT INTO `t_city` VALUES ('360202', '昌江区', '3');
INSERT INTO `t_city` VALUES ('360203', '珠山区', '3');
INSERT INTO `t_city` VALUES ('360222', '浮梁县', '3');
INSERT INTO `t_city` VALUES ('360281', '乐平市', '3');
INSERT INTO `t_city` VALUES ('360300', '萍乡市', '2');
INSERT INTO `t_city` VALUES ('360301', '市辖区', '3');
INSERT INTO `t_city` VALUES ('360302', '安源区', '3');
INSERT INTO `t_city` VALUES ('360313', '湘东区', '3');
INSERT INTO `t_city` VALUES ('360321', '莲花县', '3');
INSERT INTO `t_city` VALUES ('360322', '上栗县', '3');
INSERT INTO `t_city` VALUES ('360323', '芦溪县', '3');
INSERT INTO `t_city` VALUES ('360400', '九江市', '2');
INSERT INTO `t_city` VALUES ('360401', '市辖区', '3');
INSERT INTO `t_city` VALUES ('360402', '濂溪区', '3');
INSERT INTO `t_city` VALUES ('360403', '浔阳区', '3');
INSERT INTO `t_city` VALUES ('360421', '九江县', '3');
INSERT INTO `t_city` VALUES ('360423', '武宁县', '3');
INSERT INTO `t_city` VALUES ('360424', '修水县', '3');
INSERT INTO `t_city` VALUES ('360425', '永修县', '3');
INSERT INTO `t_city` VALUES ('360426', '德安县', '3');
INSERT INTO `t_city` VALUES ('360428', '都昌县', '3');
INSERT INTO `t_city` VALUES ('360429', '湖口县', '3');
INSERT INTO `t_city` VALUES ('360430', '彭泽县', '3');
INSERT INTO `t_city` VALUES ('360481', '瑞昌市', '3');
INSERT INTO `t_city` VALUES ('360482', '共青城市', '3');
INSERT INTO `t_city` VALUES ('360483', '庐山市', '3');
INSERT INTO `t_city` VALUES ('360500', '新余市', '2');
INSERT INTO `t_city` VALUES ('360501', '市辖区', '3');
INSERT INTO `t_city` VALUES ('360502', '渝水区', '3');
INSERT INTO `t_city` VALUES ('360521', '分宜县', '3');
INSERT INTO `t_city` VALUES ('360600', '鹰潭市', '2');
INSERT INTO `t_city` VALUES ('360601', '市辖区', '3');
INSERT INTO `t_city` VALUES ('360602', '月湖区', '3');
INSERT INTO `t_city` VALUES ('360622', '余江县', '3');
INSERT INTO `t_city` VALUES ('360681', '贵溪市', '3');
INSERT INTO `t_city` VALUES ('360700', '赣州市', '2');
INSERT INTO `t_city` VALUES ('360701', '市辖区', '3');
INSERT INTO `t_city` VALUES ('360702', '章贡区', '3');
INSERT INTO `t_city` VALUES ('360703', '南康区', '3');
INSERT INTO `t_city` VALUES ('360721', '赣县', '3');
INSERT INTO `t_city` VALUES ('360722', '信丰县', '3');
INSERT INTO `t_city` VALUES ('360723', '大余县', '3');
INSERT INTO `t_city` VALUES ('360724', '上犹县', '3');
INSERT INTO `t_city` VALUES ('360725', '崇义县', '3');
INSERT INTO `t_city` VALUES ('360726', '安远县', '3');
INSERT INTO `t_city` VALUES ('360727', '龙南县', '3');
INSERT INTO `t_city` VALUES ('360728', '定南县', '3');
INSERT INTO `t_city` VALUES ('360729', '全南县', '3');
INSERT INTO `t_city` VALUES ('360730', '宁都县', '3');
INSERT INTO `t_city` VALUES ('360731', '于都县', '3');
INSERT INTO `t_city` VALUES ('360732', '兴国县', '3');
INSERT INTO `t_city` VALUES ('360733', '会昌县', '3');
INSERT INTO `t_city` VALUES ('360734', '寻乌县', '3');
INSERT INTO `t_city` VALUES ('360735', '石城县', '3');
INSERT INTO `t_city` VALUES ('360781', '瑞金市', '3');
INSERT INTO `t_city` VALUES ('360800', '吉安市', '2');
INSERT INTO `t_city` VALUES ('360801', '市辖区', '3');
INSERT INTO `t_city` VALUES ('360802', '吉州区', '3');
INSERT INTO `t_city` VALUES ('360803', '青原区', '3');
INSERT INTO `t_city` VALUES ('360821', '吉安县', '3');
INSERT INTO `t_city` VALUES ('360822', '吉水县', '3');
INSERT INTO `t_city` VALUES ('360823', '峡江县', '3');
INSERT INTO `t_city` VALUES ('360824', '新干县', '3');
INSERT INTO `t_city` VALUES ('360825', '永丰县', '3');
INSERT INTO `t_city` VALUES ('360826', '泰和县', '3');
INSERT INTO `t_city` VALUES ('360827', '遂川县', '3');
INSERT INTO `t_city` VALUES ('360828', '万安县', '3');
INSERT INTO `t_city` VALUES ('360829', '安福县', '3');
INSERT INTO `t_city` VALUES ('360830', '永新县', '3');
INSERT INTO `t_city` VALUES ('360881', '井冈山市', '3');
INSERT INTO `t_city` VALUES ('360900', '宜春市', '2');
INSERT INTO `t_city` VALUES ('360901', '市辖区', '3');
INSERT INTO `t_city` VALUES ('360902', '袁州区', '3');
INSERT INTO `t_city` VALUES ('360921', '奉新县', '3');
INSERT INTO `t_city` VALUES ('360922', '万载县', '3');
INSERT INTO `t_city` VALUES ('360923', '上高县', '3');
INSERT INTO `t_city` VALUES ('360924', '宜丰县', '3');
INSERT INTO `t_city` VALUES ('360925', '靖安县', '3');
INSERT INTO `t_city` VALUES ('360926', '铜鼓县', '3');
INSERT INTO `t_city` VALUES ('360981', '丰城市', '3');
INSERT INTO `t_city` VALUES ('360982', '樟树市', '3');
INSERT INTO `t_city` VALUES ('360983', '高安市', '3');
INSERT INTO `t_city` VALUES ('361000', '抚州市', '2');
INSERT INTO `t_city` VALUES ('361001', '市辖区', '3');
INSERT INTO `t_city` VALUES ('361002', '临川区', '3');
INSERT INTO `t_city` VALUES ('361021', '南城县', '3');
INSERT INTO `t_city` VALUES ('361022', '黎川县', '3');
INSERT INTO `t_city` VALUES ('361023', '南丰县', '3');
INSERT INTO `t_city` VALUES ('361024', '崇仁县', '3');
INSERT INTO `t_city` VALUES ('361025', '乐安县', '3');
INSERT INTO `t_city` VALUES ('361026', '宜黄县', '3');
INSERT INTO `t_city` VALUES ('361027', '金溪县', '3');
INSERT INTO `t_city` VALUES ('361028', '资溪县', '3');
INSERT INTO `t_city` VALUES ('361029', '东乡县', '3');
INSERT INTO `t_city` VALUES ('361030', '广昌县', '3');
INSERT INTO `t_city` VALUES ('361100', '上饶市', '2');
INSERT INTO `t_city` VALUES ('361101', '市辖区', '3');
INSERT INTO `t_city` VALUES ('361102', '信州区', '3');
INSERT INTO `t_city` VALUES ('361103', '广丰区', '3');
INSERT INTO `t_city` VALUES ('361121', '上饶县', '3');
INSERT INTO `t_city` VALUES ('361123', '玉山县', '3');
INSERT INTO `t_city` VALUES ('361124', '铅山县', '3');
INSERT INTO `t_city` VALUES ('361125', '横峰县', '3');
INSERT INTO `t_city` VALUES ('361126', '弋阳县', '3');
INSERT INTO `t_city` VALUES ('361127', '余干县', '3');
INSERT INTO `t_city` VALUES ('361128', '鄱阳县', '3');
INSERT INTO `t_city` VALUES ('361129', '万年县', '3');
INSERT INTO `t_city` VALUES ('361130', '婺源县', '3');
INSERT INTO `t_city` VALUES ('361181', '德兴市', '3');
INSERT INTO `t_city` VALUES ('370000', '山东省', '1');
INSERT INTO `t_city` VALUES ('370100', '济南市', '2');
INSERT INTO `t_city` VALUES ('370101', '市辖区', '3');
INSERT INTO `t_city` VALUES ('370102', '历下区', '3');
INSERT INTO `t_city` VALUES ('370103', '市中区', '3');
INSERT INTO `t_city` VALUES ('370104', '槐荫区', '3');
INSERT INTO `t_city` VALUES ('370105', '天桥区', '3');
INSERT INTO `t_city` VALUES ('370112', '历城区', '3');
INSERT INTO `t_city` VALUES ('370113', '长清区', '3');
INSERT INTO `t_city` VALUES ('370124', '平阴县', '3');
INSERT INTO `t_city` VALUES ('370125', '济阳县', '3');
INSERT INTO `t_city` VALUES ('370126', '商河县', '3');
INSERT INTO `t_city` VALUES ('370181', '章丘市', '3');
INSERT INTO `t_city` VALUES ('370200', '青岛市', '2');
INSERT INTO `t_city` VALUES ('370201', '市辖区', '3');
INSERT INTO `t_city` VALUES ('370202', '市南区', '3');
INSERT INTO `t_city` VALUES ('370203', '市北区', '3');
INSERT INTO `t_city` VALUES ('370211', '黄岛区', '3');
INSERT INTO `t_city` VALUES ('370212', '崂山区', '3');
INSERT INTO `t_city` VALUES ('370213', '李沧区', '3');
INSERT INTO `t_city` VALUES ('370214', '城阳区', '3');
INSERT INTO `t_city` VALUES ('370281', '胶州市', '3');
INSERT INTO `t_city` VALUES ('370282', '即墨市', '3');
INSERT INTO `t_city` VALUES ('370283', '平度市', '3');
INSERT INTO `t_city` VALUES ('370285', '莱西市', '3');
INSERT INTO `t_city` VALUES ('370300', '淄博市', '2');
INSERT INTO `t_city` VALUES ('370301', '市辖区', '3');
INSERT INTO `t_city` VALUES ('370302', '淄川区', '3');
INSERT INTO `t_city` VALUES ('370303', '张店区', '3');
INSERT INTO `t_city` VALUES ('370304', '博山区', '3');
INSERT INTO `t_city` VALUES ('370305', '临淄区', '3');
INSERT INTO `t_city` VALUES ('370306', '周村区', '3');
INSERT INTO `t_city` VALUES ('370321', '桓台县', '3');
INSERT INTO `t_city` VALUES ('370322', '高青县', '3');
INSERT INTO `t_city` VALUES ('370323', '沂源县', '3');
INSERT INTO `t_city` VALUES ('370400', '枣庄市', '2');
INSERT INTO `t_city` VALUES ('370401', '市辖区', '3');
INSERT INTO `t_city` VALUES ('370402', '市中区', '3');
INSERT INTO `t_city` VALUES ('370403', '薛城区', '3');
INSERT INTO `t_city` VALUES ('370404', '峄城区', '3');
INSERT INTO `t_city` VALUES ('370405', '台儿庄区', '3');
INSERT INTO `t_city` VALUES ('370406', '山亭区', '3');
INSERT INTO `t_city` VALUES ('370481', '滕州市', '3');
INSERT INTO `t_city` VALUES ('370500', '东营市', '2');
INSERT INTO `t_city` VALUES ('370501', '市辖区', '3');
INSERT INTO `t_city` VALUES ('370502', '东营区', '3');
INSERT INTO `t_city` VALUES ('370503', '河口区', '3');
INSERT INTO `t_city` VALUES ('370505', '垦利区', '3');
INSERT INTO `t_city` VALUES ('370522', '利津县', '3');
INSERT INTO `t_city` VALUES ('370523', '广饶县', '3');
INSERT INTO `t_city` VALUES ('370600', '烟台市', '2');
INSERT INTO `t_city` VALUES ('370601', '市辖区', '3');
INSERT INTO `t_city` VALUES ('370602', '芝罘区', '3');
INSERT INTO `t_city` VALUES ('370611', '福山区', '3');
INSERT INTO `t_city` VALUES ('370612', '牟平区', '3');
INSERT INTO `t_city` VALUES ('370613', '莱山区', '3');
INSERT INTO `t_city` VALUES ('370634', '长岛县', '3');
INSERT INTO `t_city` VALUES ('370681', '龙口市', '3');
INSERT INTO `t_city` VALUES ('370682', '莱阳市', '3');
INSERT INTO `t_city` VALUES ('370683', '莱州市', '3');
INSERT INTO `t_city` VALUES ('370684', '蓬莱市', '3');
INSERT INTO `t_city` VALUES ('370685', '招远市', '3');
INSERT INTO `t_city` VALUES ('370686', '栖霞市', '3');
INSERT INTO `t_city` VALUES ('370687', '海阳市', '3');
INSERT INTO `t_city` VALUES ('370700', '潍坊市', '2');
INSERT INTO `t_city` VALUES ('370701', '市辖区', '3');
INSERT INTO `t_city` VALUES ('370702', '潍城区', '3');
INSERT INTO `t_city` VALUES ('370703', '寒亭区', '3');
INSERT INTO `t_city` VALUES ('370704', '坊子区', '3');
INSERT INTO `t_city` VALUES ('370705', '奎文区', '3');
INSERT INTO `t_city` VALUES ('370724', '临朐县', '3');
INSERT INTO `t_city` VALUES ('370725', '昌乐县', '3');
INSERT INTO `t_city` VALUES ('370781', '青州市', '3');
INSERT INTO `t_city` VALUES ('370782', '诸城市', '3');
INSERT INTO `t_city` VALUES ('370783', '寿光市', '3');
INSERT INTO `t_city` VALUES ('370784', '安丘市', '3');
INSERT INTO `t_city` VALUES ('370785', '高密市', '3');
INSERT INTO `t_city` VALUES ('370786', '昌邑市', '3');
INSERT INTO `t_city` VALUES ('370800', '济宁市', '2');
INSERT INTO `t_city` VALUES ('370801', '市辖区', '3');
INSERT INTO `t_city` VALUES ('370811', '任城区', '3');
INSERT INTO `t_city` VALUES ('370812', '兖州区', '3');
INSERT INTO `t_city` VALUES ('370826', '微山县', '3');
INSERT INTO `t_city` VALUES ('370827', '鱼台县', '3');
INSERT INTO `t_city` VALUES ('370828', '金乡县', '3');
INSERT INTO `t_city` VALUES ('370829', '嘉祥县', '3');
INSERT INTO `t_city` VALUES ('370830', '汶上县', '3');
INSERT INTO `t_city` VALUES ('370831', '泗水县', '3');
INSERT INTO `t_city` VALUES ('370832', '梁山县', '3');
INSERT INTO `t_city` VALUES ('370881', '曲阜市', '3');
INSERT INTO `t_city` VALUES ('370883', '邹城市', '3');
INSERT INTO `t_city` VALUES ('370900', '泰安市', '2');
INSERT INTO `t_city` VALUES ('370901', '市辖区', '3');
INSERT INTO `t_city` VALUES ('370902', '泰山区', '3');
INSERT INTO `t_city` VALUES ('370911', '岱岳区', '3');
INSERT INTO `t_city` VALUES ('370921', '宁阳县', '3');
INSERT INTO `t_city` VALUES ('370923', '东平县', '3');
INSERT INTO `t_city` VALUES ('370982', '新泰市', '3');
INSERT INTO `t_city` VALUES ('370983', '肥城市', '3');
INSERT INTO `t_city` VALUES ('371000', '威海市', '2');
INSERT INTO `t_city` VALUES ('371001', '市辖区', '3');
INSERT INTO `t_city` VALUES ('371002', '环翠区', '3');
INSERT INTO `t_city` VALUES ('371003', '文登区', '3');
INSERT INTO `t_city` VALUES ('371082', '荣成市', '3');
INSERT INTO `t_city` VALUES ('371083', '乳山市', '3');
INSERT INTO `t_city` VALUES ('371100', '日照市', '2');
INSERT INTO `t_city` VALUES ('371101', '市辖区', '3');
INSERT INTO `t_city` VALUES ('371102', '东港区', '3');
INSERT INTO `t_city` VALUES ('371103', '岚山区', '3');
INSERT INTO `t_city` VALUES ('371121', '五莲县', '3');
INSERT INTO `t_city` VALUES ('371122', '莒县', '3');
INSERT INTO `t_city` VALUES ('371200', '莱芜市', '2');
INSERT INTO `t_city` VALUES ('371201', '市辖区', '3');
INSERT INTO `t_city` VALUES ('371202', '莱城区', '3');
INSERT INTO `t_city` VALUES ('371203', '钢城区', '3');
INSERT INTO `t_city` VALUES ('371300', '临沂市', '2');
INSERT INTO `t_city` VALUES ('371301', '市辖区', '3');
INSERT INTO `t_city` VALUES ('371302', '兰山区', '3');
INSERT INTO `t_city` VALUES ('371311', '罗庄区', '3');
INSERT INTO `t_city` VALUES ('371312', '河东区', '3');
INSERT INTO `t_city` VALUES ('371321', '沂南县', '3');
INSERT INTO `t_city` VALUES ('371322', '郯城县', '3');
INSERT INTO `t_city` VALUES ('371323', '沂水县', '3');
INSERT INTO `t_city` VALUES ('371324', '兰陵县', '3');
INSERT INTO `t_city` VALUES ('371325', '费县', '3');
INSERT INTO `t_city` VALUES ('371326', '平邑县', '3');
INSERT INTO `t_city` VALUES ('371327', '莒南县', '3');
INSERT INTO `t_city` VALUES ('371328', '蒙阴县', '3');
INSERT INTO `t_city` VALUES ('371329', '临沭县', '3');
INSERT INTO `t_city` VALUES ('371400', '德州市', '2');
INSERT INTO `t_city` VALUES ('371401', '市辖区', '3');
INSERT INTO `t_city` VALUES ('371402', '德城区', '3');
INSERT INTO `t_city` VALUES ('371403', '陵城区', '3');
INSERT INTO `t_city` VALUES ('371422', '宁津县', '3');
INSERT INTO `t_city` VALUES ('371423', '庆云县', '3');
INSERT INTO `t_city` VALUES ('371424', '临邑县', '3');
INSERT INTO `t_city` VALUES ('371425', '齐河县', '3');
INSERT INTO `t_city` VALUES ('371426', '平原县', '3');
INSERT INTO `t_city` VALUES ('371427', '夏津县', '3');
INSERT INTO `t_city` VALUES ('371428', '武城县', '3');
INSERT INTO `t_city` VALUES ('371481', '乐陵市', '3');
INSERT INTO `t_city` VALUES ('371482', '禹城市', '3');
INSERT INTO `t_city` VALUES ('371500', '聊城市', '2');
INSERT INTO `t_city` VALUES ('371501', '市辖区', '3');
INSERT INTO `t_city` VALUES ('371502', '东昌府区', '3');
INSERT INTO `t_city` VALUES ('371521', '阳谷县', '3');
INSERT INTO `t_city` VALUES ('371522', '莘县', '3');
INSERT INTO `t_city` VALUES ('371523', '茌平县', '3');
INSERT INTO `t_city` VALUES ('371524', '东阿县', '3');
INSERT INTO `t_city` VALUES ('371525', '冠县', '3');
INSERT INTO `t_city` VALUES ('371526', '高唐县', '3');
INSERT INTO `t_city` VALUES ('371581', '临清市', '3');
INSERT INTO `t_city` VALUES ('371600', '滨州市', '2');
INSERT INTO `t_city` VALUES ('371601', '市辖区', '3');
INSERT INTO `t_city` VALUES ('371602', '滨城区', '3');
INSERT INTO `t_city` VALUES ('371603', '沾化区', '3');
INSERT INTO `t_city` VALUES ('371621', '惠民县', '3');
INSERT INTO `t_city` VALUES ('371622', '阳信县', '3');
INSERT INTO `t_city` VALUES ('371623', '无棣县', '3');
INSERT INTO `t_city` VALUES ('371625', '博兴县', '3');
INSERT INTO `t_city` VALUES ('371626', '邹平县', '3');
INSERT INTO `t_city` VALUES ('371700', '菏泽市', '2');
INSERT INTO `t_city` VALUES ('371701', '市辖区', '3');
INSERT INTO `t_city` VALUES ('371702', '牡丹区', '3');
INSERT INTO `t_city` VALUES ('371703', '定陶区', '3');
INSERT INTO `t_city` VALUES ('371721', '曹县', '3');
INSERT INTO `t_city` VALUES ('371722', '单县', '3');
INSERT INTO `t_city` VALUES ('371723', '成武县', '3');
INSERT INTO `t_city` VALUES ('371724', '巨野县', '3');
INSERT INTO `t_city` VALUES ('371725', '郓城县', '3');
INSERT INTO `t_city` VALUES ('371726', '鄄城县', '3');
INSERT INTO `t_city` VALUES ('371728', '东明县', '3');
INSERT INTO `t_city` VALUES ('410000', '河南省', '1');
INSERT INTO `t_city` VALUES ('410100', '郑州市', '2');
INSERT INTO `t_city` VALUES ('410101', '市辖区', '3');
INSERT INTO `t_city` VALUES ('410102', '中原区', '3');
INSERT INTO `t_city` VALUES ('410103', '二七区', '3');
INSERT INTO `t_city` VALUES ('410104', '管城回族区', '3');
INSERT INTO `t_city` VALUES ('410105', '金水区', '3');
INSERT INTO `t_city` VALUES ('410106', '上街区', '3');
INSERT INTO `t_city` VALUES ('410108', '惠济区', '3');
INSERT INTO `t_city` VALUES ('410122', '中牟县', '3');
INSERT INTO `t_city` VALUES ('410181', '巩义市', '3');
INSERT INTO `t_city` VALUES ('410182', '荥阳市', '3');
INSERT INTO `t_city` VALUES ('410183', '新密市', '3');
INSERT INTO `t_city` VALUES ('410184', '新郑市', '3');
INSERT INTO `t_city` VALUES ('410185', '登封市', '3');
INSERT INTO `t_city` VALUES ('410200', '开封市', '2');
INSERT INTO `t_city` VALUES ('410201', '市辖区', '3');
INSERT INTO `t_city` VALUES ('410202', '龙亭区', '3');
INSERT INTO `t_city` VALUES ('410203', '顺河回族区', '3');
INSERT INTO `t_city` VALUES ('410204', '鼓楼区', '3');
INSERT INTO `t_city` VALUES ('410205', '禹王台区', '3');
INSERT INTO `t_city` VALUES ('410211', '金明区', '3');
INSERT INTO `t_city` VALUES ('410212', '祥符区', '3');
INSERT INTO `t_city` VALUES ('410221', '杞县', '3');
INSERT INTO `t_city` VALUES ('410222', '通许县', '3');
INSERT INTO `t_city` VALUES ('410223', '尉氏县', '3');
INSERT INTO `t_city` VALUES ('410225', '兰考县', '3');
INSERT INTO `t_city` VALUES ('410300', '洛阳市', '2');
INSERT INTO `t_city` VALUES ('410301', '市辖区', '3');
INSERT INTO `t_city` VALUES ('410302', '老城区', '3');
INSERT INTO `t_city` VALUES ('410303', '西工区', '3');
INSERT INTO `t_city` VALUES ('410304', '瀍河回族区', '3');
INSERT INTO `t_city` VALUES ('410305', '涧西区', '3');
INSERT INTO `t_city` VALUES ('410306', '吉利区', '3');
INSERT INTO `t_city` VALUES ('410311', '洛龙区', '3');
INSERT INTO `t_city` VALUES ('410322', '孟津县', '3');
INSERT INTO `t_city` VALUES ('410323', '新安县', '3');
INSERT INTO `t_city` VALUES ('410324', '栾川县', '3');
INSERT INTO `t_city` VALUES ('410325', '嵩县', '3');
INSERT INTO `t_city` VALUES ('410326', '汝阳县', '3');
INSERT INTO `t_city` VALUES ('410327', '宜阳县', '3');
INSERT INTO `t_city` VALUES ('410328', '洛宁县', '3');
INSERT INTO `t_city` VALUES ('410329', '伊川县', '3');
INSERT INTO `t_city` VALUES ('410381', '偃师市', '3');
INSERT INTO `t_city` VALUES ('410400', '平顶山市', '2');
INSERT INTO `t_city` VALUES ('410401', '市辖区', '3');
INSERT INTO `t_city` VALUES ('410402', '新华区', '3');
INSERT INTO `t_city` VALUES ('410403', '卫东区', '3');
INSERT INTO `t_city` VALUES ('410404', '石龙区', '3');
INSERT INTO `t_city` VALUES ('410411', '湛河区', '3');
INSERT INTO `t_city` VALUES ('410421', '宝丰县', '3');
INSERT INTO `t_city` VALUES ('410422', '叶县', '3');
INSERT INTO `t_city` VALUES ('410423', '鲁山县', '3');
INSERT INTO `t_city` VALUES ('410425', '郏县', '3');
INSERT INTO `t_city` VALUES ('410481', '舞钢市', '3');
INSERT INTO `t_city` VALUES ('410482', '汝州市', '3');
INSERT INTO `t_city` VALUES ('410500', '安阳市', '2');
INSERT INTO `t_city` VALUES ('410501', '市辖区', '3');
INSERT INTO `t_city` VALUES ('410502', '文峰区', '3');
INSERT INTO `t_city` VALUES ('410503', '北关区', '3');
INSERT INTO `t_city` VALUES ('410505', '殷都区', '3');
INSERT INTO `t_city` VALUES ('410506', '龙安区', '3');
INSERT INTO `t_city` VALUES ('410522', '安阳县', '3');
INSERT INTO `t_city` VALUES ('410523', '汤阴县', '3');
INSERT INTO `t_city` VALUES ('410526', '滑县', '3');
INSERT INTO `t_city` VALUES ('410527', '内黄县', '3');
INSERT INTO `t_city` VALUES ('410581', '林州市', '3');
INSERT INTO `t_city` VALUES ('410600', '鹤壁市', '2');
INSERT INTO `t_city` VALUES ('410601', '市辖区', '3');
INSERT INTO `t_city` VALUES ('410602', '鹤山区', '3');
INSERT INTO `t_city` VALUES ('410603', '山城区', '3');
INSERT INTO `t_city` VALUES ('410611', '淇滨区', '3');
INSERT INTO `t_city` VALUES ('410621', '浚县', '3');
INSERT INTO `t_city` VALUES ('410622', '淇县', '3');
INSERT INTO `t_city` VALUES ('410700', '新乡市', '2');
INSERT INTO `t_city` VALUES ('410701', '市辖区', '3');
INSERT INTO `t_city` VALUES ('410702', '红旗区', '3');
INSERT INTO `t_city` VALUES ('410703', '卫滨区', '3');
INSERT INTO `t_city` VALUES ('410704', '凤泉区', '3');
INSERT INTO `t_city` VALUES ('410711', '牧野区', '3');
INSERT INTO `t_city` VALUES ('410721', '新乡县', '3');
INSERT INTO `t_city` VALUES ('410724', '获嘉县', '3');
INSERT INTO `t_city` VALUES ('410725', '原阳县', '3');
INSERT INTO `t_city` VALUES ('410726', '延津县', '3');
INSERT INTO `t_city` VALUES ('410727', '封丘县', '3');
INSERT INTO `t_city` VALUES ('410728', '长垣县', '3');
INSERT INTO `t_city` VALUES ('410781', '卫辉市', '3');
INSERT INTO `t_city` VALUES ('410782', '辉县市', '3');
INSERT INTO `t_city` VALUES ('410800', '焦作市', '2');
INSERT INTO `t_city` VALUES ('410801', '市辖区', '3');
INSERT INTO `t_city` VALUES ('410802', '解放区', '3');
INSERT INTO `t_city` VALUES ('410803', '中站区', '3');
INSERT INTO `t_city` VALUES ('410804', '马村区', '3');
INSERT INTO `t_city` VALUES ('410811', '山阳区', '3');
INSERT INTO `t_city` VALUES ('410821', '修武县', '3');
INSERT INTO `t_city` VALUES ('410822', '博爱县', '3');
INSERT INTO `t_city` VALUES ('410823', '武陟县', '3');
INSERT INTO `t_city` VALUES ('410825', '温县', '3');
INSERT INTO `t_city` VALUES ('410882', '沁阳市', '3');
INSERT INTO `t_city` VALUES ('410883', '孟州市', '3');
INSERT INTO `t_city` VALUES ('410900', '濮阳市', '2');
INSERT INTO `t_city` VALUES ('410901', '市辖区', '3');
INSERT INTO `t_city` VALUES ('410902', '华龙区', '3');
INSERT INTO `t_city` VALUES ('410922', '清丰县', '3');
INSERT INTO `t_city` VALUES ('410923', '南乐县', '3');
INSERT INTO `t_city` VALUES ('410926', '范县', '3');
INSERT INTO `t_city` VALUES ('410927', '台前县', '3');
INSERT INTO `t_city` VALUES ('410928', '濮阳县', '3');
INSERT INTO `t_city` VALUES ('411000', '许昌市', '2');
INSERT INTO `t_city` VALUES ('411001', '市辖区', '3');
INSERT INTO `t_city` VALUES ('411002', '魏都区', '3');
INSERT INTO `t_city` VALUES ('411023', '许昌县', '3');
INSERT INTO `t_city` VALUES ('411024', '鄢陵县', '3');
INSERT INTO `t_city` VALUES ('411025', '襄城县', '3');
INSERT INTO `t_city` VALUES ('411081', '禹州市', '3');
INSERT INTO `t_city` VALUES ('411082', '长葛市', '3');
INSERT INTO `t_city` VALUES ('411100', '漯河市', '2');
INSERT INTO `t_city` VALUES ('411101', '市辖区', '3');
INSERT INTO `t_city` VALUES ('411102', '源汇区', '3');
INSERT INTO `t_city` VALUES ('411103', '郾城区', '3');
INSERT INTO `t_city` VALUES ('411104', '召陵区', '3');
INSERT INTO `t_city` VALUES ('411121', '舞阳县', '3');
INSERT INTO `t_city` VALUES ('411122', '临颍县', '3');
INSERT INTO `t_city` VALUES ('411200', '三门峡市', '2');
INSERT INTO `t_city` VALUES ('411201', '市辖区', '3');
INSERT INTO `t_city` VALUES ('411202', '湖滨区', '3');
INSERT INTO `t_city` VALUES ('411203', '陕州区', '3');
INSERT INTO `t_city` VALUES ('411221', '渑池县', '3');
INSERT INTO `t_city` VALUES ('411224', '卢氏县', '3');
INSERT INTO `t_city` VALUES ('411281', '义马市', '3');
INSERT INTO `t_city` VALUES ('411282', '灵宝市', '3');
INSERT INTO `t_city` VALUES ('411300', '南阳市', '2');
INSERT INTO `t_city` VALUES ('411301', '市辖区', '3');
INSERT INTO `t_city` VALUES ('411302', '宛城区', '3');
INSERT INTO `t_city` VALUES ('411303', '卧龙区', '3');
INSERT INTO `t_city` VALUES ('411321', '南召县', '3');
INSERT INTO `t_city` VALUES ('411322', '方城县', '3');
INSERT INTO `t_city` VALUES ('411323', '西峡县', '3');
INSERT INTO `t_city` VALUES ('411324', '镇平县', '3');
INSERT INTO `t_city` VALUES ('411325', '内乡县', '3');
INSERT INTO `t_city` VALUES ('411326', '淅川县', '3');
INSERT INTO `t_city` VALUES ('411327', '社旗县', '3');
INSERT INTO `t_city` VALUES ('411328', '唐河县', '3');
INSERT INTO `t_city` VALUES ('411329', '新野县', '3');
INSERT INTO `t_city` VALUES ('411330', '桐柏县', '3');
INSERT INTO `t_city` VALUES ('411381', '邓州市', '3');
INSERT INTO `t_city` VALUES ('411400', '商丘市', '2');
INSERT INTO `t_city` VALUES ('411401', '市辖区', '3');
INSERT INTO `t_city` VALUES ('411402', '梁园区', '3');
INSERT INTO `t_city` VALUES ('411403', '睢阳区', '3');
INSERT INTO `t_city` VALUES ('411421', '民权县', '3');
INSERT INTO `t_city` VALUES ('411422', '睢县', '3');
INSERT INTO `t_city` VALUES ('411423', '宁陵县', '3');
INSERT INTO `t_city` VALUES ('411424', '柘城县', '3');
INSERT INTO `t_city` VALUES ('411425', '虞城县', '3');
INSERT INTO `t_city` VALUES ('411426', '夏邑县', '3');
INSERT INTO `t_city` VALUES ('411481', '永城市', '3');
INSERT INTO `t_city` VALUES ('411500', '信阳市', '2');
INSERT INTO `t_city` VALUES ('411501', '市辖区', '3');
INSERT INTO `t_city` VALUES ('411502', '浉河区', '3');
INSERT INTO `t_city` VALUES ('411503', '平桥区', '3');
INSERT INTO `t_city` VALUES ('411521', '罗山县', '3');
INSERT INTO `t_city` VALUES ('411522', '光山县', '3');
INSERT INTO `t_city` VALUES ('411523', '新县', '3');
INSERT INTO `t_city` VALUES ('411524', '商城县', '3');
INSERT INTO `t_city` VALUES ('411525', '固始县', '3');
INSERT INTO `t_city` VALUES ('411526', '潢川县', '3');
INSERT INTO `t_city` VALUES ('411527', '淮滨县', '3');
INSERT INTO `t_city` VALUES ('411528', '息县', '3');
INSERT INTO `t_city` VALUES ('411600', '周口市', '2');
INSERT INTO `t_city` VALUES ('411601', '市辖区', '3');
INSERT INTO `t_city` VALUES ('411602', '川汇区', '3');
INSERT INTO `t_city` VALUES ('411621', '扶沟县', '3');
INSERT INTO `t_city` VALUES ('411622', '西华县', '3');
INSERT INTO `t_city` VALUES ('411623', '商水县', '3');
INSERT INTO `t_city` VALUES ('411624', '沈丘县', '3');
INSERT INTO `t_city` VALUES ('411625', '郸城县', '3');
INSERT INTO `t_city` VALUES ('411626', '淮阳县', '3');
INSERT INTO `t_city` VALUES ('411627', '太康县', '3');
INSERT INTO `t_city` VALUES ('411628', '鹿邑县', '3');
INSERT INTO `t_city` VALUES ('411681', '项城市', '3');
INSERT INTO `t_city` VALUES ('411700', '驻马店市', '2');
INSERT INTO `t_city` VALUES ('411701', '市辖区', '3');
INSERT INTO `t_city` VALUES ('411702', '驿城区', '3');
INSERT INTO `t_city` VALUES ('411721', '西平县', '3');
INSERT INTO `t_city` VALUES ('411722', '上蔡县', '3');
INSERT INTO `t_city` VALUES ('411723', '平舆县', '3');
INSERT INTO `t_city` VALUES ('411724', '正阳县', '3');
INSERT INTO `t_city` VALUES ('411725', '确山县', '3');
INSERT INTO `t_city` VALUES ('411726', '泌阳县', '3');
INSERT INTO `t_city` VALUES ('411727', '汝南县', '3');
INSERT INTO `t_city` VALUES ('411728', '遂平县', '3');
INSERT INTO `t_city` VALUES ('411729', '新蔡县', '3');
INSERT INTO `t_city` VALUES ('419000', '省直辖县级行政区划', '2');
INSERT INTO `t_city` VALUES ('419001', '济源市', '3');
INSERT INTO `t_city` VALUES ('420000', '湖北省', '1');
INSERT INTO `t_city` VALUES ('420100', '武汉市', '2');
INSERT INTO `t_city` VALUES ('420101', '市辖区', '3');
INSERT INTO `t_city` VALUES ('420102', '江岸区', '3');
INSERT INTO `t_city` VALUES ('420103', '江汉区', '3');
INSERT INTO `t_city` VALUES ('420104', '硚口区', '3');
INSERT INTO `t_city` VALUES ('420105', '汉阳区', '3');
INSERT INTO `t_city` VALUES ('420106', '武昌区', '3');
INSERT INTO `t_city` VALUES ('420107', '青山区', '3');
INSERT INTO `t_city` VALUES ('420111', '洪山区', '3');
INSERT INTO `t_city` VALUES ('420112', '东西湖区', '3');
INSERT INTO `t_city` VALUES ('420113', '汉南区', '3');
INSERT INTO `t_city` VALUES ('420114', '蔡甸区', '3');
INSERT INTO `t_city` VALUES ('420115', '江夏区', '3');
INSERT INTO `t_city` VALUES ('420116', '黄陂区', '3');
INSERT INTO `t_city` VALUES ('420117', '新洲区', '3');
INSERT INTO `t_city` VALUES ('420200', '黄石市', '2');
INSERT INTO `t_city` VALUES ('420201', '市辖区', '3');
INSERT INTO `t_city` VALUES ('420202', '黄石港区', '3');
INSERT INTO `t_city` VALUES ('420203', '西塞山区', '3');
INSERT INTO `t_city` VALUES ('420204', '下陆区', '3');
INSERT INTO `t_city` VALUES ('420205', '铁山区', '3');
INSERT INTO `t_city` VALUES ('420222', '阳新县', '3');
INSERT INTO `t_city` VALUES ('420281', '大冶市', '3');
INSERT INTO `t_city` VALUES ('420300', '十堰市', '2');
INSERT INTO `t_city` VALUES ('420301', '市辖区', '3');
INSERT INTO `t_city` VALUES ('420302', '茅箭区', '3');
INSERT INTO `t_city` VALUES ('420303', '张湾区', '3');
INSERT INTO `t_city` VALUES ('420304', '郧阳区', '3');
INSERT INTO `t_city` VALUES ('420322', '郧西县', '3');
INSERT INTO `t_city` VALUES ('420323', '竹山县', '3');
INSERT INTO `t_city` VALUES ('420324', '竹溪县', '3');
INSERT INTO `t_city` VALUES ('420325', '房县', '3');
INSERT INTO `t_city` VALUES ('420381', '丹江口市', '3');
INSERT INTO `t_city` VALUES ('420500', '宜昌市', '2');
INSERT INTO `t_city` VALUES ('420501', '市辖区', '3');
INSERT INTO `t_city` VALUES ('420502', '西陵区', '3');
INSERT INTO `t_city` VALUES ('420503', '伍家岗区', '3');
INSERT INTO `t_city` VALUES ('420504', '点军区', '3');
INSERT INTO `t_city` VALUES ('420505', '猇亭区', '3');
INSERT INTO `t_city` VALUES ('420506', '夷陵区', '3');
INSERT INTO `t_city` VALUES ('420525', '远安县', '3');
INSERT INTO `t_city` VALUES ('420526', '兴山县', '3');
INSERT INTO `t_city` VALUES ('420527', '秭归县', '3');
INSERT INTO `t_city` VALUES ('420528', '长阳土家族自治县', '3');
INSERT INTO `t_city` VALUES ('420529', '五峰土家族自治县', '3');
INSERT INTO `t_city` VALUES ('420581', '宜都市', '3');
INSERT INTO `t_city` VALUES ('420582', '当阳市', '3');
INSERT INTO `t_city` VALUES ('420583', '枝江市', '3');
INSERT INTO `t_city` VALUES ('420600', '襄阳市', '2');
INSERT INTO `t_city` VALUES ('420601', '市辖区', '3');
INSERT INTO `t_city` VALUES ('420602', '襄城区', '3');
INSERT INTO `t_city` VALUES ('420606', '樊城区', '3');
INSERT INTO `t_city` VALUES ('420607', '襄州区', '3');
INSERT INTO `t_city` VALUES ('420624', '南漳县', '3');
INSERT INTO `t_city` VALUES ('420625', '谷城县', '3');
INSERT INTO `t_city` VALUES ('420626', '保康县', '3');
INSERT INTO `t_city` VALUES ('420682', '老河口市', '3');
INSERT INTO `t_city` VALUES ('420683', '枣阳市', '3');
INSERT INTO `t_city` VALUES ('420684', '宜城市', '3');
INSERT INTO `t_city` VALUES ('420700', '鄂州市', '2');
INSERT INTO `t_city` VALUES ('420701', '市辖区', '3');
INSERT INTO `t_city` VALUES ('420702', '梁子湖区', '3');
INSERT INTO `t_city` VALUES ('420703', '华容区', '3');
INSERT INTO `t_city` VALUES ('420704', '鄂城区', '3');
INSERT INTO `t_city` VALUES ('420800', '荆门市', '2');
INSERT INTO `t_city` VALUES ('420801', '市辖区', '3');
INSERT INTO `t_city` VALUES ('420802', '东宝区', '3');
INSERT INTO `t_city` VALUES ('420804', '掇刀区', '3');
INSERT INTO `t_city` VALUES ('420821', '京山县', '3');
INSERT INTO `t_city` VALUES ('420822', '沙洋县', '3');
INSERT INTO `t_city` VALUES ('420881', '钟祥市', '3');
INSERT INTO `t_city` VALUES ('420900', '孝感市', '2');
INSERT INTO `t_city` VALUES ('420901', '市辖区', '3');
INSERT INTO `t_city` VALUES ('420902', '孝南区', '3');
INSERT INTO `t_city` VALUES ('420921', '孝昌县', '3');
INSERT INTO `t_city` VALUES ('420922', '大悟县', '3');
INSERT INTO `t_city` VALUES ('420923', '云梦县', '3');
INSERT INTO `t_city` VALUES ('420981', '应城市', '3');
INSERT INTO `t_city` VALUES ('420982', '安陆市', '3');
INSERT INTO `t_city` VALUES ('420984', '汉川市', '3');
INSERT INTO `t_city` VALUES ('421000', '荆州市', '2');
INSERT INTO `t_city` VALUES ('421001', '市辖区', '3');
INSERT INTO `t_city` VALUES ('421002', '沙市区', '3');
INSERT INTO `t_city` VALUES ('421003', '荆州区', '3');
INSERT INTO `t_city` VALUES ('421022', '公安县', '3');
INSERT INTO `t_city` VALUES ('421023', '监利县', '3');
INSERT INTO `t_city` VALUES ('421024', '江陵县', '3');
INSERT INTO `t_city` VALUES ('421081', '石首市', '3');
INSERT INTO `t_city` VALUES ('421083', '洪湖市', '3');
INSERT INTO `t_city` VALUES ('421087', '松滋市', '3');
INSERT INTO `t_city` VALUES ('421100', '黄冈市', '2');
INSERT INTO `t_city` VALUES ('421101', '市辖区', '3');
INSERT INTO `t_city` VALUES ('421102', '黄州区', '3');
INSERT INTO `t_city` VALUES ('421121', '团风县', '3');
INSERT INTO `t_city` VALUES ('421122', '红安县', '3');
INSERT INTO `t_city` VALUES ('421123', '罗田县', '3');
INSERT INTO `t_city` VALUES ('421124', '英山县', '3');
INSERT INTO `t_city` VALUES ('421125', '浠水县', '3');
INSERT INTO `t_city` VALUES ('421126', '蕲春县', '3');
INSERT INTO `t_city` VALUES ('421127', '黄梅县', '3');
INSERT INTO `t_city` VALUES ('421181', '麻城市', '3');
INSERT INTO `t_city` VALUES ('421182', '武穴市', '3');
INSERT INTO `t_city` VALUES ('421200', '咸宁市', '2');
INSERT INTO `t_city` VALUES ('421201', '市辖区', '3');
INSERT INTO `t_city` VALUES ('421202', '咸安区', '3');
INSERT INTO `t_city` VALUES ('421221', '嘉鱼县', '3');
INSERT INTO `t_city` VALUES ('421222', '通城县', '3');
INSERT INTO `t_city` VALUES ('421223', '崇阳县', '3');
INSERT INTO `t_city` VALUES ('421224', '通山县', '3');
INSERT INTO `t_city` VALUES ('421281', '赤壁市', '3');
INSERT INTO `t_city` VALUES ('421300', '随州市', '2');
INSERT INTO `t_city` VALUES ('421301', '市辖区', '3');
INSERT INTO `t_city` VALUES ('421303', '曾都区', '3');
INSERT INTO `t_city` VALUES ('421321', '随县', '3');
INSERT INTO `t_city` VALUES ('421381', '广水市', '3');
INSERT INTO `t_city` VALUES ('422800', '恩施土家族苗族自治州', '2');
INSERT INTO `t_city` VALUES ('422801', '恩施市', '3');
INSERT INTO `t_city` VALUES ('422802', '利川市', '3');
INSERT INTO `t_city` VALUES ('422822', '建始县', '3');
INSERT INTO `t_city` VALUES ('422823', '巴东县', '3');
INSERT INTO `t_city` VALUES ('422825', '宣恩县', '3');
INSERT INTO `t_city` VALUES ('422826', '咸丰县', '3');
INSERT INTO `t_city` VALUES ('422827', '来凤县', '3');
INSERT INTO `t_city` VALUES ('422828', '鹤峰县', '3');
INSERT INTO `t_city` VALUES ('429000', '省直辖县级行政区划', '2');
INSERT INTO `t_city` VALUES ('429004', '仙桃市', '3');
INSERT INTO `t_city` VALUES ('429005', '潜江市', '3');
INSERT INTO `t_city` VALUES ('429006', '天门市', '3');
INSERT INTO `t_city` VALUES ('429021', '神农架林区', '3');
INSERT INTO `t_city` VALUES ('430000', '湖南省', '1');
INSERT INTO `t_city` VALUES ('430100', '长沙市', '2');
INSERT INTO `t_city` VALUES ('430101', '市辖区', '3');
INSERT INTO `t_city` VALUES ('430102', '芙蓉区', '3');
INSERT INTO `t_city` VALUES ('430103', '天心区', '3');
INSERT INTO `t_city` VALUES ('430104', '岳麓区', '3');
INSERT INTO `t_city` VALUES ('430105', '开福区', '3');
INSERT INTO `t_city` VALUES ('430111', '雨花区', '3');
INSERT INTO `t_city` VALUES ('430112', '望城区', '3');
INSERT INTO `t_city` VALUES ('430121', '长沙县', '3');
INSERT INTO `t_city` VALUES ('430124', '宁乡县', '3');
INSERT INTO `t_city` VALUES ('430181', '浏阳市', '3');
INSERT INTO `t_city` VALUES ('430200', '株洲市', '2');
INSERT INTO `t_city` VALUES ('430201', '市辖区', '3');
INSERT INTO `t_city` VALUES ('430202', '荷塘区', '3');
INSERT INTO `t_city` VALUES ('430203', '芦淞区', '3');
INSERT INTO `t_city` VALUES ('430204', '石峰区', '3');
INSERT INTO `t_city` VALUES ('430211', '天元区', '3');
INSERT INTO `t_city` VALUES ('430221', '株洲县', '3');
INSERT INTO `t_city` VALUES ('430223', '攸县', '3');
INSERT INTO `t_city` VALUES ('430224', '茶陵县', '3');
INSERT INTO `t_city` VALUES ('430225', '炎陵县', '3');
INSERT INTO `t_city` VALUES ('430281', '醴陵市', '3');
INSERT INTO `t_city` VALUES ('430300', '湘潭市', '2');
INSERT INTO `t_city` VALUES ('430301', '市辖区', '3');
INSERT INTO `t_city` VALUES ('430302', '雨湖区', '3');
INSERT INTO `t_city` VALUES ('430304', '岳塘区', '3');
INSERT INTO `t_city` VALUES ('430321', '湘潭县', '3');
INSERT INTO `t_city` VALUES ('430381', '湘乡市', '3');
INSERT INTO `t_city` VALUES ('430382', '韶山市', '3');
INSERT INTO `t_city` VALUES ('430400', '衡阳市', '2');
INSERT INTO `t_city` VALUES ('430401', '市辖区', '3');
INSERT INTO `t_city` VALUES ('430405', '珠晖区', '3');
INSERT INTO `t_city` VALUES ('430406', '雁峰区', '3');
INSERT INTO `t_city` VALUES ('430407', '石鼓区', '3');
INSERT INTO `t_city` VALUES ('430408', '蒸湘区', '3');
INSERT INTO `t_city` VALUES ('430412', '南岳区', '3');
INSERT INTO `t_city` VALUES ('430421', '衡阳县', '3');
INSERT INTO `t_city` VALUES ('430422', '衡南县', '3');
INSERT INTO `t_city` VALUES ('430423', '衡山县', '3');
INSERT INTO `t_city` VALUES ('430424', '衡东县', '3');
INSERT INTO `t_city` VALUES ('430426', '祁东县', '3');
INSERT INTO `t_city` VALUES ('430481', '耒阳市', '3');
INSERT INTO `t_city` VALUES ('430482', '常宁市', '3');
INSERT INTO `t_city` VALUES ('430500', '邵阳市', '2');
INSERT INTO `t_city` VALUES ('430501', '市辖区', '3');
INSERT INTO `t_city` VALUES ('430502', '双清区', '3');
INSERT INTO `t_city` VALUES ('430503', '大祥区', '3');
INSERT INTO `t_city` VALUES ('430511', '北塔区', '3');
INSERT INTO `t_city` VALUES ('430521', '邵东县', '3');
INSERT INTO `t_city` VALUES ('430522', '新邵县', '3');
INSERT INTO `t_city` VALUES ('430523', '邵阳县', '3');
INSERT INTO `t_city` VALUES ('430524', '隆回县', '3');
INSERT INTO `t_city` VALUES ('430525', '洞口县', '3');
INSERT INTO `t_city` VALUES ('430527', '绥宁县', '3');
INSERT INTO `t_city` VALUES ('430528', '新宁县', '3');
INSERT INTO `t_city` VALUES ('430529', '城步苗族自治县', '3');
INSERT INTO `t_city` VALUES ('430581', '武冈市', '3');
INSERT INTO `t_city` VALUES ('430600', '岳阳市', '2');
INSERT INTO `t_city` VALUES ('430601', '市辖区', '3');
INSERT INTO `t_city` VALUES ('430602', '岳阳楼区', '3');
INSERT INTO `t_city` VALUES ('430603', '云溪区', '3');
INSERT INTO `t_city` VALUES ('430611', '君山区', '3');
INSERT INTO `t_city` VALUES ('430621', '岳阳县', '3');
INSERT INTO `t_city` VALUES ('430623', '华容县', '3');
INSERT INTO `t_city` VALUES ('430624', '湘阴县', '3');
INSERT INTO `t_city` VALUES ('430626', '平江县', '3');
INSERT INTO `t_city` VALUES ('430681', '汨罗市', '3');
INSERT INTO `t_city` VALUES ('430682', '临湘市', '3');
INSERT INTO `t_city` VALUES ('430700', '常德市', '2');
INSERT INTO `t_city` VALUES ('430701', '市辖区', '3');
INSERT INTO `t_city` VALUES ('430702', '武陵区', '3');
INSERT INTO `t_city` VALUES ('430703', '鼎城区', '3');
INSERT INTO `t_city` VALUES ('430721', '安乡县', '3');
INSERT INTO `t_city` VALUES ('430722', '汉寿县', '3');
INSERT INTO `t_city` VALUES ('430723', '澧县', '3');
INSERT INTO `t_city` VALUES ('430724', '临澧县', '3');
INSERT INTO `t_city` VALUES ('430725', '桃源县', '3');
INSERT INTO `t_city` VALUES ('430726', '石门县', '3');
INSERT INTO `t_city` VALUES ('430781', '津市市', '3');
INSERT INTO `t_city` VALUES ('430800', '张家界市', '2');
INSERT INTO `t_city` VALUES ('430801', '市辖区', '3');
INSERT INTO `t_city` VALUES ('430802', '永定区', '3');
INSERT INTO `t_city` VALUES ('430811', '武陵源区', '3');
INSERT INTO `t_city` VALUES ('430821', '慈利县', '3');
INSERT INTO `t_city` VALUES ('430822', '桑植县', '3');
INSERT INTO `t_city` VALUES ('430900', '益阳市', '2');
INSERT INTO `t_city` VALUES ('430901', '市辖区', '3');
INSERT INTO `t_city` VALUES ('430902', '资阳区', '3');
INSERT INTO `t_city` VALUES ('430903', '赫山区', '3');
INSERT INTO `t_city` VALUES ('430921', '南县', '3');
INSERT INTO `t_city` VALUES ('430922', '桃江县', '3');
INSERT INTO `t_city` VALUES ('430923', '安化县', '3');
INSERT INTO `t_city` VALUES ('430981', '沅江市', '3');
INSERT INTO `t_city` VALUES ('431000', '郴州市', '2');
INSERT INTO `t_city` VALUES ('431001', '市辖区', '3');
INSERT INTO `t_city` VALUES ('431002', '北湖区', '3');
INSERT INTO `t_city` VALUES ('431003', '苏仙区', '3');
INSERT INTO `t_city` VALUES ('431021', '桂阳县', '3');
INSERT INTO `t_city` VALUES ('431022', '宜章县', '3');
INSERT INTO `t_city` VALUES ('431023', '永兴县', '3');
INSERT INTO `t_city` VALUES ('431024', '嘉禾县', '3');
INSERT INTO `t_city` VALUES ('431025', '临武县', '3');
INSERT INTO `t_city` VALUES ('431026', '汝城县', '3');
INSERT INTO `t_city` VALUES ('431027', '桂东县', '3');
INSERT INTO `t_city` VALUES ('431028', '安仁县', '3');
INSERT INTO `t_city` VALUES ('431081', '资兴市', '3');
INSERT INTO `t_city` VALUES ('431100', '永州市', '2');
INSERT INTO `t_city` VALUES ('431101', '市辖区', '3');
INSERT INTO `t_city` VALUES ('431102', '零陵区', '3');
INSERT INTO `t_city` VALUES ('431103', '冷水滩区', '3');
INSERT INTO `t_city` VALUES ('431121', '祁阳县', '3');
INSERT INTO `t_city` VALUES ('431122', '东安县', '3');
INSERT INTO `t_city` VALUES ('431123', '双牌县', '3');
INSERT INTO `t_city` VALUES ('431124', '道县', '3');
INSERT INTO `t_city` VALUES ('431125', '江永县', '3');
INSERT INTO `t_city` VALUES ('431126', '宁远县', '3');
INSERT INTO `t_city` VALUES ('431127', '蓝山县', '3');
INSERT INTO `t_city` VALUES ('431128', '新田县', '3');
INSERT INTO `t_city` VALUES ('431129', '江华瑶族自治县', '3');
INSERT INTO `t_city` VALUES ('431200', '怀化市', '2');
INSERT INTO `t_city` VALUES ('431201', '市辖区', '3');
INSERT INTO `t_city` VALUES ('431202', '鹤城区', '3');
INSERT INTO `t_city` VALUES ('431221', '中方县', '3');
INSERT INTO `t_city` VALUES ('431222', '沅陵县', '3');
INSERT INTO `t_city` VALUES ('431223', '辰溪县', '3');
INSERT INTO `t_city` VALUES ('431224', '溆浦县', '3');
INSERT INTO `t_city` VALUES ('431225', '会同县', '3');
INSERT INTO `t_city` VALUES ('431226', '麻阳苗族自治县', '3');
INSERT INTO `t_city` VALUES ('431227', '新晃侗族自治县', '3');
INSERT INTO `t_city` VALUES ('431228', '芷江侗族自治县', '3');
INSERT INTO `t_city` VALUES ('431229', '靖州苗族侗族自治县', '3');
INSERT INTO `t_city` VALUES ('431230', '通道侗族自治县', '3');
INSERT INTO `t_city` VALUES ('431281', '洪江市', '3');
INSERT INTO `t_city` VALUES ('431300', '娄底市', '2');
INSERT INTO `t_city` VALUES ('431301', '市辖区', '3');
INSERT INTO `t_city` VALUES ('431302', '娄星区', '3');
INSERT INTO `t_city` VALUES ('431321', '双峰县', '3');
INSERT INTO `t_city` VALUES ('431322', '新化县', '3');
INSERT INTO `t_city` VALUES ('431381', '冷水江市', '3');
INSERT INTO `t_city` VALUES ('431382', '涟源市', '3');
INSERT INTO `t_city` VALUES ('433100', '湘西土家族苗族自治州', '2');
INSERT INTO `t_city` VALUES ('433101', '吉首市', '3');
INSERT INTO `t_city` VALUES ('433122', '泸溪县', '3');
INSERT INTO `t_city` VALUES ('433123', '凤凰县', '3');
INSERT INTO `t_city` VALUES ('433124', '花垣县', '3');
INSERT INTO `t_city` VALUES ('433125', '保靖县', '3');
INSERT INTO `t_city` VALUES ('433126', '古丈县', '3');
INSERT INTO `t_city` VALUES ('433127', '永顺县', '3');
INSERT INTO `t_city` VALUES ('433130', '龙山县', '3');
INSERT INTO `t_city` VALUES ('440000', '广东省', '1');
INSERT INTO `t_city` VALUES ('440100', '广州市', '2');
INSERT INTO `t_city` VALUES ('440101', '市辖区', '3');
INSERT INTO `t_city` VALUES ('440103', '荔湾区', '3');
INSERT INTO `t_city` VALUES ('440104', '越秀区', '3');
INSERT INTO `t_city` VALUES ('440105', '海珠区', '3');
INSERT INTO `t_city` VALUES ('440106', '天河区', '3');
INSERT INTO `t_city` VALUES ('440111', '白云区', '3');
INSERT INTO `t_city` VALUES ('440112', '黄埔区', '3');
INSERT INTO `t_city` VALUES ('440113', '番禺区', '3');
INSERT INTO `t_city` VALUES ('440114', '花都区', '3');
INSERT INTO `t_city` VALUES ('440115', '南沙区', '3');
INSERT INTO `t_city` VALUES ('440117', '从化区', '3');
INSERT INTO `t_city` VALUES ('440118', '增城区', '3');
INSERT INTO `t_city` VALUES ('440200', '韶关市', '2');
INSERT INTO `t_city` VALUES ('440201', '市辖区', '3');
INSERT INTO `t_city` VALUES ('440203', '武江区', '3');
INSERT INTO `t_city` VALUES ('440204', '浈江区', '3');
INSERT INTO `t_city` VALUES ('440205', '曲江区', '3');
INSERT INTO `t_city` VALUES ('440222', '始兴县', '3');
INSERT INTO `t_city` VALUES ('440224', '仁化县', '3');
INSERT INTO `t_city` VALUES ('440229', '翁源县', '3');
INSERT INTO `t_city` VALUES ('440232', '乳源瑶族自治县', '3');
INSERT INTO `t_city` VALUES ('440233', '新丰县', '3');
INSERT INTO `t_city` VALUES ('440281', '乐昌市', '3');
INSERT INTO `t_city` VALUES ('440282', '南雄市', '3');
INSERT INTO `t_city` VALUES ('440300', '深圳市', '2');
INSERT INTO `t_city` VALUES ('440301', '市辖区', '3');
INSERT INTO `t_city` VALUES ('440303', '罗湖区', '3');
INSERT INTO `t_city` VALUES ('440304', '福田区', '3');
INSERT INTO `t_city` VALUES ('440305', '南山区', '3');
INSERT INTO `t_city` VALUES ('440306', '宝安区', '3');
INSERT INTO `t_city` VALUES ('440307', '龙岗区', '3');
INSERT INTO `t_city` VALUES ('440308', '盐田区', '3');
INSERT INTO `t_city` VALUES ('440400', '珠海市', '2');
INSERT INTO `t_city` VALUES ('440401', '市辖区', '3');
INSERT INTO `t_city` VALUES ('440402', '香洲区', '3');
INSERT INTO `t_city` VALUES ('440403', '斗门区', '3');
INSERT INTO `t_city` VALUES ('440404', '金湾区', '3');
INSERT INTO `t_city` VALUES ('440500', '汕头市', '2');
INSERT INTO `t_city` VALUES ('440501', '市辖区', '3');
INSERT INTO `t_city` VALUES ('440507', '龙湖区', '3');
INSERT INTO `t_city` VALUES ('440511', '金平区', '3');
INSERT INTO `t_city` VALUES ('440512', '濠江区', '3');
INSERT INTO `t_city` VALUES ('440513', '潮阳区', '3');
INSERT INTO `t_city` VALUES ('440514', '潮南区', '3');
INSERT INTO `t_city` VALUES ('440515', '澄海区', '3');
INSERT INTO `t_city` VALUES ('440523', '南澳县', '3');
INSERT INTO `t_city` VALUES ('440600', '佛山市', '2');
INSERT INTO `t_city` VALUES ('440601', '市辖区', '3');
INSERT INTO `t_city` VALUES ('440604', '禅城区', '3');
INSERT INTO `t_city` VALUES ('440605', '南海区', '3');
INSERT INTO `t_city` VALUES ('440606', '顺德区', '3');
INSERT INTO `t_city` VALUES ('440607', '三水区', '3');
INSERT INTO `t_city` VALUES ('440608', '高明区', '3');
INSERT INTO `t_city` VALUES ('440700', '江门市', '2');
INSERT INTO `t_city` VALUES ('440701', '市辖区', '3');
INSERT INTO `t_city` VALUES ('440703', '蓬江区', '3');
INSERT INTO `t_city` VALUES ('440704', '江海区', '3');
INSERT INTO `t_city` VALUES ('440705', '新会区', '3');
INSERT INTO `t_city` VALUES ('440781', '台山市', '3');
INSERT INTO `t_city` VALUES ('440783', '开平市', '3');
INSERT INTO `t_city` VALUES ('440784', '鹤山市', '3');
INSERT INTO `t_city` VALUES ('440785', '恩平市', '3');
INSERT INTO `t_city` VALUES ('440800', '湛江市', '2');
INSERT INTO `t_city` VALUES ('440801', '市辖区', '3');
INSERT INTO `t_city` VALUES ('440802', '赤坎区', '3');
INSERT INTO `t_city` VALUES ('440803', '霞山区', '3');
INSERT INTO `t_city` VALUES ('440804', '坡头区', '3');
INSERT INTO `t_city` VALUES ('440811', '麻章区', '3');
INSERT INTO `t_city` VALUES ('440823', '遂溪县', '3');
INSERT INTO `t_city` VALUES ('440825', '徐闻县', '3');
INSERT INTO `t_city` VALUES ('440881', '廉江市', '3');
INSERT INTO `t_city` VALUES ('440882', '雷州市', '3');
INSERT INTO `t_city` VALUES ('440883', '吴川市', '3');
INSERT INTO `t_city` VALUES ('440900', '茂名市', '2');
INSERT INTO `t_city` VALUES ('440901', '市辖区', '3');
INSERT INTO `t_city` VALUES ('440902', '茂南区', '3');
INSERT INTO `t_city` VALUES ('440904', '电白区', '3');
INSERT INTO `t_city` VALUES ('440981', '高州市', '3');
INSERT INTO `t_city` VALUES ('440982', '化州市', '3');
INSERT INTO `t_city` VALUES ('440983', '信宜市', '3');
INSERT INTO `t_city` VALUES ('441200', '肇庆市', '2');
INSERT INTO `t_city` VALUES ('441201', '市辖区', '3');
INSERT INTO `t_city` VALUES ('441202', '端州区', '3');
INSERT INTO `t_city` VALUES ('441203', '鼎湖区', '3');
INSERT INTO `t_city` VALUES ('441204', '高要区', '3');
INSERT INTO `t_city` VALUES ('441223', '广宁县', '3');
INSERT INTO `t_city` VALUES ('441224', '怀集县', '3');
INSERT INTO `t_city` VALUES ('441225', '封开县', '3');
INSERT INTO `t_city` VALUES ('441226', '德庆县', '3');
INSERT INTO `t_city` VALUES ('441284', '四会市', '3');
INSERT INTO `t_city` VALUES ('441300', '惠州市', '2');
INSERT INTO `t_city` VALUES ('441301', '市辖区', '3');
INSERT INTO `t_city` VALUES ('441302', '惠城区', '3');
INSERT INTO `t_city` VALUES ('441303', '惠阳区', '3');
INSERT INTO `t_city` VALUES ('441322', '博罗县', '3');
INSERT INTO `t_city` VALUES ('441323', '惠东县', '3');
INSERT INTO `t_city` VALUES ('441324', '龙门县', '3');
INSERT INTO `t_city` VALUES ('441400', '梅州市', '2');
INSERT INTO `t_city` VALUES ('441401', '市辖区', '3');
INSERT INTO `t_city` VALUES ('441402', '梅江区', '3');
INSERT INTO `t_city` VALUES ('441403', '梅县区', '3');
INSERT INTO `t_city` VALUES ('441422', '大埔县', '3');
INSERT INTO `t_city` VALUES ('441423', '丰顺县', '3');
INSERT INTO `t_city` VALUES ('441424', '五华县', '3');
INSERT INTO `t_city` VALUES ('441426', '平远县', '3');
INSERT INTO `t_city` VALUES ('441427', '蕉岭县', '3');
INSERT INTO `t_city` VALUES ('441481', '兴宁市', '3');
INSERT INTO `t_city` VALUES ('441500', '汕尾市', '2');
INSERT INTO `t_city` VALUES ('441501', '市辖区', '3');
INSERT INTO `t_city` VALUES ('441502', '城区', '3');
INSERT INTO `t_city` VALUES ('441521', '海丰县', '3');
INSERT INTO `t_city` VALUES ('441523', '陆河县', '3');
INSERT INTO `t_city` VALUES ('441581', '陆丰市', '3');
INSERT INTO `t_city` VALUES ('441600', '河源市', '2');
INSERT INTO `t_city` VALUES ('441601', '市辖区', '3');
INSERT INTO `t_city` VALUES ('441602', '源城区', '3');
INSERT INTO `t_city` VALUES ('441621', '紫金县', '3');
INSERT INTO `t_city` VALUES ('441622', '龙川县', '3');
INSERT INTO `t_city` VALUES ('441623', '连平县', '3');
INSERT INTO `t_city` VALUES ('441624', '和平县', '3');
INSERT INTO `t_city` VALUES ('441625', '东源县', '3');
INSERT INTO `t_city` VALUES ('441700', '阳江市', '2');
INSERT INTO `t_city` VALUES ('441701', '市辖区', '3');
INSERT INTO `t_city` VALUES ('441702', '江城区', '3');
INSERT INTO `t_city` VALUES ('441704', '阳东区', '3');
INSERT INTO `t_city` VALUES ('441721', '阳西县', '3');
INSERT INTO `t_city` VALUES ('441781', '阳春市', '3');
INSERT INTO `t_city` VALUES ('441800', '清远市', '2');
INSERT INTO `t_city` VALUES ('441801', '市辖区', '3');
INSERT INTO `t_city` VALUES ('441802', '清城区', '3');
INSERT INTO `t_city` VALUES ('441803', '清新区', '3');
INSERT INTO `t_city` VALUES ('441821', '佛冈县', '3');
INSERT INTO `t_city` VALUES ('441823', '阳山县', '3');
INSERT INTO `t_city` VALUES ('441825', '连山壮族瑶族自治县', '3');
INSERT INTO `t_city` VALUES ('441826', '连南瑶族自治县', '3');
INSERT INTO `t_city` VALUES ('441881', '英德市', '3');
INSERT INTO `t_city` VALUES ('441882', '连州市', '3');
INSERT INTO `t_city` VALUES ('441900', '东莞市', '2');
INSERT INTO `t_city` VALUES ('442000', '中山市', '2');
INSERT INTO `t_city` VALUES ('445100', '潮州市', '2');
INSERT INTO `t_city` VALUES ('445101', '市辖区', '3');
INSERT INTO `t_city` VALUES ('445102', '湘桥区', '3');
INSERT INTO `t_city` VALUES ('445103', '潮安区', '3');
INSERT INTO `t_city` VALUES ('445122', '饶平县', '3');
INSERT INTO `t_city` VALUES ('445200', '揭阳市', '2');
INSERT INTO `t_city` VALUES ('445201', '市辖区', '3');
INSERT INTO `t_city` VALUES ('445202', '榕城区', '3');
INSERT INTO `t_city` VALUES ('445203', '揭东区', '3');
INSERT INTO `t_city` VALUES ('445222', '揭西县', '3');
INSERT INTO `t_city` VALUES ('445224', '惠来县', '3');
INSERT INTO `t_city` VALUES ('445281', '普宁市', '3');
INSERT INTO `t_city` VALUES ('445300', '云浮市', '2');
INSERT INTO `t_city` VALUES ('445301', '市辖区', '3');
INSERT INTO `t_city` VALUES ('445302', '云城区', '3');
INSERT INTO `t_city` VALUES ('445303', '云安区', '3');
INSERT INTO `t_city` VALUES ('445321', '新兴县', '3');
INSERT INTO `t_city` VALUES ('445322', '郁南县', '3');
INSERT INTO `t_city` VALUES ('445381', '罗定市', '3');
INSERT INTO `t_city` VALUES ('450000', '广西壮族自治区', '1');
INSERT INTO `t_city` VALUES ('450100', '南宁市', '2');
INSERT INTO `t_city` VALUES ('450101', '市辖区', '3');
INSERT INTO `t_city` VALUES ('450102', '兴宁区', '3');
INSERT INTO `t_city` VALUES ('450103', '青秀区', '3');
INSERT INTO `t_city` VALUES ('450105', '江南区', '3');
INSERT INTO `t_city` VALUES ('450107', '西乡塘区', '3');
INSERT INTO `t_city` VALUES ('450108', '良庆区', '3');
INSERT INTO `t_city` VALUES ('450109', '邕宁区', '3');
INSERT INTO `t_city` VALUES ('450110', '武鸣区', '3');
INSERT INTO `t_city` VALUES ('450123', '隆安县', '3');
INSERT INTO `t_city` VALUES ('450124', '马山县', '3');
INSERT INTO `t_city` VALUES ('450125', '上林县', '3');
INSERT INTO `t_city` VALUES ('450126', '宾阳县', '3');
INSERT INTO `t_city` VALUES ('450127', '横县', '3');
INSERT INTO `t_city` VALUES ('450200', '柳州市', '2');
INSERT INTO `t_city` VALUES ('450201', '市辖区', '3');
INSERT INTO `t_city` VALUES ('450202', '城中区', '3');
INSERT INTO `t_city` VALUES ('450203', '鱼峰区', '3');
INSERT INTO `t_city` VALUES ('450204', '柳南区', '3');
INSERT INTO `t_city` VALUES ('450205', '柳北区', '3');
INSERT INTO `t_city` VALUES ('450206', '柳江区', '3');
INSERT INTO `t_city` VALUES ('450222', '柳城县', '3');
INSERT INTO `t_city` VALUES ('450223', '鹿寨县', '3');
INSERT INTO `t_city` VALUES ('450224', '融安县', '3');
INSERT INTO `t_city` VALUES ('450225', '融水苗族自治县', '3');
INSERT INTO `t_city` VALUES ('450226', '三江侗族自治县', '3');
INSERT INTO `t_city` VALUES ('450300', '桂林市', '2');
INSERT INTO `t_city` VALUES ('450301', '市辖区', '3');
INSERT INTO `t_city` VALUES ('450302', '秀峰区', '3');
INSERT INTO `t_city` VALUES ('450303', '叠彩区', '3');
INSERT INTO `t_city` VALUES ('450304', '象山区', '3');
INSERT INTO `t_city` VALUES ('450305', '七星区', '3');
INSERT INTO `t_city` VALUES ('450311', '雁山区', '3');
INSERT INTO `t_city` VALUES ('450312', '临桂区', '3');
INSERT INTO `t_city` VALUES ('450321', '阳朔县', '3');
INSERT INTO `t_city` VALUES ('450323', '灵川县', '3');
INSERT INTO `t_city` VALUES ('450324', '全州县', '3');
INSERT INTO `t_city` VALUES ('450325', '兴安县', '3');
INSERT INTO `t_city` VALUES ('450326', '永福县', '3');
INSERT INTO `t_city` VALUES ('450327', '灌阳县', '3');
INSERT INTO `t_city` VALUES ('450328', '龙胜各族自治县', '3');
INSERT INTO `t_city` VALUES ('450329', '资源县', '3');
INSERT INTO `t_city` VALUES ('450330', '平乐县', '3');
INSERT INTO `t_city` VALUES ('450331', '荔浦县', '3');
INSERT INTO `t_city` VALUES ('450332', '恭城瑶族自治县', '3');
INSERT INTO `t_city` VALUES ('450400', '梧州市', '2');
INSERT INTO `t_city` VALUES ('450401', '市辖区', '3');
INSERT INTO `t_city` VALUES ('450403', '万秀区', '3');
INSERT INTO `t_city` VALUES ('450405', '长洲区', '3');
INSERT INTO `t_city` VALUES ('450406', '龙圩区', '3');
INSERT INTO `t_city` VALUES ('450421', '苍梧县', '3');
INSERT INTO `t_city` VALUES ('450422', '藤县', '3');
INSERT INTO `t_city` VALUES ('450423', '蒙山县', '3');
INSERT INTO `t_city` VALUES ('450481', '岑溪市', '3');
INSERT INTO `t_city` VALUES ('450500', '北海市', '2');
INSERT INTO `t_city` VALUES ('450501', '市辖区', '3');
INSERT INTO `t_city` VALUES ('450502', '海城区', '3');
INSERT INTO `t_city` VALUES ('450503', '银海区', '3');
INSERT INTO `t_city` VALUES ('450512', '铁山港区', '3');
INSERT INTO `t_city` VALUES ('450521', '合浦县', '3');
INSERT INTO `t_city` VALUES ('450600', '防城港市', '2');
INSERT INTO `t_city` VALUES ('450601', '市辖区', '3');
INSERT INTO `t_city` VALUES ('450602', '港口区', '3');
INSERT INTO `t_city` VALUES ('450603', '防城区', '3');
INSERT INTO `t_city` VALUES ('450621', '上思县', '3');
INSERT INTO `t_city` VALUES ('450681', '东兴市', '3');
INSERT INTO `t_city` VALUES ('450700', '钦州市', '2');
INSERT INTO `t_city` VALUES ('450701', '市辖区', '3');
INSERT INTO `t_city` VALUES ('450702', '钦南区', '3');
INSERT INTO `t_city` VALUES ('450703', '钦北区', '3');
INSERT INTO `t_city` VALUES ('450721', '灵山县', '3');
INSERT INTO `t_city` VALUES ('450722', '浦北县', '3');
INSERT INTO `t_city` VALUES ('450800', '贵港市', '2');
INSERT INTO `t_city` VALUES ('450801', '市辖区', '3');
INSERT INTO `t_city` VALUES ('450802', '港北区', '3');
INSERT INTO `t_city` VALUES ('450803', '港南区', '3');
INSERT INTO `t_city` VALUES ('450804', '覃塘区', '3');
INSERT INTO `t_city` VALUES ('450821', '平南县', '3');
INSERT INTO `t_city` VALUES ('450881', '桂平市', '3');
INSERT INTO `t_city` VALUES ('450900', '玉林市', '2');
INSERT INTO `t_city` VALUES ('450901', '市辖区', '3');
INSERT INTO `t_city` VALUES ('450902', '玉州区', '3');
INSERT INTO `t_city` VALUES ('450903', '福绵区', '3');
INSERT INTO `t_city` VALUES ('450921', '容县', '3');
INSERT INTO `t_city` VALUES ('450922', '陆川县', '3');
INSERT INTO `t_city` VALUES ('450923', '博白县', '3');
INSERT INTO `t_city` VALUES ('450924', '兴业县', '3');
INSERT INTO `t_city` VALUES ('450981', '北流市', '3');
INSERT INTO `t_city` VALUES ('451000', '百色市', '2');
INSERT INTO `t_city` VALUES ('451001', '市辖区', '3');
INSERT INTO `t_city` VALUES ('451002', '右江区', '3');
INSERT INTO `t_city` VALUES ('451021', '田阳县', '3');
INSERT INTO `t_city` VALUES ('451022', '田东县', '3');
INSERT INTO `t_city` VALUES ('451023', '平果县', '3');
INSERT INTO `t_city` VALUES ('451024', '德保县', '3');
INSERT INTO `t_city` VALUES ('451026', '那坡县', '3');
INSERT INTO `t_city` VALUES ('451027', '凌云县', '3');
INSERT INTO `t_city` VALUES ('451028', '乐业县', '3');
INSERT INTO `t_city` VALUES ('451029', '田林县', '3');
INSERT INTO `t_city` VALUES ('451030', '西林县', '3');
INSERT INTO `t_city` VALUES ('451031', '隆林各族自治县', '3');
INSERT INTO `t_city` VALUES ('451081', '靖西市', '3');
INSERT INTO `t_city` VALUES ('451100', '贺州市', '2');
INSERT INTO `t_city` VALUES ('451101', '市辖区', '3');
INSERT INTO `t_city` VALUES ('451102', '八步区', '3');
INSERT INTO `t_city` VALUES ('451103', '平桂区', '3');
INSERT INTO `t_city` VALUES ('451121', '昭平县', '3');
INSERT INTO `t_city` VALUES ('451122', '钟山县', '3');
INSERT INTO `t_city` VALUES ('451123', '富川瑶族自治县', '3');
INSERT INTO `t_city` VALUES ('451200', '河池市', '2');
INSERT INTO `t_city` VALUES ('451201', '市辖区', '3');
INSERT INTO `t_city` VALUES ('451202', '金城江区', '3');
INSERT INTO `t_city` VALUES ('451221', '南丹县', '3');
INSERT INTO `t_city` VALUES ('451222', '天峨县', '3');
INSERT INTO `t_city` VALUES ('451223', '凤山县', '3');
INSERT INTO `t_city` VALUES ('451224', '东兰县', '3');
INSERT INTO `t_city` VALUES ('451225', '罗城仫佬族自治县', '3');
INSERT INTO `t_city` VALUES ('451226', '环江毛南族自治县', '3');
INSERT INTO `t_city` VALUES ('451227', '巴马瑶族自治县', '3');
INSERT INTO `t_city` VALUES ('451228', '都安瑶族自治县', '3');
INSERT INTO `t_city` VALUES ('451229', '大化瑶族自治县', '3');
INSERT INTO `t_city` VALUES ('451281', '宜州市', '3');
INSERT INTO `t_city` VALUES ('451300', '来宾市', '2');
INSERT INTO `t_city` VALUES ('451301', '市辖区', '3');
INSERT INTO `t_city` VALUES ('451302', '兴宾区', '3');
INSERT INTO `t_city` VALUES ('451321', '忻城县', '3');
INSERT INTO `t_city` VALUES ('451322', '象州县', '3');
INSERT INTO `t_city` VALUES ('451323', '武宣县', '3');
INSERT INTO `t_city` VALUES ('451324', '金秀瑶族自治县', '3');
INSERT INTO `t_city` VALUES ('451381', '合山市', '3');
INSERT INTO `t_city` VALUES ('451400', '崇左市', '2');
INSERT INTO `t_city` VALUES ('451401', '市辖区', '3');
INSERT INTO `t_city` VALUES ('451402', '江州区', '3');
INSERT INTO `t_city` VALUES ('451421', '扶绥县', '3');
INSERT INTO `t_city` VALUES ('451422', '宁明县', '3');
INSERT INTO `t_city` VALUES ('451423', '龙州县', '3');
INSERT INTO `t_city` VALUES ('451424', '大新县', '3');
INSERT INTO `t_city` VALUES ('451425', '天等县', '3');
INSERT INTO `t_city` VALUES ('451481', '凭祥市', '3');
INSERT INTO `t_city` VALUES ('460000', '海南省', '1');
INSERT INTO `t_city` VALUES ('460100', '海口市', '2');
INSERT INTO `t_city` VALUES ('460101', '市辖区', '3');
INSERT INTO `t_city` VALUES ('460105', '秀英区', '3');
INSERT INTO `t_city` VALUES ('460106', '龙华区', '3');
INSERT INTO `t_city` VALUES ('460107', '琼山区', '3');
INSERT INTO `t_city` VALUES ('460108', '美兰区', '3');
INSERT INTO `t_city` VALUES ('460200', '三亚市', '2');
INSERT INTO `t_city` VALUES ('460201', '市辖区', '3');
INSERT INTO `t_city` VALUES ('460202', '海棠区', '3');
INSERT INTO `t_city` VALUES ('460203', '吉阳区', '3');
INSERT INTO `t_city` VALUES ('460204', '天涯区', '3');
INSERT INTO `t_city` VALUES ('460205', '崖州区', '3');
INSERT INTO `t_city` VALUES ('460300', '三沙市', '2');
INSERT INTO `t_city` VALUES ('460400', '儋州市', '2');
INSERT INTO `t_city` VALUES ('469000', '省直辖县级行政区划', '2');
INSERT INTO `t_city` VALUES ('469001', '五指山市', '3');
INSERT INTO `t_city` VALUES ('469002', '琼海市', '3');
INSERT INTO `t_city` VALUES ('469005', '文昌市', '3');
INSERT INTO `t_city` VALUES ('469006', '万宁市', '3');
INSERT INTO `t_city` VALUES ('469007', '东方市', '3');
INSERT INTO `t_city` VALUES ('469021', '定安县', '3');
INSERT INTO `t_city` VALUES ('469022', '屯昌县', '3');
INSERT INTO `t_city` VALUES ('469023', '澄迈县', '3');
INSERT INTO `t_city` VALUES ('469024', '临高县', '3');
INSERT INTO `t_city` VALUES ('469025', '白沙黎族自治县', '3');
INSERT INTO `t_city` VALUES ('469026', '昌江黎族自治县', '3');
INSERT INTO `t_city` VALUES ('469027', '乐东黎族自治县', '3');
INSERT INTO `t_city` VALUES ('469028', '陵水黎族自治县', '3');
INSERT INTO `t_city` VALUES ('469029', '保亭黎族苗族自治县', '3');
INSERT INTO `t_city` VALUES ('469030', '琼中黎族苗族自治县', '3');
INSERT INTO `t_city` VALUES ('500000', '重庆市', '1');
INSERT INTO `t_city` VALUES ('500100', '市辖区', '2');
INSERT INTO `t_city` VALUES ('500101', '万州区', '3');
INSERT INTO `t_city` VALUES ('500102', '涪陵区', '3');
INSERT INTO `t_city` VALUES ('500103', '渝中区', '3');
INSERT INTO `t_city` VALUES ('500104', '大渡口区', '3');
INSERT INTO `t_city` VALUES ('500105', '江北区', '3');
INSERT INTO `t_city` VALUES ('500106', '沙坪坝区', '3');
INSERT INTO `t_city` VALUES ('500107', '九龙坡区', '3');
INSERT INTO `t_city` VALUES ('500108', '南岸区', '3');
INSERT INTO `t_city` VALUES ('500109', '北碚区', '3');
INSERT INTO `t_city` VALUES ('500110', '綦江区', '3');
INSERT INTO `t_city` VALUES ('500111', '大足区', '3');
INSERT INTO `t_city` VALUES ('500112', '渝北区', '3');
INSERT INTO `t_city` VALUES ('500113', '巴南区', '3');
INSERT INTO `t_city` VALUES ('500114', '黔江区', '3');
INSERT INTO `t_city` VALUES ('500115', '长寿区', '3');
INSERT INTO `t_city` VALUES ('500116', '江津区', '3');
INSERT INTO `t_city` VALUES ('500117', '合川区', '3');
INSERT INTO `t_city` VALUES ('500118', '永川区', '3');
INSERT INTO `t_city` VALUES ('500119', '南川区', '3');
INSERT INTO `t_city` VALUES ('500120', '璧山区', '3');
INSERT INTO `t_city` VALUES ('500151', '铜梁区', '3');
INSERT INTO `t_city` VALUES ('500152', '潼南区', '3');
INSERT INTO `t_city` VALUES ('500153', '荣昌区', '3');
INSERT INTO `t_city` VALUES ('500154', '开州区', '3');
INSERT INTO `t_city` VALUES ('500200', '县', '2');
INSERT INTO `t_city` VALUES ('500228', '梁平县', '3');
INSERT INTO `t_city` VALUES ('500229', '城口县', '3');
INSERT INTO `t_city` VALUES ('500230', '丰都县', '3');
INSERT INTO `t_city` VALUES ('500231', '垫江县', '3');
INSERT INTO `t_city` VALUES ('500232', '武隆县', '3');
INSERT INTO `t_city` VALUES ('500233', '忠县', '3');
INSERT INTO `t_city` VALUES ('500235', '云阳县', '3');
INSERT INTO `t_city` VALUES ('500236', '奉节县', '3');
INSERT INTO `t_city` VALUES ('500237', '巫山县', '3');
INSERT INTO `t_city` VALUES ('500238', '巫溪县', '3');
INSERT INTO `t_city` VALUES ('500240', '石柱土家族自治县', '3');
INSERT INTO `t_city` VALUES ('500241', '秀山土家族苗族自治县', '3');
INSERT INTO `t_city` VALUES ('500242', '酉阳土家族苗族自治县', '3');
INSERT INTO `t_city` VALUES ('500243', '彭水苗族土家族自治县', '3');
INSERT INTO `t_city` VALUES ('510000', '四川省', '1');
INSERT INTO `t_city` VALUES ('510100', '成都市', '2');
INSERT INTO `t_city` VALUES ('510101', '市辖区', '3');
INSERT INTO `t_city` VALUES ('510104', '锦江区', '3');
INSERT INTO `t_city` VALUES ('510105', '青羊区', '3');
INSERT INTO `t_city` VALUES ('510106', '金牛区', '3');
INSERT INTO `t_city` VALUES ('510107', '武侯区', '3');
INSERT INTO `t_city` VALUES ('510108', '成华区', '3');
INSERT INTO `t_city` VALUES ('510112', '龙泉驿区', '3');
INSERT INTO `t_city` VALUES ('510113', '青白江区', '3');
INSERT INTO `t_city` VALUES ('510114', '新都区', '3');
INSERT INTO `t_city` VALUES ('510115', '温江区', '3');
INSERT INTO `t_city` VALUES ('510116', '双流区', '3');
INSERT INTO `t_city` VALUES ('510121', '金堂县', '3');
INSERT INTO `t_city` VALUES ('510124', '郫县', '3');
INSERT INTO `t_city` VALUES ('510129', '大邑县', '3');
INSERT INTO `t_city` VALUES ('510131', '蒲江县', '3');
INSERT INTO `t_city` VALUES ('510132', '新津县', '3');
INSERT INTO `t_city` VALUES ('510181', '都江堰市', '3');
INSERT INTO `t_city` VALUES ('510182', '彭州市', '3');
INSERT INTO `t_city` VALUES ('510183', '邛崃市', '3');
INSERT INTO `t_city` VALUES ('510184', '崇州市', '3');
INSERT INTO `t_city` VALUES ('510185', '简阳市', '3');
INSERT INTO `t_city` VALUES ('510300', '自贡市', '2');
INSERT INTO `t_city` VALUES ('510301', '市辖区', '3');
INSERT INTO `t_city` VALUES ('510302', '自流井区', '3');
INSERT INTO `t_city` VALUES ('510303', '贡井区', '3');
INSERT INTO `t_city` VALUES ('510304', '大安区', '3');
INSERT INTO `t_city` VALUES ('510311', '沿滩区', '3');
INSERT INTO `t_city` VALUES ('510321', '荣县', '3');
INSERT INTO `t_city` VALUES ('510322', '富顺县', '3');
INSERT INTO `t_city` VALUES ('510400', '攀枝花市', '2');
INSERT INTO `t_city` VALUES ('510401', '市辖区', '3');
INSERT INTO `t_city` VALUES ('510402', '东区', '3');
INSERT INTO `t_city` VALUES ('510403', '西区', '3');
INSERT INTO `t_city` VALUES ('510411', '仁和区', '3');
INSERT INTO `t_city` VALUES ('510421', '米易县', '3');
INSERT INTO `t_city` VALUES ('510422', '盐边县', '3');
INSERT INTO `t_city` VALUES ('510500', '泸州市', '2');
INSERT INTO `t_city` VALUES ('510501', '市辖区', '3');
INSERT INTO `t_city` VALUES ('510502', '江阳区', '3');
INSERT INTO `t_city` VALUES ('510503', '纳溪区', '3');
INSERT INTO `t_city` VALUES ('510504', '龙马潭区', '3');
INSERT INTO `t_city` VALUES ('510521', '泸县', '3');
INSERT INTO `t_city` VALUES ('510522', '合江县', '3');
INSERT INTO `t_city` VALUES ('510524', '叙永县', '3');
INSERT INTO `t_city` VALUES ('510525', '古蔺县', '3');
INSERT INTO `t_city` VALUES ('510600', '德阳市', '2');
INSERT INTO `t_city` VALUES ('510601', '市辖区', '3');
INSERT INTO `t_city` VALUES ('510603', '旌阳区', '3');
INSERT INTO `t_city` VALUES ('510623', '中江县', '3');
INSERT INTO `t_city` VALUES ('510626', '罗江县', '3');
INSERT INTO `t_city` VALUES ('510681', '广汉市', '3');
INSERT INTO `t_city` VALUES ('510682', '什邡市', '3');
INSERT INTO `t_city` VALUES ('510683', '绵竹市', '3');
INSERT INTO `t_city` VALUES ('510700', '绵阳市', '2');
INSERT INTO `t_city` VALUES ('510701', '市辖区', '3');
INSERT INTO `t_city` VALUES ('510703', '涪城区', '3');
INSERT INTO `t_city` VALUES ('510704', '游仙区', '3');
INSERT INTO `t_city` VALUES ('510705', '安州区', '3');
INSERT INTO `t_city` VALUES ('510722', '三台县', '3');
INSERT INTO `t_city` VALUES ('510723', '盐亭县', '3');
INSERT INTO `t_city` VALUES ('510725', '梓潼县', '3');
INSERT INTO `t_city` VALUES ('510726', '北川羌族自治县', '3');
INSERT INTO `t_city` VALUES ('510727', '平武县', '3');
INSERT INTO `t_city` VALUES ('510781', '江油市', '3');
INSERT INTO `t_city` VALUES ('510800', '广元市', '2');
INSERT INTO `t_city` VALUES ('510801', '市辖区', '3');
INSERT INTO `t_city` VALUES ('510802', '利州区', '3');
INSERT INTO `t_city` VALUES ('510811', '昭化区', '3');
INSERT INTO `t_city` VALUES ('510812', '朝天区', '3');
INSERT INTO `t_city` VALUES ('510821', '旺苍县', '3');
INSERT INTO `t_city` VALUES ('510822', '青川县', '3');
INSERT INTO `t_city` VALUES ('510823', '剑阁县', '3');
INSERT INTO `t_city` VALUES ('510824', '苍溪县', '3');
INSERT INTO `t_city` VALUES ('510900', '遂宁市', '2');
INSERT INTO `t_city` VALUES ('510901', '市辖区', '3');
INSERT INTO `t_city` VALUES ('510903', '船山区', '3');
INSERT INTO `t_city` VALUES ('510904', '安居区', '3');
INSERT INTO `t_city` VALUES ('510921', '蓬溪县', '3');
INSERT INTO `t_city` VALUES ('510922', '射洪县', '3');
INSERT INTO `t_city` VALUES ('510923', '大英县', '3');
INSERT INTO `t_city` VALUES ('511000', '内江市', '2');
INSERT INTO `t_city` VALUES ('511001', '市辖区', '3');
INSERT INTO `t_city` VALUES ('511002', '市中区', '3');
INSERT INTO `t_city` VALUES ('511011', '东兴区', '3');
INSERT INTO `t_city` VALUES ('511024', '威远县', '3');
INSERT INTO `t_city` VALUES ('511025', '资中县', '3');
INSERT INTO `t_city` VALUES ('511028', '隆昌县', '3');
INSERT INTO `t_city` VALUES ('511100', '乐山市', '2');
INSERT INTO `t_city` VALUES ('511101', '市辖区', '3');
INSERT INTO `t_city` VALUES ('511102', '市中区', '3');
INSERT INTO `t_city` VALUES ('511111', '沙湾区', '3');
INSERT INTO `t_city` VALUES ('511112', '五通桥区', '3');
INSERT INTO `t_city` VALUES ('511113', '金口河区', '3');
INSERT INTO `t_city` VALUES ('511123', '犍为县', '3');
INSERT INTO `t_city` VALUES ('511124', '井研县', '3');
INSERT INTO `t_city` VALUES ('511126', '夹江县', '3');
INSERT INTO `t_city` VALUES ('511129', '沐川县', '3');
INSERT INTO `t_city` VALUES ('511132', '峨边彝族自治县', '3');
INSERT INTO `t_city` VALUES ('511133', '马边彝族自治县', '3');
INSERT INTO `t_city` VALUES ('511181', '峨眉山市', '3');
INSERT INTO `t_city` VALUES ('511300', '南充市', '2');
INSERT INTO `t_city` VALUES ('511301', '市辖区', '3');
INSERT INTO `t_city` VALUES ('511302', '顺庆区', '3');
INSERT INTO `t_city` VALUES ('511303', '高坪区', '3');
INSERT INTO `t_city` VALUES ('511304', '嘉陵区', '3');
INSERT INTO `t_city` VALUES ('511321', '南部县', '3');
INSERT INTO `t_city` VALUES ('511322', '营山县', '3');
INSERT INTO `t_city` VALUES ('511323', '蓬安县', '3');
INSERT INTO `t_city` VALUES ('511324', '仪陇县', '3');
INSERT INTO `t_city` VALUES ('511325', '西充县', '3');
INSERT INTO `t_city` VALUES ('511381', '阆中市', '3');
INSERT INTO `t_city` VALUES ('511400', '眉山市', '2');
INSERT INTO `t_city` VALUES ('511401', '市辖区', '3');
INSERT INTO `t_city` VALUES ('511402', '东坡区', '3');
INSERT INTO `t_city` VALUES ('511403', '彭山区', '3');
INSERT INTO `t_city` VALUES ('511421', '仁寿县', '3');
INSERT INTO `t_city` VALUES ('511423', '洪雅县', '3');
INSERT INTO `t_city` VALUES ('511424', '丹棱县', '3');
INSERT INTO `t_city` VALUES ('511425', '青神县', '3');
INSERT INTO `t_city` VALUES ('511500', '宜宾市', '2');
INSERT INTO `t_city` VALUES ('511501', '市辖区', '3');
INSERT INTO `t_city` VALUES ('511502', '翠屏区', '3');
INSERT INTO `t_city` VALUES ('511503', '南溪区', '3');
INSERT INTO `t_city` VALUES ('511521', '宜宾县', '3');
INSERT INTO `t_city` VALUES ('511523', '江安县', '3');
INSERT INTO `t_city` VALUES ('511524', '长宁县', '3');
INSERT INTO `t_city` VALUES ('511525', '高县', '3');
INSERT INTO `t_city` VALUES ('511526', '珙县', '3');
INSERT INTO `t_city` VALUES ('511527', '筠连县', '3');
INSERT INTO `t_city` VALUES ('511528', '兴文县', '3');
INSERT INTO `t_city` VALUES ('511529', '屏山县', '3');
INSERT INTO `t_city` VALUES ('511600', '广安市', '2');
INSERT INTO `t_city` VALUES ('511601', '市辖区', '3');
INSERT INTO `t_city` VALUES ('511602', '广安区', '3');
INSERT INTO `t_city` VALUES ('511603', '前锋区', '3');
INSERT INTO `t_city` VALUES ('511621', '岳池县', '3');
INSERT INTO `t_city` VALUES ('511622', '武胜县', '3');
INSERT INTO `t_city` VALUES ('511623', '邻水县', '3');
INSERT INTO `t_city` VALUES ('511681', '华蓥市', '3');
INSERT INTO `t_city` VALUES ('511700', '达州市', '2');
INSERT INTO `t_city` VALUES ('511701', '市辖区', '3');
INSERT INTO `t_city` VALUES ('511702', '通川区', '3');
INSERT INTO `t_city` VALUES ('511703', '达川区', '3');
INSERT INTO `t_city` VALUES ('511722', '宣汉县', '3');
INSERT INTO `t_city` VALUES ('511723', '开江县', '3');
INSERT INTO `t_city` VALUES ('511724', '大竹县', '3');
INSERT INTO `t_city` VALUES ('511725', '渠县', '3');
INSERT INTO `t_city` VALUES ('511781', '万源市', '3');
INSERT INTO `t_city` VALUES ('511800', '雅安市', '2');
INSERT INTO `t_city` VALUES ('511801', '市辖区', '3');
INSERT INTO `t_city` VALUES ('511802', '雨城区', '3');
INSERT INTO `t_city` VALUES ('511803', '名山区', '3');
INSERT INTO `t_city` VALUES ('511822', '荥经县', '3');
INSERT INTO `t_city` VALUES ('511823', '汉源县', '3');
INSERT INTO `t_city` VALUES ('511824', '石棉县', '3');
INSERT INTO `t_city` VALUES ('511825', '天全县', '3');
INSERT INTO `t_city` VALUES ('511826', '芦山县', '3');
INSERT INTO `t_city` VALUES ('511827', '宝兴县', '3');
INSERT INTO `t_city` VALUES ('511900', '巴中市', '2');
INSERT INTO `t_city` VALUES ('511901', '市辖区', '3');
INSERT INTO `t_city` VALUES ('511902', '巴州区', '3');
INSERT INTO `t_city` VALUES ('511903', '恩阳区', '3');
INSERT INTO `t_city` VALUES ('511921', '通江县', '3');
INSERT INTO `t_city` VALUES ('511922', '南江县', '3');
INSERT INTO `t_city` VALUES ('511923', '平昌县', '3');
INSERT INTO `t_city` VALUES ('512000', '资阳市', '2');
INSERT INTO `t_city` VALUES ('512001', '市辖区', '3');
INSERT INTO `t_city` VALUES ('512002', '雁江区', '3');
INSERT INTO `t_city` VALUES ('512021', '安岳县', '3');
INSERT INTO `t_city` VALUES ('512022', '乐至县', '3');
INSERT INTO `t_city` VALUES ('513200', '阿坝藏族羌族自治州', '2');
INSERT INTO `t_city` VALUES ('513201', '马尔康市', '3');
INSERT INTO `t_city` VALUES ('513221', '汶川县', '3');
INSERT INTO `t_city` VALUES ('513222', '理县', '3');
INSERT INTO `t_city` VALUES ('513223', '茂县', '3');
INSERT INTO `t_city` VALUES ('513224', '松潘县', '3');
INSERT INTO `t_city` VALUES ('513225', '九寨沟县', '3');
INSERT INTO `t_city` VALUES ('513226', '金川县', '3');
INSERT INTO `t_city` VALUES ('513227', '小金县', '3');
INSERT INTO `t_city` VALUES ('513228', '黑水县', '3');
INSERT INTO `t_city` VALUES ('513230', '壤塘县', '3');
INSERT INTO `t_city` VALUES ('513231', '阿坝县', '3');
INSERT INTO `t_city` VALUES ('513232', '若尔盖县', '3');
INSERT INTO `t_city` VALUES ('513233', '红原县', '3');
INSERT INTO `t_city` VALUES ('513300', '甘孜藏族自治州', '2');
INSERT INTO `t_city` VALUES ('513301', '康定市', '3');
INSERT INTO `t_city` VALUES ('513322', '泸定县', '3');
INSERT INTO `t_city` VALUES ('513323', '丹巴县', '3');
INSERT INTO `t_city` VALUES ('513324', '九龙县', '3');
INSERT INTO `t_city` VALUES ('513325', '雅江县', '3');
INSERT INTO `t_city` VALUES ('513326', '道孚县', '3');
INSERT INTO `t_city` VALUES ('513327', '炉霍县', '3');
INSERT INTO `t_city` VALUES ('513328', '甘孜县', '3');
INSERT INTO `t_city` VALUES ('513329', '新龙县', '3');
INSERT INTO `t_city` VALUES ('513330', '德格县', '3');
INSERT INTO `t_city` VALUES ('513331', '白玉县', '3');
INSERT INTO `t_city` VALUES ('513332', '石渠县', '3');
INSERT INTO `t_city` VALUES ('513333', '色达县', '3');
INSERT INTO `t_city` VALUES ('513334', '理塘县', '3');
INSERT INTO `t_city` VALUES ('513335', '巴塘县', '3');
INSERT INTO `t_city` VALUES ('513336', '乡城县', '3');
INSERT INTO `t_city` VALUES ('513337', '稻城县', '3');
INSERT INTO `t_city` VALUES ('513338', '得荣县', '3');
INSERT INTO `t_city` VALUES ('513400', '凉山彝族自治州', '2');
INSERT INTO `t_city` VALUES ('513401', '西昌市', '3');
INSERT INTO `t_city` VALUES ('513422', '木里藏族自治县', '3');
INSERT INTO `t_city` VALUES ('513423', '盐源县', '3');
INSERT INTO `t_city` VALUES ('513424', '德昌县', '3');
INSERT INTO `t_city` VALUES ('513425', '会理县', '3');
INSERT INTO `t_city` VALUES ('513426', '会东县', '3');
INSERT INTO `t_city` VALUES ('513427', '宁南县', '3');
INSERT INTO `t_city` VALUES ('513428', '普格县', '3');
INSERT INTO `t_city` VALUES ('513429', '布拖县', '3');
INSERT INTO `t_city` VALUES ('513430', '金阳县', '3');
INSERT INTO `t_city` VALUES ('513431', '昭觉县', '3');
INSERT INTO `t_city` VALUES ('513432', '喜德县', '3');
INSERT INTO `t_city` VALUES ('513433', '冕宁县', '3');
INSERT INTO `t_city` VALUES ('513434', '越西县', '3');
INSERT INTO `t_city` VALUES ('513435', '甘洛县', '3');
INSERT INTO `t_city` VALUES ('513436', '美姑县', '3');
INSERT INTO `t_city` VALUES ('513437', '雷波县', '3');
INSERT INTO `t_city` VALUES ('520000', '贵州省', '1');
INSERT INTO `t_city` VALUES ('520100', '贵阳市', '2');
INSERT INTO `t_city` VALUES ('520101', '市辖区', '3');
INSERT INTO `t_city` VALUES ('520102', '南明区', '3');
INSERT INTO `t_city` VALUES ('520103', '云岩区', '3');
INSERT INTO `t_city` VALUES ('520111', '花溪区', '3');
INSERT INTO `t_city` VALUES ('520112', '乌当区', '3');
INSERT INTO `t_city` VALUES ('520113', '白云区', '3');
INSERT INTO `t_city` VALUES ('520115', '观山湖区', '3');
INSERT INTO `t_city` VALUES ('520121', '开阳县', '3');
INSERT INTO `t_city` VALUES ('520122', '息烽县', '3');
INSERT INTO `t_city` VALUES ('520123', '修文县', '3');
INSERT INTO `t_city` VALUES ('520181', '清镇市', '3');
INSERT INTO `t_city` VALUES ('520200', '六盘水市', '2');
INSERT INTO `t_city` VALUES ('520201', '钟山区', '3');
INSERT INTO `t_city` VALUES ('520203', '六枝特区', '3');
INSERT INTO `t_city` VALUES ('520221', '水城县', '3');
INSERT INTO `t_city` VALUES ('520222', '盘县', '3');
INSERT INTO `t_city` VALUES ('520300', '遵义市', '2');
INSERT INTO `t_city` VALUES ('520301', '市辖区', '3');
INSERT INTO `t_city` VALUES ('520302', '红花岗区', '3');
INSERT INTO `t_city` VALUES ('520303', '汇川区', '3');
INSERT INTO `t_city` VALUES ('520304', '播州区', '3');
INSERT INTO `t_city` VALUES ('520322', '桐梓县', '3');
INSERT INTO `t_city` VALUES ('520323', '绥阳县', '3');
INSERT INTO `t_city` VALUES ('520324', '正安县', '3');
INSERT INTO `t_city` VALUES ('520325', '道真仡佬族苗族自治县', '3');
INSERT INTO `t_city` VALUES ('520326', '务川仡佬族苗族自治县', '3');
INSERT INTO `t_city` VALUES ('520327', '凤冈县', '3');
INSERT INTO `t_city` VALUES ('520328', '湄潭县', '3');
INSERT INTO `t_city` VALUES ('520329', '余庆县', '3');
INSERT INTO `t_city` VALUES ('520330', '习水县', '3');
INSERT INTO `t_city` VALUES ('520381', '赤水市', '3');
INSERT INTO `t_city` VALUES ('520382', '仁怀市', '3');
INSERT INTO `t_city` VALUES ('520400', '安顺市', '2');
INSERT INTO `t_city` VALUES ('520401', '市辖区', '3');
INSERT INTO `t_city` VALUES ('520402', '西秀区', '3');
INSERT INTO `t_city` VALUES ('520403', '平坝区', '3');
INSERT INTO `t_city` VALUES ('520422', '普定县', '3');
INSERT INTO `t_city` VALUES ('520423', '镇宁布依族苗族自治县', '3');
INSERT INTO `t_city` VALUES ('520424', '关岭布依族苗族自治县', '3');
INSERT INTO `t_city` VALUES ('520425', '紫云苗族布依族自治县', '3');
INSERT INTO `t_city` VALUES ('520500', '毕节市', '2');
INSERT INTO `t_city` VALUES ('520501', '市辖区', '3');
INSERT INTO `t_city` VALUES ('520502', '七星关区', '3');
INSERT INTO `t_city` VALUES ('520521', '大方县', '3');
INSERT INTO `t_city` VALUES ('520522', '黔西县', '3');
INSERT INTO `t_city` VALUES ('520523', '金沙县', '3');
INSERT INTO `t_city` VALUES ('520524', '织金县', '3');
INSERT INTO `t_city` VALUES ('520525', '纳雍县', '3');
INSERT INTO `t_city` VALUES ('520526', '威宁彝族回族苗族自治县', '3');
INSERT INTO `t_city` VALUES ('520527', '赫章县', '3');
INSERT INTO `t_city` VALUES ('520600', '铜仁市', '2');
INSERT INTO `t_city` VALUES ('520601', '市辖区', '3');
INSERT INTO `t_city` VALUES ('520602', '碧江区', '3');
INSERT INTO `t_city` VALUES ('520603', '万山区', '3');
INSERT INTO `t_city` VALUES ('520621', '江口县', '3');
INSERT INTO `t_city` VALUES ('520622', '玉屏侗族自治县', '3');
INSERT INTO `t_city` VALUES ('520623', '石阡县', '3');
INSERT INTO `t_city` VALUES ('520624', '思南县', '3');
INSERT INTO `t_city` VALUES ('520625', '印江土家族苗族自治县', '3');
INSERT INTO `t_city` VALUES ('520626', '德江县', '3');
INSERT INTO `t_city` VALUES ('520627', '沿河土家族自治县', '3');
INSERT INTO `t_city` VALUES ('520628', '松桃苗族自治县', '3');
INSERT INTO `t_city` VALUES ('522300', '黔西南布依族苗族自治州', '2');
INSERT INTO `t_city` VALUES ('522301', '兴义市', '3');
INSERT INTO `t_city` VALUES ('522322', '兴仁县', '3');
INSERT INTO `t_city` VALUES ('522323', '普安县', '3');
INSERT INTO `t_city` VALUES ('522324', '晴隆县', '3');
INSERT INTO `t_city` VALUES ('522325', '贞丰县', '3');
INSERT INTO `t_city` VALUES ('522326', '望谟县', '3');
INSERT INTO `t_city` VALUES ('522327', '册亨县', '3');
INSERT INTO `t_city` VALUES ('522328', '安龙县', '3');
INSERT INTO `t_city` VALUES ('522600', '黔东南苗族侗族自治州', '2');
INSERT INTO `t_city` VALUES ('522601', '凯里市', '3');
INSERT INTO `t_city` VALUES ('522622', '黄平县', '3');
INSERT INTO `t_city` VALUES ('522623', '施秉县', '3');
INSERT INTO `t_city` VALUES ('522624', '三穗县', '3');
INSERT INTO `t_city` VALUES ('522625', '镇远县', '3');
INSERT INTO `t_city` VALUES ('522626', '岑巩县', '3');
INSERT INTO `t_city` VALUES ('522627', '天柱县', '3');
INSERT INTO `t_city` VALUES ('522628', '锦屏县', '3');
INSERT INTO `t_city` VALUES ('522629', '剑河县', '3');
INSERT INTO `t_city` VALUES ('522630', '台江县', '3');
INSERT INTO `t_city` VALUES ('522631', '黎平县', '3');
INSERT INTO `t_city` VALUES ('522632', '榕江县', '3');
INSERT INTO `t_city` VALUES ('522633', '从江县', '3');
INSERT INTO `t_city` VALUES ('522634', '雷山县', '3');
INSERT INTO `t_city` VALUES ('522635', '麻江县', '3');
INSERT INTO `t_city` VALUES ('522636', '丹寨县', '3');
INSERT INTO `t_city` VALUES ('522700', '黔南布依族苗族自治州', '2');
INSERT INTO `t_city` VALUES ('522701', '都匀市', '3');
INSERT INTO `t_city` VALUES ('522702', '福泉市', '3');
INSERT INTO `t_city` VALUES ('522722', '荔波县', '3');
INSERT INTO `t_city` VALUES ('522723', '贵定县', '3');
INSERT INTO `t_city` VALUES ('522725', '瓮安县', '3');
INSERT INTO `t_city` VALUES ('522726', '独山县', '3');
INSERT INTO `t_city` VALUES ('522727', '平塘县', '3');
INSERT INTO `t_city` VALUES ('522728', '罗甸县', '3');
INSERT INTO `t_city` VALUES ('522729', '长顺县', '3');
INSERT INTO `t_city` VALUES ('522730', '龙里县', '3');
INSERT INTO `t_city` VALUES ('522731', '惠水县', '3');
INSERT INTO `t_city` VALUES ('522732', '三都水族自治县', '3');
INSERT INTO `t_city` VALUES ('530000', '云南省', '1');
INSERT INTO `t_city` VALUES ('530100', '昆明市', '2');
INSERT INTO `t_city` VALUES ('530101', '市辖区', '3');
INSERT INTO `t_city` VALUES ('530102', '五华区', '3');
INSERT INTO `t_city` VALUES ('530103', '盘龙区', '3');
INSERT INTO `t_city` VALUES ('530111', '官渡区', '3');
INSERT INTO `t_city` VALUES ('530112', '西山区', '3');
INSERT INTO `t_city` VALUES ('530113', '东川区', '3');
INSERT INTO `t_city` VALUES ('530114', '呈贡区', '3');
INSERT INTO `t_city` VALUES ('530122', '晋宁县', '3');
INSERT INTO `t_city` VALUES ('530124', '富民县', '3');
INSERT INTO `t_city` VALUES ('530125', '宜良县', '3');
INSERT INTO `t_city` VALUES ('530126', '石林彝族自治县', '3');
INSERT INTO `t_city` VALUES ('530127', '嵩明县', '3');
INSERT INTO `t_city` VALUES ('530128', '禄劝彝族苗族自治县', '3');
INSERT INTO `t_city` VALUES ('530129', '寻甸回族彝族自治县', '3');
INSERT INTO `t_city` VALUES ('530181', '安宁市', '3');
INSERT INTO `t_city` VALUES ('530300', '曲靖市', '2');
INSERT INTO `t_city` VALUES ('530301', '市辖区', '3');
INSERT INTO `t_city` VALUES ('530302', '麒麟区', '3');
INSERT INTO `t_city` VALUES ('530303', '沾益区', '3');
INSERT INTO `t_city` VALUES ('530321', '马龙县', '3');
INSERT INTO `t_city` VALUES ('530322', '陆良县', '3');
INSERT INTO `t_city` VALUES ('530323', '师宗县', '3');
INSERT INTO `t_city` VALUES ('530324', '罗平县', '3');
INSERT INTO `t_city` VALUES ('530325', '富源县', '3');
INSERT INTO `t_city` VALUES ('530326', '会泽县', '3');
INSERT INTO `t_city` VALUES ('530381', '宣威市', '3');
INSERT INTO `t_city` VALUES ('530400', '玉溪市', '2');
INSERT INTO `t_city` VALUES ('530401', '市辖区', '3');
INSERT INTO `t_city` VALUES ('530402', '红塔区', '3');
INSERT INTO `t_city` VALUES ('530403', '江川区', '3');
INSERT INTO `t_city` VALUES ('530422', '澄江县', '3');
INSERT INTO `t_city` VALUES ('530423', '通海县', '3');
INSERT INTO `t_city` VALUES ('530424', '华宁县', '3');
INSERT INTO `t_city` VALUES ('530425', '易门县', '3');
INSERT INTO `t_city` VALUES ('530426', '峨山彝族自治县', '3');
INSERT INTO `t_city` VALUES ('530427', '新平彝族傣族自治县', '3');
INSERT INTO `t_city` VALUES ('530428', '元江哈尼族彝族傣族自治县', '3');
INSERT INTO `t_city` VALUES ('530500', '保山市', '2');
INSERT INTO `t_city` VALUES ('530501', '市辖区', '3');
INSERT INTO `t_city` VALUES ('530502', '隆阳区', '3');
INSERT INTO `t_city` VALUES ('530521', '施甸县', '3');
INSERT INTO `t_city` VALUES ('530523', '龙陵县', '3');
INSERT INTO `t_city` VALUES ('530524', '昌宁县', '3');
INSERT INTO `t_city` VALUES ('530581', '腾冲市', '3');
INSERT INTO `t_city` VALUES ('530600', '昭通市', '2');
INSERT INTO `t_city` VALUES ('530601', '市辖区', '3');
INSERT INTO `t_city` VALUES ('530602', '昭阳区', '3');
INSERT INTO `t_city` VALUES ('530621', '鲁甸县', '3');
INSERT INTO `t_city` VALUES ('530622', '巧家县', '3');
INSERT INTO `t_city` VALUES ('530623', '盐津县', '3');
INSERT INTO `t_city` VALUES ('530624', '大关县', '3');
INSERT INTO `t_city` VALUES ('530625', '永善县', '3');
INSERT INTO `t_city` VALUES ('530626', '绥江县', '3');
INSERT INTO `t_city` VALUES ('530627', '镇雄县', '3');
INSERT INTO `t_city` VALUES ('530628', '彝良县', '3');
INSERT INTO `t_city` VALUES ('530629', '威信县', '3');
INSERT INTO `t_city` VALUES ('530630', '水富县', '3');
INSERT INTO `t_city` VALUES ('530700', '丽江市', '2');
INSERT INTO `t_city` VALUES ('530701', '市辖区', '3');
INSERT INTO `t_city` VALUES ('530702', '古城区', '3');
INSERT INTO `t_city` VALUES ('530721', '玉龙纳西族自治县', '3');
INSERT INTO `t_city` VALUES ('530722', '永胜县', '3');
INSERT INTO `t_city` VALUES ('530723', '华坪县', '3');
INSERT INTO `t_city` VALUES ('530724', '宁蒗彝族自治县', '3');
INSERT INTO `t_city` VALUES ('530800', '普洱市', '2');
INSERT INTO `t_city` VALUES ('530801', '市辖区', '3');
INSERT INTO `t_city` VALUES ('530802', '思茅区', '3');
INSERT INTO `t_city` VALUES ('530821', '宁洱哈尼族彝族自治县', '3');
INSERT INTO `t_city` VALUES ('530822', '墨江哈尼族自治县', '3');
INSERT INTO `t_city` VALUES ('530823', '景东彝族自治县', '3');
INSERT INTO `t_city` VALUES ('530824', '景谷傣族彝族自治县', '3');
INSERT INTO `t_city` VALUES ('530825', '镇沅彝族哈尼族拉祜族自治县', '3');
INSERT INTO `t_city` VALUES ('530826', '江城哈尼族彝族自治县', '3');
INSERT INTO `t_city` VALUES ('530827', '孟连傣族拉祜族佤族自治县', '3');
INSERT INTO `t_city` VALUES ('530828', '澜沧拉祜族自治县', '3');
INSERT INTO `t_city` VALUES ('530829', '西盟佤族自治县', '3');
INSERT INTO `t_city` VALUES ('530900', '临沧市', '2');
INSERT INTO `t_city` VALUES ('530901', '市辖区', '3');
INSERT INTO `t_city` VALUES ('530902', '临翔区', '3');
INSERT INTO `t_city` VALUES ('530921', '凤庆县', '3');
INSERT INTO `t_city` VALUES ('530922', '云县', '3');
INSERT INTO `t_city` VALUES ('530923', '永德县', '3');
INSERT INTO `t_city` VALUES ('530924', '镇康县', '3');
INSERT INTO `t_city` VALUES ('530925', '双江拉祜族佤族布朗族傣族自治县', '3');
INSERT INTO `t_city` VALUES ('530926', '耿马傣族佤族自治县', '3');
INSERT INTO `t_city` VALUES ('530927', '沧源佤族自治县', '3');
INSERT INTO `t_city` VALUES ('532300', '楚雄彝族自治州', '2');
INSERT INTO `t_city` VALUES ('532301', '楚雄市', '3');
INSERT INTO `t_city` VALUES ('532322', '双柏县', '3');
INSERT INTO `t_city` VALUES ('532323', '牟定县', '3');
INSERT INTO `t_city` VALUES ('532324', '南华县', '3');
INSERT INTO `t_city` VALUES ('532325', '姚安县', '3');
INSERT INTO `t_city` VALUES ('532326', '大姚县', '3');
INSERT INTO `t_city` VALUES ('532327', '永仁县', '3');
INSERT INTO `t_city` VALUES ('532328', '元谋县', '3');
INSERT INTO `t_city` VALUES ('532329', '武定县', '3');
INSERT INTO `t_city` VALUES ('532331', '禄丰县', '3');
INSERT INTO `t_city` VALUES ('532500', '红河哈尼族彝族自治州', '2');
INSERT INTO `t_city` VALUES ('532501', '个旧市', '3');
INSERT INTO `t_city` VALUES ('532502', '开远市', '3');
INSERT INTO `t_city` VALUES ('532503', '蒙自市', '3');
INSERT INTO `t_city` VALUES ('532504', '弥勒市', '3');
INSERT INTO `t_city` VALUES ('532523', '屏边苗族自治县', '3');
INSERT INTO `t_city` VALUES ('532524', '建水县', '3');
INSERT INTO `t_city` VALUES ('532525', '石屏县', '3');
INSERT INTO `t_city` VALUES ('532527', '泸西县', '3');
INSERT INTO `t_city` VALUES ('532528', '元阳县', '3');
INSERT INTO `t_city` VALUES ('532529', '红河县', '3');
INSERT INTO `t_city` VALUES ('532530', '金平苗族瑶族傣族自治县', '3');
INSERT INTO `t_city` VALUES ('532531', '绿春县', '3');
INSERT INTO `t_city` VALUES ('532532', '河口瑶族自治县', '3');
INSERT INTO `t_city` VALUES ('532600', '文山壮族苗族自治州', '2');
INSERT INTO `t_city` VALUES ('532601', '文山市', '3');
INSERT INTO `t_city` VALUES ('532622', '砚山县', '3');
INSERT INTO `t_city` VALUES ('532623', '西畴县', '3');
INSERT INTO `t_city` VALUES ('532624', '麻栗坡县', '3');
INSERT INTO `t_city` VALUES ('532625', '马关县', '3');
INSERT INTO `t_city` VALUES ('532626', '丘北县', '3');
INSERT INTO `t_city` VALUES ('532627', '广南县', '3');
INSERT INTO `t_city` VALUES ('532628', '富宁县', '3');
INSERT INTO `t_city` VALUES ('532800', '西双版纳傣族自治州', '2');
INSERT INTO `t_city` VALUES ('532801', '景洪市', '3');
INSERT INTO `t_city` VALUES ('532822', '勐海县', '3');
INSERT INTO `t_city` VALUES ('532823', '勐腊县', '3');
INSERT INTO `t_city` VALUES ('532900', '大理白族自治州', '2');
INSERT INTO `t_city` VALUES ('532901', '大理市', '3');
INSERT INTO `t_city` VALUES ('532922', '漾濞彝族自治县', '3');
INSERT INTO `t_city` VALUES ('532923', '祥云县', '3');
INSERT INTO `t_city` VALUES ('532924', '宾川县', '3');
INSERT INTO `t_city` VALUES ('532925', '弥渡县', '3');
INSERT INTO `t_city` VALUES ('532926', '南涧彝族自治县', '3');
INSERT INTO `t_city` VALUES ('532927', '巍山彝族回族自治县', '3');
INSERT INTO `t_city` VALUES ('532928', '永平县', '3');
INSERT INTO `t_city` VALUES ('532929', '云龙县', '3');
INSERT INTO `t_city` VALUES ('532930', '洱源县', '3');
INSERT INTO `t_city` VALUES ('532931', '剑川县', '3');
INSERT INTO `t_city` VALUES ('532932', '鹤庆县', '3');
INSERT INTO `t_city` VALUES ('533100', '德宏傣族景颇族自治州', '2');
INSERT INTO `t_city` VALUES ('533102', '瑞丽市', '3');
INSERT INTO `t_city` VALUES ('533103', '芒市', '3');
INSERT INTO `t_city` VALUES ('533122', '梁河县', '3');
INSERT INTO `t_city` VALUES ('533123', '盈江县', '3');
INSERT INTO `t_city` VALUES ('533124', '陇川县', '3');
INSERT INTO `t_city` VALUES ('533300', '怒江傈僳族自治州', '2');
INSERT INTO `t_city` VALUES ('533301', '泸水市', '3');
INSERT INTO `t_city` VALUES ('533323', '福贡县', '3');
INSERT INTO `t_city` VALUES ('533324', '贡山独龙族怒族自治县', '3');
INSERT INTO `t_city` VALUES ('533325', '兰坪白族普米族自治县', '3');
INSERT INTO `t_city` VALUES ('533400', '迪庆藏族自治州', '2');
INSERT INTO `t_city` VALUES ('533401', '香格里拉市', '3');
INSERT INTO `t_city` VALUES ('533422', '德钦县', '3');
INSERT INTO `t_city` VALUES ('533423', '维西傈僳族自治县', '3');
INSERT INTO `t_city` VALUES ('540000', '西藏自治区', '1');
INSERT INTO `t_city` VALUES ('540100', '拉萨市', '2');
INSERT INTO `t_city` VALUES ('540101', '市辖区', '3');
INSERT INTO `t_city` VALUES ('540102', '城关区', '3');
INSERT INTO `t_city` VALUES ('540103', '堆龙德庆区', '3');
INSERT INTO `t_city` VALUES ('540121', '林周县', '3');
INSERT INTO `t_city` VALUES ('540122', '当雄县', '3');
INSERT INTO `t_city` VALUES ('540123', '尼木县', '3');
INSERT INTO `t_city` VALUES ('540124', '曲水县', '3');
INSERT INTO `t_city` VALUES ('540126', '达孜县', '3');
INSERT INTO `t_city` VALUES ('540127', '墨竹工卡县', '3');
INSERT INTO `t_city` VALUES ('540200', '日喀则市', '2');
INSERT INTO `t_city` VALUES ('540202', '桑珠孜区', '3');
INSERT INTO `t_city` VALUES ('540221', '南木林县', '3');
INSERT INTO `t_city` VALUES ('540222', '江孜县', '3');
INSERT INTO `t_city` VALUES ('540223', '定日县', '3');
INSERT INTO `t_city` VALUES ('540224', '萨迦县', '3');
INSERT INTO `t_city` VALUES ('540225', '拉孜县', '3');
INSERT INTO `t_city` VALUES ('540226', '昂仁县', '3');
INSERT INTO `t_city` VALUES ('540227', '谢通门县', '3');
INSERT INTO `t_city` VALUES ('540228', '白朗县', '3');
INSERT INTO `t_city` VALUES ('540229', '仁布县', '3');
INSERT INTO `t_city` VALUES ('540230', '康马县', '3');
INSERT INTO `t_city` VALUES ('540231', '定结县', '3');
INSERT INTO `t_city` VALUES ('540232', '仲巴县', '3');
INSERT INTO `t_city` VALUES ('540233', '亚东县', '3');
INSERT INTO `t_city` VALUES ('540234', '吉隆县', '3');
INSERT INTO `t_city` VALUES ('540235', '聂拉木县', '3');
INSERT INTO `t_city` VALUES ('540236', '萨嘎县', '3');
INSERT INTO `t_city` VALUES ('540237', '岗巴县', '3');
INSERT INTO `t_city` VALUES ('540300', '昌都市', '2');
INSERT INTO `t_city` VALUES ('540302', '卡若区', '3');
INSERT INTO `t_city` VALUES ('540321', '江达县', '3');
INSERT INTO `t_city` VALUES ('540322', '贡觉县', '3');
INSERT INTO `t_city` VALUES ('540323', '类乌齐县', '3');
INSERT INTO `t_city` VALUES ('540324', '丁青县', '3');
INSERT INTO `t_city` VALUES ('540325', '察雅县', '3');
INSERT INTO `t_city` VALUES ('540326', '八宿县', '3');
INSERT INTO `t_city` VALUES ('540327', '左贡县', '3');
INSERT INTO `t_city` VALUES ('540328', '芒康县', '3');
INSERT INTO `t_city` VALUES ('540329', '洛隆县', '3');
INSERT INTO `t_city` VALUES ('540330', '边坝县', '3');
INSERT INTO `t_city` VALUES ('540400', '林芝市', '2');
INSERT INTO `t_city` VALUES ('540402', '巴宜区', '3');
INSERT INTO `t_city` VALUES ('540421', '工布江达县', '3');
INSERT INTO `t_city` VALUES ('540422', '米林县', '3');
INSERT INTO `t_city` VALUES ('540423', '墨脱县', '3');
INSERT INTO `t_city` VALUES ('540424', '波密县', '3');
INSERT INTO `t_city` VALUES ('540425', '察隅县', '3');
INSERT INTO `t_city` VALUES ('540426', '朗县', '3');
INSERT INTO `t_city` VALUES ('540500', '山南市', '2');
INSERT INTO `t_city` VALUES ('540501', '市辖区', '3');
INSERT INTO `t_city` VALUES ('540502', '乃东区', '3');
INSERT INTO `t_city` VALUES ('540521', '扎囊县', '3');
INSERT INTO `t_city` VALUES ('540522', '贡嘎县', '3');
INSERT INTO `t_city` VALUES ('540523', '桑日县', '3');
INSERT INTO `t_city` VALUES ('540524', '琼结县', '3');
INSERT INTO `t_city` VALUES ('540525', '曲松县', '3');
INSERT INTO `t_city` VALUES ('540526', '措美县', '3');
INSERT INTO `t_city` VALUES ('540527', '洛扎县', '3');
INSERT INTO `t_city` VALUES ('540528', '加查县', '3');
INSERT INTO `t_city` VALUES ('540529', '隆子县', '3');
INSERT INTO `t_city` VALUES ('540530', '错那县', '3');
INSERT INTO `t_city` VALUES ('540531', '浪卡子县', '3');
INSERT INTO `t_city` VALUES ('542400', '那曲地区', '2');
INSERT INTO `t_city` VALUES ('542421', '那曲县', '3');
INSERT INTO `t_city` VALUES ('542422', '嘉黎县', '3');
INSERT INTO `t_city` VALUES ('542423', '比如县', '3');
INSERT INTO `t_city` VALUES ('542424', '聂荣县', '3');
INSERT INTO `t_city` VALUES ('542425', '安多县', '3');
INSERT INTO `t_city` VALUES ('542426', '申扎县', '3');
INSERT INTO `t_city` VALUES ('542427', '索县', '3');
INSERT INTO `t_city` VALUES ('542428', '班戈县', '3');
INSERT INTO `t_city` VALUES ('542429', '巴青县', '3');
INSERT INTO `t_city` VALUES ('542430', '尼玛县', '3');
INSERT INTO `t_city` VALUES ('542431', '双湖县', '3');
INSERT INTO `t_city` VALUES ('542500', '阿里地区', '2');
INSERT INTO `t_city` VALUES ('542521', '普兰县', '3');
INSERT INTO `t_city` VALUES ('542522', '札达县', '3');
INSERT INTO `t_city` VALUES ('542523', '噶尔县', '3');
INSERT INTO `t_city` VALUES ('542524', '日土县', '3');
INSERT INTO `t_city` VALUES ('542525', '革吉县', '3');
INSERT INTO `t_city` VALUES ('542526', '改则县', '3');
INSERT INTO `t_city` VALUES ('542527', '措勤县', '3');
INSERT INTO `t_city` VALUES ('610000', '陕西省', '1');
INSERT INTO `t_city` VALUES ('610100', '西安市', '2');
INSERT INTO `t_city` VALUES ('610101', '市辖区', '3');
INSERT INTO `t_city` VALUES ('610102', '新城区', '3');
INSERT INTO `t_city` VALUES ('610103', '碑林区', '3');
INSERT INTO `t_city` VALUES ('610104', '莲湖区', '3');
INSERT INTO `t_city` VALUES ('610111', '灞桥区', '3');
INSERT INTO `t_city` VALUES ('610112', '未央区', '3');
INSERT INTO `t_city` VALUES ('610113', '雁塔区', '3');
INSERT INTO `t_city` VALUES ('610114', '阎良区', '3');
INSERT INTO `t_city` VALUES ('610115', '临潼区', '3');
INSERT INTO `t_city` VALUES ('610116', '长安区', '3');
INSERT INTO `t_city` VALUES ('610117', '高陵区', '3');
INSERT INTO `t_city` VALUES ('610122', '蓝田县', '3');
INSERT INTO `t_city` VALUES ('610124', '周至县', '3');
INSERT INTO `t_city` VALUES ('610125', '户县', '3');
INSERT INTO `t_city` VALUES ('610200', '铜川市', '2');
INSERT INTO `t_city` VALUES ('610201', '市辖区', '3');
INSERT INTO `t_city` VALUES ('610202', '王益区', '3');
INSERT INTO `t_city` VALUES ('610203', '印台区', '3');
INSERT INTO `t_city` VALUES ('610204', '耀州区', '3');
INSERT INTO `t_city` VALUES ('610222', '宜君县', '3');
INSERT INTO `t_city` VALUES ('610300', '宝鸡市', '2');
INSERT INTO `t_city` VALUES ('610301', '市辖区', '3');
INSERT INTO `t_city` VALUES ('610302', '渭滨区', '3');
INSERT INTO `t_city` VALUES ('610303', '金台区', '3');
INSERT INTO `t_city` VALUES ('610304', '陈仓区', '3');
INSERT INTO `t_city` VALUES ('610322', '凤翔县', '3');
INSERT INTO `t_city` VALUES ('610323', '岐山县', '3');
INSERT INTO `t_city` VALUES ('610324', '扶风县', '3');
INSERT INTO `t_city` VALUES ('610326', '眉县', '3');
INSERT INTO `t_city` VALUES ('610327', '陇县', '3');
INSERT INTO `t_city` VALUES ('610328', '千阳县', '3');
INSERT INTO `t_city` VALUES ('610329', '麟游县', '3');
INSERT INTO `t_city` VALUES ('610330', '凤县', '3');
INSERT INTO `t_city` VALUES ('610331', '太白县', '3');
INSERT INTO `t_city` VALUES ('610400', '咸阳市', '2');
INSERT INTO `t_city` VALUES ('610401', '市辖区', '3');
INSERT INTO `t_city` VALUES ('610402', '秦都区', '3');
INSERT INTO `t_city` VALUES ('610403', '杨陵区', '3');
INSERT INTO `t_city` VALUES ('610404', '渭城区', '3');
INSERT INTO `t_city` VALUES ('610422', '三原县', '3');
INSERT INTO `t_city` VALUES ('610423', '泾阳县', '3');
INSERT INTO `t_city` VALUES ('610424', '乾县', '3');
INSERT INTO `t_city` VALUES ('610425', '礼泉县', '3');
INSERT INTO `t_city` VALUES ('610426', '永寿县', '3');
INSERT INTO `t_city` VALUES ('610427', '彬县', '3');
INSERT INTO `t_city` VALUES ('610428', '长武县', '3');
INSERT INTO `t_city` VALUES ('610429', '旬邑县', '3');
INSERT INTO `t_city` VALUES ('610430', '淳化县', '3');
INSERT INTO `t_city` VALUES ('610431', '武功县', '3');
INSERT INTO `t_city` VALUES ('610481', '兴平市', '3');
INSERT INTO `t_city` VALUES ('610500', '渭南市', '2');
INSERT INTO `t_city` VALUES ('610501', '市辖区', '3');
INSERT INTO `t_city` VALUES ('610502', '临渭区', '3');
INSERT INTO `t_city` VALUES ('610503', '华州区', '3');
INSERT INTO `t_city` VALUES ('610522', '潼关县', '3');
INSERT INTO `t_city` VALUES ('610523', '大荔县', '3');
INSERT INTO `t_city` VALUES ('610524', '合阳县', '3');
INSERT INTO `t_city` VALUES ('610525', '澄城县', '3');
INSERT INTO `t_city` VALUES ('610526', '蒲城县', '3');
INSERT INTO `t_city` VALUES ('610527', '白水县', '3');
INSERT INTO `t_city` VALUES ('610528', '富平县', '3');
INSERT INTO `t_city` VALUES ('610581', '韩城市', '3');
INSERT INTO `t_city` VALUES ('610582', '华阴市', '3');
INSERT INTO `t_city` VALUES ('610600', '延安市', '2');
INSERT INTO `t_city` VALUES ('610601', '市辖区', '3');
INSERT INTO `t_city` VALUES ('610602', '宝塔区', '3');
INSERT INTO `t_city` VALUES ('610603', '安塞区', '3');
INSERT INTO `t_city` VALUES ('610621', '延长县', '3');
INSERT INTO `t_city` VALUES ('610622', '延川县', '3');
INSERT INTO `t_city` VALUES ('610623', '子长县', '3');
INSERT INTO `t_city` VALUES ('610625', '志丹县', '3');
INSERT INTO `t_city` VALUES ('610626', '吴起县', '3');
INSERT INTO `t_city` VALUES ('610627', '甘泉县', '3');
INSERT INTO `t_city` VALUES ('610628', '富县', '3');
INSERT INTO `t_city` VALUES ('610629', '洛川县', '3');
INSERT INTO `t_city` VALUES ('610630', '宜川县', '3');
INSERT INTO `t_city` VALUES ('610631', '黄龙县', '3');
INSERT INTO `t_city` VALUES ('610632', '黄陵县', '3');
INSERT INTO `t_city` VALUES ('610700', '汉中市', '2');
INSERT INTO `t_city` VALUES ('610701', '市辖区', '3');
INSERT INTO `t_city` VALUES ('610702', '汉台区', '3');
INSERT INTO `t_city` VALUES ('610721', '南郑县', '3');
INSERT INTO `t_city` VALUES ('610722', '城固县', '3');
INSERT INTO `t_city` VALUES ('610723', '洋县', '3');
INSERT INTO `t_city` VALUES ('610724', '西乡县', '3');
INSERT INTO `t_city` VALUES ('610725', '勉县', '3');
INSERT INTO `t_city` VALUES ('610726', '宁强县', '3');
INSERT INTO `t_city` VALUES ('610727', '略阳县', '3');
INSERT INTO `t_city` VALUES ('610728', '镇巴县', '3');
INSERT INTO `t_city` VALUES ('610729', '留坝县', '3');
INSERT INTO `t_city` VALUES ('610730', '佛坪县', '3');
INSERT INTO `t_city` VALUES ('610800', '榆林市', '2');
INSERT INTO `t_city` VALUES ('610801', '市辖区', '3');
INSERT INTO `t_city` VALUES ('610802', '榆阳区', '3');
INSERT INTO `t_city` VALUES ('610803', '横山区', '3');
INSERT INTO `t_city` VALUES ('610821', '神木县', '3');
INSERT INTO `t_city` VALUES ('610822', '府谷县', '3');
INSERT INTO `t_city` VALUES ('610824', '靖边县', '3');
INSERT INTO `t_city` VALUES ('610825', '定边县', '3');
INSERT INTO `t_city` VALUES ('610826', '绥德县', '3');
INSERT INTO `t_city` VALUES ('610827', '米脂县', '3');
INSERT INTO `t_city` VALUES ('610828', '佳县', '3');
INSERT INTO `t_city` VALUES ('610829', '吴堡县', '3');
INSERT INTO `t_city` VALUES ('610830', '清涧县', '3');
INSERT INTO `t_city` VALUES ('610831', '子洲县', '3');
INSERT INTO `t_city` VALUES ('610900', '安康市', '2');
INSERT INTO `t_city` VALUES ('610901', '市辖区', '3');
INSERT INTO `t_city` VALUES ('610902', '汉滨区', '3');
INSERT INTO `t_city` VALUES ('610921', '汉阴县', '3');
INSERT INTO `t_city` VALUES ('610922', '石泉县', '3');
INSERT INTO `t_city` VALUES ('610923', '宁陕县', '3');
INSERT INTO `t_city` VALUES ('610924', '紫阳县', '3');
INSERT INTO `t_city` VALUES ('610925', '岚皋县', '3');
INSERT INTO `t_city` VALUES ('610926', '平利县', '3');
INSERT INTO `t_city` VALUES ('610927', '镇坪县', '3');
INSERT INTO `t_city` VALUES ('610928', '旬阳县', '3');
INSERT INTO `t_city` VALUES ('610929', '白河县', '3');
INSERT INTO `t_city` VALUES ('611000', '商洛市', '2');
INSERT INTO `t_city` VALUES ('611001', '市辖区', '3');
INSERT INTO `t_city` VALUES ('611002', '商州区', '3');
INSERT INTO `t_city` VALUES ('611021', '洛南县', '3');
INSERT INTO `t_city` VALUES ('611022', '丹凤县', '3');
INSERT INTO `t_city` VALUES ('611023', '商南县', '3');
INSERT INTO `t_city` VALUES ('611024', '山阳县', '3');
INSERT INTO `t_city` VALUES ('611025', '镇安县', '3');
INSERT INTO `t_city` VALUES ('611026', '柞水县', '3');
INSERT INTO `t_city` VALUES ('620000', '甘肃省', '1');
INSERT INTO `t_city` VALUES ('620100', '兰州市', '2');
INSERT INTO `t_city` VALUES ('620101', '市辖区', '3');
INSERT INTO `t_city` VALUES ('620102', '城关区', '3');
INSERT INTO `t_city` VALUES ('620103', '七里河区', '3');
INSERT INTO `t_city` VALUES ('620104', '西固区', '3');
INSERT INTO `t_city` VALUES ('620105', '安宁区', '3');
INSERT INTO `t_city` VALUES ('620111', '红古区', '3');
INSERT INTO `t_city` VALUES ('620121', '永登县', '3');
INSERT INTO `t_city` VALUES ('620122', '皋兰县', '3');
INSERT INTO `t_city` VALUES ('620123', '榆中县', '3');
INSERT INTO `t_city` VALUES ('620200', '嘉峪关市', '2');
INSERT INTO `t_city` VALUES ('620201', '市辖区', '3');
INSERT INTO `t_city` VALUES ('620300', '金昌市', '2');
INSERT INTO `t_city` VALUES ('620301', '市辖区', '3');
INSERT INTO `t_city` VALUES ('620302', '金川区', '3');
INSERT INTO `t_city` VALUES ('620321', '永昌县', '3');
INSERT INTO `t_city` VALUES ('620400', '白银市', '2');
INSERT INTO `t_city` VALUES ('620401', '市辖区', '3');
INSERT INTO `t_city` VALUES ('620402', '白银区', '3');
INSERT INTO `t_city` VALUES ('620403', '平川区', '3');
INSERT INTO `t_city` VALUES ('620421', '靖远县', '3');
INSERT INTO `t_city` VALUES ('620422', '会宁县', '3');
INSERT INTO `t_city` VALUES ('620423', '景泰县', '3');
INSERT INTO `t_city` VALUES ('620500', '天水市', '2');
INSERT INTO `t_city` VALUES ('620501', '市辖区', '3');
INSERT INTO `t_city` VALUES ('620502', '秦州区', '3');
INSERT INTO `t_city` VALUES ('620503', '麦积区', '3');
INSERT INTO `t_city` VALUES ('620521', '清水县', '3');
INSERT INTO `t_city` VALUES ('620522', '秦安县', '3');
INSERT INTO `t_city` VALUES ('620523', '甘谷县', '3');
INSERT INTO `t_city` VALUES ('620524', '武山县', '3');
INSERT INTO `t_city` VALUES ('620525', '张家川回族自治县', '3');
INSERT INTO `t_city` VALUES ('620600', '武威市', '2');
INSERT INTO `t_city` VALUES ('620601', '市辖区', '3');
INSERT INTO `t_city` VALUES ('620602', '凉州区', '3');
INSERT INTO `t_city` VALUES ('620621', '民勤县', '3');
INSERT INTO `t_city` VALUES ('620622', '古浪县', '3');
INSERT INTO `t_city` VALUES ('620623', '天祝藏族自治县', '3');
INSERT INTO `t_city` VALUES ('620700', '张掖市', '2');
INSERT INTO `t_city` VALUES ('620701', '市辖区', '3');
INSERT INTO `t_city` VALUES ('620702', '甘州区', '3');
INSERT INTO `t_city` VALUES ('620721', '肃南裕固族自治县', '3');
INSERT INTO `t_city` VALUES ('620722', '民乐县', '3');
INSERT INTO `t_city` VALUES ('620723', '临泽县', '3');
INSERT INTO `t_city` VALUES ('620724', '高台县', '3');
INSERT INTO `t_city` VALUES ('620725', '山丹县', '3');
INSERT INTO `t_city` VALUES ('620800', '平凉市', '2');
INSERT INTO `t_city` VALUES ('620801', '市辖区', '3');
INSERT INTO `t_city` VALUES ('620802', '崆峒区', '3');
INSERT INTO `t_city` VALUES ('620821', '泾川县', '3');
INSERT INTO `t_city` VALUES ('620822', '灵台县', '3');
INSERT INTO `t_city` VALUES ('620823', '崇信县', '3');
INSERT INTO `t_city` VALUES ('620824', '华亭县', '3');
INSERT INTO `t_city` VALUES ('620825', '庄浪县', '3');
INSERT INTO `t_city` VALUES ('620826', '静宁县', '3');
INSERT INTO `t_city` VALUES ('620900', '酒泉市', '2');
INSERT INTO `t_city` VALUES ('620901', '市辖区', '3');
INSERT INTO `t_city` VALUES ('620902', '肃州区', '3');
INSERT INTO `t_city` VALUES ('620921', '金塔县', '3');
INSERT INTO `t_city` VALUES ('620922', '瓜州县', '3');
INSERT INTO `t_city` VALUES ('620923', '肃北蒙古族自治县', '3');
INSERT INTO `t_city` VALUES ('620924', '阿克塞哈萨克族自治县', '3');
INSERT INTO `t_city` VALUES ('620981', '玉门市', '3');
INSERT INTO `t_city` VALUES ('620982', '敦煌市', '3');
INSERT INTO `t_city` VALUES ('621000', '庆阳市', '2');
INSERT INTO `t_city` VALUES ('621001', '市辖区', '3');
INSERT INTO `t_city` VALUES ('621002', '西峰区', '3');
INSERT INTO `t_city` VALUES ('621021', '庆城县', '3');
INSERT INTO `t_city` VALUES ('621022', '环县', '3');
INSERT INTO `t_city` VALUES ('621023', '华池县', '3');
INSERT INTO `t_city` VALUES ('621024', '合水县', '3');
INSERT INTO `t_city` VALUES ('621025', '正宁县', '3');
INSERT INTO `t_city` VALUES ('621026', '宁县', '3');
INSERT INTO `t_city` VALUES ('621027', '镇原县', '3');
INSERT INTO `t_city` VALUES ('621100', '定西市', '2');
INSERT INTO `t_city` VALUES ('621101', '市辖区', '3');
INSERT INTO `t_city` VALUES ('621102', '安定区', '3');
INSERT INTO `t_city` VALUES ('621121', '通渭县', '3');
INSERT INTO `t_city` VALUES ('621122', '陇西县', '3');
INSERT INTO `t_city` VALUES ('621123', '渭源县', '3');
INSERT INTO `t_city` VALUES ('621124', '临洮县', '3');
INSERT INTO `t_city` VALUES ('621125', '漳县', '3');
INSERT INTO `t_city` VALUES ('621126', '岷县', '3');
INSERT INTO `t_city` VALUES ('621200', '陇南市', '2');
INSERT INTO `t_city` VALUES ('621201', '市辖区', '3');
INSERT INTO `t_city` VALUES ('621202', '武都区', '3');
INSERT INTO `t_city` VALUES ('621221', '成县', '3');
INSERT INTO `t_city` VALUES ('621222', '文县', '3');
INSERT INTO `t_city` VALUES ('621223', '宕昌县', '3');
INSERT INTO `t_city` VALUES ('621224', '康县', '3');
INSERT INTO `t_city` VALUES ('621225', '西和县', '3');
INSERT INTO `t_city` VALUES ('621226', '礼县', '3');
INSERT INTO `t_city` VALUES ('621227', '徽县', '3');
INSERT INTO `t_city` VALUES ('621228', '两当县', '3');
INSERT INTO `t_city` VALUES ('622900', '临夏回族自治州', '2');
INSERT INTO `t_city` VALUES ('622901', '临夏市', '3');
INSERT INTO `t_city` VALUES ('622921', '临夏县', '3');
INSERT INTO `t_city` VALUES ('622922', '康乐县', '3');
INSERT INTO `t_city` VALUES ('622923', '永靖县', '3');
INSERT INTO `t_city` VALUES ('622924', '广河县', '3');
INSERT INTO `t_city` VALUES ('622925', '和政县', '3');
INSERT INTO `t_city` VALUES ('622926', '东乡族自治县', '3');
INSERT INTO `t_city` VALUES ('622927', '积石山保安族东乡族撒拉族自治县', '3');
INSERT INTO `t_city` VALUES ('623000', '甘南藏族自治州', '2');
INSERT INTO `t_city` VALUES ('623001', '合作市', '3');
INSERT INTO `t_city` VALUES ('623021', '临潭县', '3');
INSERT INTO `t_city` VALUES ('623022', '卓尼县', '3');
INSERT INTO `t_city` VALUES ('623023', '舟曲县', '3');
INSERT INTO `t_city` VALUES ('623024', '迭部县', '3');
INSERT INTO `t_city` VALUES ('623025', '玛曲县', '3');
INSERT INTO `t_city` VALUES ('623026', '碌曲县', '3');
INSERT INTO `t_city` VALUES ('623027', '夏河县', '3');
INSERT INTO `t_city` VALUES ('630000', '青海省', '1');
INSERT INTO `t_city` VALUES ('630100', '西宁市', '2');
INSERT INTO `t_city` VALUES ('630101', '市辖区', '3');
INSERT INTO `t_city` VALUES ('630102', '城东区', '3');
INSERT INTO `t_city` VALUES ('630103', '城中区', '3');
INSERT INTO `t_city` VALUES ('630104', '城西区', '3');
INSERT INTO `t_city` VALUES ('630105', '城北区', '3');
INSERT INTO `t_city` VALUES ('630121', '大通回族土族自治县', '3');
INSERT INTO `t_city` VALUES ('630122', '湟中县', '3');
INSERT INTO `t_city` VALUES ('630123', '湟源县', '3');
INSERT INTO `t_city` VALUES ('630200', '海东市', '2');
INSERT INTO `t_city` VALUES ('630202', '乐都区', '3');
INSERT INTO `t_city` VALUES ('630203', '平安区', '3');
INSERT INTO `t_city` VALUES ('630222', '民和回族土族自治县', '3');
INSERT INTO `t_city` VALUES ('630223', '互助土族自治县', '3');
INSERT INTO `t_city` VALUES ('630224', '化隆回族自治县', '3');
INSERT INTO `t_city` VALUES ('630225', '循化撒拉族自治县', '3');
INSERT INTO `t_city` VALUES ('632200', '海北藏族自治州', '2');
INSERT INTO `t_city` VALUES ('632221', '门源回族自治县', '3');
INSERT INTO `t_city` VALUES ('632222', '祁连县', '3');
INSERT INTO `t_city` VALUES ('632223', '海晏县', '3');
INSERT INTO `t_city` VALUES ('632224', '刚察县', '3');
INSERT INTO `t_city` VALUES ('632300', '黄南藏族自治州', '2');
INSERT INTO `t_city` VALUES ('632321', '同仁县', '3');
INSERT INTO `t_city` VALUES ('632322', '尖扎县', '3');
INSERT INTO `t_city` VALUES ('632323', '泽库县', '3');
INSERT INTO `t_city` VALUES ('632324', '河南蒙古族自治县', '3');
INSERT INTO `t_city` VALUES ('632500', '海南藏族自治州', '2');
INSERT INTO `t_city` VALUES ('632521', '共和县', '3');
INSERT INTO `t_city` VALUES ('632522', '同德县', '3');
INSERT INTO `t_city` VALUES ('632523', '贵德县', '3');
INSERT INTO `t_city` VALUES ('632524', '兴海县', '3');
INSERT INTO `t_city` VALUES ('632525', '贵南县', '3');
INSERT INTO `t_city` VALUES ('632600', '果洛藏族自治州', '2');
INSERT INTO `t_city` VALUES ('632621', '玛沁县', '3');
INSERT INTO `t_city` VALUES ('632622', '班玛县', '3');
INSERT INTO `t_city` VALUES ('632623', '甘德县', '3');
INSERT INTO `t_city` VALUES ('632624', '达日县', '3');
INSERT INTO `t_city` VALUES ('632625', '久治县', '3');
INSERT INTO `t_city` VALUES ('632626', '玛多县', '3');
INSERT INTO `t_city` VALUES ('632700', '玉树藏族自治州', '2');
INSERT INTO `t_city` VALUES ('632701', '玉树市', '3');
INSERT INTO `t_city` VALUES ('632722', '杂多县', '3');
INSERT INTO `t_city` VALUES ('632723', '称多县', '3');
INSERT INTO `t_city` VALUES ('632724', '治多县', '3');
INSERT INTO `t_city` VALUES ('632725', '囊谦县', '3');
INSERT INTO `t_city` VALUES ('632726', '曲麻莱县', '3');
INSERT INTO `t_city` VALUES ('632800', '海西蒙古族藏族自治州', '2');
INSERT INTO `t_city` VALUES ('632801', '格尔木市', '3');
INSERT INTO `t_city` VALUES ('632802', '德令哈市', '3');
INSERT INTO `t_city` VALUES ('632821', '乌兰县', '3');
INSERT INTO `t_city` VALUES ('632822', '都兰县', '3');
INSERT INTO `t_city` VALUES ('632823', '天峻县', '3');
INSERT INTO `t_city` VALUES ('640000', '宁夏回族自治区', '1');
INSERT INTO `t_city` VALUES ('640100', '银川市', '2');
INSERT INTO `t_city` VALUES ('640101', '市辖区', '3');
INSERT INTO `t_city` VALUES ('640104', '兴庆区', '3');
INSERT INTO `t_city` VALUES ('640105', '西夏区', '3');
INSERT INTO `t_city` VALUES ('640106', '金凤区', '3');
INSERT INTO `t_city` VALUES ('640121', '永宁县', '3');
INSERT INTO `t_city` VALUES ('640122', '贺兰县', '3');
INSERT INTO `t_city` VALUES ('640181', '灵武市', '3');
INSERT INTO `t_city` VALUES ('640200', '石嘴山市', '2');
INSERT INTO `t_city` VALUES ('640201', '市辖区', '3');
INSERT INTO `t_city` VALUES ('640202', '大武口区', '3');
INSERT INTO `t_city` VALUES ('640205', '惠农区', '3');
INSERT INTO `t_city` VALUES ('640221', '平罗县', '3');
INSERT INTO `t_city` VALUES ('640300', '吴忠市', '2');
INSERT INTO `t_city` VALUES ('640301', '市辖区', '3');
INSERT INTO `t_city` VALUES ('640302', '利通区', '3');
INSERT INTO `t_city` VALUES ('640303', '红寺堡区', '3');
INSERT INTO `t_city` VALUES ('640323', '盐池县', '3');
INSERT INTO `t_city` VALUES ('640324', '同心县', '3');
INSERT INTO `t_city` VALUES ('640381', '青铜峡市', '3');
INSERT INTO `t_city` VALUES ('640400', '固原市', '2');
INSERT INTO `t_city` VALUES ('640401', '市辖区', '3');
INSERT INTO `t_city` VALUES ('640402', '原州区', '3');
INSERT INTO `t_city` VALUES ('640422', '西吉县', '3');
INSERT INTO `t_city` VALUES ('640423', '隆德县', '3');
INSERT INTO `t_city` VALUES ('640424', '泾源县', '3');
INSERT INTO `t_city` VALUES ('640425', '彭阳县', '3');
INSERT INTO `t_city` VALUES ('640500', '中卫市', '2');
INSERT INTO `t_city` VALUES ('640501', '市辖区', '3');
INSERT INTO `t_city` VALUES ('640502', '沙坡头区', '3');
INSERT INTO `t_city` VALUES ('640521', '中宁县', '3');
INSERT INTO `t_city` VALUES ('640522', '海原县', '3');
INSERT INTO `t_city` VALUES ('650000', '新疆维吾尔自治区', '1');
INSERT INTO `t_city` VALUES ('650100', '乌鲁木齐市', '2');
INSERT INTO `t_city` VALUES ('650101', '市辖区', '3');
INSERT INTO `t_city` VALUES ('650102', '天山区', '3');
INSERT INTO `t_city` VALUES ('650103', '沙依巴克区', '3');
INSERT INTO `t_city` VALUES ('650104', '新市区', '3');
INSERT INTO `t_city` VALUES ('650105', '水磨沟区', '3');
INSERT INTO `t_city` VALUES ('650106', '头屯河区', '3');
INSERT INTO `t_city` VALUES ('650107', '达坂城区', '3');
INSERT INTO `t_city` VALUES ('650109', '米东区', '3');
INSERT INTO `t_city` VALUES ('650121', '乌鲁木齐县', '3');
INSERT INTO `t_city` VALUES ('650200', '克拉玛依市', '2');
INSERT INTO `t_city` VALUES ('650201', '市辖区', '3');
INSERT INTO `t_city` VALUES ('650202', '独山子区', '3');
INSERT INTO `t_city` VALUES ('650203', '克拉玛依区', '3');
INSERT INTO `t_city` VALUES ('650204', '白碱滩区', '3');
INSERT INTO `t_city` VALUES ('650205', '乌尔禾区', '3');
INSERT INTO `t_city` VALUES ('650400', '吐鲁番市', '2');
INSERT INTO `t_city` VALUES ('650402', '高昌区', '3');
INSERT INTO `t_city` VALUES ('650421', '鄯善县', '3');
INSERT INTO `t_city` VALUES ('650422', '托克逊县', '3');
INSERT INTO `t_city` VALUES ('650500', '哈密市', '2');
INSERT INTO `t_city` VALUES ('650502', '伊州区', '3');
INSERT INTO `t_city` VALUES ('650521', '巴里坤哈萨克自治县', '3');
INSERT INTO `t_city` VALUES ('650522', '伊吾县', '3');
INSERT INTO `t_city` VALUES ('652300', '昌吉回族自治州', '2');
INSERT INTO `t_city` VALUES ('652301', '昌吉市', '3');
INSERT INTO `t_city` VALUES ('652302', '阜康市', '3');
INSERT INTO `t_city` VALUES ('652323', '呼图壁县', '3');
INSERT INTO `t_city` VALUES ('652324', '玛纳斯县', '3');
INSERT INTO `t_city` VALUES ('652325', '奇台县', '3');
INSERT INTO `t_city` VALUES ('652327', '吉木萨尔县', '3');
INSERT INTO `t_city` VALUES ('652328', '木垒哈萨克自治县', '3');
INSERT INTO `t_city` VALUES ('652700', '博尔塔拉蒙古自治州', '2');
INSERT INTO `t_city` VALUES ('652701', '博乐市', '3');
INSERT INTO `t_city` VALUES ('652702', '阿拉山口市', '3');
INSERT INTO `t_city` VALUES ('652722', '精河县', '3');
INSERT INTO `t_city` VALUES ('652723', '温泉县', '3');
INSERT INTO `t_city` VALUES ('652800', '巴音郭楞蒙古自治州', '2');
INSERT INTO `t_city` VALUES ('652801', '库尔勒市', '3');
INSERT INTO `t_city` VALUES ('652822', '轮台县', '3');
INSERT INTO `t_city` VALUES ('652823', '尉犁县', '3');
INSERT INTO `t_city` VALUES ('652824', '若羌县', '3');
INSERT INTO `t_city` VALUES ('652825', '且末县', '3');
INSERT INTO `t_city` VALUES ('652826', '焉耆回族自治县', '3');
INSERT INTO `t_city` VALUES ('652827', '和静县', '3');
INSERT INTO `t_city` VALUES ('652828', '和硕县', '3');
INSERT INTO `t_city` VALUES ('652829', '博湖县', '3');
INSERT INTO `t_city` VALUES ('652900', '阿克苏地区', '2');
INSERT INTO `t_city` VALUES ('652901', '阿克苏市', '3');
INSERT INTO `t_city` VALUES ('652922', '温宿县', '3');
INSERT INTO `t_city` VALUES ('652923', '库车县', '3');
INSERT INTO `t_city` VALUES ('652924', '沙雅县', '3');
INSERT INTO `t_city` VALUES ('652925', '新和县', '3');
INSERT INTO `t_city` VALUES ('652926', '拜城县', '3');
INSERT INTO `t_city` VALUES ('652927', '乌什县', '3');
INSERT INTO `t_city` VALUES ('652928', '阿瓦提县', '3');
INSERT INTO `t_city` VALUES ('652929', '柯坪县', '3');
INSERT INTO `t_city` VALUES ('653000', '克孜勒苏柯尔克孜自治州', '2');
INSERT INTO `t_city` VALUES ('653001', '阿图什市', '3');
INSERT INTO `t_city` VALUES ('653022', '阿克陶县', '3');
INSERT INTO `t_city` VALUES ('653023', '阿合奇县', '3');
INSERT INTO `t_city` VALUES ('653024', '乌恰县', '3');
INSERT INTO `t_city` VALUES ('653100', '喀什地区', '2');
INSERT INTO `t_city` VALUES ('653101', '喀什市', '3');
INSERT INTO `t_city` VALUES ('653121', '疏附县', '3');
INSERT INTO `t_city` VALUES ('653122', '疏勒县', '3');
INSERT INTO `t_city` VALUES ('653123', '英吉沙县', '3');
INSERT INTO `t_city` VALUES ('653124', '泽普县', '3');
INSERT INTO `t_city` VALUES ('653125', '莎车县', '3');
INSERT INTO `t_city` VALUES ('653126', '叶城县', '3');
INSERT INTO `t_city` VALUES ('653127', '麦盖提县', '3');
INSERT INTO `t_city` VALUES ('653128', '岳普湖县', '3');
INSERT INTO `t_city` VALUES ('653129', '伽师县', '3');
INSERT INTO `t_city` VALUES ('653130', '巴楚县', '3');
INSERT INTO `t_city` VALUES ('653131', '塔什库尔干塔吉克自治县', '3');
INSERT INTO `t_city` VALUES ('653200', '和田地区', '2');
INSERT INTO `t_city` VALUES ('653201', '和田市', '3');
INSERT INTO `t_city` VALUES ('653221', '和田县', '3');
INSERT INTO `t_city` VALUES ('653222', '墨玉县', '3');
INSERT INTO `t_city` VALUES ('653223', '皮山县', '3');
INSERT INTO `t_city` VALUES ('653224', '洛浦县', '3');
INSERT INTO `t_city` VALUES ('653225', '策勒县', '3');
INSERT INTO `t_city` VALUES ('653226', '于田县', '3');
INSERT INTO `t_city` VALUES ('653227', '民丰县', '3');
INSERT INTO `t_city` VALUES ('654000', '伊犁哈萨克自治州', '2');
INSERT INTO `t_city` VALUES ('654002', '伊宁市', '3');
INSERT INTO `t_city` VALUES ('654003', '奎屯市', '3');
INSERT INTO `t_city` VALUES ('654004', '霍尔果斯市', '3');
INSERT INTO `t_city` VALUES ('654021', '伊宁县', '3');
INSERT INTO `t_city` VALUES ('654022', '察布查尔锡伯自治县', '3');
INSERT INTO `t_city` VALUES ('654023', '霍城县', '3');
INSERT INTO `t_city` VALUES ('654024', '巩留县', '3');
INSERT INTO `t_city` VALUES ('654025', '新源县', '3');
INSERT INTO `t_city` VALUES ('654026', '昭苏县', '3');
INSERT INTO `t_city` VALUES ('654027', '特克斯县', '3');
INSERT INTO `t_city` VALUES ('654028', '尼勒克县', '3');
INSERT INTO `t_city` VALUES ('654200', '塔城地区', '2');
INSERT INTO `t_city` VALUES ('654201', '塔城市', '3');
INSERT INTO `t_city` VALUES ('654202', '乌苏市', '3');
INSERT INTO `t_city` VALUES ('654221', '额敏县', '3');
INSERT INTO `t_city` VALUES ('654223', '沙湾县', '3');
INSERT INTO `t_city` VALUES ('654224', '托里县', '3');
INSERT INTO `t_city` VALUES ('654225', '裕民县', '3');
INSERT INTO `t_city` VALUES ('654226', '和布克赛尔蒙古自治县', '3');
INSERT INTO `t_city` VALUES ('654300', '阿勒泰地区', '2');
INSERT INTO `t_city` VALUES ('654301', '阿勒泰市', '3');
INSERT INTO `t_city` VALUES ('654321', '布尔津县', '3');
INSERT INTO `t_city` VALUES ('654322', '富蕴县', '3');
INSERT INTO `t_city` VALUES ('654323', '福海县', '3');
INSERT INTO `t_city` VALUES ('654324', '哈巴河县', '3');
INSERT INTO `t_city` VALUES ('654325', '青河县', '3');
INSERT INTO `t_city` VALUES ('654326', '吉木乃县', '3');
INSERT INTO `t_city` VALUES ('659000', '自治区直辖县级行政区划', '2');
INSERT INTO `t_city` VALUES ('659001', '石河子市', '3');
INSERT INTO `t_city` VALUES ('659002', '阿拉尔市', '3');
INSERT INTO `t_city` VALUES ('659003', '图木舒克市', '3');
INSERT INTO `t_city` VALUES ('659004', '五家渠市', '3');
INSERT INTO `t_city` VALUES ('659006', '铁门关市', '3');
INSERT INTO `t_city` VALUES ('710000', '台湾省', '1');
INSERT INTO `t_city` VALUES ('810000', '香港特别行政区', '1');
INSERT INTO `t_city` VALUES ('820000', '澳门特别行政区', '1');

-- ----------------------------
-- Table structure for `t_commit`
-- ----------------------------
DROP TABLE IF EXISTS `t_commit`;
CREATE TABLE `t_commit` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT,
  `c_id` varchar(50) DEFAULT NULL,
  `message` varchar(1000) DEFAULT NULL,
  `url` varchar(500) DEFAULT NULL,
  `timestamp` datetime DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `push_sha` varchar(200) NOT NULL DEFAULT '' COMMENT 'push编号',
  `issue_id` bigint(20) DEFAULT NULL COMMENT '工作项编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_commit
-- ----------------------------

-- ----------------------------
-- Table structure for `t_commit_file`
-- ----------------------------
DROP TABLE IF EXISTS `t_commit_file`;
CREATE TABLE `t_commit_file` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `commit_sha1` varchar(50) NOT NULL COMMENT '提交版本号',
  `path` varchar(500) NOT NULL COMMENT '文件名',
  `extension` varchar(5000) NOT NULL COMMENT '文件类型',
  `status` varchar(20) NOT NULL COMMENT '状态',
  `add_lines` bigint(20) NOT NULL COMMENT '新增行数',
  `remove_lines` bigint(20) NOT NULL COMMENT '删除行数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_commit_file
-- ----------------------------

-- ----------------------------
-- Table structure for `t_commit_log`
-- ----------------------------
DROP TABLE IF EXISTS `t_commit_log`;
CREATE TABLE `t_commit_log` (
  `sha1` varchar(100) NOT NULL COMMENT '版本号',
  `message` varchar(5000) NOT NULL COMMENT '内容',
  `author` varchar(20) NOT NULL COMMENT '提交人',
  `date` datetime NOT NULL COMMENT '提交时间',
  `branch` varchar(20) NOT NULL COMMENT '所在分支',
  `issue_id` varchar(10) DEFAULT NULL COMMENT '工作项编号',
  `job_id` bigint(20) NOT NULL COMMENT '定时任务编号',
  `insert_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`sha1`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_commit_log
-- ----------------------------

-- ----------------------------
-- Table structure for `t_dict`
-- ----------------------------
DROP TABLE IF EXISTS `t_dict`;
CREATE TABLE `t_dict` (
  `id` bigint(50) NOT NULL AUTO_INCREMENT,
  `code` varchar(20) NOT NULL COMMENT '编码',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `cata_code` varchar(50) NOT NULL COMMENT '字典分类编码',
  `seq` smallint(6) DEFAULT NULL COMMENT '顺序号',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint(50) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(50) DEFAULT NULL COMMENT '更新人',
  `update_count` tinyint(6) DEFAULT '0' COMMENT '版本',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_name` (`name`,`cata_code`),
  KEY `uk_code` (`code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=170 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_dict
-- ----------------------------
INSERT INTO `t_dict` VALUES ('1', 'alioss', '阿里云OSS服务', 't_file_index.storage_type', '2', '2017-10-27 17:00:52', '623', null, '623', '0');
INSERT INTO `t_dict` VALUES ('2', 'all', '允许访问全部数据', 't_role_func.data_auth', '1', null, '623', null, '623', '0');
INSERT INTO `t_dict` VALUES ('4', 'Onclick', '点击控件', 't_user_log.action', '4', null, '623', '2017-10-27 17:00:52', '623', '0');
INSERT INTO `t_dict` VALUES ('6', 'department', '允许访问本部门数据', 't_role_func.data_auth', '2', null, '623', null, '623', '0');
INSERT INTO `t_dict` VALUES ('7', 'disable', '禁用', 't_schedule_job.status', '1', null, '623', null, '623', '0');
INSERT INTO `t_dict` VALUES ('8', 'disable', '禁用', 't_user.status', '2', null, '623', null, '623', '0');
INSERT INTO `t_dict` VALUES ('9', 'divorced', '离异', 't_employee.marital', '3', null, '623', '2017-10-27 16:46:24', '623', '0');
INSERT INTO `t_dict` VALUES ('10', 'employ', '已入职', 't_employee.status', '3', null, '623', '2017-10-27 16:42:58', '623', '0');
INSERT INTO `t_dict` VALUES ('11', 'enable', '启用', 't_schedule_job.status', '2', null, '623', null, '623', '0');
INSERT INTO `t_dict` VALUES ('12', 'enable', '启用', 't_user.status', '1', null, '623', null, '623', '0');
INSERT INTO `t_dict` VALUES ('13', 'export', '服务器导出文件', 't_file_index.source', '2', null, '623', null, '623', '0');
INSERT INTO `t_dict` VALUES ('14', 'female', '女', 't_employee.gender', '2', null, '623', '2017-10-27 16:49:23', '623', '0');
INSERT INTO `t_dict` VALUES ('15', 'fulltime', '全职', 't_education.learning_style', '1', null, '623', '2017-10-27 16:47:00', '623', '0');
INSERT INTO `t_dict` VALUES ('16', 'individual', '允许访问个人数据', 't_role_func.data_auth', '3', null, '623', null, '623', '0');
INSERT INTO `t_dict` VALUES ('17', 'javabean', 'Jave Bean', 't_schedule_job.type', '1', null, '623', null, '623', '0');
INSERT INTO `t_dict` VALUES ('18', 'ldap', 'LDAP认证', 't_user.auth_type', '2', null, '623', null, '623', '0');
INSERT INTO `t_dict` VALUES ('20', 'local', '本地服务器存储', 't_file_index.storage_type', '1', null, '623', null, '623', '0');
INSERT INTO `t_dict` VALUES ('21', 'local', '本地服务器认证', 't_user.auth_type', '1', null, '623', null, '623', '0');
INSERT INTO `t_dict` VALUES ('22', 'login', '登录系统', 't_user_log.action', '1', null, '623', '2017-10-27 17:00:18', '623', '0');
INSERT INTO `t_dict` VALUES ('23', 'logout', '退出系统', 't_user_log.action', '2', null, '623', '2017-10-27 17:00:29', '623', '0');
INSERT INTO `t_dict` VALUES ('24', 'male', '男', 't_employee.gender', '1', null, '623', '2017-10-27 16:49:15', '623', '0');
INSERT INTO `t_dict` VALUES ('25', 'married', '已婚', 't_employee.marital', '2', null, '623', '2017-10-27 16:46:17', '623', '0');
INSERT INTO `t_dict` VALUES ('26', 'methodname', '方法名', 't_schedule_param.name', '4', null, '623', '2017-10-27 16:51:18', '623', '0');
INSERT INTO `t_dict` VALUES ('27', 'multi', '多线程执行', 't_schedule_job.concurrent', '2', null, '623', null, '623', '0');
INSERT INTO `t_dict` VALUES ('28', 'new', '新窗口', 't_menu.target', '3', null, '623', null, '623', '0');
INSERT INTO `t_dict` VALUES ('29', 'offer', '已录用', 't_employee.status', '2', null, '623', '2017-10-27 16:42:48', '623', '0');
INSERT INTO `t_dict` VALUES ('31', 'parttime', '在职', 't_education.learning_style', '2', null, '623', '2017-10-27 16:47:04', '623', '0');
INSERT INTO `t_dict` VALUES ('32', 'private', '私有文件', 't_file_index.access_type', '1', null, '623', null, '623', '0');
INSERT INTO `t_dict` VALUES ('33', 'public', '公开文件', 't_file_index.access_type', '2', null, '623', null, '623', '0');
INSERT INTO `t_dict` VALUES ('34', 'reserve', '已储备', 't_employee.status', '1', null, '623', '2017-10-27 16:42:40', '623', '0');
INSERT INTO `t_dict` VALUES ('36', 'single', '单线程执行', 't_schedule_job.concurrent', '1', null, '623', null, '623', '0');
INSERT INTO `t_dict` VALUES ('37', 'springbean', 'Spring Bean', 't_schedule_job.type', '2', null, '623', null, '623', '0');
INSERT INTO `t_dict` VALUES ('38', 'springid', 'Spring ID', 't_schedule_param.name', '2', null, '623', '2017-10-27 16:51:03', '623', '0');
INSERT INTO `t_dict` VALUES ('39', 'sql', 'SQL脚本', 't_schedule_job.type', '3', null, '623', null, '623', '0');
INSERT INTO `t_dict` VALUES ('40', 'sql', '脚本名称', 't_schedule_param.name', '3', null, '623', '2017-10-27 16:50:56', '623', '0');
INSERT INTO `t_dict` VALUES ('42', 'unmarried', '未婚', 't_employee.marital', '1', null, '623', '2017-10-27 16:46:11', '623', '0');
INSERT INTO `t_dict` VALUES ('43', 'upload', '用户上传文件', 't_file_index.source', '1', null, '623', null, '623', '0');
INSERT INTO `t_dict` VALUES ('44', 'widowed', '丧偶', 't_employee.marital', '4', null, '623', '2017-10-27 16:46:30', '623', '0');
INSERT INTO `t_dict` VALUES ('45', 'work', '已上岗', 't_employee.status', '4', null, '623', '2017-10-27 16:43:06', '623', '0');
INSERT INTO `t_dict` VALUES ('46', 'fulltime', '全职员工', 't_employee.type', '2', null, '623', '2017-10-27 16:49:58', '623', '0');
INSERT INTO `t_dict` VALUES ('47', 'parttime', '兼职员工', 't_employee.type', '3', null, '623', '2017-10-27 16:50:02', '623', '0');
INSERT INTO `t_dict` VALUES ('48', 'student', '兼职学生', 't_employee.type', '1', null, '623', '2017-10-27 16:49:51', '623', '0');
INSERT INTO `t_dict` VALUES ('49', '1', '是', 't_resource.is_auth', '1', null, '623', '2017-10-26 15:35:04', '623', '0');
INSERT INTO `t_dict` VALUES ('50', '0', '否', 't_resource.is_auth', '2', null, '623', '2017-10-26 15:35:15', '623', '0');
INSERT INTO `t_dict` VALUES ('51', 'bachelor', '本科', 't_education.degree', '1', null, '623', '2017-10-27 16:47:30', '623', '0');
INSERT INTO `t_dict` VALUES ('52', 'threeyear', '大专', 't_education.degree', '2', null, '623', '2017-10-27 16:47:40', '623', '0');
INSERT INTO `t_dict` VALUES ('53', 'doctor', '博士', 't_education.degree', '3', null, '623', '2017-10-27 16:47:47', '623', '0');
INSERT INTO `t_dict` VALUES ('54', 'master', '硕士', 't_education.degree', '4', null, '623', '2017-10-27 16:48:01', '623', '0');
INSERT INTO `t_dict` VALUES ('55', 'vocational', '中专', 't_education.degree', '5', null, '623', '2017-10-27 16:48:11', '623', '0');
INSERT INTO `t_dict` VALUES ('56', 'juniorhigh', '高中', 't_education.degree', '6', null, '623', '2017-10-27 16:48:23', '623', '0');
INSERT INTO `t_dict` VALUES ('57', 'seniorhigh', '初中', 't_education.degree', '7', null, '623', '2017-10-27 16:48:32', '623', '0');
INSERT INTO `t_dict` VALUES ('58', 'elementary', '小学', 't_education.degree', '8', null, '623', '2017-10-27 16:48:38', '623', '0');
INSERT INTO `t_dict` VALUES ('59', 'OnLoad', '打开页面', 't_user_log.action', '6', null, '623', '2017-10-27 17:01:09', '623', '0');
INSERT INTO `t_dict` VALUES ('60', 'OnSelect', '选择事件', 't_user_log.action', '5', null, '623', '2017-10-27 17:01:00', '623', '0');
INSERT INTO `t_dict` VALUES ('61', 'index', '首页', 't_menu.type', '3', null, '623', '2017-10-27 17:01:44', '623', '0');
INSERT INTO `t_dict` VALUES ('62', 'mainMenu', '主菜单', 't_menu.type', '1', null, '623', '2017-10-27 17:01:33', '623', '0');
INSERT INTO `t_dict` VALUES ('63', 'quickMenu', '快捷菜单', 't_menu.type', '2', null, '623', '2017-10-27 17:01:37', '623', '0');
INSERT INTO `t_dict` VALUES ('64', 'beanclass', 'Bean类名', 't_schedule_param.name', '1', null, '623', '2017-10-27 16:51:14', '623', '0');
INSERT INTO `t_dict` VALUES ('65', 'window', '弹出框', 't_menu.target', '2', null, '623', '2017-10-30 15:08:47', '623', '0');
INSERT INTO `t_dict` VALUES ('66', 'tab', '工作区', 't_menu.target', '1', null, '623', '2017-10-30 15:08:29', '623', '0');
INSERT INTO `t_dict` VALUES ('67', 'RelativeURL', '内部页面', 't_resource.type', '1', null, '623', null, '623', '0');
INSERT INTO `t_dict` VALUES ('68', 'AbsoluteURL', '外部页面', 't_resource.type', '2', null, '623', null, '623', '0');
INSERT INTO `t_dict` VALUES ('69', 'Component', '组件', 't_resource.type', '3', null, '623', null, '623', '0');
INSERT INTO `t_dict` VALUES ('97', 'unavailable', '不可用', 't_employee.status', '5', null, '623', '2017-10-27 16:45:31', '623', '0');
INSERT INTO `t_dict` VALUES ('98', 'dimission', '已离职', 't_employee.status', '6', null, '623', '2017-10-27 16:45:25', '623', '0');
INSERT INTO `t_dict` VALUES ('99', 'Module', '模块', 't_resource.type', '4', null, '623', null, '623', '0');
INSERT INTO `t_dict` VALUES ('100', 'other', '其他', 't_education.degree', '9', null, '623', null, '623', '0');
INSERT INTO `t_dict` VALUES ('119', 'System', '系统', 't_resource.type', '5', null, '623', null, '623', '0');
INSERT INTO `t_dict` VALUES ('120', 'SubSystem', '子系统', 't_resource.type', '6', null, '623', null, '623', '0');
INSERT INTO `t_dict` VALUES ('130', '1', '界面详细设计说明书', 't_markdown_doc.type', '1', null, null, null, null, '0');
INSERT INTO `t_dict` VALUES ('131', 'projMember', '项目成员', 't_role_func.data_auth', '1', '2017-12-20 10:28:19', '623', '2017-12-20 10:28:19', '623', '0');
INSERT INTO `t_dict` VALUES ('165', '按时', '啊', 't_education.degree', '1', '2018-03-28 10:39:45', '674', '2018-03-28 13:14:57', '674', '0');

-- ----------------------------
-- Table structure for `t_dict_cata`
-- ----------------------------
DROP TABLE IF EXISTS `t_dict_cata`;
CREATE TABLE `t_dict_cata` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT,
  `code` varchar(50) NOT NULL COMMENT '编码',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint(50) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(50) DEFAULT NULL COMMENT '更新人',
  `update_count` tinyint(6) DEFAULT '0' COMMENT '版本号',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_dict_cata
-- ----------------------------
INSERT INTO `t_dict_cata` VALUES ('1', 't_education.degree', '人员学历', null, '623', '2017-11-28 19:08:33', '623', '0');
INSERT INTO `t_dict_cata` VALUES ('2', 't_education.learning_style', '人员学习方式', null, '623', '2017-11-30 15:53:17', '623', '0');
INSERT INTO `t_dict_cata` VALUES ('3', 't_employee.gender', '人员性别', null, '623', null, '623', '0');
INSERT INTO `t_dict_cata` VALUES ('4', 't_employee.marital', '人员婚姻状态', null, '623', null, '623', '0');
INSERT INTO `t_dict_cata` VALUES ('5', 't_employee.status', '人员状态', null, '623', null, '623', '0');
INSERT INTO `t_dict_cata` VALUES ('6', 't_employee.type', '人员类型', null, '623', null, '623', '0');
INSERT INTO `t_dict_cata` VALUES ('7', 't_file_index.access_type', '文件访问类型', null, '623', null, '623', '0');
INSERT INTO `t_dict_cata` VALUES ('8', 't_file_index.source', '文件来源', null, '623', null, '623', '0');
INSERT INTO `t_dict_cata` VALUES ('9', 't_file_index.storage_type', '文件存储类型', null, '623', null, '623', '0');
INSERT INTO `t_dict_cata` VALUES ('10', 't_issue.priority', '工作项优先级', null, '623', null, '623', '0');
INSERT INTO `t_dict_cata` VALUES ('11', 't_issue.status', '工作项状态', null, '623', null, '623', '0');
INSERT INTO `t_dict_cata` VALUES ('12', 't_issue.type', '工作项类型', null, '623', null, '623', '0');
INSERT INTO `t_dict_cata` VALUES ('13', 't_issue_log.type', '工作项日志类型', null, '623', null, '623', '0');
INSERT INTO `t_dict_cata` VALUES ('14', 't_menu.target', 'URL打开方式', null, '623', '2017-11-28 19:08:56', '623', '0');
INSERT INTO `t_dict_cata` VALUES ('15', 't_menu.type', '菜单类型', null, '623', null, '623', '0');
INSERT INTO `t_dict_cata` VALUES ('16', 't_project.status', '项目状态', null, '623', null, '623', '0');
INSERT INTO `t_dict_cata` VALUES ('17', 't_resource.is_auth', '是否授权', null, '623', null, '623', '0');
INSERT INTO `t_dict_cata` VALUES ('18', 't_role_func.data_auth', '数据访问权限', null, '623', null, '623', '0');
INSERT INTO `t_dict_cata` VALUES ('19', 't_schedule_job', '定时任务并发控制', null, '623', null, '623', '0');
INSERT INTO `t_dict_cata` VALUES ('20', 't_schedule_job.concurrent', '并发控制', null, '623', null, '623', '0');
INSERT INTO `t_dict_cata` VALUES ('21', 't_schedule_job.status', '定时任务状态', null, '623', null, '623', '0');
INSERT INTO `t_dict_cata` VALUES ('22', 't_schedule_job.type', '定时任务类型', null, '623', null, '623', '0');
INSERT INTO `t_dict_cata` VALUES ('23', 't_schedule_param.name', '定时任务参数名称', null, '623', null, '623', '0');
INSERT INTO `t_dict_cata` VALUES ('24', 't_sprint.status', '迭代状态', null, '623', null, '623', '0');
INSERT INTO `t_dict_cata` VALUES ('25', 't_testcase.level', '测试用例等级', null, '623', null, '623', '0');
INSERT INTO `t_dict_cata` VALUES ('26', 't_testcase_record.result', '测试结果', null, '623', null, '623', '0');
INSERT INTO `t_dict_cata` VALUES ('27', 't_user.auth_type', '用户认证方式', null, '623', '2017-11-30 17:33:29', '193', '0');
INSERT INTO `t_dict_cata` VALUES ('28', 't_user.status', '用户状态', null, '623', null, '623', '0');
INSERT INTO `t_dict_cata` VALUES ('29', 't_user_log.action', '用户操作动作', null, '623', null, '623', '0');
INSERT INTO `t_dict_cata` VALUES ('30', '_worklog_detail. category', '工作日报分类', '2017-12-04 16:47:56', null, '2017-12-04 16:48:00', null, '0');
INSERT INTO `t_dict_cata` VALUES ('33', 't_review.review_type', '评审形式', '2017-12-04 18:56:39', '623', '2017-12-04 18:56:39', '623', '0');
INSERT INTO `t_dict_cata` VALUES ('34', 't_review.review_result', '评审结果', '2017-12-04 18:57:03', '623', '2017-12-04 18:57:03', '623', '0');
INSERT INTO `t_dict_cata` VALUES ('37', 't_issue.risk_result_type', '风险工作项处理结果', '2018-01-03 09:26:59', null, null, null, '0');
INSERT INTO `t_dict_cata` VALUES ('41', 't_sales_clues.stage', '销售线索阶段', '2018-03-30 13:33:16', '623', '2018-03-30 13:33:16', '623', '0');
INSERT INTO `t_dict_cata` VALUES ('43', 't_announcement.type', '公告类别', null, null, null, null, '0');
INSERT INTO `t_dict_cata` VALUES ('44', 't_sales_clues.way', '销售线索渠道', null, null, null, null, '0');
INSERT INTO `t_dict_cata` VALUES ('45', 't_contacts.type', '客户联系人类型', null, null, null, null, '0');

-- ----------------------------
-- Table structure for `t_doc_cata`
-- ----------------------------
DROP TABLE IF EXISTS `t_doc_cata`;
CREATE TABLE `t_doc_cata` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `pid` bigint(20) DEFAULT NULL COMMENT '父节点ID',
  `name` varchar(200) NOT NULL COMMENT '目录名称',
  `seq` smallint(6) NOT NULL COMMENT '排序号',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint(50) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(50) DEFAULT NULL COMMENT '更新人',
  `update_count` tinyint(6) DEFAULT '0' COMMENT '版本号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_doc_cata
-- ----------------------------

-- ----------------------------
-- Table structure for `t_document`
-- ----------------------------
DROP TABLE IF EXISTS `t_document`;
CREATE TABLE `t_document` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `file_id` bigint(20) NOT NULL COMMENT '文件ID',
  `cata_id` bigint(20) NOT NULL COMMENT '父节点目录ID',
  `title` varchar(200) NOT NULL COMMENT '文档标题',
  `keyword` varchar(200) NOT NULL COMMENT '关键词',
  `status` varchar(20) NOT NULL COMMENT '状态',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint(50) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(50) DEFAULT NULL COMMENT '更新人',
  `update_count` tinyint(6) DEFAULT '0' COMMENT '版本号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_document
-- ----------------------------

-- ----------------------------
-- Table structure for `t_education`
-- ----------------------------
DROP TABLE IF EXISTS `t_education`;
CREATE TABLE `t_education` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `emp_id` bigint(20) NOT NULL COMMENT '人员ID',
  `start_date` datetime NOT NULL COMMENT '开始日期',
  `end_date` datetime NOT NULL COMMENT '结束日期',
  `school` varchar(50) NOT NULL COMMENT '学校名称',
  `speciality` varchar(50) NOT NULL COMMENT '专业',
  `degree` varchar(50) NOT NULL COMMENT '学历',
  `learning_style` varchar(20) NOT NULL COMMENT '学习方式',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint(50) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(50) DEFAULT NULL COMMENT '更新人',
  `update_count` tinyint(6) DEFAULT '0' COMMENT '版本号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_education
-- ----------------------------

-- ----------------------------
-- Table structure for `t_email_record`
-- ----------------------------
DROP TABLE IF EXISTS `t_email_record`;
CREATE TABLE `t_email_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `address` varchar(50) NOT NULL COMMENT '地址',
  `send_name` varchar(50) DEFAULT NULL COMMENT '希望显示的发送人昵称',
  `char_set` varchar(10) DEFAULT NULL,
  `receive_name` varchar(50) DEFAULT NULL COMMENT '希望显示的收件人名字',
  `content` varchar(2000) NOT NULL COMMENT '邮件正文',
  `subject` varchar(200) NOT NULL COMMENT '邮件主题',
  `sent_date` datetime DEFAULT NULL COMMENT '发送时间',
  `result` varchar(10) NOT NULL,
  `create_time` datetime NOT NULL,
  `create_by` bigint(20) NOT NULL,
  `update_time` datetime NOT NULL,
  `update_by` bigint(20) NOT NULL,
  `send_count` bigint(2) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_email_record
-- ----------------------------

-- ----------------------------
-- Table structure for `t_emp_log`
-- ----------------------------
DROP TABLE IF EXISTS `t_emp_log`;
CREATE TABLE `t_emp_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `emp_id` varchar(20) DEFAULT NULL COMMENT '人员编号',
  `type` varchar(20) NOT NULL COMMENT '操作类型',
  `log_content` varchar(2000) NOT NULL COMMENT '日志内容',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint(50) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(50) DEFAULT NULL COMMENT '更新人',
  `update_count` tinyint(6) DEFAULT '0' COMMENT '版本号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_emp_log
-- ----------------------------

-- ----------------------------
-- Table structure for `t_employee`
-- ----------------------------
DROP TABLE IF EXISTS `t_employee`;
CREATE TABLE `t_employee` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(50) NOT NULL COMMENT '姓名',
  `code` varchar(20) NOT NULL COMMENT '编码',
  `ext_code` varchar(20) DEFAULT NULL COMMENT '外部编码',
  `org_id` bigint(20) DEFAULT NULL COMMENT '所属组织',
  `id_card` varchar(18) DEFAULT NULL COMMENT '身份证号',
  `status` varchar(20) NOT NULL COMMENT '状态',
  `type` varchar(10) NOT NULL COMMENT '曾用名',
  `gender` varchar(20) NOT NULL COMMENT '性别',
  `nationality` varchar(20) DEFAULT NULL COMMENT '民族',
  `birth_place` varchar(20) DEFAULT NULL COMMENT '出生地',
  `birth_date` datetime DEFAULT NULL COMMENT '出生日期',
  `marital` varchar(20) DEFAULT NULL COMMENT '婚姻状态',
  `mobile` varchar(11) DEFAULT NULL COMMENT '移动电话',
  `office_phone` varchar(13) DEFAULT NULL COMMENT '办公电话',
  `home_phone` varchar(13) DEFAULT NULL COMMENT '家庭电话',
  `email` varchar(50) DEFAULT NULL COMMENT '电子邮件',
  `position_id` varchar(50) DEFAULT NULL COMMENT '微信',
  `entry_date` datetime DEFAULT NULL COMMENT '入职日期',
  `home_address` varchar(200) DEFAULT NULL COMMENT '家庭住址',
  `home_postcode` varchar(6) DEFAULT NULL COMMENT '家庭邮政编码',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `photo_id` bigint(100) DEFAULT NULL COMMENT '照片文件ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint(50) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(50) DEFAULT NULL COMMENT '更新人',
  `update_count` tinyint(6) DEFAULT '0' COMMENT '版本号',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_employee
-- ----------------------------
INSERT INTO `t_employee` VALUES ('1', 'test001', '001', '', '306', '', 'work', 'fulltime', 'male', '', '', null, '', '', '', '', '', '', null, '', '', '', null, '2018-06-26 14:58:56', '623', '2018-06-26 21:06:55', '626', '0');

-- ----------------------------
-- Table structure for `t_experience`
-- ----------------------------
DROP TABLE IF EXISTS `t_experience`;
CREATE TABLE `t_experience` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `emp_id` bigint(20) NOT NULL COMMENT '人员ID',
  `start_date` datetime NOT NULL COMMENT '开始日期',
  `end_date` datetime NOT NULL COMMENT '结束日期',
  `company` varchar(50) NOT NULL COMMENT '单位名称',
  `department` varchar(50) NOT NULL COMMENT '部门',
  `position` varchar(50) NOT NULL COMMENT '职位',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint(50) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(50) DEFAULT NULL COMMENT '更新人',
  `update_count` tinyint(6) DEFAULT '0' COMMENT '版本号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_experience
-- ----------------------------

-- ----------------------------
-- Table structure for `t_file_index`
-- ----------------------------
DROP TABLE IF EXISTS `t_file_index`;
CREATE TABLE `t_file_index` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `uuid` varchar(32) NOT NULL COMMENT '文件唯一标识',
  `filename` varchar(200) NOT NULL COMMENT '文件名称',
  `file_type` varchar(50) NOT NULL COMMENT '文件类型',
  `length` bigint(20) NOT NULL COMMENT '文件长度',
  `storage_type` varchar(20) NOT NULL COMMENT '存储类型',
  `access_type` varchar(20) NOT NULL COMMENT '访问类型',
  `source` varchar(20) DEFAULT NULL COMMENT '来源',
  `url` varchar(500) DEFAULT NULL COMMENT 'public文件url',
  `path` varchar(500) NOT NULL,
  `description` varchar(200) DEFAULT NULL COMMENT '描述',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_by` bigint(20) NOT NULL COMMENT '创建人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_uuid` (`uuid`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_file_index
-- ----------------------------
INSERT INTO `t_file_index` VALUES ('1', 'aaa78463684c43e8a51cc58e5e9da1ac', '20180628095920', 'xls', '0', 'local', 'private', 'export', null, 'D:\\files\\private\\export\\20180628\\', null, '2018-06-28 09:59:21', '626');
INSERT INTO `t_file_index` VALUES ('2', 'e046e6d326ab444c8caedcc14b1fcd30', '20180710171815', 'csv', '0', 'local', 'private', 'export', null, 'D:\\files\\private\\export\\20180710\\', null, '2018-07-10 17:18:15', '0');

-- ----------------------------
-- Table structure for `t_function`
-- ----------------------------
DROP TABLE IF EXISTS `t_function`;
CREATE TABLE `t_function` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `description` varchar(200) DEFAULT NULL COMMENT '描述',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint(50) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(50) DEFAULT NULL COMMENT '更新人',
  `update_count` tinyint(6) DEFAULT '0' COMMENT '版本号',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=254 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_function
-- ----------------------------
INSERT INTO `t_function` VALUES ('215', '管理人员', null, '2017-11-23 12:46:12', '1', '2017-11-23 12:46:12', '1', '0');
INSERT INTO `t_function` VALUES ('216', '管理日志', null, '2017-11-23 12:49:46', '1', '2017-11-23 12:49:46', '1', '0');
INSERT INTO `t_function` VALUES ('217', '设置系统', null, '2017-11-23 13:37:16', '1', '2017-11-23 13:37:16', '1', '0');
INSERT INTO `t_function` VALUES ('218', '开发调试', null, '2017-11-23 13:46:57', '1', '2017-11-23 13:46:57', '1', '0');
INSERT INTO `t_function` VALUES ('219', '分配测试人员', null, '2017-11-23 13:53:15', '1', '2017-11-23 13:53:15', '1', '0');
INSERT INTO `t_function` VALUES ('220', '变更申请', null, '2017-12-11 18:29:37', '623', '2017-12-11 18:29:37', '623', '0');
INSERT INTO `t_function` VALUES ('221', '工作日报', null, '2017-12-13 15:55:45', '623', '2017-12-13 15:55:45', '623', '0');
INSERT INTO `t_function` VALUES ('222', 'mdFlow', null, '2017-12-15 14:50:06', '623', '2017-12-15 14:50:06', '623', '0');
INSERT INTO `t_function` VALUES ('225', '表单管理', null, '2017-12-19 16:27:19', '623', '2017-12-19 16:27:19', '623', '0');
INSERT INTO `t_function` VALUES ('230', '心跳监测', null, '2017-12-28 17:01:05', '623', '2017-12-28 17:01:05', '623', '0');
INSERT INTO `t_function` VALUES ('235', '人员活跃统计', null, '2018-03-02 10:49:42', '623', '2018-03-02 10:49:42', '623', '0');
INSERT INTO `t_function` VALUES ('237', '删除权限', null, '2018-03-10 11:02:50', '623', '2018-03-10 11:02:50', '623', '0');
INSERT INTO `t_function` VALUES ('246', '帮助中心', null, '2018-03-27 09:25:01', '623', '2018-03-27 09:25:01', '623', '0');
INSERT INTO `t_function` VALUES ('253', '课程学习', null, '2018-05-24 14:53:27', '623', '2018-05-24 14:53:27', '623', '0');

-- ----------------------------
-- Table structure for `t_job_log`
-- ----------------------------
DROP TABLE IF EXISTS `t_job_log`;
CREATE TABLE `t_job_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `job_id` bigint(20) NOT NULL COMMENT '任务ID',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `cost` bigint(20) NOT NULL COMMENT '耗时毫秒',
  `type` varchar(20) NOT NULL COMMENT '类型',
  `concurrent` varchar(20) NOT NULL COMMENT '并发控制',
  `param` varchar(500) NOT NULL COMMENT '任务参数',
  `result` varchar(10) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=131029 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_job_log
-- ----------------------------
INSERT INTO `t_job_log` VALUES ('131019', '0', '2018-07-17 13:49:00', '26', 'springbean', 'single', '{\"beanclass\":\"com.tedu.base.task.service.JobTaskService\",\"springid\":\"jobTaskService\",\"methodname\":\"initAllJob\"}', 'success');
INSERT INTO `t_job_log` VALUES ('131020', '0', '2018-07-17 13:49:00', '26', 'springbean', 'single', '{\"beanclass\":\"com.tedu.base.task.service.JobTaskService\",\"springid\":\"jobTaskService\",\"methodname\":\"initAllJob\"}', 'success');
INSERT INTO `t_job_log` VALUES ('131021', '0', '2018-07-17 13:50:00', '16', 'springbean', 'single', '{\"beanclass\":\"com.tedu.base.task.service.JobTaskService\",\"springid\":\"jobTaskService\",\"methodname\":\"initAllJob\"}', 'success');
INSERT INTO `t_job_log` VALUES ('131022', '0', '2018-07-17 13:50:00', '48', 'springbean', 'single', '{\"beanclass\":\"com.tedu.base.task.service.JobTaskService\",\"springid\":\"jobTaskService\",\"methodname\":\"initAllJob\"}', 'success');
INSERT INTO `t_job_log` VALUES ('131023', '0', '2018-07-17 13:51:00', '12', 'springbean', 'single', '{\"beanclass\":\"com.tedu.base.task.service.JobTaskService\",\"springid\":\"jobTaskService\",\"methodname\":\"initAllJob\"}', 'success');
INSERT INTO `t_job_log` VALUES ('131024', '0', '2018-07-17 13:51:00', '36', 'springbean', 'single', '{\"beanclass\":\"com.tedu.base.task.service.JobTaskService\",\"springid\":\"jobTaskService\",\"methodname\":\"initAllJob\"}', 'success');
INSERT INTO `t_job_log` VALUES ('131025', '0', '2018-07-17 13:52:00', '12', 'springbean', 'single', '{\"beanclass\":\"com.tedu.base.task.service.JobTaskService\",\"springid\":\"jobTaskService\",\"methodname\":\"initAllJob\"}', 'success');
INSERT INTO `t_job_log` VALUES ('131026', '0', '2018-07-17 13:52:00', '14', 'springbean', 'single', '{\"beanclass\":\"com.tedu.base.task.service.JobTaskService\",\"springid\":\"jobTaskService\",\"methodname\":\"initAllJob\"}', 'success');
INSERT INTO `t_job_log` VALUES ('131027', '0', '2018-07-17 13:53:00', '27', 'springbean', 'single', '{\"beanclass\":\"com.tedu.base.task.service.JobTaskService\",\"springid\":\"jobTaskService\",\"methodname\":\"initAllJob\"}', 'success');
INSERT INTO `t_job_log` VALUES ('131028', '0', '2018-07-17 13:53:00', '14', 'springbean', 'single', '{\"beanclass\":\"com.tedu.base.task.service.JobTaskService\",\"springid\":\"jobTaskService\",\"methodname\":\"initAllJob\"}', 'success');

-- ----------------------------
-- Table structure for `t_markdown_doc`
-- ----------------------------
DROP TABLE IF EXISTS `t_markdown_doc`;
CREATE TABLE `t_markdown_doc` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` varchar(50) NOT NULL COMMENT '文档名称',
  `property` varchar(50) NOT NULL COMMENT '标识',
  `type` varchar(50) NOT NULL COMMENT '文档类型',
  `name` varchar(50) NOT NULL COMMENT '文件名称',
  `path` varchar(200) DEFAULT NULL COMMENT '文件目录',
  `file_id` bigint(20) NOT NULL COMMENT '文件ID',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_markdown_doc
-- ----------------------------

-- ----------------------------
-- Table structure for `t_menu`
-- ----------------------------
DROP TABLE IF EXISTS `t_menu`;
CREATE TABLE `t_menu` (
  `code` bigint(10) NOT NULL AUTO_INCREMENT COMMENT '编码',
  `parent` bigint(10) DEFAULT NULL,
  `name` varchar(50) NOT NULL COMMENT '显示名称',
  `res_id` bigint(20) DEFAULT NULL COMMENT '资源ID',
  `type` varchar(20) NOT NULL DEFAULT 'mainMenu' COMMENT '类型',
  `seq` smallint(6) DEFAULT '0',
  `target` varchar(20) DEFAULT NULL COMMENT 'URL打开方式',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint(50) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(50) DEFAULT NULL COMMENT '更新人',
  `update_count` tinyint(6) DEFAULT '0' COMMENT '版本号',
  PRIMARY KEY (`code`),
  UNIQUE KEY `uk_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=128 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_menu
-- ----------------------------
INSERT INTO `t_menu` VALUES ('1', null, '主菜单', null, 'mainMenu', '0', 'tab', null, '623', null, '623', '0');
INSERT INTO `t_menu` VALUES ('2', '1', '人事管理', null, 'mainMenu', '5', 'tab', '2017-10-16 14:07:41', '623', '2017-11-23 15:37:11', '623', '0');
INSERT INTO `t_menu` VALUES ('3', '2', '部门管理', '49', 'mainMenu', '1', 'tab', '2017-10-16 14:07:41', '623', '2017-10-30 11:53:28', '623', '0');
INSERT INTO `t_menu` VALUES ('5', '2', '人员管理', '51', 'mainMenu', '2', 'tab', '2017-10-16 14:07:41', '623', '2017-10-30 11:53:38', '623', '0');
INSERT INTO `t_menu` VALUES ('6', '10', '账号管理', '47', 'mainMenu', '1', 'tab', null, '623', '2017-10-30 11:54:39', '623', '0');
INSERT INTO `t_menu` VALUES ('7', '10', '角色管理', '46', 'mainMenu', '2', 'tab', null, '623', '2017-10-30 11:54:57', '623', '0');
INSERT INTO `t_menu` VALUES ('8', '10', '权限管理', '45', 'mainMenu', '3', 'tab', null, '623', '2017-10-30 11:55:12', '623', '0');
INSERT INTO `t_menu` VALUES ('9', '2', '人员查询', '144', 'mainMenu', '3', 'tab', '2017-10-16 14:07:41', '623', '2017-10-30 11:53:51', '623', '0');
INSERT INTO `t_menu` VALUES ('10', '1', '用户管理', null, 'mainMenu', '6', 'tab', null, '623', '2017-11-23 15:37:17', '623', '0');
INSERT INTO `t_menu` VALUES ('11', '59', '字典项管理', '52', 'mainMenu', '1', 'tab', null, '623', '2017-10-30 11:56:34', '623', '0');
INSERT INTO `t_menu` VALUES ('12', '59', '系统资源管理', '54', 'mainMenu', '2', 'tab', null, '623', '2017-10-30 11:56:54', '623', '0');
INSERT INTO `t_menu` VALUES ('13', '59', '菜单管理', '55', 'mainMenu', '4', 'tab', null, '623', '2017-10-30 11:57:41', '623', '0');
INSERT INTO `t_menu` VALUES ('14', '59', '定时任务管理', '91', 'mainMenu', '5', 'tab', null, '623', '2017-10-30 11:57:58', '623', '0');
INSERT INTO `t_menu` VALUES ('15', '59', '资源访问日志', '50', 'mainMenu', '3', 'tab', null, '623', '2017-10-30 11:57:11', '623', '0');
INSERT INTO `t_menu` VALUES ('16', '10', '用户操作日志', '53', 'mainMenu', '4', 'tab', null, '623', '2017-10-30 11:56:06', '623', '0');
INSERT INTO `t_menu` VALUES ('17', '59', '任务执行日志', '92', 'mainMenu', '6', 'tab', null, '623', '2017-10-30 11:58:40', '623', '0');
INSERT INTO `t_menu` VALUES ('18', '59', '上传文件管理', '93', 'mainMenu', '7', 'tab', null, '623', '2017-10-30 11:59:03', '623', '0');
INSERT INTO `t_menu` VALUES ('37', '1', '平台工具', null, 'mainMenu', '8', 'tab', null, '623', '2018-01-26 08:56:04', '623', '0');
INSERT INTO `t_menu` VALUES ('40', null, '首页', null, 'index', '3', 'tab', null, '623', '2017-10-10 15:38:59', '623', '0');
INSERT INTO `t_menu` VALUES ('41', '40', '欢迎页', '226', 'index', '1', 'tab', null, '623', '2017-11-23 09:08:17', '623', '0');
INSERT INTO `t_menu` VALUES ('42', null, '快捷菜单', null, 'quickMenu', '2', 'tab', null, '623', '2017-10-10 15:35:04', '623', '0');
INSERT INTO `t_menu` VALUES ('45', '42', '个人信息', '229', 'quickMenu', '2', 'window', '2017-10-12 14:19:23', '623', '2017-11-07 15:10:43', '623', '0');
INSERT INTO `t_menu` VALUES ('46', '42', '修改口令', '151', 'quickMenu', '1', 'window', '2017-10-12 15:13:34', '623', '2017-10-16 16:23:52', '623', '0');
INSERT INTO `t_menu` VALUES ('49', '37', '表单配置详情', '323', 'mainMenu', '2', 'tab', '2017-10-13 13:48:42', '623', '2017-12-19 16:27:02', '623', '0');
INSERT INTO `t_menu` VALUES ('50', '2', '人员统计', '217', 'mainMenu', '4', 'tab', '2017-10-16 14:07:41', '623', '2017-10-30 11:53:58', '623', '0');
INSERT INTO `t_menu` VALUES ('56', '37', '配置文件管理', '252', 'mainMenu', '1', 'tab', '2017-10-25 18:37:52', '623', '2017-10-25 18:37:52', '623', '0');
INSERT INTO `t_menu` VALUES ('59', '1', '系统管理', null, 'mainMenu', '7', '', '2017-10-30 11:52:13', '623', '2017-11-23 15:37:27', '623', '0');
INSERT INTO `t_menu` VALUES ('61', '2', '岗位管理', '281', 'mainMenu', '5', 'tab', '2017-11-15 16:40:39', '623', '2017-11-15 17:07:21', '623', '0');
INSERT INTO `t_menu` VALUES ('84', '42', 'markdown流程图', '320', 'quickMenu', '3', 'window', '2017-12-15 13:23:32', '623', '2017-12-15 13:24:43', '623', '0');
INSERT INTO `t_menu` VALUES ('86', '59', '异步任务日志', '322', 'mainMenu', '10', 'tab', '2017-12-18 10:29:10', '623', '2017-12-18 10:29:10', '623', '0');
INSERT INTO `t_menu` VALUES ('91', '42', '关于', '332', 'quickMenu', '3', 'window', '2017-12-28 17:11:03', '192', '2017-12-29 14:19:55', '192', '0');
INSERT INTO `t_menu` VALUES ('94', '2', '营销管理', '293', 'mainMenu', '6', 'tab', '2018-01-26 08:55:52', '623', '2018-01-26 08:55:52', '623', '0');
INSERT INTO `t_menu` VALUES ('105', '42', '帮助中心', '137', 'quickMenu', '1', 'window', '2018-03-27 09:26:06', '623', '2018-03-27 09:26:06', '623', '0');
INSERT INTO `t_menu` VALUES ('110', '37', '数据表列表', '347', 'mainMenu', '4', 'tab', '2018-04-11 09:57:52', '623', '2018-04-11 09:57:52', '623', '0');
INSERT INTO `t_menu` VALUES ('120', '2', '课程学习', '358', 'mainMenu', '7', 'tab', '2018-05-22 10:38:14', '623', '2018-05-22 10:43:12', '623', '0');
INSERT INTO `t_menu` VALUES ('124', '37', '自动生成文件', '363', 'mainMenu', '5', 'tab', '2018-06-04 17:18:47', '623', '2018-06-15 13:36:54', '626', '0');
INSERT INTO `t_menu` VALUES ('125', '37', '模拟请求', '364', 'mainMenu', '6', 'tab', '2018-06-05 10:11:45', '626', '2018-06-05 10:11:45', '626', '0');

-- ----------------------------
-- Table structure for `t_message_log`
-- ----------------------------
DROP TABLE IF EXISTS `t_message_log`;
CREATE TABLE `t_message_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `topic` varchar(50) DEFAULT NULL COMMENT '队列',
  `create_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `cost` bigint(20) DEFAULT NULL,
  `message_class` varchar(200) DEFAULT NULL COMMENT '消息类名',
  `message` varchar(500) DEFAULT NULL,
  `type` varchar(10) DEFAULT '' COMMENT 'send/consume',
  `status` smallint(6) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1126 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_message_log
-- ----------------------------

-- ----------------------------
-- Table structure for `t_nationality`
-- ----------------------------
DROP TABLE IF EXISTS `t_nationality`;
CREATE TABLE `t_nationality` (
  `code` varchar(20) NOT NULL COMMENT '编码',
  `name` varchar(50) NOT NULL COMMENT '名称',
  PRIMARY KEY (`code`),
  UNIQUE KEY `uk_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_nationality
-- ----------------------------
INSERT INTO `t_nationality` VALUES ('26', '东乡族');
INSERT INTO `t_nationality` VALUES ('43', '乌孜别克族');
INSERT INTO `t_nationality` VALUES ('49', '京族');
INSERT INTO `t_nationality` VALUES ('37', '仡佬族');
INSERT INTO `t_nationality` VALUES ('32', '仫佬族');
INSERT INTO `t_nationality` VALUES ('21', '佤族');
INSERT INTO `t_nationality` VALUES ('12', '侗族');
INSERT INTO `t_nationality` VALUES ('44', '俄罗斯族');
INSERT INTO `t_nationality` VALUES ('47', '保安族');
INSERT INTO `t_nationality` VALUES ('20', '傈僳族');
INSERT INTO `t_nationality` VALUES ('18', '傣族');
INSERT INTO `t_nationality` VALUES ('97', '其他');
INSERT INTO `t_nationality` VALUES ('16', '哈尼族');
INSERT INTO `t_nationality` VALUES ('17', '哈萨克族');
INSERT INTO `t_nationality` VALUES ('3', '回族');
INSERT INTO `t_nationality` VALUES ('15', '土家族');
INSERT INTO `t_nationality` VALUES ('30', '土族');
INSERT INTO `t_nationality` VALUES ('56', '基诺族');
INSERT INTO `t_nationality` VALUES ('41', '塔吉克族');
INSERT INTO `t_nationality` VALUES ('50', '塔塔尔族');
INSERT INTO `t_nationality` VALUES ('8', '壮族');
INSERT INTO `t_nationality` VALUES ('98', '外国血统');
INSERT INTO `t_nationality` VALUES ('46', '崩龙族');
INSERT INTO `t_nationality` VALUES ('9', '布依族');
INSERT INTO `t_nationality` VALUES ('34', '布朗族');
INSERT INTO `t_nationality` VALUES ('7', '彝族');
INSERT INTO `t_nationality` VALUES ('42', '怒族');
INSERT INTO `t_nationality` VALUES ('24', '拉祜族');
INSERT INTO `t_nationality` VALUES ('35', '撒拉族');
INSERT INTO `t_nationality` VALUES ('40', '普米族');
INSERT INTO `t_nationality` VALUES ('28', '景颇族');
INSERT INTO `t_nationality` VALUES ('10', '朝鲜族');
INSERT INTO `t_nationality` VALUES ('29', '柯尔克孜族');
INSERT INTO `t_nationality` VALUES ('36', '毛难族');
INSERT INTO `t_nationality` VALUES ('25', '水族');
INSERT INTO `t_nationality` VALUES ('1', '汉族');
INSERT INTO `t_nationality` VALUES ('11', '满族');
INSERT INTO `t_nationality` VALUES ('51', '独龙族');
INSERT INTO `t_nationality` VALUES ('55', '珞巴族');
INSERT INTO `t_nationality` VALUES ('13', '瑶族');
INSERT INTO `t_nationality` VALUES ('22', '畲族');
INSERT INTO `t_nationality` VALUES ('14', '白族');
INSERT INTO `t_nationality` VALUES ('27', '纳西族');
INSERT INTO `t_nationality` VALUES ('5', '维吾尔族');
INSERT INTO `t_nationality` VALUES ('33', '羌族');
INSERT INTO `t_nationality` VALUES ('6', '苗族');
INSERT INTO `t_nationality` VALUES ('2', '蒙古族');
INSERT INTO `t_nationality` VALUES ('4', '藏族');
INSERT INTO `t_nationality` VALUES ('48', '裕固族');
INSERT INTO `t_nationality` VALUES ('53', '赫哲族');
INSERT INTO `t_nationality` VALUES ('31', '达斡尔族');
INSERT INTO `t_nationality` VALUES ('52', '鄂伦春族');
INSERT INTO `t_nationality` VALUES ('45', '鄂温克族');
INSERT INTO `t_nationality` VALUES ('38', '锡伯族');
INSERT INTO `t_nationality` VALUES ('54', '门巴族');
INSERT INTO `t_nationality` VALUES ('39', '阿昌族');
INSERT INTO `t_nationality` VALUES ('23', '高山族');
INSERT INTO `t_nationality` VALUES ('19', '黎族');

-- ----------------------------
-- Table structure for `t_org`
-- ----------------------------
DROP TABLE IF EXISTS `t_org`;
CREATE TABLE `t_org` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `pid` bigint(20) DEFAULT NULL COMMENT '父节点ID',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `code` varchar(20) DEFAULT NULL COMMENT '编码',
  `fullname` varchar(50) DEFAULT NULL COMMENT '长名称',
  `ext_code` varchar(20) DEFAULT NULL COMMENT '外部编码',
  `phone` varchar(13) DEFAULT NULL COMMENT '电话',
  `fax` varchar(13) DEFAULT NULL COMMENT '传真',
  `address` varchar(200) DEFAULT NULL COMMENT '地址',
  `postcode` varchar(6) DEFAULT NULL COMMENT '邮政编码',
  `manager_id` bigint(20) DEFAULT NULL COMMENT '组织主管',
  `description` varchar(200) DEFAULT NULL COMMENT '描述',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint(50) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(50) DEFAULT NULL COMMENT '更新人',
  `update_count` tinyint(6) DEFAULT '0' COMMENT '版本号',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_name` (`name`),
  UNIQUE KEY `uk_code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=328 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_org
-- ----------------------------
INSERT INTO `t_org` VALUES ('290', null, '软件公司', '0000', '', '', '', '', '北京市海淀区北三环大钟寺中坤广场E座904室', '100098', '623', '', '2017-11-13 17:42:16', '623', '2018-03-12 14:27:47', '674', '0');
INSERT INTO `t_org` VALUES ('297', '290', '研发中心', '1000', '', '', '', '', '', '', '191', '', '2017-11-16 10:38:10', '623', '2018-04-19 09:33:50', '623', '0');
INSERT INTO `t_org` VALUES ('299', '297', '开发部', '1001', '', '', '', '', '', '', '191', '', '2017-11-16 10:40:10', '623', '2018-03-16 09:56:30', '674', '0');
INSERT INTO `t_org` VALUES ('301', '297', '产品部', '1003', '', '', '', '', '', '', null, '', '2017-11-16 10:41:20', '623', '2018-03-13 16:31:17', '674', '0');
INSERT INTO `t_org` VALUES ('305', '290', '人事部', '3100', '人力资源部', '', '', '', '', '', '192', '', '2017-12-14 14:54:11', '192', '2017-12-19 14:51:48', '192', '0');
INSERT INTO `t_org` VALUES ('306', '305', '行政部', '000', '000', '000', '000', '000', '000', '000', '629', '000', '2017-12-14 14:55:09', '192', '2017-12-15 14:46:42', '192', '0');
INSERT INTO `t_org` VALUES ('309', '290', '财务部', '001', '001', '001', '001', '001', '001', '001001', '634', '001', '2017-12-15 14:47:30', '192', '2018-01-17 12:34:29', '623', '0');
INSERT INTO `t_org` VALUES ('312', '305', '人力资源部', '.0', null, null, null, null, null, null, null, null, '2017-12-28 15:24:06', '192', '2017-12-28 15:24:12', '192', '0');
INSERT INTO `t_org` VALUES ('315', '309', '财务一部', '', '', '', '', '', '', '', null, '', '2018-01-17 12:34:24', '623', '2018-01-17 12:59:19', '623', '0');
INSERT INTO `t_org` VALUES ('317', '309', '财务二部', null, null, null, null, null, null, null, null, null, '2018-02-28 16:28:21', '623', '2018-02-28 16:28:29', '623', '0');
INSERT INTO `t_org` VALUES ('322', '299', '销售', '1234', '', '', '', '', '', '', null, '', '2018-03-22 13:37:25', '674', '2018-03-28 14:56:01', '674', '0');

-- ----------------------------
-- Table structure for `t_position`
-- ----------------------------
DROP TABLE IF EXISTS `t_position`;
CREATE TABLE `t_position` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `remark` varchar(200) DEFAULT NULL COMMENT '描述',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint(50) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(50) DEFAULT NULL COMMENT '更新人',
  `update_count` tinyint(6) DEFAULT '0' COMMENT '版本号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_position
-- ----------------------------
INSERT INTO `t_position` VALUES ('4', '大区培训经理', '', '2017-11-16 11:54:54', '623', '2018-05-16 15:41:08', '674', '0');
INSERT INTO `t_position` VALUES ('6', '开发工程', 'dadd', '2017-12-14 20:58:34', '628', '2018-06-26 14:43:05', '626', '0');
INSERT INTO `t_position` VALUES ('7', '测试工程师', '测试', '2017-12-15 14:52:13', '192', '2017-12-15 14:52:21', '192', '0');
INSERT INTO `t_position` VALUES ('9', '讲师', '001', '2017-12-28 15:29:29', '192', '2017-12-28 15:29:29', '192', '0');
INSERT INTO `t_position` VALUES ('10', 'ewa', null, '2018-03-13 16:49:27', '674', '2018-03-13 16:49:27', '674', '0');
INSERT INTO `t_position` VALUES ('11', 'ewea', 'huabusbd', '2018-03-13 16:50:22', '674', '2018-03-13 16:50:22', '674', '0');
INSERT INTO `t_position` VALUES ('13', '小弟弟', null, '2018-03-16 17:31:39', '674', '2018-03-16 17:31:39', '674', '0');
INSERT INTO `t_position` VALUES ('14', '122222', '<p>\r\n	<br />\r\n</p>\r\n<p>\r\n	<img src=\"http://localhost:8080/base-web/localDownload?methodType=markdown&fileId=893\" alt=\"\" />\r\n</p>', '2018-03-19 10:38:01', '674', '2018-04-13 12:46:53', '623', '0');
INSERT INTO `t_position` VALUES ('15', '1222222', '111111', '2018-03-19 13:20:49', '674', '2018-03-19 13:20:49', '674', '0');
INSERT INTO `t_position` VALUES ('16', '测试020', null, '2018-03-20 10:23:43', '674', '2018-03-20 10:23:43', '674', '0');
INSERT INTO `t_position` VALUES ('17', '撒大大大V', null, '2018-03-22 13:39:35', '674', '2018-03-22 13:39:35', '674', '0');
INSERT INTO `t_position` VALUES ('19', '777777777777', '777777777777', '2018-03-23 13:47:20', '674', '2018-03-23 13:47:20', '674', '0');
INSERT INTO `t_position` VALUES ('20', 'zuzhang', 'dadadadada', '2018-03-28 13:46:48', '674', '2018-03-28 13:46:48', '674', '0');
INSERT INTO `t_position` VALUES ('23', 'da', 'da', '2018-04-09 15:45:13', '674', '2018-04-09 15:45:13', '674', '0');
INSERT INTO `t_position` VALUES ('24', 'test', '', '2018-04-13 11:25:07', '623', '2018-04-13 11:25:15', '623', '0');
INSERT INTO `t_position` VALUES ('25', 'testaaaaaa', 'abc', '2018-04-13 11:25:30', '623', '2018-04-13 12:36:33', '623', '0');
INSERT INTO `t_position` VALUES ('26', '测试经理', '负责测试12', '2018-05-16 15:40:08', '674', '2018-05-16 15:40:08', '674', '0');

-- ----------------------------
-- Table structure for `t_post`
-- ----------------------------
DROP TABLE IF EXISTS `t_post`;
CREATE TABLE `t_post` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content` varchar(200) DEFAULT NULL COMMENT '内容',
  `create_by` bigint(20) DEFAULT NULL COMMENT '发帖人Id',
  `create_time` datetime DEFAULT NULL COMMENT '发贴时间',
  `course_id` bigint(20) DEFAULT NULL COMMENT '课程Id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_post
-- ----------------------------

-- ----------------------------
-- Table structure for `t_push`
-- ----------------------------
DROP TABLE IF EXISTS `t_push`;
CREATE TABLE `t_push` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT,
  `p_before` varchar(200) DEFAULT NULL,
  `after` varchar(200) DEFAULT NULL,
  `ref` varchar(200) DEFAULT NULL,
  `user_name` varchar(50) DEFAULT NULL,
  `user_email` varchar(50) DEFAULT NULL,
  `total_commits_count` smallint(10) DEFAULT NULL,
  `repository` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1435 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_push
-- ----------------------------

-- ----------------------------
-- Table structure for `t_res_func`
-- ----------------------------
DROP TABLE IF EXISTS `t_res_func`;
CREATE TABLE `t_res_func` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `res_id` bigint(20) NOT NULL COMMENT '资源ID',
  `func_id` bigint(20) NOT NULL COMMENT '功能ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint(50) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(50) DEFAULT NULL COMMENT '更新人',
  `update_count` tinyint(6) DEFAULT '0' COMMENT '版本号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1232 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_res_func
-- ----------------------------
INSERT INTO `t_res_func` VALUES ('794', '47', '217', '2017-11-23 13:41:25', '623', '2017-11-23 13:41:25', '623', '0');
INSERT INTO `t_res_func` VALUES ('795', '174', '217', '2017-11-23 13:41:31', '623', '2017-11-23 13:41:31', '623', '0');
INSERT INTO `t_res_func` VALUES ('796', '175', '217', '2017-11-23 13:41:35', '623', '2017-11-23 13:41:35', '623', '0');
INSERT INTO `t_res_func` VALUES ('797', '176', '217', '2017-11-23 13:41:39', '623', '2017-11-23 13:41:39', '623', '0');
INSERT INTO `t_res_func` VALUES ('798', '177', '217', '2017-11-23 13:41:43', '623', '2017-11-23 13:41:43', '623', '0');
INSERT INTO `t_res_func` VALUES ('799', '178', '217', '2017-11-23 13:41:48', '623', '2017-11-23 13:41:48', '623', '0');
INSERT INTO `t_res_func` VALUES ('800', '46', '217', '2017-11-23 13:42:28', '623', '2017-11-23 13:42:28', '623', '0');
INSERT INTO `t_res_func` VALUES ('801', '173', '217', '2017-11-23 13:42:33', '623', '2017-11-23 13:42:33', '623', '0');
INSERT INTO `t_res_func` VALUES ('802', '183', '217', '2017-11-23 13:42:37', '623', '2017-11-23 13:42:37', '623', '0');
INSERT INTO `t_res_func` VALUES ('803', '184', '217', '2017-11-23 13:42:41', '623', '2017-11-23 13:42:41', '623', '0');
INSERT INTO `t_res_func` VALUES ('804', '185', '217', '2017-11-23 13:42:46', '623', '2017-11-23 13:42:46', '623', '0');
INSERT INTO `t_res_func` VALUES ('805', '45', '217', '2017-11-23 13:43:01', '623', '2017-11-23 13:43:01', '623', '0');
INSERT INTO `t_res_func` VALUES ('806', '168', '217', '2017-11-23 13:43:05', '623', '2017-11-23 13:43:05', '623', '0');
INSERT INTO `t_res_func` VALUES ('807', '169', '217', '2017-11-23 13:43:09', '623', '2017-11-23 13:43:09', '623', '0');
INSERT INTO `t_res_func` VALUES ('808', '170', '217', '2017-11-23 13:43:14', '623', '2017-11-23 13:43:14', '623', '0');
INSERT INTO `t_res_func` VALUES ('809', '187', '217', '2017-11-23 13:43:19', '623', '2017-11-23 13:43:19', '623', '0');
INSERT INTO `t_res_func` VALUES ('811', '52', '217', '2017-11-23 13:44:10', '623', '2017-11-23 13:44:10', '623', '0');
INSERT INTO `t_res_func` VALUES ('812', '188', '217', '2017-11-23 13:44:14', '623', '2017-11-23 13:44:14', '623', '0');
INSERT INTO `t_res_func` VALUES ('813', '189', '217', '2017-11-23 13:44:23', '623', '2017-11-23 13:44:23', '623', '0');
INSERT INTO `t_res_func` VALUES ('814', '190', '217', '2017-11-23 13:44:37', '623', '2017-11-23 13:44:37', '623', '0');
INSERT INTO `t_res_func` VALUES ('815', '191', '217', '2017-11-23 13:44:40', '623', '2017-11-23 13:44:40', '623', '0');
INSERT INTO `t_res_func` VALUES ('816', '192', '217', '2017-11-23 13:44:44', '623', '2017-11-23 13:44:44', '623', '0');
INSERT INTO `t_res_func` VALUES ('817', '193', '217', '2017-11-23 13:44:49', '623', '2017-11-23 13:44:49', '623', '0');
INSERT INTO `t_res_func` VALUES ('818', '54', '217', '2017-11-23 13:45:22', '623', '2017-11-23 13:45:22', '623', '0');
INSERT INTO `t_res_func` VALUES ('820', '194', '217', '2017-11-23 13:45:30', '623', '2017-11-23 13:45:30', '623', '0');
INSERT INTO `t_res_func` VALUES ('821', '195', '217', '2017-11-23 13:45:34', '623', '2017-11-23 13:45:34', '623', '0');
INSERT INTO `t_res_func` VALUES ('822', '196', '217', '2017-11-23 13:45:38', '623', '2017-11-23 13:45:38', '623', '0');
INSERT INTO `t_res_func` VALUES ('823', '55', '217', '2017-11-23 13:46:09', '623', '2017-11-23 13:46:09', '623', '0');
INSERT INTO `t_res_func` VALUES ('824', '197', '217', '2017-11-23 13:46:13', '623', '2017-11-23 13:46:13', '623', '0');
INSERT INTO `t_res_func` VALUES ('825', '198', '217', '2017-11-23 13:46:18', '623', '2017-11-23 13:46:18', '623', '0');
INSERT INTO `t_res_func` VALUES ('826', '199', '217', '2017-11-23 13:46:22', '623', '2017-11-23 13:46:22', '623', '0');
INSERT INTO `t_res_func` VALUES ('827', '252', '218', '2017-11-23 13:47:40', '623', '2017-11-23 13:47:40', '623', '0');
INSERT INTO `t_res_func` VALUES ('828', '289', '218', '2017-11-23 13:48:06', '623', '2017-11-23 13:48:06', '623', '0');
INSERT INTO `t_res_func` VALUES ('829', '213', '219', '2017-11-23 13:53:54', '623', '2017-11-23 13:53:54', '623', '0');
INSERT INTO `t_res_func` VALUES ('830', '244', '219', '2017-11-23 13:54:13', '623', '2017-11-23 13:54:13', '623', '0');
INSERT INTO `t_res_func` VALUES ('831', '24', '193', '2017-11-23 14:30:05', '623', '2017-11-23 14:30:05', '623', '0');
INSERT INTO `t_res_func` VALUES ('832', '155', '193', '2017-11-23 14:30:19', '623', '2017-11-23 14:30:19', '623', '0');
INSERT INTO `t_res_func` VALUES ('833', '156', '193', '2017-11-23 14:30:23', '623', '2017-11-23 14:30:23', '623', '0');
INSERT INTO `t_res_func` VALUES ('834', '157', '193', '2017-11-23 14:30:28', '623', '2017-11-23 14:30:28', '623', '0');
INSERT INTO `t_res_func` VALUES ('835', '207', '193', '2017-11-23 14:30:32', '623', '2017-11-23 14:30:32', '623', '0');
INSERT INTO `t_res_func` VALUES ('836', '206', '193', '2017-11-23 14:30:35', '623', '2017-11-23 14:30:35', '623', '0');
INSERT INTO `t_res_func` VALUES ('837', '208', '193', '2017-11-23 14:30:39', '623', '2017-11-23 14:30:39', '623', '0');
INSERT INTO `t_res_func` VALUES ('838', '209', '193', '2017-11-23 14:30:43', '623', '2017-11-23 14:30:43', '623', '0');
INSERT INTO `t_res_func` VALUES ('839', '210', '193', '2017-11-23 14:30:48', '623', '2017-11-23 14:30:48', '623', '0');
INSERT INTO `t_res_func` VALUES ('840', '211', '193', '2017-11-23 14:30:52', '623', '2017-11-23 14:30:52', '623', '0');
INSERT INTO `t_res_func` VALUES ('841', '212', '193', '2017-11-23 14:30:56', '623', '2017-11-23 14:30:56', '623', '0');
INSERT INTO `t_res_func` VALUES ('842', '24', '194', '2017-11-23 14:31:31', '623', '2017-11-23 14:31:31', '623', '0');
INSERT INTO `t_res_func` VALUES ('843', '219', '195', '2017-11-23 14:31:48', '623', '2017-11-23 14:31:48', '623', '0');
INSERT INTO `t_res_func` VALUES ('845', '225', '195', '2017-11-23 14:32:07', '623', '2017-11-23 14:32:07', '623', '0');
INSERT INTO `t_res_func` VALUES ('846', '228', '195', '2017-11-23 14:32:11', '623', '2017-11-23 14:32:11', '623', '0');
INSERT INTO `t_res_func` VALUES ('847', '255', '195', '2017-11-23 14:32:17', '623', '2017-11-23 14:32:17', '623', '0');
INSERT INTO `t_res_func` VALUES ('848', '256', '195', '2017-11-23 14:32:21', '623', '2017-11-23 14:32:21', '623', '0');
INSERT INTO `t_res_func` VALUES ('849', '257', '195', '2017-11-23 14:32:25', '623', '2017-11-23 14:32:25', '623', '0');
INSERT INTO `t_res_func` VALUES ('850', '258', '195', '2017-11-23 14:32:30', '623', '2017-11-23 14:32:30', '623', '0');
INSERT INTO `t_res_func` VALUES ('851', '276', '195', '2017-11-23 14:32:34', '623', '2017-11-23 14:32:34', '623', '0');
INSERT INTO `t_res_func` VALUES ('852', '215', '196', '2017-11-23 14:40:33', '623', '2017-11-23 14:40:33', '623', '0');
INSERT INTO `t_res_func` VALUES ('853', '234', '196', '2017-11-23 14:40:38', '623', '2017-11-23 14:40:38', '623', '0');
INSERT INTO `t_res_func` VALUES ('854', '235', '197', '2017-11-23 14:41:05', '623', '2017-11-23 14:41:05', '623', '0');
INSERT INTO `t_res_func` VALUES ('855', '215', '197', '2017-11-23 14:41:10', '623', '2017-11-23 14:41:10', '623', '0');
INSERT INTO `t_res_func` VALUES ('856', '260', '197', '2017-11-23 14:41:19', '623', '2017-11-23 14:41:19', '623', '0');
INSERT INTO `t_res_func` VALUES ('857', '215', '198', '2017-11-23 14:41:31', '623', '2017-11-23 14:41:31', '623', '0');
INSERT INTO `t_res_func` VALUES ('858', '236', '198', '2017-11-23 14:41:36', '623', '2017-11-23 14:41:36', '623', '0');
INSERT INTO `t_res_func` VALUES ('859', '259', '198', '2017-11-23 14:41:43', '623', '2017-11-23 14:41:43', '623', '0');
INSERT INTO `t_res_func` VALUES ('860', '237', '199', '2017-11-23 14:42:14', '623', '2017-11-23 14:42:14', '623', '0');
INSERT INTO `t_res_func` VALUES ('861', '215', '199', '2017-11-23 14:42:19', '623', '2017-11-23 14:42:19', '623', '0');
INSERT INTO `t_res_func` VALUES ('862', '261', '199', '2017-11-23 14:42:26', '623', '2017-11-23 14:42:26', '623', '0');
INSERT INTO `t_res_func` VALUES ('863', '215', '200', '2017-11-23 14:42:39', '623', '2017-11-23 14:42:39', '623', '0');
INSERT INTO `t_res_func` VALUES ('864', '238', '200', '2017-11-23 14:42:44', '623', '2017-11-23 14:42:44', '623', '0');
INSERT INTO `t_res_func` VALUES ('865', '262', '200', '2017-11-23 14:42:54', '623', '2017-11-23 14:42:54', '623', '0');
INSERT INTO `t_res_func` VALUES ('866', '215', '201', '2017-11-23 14:43:26', '623', '2017-11-23 14:43:26', '623', '0');
INSERT INTO `t_res_func` VALUES ('867', '239', '201', '2017-11-23 14:43:41', '623', '2017-11-23 14:43:41', '623', '0');
INSERT INTO `t_res_func` VALUES ('868', '263', '201', '2017-11-23 14:43:49', '623', '2017-11-23 14:43:49', '623', '0');
INSERT INTO `t_res_func` VALUES ('869', '215', '202', '2017-11-23 14:44:04', '623', '2017-11-23 14:44:04', '623', '0');
INSERT INTO `t_res_func` VALUES ('870', '240', '202', '2017-11-23 14:44:12', '623', '2017-11-23 14:44:12', '623', '0');
INSERT INTO `t_res_func` VALUES ('871', '264', '202', '2017-11-23 14:44:18', '623', '2017-11-23 14:44:18', '623', '0');
INSERT INTO `t_res_func` VALUES ('872', '215', '203', '2017-11-23 14:45:26', '623', '2017-11-23 14:45:26', '623', '0');
INSERT INTO `t_res_func` VALUES ('873', '241', '203', '2017-11-23 14:45:32', '623', '2017-11-23 14:45:32', '623', '0');
INSERT INTO `t_res_func` VALUES ('874', '265', '203', '2017-11-23 14:45:39', '623', '2017-11-23 14:45:39', '623', '0');
INSERT INTO `t_res_func` VALUES ('875', '215', '204', '2017-11-23 14:46:00', '623', '2017-11-23 14:46:00', '623', '0');
INSERT INTO `t_res_func` VALUES ('876', '242', '204', '2017-11-23 14:46:08', '623', '2017-11-23 14:46:08', '623', '0');
INSERT INTO `t_res_func` VALUES ('877', '266', '204', '2017-11-23 14:46:15', '623', '2017-11-23 14:46:15', '623', '0');
INSERT INTO `t_res_func` VALUES ('878', '215', '205', '2017-11-23 14:46:41', '623', '2017-11-23 14:46:41', '623', '0');
INSERT INTO `t_res_func` VALUES ('879', '267', '206', '2017-11-23 14:47:06', '623', '2017-11-23 14:47:06', '623', '0');
INSERT INTO `t_res_func` VALUES ('880', '215', '206', '2017-11-23 14:47:17', '623', '2017-11-23 14:47:17', '623', '0');
INSERT INTO `t_res_func` VALUES ('881', '215', '207', '2017-11-23 14:47:34', '623', '2017-11-23 14:47:34', '623', '0');
INSERT INTO `t_res_func` VALUES ('882', '268', '208', '2017-11-23 14:48:04', '623', '2017-11-23 14:48:04', '623', '0');
INSERT INTO `t_res_func` VALUES ('883', '215', '208', '2017-11-23 14:48:11', '623', '2017-11-23 14:48:11', '623', '0');
INSERT INTO `t_res_func` VALUES ('884', '215', '209', '2017-11-23 14:49:30', '623', '2017-11-23 14:49:30', '623', '0');
INSERT INTO `t_res_func` VALUES ('885', '213', '211', '2017-11-23 14:49:54', '623', '2017-11-23 14:49:54', '623', '0');
INSERT INTO `t_res_func` VALUES ('886', '244', '211', '2017-11-23 14:49:58', '623', '2017-11-23 14:49:58', '623', '0');
INSERT INTO `t_res_func` VALUES ('887', '245', '211', '2017-11-23 14:50:02', '623', '2017-11-23 14:50:02', '623', '0');
INSERT INTO `t_res_func` VALUES ('888', '246', '211', '2017-11-23 14:50:06', '623', '2017-11-23 14:50:06', '623', '0');
INSERT INTO `t_res_func` VALUES ('889', '247', '211', '2017-11-23 14:50:10', '623', '2017-11-23 14:50:10', '623', '0');
INSERT INTO `t_res_func` VALUES ('890', '277', '211', '2017-11-23 14:50:41', '623', '2017-11-23 14:50:41', '623', '0');
INSERT INTO `t_res_func` VALUES ('891', '269', '211', '2017-11-23 14:50:44', '623', '2017-11-23 14:50:44', '623', '0');
INSERT INTO `t_res_func` VALUES ('892', '270', '211', '2017-11-23 14:50:57', '623', '2017-11-23 14:50:57', '623', '0');
INSERT INTO `t_res_func` VALUES ('893', '271', '211', '2017-11-23 14:51:04', '623', '2017-11-23 14:51:04', '623', '0');
INSERT INTO `t_res_func` VALUES ('894', '274', '211', '2017-11-23 14:51:09', '623', '2017-11-23 14:51:09', '623', '0');
INSERT INTO `t_res_func` VALUES ('895', '275', '211', '2017-11-23 14:51:14', '623', '2017-11-23 14:51:14', '623', '0');
INSERT INTO `t_res_func` VALUES ('896', '213', '212', '2017-11-23 14:51:58', '623', '2017-11-23 14:51:58', '623', '0');
INSERT INTO `t_res_func` VALUES ('898', '272', '212', '2017-11-23 14:52:49', '623', '2017-11-23 14:52:49', '623', '0');
INSERT INTO `t_res_func` VALUES ('899', '275', '212', '2017-11-23 14:52:53', '623', '2017-11-23 14:52:53', '623', '0');
INSERT INTO `t_res_func` VALUES ('900', '222', '213', '2017-11-23 14:53:30', '623', '2017-11-23 14:53:30', '623', '0');
INSERT INTO `t_res_func` VALUES ('901', '223', '213', '2017-11-23 14:53:34', '623', '2017-11-23 14:53:34', '623', '0');
INSERT INTO `t_res_func` VALUES ('902', '224', '213', '2017-11-23 14:53:38', '623', '2017-11-23 14:53:38', '623', '0');
INSERT INTO `t_res_func` VALUES ('903', '225', '213', '2017-11-23 14:53:42', '623', '2017-11-23 14:53:42', '623', '0');
INSERT INTO `t_res_func` VALUES ('904', '251', '213', '2017-11-23 14:53:51', '623', '2017-11-23 14:53:51', '623', '0');
INSERT INTO `t_res_func` VALUES ('905', '218', '213', '2017-11-23 14:54:37', '623', '2017-11-23 14:54:37', '623', '0');
INSERT INTO `t_res_func` VALUES ('906', '220', '213', '2017-11-23 14:54:57', '623', '2017-11-23 14:54:57', '623', '0');
INSERT INTO `t_res_func` VALUES ('907', '221', '213', '2017-11-23 14:55:13', '623', '2017-11-23 14:55:13', '623', '0');
INSERT INTO `t_res_func` VALUES ('908', '226', '214', '2017-11-23 14:56:11', '623', '2017-11-23 14:56:11', '623', '0');
INSERT INTO `t_res_func` VALUES ('909', '51', '215', '2017-11-23 14:56:25', '623', '2017-11-23 14:56:25', '623', '0');
INSERT INTO `t_res_func` VALUES ('910', '144', '215', '2017-11-23 14:56:29', '623', '2017-11-23 14:56:29', '623', '0');
INSERT INTO `t_res_func` VALUES ('911', '162', '215', '2017-11-23 14:56:32', '623', '2017-11-23 14:56:32', '623', '0');
INSERT INTO `t_res_func` VALUES ('912', '163', '215', '2017-11-23 14:56:36', '623', '2017-11-23 14:56:36', '623', '0');
INSERT INTO `t_res_func` VALUES ('913', '164', '215', '2017-11-23 14:56:39', '623', '2017-11-23 14:56:39', '623', '0');
INSERT INTO `t_res_func` VALUES ('914', '165', '215', '2017-11-23 14:56:43', '623', '2017-11-23 14:56:43', '623', '0');
INSERT INTO `t_res_func` VALUES ('915', '166', '215', '2017-11-23 14:56:47', '623', '2017-11-23 14:56:47', '623', '0');
INSERT INTO `t_res_func` VALUES ('916', '167', '215', '2017-11-23 14:56:51', '623', '2017-11-23 14:56:51', '623', '0');
INSERT INTO `t_res_func` VALUES ('917', '217', '215', '2017-11-23 14:56:56', '623', '2017-11-23 14:56:56', '623', '0');
INSERT INTO `t_res_func` VALUES ('918', '281', '215', '2017-11-23 14:57:04', '623', '2017-11-23 14:57:04', '623', '0');
INSERT INTO `t_res_func` VALUES ('919', '282', '215', '2017-11-23 14:57:11', '623', '2017-11-23 14:57:11', '623', '0');
INSERT INTO `t_res_func` VALUES ('920', '283', '215', '2017-11-23 14:57:15', '623', '2017-11-23 14:57:15', '623', '0');
INSERT INTO `t_res_func` VALUES ('921', '284', '215', '2017-11-23 14:57:19', '623', '2017-11-23 14:57:19', '623', '0');
INSERT INTO `t_res_func` VALUES ('922', '285', '215', '2017-11-23 14:57:23', '623', '2017-11-23 14:57:23', '623', '0');
INSERT INTO `t_res_func` VALUES ('923', '286', '215', '2017-11-23 14:57:27', '623', '2017-11-23 14:57:27', '623', '0');
INSERT INTO `t_res_func` VALUES ('925', '50', '216', '2017-11-23 14:58:02', '623', '2017-11-23 14:58:02', '623', '0');
INSERT INTO `t_res_func` VALUES ('926', '92', '216', '2017-11-23 14:58:06', '623', '2017-11-23 14:58:06', '623', '0');
INSERT INTO `t_res_func` VALUES ('927', '231', '216', '2017-11-23 14:58:10', '623', '2017-11-23 14:58:10', '623', '0');
INSERT INTO `t_res_func` VALUES ('928', '49', '217', '2017-11-23 14:58:51', '623', '2017-11-23 14:58:51', '623', '0');
INSERT INTO `t_res_func` VALUES ('929', '158', '217', '2017-11-23 14:58:55', '623', '2017-11-23 14:58:55', '623', '0');
INSERT INTO `t_res_func` VALUES ('930', '159', '217', '2017-11-23 14:58:59', '623', '2017-11-23 14:58:59', '623', '0');
INSERT INTO `t_res_func` VALUES ('931', '160', '217', '2017-11-23 14:59:03', '623', '2017-11-23 14:59:03', '623', '0');
INSERT INTO `t_res_func` VALUES ('932', '161', '217', '2017-11-23 14:59:07', '623', '2017-11-23 14:59:07', '623', '0');
INSERT INTO `t_res_func` VALUES ('933', '91', '218', '2017-11-23 15:00:01', '623', '2017-11-23 15:00:01', '623', '0');
INSERT INTO `t_res_func` VALUES ('934', '200', '218', '2017-11-23 15:00:04', '623', '2017-11-23 15:00:04', '623', '0');
INSERT INTO `t_res_func` VALUES ('935', '202', '218', '2017-11-23 15:00:07', '623', '2017-11-23 15:00:07', '623', '0');
INSERT INTO `t_res_func` VALUES ('936', '203', '218', '2017-11-23 15:00:11', '623', '2017-11-23 15:00:11', '623', '0');
INSERT INTO `t_res_func` VALUES ('937', '204', '218', '2017-11-23 15:00:15', '623', '2017-11-23 15:00:15', '623', '0');
INSERT INTO `t_res_func` VALUES ('938', '205', '218', '2017-11-23 15:00:19', '623', '2017-11-23 15:00:19', '623', '0');
INSERT INTO `t_res_func` VALUES ('939', '93', '216', '2017-11-23 15:01:22', '623', '2017-11-23 15:01:22', '623', '0');
INSERT INTO `t_res_func` VALUES ('940', '53', '216', '2017-11-23 15:03:37', '623', '2017-11-23 15:03:37', '623', '0');
INSERT INTO `t_res_func` VALUES ('941', '295', '209', '2017-11-24 15:26:00', '623', '2017-11-24 15:26:00', '623', '0');
INSERT INTO `t_res_func` VALUES ('942', '294', '207', '2017-11-24 15:26:17', '623', '2017-11-24 15:26:17', '623', '0');
INSERT INTO `t_res_func` VALUES ('943', '299', '220', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('944', '316', '221', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('945', '317', '221', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('946', '318', '221', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('947', '322', '216', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('948', '300', '224', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('949', '323', '225', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('950', '314', '226', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('951', '49', '194', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('952', '24', '227', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('953', '45', '227', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('954', '46', '227', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('955', '47', '227', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('956', '49', '227', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('957', '50', '227', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('958', '51', '227', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('959', '52', '227', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('960', '53', '227', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('961', '54', '227', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('962', '55', '227', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('963', '91', '227', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('964', '92', '227', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('965', '93', '227', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('966', '144', '227', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('967', '145', '227', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('968', '155', '227', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('969', '156', '227', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('970', '157', '227', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('971', '158', '227', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('972', '159', '227', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('973', '160', '227', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('974', '161', '227', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('975', '162', '227', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('976', '163', '227', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('977', '164', '227', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('978', '165', '227', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('979', '166', '227', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('980', '167', '227', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('981', '168', '227', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('982', '169', '227', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('983', '170', '227', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('984', '171', '227', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('985', '172', '227', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('986', '173', '227', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('987', '174', '227', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('988', '175', '227', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('989', '176', '227', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('990', '177', '227', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('991', '178', '227', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('992', '179', '227', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('993', '180', '227', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('994', '181', '227', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('995', '182', '227', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('996', '183', '227', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('997', '184', '227', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('998', '185', '227', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('999', '186', '227', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('1000', '187', '227', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('1001', '188', '227', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('1002', '329', '228', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('1003', '330', '229', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('1004', '45', '231', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('1005', '46', '231', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('1006', '47', '231', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('1007', '53', '231', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('1008', '226', '231', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('1009', '215', '231', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('1010', '231', '231', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('1011', '234', '231', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('1012', '235', '231', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('1013', '236', '231', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('1014', '237', '231', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('1015', '238', '231', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('1016', '239', '231', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('1017', '240', '231', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('1018', '241', '231', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('1019', '242', '231', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('1020', '259', '231', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('1021', '260', '231', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('1022', '261', '231', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('1023', '262', '231', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('1024', '263', '231', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('1025', '264', '231', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('1026', '265', '231', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('1027', '266', '231', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('1028', '267', '231', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('1029', '268', '231', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('1030', '294', '231', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('1031', '295', '231', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('1032', '314', '231', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('1033', '251', '231', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('1034', '24', '231', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('1035', '219', '231', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('1036', '220', '231', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('1037', '225', '231', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('1038', '228', '231', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('1039', '255', '231', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('1040', '256', '231', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('1041', '257', '231', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('1042', '258', '231', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('1043', '276', '231', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('1044', '218', '231', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('1045', '174', '231', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('1046', '175', '231', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('1047', '176', '231', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('1048', '177', '231', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('1049', '178', '231', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('1050', '173', '231', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('1051', '183', '231', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('1052', '184', '231', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('1053', '185', '231', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('1054', '168', '231', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('1055', '169', '231', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('1056', '170', '231', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('1057', '187', '231', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('1058', '286', '231', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('1059', '155', '231', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('1060', '156', '231', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('1061', '157', '231', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('1062', '206', '231', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('1063', '207', '231', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('1064', '208', '231', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('1065', '209', '231', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('1066', '210', '231', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('1067', '211', '231', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('1068', '212', '231', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('1070', '334', '233', null, null, null, null, '0');
INSERT INTO `t_res_func` VALUES ('1075', '334', '193', '2018-03-01 18:12:27', '623', '2018-03-01 18:12:27', '623', '0');
INSERT INTO `t_res_func` VALUES ('1076', '335', '234', '2018-03-01 19:40:17', '623', '2018-03-01 19:40:17', '623', '0');
INSERT INTO `t_res_func` VALUES ('1077', '333', '235', '2018-03-02 10:50:11', '623', '2018-03-02 10:50:11', '623', '0');
INSERT INTO `t_res_func` VALUES ('1078', '49', '193', '2018-03-05 16:38:42', '623', '2018-03-05 16:38:42', '623', '0');
INSERT INTO `t_res_func` VALUES ('1086', '24', '198', '2018-03-12 15:33:29', '674', '2018-03-12 15:33:29', '674', '0');
INSERT INTO `t_res_func` VALUES ('1087', '51', '198', '2018-03-12 15:37:46', '674', '2018-03-12 15:37:46', '674', '0');
INSERT INTO `t_res_func` VALUES ('1089', '47', '238', '2018-03-13 16:20:32', '674', '2018-03-13 16:20:32', '674', '0');
INSERT INTO `t_res_func` VALUES ('1090', '226', '240', '2018-03-15 17:58:30', '623', '2018-03-15 17:58:30', '623', '0');
INSERT INTO `t_res_func` VALUES ('1091', '333', '240', '2018-03-15 18:01:05', '623', '2018-03-15 18:01:05', '623', '0');
INSERT INTO `t_res_func` VALUES ('1092', '215', '240', '2018-03-15 18:01:23', '623', '2018-03-15 18:01:23', '623', '0');
INSERT INTO `t_res_func` VALUES ('1093', '251', '240', '2018-03-15 18:01:43', '623', '2018-03-15 18:01:43', '623', '0');
INSERT INTO `t_res_func` VALUES ('1094', '231', '240', '2018-03-15 18:02:34', '623', '2018-03-15 18:02:34', '623', '0');
INSERT INTO `t_res_func` VALUES ('1095', '335', '240', '2018-03-15 18:22:16', '623', '2018-03-15 18:22:16', '623', '0');
INSERT INTO `t_res_func` VALUES ('1096', '324', '240', '2018-03-15 18:22:54', '623', '2018-03-15 18:22:54', '623', '0');
INSERT INTO `t_res_func` VALUES ('1097', '296', '240', '2018-03-15 18:23:03', '623', '2018-03-15 18:23:03', '623', '0');
INSERT INTO `t_res_func` VALUES ('1103', '49', '240', '2018-03-15 18:28:13', '623', '2018-03-15 18:28:13', '623', '0');
INSERT INTO `t_res_func` VALUES ('1104', '51', '240', '2018-03-15 18:28:20', '623', '2018-03-15 18:28:20', '623', '0');
INSERT INTO `t_res_func` VALUES ('1105', '217', '240', '2018-03-15 18:28:27', '623', '2018-03-15 18:28:27', '623', '0');
INSERT INTO `t_res_func` VALUES ('1106', '144', '240', '2018-03-15 18:28:37', '623', '2018-03-15 18:28:37', '623', '0');
INSERT INTO `t_res_func` VALUES ('1107', '281', '240', '2018-03-15 18:28:49', '623', '2018-03-15 18:28:49', '623', '0');
INSERT INTO `t_res_func` VALUES ('1108', '45', '240', '2018-03-15 18:29:12', '623', '2018-03-15 18:29:12', '623', '0');
INSERT INTO `t_res_func` VALUES ('1109', '53', '240', '2018-03-15 18:29:21', '623', '2018-03-15 18:29:21', '623', '0');
INSERT INTO `t_res_func` VALUES ('1110', '24', '246', '2018-03-27 09:26:37', '623', '2018-03-27 09:26:37', '623', '0');
INSERT INTO `t_res_func` VALUES ('1111', '45', '246', '2018-03-27 09:26:37', '623', '2018-03-27 09:26:37', '623', '0');
INSERT INTO `t_res_func` VALUES ('1112', '46', '246', '2018-03-27 09:26:37', '623', '2018-03-27 09:26:37', '623', '0');
INSERT INTO `t_res_func` VALUES ('1113', '47', '246', '2018-03-27 09:26:37', '623', '2018-03-27 09:26:37', '623', '0');
INSERT INTO `t_res_func` VALUES ('1114', '49', '246', '2018-03-27 09:26:37', '623', '2018-03-27 09:26:37', '623', '0');
INSERT INTO `t_res_func` VALUES ('1115', '50', '246', '2018-03-27 09:26:37', '623', '2018-03-27 09:26:37', '623', '0');
INSERT INTO `t_res_func` VALUES ('1116', '51', '246', '2018-03-27 09:26:37', '623', '2018-03-27 09:26:37', '623', '0');
INSERT INTO `t_res_func` VALUES ('1117', '52', '246', '2018-03-27 09:26:37', '623', '2018-03-27 09:26:37', '623', '0');
INSERT INTO `t_res_func` VALUES ('1118', '53', '246', '2018-03-27 09:26:37', '623', '2018-03-27 09:26:37', '623', '0');
INSERT INTO `t_res_func` VALUES ('1119', '54', '246', '2018-03-27 09:26:37', '623', '2018-03-27 09:26:37', '623', '0');
INSERT INTO `t_res_func` VALUES ('1120', '55', '246', '2018-03-27 09:26:37', '623', '2018-03-27 09:26:37', '623', '0');
INSERT INTO `t_res_func` VALUES ('1121', '91', '246', '2018-03-27 09:26:37', '623', '2018-03-27 09:26:37', '623', '0');
INSERT INTO `t_res_func` VALUES ('1122', '92', '246', '2018-03-27 09:26:37', '623', '2018-03-27 09:26:37', '623', '0');
INSERT INTO `t_res_func` VALUES ('1123', '93', '246', '2018-03-27 09:26:37', '623', '2018-03-27 09:26:37', '623', '0');
INSERT INTO `t_res_func` VALUES ('1124', '144', '246', '2018-03-27 09:26:37', '623', '2018-03-27 09:26:37', '623', '0');
INSERT INTO `t_res_func` VALUES ('1125', '145', '246', '2018-03-27 09:26:37', '623', '2018-03-27 09:26:37', '623', '0');
INSERT INTO `t_res_func` VALUES ('1126', '155', '246', '2018-03-27 09:26:37', '623', '2018-03-27 09:26:37', '623', '0');
INSERT INTO `t_res_func` VALUES ('1127', '156', '246', '2018-03-27 09:26:37', '623', '2018-03-27 09:26:37', '623', '0');
INSERT INTO `t_res_func` VALUES ('1128', '157', '246', '2018-03-27 09:26:37', '623', '2018-03-27 09:26:37', '623', '0');
INSERT INTO `t_res_func` VALUES ('1129', '158', '246', '2018-03-27 09:26:37', '623', '2018-03-27 09:26:37', '623', '0');
INSERT INTO `t_res_func` VALUES ('1130', '159', '246', '2018-03-27 09:26:37', '623', '2018-03-27 09:26:37', '623', '0');
INSERT INTO `t_res_func` VALUES ('1131', '160', '246', '2018-03-27 09:26:37', '623', '2018-03-27 09:26:37', '623', '0');
INSERT INTO `t_res_func` VALUES ('1132', '161', '246', '2018-03-27 09:26:37', '623', '2018-03-27 09:26:37', '623', '0');
INSERT INTO `t_res_func` VALUES ('1133', '162', '246', '2018-03-27 09:26:37', '623', '2018-03-27 09:26:37', '623', '0');
INSERT INTO `t_res_func` VALUES ('1134', '163', '246', '2018-03-27 09:26:37', '623', '2018-03-27 09:26:37', '623', '0');
INSERT INTO `t_res_func` VALUES ('1135', '164', '246', '2018-03-27 09:26:37', '623', '2018-03-27 09:26:37', '623', '0');
INSERT INTO `t_res_func` VALUES ('1136', '165', '246', '2018-03-27 09:26:37', '623', '2018-03-27 09:26:37', '623', '0');
INSERT INTO `t_res_func` VALUES ('1137', '166', '246', '2018-03-27 09:26:37', '623', '2018-03-27 09:26:37', '623', '0');
INSERT INTO `t_res_func` VALUES ('1138', '167', '246', '2018-03-27 09:26:37', '623', '2018-03-27 09:26:37', '623', '0');
INSERT INTO `t_res_func` VALUES ('1139', '168', '246', '2018-03-27 09:26:37', '623', '2018-03-27 09:26:37', '623', '0');
INSERT INTO `t_res_func` VALUES ('1140', '169', '246', '2018-03-27 09:26:37', '623', '2018-03-27 09:26:37', '623', '0');
INSERT INTO `t_res_func` VALUES ('1141', '170', '246', '2018-03-27 09:26:37', '623', '2018-03-27 09:26:37', '623', '0');
INSERT INTO `t_res_func` VALUES ('1142', '171', '246', '2018-03-27 09:26:37', '623', '2018-03-27 09:26:37', '623', '0');
INSERT INTO `t_res_func` VALUES ('1143', '172', '246', '2018-03-27 09:26:37', '623', '2018-03-27 09:26:37', '623', '0');
INSERT INTO `t_res_func` VALUES ('1144', '173', '246', '2018-03-27 09:26:37', '623', '2018-03-27 09:26:37', '623', '0');
INSERT INTO `t_res_func` VALUES ('1145', '174', '246', '2018-03-27 09:26:37', '623', '2018-03-27 09:26:37', '623', '0');
INSERT INTO `t_res_func` VALUES ('1146', '175', '246', '2018-03-27 09:26:37', '623', '2018-03-27 09:26:37', '623', '0');
INSERT INTO `t_res_func` VALUES ('1147', '176', '246', '2018-03-27 09:26:37', '623', '2018-03-27 09:26:37', '623', '0');
INSERT INTO `t_res_func` VALUES ('1148', '177', '246', '2018-03-27 09:26:37', '623', '2018-03-27 09:26:37', '623', '0');
INSERT INTO `t_res_func` VALUES ('1149', '178', '246', '2018-03-27 09:26:37', '623', '2018-03-27 09:26:37', '623', '0');
INSERT INTO `t_res_func` VALUES ('1150', '179', '246', '2018-03-27 09:26:37', '623', '2018-03-27 09:26:37', '623', '0');
INSERT INTO `t_res_func` VALUES ('1151', '180', '246', '2018-03-27 09:26:37', '623', '2018-03-27 09:26:37', '623', '0');
INSERT INTO `t_res_func` VALUES ('1152', '181', '246', '2018-03-27 09:26:37', '623', '2018-03-27 09:26:37', '623', '0');
INSERT INTO `t_res_func` VALUES ('1153', '182', '246', '2018-03-27 09:26:37', '623', '2018-03-27 09:26:37', '623', '0');
INSERT INTO `t_res_func` VALUES ('1154', '183', '246', '2018-03-27 09:26:37', '623', '2018-03-27 09:26:37', '623', '0');
INSERT INTO `t_res_func` VALUES ('1155', '184', '246', '2018-03-27 09:26:37', '623', '2018-03-27 09:26:37', '623', '0');
INSERT INTO `t_res_func` VALUES ('1156', '185', '246', '2018-03-27 09:26:37', '623', '2018-03-27 09:26:37', '623', '0');
INSERT INTO `t_res_func` VALUES ('1157', '186', '246', '2018-03-27 09:26:37', '623', '2018-03-27 09:26:37', '623', '0');
INSERT INTO `t_res_func` VALUES ('1158', '187', '246', '2018-03-27 09:26:37', '623', '2018-03-27 09:26:37', '623', '0');
INSERT INTO `t_res_func` VALUES ('1159', '188', '246', '2018-03-27 09:26:37', '623', '2018-03-27 09:26:37', '623', '0');
INSERT INTO `t_res_func` VALUES ('1160', '24', '244', '2018-03-28 11:09:24', '674', '2018-03-28 11:09:24', '674', '0');
INSERT INTO `t_res_func` VALUES ('1161', '24', '249', '2018-04-09 16:13:57', '674', '2018-04-09 16:13:57', '674', '0');
INSERT INTO `t_res_func` VALUES ('1162', '45', '249', '2018-04-09 16:13:57', '674', '2018-04-09 16:13:57', '674', '0');
INSERT INTO `t_res_func` VALUES ('1163', '46', '249', '2018-04-09 16:13:57', '674', '2018-04-09 16:13:57', '674', '0');
INSERT INTO `t_res_func` VALUES ('1164', '47', '249', '2018-04-09 16:13:57', '674', '2018-04-09 16:13:57', '674', '0');
INSERT INTO `t_res_func` VALUES ('1165', '49', '249', '2018-04-09 16:13:57', '674', '2018-04-09 16:13:57', '674', '0');
INSERT INTO `t_res_func` VALUES ('1166', '50', '249', '2018-04-09 16:13:57', '674', '2018-04-09 16:13:57', '674', '0');
INSERT INTO `t_res_func` VALUES ('1167', '51', '249', '2018-04-09 16:13:57', '674', '2018-04-09 16:13:57', '674', '0');
INSERT INTO `t_res_func` VALUES ('1168', '52', '249', '2018-04-09 16:13:57', '674', '2018-04-09 16:13:57', '674', '0');
INSERT INTO `t_res_func` VALUES ('1169', '53', '249', '2018-04-09 16:13:57', '674', '2018-04-09 16:13:57', '674', '0');
INSERT INTO `t_res_func` VALUES ('1170', '54', '249', '2018-04-09 16:13:57', '674', '2018-04-09 16:13:57', '674', '0');
INSERT INTO `t_res_func` VALUES ('1171', '55', '249', '2018-04-09 16:13:57', '674', '2018-04-09 16:13:57', '674', '0');
INSERT INTO `t_res_func` VALUES ('1172', '91', '249', '2018-04-09 16:13:57', '674', '2018-04-09 16:13:57', '674', '0');
INSERT INTO `t_res_func` VALUES ('1173', '92', '249', '2018-04-09 16:13:57', '674', '2018-04-09 16:13:57', '674', '0');
INSERT INTO `t_res_func` VALUES ('1174', '93', '249', '2018-04-09 16:13:57', '674', '2018-04-09 16:13:57', '674', '0');
INSERT INTO `t_res_func` VALUES ('1175', '144', '249', '2018-04-09 16:13:57', '674', '2018-04-09 16:13:57', '674', '0');
INSERT INTO `t_res_func` VALUES ('1176', '145', '249', '2018-04-09 16:13:57', '674', '2018-04-09 16:13:57', '674', '0');
INSERT INTO `t_res_func` VALUES ('1177', '155', '249', '2018-04-09 16:13:57', '674', '2018-04-09 16:13:57', '674', '0');
INSERT INTO `t_res_func` VALUES ('1178', '156', '249', '2018-04-09 16:13:57', '674', '2018-04-09 16:13:57', '674', '0');
INSERT INTO `t_res_func` VALUES ('1179', '157', '249', '2018-04-09 16:13:57', '674', '2018-04-09 16:13:57', '674', '0');
INSERT INTO `t_res_func` VALUES ('1180', '158', '249', '2018-04-09 16:13:57', '674', '2018-04-09 16:13:57', '674', '0');
INSERT INTO `t_res_func` VALUES ('1181', '159', '249', '2018-04-09 16:13:57', '674', '2018-04-09 16:13:57', '674', '0');
INSERT INTO `t_res_func` VALUES ('1182', '160', '249', '2018-04-09 16:13:57', '674', '2018-04-09 16:13:57', '674', '0');
INSERT INTO `t_res_func` VALUES ('1183', '161', '249', '2018-04-09 16:13:57', '674', '2018-04-09 16:13:57', '674', '0');
INSERT INTO `t_res_func` VALUES ('1184', '162', '249', '2018-04-09 16:13:57', '674', '2018-04-09 16:13:57', '674', '0');
INSERT INTO `t_res_func` VALUES ('1185', '163', '249', '2018-04-09 16:13:57', '674', '2018-04-09 16:13:57', '674', '0');
INSERT INTO `t_res_func` VALUES ('1186', '164', '249', '2018-04-09 16:13:57', '674', '2018-04-09 16:13:57', '674', '0');
INSERT INTO `t_res_func` VALUES ('1187', '165', '249', '2018-04-09 16:13:57', '674', '2018-04-09 16:13:57', '674', '0');
INSERT INTO `t_res_func` VALUES ('1188', '166', '249', '2018-04-09 16:13:57', '674', '2018-04-09 16:13:57', '674', '0');
INSERT INTO `t_res_func` VALUES ('1189', '167', '249', '2018-04-09 16:13:57', '674', '2018-04-09 16:13:57', '674', '0');
INSERT INTO `t_res_func` VALUES ('1190', '168', '249', '2018-04-09 16:13:57', '674', '2018-04-09 16:13:57', '674', '0');
INSERT INTO `t_res_func` VALUES ('1191', '169', '249', '2018-04-09 16:13:57', '674', '2018-04-09 16:13:57', '674', '0');
INSERT INTO `t_res_func` VALUES ('1193', '171', '249', '2018-04-09 16:13:57', '674', '2018-04-09 16:13:57', '674', '0');
INSERT INTO `t_res_func` VALUES ('1194', '172', '249', '2018-04-09 16:13:57', '674', '2018-04-09 16:13:57', '674', '0');
INSERT INTO `t_res_func` VALUES ('1195', '173', '249', '2018-04-09 16:13:57', '674', '2018-04-09 16:13:57', '674', '0');
INSERT INTO `t_res_func` VALUES ('1196', '174', '249', '2018-04-09 16:13:57', '674', '2018-04-09 16:13:57', '674', '0');
INSERT INTO `t_res_func` VALUES ('1197', '175', '249', '2018-04-09 16:13:57', '674', '2018-04-09 16:13:57', '674', '0');
INSERT INTO `t_res_func` VALUES ('1198', '176', '249', '2018-04-09 16:13:57', '674', '2018-04-09 16:13:57', '674', '0');
INSERT INTO `t_res_func` VALUES ('1199', '177', '249', '2018-04-09 16:13:57', '674', '2018-04-09 16:13:57', '674', '0');
INSERT INTO `t_res_func` VALUES ('1200', '178', '249', '2018-04-09 16:13:57', '674', '2018-04-09 16:13:57', '674', '0');
INSERT INTO `t_res_func` VALUES ('1201', '179', '249', '2018-04-09 16:13:57', '674', '2018-04-09 16:13:57', '674', '0');
INSERT INTO `t_res_func` VALUES ('1202', '180', '249', '2018-04-09 16:13:57', '674', '2018-04-09 16:13:57', '674', '0');
INSERT INTO `t_res_func` VALUES ('1203', '181', '249', '2018-04-09 16:13:57', '674', '2018-04-09 16:13:57', '674', '0');
INSERT INTO `t_res_func` VALUES ('1204', '182', '249', '2018-04-09 16:13:57', '674', '2018-04-09 16:13:57', '674', '0');
INSERT INTO `t_res_func` VALUES ('1205', '183', '249', '2018-04-09 16:13:57', '674', '2018-04-09 16:13:57', '674', '0');
INSERT INTO `t_res_func` VALUES ('1206', '184', '249', '2018-04-09 16:13:57', '674', '2018-04-09 16:13:57', '674', '0');
INSERT INTO `t_res_func` VALUES ('1207', '185', '249', '2018-04-09 16:13:57', '674', '2018-04-09 16:13:57', '674', '0');
INSERT INTO `t_res_func` VALUES ('1208', '186', '249', '2018-04-09 16:13:57', '674', '2018-04-09 16:13:57', '674', '0');
INSERT INTO `t_res_func` VALUES ('1209', '187', '249', '2018-04-09 16:13:57', '674', '2018-04-09 16:13:57', '674', '0');
INSERT INTO `t_res_func` VALUES ('1210', '188', '249', '2018-04-09 16:13:57', '674', '2018-04-09 16:13:57', '674', '0');
INSERT INTO `t_res_func` VALUES ('1211', '54', '250', '2018-04-10 16:53:05', '623', '2018-04-10 16:53:05', '623', '0');
INSERT INTO `t_res_func` VALUES ('1212', '46', '250', '2018-04-10 16:55:22', '623', '2018-04-10 16:55:22', '623', '0');
INSERT INTO `t_res_func` VALUES ('1213', '47', '250', '2018-04-10 16:55:22', '623', '2018-04-10 16:55:22', '623', '0');
INSERT INTO `t_res_func` VALUES ('1214', '144', '251', '2018-04-12 15:14:53', '623', '2018-04-12 15:14:53', '623', '0');
INSERT INTO `t_res_func` VALUES ('1215', '91', '251', '2018-04-12 15:14:57', '623', '2018-04-12 15:14:57', '623', '0');
INSERT INTO `t_res_func` VALUES ('1216', '273', '252', '2018-04-27 14:41:15', '623', '2018-04-27 14:41:15', '623', '0');
INSERT INTO `t_res_func` VALUES ('1217', '45', '193', '2018-05-17 09:28:05', '674', '2018-05-17 09:28:05', '674', '0');
INSERT INTO `t_res_func` VALUES ('1218', '46', '193', '2018-05-17 09:28:05', '674', '2018-05-17 09:28:05', '674', '0');
INSERT INTO `t_res_func` VALUES ('1219', '47', '193', '2018-05-17 09:28:05', '674', '2018-05-17 09:28:05', '674', '0');
INSERT INTO `t_res_func` VALUES ('1220', '50', '193', '2018-05-17 09:28:05', '674', '2018-05-17 09:28:05', '674', '0');
INSERT INTO `t_res_func` VALUES ('1221', '51', '193', '2018-05-17 09:28:30', '674', '2018-05-17 09:28:30', '674', '0');
INSERT INTO `t_res_func` VALUES ('1222', '52', '193', '2018-05-17 09:28:30', '674', '2018-05-17 09:28:30', '674', '0');
INSERT INTO `t_res_func` VALUES ('1223', '53', '193', '2018-05-17 09:28:46', '674', '2018-05-17 09:28:46', '674', '0');
INSERT INTO `t_res_func` VALUES ('1224', '54', '193', '2018-05-17 09:28:46', '674', '2018-05-17 09:28:46', '674', '0');
INSERT INTO `t_res_func` VALUES ('1225', '55', '193', '2018-05-17 09:28:46', '674', '2018-05-17 09:28:46', '674', '0');
INSERT INTO `t_res_func` VALUES ('1226', '45', '194', '2018-05-17 09:42:57', '674', '2018-05-17 09:42:57', '674', '0');
INSERT INTO `t_res_func` VALUES ('1227', '46', '194', '2018-05-17 09:42:57', '674', '2018-05-17 09:42:57', '674', '0');
INSERT INTO `t_res_func` VALUES ('1228', '47', '194', '2018-05-17 09:42:57', '674', '2018-05-17 09:42:57', '674', '0');
INSERT INTO `t_res_func` VALUES ('1229', '50', '194', '2018-05-17 09:42:57', '674', '2018-05-17 09:42:57', '674', '0');
INSERT INTO `t_res_func` VALUES ('1230', '358', '253', '2018-05-24 14:53:46', '623', '2018-05-24 14:53:46', '623', '0');
INSERT INTO `t_res_func` VALUES ('1231', '363', '218', '2018-06-04 17:45:23', '623', '2018-06-04 17:45:22', '623', '0');

-- ----------------------------
-- Table structure for `t_resource`
-- ----------------------------
DROP TABLE IF EXISTS `t_resource`;
CREATE TABLE `t_resource` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `parent` bigint(20) DEFAULT NULL,
  `url` varchar(2000) DEFAULT NULL,
  `type` varchar(20) NOT NULL DEFAULT 'RelativeURL' COMMENT '资源类型',
  `Is_auth` tinyint(4) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `create_by` bigint(50) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `update_by` bigint(50) DEFAULT NULL,
  `update_count` tinyint(6) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=367 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_resource
-- ----------------------------
INSERT INTO `t_resource` VALUES ('45', '权限管理', '308', '/ui/frmPermsList', 'RelativeURL', '1', null, '623', '2017-12-08 16:02:12', '192', '0');
INSERT INTO `t_resource` VALUES ('46', '角色管理', '308', '/ui/frmRoleList', 'RelativeURL', '1', null, '623', '2017-12-08 16:18:03', '192', '0');
INSERT INTO `t_resource` VALUES ('47', '账号管理', '308', '/ui/frmUserList', 'RelativeURL', '1', null, '623', '2017-12-08 16:10:39', '192', '0');
INSERT INTO `t_resource` VALUES ('49', '部门管理', '307', '/ui/orgList', 'RelativeURL', '1', null, '623', '2017-12-08 16:01:56', '192', '0');
INSERT INTO `t_resource` VALUES ('50', '资源访问日志列表', '309', '/ui/frmAccessLogList', 'RelativeURL', '1', null, '623', '2017-12-08 16:17:52', '192', '0');
INSERT INTO `t_resource` VALUES ('51', '人员管理', '307', '/ui/frmEmpList', 'RelativeURL', '1', null, '623', '2017-12-08 16:10:27', '192', '0');
INSERT INTO `t_resource` VALUES ('52', '字典项管理', '309', '/ui/frmDictList', 'RelativeURL', '1', null, '623', '2017-12-08 16:20:12', '192', '0');
INSERT INTO `t_resource` VALUES ('53', '用户操作日志', '308', '/ui/frmUserLogList', 'RelativeURL', '1', null, '623', '2017-12-08 16:17:26', '192', '0');
INSERT INTO `t_resource` VALUES ('54', '系统资源管理', '309', '/ui/sysResList', 'RelativeURL', '1', null, '623', '2017-12-08 16:05:37', '192', '0');
INSERT INTO `t_resource` VALUES ('55', '菜单管理', '309', '/ui/sysMenuList', 'RelativeURL', '1', null, '623', '2017-12-08 16:20:02', '192', '0');
INSERT INTO `t_resource` VALUES ('91', '定时任务管理', '309', '/ui/jobList', 'RelativeURL', '1', null, '623', '2017-12-08 16:17:12', '192', '0');
INSERT INTO `t_resource` VALUES ('92', '任务执行日志', '309', '/ui/jobLogList', 'RelativeURL', '1', null, '623', '2017-12-08 16:04:52', '192', '0');
INSERT INTO `t_resource` VALUES ('93', '上传文件管理', '309', '/ui/fileList', 'RelativeURL', '1', null, '623', '2017-12-08 16:19:21', '192', '0');
INSERT INTO `t_resource` VALUES ('137', '帮助中心-刷新xml', '313', '/refreshXML', 'RelativeURL', '0', null, '623', '2017-12-08 16:51:17', '192', '0');
INSERT INTO `t_resource` VALUES ('144', '人员查询', '307', '/ui/frmEmpQuery', 'RelativeURL', '1', null, '623', '2017-12-08 16:03:29', '192', '0');
INSERT INTO `t_resource` VALUES ('145', '系统', null, '/', 'System', '1', null, '623', '2017-11-15 17:15:42', '623', '0');
INSERT INTO `t_resource` VALUES ('151', '修改口令', '313', '/ui/frmPwdEdit', 'RelativeURL', '0', '2017-10-13 09:10:35', '623', '2017-12-08 16:51:09', '192', '0');
INSERT INTO `t_resource` VALUES ('158', '新增部门', '49', 'orgList.pnlUp.ctlAdd', 'Component', '1', '2017-10-13 16:41:12', '623', '2017-10-30 15:42:49', '623', '0');
INSERT INTO `t_resource` VALUES ('159', '编辑部门信息', '49', 'orgList.pnlUp.ctlSave', 'Component', '1', '2017-10-13 16:41:45', '623', '2017-10-30 15:42:58', '623', '0');
INSERT INTO `t_resource` VALUES ('160', '部门变更所属', '49', 'orgList.pnlUp.ctlUpdate', 'Component', '1', '2017-10-13 16:42:34', '623', '2017-10-30 15:43:10', '623', '0');
INSERT INTO `t_resource` VALUES ('161', '删除部门', '49', 'orgList.pnlUp.ctlDel', 'Component', '1', '2017-10-13 16:43:16', '623', '2017-10-30 15:43:17', '623', '0');
INSERT INTO `t_resource` VALUES ('162', '新增人员', '51', 'frmEmpList.empTool.ctlAdd', 'Component', '1', '2017-10-13 16:45:00', '623', '2017-10-16 12:02:18', '623', '0');
INSERT INTO `t_resource` VALUES ('163', '编辑人员信息', '51', 'frmEmpList.empDg.ctlEdit', 'Component', '1', '2017-10-13 16:45:44', '623', '2017-10-13 16:45:44', '623', '0');
INSERT INTO `t_resource` VALUES ('164', '删除人员信息', '51', 'frmEmpList.empDg.ctlDel', 'Component', '1', '2017-10-13 16:46:09', '623', '2017-10-13 16:46:09', '623', '0');
INSERT INTO `t_resource` VALUES ('165', '新增人员（人员查询）', '144', 'frmEmpQuery.pnlUp.ctlAdd', 'Component', '1', '2017-10-13 16:55:12', '623', '2017-10-26 11:16:56', '623', '0');
INSERT INTO `t_resource` VALUES ('166', '编辑人员（人员查询）', '144', 'frmEmpQuery.pnlDg.ctlEdit', 'Component', '1', '2017-10-13 16:56:21', '623', '2017-10-26 11:17:00', '623', '0');
INSERT INTO `t_resource` VALUES ('167', '删除人员（人员查询）', '144', 'frmEmpQuery.pnlDg.ctlDel', 'Component', '1', '2017-10-13 16:59:12', '623', '2017-10-26 11:17:03', '623', '0');
INSERT INTO `t_resource` VALUES ('168', '新增权限', '45', 'frmPermsList.pToolbar.btnCreate', 'Component', '1', '2017-10-13 17:01:29', '623', '2017-10-13 17:01:29', '623', '0');
INSERT INTO `t_resource` VALUES ('169', '编辑权限', '45', 'frmPermsList.pTable.edit', 'Component', '1', '2017-10-13 17:02:01', '623', '2017-10-13 17:02:01', '623', '0');
INSERT INTO `t_resource` VALUES ('170', '删除权限', '45', 'frmPermsList.pTable.delete', 'Component', '1', '2017-10-13 17:02:38', '623', '2017-10-13 17:02:38', '623', '0');
INSERT INTO `t_resource` VALUES ('171', '授权管理', '45', 'frmPermsList.pTable.auth', 'Component', '1', '2017-10-13 17:03:43', '623', '2017-10-13 17:03:43', '623', '0');
INSERT INTO `t_resource` VALUES ('172', '资源授权清单', '45', 'frmPermsList.pToolbar.btnResource', 'Component', '1', '2017-10-13 17:06:03', '623', '2017-10-13 17:06:03', '623', '0');
INSERT INTO `t_resource` VALUES ('173', '角色授权统计', '45', 'frmPermsList.pToolbar.btnRole', 'Component', '1', '2017-10-13 17:07:01', '623', '2017-10-13 17:07:01', '623', '0');
INSERT INTO `t_resource` VALUES ('174', '新增账号', '47', 'frmUserList.pToolbar.btnNew', 'Component', '1', '2017-10-13 17:09:56', '623', '2017-10-30 16:31:16', '623', '0');
INSERT INTO `t_resource` VALUES ('175', '编辑账号信息', '47', 'frmUserList.pTable.edit', 'Component', '1', '2017-10-13 17:11:10', '623', '2017-10-30 16:31:23', '623', '0');
INSERT INTO `t_resource` VALUES ('176', '冻结账号', '47', 'frmUserList.pTable.lock', 'Component', '1', '2017-10-13 17:11:40', '623', '2017-10-30 16:31:29', '623', '0');
INSERT INTO `t_resource` VALUES ('177', '解冻账号', '47', 'frmUserList.pTable.unlock', 'Component', '1', '2017-10-13 17:12:10', '623', '2017-10-30 16:31:37', '623', '0');
INSERT INTO `t_resource` VALUES ('178', '分配角色（账号管理）', '47', 'frmUserView.pToolbar.btnAssign', 'Component', '1', '2017-10-13 17:16:02', '623', '2017-10-30 16:31:50', '623', '0');
INSERT INTO `t_resource` VALUES ('179', '新增教育经历', '51', 'frmEmpDetail.pnlDetailTool.ctlEducation', 'Component', '1', '2017-10-13 17:17:44', '623', '2017-10-13 17:17:44', '623', '0');
INSERT INTO `t_resource` VALUES ('180', '新增工作经历', '51', 'frmEmpDetail.pnlDetailTool.ctlExperience', 'Component', '1', '2017-10-13 17:18:25', '623', '2017-10-13 17:18:25', '623', '0');
INSERT INTO `t_resource` VALUES ('181', '新增培训记录', '51', 'frmEmpDetail.pnlDetailTool.ctlTraining', 'Component', '1', '2017-10-13 17:19:11', '623', '2017-10-13 17:19:11', '623', '0');
INSERT INTO `t_resource` VALUES ('182', '新增考核记录', '51', 'frmEmpDetail.pnlDetailTool.ctlAssessment', 'Component', '1', '2017-10-13 17:19:50', '623', '2017-10-13 17:19:50', '623', '0');
INSERT INTO `t_resource` VALUES ('183', '新增角色', '46', 'frmRoleList.pToolbar.btnCreate', 'Component', '1', '2017-10-13 17:22:04', '623', '2017-10-13 17:22:04', '623', '0');
INSERT INTO `t_resource` VALUES ('184', '编辑角色', '46', 'frmRoleList.pTable.edit', 'Component', '1', '2017-10-13 17:22:53', '623', '2017-10-13 17:22:53', '623', '0');
INSERT INTO `t_resource` VALUES ('185', '删除角色', '46', 'frmRoleList.pTable.delete', 'Component', '1', '2017-10-13 17:25:05', '623', '2017-10-13 17:25:05', '623', '0');
INSERT INTO `t_resource` VALUES ('186', '加入成员', '46', 'frmRoleView.pToolbar.btnAdd', 'Component', '1', '2017-10-13 17:26:23', '623', '2017-10-13 17:26:23', '623', '0');
INSERT INTO `t_resource` VALUES ('187', '分配权限', '46', 'frmRoleView.pToolbar.btnAssign', 'Component', '1', '2017-10-13 17:26:48', '623', '2017-10-13 17:26:48', '623', '0');
INSERT INTO `t_resource` VALUES ('188', '新增字典项', '52', 'frmDictList.pToolbar.dicCreate', 'Component', '1', '2017-10-13 17:28:22', '623', '2017-10-13 17:28:22', '623', '0');
INSERT INTO `t_resource` VALUES ('189', '删除字典项', '52', 'frmDictList.pTable.dicdelete', 'Component', '1', '2017-10-13 17:29:13', '623', '2017-10-13 17:29:13', '623', '0');
INSERT INTO `t_resource` VALUES ('190', '编辑字典项', '52', 'frmDictList.pTable.dicedit', 'Component', '1', '2017-10-13 17:29:37', '623', '2017-10-13 17:29:37', '623', '0');
INSERT INTO `t_resource` VALUES ('191', '新增字典项类型', '52', 'frmDictCataList.pToolbar.dicCreate', 'Component', '1', '2017-10-13 17:31:02', '623', '2017-10-13 17:31:02', '623', '0');
INSERT INTO `t_resource` VALUES ('192', '编辑字典项类型信息', '52', 'frmDictCataList.pTable.dcaEdit', 'Component', '1', '2017-10-13 17:32:03', '623', '2017-10-13 17:32:03', '623', '0');
INSERT INTO `t_resource` VALUES ('193', '删除字典项类型', '52', 'frmDictCataList.pTable.dcaDelete', 'Component', '1', '2017-10-13 17:32:31', '623', '2017-10-13 17:32:31', '623', '0');
INSERT INTO `t_resource` VALUES ('194', '新增系统资源', '54', 'sysResList.pnlUp.ctlAdd', 'Component', '1', '2017-10-13 17:38:25', '623', '2017-10-13 17:38:25', '623', '0');
INSERT INTO `t_resource` VALUES ('195', '编辑系统资源', '54', 'sysResList.pnlUp.ctlSave', 'Component', '1', '2017-10-13 17:38:51', '623', '2017-10-13 17:38:51', '623', '0');
INSERT INTO `t_resource` VALUES ('196', '删除系统资源', '54', 'sysResList.pnlUp.ctlDel', 'Component', '1', '2017-10-13 17:39:25', '623', '2017-10-13 17:39:25', '623', '0');
INSERT INTO `t_resource` VALUES ('197', '新增菜单', '55', 'sysMenuList.pnlUp.ctlAdd', 'Component', '1', '2017-10-13 17:41:12', '623', '2017-10-13 17:41:12', '623', '0');
INSERT INTO `t_resource` VALUES ('198', '删除菜单', '55', 'sysMenuList.pnlUp.ctlDel', 'Component', '1', '2017-10-13 17:41:58', '623', '2017-10-13 17:41:58', '623', '0');
INSERT INTO `t_resource` VALUES ('199', '编辑菜单', '55', 'sysMenuList.pnlUp.ctlSave', 'Component', '1', '2017-10-13 17:42:17', '623', '2017-10-13 17:42:17', '623', '0');
INSERT INTO `t_resource` VALUES ('200', '新增定时任务', '91', 'jobList.pnlUp.ctlAdd', 'Component', '1', '2017-10-13 17:43:37', '623', '2017-10-30 18:10:17', '623', '0');
INSERT INTO `t_resource` VALUES ('201', '编辑定时任务', '91', 'jobList.pnlDg.ctlEdit', 'Component', '0', '2017-10-13 17:44:21', '623', '2017-10-17 18:36:11', '623', '0');
INSERT INTO `t_resource` VALUES ('202', '删除定时任务', '91', 'jobList.pnlDg.ctlDel', 'Component', '1', '2017-10-13 17:44:54', '623', '2017-10-13 17:45:24', '623', '0');
INSERT INTO `t_resource` VALUES ('203', '启动定时任务', '91', 'jobList.pnlDg.ctlOpen', 'Component', '1', '2017-10-13 17:45:59', '623', '2017-10-13 17:45:59', '623', '0');
INSERT INTO `t_resource` VALUES ('204', '禁用定时任务', '91', 'jobList.pnlDg.ctlClose', 'Component', '1', '2017-10-13 17:46:21', '623', '2017-10-13 17:46:21', '623', '0');
INSERT INTO `t_resource` VALUES ('205', '执行定时任务', '91', 'jobList.pnlDg.ctlExc', 'Component', '1', '2017-10-13 17:47:39', '623', '2017-10-13 17:47:39', '623', '0');
INSERT INTO `t_resource` VALUES ('217', '人员统计', '307', '/ui/frmEmpStat', 'RelativeURL', '1', '2017-10-16 14:06:38', '623', '2017-12-08 16:16:53', '192', '0');
INSERT INTO `t_resource` VALUES ('229', '个人信息', '313', '/ui/frmMainEmpEdit', 'RelativeURL', '0', '2017-10-18 10:41:40', '623', '2017-12-08 16:51:25', '192', '0');
INSERT INTO `t_resource` VALUES ('252', '配置文件管理', '310', '/ui/frmUIList', 'RelativeURL', '1', '2017-10-25 18:36:21', '623', '2017-12-08 16:20:21', '192', '0');
INSERT INTO `t_resource` VALUES ('281', '岗位管理', '307', '/ui/frmPositionList', 'RelativeURL', '1', '2017-11-15 17:00:47', '623', '2017-12-08 16:18:29', '192', '0');
INSERT INTO `t_resource` VALUES ('282', '新增岗位', '281', 'frmPositionList.pToolbar.btnCreate', 'Component', '1', '2017-11-15 17:03:08', '623', '2017-11-15 18:20:30', '623', '0');
INSERT INTO `t_resource` VALUES ('283', '编辑岗位', '281', 'frmPositionList.pTable.edit', 'Component', '1', '2017-11-15 17:03:42', '623', '2017-11-15 18:20:36', '623', '0');
INSERT INTO `t_resource` VALUES ('284', '删除岗位', '281', 'frmPositionList.pToolbar.delete', 'Component', '1', '2017-11-15 17:04:12', '623', '2017-11-15 18:20:43', '623', '0');
INSERT INTO `t_resource` VALUES ('285', '加入岗位成员', '281', 'frmPositionView.pToolbar.btnAdd', 'Component', '1', '2017-11-15 17:05:10', '623', '2017-11-15 18:20:54', '623', '0');
INSERT INTO `t_resource` VALUES ('286', '分配岗位权限', '281', 'frmPositionView.pToolbar.btnAssign', 'Component', '1', '2017-11-15 17:05:41', '623', '2017-11-15 18:21:02', '623', '0');
INSERT INTO `t_resource` VALUES ('288', 'editorMd独立查看界面', '289', '/view/editormd/editorMd.jsp', 'RelativeURL', '0', '2017-11-16 16:22:33', '623', '2017-11-17 11:49:12', '623', '0');
INSERT INTO `t_resource` VALUES ('289', 'editorMarkDown文本编辑器', '303', '/ui/frmFileMdList', 'RelativeURL', '1', '2017-11-17 11:47:52', '623', '2017-12-08 16:27:30', '192', '0');
INSERT INTO `t_resource` VALUES ('290', '新增markDown', '289', 'frmFileMdList.pToolbar.btnAdd', 'Component', '1', '2017-11-17 11:50:24', '623', '2017-11-17 18:40:05', '623', '0');
INSERT INTO `t_resource` VALUES ('291', '编辑markDown', '289', 'frmFileMdList.pToolbar.btnEdit', 'Component', '1', '2017-11-17 11:51:21', '623', '2017-11-17 18:40:10', '623', '0');
INSERT INTO `t_resource` VALUES ('292', '查看markDown', '289', 'frmFileMdList.pToolbar.btnView', 'Component', '1', '2017-11-17 11:51:49', '623', '2017-11-17 18:40:15', '623', '0');
INSERT INTO `t_resource` VALUES ('302', '项目管理子系统', '145', '', 'SubSystem', '0', '2017-12-08 15:43:23', '192', '2017-12-08 16:48:35', '192', '0');
INSERT INTO `t_resource` VALUES ('303', '工作管理模块', '302', null, 'Module', '0', '2017-12-08 15:44:13', '192', '2017-12-08 15:44:13', '192', '0');
INSERT INTO `t_resource` VALUES ('307', '人事管理模块', '311', '', 'Module', '0', '2017-12-08 15:46:55', '192', '2018-03-28 14:20:32', '674', '0');
INSERT INTO `t_resource` VALUES ('308', '用户管理模块', '311', '', 'Module', '0', '2017-12-08 15:47:07', '192', '2017-12-08 16:49:26', '192', '0');
INSERT INTO `t_resource` VALUES ('309', '系统管理模块', '311', '', 'Module', '0', '2017-12-08 15:47:24', '192', '2017-12-08 16:49:56', '192', '0');
INSERT INTO `t_resource` VALUES ('310', '开发工具模块', '311', '', 'Module', '0', '2017-12-08 15:47:54', '192', '2017-12-08 16:49:35', '192', '0');
INSERT INTO `t_resource` VALUES ('311', '基础管理子系统', '145', '', 'SubSystem', '0', '2017-12-08 16:23:34', '192', '2017-12-08 16:48:19', '192', '0');
INSERT INTO `t_resource` VALUES ('313', '主框架模块', '311', null, 'Module', '0', '2017-12-08 16:50:43', '192', '2017-12-08 16:50:43', '192', '0');
INSERT INTO `t_resource` VALUES ('320', 'mdl流程图', '313', '/flowChartMD', 'RelativeURL', '0', '2017-12-15 13:24:10', '623', '2017-12-15 13:24:10', '623', '0');
INSERT INTO `t_resource` VALUES ('321', '工作流管理', '309', '/ui/frmWorkflowList', 'RelativeURL', '1', '2017-12-15 13:48:52', '192', '2017-12-15 13:48:52', '192', '0');
INSERT INTO `t_resource` VALUES ('322', '异步任务日志', '309', '/ui/frmMessageLogList', 'RelativeURL', '1', '2017-12-18 10:28:08', '623', '2017-12-18 10:28:08', '623', '0');
INSERT INTO `t_resource` VALUES ('323', '表单管理', '310', '/ui/frmMdownFormList', 'RelativeURL', '1', '2017-12-19 16:26:36', '623', '2017-12-19 16:31:11', '623', '0');
INSERT INTO `t_resource` VALUES ('331', '心跳监测', '313', '/heatBeat', 'RelativeURL', '0', '2017-12-28 16:59:54', '623', '2017-12-28 16:59:54', '623', '0');
INSERT INTO `t_resource` VALUES ('332', '关于本站', '313', '/about', 'RelativeURL', '0', '2017-12-28 17:10:22', '192', '2017-12-28 17:10:22', '192', '0');
INSERT INTO `t_resource` VALUES ('347', '数据表列表', '310', '/ui/frmDatasheetList', 'RelativeURL', '1', '2018-04-11 09:57:25', '623', '2018-04-11 09:57:25', '623', '0');
INSERT INTO `t_resource` VALUES ('353', '公告列表', '309', '/ui/frmAnnouncementList', 'RelativeURL', '1', '2018-05-16 09:26:05', '623', '2018-05-16 09:26:05', '623', '0');
INSERT INTO `t_resource` VALUES ('363', '自动生成Model', '310', '/ui/frmCreateModel', 'RelativeURL', '1', '2018-06-04 17:15:17', '623', '2018-06-04 17:16:29', '623', '0');
INSERT INTO `t_resource` VALUES ('364', '模拟请求', '310', '/ui/frmSimulateRequest', 'RelativeURL', '1', '2018-06-05 10:09:56', '626', '2018-06-05 10:09:57', '626', '0');
INSERT INTO `t_resource` VALUES ('366', '文章管理', '307', '/ui/frmArticleList', 'RelativeURL', '1', '2018-07-04 09:39:17', '626', '2018-07-04 09:39:17', '626', '0');

-- ----------------------------
-- Table structure for `t_role`
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `description` varchar(200) DEFAULT NULL,
  `create_by` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `update_count` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=201 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_role
-- ----------------------------
INSERT INTO `t_role` VALUES ('1', 'Administrator', '系统管理员', '623', null, '623', '2017-10-31 16:57:13', null);
INSERT INTO `t_role` VALUES ('2', '开发管理', null, '623', '2017-11-23 11:49:25', '623', '2017-11-23 11:49:25', '1');
INSERT INTO `t_role` VALUES ('200', '访客', null, '623', '2017-11-23 11:50:22', '623', '2017-11-23 11:50:22', '1');

-- ----------------------------
-- Table structure for `t_role_func`
-- ----------------------------
DROP TABLE IF EXISTS `t_role_func`;
CREATE TABLE `t_role_func` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `func_id` bigint(20) NOT NULL COMMENT '功能ID',
  `data_auth` varchar(20) DEFAULT NULL COMMENT '数据访问权限',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint(50) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(50) DEFAULT NULL COMMENT '更新人',
  `update_count` tinyint(6) DEFAULT '0' COMMENT '版本号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=631 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_role_func
-- ----------------------------
INSERT INTO `t_role_func` VALUES ('511', '1', '219', null, '2017-11-23 14:13:55', '623', '2017-11-23 14:13:55', '623', '0');
INSERT INTO `t_role_func` VALUES ('516', '1', '216', null, '2017-11-23 14:14:10', '623', '2017-11-23 14:14:10', '623', '0');
INSERT INTO `t_role_func` VALUES ('518', '1', '215', null, '2017-11-23 14:14:20', '623', '2017-11-23 14:14:20', '623', '0');
INSERT INTO `t_role_func` VALUES ('519', '1', '218', null, '2017-11-23 14:14:23', '623', '2017-11-23 14:14:23', '623', '0');
INSERT INTO `t_role_func` VALUES ('530', '1', '217', null, '2017-11-23 14:15:21', '623', '2017-11-23 14:15:21', '623', '0');
INSERT INTO `t_role_func` VALUES ('578', '1', '225', null, '2017-12-19 16:27:42', '623', '2017-12-19 16:27:42', '623', '0');
INSERT INTO `t_role_func` VALUES ('630', '1', '235', null, '2018-03-02 10:50:20', '623', '2018-03-02 10:50:20', '623', '0');

-- ----------------------------
-- Table structure for `t_schedule_job`
-- ----------------------------
DROP TABLE IF EXISTS `t_schedule_job`;
CREATE TABLE `t_schedule_job` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `status` varchar(20) NOT NULL COMMENT '状态',
  `cron_expression` varchar(100) NOT NULL COMMENT 'CRON表达式',
  `concurrent` varchar(20) NOT NULL COMMENT '并发控制',
  `type` varchar(20) NOT NULL COMMENT '类型',
  `description` varchar(200) DEFAULT NULL COMMENT '描述',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint(50) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(50) DEFAULT NULL COMMENT '更新人',
  `update_count` tinyint(6) DEFAULT '0' COMMENT '版本号',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_name` (`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_schedule_job
-- ----------------------------
INSERT INTO `t_schedule_job` VALUES ('1', '日志批量移出', 'disable', '0*/1***？', 'single', 'javabean', '将指定日志表定时批量移出到其他数据库。', '2018-07-02 11:21:36', '626', '2018-07-05 09:11:20', '626', '0');

-- ----------------------------
-- Table structure for `t_schedule_param`
-- ----------------------------
DROP TABLE IF EXISTS `t_schedule_param`;
CREATE TABLE `t_schedule_param` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `job_id` bigint(20) NOT NULL COMMENT '任务ID',
  `name` varchar(20) NOT NULL COMMENT '参数名称',
  `value` varchar(100) NOT NULL COMMENT '参数值',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_schedule_param
-- ----------------------------
INSERT INTO `t_schedule_param` VALUES ('9', '1', 'beanclass', 'com.tedu.plugin.jobClass.ShiftOutLog');
INSERT INTO `t_schedule_param` VALUES ('10', '1', 'methodname', 'logShiftOut');

-- ----------------------------
-- Table structure for `t_user`
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(50) NOT NULL COMMENT '用户名',
  `emp_id` bigint(20) NOT NULL COMMENT '人员ID',
  `password` varchar(100) DEFAULT NULL COMMENT '口令',
  `auth_type` varchar(20) NOT NULL COMMENT '认证方式',
  `status` varchar(20) NOT NULL COMMENT '状态',
  `wrong_count` bigint(20) DEFAULT NULL COMMENT '错误次数',
  `validate` bigint(20) DEFAULT NULL COMMENT '禁止登陆状态',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint(50) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(50) DEFAULT NULL COMMENT '更新人',
  `update_count` tinyint(6) DEFAULT '0' COMMENT '版本号',
  PRIMARY KEY (`id`),
  KEY `uk_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=176 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES ('1', 'test', '1', '{md5}74e81385d2c5258fc4b757be18a79d46F8C471B798E34524B5A91E491EFD2E9F', 'local', 'enable', null, null, '2017-11-23 11:47:02', '1', '2018-05-16 16:34:36', null, '1');

-- ----------------------------
-- Table structure for `t_user_log`
-- ----------------------------
DROP TABLE IF EXISTS `t_user_log`;
CREATE TABLE `t_user_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` bigint(20) DEFAULT NULL,
  `emp_id` bigint(20) DEFAULT NULL,
  `action` varchar(20) NOT NULL COMMENT '动作',
  `ui_name` varchar(20) NOT NULL COMMENT '界面名称',
  `ui_title` varchar(50) NOT NULL COMMENT '界面标题',
  `panel_name` varchar(20) DEFAULT NULL COMMENT '区域名称',
  `panel_title` varchar(50) DEFAULT NULL COMMENT '区域标题',
  `control_name` varchar(20) DEFAULT NULL COMMENT '控件名称',
  `control_title` varchar(50) DEFAULT NULL COMMENT '控件标题',
  `flow_id` varchar(32) DEFAULT NULL COMMENT '操作唯一标识,关联changeLog',
  `session_id` varchar(50) DEFAULT NULL,
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `exec_result` varchar(20) DEFAULT NULL,
  `error_reason` varchar(50) DEFAULT NULL,
  `client_ip` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7699074 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_user_log
-- ----------------------------

-- ----------------------------
-- Table structure for `t_user_role`
-- ----------------------------
DROP TABLE IF EXISTS `t_user_role`;
CREATE TABLE `t_user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint(50) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(50) DEFAULT NULL COMMENT '更新人',
  `update_count` tinyint(6) DEFAULT '0' COMMENT '版本号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=312 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_user_role
-- ----------------------------
INSERT INTO `t_user_role` VALUES ('1', '1', '1', null, '1', null, '1', '0');
