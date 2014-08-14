package br.com.rtools.pessoa.dao;

import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class DependenteDao extends DB {

    public List pesquisaDependentesPorPessoa(int pessoa) {
        try {
            Query query = getEntityManager().createQuery("SELECT D FROM Dependente AS D WHERE D.pessoa.id = :pessoa");
            query.setParameter("pessoa", pessoa);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }

        } catch (Exception e) {

        }
        return new ArrayList();
    }

}
