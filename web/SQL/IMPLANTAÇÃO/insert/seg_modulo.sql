-- seg_modulo
-- Criate: 2014-07-07
-- Last edition: 2014-07-07 - by: Bruno Vieira

INSERT INTO seg_modulo (id, ds_descricao) SELECT 1, 'Segurança'     WHERE NOT EXISTS ( SELECT id FROM seg_modulo WHERE id = 1);
INSERT INTO seg_modulo (id, ds_descricao) SELECT 2, 'Cadastro'      WHERE NOT EXISTS ( SELECT id FROM seg_modulo WHERE id = 2);
INSERT INTO seg_modulo (id, ds_descricao) SELECT 3, 'Ficha Técnica' WHERE NOT EXISTS ( SELECT id FROM seg_modulo WHERE id = 3);
INSERT INTO seg_modulo (id, ds_descricao) SELECT 4, 'Financeiro'    WHERE NOT EXISTS ( SELECT id FROM seg_modulo WHERE id = 4);
SELECT setval('seg_modulo_id_seq', max(id)) FROM seg_modulo;

