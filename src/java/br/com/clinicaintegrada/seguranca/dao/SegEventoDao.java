package br.com.clinicaintegrada.seguranca.dao;

import br.com.clinicaintegrada.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class SegEventoDao extends DB {

    public List listaEventoPermissaoAgrupado(int modulo, int rotina, int cliente) {
        try {
            Query query = getEntityManager().createQuery(
                    "      SELECT P.segEvento                                   "
                    + "      FROM Permissao AS P                                "
                    + "     WHERE P.modulo.id    = :modulo                      "
                    + "       AND P.rotina.id    = :rotina                      "
                    + "       AND P.cliente.id   = :cliente                     "
                    + "       AND P.rotina.ativo = true                         "
                    + "  GROUP BY P.segEvento                                   "
                    + "  ORDER BY P.segEvento.descricao ASC                     ");
            query.setParameter("modulo", modulo);
            query.setParameter("rotina", rotina);
            query.setParameter("cliente", cliente);
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
