package br.com.clinicaintegrada.pessoa.dao;

import br.com.clinicaintegrada.pessoa.TipoDocumentoProfissao;
import br.com.clinicaintegrada.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class TipoDocumentoProfissaoDao extends DB {

    public List pesquisaTipoDocumentoProfissaoPorEquipeAgrupado() {
        try {
            Query query = getEntityManager().createQuery("SELECT E.funcaoEquipe.tipoDocumentoProfissao.descricao FROM Equipe AS E GROUP BY E.funcaoEquipe.tipoDocumentoProfissao.descricao ORDER BY E.funcaoEquipe.tipoDocumentoProfissao.descricao ASC ");
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return new ArrayList();
    }

    public TipoDocumentoProfissao pesquisaTipoDocumentoProfissaoPorDescricao(String descricao) {
        try {
            Query query = getEntityManager().createQuery("SELECT TDP FROM TipoDocumentoProfissao AS TDP WHERE UPPER(TDP.descricao) = :descricao");
            query.setParameter("descricao", descricao);
            query.setMaxResults(1);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return (TipoDocumentoProfissao) query.getSingleResult();
            }
        } catch (Exception e) {

        }
        return null;
    }
}
