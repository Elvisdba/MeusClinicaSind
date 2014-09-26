package br.com.clinicaintegrada.financeiro.dao;

import br.com.clinicaintegrada.financeiro.ContaBanco;
import br.com.clinicaintegrada.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class ContaBancoDao extends DB {

    public List findAllByCliente(int idCliente) {
        try {
            Query query = getEntityManager().createQuery("SELECT CB FROM ContaBanco AS CB WHERE CB.cliente.id = :idCliente ORDER BY CB.banco.banco");
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return null;
    }

    public List findContaBanco(String descricao, String por, String como) {
        descricao = descricao.toLowerCase().toUpperCase();
        String textQuery = "";
        switch (como) {
            case "T":
                textQuery = "SELECT CB FROM ContaBanco AS CB";
                break;
            case "P":
                descricao = "%" + descricao + "%";
                if (por.equals("banco")) {
                    textQuery = "SELECT CB FROM ContaBanco AS CB WHERE UPPER(CB.banco." + por + ") LIKE :descricao";
                } else {
                    textQuery = "SELECT CB FROM ContaBanco AS CB WHERE UPPER(CB." + por + ") LIKE :descricao";
                }
                break;
            case "I":
                descricao = descricao + "%";
                if (por.equals("banco")) {
                    textQuery = "SELECT CB FROM ContaBanco AS CB WHERE UPPER(CB.banco." + por + ") LIKE :descricao";
                } else {
                    textQuery = "SELECT CB FROM ContaBanco AS CB WHERE UPPER(CB." + por + ") LIKE :descricao";
                }
                break;
        }
        try {
            Query query = getEntityManager().createQuery(textQuery);
            if ((descricao != null) && (!(como.equals("T")))) {
                query.setParameter("descricao", descricao);
            }
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {

        }
        return new ArrayList<>();
    }

    public List findPlano5ByConta() {
        try {
            Query qry = getEntityManager().createQuery(
                    "   SELECT P5                                                           "
                    + "   FROM Plano5 AS P5                                                 "
                    + "  WHERE P5.plano4.id IN (SELECT CR.plano4.id                         "
                    + "                           FROM ContaRotina AS CR                    "
                    + "                          WHERE CR.rotina.id = 2                     "
                    + "                            AND (CR.pagRec IS NULL OR CR.pagRec = '')"
                    + "                        )                                            "
                    + "    AND P5.contaBanco IS NULL                                        "
            );
            List list = qry.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    public ContaBanco findContaBancoByConta(String conta) {
        try {
            Query query = getEntityManager().createQuery("SELECT C FROM ContaBanco AS C WHERE C.conta = :conta");
            query.setParameter("conta", conta);
            List list = query.getResultList();
            if (!list.isEmpty() && list.size() == 1) {
                return (ContaBanco) query.getSingleResult();

            }
        } catch (Exception e) {
        }
        return null;
    }
}
