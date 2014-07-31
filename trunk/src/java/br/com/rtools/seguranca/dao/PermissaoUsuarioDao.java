package br.com.rtools.seguranca.dao;

import br.com.rtools.principal.DB;
import br.com.rtools.seguranca.*;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

@SuppressWarnings("unchecked")
public class PermissaoUsuarioDao extends DB {

    public List pesquisaListaPermissaoPorUsuario(int idUsuario, int idCliente) {
        try {
            Query query = getEntityManager().createQuery(""
                    + "   SELECT PU                          "
                    + "     FROM PermissaoUsuario AS PU      "
                    + "    WHERE PU.usuario.id = :usuario    "
                    + "      AND PU.cliente.id = :cliente    "
                    + " ORDER BY PU.departamento.descricao   ");
            query.setParameter("usuario", idUsuario);
            query.setParameter("cliente", idCliente);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    public PermissaoUsuario pesquisaPermissaoUsuario(int idUsuario, int idDepartamento, int idNivel, int idCliente) {
        try {
            Query qry = getEntityManager().createQuery(
                    "  SELECT PU                                                "
                    + "  FROM PermissaoUsuario AS PU                            "
                    + " WHERE PU.usuario.id         = :usuario                  "
                    + "   AND PU.departamento.id    = :departamento             "
                    + "   AND PU.cliente.id         = :cliente                  "
                    + "   AND PU.nivel.id           = :nivel                    ");
            qry.setParameter("usuario", idUsuario);
            qry.setParameter("departamento", idDepartamento);
            qry.setParameter("cliente", idCliente);
            qry.setParameter("nivel", idNivel);
            List list = qry.getResultList();
            if (!list.isEmpty() && list.size() == 1) {
                return (PermissaoUsuario) (qry.getSingleResult());
            }
        } catch (Exception e) {
        }
        return null;
    }

    public PermissaoUsuario pesquisaAcessoPermissao(int idUsuario, int idModulo, int idRotina, int idEvento, int idCliente) {
        try {
            Query query = getEntityManager().createQuery(
                    " SELECT PU                                                 "
                    + " FROM PermissaoUsuario       AS PU,                      "
                    + "      PermissaoDepartamento  AS PD,                      "
                    + "      Permissao              AS P                        "
                    + "WHERE PU.departamento.id = PD.departamento.id            "
                    + "  AND PU.nivel.id        = PD.nivel.id                   "
                    + "  AND PD.permissao.id    = P.id                          "
                    + "  AND PU.usuario.id      = :usuario                      "
                    + "  AND P.modulo.id        = :modulo                       "
                    + "  AND P.rotina.id        = :rotina                       "
                    + "  AND P.evento.id        = :evento                       "
                    + "  AND P.cliente.id       = :cliente                      ");
            query.setParameter("usuario", idUsuario);
            query.setParameter("modulo", idModulo);
            query.setParameter("rotina", idRotina);
            query.setParameter("evento", idEvento);
            query.setParameter("cliente", idCliente);
            List list = query.getResultList();
            if (!list.isEmpty() && list.size() == 1) {
                return (PermissaoUsuario) query.getSingleResult();
            }
        } catch (Exception e) {
        }
        return null;
    }

    public PermissaoUsuario pesquisaAcessoPermissaoSM(int idUsuario, int idRotina, int idEvento, int idCliente) {
        try {
            Query query = getEntityManager().createQuery(""
                    + "SELECT PU                                                "
                    + "  FROM PermissaoUsuario      AS PU,                      "
                    + "       PermissaoDepartamento AS PD,                      "
                    + "       Permissao             AS P                        "
                    + " WHERE PU.departamento.id    = PD.departamento.id        "
                    + "   AND PD.nivel.id           = PD.nivel.id               "
                    + "   AND PD.permissao.id       = P.id                      "
                    + "   AND PU.usuario.id         = :usuario                  "
                    + "   AND P.rotina.id           = :rotina                   "
                    + "   AND P.cliente.id          = :cliente                  "
                    + "   AND P.evento.id           = :evento                   ");
            query.setParameter("usuario", idUsuario);
            query.setParameter("rotina", idRotina);
            query.setParameter("evento", idEvento);
            query.setParameter("cliente", idCliente);
            List list = query.getResultList();
            if (!list.isEmpty() && list.size() == 1) {
                return (PermissaoUsuario) query.getSingleResult();
            }
        } catch (Exception e) {
        }
        return null;
    }

    public List<PermissaoUsuario> listaPermissaoUsuario(int idUsuario) {
        try {
            Query query = getEntityManager().createQuery(
                    "     SELECT PU                                             "
                    + "     FROM PermissaoUsuario AS PU                         "
                    + "    WHERE PU.usuario.id = :usuario                       "
                    + " ORDER BY PU.departamento.descricao                      ");
            query.setParameter("usuario", idUsuario);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    public boolean existePermissaoUsuario(PermissaoUsuario permissaoUsuario) {
        try {
            Query query = getEntityManager().createQuery(" SELECT PU FROM PermissaoUsuario AS PU WHERE PU.usuario.id = :usuario AND PU.departamento.id = :departamento AND PU.nivel.id = :nivel AND PU.cliente.id = :cliente");
            query.setParameter("usuario", permissaoUsuario.getUsuario().getId());
            query.setParameter("departamento", permissaoUsuario.getDepartamento().getId());
            query.setParameter("nivel", permissaoUsuario.getNivel().getId());
            query.setParameter("cliente", permissaoUsuario.getCliente().getId());
            if (!query.getResultList().isEmpty()) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }
}
