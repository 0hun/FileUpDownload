create table pds_item (
	pds_item_id int not null auto_increment,
	filename varchar(200) not null,
	realpath varchar(200) not null,
	filesize int,
	downloadcount int default 0,
	description varchar(255),
	primary key (pds_item_id)
);
