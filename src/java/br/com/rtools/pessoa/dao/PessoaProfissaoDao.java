package br.com.rtools.pessoa.dao;

import br.com.rtools.pessoa.PessoaProfissao;
import br.com.rtools.principal.DB;
import java.util.List;
import javax.persistence.Query;

public class PessoaProfissaoDao extends DB {

    public PessoaProfissao pesquisaPessoaProfissaoPorFisica(int idFisica) {
        try {
            Query query = getEntityManager().createQuery("SELECT PP FROM PessoaProfissao AS PP WHERE PP.fisica.id = :fisica");
            query.setParameter("fisica", idFisica);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return (PessoaProfissao) query.getSingleResult();
            }
        } catch (Exception e) {
        }
        return null;
    }
}
