-- fin_layout 
-- Criate: 2013-08-07
-- Last edition: 2013-08-07 - by: Bruno Vieira

INSERT INTO fin_layout (id, ds_descricao, url) SELECT 1, 'A definir', '/Relatorios/null' WHERE NOT EXISTS ( SELECT id FROM fin_layout WHERE id = 1);
