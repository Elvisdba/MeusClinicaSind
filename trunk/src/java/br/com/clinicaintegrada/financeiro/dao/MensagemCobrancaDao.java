package br.com.clinicaintegrada.financeiro.dao;

import br.com.clinicaintegrada.financeiro.MensagemCobranca;
import br.com.clinicaintegrada.principal.DB;
import java.util.List;
import javax.persistence.Query;

public class MensagemCobrancaDao extends DB {

    public MensagemCobranca findByMovimento(Integer movimento) {
        try {
            Query query = getEntityManager().createQuery("SELECT MC FROM MensagemCobranca AS MC WHERE MC.movimento.id = :movimento");
            query.setParameter("movimento", movimento);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return (MensagemCobranca) query.getSingleResult();
            }
        } catch (Exception e) {
        }
        return null;
    }

}
