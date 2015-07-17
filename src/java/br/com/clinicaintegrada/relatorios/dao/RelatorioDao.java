package br.com.clinicaintegrada.relatorios.dao;

import br.com.clinicaintegrada.principal.DB;
import br.com.clinicaintegrada.relatorios.Relatorios;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class RelatorioDao extends DB {

    public List findByRotina(Integer rotina_id) {
        try {
            Query query = getEntityManager().createQuery("SELECT R FROM Relatorios AS R WHERE R.rotina.id = :rotina_id ORDER BY R.nome");
            query.setParameter("rotina_id", rotina_id);
            return query.getResultList();
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    public Relatorios findByJasper(String relatorio_jasper) {
        try {
            Query query = getEntityManager().createQuery("SELECT R FROM Relatorios AS R WHERE R.jasper = :relatorio_jasper");
            query.setParameter("relatorio_jasper", relatorio_jasper);
            return (Relatorios) query.getSingleResult();
        } catch (Exception e) {
        }
        return null;
    }
}
