/*
Navicat MySQL Data Transfer

Source Server         : local
Source Server Version : 80017
Source Host           : 127.0.0.1:3306
Source Database       : yeecode

Target Server Type    : MYSQL
Target Server Version : 80017
File Encoding         : 65001

Date: 2019-11-27 15:27:39
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for user_memory
-- ----------------------------
DROP TABLE IF EXISTS `user_memory`;
CREATE TABLE `user_memory` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  `sex` int(255) DEFAULT NULL,
  `schoolName` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ui` (`schoolName`,`name`) USING HASH
) ENGINE=MEMORY;

-- ----------------------------
-- Records of user_memory
-- ----------------------------
INSERT INTO `user_memory` VALUES ('1', '易哥', 'yeecode@sample.com', '18', '0', 'Sunny School');
INSERT INTO `user_memory` VALUES ('2', '莉莉', 'lili@sample.com', '15', '1', 'Garden School');
INSERT INTO `user_memory` VALUES ('3', '杰克', 'jack@sample.com', '25', '0', 'Sunny School');
INSERT INTO `user_memory` VALUES ('4', '张大壮', 'zdazhaung@sample.com', '16', '0', 'Garden School');
INSERT INTO `user_memory` VALUES ('5', '王小壮', 'wxiaozhuang@sample.com', '27', '0', 'Sunny School');
INSERT INTO `user_memory` VALUES ('6', '露西', 'lucy@sample.com', '14', '1', 'Garden School');
INSERT INTO `user_memory` VALUES ('7', '李二壮', 'lerzhuang@sample.com', '9', '0', 'Sunny School');
