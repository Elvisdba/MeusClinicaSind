package br.com.clinicaintegrada.coordenacao.dao;

import br.com.clinicaintegrada.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class GradeDao extends DB {

    public boolean existeGradePorCliente(int cliente, int filial, int semana, int evento, String horaInicio, String horaFim) {
        try {
            String queryString = ""
                    + "   SELECT id "
                    + "     FROM rot_grade "
                    + "    WHERE id_cliente = " + cliente
                    + "      AND id_filial = " + filial
                    + "      AND id_semana = " + semana
                    + "      AND id_evento = " + evento
                    + "      AND ds_hora_inicio = '" + horaInicio + "' "
                    + "      AND ds_hora_fim = '" + horaFim + "' "
                    + "    LIMIT 1";
            Query query = getEntityManager().createNativeQuery(queryString);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public List pesquisaTodasGradesPorCliente(int cliente) {
        try {
            Query query = getEntityManager().createQuery("SELECT G FROM Grade AS G WHERE G.cliente.id = :cliente  ORDER BY G.dataEvento DESC");
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

    public List pesquisaTodasGradesPorClienteFilial(int cliente, int filial) {
        try {
            Query query = getEntityManager().createQuery("SELECT G FROM Grade AS G WHERE G.cliente.id = :cliente AND G.filial.id = :filial ORDER BY G.dataEvento DESC");
            query.setParameter("cliente", cliente);
            query.setParameter("filial", filial);
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
