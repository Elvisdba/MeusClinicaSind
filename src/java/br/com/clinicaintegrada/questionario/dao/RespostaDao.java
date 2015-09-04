package br.com.clinicaintegrada.questionario.dao;

import br.com.clinicaintegrada.principal.DB;
import br.com.clinicaintegrada.questionario.Resposta;
import br.com.clinicaintegrada.utils.Dao;
import java.util.List;
import javax.persistence.Query;

public class RespostaDao extends DB {

    public Resposta findByPessoaAndRespostaFixa(Integer pessoa_id, Integer resposta_fixa_id) {
        try {
            Query query = getEntityManager().createQuery("SELECT R FROM Resposta AS R WHERE R.pessoa.id = :pessoa_id AND R.respostaFixa.id = :resposta_fixa_id");
            query.setParameter("pessoa_id", pessoa_id);
            query.setParameter("resposta_fixa_id", resposta_fixa_id);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                if (list.size() == 1) {
                    return (Resposta) list.get(0);
                } else {
                    for (int i = 0; i < list.size(); i++) {
                        new Dao().delete(list.get(i), true);
                    }
                }
            }
        } catch (Exception e) {
            return new Resposta();
        }
        return new Resposta();
    }

    public Resposta findByQuestao(Integer pessoa_id, Integer questao_id) {
        try {
            Query query = getEntityManager().createQuery("SELECT R FROM Resposta AS R WHERE R.pessoa.id = :pessoa_id AND R.respostaFixa.questao.id = :questao_id");
            query.setParameter("pessoa_id", pessoa_id);
            query.setParameter("questao_id", questao_id);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                if (list.size() == 1) {
                    return (Resposta) list.get(0);
                } else {
                    for (int i = 0; i < list.size(); i++) {
                        new Dao().delete(list.get(i), true);
                    }
                }
            }
        } catch (Exception e) {
            return new Resposta();
        }
        return new Resposta();
    }
}
