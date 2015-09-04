package br.com.clinicaintegrada.questionario.dao;

import br.com.clinicaintegrada.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class QuestaoDao extends DB {
    
    private String order_by = "Q.descricao ASC";

    public List findByRotina(Integer rotina_id) {
        try {
            Query query = getEntityManager().createQuery("SELECT Q FROM Questao AS Q WHERE Q.subgrupo.grupo.questionario.rotina.id = :rotina_id ORDER BY Q.subgrupo.grupo.questionario.id, Q.subgrupo.grupo.id, Q.subgrupo.id, Q.id");
            query.setParameter("rotina_id", rotina_id);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return new ArrayList();
    }

    public List findBySubgrupo(Integer subgrupo_id) {
        try {
            Query query = getEntityManager().createQuery("SELECT Q FROM Questao AS Q WHERE Q.subgrupo.id = :subgrupo_id ORDER BY " + order_by);
            query.setParameter("subgrupo_id", subgrupo_id);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return new ArrayList();
    }

    public String getOrder_by() {
        return order_by;
    }

    public void setOrder_by(String order_by) {
        this.order_by = order_by;
    }
}
