package br.com.clinicaintegrada.coordenacao.dao;

import br.com.clinicaintegrada.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class AvaliacaoEquipeDao extends DB {

    /**
     *
     * @param idAvaliacao
     * @param idFuncaoEquipe
     * @return
     */
    public boolean exists(Integer idAvaliacao, Integer idFuncaoEquipe) {
        try {
            Query query = getEntityManager().createQuery("SELECT AE FROM AvaliacaoEquipe AS AE WHERE AE.funcaoEquipe = :funcaoEquipe AND AE.avaliacao = :avaliacao");
            query.setParameter("avaliacao", idAvaliacao);
            query.setParameter("funcaoEquipe", idFuncaoEquipe);
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
     * @param idAvaliacao
     * @return
     */
    public List findAllByAvaliacao(Integer idAvaliacao) {
        try {
            Query query = getEntityManager().createQuery("SELECT AE FROM AvaliacaoEquipe AS AE WHERE AE.avaliacao.id = :avaliacao");
            query.setParameter("avaliacao", idAvaliacao);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return new ArrayList();
    }

    /**
     *
     * @param idFuncaoEquipe
     * @return
     */
    public List findAllByFuncaoEquipe(Integer idFuncaoEquipe) {
        try {
            Query query = getEntityManager().createQuery("SELECT AE FROM AvaliacaoEquipe AS AE WHERE AE.funcaoEquipe.id = :funcaoEquipe");
            query.setParameter("funcaoEquipe", idFuncaoEquipe);
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
