create database gab2;
show tables;
use gab2;
drop table user;

create table user (
id bigint primary key auto_increment,
full_name varchar(250) not null,
address varchar(250) not null,
phone_number varchar(250) not null,
mail_id varchar(250) not null,
image longblob,
date_updated datetime
);


insert into user(full_name, address, phone_number, mail_id, image, date_updated) values(
"Shahin Shifana",
"G.R. Nagar",
"8148204215, 8124937079",
"shahinshifana@gmail.com",
"",
now()
);

select * from user;

delete from user where id = 9;

desc user;