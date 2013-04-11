-- Database: db_userplatform
-- 用户表添加
use db_userplatform ;
alter table db_userplatform.dt_user add column nickName  varchar(20) COMMENT '昵称' ;
alter table db_userplatform.dt_user add column qq        varchar(20) COMMENT 'QQ号码' ;
alter table db_userplatform.dt_user add column msn       varchar(100) COMMENT 'MSN';
alter table db_userplatform.dt_user add column linkPhone varchar(20) COMMENT '联系电话' ;
alter table db_userplatform.dt_user add column hobby varchar(50) COMMENT '爱好' ;
alter table db_userplatform.dt_user add column selfIntroduction varchar(200)  COMMENT '自我介绍' ;
alter table db_userplatform.dt_user add column schoolName varchar(20) COMMENT '学校名称' ;
alter table db_userplatform.dt_user add column companyName varchar(50) COMMENT '单位名称' ;
alter table db_userplatform.dt_user add column workPost varchar(20) COMMENT '职位' ;
alter table db_userplatform.dt_user add column pswStrength TINYINT COMMENT '密码等级';
alter table db_userplatform.dt_user add column headDir varchar(200) COMMENT '头像路径' ;
alter table dt_user add  `mobile` varchar(15) default '' COMMENT '手机号';
-- 添加时间不能给默认值,mysql后添加列默认值时间是:0,程序转换时会有异常
alter table dt_user add  `lastMessageTime` timestamp null COMMENT '短信最后发送时间';
alter table dt_user add  `messageCount` tinyint(2) unsigned default '0' COMMENT '短信条数/天';


alter table db_userplatform.dt_user add column eduLevel TINYINT COMMENT '教育程度 0,其它 1,初中 2,高中 3,中技 4,中专 5,大专 6,本科 7,硕士 8,博士' ;
alter table db_userplatform.dt_user add column schoolType TINYINT COMMENT '学校类型 0,其它 1,小学 2,高中 3,初中 4,大学' ;
alter table db_userplatform.dt_user add column marryStatus TINYINT COMMENT '婚姻状况 0,未婚 1,已婚' ;
alter table db_userplatform.dt_user add column startEduYear SMALLINT COMMENT '入学年份' ;
alter table db_userplatform.dt_user add column workStartYear SMALLINT COMMENT '工作开始年份' ;
alter table db_userplatform.dt_user add column workEndYear SMALLINT COMMENT '工作结束年份' ;


------------------------------------------修改测试服------------------------------------
/*改变字段类型*/
alter table db_userplatform.dt_user DROP column eduLevel    ;
alter table db_userplatform.dt_user DROP column schoolType   ;
alter table db_userplatform.dt_user DROP column marryStatus ;

alter table db_userplatform.dt_user add column eduLevel    TINYINT COMMENT '教育程度' ;
alter table db_userplatform.dt_user add column schoolType  TINYINT COMMENT '学校类型' ;
alter table db_userplatform.dt_user add column marryStatus TINYINT COMMENT '婚姻状况' ;


alter table db_userplatform.dt_user CHANGE  workStartYear workStartYear SMALLINT COMMENT '工作开始年份' ;
alter table db_userplatform.dt_user CHANGE  workEndYear   workEndYear   SMALLINT COMMENT '工作结束年份' ;
alter table db_userplatform.dt_user CHANGE  startEduYear  startEduYear  SMALLINT COMMENT '入学年份';



insert into conf_system_variables values('addictionUrl','http://10.10.4.187/playeraasoperateservlet','防沉迷接口url');
insert into conf_system_variables values('addictionSwitch','open','防沉迷接口开关');
insert into conf_system_variables values('jedisCacheIP','192.168.1.10','jedis缓存的内网IP');



insert into conf_pay_table(channelId,subType,subTag,scale,status,channelName,subTypeName,subTagName) values('B',1,'ICBC',10,0,'财付通','网银充值','工商银行');
insert into conf_pay_table(channelId,subType,subTag,scale,status,channelName,subTypeName,subTagName) values('B',1,'CCB',10,0,'财付通','网银充值','建设银行');
insert into conf_pay_table(channelId,subType,subTag,scale,status,channelName,subTypeName,subTagName) values('B',1,'ABC',10,0,'财付通','网银充值','农业银行');
insert into conf_pay_table(channelId,subType,subTag,scale,status,channelName,subTypeName,subTagName) values('B',1,'CMB',10,0,'财付通','网银充值','招商银行');
insert into conf_pay_table(channelId,subType,subTag,scale,status,channelName,subTypeName,subTagName) values('B',1,'SPDB',10,0,'财付通','网银充值','上海浦发银行');
insert into conf_pay_table(channelId,subType,subTag,scale,status,channelName,subTypeName,subTagName) values('B',1,'SDB',10,0,'财付通','网银充值','深圳发展银行');
insert into conf_pay_table(channelId,subType,subTag,scale,status,channelName,subTypeName,subTagName) values('B',1,'CIB',10,0,'财付通','网银充值','兴业银行');
insert into conf_pay_table(channelId,subType,subTag,scale,status,channelName,subTypeName,subTagName) values('B',1,'BOB',10,0,'财付通','网银充值','北京银行');
insert into conf_pay_table(channelId,subType,subTag,scale,status,channelName,subTypeName,subTagName) values('B',1,'CEB',10,0,'财付通','网银充值','光大银行');
insert into conf_pay_table(channelId,subType,subTag,scale,status,channelName,subTypeName,subTagName) values('B',1,'CMBC',10,0,'财付通','网银充值','民生银行');
insert into conf_pay_table(channelId,subType,subTag,scale,status,channelName,subTypeName,subTagName) values('B',1,'CITIC',10,0,'财付通','网银充值','中信银行');
insert into conf_pay_table(channelId,subType,subTag,scale,status,channelName,subTypeName,subTagName) values('B',1,'GDB',10,0,'财付通','网银充值','广东发展银行');
insert into conf_pay_table(channelId,subType,subTag,scale,status,channelName,subTypeName,subTagName) values('B',1,'PAB',10,0,'财付通','网银充值','平安银行');
insert into conf_pay_table(channelId,subType,subTag,scale,status,channelName,subTypeName,subTagName) values('B',1,'BOC',10,0,'财付通','网银充值','中国银行');
insert into conf_pay_table(channelId,subType,subTag,scale,status,channelName,subTypeName,subTagName) values('B',1,'COMM',10,0,'财付通','网银充值','交通银行');
insert into conf_pay_table(channelId,subType,subTag,scale,status,channelName,subTypeName,subTagName) values('B',1,'NJCB',10,0,'财付通','网银充值','南京银行');
insert into conf_pay_table(channelId,subType,subTag,scale,status,channelName,subTypeName,subTagName) values('B',1,'NBCB',10,0,'财付通','网银充值','宁波银行');
insert into conf_pay_table(channelId,subType,subTag,scale,status,channelName,subTypeName,subTagName) values('B',1,'SRCB',10,0,'财付通','网银充值','上海农商');
insert into conf_pay_table(channelId,subType,subTag,scale,status,channelName,subTypeName,subTagName) values('B',1,'BEA',10,0,'财付通','网银充值','东亚银行');
insert into conf_pay_table(channelId,subType,subTag,scale,status,channelName,subTypeName,subTagName) values('B',1,'POSTGC',10,0,'财付通','网银充值','邮政储蓄银行');



insert into conf_pay_table(channelId,subType,subTag,scale,status,channelName,subTypeName,subTagName) values('B',0,'BL',10,0,'财付通','余额充值','余额充值');


insert into conf_pay_table(channelId,subType,subTag,scale,status,channelName,subTypeName,subTagName)
values('B',2,'ICBCB2B',10,0,'财付通','企业网银充值','工商银行（企业）');
insert into conf_pay_table(channelId,subType,subTag,scale,status,channelName,subTypeName,subTagName)
values('B',2,'CMBB2B',10,0,'财付通','企业网银充值','招商银行（企业）');
insert into conf_pay_table(channelId,subType,subTag,scale,status,channelName,subTypeName,subTagName)
values('B',2,'CCBB2B',10,0,'财付通','企业网银充值','建设银行（企业）');
insert into conf_pay_table(channelId,subType,subTag,scale,status,channelName,subTypeName,subTagName)
 values('B',2,'ABCB2B',10,0,'财付通','企业网银充值','农业银行（企业）');
insert into conf_pay_table(channelId,subType,subTag,scale,status,channelName,subTypeName,subTagName)
values('B',2,'SPDBB2B',10,0,'财付通','企业网银充值','浦发银行（企业）');