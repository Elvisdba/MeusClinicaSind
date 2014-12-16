package br.com.clinicaintegrada.coordenacao.dao;

import br.com.clinicaintegrada.coordenacao.Transferencia;
import br.com.clinicaintegrada.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class TransferenciaDao extends DB {

    /**
     *
     * @param idContrato
     * @param idFilialAtual
     * @param idFilialDestino
     * @param dataSaida
     * @return
     */
    public boolean exists(Integer idContrato, Integer idFilialAtual, Integer idFilialDestino, String dataSaida) {
        try {
            Query query = getEntityManager().createQuery("SELECT T FROM Transferencia AS T WHERE T.contrato.id = :contrato AND T.filialAtual.id = :filialAtual AND T.filialDestino.id = :filialDestino AND T.dataSaida = :dataSaida ");
            query.setParameter("contrato", idContrato);
            query.setParameter("filialAtual", idFilialAtual);
            query.setParameter("filialDestino", idFilialDestino);
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
     * @param dataStart
     * @param dataFinish
     * @param idContrato
     * @return 
     */
    public List findAll(Integer idCliente, String dataStart, String dataFinish, Integer idContrato) {
        return findAll(idCliente, dataStart, dataFinish, "", "contrato_id", "" + idContrato, null, null);
    }

    /**
     *
     * @param idCliente
     * @param dataStart
     * @param dataFinish
     * @param type
     * @param startFinish
     * @param description
     * @param idFilialAtual
     * @param idFilialDestino
     * @return
     */
    public List findAll(Integer idCliente, String dataStart, String dataFinish, String startFinish, String type, String description, Integer idFilialAtual, Integer idFilialDestino) {
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
                    + "      SELECT T.*                                                                                     "
                    + "        FROM rot_transferencia AS T                                                                  ";
            switch (type) {
                case "contrato_id":
                    whereString += " WHERE T.id_contrato = " + description;
                    break;
                case "paciente":
                    queryString += " INNER JOIN ctr_contrato AS C ON C.id = T.id_contrato AND C.id_cliente = " + idCliente;
                    queryString += " INNER JOIN pes_pessoa AS P ON P.id = C.id_paciente ";
                    whereString += " WHERE func_translate(P.ds_nome) ILIKE func_translate('" + s + description + f + "')";
                    break;
                case "equipe":
                    queryString += " INNER JOIN pes_equipe AS PE ON PE.id = T.id_equipe AND PE.id_cliente = " + idCliente;
                    queryString += " INNER JOIN pes_pessoa AS P ON P.id = PE.id_pessoa";
                    whereString += " WHERE func_translate(P.ds_nome) ILIKE func_translate('" + s + description + f + "')";
                    break;
                case "hoje":
                    columnDataString = "dt_chegada";
                    break;
                case "saida":
                    columnDataString = "dt_saida";
                    break;
                case "chegada":
                    columnDataString = "dt_chegada";
                    break;
                case "lancamento":
                    columnDataString = "dt_lancamento";
                    break;
            }
            if (idFilialAtual != null) {
                whereString += " AND T.id_filial_atual = " + idFilialAtual;
            }
            if (idFilialDestino != null) {
                whereString += " AND T.id_filial_destino = " + idFilialDestino;
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
                    String dataString = whereAnd + " T." + columnDataString + " = '" + di + "'";
                    if (dataFinish != null && !dataFinish.isEmpty()) {
                        String df = dataFinish;
                        dataString = whereAnd + " T." + columnDataString + " BETWEEN '" + di + "' AND '" + df + "'";
                    }
                    whereString += dataString;
                }
            }
            queryString += whereString;
            queryString += " ORDER BY T." + columnDataString + " , T.ds_hora_saida";
            Query query = getEntityManager().createNativeQuery(queryString, Transferencia.class);
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
