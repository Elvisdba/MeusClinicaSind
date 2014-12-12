package br.com.clinicaintegrada.coordenacao.dao;

import br.com.clinicaintegrada.coordenacao.Notificacao;
import br.com.clinicaintegrada.principal.DB;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Query;
import javax.persistence.TemporalType;

public class NotificacaoDao extends DB {

    /**
     *
     * @param idContrato
     * @param idEquipe
     * @param idTipoNotificacao
     * @param date
     * @return
     */
    public boolean exists(Integer idContrato, Integer idEquipe, Integer idTipoNotificacao, Date date) {
        try {
            Query query = getEntityManager().createQuery("SELECT N FROM Notificacao AS N WHERE N.dataLancamento = :date AND N.contrato.id = :contrato AND N.equipe.id = :equipe AND N.tipoNotificacao.id = :tipoNotificacao");
            query.setParameter("contrato", idContrato);
            query.setParameter("equipe", idEquipe);
            query.setParameter("tipoNotificacao", idTipoNotificacao);
            query.setParameter("dataEscala", date, TemporalType.DATE);
            query.setMaxResults(1);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return true;
            }
        } catch (Exception e) {
            return true;
        }
        return false;
    }

    /**
     *
     * @param idCliente
     * @param id
     * @param type
     * @return
     */
    public List findAll(Integer idCliente, Integer id, String type) {
        return findAll(idCliente, id, type, null, null);
    }

    /**
     *
     * @param idCliente
     * @param id
     * @param type
     * @param dataStart
     * @param dataFinish
     * @return
     */
    public List findAll(Integer idCliente, Integer id, String type, String dataStart, String dataFinish) {
        if (id != null) {
            return findAll(idCliente, id, dataStart, dataFinish, "", type, Integer.toString(id), null, false);
        }
        return new ArrayList();
    }

    /**
     *
     * @param idCliente
     * @param id
     * @param typeFilter
     * @param dataStart
     * @param dataFinish
     * @param startFinish
     * @param description
     * @param idTipoNotificacao
     * @param isFiltroData
     * @return
     */
    public List findAll(Integer idCliente, Integer id, String dataStart, String dataFinish, String startFinish, String typeFilter, String description, Integer idTipoNotificacao, Boolean isFiltroData) {
        try {
            String s = "";
            String f = "";
            switch (startFinish) {
                case "I":
                    s = "";
                    f = "%";
                    break;
                case "P":
                    s = "%";
                    f = "%";
                    break;
            }
            String columnDataString = "dt_lancamento";
            String whereString = "";
            String queryString = ""
                    + "      SELECT N.*                                                                 "
                    + "        FROM rot_notificacao         AS N                                        "
                    + "  INNER JOIN rot_tipo_notificacao    AS TN   ON TN.id    = N.id_tipo_notificacao ";
            switch (typeFilter) {
                case "equipe_id":
                    queryString += " INNER JOIN pes_equipe AS PE  ON PE.id = N.id_equipe AND PE.id_cliente = " + idCliente;
                    whereString += " WHERE E.id_equipe = " + description;
                    break;
                case "equipe":
                    queryString += " INNER JOIN pes_equipe   AS PE  ON PE.id = N.id_equipe AND PE.id_cliente = " + idCliente;
                    queryString += " INNER JOIN pes_pessoa AS P   ON P.id = PE.id_pessoa ";
                    whereString += " WHERE func_translate(P.ds_nome) ILIKE func_translate('" + s + description + f + "')";
                    break;
                case "paciente":
                    queryString += " INNER JOIN ctr_contrato AS C ON C.id = N.id_contrato AND C.id_cliente  = " + idCliente;
                    queryString += " INNER JOIN pes_pessoa AS P ON P.id = C.id_paciente ";
                    whereString += " WHERE func_translate(P.ds_nome) ILIKE func_translate('" + s + description + f + "')";
                    break;
                case "contrato":
                    whereString += " WHERE N.id_contrato = " + description;
                    break;
                case "hoje":
                    isFiltroData = true;
                    columnDataString = "dt_lancamento";
                    queryString += " LEFT JOIN ctr_contrato AS C ON C.id = N.id_contrato AND C.id_cliente  = " + idCliente;
                    break;
                case "periodo":
                    isFiltroData = true;
                    columnDataString = "dt_lancamento";
                    queryString += " LEFT JOIN ctr_contrato AS C ON C.id = N.id_contrato AND C.id_cliente  = " + idCliente;
                    break;
                case "lancamento":
                    isFiltroData = true;
                    columnDataString = "dt_lancamento";
                    queryString += " LEFT JOIN ctr_contrato AS C ON C.id = N.id_contrato AND C.id_cliente  = " + idCliente;
                    break;
            }
            if (idTipoNotificacao != null) {
                whereString += " AND N.id_tipo_notificacao = " + idTipoNotificacao;
            }
            if(dataStart != null) {
                isFiltroData = true;
            }
            if(isFiltroData) {
                if (dataStart != null && !dataStart.isEmpty()) {
                    String whereAnd;
                    if (whereString.isEmpty()) {
                        whereAnd = " WHERE ";
                    } else {
                        whereAnd = " AND ";
                    }
                    String di = dataStart;
                    String dataString = whereAnd + " N." + columnDataString + " = '" + di + "'";
                    if (dataFinish != null && !dataFinish.isEmpty()) {
                        String df = dataFinish;
                        dataString = whereAnd + " N." + columnDataString + " BETWEEN '" + di + "' AND '" + df + "'";
                    }
                    whereString += dataString;
                }                
            }
            queryString += whereString;
            queryString += " ORDER BY N.dt_lancamento ";
            Query query = getEntityManager().createNativeQuery(queryString, Notificacao.class);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return new ArrayList();
    }

    public List findAllByContrato(Integer idContrato) {
        try {
            Query query = getEntityManager().createQuery("SELECT N FROM Notificacao AS N WHERE N.dataLancamento = :date AND N.contrato.id = :contrato ORDER BY N.dataLancamento ASC");
            query.setParameter("contrato", idContrato);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return new ArrayList();
    }
}
