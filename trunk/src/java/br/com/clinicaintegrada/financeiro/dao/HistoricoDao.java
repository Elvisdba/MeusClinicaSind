package br.com.clinicaintegrada.financeiro.dao;

import br.com.clinicaintegrada.financeiro.Historico;
import br.com.clinicaintegrada.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class HistoricoDao extends DB {

    public Historico findHistoricoByContaCobrancaAndMovimento(int idContaCobranca, int idMovimento) {
        try {
            String queryString = ""
                    + "      SELECT H.*                                                             "
                    + "        FROM fin_historico AS H                                              "
                    + "  INNER JOIN fin_movimento   AS M    ON M.id = H.id_movimento                "
                    + "  INNER JOIN fin_boleto      AS B    ON M.nr_ctr_boleto = B.nr_ctr_boleto    "
                    + "       WHERE M.id = " + idMovimento
                    + "         AND B.id_conta_cobranca = " + idContaCobranca + " "
                    + "       LIMIT 1";
            Query query = getEntityManager().createNativeQuery(queryString, Historico.class);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return (Historico) list.get(0);
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public List listHistoricoAgrupadoByAcordo(int idAcordo) {
        try {
            Query query = getEntityManager().createQuery(""
                    + "    SELECT H.historico                       "
                    + "      FROM Historico AS H                    "
                    + "     WHERE H.movimento.id IN (               "
                    + "           SELECT M.id FROM Movimento AS M   "
                    + "            WHERE M.acordo.id = :idAcordo    "
                    + "              AND M.ativo = true             "
                    + "     )                                       "
                    + " GROUP BY H.historico                        "
                    + "");
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

    public Historico findHistoricoByMovimento(int idMovimento) {
        try {
            Query query = getEntityManager().createQuery("SELECT H FROM Historico AS H WHERE H.movimento.id = :idMovimento");
            query.setParameter("idMovimento", idMovimento);
            List list = query.getResultList();
            if (list.isEmpty()) {
                return (Historico) query.getSingleResult();
            }
        } catch (Exception e) {
        }
        return null;
    }

    public Historico findHistoricoBaixado(String nrBoleto, int idServico) {
        try {
            Query query = getEntityManager().createQuery(""
                    + "SELECT H                                                 "
                    + "  FROM Historico AS H                                    "
                    + " WHERE H.movimento.documento = :nrBoleto                 "
                    + "   AND H.movimento.servicos.id = :idServico              "
                    + "   AND H.movimento.tipoServico.id = 4                    ");
            query.setParameter("nrBoleto", nrBoleto);
            query.setParameter("idServico", idServico);
            List list = query.getResultList();
            if (!list.isEmpty() && list.size() == 1) {
                return (Historico) query.getSingleResult();
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}
