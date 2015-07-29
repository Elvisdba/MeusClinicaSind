package br.com.clinicaintegrada.seguranca.dao;

import br.com.clinicaintegrada.principal.DB;
import br.com.clinicaintegrada.seguranca.FilialRotina;
import java.util.List;
import javax.persistence.Query;

public class FilialRotinaDao extends DB {

    public Boolean exist(Integer filial_id, Integer rotina_id) {
        return find(filial_id, rotina_id) != null;
    }

    public FilialRotina find(Integer filial_id, Integer rotina_id) {
        try {
            Query query = getEntityManager().createQuery("SELECT FR FROM FilialRotina AS FR WHERE FR.filial.id = :filial_id AND FR.rotina.id = :rotina_id");
            query.setParameter("filial_id", filial_id);
            query.setParameter("rotina_id", rotina_id);
            List list = query.getResultList();
            if (!list.isEmpty() && list.size() == 1) {
                return (FilialRotina) query.getSingleResult();
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

}
