package br.com.clinicaintegrada.financeiro.dao;

import br.com.clinicaintegrada.financeiro.Lote;
import br.com.clinicaintegrada.principal.DB;
import java.util.List;
import javax.persistence.Query;

public class LoteDao extends DB {

    public Lote findLoteByContrato(int idContrato) {
        try {
            Query query = getEntityManager().createQuery("SELECT L FROM Lote As L WHERE L.contrato.id = :idContrato");
            query.setParameter("idContrato", idContrato);
            List list = query.getResultList();
            if (!list.isEmpty() && list.size() == 1) {
                return (Lote) query.getSingleResult();
            }
        } catch (Exception e) {
        }
        return null;
    }

}
