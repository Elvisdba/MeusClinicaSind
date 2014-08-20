package br.com.rtools.coordenacao.bao;

import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class EventoDao extends DB {

    public boolean existeEvento(int grupoEvento, String descricao, int cliente) {
        descricao = descricao.toUpperCase();
        try {
            Query query = getEntityManager().createQuery("SELECT E FROM Evento AS E WHERE E.grupoEvento.id = :grupoEvento AND UPPER(E.descricao) = :descricao AND E.cliente.id = :cliente");
            query.setParameter("grupoEvento", grupoEvento);
            query.setParameter("descricao", descricao);
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

    public List pesquisaTodosPorCliente(int cliente) {
        try {
            Query query = getEntityManager().createQuery("SELECT E FROM Evento AS E WHERE E.cliente.id = :cliente");
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
