package br.com.clinicaintegrada.fichamedica.dao;

import br.com.clinicaintegrada.fichamedica.AtendimentoAvaliacao;
import br.com.clinicaintegrada.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class AtendimentoAvaliacaoDao extends DB {

    /**
     *
     * @param idAtendimento
     * @param idAvaliacao
     * @return
     */
    public boolean exists(Integer idAtendimento, Integer idAvaliacao) {
        try {
            Query query = getEntityManager().createQuery("SELECT AA FROM AtendimentoAvaliacao AS AA WHERE AA.atendimento.id = :atendimento AND AA.avaliacao.id = :avaliacao");
            query.setParameter("atendimento", idAtendimento);
            query.setParameter("avaliacao", idAvaliacao);
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
     * @param idAtendimento
     * @param idAvaliacao
     * @return
     */
    public AtendimentoAvaliacao findByAtendimentoAndAvaliacao(Integer idAtendimento, Integer idAvaliacao) {
        try {
            Query query = getEntityManager().createQuery("SELECT AA FROM AtendimentoAvaliacao AS AA WHERE AA.atendimento.id = :atendimento AND AA.avaliacao.id = :avaliacao");
            query.setParameter("atendimento", idAtendimento);
            query.setParameter("avaliacao", idAvaliacao);
            List list = query.getResultList();
            if (!list.isEmpty() && list.size() == 1) {
                return (AtendimentoAvaliacao) query.getSingleResult();
            }
        } catch (Exception e) {
        }
        return null;
    }

    /**
     *
     * @param idAtendimento
     * @return
     */
    public List<AtendimentoAvaliacao> listByAtendimento(Integer idAtendimento) {
        try {
            Query query = getEntityManager().createQuery("SELECT AA FROM AtendimentoAvaliacao AS AA WHERE AA.atendimento.id = :atendimento ORDER BY AA.id ASC");
            query.setParameter("atendimento", idAtendimento);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }
}
