package br.com.clinicaintegrada.fichamedica.dao;

import br.com.clinicaintegrada.coordenacao.Agendamento;
import br.com.clinicaintegrada.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class MenuEquipeTecnicaDao extends DB {

    private String inAtendimento = "1,2";
    private Integer limit = 150;

    // _________________
    // >>>> STATUS <<<<*|
    // *****************|
    //                  |
    // 1 - AGENDADO     |
    // 2 - ATENDIMENTO  |
    // 3 - CONCLUÍDO    |
    // 4 - CANCELADO    |
    // 5 - FALTOU       |
    // 6 - ENFERMARIA   |
    // _________________|
    //
    /**
     * Tipo de atendimento 1 / 2
     *
     * @param idStatus
     * @param idPessoa
     * @param date
     * @param startFinish ("aaaa%") ou ("%aaaa%")
     * @param by (Tipo da pesquisa)
     * @param description (Descrição da pesquisa)
     * @param atendimemto (Atendimento pela pessoa atual)
     * @param isEnfermaria
     * @return
     */
    public List findAllAgendamentoByEquipe(Integer idStatus, Integer idPessoa, String date[], String startFinish, String by, String description, Boolean atendimemto, Boolean isEnfermaria) {
        String s = "";
        String f = "";
        if (startFinish != null) {
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
        }

        try {
            String queryString;
            if (isEnfermaria) {
                inAtendimento = "1";
                queryString = ""
                        + "     SELECT A.*                                                          "
                        + "       FROM rot_agendamento  AS A                                        "
                        + "  LEFT JOIN pes_equipe       AS EQ  ON EQ.id  = A.id_equipe              "
                        + "  LEFT JOIN pes_pessoa       AS PEQ ON PEQ.id = EQ.id_pessoa             "
                        + "      WHERE A.id_funcao_equipe IN(                                       "
                        + "         SELECT E.id_funcao_equipe                                       "
                        + "           FROM pes_equipe        AS E                                   "
                        + "     INNER JOIN pes_funcao_equipe AS PEI ON PEI.id = E.id_funcao_equipe  "
                        + "          WHERE PEI.id_tipo_atendimento IN(" + inAtendimento + ")        "
                        + "            AND PEI.is_enfermaria = true                                 ";
            } else {
                queryString = ""
                        + "     SELECT A.*                                                          "
                        + "       FROM rot_agendamento  AS A                                        "
                        + "  LEFT JOIN pes_equipe       AS EQ  ON EQ.id  = A.id_equipe              "
                        + "  LEFT JOIN pes_pessoa       AS PEQ ON PEQ.id = EQ.id_pessoa             "
                        + "      WHERE A.id_funcao_equipe IN(                                       "
                        + "         SELECT E.id_funcao_equipe                                       "
                        + "           FROM pes_equipe        AS E                                   "
                        + "     INNER JOIN pes_funcao_equipe AS PEI ON PEI.id = E.id_funcao_equipe  "
                        + "          WHERE PEI.id_tipo_atendimento IN(" + inAtendimento + ")        ";
//                queryString = ""
//                        + "     SELECT A.*                                                          "
//                        + "       FROM rot_agendamento  AS A                                        "
//                        + "  LEFT JOIN pes_equipe       AS EQ  ON EQ.id  = A.id_equipe              "
//                        + "  LEFT JOIN pes_pessoa       AS PEQ ON PEQ.id = EQ.id_pessoa             "
//                        + "      WHERE A.dt_lancamento IS NOT NULL                                  ";
            }
            if (idPessoa != null && !isEnfermaria) {
                queryString += " AND E.id_pessoa = " + idPessoa;
            }
            queryString += ") ";
            queryString += " AND A.id_evento = 26 ";
            if (idStatus != null) {
                if (idStatus != 0) {
                    queryString += " AND A.id_status = " + idStatus;
                }
            }
            if (atendimemto != null) {
                if (atendimemto) {
                    queryString += " AND PEQ.id = " + idPessoa;
                }
            }
            String queryStringDate = "";
            if (idStatus == 0 || idStatus == 1 || idStatus == 3 || idStatus == 4 || idStatus == 5) {
                if (date[0] != null && !date[0].isEmpty()) {
                    queryStringDate = " AND A.dt_agenda = '" + date[0] + "'";
                    if (date[1] != null && !date[1].isEmpty()) {
                        queryStringDate = " AND A.dt_agenda BETWEEN '" + date[0] + "' AND '" + date[1] + "'";
                    }
                }
                queryString += queryStringDate;
            }
            queryString += " ORDER BY A.dt_agenda DESC, A.ds_hora_agenda ASC ";
            queryString += " LIMIT " + limit;
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

    public String getInAtendimento() {
        return inAtendimento;
    }

    public void setInAtendimento(String inAtendimento) {
        this.inAtendimento = inAtendimento;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
