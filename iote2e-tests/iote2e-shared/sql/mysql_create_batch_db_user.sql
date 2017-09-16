DROP USER 'iote2e_batch'@'%';
DROP DATABASE IF EXISTS db_iote2e_batch;
CREATE DATABASE db_iote2e_batch;
CREATE USER 'iote2e_batch'@'%' IDENTIFIED BY 'Password*8';
GRANT ALL ON db_iote2e_batch.* TO 'iote2e_batch'@'%';
FLUSH PRIVILEGES;