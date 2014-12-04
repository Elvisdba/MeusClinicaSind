package br.com.clinicaintegrada.financeiro.dao;

import br.com.clinicaintegrada.financeiro.ContaRotina;
import br.com.clinicaintegrada.financeiro.Plano4;
import br.com.clinicaintegrada.principal.DB;
import java.util.List;
import javax.persistence.Query;

public class ContaRotinaDao extends DB  {

    public List pesquisaPlano5Partida(int rotina) {
        List result = null;
        try {
            Query qry = getEntityManager().createQuery(
                    "select pl5.conta"
                    + "  from ContaRotina c, Plano5 pl5 "
                    + " where c.partida  = 1"
                    + "   and c.rotina.id = :rotina"
                    + "   and c.plano4.id = pl5.plano4.id");
            qry.setParameter("rotina", rotina);
            result = (List) qry.getResultList();
        } catch (Exception e) {
        }
        return result;
    }


    public List pesquisaContasPorRotina(int rotina) {
        List result = null;
        try {
            Query qry = getEntityManager().createQuery(
                    "select pl5"
                    + "  from ContaRotina c, Plano5 pl5 "
                    + " where c.partida  = 1"
                    + "   and c.rotina.id = :rotina"
                    + "   and c.plano4.id = pl5.plano4.id");
            qry.setParameter("rotina", rotina);
            result = (List) qry.getResultList();
        } catch (Exception e) {
        }
        return result;
    }


    public List pesquisaContasPorRotina() {
        List result = null;
        try {
            Query qry = getEntityManager().createQuery(
                    "select pl5"
                    + "  from ContaRotina c, Plano5 pl5 "
                    + " where c.partida  = 1"
                    + "   and c.rotina.id in (1,2)"
                    + "   and c.plano4.id = pl5.plano4.id");
            result = (List) qry.getResultList();
        } catch (Exception e) {
        }
        return result;
    }


    public List pesquisaPlano4Grupo(int rotina, String pagRec) {
        List result = null;
        try {
            Query qry = getEntityManager().createQuery(
                    "select c.plano4.conta"
                    + "  from ContaRotina c"
                    + " where c.partida  = 0"
                    + "   and c.pagRec = :pagRec"
                    + "   and c.rotina.id = :rotina");
            qry.setParameter("pagRec", pagRec);
            qry.setParameter("rotina", rotina);
            result = (List) qry.getResultList();
        } catch (Exception e) {
        }
        return result;
    }


    public List pesquisaPlano5(int p4, int rotina) {
        List result = null;
        try {
            Query qry = getEntityManager().createQuery(
                    "select pl5.conta"
                    + "  from ContaRotina c, Plano5 pl5 "
                    + " where c.plano4.id = :p4"
                    + "   and c.plano4.id = pl5.plano4.id"
                    + "   and c.rotina.id = :rotina");
            qry.setParameter("p4", p4);
            qry.setParameter("rotina", rotina);
            result = (List) qry.getResultList();
        } catch (Exception e) {
        }
        return result;
    }


    public Plano4 pesquisaPlano4PorDescricao(String desc) {
        Plano4 result = null;
        try {
            Query qry = getEntityManager().createQuery(
                    "select pl4"
                    + "  from Plano4 pl4"
                    + " where pl4.conta like :desc");
            qry.setParameter("desc", desc);
            result = (Plano4) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }


    public ContaRotina pesquisaContaRotina(int id, int rot) {
        ContaRotina result = null;
        try {
            Query qry = getEntityManager().createQuery(
                    "select c"
                    + "  from ContaRotina c"
                    + " where c.plano4.id = :pid"
                    + "   and c.rotina.id = :rot");
            qry.setParameter("pid", id);
            qry.setParameter("rot", rot);
            result = (ContaRotina) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }


    public int pesquisaRotina(int id_plano4) {
        int result = -1;
        try {
            Query qry = getEntityManager().createQuery(
                    "select c.rotina.id"
                    + "  from ContaRotina c"
                    + " where c.plano4.id = :plano4");
            qry.setParameter("plano4", id_plano4);
            result = (Integer) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }


    public int verificaRotinaParaConta(int id_plano5) {
        int result = 0;
        try {
            Query qry = getEntityManager().createQuery(
                    "select c.rotina.id"
                    + "  from ContaRotina c,"
                    + "       Plano5 pl5 "
                    + " where pl5.id = :idPlano5"
                    + "   and c.plano4.id = pl5.plano4.id");
            qry.setParameter("idPlano5", id_plano5);
            result = (Integer) qry.getSingleResult();
        } catch (Exception e) {
            e.getMessage();
        }
        return result;
    }
}