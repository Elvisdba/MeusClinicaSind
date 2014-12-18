package br.com.clinicaintegrada.fichamedica.dao;

import br.com.clinicaintegrada.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class AvaliacaoDao extends DB {

    /**
     *
     * @param idGrupoAvaliacao
     * @param idTiposAvaliacao
     * @return
     */
    public boolean exists(Integer idGrupoAvaliacao, Integer idTiposAvaliacao) {
        try {
            Query query = getEntityManager().createQuery("SELECT A FROM Avaliacao AS A WHERE A.grupoAvaliacao.id = :grupoAvaliacao AND A.tiposAvaliacao.id = :tiposAvaliacao");
            query.setParameter("grupoAvaliacao", idGrupoAvaliacao);
            query.setParameter("tiposAvaliacao", idTiposAvaliacao);
            query.setMaxResults(1);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    /**
     *
     * @param idGrupoAvaliacao
     * @return
     */
    public List findAllByGrupo(Integer idGrupoAvaliacao) {
        try {
            Query query = getEntityManager().createQuery("SELECT A FROM Avaliacao AS A WHERE A.grupoAvaliacao.id = :grupoAvaliacao ORDER BY A.id");
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
