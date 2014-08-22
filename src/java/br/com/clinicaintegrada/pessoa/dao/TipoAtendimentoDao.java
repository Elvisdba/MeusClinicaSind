package br.com.clinicaintegrada.pessoa.dao;

import br.com.clinicaintegrada.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class TipoAtendimentoDao extends DB {

    public List pesquisaTodasTipoAtendimentoPorCliente(int cliente) {
        try {
            Query query = getEntityManager().createQuery("SELECT TA FROM TipoAtendimento AS TA WHERE TA.cliente.id = :cliente ORDER BY TA.descricao ASC");
            query.setParameter("cliente", cliente);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {

        }
        return new ArrayList();
    }
}
