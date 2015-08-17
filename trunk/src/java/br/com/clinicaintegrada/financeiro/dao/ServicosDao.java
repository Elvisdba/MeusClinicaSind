package br.com.clinicaintegrada.financeiro.dao;

import br.com.clinicaintegrada.financeiro.Servicos;
import br.com.clinicaintegrada.principal.DB;
import br.com.clinicaintegrada.seguranca.controleUsuario.SessaoCliente;
import br.com.clinicaintegrada.utils.QueryString;
import br.com.clinicaintegrada.utils.dao.FindDao;
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

    public List findAllByContaCobranca(int idRotina) {
        try {
            Query query = getEntityManager().createQuery(
                    "SELECT SR.servicos "
                    + "  FROM ServicoRotina AS SR"
                    + " WHERE SR.rotina.id = :rotina"
                    + "   AND SR.servicos.id IN(SELECT S.servicos.id FROM ServicoContaCobranca AS S)");
            query.setParameter("rrotina", idRotina);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    public List findAllWServicoRotina(int idRotina) {
        try {
            Query query = getEntityManager().createQuery("SELECT SR FROM ServicoRotina AS SR WHERE SR.rotina.id = :rotina GROUP BY SR.servicos ORDER BY SR.servicos.descricao");
            query.setParameter("rotina", idRotina);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return new ArrayList();
    }

    /**
     * Nome da tabela onde esta a lista de filiais Ex:
     * findNotInByTabela('matr_escola');
     *
     * @param table (Use alias T+colum
     * @param colum_filter_key Nome da coluna do filtro
     * @return Todas as rotinas da tabela específicada
     * @param colum_filter_value Valor do filtro
     */
    public List findNotInByTabela(String table, String colum_filter_key, String colum_filter_value) {
        return findNotInByTabela(SessaoCliente.get().getId(), table, "id_servico", colum_filter_key, colum_filter_value, true);
    }

    /**
     * Nome da tabela onde esta a lista de filiais Ex:
     * findNotInByTabela('seg_filial_rotina', 'id_filial', 1);
     *
     * @param cliente_id
     * @param table (Use alias T+colum)
     * @param column
     * @param colum_filter_key Nome da coluna do filtro
     * @return Todas as rotinas não usadas em uma chave conforme o valor
     * @param colum_filter_value Valor do filtro
     * @param is_ativo default null
     */
    public List findNotInByTabela(Integer cliente_id, String table, String column, String colum_filter_key, String colum_filter_value, Boolean is_ativo) {
        if (column == null || column.isEmpty()) {
            column = "id_servico";
        }
        if (colum_filter_key == null || colum_filter_key.isEmpty() || colum_filter_value == null || colum_filter_value.isEmpty()) {
            return new ArrayList();
        }
        String where = "";
        if (is_ativo != null) {
            where += " AND T1.is_ativo = " + is_ativo;
        }
        return new FindDao().findNotInByTabela(cliente_id, Servicos.class, "fin_servicos", new String[]{"ds_descricao"}, table, column, colum_filter_key, colum_filter_value, where);
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

    /**
     * Nome da tabela onde esta a lista de filiais Ex:
     * findByTabela('matr_escola');
     *
     * @param table
     * @return Todas as serviços da tabela específicada
     */
    public List findByTabela(String table) {
        return findByTabela(SessaoCliente.get().getId(), table, "id_servico");
    }

    /**
     * Nome da tabela onde esta a lista de filiais Ex:
     * findByTabela('matr_escola');
     *
     * @param cliente_id
     * @param table
     * @return Todas os serviços da tabela específicada
     * @param column Nome da coluna
     */
    public List findByTabela(Integer cliente_id, String table, String column) {
        if (column == null || column.isEmpty()) {
            column = "id_servico";
        }
        try {
            String queryString
                    = "     SELECT O1.* FROM fin_servicos AS O1                 \n"
                    + "      WHERE O1.id IN (                                   \n"
                    + "	           SELECT O2." + column + "                     \n"
                    + "              FROM " + table + " AS O2                   \n"
                    + "             WHERE O2.id_cliente = " + cliente_id + "    \n"
                    + "          GROUP BY O2." + column + "                     \n"
                    + ")                                                        \n"
                    + "          AND O1.id_cliente = " + cliente_id + "           \n"
                    + " ORDER BY O1.ds_descricao                                ";
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

    public List findAllByMovimentoGroup(Integer cliente_id) {
        try {
            String queryString
                    = "     SELECT S.* FROM fin_servicos AS S                   \n"
                    + "      WHERE S.id IN (                                    \n"
                    + "	           SELECT M.id_servicos                         \n"
                    + "              FROM fin_movimento AS M                    \n"
                    + "        INNER JOIN fin_lote AS L ON L.id = M.id_lote     \n"
                    + "                           AND L.id_cliente = 3          \n"
                    + "          GROUP BY M.id_servicoS                         \n"
                    + ")                                                        \n"
                    + " ORDER BY S.ds_descricao";
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
}
