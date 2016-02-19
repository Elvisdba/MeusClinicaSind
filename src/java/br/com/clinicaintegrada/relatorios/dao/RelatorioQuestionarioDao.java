package br.com.clinicaintegrada.relatorios.dao;

import br.com.clinicaintegrada.principal.DB;
import br.com.clinicaintegrada.questionario.RespostaLote;
import br.com.clinicaintegrada.relatorios.Relatorios;
import br.com.clinicaintegrada.seguranca.Registro;
import br.com.clinicaintegrada.seguranca.controleUsuario.SessaoCliente;
import br.com.clinicaintegrada.seguranca.dao.RegistroDao;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class RelatorioQuestionarioDao extends DB {

    private String order = "";

    private Relatorios relatorios;
    private Boolean IS_QUERY = false;
    private String QUERY = "";

    /**
     *
     * @param respostaLote
     * @param resposta_lote_id
     * @return
     */
    public List find(RespostaLote respostaLote) {
        List listWhere = new ArrayList();
        Registro r = new RegistroDao().pesquisaRegistroPorCliente(SessaoCliente.get().getId());
        try {
            String queryString;
//            queryString
//                    = // ALIAS
//                    "       SELECT RL.id            AS lote_id,                 \n"
//                    + "            Q.id             AS questionario_id,         \n"
//                    + "            Q.ds_descricao   AS questionario,            \n"
//                    + "            -- Q.id          AS QID,                     \n"
//                    + "            QG.id            AS grupo_id,                \n"
//                    + "            QG.ds_descricao  AS grupo,                   \n"
//                    + "            QSG.id           AS subgrupo_id,             \n"
//                    + "            QSG.ds_descricao AS subgrupo,                \n"
//                    + "            QUE.id           AS questao_id,              \n"
//                    + "            QUE.ds_descricao AS questao,                 \n"
//                    + "            RL.dt_lancamento AS lancamento,              \n"
//                    + "            P.id             AS pessoa_id,               \n"
//                    + "            P.ds_nome        AS pessoa_nome,             \n"
//                    + "            P.ds_telefone1   AS pessoa_telefone1,        \n"
//                    + "            P.ds_telefone2   AS pessoa_telefone2,        \n"
//                    + "            P.ds_documento   AS pessoa_documento,        \n"
//                    + "            F.dt_nascimento  AS fisica_nascimento,       \n"
//                    + "            F.ds_sexo        AS fisica_sexo,             \n"
//                    + "            func_idade(F.dt_nascimento) AS fisica_idade, \n"
//                    + "            QRF.ds_descricao AS resposta_fixa,           \n"
//                    + "            QR.ds_descricao  AS resposta,                \n"
//                    + "            PE.ds_nome       AS equipe_nome,             \n"
//                    + "            L.ds_descricao "
//                    + "            || ' ' || DE.ds_descricao "
//                    + "            || ', ' || PENDE.ds_numero "
//                    + "            || ' ' || PENDE.ds_complemento "
//                    + "            || ' - ' || B.ds_descricao "
//                    + "            || ' - ' || C.ds_cidade  "
//                    + "            || ' / ' || C.ds_uf  AS endereco,            \n"
//                    + "            ENDE.ds_cep AS cep                           \n"
//                    + "       FROM que_resposta_lote AS RL                                  \n"
//                    + " INNER JOIN que_resposta      AS QR  ON RL.id  = QR.id_resposta_lote \n"
//                    + " INNER JOIN que_resposta_fixa AS QRF ON QRF.id = QR.id_resposta_fixa \n"
//                    + " INNER JOIN que_questao       AS QUE ON QUE.id = QRF.id_questao      \n"
//                    + " INNER JOIN que_subgrupo      AS QSG ON QSG.id = QUE.id_subgrupo     \n"
//                    + " INNER JOIN que_grupo         AS QG  ON QG.id  = QSG.id_grupo        \n"
//                    + " INNER JOIN que_questionario  AS Q   ON Q.id   = QG.id_questionario  \n"
//                    + " INNER JOIN pes_pessoa        AS P   ON P.id   = RL.id_pessoa        \n"
//                    + " INNER JOIN pes_fisica        AS F   ON P.id   = F.id_pessoa         \n"
//                    + " INNER JOIN pes_equipe        AS E   ON E.id   = RL.id_equipe        \n"
//                    + " INNER JOIN pes_pessoa        AS PE  ON PE.id  = E.id_pessoa         \n"
//                    + " INNER JOIN pes_pessoa_endereco PENDE ON PENDE.id_pessoa = RL.id_pessoa AND PENDE.id_tipo_endereco = 1 \n"
//                    + " INNER JOIN end_endereco      AS ENDE ON ENDE.id = PENDE.id_endereco            \n"
//                    + " INNER JOIN end_logradouro    AS L    ON L.id  = ENDE.id_logradouro             \n"
//                    + " INNER JOIN end_descricao_endereco AS DE ON DE.id = ENDE.id_descricao_endereco  \n"
//                    + " INNER JOIN end_bairro       AS B     ON B.id  = ENDE.id_bairro                 \n"
//                    + " INNER JOIN end_cidade       AS C     ON C.id  = ENDE.id_cidade                 \n"
//                    + "      WHERE RL.id = " + resposta_lote_id + "                                    \n"
//                    + "   ORDER BY Q.id, QG.id, QSG.id, QUE.id ";

            queryString = ""
                    + "SELECT      RESULT.lote_id,                              \n"
                    + "            Q.id             AS questionario_id,         \n"
                    + "            Q.ds_descricao   AS questionario,            \n"
                    + "            -- Q.id          AS QID,                     \n"
                    + "            QG.id            AS grupo_id,                \n"
                    + "            QG.ds_descricao  AS grupo,                   \n"
                    + "            QSG.id           AS subgrupo_id,             \n"
                    + "            QSG.ds_descricao AS subgrupo,                \n"
                    + "            QUE.id           AS questao_id,              \n"
                    + "            QUE.ds_descricao AS questao,                 \n"
                    + "            RESULT.lancamento,                           \n"
                    + "            P.id             AS pessoa_id,               \n"
                    + "            P.ds_nome        AS pessoa_nome,             \n"
                    + "            P.ds_telefone1   AS pessoa_telefone1,        \n"
                    + "            P.ds_telefone2   AS pessoa_telefone2,        \n"
                    + "            P.ds_documento   AS pessoa_documento,        \n"
                    + "            F.dt_nascimento  AS fisica_nascimento,       \n"
                    + "            F.ds_sexo        AS fisica_sexo,             \n"
                    + "            func_idade(F.dt_nascimento) AS fisica_idade, \n"
                    + "            RESULT.resposta_fixa,                        \n"
                    + "            CASE                                                                                             \n"
                    + "                 WHEN  RESULT.resposta IS NULL AND RESULT.resposta_fixa IS NULL  THEN 'NEGA'                 \n"
                    + "                 WHEN  RESULT.resposta = '' AND RESULT.resposta_fixa IS NOT NULL THEN RESULT.resposta_fixa   \n"
                    + "             ELSE RESULT.resposta\n"
                    + "	    END,\n"
                    + "            RESULT.equipe_nome,                          \n"
                    + "            L.ds_descricao             || ' ' || DE.ds_descricao             || ', ' || PENDE.ds_numero             || ' ' || PENDE.ds_complemento             || ' - ' || B.ds_descricao             || ' - ' || C.ds_cidade              || ' / ' || C.ds_uf  AS endereco,            \n"
                    + "            ENDE.ds_cep AS cep                           \n"
                    + "  FROM que_questao AS QUE                                \n"
                    + "  LEFT join (                                            \n"
                    + "	SELECT RL.id            AS lote_id,                     \n"
                    + "            Q.id             AS questionario_id,         \n"
                    + "            Q.ds_descricao   AS questionario,            \n"
                    + "            -- Q.id          AS QID,                     \n"
                    + "            QG.id            AS grupo_id,                \n"
                    + "            QG.ds_descricao  AS grupo,                   \n"
                    + "            QSG.id           AS subgrupo_id,             \n"
                    + "            QSG.ds_descricao AS subgrupo,                \n"
                    + "            QUE.id           AS questao_id,              \n"
                    + "            QUE.ds_descricao AS questao,                 \n"
                    + "            RL.dt_lancamento AS lancamento,              \n"
                    + "            QRF.ds_descricao AS resposta_fixa,           \n"
                    + "            QR.ds_descricao  AS resposta,                \n"
                    + "            PE.ds_nome       AS equipe_nome              \n"
                    + "       FROM que_resposta_lote AS RL                      \n"
                    + "  LEFT JOIN que_resposta      AS QR  ON RL.id  = QR.id_resposta_lote \n"
                    + "  LEFT JOIN que_resposta_fixa AS QRF ON QRF.id = QR.id_resposta_fixa \n"
                    + "  LEFT JOIN que_questao       AS QUE ON QUE.id = QRF.id_questao      \n"
                    + "  LEFT JOIN que_subgrupo      AS QSG ON QSG.id = QUE.id_subgrupo     \n"
                    + "  LEFT JOIN que_grupo         AS QG  ON QG.id  = QSG.id_grupo        \n"
                    + "  LEFT JOIN que_questionario  AS Q   ON Q.id   = QG.id_questionario  \n"
                    + "  LEFT JOIN pes_equipe        AS E   ON E.id   = RL.id_equipe        \n"
                    + "  LEFT JOIN pes_pessoa        AS PE  ON PE.id  = E.id_pessoa         \n"
                    + "      WHERE RL.id = " + respostaLote.getId() + "                     \n"
                    + "  ) AS RESULT ON QUE.id = RESULT.questao_id                          \n"
                    + "  LEFT JOIN que_subgrupo      AS QSG ON QSG.id = QUE.id_subgrupo     \n"
                    + "  LEFT JOIN que_grupo         AS QG  ON QG.id  = QSG.id_grupo        \n"
                    + "  LEFT JOIN que_questionario  AS Q   ON Q.id   = QG.id_questionario\n"
                    + "  INNER JOIN pes_pessoa        AS P   ON P.id   = " + respostaLote.getPessoa().getId() + "   \n"
                    + " INNER JOIN pes_fisica        AS F   ON P.id   = F.id_pessoa                                 \n"
                    + " INNER JOIN pes_pessoa_endereco PENDE ON PENDE.id_pessoa = " + respostaLote.getPessoa().getId() + " AND PENDE.id_tipo_endereco = 1 \n"
                    + " INNER JOIN end_endereco      AS ENDE ON ENDE.id = PENDE.id_endereco            \n"
                    + " INNER JOIN end_logradouro    AS L    ON L.id  = ENDE.id_logradouro             \n"
                    + " INNER JOIN end_descricao_endereco AS DE ON DE.id = ENDE.id_descricao_endereco  \n"
                    + " INNER JOIN end_bairro       AS B     ON B.id  = ENDE.id_bairro                 \n"
                    + " INNER JOIN end_cidade       AS C     ON C.id  = ENDE.id_cidade  \n"
                    + "  ORDER BY Q.id, QG.id, QSG.id, QUE.id;";
            if (!order.isEmpty()) {
                queryString += "ORDER BY " + order;
            }
            if (IS_QUERY) {
                QUERY = queryString;
                IS_QUERY = false;
                return new ArrayList();
            } else {
                Query query = getEntityManager().createNativeQuery(queryString);
                return query.getResultList();
            }
        } catch (Exception e) {
            return new ArrayList();
        }
    }

    public Relatorios getRelatorios() {
        return relatorios;
    }

    public void setRelatorios(Relatorios relatorios) {
        this.relatorios = relatorios;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public Boolean getIS_QUERY() {
        return IS_QUERY;
    }

    public void setIS_QUERY(Boolean IS_QUERY) {
        this.IS_QUERY = IS_QUERY;
    }

    public String getQUERY() {
        return QUERY;
    }

    public void setQUERY(String QUERY) {
        this.QUERY = QUERY;
    }
}
