package br.com.clinicaintegrada.financeiro.dao;

import br.com.clinicaintegrada.financeiro.MensagemBoleto;
import br.com.clinicaintegrada.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class MensagemBoletoDao extends DB {

    public List listAllOrdering(String referencia, int idServicos, int idTipoServico) {
        String queryString
                = "     SELECT MB.*                                                     "
                + "       FROM fin_mensagem_boleto  AS MB                               "
                + " INNER JOIN fin_servicos         AS S on (S.id = MB.id_servicos)      "
                + " INNER JOIN fin_tipo_servico     AS T on (T.id = MB.id_tipo_servico)  "
                + "      WHERE MB.ds_referencia     = \'" + referencia + "\'            "
                + "        AND MB.id_servicos       = " + idServicos
                + "        AND MB.id_tipo_servico   = " + idTipoServico
                + "   ORDER BY substring(ds_referencia,4,7) DESC,                       "
                + "            substring(ds_referencia,1,2),                            "
                + "            S.ds_descricao,                                          "
                + "            T.ds_descricao                                           ";
        try {
            Query query = getEntityManager().createNativeQuery(queryString, MensagemBoleto.class);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    public List listAllSemReferencia() {
        try {
            Query query = getEntityManager().createQuery("SELECT MB FROM MensagemBoleto AS MB WHERE MB.referencia = '' ORDER BY MB.id DESC");
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    public List listAllSemServico4() {
        try {
            Query query = getEntityManager().createQuery("SELECT MB FROM MensagemBoleto AS MB WHERE MB.servicos.id <> 4 ORDER BY MB.id DESC");
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    public MensagemBoleto verificaMensagem(int idServicos, int idTipoServico, String referencia) {
        try {
            Query query = getEntityManager().createQuery(
                    "  SELECT MB                                                "
                    + "  FROM MensagemBoleto AS MB                              "
                    + " WHERE MB.servicos.id = :idServicos                      "
                    + "   AND MB.tipoServico.id = :idTipoServico                "
                    + "   AND MB.referencia like :referencia");
            query.setParameter("idServicos", idServicos);
            query.setParameter("idTipoServico", idTipoServico);
            query.setParameter("referencia", referencia);
            List list = query.getResultList();
            if (!list.isEmpty() && list.size() == 1) {
                return (MensagemBoleto) query.getSingleResult();
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public List mesmoTipoServico(int idServicos, int idTipoServico, String ano) {
        ano = "%" + ano + "%";
        try {
            Query query = getEntityManager().createQuery(
                    "  SELECT MB                                                "
                    + "  FROM MensagemBoleto AS MB                              "
                    + " WHERE MB.servicos.id = :idServicos                      "
                    + "   AND MB.tipoServico.id = :idTipoServico                "
                    + "   AND MB.referencia like :ano");
            query.setParameter("idServicos", idServicos);
            query.setParameter("idTipoServico", idTipoServico);
            query.setParameter("ano", ano);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    public MensagemBoleto retornaDiaString(int idJuridica, String ref, int idTipoServico, int idServicos) {
        String textQuery = ""
                + " SELECT MB.*                                                 "
                + "   FROM pes_juridica         AS J,                           "
                + "        fin_mensagem_boleto  AS MB,                          "
                + "  WHERE PE.id_pessoa         = j.id_pessoa                   "
                + "    AND PE.id_tipo_endereco  = 5                             "
                + "    AND PE.id_endereco       = e.id                          "
                + "    AND MB.ds_referencia     = '" + ref + "'                 "
                + "    AND MB.id_tipo_Servico   =  " + idTipoServico + "        "
                + "    AND MB.id_servicos       =  " + idServicos + "           "
                + "    AND J.id                 = " + idJuridica;
        try {
            Query query = getEntityManager().createNativeQuery(textQuery, MensagemBoleto.class);
            List list = query.getResultList();
            if (!list.isEmpty() && list.size() == 1) {
                return (MensagemBoleto) query.getSingleResult();
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

//    public MensagemBoleto retornaDiaString(Pessoa pessoa, String ref, int idTipoServico, int idServicos) {
//        MensagemBoleto result = new MensagemBoleto();
//        JuridicaDao  = new JuridicaDBToplink();
//        Query qry = null;
//        List<Vector> listax = db.listaJuridicaContribuinteID();
//        String in = "";
//        for (int i = 0; listax.size() < i; i++) {
//            if (in.length() > 0 && i != listax.size()) {
//                in += ",";
//            }
//            in += String.valueOf(listax.get(i).get(0));
//        }
//        String textQuery = " select m                                    "
//                + "   from Juridica j,                          "
//                + "        PessoaEndereco pe,                   "
//                + "        CnaeConvencao cc,                    "
//                + "        GrupoCidades gc,                     "
//                + "        MensagemBoleto m                  "
//                + "  where j.id in (" + in + ")                 "
//                + "    and cc.cnae.id = j.cnae.id               "
//                + "    and cc.convencao.id = m.convencao.id     "
//                + "    and pe.pessoa.id = j.pessoa.id           "
//                + "    and pe.tipoEndereco.id = 5               "
//                + "    and pe.endereco.cidade.id = gc.cidade.id "
//                + "    and gc.grupoCidade.id = m.grupoCidade.id "
//                + "    and m.referencia = :referencia           "
//                + "    and m.tipoServico.id = :idTipo           "
//                + "    and m.servicos.id = :idServicos          "
//                + "    and j.pessoa.id = :pid                        ";
//        try {
//            qry = getEntityManager().createQuery(textQuery);
//            qry.setParameter("pid", pessoa.getId());
//            qry.setParameter("idTipo", idTipoServico);
//            qry.setParameter("idServicos", idServicos);
//            qry.setParameter("referencia", ref);
//            result = ((MensagemBoleto) qry.getSingleResult());
//        } catch (Exception e) {
//            result = null;
//        }
//        return result;
//    }
    public MensagemBoleto findLastMensagem(int idServicos, int idTipoServico) {
        try {
            Query query;
            String queryString = ""
                    + "SELECT max(MB.id)                                        "
                    + "  FROM fin_mensagem_boleto AS MB                         "
                    + " WHERE MB.id_tipo_servico = " + idTipoServico
                    + "   AND MB.id_servicos = " + idServicos;

            query = getEntityManager().createNativeQuery(queryString);
            List listaCount = query.getResultList();
            int idMax = (Integer) ((List) listaCount.get(0)).get(0);
            String textQuery = " "
                    + " SELECT MB                                               "
                    + "   FROM MensagemBoleto AS MB                             "
                    + "  WHERE MB.id = " + idMax;
            query = getEntityManager().createQuery(textQuery);
            List list = query.getResultList();
            if (!list.isEmpty() && list.size() == 1) {
                return (MensagemBoleto) query.getSingleResult();

            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public List<MensagemBoleto> listAllByAno(String ano) {
        try {
            String queryString = ""
                    + " SELECT MB.* "
                    + "   FROM fin_mensagem_boleto AS MB "
                    + "  WHERE substring(MB.ds_referencia,4,7) = '" + ano + "'";
            Query query = getEntityManager().createNativeQuery(queryString, MensagemBoleto.class);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }
}
