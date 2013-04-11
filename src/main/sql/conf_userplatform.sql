-- MySQL dump 10.13  Distrib 5.5.22, for debian-linux-gnu (x86_64)
--
-- Host: 127.0.0.1    Database: conf_userplatform
-- ------------------------------------------------------
-- Server version	5.0.95

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Not dumping tablespaces as no INFORMATION_SCHEMA.FILES table on this server
--

--
-- Table structure for table `conf_game_table`
--

DROP TABLE IF EXISTS `conf_game_table`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `conf_game_table` (
  `id` int(10) unsigned NOT NULL,
  `gameName` varchar(45) NOT NULL COMMENT '游戏名称',
  `goldName` varchar(45) NOT NULL COMMENT '游戏币名称',
  `goldScale` int(10) unsigned NOT NULL default '10' COMMENT '游戏币默认比值',
  `homePage` varchar(250) default NULL COMMENT '游戏主页',
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='游戏配置表。';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `conf_game_table`
--

LOCK TABLES `conf_game_table` WRITE;
/*!40000 ALTER TABLE `conf_game_table` DISABLE KEYS */;
INSERT INTO `conf_game_table` VALUES (1,'神曲','钻石',10,'http://sq.7road.com');
/*!40000 ALTER TABLE `conf_game_table` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `conf_pay_table`
--

DROP TABLE IF EXISTS `conf_pay_table`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `conf_pay_table` (
  `channelId` char(1) NOT NULL COMMENT '充值渠道ID，例如支付宝（A），财富通（B），易宝（C），快钱（D）等。',
  `subType` tinyint(2) unsigned NOT NULL default '1' COMMENT '充值渠道支付类型，比如银行支付（1），手机银行支付（2），充值卡支付（3），游戏卡支付（4）等。',
  `subTag` varchar(30) NOT NULL COMMENT '支付类型标识，比如招商银行（CMB）等。',
  `scale` mediumint(5) unsigned NOT NULL default '10' COMMENT '充值比例，1：scale',
  `status` tinyint(1) NOT NULL default '0' COMMENT '状态，开通（0），关闭（-1）',
  `channelName` varchar(20) NOT NULL COMMENT '充值渠道名称',
  `subTypeName` varchar(20) NOT NULL COMMENT '充值渠道支付类型名称',
  `subTagName` varchar(60) NOT NULL COMMENT '支付类型标识名称',
  PRIMARY KEY  (`channelId`,`subType`,`subTag`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `conf_pay_table`
--

LOCK TABLES `conf_pay_table` WRITE;
/*!40000 ALTER TABLE `conf_pay_table` DISABLE KEYS */;
INSERT INTO `conf_pay_table` VALUES ('A',1,'ICBCB2C',10,0,'支付宝','网银充值','中国工商银行'),('A',1,'ABC',10,0,'支付宝','网银充值','中国农业银行'),('A',1,'CCB',10,-1,'支付宝','网银充值','中国建设银行'),('A',1,'CMB',10,0,'支付宝','网银充值','中国招商银行'),('A',1,'BOCB2C',10,0,'支付宝','网银充值','中国银行'),('A',1,'SPDB',10,0,'支付宝','网银充值','上海浦东发展银行'),('A',1,'CIB',10,0,'支付宝','网银充值','兴业银行'),('A',1,'GDB',10,0,'支付宝','网银充值','广东发展银行'),('A',1,'SDB',10,0,'支付宝','网银充值','深圳发展银行'),('A',1,'CMBC',10,0,'支付宝','网银充值','中国民生银行'),('A',1,'COMM',10,0,'支付宝','网银充值','交通银行'),('A',1,'CITIC',10,0,'支付宝','网银充值','中信银行'),('A',1,'CEBBANK',10,0,'支付宝','网银充值','光大银行'),('A',0,'',10,0,'支付宝','余额充值',''),('C',1,'ICBC-NET-B2C',10,0,'易宝','网银充值','中国工商银行'),('C',1,'ABC-NET-B2C',10,0,'易宝','网银充值','中国农业银行'),('C',1,'CCB-NET-B2C',10,-1,'易宝','网银充值','中国建设银行'),('C',1,'CMBCHINA-NET-B2C',10,0,'易宝','网银充值','中国招商银行'),('C',1,'BOCB2C-NET-B2C',10,0,'易宝','网银充值','中国银行'),('C',1,'SPDB-NET-B2C',10,0,'易宝','网银充值','上海浦东发展银行'),('C',1,'CIB-NET-B2C',10,0,'易宝','网银充值','兴业银行'),('C',1,'GDB-NET-B2C',10,0,'易宝','网银充值','广东发展银行'),('C',1,'SDB-NET-B2C',10,0,'易宝','网银充值','深圳发展银行'),('C',1,'CMBC-NET-B2C',10,0,'易宝','网银充值','中国民生银行'),('C',1,'BOCO-NET-B2C',10,0,'易宝','网银充值','交通银行'),('C',1,'ECITIC-NET-B2C',10,0,'易宝','网银充值','中信银行'),('C',1,'CEB-NET-B2C',10,0,'易宝','网银充值','光大银行'),('C',3,'SZX-NET',10,0,'易宝','充值卡充值','神州行充值卡'),('C',3,'UNICOM-NET',10,0,'易宝','充值卡充值','联通充值卡'),('C',3,'TELECOM-NET',10,0,'易宝','充值卡充值','电信充值卡'),('C',3,'JUNNET-NET',10,0,'易宝','充值卡充值','骏网一卡通'),('C',4,'SNDACARD-NET',10,0,'易宝','游戏卡充值','盛大一卡通'),('C',4,'SOHU-NET',9,0,'易宝','游戏卡充值','搜狐一卡通'),('C',4,'ZHENGTU-NET',9,0,'易宝','游戏卡充值','征途游戏卡'),('C',4,'NETEASE-NET',9,0,'易宝','游戏卡充值','网易一卡通'),('C',4,'WANMEI-NET',9,0,'易宝','游戏卡充值','完美一卡通'),('C',4,'TIANXIA-NET',9,0,'易宝','游戏卡充值','天下一卡通'),('C',4,'ZONGYOU-NET',9,0,'易宝','游戏卡充值','纵游一卡通'),('C',4,'TIANHONG-NET',9,0,'易宝','游戏卡充值','天宏一卡通'),('D',1,'ICBC',10,0,'快钱','网银充值','中国工商银行'),('D',1,'ABC',10,0,'快钱','网银充值','中国农业银行'),('D',1,'CCB',10,-1,'快钱','网银充值','中国建设银行'),('D',1,'CMB',10,0,'快钱','网银充值','中国招商银行'),('D',1,'BOC',10,0,'快钱','网银充值','中国银行'),('D',1,'SPDB',10,0,'快钱','网银充值','上海浦东发展银行'),('D',1,'CIB',10,0,'快钱','网银充值','兴业银行'),('D',1,'GDB',10,0,'快钱','网银充值','广东发展银行'),('D',1,'SDB',10,0,'快钱','网银充值','深圳发展银行'),('D',1,'CMBC',10,0,'快钱','网银充值','中国民生银行'),('D',1,'COMM',10,0,'快钱','网银充值','交通银行'),('D',1,'CITIC',10,0,'快钱','网银充值','中信银行'),('D',1,'CEB',10,0,'快钱','网银充值','光大银行'),('D',3,'0',10,0,'快钱','充值卡充值','神州行充值卡'),('D',3,'1',10,0,'快钱','充值卡充值','联通充值卡'),('D',3,'3',10,0,'快钱','充值卡充值','电信充值卡'),('D',3,'4',10,0,'快钱','充值卡充值','骏网一卡通'),('D',4,'C',10,0,'快钱','游戏卡充值','盛大一卡通'),('D',4,'N',9,0,'快钱','游戏卡充值','搜狐一卡通'),('D',4,'D',9,0,'快钱','游戏卡充值','征途游戏卡'),('D',4,'M',9,0,'快钱','游戏卡充值','网易一卡通'),('D',4,'U',9,0,'快钱','游戏卡充值','完美一卡通');
/*!40000 ALTER TABLE `conf_pay_table` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `conf_server_table`
--

DROP TABLE IF EXISTS `conf_server_table`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `conf_server_table` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `gameId` int(10) unsigned NOT NULL,
  `serverNo` int(10) unsigned NOT NULL default '1' COMMENT '服务区编号',
  `serverName` varchar(45) NOT NULL,
  `serverStatus` tinyint(2) NOT NULL default '0' COMMENT '服务器状态，0为默认正常状态，1为火爆，2为拥挤，-1为关闭，-2为维护。',
  `recommand` bit(1) NOT NULL default b'1' COMMENT '1为推荐，0反之',
  `createTime` timestamp NOT NULL default '0000-00-00 00:00:00',
  `openingTime` timestamp NULL default NULL COMMENT '开区时间',
  `privateKey` varchar(100) NOT NULL,
  `site` varchar(45) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COMMENT='游戏服务器列表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `conf_server_table`
--

LOCK TABLES `conf_server_table` WRITE;
/*!40000 ALTER TABLE `conf_server_table` DISABLE KEYS */;
INSERT INTO `conf_server_table` VALUES (1,1,1,'[双线1服]烽火测试',1,'','2012-05-12 15:10:10','2012-05-26 08:42:32','QY7RODDDshen-16SDEDE-WAN-0668-2525-7ROAD-SHENQU-lovDDe777','sqtest_0001');
/*!40000 ALTER TABLE `conf_server_table` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `conf_system_variables`
--

DROP TABLE IF EXISTS `conf_system_variables`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `conf_system_variables` (
  `key` varchar(45) NOT NULL,
  `value` varchar(100) default NULL,
  `comment` tinytext,
  PRIMARY KEY  (`key`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='系统变量配置表。';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `conf_system_variables`
--

LOCK TABLES `conf_system_variables` WRITE;
/*!40000 ALTER TABLE `conf_system_variables` DISABLE KEYS */;
INSERT INTO `conf_system_variables` VALUES ('instance.id','X',NULL),('99bill.gatewayUrl','https://sandbox2.99bill.com/gateway/recvMerchantInfoAction.htm',NULL),('image.domainUrl','',NULL),('99bill.merchantAcctId','1001181534101',NULL),('meager.domainUrl','http://www.7road.com',NULL),('99bill.gcard.gatewayUrl','http://222.73.15.116/Pay_gatekq.aspx',NULL),('mail.password','hamleo24',NULL),('static.domainUrl','',NULL),('mail.account','liaopng@163.com',NULL),('99bill.pageUri','/AssertReturn',NULL),('pay.common.result.pageUri','/pay/result.html?type=result&_n=%s',NULL),('yeepay.encoding','GBK',NULL),('99bill.gcard.keyValue','ZHOUDANDAN901119',NULL),('alipay.notifyUri','/AssertNotify',NULL),('alipay.sellerEmail','finance_revenue@7road.com',NULL),('host.customerUri','http://account.7road.com/service/',NULL),('yeepay.onlinePaymentReqURL','http://tech.yeepay.com:8080/robot/debug.action',NULL),('99bill.inputCharset','1',NULL),('alipay.signType','MD5',NULL),('99bill.gcard.merchantAcctId','107901',NULL),('99bill.ccard.merchantAcctId','1000300079902',NULL),('host.payPageUri','/pay/index.html',NULL),('99bill.ccard.keyValue','IQUJRFHCMYJGYFWZ',NULL),('payHomePage.domainUrl','/pay/selectGame.jsp',NULL),('host.domain','',NULL),('alipay.showUrl','http://pay.7road.com',NULL),('homePage.domainUrl','http://sq.7road.com',NULL),('yeepay.keyValue','3P760JQ790R3jfvsdyF8o59GlPy280D21miSmo5542QM072D8z2213x1l4U9',NULL),('gateway.purchaseUri','/Purchase',NULL),('payPage.domainUrl','/pay/pay.jsp',NULL),('paySuccess.domainUrl','/pay/success.jsp',NULL),('alipay.inputCharset','UTF-8',NULL),('game.shenqu.loginUrl','http://s.test.ddshenqu.cn/login',NULL),('yeepay.merchantAcctId','10011832422',NULL),('alipay.keyValue','22feax5o47m6u5zrrr3ku6dsvb5tk49c',NULL),('forum.domainUrl','http://bbs.7road.com',NULL),('gateway.getOrderUri','/GetOrder',NULL),('payFail.domainUrl','/pay/fail.jsp',NULL),('alipay.merchantAcctId','2088701954617639',NULL),('99bill.ccard.gatewayUrl','https://www.99bill.com/szxgateway/recvMerchantInfoAction.htm',NULL),('yeepay.commonReqURL','https://www.yeepay.com/app-merchant-proxy/node',NULL),('alipay.verifyUrl','https://www.alipay.com/cooperate/gateway.do?service=notify_verify&',NULL),('pay.common.description','强撸灰飞烟',NULL),('alipay.gateway','https://mapi.alipay.com/gateway.do',NULL),('yeepay.queryRefundReqURL','http://tech.yeepay.com:8080/robot/debug.action',NULL),('help.payUri','http://account.7road.com/service/kf3.html',NULL),('game.shenqu.domainUrl','http://s.test.ddshenqu.cn/loginselectlist',NULL),('mail.host','smtp.163.com',NULL),('gateway.assertUri','/Assert',NULL),('host.accountUri','/accountcenter.html',NULL),('alipay.returnUri','/AssertReturn',NULL),('gateway.domain','http://localhost:8080',NULL),('99bill.bgUri','/AssertNotify',NULL),('account.centerUrl','/account/account.jsp',NULL);
/*!40000 ALTER TABLE `conf_system_variables` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2012-05-23 16:31:44
