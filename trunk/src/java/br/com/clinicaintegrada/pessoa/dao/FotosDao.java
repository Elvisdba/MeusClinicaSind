package br.com.clinicaintegrada.pessoa.dao;

import br.com.clinicaintegrada.pessoa.Fotos;
import br.com.clinicaintegrada.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class FotosDao extends DB {

    public List findFotosByContrato(Integer contrato_id) {
        return findFotosByContrato(contrato_id, null);
    }

    public List findFotosByContrato(Integer contrato_id, Integer interval) {
        try {
            Query query;
            if (interval == null) {
                query = getEntityManager().createQuery("SELECT F FROM Fotos AS F WHERE F.contrato.id = :contrato_id ORDER BY F.data DESC");
                query.setParameter("contrato_id", contrato_id);
            } else {
                String queryString = "                                          "
                        + "     SELECT *                                      "
                        + "       FROM pes_fotos AS F                           "
                        + "      WHERE F.id_contrato = " + contrato_id
                        + "        AND F.dt_data <= (current_date + INTERVAL '" + interval + " days')";
                query = getEntityManager().createNativeQuery(queryString, Fotos.class);
            }
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return new ArrayList();
    }

    public Integer next() {
        try {
            String queryString = "SELECT max(id) FROM pes_fotos AS F";
            Query query = getEntityManager().createNativeQuery(queryString);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                Integer max_id = Integer.parseInt(((List) list.get(0)).get(0).toString());
                return max_id;
            }
        } catch (Exception e) {
            return null;
        }
        return null;

    }

}
