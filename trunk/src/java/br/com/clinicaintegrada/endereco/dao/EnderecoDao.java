package br.com.clinicaintegrada.endereco.dao;

import br.com.clinicaintegrada.endereco.Endereco;
import br.com.clinicaintegrada.principal.DB;
import br.com.clinicaintegrada.seguranca.controleUsuario.SessaoCliente;
import br.com.clinicaintegrada.utils.AnaliseString;
import br.com.clinicaintegrada.utils.Dao;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

@SuppressWarnings("unchecked")
public class EnderecoDao extends DB {

    public List pesquisaEnderecoPorCep(String cep) {
        cep = cep.replace("-", "");
        try {
            String queryString = ""
                    + "       SELECT ENDE.* "
                    + "         FROM end_endereco AS ENDE "
                    + "   INNER JOIN end_descricao_endereco AS DE ON DE.id = ENDE.id_descricao_endereco "
                    + "        WHERE ENDE.ds_cep LIKE '" + cep + "'"
                    + "          AND (ENDE.id_cliente IS NULL OR ENDE.id_cliente = " + SessaoCliente.get().getId() + ") "
                    + "     ORDER BY DE.ds_descricao ";
            Query query = getEntityManager().createNativeQuery(queryString, Endereco.class);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return new ArrayList();
    }

    public List pesquisaEndereco(String uf, String cidade, String logradouro, String descricao, String iniParcial) {
        descricao = descricao.toLowerCase().toUpperCase();
        try {
            if (iniParcial.equals("I")) {
                descricao = descricao + "%";
            } else {
                descricao = "%" + descricao + "%";
            }
            Dao dao = new Dao();
            String queryString = ""
                    + "   SELECT ende.*                                        "
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
                    + "      AND UPPER(func_translate(des.ds_descricao)) LIKE '" + AnaliseString.removerAcentos(descricao) + "'"
                    + "      AND ( ende.id_cliente = " + SessaoCliente.get().getId() + " OR ende.id_cliente IS NULL ) "
                    + " ORDER BY des.ds_descricao";
            Query query = getEntityManager().createNativeQuery(queryString, Endereco.class);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    public List pesquisaEndereco(int idDescricao, int idCidade, int idBairro, int idLogradouro) {
        try {
            Query qry = getEntityManager().createNativeQuery(""
                    + "SELECT ENDE.*                                            "
                    + "  FROM end_endereco AS ENDE                              "
                    + " WHERE ENDE.id_descricao_endereco    = " + idDescricao
                    + "   AND ENDE.id_cidade                = " + idCidade
                    + "   AND ENDE.id_bairro                = " + idBairro
                    + "   AND ENDE.id_logradouro            = " + idLogradouro
                    + "   AND ( ENDE.id_cliente  = " + SessaoCliente.get().getId()
                    + "         OR ENDE.id_cliente IS NULL                      "
                    + "        )                                                ", Endereco.class);
            List list = qry.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }
}
