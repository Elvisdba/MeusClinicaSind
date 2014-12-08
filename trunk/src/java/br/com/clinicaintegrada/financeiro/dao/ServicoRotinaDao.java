package br.com.clinicaintegrada.financeiro.dao;

import br.com.clinicaintegrada.financeiro.Servicos;
import br.com.clinicaintegrada.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class ServicoRotinaDao extends DB {

    public List findAllByCliente(int idCliente) {
        try {
            Query query = getEntityManager().createQuery("SELECT SR From ServicoRotina AS SR WHERE SR.cliente.id = :idCliente");
            query.setParameter("cliente", idCliente);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    public List findByServico(int idServico) {
        try {
            Query query = getEntityManager().createQuery("SELECT SR FROM ServicoRotina AS SR WHERE SR.servicos.id = :servico ORDER BY SR.rotina.rotina ASC ");
            query.setParameter("servico", idServico);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }


    public boolean exists(int idServico, int idRotina) {
        try {
            Query query = getEntityManager().createQuery(" SELECT SR FROM ServicoRotina AS SR WHERE SR.servicos.id = :servicos AND SR.rotina.id = :rotina ");
            query.setParameter(":servicos", idServico);
            query.setParameter(":rotina ", idRotina);
            if (!query.getResultList().isEmpty()) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    public List<Servicos> listaServicosNotIn(String ids) {
        try {
            Query query = getEntityManager().createQuery(
                    "   SELECT sr.servicos "
                    + "   FROM ServicoRotina AS sr"
                    + "  WHERE sr.rotina.id NOT IN (" + ids + ") "
                    + "  ORDER BY sr.servicos.descricao");
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }
}
