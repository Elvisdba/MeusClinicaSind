package br.com.clinicaintegrada.sistema.dao;

import br.com.clinicaintegrada.principal.DB;
import br.com.clinicaintegrada.sistema.Links;
import javax.persistence.Query;

public class LinksDao extends DB {

    public Links pesquisaNomeArquivo(String arquivo) {
        try {
            Query qry = getEntityManager().createQuery(
                    "select l "
                    + "  from Links l "
                    + " where l.nomeArquivo = '" + arquivo + "'");
            return (Links) qry.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}
