-- INSERIR DADOS NA SEQUÊNCIA;

-- PES_TIPO_DOCUMENTO

INSERT INTO pes_tipo_documento (id, ds_descricao) SELECT 1, 'CPF' WHERE NOT EXISTS ( SELECT id FROM pes_tipo_documento WHERE id = 1);
INSERT INTO pes_tipo_documento (id, ds_descricao) SELECT 2, 'CNPJ' WHERE NOT EXISTS ( SELECT id FROM pes_tipo_documento WHERE id = 2);
INSERT INTO pes_tipo_documento (id, ds_descricao) SELECT 3, 'CEI' WHERE NOT EXISTS ( SELECT id FROM pes_tipo_documento WHERE id = 3);
INSERT INTO pes_tipo_documento (id, ds_descricao) SELECT 4, 'SEM DOCUMENTO' WHERE NOT EXISTS ( SELECT id FROM pes_tipo_documento WHERE id = 4);
SELECT setval('pes_tipo_documento_id_seq', max(id)) FROM pes_tipo_documento;

-- SEG_CLIENTE

INSERT INTO seg_cliente(
            id, id_juridica, nr_acesso, ds_persistence, dt_cadastro, ds_identifica, 
            is_ativo, ds_caminho_sistema, ds_host, ds_nome_cliente, ds_senha)
    VALUES (1, 1, 0, 'ClinicaIntegrada', CURRENT_DATE, 'ClinicaIntegrada', 
            true, 'ClinicaIntegrada', '', 'Administrador Clínica Integrada', '');


-- PES_PESSOA

INSERT INTO pes_pessoa(
            id, id_cliente, ds_email1, ds_email2, ds_obs, ds_email3, ds_telefone1, ds_documento, 
            ds_telefone3, ds_login, ds_site, ds_senha, ds_nome, dt_criacao, 
            ds_telefone2, id_tipo_documento)
    VALUES (1, 1, '', '', '', '', '', '', 
            '', '', '', '', 'ADMIN', current_date, 
            '', 1);



-- PES_JURIDICA

INSERT INTO pes_juridica(
            id, id_cliente, dt_abertura, dt_fechamento, ds_inscricao_estadual, is_email_escritorio, 
            ds_contato, ds_inscricao_municipal, ds_fantasia, ds_responsavel, 
            id_cnae, id_contabilidade, id_porte, id_pessoa, is_cobranca_escritorio)
    VALUES (1, 1, null, null, '', false, 
            '', '', '', '', 
            null, null, 1, 1, false);

-- SEG_USUARIO

INSERT INTO seg_usuario(
            id, id_cliente, ds_login, ds_senha, id_pessoa, is_ativo, ds_email, is_administrador)
    VALUES (1, 1, 'admin', 'admin', 1, true, '', true);

-- PES_FILIAL

INSERT INTO pes_filial(
            id, nr_centro_custo, id_filial, id_matriz)
    VALUES (1, 0, 1, 1);


-- SEG_REGISTRO


INSERT INTO seg_registro(
            id, sis_is_enviar_email_anexo, sis_email_resposta, sis_ds_senha, 
            sis_email_porta, sis_ds_url_path, sis_is_email_marketing, sis_ds_smtp, 
            sis_ds_email, id_cliente, id_email_protocolo)
    VALUES (1, false, '', '', 
            0, '', false, '', 
            '', 1, 1);



-- SEGURANÇA

-- SEG_REGISTRO


INSERT INTO seg_nivel (id, ds_descricao) SELECT 1, 'Usuario'   WHERE NOT EXISTS ( SELECT id FROM seg_nivel WHERE id = 1);
INSERT INTO seg_nivel (id, ds_descricao) SELECT 2, 'Usuário (Avançado)'    WHERE NOT EXISTS ( SELECT id FROM seg_nivel WHERE id = 2);
INSERT INTO seg_nivel (id, ds_descricao) SELECT 3, 'Coordenador (Depto)' WHERE NOT EXISTS ( SELECT id FROM seg_nivel WHERE id = 3);
INSERT INTO seg_nivel (id, ds_descricao) SELECT 4, 'Gerente'  WHERE NOT EXISTS ( SELECT id FROM seg_nivel WHERE id = 4);
INSERT INTO seg_nivel (id, ds_descricao) SELECT 5, 'Administrador' WHERE NOT EXISTS ( SELECT id FROM seg_nivel WHERE id = 5);
SELECT setval('seg_nivel_id_seq', max(id)) FROM seg_nivel;


-- SEG_MODULO

INSERT INTO seg_modulo (id, ds_descricao) SELECT 1, 'Financeiro'        WHERE NOT EXISTS ( SELECT id FROM seg_modulo WHERE id = 1);
INSERT INTO seg_modulo (id, ds_descricao) SELECT 2, 'Social'            WHERE NOT EXISTS ( SELECT id FROM seg_modulo WHERE id = 2);
INSERT INTO seg_modulo (id, ds_descricao) SELECT 3, 'Arrecadação'       WHERE NOT EXISTS ( SELECT id FROM seg_modulo WHERE id = 3);
INSERT INTO seg_modulo (id, ds_descricao) SELECT 4, 'Homologação'       WHERE NOT EXISTS ( SELECT id FROM seg_modulo WHERE id = 4);
INSERT INTO seg_modulo (id, ds_descricao) SELECT 5, 'Jurídico'          WHERE NOT EXISTS ( SELECT id FROM seg_modulo WHERE id = 5);
INSERT INTO seg_modulo (id, ds_descricao) SELECT 6, 'Clube'             WHERE NOT EXISTS ( SELECT id FROM seg_modulo WHERE id = 6);
INSERT INTO seg_modulo (id, ds_descricao) SELECT 7, 'Academia'          WHERE NOT EXISTS ( SELECT id FROM seg_modulo WHERE id = 7);
INSERT INTO seg_modulo (id, ds_descricao) SELECT 8, 'Escola'            WHERE NOT EXISTS ( SELECT id FROM seg_modulo WHERE id = 8);
INSERT INTO seg_modulo (id, ds_descricao) SELECT 9, 'Cadastro Auxiliar' WHERE NOT EXISTS ( SELECT id FROM seg_modulo WHERE id = 9);
INSERT INTO seg_modulo (id, ds_descricao) SELECT 10, 'Segurança'        WHERE NOT EXISTS ( SELECT id FROM seg_modulo WHERE id = 10);
SELECT setval('seg_modulo_id_seq', max(id)) FROM seg_modulo;

-- SEG_EVENTO

INSERT INTO seg_evento (id, ds_descricao) SELECT 1, 'Inclusão'  WHERE NOT EXISTS ( SELECT id FROM seg_evento WHERE id = 1);
INSERT INTO seg_evento (id, ds_descricao) SELECT 2, 'Exclusão'  WHERE NOT EXISTS ( SELECT id FROM seg_evento WHERE id = 2);
INSERT INTO seg_evento (id, ds_descricao) SELECT 3, 'Alteração' WHERE NOT EXISTS ( SELECT id FROM seg_evento WHERE id = 3);
INSERT INTO seg_evento (id, ds_descricao) SELECT 4, 'Consulta'  WHERE NOT EXISTS ( SELECT id FROM seg_evento WHERE id = 4);
SELECT setval('seg_evento_id_seq', max(id)) FROM seg_evento;

-- seg_rotina

-- PEGAR AS ROTINAS NO INSERT - seg_rotina.sql


-- SEG_DEPARTAMENTO

-- VERIFICAR O SCRIPT seq_departamento.sql antes de inserir - ultima id = 14

INSERT INTO seg_departamento (id, ds_descricao) SELECT 1, 'CPD'   WHERE NOT EXISTS ( SELECT id FROM seg_departamento WHERE id = 1);
SELECT setval('seg_departamento_id_seq', max(id)) FROM seg_departamento;

-- CONFIGURAÇÃO EMAIL

-- SIS_EMAIL_PROTOCOLO

INSERT INTO sis_email_protocolo (id, ds_descricao) SELECT 1,'NENHUMA' WHERE NOT EXISTS ( SELECT id FROM sis_email_protocolo WHERE id = 1);
INSERT INTO sis_email_protocolo (id, ds_descricao) SELECT 2,'STARTTLS' WHERE NOT EXISTS ( SELECT id FROM sis_email_protocolo WHERE id = 2);
INSERT INTO sis_email_protocolo (id, ds_descricao) SELECT 3,'SSL/TLS' WHERE NOT EXISTS ( SELECT id FROM sis_email_protocolo WHERE id = 3);
SELECT setval('sis_email_protocolo_id_seq', max(id)) FROM sis_email_protocolo;
