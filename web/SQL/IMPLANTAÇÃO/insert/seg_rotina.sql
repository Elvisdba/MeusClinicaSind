-- DELETE FROM seg_rotina

-- SELECT setval('seg_rotina_id_seq', max(id)) FROM seg_rotina;

-- seg_rotina
-- Criate: 2014-07-04
-- Last edition: 2013-08-02 - by: Bruno Vieira

-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 1, 'ROTINA', '"/ClinicaIntegrada/rotina.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 1);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 2, 'SEGURANÇA EVENTO', '"/ClinicaIntegrada/segEvento.jsf"', 'SegEvento', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 2);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 3, 'MÓDULO', '"/ClinicaIntegrada/modulo.jsf"', 'Modulo', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 3);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 4, 'PERMISSÃO', '"/ClinicaIntegrada/permissao.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 4);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 5, 'DEPARTAMENTO', '"/ClinicaIntegrada/departamento.jsf"', 'Departamento', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 5);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 6, 'NÍVEL', '"/ClinicaIntegrada/nivel.jsf"', 'Nivel', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 6);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 7, 'PESSOA FÍSICA', '"/ClinicaIntegrada/pessoaFisica.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 7);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 8, 'USUÁRIO', '"/ClinicaIntegrada/usuario.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 8);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 9, 'PESSOA JURÍDICA', '"/ClinicaIntegrada/pessoaJuridica.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 9);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 10, 'USUÁRIO FALHOU', '"/ClinicaIntegrada/usuarioFalhou.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 10);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 11, 'ACESSO NEGADO', '"/ClinicaIntegrada/acessoNegado.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 11);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 12, 'FILIAL', '"/ClinicaIntegrada/filial.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 12);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 13, 'BAIRRO', '"/ClinicaIntegrada/bairro.jsf"', 'Bairro', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 13);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 14, 'CIDADE', '"/ClinicaIntegrada/cidade.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 14);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 15, 'DESCRIÇÃO ENDEREÇO', '"/ClinicaIntegrada/descricaoEndereco.jsf"', 'DescricaoEndereco', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 14);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 16, 'LOGRADOURO', '"/ClinicaIntegrada/logradouro.jsf"', 'Logradouro', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 16);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 17, 'ENDEREÇO', '"/ClinicaIntegrada/endereco.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 17);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 18, 'PROFISSÃO', '"/ClinicaIntegrada/profissao.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 18);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 19, 'TIPO ENDEREÇO', '"/ClinicaIntegrada/tipoEndereco.jsf"', 'TipoEndereco', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 19);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 20, 'TIPO DOCUMENTO', '"/ClinicaIntegrada/tipoDocumento.jsf"', 'TipoDocumento', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 20);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 21, 'PESQUISA PESSOA', '"/ClinicaIntegrada/pesquisaPessoa.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 21);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 22, 'PESQUISA PESSOA FISÍCA', '"/ClinicaIntegrada/pesquisaPessoaFisica.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 22);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 23, 'PESQUISA PESSOA JURIDICA', '"/ClinicaIntegrada/pesquisaPessoaJuridica.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 23);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 24, 'MENU PRINCIPAL', '"/ClinicaIntegrada/menuPrincipal.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 24);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 25, 'MENU COORDENAÇÃO', '"/ClinicaIntegrada/menuCoordenadacao.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 25);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 26, 'MENU EQUIPE TÉCNICA', '"/ClinicaIntegrada/menuEquipeTecnica.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 26);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 27, 'PESQUISA USUÁRIO', '"/ClinicaIntegrada/pesquisaUsuario.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 27);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 28, 'PESQUISA ENDEREÇO', '"/ClinicaIntegrada/pesquisaEndereco.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 28);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 29, 'PERMISSÃO DEPARTAMENTO', '"/ClinicaIntegrada/permissaoDepartamento.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 29);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 30, 'CNAE', '"/ClinicaIntegrada/cnae.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 30);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 31, 'AGENDA', '"/ClinicaIntegrada/agenda.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 31);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 32, 'GRUPO AGENDA', '"/ClinicaIntegrada/grupoAgenda.jsf"', 'GrupoAgenda', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 32);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 33, 'EMAIL', '"/ClinicaIntegrada/email.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 33);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 34, 'CADASTRO SIMPLES', '"/ClinicaIntegrada/simples.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 34);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 35, 'REGISTRO COMPUTADOR', '"/ClinicaIntegrada/macFilial.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 35);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 36, 'AGENDA TELEFÔNICA', '"/ClinicaIntegrada/agendaTelefone.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 36);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 37, 'PESQUISA AGENDA TELEFÔNICA', '"/ClinicaIntegrada/pesquisaAgendaTelefone.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 37);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 38, 'CLIENTE', '"/ClinicaIntegrada/cliente.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 38);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 39, 'PESQUISA CLIENTES', '"/ClinicaIntegrada/pesquisaCliente.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 39);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 40, 'USUÁRIO PERFIL', '"/ClinicaIntegrada/usuarioPerfil.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 40);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 41, 'PESQUISA PESSOA', '"/ClinicaIntegrada/pesquisaSisPessoa.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 41);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 42, 'FILIAL DEPARTAMENTO', '"/ClinicaIntegrada/filialDepartamento.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 42);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 43, 'PESQUISAR LOGS', '"/ClinicaIntegrada/pesquisaLog.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 43);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 44, 'MENU ADMINISTRATIVO', '"/ClinicaIntegrada/menuAdministrativo.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 44);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 45, 'TIPO DE CADASTRO', '"/ClinicaIntegrada/tipoCadastro.jsf"', 'TipoCadastro', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 45);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 46, 'GRAU PARENTESCO', '"/ClinicaIntegrada/grauParentesco.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 46);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 47, 'TIPO ATENDIMENTO', '"/ClinicaIntegrada/tipoAtendimento.jsf"', 'TipoAtendimento', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 47);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 48, 'FUNÇÃO ESCALA', '"/ClinicaIntegrada/funcaoEscala.jsf"', 'FuncaoEscala', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 48);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 49, 'COMBUSTÍVEL', '"/ClinicaIntegrada/combustivel.jsf"', 'Combustivel', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 49);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 50, 'TIPO INTERNAÇÃO', '"/ClinicaIntegrada/tipoInternacao.jsf"', 'TipoInternacao', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 50);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 51, 'TIPO DESLIGAMENTO', '"/ClinicaIntegrada/tipoDesligamento.jsf"', 'TipoDesligamento', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 51);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 52, 'FUNÇÃO EQUIPE', '"/ClinicaIntegrada/funcaoEquipe.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 52);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 53, 'EQUIPE', '"/ClinicaIntegrada/equipe.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 53);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 54, 'TIPO DOCUMENTO PROFISSÃO', '"/ClinicaIntegrada/tipoDocumentoProfissao.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 54);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 55, 'PESQUISA EQUIPE', '"/ClinicaIntegrada/pesquisaEquipe.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 55);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 56, 'VEÍCULO', '"/ClinicaIntegrada/veiculo.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 56);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 57, 'GRUPO EVENTO', '"/ClinicaIntegrada/grupoEvento.jsf"', 'GrupoEvento', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 57);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 58, 'EVENTOS DIVERSOS', '"/ClinicaIntegrada/evento.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 58);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 59, 'AGENDA GRUPO USUÁRIO', '"/ClinicaIntegrada/agendaGrupoUsuario.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 59);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 60, 'CONFIGURAÇÕES', '"/ClinicaIntegrada/registro.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 60);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 61, 'CRONÔGRAMA', '"/ClinicaIntegrada/cronograma.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 61);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 62, 'CONTRATO', '"/ClinicaIntegrada/contrato.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 62);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 63, 'PLANO DE CONTAS', '"/ClinicaIntegrada/plano.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 63);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 64, 'MENU FINANCEIRO', '"/ClinicaIntegrada/menuFinanceiro.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 64);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 65, 'CONTA BANCO', '"/ClinicaIntegrada/contaBanco.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 65);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 66, 'CONTA COBRANÇA', '"/ClinicaIntegrada/contaCobranca.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 66);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 67, 'BANCO', '"/ClinicaIntegrada/banco.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 67);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 68, 'ÍNDICE MENSAL', '"/ClinicaIntegrada/indiceMensal.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 68);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 69, 'ÍNDICE', '"/ClinicaIntegrada/indice.jsf"', 'Indice', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 69);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 70, 'SERVIÇOS', '"/ClinicaIntegrada/servicos.jsf"', 'Indice', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 70);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 71, 'PESQUISA SERVIÇOS', '"/ClinicaIntegrada/pesquisaServicos.jsf"', 'Indice', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 71);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 72, 'TAXAS', '"/ClinicaIntegrada/taxas.jsf"', 'Taxas', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 72);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 73, 'MODELO CONTRATO', '"/ClinicaIntegrada/modeloContrato.jsf"', 'Taxas', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 73);
-- SELECT setval('seg_rotina_id_seq', max(id)) FROM seg_rotina;


