package br.com.clinicaintegrada.pessoa.dao;

import br.com.clinicaintegrada.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class ProfissaoDao extends DB {

    public List pesquisaProfissao(String por, String combo, String descricao) {
        if (!descricao.equals("") && !por.equals("")) {
            switch (por) {
                case "I":
                    descricao = descricao + "%";
                    break;
                case "P":
                    descricao = "%" + descricao + "%";
                    break;
            }
        } else {
            return new ArrayList();
        }
        if (combo.equals("")) {
            combo = "profissao";
        }
        try {
            String textQuery = "SELECT P FROM Profissao AS P WHERE UPPER(P." + combo + ") LIKE :profissao ORDER BY P.profissao";
            Query query = getEntityManager().createQuery(textQuery);
            query.setParameter("profissao", descricao.toLowerCase().toUpperCase());
            return (query.getResultList());
        } catch (Exception e) {
            return new ArrayList();
        }
    }
}
