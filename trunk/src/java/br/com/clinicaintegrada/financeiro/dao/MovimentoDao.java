package br.com.clinicaintegrada.financeiro.dao;

import br.com.clinicaintegrada.financeiro.Movimento;
import br.com.clinicaintegrada.principal.DB;
import br.com.clinicaintegrada.utils.DataHoje;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Query;
import oracle.toplink.essentials.exceptions.EJBQLException;

public class MovimentoDao extends DB {

    private String order;

    public MovimentoDao() {
        order = "";
    }

    public List findMovimentosByLote(int idLote) {
        return findMovimentos(idLote, -1, 0, true);
    }

    public List findMovimentosByLoteServicos(int idLote, int idServicos) {
        return findMovimentos(idLote, idServicos, 0, true);
    }

    public List findMovimentosByLoteServicosBaixa(int idLote, int idServicos, int tBaixa) {
        return findMovimentos(idLote, idServicos, tBaixa, true);
    }

    /**
     *
     * @param idLote
     * @param idServico
     * @param tBaixa = 0 => todos; 1 => não baixados; 2 => baixados
     * @param ativo
     * @return
     */
    public List findMovimentos(int idLote, int idServico, int tBaixa, boolean ativo) {
        try {
            String queryString = " SELECT M FROM Movimento AS M WHERE M.ativo = " + ativo + " AND M.lote.id = " + idLote;
            if (idServico != -1) {
                queryString += " AND M.servicos.id = " + idServico;
            }
            if (tBaixa == 1) {
                queryString += " AND M.baixa IS NULL ";
            } else if (tBaixa == 2) {
                queryString += " AND M.baixa IS NOT NULL ";
            }
            if (!order.isEmpty()) {
                queryString += " ORDER BY " + order;
            }
            Query query = getEntityManager().createQuery(queryString);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return new ArrayList();
    }

    /**
     *
     * @param idBaixa
     * @return
     */
    public List<Movimento> listMovimentosByBaixaOrderByBaixa(int idBaixa) {
        try {
            String textoQuery = " SELECT M FROM Movimento AS M WHERE M.baixa.id = :idBaixa ORDER BY M.pessoa.id ASC";
            Query query = getEntityManager().createQuery(textoQuery);
            query.setParameter("idBaixa", idBaixa);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return new ArrayList();
    }

    /**
     *
     * @param idMovimento
     * @return
     */
    public List<List> findAcrescimoByMovimento(int idMovimento) {
        try {
            String queryString = ""
                    + "SELECT M.nr_valor    AS valor,                                                           "
                    + "       M.nr_multa    AS multa,                                                           "
                    + "       M.nr_juros    AS juros,                                                           "
                    + "       M.nr_correcao AS correcao,                                                        "
                    + "       M.nr_desconto AS desconto,                                                        "
                    + "      (M.nr_valor + M.nr_multa + M.nr_juros + M.nr_correcao - M.nr_desconto) vlrpagar    "
                    + "  FROM fin_movimento AS M "
                    + " WHERE M.id = " + idMovimento;
            Query query = getEntityManager().createNativeQuery(queryString);
            return (List) query.getResultList();
        } catch (Exception e) {
            return new ArrayList();
        }
    }

    /**
     *
     * @param idAcordo
     * @return
     */
    public List<Movimento> findMovimentoByAcordoAberto(int idAcordo) {
        try {
            String queryString
                    = "  SELECT M                       "
                    + "    FROM Movimento AS M          "
                    + "   WHERE M.acordo.id = :idAcordo "
                    + "     AND M.ativo = true          "
                    + "ORDER BY M.dtVencimento ASC      ";
            Query query = getEntityManager().createQuery(queryString);
            query.setParameter("idAcordo", idAcordo);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return new ArrayList();
    }

    public List<Movimento> findAllAcordos(int idAcordo) {
        try {
            Query query = getEntityManager().createQuery(
                    "    SELECT M                                               "
                    + "    FROM Movimento AS M                                  "
                    + "   WHERE m.acordo.id = :acordo                           "
                    + "ORDER BY M.vencimento ASC                                "
            );
            query.setParameter("acordo", idAcordo);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return new ArrayList<>();
        }
        return new ArrayList<>();
    }

    public List<Movimento> findByDocumento(String numero, Date vencimento, int idContaCobranca) {
        String queryString = ""
                + "  SELECT M                                   "
                + "    FROM Movimento   AS M,                   "
                + "         Boleto      AS B                    "
                + "   WHERE M.nrCtrBoleto = B.nrCtrBoleto       "
                + "     AND M.baixa IS NULL                     "
                + "     AND M.ativo = true                      "
                + "     AND B.boletoComposto = '" + numero + "' ";
        if (DataHoje.converteData(vencimento).equals("11/11/1111")) {
            queryString += " AND B.contaCobranca.id = " + idContaCobranca;
        } else {
            queryString += " AND B.contaCobranca.id = " + idContaCobranca
                    + " AND M.vencimento = '" + DataHoje.converteData(vencimento) + "'";
        }
        try {
            Query query = getEntityManager().createQuery(queryString);
            return query.getResultList();
        } catch (EJBQLException e) {

        }
        return new ArrayList();

    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}
