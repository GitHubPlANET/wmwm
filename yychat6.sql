/*
Navicat MySQL Data Transfer

Source Server         : mysql
Source Server Version : 50624
Source Host           : localhost:3306
Source Database       : yychat6

Target Server Type    : MYSQL
Target Server Version : 50624
File Encoding         : 65001

Date: 2019-05-09 00:27:04
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for yychat6
-- ----------------------------
DROP TABLE IF EXISTS `yychat6`;
CREATE TABLE `yychat6` (
  `id` varchar(20) COLLATE utf8_general_mysql500_ci NOT NULL,
  `username` varchar(20) COLLATE utf8_general_mysql500_ci DEFAULT NULL,
  `password` varchar(20) COLLATE utf8_general_mysql500_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_mysql500_ci;

-- ----------------------------
-- Records of yychat6
-- ----------------------------
INSERT INTO `yychat6` VALUES ('1', 'ry', '123456');
INSERT INTO `yychat6` VALUES ('2', 'ry1', '123456');
INSERT INTO `yychat6` VALUES ('3', 'ry2', '123456');
INSERT INTO `yychat6` VALUES ('4', 'ry3', '123456');
