package br.com.rtools.pessoa.dao;

import br.com.rtools.pessoa.Cnae;
import br.com.rtools.principal.DB;
import br.com.rtools.utilitarios.Dao;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;
import oracle.toplink.essentials.exceptions.EJBQLException;

public class CnaeDao extends DB {

    public List pesquisaCnae(String descricao, String por, String como) {
        if (descricao.isEmpty()) {
            return new ArrayList();
        }
        List<Cnae> lista = new ArrayList();
        descricao = descricao.toLowerCase().toUpperCase().replace("-", "").replace(".", "").replace("/", "");
        try {
            String queryString;
            if (descricao.isEmpty()) {
                return lista;
            }
            if (como.equals("I")) {
                descricao = descricao + "%";
            } else {
                descricao = "%" + descricao + "%";
            }
            if (por.equals("numero")) {
                queryString = "SELECT id FROM pes_cnae WHERE TRANSLATE(ds_numero, '.-/', '') LIKE '" + descricao + "'";
            } else {
                queryString = "SELECT id FROM pes_cnae WHERE UPPER(ds_cnae) LIKE '" + descricao + "'";
            }
            Query query = getEntityManager().createNativeQuery(queryString);
            List list = query.getResultList();
            Dao dao = new Dao();
            if (!list.isEmpty()) {
                for (Object l : list) {
                    lista.add((Cnae) dao.find(new Cnae(), (Integer) ((List) l).get(0)));
                }
            }
            return lista;
        } catch (EJBQLException e) {
            return new ArrayList();
        }
    }

    public Cnae pesquisaCnaePorCnae(String cnae) {
        cnae = cnae.toLowerCase().toUpperCase();
        try {
            Query query = getEntityManager().createQuery("SELECT C FROM Cnae AS C WHERE UPPER(C.cnae) = :cnae");
            query.setParameter("cnae", cnae);
            List list = query.getResultList();
            if (!list.isEmpty() && list.size() == 1) {
                return (Cnae) query.getSingleResult();
            }
        } catch (Exception e) {
        }
        return null;
    }

    public Cnae pesquisaCnaePorNumero(String numero) {
        try {
            Query query = getEntityManager().createQuery("SELECT C FROM Cnae AS C WHERE C.numero = :numero");
            query.setParameter("numero", numero);
            List list = query.getResultList();
            if (!list.isEmpty() && list.size() == 1) {
                return (Cnae) query.getSingleResult();
            }
        } catch (Exception e) {
        }
        return null;
    }
}
