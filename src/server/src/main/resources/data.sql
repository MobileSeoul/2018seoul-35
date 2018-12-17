--蜡历积己
create user 'sspruser'@'%' identified by '0000';

--蜡历 鼻茄
GRANT ALL PRIVILEGES ON sspr.* TO 'sspruser'@'%';

--CREATE sspr Database
CREATE DATABASE sspr DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

--CREATE restroom TABLE
create table tbl_restroom(
id VARCHAR(50) NOT NULL,
state VARCHAR(50) NOT NULL,
msg VARCHAR(50),
updatedate date,
PRIMARY KEY(id)
);

--CREATE report TABLE
create table tbl_report(
report_id INTEGER NOT NULL auto_increment,
restroom_id VARCHAR(50) NOT NULL,
writer VARCHAR(50) NOT NULL,
pwd VARCHAR(50) NOT NULL,
msg VARCHAR(50),
img VARCHAR(200),
updatedate date,
PRIMARY KEY(report_id),
foreign key(restroom_id) references tbl_restroom(id)
);
