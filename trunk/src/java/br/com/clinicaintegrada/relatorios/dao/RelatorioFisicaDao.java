package br.com.clinicaintegrada.relatorios.dao;

import br.com.clinicaintegrada.principal.DB;
import br.com.clinicaintegrada.relatorios.Relatorios;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class RelatorioFisicaDao extends DB {

    private String order = "";

    private Relatorios relatorios;
    private Boolean IS_QUERY = false;
    private String QUERY = "";

    /**
     *
     * @param cliente_id
     * @param fisica_id
     * @param in_cidades
     * @param data_cadastro_I
     * @param data_cadastro_F
     * @param data_nascimento_I
     * @param data_nascimento_F
     * @param idade
     * @param sexo
     * @return
     */
    public List find(Integer cliente_id, Integer fisica_id, String in_cidades, String data_cadastro_I, String data_cadastro_F, String data_nascimento_I, String data_nascimento_F, Integer[] idade, String sexo) {
        List listWhere = new ArrayList();
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
                    + "            L.ds_descricao || ' ' || DE.ds_descricao || ', ' || PE.ds_numero || ' ' || PE.ds_complemento || ' - ' ||  B.ds_descricao || ' - ' || C.ds_cidade || ' / ' || C.ds_uf || ' - CEP: ' || E.ds_cep AS pessoa_endereco_completo               \n" // 19 - ENDEREÇO COMPLETO
                    // FROM
                    + "       FROM pes_fisica               AS F                                        \n"
                    + " INNER JOIN pes_pessoa               AS P  ON  P.id = F.id_pessoa                \n"
                    + "  LEFT JOIN pes_pessoa_endereco      AS PE ON PE.id_pessoa = F.id_pessoa         \n"
                    + "            AND PE.id_tipo_endereco = 1                                          \n"
                    + "  LEFT JOIN end_endereco             AS E  ON  E.id = PE.id_endereco             \n"
                    + "  LEFT JOIN end_cidade               AS C  ON  C.id = E.id_cidade                \n"
                    + "  LEFT JOIN end_logradouro           AS L  ON  L.id = E.id_logradouro            \n"
                    + "  LEFT JOIN end_descricao_endereco   AS DE ON DE.id = E.id_descricao_endereco    \n"
                    + "  LEFT JOIN end_bairro               AS B  ON  B.id = E.id_bairro                \n";
            // WHERE
            // CLIENTE
            if (cliente_id != null) {
                listWhere.add("P.id_cliente = " + cliente_id);
            }
            // CIDADE
            if (!in_cidades.isEmpty()) {
                listWhere.add("C.id IN (" + in_cidades + ")");
            }
            // DATAS
            // CADASTRO
            if (!data_cadastro_I.isEmpty() || !data_cadastro_F.isEmpty()) {
                // IGUAIS
                if (data_cadastro_I.equals(data_nascimento_F)) {
                    listWhere.add("P.dt_criacao = " + data_nascimento_I);
                    // MAIOR OU IGUAL A INICIAL
                } else if (!data_cadastro_I.isEmpty() && data_cadastro_F.isEmpty()) {
                    listWhere.add("P.dt_criacao >= '" + data_cadastro_I + "'");
                    // MAIOR OU IGUAL A FINAL
                } else if (data_cadastro_I.isEmpty() && !data_cadastro_F.isEmpty()) {
                    listWhere.add("P.dt_criacao <= '" + data_cadastro_F + "'");
                    // DIFERENTES
                } else if (!data_cadastro_I.isEmpty() && !data_cadastro_F.isEmpty()) {
                    listWhere.add("P.dt_criacao BETWEEN '" + data_cadastro_I + "' AND '" + data_cadastro_F + "'");
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
                queryString += " ORDER BY " + order;
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
