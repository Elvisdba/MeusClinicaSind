package br.com.clinicaintegrada.sistema.dao;

import br.com.clinicaintegrada.principal.DB;
import br.com.clinicaintegrada.sistema.Atalhos;
import br.com.clinicaintegrada.sistema.ContadorAcessos;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

@SuppressWarnings("unchecked")
public class AtalhoDao extends DB {

   
    public List<Atalhos> listaTodos(int id_pessoa) {
        try {
            Query qry = getEntityManager().createQuery(
                    "select at "
                    + "  from Atalhos at "
                    + " where at.pessoa.id = " + id_pessoa);
            return qry.getResultList();
        } catch (Exception e) {
            return new ArrayList();
        }
    }

   
    public Atalhos pesquisaPorSigla(String sigla) {
        try {
            Query qry = getEntityManager().createQuery(
                    "select at "
                    + "  from Atalhos at "
                    + " where at.sigla = '" + sigla + "'");
            return (Atalhos) qry.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

   
    public Atalhos pesquisaPorRotina(int id_rotina) {
        try {
            Query qry = getEntityManager().createQuery(
                    "select at "
                    + "  from Atalhos at "
                    + " where at.rotina.id = " + id_rotina);
            return (Atalhos) qry.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

   
    public List<ContadorAcessos> listaAcessosUsuario(int id_usuario) {
        try {
            Query qry = getEntityManager().createQuery(
                    "select ca from ContadorAcessos ca where ca.usuario.id = " + id_usuario + " order by ca.acessos desc");
            qry.setMaxResults(15);
            return qry.getResultList();
        } catch (Exception e) {
            return new ArrayList();
        }
    }

   
    public ContadorAcessos pesquisaContadorAcessos(int id_usuario, int id_rotina) {
        try {
            Query qry = getEntityManager().createQuery(
                    "select ca from ContadorAcessos ca where ca.usuario.id = " + id_usuario + " and ca.rotina.id = " + id_rotina);
            return (ContadorAcessos) qry.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}
