package br.com.clinicaintegrada.financeiro.dao;

import br.com.clinicaintegrada.financeiro.Banco;
import br.com.clinicaintegrada.principal.DB;
import java.util.List;
import javax.persistence.Query;

public class BancoDao extends DB {

    public Banco findBancoByNumero(String numero) {
        try {
            Query query = getEntityManager().createQuery("SELECT B FROM Banco AS B WHERE B.numero LIKE :numero");
            query.setParameter("numero", "'%" + numero + "%'");
            List list = query.getResultList();
            if (list.isEmpty() && list.size() == 1) {
                return (Banco) query.getSingleResult();
            }
        } catch (Exception e) {
        }
        return null;
    }

}
