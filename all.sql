-- Database Table Creation
--
drop database if exists Lab;
create database Lab;

use Lab;
-- First drop any existing tables.
drop table supervisor cascade constraints;
drop table researcher cascade constraints;
drop table labbook cascade constraints;
drop table experiment cascade constraints;
drop table inventory cascade constraints;
drop table eusesi cascade constraints;
-- JS: Can't remember if we should change the table below:
drop table sreviewsi cascade constraints;
drop table rupdatei cascade constraints;
drop table rcreatesi cascade constraints;
drop table labcreated cascade constraints;
drop table machinery cascade constraints;
drop table consumable cascade constraints;
drop table inspection cascade constraints;
drop table breakdown cascade constraints;
drop table productinfo cascade constraints;
drop table equipment cascade constraints;
-- Create each table.
create table supervisor(
	ssid varchar2(10) NOT NULL primary key,
	sname varchar2(40),
	spwd varchar2(40)
	);
create table researcher(
	rsid varchar2(10) NOT NULL primary key,
	rname varchar2(40),
	rpwd varchar2(40)
	);
create table labbook(
	booknum int NOT NULL primary key,
	rsid varchar2(10) NOT NULL,
	foreign key (rsid) references researcher(rsid)
	);
create table experiment(
	-- JL: i dont think this one is right in the google doc the on update cascade, on delete cascade
	-- JM: I've added the other bits of the table anyway.  We'll have a look.
	booknum int,
	cdate date,
	-- JS: We should probs specify that pagenum can't be negative
	pagenum int,
	primary key (booknum, cdate),
	foreign key (booknum) references labbook(booknum) on delete cascade
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
	primary key (booknum, cdate, iid),
	foreign key (iid) references inventory(iid),
	foreign key (booknum, cdate) references experiment(booknum, cdate)
	);
create table sreviewsi(
	lastchecked date NOT NULL,
	ssid varchar2(10) NOT NULL,
	iid varchar2(10) NOT NULL,
	primary key (ssid, iid),
	foreign key (ssid) references supervisor(ssid),
	foreign key (iid) references inventory(iid)
	);
create table rupdatei(
	rsid varchar2(10) NOT NULL,
	iid varchar2(10) NOT NULL,
	lastchecked date NOT NULL,
	primary key (rsid, iid),
	foreign key (rsid) references researcher(rsid),
	foreign key (iid) references inventory(iid)
	);
create table labcreated(
	iid varchar2(10) primary key,
	-- JS: Maybe amt should be a char, so as not to restrict units (aka, only stored in mL)?
	amnt float NOT NULL,
    units varchar2(2) NOT NULL,
	booknum integer NOT NULL,
	datelc date NOT NULL,
	foreign key (iid) references inventory(iid),
	foreign key (datelc, booknum) references experiment(cdate, booknum)
	);
-- researcher only creates new labcreated tuples.
-- Does references labcreated make sense? Can we cascade up to inventory to update that table?
-- Or is there some other way we should inforce this restriction?
create table rcreatesi(
	rsid varchar2(10) NOT NULL,
	iid varchar2(10) NOT NULL,
	primary key (rsid, iid),
	foreign key (rsid) references researcher(rsid),
	foreign key (iid) references labcreated(iid)
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
	foreign key (iid) references inventory(iid),
	foreign key (supplier, ordernum) references productinfo(supplier,ordernum)
	);
create table machinery(
	iid varchar2(10) primary key,
	serialnum varchar2(10),
	foreign key (iid) references equipment(iid)
	);
create table consumable(
	iid varchar2(10) primary key,
	amnt float NOT NULL,
	foreign key (iid) references equipment(iid)
	);
create table inspection(
	datec date NOT NULL,
	iid varchar2(10) NOT NULL,
	primary key(datec, iid),
	foreign key (iid) references machinery(iid)
	);
create table breakdown(
	iid varchar2(10) NOT NULL,
	datec date NOT NULL,
	description varchar2(40) NOT NULL,
	primary key(iid, datec),
	foreign key (iid) references machinery(iid)
	);


	
-- Adding supervisor tuples
insert into supervisor values('s1.eyung', 'Eric Yung', '1234');
insert into supervisor values('s2.rholt', 'Rob Holt', 'abcd');
insert into supervisor values('s3.gperona', 'Georgia Perona', '1234');
insert into supervisor values('s4.dacton', 'Donald Acton', '5678');
insert into supervisor values('s5.rgatema', 'Robert Gateman', 'asdf');
-- Adding researcher tuples
insert into researcher values('r1.jsihvon', 'Jelena Sihvonen', 'qwerty');
insert into researcher values('r2.jlam', 'Jeanie Lam', 'asdfgh');
insert into researcher values('r3.jmyles', 'Joey Myles', 'zxcvb');
insert into researcher values('r4.bli', 'Benson Li' 'poiuy');
insert into researcher values('r5.akapron', 'Anna Kapron-King', 'lkjh');
-- Adding labbook tuples
insert into labbook values(1, 'r1.jsihvon');
insert into labbook values(2, 'r2.jlam');
insert into labbook values(3, 'r3.jmyles');
insert into labbook values(4, 'r4.bli');
insert into labbook values(5, 'r1.jsihvon');
-- Adding experiment tuples
insert into experiment values(1, '2001-01-01', 1);
insert into experiment values(2, '2016-01-01', 307);
insert into experiment values(2, '2015-07-03', 210);
insert into experiment values(3, '2015-10-28', 313);
insert into experiment values(1, '2014-11-10', 221);

-- Adding inventory tuples
insert into inventory values('l.00000001', '4-45 fridge', '2001-01-10', 1, 'plasmid 330');
insert into inventory values('l.00000002', 'liquid nitrogen tank', '2016-01-01', 10, 'p241 infected HEK293T cells');
insert into inventory values('l.00000003', '80-17 freezer', '2015-07-04', 1, 'p241 transformed bacteria stock');
insert into inventory values('l.00000004', '20-37 freezer', '2015-10-29', 1, 'F. nucleatum gDNA');
insert into inventory values('l.00000005', '20-45 fridge', '2014-11-10', 1, 'plasmid p241');
insert into inventory values('m.00000001', 'Bench 2', '2014-01-01', 1, 'Mini centrifuge');
insert into inventory values('m.00000002', 'Corner of Bench 2', '2014-02-02', 1, 'Table top shaker');
insert into inventory values('m.00000003', 'Cabinet 5', '2014-03-03', 1, 'power box');
insert into inventory values('m.00000004', 'FACs room', '2014-04-04', 1, 'flow cytometer');
insert into inventory values('m.00000005', 'North wall', '2014-01-01', 1, 'PCR machine');
insert into inventory values('c.00000001', '4-45 fridge', '2016-01-01', 10, 'DMEM');
insert into inventory values('c.00000002', '4-45 fridge', '2016-01-01', 10, 'MMEM');
insert into inventory values('c.00000003', 'Coat rack', '2015-12-12', 10, 'Lab coats');
insert into inventory values('c.00000004', 'Glasses cabinet', '2015-12-10', 20, 'Safety glasses');
insert into inventory values('c.00000005', 'Storage shelf 2', '2015-12-10', 20, '10mL pipettes');
-- Adding eusesi tuples
insert into eusesi values(0001, '2001-01-01', 'l.00000001');
insert into eusesi values(0002, '2016-01-01', 'l.00000002');
insert into eusesi values(0002, '2015-07-03', 'l.00000003');
insert into eusesi values(0003, '2015-10-28', 'l.00000004');
insert into eusesi values(0001, '2014-11-10', 'l.00000005');
-- Adding sreviewsi tuples
insert into sreviewsi values('2015-03-18', 's1.eyung', 'm.00000001');
insert into sreviewsi values('2015-05-25', 's2.rholt', 'c.00000003');
insert into sreviewsi values('2015-08-08', 's3.gperona', 'm.00000004');
insert into sreviewsi values('2015-12-12', 's4.dacton', 'c.00000005');
insert into sreviewsi values('2016-02-13', 's1.eyung', 'm.00000005');
-- Adding rupdatei tuples
insert into rupdatei values('r1.jsihvon', 'c.00000002', '2014-10-10');
insert into rupdatei values('r2.jlam', 'c.00000005', '2015-04-07');
insert into rupdatei values('r4.bli', 'm.00000002', '2015-09-23');
insert into rupdatei values('r5.akapron', 'm.00000003', '2015-11-09');
insert into rupdatei values('r1.jsihvon', 'c.00000004', '2016-02-02');
-- Adding labcreated tuples
insert into labcreated values('l.00000001', 0, 'uL', 0001, '2001-01-01');
insert into labcreated values('l.00000002', 1.5, 'mL', 0002, '2016-01-01');
insert into labcreated values('l.00000003', 40, 'uL', 0002, '2015-07-03');
insert into labcreated values('l.00000004', 5, 'uL', 0003, '2015-10-28');
insert into labcreated values('l.00000005', 10, 'uL', 0001, '2014-11-10');
-- Adding rcreatesi tuples
insert into rcreatesi values('r1.jsihvon', 'l.00000004');
insert into rcreatesi values('r2.jlam', 'l.00000001');
insert into rcreatesi values('r2.jlam', 'l.00000005');
insert into rcreatesi values('r4.bli', 'l.00000002');
insert into rcreatesi values('r5.akapron', 'l.00000003');
-- Adding product info tuples
insert into productinfo values('Invitrogen', 88947);
insert into productinfo values('Invitrogen', 32439);
insert into productinfo values('TxMedical', 28263);
insert into productinfo values('TxMedical', 29768);
insert into productinfo values('ThermoScientific', 01924);
-- Adding equipment tuples
insert into equipment values('m.00000001', 'Invitrogen', 88947);
insert into equipment values('m.00000003', 'Invitrogen', 32439);
insert into equipment values('c.00000001', 'TxMedical', 28263);
insert into equipment values('c.00000002', 'TxMedical', 29768);
insert into equipment values('c.00000005', 'ThermoScientific', 01924);
-- Adding machinery tuples
insert into machinery values('m.00000001', 'abcd');
insert into machinery values('c.00000002', 'efgh');
insert into machinery values('c.00000001', 'mnop');
insert into machinery values('m.00000003', 'ijkl');
insert into machinery values('c.00000005', 'qrst');
-- Adding consumable tuples
insert into consumable values('m.00000001', 300);
insert into consumable values('c.00000002', 300);
insert into consumable values('m.00000003', 100);
insert into consumable values('c.00000005', 25);
insert into consumable values('c.00000001', 90);
-- Adding inspection tuples
insert into inspection values('2015-01-01', 'm.00000001');
insert into inspection values('2015-02-02', 'm.00000003');
insert into inspection values('2015-03-03', 'c.00000001');
insert into inspection values('2015-04-04', 'c.00000002');
insert into inspection values('2016-01-01', 'c.00000005');
-- Adding breakdown tuples
insert into breakdown values('m.00000001', '2015-03-08', 'explosion');
insert into breakdown values('m.00000001', '2015-11-11', '2nd explosion');
insert into breakdown values('m.00000003', '2015-12-12', 'exploded due to m1');
insert into breakdown values('m.00000003', '2015-12-25', 'Christmas miracle disables laser');
insert into breakdown values('m.00000003', '2016-01-01', 'button stopped working');
