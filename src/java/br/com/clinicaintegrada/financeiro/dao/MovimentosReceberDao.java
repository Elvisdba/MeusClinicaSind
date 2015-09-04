package br.com.clinicaintegrada.financeiro.dao;

import br.com.clinicaintegrada.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class MovimentosReceberDao extends DB {

    public List listMovimentos(String ids, String status) {
        return listMovimentos(ids, status, null);
    }

    public List listMovimentos(String ids, String status, Integer contrato_id) {
        try {
            String queryString = " "
                    + "      SELECT S.ds_descricao                                      AS servico,         \n" // 0
                    + "             TS.ds_descricao                                     AS tipo,            \n" // 1
                    + "             M.ds_referencia,                                                        \n" // 2
                    + "             M.dt_vencimento AS vencimento,                                          \n" // 3
                    + "             func_valor(M.id) AS valor,                                              \n" // 4
                    + "             func_multa(M.id) + func_juros(M.id)+func_correcao(M.id) AS acrescimo,   \n" // 5
                    + "             M.nr_desconto AS desconto,                                              \n" // 6
                    + "             cast(round(cast(M.nr_valor + func_multa(M.id)+func_juros(M.id) + func_correcao(M.id) AS numeric), 10) AS double precision) AS vl_calculado, \n" // 7
                    + "             B.dt_baixa,                                                             \n" // 8
                    + "             nr_valor_baixa                                      AS valor_pago,      \n" // 9
                    + "             M.ds_es                                             AS es,              \n" // 10
                    + "             P.ds_nome                                           AS responsavel,     \n" // 11
                    + "             P.id                                                AS id_responsavel,  \n" // 12
                    + "             M.id                                                AS id_movimento,    \n" // 13
                    + "             M.id_lote                                           AS lote,            \n" // 14
                    + "             L.dt_lancamento                                     AS criacao,         \n" // 15
                    + "             M.ds_documento                                      AS boleto,          \n" // 16
                    + "             func_intervalo_dias(M.dt_vencimento,CURRENT_DATE)   AS dias_atraso,     \n" // 17
                    + "             func_multa(M.id)                                    AS multa,           \n" // 18
                    + "             func_juros(M.id)                                    AS juros,           \n" // 19
                    + "             func_correcao(M.id)                                 AS correcao,        \n" // 20
                    + "             PU.ds_nome                                          AS caixa,           \n" // 21
                    + "             M.id_baixa                                          AS lote_baixa,      \n" // 22
                    + "             L.ds_documento                                      AS documento        \n" // 23
                    + "        FROM fin_movimento       AS M                                    \n"
                    + "  INNER JOIN fin_lote            AS L    ON l.id  = M.id_lote            \n"
                    + "  INNER JOIN pes_pessoa          AS P    ON p.id  = M.id_pessoa          \n"
                    + "  INNER JOIN fin_servicos        AS S    ON S.id  = M.id_servicos        \n"
                    + "  INNER JOIN fin_tipo_servico    AS TS   ON TS.id = M.id_tipo_servico    \n"
                    + "   LEFT JOIN fin_baixa           AS B    ON B.id  = M.id_baixa           \n"
                    + "   LEFT JOIN seg_usuario         AS U    ON U.id  = B.id_usuario         \n"
                    + "   LEFT JOIN pes_pessoa          AS PU   ON PU.id = U.id_pessoa          \n";
            List listQuery = new ArrayList<>();
            String queryOrder = "";
            switch (status) {
                case "todos":
                    if (!ids.isEmpty()) {
                        listQuery.add(" M.id_pessoa IN (" + ids + ") AND M.is_ativo = true ");
                        queryOrder = " ORDER BY M.dt_vencimento ASC, P.ds_nome, S.ds_descricao ";
                    }
                    break;
                case "abertos":
                    if (!ids.isEmpty()) {
                        listQuery.add(" M.id_pessoa IN (" + ids + ") AND M.id_baixa IS NULL AND m.is_ativo = true ");
                        queryOrder = " ORDER BY M.dt_vencimento ASC, P.ds_nome, S.ds_descricao ";
                    }
                    break;
                default:
                    if (!ids.isEmpty()) {
                        listQuery.add(" M.id_pessoa IN (" + ids + ") AND m.id_baixa IS NOT NULL AND M.is_ativo = true ");
                        queryOrder = " ORDER BY B.dt_baixa ASC, M.dt_vencimento, P.ds_nome, S.ds_descricao ";
                    }
                    break;
            }
            if (contrato_id != null) {
                if (ids.isEmpty()) {
                    switch (status) {
                        case "todos":
                            listQuery.add(" L.id_contrato = " + contrato_id + " AND M.is_ativo = true  AND L.id_contrato IS NOT NULL");
                            queryOrder = " ORDER BY M.dt_vencimento ASC, P.ds_nome, S.ds_descricao ";
                            break;
                        case "abertos":
                            listQuery.add(" L.id_contrato = " + contrato_id + " AND M.id_baixa IS NULL AND m.is_ativo = true AND L.id_contrato IS NOT NULL");
                            queryOrder = " ORDER BY M.dt_vencimento ASC, P.ds_nome, S.ds_descricao ";
                            break;
                        default:
                            listQuery.add(" L.id_contrato = " + contrato_id + " AND m.id_baixa IS NOT NULL AND M.is_ativo = true AND L.id_contrato IS NOT NULL");
                            queryOrder = " ORDER BY B.dt_baixa ASC, M.dt_vencimento, P.ds_nome, S.ds_descricao ";
                            break;
                    }
                } else {
                    listQuery.add(" L.id_contrato = " + contrato_id);
                }
            }
            if (queryOrder.isEmpty()) {
                queryOrder = " ORDER BY B.dt_baixa ASC, M.dt_vencimento, P.ds_nome, S.ds_descricao ";
            }
            for (int i = 0; i < listQuery.size(); i++) {
                if (i == 0) {
                    queryString += " WHERE ";
                } else {
                    queryString += " AND ";                    
                }
                queryString += listQuery.get(i).toString() + "\n";
            }
            queryString += queryOrder;
            Query query = getEntityManager().createNativeQuery(queryString);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return new ArrayList();
    }

}
