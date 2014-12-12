package br.com.clinicaintegrada.coordenacao.dao;

import br.com.clinicaintegrada.coordenacao.Agendamento;
import br.com.clinicaintegrada.principal.DB;
import br.com.clinicaintegrada.utils.AnaliseString;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Query;
import javax.persistence.TemporalType;

public class AgendamentoDao extends DB {

    /**
     *
     * @param idContrato
     * @param date
     * @param hora
     * @return
     */
    public boolean exists(Integer idContrato, Date date, String hora) {
        try {
            Query query = getEntityManager().createQuery("SELECT A FROM Agendamento AS A WHERE A.contrato.id = :contrato AND A.dataAgenda = :dataAgenda AND A.horaAgenda = :horaAgenda");
            query.setParameter("contrato", idContrato);
            query.setParameter("dataAgenda", date, TemporalType.DATE);
            query.setParameter("horaAgenda", hora);
            query.setMaxResults(1);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    /**
     *
     * @param idContrato
     * @param actives
     * @return
     */
    public List findAllByContrato(Integer idContrato, Boolean actives) {
        try {
            String queryString = " "
                    + "     SELECT A.*                                                              "
                    + "       FROM rot_agendamento  AS A                                            "
                    + " INNER JOIN ctr_contrato     AS C  ON C.id               = A.id_contrato     "
                    + " INNER JOIN rot_evento       AS E  ON E.id               = A.id_evento       "
                    + " INNER JOIN pes_pessoa       AS PR ON PR.id              = C.id_responsavel  "
                    + " INNER JOIN pes_pessoa       AS PC ON PC.id              = C.id_paciente     "
                    + "      WHERE A.id_contrato    = " + idContrato;
            if (actives) {
                queryString += " AND A.dt_atendimento IS NULL ";
            }
            queryString += " ORDER BY A.dt_agenda ";
            Query query = getEntityManager().createNativeQuery(queryString, Agendamento.class);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return new ArrayList();
    }

    /**
     * Contrato / Data / Paciente / Responsável / Evento
     *
     * @param startFinish
     * @param by
     * @param description
     * @param dataStart
     * @param dataFinish
     * @param idCliente
     * @param idEvento
     * @return
     */
    public List findAll(String startFinish, String by, String description, String dataStart, String dataFinish, Integer idCliente, Integer idEvento) {
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
            String order = " ";
            String queryString = " "
                    + "     SELECT A.*                                                              "
                    + "       FROM rot_agendamento  AS A                                            "
                    + " INNER JOIN ctr_contrato     AS C  ON C.id               = A.id_contrato     "
                    + " INNER JOIN rot_evento       AS E  ON E.id               = A.id_evento       "
                    + " INNER JOIN pes_pessoa       AS PR ON PR.id              = C.id_responsavel  "
                    + " INNER JOIN pes_pessoa       AS PC ON PC.id              = C.id_paciente     ";
            switch (by) {
                case "paciente":
                    queryString += " WHERE UPPER(func_translate(PC.ds_nome)) LIKE '" + s + AnaliseString.removerAcentos(description.toUpperCase()) + f + "' ";
                    break;
                case "responsavel":
                    queryString += " WHERE UPPER(func_translate(PR.ds_nome)) LIKE '" + s + AnaliseString.removerAcentos(description.toUpperCase()) + f + "' ";
                    break;
                case "contrato":
                    queryString += " WHERE C.id = " + description;
                    break;
                case "periodo":
                    if (dataFinish == null || dataFinish.isEmpty()) {
                        queryString += " WHERE A.dt_agenda >= '" + dataStart + "' ";
                    } else {
                        queryString += " WHERE A.dt_agenda BETWEEN '" + dataStart + "' AND '" + dataFinish + "' ";
                    }
                    order = " A.dt_agenda, A.ds_hora_agenda, ";
                    break;
                case "lancamento":
                    if (dataFinish == null || dataFinish.isEmpty()) {
                        queryString += " WHERE A.dt_lancamento >= '" + dataStart + "' ";
                    } else {
                        queryString += " WHERE A.dt_lancamento BETWEEN '" + dataStart + "' AND '" + dataFinish + "' ";
                    }
                    order = " A.dt_lancamento, A.ds_hora_agenda, ";
                    break;
                case "atendimento":
                    if (dataFinish == null || dataFinish.isEmpty()) {
                        queryString += " WHERE A.dt_atendimento >= '" + dataStart + "' ";
                    } else {
                        queryString += " WHERE A.dt_atendimento BETWEEN '" + dataStart + "' AND '" + dataFinish + "' ";
                    }
                    order = " A.dt_atendimento, A.ds_hora_atendimento, ";
                    break;
                case "hoje":
                    queryString += " WHERE A.dt_agenda = '" + dataStart + "'";
                    order = " A.dt_agenda, A.ds_hora_agenda, ";
                    break;
                case "ontem":
                    queryString += " WHERE A.dt_agenda = '" + dataStart + "'";
                    order = " A.dt_agenda, A.ds_hora_agenda, ";
                    break;
            }
            if (idEvento != null) {
                queryString += " AND A.id_evento = " + idEvento;
            }
            queryString += " AND C.id_cliente = " + idCliente;
            queryString += " ORDER BY " + order + " PC.ds_nome ";
            Query query = getEntityManager().createNativeQuery(queryString, Agendamento.class);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {

        }
        return new ArrayList();
    }
}
