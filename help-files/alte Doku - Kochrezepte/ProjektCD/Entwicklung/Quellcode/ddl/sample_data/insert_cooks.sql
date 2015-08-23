---------------------------------
-- Insert Cooks v0.1
-- Jochen Pätzold, Team lokalhorst
---------------------------------

--
-- Anlegen von Dummy-Köchen
--
insert into COOK values (
	cook_id_seq.nextval,	-- cook_id
	'ChefKoch',				-- Name
	'37eee1449807c9c73ac56a62a352fe7b',				-- 'Koche'
	true,					-- kein admin
	'Viele Köche',			-- Geheimfrage
	'verderben den Brei'	-- Geheimantwort
);

insert into COOK values (
	cook_id_seq.nextval,	-- cook_id
	'Brutzler',				-- Name
	'5ebe2294ecd0e0f08eab7690d2a6ee69',				-- 'secret'
	true,					-- kein admin
	'Hänschen klein',		-- Geheimfrage
	'ging allein'			-- Geheimantwort
);

insert into COOK values (
	cook_id_seq.nextval,	-- cook_id
	'Schneider',			-- Name
	'e8636ea013e682faf61f56ce1cb1ab5c',				-- 'geheim'
	false,					-- kein admin
	'Auf einen Streich',	-- Geheimfrage
	'Sieben'				-- Geheimantwort
);

insert into COOK values (
	cook_id_seq.nextval,	-- cook_id
	'Laffer',				-- Name
	'7b4ea4a44ed2a180dcd36c6d3fd38f59',				-- 'Löffel'
	false,					-- kein admin
	'Suppen',				-- Geheimfrage
	'Kelle'					-- Geheimantwort
);

insert into COOK values (
	cook_id_seq.nextval,	-- cook_id
	'Zahn',					-- Name
	'7112e445fe3526c203a9032801ede54d',				-- 'Seide'
	false,					-- kein admin
	'Zähne',				-- Geheimfrage
	'putzen'				-- Geheimantwort
);