--liquibase formatted sql
--changeset author:mmordawski context:prod dbms:mysql

INSERT INTO `users` (`passwordHash`, `authority`, `active`) VALUES
	('$2a$04$5zAGm9JlMLXkWRi0wnLipeFEj3ekSGY879ji1.tzoz.5mhxhSeHEm', 'ADMINISTRATOR', 1); -- password is 'password'