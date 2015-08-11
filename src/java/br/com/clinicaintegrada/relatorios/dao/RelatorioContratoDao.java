package br.com.clinicaintegrada.relatorios.dao;

import br.com.clinicaintegrada.principal.DB;
import br.com.clinicaintegrada.relatorios.Relatorios;
import br.com.clinicaintegrada.seguranca.Registro;
import br.com.clinicaintegrada.seguranca.controleUsuario.SessaoCliente;
import br.com.clinicaintegrada.seguranca.dao.RegistroDao;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class RelatorioContratoDao extends DB {

    private String order = "";

    private Relatorios relatorios;
    private Boolean IS_QUERY = false;
    private String QUERY = "";

    /**
     *
     * @param cliente_id
     * @param paciente_id
     * @param responsavel_id
     * @param cobranca2_id
     * @param in_cidades
     * @param data_contrato_I
     * @param data_contrato_F
     * @param data_internacao_I
     * @param data_internacao_F
     * @param data_rescisao_I
     * @param data_rescisao_F
     * @param data_nascimento_I
     * @param data_nascimento_F
     * @param idade
     * @param sexo
     * @param in_tipo_contrato
     * @param in_filial_atual
     * @param in_internacao_motivo
     * @param in_desligamento_motivo
     * @return
     */
    public List find(Integer cliente_id, Integer paciente_id, Integer responsavel_id, Integer cobranca2_id, String in_cidades, String data_contrato_I, String data_contrato_F, String data_internacao_I, String data_internacao_F, String data_rescisao_I, String data_rescisao_F, String data_nascimento_I, String data_nascimento_F, Integer[] idade, String sexo, String in_tipo_contrato, String in_filial_atual, String in_internacao_motivo, String in_desligamento_motivo) {
        List listWhere = new ArrayList();
        Registro r = new RegistroDao().pesquisaRegistroPorCliente(SessaoCliente.get().getId());
        try {
            String queryString;
            queryString
                    = // ALIAS
                    "       SELECT F.id                         AS fisica_id,                             \n" // 0 - FISICA ID
                    + "            P.ds_nome                    AS pessoa_nome,                           \n" // 1 - NOME
                    + "            P.ds_documento               AS pessoa_documento,                      \n" // 2 - DOCUMENTO
                    + "            F.dt_nascimento              AS fisica_nascimento,                     \n" // 3 - NASCIMENTO
                    + "            func_idade(F.dt_nascimento)  AS fisica_idade,                          \n" // 4 - IDADE
                    + "            F.ds_sexo                    AS fisica_sexo,                           \n" // 5 - SEXO
                    + "            F.ds_rg                      AS fisica_rg,                             \n" // 6 - RG
                    + "            P.dt_criacao                 AS pessoa_cadastro,                       \n" // 7 - CADASTRO
                    + "            L.ds_descricao               AS pessoa_logradouro,                     \n" // 8 - LOGRADOURO
                    + "            DE.ds_descricao              AS pessoa_descricao_endereco,             \n" // 9 - DESC. ENDEREÇO
                    + "            PE.ds_numero                 AS pessoa_numero,                         \n" // 10 - NUMERO
                    + "            PE.ds_complemento            AS pessoa_complemento,                    \n" // 11 - COMPLEMENTO
                    + "            B.ds_descricao               AS pessoa_bairro,                         \n" // 12 - BAIRRO
                    + "            C.ds_cidade                  AS pessoa_cidade,                         \n" // 13 - CIDADE
                    + "            C.ds_uf                      AS pessoa_uf,                             \n" // 14 - UF
                    + "            E.ds_cep                     AS pessoa_cep,                            \n" // 15 - CEP
                    + "            P.ds_telefone1               AS pessoa_telefone1,                      \n" // 16 - TELEFONE 1
                    + "            P.ds_email1                  AS pessoa_email1,                         \n" // 17 - EMAIL 1
                    + "            P.ds_telefone2               AS pessoa_telefone2,                      \n" // 18 - CELULAR 2
                    + "            TI.ds_descricao              AS contrato_tipo_internacao,              \n" // 19 - TIPO INTERNAÇÃO
                    + "            TD.ds_descricao              AS contrato_tipo_desligamento,            \n" // 20 - TIPO DESLIGAMENTO
                    + "            CTR.dt_cadastro              AS contrato_data_contrato,                \n" // 21 - DATA CONTRATO
                    + "            CTR.dt_internacao            AS contrato_data_internacao,              \n" // 22 - DATA INTERNAÇÃO
                    + "            CTR.dt_rescisao              AS contrato_data_desligamento,            \n" // 23 - TIPO DESLIGAMENTO
                    + "            CTR.id                       AS contrato_numero,                       \n" // 24 - CONTRATO NÚMERO
                    + "            -- RESPONSAVEL                                                         \n"
                    + "            R.ds_nome                    AS responsavel_nome,                      \n" // 25 - RESPOSÁVEL NOME
                    + "            R.ds_documento               AS responsavel_documento,                 \n" // 26 - RESPOSÁVEL DOCUMENTO
                    + "            R.ds_telefone1               AS responsavel_telefone1,                 \n" // 27 - RESPOSÁVEL TELEFONE 1
                    + "            R.ds_email1                  AS responsavel_email1,                    \n" // 28 - RESPOSÁVEL EMAIL 1
                    + "            L.ds_descricao || ' ' || DE.ds_descricao || ', ' || PE.ds_numero || ' ' || PE.ds_complemento || ' - ' ||  B.ds_descricao || ' - ' || C.ds_cidade || ' / ' || C.ds_uf || ' - CEP: ' || E.ds_cep AS pessoa_endereco_completo,              \n" // 29 - ENDEREÇO COMPLETO
                    + "            PFA.ds_nome                  AS contrato_filial_atual                \n"
                    // FROM
                    + "       FROM ctr_contrato             AS CTR                                      \n"
                    + " INNER JOIN pes_pessoa               AS P  ON  P.id = CTR.id_paciente            \n"
                    + " INNER JOIN pes_pessoa               AS R  ON  R.id = CTR.id_responsavel         \n"
                    + " INNER JOIN pes_fisica               AS F  ON  F.id_pessoa = P.id                \n"
                    + " INNER JOIN ctr_tipo_internacao      AS TI ON  TI.id = CTR.id_tipo_internacao    \n"
                    + "  LEFT JOIN pes_pessoa_endereco      AS PE ON PE.id_pessoa = F.id_pessoa         \n"
                    + "            AND PE.id_tipo_endereco = 1                                          \n"
                    + "  LEFT JOIN end_endereco             AS E  ON  E.id = PE.id_endereco             \n"
                    + "  LEFT JOIN ctr_tipo_desligamento    AS TD ON  TD.id = CTR.id_tipo_desligamento  \n"
                    + "  LEFT JOIN end_cidade               AS C  ON  C.id = E.id_cidade                \n"
                    + "  LEFT JOIN end_logradouro           AS L  ON  L.id = E.id_logradouro            \n"
                    + "  LEFT JOIN end_descricao_endereco   AS DE ON  DE.id = E.id_descricao_endereco   \n"
                    + "  LEFT JOIN end_bairro               AS B  ON  B.id = E.id_bairro                \n"
                    + "  LEFT JOIN pes_juridica             AS JFA ON JFA.id = CTR.id_filial_atual      \n"
                    + "  LEFT JOIN pes_pessoa               AS PFA ON PFA.id = JFA.id_pessoa            \n";
            // WHERE
            // CLIENTE
            if (cliente_id != null) {
                listWhere.add("P.id_cliente = " + cliente_id);
            }
            // PACIENTE
            if (paciente_id != null) {
                listWhere.add("CTR.id_paciente = " + paciente_id);
            }
            // RESPONSÁVEL
            if (responsavel_id != null) {
                listWhere.add("CTR.id_responsavel = " + responsavel_id);
            }
            // CIDADE
            if (!in_cidades.isEmpty()) {
                listWhere.add("CTR.id IN (" + in_cidades + ")");
            }
            // MOTIVO INTERNACAO
            if (!in_internacao_motivo.isEmpty()) {
                listWhere.add("CTR.id_tipo_internacao IN (" + in_internacao_motivo + ")");
            }
            // TIPO CONTRATO
            if (!in_tipo_contrato.isEmpty()) {
                listWhere.add("CTR.id_tipo_contrato IN (" + in_tipo_contrato + ")");
            }
            // MOTIVO DESLIGAMENTO
            if (!in_desligamento_motivo.isEmpty()) {
                listWhere.add("CTR.id_tipo_desligamento IN (" + in_desligamento_motivo + ")");
            }
            // FILIAL ATUAL
            if (!in_filial_atual.equals("null") && !in_filial_atual.isEmpty() && !in_filial_atual.equals("0")) {
                listWhere.add("CTR.id_filial_atual IN (" + in_filial_atual + ")");
            }
            // DATAS
            // CONTRATO
            if (!data_contrato_I.isEmpty() || !data_contrato_F.isEmpty()) {
                // IGUAIS
                if (data_contrato_I.equals(data_nascimento_F)) {
                    listWhere.add("CTR.dt_cadastro = " + data_contrato_I);
                    // MAIOR OU IGUAL A INICIAL
                } else if (!data_contrato_I.isEmpty() && data_contrato_F.isEmpty()) {
                    listWhere.add("CTR.dt_cadastro >= '" + data_contrato_I + "'");
                    // MAIOR OU IGUAL A FINAL
                } else if (data_contrato_I.isEmpty() && !data_contrato_F.isEmpty()) {
                    listWhere.add("CTR.dt_cadastro <= '" + data_contrato_F + "'");
                    // DIFERENTES
                } else if (!data_contrato_I.isEmpty() && !data_contrato_F.isEmpty()) {
                    listWhere.add("CTR.dt_cadastro BETWEEN '" + data_contrato_I + "' AND '" + data_contrato_F + "'");
                }
            }
            // INTERNAÇÃO
            if (!data_internacao_I.isEmpty() || !data_internacao_F.isEmpty()) {
                // IGUAIS
                if (data_internacao_I.equals(data_nascimento_F)) {
                    listWhere.add("CTR.dt_internacao = " + data_internacao_I);
                    // MAIOR OU IGUAL A INICIAL
                } else if (!data_internacao_I.isEmpty() && data_internacao_F.isEmpty()) {
                    listWhere.add("CTR.dt_internacao >= '" + data_internacao_F + "'");
                    // MAIOR OU IGUAL A FINAL
                } else if (data_internacao_I.isEmpty() && !data_internacao_F.isEmpty()) {
                    listWhere.add("CTR.dt_internacao <= '" + data_internacao_F + "'");
                    // DIFERENTES
                } else if (!data_internacao_I.isEmpty() && !data_internacao_F.isEmpty()) {
                    listWhere.add("CTR.dt_internacao BETWEEN '" + data_internacao_I + "' AND '" + data_internacao_F + "'");
                }
            }
            // NASCIMENTO
            if (!data_nascimento_I.isEmpty() || !data_nascimento_F.isEmpty()) {
                // IGUAIS
                if (data_nascimento_I.equals(data_nascimento_F)) {
                    listWhere.add("F.dt_nascimento = " + data_nascimento_I);
                    // MAIOR OU IGUAL A INICIAL
                } else if (!data_nascimento_I.isEmpty() && data_nascimento_F.isEmpty()) {
                    listWhere.add("F.dt_nascimento >= '" + data_nascimento_I + "'");
                    // MAIOR OU IGUAL A FINAL
                } else if (data_nascimento_I.isEmpty() && !data_nascimento_F.isEmpty()) {
                    listWhere.add("F.dt_nascimento <= '" + data_nascimento_F + "'");
                    // DIFERENTES
                } else if (!data_nascimento_I.isEmpty() && !data_nascimento_F.isEmpty()) {
                    listWhere.add("F.dt_nascimento BETWEEN '" + data_nascimento_I + "' AND '" + data_nascimento_F + "'");
                }
            }

            // IDADE
            if (idade[0] != 0 || idade[1] != 0) {
                if (idade[0].equals(idade[1])) {
                    listWhere.add(" func_idade(F.dt_nascimento) = " + idade[0]);
                } else if (idade[0] >= 0 && idade[1] == 0) {
                    listWhere.add(" func_idade(F.dt_nascimento) >= " + idade[0]);
                } else {
                    listWhere.add(" func_idade(F.dt_nascimento) BETWEEN " + idade[0] + " AND " + idade[1]);
                }
            }
            if (sexo != null && !sexo.isEmpty()) {
                listWhere.add("F.ds_sexo LIKE '" + sexo + "'");
            }
            if (!relatorios.getQry().isEmpty()) {
                listWhere.add(relatorios.getQry());
            }
            relatorios = new Relatorios();
            if (!listWhere.isEmpty()) {
                queryString += " WHERE ";
                for (int i = 0; i < listWhere.size(); i++) {
                    if (i > 0) {
                        queryString += " AND ";
                    }
                    queryString += listWhere.get(i).toString();
                }
            }
            if (!order.isEmpty()) {
                queryString += "ORDER BY " + order;
            }
            if (IS_QUERY) {
                QUERY = queryString;
                IS_QUERY = false;
                return new ArrayList();
            } else {
                Query query = getEntityManager().createNativeQuery(queryString);
                return query.getResultList();
            }
        } catch (Exception e) {
            return new ArrayList();
        }
    }

    public Relatorios getRelatorios() {
        return relatorios;
    }

    public void setRelatorios(Relatorios relatorios) {
        this.relatorios = relatorios;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public Boolean getIS_QUERY() {
        return IS_QUERY;
    }

    public void setIS_QUERY(Boolean IS_QUERY) {
        this.IS_QUERY = IS_QUERY;
    }

    public String getQUERY() {
        return QUERY;
    }

    public void setQUERY(String QUERY) {
        this.QUERY = QUERY;
    }
}
