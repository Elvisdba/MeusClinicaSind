package br.com.rtools.endereco.dao;

import br.com.rtools.endereco.*;
import br.com.rtools.principal.DB;
import br.com.rtools.seguranca.controleUsuario.SessaoCliente;
import br.com.rtools.utilitarios.AnaliseString;
import br.com.rtools.utilitarios.Dao;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

@SuppressWarnings("unchecked")
public class EnderecoDao extends DB {

    public List pesquisaEnderecoPorCep(String cep) {
        cep = cep.replace("-", "");
        try {
            Query query = getEntityManager().createQuery("SELECT ENDE FROM Endereco AS ENDE WHERE ENDE.cep LIKE :cep ORDER BY ENDE.descricaoEndereco.descricao");
            query.setParameter("cep", cep);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    public List pesquisaEndereco(String uf, String cidade, String logradouro, String descricao, String iniParcial) {
        List<Endereco> listEnd = new ArrayList();
        descricao = descricao.toLowerCase().toUpperCase();
        try {
            if (iniParcial.equals("I")) {
                descricao = descricao + "%";
            } else {
                descricao = "%" + descricao + "%";
            }
            Dao dao = new Dao();
            String queryString = ""
                    + "   SELECT ende.id                                        "
                    + "     FROM end_endereco              AS ende,             "
                    + "          end_cidade                AS cid,              "
                    + "          end_logradouro            AS logr,             "
                    + "          end_descricao_endereco    AS des               "
                    + "    WHERE ende.id_cidade = cid.id                        "
                    + "      AND ende.id_logradouro = logr.id                   "
                    + "      AND ende.id_descricao_endereco = des.id            "
                    + "      AND UPPER(cid.ds_cidade) = '" + cidade.toLowerCase().toUpperCase() + "'"
                    + "      AND UPPER(cid.ds_uf) = '" + uf.toLowerCase().toUpperCase() + "'"
                    + "      AND UPPER(logr.ds_descricao) = '" + logradouro.toLowerCase().toUpperCase() + "'"
                    + "      AND UPPER(translate(des.ds_descricao)) LIKE '" + AnaliseString.removerAcentos(descricao) + "'"
                    + "      AND ( ende.id_cliente = " + SessaoCliente.get().getId() + " OR ende.id_cliente IS NULL ) "
                    + " ORDER BY des.ds_descricao";
            Query qry = getEntityManager().createNativeQuery(queryString);
            List list = qry.getResultList();
            if (!list.isEmpty()) {
                for (Object l : list) {
                    listEnd.add((Endereco) dao.find(new Endereco(), (Integer) ((List) l).get(0)));
                }
            }
            return listEnd;
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    public List pesquisaEndereco(int idDescricao, int idCidade, int idBairro, int idLogradouro) {
        try {
            Query qry = getEntityManager().createQuery(""
                    + "SELECT ENDE                                              "
                    + "  FROM Endereco AS ENDE                                  "
                    + " WHERE ENDE.descricaoEndereco.id = :descricaoEndereco    "
                    + "   AND ENDE.cidade.id = :cidade                          "
                    + "   AND ENDE.bairro.id = :bairro                          "
                    + "   AND ENDE.logradouro.id = :logradouro                  "
                    + "   AND ( ENDE.cliente.id = :cliente                      "
                    + "         OR ENDE.cliente IS NULL                         "
                    + "        )                                                ");
            qry.setParameter("descricaoEndereco", idDescricao);
            qry.setParameter("cidade", idCidade);
            qry.setParameter("bairro", idBairro);
            qry.setParameter("logradouro", idLogradouro);
            qry.setParameter("cliente", SessaoCliente.get().getId());
            List list = qry.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }
}
