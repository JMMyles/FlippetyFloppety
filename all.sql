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
	ssid varchar2(10) NOT NULL primary key,
	sname varchar2(40)
	);
create table researcher(
	rsid varchar2(10) NOT NULL primary key,
	rname varchar2(40)
	);
create table labbook(
	booknum int NOT NULL primary key,
	rsid varchar2(10) NOT NULL,
	foreign key (rsid) references researcher
	);
create table experiment(
	-- JL: i dont think this one is right in the google doc the on update cascade, on delete cascade
	-- JM: I've added the other bits of the table anyway.  We'll have a look.
	booknum int,
	cdate date,
	pagenum int,
	primary key (booknum, cdate),
	foreign key (booknum) references labbook on delete cascade
	);
create table inventory(
	iid varchar2(10) NOT NULL primary key,
	iloc varchar2(40) NOT NULL,
	datec date NOT NULL,
	qnty integer NOT NULL,
	iname varchar2(40) NOT NULL
	);
create table eusesi(
	booknum int NOT NULL,
	cdate date NOT NULL,
	iid varchar2(10),
	primary key booknum, date, iid,
	foreign key cdate references inventory,
	foreign key booknum references labbook
	);
create table sreviewsi(
	lastchecked date NOT NULL,
	ssid varchar2(10) NOT NULL,
	iid varchar2(10) NOT NULL,
	primary key (ssid, iid),
	foreign key (ssid) references supervisor,
	foreign key (iid) references inventory
	);
create table rupdatei(
	rsid varchar2(10) NOT NULL,
	iid varchar2(10) NOT NULL,
	lastchecked date NOT NULL,
	primary key (rsid, iid),
	foreign key (rsid) references researcher,
	foreign key (iid) references inventory
	);
create table labcreated(
	iid varchar2(10) primary key,
	amnt double NOT NULL,
	booknum integer NOT NULL,
	datelc date NOT NULL,
	foreign key (iid) references inventory,
	foreign key (booknum) references labbook,
	foreign key (datelc) references experiment
	);
-- researcher only creates new labcreated tuples.
-- Does references labcreated make sense? Can we cascade up to inventory to update that table?
-- Or is there some other way we should inforce this restriction?
	create table rcreatesi(
	rsid varchar2(10) NOT NULL,
	iid varchar2(10) NOT NULL,
	primary key (rsid, iid, iname),
	foreign key (rsid) references researcher,
	foreign key (iid) references labcreated
	);
create table machinery(
	iid varchar2(10) primary key,
	serialnum varchar2(10),
	foreign key (iid) references equipment
	);
create table consumable(
	iid varchar2(10) primary key,
	amnt float NOT NULL,
	foreign key (iid) references equipment
	);
create table inspection(
	datec date NOT NULL,
	iid varchar2(10) NOT NULL,
	primary key(datec, iid),
	foreign key (iid) references machinery
	);
create table breakdown(
	iid varchar2(10) NOT NULL,
	datec date NOT NULL,
	description varchar2(40) NOT NULL,
	primary key(iid, datec),
	foreign key (iid) references machinery
	);
create table productinfo(
	supplier varchar2(40) NOT NULL,
	ordernum varchar2(10) NOT NULL,
	primary key (supplier, ordernum)
	);
create table equipment(
	iid varchar2(10) primary key,
	supplier varchar2(40) NOT NULL,
	ordernum varchar2(10) NOT NULL,
	foreign key (inventory) references inventory,
	foreign key (ordernum, supplier) references productinfo,
	);
