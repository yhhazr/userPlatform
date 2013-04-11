

-- Database: db_userplatform
-- 密保答案表
CREATE TABLE `db_userplatform`.`dt_question` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `userId` int(10) unsigned NOT NULL COMMENT '用户编号',
  `question` varchar(100) NOT NULL COMMENT '问题',
  `answer` varchar(100) NOT NULL COMMENT '答案',
  `status` tinyint(2) NOT NULL default '0' COMMENT '问题状态: 0:失效, 1:正常',
  `addTime` datetime default NULL COMMENT '添加时间',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='密保问题答案表';

CREATE TABLE `db_userplatform`.`dt_appeal` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `userId` int(10) unsigned NOT NULL COMMENT '用户ID',
  `userName` varchar(16) NOT NULL COMMENT '用户名',
  `realName` varchar(20) default '' COMMENT '真实姓名',
  `idCard` varchar(20) default '' COMMENT '身份证号',
  `oftenPlayGame` varchar(50) default '' COMMENT '经常玩的游戏',
  `serverName` varchar(50) default '',
  `email` varchar(100) default '' COMMENT '联系邮箱',
  `playerName` varchar(50) default '' COMMENT '角色名',
  `playerLevel` smallint(6) default '0' COMMENT '角色等级',
  `createDate` varchar(50) default '' COMMENT '注册时间',
  `createCity` varchar(50) default '' COMMENT '注册城市',
  `exceptionDate` varchar(50) default '' COMMENT '账号异常时间',
  `lastLoginDate` varchar(50) default '' COMMENT '最后登录时间',
  `pay` tinyint(4) default '0' COMMENT '是否充值过(1:是 ; 0:否)',
  `orderIds` varchar(2000) default '' COMMENT '单订id,以逗号分隔,例如:(id1,id2...)',
  `idCardImgPath` varchar(200) default '' COMMENT '身份证扫描件路劲',
  `otherInfo` varchar(2000) default '' COMMENT '他其材料信息',
  `gainPoints` int(10) default '0' COMMENT '综合评价分数(参考)',
  `status` tinyint(4) unsigned default '0' COMMENT '审核状态(0:未处理,1:通过;2:未通过)',
  `appealTime` timestamp NULL default NULL COMMENT '申诉时间',
  `auditor` varchar(255) default '' COMMENT '审核人',
  `auditorTime` timestamp NULL default NULL on update CURRENT_TIMESTAMP COMMENT '审核时间',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='账户申诉表';

-- Database: conf_userplatform
-- 短信相关(过期时间,sn,key,api地址,发送短信内容)
delete from `conf_system_variables` where `key` like 'sendMsg.%'
INSERT INTO `conf_system_variables` VALUES ('sendMsg.expiryTime', '600000', '单位:毫秒');
INSERT INTO `conf_system_variables` VALUES ('sendMsg.sn', 'SDK-666-010-01745', 'sn编号');
INSERT INTO `conf_system_variables` VALUES ('sendMsg.key', '399524', '用户key,与sn结合生成密码');
INSERT INTO `conf_system_variables` VALUES ('sendMsg.url', 'http://sdk2.entinfo.cn/z_mdsmssend.aspx', '短信api接口地址');
INSERT INTO `conf_system_variables` VALUES ('sendMsg.content', '亲爱的用户，您的验证码是:%s。请在10分钟内完成验证[第七大道]', '短信发送内容');
--  本地测试
--  INSERT INTO `conf_userplatform`.`conf_system_variables` VALUES ('path.img.appeal.idcard', 'd:/7road/userplatform/img_idcard/', '身份证图片路径');
INSERT INTO `conf_userplatform`.`conf_system_variables` VALUES ('path.img.appeal.idcard', '/var/web/userresource/img_idcard/', '身份证图片路径');

-- 验证码改为6位
alter table dt_verify_code modify column code varchar(6);


INSERT INTO `conf_system_variables` VALUES ('addictionServerUrl', 'http://aas.7road.com/aas/insertInfo.action', '插入防沉迷信息的服务器url');
INSERT INTO `conf_system_variables` VALUES ('queryAddictionServerUrl', 'http://aas.7road.com/aas/aasAuth.action', '查询防沉迷认证url');
INSERT INTO `conf_system_variables` VALUES ('addictionKey', 'cf7b-bc85-53bb-807b-a33b', '插入防沉迷信息的key');