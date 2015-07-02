package br.com.clinicaintegrada.pessoa.dao;

import br.com.clinicaintegrada.pessoa.Juridica;
import br.com.clinicaintegrada.principal.DB;
import br.com.clinicaintegrada.seguranca.Cliente;
import br.com.clinicaintegrada.seguranca.controleUsuario.SessaoCliente;
import br.com.clinicaintegrada.utils.Dao;
import br.com.clinicaintegrada.utils.DataHoje;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class JuridicaDao extends DB {

    public List pesquisaPessoaJuridica(String descricao, String por, String como, Cliente cliente) {
        if (descricao.isEmpty()) {
            return new ArrayList();
        }
        descricao = descricao.toLowerCase().toUpperCase();
        String queryString = ""
                + "     SELECT J.*                                              "
                + "       FROM pes_juridica AS J                                "
                + " INNER JOIN pes_pessoa AS P ON P.id = J.id_pessoa            "
                + "      WHERE (P.id_cliente = " + cliente.getId() + " OR J.id = " + cliente.getIdJuridica() + ")";
        if (por.equals("ds_fantasia")) {
            por = "ds_fantasia";
            switch (como) {
                case "P":
                    descricao = "%" + descricao + "%";
                    break;
                case "I":
                    por = "ds_fantasia";
                    descricao = descricao + "%";
                    break;
            }
            queryString += " AND UPPER(func_translate(J." + por + ")) LIKE func_translate(UPPER('" + descricao + "')) ORDER BY P.ds_nome ";
        }
        if (por.equals("ds_nome") || por.equals("ds_email1") || por.equals("ds_email2") || por.equals("ds_cpf") || por.equals("ds_cnpj") || por.equals("ds_cei")) {
            if (por.equals("ds_cpf") || por.equals("ds_cnpj") || por.equals("ds_cei")) {
                por = "ds_documento";
            }
            switch (como) {
                case "P":
                    descricao = "%" + descricao + "%";
                    break;
                case "I":
                    descricao = descricao + "%";
                    break;
            }
            queryString += " AND UPPER(func_translate(P." + por + ")) LIKE func_translate(UPPER('" + descricao + "')) ORDER BY P.ds_nome ";
        }
        if (por.equals("endereco")) {
            queryString = ""
                    + " SELECT J.* FROM pes_juridica AS J WHERE J.id IN ("
                    + "       SELECT jur.id                                                                         \n"
                    + "        FROM pes_pessoa_endereco     AS pesend                                               \n"
                    + "  INNER JOIN pes_pessoa              AS pes      ON (pes.id = pesend.id_pessoa)              \n"
                    + "  INNER JOIN end_endereco            AS ende     ON (ende.id = pesend.id_endereco)           \n"
                    + "  INNER JOIN end_cidade              AS cid      ON (cid.id = ende.id_cidade)                \n"
                    + "  INNER JOIN end_descricao_endereco  AS enddes   ON (enddes.id = ende.id_descricao_endereco) \n"
                    + "  INNER JOIN end_bairro              AS bai      ON (bai.id = ende.id_bairro)                \n"
                    + "  INNER JOIN end_logradouro          AS logr     ON (logr.id = ende.id_logradouro)           \n"
                    + "  INNER JOIN pes_juridica jur ON (jur.id_pessoa = pes.id)                                    \n"
                    + "  WHERE UPPER(func_translate(logr.ds_descricao || ' ' || enddes.ds_descricao || ', ' || pesend.ds_numero || ', ' || bai.ds_descricao || ', ' || cid.ds_cidade || ', ' || cid.ds_uf))    LIKE UPPER(func_translate('%" + descricao + "%'))    \n"
                    + "     OR UPPER(func_translate(logr.ds_descricao || ' ' || enddes.ds_descricao || ', ' || bai.ds_descricao || ', ' || cid.ds_cidade || ', ' || cid.ds_uf))    LIKE UPPER(func_translate('%" + descricao + "%'))                                \n"
                    + "     OR UPPER(func_translate(logr.ds_descricao || ' ' || enddes.ds_descricao || ', ' || cid.ds_cidade  || ', ' || cid.ds_uf)) LIKE UPPER(func_translate('%" + descricao + "%'))                                                              \n"
                    + "     OR UPPER(func_translate(logr.ds_descricao || ' ' || enddes.ds_descricao || ', ' || cid.ds_cidade  )) LIKE UPPER(func_translate('%" + descricao + "%'))                                                                                  \n"
                    + "     OR UPPER(func_translate(logr.ds_descricao || ' ' || enddes.ds_descricao || ', ' || pesend.ds_numero )) LIKE UPPER(func_translate('%" + descricao + "%'))                                                                                \n"
                    + "     OR UPPER(func_translate(logr.ds_descricao || ' ' || enddes.ds_descricao)) LIKE UPPER(func_translate('%" + descricao + "%'))                                                                                                             \n"
                    + "     OR UPPER(func_translate(enddes.ds_descricao)) LIKE UPPER('%" + descricao + "%')                                                                                                                                                         \n"
                    + "     OR UPPER(func_translate(cid.ds_cidade)) LIKE UPPER(func_translate('%" + descricao + "%'))                                                                                                                                               \n"
                    + "     OR ende.ds_cep LIKE '%" + descricao + "%'                                                                                                                                                                                                     \n"
                    + "    AND (pes.id_cliente = " + cliente.getId()
                    + "     OR jur.id = " + cliente.getIdJuridica() + " )"
                    + "  LIMIT 1000 )";

        }
        try {
            Query query = getEntityManager().createNativeQuery(queryString, Juridica.class);
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
