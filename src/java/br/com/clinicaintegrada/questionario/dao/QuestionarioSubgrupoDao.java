package br.com.clinicaintegrada.questionario.dao;

import br.com.clinicaintegrada.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class QuestionarioSubgrupoDao extends DB {

    private String order_by = "QSG.descricao";

    public List findByGrupo(Integer questionario_grupo_id) {
        try {
            Query query = getEntityManager().createQuery("SELECT QSG FROM QuestionarioSubgrupo AS QSG WHERE QSG.grupo.id = :questionario_grupo_id ORDER BY " + order_by);
            query.setParameter("questionario_grupo_id", questionario_grupo_id);
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
