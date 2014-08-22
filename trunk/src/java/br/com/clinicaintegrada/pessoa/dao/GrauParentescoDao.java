package br.com.clinicaintegrada.pessoa.dao;

//package br.com.rtools.pessoa.dao;
//
//import br.com.rtools.principal.DB;
//import java.util.ArrayList;
//import java.util.List;
//import javax.persistence.Query;
//
//public class GrauParentescoDao extends DB {
//
//    public List pesquisaTodos() {
//        try {
//            Query query = getEntityManager().createQuery("SELECT GP FROM GrauParentesco AS GP ORDER BY GP.descricao ASC");
//            List list = query.getResultList();
//            if (!list.isEmpty()) {
//                return list;
//            }
//        } catch (Exception e) {
//        }
//        return new ArrayList();
//    }
//
//    public boolean existeParentescoCliente(String descricao) {
//        try {
//            Query query = getEntityManager().createQuery("SELECT GP FROM GrauParentesco AS GP WHERE GP.cliente.id = :cliente AND UPPER(GP.descricao) LIKE :descricao ORDER BY GP.descricao ASC");
//            List list = query.getResultList();
//            query.setParameter("descricao", descricao.toUpperCase());
//            query.setMaxResults(1);
//            if (!list.isEmpty()) {
//                return true;
//            }
//        } catch (Exception e) {
//        }
//        return false;
//    }
//
//}
