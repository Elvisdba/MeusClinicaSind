package br.com.rtools.sistema.dao;

import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class VeiculoDao extends DB {

    public boolean existeVeiculo(String descricao, String placa, int combustivel, int cliente) {
        try {
            Query query = getEntityManager().createQuery("SELECT V FROM Veiculo AS V WHERE UPPER(V.descricao) = :descricao AND V.placa = :placa AND V.combustivel.id = :combustivel AND V.cliente.id = :cliente");
            query.setParameter("descricao", descricao.toUpperCase());
            query.setParameter("placa", placa);
            query.setParameter("combustivel", combustivel);
            query.setParameter("cliente", cliente);
            query.setMaxResults(1);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return true;
            }
        } catch (Exception e) {

        }
        return false;
    }

    public List pesquisaVeiculosPorCliente(int cliente) {
        try {
            Query query = getEntityManager().createQuery("SELECT V FROM Veiculo AS V WHERE V.cliente.id = :cliente");
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
