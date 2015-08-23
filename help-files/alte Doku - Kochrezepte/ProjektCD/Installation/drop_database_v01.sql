---------------------------------
---Drop Database Skript V0.1
---Christian Zöllner, Team lokalhorst
---------------------------------

--
-- Tabellen löschen
--
drop table Uses;
drop table Preparation;
drop table BuyListed;
drop table Recipe;
drop table Ingredient;
drop table Unit;
drop table Difficulty;
drop table Cook;
drop table Category;

--
-- Sequenzen l�schen
--
drop SEQUENCE recp_id_seq;
drop SEQUENCE cook_id_seq;
drop SEQUENCE cat_id_seq;
drop SEQUENCE ingr_id_seq;
drop SEQUENCE unit_id_seq;