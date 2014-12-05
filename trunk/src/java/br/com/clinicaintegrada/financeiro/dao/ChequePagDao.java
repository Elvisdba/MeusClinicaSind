package br.com.clinicaintegrada.financeiro.dao;

import br.com.clinicaintegrada.financeiro.ChequePag;
import br.com.clinicaintegrada.principal.DB;
import java.util.List;
import javax.persistence.Query;

public class ChequePagDao extends DB {

    public ChequePag findChequePagByNumeroAndPlano5(String numero, int idPlano5) {
        try {
            Query query = getEntityManager().createQuery("SELECT CP FROM ChequePag AS CP WHERE CP.plano5.id = :plano5 AND CP.cheque = :numero");
            query.setParameter("plano5", idPlano5);
            query.setParameter("numero", numero);
            List list = query.getResultList();
            if (!list.isEmpty() && list.size() == 1) {
                return (ChequePag) query.getSingleResult();
            }
        } catch (Exception e) {
        }
        return null;
    }

}
