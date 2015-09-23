package br.com.clinicaintegrada.coordenacao.dao;

import br.com.clinicaintegrada.coordenacao.AteFamilia;
import br.com.clinicaintegrada.principal.DB;
import br.com.clinicaintegrada.utils.Dao;
import java.util.List;
import javax.persistence.Query;

public class AteFamiliaDao extends DB {

    public List findByCliente(Integer cliente_id) {
        try {
            Query query = getEntityManager().createQuery("SELECT AF FROM AteFamilia AS AF WHERE AF.equipe.cliente.id = :cliente_id ORDER BY AF.ordem");
            query.setParameter("cliente_id", cliente_id);
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public AteFamilia maxOrdemByCliente(Integer cliente_id) {
        try {
            Query query = getEntityManager().createQuery("SELECT AF FROM AteFamilia AS AF WHERE AF.equipe.cliente.id = :cliente_id ORDER BY AF.ordem DESC");
            query.setParameter("cliente_id", cliente_id);
            query.setMaxResults(1);
            return (AteFamilia) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public AteFamilia minOrdemByCliente(Integer cliente_id) {
        try {
            Query query = getEntityManager().createQuery("SELECT AF FROM AteFamilia AS AF WHERE AF.equipe.cliente.id = :cliente_id ORDER BY AF.ordem ASC");
            query.setParameter("cliente_id", cliente_id);
            query.setMaxResults(1);
            return (AteFamilia) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public AteFamilia nextOrdemByCliente(Integer current_orderm, Integer cliente_id) {
        try {
            String queryString = ""
                    + "     SELECT F.id,                                    \n"
                    + "            F.nr_ordem                               \n"
                    + "       FROM ate_familia AS F                         \n"
                    + " INNER JOIN pes_equipe AS PE ON PE.id = F.id_equipe  \n"
                    + "      WHERE PE.id_cliente = " + cliente_id + "       \n"
                    + "   GROUP BY F.id,                                    \n"
                    + "            F.nr_ordem                               \n"
                    + "     HAVING F.nr_ordem > " + current_orderm + "      \n"
                    + "   ORDER BY F.nr_ordem ASC                           ";

            Query query = getEntityManager().createNativeQuery(queryString);
            query.setMaxResults(1);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return (AteFamilia) new Dao().find(new AteFamilia(), ((List) list.get(0)).get(0));
            } else {
                return minOrdemByCliente(cliente_id);

            }
        } catch (Exception e) {
            return null;
        }
    }

    public AteFamilia findByPonteiro(Integer cliente_id) {
        try {
            Query query = getEntityManager().createQuery("SELECT AF FROM AteFamilia AS AF WHERE AF.ponteiro = true");
            return (AteFamilia) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}
