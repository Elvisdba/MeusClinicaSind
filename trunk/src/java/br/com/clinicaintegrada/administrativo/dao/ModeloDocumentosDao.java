package br.com.clinicaintegrada.administrativo.dao;

import br.com.clinicaintegrada.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class ModeloDocumentosDao extends DB {

    public List pesquisaTodosPorContrato(int idContrato) {
        try {
            Query query = getEntityManager().createQuery(" SELECT MD FROM ModeloDocumentos AS MD WHERE MD.contrato.id = :idContrato ORDER BY MD.descricao ASC");
            query.setParameter("idContrato", idContrato);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }
}
