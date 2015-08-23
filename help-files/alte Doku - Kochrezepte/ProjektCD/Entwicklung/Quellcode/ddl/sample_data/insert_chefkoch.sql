---------------------------------
-- Insert Cooks v0.1
-- Jochen Pätzold, Christian Zöllner Team lokalhorst
---------------------------------

--
-- Anlegen vom Chefkoch
--
insert into COOK values (
	cook_id_seq.nextval,	-- cook_id
	'ChefKoch',				-- Name
	'37eee1449807c9c73ac56a62a352fe7b',				-- 'Koche'
	true,					-- kein admin
	'Viele Köche',			-- Geheimfrage
	'verderben den Brei'	-- Geheimantwort
);
