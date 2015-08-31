package br.com.clinicaintegrada.seguranca.dao;

import br.com.clinicaintegrada.principal.DB;
import br.com.clinicaintegrada.seguranca.Permissao;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

@SuppressWarnings("unchecked")
public class PermissaoDao extends DB {

    public Permissao pesquisaPermissaoModuloRotinaEvento(int idModulo, int idRotina, int idEvento, int idCliente) {
        try {
            Query query = getEntityManager().createQuery(
                    "   SELECT P                                                "
                    + "   FROM Permissao AS P                                   "
                    + "  WHERE P.modulo.id  = :modulo                           "
                    + "    AND P.rotina.id  = :rotina                           "
                    + "    AND P.segEvento.id  = :evento                        ");
                    // + "    AND P.cliente.id = :cliente");
            query.setParameter("modulo", idModulo);
            query.setParameter("rotina", idRotina);
            query.setParameter("evento", idEvento);
            // query.setParameter("cliente", idCliente);
            List list = query.getResultList();
            if (!list.isEmpty() && list.size() == 1) {
                return (Permissao) query.getSingleResult();
            }
        } catch (Exception e) {
        }
        return null;
    }

    public Permissao pesquisaPermissao(int idModulo, int idRotina, int idEvento, int idCliente) {
        try {
            Query query = getEntityManager().createQuery(
                    "   SELECT P                                                "
                    + "   FROM Permissao AS P                                   "
                    + "  WHERE P.modulo.id      = :modulo                       "
                    + "    AND P.rotina.id      = :rotina                       "
                    + "    AND P.rotina.ativo   = true                          "
                    + "    AND P.segEvento.id   = :evento                       ");
                    // + "    AND P.cliente.id     = :cliente                      ");
            query.setParameter("modulo", idModulo);
            query.setParameter("rotina", idRotina);
            query.setParameter("evento", idEvento);
            // query.setParameter("cliente", idCliente);
            List list = query.getResultList();
            if (!list.isEmpty() && list.size() == 1) {
                return (Permissao) query.getSingleResult();
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public List pesquisaTodosAgrupados(int idCliente) {
        try {
            Query query = getEntityManager().createQuery(
                    "     SELECT P                                              "
                    + "     FROM Permissao AS P                                 "
                    + "    WHERE P.segEvento.id  = 1                               "
                    + "      AND P.cliente.id = :cliente                        "
                    + " ORDER BY P.modulo.descricao ASC,                        "
                    + "          P.rotina.rotina ASC                            ");
            List list = query.getResultList();
            query.setParameter("cliente", idCliente);
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    public List pesquisaTodosAgrupadosPorModulo(int idModulo, int idCliente) {
        try {
            Query query = getEntityManager().createQuery(
                    "     SELECT P                                              "
                    + "     FROM Permissao AS P                                 "
                    + "    WHERE P.segEvento.id  = 1                               "
                    + "      AND P.modulo.id  = :modulo                         "
                    + "      AND P.cliente.id = :cliente                        "
                    + " ORDER BY P.modulo.descricao ASC,                        "
                    + "          P.rotina.rotina ASC                            ");
            query.setParameter("modulo", idModulo);
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

    public List pesquisaPermissaoModuloRotina(int idModulo, int idRotina, int idCliente) {
        try {
            Query qry = getEntityManager().createQuery(
                    "   SELECT P                                                "
                    + "   FROM Permissao AS P                                   "
                    + "  WHERE P.modulo.id  = :modulo                           "
                    + "    AND P.rotina.id  = :rotina                           "
                    + "    AND P.cliente.id = :cliente                          ");
            qry.setParameter("modulo", idModulo);
            qry.setParameter("rotina", idRotina);
            qry.setParameter("cliente", idCliente);
            List list = qry.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    public List pesquisaPermissaDisponivel(String ids) {
        String queryString;
        try {
            if (ids.length() == 0) {
                queryString = "SELECT P                                         "
                        + "      FROM Permissao AS P                            "
                        + "  ORDER BY P.modulo.descricao                        ";
            } else {
                queryString = "SELECT P                                         "
                        + "      FROM Permissao AS P                            "
                        + "     WHERE P.id NOT IN(                              "
                        + "           SELECT PD.permissao.id                    "
                        + "             FROM PermissaoDepartamento AS PD        "
                        + "            WHERE PD.id IN(" + ids + ")              "
                        + "           )                                         "
                        + "  ORDER BY P.modulo.descricao,                       "
                        + "           P.rotina.rotina                           ";
            }

            Query query = getEntityManager().createQuery(queryString);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    public List<Permissao> listaPermissaoDepartamentoDisponivel(int idDepartamento, int idNivel, String descricaoPesquisa, int idCliente) {
        String queryFiltro = "";
        if (!descricaoPesquisa.equals("")) {
            queryFiltro = " AND UPPER(P.rotina.rotina) LIKE '%" + descricaoPesquisa.toUpperCase() + "%'";
        }
        try {
            Query query = getEntityManager().createQuery(" SELECT P FROM Permissao AS P WHERE P.id NOT IN ( SELECT PD.permissao.id FROM PermissaoDepartamento AS PD WHERE PD.departamento.id = " + idDepartamento + " AND PD.nivel.id = " + idNivel + " AND PD.cliente.id = " + idCliente + " ) " + queryFiltro + " ORDER BY P.modulo.descricao ASC, P.rotina.rotina ASC ");
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return new ArrayList();

    }

    public List pesquisaPermissaoModuloRotinaEventi(int idModulo, int idRotina, int idEvento, int idCliente) {
        try {
            Query qry = getEntityManager().createQuery(
                    "   SELECT P                                                "
                    + "   FROM Permissao AS P                                   "
                    + "  WHERE P.modulo.id  = :modulo                           "
                    + "    AND P.rotina.id  = :rotina                           "
                    + "    AND P.evento.id  = :evento                           "
                    + "    AND P.cliente.id = :cliente                          ");
            qry.setParameter("modulo", idModulo);
            qry.setParameter("rotina", idRotina);
            qry.setParameter("evento", idEvento);
            qry.setParameter("evento", idCliente);
            List list = qry.getResultList();
            if (!list.isEmpty()) {
                return qry.getResultList();
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }
}
