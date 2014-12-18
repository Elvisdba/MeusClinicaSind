package br.com.clinicaintegrada.pessoa.dao;

import br.com.clinicaintegrada.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class FuncaoEquipeDao extends DB {

    public List pesquisaTodasFuncaoEquipe() {
        try {
            Query query = getEntityManager().createQuery("SELECT FE FROM FuncaoEquipe AS FE ORDER BY FE.tipoAtendimento.descricao ASC, FE.profissao.profissao ASC, FE.tipoDocumentoProfissao.descricao ASC");
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {

        }
        return new ArrayList();
    }

    public List pesquisaTodasFuncaoEquipePorCliente(int cliente) {
        try {
            Query query = getEntityManager().createQuery("SELECT FE FROM FuncaoEquipe AS FE WHERE FE.cliente.id = :cliente ORDER BY FE.profissao.profissao ASC, FE.tipoDocumentoProfissao.descricao ASC");
            query.setParameter("cliente", cliente);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {

        }
        return new ArrayList();
    }

    public List pesquisaTodasAtendimentoPorFuncaoEquipe(int funcaoEquipe) {
        try {
            Query query = getEntityManager().createQuery("SELECT FE FROM FuncaoEquipe AS FE WHERE FE.id = :funcaoEquipe ORDER BY FE.tipoAtendimento.descricao ASC");
            query.setParameter("funcaoEquipe", funcaoEquipe);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {

        }
        return new ArrayList();
    }

    public List pesquisaFuncaoEquipePorProfissao(int profissao) {
        try {
            Query query = getEntityManager().createQuery("SELECT FE FROM FuncaoEquipe AS FE WHERE FE.profissao.id = :profissao ORDER BY FE.tipoDocumentoProfissao.descricao ASC");
            query.setParameter("profissao", profissao);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {

        }
        return new ArrayList();
    }

    public List pesquisaFuncaoEquipePorProfissaoCliente(int profissao, int cliente) {
        try {
            Query query = getEntityManager().createQuery("SELECT FE FROM FuncaoEquipe AS FE WHERE FE.profissao.id = :profissao AND FE.cliente.id = :cliente ORDER BY FE.tipoDocumentoProfissao.descricao ASC");
            query.setParameter("profissao", profissao);
            query.setParameter("cliente", cliente);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {

        }
        return new ArrayList();
    }

    public boolean existeFuncaoEquipe(int tipoAtendimento, int tipoDocumentoProfissao, int profissao) {
        try {
            Query query;
            if (tipoAtendimento == 0 || tipoAtendimento == -1) {
                query = getEntityManager().createQuery("SELECT FE FROM FuncaoEquipe AS FE WHERE FE.tipoAtendimento IS NULL AND FE.tipoDocumentoProfissao.id = :tipoDocumentoProfissao  AND FE.profissao.id = :profissao ");
            } else {
                query = getEntityManager().createQuery("SELECT FE FROM FuncaoEquipe AS FE WHERE FE.tipoAtendimento.id = :tipoAtendimento AND FE.tipoDocumentoProfissao.id = :tipoDocumentoProfissao  AND FE.profissao.id = :profissao ");
                query.setParameter("tipoAtendimento", tipoAtendimento);
            }
            query.setParameter("tipoDocumentoProfissao", tipoDocumentoProfissao);
            query.setParameter("profissao", profissao);
            query.setMaxResults(1);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    public boolean existeFuncaoEquipeCliente(int tipoAtendimento, int tipoDocumentoProfissao, int profissao, int cliente) {
        try {
            Query query;
            if (tipoAtendimento == 0 || tipoAtendimento == -1) {
                query = getEntityManager().createQuery("SELECT FE FROM FuncaoEquipe AS FE WHERE FE.tipoAtendimento IS NULL AND FE.tipoDocumentoProfissao.id = :tipoDocumentoProfissao AND FE.profissao.id = :profissao AND FE.cliente.id = :cliente ");
            } else {
                query = getEntityManager().createQuery("SELECT FE FROM FuncaoEquipe AS FE WHERE FE.tipoAtendimento.id = :tipoAtendimento AND FE.tipoDocumentoProfissao.id = :tipoDocumentoProfissao  AND FE.profissao.id = :profissao AND FE.cliente.id = :cliente");
                query.setParameter("tipoAtendimento", tipoAtendimento);
            }
            query.setParameter("tipoDocumentoProfissao", tipoDocumentoProfissao);
            query.setParameter("profissao", profissao);
            query.setParameter("cliente", cliente);
            query.setMaxResults(1);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    public boolean existeFuncaoEquipePorProfissao(int profissao) {
        try {
            Query query = getEntityManager().createQuery("SELECT FE FROM FuncaoEquipe AS FE WHERE FE.profissao.id = :profissao ");
            query.setParameter("profissao", profissao);
            query.setMaxResults(1);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    public boolean existeFuncaoEquipePorProfissaoCliente(int profissao, int cliente) {
        try {
            Query query = getEntityManager().createQuery("SELECT FE FROM FuncaoEquipe AS FE WHERE FE.profissao.id = :profissao AND FE.cliente.id = :cliente");
            query.setParameter("profissao", profissao);
            query.setParameter("cliente", cliente);
            query.setMaxResults(1);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    public List findAllByClienteAndInTipoAtendimento(Integer cliente, String inTipoAtendimento) {
        try {
            String queryString = "SELECT FE FROM FuncaoEquipe AS FE WHERE FE.cliente.id = :cliente AND FE.tipoAtendimento.id IN(" + inTipoAtendimento + ") ORDER BY FE.profissao.profissao ASC, FE.tipoDocumentoProfissao.descricao ASC";
            Query query = getEntityManager().createQuery(queryString);
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
    
    public List findAllByClienteAndInAvaliacao(Integer cliente, Integer idAvaliacao) {
        try {
            String queryString = "SELECT FE FROM FuncaoEquipe AS FE WHERE FE.cliente.id = :cliente AND FE.id NOT IN (SELECT AE.funcaoEquipe.id FROM AvaliacaoEquipe AS AE WHERE AE.avaliacao.id = " + idAvaliacao + " ) ORDER BY FE.profissao.profissao ASC, FE.tipoDocumentoProfissao.descricao ASC";
            Query query = getEntityManager().createQuery(queryString);
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

}
