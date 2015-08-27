package br.com.clinicaintegrada.pessoa.dao;

import br.com.clinicaintegrada.pessoa.Pessoa;
import br.com.clinicaintegrada.principal.DB;
import br.com.clinicaintegrada.utils.AnaliseString;
import br.com.clinicaintegrada.utils.Dao;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class PessoaDao extends DB {

    public List pesquisarPessoa(String descricao, String por, String como, int cliente) {
        if (descricao.isEmpty()) {
            return new ArrayList();
        }
        if (por.equals("cnpj") || por.equals("cpf") || por.equals("cei")) {
            por = "ds_documento";
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
            String queryString = "SELECT p.* FROM pes_pessoa AS p WHERE UPPER(FUNC_FUNC_TRANSLATE(p." + por + ")) LIKE '" + AnaliseString.removerAcentos(descricao) + "' AND p.id_cliente = " + cliente + " ORDER BY p.ds_nome";
            Query qry = getEntityManager().createNativeQuery(queryString, Pessoa.class);
            List list = qry.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return new ArrayList();
    }

    public Pessoa pesquisaPessoaPorDocumento(String documento, int cliente) {
        Dao dao = new Dao();
        try {
            Query query = getEntityManager().createNativeQuery(
                    "        SELECT pes.id                                                                              "
                    + "        FROM pes_pessoa AS PES                                                                   "
                    + "  INNER JOIN pes_fisica AS FIS ON FIS.id_pessoa = PES.id                                         "
                    + "       WHERE PES.ds_documento = '" + documento + "'                                              "
                    + "          OR FUNC_TRANSLATE(UPER(FIS.ds_rg),'./-', '') = FUNC_TRANSLATE(UPER('" + documento + "'),'./-','')"
                    + "         AND p.id_cliente = " + cliente
            );
            query.setFirstResult(0);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return (Pessoa) dao.find(new Pessoa(), (Integer) ((List) list.get(0)).get(0));
            }
        } catch (Exception e) {
        }
        return null;
    }

    public List pesquisaPessoasSemLogin(int cliente) {
        try {
            Query qry = getEntityManager().createQuery("SELECT PES FROM Pessoa AS PES WHERE PES.login IS NULL AND PES.id > 0 AND PES.cliente.id = :cliente ORDER BY PES.nome");
            qry.setParameter("cliente", cliente);
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

    public Pessoa findPessoaByBoletoAndContaCobranca(String boleto, int idContaCobranca) {
        Pessoa pessoa = null;
        String textqry
                = "      SELECT PES.*                                                   "
                + "        FROM pes_pessoa AS PES                                       "
                + "  INNER JOIN fin_movimento AS M ON PES.id = M.id_pessoa              "
                + "  INNER JOIN fin_boleto AS B ON M.nr_ctr_boleto = B.nr_ctr_boleto    "
                + "         AND M.is_ativo IS TRUE                                      "
                + "         AND B.id_conta_cobranca = " + idContaCobranca
                + "         AND M.ds_documento = '" + boleto + "'";
        try {
            Query qry = getEntityManager().createNativeQuery(textqry, Pessoa.class);
            qry.setMaxResults(1);
            pessoa = (Pessoa) qry.getSingleResult();
        } catch (Exception e) {
            e.getMessage();
        }
        return pessoa;
    }

}
