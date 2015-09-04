package br.com.clinicaintegrada.questionario.dao;

import br.com.clinicaintegrada.principal.DB;
import br.com.clinicaintegrada.questionario.RespostaFixa;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class RespostaFixaDao extends DB {
    
    public List findByQuestao(Integer questao_id) {
        try {
            Query query = getEntityManager().createQuery("SELECT RF FROM RespostaFixa AS RF WHERE RF.questao.id = :questao_id ORDER BY RF.descricao ASC");
            query.setParameter("questao_id", questao_id);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return new ArrayList();
    }
    
    public RespostaFixa findByQuestao(Integer questao_id, Boolean single_result) {
        try {
            Query query = getEntityManager().createQuery("SELECT RF FROM RespostaFixa AS RF WHERE RF.questao.id = :questao_id ORDER BY RF.descricao ASC");
            query.setParameter("questao_id", questao_id);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return (RespostaFixa) list.get(0);
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}
