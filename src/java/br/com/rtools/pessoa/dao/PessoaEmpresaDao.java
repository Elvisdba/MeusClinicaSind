package br.com.rtools.pessoa.dao;

import br.com.rtools.pessoa.PessoaEmpresa;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class PessoaEmpresaDao extends DB {

    public List listaPessoaEmpresaPorFisica(int idFisica) {
        try {
            Query query = getEntityManager().createQuery(" SELECT PE FROM PessoaEmpresa AS PE WHERE PE.fisica.id = :fisica AND PE.dtDemissao IS NOT NULL ORDER BY PE.dtAdmissao DESC ");
            query.setParameter("fisica", idFisica);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return new ArrayList();
    }

    public List listaEmpresasPorFisica(int idFisica) {
        try {
            Query query = getEntityManager().createQuery("SELECT PE FROM PessoaEmpresa AS PE WHERE PE.fisica.id = :fisica");
            query.setParameter("fisica", idFisica);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return new ArrayList();
    }

    public PessoaEmpresa pesquisaPessoaEmpresaPorFisica(int idFisica) {
        try {
            Query qry = getEntityManager().createQuery(
                    "    SELECT PE                          "
                    + "    FROM PessoaEmpresa AS PE         "
                    + "   WHERE PE.fisica.id = " + idFisica
                    + "     AND PE.dtDemissao IS NULL       ");
            List list = qry.getResultList();
            if (!list.isEmpty() && list.size() == 1) {
                return (PessoaEmpresa) qry.getSingleResult();
            }
        } catch (Exception e) {
        }
        return null;
    }

    public PessoaEmpresa pesquisaPessoaEmpresaPorPessoa(int idPessoa) {
        try {
            Query query = getEntityManager().createQuery(
                    "  SELECT PE                                    "
                    + "  FROM PessoaEmpresa AS PE                   "
                    + " WHERE PE.fisica.pessoa.id = " + idPessoa
                    + "   AND PE.dtDemissao IS NULL                 ");
            List list = query.getResultList();
            if (!list.isEmpty() && list.size() == 1) {
                return (PessoaEmpresa) query.getSingleResult();
            }
        } catch (Exception e) {
        }
        return null;
    }
}
