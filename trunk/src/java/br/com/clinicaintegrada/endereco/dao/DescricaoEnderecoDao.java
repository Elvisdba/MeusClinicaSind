package br.com.clinicaintegrada.endereco.dao;

import br.com.clinicaintegrada.endereco.DescricaoEndereco;
import br.com.clinicaintegrada.principal.DB;
import br.com.clinicaintegrada.utils.AnaliseString;
import java.util.List;
import javax.persistence.Query;

public class DescricaoEnderecoDao extends DB {

    public DescricaoEndereco pesquisaDescricaoEnderecoPorDescricaoCliente(String descricao, int cliente) {
        descricao = descricao.toLowerCase().toUpperCase();
        try {
            Query query = getEntityManager().createNativeQuery("SELECT DE.* FROM end_descricao_endereco AS DE WHERE UPPER(TRANSLATE(DE.ds_descricao)) = '" + AnaliseString.removerAcentos(descricao) + "' AND (DE.id_cliente = " + cliente + " OR DE.id_cliente IS NULL)", DescricaoEndereco.class);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return (DescricaoEndereco) list.get(0);
            }
        } catch (Exception e) {
        }
        return null;
    }
}
