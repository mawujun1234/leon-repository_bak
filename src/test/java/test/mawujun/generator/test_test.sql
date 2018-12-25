/*
 Navicat Premium Data Transfer

 Source Server         : zhjc
 Source Server Type    : MySQL
 Source Server Version : 50720
 Source Host           : rm-bp162m80wo2w8t2gu5o.mysql.rds.aliyuncs.com:3306
 Source Schema         : zhjctest

 Target Server Type    : MySQL
 Target Server Version : 50720
 File Encoding         : 65001

 Date: 25/12/2018 19:38:04
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for test_test
-- ----------------------------
DROP TABLE IF EXISTS `test_test`;
CREATE TABLE `test_test`  (
  `id1` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `id2` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `age` int(11) NULL DEFAULT 1,
  `price` decimal(10, 2) NULL DEFAULT 1.10,
  `test` float(4, 0) NULL DEFAULT NULL,
  `timee` double(10, 2) NULL DEFAULT NULL COMMENT '测试和注释',
  PRIMARY KEY (`id1`, `id2`) USING BTREE,
  UNIQUE INDEX `aaa`(`name`, `age`) USING BTREE,
  INDEX `bbb`(`price`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '标记注释' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
