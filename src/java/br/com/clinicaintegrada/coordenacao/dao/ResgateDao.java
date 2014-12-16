package br.com.clinicaintegrada.coordenacao.dao;

import br.com.clinicaintegrada.coordenacao.Escala;
import br.com.clinicaintegrada.coordenacao.Resgate;
import br.com.clinicaintegrada.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class ResgateDao extends DB {

    /**
     *
     * @param idPaciente
     * @param dataSaida
     * @return
     */
    public boolean exists(Integer idPaciente, String dataSaida) {
        try {
            Query query = getEntityManager().createQuery("SELECT R FROM Resgate AS R WHERE R.paciente.id = :paciente AND R.dataSaida = :dataSaida ");
            query.setParameter("paciente", idPaciente);
            query.setParameter("dataEscala", dataSaida);
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
     * @param isFiltroData
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
            String columnDataString = "dt_saida";
            String whereString = "";
            String queryString = ""
                    + "      SELECT R.*                                                                             "
                    + "        FROM rot_resgate AS R                                                             ";
            switch (typeFilter) {
                case "tecnico":
                    queryString += " INNER JOIN pes_equipe   AS PE  ON PE.id = R.id_motorista AND PE.id_cliente = " + idCliente;
                    queryString += " INNER JOIN pes_pessoa AS P   ON P.id = PE.id_pessoa ";
                    whereString += " WHERE func_translate(P.ds_nome) ILIKE func_translate('" + s + description + f + "')";
                    break;
                case "motorista":
                    queryString += " INNER JOIN pes_equipe   AS PE  ON PE.id = R.id_tecnico AND PE.id_cliente = " + idCliente;
                    queryString += " INNER JOIN pes_pessoa AS P   ON P.id = PE.id_pessoa ";
                    whereString += " WHERE func_translate(P.ds_nome) ILIKE func_translate('" + s + description + f + "')";
                    break;
                case "paciente":
                    queryString += " INNER JOIN pes_pessoa AS P ON P.id = R.id_paciente AND P.id_cliente = " + idCliente;
                    whereString += " WHERE func_translate(P.ds_nome) ILIKE func_translate('" + s + description + f + "')";
                    break;
                case "solicitante":
                    queryString += " INNER JOIN pes_pessoa AS P ON P.id = R.id_solicitante AND P.id_cliente = " + idCliente;
                    whereString += " WHERE func_translate(P.ds_nome) ILIKE func_translate('" + s + description + f + "')";
                    break;
                case "emissao":
                    columnDataString = "dt_emissao";
                    break;
                case "hoje":
                    columnDataString = "dt_saida";
                    break;
                case "saida":
                    columnDataString = "dt_saida";
                    break;
                case "retorno":
                    columnDataString = "dt_retorno";
                    break;
            }
            if (idFuncaoEscala != null) {
                whereString += " AND R.id_veiculo = " + idFuncaoEscala;
            }
            if (dataStart != null) {
                if (!dataStart.isEmpty()) {
                    String whereAnd;
                    if (whereString.isEmpty()) {
                        whereAnd = " WHERE ";
                    } else {
                        whereAnd = " AND ";
                    }
                    String di = dataStart;
                    String dataString = whereAnd + " R." + columnDataString + " = '" + di + "'";
                    if (dataFinish != null && !dataFinish.isEmpty()) {
                        String df = dataFinish;
                        dataString = whereAnd + " R." + columnDataString + " BETWEEN '" + di + "' AND '" + df + "'";
                    }
                    whereString += dataString;
                }
            }
            queryString += whereString;
            queryString += " ORDER BY R." + columnDataString + " , R.ds_hora_saida";
            Query query = getEntityManager().createNativeQuery(queryString, Resgate.class);
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
