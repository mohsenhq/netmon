drop database netmon_app;
drop USER 'netmon'@'localhost';
create database netmon_app;
CREATE USER 'netmon'@'localhost' IDENTIFIED BY 'netmonpass';
GRANT ALL PRIVILEGES ON netmon_app.* TO 'netmon'@'localhost';
FLUSH PRIVILEGES;

