package br.com.clinicaintegrada.endereco.dao;

import br.com.clinicaintegrada.principal.DB;
import br.com.clinicaintegrada.endereco.Cidade;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

@SuppressWarnings("unchecked")
public class CidadeDao extends DB {

    public List pesquisaCidadesPorEstado(String uf) {
        if (uf.isEmpty()) {
            return new ArrayList();
        }
        try {
            Query qry = getEntityManager().createQuery("SELECT C FROM Cidade AS C WHERE C.uf = :uf");
            qry.setParameter("uf", uf);
            List list = qry.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    public List<String> pesquisaCidadePorEstadoCidade(String uf, String cidade, boolean returnListString) {
        List<String> result = new ArrayList();
        try {
            Query qry = getEntityManager().createQuery("select cid.cidade "
                    + "  from Cidade cid "
                    + " where upper(cid.cidade) like :des_cidade "
                    + "   and upper(cid.uf) = :des_uf order by cid.cidade");
            qry.setParameter("des_cidade", cidade.toUpperCase());
            qry.setParameter("des_uf", uf.toUpperCase());
            result = (List<String>) qry.getResultList();
        } catch (Exception e) {
        }
        return result;
    }

    public List pesquisaCidadePorEstadoCidade(String uf, String cidade, String como) {
        if (cidade.isEmpty()) {
            return new ArrayList();
        }
        cidade = cidade.toLowerCase().toUpperCase();
        switch (como) {
            case "P":
                cidade = "%" + cidade + "%";
                break;
            case "I":
                cidade = cidade + "%";
                break;
        }
        try {
            Query query = getEntityManager().createQuery("SELECT C FROM Cidade AS C WHERE UPPER(C.cidade) LIKE :cidade AND C.uf = :uf");
            query.setParameter("cidade", cidade);
            query.setParameter("uf", uf);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    public Cidade pesquisaCidadePorEstadoCidade(String uf, String cidade) {
        cidade = cidade.toLowerCase().toUpperCase();
        try {
            Query query = getEntityManager().createQuery("SELECT C FROM Cidade AS C WHERE UPPER(C.cidade) = :cidade AND C.uf = :uf");
            query.setParameter("cidade", cidade);
            query.setParameter("uf", uf);
            List list = query.getResultList();
            if (!list.isEmpty() || list.size() == 1) {
                return (Cidade) query.getSingleResult();
            }
        } catch (Exception e) {
        }
        return null;
    }
}
