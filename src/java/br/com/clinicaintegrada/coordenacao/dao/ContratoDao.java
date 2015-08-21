package br.com.clinicaintegrada.coordenacao.dao;

import br.com.clinicaintegrada.coordenacao.Contrato;
import br.com.clinicaintegrada.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class ContratoDao extends DB {

    private String order = "ASC";
    private Integer limit = 100;
    private Integer cliente = -1;

    public boolean existeContrato(Contrato contrato) {
        try {

        } catch (Exception e) {

        }
        return false;
    }

    public List<?> find(String by, String startsOrWith, String text) {
        String querySting = "";
        String whereSting = "";
        String pessoa = "id_paciente";
        String s;
        String f;
        String orderQuery = "ds_nome";
        switch (startsOrWith) {
            case "I":
                s = "";
                f = "%";
                break;
            case "P":
                s = "%";
                f = "%";
                break;
            default:
                s = "";
                f = "";
                break;
        }
        try {
            switch (by) {
                case "paciente_nome":
                    pessoa = "id_paciente";
                    whereSting = " WHERE UPPER(func_translate(P.ds_nome)) LIKE UPPER(func_translate('" + s + text + f + "'))";
                    break;
                case "paciente_documento":
                    pessoa = "id_paciente";
                    whereSting = " WHERE P.ds_documento LIKE '" + s + text + f + "'";
                    break;
                case "responsavel_nome":
                    pessoa = "id_responsavel";
                    whereSting = " WHERE UPPER(func_translate(P.ds_nome)) LIKE UPPER(func_translate('" + s + text + f + "'))";
                    break;
                case "responsavel_documento":
                    pessoa = "id_responsavel";
                    whereSting = " WHERE P.ds_documento LIKE '" + s + text + f + "'";
                    break;
                case "cobranca2_nome":
                    pessoa = "id_cobranca2";
                    whereSting = " WHERE UPPER(func_translate(P.ds_nome)) LIKE UPPER(func_translate('" + s + text + f + "'))  AND id_cobranca2 IS NOT NULL";
                    break;
                case "cobranca2_documento":
                    pessoa = "id_cobranca2";
                    whereSting = " WHERE P.ds_documento LIKE '" + s + text + f + "' AND id_cobranca2 IS NOT NULL";
                    break;
                case "contrato":
                    Integer nrContrato;
                    try {
                        nrContrato = Integer.parseInt(text.trim());
                    } catch (Exception e) {
                        return new ArrayList();
                    }
                    whereSting = " WHERE C.id = " + nrContrato;
                    break;
            }
            querySting = ""
                    + "     SELECT C.*                                          "
                    + "       FROM ctr_contrato AS C                            "
                    + "  LEFT JOIN pes_pessoa   AS P ON P.id = C." + pessoa
                    + whereSting
                    + "        AND C.id_cliente = " + cliente
                    + "   ORDER BY P. " + orderQuery + " " + order;
            Query query = getEntityManager().createNativeQuery(querySting, Contrato.class);
            return query.getResultList();
        } catch (Exception e) {
            return new ArrayList();
        }
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getCliente() {
        return cliente;
    }

    public void setCliente(Integer cliente) {
        this.cliente = cliente;
    }

}
