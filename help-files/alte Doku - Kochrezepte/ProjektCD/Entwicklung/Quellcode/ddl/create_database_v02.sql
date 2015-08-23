---------------------------------
---Create Database Skript V0.2
---Christian Zöllner, Team lokalhorst
---------------------------------

--
----- Tabellen anlegen
--

--
-- Tabelle Category
--
create table Category ( cat_id integer, 
						cat_name varchar(50)
					  );

alter table Category add constraint pk_category PRIMARY KEY (cat_id);
alter table Category add constraint c_cat_name check ( cat_name is not null);

--
-- Tabelle Cook
--
create table Cook ( cook_id integer, 
					cook_name varchar(50), 
					pwd varchar(50), 
					is_admin boolean default FALSE, 
					sec_question varchar(100),
					sec_answer varchar(100)
				  );

alter table Cook add constraint pk_cook PRIMARY KEY (cook_id);
alter table Cook add constraint c_cook_name unique ( cook_name );
alter table Cook add constraint c_cook_name_null check ( cook_name is not null );
alter table Cook add constraint c_pwd check ( pwd is not null );
alter table Cook add constraint c_sec_question check ( sec_question is not null ); 
alter table Cook add constraint c_sec_answer check ( sec_answer is not null ); 

--
-- Tabelle Difficulty
--
create table Difficulty ( diff_level integer,
						  description varchar(20)
						);

alter table Difficulty add constraint pk_difficulty PRIMARY KEY (diff_level);
alter table Difficulty add constraint c_description check ( description is not null );

--
-- Tabelle Unit
-- mit id da sonst fremdschl�sselbeziehung zu ingredient sinnlos
create table Unit ( unit_id integer,
					unit_name varchar(3) 
				  );

alter table Unit add constraint pk_unit PRIMARY KEY (unit_id);

--
-- Tabelle Ingredient
--Key ?????
create table Ingredient ( ingr_id integer,
						  ingr_name varchar(50),
						  ingr_unit integer
						);

alter table Ingredient add constraint pk_ingredient PRIMARY KEY ( ingr_name, ingr_unit );
alter table Ingredient add constraint c_ingr_id UNIQUE (ingr_id);
alter table Ingredient add constraint c_ingr_id_null check ( ingr_id is not null);
alter table Ingredient add constraint fk_ingredient_unit FOREIGN KEY (ingr_unit) REFERENCES Unit (unit_id) ON DELETE RESTRICT;

--
-- Tabelle Recipe
-- daten auch not null ??, deleteverhatlten kl�ren
create table Recipe ( recp_id integer,
					  r_name varchar(100),
					  r_description varchar(500),
					  date_applied date default DATE,
					  date_released date,
					  date_last_edited date,
					  prep_time integer,
					  is_released boolean default FALSE,
					  diff_level integer,
					  cat_id integer,
					  cook_id integer 
					);

alter table Recipe add constraint pk_recipe PRIMARY KEY (recp_id);
alter table Recipe add constraint c_name check (r_name is not null);
alter table Recipe add constraint c_description check (r_description is not null);
alter table Recipe add constraint c_date_applied check (date_applied is not null);
alter table Recipe add constraint c_date_released check (date_released is not null);
alter table Recipe add constraint c_date_last_edited check (date_last_edited is not null);
alter table Recipe add constraint c_prep_time check (prep_time is not null);
alter table Recipe add constraint c_is_released check (is_released is not null);
alter table Recipe add constraint c_diff_level check (diff_level is not null);
alter table Recipe add constraint c_cat_id check (cat_id is not null);
alter table Recipe add constraint c_cook_id check (cook_id is not null);
alter table Recipe add constraint fk_recipe_diff FOREIGN KEY (diff_level) REFERENCES Difficulty(diff_level) ON DELETE RESTRICT;
alter table Recipe add constraint fk_recipe_category FOREIGN KEY (cat_id) REFERENCES Category(cat_id) ON DELETE RESTRICT;
alter table Recipe add constraint fk_recipe_cook FOREIGN KEY (cook_id) REFERENCES Cook (cook_id) ON DELETE RESTRICT;

--
-- Tabelle BuyListed
-- quantity f�r personenzahl 
create table BuyListed ( cook_id integer,
						 recp_id integer,
						 quantity integer 
					   );

alter table BuyListed add constraint pk_buylisted PRIMARY KEY (cook_id, recp_id);
alter table Buylisted add constraint c_quantity check ( quantity is not null);
alter table Buylisted add constraint fk_buylosted_cook FOREIGN KEY (cook_id) REFERENCES Cook (cook_id) ON DELETE CASCADE ;
alter table Buylisted add constraint fk_buylosted_recipe FOREIGN KEY (recp_id) REFERENCES Recipe (recp_id) ON DELETE RESTRICT;

-- 
-- Tabelle Preparation
--
create table Preparation ( prep_step integer, 
						   recp_id integer, 
						   instruction varchar(4000)
						 );

alter table Preparation add constraint pk_preparation PRIMARY KEY (recp_id, prep_step);
alter table Preparation add constraint c_instruction check (instruction is not null);
alter table Preparation add constraint fk_preparation_recipe FOREIGN KEY (recp_id) REFERENCES Recipe (recp_id) ON DELETE CASCADE;

--
-- Tabelle Uses
-- wenn rezept weg, dann in uses noch drin ?
create table Uses ( recp_id integer,
					ingr_id integer,
					amount integer
				  );

alter table Uses add constraint pk_uses PRIMARY KEY ( recp_id, ingr_id );
alter table Uses add constraint c_amount check ( amount is not null );
alter table Uses add constraint fk_uses_recipe FOREIGN KEY (recp_id) REFERENCES Recipe (recp_id) ON DELETE CASCADE ;
alter table Uses add constraint fk_uses_ingredients FOREIGN KEY (ingr_id) REFERENCES Ingredient (ingr_id); 


--
-- Sequenzen erzeugen
--
create SEQUENCE recp_id_seq;
create SEQUENCE cook_id_seq;
create SEQUENCE cat_id_seq;
create SEQUENCE ingr_id_seq;
create SEQUENCE unit_id_seq;
