package br.com.clinicaintegrada.seguranca.dao;

import br.com.clinicaintegrada.principal.DB;
import br.com.clinicaintegrada.seguranca.Rotina;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

@SuppressWarnings("unchecked")
public class RotinaDao extends DB {

    public Rotina pesquisaRotinaPermissao(String pagina) {
        try {
            Query query = getEntityManager().createQuery("SELECT R FROM Rotina AS R WHERE R.pagina LIKE :pagina");
            query.setParameter("pagina", "%" + pagina + "%");
            List list = query.getResultList();
            if (!list.isEmpty() && list.size() == 1) {
                return (Rotina) query.getSingleResult();
            }
        } catch (Exception e) {
        }
        return null;
    }

    public Rotina pesquisaRotinaPermissaoPorClasse(String className) {
        try {
            Query query = getEntityManager().createQuery("SELECT R FROM Rotina AS R WHERE R.classe LIKE :classe");
            query.setParameter("classe", className);
            List list = query.getResultList();
            if (!list.isEmpty() && list.size() == 1) {
                return (Rotina) query.getSingleResult();
            }
        } catch (Exception e) {
        }
        return null;
    }

    public Rotina pesquisaRotinaPorPagina(String pagina) {
        try {
            Query query = getEntityManager().createQuery("SELECT R FROM Rotina AS R WHERE R.pagina LIKE :pagina");
            query.setParameter("pagina", "%" + pagina + "%");
            List list = query.getResultList();
            if (!list.isEmpty() && list.size() == 1) {
                return (Rotina) query.getSingleResult();
            }
        } catch (Exception e) {
        }
        return null;
    }

    public Rotina pesquisaRotinaPorPagina(String pagina, boolean aliasProjeto) {
        try {
            Query query = getEntityManager().createQuery("SELECT ROT FROM Rotina AS ROT WHERE ROT.pagina LIKE 'ClinicaIntegrada/" + pagina + ".jsf' OR ROT.pagina LIKE '\"/ClinicaIntegrada/" + pagina + ".jsf\"'");
            List list = query.getResultList();
            if (!list.isEmpty() && list.size() == 1) {
                return (Rotina) query.getSingleResult();
            }
        } catch (Exception e) {
        }
        return null;
    }

    public Rotina pesquisaRotinaPorClasse(String classe) {
        try {
            Query query = getEntityManager().createQuery("SELECT R FROM Rotina AS R WHERE R.classe = :classe");
            query.setParameter("classe", classe);
            List list = query.getResultList();
            if (!list.isEmpty() && list.size() == 1) {
                return (Rotina) query.getSingleResult();
            }
        } catch (Exception e) {
        }
        return null;
    }

    public Rotina pesquisaAcesso(String pagina) {
        try {
            Query query = getEntityManager().createQuery("SELECT R FROM Rotina AS R WHERE R.pagina = :pagina");
            query.setParameter("pagina", pagina);
            List list = query.getResultList();
            if (!list.isEmpty() && list.size() == 1) {
                return (Rotina) query.getSingleResult();
            }
        } catch (Exception e) {
        }
        return null;
    }

    public List<Rotina> pesquisaRotina(String rotina) {
        try {
            Query query = getEntityManager().createQuery("SELECT R FROM Rotina AS R WHERE R.rotina LIKE :rotina");
            query.setParameter("rotina", "%" + rotina + "%");
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    public List<Rotina> pesquisaRotinaPorDescricao(String descricaoRotina) {
        try {
            Query query = getEntityManager().createQuery("SELECT ROT FROM Rotina AS ROT WHERE UPPER(ROT.rotina) LIKE '%" + descricaoRotina.toUpperCase() + "%' OR UPPER(ROT.pagina) LIKE '%" + descricaoRotina.toUpperCase() + "%' ORDER BY ROT.rotina ASC, ROT.pagina ASC ");
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    public List listaRotinaPermissaoAgrupado(int idModulo, int idCliente) {
        try {
            Query query = getEntityManager().createQuery(
                    "     SELECT P.rotina                                       "
                    + "     FROM Permissao AS P                                 "
                    + "    WHERE P.modulo.id = :modulo                          "
                    + "      AND P.rotina.ativo = true                          "
                    + "      AND P.cliente.id   = :cliente                      "
                    + " GROUP BY P.rotina                                       "
                    + " ORDER BY P.rotina.rotina ASC                            ");
            query.setParameter("modulo", idModulo);
            query.setParameter("cliente", idCliente);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    public List pesquisaTodosOrdenadoAtivo() {
        try {
            Query qry = getEntityManager().createQuery(" SELECT rot FROM Rotina rot WHERE rot.ativo = true ORDER BY rot.rotina ASC ");
            List list = qry.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    public List pesquisaRotinasDisponiveisModulo(int idModulo) {
        try {
            Query query = getEntityManager().createQuery(" SELECT R FROM Rotina AS R WHERE R.ativo = true AND R.id NOT IN ( SELECT PER.rotina.id FROM Permissao AS PER WHERE PER.modulo.id = :modulo GROUP BY PER.rotina.id ) ORDER BY R.rotina ASC ");
            query.setParameter("modulo", idModulo);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return new ArrayList();
    }

    public List pesquisaTodosSimples() {
        try {
            Query query = getEntityManager().createQuery("SELECT R FROM Rotina AS R WHERE R.classe IS NOT NULL ORDER BY R.rotina ASC ");
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    public boolean existeRotina(Rotina rotina) {
        try {
            Query query = getEntityManager().createQuery("SELECT ROT FROM Rotina AS ROT WHERE UPPER(ROT.rotina) = :descricaoRotina");
            query.setParameter("descricaoRotina", rotina.getRotina().toUpperCase());
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }
}
