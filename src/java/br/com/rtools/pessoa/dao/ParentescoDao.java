package br.com.rtools.pessoa.dao;

import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class ParentescoDao extends DB {

    public List pesquisaTodos() {
        try {
            Query query = getEntityManager().createQuery("SELECT P FROM Parentesco AS P ORDER BY P.id ASC");
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    public List pesquisaTodosSemTitular() {
        try {
            Query query = getEntityManager().createQuery(
                    "   SELECT P                         "
                    + "   FROM Parentesco AS P           "
                    + "  WHERE P.ativo = true            "
                    + "    AND P.id <> 1 ORDER BY P.id   ");
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

}
