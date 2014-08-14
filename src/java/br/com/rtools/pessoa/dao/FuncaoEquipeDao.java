package br.com.rtools.pessoa.dao;

import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class FuncaoEquipeDao extends DB {

    public List pesquisaTodasFuncaoEquipePorCliente(int cliente) {
        try {
            Query query = getEntityManager().createQuery("SELECT FE FROM FuncaoEquipe AS FE WHERE FE.cliente.id = :cliente ORDER BY FE.tipoAtendimento.descricao ASC, FE.profissao.profissao ASC, FE.tipoDocumentoProfissao.descricao ASC");
            query.setParameter("cliente", cliente);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {

        }
        return new ArrayList();
    }

    public boolean existeFuncaoEquipe(int tipoAtendimento, int tipoDocumentoProfissao, int profissao, int cliente) {
        try {
            Query query = getEntityManager().createQuery("SELECT FE FROM FuncaoEquipe AS FE WHERE FE.tipoAtendimento.id = :tipoAtendimento AND FE.tipoDocumentoProfissao.id = :tipoDocumentoProfissao  AND FE.profissao.id = :profissao  AND FE.cliente.id = :cliente ");
            query.setMaxResults(1);
            query.setParameter("tipoAtendimento", tipoAtendimento);
            query.setParameter("tipoDocumentoProfissao", tipoDocumentoProfissao);
            query.setParameter("profissao", profissao);
            query.setParameter("cliente", cliente);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return true;
            }
        } catch (Exception e) {

        }
        return false;
    }

}
