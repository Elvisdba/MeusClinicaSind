package br.com.clinicaintegrada.pessoa.dao;

import br.com.clinicaintegrada.pessoa.FilialCidade;
import br.com.clinicaintegrada.principal.DB;
import java.util.List;
import javax.persistence.Query;

public class FilialCidadeDao extends DB {

    public FilialCidade pesquisaFilialCidade(int idFilial, int idCidade) {
        try {
            Query query = getEntityManager().createQuery("SELECT FC FROM FilialCidade AS FC WHERE FC.cidade.id = :cidade AND FC.filial.id = :filial");
            query.setParameter("filial", idFilial);
            query.setParameter("cidade", idCidade);
            List list = query.getResultList();
            if (!list.isEmpty() && list.size() == 1) {
                return (FilialCidade) query.getSingleResult();
            }
        } catch (Exception e) {
        }
        return null;
    }

    public FilialCidade pesquisaFilialPorCidade(int idCidade) {
        try {
            Query query = getEntityManager().createQuery("SELECT FC FROM FilialCidade AS FC WHERE FC.cidade.id = :cidade");
            query.setParameter("cidade", idCidade);
            List list = query.getResultList();
            if (!list.isEmpty() && list.size() == 1) {
                return (FilialCidade) query.getSingleResult();
            }
        } catch (Exception e) {
        }
        return null;
    }
}
