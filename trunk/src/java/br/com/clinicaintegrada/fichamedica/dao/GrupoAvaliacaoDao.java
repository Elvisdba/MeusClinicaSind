package br.com.clinicaintegrada.fichamedica.dao;

import br.com.clinicaintegrada.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class GrupoAvaliacaoDao extends DB {

    /**
     *
     * @param idFuncaoEquipe
     * @return
     */
    public List findAllAvaliacaoByFuncaoEquipe(Integer idFuncaoEquipe) {
        try {
            Query query = getEntityManager().createQuery("SELECT AE FROM AvaliacaoEquipe AS AE WHERE AE.funcaoEquipe.id = :funcaoEquipe GROUP BY AE.avaliacao.grupoAvaliacao");
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
