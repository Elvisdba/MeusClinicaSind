package br.com.clinicaintegrada.financeiro.dao;

import br.com.clinicaintegrada.financeiro.Boleto;
import br.com.clinicaintegrada.financeiro.FormaPagamento;
import br.com.clinicaintegrada.financeiro.Movimento;
import br.com.clinicaintegrada.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class MovimentoDao extends DB {

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

}
