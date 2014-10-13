package br.com.clinicaintegrada.administrativo.dao;

import br.com.clinicaintegrada.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class ModeloDocumentosDao extends DB {

    public List pesquisaTodosPorRotina(int idRotina) {
        try {
            Query query = getEntityManager().createQuery(" SELECT MD FROM ModeloDocumentos AS MD WHERE MD.rotina.id = :idRotina ORDER BY MD.descricao ASC");
            query.setParameter("idRotina", idRotina);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }
}
