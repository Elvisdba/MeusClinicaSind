package br.com.rtools.seguranca.dao;

import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class ModuloDao extends DB {

    public List listaModuloPermissaoAgrupado(int idCliente) {
        try {
            Query query = getEntityManager().createQuery("SELECT P.modulo FROM Permissao AS P WHERE P.cliente.id = :cliente GROUP BY P.modulo ORDER BY P.modulo.descricao ASC ");
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
