package br.com.clinicaintegrada.financeiro.dao;

import br.com.clinicaintegrada.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class IndiceMensalDao extends DB {

    public List existIndiceMensal(int idIndice, int ano, int mes) {
        try {
            Query query = getEntityManager().createQuery("SELECT IM FROM IndiceMensal IM WHERE IM.indice.id = :idIndice AND IM.ano = :ano AND IM.mes.id = :mes");
            query.setParameter("idIndice", idIndice);
            query.setParameter("ano", ano);
            query.setParameter("mes", mes);
            List list = query.getResultList();
            if (list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    public List findIndiceMensalByIndice(int idIndice) {
        try {
            Query query = getEntityManager().createQuery("SELECT IM FROM IndiceMensal AS IM WHERE IM.indice.id = :idIndice ORDER BY IM.ano DESC, IM.mes.descricao ASC");
            query.setParameter("idIndice", idIndice);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }
}
