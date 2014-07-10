package br.com.rtools.sistema.dao;

import br.com.rtools.principal.DB;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.sistema.EmailPessoa;
import br.com.rtools.utilitarios.Sessions;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Query;
import javax.persistence.TemporalType;

@SuppressWarnings("unchecked")
public class EmailDao extends DB {

    public List findEmail(int idRotina, Date dateStart, Date dateFinish, String filterBy, String descricaoPesquisa, String orderBy) {
        String desc = descricaoPesquisa.toLowerCase().toUpperCase();
        List listQuery = new ArrayList();
        boolean isDate = false;
        boolean isDateFinish = false;
        try {
            if (idRotina > 0) {
                listQuery.add("EP.email.rotina.id = " + idRotina);
            }
            if (dateStart != null) {
                isDate = true;
                if (dateFinish != null) {
                    listQuery.add("EP.email.data BETWEEN :dateStart AND :dateFinish");
                    isDateFinish = true;
                } else {
                    listQuery.add("EP.email.data = :dateStart");
                }
            }
            if (!filterBy.isEmpty()) {
                if (!descricaoPesquisa.isEmpty()) {
                    if (filterBy.equals("assunto")) {
                    } else if (filterBy.equals("email")) {
                        listQuery.add((" ( UPPER(EP.pessoa.email1)    LIKE '%" + desc + "%' "
                                + " OR UPPER(EP.destinatario)  LIKE '%" + desc + "%' "
                                + " OR UPPER(EP.cc)            LIKE '%" + desc + "%' "
                                + " OR UPPER(EP.bcc)           LIKE '%" + desc + "%' "
                                + ") "));

                    } else if (filterBy.equals("assunto")) {
                        listQuery.add("UPPER(EP.email.assunto) LIKE '%" + desc + "'%");
                    } else if (filterBy.equals("pessoa")) {
                    }
                }

            }
            Usuario u = (Usuario) Sessions.getObject("sessaoUsuario");
            String queryString = " SELECT EP FROM EmailPessoa AS EP WHERE EP.email.rascunho = false AND EP.email.usuario.id = " + u.getId() + " ";
            if (!listQuery.isEmpty()) {
                queryString += "  ";
                for (Object o : listQuery) {
                    queryString += " AND " + o.toString();
                }
            }
            if (!orderBy.isEmpty()) {
                queryString += " ORDER BY " + orderBy + " , EP.email.id DESC  ";
            } else {
                queryString += " ORDER BY EP.email.data DESC, EP.email.hora DESC, EP.email.id DESC ";
            }
            Query query = getEntityManager().createQuery(queryString);
            if (isDate) {
                query.setParameter("dateStart", dateStart, TemporalType.DATE);
                if (isDateFinish) {
                    query.setParameter("dateFinish", dateFinish, TemporalType.DATE);
                }
            }
            query.setMaxResults(1000);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return (List<EmailPessoa>) list;
            }

        } catch (Exception e) {
        }
        return new ArrayList<EmailPessoa>();
    }

    public List findRascunho() {
        List list;
        try {
            Query query = getEntityManager().createQuery("SELECT EP FROM EmailPessoa AS EP WHERE EP.email.rascunho = TRUE ");
            list = query.getResultList();
            if (!list.isEmpty()) {
                return (List<EmailPessoa>) list;
            }
        } catch (Exception e) {
        }
        return new ArrayList<EmailPessoa>();
    }

}
