package br.com.rtools.seguranca.dao;

import br.com.rtools.principal.DB;
import br.com.rtools.seguranca.PermissaoDepartamento;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

@SuppressWarnings("unchecked")
public class PermissaoDepartamentoDao extends DB {

    public List pesquisaTodos() {
        try {
            Query qry = getEntityManager().createQuery("SELECT PD FROM PermissaoDepartamento AS PD ");
            return (qry.getResultList());
        } catch (Exception e) {
            return null;
        }
    }

    public List pesquisaPermissaoAdicionada(int idDepto, int idNivel, int idCliente) {
        try {
            Query qry = getEntityManager().createQuery(
                    "     SELECT PD                                             "
                    + "     FROM PermissaoDepartamento AS PD                    "
                    + "    WHERE PD.departamento.id = :departamento             "
                    + "      AND PD.nivel.id        = :nivel                    "
                    + "      AND PD.cliente.id      = :cliente                  "
                    + " ORDER BY PD.permissao.modulo.descricao,                 "
                    + "          PD.permissao.rotina.rotina                     ");
            qry.setParameter("departamento", idDepto);
            qry.setParameter("nivel", idNivel);
            qry.setParameter("cliente", idCliente);
            List list = qry.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
            return (qry.getResultList());
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    public List pesquisaPermissaDepartamento(String ids) {
        try {
            Query query = getEntityManager().createQuery("SELECT PD FROM PermissaoDepartamento AS PD WHERE PD.permissao.id IN( :ids )");
            query.setParameter("ids", ids);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    public List<PermissaoDepartamento> listaPermissaoDepartamentoAdicionada(int idDepartamento, int idNivel, String descricaoPesquisa, int idCliente) {
        String queryFiltro = "";
        if (!descricaoPesquisa.equals("")) {
            queryFiltro = " AND UPPER(PD.permissao.rotina.rotina) LIKE '%" + descricaoPesquisa.toUpperCase() + "%'";
        }
        try {
            Query query = getEntityManager().createQuery(" SELECT PD FROM PermissaoDepartamento AS PD WHERE PD.departamento.id = :departamento AND PD.nivel.id = :nivel AND PD.cliente.id = :cliente " + queryFiltro + " ORDER BY PD.permissao.modulo.descricao ASC, PD.permissao.rotina.rotina ASC ");
            query.setParameter("departamento", idDepartamento);
            query.setParameter("nivel", idNivel);
            query.setParameter("cliente", idCliente);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return new ArrayList();
    }

    public PermissaoDepartamento pesquisaPermissaoDepartamento(int idDepartamento, int idNivel, int idPermissao, int idCliente) {
        try {
            Query query = getEntityManager().createQuery(
                    " SELECT PD "
                    + " FROM PermissaoDepartamento AS PD                        "
                    + "WHERE PD.departamento.id = :departamento                 "
                    + "  AND PD.nivel.id        = :nivel                        "
                    + "  AND PD.permissao.id    = :permissao                    "
                    + "  AND PD.cliente.id      = :cliente                      ");
            query.setParameter("departamento", idDepartamento);
            query.setParameter("nivel", idNivel);
            query.setParameter("permissao", idPermissao);
            query.setParameter("cliente", idCliente);
            List list = query.getResultList();
            if (!list.isEmpty() && list.size() == 1) {
                return (PermissaoDepartamento) query.getSingleResult();
            }
        } catch (Exception e) {
        }
        return null;
    }

    public List<PermissaoDepartamento> pesquisaPermissaoDepartamento(int idDepartamento, int idNivel, int idCliente) {
        try {
            Query query = getEntityManager().createQuery(
                    " SELECT PD                                                 "
                    + "  FROM PermissaoDepartamento AS PD                       "
                    + " WHERE PD.departamento.id = :departamento                "
                    + "   AND PD.nivel.id        = :nivel                       "
                    + "   AND PD.cliente.id      = :cliente                     ");
            query.setParameter("departamento", idDepartamento);
            query.setParameter("nivel", idNivel);
            query.setParameter("cliente", idCliente);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }
}
