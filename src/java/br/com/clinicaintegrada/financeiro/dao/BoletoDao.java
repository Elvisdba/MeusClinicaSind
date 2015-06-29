package br.com.clinicaintegrada.financeiro.dao;

import br.com.clinicaintegrada.financeiro.Boleto;
import br.com.clinicaintegrada.financeiro.BoletosVw;
import br.com.clinicaintegrada.principal.DB;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Query;

public class BoletoDao extends DB {

    public Boleto findBoletoByNrCtrBoleto(String nrCtrBoleto) {
        try {
            Query qry = getEntityManager().createQuery(
                    "  SELECT B                             "
                    + "  FROM Boleto AS B                   "
                    + " WHERE B.nrCtrBoleto = '" + nrCtrBoleto + "'");
            List list = qry.getResultList();
            if (!list.isEmpty()) {
                return (Boleto) list.get(0);
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public List listBoletosVwByNrCtrBoleto(String nrCtrBoleto) {
        try {
            Query qry = getEntityManager().createNativeQuery(
                    "  SELECT B.*                             "
                    + "  FROM boletos_vw AS B                   "
                    + " WHERE B.nr_ctr_boleto = '" + nrCtrBoleto + "'");
            List list = qry.getResultList();
            if (!list.isEmpty()) {
                return converte(list);
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return new ArrayList();
    }

    public BoletosVw findBoletosVwByNrCtrBoleto(String nrCtrBoleto) {
        try {
            return (BoletosVw) listBoletosVwByNrCtrBoleto(nrCtrBoleto).get(0);
        } catch (Exception e) {
            return null;
        }

    }

    public int saveBoletoNativeQuery(int idContaCobranca, int idCliente) {
        try {
            String queryString = "INSERT INTO fin_boleto (id_conta_cobranca, is_ativo, id_cliente) VALUES (" + idContaCobranca + ", true, " + idCliente + ")";
            Query query = getEntityManager().createNativeQuery(queryString);

            getEntityManager().getTransaction().begin();
            query.executeUpdate();
            getEntityManager().getTransaction().commit();

            queryString = "SELECT MAX(id) FROM fin_boleto ";
            query = getEntityManager().createNativeQuery(queryString);
            List list = (List) query.getResultList();
            return (Integer) ((List) list.get(0)).get(0);
        } catch (Exception e) {
            return -1;
        }
    }

    // NOVO
    public List listaBoletos(String nr_ctr_boleto) {
        try {
            String queryString = "SELECT B.* FROM boletos_vw AS B WHERE B.nr_ctr_boleto IN ('" + nr_ctr_boleto + "')";
            Query query = getEntityManager().createNativeQuery(queryString);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return converte(list);
            }
        } catch (Exception e) {
            return new ArrayList<>();
        }
        return new ArrayList<>();
    }

    public List finBoletosByLote(Integer idLote) {
        String queryString = ""
                + "    SELECT *                                                                             "
                + "      FROM boletos_vw                                                                    "
                + "     WHERE id_fin_lote  = " + idLote
                + "  ORDER BY vencimento, nr_ctr_boleto DESC";

        try {
            Query query = getEntityManager().createNativeQuery(queryString);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return converte(list);
            }
        } catch (Exception e) {
            return new ArrayList<>();
        }
        return new ArrayList<>();
    }

    public List listaBoletosGroup(String responsavel, String lote, String data) {
        List<BoletosVw> boletosVws = new ArrayList();
        String queryWhere = "";
        if (!responsavel.isEmpty()) {
            queryWhere = " WHERE UPPER(responsavel) LIKE '%" + responsavel.toUpperCase() + "%'";
        }
        if (!lote.isEmpty() && responsavel.isEmpty()) {
            queryWhere += " WHERE id_lote_boleto = " + Integer.valueOf(lote);
        } else if (!lote.isEmpty()) {
            queryWhere += " AND id_lote_boleto = " + Integer.valueOf(lote);
        }
        if (!data.isEmpty() && responsavel.isEmpty() && lote.isEmpty()) {
            queryWhere += " WHERE processamento = '" + data + "'";
        } else if (!data.isEmpty()) {
            queryWhere += " AND processamento = '" + data + "'";
        }

        String queryString = ""
                + "    SELECT -1 AS id,                                                                     "
                + "           nr_ctr_boleto,                                                                "
                + "           id_lote_boleto,                                                               "
                + "           responsavel,                                                                  "
                + "           boleto,                                                                       "
                + "           vencimento,                                                                   "
                + "           processamento,                                                                "
                + "           sum(valor) as valor                                                           "
                + "      FROM boletos_vw " + queryWhere + "                                                 "
                + "  GROUP BY nr_ctr_boleto, id_lote_boleto, responsavel, boleto, vencimento, processamento "
                + "  ORDER BY responsavel, vencimento DESC";
        try {
            Query query = getEntityManager().createNativeQuery(queryString);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                for (Object list1 : list) {
                    Object[] objects = ((List) list1).toArray();
                    BoletosVw boletosVw = new BoletosVw();
                    boletosVw.setId((Long) Long.parseLong(Integer.toString((int) objects[0])));
                    boletosVw.setNrCtrBoleto((String) objects[1]);
                    boletosVw.setLoteBoleto((Integer) objects[2]);
                    boletosVw.setResponsavel((String) objects[3]);
                    boletosVw.setBoleto((String) objects[4]);
                    boletosVw.setVencimento((Date) objects[5]);
                    boletosVw.setProcessamento((Date) objects[6]);
                    boletosVw.setValor((Double) objects[7]);
                    boletosVws.add(boletosVw);
                }
                return boletosVws;
            }
        } catch (Exception e) {
            return new ArrayList<>();
        }
        return new ArrayList<>();
    }

    public List converte(List list) {
        List<BoletosVw> boletosVws = new ArrayList();
//         0 - row_number()
//        01 - l.id
//        02 - m.id AS
//        03 - m.nr_ctr_boleto
//        04 - sl.id
//        05 - sl.dt_processamento
//        06 - '' AS logo_banco,
//        07 - '' AS logo,
//        08 - '' AS logo_informativo,
//        09 - '' AS logo_verso,
//        10 - pr.id AS codigo,
//        11 - pr.ds_nome AS responsavel,
//        12 - '1997-10-07'::date + "substring"(m.nr_ctr_boleto::text, 9, 4)::integer AS vencimento,
//        13 - se.ds_descricao AS servico,
//        14 - m.nr_valor AS valor,
//        15 - 0 AS mensalidades_corrigidas,
//        16 - 'NÃO RECEBER APÓS O VENCIMENTO.' AS mensagem_boleto,
//        17 - bco.nr_num_banco AS banco,
//        18 - cb.ds_agencia AS agencia,
//        19 - c.ds_cod_cedente AS cedente,
//        20 - b.ds_boleto AS boleto,
//        21 - f.juremail1 AS email,
//        22 - f.jurnome AS nome_filial,
//        23 - f.jursite AS silte_filial,
//        24 - f.jurdocumento AS cnpj_filial,
//        25 - f.jurtelefone AS tel_filial,
//        26 - (((((f.jurlogradouro::text || ' '::text) || f.jurendereco::text) || ', '::text) || f.jurnumero::text) || ' '::text) || f.jurcomplemento::text AS endereco_filial,
//        27 - f.jurbairro AS bairro_filial,
//        28 - f.jurcidade AS cidade_filial,
//        29 - f.juruf AS uf_filial,
//        30 - ("substring"(f.jurcep::text, 1, 5) || '-'::text) || "right"(f.jurcep::text, 3) AS cep_filial,
//        31 - er.logradouro AS logradouro_responsavel,
//        32 - rtrim(((((er.endereco::text || ', '::text) || per.ds_numero::text) || ' '::text) || per.ds_complemento::text) || er.bairro::text) AS endereco_responsavel,
//        33 - ("left"(er.cep::text, 5) || '-'::text) || "right"(er.cep::text, 3) AS cep_responsavel,
//        34 - er.uf AS uf_responsavel,
//        35 - er.cidade AS cidade_responsavel,
//        36 - '** TEXTO LIVRE **' AS informativo,
//        37 - '** Local de Pagamento **' AS local_pagamento,        
        try {
            for (Object list1 : list) {
                Object[] objects = ((List) list1).toArray();
                BoletosVw boletosVw = new BoletosVw();
                boletosVw.setId((Long) objects[0]);
                boletosVw.setLote((Integer) objects[1]);
                boletosVw.setMovimento((Integer) objects[2]);
                boletosVw.setNrCtrBoleto((String) objects[3]);
                boletosVw.setLoteBoleto((Integer) objects[4]);
                boletosVw.setProcessamento((Date) objects[5]);
                boletosVw.setCodigo((Integer) objects[10]);
                boletosVw.setResponsavel((String) objects[11]);
                boletosVw.setVencimento((Date) objects[12]);
                boletosVw.setGrupoCategoria(null);
                boletosVw.setCategoria(null);
                boletosVw.setServico((String) objects[13]);
                boletosVw.setPessoa(null);
                boletosVw.setNomeBeneficiario(null);
                boletosVw.setValor((Double) objects[14]);
                boletosVw.setMensalidadesCorrigidas((Integer) objects[15]);
                boletosVw.setMensagemBoleto((String) objects[16]);
                boletosVw.setBanco((String) objects[17]);
                boletosVw.setAgencia((String) objects[18]);
                boletosVw.setCedente((String) objects[19]);
                boletosVw.setBoleto((String) objects[20]);
                boletosVw.setEmail((String) objects[21]);
                boletosVw.setNomeFilial((String) objects[22]);
                boletosVw.setSilteFilial((String) objects[23]);
                boletosVw.setCnpjFilial((String) objects[24]);
                boletosVw.setTelefoneFilial((String) objects[25]);
                boletosVw.setEnderecoFilial((String) objects[26]);
                boletosVw.setBairroFilial((String) objects[27]);
                boletosVw.setCidadeFilial((String) objects[28]);
                boletosVw.setUfFilial((String) objects[29]);
                boletosVw.setCepFilial((String) objects[30]);
                boletosVw.setLogradouroResponsavel((String) objects[31]);
                boletosVw.setEnderecoResponsavel((String) objects[32]);
                boletosVw.setCepResponsavel((String) objects[33]);
                boletosVw.setUfResponsavel((String) objects[34]);
                boletosVw.setCidadeResponsavel((String) objects[35]);
                boletosVw.setInformativo((String) objects[36]);
                boletosVw.setLocalPagamento((String) objects[37]);
                boletosVws.add(boletosVw);
            }
        } catch (Exception e) {
            return new ArrayList<>();
        }
        return boletosVws;
    }

}
