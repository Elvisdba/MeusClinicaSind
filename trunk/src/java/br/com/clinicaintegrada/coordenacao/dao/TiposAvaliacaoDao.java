package br.com.clinicaintegrada.coordenacao.dao;

import br.com.clinicaintegrada.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class TiposAvaliacaoDao extends DB {

    public List findAllByGrupoForAvaliacao(Integer idGrupoAvaliacao) {
        try {
            Query query = getEntityManager().createQuery("SELECT TA FROM TiposAvaliacao AS TA WHERE TA.id NOT IN (SELECT A.tiposAvaliacao.id FROM Avaliacao AS A WHERE A.grupoAvaliacao.id = :grupoAvaliacao) ");
            query.setParameter("grupoAvaliacao", idGrupoAvaliacao);
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
