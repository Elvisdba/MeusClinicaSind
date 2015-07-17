package br.com.clinicaintegrada.endereco.dao;

import br.com.clinicaintegrada.principal.DB;
import br.com.clinicaintegrada.endereco.Cidade;
import br.com.clinicaintegrada.utils.AnaliseString;
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
            Query query = getEntityManager().createNativeQuery("SELECT C.* FROM end_cidade AS C WHERE UPPER(FUNC_TRANSLATE(C.ds_cidade)) LIKE '" + AnaliseString.removerAcentos(cidade.toUpperCase()) + "'  AND UPPER(C.ds_uf) = '" + uf.toUpperCase() + "'", Cidade.class);
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
            Query query = getEntityManager().createNativeQuery("SELECT C.* FROM end_cidade AS C WHERE UPPER(FUNC_TRANSLATE(C.ds_cidade)) = '" + AnaliseString.removerAcentos(cidade) + "'  AND UPPER(C.ds_uf) = '" + uf.toUpperCase() + "'", Cidade.class);
            List list = query.getResultList();
            if (!list.isEmpty() || list.size() == 1) {
                return (Cidade) query.getSingleResult();
            }
        } catch (Exception e) {
        }
        return null;
    }

    /**
     *
     * @param cliente_id tipo)
     * @return
     */
    public List findCidadesByPessoaEnderecoGroup(Integer cliente_id) {
        try {
            String queryString = ""
                    + "     SELECT CI.*                                                     \n"
                    + "       FROM end_cidade AS CI WHERE CI.id IN (                        \n"
                    // SUB QUERY
                    + "        SELECT C.id "
                    + "          FROM pes_pessoa AS P                                       \n"
                    + "    INNER JOIN pes_fisica AS F ON F.id_pessoa = P.id                 \n"
                    + "    INNER JOIN pes_pessoa_endereco AS PE ON PE.id_pessoa = P.id      \n"
                    + "    INNER JOIN end_endereco AS E ON E.id = PE.id_endereco            \n"
                    + "    INNER JOIN end_cidade AS C ON C.id = E.id_cidade                 \n"
                    + "         WHERE P.id_cliente = ?                                      \n"
                    + "      GROUP BY C.id                                                  \n"
                    + ") "
                    + " ORDER BY CI.ds_cidade;                                              ";

            Query query = getEntityManager().createNativeQuery(queryString, Cidade.class);
            query.setParameter(1, cliente_id);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }

        } catch (Exception e) {

        }
        return new ArrayList();
    }
}
