
INSERT INTO pes_grau_parentesco(id, ds_descricao, id_cliente) VALUES (1, '', 1);
SELECT setval('pes_grau_parentesco_id_seq', max(id)) FROM pes_grau_parentesco;