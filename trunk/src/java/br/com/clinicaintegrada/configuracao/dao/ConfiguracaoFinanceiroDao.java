package br.com.clinicaintegrada.configuracao.dao;

import br.com.clinicaintegrada.principal.DB;
import br.com.clinicaintegrada.configuracao.ConfiguracaoFinanceiro;
import java.util.List;
import javax.persistence.Query;

public class ConfiguracaoFinanceiroDao extends DB {

    /**
     *
     * @param idCliente
     * @return
     */
    public ConfiguracaoFinanceiro findByCliente(Integer idCliente) {
        try {
            Query query = getEntityManager().createQuery("SELECT CF FROM ConfiguracaoFinanceiro AS CF WHERE CF.cliente.id = :cliente");
            query.setParameter("cliente", idCliente);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return (ConfiguracaoFinanceiro) query.getSingleResult();
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

}
