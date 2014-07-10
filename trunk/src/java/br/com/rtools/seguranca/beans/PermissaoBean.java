package br.com.rtools.seguranca.beans;

import br.com.rtools.logSistema.Logger;
import br.com.rtools.seguranca.*;
import br.com.rtools.seguranca.dao.PermissaoDao;
import br.com.rtools.seguranca.dao.PermissaoDepartamentoDao;
import br.com.rtools.seguranca.dao.RotinaDao;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DaoInterface;
import br.com.rtools.utilitarios.DataObject;
import br.com.rtools.utilitarios.Messages;
import br.com.rtools.utilitarios.Sessions;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class PermissaoBean implements Serializable {

    private Permissao permissao;
    private Modulo modulo;
    private Rotina rotina;
    private Evento evento;
    private List<Permissao> listaPermissoes;
    private PermissaoDepartamento permissaoDepartamento;
    private String msgConfirma;
    private String indicaTab;
    private String descricaoPesquisa;
    private String tabDisabled;
    private List listaPermissoesDisponiveis;
    private List listaPermissoesAdicionadas;
    private List<SelectItem> listaRotinas;
    private List<SelectItem> listaModulos;
    private List<SelectItem> listaEventos;
    private List<SelectItem> listaDepartamentos;
    private List<SelectItem> listaNiveis;
    private int idModulo;
    private int idRotina;
    private int idEvento;
    private int idDepartamento;
    private int idNivel;
    private int idIndex;

    @PostConstruct
    public void init() {
        permissao = new Permissao();
        modulo = new Modulo();
        rotina = new Rotina();
        evento = new Evento();
        listaPermissoes = new ArrayList();
        permissaoDepartamento = new PermissaoDepartamento();
        msgConfirma = "";
        indicaTab = "permissao";
        descricaoPesquisa = "";
        tabDisabled = "true";
        listaPermissoesDisponiveis = new ArrayList();
        listaPermissoesAdicionadas = new ArrayList();
        listaRotinas = new ArrayList();
        listaModulos = new ArrayList();
        listaEventos = new ArrayList();
        listaDepartamentos = new ArrayList();
        listaNiveis = new ArrayList();
        idModulo = 0;
        idRotina = 0;
        idEvento = 0;
        idDepartamento = 0;
        idNivel = 0;
        idIndex = -1;
    }

    @PreDestroy
    public void destroy() {
        clear();
    }

    public void clear() {
        Sessions.remove("permissaoBean");
    }

    // MÓDULO / ROTINA
    public void addPermissao() {
        if (listaRotinas.isEmpty()) {
            Messages.warn("Sistema", "Não há rotinas disponíveis para serem adicionadas a esse módulo");
            return;
        }
        PermissaoDao db = new PermissaoDao();
        DaoInterface di = new Dao();
        modulo = (Modulo) di.find(new Modulo(), Integer.valueOf(listaModulos.get(idModulo).getDescription()));
        rotina = (Rotina) di.find(new Rotina(), Integer.valueOf(listaRotinas.get(idRotina).getDescription()));
        boolean sucesso = false;
        if (db.pesquisaPermissaoModRot(modulo.getId(), rotina.getId()).isEmpty()) {
            di.openTransaction();
            for (int i = 0; i < getListaEventos().size(); i++) {
                evento = (Evento) di.find(new Evento(), Integer.valueOf(getListaEventos().get(i).getDescription()));
                permissao.setModulo(modulo);
                permissao.setRotina(rotina);
                permissao.setEvento(evento);
                if (!di.save(permissao)) {
                    sucesso = false;
                    break;
                }
                permissao = new Permissao();
                sucesso = true;
            }
            if (sucesso) {
                Logger logger = new Logger();
                logger.save("Permissão [" + modulo.getDescricao() + " - " + rotina.getRotina() + "]");
                di.commit();
                Messages.info("Sucesso", "Registro adicionado com sucesso");
                listaRotinas.clear();
            } else {
                di.rollback();
                Messages.warn("Erro", "Erro adicionar permissão(s)!");
            }
        } else {
            Messages.warn("Sistema", "Permissão já existente!");
        }
        permissao = new Permissao();
    }

    public void removePermissao(Permissao p) {
        PermissaoDao db = new PermissaoDao();
        List<Permissao> listaPermissao = (List<Permissao>) db.pesquisaPermissaoModRot(p.getModulo().getId(), p.getRotina().getId());
        DaoInterface di = new Dao();
        di.openTransaction();
        boolean sucesso = false;
        for (int i = 0; i < listaPermissao.size(); i++) {
            permissao = (Permissao) di.find(new Permissao(), listaPermissao.get(i).getId());
            if (!di.delete(permissao)) {
                sucesso = false;
                break;
            }
            sucesso = true;
            permissao = new Permissao();
        }
        if (sucesso) {
            Logger novoLog = new Logger();
            novoLog.save("Permissão [" + p.getModulo().getDescricao() + " - " + p.getRotina().getRotina() + "]");
            di.commit();
            Messages.info("Sucesso", "Permissão(s) removida(s) com sucesso");
            listaRotinas.clear();
        } else {
            di.rollback();
            Messages.warn("Erro", "Erro ao remover permissão(s)!");
        }
    }

    // PERMISSÃO DEPARTAMENTO   
    public String adicionarPermissaoDpto() {
        if (!listaPermissoesDisponiveis.isEmpty()) {
            boolean erro = false;
            boolean temRegistros = false;
            DaoInterface sv = new Dao();
            sv.openTransaction();
            for (int i = 0; i < listaPermissoesDisponiveis.size(); i++) {
                if ((Boolean) ((DataObject) listaPermissoesDisponiveis.get(i)).getArgumento0() == true) {
                    Permissao perm = (Permissao) ((DataObject) listaPermissoesDisponiveis.get(i)).getArgumento1();
                    Departamento depto = (Departamento) sv.find(new Departamento(), Integer.parseInt(getListaDepartamentos().get(idDepartamento).getDescription()));
                    Nivel niv = (Nivel) sv.find(new Nivel(), Integer.parseInt(getListaNiveis().get(idNivel).getDescription()));
                    permissaoDepartamento.setPermissao(perm);
                    permissaoDepartamento.setDepartamento(depto);
                    permissaoDepartamento.setNivel(niv);

                    if (!sv.save(permissaoDepartamento)) {
                        temRegistros = false;
                        erro = true;
                        break;
                    }
                    temRegistros = true;
                    permissaoDepartamento = new PermissaoDepartamento();
                }
            }
            if (erro) {
                sv.rollback();
                Messages.warn("Erro", "Erro ao adicionar permissão(s)!");
            } else {
                sv.commit();
                if (temRegistros) {
                    listaPermissoesAdicionadas.clear();
                    listaPermissoesDisponiveis.clear();
                    Messages.info("Sucesso", "Permissão(s) adicionada(s) com sucesso");
                } else {
                    Messages.info("Sistema", "Não foi selecionada nenhuma permissão!");
                }
            }
        }
        return null;
    }

    public String adicionarPermissaoDptoDBClick(Permissao p) {
        if (!listaPermissoesDisponiveis.isEmpty()) {
            boolean erro = false;
            DaoInterface di = new Dao();
            di.openTransaction();
            Permissao perm = p;
            Departamento depto = (Departamento) di.find(new Departamento(), Integer.parseInt(getListaDepartamentos().get(idDepartamento).getDescription()));
            Nivel niv = (Nivel) di.find(new Nivel(), Integer.parseInt(getListaNiveis().get(idNivel).getDescription()));
            permissaoDepartamento.setPermissao(perm);
            permissaoDepartamento.setDepartamento(depto);
            permissaoDepartamento.setNivel(niv);
            if (!di.save(permissaoDepartamento)) {
                erro = true;
            }
            permissaoDepartamento = new PermissaoDepartamento();
            if (erro) {
                di.rollback();
                Messages.warn("Erro", "Erro ao adicionar permissão(s)!");
            } else {
                di.commit();
                listaPermissoesAdicionadas.clear();
                listaPermissoesDisponiveis.clear();
                Messages.info("Sucesso", "Permissão(s) adicionada(s) com sucesso");
            }
        }
        return null;
    }

    public String adicionarTodasPermissaoDpto() {
        if (!listaPermissoesDisponiveis.isEmpty()) {
            boolean erro = false;
            DaoInterface di = new Dao();
            di.openTransaction();
            for (int i = 0; i < listaPermissoesDisponiveis.size(); i++) {
                Permissao perm = (Permissao) ((DataObject) listaPermissoesDisponiveis.get(i)).getArgumento1();
                Departamento depto = (Departamento) di.find(new Departamento(), Integer.parseInt(listaDepartamentos.get(idDepartamento).getDescription()));
                Nivel niv = (Nivel) di.find(new Nivel(), Integer.parseInt(listaNiveis.get(idNivel).getDescription()));
                permissaoDepartamento.setPermissao(perm);
                permissaoDepartamento.setDepartamento(depto);
                permissaoDepartamento.setNivel(niv);
                if (!di.save(permissaoDepartamento)) {
                    erro = true;
                    break;
                }
                permissaoDepartamento = new PermissaoDepartamento();
            }
            if (erro) {
                di.rollback();
                Messages.warn("Erro", "Erro ao adicionar permissão(s)!");
            } else {
                di.commit();
                listaPermissoesAdicionadas.clear();
                listaPermissoesDisponiveis.clear();
                Messages.info("Sucesso", "Permissão(s) adicionada(s) com sucesso");
            }
        }
        return null;
    }

    public String excluirPermissaoDepto() {
        if (!listaPermissoesAdicionadas.isEmpty()) {
            boolean erro = false;
            boolean temRegistros = false;
            DaoInterface di = new Dao();
            di.openTransaction();
            for (int i = 0; i < listaPermissoesAdicionadas.size(); i++) {
                if ((Boolean) ((DataObject) listaPermissoesAdicionadas.get(i)).getArgumento0() == true) {
                    permissaoDepartamento = (PermissaoDepartamento) ((DataObject) listaPermissoesAdicionadas.get(i)).getArgumento2();
                    if (!di.delete(permissaoDepartamento)) {
                        erro = true;
                        temRegistros = false;
                        break;
                    }
                    temRegistros = true;
                }
                permissaoDepartamento = new PermissaoDepartamento();
            }
            if (erro) {
                di.rollback();
                Messages.warn("Erro", "Erro ao remover permissão(s)!");
            } else {
                di.commit();
                if (temRegistros) {
                    listaPermissoesAdicionadas.clear();
                    listaPermissoesDisponiveis.clear();
                    Messages.info("Sucesso", "Permissão(s) removida(s) com sucesso");
                } else {
                    Messages.info("Sistema", "Não foi selecionada nenhuma permissão!");
                }
            }
        }
        return null;
    }

    public String excluirPermissaoDeptoDBClick(PermissaoDepartamento pd) {
        if (!listaPermissoesAdicionadas.isEmpty()) {
            boolean erro = false;
            DaoInterface di = new Dao();
            di.openTransaction();
            permissaoDepartamento = pd;
            if (!di.delete(permissaoDepartamento)) {
                erro = true;
            }
            permissaoDepartamento = new PermissaoDepartamento();
            if (erro) {
                di.rollback();
                Messages.warn("Erro", "Erro ao remover permissão(s)!");
            } else {
                di.commit();
                listaPermissoesAdicionadas.clear();
                listaPermissoesDisponiveis.clear();
                Messages.info("Sucesso", "Permissão(s) removida(s) com sucesso");
            }
        }
        return null;
    }

    public String excluirTodasPermissaoDepto() {
        if (!listaPermissoesAdicionadas.isEmpty()) {
            boolean erro = false;
            DaoInterface di = new Dao();
            di.openTransaction();
            for (int i = 0; i < listaPermissoesAdicionadas.size(); i++) {
                permissaoDepartamento = (PermissaoDepartamento) ((DataObject) listaPermissoesAdicionadas.get(i)).getArgumento2();
                if (!di.delete(permissaoDepartamento)) {
                    erro = true;
                    break;
                }
            }
            if (erro) {
                di.rollback();
                Messages.warn("Erro", "Erro ao remover permissão(s)!");
            } else {
                di.commit();
                listaPermissoesAdicionadas.clear();
                listaPermissoesDisponiveis.clear();
                Messages.info("Sucesso", "Permissão(s) removidas com sucesso");
            }
        }
        permissaoDepartamento = new PermissaoDepartamento();
        return null;
    }

    public void pesquisaPermissoesDepartamento() {
        listaPermissoesDisponiveis.clear();
        listaPermissoesAdicionadas.clear();
    }

    public void limparPesquisaPermissoesDepartamento() {
        descricaoPesquisa = "";
        listaPermissoesDisponiveis.clear();
        listaPermissoesAdicionadas.clear();
    }

    public List getPermissaoDisponivel() {
        if (listaPermissoesDisponiveis.isEmpty()) {
            listaPermissoesDisponiveis.clear();
            PermissaoDepartamentoDao permissaoDepartamentoDB = new PermissaoDepartamentoDao();
            int idDepto = Integer.parseInt(listaDepartamentos.get(idDepartamento).getDescription());
            int idNiv = Integer.parseInt(listaNiveis.get(idNivel).getDescription());
            List<Permissao> list = permissaoDepartamentoDB.listaPermissaoDepartamentoDisponivel(idDepto, idNiv, descricaoPesquisa);
            DataObject dtObject;
            for (int i = 0; i < list.size(); i++) {
                dtObject = new DataObject(
                        false,
                        (Permissao) list.get(i));
                listaPermissoesDisponiveis.add(dtObject);
            }
        }
        return listaPermissoesDisponiveis;
    }

    public List getPermissaoAdicionada() {
        if (listaPermissoesAdicionadas.isEmpty()) {
            PermissaoDepartamentoDao permissaoDepartamentoDB = new PermissaoDepartamentoDao();
            int idDepto = Integer.parseInt(listaDepartamentos.get(idDepartamento).getDescription());
            int idNiv = Integer.parseInt(listaNiveis.get(idNivel).getDescription());
            List<PermissaoDepartamento> list = permissaoDepartamentoDB.listaPermissaoDepartamentoAdicionada(idDepto, idNiv, descricaoPesquisa);
            DataObject dtObject;
            for (int i = 0; i < list.size(); i++) {
                dtObject = new DataObject(false,
                        ((PermissaoDepartamento) list.get(i)).getPermissao(),
                        ((PermissaoDepartamento) list.get(i)),
                        null,
                        null,
                        null);
                listaPermissoesAdicionadas.add(dtObject);
            }
        }
        return listaPermissoesAdicionadas;
    }

    public List getListaPermissaoDpto() {
        PermissaoDepartamentoDao db = new PermissaoDepartamentoDao();
        List result = db.pesquisaTodos();
        return result;
    }

    public Permissao getPermissao() {
        return permissao;
    }

    public void setPermissao(Permissao permissao) {
        this.permissao = permissao;
    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }

    public int getIdModulo() {
        return idModulo;
    }

    public void setIdModulo(int idModulo) {
        this.idModulo = idModulo;
    }

    public int getIdRotina() {
        return idRotina;
    }

    public void setIdRotina(int idRotina) {
        this.idRotina = idRotina;
    }

    public int getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(int idEvento) {
        this.idEvento = idEvento;
    }

    public String getIndicaTab() {
        return indicaTab;
    }

    public void setIndicaTab(String indicaTab) {
        this.indicaTab = indicaTab;
    }

    public String getTabDisabled() {
        return tabDisabled;
    }

    public void setTabDisabled(String tabDisabled) {
        this.tabDisabled = tabDisabled;
    }

    public int getIdDepartamento() {
        return idDepartamento;
    }

    public void setIdDepartamento(int idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    public int getIdNivel() {
        return idNivel;
    }

    public void setIdNivel(int idNivel) {
        this.idNivel = idNivel;
    }

    public PermissaoDepartamento getPermissaoDepartamento() {
        return permissaoDepartamento;
    }

    public void setPermissaoDepartamento(PermissaoDepartamento permissaoDepartamento) {
        this.permissaoDepartamento = permissaoDepartamento;
    }

    public int getIdIndex() {
        return idIndex;
    }

    public void setIdIndex(int idIndex) {
        this.idIndex = idIndex;
    }

    public List<SelectItem> getListaModulos() {
        if (listaModulos.isEmpty()) {
            DaoInterface di = new Dao();
            List modulos = di.list(new Modulo(), true);
            for (int i = 0; i < modulos.size(); i++) {
                listaModulos.add(new SelectItem(i,
                        ((Modulo) modulos.get(i)).getDescricao(),
                        Integer.toString(((Modulo) modulos.get(i)).getId())));
            }
        }
        return listaModulos;
    }

    public void setListaModulos(List<SelectItem> listaModulos) {
        this.listaModulos = listaModulos;
    }

    public List<SelectItem> getListaRotinas() {
        listaRotinas.clear();
        if (listaRotinas.isEmpty()) {
            RotinaDao rotinaDB = new RotinaDao();
            List list = rotinaDB.pesquisaRotinasDisponiveisModulo(Integer.parseInt(listaModulos.get(idModulo).getDescription()));
            for (int i = 0; i < list.size(); i++) {
                listaRotinas.add(new SelectItem(i,
                        ((Rotina) list.get(i)).getRotina(),
                        Integer.toString(((Rotina) list.get(i)).getId())));
            }
        }
        return listaRotinas;
    }

    public void setListaRotinas(List<SelectItem> listaRotinas) {
        this.listaRotinas = listaRotinas;
    }

    public List<SelectItem> getListaEventos() {
        if (listaEventos.isEmpty()) {
            DaoInterface di = new Dao();
            List eventos = di.list(new Evento(), true);
            for (int i = 0; i < eventos.size(); i++) {
                listaEventos.add(new SelectItem(i, ((Evento) eventos.get(i)).getDescricao(), Integer.toString(((Evento) eventos.get(i)).getId())));
            }
        }
        return listaEventos;
    }

    public void setListaEventos(List<SelectItem> listaEventos) {
        this.listaEventos = listaEventos;
    }

    public List<SelectItem> getListaNiveis() {
        if (listaNiveis.isEmpty()) {
            DaoInterface di = new Dao();
            List niveis = di.list(new Nivel(), true);
            for (int i = 0; i < niveis.size(); i++) {
                listaNiveis.add(new SelectItem(i,
                        ((Nivel) niveis.get(i)).getDescricao(),
                        Integer.toString(((Nivel) niveis.get(i)).getId())));
            }

        }
        return listaNiveis;
    }

    public void setListaNiveis(List<SelectItem> listaNiveis) {
        this.listaNiveis = listaNiveis;
    }

    public List<SelectItem> getListaDepartamentos() {
        if (listaDepartamentos.isEmpty()) {
            DaoInterface di = new Dao();
            List departamentos = di.list(new Departamento(), true);
            for (int i = 0; i < departamentos.size(); i++) {
                listaDepartamentos.add(new SelectItem(i,
                        ((Departamento) departamentos.get(i)).getDescricao(),
                        Integer.toString(((Departamento) departamentos.get(i)).getId())));
            }
        }
        return listaDepartamentos;
    }

    public void setListaDepartamentos(List<SelectItem> listaDepartamentos) {
        this.listaDepartamentos = listaDepartamentos;
    }

    public String getDescricaoPesquisa() {
        return descricaoPesquisa;
    }

    public void setDescricaoPesquisa(String descricaoPesquisa) {
        this.descricaoPesquisa = descricaoPesquisa;
    }

    public List<Permissao> getListaPermissoes() {
        listaPermissoes.clear();
        PermissaoDao db = new PermissaoDao();
        listaPermissoes = db.pesquisaTodosAgrupadosPorModulo(Integer.parseInt(listaModulos.get(idModulo).getDescription()));
        return listaPermissoes;
    }

    public void setListaPermissoes(List<Permissao> listaPermissoes) {
        this.listaPermissoes = listaPermissoes;
    }
}
