package br.com.clinicaintegrada.pessoa.dao;

import br.com.clinicaintegrada.pessoa.JuridicaReceita;
import br.com.clinicaintegrada.principal.DB;
import java.util.List;
import javax.persistence.Query;

public class JuridicaReceitaDao extends DB {

    public JuridicaReceita pesquisaJuridicaReceitaPorDocumento(String documento) {
        try {
            Query query = getEntityManager().createQuery("SELECT JR FROM JuridicaReceita AS JR WHERE JR.documento = :documento ");
            query.setParameter("documento", documento);
            List list = query.getResultList();
            if (!list.isEmpty() && list.size() == 1) {
                return (JuridicaReceita) query.getSingleResult();
            }
        } catch (Exception e) {
        }
        return null;
    }
}
