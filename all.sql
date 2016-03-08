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
	);
create table experiment(
	);
create table inventory(
	iid varchar(10) primary key,
	iloc varchar(40),
	datec date,
	qnty integer,
	iname varchar2(40)
	);
create table eusesi(
	);
create table sreviewsi(
	);
create table rupdatei(
	);
create table rcreatesi(
	);
create table labcreated(
	iid varchar(10) primary key,
	amnt double,
	booknum integer,
	datelc date,
	foreign key (booknum) references labbook,
	foreign key (datelc) references experiment
	);
create table machinery(
	);
create table consumable(
	);
create table inspection(
	);
create table breakdown(
	);
create table productinfo(
	);
create table equipment(
	);