package br.com.rtools.seguranca.dao;

import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class SegEventoDao extends DB {

    public List listaEventoPermissaoAgrupado(int idModulo, int idRotina, int idCliente) {
        try {
            Query query = getEntityManager().createQuery(
                    "      SELECT P.segEvento                                   "
                    + "      FROM Permissao AS P                                "
                    + "     WHERE P.modulo.id    = :modulo                      "
                    + "       AND P.rotina.id    = :rotina                      "
                    + "       AND P.rotina.ativo = true                         "
                    + "  GROUP BY P.segEvento                                   "
                    + "  ORDER BY P.segEvento.descricao ASC                     ");
            query.setParameter("modulo", idModulo);
            query.setParameter("rotina", idRotina);
            query.setParameter("cliente", idCliente);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

}