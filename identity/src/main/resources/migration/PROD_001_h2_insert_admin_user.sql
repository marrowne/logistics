--liquibase formatted sql
--changeset author:mmordawski context:prod dbms:h2

INSERT INTO `users` (`id`,`passwordHash`, `authority`, `active`) VALUES
	('100000', '$2a$04$5zAGm9JlMLXkWRi0wnLipeFEj3ekSGY879ji1.tzoz.5mhxhSeHEm', 'ADMINISTRATOR', 1); -- password is 'password'

DROP SEQUENCE IF EXISTS `USERS_SEQUENCE`;
CREATE SEQUENCE USERS_SEQUENCE  START WITH 100001 INCREMENT BY 1;