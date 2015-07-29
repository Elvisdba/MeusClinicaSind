package br.com.clinicaintegrada.seguranca.dao;

import br.com.clinicaintegrada.principal.DB;
import br.com.clinicaintegrada.seguranca.Cliente;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class ClienteDao extends DB {

    public Cliente findByIdentificador(String identificador) {
        try {
            Query query = getEntityManager().createQuery(" SELECT C FROM Cliente AS C WHERE C.identifica = :identificador ");
            query.setParameter("identificador", identificador);
            if (!query.getResultList().isEmpty()) {
                return (Cliente) query.getSingleResult();
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public boolean existeIdentificador(Cliente cliente) {
        try {
            Query query = getEntityManager().createQuery(" SELECT C FROM Cliente AS C WHERE C.identifica = :identificador ");
            query.setParameter("identificador", cliente.getIdentifica());
            if (!query.getResultList().isEmpty()) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public boolean existeIdentificadorPessoa(Cliente cliente) {
        try {
            Query query = getEntityManager().createQuery(" SELECT C FROM Cliente AS C WHERE C.identifica = :identificador AND C.idJuridica = :idJuridica ");
            query.setParameter("identificador", cliente.getIdentifica());
            query.setParameter("idJuridica", cliente.getIdJuridica());
            if (!query.getResultList().isEmpty()) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public List listCliente(String descricaoPesquisa) {
        try {
            Query query = getEntityManager().createQuery(" SELECT C FROM Cliente AS C WHERE C.juridica.fantasia LIKE '%" + descricaoPesquisa + "%' OR C.identifica LIKE '%" + descricaoPesquisa + "%' OR C.juridica.pessoa.nome LIKE '%" + descricaoPesquisa + "%' ");
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }
}
