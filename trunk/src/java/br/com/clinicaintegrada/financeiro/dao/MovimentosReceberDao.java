package br.com.clinicaintegrada.financeiro.dao;

import br.com.clinicaintegrada.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class MovimentosReceberDao extends DB {

    public List listMovimentos(String ids, String por_status) {
        try {
            if (ids.isEmpty()) {
                return new ArrayList();
            }
            String textqry = " "
                    + "      SELECT S.ds_descricao                                      AS servico,         " // 0
                    + "             TS.ds_descricao                                     AS tipo,            " // 1
                    + "             M.ds_referencia,                                                        " // 2
                    + "             M.dt_vencimento AS vencimento,                                          " // 3
                    + "             func_valor(M.id) AS valor,                                              " // 4
                    + "             func_multa(M.id) + func_juros(M.id)+func_correcao(M.id) as acrescimo,   " // 5
                    + "             M.nr_desconto as desconto,                                              " // 6
                    + "             M.nr_valor + func_multa(M.id)+func_juros(M.id) + func_correcao(M.id) as vl_calculado, " // 7
                    + "             B.dt_baixa,                                                             " // 8
                    + "             nr_valor_baixa                                      AS valor_pago,      " // 9
                    + "             M.ds_es                                             AS es,              " // 10
                    + "             P.ds_nome                                           AS responsavel,     " // 11
                    + "             P.id                                                AS id_responsavel,  " // 12
                    + "             M.id                                                AS id_movimento,    " // 13
                    + "             M.id_lote                                           AS lote,            " // 14
                    + "             L.dt_lancamento                                     AS criacao,         " // 15
                    + "             M.ds_documento                                      AS boleto,          " // 16
                    + "             func_intervalo_dias(M.dt_vencimento,CURRENT_DATE)   AS dias_atraso,     " // 17
                    + "             func_multa(M.id)                                    AS multa,           " // 18
                    + "             func_juros(M.id)                                    AS juros,           " // 19
                    + "             func_correcao(M.id)                                 AS correcao,        " // 20
                    + "             PU.ds_nome                                          AS caixa,           " // 21
                    + "             M.id_baixa                                          AS lote_baixa,      " // 22
                    + "             L.ds_documento                                      AS documento        " // 23
                    + "        FROM fin_movimento       AS M                                    "
                    + "  INNER JOIN fin_lote            AS L    ON l.id  = M.id_lote            "
                    + "  INNER JOIN pes_pessoa          AS P    ON p.id  = M.id_pessoa          "
                    + "  INNER JOIN fin_servicos        AS S    ON S.id  = M.id_servicos        "
                    + "  INNER JOIN fin_tipo_servico    AS TS   ON TS.id = M.id_tipo_servico    "
                    + "   LEFT JOIN fin_baixa           AS B    ON B.id  = M.id_baixa           "
                    + "   LEFT JOIN seg_usuario         AS U    ON U.id  = B.id_usuario         "
                    + "   LEFT JOIN pes_pessoa          AS PU   ON PU.id = U.id_pessoa          ";
            String order_by = "";
            String where = "";
            String ands = "";

            switch (por_status) {
                case "todos":
                    ands = where + " WHERE M.id_pessoa IN (" + ids + ") AND M.is_ativo = true ";
                    order_by = " ORDER BY M.dt_vencimento ASC, P.ds_nome, S.ds_descricao ";
                    break;
                case "abertos":
                    ands = where + " WHERE M.id_pessoa IN (" + ids + ") AND M.id_baixa IS NULL AND m.is_ativo = true ";
                    order_by = " ORDER BY M.dt_vencimento ASC, P.ds_nome, S.ds_descricao ";
                    break;
                default:
                    ands = where + " WHERE M.id_pessoa IN (" + ids + ") AND m.id_baixa IS NOT NULL AND M.is_ativo = true ";
                    order_by = " ORDER BY B.dt_baixa ASC, M.dt_vencimento, P.ds_nome, S.ds_descricao ";
                    break;
            }

            textqry += ands + order_by;
            Query qry = getEntityManager().createNativeQuery(textqry);

            return qry.getResultList();
        } catch (Exception e) {
            e.getMessage();
        }
        return new ArrayList();
    }

}