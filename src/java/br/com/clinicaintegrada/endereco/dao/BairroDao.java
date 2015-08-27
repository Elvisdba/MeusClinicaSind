package br.com.clinicaintegrada.endereco.dao;

import br.com.clinicaintegrada.endereco.Bairro;
import br.com.clinicaintegrada.principal.DB;
import br.com.clinicaintegrada.utils.AnaliseString;
import java.util.List;
import javax.persistence.Query;

public class BairroDao extends DB {

    public Bairro pesquisaBairroPorDescricaoCliente(String descricao, int cliente) {
        descricao = descricao.toLowerCase().toUpperCase();
        try {
            Query query = getEntityManager().createNativeQuery("SELECT B.* FROM end_bairro AS B WHERE UPPER(func_translate(B.ds_descricao)) = '" + AnaliseString.removerAcentos(descricao) + "' AND (B.id_cliente = " + cliente + " OR B.id_cliente IS NULL)", Bairro.class);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return (Bairro) list.get(0);
            }
        } catch (Exception e) {
        }
        return null;
    }
}
