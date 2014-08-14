package br.com.rtools.pessoa.dao;

import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class EquipeDao extends DB {

    public List pesquisaPessoaFisica(String descricao, String por, String como, int funcaoEquipe, int cliente) {
        try {
            String queryString = "SELECT E FROM Equipe AS E WHERE E.cliente.id = :cliente ORDER BY E.pessoa.nome";
            if (por.equals("documento") || por.equals("nome")) {
                switch (como) {
                    case "P":
                        descricao = "%" + descricao + "%";
                        break;
                    case "I":
                        descricao = descricao + "%";
                        break;
                }
                if (como.isEmpty()) {
                    queryString += " AND UPPER(E.pessoa." + por + ") = " + descricao;
                } else {
                    queryString += " AND UPPER(E.pessoa." + por + ") LIKE " + descricao;
                }
            }
            if (funcaoEquipe > 0) {
                if (!queryString.isEmpty()) {
                    queryString += " AND ";
                }
                queryString += " UPPER(E.funcaoEquipe.id ";
            }
            Query query = getEntityManager().createQuery(queryString);
            query.setParameter("cliente", cliente);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {

        }
        return new ArrayList();
    }

    public boolean existeEquipe(int tipoAtendimento, int tipoDocumentoProfissao, int profissao, int cliente) {
        try {
            Query query = getEntityManager().createQuery("SELECT FE FROM FuncaoEquipe AS FE WHERE FE.tipoAtendimento.id = :tipoAtendimento AND FE.tipoDocumentoProfissao.id = :tipoDocumentoProfissao  AND FE.profissao.id = :profissao  AND FE.cliente.id = :cliente ");
            query.setMaxResults(1);
            query.setParameter("tipoAtendimento", tipoAtendimento);
            query.setParameter("tipoDocumentoProfissao", tipoDocumentoProfissao);
            query.setParameter("profissao", profissao);
            query.setParameter("cliente", cliente);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return true;
            }
        } catch (Exception e) {

        }
        return false;
    }

}
