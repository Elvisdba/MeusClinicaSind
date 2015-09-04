package br.com.clinicaintegrada.questionario.dao;

import br.com.clinicaintegrada.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class QuestionarioDao extends DB {

    public List findByRotina(Integer rotina_id) {
        try {
            Query query = getEntityManager().createQuery("SELECT Q FROM Questionario AS Q WHERE Q.rotina.id = :rotina_id ORDER BY Q.descricao ASC");
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
}
