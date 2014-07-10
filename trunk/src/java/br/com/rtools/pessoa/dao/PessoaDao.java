package br.com.rtools.pessoa.dao;

import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.principal.DB;
import br.com.rtools.utilitarios.Dao;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class PessoaDao extends DB {

    public List pesquisarPessoa(String descricao, String por, String como) {
        if (descricao.isEmpty()) {
            return new ArrayList();
        }
        if (por.equals("cnpj") || por.equals("cpf") || por.equals("cei")) {
            por = "documento";
        }
        descricao = descricao.toLowerCase().toUpperCase();
        switch (como) {
            case "P":
                descricao = "%" + descricao + "%";
                break;
            case "I":
                descricao = descricao + "%";
                break;
        }
        try {
            String queryString = "SELECT P FROM Pessoa AS P WHERE UPPER(P." + por + ") LIKE :descricao ORDER BY P.nome";
            Query qry = getEntityManager().createQuery(queryString);
            qry.setParameter("descricao", descricao);
            List list = qry.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    public Pessoa pesquisaPessoaPorDocumento(String documento) {
        Dao dao = new Dao();
        try {
            Query query = getEntityManager().createNativeQuery(
                    "        SELECT pes.id                                                                              "
                    + "        FROM pes_pessoa AS PES                                                                   "
                    + "  INNER JOIN pes_fisica AS FIS ON FIS.id_pessoa = PES.id                                         "
                    + "       WHERE PES.ds_documento = '" + documento + "'                                              "
                    + "          OR TRANSLATE(UPER(FIS.ds_rg),'./-', '') = TRANSLATE(UPER('" + documento + "'),'./-','')");
            query.setFirstResult(0);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return (Pessoa) dao.find(new Pessoa(), (Integer) ((List) list.get(0)).get(0));
            }
        } catch (Exception e) {
        }
        return null;
    }

    public List pesquisaPessoasSemLogin() {
        try {
            Query qry = getEntityManager().createQuery("SELECT PES FROM Pessoa AS PES WHERE PES.login IS NULL AND PES.id > 0 ORDER BY PES.nome");
            return (qry.getResultList());
        } catch (Exception e) {
            return null;
        }
    }

    public Pessoa ultimoId() {
        Pessoa result = null;
        try {
            Query qry = getEntityManager().createQuery("SELECT MAX(PES.id) FROM Pessoa AS PES ");
            result = (Pessoa) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;

    }

}
