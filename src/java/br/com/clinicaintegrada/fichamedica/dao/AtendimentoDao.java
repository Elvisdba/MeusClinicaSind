package br.com.clinicaintegrada.fichamedica.dao;

import br.com.clinicaintegrada.fichamedica.Atendimento;
import br.com.clinicaintegrada.principal.DB;
import java.util.List;
import javax.persistence.Query;

public class AtendimentoDao extends DB {

    /**
     *
     * @param idAgendamento
     * @return
     */
    public boolean exists(Integer idAgendamento) {
        try {
            Query query = getEntityManager().createQuery("SELECT A FROM Atendimento AS A WHERE A.agendamento.id = :agendamento");
            query.setParameter("agendamento", idAgendamento);
            List list = query.getResultList();
            query.setMaxResults(1);
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
     * @param idAgendamento
     * @return
     */
    public Atendimento findByAgendamento(Integer idAgendamento) {
        try {
            Query query = getEntityManager().createQuery("SELECT A FROM Atendimento AS A WHERE A.agendamento.id = :agendamento");
            query.setParameter("agendamento", idAgendamento);
            List list = query.getResultList();
            if (!list.isEmpty() && list.size() == 1) {
                return (Atendimento) query.getSingleResult();
            }
        } catch (Exception e) {
        }
        return null;
    }

}
