package br.com.clinicaintegrada.financeiro.dao;

import br.com.clinicaintegrada.financeiro.Plano5;
import br.com.clinicaintegrada.principal.DB;
import br.com.clinicaintegrada.utils.AnaliseString;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class PlanoDao extends DB {

    public List findAllPlanoByCliente(int idCliente) {
        try {
            Query query = getEntityManager().createQuery("SELECT P FROM Plano AS P WHERE P.cliente.id = :idCliente");
            query.setParameter("idCliente", idCliente);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    public List findAllPlano2ByCliente(int idCliente) {
        try {
            Query query = getEntityManager().createQuery("SELECT P2 FROM Plano2 AS P2 WHERE P2.plano.cliente.id = :idCliente");
            query.setParameter("idCliente", idCliente);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    public List findAllPlano3ByCliente(int idCliente) {
        try {
            Query query = getEntityManager().createQuery("SELECT P3 FROM Plano3 AS P3 WHERE P3.plano2.plano.cliente.id = :idCliente");
            query.setParameter("idCliente", idCliente);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    public List findAllPlano4ByCliente(int idCliente) {
        try {
            Query query = getEntityManager().createQuery("SELECT P4 FROM Plano4 AS P4 WHERE P4.plano3.plano2.plano.cliente.id = :idCliente");
            query.setParameter("idCliente", idCliente);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    public List findAllPlano5ByCliente(int idCliente) {
        try {
            Query query = getEntityManager().createQuery("SELECT P5 FROM Plano5 AS P5 WHERE P5.plano4.plano3.plano2.plano.cliente.id = :idCliente");
            query.setParameter("idCliente", idCliente);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    public List findAllPlano2ByClienteAndPlano1(int idCliente, int idPlano1) {
        try {
            Query query = getEntityManager().createQuery("SELECT P2 FROM Plano2 AS P2 WHERE P2.plano.cliente.id = :idCliente AND P2.plano.id = :idPlano1");
            query.setParameter("idCliente", idCliente);
            query.setParameter("idPlano1", idPlano1);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    public List findAllPlano3ByClienteAndPlano2(int idCliente, int idPlano2) {
        try {
            Query query = getEntityManager().createQuery("SELECT P3 FROM Plano3 AS P3 WHERE P3.plano2.plano.cliente.id = :idCliente AND P3.plano2.id = :idPlano2");
            query.setParameter("idCliente", idCliente);
            query.setParameter("idPlano2", idPlano2);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    public List findAllPlano4ByClienteAndPlano3(int idCliente, int idPlano3) {
        try {
            Query query = getEntityManager().createQuery("SELECT P4 FROM Plano4 AS P4 WHERE P4.plano3.plano2.plano.cliente.id = :idCliente AND P4.plano3.id = :idPlano3");
            query.setParameter("idCliente", idCliente);
            query.setParameter("idPlano3", idPlano3);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    public List findAllPlano5ByClienteAndPlano4(int idCliente, int idPlano4) {
        try {
            Query query = getEntityManager().createQuery("SELECT P5 FROM Plano5 AS P5 WHERE P5.plano4.plano3.plano2.plano.cliente.id = :idCliente AND P5.plano4.id = :idPlano4");
            query.setParameter("idCliente", idCliente);
            query.setParameter("idPlano4", idPlano4);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    public List findPlanoAdvanced(String desc, String por, String como, String plano, int id, int idCliente) {
        String queryString = "";
        if (como.equals("T")) {
            switch (plano) {
                case "Plano":
                    queryString = "SELECT P FROM Plano AS P WHERE P.cliente.id = " + idCliente + " ORDER BY P.numero";
                    break;
                case "Plano2":
                    queryString = "SELECT P2 FROM Plano2 AS P2 WHERE P2.plano.id = " + id + " AND P2.plano.cliente.id = " + idCliente + " ORDER BY P2.numero";
                    break;
                case "Plano3":
                    queryString = "SELECT P3 FROM Plano3 AS P3 WHERE P3.plano2.id = " + id + " AND P3.plano2.plano.cliente.id = " + idCliente + " ORDER BY P3.numero";
                    break;
                case "Plano4":
                    queryString = "SELECT P4 FROM Plano4 AS P4 WHERE P4.plano3.id = " + id + " AND P4.plano3.plano2.plano.cliente.id = " + idCliente + " ORDER BY P3.numero";
                    break;
                case "Plano5":
                    if (id != -1) {
                        queryString = "SELECT P5 FROM Plano5 AS P5 WHERE P5.plano4.id = " + id + " AND P5.plano4.plano3.plano2.plano.cliente.id = " + idCliente + " ORDER BY P5.numero";
                    } else {
                        queryString = "SELECT P5 FROM Plano5 AS P5" + " AND P5.plano4.plano3.plano2.plano.cliente.id = " + idCliente + " ORDER BY P5.numero";
                    }
                    break;
            }
            try {
                Query query = getEntityManager().createQuery(queryString);
                List list = query.getResultList();
                if (!list.isEmpty()) {
                    return list;
                }
            } catch (Exception e) {
                return new ArrayList();
            }
        } else {
            switch (como) {
                case "I":
                    desc = desc.toLowerCase().toUpperCase() + "%";
                    break;
                case "P":
                    desc = "%" + desc.toLowerCase().toUpperCase() + "%";
                    break;
            }

            switch (plano) {
                case "Plano":
                    queryString = "SELECT P FROM Plano AS P WHERE p." + por + " LIKE :desc AND P.cliente.id = :cliente ORDER BY p.numero";
                    break;
                case "Plano2":
                    queryString = "SELECT P2 FROM Plano2 AS P2 WHERE P2." + por + " LIKE :desc AND P2.plano.id = " + id + " AND P2.plano.cliente.id = :idCliente ORDER BY P2.numero";
                    break;
                case "Plano3":
                    queryString = "SELECT P3 FROM Plano3 AS P3 WHERE P3." + por + " LIKE :desc AND P3.plano2.id = " + id + " AND P3.plano2.plano.cliente.id = :idCliente ORDER BY P3.numero";
                    break;
                case "Plano4":
                    queryString = "SELECT P4 FROM Plano4 AS P4 WHERE P4." + por + " LIKE :desc AND P4.plano3.id = " + id + " AND P4.plano3.plano2.plano.cliente.id = :idCliente ORDER BY P4.numero";
                    break;
                case "Plano5":
                    if (id != -1) {
                        queryString = "SELECT P5 FROM Plano5 AS P5 WHERE P5." + por + " LIKE :desc AND P5.plano5.id = " + id + " AND P5.plano4.plano3.plano2.plano.cliente.id = :idCliente ORDER BY P5.numero";
                    } else {
                        queryString = "SELECT P5 FROM Plano5 AS P5 WHERE P5." + por + " LIKE :desc AND P5.plano4.plano3.plano2.plano.cliente.id = :idCliente  ORDER BY P5.numero";
                    }
                    break;
            }

            if (desc != null) {
                try {
                    Query query = getEntityManager().createQuery(queryString);
                    query.setParameter("desc", desc);
                    query.setParameter("idCliente", idCliente);
                    List list = query.getResultList();
                    if (!list.isEmpty()) {
                        return list;
                    }
                } catch (Exception e) {
                    return new ArrayList();
                }
            }
        }
        return new ArrayList();
    }

    public List findByPlano(String desc, String por, String como, String plano, int idCliente) {
        desc = AnaliseString.removerAcentos(desc);
        desc = desc.toUpperCase();
        String textQuery = null;
        switch (como) {
            case "T":
                textQuery = "";
                break;
            case "P":
                desc = "%" + desc.toLowerCase().toUpperCase() + "%";
                textQuery = "SELECT O.* FROM " + plano + " O WHERE O.id_cliente = " + idCliente + " AND UPPER(FUNC_TRANSLATE(O." + por + ")) LIKE '" + desc + "' ORDER BY O." + por;
                break;
            case "I":
                desc = desc.toLowerCase().toUpperCase() + "%";
                textQuery = "SELECT O.* FROM " + plano + " O WHERE O.id_cliente = " + idCliente + " AND UPPER(FUNC_TRANSLATE(O." + por + ")) LIKE '" + desc + "' ORDER BY O." + por;
                break;
        }
        try {
            Query query = getEntityManager().createNativeQuery(textQuery, Plano5.class);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    public Plano5 findPlano5ByContaBanco(int idContaBanco, int idCliente) {
        try {
            Query query = getEntityManager().createQuery("SELECT P5 FROM Plano5 AS P5 WHERE P5.contaBanco.id = :idContaBanco AND P5.contaBanco.cliente.id = :idCliente");
            query.setParameter("idContaBanco", idContaBanco);
            query.setParameter("idCliente", idCliente);
            List list = query.getResultList();
            if (!list.isEmpty() && list.size() == 1) {
                return (Plano5) query.getSingleResult();
            }
        } catch (Exception e) {
        }
        return null;
    }

    // VERIFICAR E ENTENDER NOS TESTES AS DIFERENÇAS ENTRE ESTE MÉTODO E O MÉTODO findPlano5ByContaBanco(int idContaBanco);
    // PARECEM SER IGUAIS - ANALISAR
    public List findPlano5ContaComID(int idContaBanco) {
        try {
            Query query = getEntityManager().createQuery(
                    "   SELECT P5                               "
                    + "   FROM Plano5 AS P5,                    "
                    + "        ContaBanco AS CB,                "
                    + "        Banco AS B                       "
                    + "  WHERE P5.contaBanco.id = CB.id         "
                    + "    AND CB.banco.id = B.id               "
                    + "    AND P5.contaBanco.id = :idContaBanco ");
            query.setParameter("idContaBanco", idContaBanco);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    public List findPlano5InContaRotina(int idCliente) {
        try {
            Query query = getEntityManager().createQuery(
                    "   SELECT P5                                                           "
                    + "   FROM Plano5 AS P5                                                 "
                    + "  WHERE P5.plano4.id IN(SELECT CR.plano4.id                          "
                    + "                          FROM ContaRotina AS CR                     "
                    + "                         WHERE CR.rotina.id = 2                      "
                    + "                           AND (CR.pagRec IS NULL OR CR.pagRec = '') "
                    + "  )                                                                  "
                    + "    AND P5.contaBanco IS NULL                                        "
                    + "    AND P5.plano4.plano3.plano2.plano.cliente.id = :idCliente        ");
            query.setParameter("idCliente", idCliente);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return new ArrayList();
    }

    public List findPlanosByClass(String className, int idCliente) {
        String queryPlano = "";
        switch (className) {
            case "Plano":
                queryPlano = "P.cliente.id";
                break;
            case "Plano2":
                queryPlano = "P.plano.cliente.id";
                break;
            case "Plano3":
                queryPlano = "P.plano2.plano.cliente.id";
                break;
            case "Plano4":
                queryPlano = "P.plano3.plano2.plano.cliente.id";
                break;
            case "Plano5":
                queryPlano = "P.plano4.plano3.plano2.plano.cliente.id";
                break;
        }
        try {
            String queryString = "SELECT P FROM " + className + " AS P " + " WHERE " + queryPlano + " = :idCliente ORDER BY P.classificador, P.numero";
            Query query = getEntityManager().createQuery(queryString);
            query.setParameter("idCliente", idCliente);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }

        return new ArrayList();
    }

    public List findPlano5ByContaRotina(int idRotina) {
        try {
            Query query = getEntityManager().createQuery(
                    "  SELECT P5                        "
                    + "  FROM ContaRotina AS C,         "
                    + "       Plano5 AS P5              "
                    + " WHERE C.partida = 1             "
                    + "   AND C.rotina.id = :idRotina   "
                    + "   AND C.plano4.id = P5.plano4.id");
            query.setParameter("idRotina", idRotina);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }
}
