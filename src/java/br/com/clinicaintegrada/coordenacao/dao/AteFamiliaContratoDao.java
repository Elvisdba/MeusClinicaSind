package br.com.clinicaintegrada.coordenacao.dao;

import br.com.clinicaintegrada.coordenacao.AteFamiliaContrato;
import br.com.clinicaintegrada.principal.DB;
import java.util.List;
import javax.persistence.Query;

public class AteFamiliaContratoDao extends DB {

    public AteFamiliaContrato findByContrato(Integer contrato_id) {
        try {
            Query query = getEntityManager().createQuery("SELECT AFC FROM AteFamiliaContrato AS AFC WHERE AFC.contrato.id = :contrato_id");
            query.setParameter("contrato_id", contrato_id);
            return (AteFamiliaContrato) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public List findByCliente(Integer cliente_id) {
        try {
            Query query = getEntityManager().createQuery("SELECT AFC FROM AteFamiliaContrato AS AFC WHERE AFC.contrato.cliente.id = :cliente_id ORDER BY AFC.id DESC");
            query.setParameter("cliente_id", cliente_id);
            query.setMaxResults(100);
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
}
