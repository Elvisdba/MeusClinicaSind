package br.com.clinicaintegrada.financeiro.dao;

import br.com.clinicaintegrada.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class CartaoDao extends DB {

    public List findAllByCliente(int idCliente) {
        try {
            Query query = getEntityManager().createQuery("SELECT C FROM Cartao AS C WHERE C.cliente.id = :cliente ORDER BY C.descricao");
            query.setParameter("cliente", idCliente);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {

        }
        return new ArrayList();
    }

    public boolean exist(String descricao, int idPlano5) {
        try {
            Query query = getEntityManager().createQuery("SELECT C FROM Cartao AS C WHERE UPPER(C.descricao) = :descricao AND C.plano5.id = :plano5");
            query.setParameter("descricao", descricao.toUpperCase());
            query.setParameter("idPlano5", idPlano5);
            query.setMaxResults(1);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return true;
            }
        } catch (Exception e) {

        }
        return false;
    }

}
