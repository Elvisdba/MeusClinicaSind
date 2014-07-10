package br.com.rtools.seguranca.dao;

import br.com.rtools.principal.DB;
import br.com.rtools.seguranca.UsuarioAcesso;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class UsuarioAcessoDao extends DB {

    public UsuarioAcesso pesquisaUsuarioAcesso(int idUsuario, int idPermissao, int idCliente) {
        try {
            Query query = getEntityManager().createQuery("SELECT UA FROM UsuarioAcesso AS UA WHERE UA.usuario.id = :usuario AND UA.permissao.id = :permissao AND UA.cliente.id = :cliente");
            query.setParameter("usuario", idUsuario);
            query.setParameter("permissao", idPermissao);
            query.setParameter("cliente", idCliente);
            List list = query.getResultList();
            if (!list.isEmpty() && list.size() == 1) {
                return (UsuarioAcesso) query.getSingleResult();
            }
        } catch (Exception e) {
        }
        return null;
    }

    public List<UsuarioAcesso> pesquisaAcesso(int idPermissao) {
        try {
            Query query = getEntityManager().createQuery("SELECT UA FROM UsuarioAcesso AS UA WHERE UA.permissao.id = :permissao");
            query.setParameter("permissao", idPermissao);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    public UsuarioAcesso pesquisaUsuarioAcessoModuloRotinaEvento(int idUsuario, int idModulo, int idRotina, int idEvento, int idCliente) {
        try {
            Query query = getEntityManager().createQuery(
                    "   SELECT UA                                               "
                    + "   FROM UsuarioAcesso AS UA                              "
                    + "  WHERE UA.permissao.modulo.id   = :modulo               "
                    + "    AND UA.permissao.rotina.id   = :rotina               "
                    + "    AND UA.permissao.evento.id   = :evento               "
                    + "    AND UA.usuario.id            = :usuario              "
                    + "    AND UA.cliente.id            = :cliente              ");
            query.setParameter("modulo", idModulo);
            query.setParameter("rotina", idRotina);
            query.setParameter("evento", idEvento);
            query.setParameter("usuario", idUsuario);
            query.setParameter("cliente", idCliente);
            List list = query.getResultList();
            if (!list.isEmpty() && list.size() == 1) {
                return (UsuarioAcesso) query.getSingleResult();
            }
        } catch (Exception e) {
        }
        return null;
    }

    public List<UsuarioAcesso> listaUsuarioAcesso(int idUsuario, int idModulo, int idRotina, int idEvento, int idCliente) {
        String moduloString = "";
        String rotinaString = "";
        String eventoString = "";
        if (idModulo > 0) {
            moduloString = " AND UA.permissao.modulo.id = :modulo ";
        }
        if (idRotina > 0) {
            rotinaString = " AND UA.permissao.rotina.id = :rotina ";
        }
        if (idEvento > 0) {
            eventoString = " AND UA.permissao.evento.id = :evento ";
        }
        if (idCliente > 0) {
            eventoString = " AND UA.permissao.cliente.id = :cliente ";
        }
        try {
            Query qry = getEntityManager().createQuery(
                    "     SELECT UA                                     "
                    + "     FROM UsuarioAcesso AS UA                    "
                    + "    WHERE UA.usuario.id = :usuario               "
                    + moduloString
                    + rotinaString
                    + eventoString
                    + " ORDER BY UA.permissao.modulo.descricao ASC,     "
                    + "          UA.permissao.rotina.rotina ASC,        "
                    + "          UA.permissao.evento.descricao ASC      ");
            qry.setParameter("usuario", idUsuario);
            if (idModulo > 0) {
                qry.setParameter("modulo", idModulo);
            }
            if (idRotina > 0) {
                qry.setParameter("rotina", idRotina);
            }
            if (idEvento > 0) {
                qry.setParameter("evento", idEvento);
            }
            if (idCliente > 0) {
                qry.setParameter("cliente", idCliente);
            }
            List list = qry.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return new ArrayList();
    }

}
