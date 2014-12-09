package br.com.clinicaintegrada.coordenacao.dao;

import br.com.clinicaintegrada.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class PertenceDao extends DB {

    private Integer pertenceGrupo;

    public boolean exists(int idContrato, String descricao, String dataEntradaString) {
        descricao = descricao.toUpperCase();
        try {
            Query query = getEntityManager().createQuery("SELECT PE FROM PertenceEntrada AS PE WHERE PE.contrato.id = :contrato AND UPPER(PE.descricao) = :descricao AND PE.entrada = :entrada");
            query.setParameter("contrato", idContrato);
            query.setParameter("descricao", descricao);
            query.setParameter("entrada", dataEntradaString);
            query.setMaxResults(1);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public List findAllByContrato(int idContrato) {
        try {
            String queryString = "SELECT PE FROM PertenceEntrada AS PE WHERE PE.contrato.id = :contrato ";
            if (pertenceGrupo != null) {
                queryString += " AND PE.pertenceGrupo.id = " + pertenceGrupo;
            }
            queryString += " ORDER BY PE.entrada, PE.descricao ";
            Query query = getEntityManager().createQuery(queryString);
            query.setParameter("contrato", idContrato);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return new ArrayList();
    }

    public Integer countPertenceSaida(int idPertenceEntrada) {
        try {
            Query query = getEntityManager().createQuery("SELECT SUM(PS.quantidade) FROM PertenceSaida AS PS WHERE PS.pertenceEntrada.id = :pertenceEntrada");
            query.setParameter("pertenceEntrada", idPertenceEntrada);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                Long l = (Long) query.getSingleResult();
                Integer count = Integer.parseInt("" + l);
                return count;
            }
        } catch (Exception e) {
            return 0;
        }
        return 0;
    }

    public List findSaidaAllByEntrada(int idPertenceEntrada) {
        try {
            Query query = getEntityManager().createQuery("SELECT PS FROM PertenceSaida AS PS WHERE PS.pertenceEntrada.id = :pertenceEntrada");
            query.setParameter("pertenceEntrada", idPertenceEntrada);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    public Integer getPertenceGrupo() {
        return pertenceGrupo;
    }

    public void setPertenceGrupo(Integer pertenceGrupo) {
        this.pertenceGrupo = pertenceGrupo;
    }

}
