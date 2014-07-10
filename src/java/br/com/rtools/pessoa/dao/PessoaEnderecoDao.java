package br.com.rtools.pessoa.dao;

import br.com.rtools.endereco.Endereco;
import br.com.rtools.pessoa.PessoaEndereco;
import br.com.rtools.principal.DB;
import br.com.rtools.seguranca.controleUsuario.SessaoCliente;
import br.com.rtools.utilitarios.Dao;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class PessoaEnderecoDao extends DB {

    public List pesquisaPessoaEnderecoPorPessoa(int idPessoa) {
        try {
            Query qry = getEntityManager().createQuery(
                    "     SELECT PE                     "
                    + "     FROM PessoaEndereco AS PE   "
                    + "    WHERE PE.pessoa.id = :pessoa "
                    + " ORDER BY PE.tipoEndereco.id     ");
            qry.setParameter("pessoa", idPessoa);
            List list = qry.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    public PessoaEndereco pesquisaPessoaEnderecoPorPessoaTipo(int idPessoa, int idTipoEndereco) {
        try {
            Query qry = getEntityManager().createQuery(
                    "     SELECT PE                                 "
                    + "     FROM PessoaEndereco AS PE               "
                    + "    WHERE PE.pessoa.id = :pessoa             "
                    + "      AND PE.tipoEndereco.id = :tipoEndereco ");
            qry.setParameter("pesspa", idPessoa);
            qry.setParameter("tipoEndereco", idTipoEndereco);
            List list = qry.getResultList();
            if (!list.isEmpty() && list.size() == 1) {
                return (PessoaEndereco) qry.getSingleResult();
            }
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * <ul>
     * <li>Tipos de Endereço</li>
     * <li>1 - Residencial</li>
     * <li>2 - Comercial</li>
     * <li>3 - Cobrança</li>
     * <li>4 - Correspondência</li>
     * <li>5 - Base Territorial</li>
     * </ul>
     *
     * @param idPessoa
     * @param idTipoEndereco
     * @return
     */
    public List listaPessoaEnderecoPorPessoaTipo(int idPessoa, int idTipoEndereco) {
        try {
            Query qry = getEntityManager().createQuery(
                    "     SELECT PE                                 "
                    + "     FROM PessoaEndereco AS PE               "
                    + "    WHERE PE.pessoa.id = :pessoa             "
                    + "      AND PE.tipoEndereco.id = :tipoEndereco ");
            qry.setParameter("pesspa", idPessoa);
            qry.setParameter("tipoEndereco", idTipoEndereco);
            List list = qry.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    public List pesquisaPessoaEndederecoEmpresaComContabilidade(int idJuridicaContabilidade) {
        try {
            Query qry = getEntityManager().createQuery(""
                    + "SELECT PE                                                "
                    + "  FROM PessoaEndereco AS PE,                             "
                    + "       Juridica AS J                                     "
                    + " WHERE PE.pessoa.id = J.pessoa.id                        "
                    + "   AND PE.tipoEndereco.id = 3                            "
                    + "   AND J.contabilidade.id = :idJuridicaContabilidade     "
                    + "   AND J.pessoa.cliente.id = :cliente                    ");
            qry.setParameter("idJuridicaContabilidade", idJuridicaContabilidade);
            qry.setParameter("cliente", SessaoCliente.get().getId());
            return qry.getResultList();
        } catch (Exception e) {
            return new ArrayList();
        }
    }

    public Endereco enderecoReceita(String cep, String[] descricao, String[] bairro) {
        Dao dao = new Dao();
        try {
            String queryString = ""
                    + "     SELECT *                                                                    "
                    + "       FROM end_endereco AS E                                                    "
                    + " INNER JOIN end_descricao_endereco   AS DE ON (DE.id = E.id_descricao_endereco)  "
                    + " INNER JOIN end_bairro               AS BA ON (BA.id = E.id_bairro)              "
                    + "      WHERE ds_cep = '" + cep + "'                                               "
                    + "        AND E.id_cliente = " + SessaoCliente.get().getId();
            String or_desc = "", or_bairro = "";
            for (String d : descricao) {
                if (descricao.length == 1) {
                    queryString += " AND ( UPPER(TRANSLATE(DE.ds_descricao)) LIKE UPPER('%" + d + "%') ) ";
                    break;
                } else {
                    or_desc += " OR TRANSLATE(TRANSLATE(DE.ds_descricao)) LIKE UPPER('%" + d + "%') ";
                }
            }
            if (descricao.length > 1) {
                queryString += " AND ( TRANSLATE(TRANSLATE(DE.ds_descricao)) LIKE UPPER('%" + descricao[0] + "%') " + or_desc + ") ";
            }

            for (String b : bairro) {
                if (bairro.length == 1) {
                    queryString += " AND ( UPPER(TRANSLATE(BA.ds_descricao)) LIKE UPPER('%" + b + "%') ) ";
                    break;
                } else {
                    or_bairro += " OR UPPER(TRANSLATE(BA.ds_descricao)) LIKE UPPER('%" + b + "%') ";
                }
            }
            if (bairro.length > 1) {
                queryString += " AND ( UPPER(TRANSLATE(BA.ds_descricao)) LIKE UPPER('%" + bairro[0] + "%') " + or_bairro + ") ";
            }

            Query qry = getEntityManager().createNativeQuery(queryString);
            qry.setMaxResults(1);
            List list = qry.getResultList();
            if (!list.isEmpty()) {
                return (Endereco) dao.find(new Endereco(), (Integer) ((List) list.get(0)).get(0));
            }
        } catch (Exception e) {
        }
        return null;
    }
}
