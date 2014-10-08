package br.com.clinicaintegrada.administrativo.dao;

import br.com.clinicaintegrada.administrativo.ModeloContrato;
import br.com.clinicaintegrada.administrativo.ModeloContratoCampos;
import br.com.clinicaintegrada.administrativo.ModeloContratoServico;
import br.com.clinicaintegrada.financeiro.Servicos;
import br.com.clinicaintegrada.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class ModeloContratoDao extends DB {

    public ModeloContrato pesquisaCodigoServico(int servico) {
        ModeloContrato result = null;
        try {
            Query query = getEntityManager().createQuery(" SELECT mcs.contrato FROM ModeloContratoServico mcs WHERE mcs.servicos.id = :servico ");
            query.setParameter("servico", servico);
            result = (ModeloContrato) query.getSingleResult();
        } catch (Exception e) {
            e.getMessage();
        }
        return result;
    }

    public boolean existeModeloContrato(ModeloContrato matriculaContrato) {
        try {
            Query query = getEntityManager().createQuery(" SELECT MC FROM ModeloContrato AS MC WHERE MC.titulo = :titulo AND MC.modulo.id = :idModulo AND MC.cliente.id = :idCliente");
            query.setParameter("idModulo", matriculaContrato.getModulo().getId());
            query.setParameter("titulo", matriculaContrato.getTitulo());
            query.setParameter("idCliente", matriculaContrato.getCliente().getId());
            if (query.getResultList().size() > 0) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    public boolean existeServicoModeloContrato(int idServico) {
        try {
            Query query = getEntityManager().createQuery(" SELECT MCS FROM ModeloContratoServico AS MCS WHERE MCS.servicos.id = :idServico ");
            query.setParameter("idServico", idServico);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return true;
            }
        } catch (Exception e) {

        }
        return false;
    }

    public List pesquisaTodosPorModulo(int idModulo, int idCliente) {
        try {
            Query query = getEntityManager().createQuery(" SELECT mc FROM ModeloContrato mc WHERE mc.modulo.id = :idModulo AND mc.cliente.id = :idCliente");
            query.setParameter("idModulo", idModulo);
            query.setParameter("idCliente", idCliente);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    public List<ModeloContratoServico> pesquisaModeloContratoServico(int idModeloContrato) {
        List<ModeloContratoServico> list;
        try {
            Query query = getEntityManager().createQuery(" SELECT mcs FROM ModeloContratoServico mcs WHERE mcs.contrato.id = :idModeloContrato ");
            query.setParameter("idModeloContrato", idModeloContrato);
            list = query.getResultList();
            return list;
        } catch (Exception e) {
            return new ArrayList();
        }
    }

    public boolean validaModeloContratoServico(int idModeloContrato, int idServico) {
        try {
            Query query = getEntityManager().createQuery(" SELECT mcs FROM ModeloContratoServico mcs WHERE mcs.contrato.id = :idModeloContrato AND mcs.servicos.id = :idServico ");
            query.setParameter("idModeloContrato", idModeloContrato);
            query.setParameter("idServico", idServico);
            if (query.getResultList().size() > 0) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public boolean existeModeloContratoCampo(ModeloContratoCampos mcc, String tipoVerificacao) {
        String queryCampo;
        if (tipoVerificacao.equals("campo")) {
            queryCampo = "UPPER(MCC.campo) = '" + mcc.getCampo().toUpperCase() + "'";
        } else if (tipoVerificacao.equals("variavel")) {
            queryCampo = "UPPER(MCC.variavel) = '" + mcc.getCampo().toUpperCase() + "'";
        } else if (tipoVerificacao.equals("todos")) {
            queryCampo = "UPPER(MCC.variavel) = '" + mcc.getCampo().toUpperCase() + "' AND UPPER(MCC.campo) = '" + mcc.getCampo().toUpperCase() + "'";
        } else {
            return false;
        }
        try {
            Query query = getEntityManager().createQuery(" SELECT MCC FROM ModeloContratoCampos AS MCC WHERE " + queryCampo + " AND MCC.modulo.id = " + mcc.getModulo().getId());
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return true;
            }
        } catch (Exception e) {
            return false;
        } finally {
            return false;
        }
    }

    public List listaModeloContratoCampo(int idModulo) {
        return listaModeloContratoCampo(idModulo, "");
    }

    public List listaModeloContratoCampo(int idModulo, String descricaoPesquisa) {
        String filtroString = "";
        if (!descricaoPesquisa.equals("")) {
            filtroString = " AND UPPER(MCC.campo) LIKE '%" + descricaoPesquisa.toLowerCase().toUpperCase() + "%' ";
        }
        List list = new ArrayList();
        String tipoPesquisaModulo = "";
        if (idModulo > 0) {
            tipoPesquisaModulo = " WHERE MCC.modulo.id = :idModulo " + filtroString;
        }
        try {
            Query query = getEntityManager().createQuery(" SELECT MCC FROM ModeloContratoCampos AS MCC " + tipoPesquisaModulo + " ORDER BY MCC.modulo.descricao ASC, MCC.campo ASC, MCC.variavel ASC ");
            if (idModulo > 0) {
                query.setParameter("idModulo", idModulo);
            }
            list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return list;
        } finally {
            return list;
        }
    }

    public List listaModulosModeloContratoCampos() {
        List list = new ArrayList();
        try {
            Query query = getEntityManager().createQuery(" SELECT MCC.modulo FROM ModeloContratoCampos AS MCC GROUP BY MCC.modulo ORDER BY MCC.modulo.descricao ASC ");
            list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return list;
        } finally {
            return list;
        }
    }

    public List<Servicos> listaServicosDispiniveis() {
        List list = new ArrayList();
        try {
            Query query = getEntityManager().createQuery(" SELECT S FROM Servicos AS S WHERE S.id NOT IN(SELECT MCS.servicos.id FROM ModeloContratoServico AS MCS GROUP BY MCS.servicos.id )  ORDER BY S.descricao ASC ");
            list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return list;
    }
}
