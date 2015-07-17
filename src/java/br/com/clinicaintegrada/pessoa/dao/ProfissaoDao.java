package br.com.clinicaintegrada.pessoa.dao;

import br.com.clinicaintegrada.pessoa.Profissao;
import br.com.clinicaintegrada.principal.DB;
import br.com.clinicaintegrada.utils.QueryString;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class ProfissaoDao extends DB {

    public List pesquisaProfissao(String por, String combo, String descricao) {
        Integer type = 0;
        if (!descricao.equals("") && !por.equals("")) {
            switch (por) {
                case "I":
                    type = 1;
                    break;
                case "P":
                    type = 2;
                    break;
            }
        } else {
            return new ArrayList();
        }
        if (combo.equals("")) {
            combo = "ds_profissao";
        }
        try {
            String textQuery = "SELECT P.* FROM pes_profissao AS P WHERE func_translate(UPPER(P." + combo + ")) " + QueryString.typeSearch(descricao, type) + " ORDER BY P.ds_profissao";
            Query query = getEntityManager().createNativeQuery(textQuery, Profissao.class);
            return (query.getResultList());
        } catch (Exception e) {
            return new ArrayList();
        }
    }
}
