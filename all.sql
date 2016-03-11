-- Database Table Creation
--
-- First drop any existing tables.
drop table supervisor;
drop table researcher;
drop table labbook;
drop table experiment;
drop table inventory;
drop table eusesi;
-- JS: Can't remember if we should change the table below:
drop table sreviewsi;
drop table rupdatei;
drop table rcreatesi;
drop table labcreated;
drop table machinery;
drop table consumable;
drop table inspection;
drop table breakdown;
drop table productinfo;
drop table equipment;
-- Create each table.
create table supervisor(
	ssid varchar2(10) primary key,
	sname varchar2(40)
	);
create table researcher(
	rsid varchar2(10) primary key,
	rname varchar2(40)
	);
create table labbook(
	booknum int primary key,
	rsid varchar(10) NOT NULL,
	foreign key (rsid) references researcher
	);
create table experiment(
	-- JL: i dont think this one is right in the google doc the on update cascade, on delete cascade
	-- JM: I've added the other bits of the table anyway.  We'll have a look.
	book# int,
	cdate date,
	page# int,
	primary key (book#, cdate)
	);
create table inventory(
	iid varchar(10) primary key,
	iloc varchar(40),
	datec date,
	qnty integer,
	iname varchar2(40)
	);
create table eusesi(
	book# int NOT NULL,
	cdate date NOT NULL,
	iid varchar(10),
	primary key book#, date, iid,
	foreign key cdate references inventory,
	foreign key book# references labbook
	);
create table sreviewsi(
	lastchecked date,
	ssid varchar(10),
	iid varchar(10),
	primary key (ssid, iid),
	foreign key (ssid) references supervisor,
	foreign key (iid) references inventory
	);
create table rupdatei(
	rsid varchar(10),
	iid varchar(10),
	lastchecked date,
	primary key (rsid, iid),
	foreign key (rsid) references researcher,
	foreign key (iid) references inventory
	);
create table labcreated(
	iid varchar(10) primary key,
	amnt double,
	booknum integer,
	datelc date,
	foreign key (iid) references inventory,
	foreign key (booknum) references labbook,
	foreign key (datelc) references experiment
	);
-- researcher only creates new labcreated tuples.
-- Does references labcreated make sense? Can we cascade up to inventory to update that table?
-- Or is there some other way we should inforce this restriction?
	create table rcreatesi(
	rsid varchar(10),
	iid varchar(10),
	iname varchar2(40),
	-- does iname really need to be a part of the key?
	primary key (rsid, iid, iname),
	foreign key (rsid) references researcher,
	foreign key (iid, iname) references labcreated
	);
create table machinery(
	iid varchar(10) primary key,
	serialnum varchar(10),
	foreign key (iid) references equipment
	);
create table consumable(
	iid varchar(10) primary key,
	amnt float,
	foreign key (iid) references equipment
	);
create table inspection(
	datec date,
	iid varchar(10),
	primary key(datec, iid),
	foreign key (iid) references machinery
	);
create table breakdown(
	iid varchar(10),
	datec date,
	description varchar(40),
	primary key(iid, datec),
	foreign key (iid) references machinery
	);
create table productinfo(
	supplier varchar2(40),
	ordernum varchar(10),
	primary key (supplier, ordernum)
	);
create table equipment(
	iid varchar(10) primary key,
	supplier varchar2(40),
	ordernum varchar(10),
	foreign key (inventory) references inventory,
	foreign key (ordernum) references productinfo
	);
