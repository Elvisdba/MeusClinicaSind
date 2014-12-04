package br.com.clinicaintegrada.financeiro.dao;

import br.com.clinicaintegrada.financeiro.Boleto;
import br.com.clinicaintegrada.principal.DB;
import java.util.List;
import java.util.Vector;
import javax.persistence.Query;

public class BoletoDao extends DB {

    public Boleto findBoletosByNrCtrBoleto(String nrCtrBoleto) {
        try {
            Query qry = getEntityManager().createQuery(
                    "  SELECT B                             "
                    + "  FROM Boleto AS B                   "
                    + " WHERE B.nrCtrBoleto = " + nrCtrBoleto);
            List list = qry.getResultList();
            if (!list.isEmpty()) {
                return (Boleto) qry.getSingleResult();
            }
        } catch (Exception e) {
        }
        return null;
    }

    public int saveBoletoNativeQuery(int idContaCobranca) {
        try {
            String queryString = "INSERT INTO fin_boleto (id_conta_cobranca, is_ativo) VALUES (" + idContaCobranca + ", true)";
            Query query = getEntityManager().createNativeQuery(queryString);

            getEntityManager().getTransaction().begin();
            query.executeUpdate();
            getEntityManager().getTransaction().commit();

            queryString = "SELECT MAX(id) FROM fin_boleto";
            query = getEntityManager().createNativeQuery(queryString);
            List list = (List) query.getResultList();
            return (Integer) ((List) list.get(0)).get(0);
        } catch (Exception e) {
            return -1;
        }
    }

}
