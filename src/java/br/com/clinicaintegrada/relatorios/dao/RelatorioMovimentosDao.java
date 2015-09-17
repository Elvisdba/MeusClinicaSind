package br.com.clinicaintegrada.relatorios.dao;

import br.com.clinicaintegrada.principal.DB;
import br.com.clinicaintegrada.relatorios.Relatorios;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class RelatorioMovimentosDao extends DB {

    private String order = "";

    private Relatorios relatorios;
    private Boolean IS_QUERY = false;
    private String QUERY = "";

    /**
     *
     * @param cliente_id
     * @param contrato_numero
     * @param filtro_pessoa
     * @param pessoa_id
     * @param in_servicos
     * @param data_contrato_I
     * @param data_contrato_F
     * @param data_vencimento_I
     * @param data_vencimento_F
     * @param data_baixa_F
     * @param data_baixa_I
     * @param in_filial
     * @return
     */
    public List find(Integer cliente_id, Integer contrato_numero, String filtro_pessoa, Integer pessoa_id, String in_servicos, String data_contrato_I, String data_contrato_F, String data_vencimento_I, String data_vencimento_F, String data_baixa_I, String data_baixa_F, String in_filial) {
        List listWhere = new ArrayList();
        try {
            String queryString;
            queryString
                    = // ALIAS
                    "      SELECT C.id             AS contrato_numero,       \n"
                    + "           C.dt_cadastro    AS contrato_data_cadastro,\n"
                    + "           R.ds_nome        AS responsavel_nome,      \n"
                    + "           P.ds_nome        AS paciente_nome,         \n"
                    + "           D.ds_nome        AS devedor_nome,          \n"
                    + "           M.dt_vencimento  AS data_vencimento,       \n"
                    + "           cast(round(cast(M.nr_valor AS numeric), 10) AS double precision)       AS valor,       \n"
                    + "           cast(round(cast(M.nr_valor_baixa AS numeric), 10) AS double precision) AS valor_baixa, \n"
                    + "           B.dt_baixa       AS data_baixa,            \n"
                    + "           B.dt_importacao  AS data_importacao,       \n"
                    + "           S.ds_descricao   AS servicos_descricao,    \n"
                    + "           M.ds_documento   AS movimento_documento    \n"
                    + "      FROM fin_lote      AS L                         \n"
                    + "INNER JOIN fin_movimento AS M ON M.id_lote = L.id     \n"
                    + " LEFT JOIN ctr_contrato  AS C ON C.id = L.id_contrato \n"
                    + "INNER JOIN pes_pessoa    AS R ON R.id = L.id_pessoa   \n"
                    + "INNER JOIN pes_pessoa    AS D ON D.id = M.id_pessoa   \n"
                    + "INNER JOIN pes_pessoa    AS P ON P.id = C.id_paciente \n"
                    + " LEFT JOIN fin_baixa     AS B ON B.id = M.id_baixa    \n"
                    + "INNER JOIN fin_servicos  AS S ON S.id = M.id_servicos \n";
            // WHERE
            listWhere.add("M.is_ativo = true");
            listWhere.add("M.ds_es='E'");
            // CLIENTE
            if (cliente_id != null) {
                listWhere.add("L.id_cliente = " + cliente_id);
            }
            // PESSOA
            if (pessoa_id != null) {
                switch (filtro_pessoa) {
                    case "paciente":
                        listWhere.add("C.id_paciente = " + pessoa_id);
                        break;
                    case "responsavel":
                        listWhere.add("C.id_responsavel = " + pessoa_id);
                        break;
                    case "devedor":
                        listWhere.add("M.id_pessoa = " + pessoa_id);
                        break;
                }
            }
            // CONTRATO NÚMERO
            if (contrato_numero != null && contrato_numero > 0) {
                listWhere.add("C.id = " + contrato_numero);
            }
            // SERVIÇOS
            if (!in_servicos.isEmpty()) {
                listWhere.add("S.id IN (" + in_servicos + ")");
            }
            // FILIAL ATUAL
            if (!in_filial.equals("null") && !in_filial.isEmpty() && !in_filial.equals("0")) {
                listWhere.add("L.id_filial IN (" + in_filial + ")");
            }
            // DATAS
            // CONTRATO
            if (!data_contrato_I.isEmpty() || !data_contrato_F.isEmpty()) {
                // IGUAIS
                if (data_contrato_I.equals(data_contrato_F)) {
                    listWhere.add("C.dt_cadastro = " + data_contrato_I);
                    // MAIOR OU IGUAL A INICIAL
                } else if (!data_contrato_I.isEmpty() && data_contrato_F.isEmpty()) {
                    listWhere.add("C.dt_cadastro >= '" + data_contrato_I + "'");
                    // MAIOR OU IGUAL A FINAL
                } else if (data_contrato_I.isEmpty() && !data_contrato_F.isEmpty()) {
                    listWhere.add("C.dt_cadastro <= '" + data_contrato_F + "'");
                    // DIFERENTES
                } else if (!data_contrato_I.isEmpty() && !data_contrato_F.isEmpty()) {
                    listWhere.add("C.dt_cadastro BETWEEN '" + data_contrato_I + "' AND '" + data_contrato_F + "'");
                }
            }
            // VENCIMENTO
            if (!data_vencimento_I.isEmpty() || !data_baixa_F.isEmpty()) {
                // IGUAIS
                if (data_vencimento_I.equals(data_baixa_F)) {
                    listWhere.add("M.dt_internacao = " + data_vencimento_I);
                    // MAIOR OU IGUAL A INICIAL
                } else if (!data_vencimento_I.isEmpty() && data_baixa_F.isEmpty()) {
                    listWhere.add("M.dt_internacao >= '" + data_baixa_F + "'");
                    // MAIOR OU IGUAL A FINAL
                } else if (data_vencimento_I.isEmpty() && !data_baixa_F.isEmpty()) {
                    listWhere.add("M.dt_internacao <= '" + data_baixa_F + "'");
                    // DIFERENTES
                } else if (!data_vencimento_I.isEmpty() && !data_baixa_F.isEmpty()) {
                    listWhere.add("M.dt_internacao BETWEEN '" + data_vencimento_I + "' AND '" + data_baixa_F + "'");
                }
            }
            // BAIXA
            if (!data_baixa_I.isEmpty() || !data_baixa_F.isEmpty()) {
                // IGUAIS
                if (data_baixa_I.equals(data_baixa_F)) {
                    listWhere.add("B.dt_baixa = " + data_baixa_I);
                    // MAIOR OU IGUAL A INICIAL
                } else if (!data_baixa_I.isEmpty() && data_baixa_F.isEmpty()) {
                    listWhere.add("B.dt_baixa >= '" + data_baixa_I + "'");
                    // MAIOR OU IGUAL A FINAL
                } else if (data_baixa_I.isEmpty() && !data_baixa_F.isEmpty()) {
                    listWhere.add("B.dt_baixa <= '" + data_baixa_F + "'");
                    // DIFERENTES
                } else if (!data_baixa_I.isEmpty() && !data_baixa_F.isEmpty()) {
                    listWhere.add("B.dt_baixa BETWEEN '" + data_baixa_I + "' AND '" + data_baixa_F + "'");
                }
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

    /**
     *
     * Relatório resumido com totais
     * 
     * @param cliente_id
     * @param contrato_numero
     * @param filtro_pessoa
     * @param pessoa_id
     * @param in_servicos
     * @param data_contrato_I
     * @param data_contrato_F
     * @param data_vencimento_I
     * @param data_vencimento_F
     * @param data_baixa_F
     * @param data_baixa_I
     * @param in_filial
     * @return
     */
    public List findResume(Integer cliente_id, Integer contrato_numero, String filtro_pessoa, Integer pessoa_id, String in_servicos, String data_contrato_I, String data_contrato_F, String data_vencimento_I, String data_vencimento_F, String data_baixa_I, String data_baixa_F, String in_filial) {
        List listWhere = new ArrayList();
        try {
            String queryString;
            queryString = " SELECT contrato_numero,                   \n"
                    + "            responsavel_nome,                  \n"
                    + "            paciente_nome,                     \n"
                    + "            devedor_nome,                      \n"
                    + "            sum(total)        AS total,        \n"
                    + "            sum(quitados)     AS quitados,     \n"
                    + "            cast(round(cast(sum(valor_aberto) AS numeric), 10) AS double precision) AS valor_aberto, \n"
                    + "            count(*)          AS parcela_meses \n"
                    + "      FROM (\n";
            queryString
                    += // ALIAS
                    "      SELECT C.id                                  AS contrato_numero, \n"
                    + "           R.ds_nome                             AS responsavel_nome,\n"
                    + "           P.ds_nome                             AS paciente_nome,   \n"
                    + "           D.ds_nome                             AS devedor_nome,    \n"
                    + "           extract(month FROM M.dt_vencimento)   AS mes,             \n"
                    + "           extract(year FROM M.dt_vencimento)    AS ano,             \n"
                    + "           cast(round(cast(sum(M.nr_valor) AS numeric), 10) AS double precision)  AS total,                      \n"
                    + "           cast(round(cast(sum(M.nr_valor_baixa) AS numeric), 10) AS double precision)  AS quitados,             \n"
                    + "            sum(func_valor_aberto(m.nr_valor,m.id_baixa))  as valor_aberto                                       \n"
                    + "      FROM fin_lote      AS L                         \n"
                    + "INNER JOIN fin_movimento AS M ON M.id_lote = L.id     \n"
                    + " LEFT JOIN ctr_contrato  AS C ON C.id = L.id_contrato \n"
                    + "INNER JOIN pes_pessoa    AS R ON R.id = L.id_pessoa   \n"
                    + "INNER JOIN pes_pessoa    AS D ON D.id = M.id_pessoa   \n"
                    + "INNER JOIN pes_pessoa    AS P ON P.id = C.id_paciente \n"
                    + " LEFT JOIN fin_baixa     AS B ON B.id = M.id_baixa    \n"
                    + "INNER JOIN fin_servicos  AS S ON S.id = M.id_servicos \n";
            // WHERE
            listWhere.add("M.is_ativo = true");
            listWhere.add("M.ds_es='E'");
            // CLIENTE
            if (cliente_id != null) {
                listWhere.add("L.id_cliente = " + cliente_id);
            }
            // PESSOA
            if (pessoa_id != null) {
                switch (filtro_pessoa) {
                    case "paciente":
                        listWhere.add("C.id_paciente = " + pessoa_id);
                        break;
                    case "responsavel":
                        listWhere.add("C.id_responsavel = " + pessoa_id);
                        break;
                    case "devedor":
                        listWhere.add("M.id_pessoa = " + pessoa_id);
                        break;
                }
            }
            // CONTRATO NÚMERO
            if (contrato_numero != null && contrato_numero > 0) {
                listWhere.add("C.id = " + contrato_numero);
            }
            // SERVIÇOS
            if (!in_servicos.isEmpty()) {
                listWhere.add("S.id IN (" + in_servicos + ")");
            }
            // FILIAL ATUAL
            if (!in_filial.equals("null") && !in_filial.isEmpty() && !in_filial.equals("0")) {
                listWhere.add("L.id_filial IN (" + in_filial + ")");
            }
            // DATAS
            // CONTRATO
            if (!data_contrato_I.isEmpty() || !data_contrato_F.isEmpty()) {
                // IGUAIS
                if (data_contrato_I.equals(data_contrato_F)) {
                    listWhere.add("C.dt_cadastro = " + data_contrato_I);
                    // MAIOR OU IGUAL A INICIAL
                } else if (!data_contrato_I.isEmpty() && data_contrato_F.isEmpty()) {
                    listWhere.add("C.dt_cadastro >= '" + data_contrato_I + "'");
                    // MAIOR OU IGUAL A FINAL
                } else if (data_contrato_I.isEmpty() && !data_contrato_F.isEmpty()) {
                    listWhere.add("C.dt_cadastro <= '" + data_contrato_F + "'");
                    // DIFERENTES
                } else if (!data_contrato_I.isEmpty() && !data_contrato_F.isEmpty()) {
                    listWhere.add("C.dt_cadastro BETWEEN '" + data_contrato_I + "' AND '" + data_contrato_F + "'");
                }
            }
            // VENCIMENTO
            if (!data_vencimento_I.isEmpty() || !data_baixa_F.isEmpty()) {
                // IGUAIS
                if (data_vencimento_I.equals(data_baixa_F)) {
                    listWhere.add("M.dt_internacao = " + data_vencimento_I);
                    // MAIOR OU IGUAL A INICIAL
                } else if (!data_vencimento_I.isEmpty() && data_baixa_F.isEmpty()) {
                    listWhere.add("M.dt_internacao >= '" + data_baixa_F + "'");
                    // MAIOR OU IGUAL A FINAL
                } else if (data_vencimento_I.isEmpty() && !data_baixa_F.isEmpty()) {
                    listWhere.add("M.dt_internacao <= '" + data_baixa_F + "'");
                    // DIFERENTES
                } else if (!data_vencimento_I.isEmpty() && !data_baixa_F.isEmpty()) {
                    listWhere.add("M.dt_internacao BETWEEN '" + data_vencimento_I + "' AND '" + data_baixa_F + "'");
                }
            }
            // BAIXA
            if (!data_baixa_I.isEmpty() || !data_baixa_F.isEmpty()) {
                // IGUAIS
                if (data_baixa_I.equals(data_baixa_F)) {
                    listWhere.add("B.dt_baixa = " + data_baixa_I);
                    // MAIOR OU IGUAL A INICIAL
                } else if (!data_baixa_I.isEmpty() && data_baixa_F.isEmpty()) {
                    listWhere.add("B.dt_baixa >= '" + data_baixa_I + "'");
                    // MAIOR OU IGUAL A FINAL
                } else if (data_baixa_I.isEmpty() && !data_baixa_F.isEmpty()) {
                    listWhere.add("B.dt_baixa <= '" + data_baixa_F + "'");
                    // DIFERENTES
                } else if (!data_baixa_I.isEmpty() && !data_baixa_F.isEmpty()) {
                    listWhere.add("B.dt_baixa BETWEEN '" + data_baixa_I + "' AND '" + data_baixa_F + "'");
                }
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
            queryString += ""
                    + " GROUP BY C.id,                                \n"
                    + "          C.dt_cadastro,                       \n"
                    + "          R.ds_nome,                           \n"
                    + "          P.ds_nome,                           \n"
                    + "          D.ds_nome,                           \n"
                    + "          extract(month FROM M.dt_vencimento), \n"
                    + "          extract(year FROM  M.dt_vencimento)  \n"
                    + ") AS subquery                                  \n"
                    + " GROUP BY contrato_numero,                     \n"
                    + "          responsavel_nome,                    \n"
                    + "          paciente_nome,                       \n"
                    + "          devedor_nome                         \n";
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
