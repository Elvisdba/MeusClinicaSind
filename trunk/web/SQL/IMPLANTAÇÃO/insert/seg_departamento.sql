INSERT INTO seg_departamento (id, ds_descricao) SELECT 1, 'CPD'   WHERE NOT EXISTS ( SELECT id FROM seg_departamento WHERE id = 1);
INSERT INTO seg_departamento (id, ds_descricao) SELECT 2, 'Coordenação'   WHERE NOT EXISTS ( SELECT id FROM seg_departamento WHERE id = 2);
INSERT INTO seg_departamento (id, ds_descricao) SELECT 3, 'Equipe Técnica'   WHERE NOT EXISTS ( SELECT id FROM seg_departamento WHERE id = 3);
INSERT INTO seg_departamento (id, ds_descricao) SELECT 4, 'Secretaría'   WHERE NOT EXISTS ( SELECT id FROM seg_departamento WHERE id = 4);
INSERT INTO seg_departamento (id, ds_descricao) SELECT 5, 'Diretoria'   WHERE NOT EXISTS ( SELECT id FROM seg_departamento WHERE id = 5);
INSERT INTO seg_departamento (id, ds_descricao) SELECT 6, 'Gerencia'   WHERE NOT EXISTS ( SELECT id FROM seg_departamento WHERE id = 6);
INSERT INTO seg_departamento (id, ds_descricao) SELECT 6, 'Administração'   WHERE NOT EXISTS ( SELECT id FROM seg_departamento WHERE id = 6);
SELECT setval('seg_departamento_id_seq', max(id)) FROM seg_departamento;