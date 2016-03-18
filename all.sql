-- Database Table Creation
--
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
    units varchar2(2) NOT NULL,
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
insert into inventory values('l.00000001', '4-45 fridge', '10-JAN-2001', 1, 'plasmid 330');
insert into inventory values('l.00000002', 'liquid nitrogen tank', '1-JAN-2016', 10, 'p241 infected HEK293T cells');
insert into inventory values('l.00000003', '80-17 freezer', '4-JUL-2015', 1, 'p241 transformed bacteria stock');
insert into inventory values('l.00000004', '20-37 freezer', '29-OCT-2015', 1, 'F. nucleatum gDNA');
insert into inventory values('l.00000005', '20-45 fridge', '10-NOV-2014', 1, 'plasmid p241');
insert into inventory values('m.00000001', 'Bench 2', '1-JAN-2014', 1, 'Mini centrifuge');
insert into inventory values('m.00000002', 'Corner of Bench 2', '2-FEB-2014', 1, 'Table top shaker');
insert into inventory values('m.00000003', 'Cabinet 5', '3-MAR-2014', 1, 'power box');
insert into inventory values('m.00000004', 'FACs room', '4-APR-2014', 1, 'flow cytometer');
insert into inventory values('m.00000005', 'North wall', '1-JAN-2014', 1, 'PCR machine');
insert into inventory values('c.00000001', '4-45 fridge', '1-JAN-2016', 10, 'DMEM');
insert into inventory values('c.00000002', '4-45 fridge', '1-JAN-2016', 10, 'MMEM');
insert into inventory values('c.00000003', 'Coat rack', '12-DEC-2015', 10, 'Lab coats');
insert into inventory values('c.00000004', 'Glasses cabinet', '10-DEC-2015', 20, 'Safety glasses');
insert into inventory values('c.00000005', 'Storage shelf 2', '10-DEC-2015', 20, '10mL pipettes');
-- Adding eusesi tuples
insert into eusesi values(0001, '1-JAN-2001', 'l.00000001');
insert into eusesi values(0002, '1-JAN-2016', 'l.00000002');
insert into eusesi values(0003, '3-JUL-2015', 'l.00000003');
insert into eusesi values(0004, '28-OCT-2015', 'l.00000004');
insert into eusesi values(0001, '10-NOV-2014', 'l.00000005');
-- Adding sreviewsi tuples
insert into sreviewsi values('18-MAR-2015', 's1.eyung', 'm.00000001');
insert into sreviewsi values('25-MAY-2015', 's2.rholt', 'c.00000003');
insert into sreviewsi values('8-AUG-2015', 's3.gperona', 'm.00000004');
insert into sreviewsi values('12-DEC-2015', 's4.dacton', 'c.00000005');
insert into sreviewsi values('13-FEB-2016', 's1.eyung', 'm.00000005');
-- Adding rupdatei tuples
insert into rupdatei values('r1.jsihvon', 'c.00000002', '10-OCT-2014');
insert into rupdatei values('r2.jlam', 'c.00000005', '7-APR-2015');
insert into rupdatei values('b4.bli', 'm.00000002', '23-SEP-2015');
insert into rupdatei values('r5.akapron', 'm.00000003', '9-NOV-2015');
insert into rupdatei values('r1.jsihvon', 'c.00000004', '2-FEB-2016');
-- Adding labcreated tuples
insert into labcreated values('l.00000001', 0, 'uL', 0001, '1-JAN-2001');
insert into labcreated values('l.00000002', 1.5, 'mL', 0002, '1-JAN-2016');
insert into labcreated values('l.00000003', 40, 'uL', 0002, '03-JUL-2015');
insert into labcreated values('l.00000004', 5, 'uL', 0003, '28-OCT-2015');
insert into labcreated values('l.00000005', 10, 'uL', 0001, '10-NOV-2014');
-- Adding rcreatesi tuples
insert into rcreatesi values('r1.jsihvon', 'l.00000004', 'F.nucleatum gDNA');
insert into rcreatesi values('r2.jlam', 'l.00000001', 'plasmid 330');
insert into rcreatesi values('r2.jlam', 'l.00000005', 'plasmid p241');
insert into rcreatesi values('r4.bli', 'l.00000002', 'p241 infected HEK293T cells');
insert into rcreatesi values('r5.akapron', 'l.00000003', 'p241 transformed bacteria stock');
-- Adding machinery tuples
insert into machinery values('m.00000001', 'abcd');
insert into machinery values('m.00000002', 'efgh');
insert into machinery values('m.00000003', 'ijkl');
insert into machinery values('m.00000004', 'mnop');
insert into machinery values('m.00000005', 'qrst');
