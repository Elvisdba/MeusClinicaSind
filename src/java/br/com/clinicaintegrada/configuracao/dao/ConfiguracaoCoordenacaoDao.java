package br.com.clinicaintegrada.configuracao.dao;

import br.com.clinicaintegrada.configuracao.ConfiguracaoCoordenacao;
import br.com.clinicaintegrada.principal.DB;
import java.util.List;
import javax.persistence.Query;

public class ConfiguracaoCoordenacaoDao extends DB {

    /**
     *
     * @param idCliente
     * @return
     */
    public ConfiguracaoCoordenacao findByCliente(Integer idCliente) {
        try {
            Query query = getEntityManager().createQuery("SELECT CC FROM ConfiguracaoCoordenacao AS CC WHERE CC.cliente.id = :cliente");
            query.setParameter("cliente", idCliente);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return (ConfiguracaoCoordenacao) query.getSingleResult();
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

}
