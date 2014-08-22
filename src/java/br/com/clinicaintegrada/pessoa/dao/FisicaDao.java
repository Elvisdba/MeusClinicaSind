package br.com.clinicaintegrada.pessoa.dao;

import br.com.clinicaintegrada.pessoa.Fisica;
import br.com.clinicaintegrada.principal.DB;
import br.com.clinicaintegrada.seguranca.controleUsuario.SessaoCliente;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Query;

public class FisicaDao extends DB {

    public List pesquisaPessoaFisica(String descricao, String por, String como) {
        if (descricao.isEmpty()) {
            return new ArrayList();
        }
        descricao = descricao.toLowerCase().toUpperCase();
        String queryString = " SELECT FIS FROM Fisica AS FIS WHERE FIS.pessoa.cliente.id = :cliente ";
        if (por.equals("documento") || por.equals("nome") || por.equals("email1") || por.equals("email2")) {
            switch (como) {
                case "P":
                    descricao = "%" + descricao + "%";
                    break;
                case "I":
                    descricao = descricao + "%";
                    break;
            }
            if (como.isEmpty()) {
                queryString += " AND UPPER(FIS.pessoa." + por + ") = :descricao ";
            } else {
                queryString += " AND UPPER(FIS.pessoa." + por + ") LIKE :descricao ";
            }
        }
        if (por.equals("rg")) {
            queryString += " AND UPPER(FIS." + por + ") LIKE :descricao ";
        }
        if (por.equals("endereco")) {
            String queryEndereco = ""
                    + "      SELECT fis.id "
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

            Query qryEndereco = getEntityManager().createNativeQuery(queryEndereco);
            List listEndereco = qryEndereco.getResultList();
            String listaId = "";
            if (!listEndereco.isEmpty()) {
                for (int i = 0; i < listEndereco.size(); i++) {
                    if (i == 0) {
                        listaId = ((Integer) ((List) listEndereco.get(i)).get(0)).toString();
                    } else {
                        listaId += ", " + ((Integer) ((List) listEndereco.get(i)).get(0)).toString();
                    }
                }
                queryString = " SELECT FIS FROM Fisica AS FIS WHERE FIS.id IN(" + listaId + ")";
            }
        }
        try {
            Query qry = getEntityManager().createQuery(queryString);
            if (!descricao.equals("%%") && !descricao.equals("%")) {
                if (!por.equals("endereco")) {
                    qry.setParameter("descricao", descricao);
                    qry.setParameter("cliente", SessaoCliente.get().getId());
                }
            }
            List list = qry.getResultList();
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
            return pesquisaPessoaFisica(documento, "documento", "P");
        } else {
            return pesquisaPessoaFisica(documento, "documento", "");
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
            Query query;
            if (like) {
                query = getEntityManager().createQuery("SELECT F FROM Fisica AS F WHERE F.pessoa.cliente.id = :p1 AND UPPER(F.pessoa.nome) LIKE :p2 ");
                query.setParameter("p2", "'%" + nome + "%'");
            } else if (rg.isEmpty() && nascimento != null) {
                query = getEntityManager().createQuery("SELECT F FROM Fisica AS F WHERE F.pessoa.cliente.id = :p1 AND UPPER(F.pessoa.nome) = :p2 AND F.dtNascimento = :p3");
                query.setParameter("p2", nome);
                query.setParameter("p3", nascimento);
            } else if (!rg.isEmpty()) {
                query = getEntityManager().createQuery("SELECT F FROM Fisica AS F WHERE F.pessoa.cliente.id = :p1 AND F.rg = :p2");
                query.setParameter("p2", rg);
            } else {
                return new ArrayList();
            }
            query.setParameter("p1", SessaoCliente.get().getId());
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }
}
