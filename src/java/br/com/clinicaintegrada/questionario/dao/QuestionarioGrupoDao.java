package br.com.clinicaintegrada.questionario.dao;

import br.com.clinicaintegrada.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class QuestionarioGrupoDao extends DB {

    private String order_by = "QG.descricao ASC";

    public List findByQuestionario(Integer questionario_id) {
        try {
            Query query = getEntityManager().createQuery("SELECT QG FROM QuestionarioGrupo AS QG WHERE QG.questionario.id = :questionario_id ORDER BY " + order_by);
            query.setParameter("questionario_id", questionario_id);
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
