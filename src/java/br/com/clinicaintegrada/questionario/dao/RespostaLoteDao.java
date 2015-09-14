package br.com.clinicaintegrada.questionario.dao;

import br.com.clinicaintegrada.principal.DB;
import br.com.clinicaintegrada.questionario.RespostaLote;
import br.com.clinicaintegrada.utils.QueryString;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Query;
import javax.persistence.TemporalType;

public class RespostaLoteDao extends DB {

    public RespostaLote findBy(Integer questionario_id, Integer pessoa_id, Date lancamento) {
        try {
            Query query = getEntityManager().createQuery("SELECT RL FROM RespostaLote AS RL WHERE RL.questionario.id = :questionario_id AND RL.pessoa.id = :pessoa_id AND RL.lancamento = :lancamento");
            query.setParameter("questionario_id", questionario_id);
            query.setParameter("pessoa_id", pessoa_id);
            query.setParameter("lancamento", lancamento, TemporalType.DATE);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return (RespostaLote) list.get(0);
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public List findBy(String by, Integer type, String[] description) {
        try {
            String query_string = "";
            query_string = ""
                    + "     SELECT RL.*                                         \n"
                    + "       FROM que_resposta_lote AS RL                      \n"
                    + " INNER JOIN pes_pessoa AS P ON P.id = RL.id_pessoa       \n"
                    + "      WHERE ";
            switch (by) {
                case "nome":
                    query_string += " TRIM(UPPER(FUNC_TRANSLATE(P.ds_nome))) " + QueryString.typeSearch(description[0], type);
                    break;
                case "documento":
                    query_string += " TRIM(P.ds_documento) " + QueryString.typeSearch(description[0], type);
                    break;
                case "lancamento":
                    if (!description[1].isEmpty()) {
                        query_string += " dt_lancamento = " + description[0];
                    } else {
                        query_string += " dt_lancamento BETWEEN " + description[0] + " AND " + description[1];
                    }
                    break;
            }
            Query query = getEntityManager().createNativeQuery(query_string, RespostaLote.class);
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
