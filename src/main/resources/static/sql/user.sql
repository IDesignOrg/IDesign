-- --------------------------------------------------------
-- 호스트:                          127.0.0.1
-- 서버 버전:                        10.10.7-MariaDB - mariadb.org binary distribution
-- 서버 OS:                        Win64
-- HeidiSQL 버전:                  12.3.0.6589
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- idesign 데이터베이스 구조 내보내기
CREATE DATABASE IF NOT EXISTS `idesign` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */;
USE `idesign`;

-- 테이블 idesign.user 구조 내보내기
CREATE TABLE IF NOT EXISTS `user` (
  `u_no` bigint(20) NOT NULL AUTO_INCREMENT,
  `u_birth` varchar(255) NOT NULL,
  `u_id` varchar(255) NOT NULL,
  `u_mail` varchar(255) NOT NULL,
  `u_name` varchar(255) NOT NULL,
  `u_pw` varchar(255) NOT NULL,
  `u_register` date NOT NULL,
  `u_tel` varchar(255) NOT NULL,
  PRIMARY KEY (`u_no`),
  UNIQUE KEY `UK_eltbn9tkept140ug95ymg61ad` (`u_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 테이블 데이터 idesign.user:~2 rows (대략적) 내보내기
INSERT INTO `user` (`u_no`, `u_birth`, `u_id`, `u_mail`, `u_name`, `u_pw`, `u_register`, `u_tel`) VALUES
	(1, '1997-02-15', 'hmw', 'dnlsemtmf@gmail.com', '한민욱', '$2a$10$wLs8N.o/E57yPjh74BMOoOxYyJ7QZyCediJYihbSQeHIE8qrPQLMO', '2024-06-12', '010-3739-7667'),
	(5, '2024-06-01', 'admin', 'dnlsemtmf@gmail.com', '관리자', '$2a$10$w3vWmq3S/CRButsu88yIse/FPZ4eR6x6eD/x0IlgaAwt8C40a3ENO', '2024-06-19', '010-3739-7667');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
