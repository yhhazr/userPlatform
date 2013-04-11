-- MySQL dump 10.13  Distrib 5.5.22, for debian-linux-gnu (x86_64)
--
-- Host: 127.0.0.1    Database: db_userplatform
-- ------------------------------------------------------
-- Server version	5.1.56-log

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
-- Table structure for table `dt_account`
--

DROP TABLE IF EXISTS `dt_account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dt_account` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `userName` varchar(16) NOT NULL COMMENT '用户名，6-16个字符或数字',
  `passWord` char(32) NOT NULL,
  `email` varchar(100) DEFAULT NULL,
  `status` tinyint(2) DEFAULT '0' COMMENT '账号状态，0为正常。',
  PRIMARY KEY (`id`),
  UNIQUE KEY `userName_UNIQUE` (`userName`)
) ENGINE=InnoDB AUTO_INCREMENT=312 DEFAULT CHARSET=utf8 COMMENT='用户账号表。';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `dt_order`
--

DROP TABLE IF EXISTS `dt_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dt_order` (
  `id` char(21) NOT NULL COMMENT '订单号',
  `channelTag` char(2) NOT NULL COMMENT '支付渠道标签',
  `subTag` varchar(20) DEFAULT NULL COMMENT '子标签',
  `amount` decimal(12,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '支付金额，以分为单位',
  `gold` int(10) unsigned NOT NULL,
  `status` tinyint(2) NOT NULL DEFAULT '0' COMMENT '支付状态，0表示已提交，-1表示故障，1表示>成功',
  `currency` tinyint(2) unsigned NOT NULL DEFAULT '0' COMMENT '货币类型，0为CNY',
  `userId` int(10) unsigned NOT NULL COMMENT '用户ID',
  `playerId` int(10) unsigned NOT NULL COMMENT '游戏角色ID',
  `gameId` int(10) unsigned NOT NULL COMMENT '游戏ID',
  `zoneId` mediumint(5) unsigned NOT NULL COMMENT '游戏区ID',
  `payTime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '支付提交时间',
  `assertTime` timestamp NULL DEFAULT NULL COMMENT '支付确认时间',
  `endOrder` varchar(45) DEFAULT NULL COMMENT '对方订单号',
  `clientIp` varchar(15) DEFAULT NULL COMMENT '客户端IP',
  `ext1` varchar(45) DEFAULT NULL COMMENT '扩展信息1',
  `ext2` varchar(45) DEFAULT NULL COMMENT '扩展信息2',
  `ext3` varchar(45) DEFAULT NULL COMMENT '扩展信息3',
  PRIMARY KEY (`id`),
  KEY `channelTag` (`channelTag`),
  KEY `userId` (`userId`,`playerId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='充值订单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `dt_user_1`
--

DROP TABLE IF EXISTS `dt_user_1`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dt_user_1` (
  `id` int(10) unsigned NOT NULL,
  `userName` varchar(16) NOT NULL COMMENT '用户名',
  `aggrRecharge` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '累计充值，单位RMB',
  `createTime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `realName` varchar(8) DEFAULT NULL COMMENT '真实姓名，最多支持4个汉字',
  `icn` char(18) DEFAULT NULL COMMENT '身份证号码,18位数',
  `lastIp` varchar(15) DEFAULT NULL COMMENT '上一次登录IP',
  `lastLoginTime` timestamp NULL DEFAULT NULL COMMENT '上一次登录时间',
  `lastGameId` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '最近次登录的游戏ID',
  `lastGameZoneId` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '最近次登录的游戏大区ID',
  `loginSum` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '登录总次数',
  `site` varchar(45) DEFAULT NULL COMMENT '注册渠道标识',
  `gender` tinyint(2) NOT NULL DEFAULT '0',
  `birthday` date DEFAULT NULL,
  `city` varchar(100) DEFAULT NULL COMMENT '所在城市，例如 广东省，深圳市，南山区。',
  `career` varchar(45) DEFAULT NULL COMMENT '职业',
  `safeLevel` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_name_UNIQUE` (`userName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户信息数据表。';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping routines for database 'db_userplatform'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2012-05-21 16:50:36
