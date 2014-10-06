package br.com.clinicaintegrada.financeiro.dao;

import br.com.clinicaintegrada.financeiro.Servicos;
import br.com.clinicaintegrada.principal.DB;
import br.com.clinicaintegrada.utils.QueryString;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class ServicosDao extends DB {

    private int limit = 300;
    private String order = "ASC";
    private int type = 0;
    private String orderBy = " ORDER BY S.ds_descricao ASC ";

    public boolean existsServicos(Servicos s) {
        limit = 1;
        List list = findServicos(s);
        return !list.isEmpty();
    }

    public List findServicos(int idCliente) {
        Servicos s = new Servicos();
        s.getCliente().setId(idCliente);
        return findServicos(s);
    }

    public List findServicosByDescricao(int idCliente, String descricao) {
        Servicos s = new Servicos();
        s.setDescricao(descricao);
        s.getCliente().setId(idCliente);
        return findServicos(s);
    }

    public List findServicos(Servicos servicos) {
        try {
            String queryString = ""
                    + "     SELECT S.* "
                    + "       FROM fin_servicos AS S "
                    + "      WHERE S.id_cliente = " + servicos.getCliente().getId();
            if (!servicos.getDescricao().isEmpty()) {
                if (servicos.getDescricao().length() == 1) {
                    limit = 50;
                } else if (servicos.getDescricao().length() == 2) {
                    limit = 100;
                } else if (servicos.getDescricao().length() == 3) {
                    limit = 150;
                }
                queryString += " AND UPPER(FUNC_TRANSLATE(S.ds_descricao)) " + QueryString.typeSearch(servicos.getDescricao(), type);
            }
            queryString += orderBy;
            queryString += " LIMIT " + limit;
            Query query = getEntityManager().createNativeQuery(queryString, Servicos.class);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return new ArrayList();
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

}
