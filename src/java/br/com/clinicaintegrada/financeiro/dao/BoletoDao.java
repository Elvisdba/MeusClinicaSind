package br.com.clinicaintegrada.financeiro.dao;

import br.com.clinicaintegrada.financeiro.Boleto;
import br.com.clinicaintegrada.principal.DB;
import java.util.List;
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

}
