package br.com.clinicaintegrada.administrativo.dao;

import br.com.clinicaintegrada.administrativo.Taxas;
import br.com.clinicaintegrada.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class TaxasDao extends DB {

    public boolean existeTaxa(Taxas t) {
        try {
            String queryString = ""
                    + "   SELECT * "
                    + "     FROM ctr_taxas "
                    + "    WHERE id_cliente = " + t.getCliente().getId()
                    + "      AND id_servicos = " + t.getServicos().getId()
                    + "    LIMIT 1";
            Query query = getEntityManager().createNativeQuery(queryString);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public List pesquisaTodasTaxasPorCliente(int cliente) {
        try {
            Query query = getEntityManager().createQuery("SELECT T FROM Taxas AS T WHERE T.cliente.id = :cliente  ORDER BY T.servicos.descricao ASC");
            query.setParameter("cliente", cliente);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return new ArrayList();
    }

    public List findServicos(int cliente) {
        try {
            Query query = getEntityManager().createQuery("SELECT S FROM Servicos AS S WHERE S.id NOT IN(SELECT T.servicos.id FROM Taxas AS T WHERE T.cliente.id = :tCliente) AND S.cliente.id = :cliente ORDER BY S.descricao ASC ");
            query.setParameter("cliente", cliente);
            query.setParameter("tCliente", cliente);
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
