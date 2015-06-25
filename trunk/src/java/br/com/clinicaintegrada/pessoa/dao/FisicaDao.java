package br.com.clinicaintegrada.pessoa.dao;

import br.com.clinicaintegrada.pessoa.Fisica;
import br.com.clinicaintegrada.principal.DB;
import br.com.clinicaintegrada.seguranca.controleUsuario.SessaoCliente;
import br.com.clinicaintegrada.utils.DataHoje;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Query;

public class FisicaDao extends DB {

    private String non_ids = "";

    public List pesquisaPessoaFisica(String descricao, String por, String como) {
        try {
            if (descricao.isEmpty()) {
                return new ArrayList();
            }
            Integer cliente = SessaoCliente.get().getId();
            descricao = descricao.toLowerCase().toUpperCase();
            String queryString = " "
                    + "     SELECT F.*                                          "
                    + "       FROM pes_fisica AS F                              "
                    + " INNER JOIN pes_pessoa AS P ON P.id = F.id_pessoa        "
                    + "      WHERE P.id_cliente = " + cliente;
            if (por.equals("ds_rg") || por.equals("ds_documento") || por.equals("ds_nome") || por.equals("ds_email1") || por.equals("ds_email2")) {
                switch (como) {
                    case "P":
                        descricao = "%" + descricao + "%";
                        break;
                    case "I":
                        descricao = descricao + "%";
                        break;
                }
                if (por.equals("ds_rg")) {
                    if (como.isEmpty()) {
                        queryString += " AND UPPER(F." + por + ") = FUNC_TRANSLATE('" + descricao + "')";
                    } else {
                        queryString += " AND UPPER(F." + por + ") LIKE FUNC_TRANSLATE('" + descricao + "')";
                    }
                } else {
                    if (como.isEmpty()) {
                        queryString += " AND UPPER(FUNC_TRANSLATE(P." + por + ")) = FUNC_TRANSLATE('" + descricao + "')";
                    } else {
                        queryString += " AND UPPER(FUNC_TRANSLATE(P." + por + ")) LIKE FUNC_TRANSLATE('" + descricao + "')";
                    }
                }
                if (!non_ids.isEmpty()) {
                    queryString += "  AND F.id_pessoa NOT IN (" + non_ids + ")";
                }
            }
            if (por.equals("endereco")) {
                queryString = ""
                        + "      SELECT F.* "
                        + "        FROM pes_pessoa_endereco     AS pesend                                                                                                                                 "
                        + "  INNER JOIN pes_pessoa              AS pes      ON (pes.id = pesend.id_pessoa)                                                                                                "
                        + "  INNER JOIN end_endereco            AS ende     ON (ende.id = pesend.id_endereco)                                                                                             "
                        + "  INNER JOIN end_cidade              AS cid      ON (cid.id = ende.id_cidade)                                                                                                  "
                        + "  INNER JOIN end_descricao_endereco  AS enddes   ON (enddes.id = ende.id_descricao_endereco)                                                                                   "
                        + "  INNER JOIN end_bairro              AS bai      ON (bai.id = ende.id_bairro)                                                                                                  "
                        + "  INNER JOIN end_logradouro          AS logr     ON (logr.id = ende.id_logradouro)                                                                                             "
                        + "  INNER JOIN pes_fisica              AS fis      ON (fis.id_pessoa = pes.id)                                                                                                   "
                        + "  WHERE UPPER(logr.ds_descricao || ' ' || enddes.ds_descricao || ', ' || bai.ds_descricao || ', ' || cid.ds_cidade || ', ' || cid.ds_uf)    LIKE UPPER('%" + descricao + "%')  "
                        + "     OR UPPER(logr.ds_descricao || ' ' || enddes.ds_descricao || ', ' || cid.ds_cidade  || ', ' || cid.ds_uf) LIKE UPPER('%" + descricao + "%')                                "
                        + "     OR UPPER(logr.ds_descricao || ' ' || enddes.ds_descricao || ', ' || cid.ds_cidade  ) LIKE UPPER('%" + descricao + "%')                                                    "
                        + "     OR UPPER(logr.ds_descricao || ' ' || enddes.ds_descricao) LIKE UPPER('%" + descricao + "%')                                                                               "
                        + "     OR UPPER(enddes.ds_descricao) LIKE UPPER('%" + descricao + "%')                                                                                                           "
                        + "     OR UPPER(cid.ds_cidade) LIKE UPPER('%" + descricao + "%')                                                                                                                 "
                        + "     OR UPPER(ende.ds_cep) = '" + descricao + "' "
                        + "    AND pes.id_cliente = " + SessaoCliente.get().getId();
                if (!non_ids.isEmpty()) {
                    queryString += " AND pes.id NOT IN (" + non_ids + " )";
                }
            }
            Query query = getEntityManager().createNativeQuery(queryString, Fisica.class);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    public List pesquisaFisicaPorDocumento(String documento) {
        return pesquisaFisicaPorDocumento(documento, true);
    }

    public List pesquisaFisicaPorDocumento(String documento, boolean like) {
        if (like) {
            return pesquisaPessoaFisica(documento, "ds_documento", "P");
        } else {
            return pesquisaPessoaFisica(documento, "ds_documento", "");
        }
    }

    public Fisica pesquisaFisicaPorPessoa(int idPessoa) {
        try {
            Query qry = getEntityManager().createQuery("SELECT F FROM Fisica AS F WHERE F.pessoa.id = :pessoa AND F.pessoa.cliente.id = :cliente");
            qry.setParameter("pessoa", idPessoa);
            qry.setParameter("cliente", SessaoCliente.get().getId());
            List list = qry.getResultList();
            if (!list.isEmpty()) {
                return (Fisica) qry.getSingleResult();
            }
        } catch (Exception e) {
        }
        return null;
    }

    public List pesquisaFisicaPorNome(String nome) {
        return pesquisaFisicaPorNomeNascimentoRG(nome, null, "", true);
    }

    public List pesquisaFisicaPorNomeNascimentoRG(String nome, Date nascimento, String rg) {
        return pesquisaFisicaPorNomeNascimentoRG(nome, nascimento, rg, false);
    }

    public List pesquisaFisicaPorNomeNascimentoRG(String nome, Date nascimento, String rg, boolean like) {
        try {
            String queryString = "";
            if (like) {
                queryString = ""
                        + "     SELECT F.*              "
                        + "       FROM pes_fisica AS F  "
                        + " INNER JOIN pes_pessoa AS P ON P.id = F.id_pessoa  "
                        + "      WHERE P.id_cliente = " + SessaoCliente.get().getId()
                        + "        AND func_translate(UPPER(P.ds_nome)) LIKE func_translate(UPPER('" + nome + "')) ";
            } else if (rg.isEmpty() && nascimento != null) {
                queryString = ""
                        + "     SELECT F.* "
                        + "       FROM pes_fisica AS F  "
                        + " INNER JOIN pes_pessoa AS P ON P.id = F.id_pessoa  "
                        + "      WHERE P.id_cliente = " + SessaoCliente.get().getId()
                        + "        AND func_translate(UPPER(P.ds_nome)) LIKE func_translate(UPPER('" + nome + "')) "
                        + "        AND F.dt_nascimento = '" + DataHoje.converteData(nascimento) + "' ";

            } else if (!rg.isEmpty()) {
                queryString = ""
                        + "     SELECT F.* "
                        + "       FROM pes_fisica AS F  "
                        + " INNER JOIN pes_pessoa AS P ON P.id = F.id_pessoa "
                        + "      WHERE P.id_cliente = " + SessaoCliente.get().getId()
                        + "        AND F.ds_rg LIKE '" + rg + "' ";
            } else {
                return new ArrayList();
            }
            Query query = getEntityManager().createNativeQuery(queryString, Fisica.class);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return new ArrayList();
    }

    public String getNon_ids() {
        return non_ids;
    }

    public void setNon_ids(String non_ids) {
        this.non_ids = non_ids;
    }
}
