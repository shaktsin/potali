-- MySQL dump 10.13  Distrib 5.6.19, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: ofc
-- ------------------------------------------------------
-- Server version	5.6.19-0ubuntu0.14.04.1

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
-- Table structure for table `address`
--

DROP TABLE IF EXISTS `address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `address` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `line_one` varchar(45) NOT NULL,
  `line_two` varchar(45) DEFAULT NULL,
  `street` varchar(45) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `pincode_id` int(11) NOT NULL,
  `pincode_state_id` int(11) NOT NULL,
  PRIMARY KEY (`id`,`pincode_id`,`pincode_state_id`),
  KEY `fk_address_pincode1_idx` (`pincode_id`,`pincode_state_id`),
  CONSTRAINT `fk_address_pincode1` FOREIGN KEY (`pincode_id`, `pincode_state_id`) REFERENCES `pincode` (`id`, `state_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `address`
--

LOCK TABLES `address` WRITE;
/*!40000 ALTER TABLE `address` DISABLE KEYS */;
/*!40000 ALTER TABLE `address` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `attachment`
--

DROP TABLE IF EXISTS `attachment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `attachment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `attachment_type` int(11) NOT NULL,
  `path` varchar(45) DEFAULT NULL,
  `size` int(11) NOT NULL,
  `created_date` datetime NOT NULL,
  `updated_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `post_id` int(11) NOT NULL,
  `width` int(11) DEFAULT NULL,
  `height` int(11) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `format` int(11) DEFAULT NULL,
  `public_id` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_attachment_post1_idx` (`post_id`),
  CONSTRAINT `fk_attachment_post1` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `attachment`
--

LOCK TABLES `attachment` WRITE;
/*!40000 ALTER TABLE `attachment` DISABLE KEYS */;
INSERT INTO `attachment` VALUES (1,0,'40/0_fit.jpg',3,'2015-01-29 23:17:02','2015-01-29 17:47:02',40,NULL,NULL,NULL,NULL,NULL),(2,0,'41/0_fit.jpg',3,'2015-01-29 23:27:59','2015-01-29 17:57:59',41,NULL,NULL,NULL,NULL,NULL),(3,0,'42/0_fit.jpg',3,'2015-01-29 23:49:28','2015-01-29 18:19:28',42,NULL,NULL,NULL,NULL,NULL),(7,0,'47/0_fit.jpg',3,'2015-02-01 11:34:26','2015-02-01 06:04:26',47,NULL,NULL,NULL,NULL,NULL),(12,0,'53/image/0_fit.jpg',3,'2015-02-17 00:25:45','2015-02-16 18:55:45',53,450,253,1424112946,0,'post/53/image/12'),(13,0,'54/image/0_fit.jpg',3,'2015-02-17 00:33:35','2015-02-16 19:03:35',54,450,253,1424113425,0,'post/54/image/13');
/*!40000 ALTER TABLE `attachment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `avatar`
--

DROP TABLE IF EXISTS `avatar`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `avatar` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` tinyint(1) NOT NULL,
  `height` int(11) NOT NULL,
  `width` int(11) NOT NULL,
  `url` varchar(100) DEFAULT NULL,
  `created_date` datetime NOT NULL,
  `updated_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `user_id` int(11) NOT NULL,
  `user_institute_id` int(11) NOT NULL,
  `public_id` varchar(100) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `format` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_avatar_user1_idx` (`user_id`,`user_institute_id`),
  CONSTRAINT `fk_avatar_user1` FOREIGN KEY (`user_id`, `user_institute_id`) REFERENCES `user` (`id`, `institute_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `avatar`
--

LOCK TABLES `avatar` WRITE;
/*!40000 ALTER TABLE `avatar` DISABLE KEYS */;
/*!40000 ALTER TABLE `avatar` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cache_cluster`
--

DROP TABLE IF EXISTS `cache_cluster`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cache_cluster` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `host` varchar(45) NOT NULL,
  `port` varchar(45) NOT NULL,
  `type` varchar(45) NOT NULL,
  `active` tinyint(1) NOT NULL DEFAULT '0',
  `primary` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cache_cluster`
--

LOCK TABLES `cache_cluster` WRITE;
/*!40000 ALTER TABLE `cache_cluster` DISABLE KEYS */;
INSERT INTO `cache_cluster` VALUES (1,'localhost','9300','es',1,0),(2,'localhost','11211','memcache',1,0);
/*!40000 ALTER TABLE `cache_cluster` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `campus`
--

DROP TABLE IF EXISTS `campus`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `campus` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `institute_id` int(11) NOT NULL,
  `address_id` int(11) NOT NULL,
  `address_pincode_id` int(11) NOT NULL,
  `address_pincode_state_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_campus_institute_idx` (`institute_id`),
  KEY `fk_campus_address1_idx` (`address_id`,`address_pincode_id`,`address_pincode_state_id`),
  CONSTRAINT `fk_campus_address1` FOREIGN KEY (`address_id`, `address_pincode_id`, `address_pincode_state_id`) REFERENCES `address` (`id`, `pincode_id`, `pincode_state_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_campus_institute` FOREIGN KEY (`institute_id`) REFERENCES `institute` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `campus`
--

LOCK TABLES `campus` WRITE;
/*!40000 ALTER TABLE `campus` DISABLE KEYS */;
/*!40000 ALTER TABLE `campus` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `circle`
--

DROP TABLE IF EXISTS `circle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `circle` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `created_date` datetime NOT NULL,
  `updated_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `circle_type_id` int(11) NOT NULL,
  `public` tinyint(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `circle`
--

LOCK TABLES `circle` WRITE;
/*!40000 ALTER TABLE `circle` DISABLE KEYS */;
INSERT INTO `circle` VALUES (21,'EveryOne','2015-01-29 11:29:56','2015-01-29 05:59:56',0,1);
/*!40000 ALTER TABLE `circle` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `city`
--

DROP TABLE IF EXISTS `city`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `city` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `state_id` int(11) NOT NULL,
  PRIMARY KEY (`id`,`state_id`),
  KEY `fk_city_state1_idx` (`state_id`),
  CONSTRAINT `fk_city_state1` FOREIGN KEY (`state_id`) REFERENCES `state` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `city`
--

LOCK TABLES `city` WRITE;
/*!40000 ALTER TABLE `city` DISABLE KEYS */;
INSERT INTO `city` VALUES (1,'Gurgaon',1),(2,'Noida',2),(3,'New Delhi',3);
/*!40000 ALTER TABLE `city` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comments`
--

DROP TABLE IF EXISTS `comments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `comments` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `comment` text,
  `created_date` datetime DEFAULT NULL,
  `updated_date` timestamp NULL DEFAULT NULL,
  `post_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `user_institute_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_comments_post1_idx` (`post_id`),
  KEY `fk_comments_user1_idx` (`user_id`,`user_institute_id`),
  CONSTRAINT `fk_comments_post1` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_comments_user1` FOREIGN KEY (`user_id`, `user_institute_id`) REFERENCES `user` (`id`, `institute_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comments`
--

LOCK TABLES `comments` WRITE;
/*!40000 ALTER TABLE `comments` DISABLE KEYS */;
INSERT INTO `comments` VALUES (1,'Nice Job','2015-01-18 14:18:18','2015-01-18 08:48:18',36,3,1),(2,'Nice Job','2015-01-18 14:20:13','2015-01-18 08:50:13',36,3,1);
/*!40000 ALTER TABLE `comments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `country`
--

DROP TABLE IF EXISTS `country`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `country` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `country_code` varchar(3) DEFAULT NULL,
  `iso_code` varchar(3) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `country`
--

LOCK TABLES `country` WRITE;
/*!40000 ALTER TABLE `country` DISABLE KEYS */;
INSERT INTO `country` VALUES (1,'India','IN','91');
/*!40000 ALTER TABLE `country` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `industry`
--

DROP TABLE IF EXISTS `industry`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `industry` (
  `id` int(11) NOT NULL,
  `name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `industry`
--

LOCK TABLES `industry` WRITE;
/*!40000 ALTER TABLE `industry` DISABLE KEYS */;
INSERT INTO `industry` VALUES (1,'OfCampus'),(2,'Amazon'),(3,'Microsoft');
/*!40000 ALTER TABLE `industry` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `industry_roles`
--

DROP TABLE IF EXISTS `industry_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `industry_roles` (
  `id` int(11) NOT NULL,
  `name` varchar(45) NOT NULL,
  `industry_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_job_roles_industry1_idx` (`industry_id`),
  CONSTRAINT `fk_job_roles_industry1` FOREIGN KEY (`industry_id`) REFERENCES `industry` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `industry_roles`
--

LOCK TABLES `industry_roles` WRITE;
/*!40000 ALTER TABLE `industry_roles` DISABLE KEYS */;
INSERT INTO `industry_roles` VALUES (1,'Software Engineer',1),(2,'Financial Analyst',2),(3,'Consultant',3);
/*!40000 ALTER TABLE `industry_roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `institute`
--

DROP TABLE IF EXISTS `institute`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `institute` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `email_suffix` varchar(45) NOT NULL,
  `third_party_auth` tinyint(1) DEFAULT NULL,
  `provider` tinyint(1) DEFAULT NULL,
  `type` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `institute`
--

LOCK TABLES `institute` WRITE;
/*!40000 ALTER TABLE `institute` DISABLE KEYS */;
INSERT INTO `institute` VALUES (1,'Ofc','gmail.com',1,1,0),(2,'ofc','ofcampus.com',0,1,0);
/*!40000 ALTER TABLE `institute` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `job_post`
--

DROP TABLE IF EXISTS `job_post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `job_post` (
  `id` int(11) NOT NULL DEFAULT '0',
  `exp_to` int(11) DEFAULT NULL,
  `exp_from` int(11) DEFAULT NULL,
  `salary_to` double DEFAULT NULL,
  `salary_from` double DEFAULT NULL,
  `time_specified` tinyint(1) DEFAULT NULL,
  `salary_specified` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_post` FOREIGN KEY (`id`) REFERENCES `post` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `job_post`
--

LOCK TABLES `job_post` WRITE;
/*!40000 ALTER TABLE `job_post` DISABLE KEYS */;
INSERT INTO `job_post` VALUES (8,0,1,100000,500000,0,0),(9,0,1,100000,500000,0,0),(10,0,1,100000,500000,0,0),(11,0,1,100000,500000,0,0),(12,0,1,100000,500000,0,0),(13,0,1,100000,500000,1,1),(14,0,1,100000,500000,1,1),(15,0,1,100000,500000,1,1),(16,0,1,100000,500000,1,1),(17,0,1,100000,500000,1,1),(18,0,1,100000,500000,1,1),(19,0,1,100000,500000,1,1),(20,0,1,100000,500000,1,1),(21,0,1,100000,500000,1,1),(22,0,1,100000,500000,1,1),(23,0,1,100000,500000,1,1),(24,0,1,100000,500000,1,1),(25,0,1,100000,500000,1,1),(26,0,1,100000,500000,1,1),(27,0,1,500000,1200000,1,1),(28,0,1,500000,1200000,1,1),(29,0,1,100000,500000,1,1),(30,25,26,999,999,1,1),(31,25,26,999,999,1,1),(32,25,26,999,999,1,1),(33,25,26,999,999,1,1),(34,25,26,999,999,1,1),(35,25,26,999,999,1,1),(36,25,26,999,999,1,1),(37,25,26,999,999,1,1),(39,25,26,999,999,1,1),(40,25,26,999,999,1,1),(41,25,26,999,999,1,1),(42,25,26,999,999,1,1),(47,25,26,999,999,1,1),(53,25,26,999,999,1,1),(54,25,26,999,999,1,1);
/*!40000 ALTER TABLE `job_post` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `job_post_has_city`
--

DROP TABLE IF EXISTS `job_post_has_city`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `job_post_has_city` (
  `job_post_to` int(11) NOT NULL,
  `city_id` int(11) NOT NULL,
  PRIMARY KEY (`job_post_to`,`city_id`),
  KEY `fk_job_post_has_city_city1_idx` (`city_id`),
  KEY `fk_job_post_has_city_job_post1_idx` (`job_post_to`),
  CONSTRAINT `fk_job_post_has_city_city1` FOREIGN KEY (`city_id`) REFERENCES `city` (`id`),
  CONSTRAINT `fk_job_post_has_city_job_post1` FOREIGN KEY (`job_post_to`) REFERENCES `job_post` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `job_post_has_city`
--

LOCK TABLES `job_post_has_city` WRITE;
/*!40000 ALTER TABLE `job_post_has_city` DISABLE KEYS */;
INSERT INTO `job_post_has_city` VALUES (8,1),(9,1),(10,1),(11,1),(12,1),(13,1),(14,1),(15,1),(16,1),(17,1),(18,1),(19,1),(20,1),(21,1),(22,1),(23,1),(24,1),(25,1),(26,1),(27,1),(29,1),(27,2),(28,3),(30,3),(31,3),(32,3),(33,3),(34,3),(35,3),(36,3),(37,3),(39,3),(40,3),(41,3),(42,3),(47,3),(53,3),(54,3);
/*!40000 ALTER TABLE `job_post_has_city` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `job_post_has_job_roles`
--

DROP TABLE IF EXISTS `job_post_has_job_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `job_post_has_job_roles` (
  `job_post_to` int(11) NOT NULL,
  `job_roles_id` int(11) NOT NULL,
  PRIMARY KEY (`job_post_to`,`job_roles_id`),
  KEY `fk_job_post_has_job_roles_job_roles1_idx` (`job_roles_id`),
  KEY `fk_job_post_has_job_roles_job_post1_idx` (`job_post_to`),
  CONSTRAINT `fk_job_post_has_job_roles_job_post1` FOREIGN KEY (`job_post_to`) REFERENCES `job_post` (`id`),
  CONSTRAINT `fk_job_post_has_job_roles_job_roles1` FOREIGN KEY (`job_roles_id`) REFERENCES `industry_roles` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `job_post_has_job_roles`
--

LOCK TABLES `job_post_has_job_roles` WRITE;
/*!40000 ALTER TABLE `job_post_has_job_roles` DISABLE KEYS */;
INSERT INTO `job_post_has_job_roles` VALUES (8,1),(9,1),(10,1),(11,1),(12,1),(13,1),(14,1),(15,1),(16,1),(17,1),(18,1),(19,1),(20,1),(21,1),(22,1),(23,1),(24,1),(25,1),(26,1),(27,1),(29,1),(30,1),(31,1),(32,1),(33,1),(34,1),(35,1),(36,1),(37,1),(39,1),(40,1),(41,1),(42,1),(47,1),(53,1),(54,1),(27,2),(28,3);
/*!40000 ALTER TABLE `job_post_has_job_roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `location`
--

DROP TABLE IF EXISTS `location`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `location` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `lng` varchar(45) DEFAULT NULL,
  `lat` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `location`
--

LOCK TABLES `location` WRITE;
/*!40000 ALTER TABLE `location` DISABLE KEYS */;
/*!40000 ALTER TABLE `location` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pincode`
--

DROP TABLE IF EXISTS `pincode`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pincode` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pincode` varchar(45) DEFAULT NULL,
  `state_id` int(11) NOT NULL,
  PRIMARY KEY (`id`,`state_id`),
  KEY `fk_pincode_state1_idx` (`state_id`),
  CONSTRAINT `fk_pincode_state1` FOREIGN KEY (`state_id`) REFERENCES `state` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pincode`
--

LOCK TABLES `pincode` WRITE;
/*!40000 ALTER TABLE `pincode` DISABLE KEYS */;
/*!40000 ALTER TABLE `pincode` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `place`
--

DROP TABLE IF EXISTS `place`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `place` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `google_place_id` varchar(45) DEFAULT NULL,
  `location_id` int(11) NOT NULL,
  `pincode_id` int(11) NOT NULL,
  `pincode_state_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_place_location1_idx` (`location_id`),
  KEY `fk_place_pincode1_idx` (`pincode_id`,`pincode_state_id`),
  CONSTRAINT `fk_place_location1` FOREIGN KEY (`location_id`) REFERENCES `location` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_place_pincode1` FOREIGN KEY (`pincode_id`, `pincode_state_id`) REFERENCES `pincode` (`id`, `state_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `place`
--

LOCK TABLES `place` WRITE;
/*!40000 ALTER TABLE `place` DISABLE KEYS */;
/*!40000 ALTER TABLE `place` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `post`
--

DROP TABLE IF EXISTS `post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `post` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `subject` varchar(500) NOT NULL,
  `content` mediumtext,
  `reply_email` varchar(45) DEFAULT NULL,
  `reply_phone` varchar(45) DEFAULT NULL,
  `reply_watspp` varchar(45) DEFAULT NULL,
  `created_date` datetime NOT NULL,
  `updated_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `user_id` int(11) NOT NULL,
  `user_institute_id` int(11) NOT NULL,
  `share_email` tinyint(1) DEFAULT '-1',
  `share_phone` tinyint(1) DEFAULT '-1',
  `share_watsapp` tinyint(1) DEFAULT '-1',
  PRIMARY KEY (`id`),
  KEY `fk_post_user1_idx` (`user_id`,`user_institute_id`),
  CONSTRAINT `fk_post_user1` FOREIGN KEY (`user_id`, `user_institute_id`) REFERENCES `user` (`id`, `institute_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post`
--

LOCK TABLES `post` WRITE;
/*!40000 ALTER TABLE `post` DISABLE KEYS */;
INSERT INTO `post` VALUES (8,'Job For MBA Brats','A nice well suited & buited job for mba brats','sushant@gmail.com','9560991111','9560991111','2014-12-21 00:00:00','2014-12-25 10:09:58',3,1,-1,-1,-1),(9,'Job For MBA Brats','A nice well suited & buited job for mba brats','sushant@gmail.com','9560991111','9560991111','2014-12-21 00:00:00','2014-12-25 10:09:58',3,1,-1,-1,-1),(10,'Job For MBA Brats','A nice well suited & buited job for mba brats','sushant@gmail.com','9560991111','9560991111','2014-12-21 00:00:00','2014-12-25 10:09:58',3,1,-1,-1,-1),(11,'Job For MBA Brats','A nice well suited & buited job for mba brats','sushant@gmail.com','9560991111','9560991111','2014-12-21 00:00:00','2014-12-25 10:09:58',3,1,-1,-1,-1),(12,'Job For MBA Brats','A nice well suited & buited job for mba brats','sushant@gmail.com','9560991111','9560991111','2014-12-21 00:00:00','2014-12-25 10:09:58',3,1,-1,-1,-1),(13,'Job For MBA Brats','A nice well suited & buited job for mba brats','sushant@gmail.com','9560991111','9560991111','2014-12-25 00:00:00','2014-12-25 10:09:58',3,1,-1,-1,-1),(14,'Job For MBA Brats','A nice well suited & buited job for mba brats','sushant@gmail.com','9560991111','9560991111','2014-12-25 00:00:00','2014-12-25 10:09:58',3,1,-1,-1,-1),(15,'Job For MBA Brats','A nice well suited & buited job for mba brats','sushant@gmail.com','9560991111','9560991111','2014-12-25 00:00:00','2014-12-25 10:09:58',3,1,-1,-1,-1),(16,'Job For MBA Brats','A nice well suited & buited job for mba brats','sushant@gmail.com','9560991111','9560991111','2014-12-25 00:00:00','2014-12-25 10:09:58',3,1,-1,-1,-1),(17,'Job For MBA Brats','A nice well suited & buited job for mba brats','sushant@gmail.com','9560991111','9560991111','2014-12-25 00:00:00','2014-12-25 10:09:58',3,1,-1,-1,-1),(18,'Job For MBA Brats','A nice well suited & buited job for mba brats','sushant@gmail.com','9560991111','9560991111','2014-12-25 00:00:00','2014-12-25 10:09:58',3,1,-1,-1,-1),(19,'Job For MBA Brats','A nice well suited & buited job for mba brats','sushant@gmail.com','9560991111','9560991111','2014-12-25 00:00:00','2014-12-25 10:09:58',3,1,-1,-1,-1),(20,'Job For MBA Brats','A nice well suited & buited job for mba brats','sushant@gmail.com','9560991111','9560991111','2014-12-25 00:00:00','2014-12-25 10:09:58',3,1,-1,-1,-1),(21,'Job For MBA Brats','A nice well suited & buited job for mba brats','sushant@gmail.com','9560991111','9560991111','2014-12-25 00:00:00','2014-12-25 10:09:58',3,1,-1,-1,-1),(22,'Job For MBA Brats','A nice well suited & buited job for mba brats','sushant@gmail.com','9560991111','9560991111','2014-12-25 00:00:00','2014-12-25 10:14:11',3,1,-1,-1,-1),(23,'Job For MBA Brats','A nice well suited & buited job for mba brats','sushant@gmail.com','9560991111','9560991111','2014-12-25 15:45:49','2014-12-25 10:15:49',3,1,-1,-1,-1),(24,'Job For MBA Brats','A nice well suited & buited job for mba brats','sushant@gmail.com','9560991111','9560991111','2014-12-25 15:52:51','2014-12-25 10:22:53',3,1,-1,-1,-1),(25,'Job For MBA Brats','A nice well suited & buited job for mba brats','sushant@gmail.com','9560991111','9560991111','2014-12-25 15:56:06','2014-12-25 10:26:06',3,1,-1,-1,-1),(26,'Job For MBA Brats','A nice well suited & buited job for mba brats','sushant@gmail.com','9560991111','9560991111','2014-12-25 16:17:11','2014-12-25 10:47:11',3,1,-1,-1,-1),(27,'Job For Techies','Android Developer needed','sushant@gmail.com','9560991111','9560991111','2014-12-25 20:37:42','2014-12-25 15:07:42',3,1,-1,-1,-1),(28,'Job For Techies','Android Developer needed','sushant@gmail.com','9560991111','9560991111','2014-12-25 20:38:36','2014-12-25 15:08:36',3,1,-1,-1,-1),(29,'Job For MBA Brats','A nice well suited & buited job for mba brats','sushant@gmail.com','9560991111','9560991111','2014-12-25 20:49:38','2014-12-25 15:19:38',3,1,-1,-1,-1),(30,'bn','bbb','dibakar@ofcampus.com','','','2015-01-05 22:19:19','2015-01-05 16:49:19',3,1,-1,-1,-1),(31,'bn','bbb','dibakar@ofcampus.com','','','2015-01-18 00:47:45','2015-01-17 19:17:45',3,1,-1,-1,-1),(32,'bn','bbb','dibakar@ofcampus.com','','','2015-01-18 00:55:39','2015-01-17 19:25:39',3,1,-1,-1,-1),(33,'bn','bbb','dibakar@ofcampus.com','','','2015-01-18 01:02:47','2015-01-17 19:32:47',3,1,-1,-1,-1),(34,'bn','bbb','dibakar@ofcampus.com','','','2015-01-18 01:10:55','2015-01-17 19:40:55',3,1,-1,-1,-1),(35,'bn','bbb','dibakar@ofcampus.com','','','2015-01-18 01:18:43','2015-01-17 19:48:43',3,1,-1,-1,-1),(36,'bn','bbb','dibakar@ofcampus.com','','','2015-01-18 02:43:20','2015-01-17 21:13:20',3,1,-1,-1,-1),(37,'bn','bbb','dibakar@ofcampus.com','','','2015-01-18 02:47:20','2015-01-17 21:17:20',3,1,-1,-1,-1),(39,'bn','bbb','dibakar@ofcampus.com','','','2015-01-29 16:38:57','2015-01-29 11:08:57',3,1,-1,-1,-1),(40,'bn','bbb','dibakar@ofcampus.com','','','2015-01-29 23:14:28','2015-01-29 17:44:28',3,1,-1,-1,-1),(41,'bn','bbb','dibakar@ofcampus.com','','','2015-01-29 23:26:56','2015-01-29 17:56:56',3,1,-1,-1,-1),(42,'bn','bbb','dibakar@ofcampus.com','','','2015-01-29 23:49:26','2015-01-29 18:19:26',3,1,-1,-1,-1),(47,'bn','bbb','dibakar@ofcampus.com','','','2015-02-01 11:34:15','2015-02-01 06:04:15',3,1,-1,-1,-1),(53,'bn','bbb','dibakar@ofcampus.com','','','2015-02-17 00:25:44','2015-02-16 18:55:44',3,1,-1,-1,-1),(54,'bn','bbb','dibakar@ofcampus.com','','','2015-02-17 00:33:34','2015-02-16 19:03:34',3,1,-1,-1,-1);
/*!40000 ALTER TABLE `post` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `post_blob`
--

DROP TABLE IF EXISTS `post_blob`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `post_blob` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content` mediumtext,
  `post_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_post_blob_post1_idx` (`post_id`),
  CONSTRAINT `fk_post_blob_post1` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post_blob`
--

LOCK TABLES `post_blob` WRITE;
/*!40000 ALTER TABLE `post_blob` DISABLE KEYS */;
INSERT INTO `post_blob` VALUES (3,'A nice well suited & buited job for mba brats',8),(4,'A nice well suited & buited job for mba brats',9),(5,'A nice well suited & buited job for mba brats',10),(6,'A nice well suited & buited job for mba brats',11),(7,'A nice well suited & buited job for mba brats',12),(8,'A nice well suited & buited job for mba brats',13),(9,'A nice well suited & buited job for mba brats',14),(10,'A nice well suited & buited job for mba brats',15),(11,'A nice well suited & buited job for mba brats',16),(12,'A nice well suited & buited job for mba brats',17),(13,'A nice well suited & buited job for mba brats',18),(14,'A nice well suited & buited job for mba brats',19),(15,'A nice well suited & buited job for mba brats',20),(16,'A nice well suited & buited job for mba brats',21),(17,'A nice well suited & buited job for mba brats',22),(18,'A nice well suited & buited job for mba brats',23),(19,'A nice well suited & buited job for mba brats',24),(20,'A nice well suited & buited job for mba brats',25),(21,'A nice well suited & buited job for mba brats',26),(22,'Android Developer needed',27),(23,'Android Developer needed',28),(24,'A nice well suited & buited job for mba brats',29),(25,'bbb',30),(26,'bbb',31),(27,'bbb',32),(28,'bbb',33),(29,'bbb',34),(30,'bbb',35),(31,'bbb',36),(32,'bbb',37),(34,'bbb',39),(35,'bbb',40),(36,'bbb',41),(37,'bbb',42),(42,'bbb',47),(48,'bbb',53),(49,'bbb',54);
/*!40000 ALTER TABLE `post_blob` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `post_reaction`
--

DROP TABLE IF EXISTS `post_reaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `post_reaction` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `reactions_id` int(11) NOT NULL,
  `post_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `created_date` datetime NOT NULL,
  `updated_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_post_reaction_reactions1_idx` (`reactions_id`),
  KEY `fk_post_reaction_post1_idx` (`post_id`),
  KEY `fk_post_reaction_user1_idx` (`user_id`),
  CONSTRAINT `fk_post_reaction_post1` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_post_reaction_reactions1` FOREIGN KEY (`reactions_id`) REFERENCES `reactions` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_post_reaction_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post_reaction`
--

LOCK TABLES `post_reaction` WRITE;
/*!40000 ALTER TABLE `post_reaction` DISABLE KEYS */;
INSERT INTO `post_reaction` VALUES (2,1,29,3,'2014-12-28 15:42:17','2014-12-28 10:12:17'),(3,2,28,3,'2015-01-10 19:39:46','2015-01-10 14:09:46'),(4,10,36,3,'2015-01-18 13:31:56','2015-01-18 08:01:56'),(5,1,54,3,'2015-02-18 00:18:58','2015-02-17 18:48:58'),(6,1,53,3,'2015-02-18 00:19:27','2015-02-17 18:49:27'),(7,1,47,3,'2015-02-18 00:19:59','2015-02-17 18:49:59'),(8,1,54,3,'2015-02-18 00:25:53','2015-02-17 18:55:53'),(9,1,47,3,'2015-02-18 00:26:15','2015-02-17 18:56:15'),(10,1,42,3,'2015-02-18 00:26:35','2015-02-17 18:56:35'),(11,1,41,3,'2015-02-18 00:27:00','2015-02-17 18:57:00');
/*!40000 ALTER TABLE `post_reaction` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reactions`
--

DROP TABLE IF EXISTS `reactions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `reactions` (
  `id` int(11) NOT NULL,
  `name` varchar(45) NOT NULL,
  `priority` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reactions`
--

LOCK TABLES `reactions` WRITE;
/*!40000 ALTER TABLE `reactions` DISABLE KEYS */;
INSERT INTO `reactions` VALUES (1,'Hide this Post',-1),(2,'Mark As Important',5),(3,'Mark As Spam',-5),(4,'Reply Via Email',1),(5,'Reply Via WatsApp',1),(6,'Reply Via Phone',1),(7,'Share Via Phone',2),(8,'Share Via Email',2),(9,'Share By WatsApp',2),(10,'Comment',5);
/*!40000 ALTER TABLE `reactions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role` (
  `id` int(11) NOT NULL,
  `name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `state`
--

DROP TABLE IF EXISTS `state`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `state` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `country_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_state_country1_idx` (`country_id`),
  CONSTRAINT `fk_state_country1` FOREIGN KEY (`country_id`) REFERENCES `country` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `state`
--

LOCK TABLES `state` WRITE;
/*!40000 ALTER TABLE `state` DISABLE KEYS */;
INSERT INTO `state` VALUES (1,'Haryana',1),(2,'Uttar Pradesh',1),(3,'New Delhi',1);
/*!40000 ALTER TABLE `state` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t1`
--

DROP TABLE IF EXISTS `t1`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t1` (
  `ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t1`
--

LOCK TABLES `t1` WRITE;
/*!40000 ALTER TABLE `t1` DISABLE KEYS */;
/*!40000 ALTER TABLE `t1` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(45) NOT NULL,
  `last_name` varchar(45) DEFAULT NULL,
  `account_name` varchar(45) DEFAULT NULL,
  `email` varchar(45) NOT NULL,
  `gender` tinyint(1) DEFAULT NULL,
  `verified` tinyint(1) DEFAULT NULL,
  `hash` varchar(100) DEFAULT NULL,
  `created_date` datetime NOT NULL,
  `updated_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `institute_id` int(11) NOT NULL,
  `year_of_graduation` int(11) DEFAULT '2009',
  `profile_image` varchar(100) DEFAULT NULL,
  `ver_token` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`institute_id`),
  KEY `fk_user_institute1_idx` (`institute_id`),
  CONSTRAINT `fk_user_institute1` FOREIGN KEY (`institute_id`) REFERENCES `institute` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (2,'Sunny Singh','Singh','Sunny','shakti.cse09@gmail.com',0,1,'uchdn3yB/R9heGKSSVlXNQ==','2014-12-13 00:00:00','2014-12-13 17:46:31',1,2009,NULL,NULL),(3,'Sushant','Trevedi','Susi','sushant@gmail.com',1,1,'XEcxLUCcSFKOMDZXCeh0og==','2014-12-20 00:00:00','2014-12-20 15:17:57',1,2009,'https://s3-ap-southeast-1.amazonaws.com/ofcampus/profile/3_3_profile_susi.png_xs.jpg',NULL),(4,'Shakti','Singh','Shaktsin','shak@gmail.com',0,1,'bqgZ+0ho8v2Jx6EHZzLC6A==','2015-01-02 18:33:38','2015-01-02 13:03:38',1,0,NULL,NULL),(10,'Shakti','Singh','Shakti','shakti@ofcampus.com',NULL,0,'XEcxLUCcSFKOMDZXCeh0og==','2015-02-20 02:29:02','2015-02-19 20:59:02',2,0,NULL,2248);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_has_circle`
--

DROP TABLE IF EXISTS `user_has_circle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_has_circle` (
  `user_id` int(11) NOT NULL,
  `user_institute_id` int(11) NOT NULL,
  `circle_id` int(11) NOT NULL,
  `admin` tinyint(4) NOT NULL DEFAULT '0',
  `authorized` tinyint(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`user_id`,`user_institute_id`,`circle_id`),
  KEY `fk_user_has_circle_circle1_idx` (`circle_id`),
  KEY `fk_user_has_circle_user1_idx` (`user_id`,`user_institute_id`),
  CONSTRAINT `fk_user_has_circle_circle1` FOREIGN KEY (`circle_id`) REFERENCES `circle` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_has_circle_user1` FOREIGN KEY (`user_id`, `user_institute_id`) REFERENCES `user` (`id`, `institute_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_has_circle`
--

LOCK TABLES `user_has_circle` WRITE;
/*!40000 ALTER TABLE `user_has_circle` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_has_circle` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_has_role`
--

DROP TABLE IF EXISTS `user_has_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_has_role` (
  `user_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `fk_user_has_role_role1_idx` (`role_id`),
  KEY `fk_user_has_role_user1_idx` (`user_id`),
  CONSTRAINT `fk_user_has_role_role1` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_has_role_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_has_role`
--

LOCK TABLES `user_has_role` WRITE;
/*!40000 ALTER TABLE `user_has_role` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_has_role` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-03-07 14:20:04
