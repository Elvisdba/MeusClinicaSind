package br.com.clinicaintegrada.pessoa.dao;

import br.com.clinicaintegrada.pessoa.Fotos;
import br.com.clinicaintegrada.principal.DB;
import br.com.clinicaintegrada.utils.DataHoje;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

public class FotosDao extends DB {

    public List findFotosByContrato(Integer contrato_id) {
        return findFotosByContrato(contrato_id, null);
    }

    public List findFotosByContrato(Integer contrato_id, Integer interval) {
        return findFotosByContratoData(contrato_id, null, interval);
    }

    public List findFotosByContratoData(Integer contrato_id, String data) {
        return findFotosByContratoData(contrato_id, data, null);
    }

    public List findFotosByContratoData(Integer contrato_id, String data, Integer interval) {
        try {
            Query query;
            if (interval == null) {
                if (contrato_id != null && data == null) {
                    query = getEntityManager().createQuery("SELECT F FROM Fotos AS F WHERE F.contrato.id = :contrato_id ORDER BY F.data DESC");
                    query.setParameter("contrato_id", contrato_id);
                } else if (contrato_id != null && data != null) {
                    query = getEntityManager().createQuery("SELECT F FROM Fotos AS F WHERE F.contrato.id = :contrato_id AND F.data = :data ORDER BY F.data DESC");
                    query.setParameter("contrato_id", contrato_id);
                    query.setParameter("data", DataHoje.converte(data), TemporalType.DATE);
                } else {
                    query = getEntityManager().createQuery("SELECT F FROM Fotos AS F WHERE F.data = :data ORDER BY F.data DESC");
                    query.setParameter("data", data);
                }
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

    public Integer currval() {
        try {
            String queryString = "SELECT currval('pes_fotos_id_seq')";
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
