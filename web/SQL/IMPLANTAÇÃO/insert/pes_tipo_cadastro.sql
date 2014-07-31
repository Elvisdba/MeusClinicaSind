
-- pes_tipo_documento 
-- Criate: 2013-08-02
-- Last edition: 2013-08-02 - by: Bruno Vieira

INSERT INTO pes_tipo_cadastro (id, ds_descricao) SELECT 1, 'FUNCIONÁRIO' WHERE NOT EXISTS ( SELECT id FROM pes_tipo_cadastro WHERE id = 1);
SELECT setval('pes_tipo_cadastro_id_seq', max(id)) FROM pes_tipo_cadastro;
