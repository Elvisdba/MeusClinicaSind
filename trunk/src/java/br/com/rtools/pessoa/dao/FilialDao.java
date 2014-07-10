package br.com.rtools.pessoa.dao;

import br.com.rtools.pessoa.Filial;
import br.com.rtools.principal.DB;
import br.com.rtools.seguranca.Registro;
import br.com.rtools.seguranca.controleUsuario.SessaoCliente;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class FilialDao extends DB {

    public List pesquisaTodasCliente() {
        try {
            Query query = getEntityManager().createQuery("SELECT F FROM Filial AS F WHERE F.matriz.pessoa.cliente.id = :cliente ORDER BY F.filial.pessoa.nome ASC ");
            query.setParameter("cliente", SessaoCliente.get().getId());
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }

        } catch (Exception e) {
        }
        return null;
    }

    public Registro pesquisaRegistroPorCliente(int idCliente) {
        try {
            Query query = getEntityManager().createQuery("SELECT R FROM Registro AS R WHERE R.cliente = :id");
            query.setParameter("id", idCliente);
            List list = query.getResultList();
            if (!list.isEmpty() && list.size() == 1) {
                return (Registro) query.getSingleResult();
            }

        } catch (Exception e) {
        }
        return null;
    }

    public List pesquisaFilialExiste(int idFilial) {
        try {
            Query query = getEntityManager().createQuery("SELECT F.filial.pessoa.nome FROM Filial AS F WHERE F.filial.id = :filial ");
            query.setParameter("filial", idFilial);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    public List pesquisaRazao(int idMatriz) {
        try {
            Query query = getEntityManager().createQuery(
                    "select fil.matriz.pessoa.nome"
                    + "  from Filial fil "
                    + " where fil.matriz.id = :matriz ");
            query.setParameter("matriz", idMatriz);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    public List pesquisaFilialPorMatriz(int idMatriz) {
        try {
            Query query = getEntityManager().createQuery("SELECT F FROM Filial AS F WHERE F.matriz.id = :matriz AND F.matriz.id <> F.filial.id");
            query.setParameter("matriz", idMatriz);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    public Filial pesquisaMatrizFilial(int idMatriz, int idFilial) {
        Filial result = null;
        try {
            Query query = getEntityManager().createQuery("SELECT F FROM Filial AS F WHERE F.matriz.id = :matriz AND F.filial.id = :filial");
            query.setParameter("matriz", idMatriz);
            query.setParameter("filial", idFilial);
            List list = query.getResultList();
            if (!list.isEmpty() && list.size() == 1) {
                return (Filial) query.getSingleResult();
            }
        } catch (Exception e) {
        }
        return result;
    }

    public List pesquisaFilial(String desc, String por, String como, int idmatriz) {
        List result = null;
        if (por.equals("todos")) {
            try {
                Query qry = getEntityManager().createQuery("select jur from Juridica jur");
                result = qry.getResultList();
            } catch (Exception e) {
                result = null;
            }
        }

        if (como.equals("I")) {
            desc = desc.toLowerCase().toUpperCase() + "%";
            if (!(por.equals("todos")) && (desc != null) && (por.equals("CNPJ"))) {
                try {
                    Query qry = getEntityManager().createQuery(
                            "select juri "
                            + "  from Juridica juri"
                            + " where juri.pessoa.documento like :desc and juri.pessoa.tipoDocumento.id = 2");
                    qry.setParameter("desc", desc);
                    result = qry.getResultList();
                } catch (Exception e) {
                    result = null;
                }
            }
            if (!(por.equals("todos")) && (desc != null) && (por.equals("CEI"))) {
                try {
                    Query qry = getEntityManager().createQuery(
                            "select juri "
                            + "  from Juridica juri"
                            + " where juri.pessoa.documento like :desc and juri.pessoa.tipoDocumento.id = 3");
                    qry.setParameter("desc", desc);
                    result = qry.getResultList();
                } catch (Exception e) {
                    result = null;
                }
            }
            if (!(por.equals("todos")) && (desc != null) && (por.equals("CPF"))) {
                try {
                    Query qry = getEntityManager().createQuery(
                            "select juri "
                            + "  from Juridica juri"
                            + " where juri.pessoa.documento like :desc and juri.pessoa.tipoDocumento.id = 1");
                    qry.setParameter("desc", desc);
                    result = qry.getResultList();
                } catch (Exception e) {
                    result = null;
                }
            }
            if (!(por.equals("todos")) && (desc != null) && (por.equals("nome"))) {
                try {
                    Query qry = getEntityManager().createQuery(
                            "select juri "
                            + "  from Juridica juri"
                            + " where juri.pessoa.nome like :desc");
                    qry.setParameter("desc", desc);
                    result = qry.getResultList();
                } catch (Exception e) {
                    result = null;
                }
            }
        } else if (como.equals("D")) {
            if (!(por.equals("todos")) && (desc != null) && (por.equals("CNPJ"))) {
                try {
                    Query qry = getEntityManager().createQuery(
                            "select juri "
                            + "  from Juridica juri"
                            + " where juri.pessoa.documento = :desc and juri.pessoa.tipoDocumento.id = 2");
                    qry.setParameter("desc", desc);
                    result = qry.getResultList();
                } catch (Exception e) {
                    result = null;
                }
            }
            if (!(por.equals("todos")) && (desc != null) && (por.equals("CEI"))) {
                try {
                    Query qry = getEntityManager().createQuery(
                            "select juri "
                            + "  from Juridica juri"
                            + " where juri.pessoa.documento = :desc and juri.pessoa.tipoDocumento.id = 3");
                    qry.setParameter("desc", desc);
                    result = qry.getResultList();
                } catch (Exception e) {
                    result = null;
                }
            }
            if (!(por.equals("todos")) && (desc != null) && (por.equals("CPF"))) {
                try {
                    Query qry = getEntityManager().createQuery(
                            "select juri "
                            + "  from Juridica juri"
                            + " where juri.pessoa.documento = :desc and juri.pessoa.tipoDocumento.id = 1");
                    qry.setParameter("desc", desc);
                    result = qry.getResultList();
                } catch (Exception e) {
                    result = null;
                }
            }
            if (!(por.equals("todos")) && (desc != null) && (por.equals("nome"))) {
                try {
                    Query qry = getEntityManager().createQuery(
                            "select juri "
                            + "  from Juridica juri"
                            + " where juri.pessoa.nome = :desc");
                    qry.setParameter("desc", desc);
                    result = qry.getResultList();
                } catch (Exception e) {
                    result = null;
                }
            }
        }
        return result;
    }

}
