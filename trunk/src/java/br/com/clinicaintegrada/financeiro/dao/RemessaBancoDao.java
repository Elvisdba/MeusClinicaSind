package br.com.clinicaintegrada.financeiro.dao;

import br.com.clinicaintegrada.principal.DB;
import java.util.List;
import javax.persistence.Query;

public class RemessaBancoDao extends DB {

    /**
     * Retorna o número máximo do lote
     * @param idContaCobranca
     * @return 
     */
    public Integer findByContaCobranca(Integer idContaCobranca) {
        try {
            Query query = getEntityManager().createNativeQuery(
                    "       SELECT max(RB.nr_lote) + 1                                          "
                    + "       FROM fin_remessa_banco    AS RB                                   "
                    + " INNER JOIN fin_movimento        AS M ON (M.id = RB.id_movimento)        "
                    + " INNER JOIN fin_conta_cobranca   AS C ON (M.id_conta_cobranca = C.id)    "
                    + "        AND C.id = " + idContaCobranca);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return (Integer) ((Object) query.getSingleResult());
            }
        } catch (Exception e) {
        }
        return null;
    }
}
