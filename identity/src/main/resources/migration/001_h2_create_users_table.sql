--liquibase formatted sql
--changeset author:mmordawski dbms:h2

CREATE TABLE `users` (
  `id` VARCHAR(254) NOT NULL PRIMARY KEY,
  `passwordHash` VARCHAR(60) NOT NULL,
  `authority` VARCHAR(60) NOT NULL,
  `token` VARCHAR(65) NULL,
  `active` BOOL NOT NULL DEFAULT 0,
  `deleted` BOOL NOT NULL DEFAULT 0
);