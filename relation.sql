/*
Navicat MySQL Data Transfer

Source Server         : mysql
Source Server Version : 50624
Source Host           : localhost:3306
Source Database       : yychat6

Target Server Type    : MYSQL
Target Server Version : 50624
File Encoding         : 65001

Date: 2019-05-09 23:40:54
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for relation
-- ----------------------------
DROP TABLE IF EXISTS `relation`;
CREATE TABLE `relation` (
  `majoruser` varchar(20) COLLATE utf8_general_mysql500_ci DEFAULT NULL,
  `slaveuser` varchar(20) COLLATE utf8_general_mysql500_ci DEFAULT NULL,
  `relationtype` varchar(20) COLLATE utf8_general_mysql500_ci DEFAULT '',
  KEY `majoruser` (`majoruser`),
  KEY `slaveuser` (`slaveuser`),
  CONSTRAINT `relation_ibfk_1` FOREIGN KEY (`majoruser`) REFERENCES `user` (`username`),
  CONSTRAINT `relation_ibfk_2` FOREIGN KEY (`slaveuser`) REFERENCES `user` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_mysql500_ci;

-- ----------------------------
-- Records of relation
-- ----------------------------
INSERT INTO `relation` VALUES ('ry', 'ry1', '1');
INSERT INTO `relation` VALUES ('ry', 'ry2', '1');
INSERT INTO `relation` VALUES ('ry', 'ry3', '1');
