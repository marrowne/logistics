--liquibase formatted sql
--changeset author:mmordawski dbms:mysql

CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `passwordHash` VARCHAR(60) NOT NULL,
  `authority` VARCHAR(60) NOT NULL,
  `token` VARCHAR(65) NULL,
  `active` BOOL NOT NULL DEFAULT 0,
  `deleted` BOOL NOT NULL DEFAULT 0,
   PRIMARY KEY (id)
) AUTO_INCREMENT=100000;