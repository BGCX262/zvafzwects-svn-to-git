---------------------------------
-- Insert Recipies v0.1
-- Jochen Pätzold, Team lokalhorst
---------------------------------


--
-- Anlegen von ein paar Zutaten
--

insert into INGREDIENT values (ingr_id_seq.nextval, 'Zucker', 1);
insert into INGREDIENT values (ingr_id_seq.nextval, 'Salz', 2);
insert into INGREDIENT values (ingr_id_seq.nextval, 'Wasser', 3);
insert into INGREDIENT values (ingr_id_seq.nextval, 'Hefe', 4);
insert into INGREDIENT values (ingr_id_seq.nextval, 'Mehl', 1);

--
-- Anlegen von 3 Rezepten
--

insert into RECIPE values (
	recp_id_seq.nextval,			-- Rezept ID
	'Ungarischer Gulasch',			-- Name
	'aller leckerster Gulasch den man sich nur w�nschen kann',			-- Beschreibung
	'2010-01-01',			-- Datum angelegt
	'2010-02-02',			-- Datum ver�ffentlicht
	'2010-03-03',			-- Datum last edit
	50,			-- Zubereitungszeit
	true,		-- ver�ffentlicht
	1,			-- Schwierigkeitsgrad ID
	1,			-- Kategorie ID
	1			-- Cook ID
);

insert into PREPARATION values (1, recp_id_seq.currval, '1. Schritt: man nehme...');
insert into PREPARATION values (2, recp_id_seq.currval, '2. Schritt: und dann...');
insert into PREPARATION values (3, recp_id_seq.currval, '3. Schritt: sowie...');

insert into USES values (recp_id_seq.currval, 3, 100);
insert into USES values (recp_id_seq.currval, 4, 60);
insert into USES values (recp_id_seq.currval, 5, 300);

insert into RECIPE values (
	recp_id_seq.nextval,			-- Rezept ID
	'Chinesischer Salat',			-- Name
	'endlich einaml ein Salat f�r Vegetarier',			-- Beschreibung
	'2010-01-01',			-- Datum angelegt
	'2010-02-02',			-- Datum ver�ffentlicht
	'2010-03-03',			-- Datum last edit
	20,			-- Zubereitungszeit
	true,		-- ver�ffentlicht
	2,			-- Schwierigkeitsgrad ID
	2,			-- Kategorie ID
	2			-- Cook ID
);

insert into PREPARATION values (1, recp_id_seq.currval, '1. Schritt: man nehme...');
insert into PREPARATION values (2, recp_id_seq.currval, '2. Schritt: und dann...');
insert into PREPARATION values (3, recp_id_seq.currval, '3. Schritt: sowie...');

insert into USES values (recp_id_seq.currval, 1, 10);
insert into USES values (recp_id_seq.currval, 2, 20);
insert into USES values (recp_id_seq.currval, 3, 30);


insert into RECIPE values (
	recp_id_seq.nextval,			-- Rezept ID
	'Leberwurststullen',			-- Name
	'schnell und zwischendurch',			-- Beschreibung
	'2010-01-01',			-- Datum angelegt
	'2010-02-02',			-- Datum veröffentlicht
	'2010-03-03',			-- Datum last edit
	5,			-- Zubereitungszeit
	true,		-- veröffentlicht
	3,			-- Schwierigkeitsgrad ID
	3,			-- Kategorie ID
	3			-- Cook ID
);

insert into PREPARATION values (1, recp_id_seq.currval, '1. Schritt: man nehme...');
insert into PREPARATION values (2, recp_id_seq.currval, '2. Schritt: und dann...');
insert into PREPARATION values (3, recp_id_seq.currval, '3. Schritt: sowie...');

insert into USES values (recp_id_seq.currval, 5, 100);
insert into USES values (recp_id_seq.currval, 3, 200);
insert into USES values (recp_id_seq.currval, 1, 300);

