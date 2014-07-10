package br.com.rtools.pessoa.dao;

import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.principal.DB;
import br.com.rtools.seguranca.controleUsuario.SessaoCliente;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DataHoje;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class JuridicaDao extends DB {

    public List pesquisaPessoaJuridica(String descricao, String por, String como) {
        if (descricao.isEmpty()) {
            return new ArrayList();
        }
        descricao = descricao.toLowerCase().toUpperCase();
        String queryString = "SELECT J FROM Juridica AS J WHERE J.pessoa.cliente.id = :cliente ";
        if (por.equals("fantasia")) {
            por = "fantasia";
            switch (como) {
                case "P":
                    descricao = "%" + descricao + "%";
                    break;
                case "I":
                    por = "fantasia";
                    descricao = descricao + "%";
                    break;
            }
            queryString += " AND UPPER(J." + por + ") LIKE :descricao ORDER BY J.pessoa.nome ";
        }
        if (por.equals("nome") || por.equals("email1") || por.equals("email2") || por.equals("cnpj") || por.equals("cpf") || por.equals("cei")) {
            if (por.equals("cnpj") || por.equals("cpf") || por.equals("cei")) {
                por = "documento";
            } else {
                por = "nome";
            }
            switch (como) {
                case "P":
                    descricao = "%" + descricao + "%";
                    break;
                case "I":
                    descricao = descricao + "%";
                    break;
            }
            queryString += " AND UPPER(J.pessoa." + por + ") LIKE :descricao ORDER BY J.pessoa.nome ";
        }
        if (por.equals("endereco")) {
            String queryEndereco = ""
                    + "       SELECT jur.id                                                                         "
                    + "        FROM pes_pessoa_endereco     AS pesend                                               "
                    + "  INNER JOIN pes_pessoa              AS pes      ON (pes.id = pesend.id_pessoa)              "
                    + "  INNER JOIN end_endereco            AS ende     ON (ende.id = pesend.id_endereco)           "
                    + "  INNER JOIN end_cidade              AS cid      ON (cid.id = ende.id_cidade)                "
                    + "  INNER JOIN end_descricao_endereco  AS enddes   ON (enddes.id = ende.id_descricao_endereco) "
                    + "  INNER JOIN end_bairro              AS bai      ON (bai.id = ende.id_bairro)                "
                    + "  INNER JOIN end_logradouro          AS logr     ON (logr.id = ende.id_logradouro)           "
                    + "  INNER JOIN pes_juridica jur ON (jur.id_pessoa = pes.id)                                    "
                    + "  WHERE UPPER(TRANSLATE(logr.ds_descricao) || ' ' || enddes.ds_descricao || ', ' || pesend.ds_numero || ', ' || bai.ds_descricao || ', ' || cid.ds_cidade || ', ' || cid.ds_uf)    LIKE UPPER('%" + descricao + "%')  "
                    + "     OR UPPER(TRANSLATE(logr.ds_descricao) || ' ' || enddes.ds_descricao || ', ' || bai.ds_descricao || ', ' || cid.ds_cidade || ', ' || cid.ds_uf)    LIKE UPPER('%" + descricao + "%')  "
                    + "     OR UPPER(TRANSLATE(logr.ds_descricao) || ' ' || enddes.ds_descricao || ', ' || cid.ds_cidade  || ', ' || cid.ds_uf) LIKE UPPER('%" + descricao + "%')                                "
                    + "     OR UPPER(TRANSLATE(logr.ds_descricao) || ' ' || enddes.ds_descricao || ', ' || cid.ds_cidade  ) LIKE UPPER('%" + descricao + "%')                                                    "
                    + "     OR UPPER(TRANSLATE(logr.ds_descricao) || ' ' || enddes.ds_descricao) LIKE UPPER('%" + descricao + "%') || ', ' || pesend.ds_numero                                                   "
                    + "     OR UPPER(TRANSLATE(logr.ds_descricao) || ' ' || enddes.ds_descricao) LIKE UPPER('%" + descricao + "%')                                                                               "
                    + "     OR UPPER(TRANSLATE(enddes.ds_descricao)) LIKE UPPER('%" + descricao + "%')                                                                                                           "
                    + "     OR UPPER(TRANSLATE(cid.ds_cidade)) LIKE UPPER('%" + descricao + "%')                                                                                                                 "
                    + "     OR UPPER(TRANSLATE(ende.ds_cep)) "
                    + "    AND pes.id_cliente = " + SessaoCliente.get().getId() + "' LIMIT 1000 ";

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
                queryString = " SELECT JUR FROM Juridica AS JUR WHERE JUR.id IN(" + listaId + ") ORDER BY JUR.pessoa.nome ASC";
            }
        }
        try {
            Query query = getEntityManager().createQuery(queryString);
            query.setParameter("cliente", SessaoCliente.get().getId());
            if (!descricao.equals("%%") && !descricao.equals("%")) {
                if (!por.equals("endereco")) {
                    query.setParameter("descricao", descricao);
                }
            }
            List lista = query.getResultList();
            if (!lista.isEmpty()) {
                return lista;
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return new ArrayList();
    }

    public List listaMotivoInativacao() {
        List result;
        try {
            Query qry = getEntityManager().createQuery("SELECT MI FROM MotivoInativacao AS MI ");
            result = qry.getResultList();
        } catch (Exception e) {
            result = null;
        }
        return result;
    }

    public Juridica pesquisaJuridicaPorPessoa(int id) {
        Juridica result = null;
        try {
            Query qry = getEntityManager().createQuery("select jur from Juridica jur "
                    + " where jur.pessoa.id = :id_jur"
                    + " order by jur.pessoa.nome");
            qry.setParameter("id_jur", id);
            result = (Juridica) qry.getSingleResult();
        } catch (Exception e) {
            result = null;
        }
        return result;
    }

    public List pesquisaJuridicaPorDocumento(String documento) {
        try {
            Query query = getEntityManager().createQuery("SELECT J FROM Juridica AS J WHERE J.pessoa.cliente.id = :cliente AND J.pessoa.documento = :documento ORDER BY J.pessoa.nome ");
            query.setParameter("documento", documento);
            query.setParameter("cliente", SessaoCliente.get().getId());
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    public List pesquisaJuridicaComEmail() {
        try {
            Query query = getEntityManager().createQuery("SELECT J FROM Juridica AS J WHERE J.id <> 1 AND J.pessoa.email1 <> '' AND J.pessoa.cliente.id = :cliente");
            query.setParameter("cliente", SessaoCliente.get().getId());
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    public List<Juridica> pesquisaJuridicaParaRetorno(String documento) {
        try {
            String queryString = "SELECT J.id                                                                                                            "
                    + "    FROM pes_juridica AS J,                                                                                                       "
                    + "         pes_pessoa   AS P                                                                                                        "
                    + "   WHERE P.id = J.id_pessoa                                                                                                       "
                    + "     AND SUBSTRING('00'||SUBSTRING(REPLACE(                                                                                       "
                    + "         REPLACE(                                                                                                                 "
                    + "             REPLACE('0000000000'||pes.ds_documento,'/',''),'-',''),'.',''),length(                                               "
                    + "                 REPLACE(                                                                                                         "
                    + "                     REPLACE(                                                                                                     "
                    + "                         REPLACE('0000000000'||pes.ds_documento,'/',''),'-',''),'.',''))-14,LENGHT(                               "
                    + "                             REPLACE(                                                                                             "
                    + "                                 REPLACE(                                                                                         "
                    + "                                     REPLACE('0000000000'||pes.ds_documento,'/',''),'-',''),'.',''))),0,16) = '" + documento + "' "
                    + "     AND P.id_cliente = " + SessaoCliente.get().getId();

            Query query = getEntityManager().createNativeQuery(queryString);
            List list = query.getResultList();
            List<Juridica> juridicas = new ArrayList();
            if (!list.isEmpty()) {
                Dao dao = new Dao();
                for (Object l : list) {
                    juridicas.add((Juridica) dao.find(new Juridica(), (Integer) ((List) l).get(0)));
                }
            }
            return juridicas;
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    public int quantidadeEmpresasContabilidade(int idContabilidade) {
        try {
            Query query = getEntityManager().createQuery("SELECT COUNT(J) FROM Juridica AS J WHERE J.contabilidade.id = :contabilidade");
            query.setParameter("contabilidade", idContabilidade);
            query.setParameter("cliente", SessaoCliente.get().getId());
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return Integer.parseInt(String.valueOf(query.getSingleResult()));
            }
        } catch (NumberFormatException e) {
        }
        return -1;
    }

    public List listaContabilidade() {
        try {
            Query query = getEntityManager().createQuery(""
                    + "   SELECT J.contabilidade                "
                    + "     FROM Juridica AS J                  "
                    + "    WHERE J.pessoa.cliente.id = :cliente "
                    + " GROUP BY J.contabilidade,               "
                    + "          J.contabilidade.pessoa.nome    "
                    + " ORDER BY J.contabilidade.pessoa.nome ASC");
            query.setParameter("cliente", SessaoCliente.get().getId());
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return query.getResultList();
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    public int[] listaInadimplencia(int id_juridica) {
        List list;
        int[] result = new int[2];

        try {
            // QUERY QUANTIDADE DE MESES INADIMPLENTES -------------------
            String textQry = "select extract(month from  dt_vencimento),extract(year from  dt_vencimento) from fin_movimento where is_ativo=true and id_baixa is null and id_pessoa=" + id_juridica + " and dt_vencimento < '" + DataHoje.data() + "'"
                    + " group by extract(month from  dt_vencimento),extract(year from  dt_vencimento)";

            Query qry = getEntityManager().createNativeQuery(textQry);
            list = qry.getResultList();

            if (list != null && !list.isEmpty()) {
//                for (int i = 0; i < vetor.size(); i++){
                result[0] = list.size();
//                }
            } else {
                result[0] = 0;
            }
            // ----------------------------------------------------------

            // QUANTIDADE DE PARCELAS INADIMPLENTES ---------------------
            textQry = "select count(*) from fin_movimento where  is_ativo=true and id_baixa is null and id_pessoa=" + id_juridica + " and dt_vencimento < '" + DataHoje.data() + "'";

            qry = getEntityManager().createNativeQuery(textQry);
            list = qry.getResultList();

//          for (int i = 0; i < vetor.size(); i++){
            result[1] = Integer.parseInt(String.valueOf((Long) ((List) list.get(0)).get(0)));
//           }
            // ----------------------------------------------------------
            return result;
        } catch (NumberFormatException e) {
            return result;
        }
    }

    public boolean empresaInativa(int idPessoa, String motivo) {
        String stringMotivo = "";
        if (!motivo.equals("")) {
            stringMotivo = " AND motivo = '" + motivo + "' ";
        }
        Query query = getEntityManager().createNativeQuery(" SELECT id_pessoa FROM arr_contribuintes_vw WHERE dt_inativacao IS NOT NULL AND id_pessoa = " + idPessoa + stringMotivo);
        try {
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }
}
