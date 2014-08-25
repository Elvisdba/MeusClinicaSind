package br.com.clinicaintegrada.endereco.dao;

import br.com.clinicaintegrada.endereco.Logradouro;
import br.com.clinicaintegrada.principal.DB;
import br.com.clinicaintegrada.utils.AnaliseString;
import java.util.List;
import javax.persistence.Query;

public class LogradouroDao extends DB {

    public Logradouro pesquisaLogradouroPorDescricao(String descricao) {
        descricao = descricao.toLowerCase().toUpperCase();
        try {
            Query query = getEntityManager().createNativeQuery("SELECT L.* FROM end_logradouro AS L WHERE UPPER(TRANSLATE(L.ds_descricao)) = '" + AnaliseString.removerAcentos(descricao) + "'", Logradouro.class);
            List list = query.getResultList();
            if (!list.isEmpty() || list.size() == 1) {
                return (Logradouro) query.getSingleResult();
            }
        } catch (Exception e) {
        }
        return null;
    }
}
