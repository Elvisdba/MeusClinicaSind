package br.com.clinicaintegrada.coordenacao.dao;

import br.com.clinicaintegrada.coordenacao.Escala;
import br.com.clinicaintegrada.principal.DB;
import br.com.clinicaintegrada.seguranca.controleUsuario.SessaoCliente;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Query;
import javax.persistence.TemporalType;

public class EscalaDao extends DB {

    /**
     *
     * @param id
     * @param type
     * @param date
     * @return
     */
    public boolean exists(String type, Integer id, Date date) {
        try {
            Query query;
            if (type.equals("equipe")) {
                query = getEntityManager().createQuery("SELECT E FROM Escala AS E WHERE E.dataEscala = :date AND E.equipe.id = :id");
            } else {
                query = getEntityManager().createQuery("SELECT E FROM Escala AS E WHERE E.dataEscala = :date AND E.paciente.id = :id");
            }
            query.setParameter("id", id);
            query.setParameter("dataEscala", date, TemporalType.DATE);
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
     * @param idFuncaoEscala
     * @return
     */
    public List findAll(Integer idCliente, Integer id, String dataStart, String dataFinish, String startFinish, String typeFilter, String description, Integer idFuncaoEscala, Boolean isFiltroData) {
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
            String columnDataString = "dt_escala";
            String whereString = "";
            String queryString = ""
                    + "      SELECT E.*                                                                         "
                    + "        FROM rot_escala AS E                                                             ";
            switch (typeFilter) {
                case "equipe_id":
                    queryString += " INNER JOIN pes_equipe AS PE  ON PE.id = E.id_equipe AND PE.id_cliente = " + idCliente;
                    whereString += " WHERE E.id_equipe = " + description;
                    break;
                case "equipe":
                    queryString += " INNER JOIN pes_equipe   AS PE  ON PE.id = E.id_equipe AND PE.id_cliente = " + idCliente;
                    queryString += " INNER JOIN pes_pessoa AS P   ON P.id = PE.id_pessoa ";
                    whereString += " WHERE func_translate(P.ds_nome) ILIKE func_translate('" + s + description + f + "')";
                    break;
                case "paciente":
                    queryString += " INNER JOIN ctr_contrato AS C ON C.id = E.id_paciente AND C.id_cliente  = " + idCliente;
                    queryString += " INNER JOIN pes_pessoa AS P ON P.id = C.id_paciente ";
                    whereString += " WHERE func_translate(P.ds_nome) ILIKE func_translate('" + s + description + f + "')";
                    break;
                case "contrato":
                    whereString += " WHERE E.id_paciente = " + description;
                    break;
                case "hoje":
                    isFiltroData = true;
                    columnDataString = "dt_escala";
                    queryString += " LEFT JOIN ctr_contrato AS C ON C.id = E.id_paciente AND C.id_cliente  = " + idCliente;
                    queryString += " LEFT JOIN pes_equipe AS PE ON PE.id = E.id_equipe AND PE.id_cliente  = " + idCliente;
                    break;
                case "periodo":
                    isFiltroData = true;
                    columnDataString = "dt_escala";
                    queryString += " LEFT JOIN ctr_contrato AS C ON C.id = E.id_paciente AND C.id_cliente  = " + idCliente;
                    queryString += " LEFT JOIN pes_equipe AS PE ON PE.id = E.id_equipe AND PE.id_cliente  = " + idCliente;
                    break;
                case "lancamento":
                    isFiltroData = true;
                    columnDataString = "dt_lancamento";
                    queryString += " LEFT JOIN ctr_contrato AS C ON C.id = E.id_paciente AND C.id_cliente  = " + idCliente;
                    queryString += " LEFT JOIN pes_equipe AS PE ON PE.id = E.id_equipe AND PE.id_cliente  = " + idCliente;
                    break;
            }
            if (idFuncaoEscala != null) {
                whereString += " AND E.id_funcao_escala = " + idFuncaoEscala;
            }
            if (dataStart != null) {
                isFiltroData = true;
            }
            if (isFiltroData) {
                if (dataStart != null && !dataStart.isEmpty()) {
                    String whereAnd;
                    if (whereString.isEmpty()) {
                        whereAnd = " WHERE ";
                    } else {
                        whereAnd = " AND ";
                    }
                    String di = dataStart;
                    String dataString = whereAnd + " E." + columnDataString + " = '" + di + "'";
                    if (dataFinish != null && !dataFinish.isEmpty()) {
                        String df = dataFinish;
                        dataString = whereAnd + " E." + columnDataString + " BETWEEN '" + di + "' AND '" + df + "'";
                    }
                    whereString += dataString;
                }
            }
            queryString += whereString;
            queryString += " ORDER BY E." + columnDataString + " , E.ds_hora_inicial, E.ds_hora_final ";
            Query query = getEntityManager().createNativeQuery(queryString, Escala.class);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return new ArrayList();
    }

    public List findAllForResgate(String date, String h1, String inIdEscalas) {
        try {
            String queryString = ""
                    + "    SELECT E.*                                                                         "
                    + "      FROM rot_escala AS E                                                             "
                    + "INNER JOIN pes_equipe AS PE ON PE.id = E.id_equipe AND PE.id_cliente = " + SessaoCliente.get().getId()
                    + "     WHERE dt_escala >= '" + date + "'                                                 "
                    + "       AND '" + h1 + "' BETWEEN E.ds_hora_inicial AND E.ds_hora_final                  "
                    + "       AND id_paciente IS NULL                                                         "
                    + "       AND E.id NOT IN( SELECT id_apoio1 FROM rot_resgate WHERE dt_saida = '" + date + "') "
                    + "       AND E.id NOT IN( SELECT id_apoio2 FROM rot_resgate WHERE dt_saida = '" + date + "') "
                    + "       AND E.id NOT IN( SELECT id_apoio3 FROM rot_resgate WHERE dt_saida = '" + date + "') "
                    + "       AND E.id NOT IN( SELECT id_apoio4 FROM rot_resgate WHERE dt_saida = '" + date + "') ";
            if (!inIdEscalas.isEmpty()) {
                queryString += " AND E.id NOT IN (" + inIdEscalas + ") ";
            }
            Query query = getEntityManager().createNativeQuery(queryString, Escala.class);
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
