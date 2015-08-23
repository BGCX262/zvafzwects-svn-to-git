---------------------------------
-- Insert Base Values v0.1
-- Jochen Pätzold, Team lokalhorst
---------------------------------

--
-- Anlegen der Schwierigkeitsgrade
--
insert into DIFFICULTY values (1, 'leicht');
insert into DIFFICULTY values (2, 'mittel');
insert into DIFFICULTY values (3, 'schwer');

--
-- Anlegen der Einheitenliste der Zutaten
--
insert into UNIT values (unit_id_seq.nextval, 'ml');
insert into UNIT values (unit_id_seq.nextval, 'mg');
insert into UNIT values (unit_id_seq.nextval, 'Stk');
insert into UNIT values (unit_id_seq.nextval, ' ');

--
-- Anlegen einiger Kategorien (10 Stück) 
--
insert into Category values (cat_id_seq.nextval, 'Vorspeisen');
insert into Category values (cat_id_seq.nextval, 'Hauptspeise');
insert into Category values (cat_id_seq.nextval, 'Nachspeise');
insert into Category values (cat_id_seq.nextval, 'Zwischenspeisen');
insert into Category values (cat_id_seq.nextval, 'Eis');
insert into Category values (cat_id_seq.nextval, 'Pudding');
insert into Category values (cat_id_seq.nextval, 'Suppe');
insert into Category values (cat_id_seq.nextval, 'Italienisch');
insert into Category values (cat_id_seq.nextval, 'Kreativ');
insert into Category values (cat_id_seq.nextval, 'Moderne Küche');