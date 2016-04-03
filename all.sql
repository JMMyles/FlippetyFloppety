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
drop table rupdatei cascade constraints;
drop table rcreatesi cascade constraints;
drop table labcreated cascade constraints;
drop table machinery cascade constraints;
drop table consumable cascade constraints;
drop table inspection cascade constraints;
drop table breakdown cascade constraints;
drop table productinfo cascade constraints;
drop table equipment cascade constraints;
drop table rinspectm cascade constraints;
drop sequence inc_iid;

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
	ename varchar2(40),
	primary key (booknum, cdate),
	foreign key (booknum) references labbook(booknum)
	);
create table inventory(
	iid int NOT NULL primary key,
	iloc varchar2(40) NOT NULL,
	datec date NOT NULL,
	qnty float NOT NULL,
	iname varchar2(40) NOT NULL
	);
create table eusesi(
	booknum int NOT NULL,
	cdate date NOT NULL,
	iid int,
	qtyUsed float,
	primary key (booknum, cdate, iid),
	foreign key (iid) references inventory(iid),
	foreign key (booknum, cdate) references experiment(booknum, cdate)
	);

create table rupdatei(
	rsid varchar2(10) NOT NULL,
	iid int NOT NULL,
	lastchecked date NOT NULL,
	primary key (rsid, iid),
	foreign key (rsid) references researcher(rsid),
	foreign key (iid) references inventory(iid) on delete cascade
	);
create table labcreated(
	iid int primary key,
	-- JS: Maybe amt should be a char, so as not to restrict units (aka, only stored in mL)?
	amnt float NOT NULL,
  units varchar2(2) NOT NULL,
	booknum integer NOT NULL,
	datelc date NOT NULL,
	foreign key (iid) references inventory(iid) on delete cascade,
	foreign key (datelc, booknum) references experiment(cdate, booknum)
	);
-- researcher only creates new labcreated tuples.
-- Does references labcreated make sense? Can we cascade up to inventory to update that table?
-- Or is there some other way we should inforce this restriction?
create table rcreatesi(
	rsid varchar2(10) NOT NULL,
	iid int NOT NULL,
	primary key (rsid, iid),
	foreign key (rsid) references researcher(rsid),
	foreign key (iid) references labcreated(iid) on delete cascade
	);
create table productinfo(
	supplier varchar2(40) NOT NULL,
	ordernum varchar2(10) NOT NULL,
	primary key (supplier, ordernum)
	);
create table equipment(
	iid int primary key,
	supplier varchar2(40) NOT NULL,
	ordernum varchar2(10) NOT NULL,
	foreign key (iid) references inventory(iid),
	foreign key (supplier, ordernum) references productinfo(supplier,ordernum) on delete cascade
	);
create table machinery(
	iid int primary key,
	serialnum varchar2(10),
	foreign key (iid) references equipment(iid) on delete cascade
	);
create table consumable(
	iid int primary key,
	amnt float NOT NULL,
	foreign key (iid) references equipment(iid) on delete cascade,
	constraint nonNegAmnt check (amnt >=0)
	);
create table inspection(
	dateInspected date NOT NULL,
	iid int NOT NULL,
	primary key(dateInspected, iid),
	foreign key (iid) references machinery(iid) on delete cascade
	);
create table rinspectm(
	dateInspected date NOT NULL,
	ssid varchar2(10) NOT NULL,
	iid int NOT NULL,
	primary key (ssid, dateInspected, iid),
	foreign key (dateInspected, iid) references inspection(dateInspected, iid) on delete cascade,
	foreign key (ssid) references supervisor(ssid)
	);
create table breakdown(
	iid int NOT NULL,
	breakdownDate date NOT NULL,
	description varchar2(40) NOT NULL,
	primary key(iid, breakdownDate),
	foreign key (iid) references machinery(iid) on delete cascade
	);

-- Create a sequence to do auto incrementing of inventory iid's
create sequence inc_iid start with 1 increment by 1;

	
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
insert into researcher values('r4.bli', 'Benson Li', 'poiuy');
insert into researcher values('r5.akapron', 'Anna Kapron-King', 'lkjh');
-- Adding labbook tuples
insert into labbook values(1, 'r1.jsihvon');
insert into labbook values(2, 'r2.jlam');
insert into labbook values(3, 'r3.jmyles');
insert into labbook values(4, 'r4.bli');
insert into labbook values(5, 'r1.jsihvon');
-- Adding experiment tuples
insert into experiment values(1, '2001-01-01', 1, 'Detecting aliens in outer space');
insert into experiment values(2, '2016-01-01', 307, 'Counting worms');
insert into experiment values(2, '2015-07-03', 210, 'Do worms have legs?');
insert into experiment values(3, '2015-10-28', 313, 'Analysis of a worm');
insert into experiment values(1, '2014-11-10', 221, 'e123');

-- Adding inventory tuples
insert into inventory values(inc_iid.nextval, '4-45 fridge', '2001-01-10', 1, 'plasmid 330');
insert into inventory values(inc_iid.nextval, 'liquid nitrogen tank', '2016-01-01', 10, 'p241 infected HEK293T cells');
insert into inventory values(inc_iid.nextval, '80-17 freezer', '2015-07-04', 1, 'p241 transformed bacteria stock');
insert into inventory values(inc_iid.nextval, '20-37 freezer', '2015-10-29', 1, 'F. nucleatum gDNA');
insert into inventory values(inc_iid.nextval, '20-45 fridge', '2014-11-10', 1, 'plasmid p241');
insert into inventory values(inc_iid.nextval, 'Bench 2', '2014-01-01', 1, 'Mini centrifuge');
insert into inventory values(inc_iid.nextval, 'Corner of Bench 2', '2014-02-02', 1, 'Table top shaker');
insert into inventory values(inc_iid.nextval, 'Cabinet 5', '2014-03-03', 1, 'power box');
insert into inventory values(inc_iid.nextval, 'FACs room', '2014-04-04', 1, 'flow cytometer');
insert into inventory values(inc_iid.nextval, 'North wall', '2014-01-01', 1, 'PCR machine');
insert into inventory values(inc_iid.nextval, '4-45 fridge', '2016-01-01', 10, 'DMEM');
insert into inventory values(inc_iid.nextval, '4-45 fridge', '2016-01-01', 10, 'MMEM');
insert into inventory values(inc_iid.nextval, 'Coat rack', '2015-12-12', 10, 'Lab coats');
insert into inventory values(inc_iid.nextval, 'Glasses cabinet', '2015-12-10', 20, 'Safety glasses');
insert into inventory values(inc_iid.nextval, 'Storage shelf 2', '2015-12-10', 20, '10mL pipettes');
-- Adding eusesi tuples
insert into eusesi values(2, '2016-01-01', 2, 1);
insert into eusesi values(2, '2015-07-03', 3, 0.2);
insert into eusesi values(3, '2015-10-28', 4, 0.2);
insert into eusesi values(1, '2014-11-10', 5, 0.02);
-- Adding rupdatei tuples
insert into rupdatei values('r1.jsihvon', 12, '2014-10-10');
insert into rupdatei values('r2.jlam', 15, '2015-04-07');
insert into rupdatei values('r4.bli', 7, '2015-09-23');
insert into rupdatei values('r5.akapron', 8, '2015-11-09');
insert into rupdatei values('r1.jsihvon', 14, '2016-02-02');
-- Adding labcreated tuples
insert into labcreated values(2, 1.5, 'mL', 2, '2016-01-01');
insert into labcreated values(3, 40, 'uL', 2, '2015-07-03');
insert into labcreated values(4, 5, 'uL', 3, '2015-10-28');
insert into labcreated values(5, 10, 'uL', 1, '2014-11-10');
-- Adding rcreatesi tuples
insert into rcreatesi values('r1.jsihvon', 4);
insert into rcreatesi values('r2.jlam', 5);
insert into rcreatesi values('r4.bli', 2);
insert into rcreatesi values('r5.akapron', 3);
-- Adding product info tuples
insert into productinfo values('Invitrogen', 88947);
insert into productinfo values('Invitrogen', 32439);
insert into productinfo values('Invitrogen', 55421);
insert into productinfo values('Invitrogen', 44521);
insert into productinfo values('HappyDays', 71328);
insert into productinfo values('FallOut', 80085);
insert into productinfo values('TxMedical', 28263);
insert into productinfo values('TxMedical', 29768);
insert into productinfo values('CompanyInc', 90981);
insert into productinfo values('CompanyInc', 00819);
insert into productinfo values('ThermoScientific', 01924);
-- Adding equipment tuples
insert into equipment values(6, 'Invitrogen', 88947);
insert into equipment values(7, 'Invitrogen', 44521);
insert into equipment values(8, 'Invitrogen', 32439);
insert into equipment values(9, 'HappyDays', 71328);
insert into equipment values(10, 'FallOut', 80085);
insert into equipment values(11, 'TxMedical', 28263);
insert into equipment values(12, 'TxMedical', 29768);
insert into equipment values(13, 'CompanyInc', 90981);
insert into equipment values(14, 'CompanyInc', 00819);
insert into equipment values(15, 'ThermoScientific', 01924);
-- Adding machinery tuples
insert into machinery values(6, 'abcd');
insert into machinery values(7, 'efgh');
insert into machinery values(8, 'mnop');
insert into machinery values(9, 'ijkl');
insert into machinery values(10, 'qrst');
-- Adding consumable tuples
insert into consumable values(11, 300);
insert into consumable values(12, 300);
insert into consumable values(13, 100);
insert into consumable values(14, 25);
insert into consumable values(15, 90);
-- Adding inspection tuples
insert into inspection values('2015-01-01', 6);
insert into inspection values('2015-02-02', 8);
insert into inspection values('2015-03-03', 7);
insert into inspection values('2015-04-04', 7);
insert into inspection values('2016-01-02', 10);
insert into inspection values('2016-01-06', 7);
insert into inspection values('2012-01-01', 9);
insert into inspection values('2016-01-01', 10);
-- Adding rinspectm tuples
insert into rinspectm values('2015-02-02', 's1.eyung', 8);
insert into rinspectm values('2015-03-03', 's1.eyung', 7);
insert into rinspectm values('2015-01-01', 's1.eyung', 6);
insert into rinspectm values('2012-01-01', 's1.eyung', 9);
insert into rinspectm values('2016-01-02', 's1.eyung', 10);
insert into rinspectm values('2015-04-04', 's2.rholt', 7);
insert into rinspectm values('2016-01-01', 's3.gperona', 10);
insert into rinspectm values('2016-01-06', 's3.gperona', 7);
-- Adding breakdown tuples
insert into breakdown values(6, '2015-03-08', 'explosion');
insert into breakdown values(6, '2015-11-11', '2nd explosion');
insert into breakdown values(8, '2015-12-12', 'exploded due to m1');
insert into breakdown values(8, '2015-12-25', 'Christmas miracle disables laser');
insert into breakdown values(8, '2016-01-01', 'button stopped working');
