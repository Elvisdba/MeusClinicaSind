package br.com.clinicaintegrada.pessoa.dao;

import br.com.clinicaintegrada.pessoa.Equipe;
import br.com.clinicaintegrada.principal.DB;
import br.com.clinicaintegrada.utils.AnaliseString;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class EquipeDao extends DB {

    public List pesquisaTodasEquipesPorCliente(String descricao, String por, String como, int funcaoEquipe, int cliente) {
        try {
            String queryString
                    = "       SELECT E.*                                                "
                    + "       FROM pes_equipe AS E                                      "
                    + " INNER JOIN seg_cliente AS C ON C.id = E.id_cliente              "
                    + " INNER JOIN pes_pessoa AS P ON P.id = E.id_pessoa                "
                    + "      WHERE E.id_cliente = " + cliente;
            switch (por) {
                case "ds_cpf":
                case "ds_nome":
                    descricao = descricao.toUpperCase();
                    if (por.equals("ds_cpf")) {
                        por = "ds_documento";
                    } else {
                        descricao = AnaliseString.removerAcentos(descricao);
                        descricao = descricao.replace("-", "");
                        descricao = descricao.replace(".", "");
                    }
                    switch (como) {
                        case "P":
                            descricao = "%" + descricao + "%";
                            break;
                        case "I":
                            descricao = descricao + "%";
                            break;
                    }
                    if (como.isEmpty()) {
                        queryString += " AND TRANSLATE(UPPER(P." + por + ")) = '" + descricao + "'";
                    } else {
                        queryString += " AND TRANSLATE(UPPER(P." + por + ")) LIKE '" + descricao + "'";
                    }
                    break;
                case "ds_documento":
                    switch (como) {
                        case "P":
                            descricao = "%" + descricao + "%";
                            break;
                        case "I":
                            descricao = descricao + "%";
                            break;
                    }
                    if (como.isEmpty()) {
                        queryString += " AND UPPER(E.ds_documento) = '" + descricao + "'";
                    } else {
                        queryString += " AND UPPER(E.ds_documento) LIKE '" + descricao + "'";
                    }
                    break;
            }
            if (funcaoEquipe > 0) {
                if (!queryString.isEmpty()) {
                    queryString += " AND ";
                }
                queryString += " E.id_funcao_equipe = " + funcaoEquipe;
            }
            Query query = getEntityManager().createNativeQuery(queryString, Equipe.class);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return new ArrayList();
    }

    public boolean existeEquipe(int funcaoEquipe, int pessoa, int cliente) {
        try {
            Query query = getEntityManager().createQuery("SELECT E FROM Equipe AS E WHERE E.funcaoEquipe.id = :funcaoEquipe AND E.pessoa.id = :pessoa AND E.cliente.id = :cliente ");
            query.setMaxResults(1);
            query.setParameter("funcaoEquipe", funcaoEquipe);
            query.setParameter("pessoa", pessoa);
            query.setParameter("cliente", cliente);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return true;
            }
        } catch (Exception e) {

        }
        return false;
    }

    public Equipe findByPessoaAndFuncaoEquipe(Integer idPessoa, Integer idFuncaoEquipe) {
        try {
            Query query = getEntityManager().createQuery("SELECT E FROM Equipe AS E WHERE E.funcaoEquipe.id = :funcaoEquipe AND E.pessoa.id = :pessoa ");
            query.setMaxResults(1);
            query.setParameter("pessoa", idPessoa);
            query.setParameter("funcaoEquipe", idFuncaoEquipe);
            List list = query.getResultList();
            if (!list.isEmpty() && list.size() == 1) {
                return (Equipe) query.getSingleResult();
            }
        } catch (Exception e) {

        }
        return null;
    }

    public Equipe findByPessoaAndProfissao(Integer idPessoa, Integer idProfissao) {
        try {
            Query query = getEntityManager().createQuery("SELECT E FROM Equipe AS E WHERE E.funcaoEquipe.profissao.id = :profissao AND E.pessoa.id = :pessoa ");
            query.setMaxResults(1);
            query.setParameter("pessoa", idPessoa);
            query.setParameter("profissao", idProfissao);
            List list = query.getResultList();
            if (!list.isEmpty() && list.size() == 1) {
                return (Equipe) query.getSingleResult();
            }
        } catch (Exception e) {

        }
        return null;
    }

}
