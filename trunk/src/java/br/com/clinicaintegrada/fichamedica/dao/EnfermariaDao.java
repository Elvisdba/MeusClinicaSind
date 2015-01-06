package br.com.clinicaintegrada.fichamedica.dao;

import br.com.clinicaintegrada.fichamedica.Enfermaria;
import br.com.clinicaintegrada.principal.DB;
import java.util.List;
import javax.persistence.Query;

public class EnfermariaDao extends DB {

    /**
     *
     * @param idAgendamento
     * @return
     */
    public Enfermaria findByAgendamento(Integer idAgendamento) {
        try {
            Query query = getEntityManager().createQuery("SELECT E FROM Enfermaria AS E WHERE E.agendamento.id = :agendamento");
            query.setParameter("agendamento", idAgendamento);
            List list = query.getResultList();
            if (!list.isEmpty() && list.size() == 1) {
                return (Enfermaria) query.getSingleResult();
            }
        } catch (Exception e) {
        }
        return null;
    }

}
