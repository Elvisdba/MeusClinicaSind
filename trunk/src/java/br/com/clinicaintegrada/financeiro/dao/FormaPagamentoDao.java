package br.com.clinicaintegrada.financeiro.dao;

import br.com.clinicaintegrada.financeiro.FormaPagamento;
import br.com.clinicaintegrada.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class FormaPagamentoDao extends DB {

    public List<FormaPagamento> findFormaPagamentoByBaixa(int idBaixa) {
        try {
            Query query = getEntityManager().createQuery("SELECT F FROM FormaPagamento AS F WHERE F.baixa.id = :idBaixa");
            query.setParameter("idBaixa", idBaixa);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return new ArrayList();
    }
}
