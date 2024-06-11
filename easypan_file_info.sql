CREATE DATABASE  IF NOT EXISTS `easypan` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `easypan`;
-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: localhost    Database: easypan
-- ------------------------------------------------------
-- Server version	8.0.35

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `file_info`
--

DROP TABLE IF EXISTS `file_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `file_info` (
  `file_id` varchar(10) NOT NULL COMMENT '文件ID',
  `user_id` varchar(15) NOT NULL COMMENT '用户ID',
  `file_md5` varchar(32) DEFAULT NULL COMMENT '文件MD5值',
  `file_pid` varchar(10) DEFAULT NULL COMMENT '父级ID',
  `file_size` bigint DEFAULT NULL COMMENT '文件大小',
  `file_name` varchar(200) DEFAULT NULL COMMENT '文件名称',
  `file_cover` varchar(100) DEFAULT NULL COMMENT '文件封面',
  `file_path` varchar(100) DEFAULT NULL COMMENT '文件路径',
  `create_time` datetime DEFAULT NULL COMMENT '文件创建时间',
  `last_update_time` datetime DEFAULT NULL COMMENT '文件最近更新时间',
  `folder_type` tinyint DEFAULT NULL COMMENT '0:文件 1:目录',
  `file_category` tinyint DEFAULT NULL COMMENT '文件分类 1:视频 2:音频 3:图片 4:文档 5:其他',
  `file_type` tinyint DEFAULT NULL COMMENT '1:视频 2:音频 3:图片 4:pdf 5:doc 6:excel 7:txt 8:code 9:zip 10:其他',
  `status` tinyint DEFAULT NULL COMMENT '0:转码中 1:转码失败 2:转码成功',
  `recovery_time` datetime DEFAULT NULL COMMENT '加入回收站的时间',
  `del_flag` tinyint DEFAULT NULL COMMENT '标记 0:删除 1:回收 2:正常',
  PRIMARY KEY (`file_id`,`user_id`),
  KEY `idx_create_time` (`create_time`) /*!80000 INVISIBLE */,
  KEY `idx_md5` (`file_md5`) /*!80000 INVISIBLE */,
  KEY `idx_file_pid` (`file_pid`) /*!80000 INVISIBLE */,
  KEY `idx_del_flag` (`del_flag`) /*!80000 INVISIBLE */,
  KEY `idx_recover_time` (`recovery_time`) /*!80000 INVISIBLE */,
  KEY `idx_status` (`status`) /*!80000 INVISIBLE */
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `file_info`
--

LOCK TABLES `file_info` WRITE;
/*!40000 ALTER TABLE `file_info` DISABLE KEYS */;
INSERT INTO `file_info` VALUES ('dmeN4SpMRz','358971705640476','98336063ef3a6cb1df5e98f94d1c7c10','0',246899,'Ai1.jpg','202405/358971705640476dmeN4SpMRz_.jpg','202405/358971705640476dmeN4SpMRz.jpg','2024-05-19 21:08:02','2024-05-19 21:08:02',0,3,3,2,NULL,2);
/*!40000 ALTER TABLE `file_info` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-06-11 22:50:12
