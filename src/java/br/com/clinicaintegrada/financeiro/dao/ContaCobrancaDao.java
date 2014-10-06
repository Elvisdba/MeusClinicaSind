package br.com.clinicaintegrada.financeiro.dao;

import br.com.clinicaintegrada.financeiro.ContaCobranca;
import br.com.clinicaintegrada.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class ContaCobrancaDao extends DB {
    
    public List findAllByCliente(int idCliente) {
        try {
            Query query = getEntityManager().createQuery("SELECT CC FROM ContaCobranca AS CC WHERE CC.cliente.id = :idCliente");
            query.setParameter("idCliente", idCliente);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }
    
    public boolean existContaCobranca(ContaCobranca cc) {
        try {
            Query query = getEntityManager().createQuery("SELECT CC FROM ContaCobranca AS CC WHERE UPPER(CC.cedente) = :cedente AND CC.cliente.id = :cliente");
            query.setParameter("cliente", cc.getCliente().getId());
            query.setParameter("cedente", cc.getCedente().toUpperCase());
            if (!query.getResultList().isEmpty()) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }
    
    public ContaCobranca findContaCobrancaByContaCobranca(ContaCobranca cc) {
        ContaCobranca result = null;
        String descricao = cc.getCedente().toLowerCase().toUpperCase();
        try {
            Query qry = getEntityManager().createQuery("select ced from ContaCobranca ced where UPPER(ced.contaCobranca) = :d_contaCobranca");
            qry.setParameter("d_contaCobranca", descricao);
            result = (ContaCobranca) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }
    
    public ContaCobranca pesquisaServicoCobranca(int idServicos, int idTipoServico) {
        ContaCobranca result = null;
        try {
            Query qry = getEntityManager().createQuery(
                    "select c.contaCobranca "
                    + "  from ServicoContaCobranca c"
                    + " where c.tipoServico.id = :idTipo"
                    + "   and c.servicos.id = :idServicos");
            qry.setParameter("idTipo", idTipoServico);
            qry.setParameter("idServicos", idServicos);
            result = (ContaCobranca) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }
    
    public ContaCobranca pesquisaCobrancaCedente(String codCedente) {
        ContaCobranca result = new ContaCobranca();
        try {
            Query qry = getEntityManager().createQuery(
                    "select c "
                    + "  from ContaCobranca c"
                    + " where c.codCedente = :codCedente");
            qry.setParameter("codCedente", codCedente);
            result = (ContaCobranca) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }
}
