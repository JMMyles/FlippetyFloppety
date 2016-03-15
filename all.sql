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
	-- JS: We should probs specify that pagenum can't be negative
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
	-- JS: Maybe amt should be a char, so as not to restrict units (aka, only stored in mL)?
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
	
-- Adding supervisor tuples
insert into supervisor values('s1.eyung', 'Eric Yung');
insert into supervisor values('s2.rholt', 'Rob Holt');
insert into supervisor values('s3.gperona', 'Georgia Perona');
insert into supervisor values('s4.dacton', 'Donald Acton');
insert into supervisor values('s5.rgatema', 'Robert Gateman');
-- Adding researcher tuples
insert into researcher values('r1.jsihvon', 'Jelena Sihvonen');
insert into researcher values('r2.jlam', 'Jeanie Lam');
insert into researcher values('r3.jmyles', 'Joey Myles');
insert into researcher values('r4.bli', 'Benson Li');
insert into researcher values('r5.akapron', 'Anna Kapron-King');
-- Adding labbook tuples
insert into labbook values(1, 'r1.jsihvon');
insert into labbook values(2, 'r2.jlam');
insert into labbook values(3, 'r3.jmyles');
insert into labbook values(4, 'r4.bli');
insert into labbook values(5, 'r1.jsihvon');
-- Adding experiment tuples
insert into experiment values(1, '1-JAN-2001', 1);
insert into experiment values(2, '1-JAN-2016', 307);
insert into experiment values(2, '3-JUL-2015', 210);
insert into experiment values(3, '28-OCT-2015', 313);
insert into experiment values(1, '10-NOV-2014', 221);
-- Adding inventory tuples
insert into inventory values('m.00000001', 'Bench 2', '1-JAN-2014', 1, 'Mini centrifuge');
insert into inventory values('m.00000002', 'Corner of Bench 2', '2-FEB-2014', 1, 'Table top shaker');
-- Adding eusesi tuples
-- Adding sreviewsi tuples
-- Adding rupdatei tuples
-- Adding labcreated tuples
-- Adding rcreatesi tuples
-- Adding machinery tuples
insert into machinery values('m.00000001', 'abcd');
insert into machinery values('m.00000002', 'efgh');
insert into machinery values('m.00000003', 'ijkl');
insert into machinery values('m.00000004', 'mnop');
insert into machinery values('m.00000005', 'qrst');