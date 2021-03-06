//package br.com.clinicaintegrada.relatorios.dao;
//
//import br.com.rtools.principal.DB;
//import br.com.clinicaintegrada.relatorios.Relatorios;
//import br.com.rtools.utilitarios.DataHoje;
//import java.util.ArrayList;
//import java.util.List;
//import javax.persistence.Query;
//
//public class RelatorioMatriculaEscolaDao extends DB {
//
//    private String order = "";
//
//    /**
//     *
//     * @param relatorios
//     * @param filial
//     * @param periodoMatricula
//     * @param periodo
//     * @param nascimento
//     * @param horario
//     * @param midia
//     * @param status
//     * @param vendedor
//     * @param tipoMatricula
//     * @param idTurmaOuCurso
//     * @param aluno
//     * @param professor
//     * @param responsavel
//     * @param sexo
//     * @return
//     */
//    public List find(Relatorios relatorios, Integer filial, String periodoMatricula[], String periodo[], String nascimento[], Integer status, Integer midia, Integer professor, Integer vendedor, Boolean tipoMatricula, Integer idTurmaOuCurso, Integer aluno, String sexo, Integer responsavel, String horario[]) {
//        String asString = "";
//        String joinString = "";
//        List listQuery = new ArrayList();
//        try {
//            if (tipoMatricula) {
//                asString += " T.dt_inicio, ";
//                asString += " T.dt_termino, ";
//                joinString += " INNER JOIN esc_matr_turma AS MT ON MT.id_matr_escola = ME.id ";
//                joinString += " INNER JOIN esc_turma AS T ON T.id = MT.id_turma ";
//                joinString += " INNER JOIN fin_servicos AS S ON S.id = T.id_curso ";
//                if (idTurmaOuCurso == null) {
//                    if (periodo[1].isEmpty()) {
//                        if (!periodo[0].isEmpty()) {
//                            listQuery.add("T.dt_inicio = '" + periodo[0] + "'");
//                        }
//                    } else {
//                        listQuery.add("T.dt_inicio >= '" + periodo[0] + "' AND T.dt_termino <= '" + periodo[1] + "'");
//                    }
//                    if (horario[1].isEmpty()) {
//                        if (!horario[0].isEmpty()) {
//                            listQuery.add("T.tm_inicio = '" + horario[0] + "'");
//                        }
//                    } else {
//                        listQuery.add("T.tm_inicio  >= '" + horario[0] + "' AND T.tm_termino <= '" + horario[1] + "'");
//                    }
//                } else {
//                    listQuery.add("T.id = " + idTurmaOuCurso);
//                }
//            } else {
//                asString += " MI.dt_inicio, ";
//                asString += " MI.dt_termino, ";
//                joinString += " INNER JOIN esc_matr_individual AS MI ON MI.id_matr_escola = ME.id ";
//                joinString += " INNER JOIN fin_servicos AS S ON S.id = MI.id_curso ";
//                joinString += " LEFT JOIN esc_professor AS PROF ON PROF.id = MI.id_professor ";
//                if (idTurmaOuCurso != null) {
//                    listQuery.add("MI.id_curso = " + idTurmaOuCurso);
//                }
//                if (vendedor != null) {
//                    listQuery.add("MI.id_professor = " + professor);
//                }
//                if (periodo[1].isEmpty()) {
//                    if (!periodo[0].isEmpty()) {
//                        listQuery.add("MI.dt_inicio = '" + periodo[0] + "'");
//                    }
//                } else {
//                    listQuery.add("MI.dt_inicio >= '" + periodo[0] + "' AND MI.dt_termino <= '" + periodo[1] + "'");
//                }
//                if (horario[1].isEmpty()) {
//                    if (!periodo[0].isEmpty()) {
//                        listQuery.add("MI.tm_inicio = '" + horario[0] + "'");
//                    }
//                } else {
//                    listQuery.add("MI.tm_inicio  >= '" + horario[0] + "' AND MI.tm_termino <= '" + horario[1] + "'");
//                }
//            }
//            String queryString;
//            queryString = ""
//                    + "      SELECT P.ds_nome,                                                                      " // 0  - NOME
//                    + "             F.dt_nascimento,                                                                " // 1  - NASCIMENTO
//                    + "             F.ds_sexo,                                                                      " // 2  - SEXO
//                    + "             ST.ds_descricao,                                                                " // 3  - MATRÍCULA STATUS
//                    + "             S.ds_descricao,                                                                 " // 4  - SERVIÇO
//                    + "         " + asString // 5 INICIO - 6 TÉRMINO
//                    + "             SVW.categoria                                                                   " // 7 - CATEGORIA DE SÓCIOS
//                    + "        FROM matr_escola AS ME                                                               "
//                    + "  INNER JOIN pes_fisica              AS F    ON F.id_pessoa   = ME.id_aluno                  "
//                    + "  INNER JOIN pes_pessoa              AS P    ON P.id          = F.id_pessoa                  "
//                    + "   LEFT JOIN soc_socios_vw           AS SVW  ON SVW.codsocio  = F.id_pessoa                  "
//                    + "   LEFT JOIN esc_status              AS ST   ON ST.id         = ME.id_status                 "
//                    + "  INNER JOIN esc_vendedor            AS V    ON V.id          = ME.id_vendedor               "
//                    + "         " + joinString
//                    + "";
//            if (filial != null) {
//                listQuery.add("ME.id_filial = " + filial);
//            }
//            if (periodoMatricula[1].isEmpty()) {
//                if (!periodoMatricula[0].isEmpty()) {
//                    listQuery.add("RM.dt_emissao = '" + periodoMatricula[0] + "'");
//                }
//            } else {
//                listQuery.add("RM.dt_emissao BETWEEN '" + periodoMatricula[0] + "' AND '" + periodoMatricula[1] + "'");
//            }
//            if (nascimento[1].isEmpty()) {
//                if (!nascimento[0].isEmpty()) {
//                    listQuery.add("F.dt_nascimento = '" + nascimento[0] + "'");
//                }
//            } else {
//                listQuery.add("F.dt_nascimento BETWEEN '" + nascimento[0] + "' AND '" + nascimento[1] + "'");
//            }
//            if (status != null) {
//                listQuery.add("ME.id_status = " + status);
//            }
//            if (sexo != null && !sexo.isEmpty()) {
//                listQuery.add("F.ds_sexo = '" + sexo + "'");
//            }
//            if (vendedor != null) {
//                listQuery.add("ME.id_vendedor = " + vendedor);
//            }
//            if (midia != null) {
//                listQuery.add("ME.id_midia = " + midia);
//            }
//            if (responsavel != null) {
//                listQuery.add("ME.id_responsavel = " + responsavel);
//            }
//            if (aluno != null) {
//                listQuery.add("ME.id_aluno = " + aluno);
//            }
//            for (int i = 0; i < listQuery.size(); i++) {
//                if (i == 0) {
//                    queryString += " WHERE ";
//                } else {
//                    queryString += " AND ";
//                }
//                queryString += " " + listQuery.get(i).toString();
//            }
//            if (!relatorios.getQryOrdem().isEmpty()) {
//                queryString += " ORDER BY " + relatorios.getQry();
//            } else {
//                if (order.isEmpty()) {
//                    queryString += " ORDER BY P.ds_nome ASC";
//                } else {
//                    queryString += " ORDER BY " + order;
//                }
//            }
//            Query query = getEntityManager().createNativeQuery(queryString);
//            List list = query.getResultList();
//            if (!list.isEmpty()) {
//                return list;
//            }
//        } catch (Exception e) {
//            return new ArrayList();
//        }
//        return new ArrayList();
//    }
//
//    public String getOrder() {
//        return order;
//    }
//
//    public void setOrder(String order) {
//        this.order = order;
//    }
//
//}
