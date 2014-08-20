package br.com.rtools.seguranca.dao;

import br.com.rtools.principal.DB;
import br.com.rtools.seguranca.Registro;
import java.util.List;
import javax.persistence.Query;

public class RegistroDao extends DB {

    public Registro pesquisaRegistroPorCliente(int cliente) {
        try {
            Query query = getEntityManager().createQuery("SELECT R FROM Registro AS R WHERE R.cliente.id = :cliente");
            query.setParameter("cliente", cliente);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return (Registro) query.getSingleResult();
            }
        } catch (Exception e) {

        }
        return null;
    }

}
