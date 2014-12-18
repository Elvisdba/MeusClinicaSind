package br.com.clinicaintegrada.seguranca.beans;

import br.com.clinicaintegrada.seguranca.Nivel;
import br.com.clinicaintegrada.seguranca.Modulo;
import br.com.clinicaintegrada.seguranca.Permissao;
import br.com.clinicaintegrada.seguranca.SegEvento;
import br.com.clinicaintegrada.seguranca.Cliente;
import br.com.clinicaintegrada.seguranca.PermissaoDepartamento;
import br.com.clinicaintegrada.seguranca.Rotina;
import br.com.clinicaintegrada.seguranca.Departamento;
import br.com.clinicaintegrada.logSistema.Logger;
import br.com.clinicaintegrada.seguranca.controleUsuario.SessaoCliente;
import br.com.clinicaintegrada.seguranca.dao.PermissaoDao;
import br.com.clinicaintegrada.seguranca.dao.PermissaoDepartamentoDao;
import br.com.clinicaintegrada.seguranca.dao.RotinaDao;
import br.com.clinicaintegrada.utils.Dao;
import br.com.clinicaintegrada.utils.DaoInterface;
import br.com.clinicaintegrada.utils.DataObject;
import br.com.clinicaintegrada.utils.Messages;
import br.com.clinicaintegrada.utils.Sessions;
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
    private SegEvento segEvento;
    private List<Permissao> listPermissoes;
    private PermissaoDepartamento permissaoDepartamento;
    private String msgConfirma;
    private String indicaTab;
    private String descricaoPesquisa;
    private String tabDisabled;
    private List listPermissoesDisponiveis;
    private List listPermissoesAdicionadas;
    private List<SelectItem> listRotinas;
    private List<SelectItem> listModulos;
    private List<SelectItem> listEventos;
    private List<SelectItem> listDepartamentos;
    private List<SelectItem> listNiveis;
    private Integer idModulo;
    private Integer idRotina;
    private Integer idEvento;
    private Integer idDepartamento;
    private Integer idNivel;
    private Integer idIndex;

    @PostConstruct
    public void init() {
        permissao = new Permissao();
        modulo = new Modulo();
        rotina = new Rotina();
        segEvento = new SegEvento();
        listPermissoes = new ArrayList();
        permissaoDepartamento = new PermissaoDepartamento();
        msgConfirma = "";
        indicaTab = "permissao";
        descricaoPesquisa = "";
        tabDisabled = "true";
        listPermissoesDisponiveis = new ArrayList();
        listPermissoesAdicionadas = new ArrayList();
        listRotinas = new ArrayList();
        listModulos = new ArrayList();
        listEventos = new ArrayList();
        listDepartamentos = new ArrayList();
        listNiveis = new ArrayList();
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
        if (listRotinas.isEmpty()) {
            Messages.warn("Sistema", "Não há rotinas disponíveis para serem adicionadas a esse módulo");
            return;
        }
        PermissaoDao permissaoDao = new PermissaoDao();
        DaoInterface di = new Dao();
        modulo = (Modulo) di.find(new Modulo(), Integer.valueOf(listModulos.get(idModulo).getDescription()));
        rotina = (Rotina) di.find(new Rotina(), Integer.valueOf(listRotinas.get(idRotina).getDescription()));
        Cliente c = SessaoCliente.get();
        boolean sucesso = false;
        if (permissaoDao.pesquisaPermissaoModuloRotina(modulo.getId(), rotina.getId(), c.getId()).isEmpty()) {
            di.openTransaction();
            for (int i = 0; i < getListEventos().size(); i++) {
                segEvento = (SegEvento) di.find(new SegEvento(), Integer.valueOf(getListEventos().get(i).getDescription()));
                permissao.setModulo(modulo);
                permissao.setRotina(rotina);
                permissao.setSegEvento(segEvento);
                permissao.setCliente(c);
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
                listRotinas.clear();
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
        PermissaoDao permissaoDao = new PermissaoDao();
        List<Permissao> listaPermissao = (List<Permissao>) permissaoDao.pesquisaPermissaoModuloRotina(p.getModulo().getId(), p.getRotina().getId(), SessaoCliente.get().getId());
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
            Logger Logger = new Logger();
            Logger.save("Permissão [" + p.getModulo().getDescricao() + " - " + p.getRotina().getRotina() + "]");
            di.commit();
            Messages.info("Sucesso", "Permissão(s) removida(s) com sucesso");
            listRotinas.clear();
        } else {
            di.rollback();
            Messages.warn("Erro", "Erro ao remover permissão(s)!");
        }
    }

    // PERMISSÃO DEPARTAMENTO   
    public String adicionarPermissaoDpto() {
        if (!listPermissoesDisponiveis.isEmpty()) {
            boolean erro = false;
            boolean temRegistros = false;
            DaoInterface sv = new Dao();
            sv.openTransaction();
            Cliente c = SessaoCliente.get();
            for (int i = 0; i < listPermissoesDisponiveis.size(); i++) {
                if ((Boolean) ((DataObject) listPermissoesDisponiveis.get(i)).getArgumento0() == true) {
                    Permissao perm = (Permissao) ((DataObject) listPermissoesDisponiveis.get(i)).getArgumento1();
                    Departamento depto = (Departamento) sv.find(new Departamento(), Integer.parseInt(getListDepartamentos().get(idDepartamento).getDescription()));
                    Nivel niv = (Nivel) sv.find(new Nivel(), Integer.parseInt(getListNiveis().get(idNivel).getDescription()));
                    permissaoDepartamento.setPermissao(perm);
                    permissaoDepartamento.setDepartamento(depto);
                    permissaoDepartamento.setNivel(niv);
                    permissaoDepartamento.setCliente(c);

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
                    listPermissoesAdicionadas.clear();
                    listPermissoesDisponiveis.clear();
                    Messages.info("Sucesso", "Permissão(s) adicionada(s) com sucesso");
                } else {
                    Messages.info("Sistema", "Não foi selecionada nenhuma permissão!");
                }
            }
        }
        return null;
    }

    public String adicionarPermissaoDptoDBClick(Permissao p) {
        if (!listPermissoesDisponiveis.isEmpty()) {
            boolean erro = false;
            DaoInterface di = new Dao();
            di.openTransaction();
            Permissao perm = p;
            Departamento depto = (Departamento) di.find(new Departamento(), Integer.parseInt(getListDepartamentos().get(idDepartamento).getDescription()));
            Nivel niv = (Nivel) di.find(new Nivel(), Integer.parseInt(getListNiveis().get(idNivel).getDescription()));
            permissaoDepartamento.setPermissao(perm);
            permissaoDepartamento.setDepartamento(depto);
            permissaoDepartamento.setNivel(niv);
            permissaoDepartamento.setCliente(SessaoCliente.get());
            if (!di.save(permissaoDepartamento)) {
                erro = true;
            }
            permissaoDepartamento = new PermissaoDepartamento();
            if (erro) {
                di.rollback();
                Messages.warn("Erro", "Erro ao adicionar permissão(s)!");
            } else {
                di.commit();
                listPermissoesAdicionadas.clear();
                listPermissoesDisponiveis.clear();
                Messages.info("Sucesso", "Permissão(s) adicionada(s) com sucesso");
            }
        }
        return null;
    }

    public String adicionarTodasPermissaoDpto() {
        if (!listPermissoesDisponiveis.isEmpty()) {
            boolean erro = false;
            DaoInterface di = new Dao();
            di.openTransaction();
            Cliente c = SessaoCliente.get();
            for (int i = 0; i < listPermissoesDisponiveis.size(); i++) {
                Permissao perm = (Permissao) ((DataObject) listPermissoesDisponiveis.get(i)).getArgumento1();
                Departamento depto = (Departamento) di.find(new Departamento(), Integer.parseInt(listDepartamentos.get(idDepartamento).getDescription()));
                Nivel niv = (Nivel) di.find(new Nivel(), Integer.parseInt(listNiveis.get(idNivel).getDescription()));
                permissaoDepartamento.setPermissao(perm);
                permissaoDepartamento.setDepartamento(depto);
                permissaoDepartamento.setNivel(niv);
                permissaoDepartamento.setCliente(c);
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
                listPermissoesAdicionadas.clear();
                listPermissoesDisponiveis.clear();
                Messages.info("Sucesso", "Permissão(s) adicionada(s) com sucesso");
            }
        }
        return null;
    }

    public String excluirPermissaoDepto() {
        if (!listPermissoesAdicionadas.isEmpty()) {
            boolean erro = false;
            boolean temRegistros = false;
            DaoInterface di = new Dao();
            di.openTransaction();
            for (int i = 0; i < listPermissoesAdicionadas.size(); i++) {
                if ((Boolean) ((DataObject) listPermissoesAdicionadas.get(i)).getArgumento0() == true) {
                    permissaoDepartamento = (PermissaoDepartamento) ((DataObject) listPermissoesAdicionadas.get(i)).getArgumento2();
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
                    listPermissoesAdicionadas.clear();
                    listPermissoesDisponiveis.clear();
                    Messages.info("Sucesso", "Permissão(s) removida(s) com sucesso");
                } else {
                    Messages.info("Sistema", "Não foi selecionada nenhuma permissão!");
                }
            }
        }
        return null;
    }

    public String excluirPermissaoDeptoDBClick(PermissaoDepartamento pd) {
        if (!listPermissoesAdicionadas.isEmpty()) {
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
                listPermissoesAdicionadas.clear();
                listPermissoesDisponiveis.clear();
                Messages.info("Sucesso", "Permissão(s) removida(s) com sucesso");
            }
        }
        return null;
    }

    public String excluirTodasPermissaoDepto() {
        if (!listPermissoesAdicionadas.isEmpty()) {
            boolean erro = false;
            DaoInterface di = new Dao();
            di.openTransaction();
            for (int i = 0; i < listPermissoesAdicionadas.size(); i++) {
                permissaoDepartamento = (PermissaoDepartamento) ((DataObject) listPermissoesAdicionadas.get(i)).getArgumento2();
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
                listPermissoesAdicionadas.clear();
                listPermissoesDisponiveis.clear();
                Messages.info("Sucesso", "Permissão(s) removidas com sucesso");
            }
        }
        permissaoDepartamento = new PermissaoDepartamento();
        return null;
    }

    public void pesquisaPermissoesDepartamento() {
        listPermissoesDisponiveis.clear();
        listPermissoesAdicionadas.clear();
    }

    public void limparPesquisaPermissoesDepartamento() {
        descricaoPesquisa = "";
        listPermissoesDisponiveis.clear();
        listPermissoesAdicionadas.clear();
    }

    public List getPermissaoDisponivel() {
        if (listPermissoesDisponiveis.isEmpty()) {
            listPermissoesDisponiveis.clear();
            PermissaoDao permissaoDao = new PermissaoDao();
            int idDepto = Integer.parseInt(listDepartamentos.get(idDepartamento).getDescription());
            int idNiv = Integer.parseInt(listNiveis.get(idNivel).getDescription());
            List<Permissao> list = permissaoDao.listaPermissaoDepartamentoDisponivel(idDepto, idNiv, descricaoPesquisa, SessaoCliente.get().getId());
            DataObject dtObject;
            for (int i = 0; i < list.size(); i++) {
                dtObject = new DataObject(
                        false,
                        (Permissao) list.get(i));
                listPermissoesDisponiveis.add(dtObject);
            }
        }
        return listPermissoesDisponiveis;
    }

    public List getPermissaoAdicionada() {
        if (listPermissoesAdicionadas.isEmpty()) {
            PermissaoDepartamentoDao permissaoDepartamentoDB = new PermissaoDepartamentoDao();
            int idDepto = Integer.parseInt(listDepartamentos.get(idDepartamento).getDescription());
            int idNiv = Integer.parseInt(listNiveis.get(idNivel).getDescription());
            List<PermissaoDepartamento> list = permissaoDepartamentoDB.listaPermissaoDepartamentoAdicionada(idDepto, idNiv, descricaoPesquisa, SessaoCliente.get().getId());
            DataObject dtObject;
            for (int i = 0; i < list.size(); i++) {
                dtObject = new DataObject(false,
                        ((PermissaoDepartamento) list.get(i)).getPermissao(),
                        ((PermissaoDepartamento) list.get(i)),
                        null,
                        null,
                        null);
                listPermissoesAdicionadas.add(dtObject);
            }
        }
        return listPermissoesAdicionadas;
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

    public Integer getIdModulo() {
        return idModulo;
    }

    public void setIdModulo(Integer idModulo) {
        this.idModulo = idModulo;
    }

    public Integer getIdRotina() {
        return idRotina;
    }

    public void setIdRotina(Integer idRotina) {
        this.idRotina = idRotina;
    }

    public Integer getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(Integer idEvento) {
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

    public Integer getIdDepartamento() {
        return idDepartamento;
    }

    public void setIdDepartamento(Integer idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    public Integer getIdNivel() {
        return idNivel;
    }

    public void setIdNivel(Integer idNivel) {
        this.idNivel = idNivel;
    }

    public PermissaoDepartamento getPermissaoDepartamento() {
        return permissaoDepartamento;
    }

    public void setPermissaoDepartamento(PermissaoDepartamento permissaoDepartamento) {
        this.permissaoDepartamento = permissaoDepartamento;
    }

    public Integer getIdIndex() {
        return idIndex;
    }

    public void setIdIndex(Integer idIndex) {
        this.idIndex = idIndex;
    }

    public List<SelectItem> getListModulos() {
        if (listModulos.isEmpty()) {
            Dao di = new Dao();
            List modulos = di.list(new Modulo(), true);
            for (int i = 0; i < modulos.size(); i++) {
                listModulos.add(new SelectItem(i,
                        ((Modulo) modulos.get(i)).getDescricao(),
                        Integer.toString(((Modulo) modulos.get(i)).getId())));
            }
        }
        return listModulos;
    }

    public void setListModulos(List<SelectItem> listModulos) {
        this.listModulos = listModulos;
    }

    public List<SelectItem> getListRotinas() {
        listRotinas.clear();
        if (listRotinas.isEmpty()) {
            RotinaDao rotinaDB = new RotinaDao();
            List list = rotinaDB.pesquisaRotinasDisponiveisModulo(Integer.parseInt(listModulos.get(idModulo).getDescription()));
            for (int i = 0; i < list.size(); i++) {
                listRotinas.add(new SelectItem(i,
                        ((Rotina) list.get(i)).getRotina(),
                        Integer.toString(((Rotina) list.get(i)).getId())));
            }
        }
        return listRotinas;
    }

    public void setListRotinas(List<SelectItem> listRotinas) {
        this.listRotinas = listRotinas;
    }

    public List<SelectItem> getListEventos() {
        if (listEventos.isEmpty()) {
            Dao di = new Dao();
            List segEventos = di.list(new SegEvento(), true);
            for (int i = 0; i < segEventos.size(); i++) {
                listEventos.add(new SelectItem(i, ((SegEvento) segEventos.get(i)).getDescricao(), Integer.toString(((SegEvento) segEventos.get(i)).getId())));
            }
        }
        return listEventos;
    }

    public void setListEventos(List<SelectItem> listEventos) {
        this.listEventos = listEventos;
    }

    public List<SelectItem> getListNiveis() {
        if (listNiveis.isEmpty()) {
            DaoInterface di = new Dao();
            List niveis = di.list(new Nivel(), true);
            for (int i = 0; i < niveis.size(); i++) {
                listNiveis.add(new SelectItem(i,
                        ((Nivel) niveis.get(i)).getDescricao(),
                        Integer.toString(((Nivel) niveis.get(i)).getId())));
            }

        }
        return listNiveis;
    }

    public void setListNiveis(List<SelectItem> listNiveis) {
        this.listNiveis = listNiveis;
    }

    public List<SelectItem> getListDepartamentos() {
        if (listDepartamentos.isEmpty()) {
            DaoInterface di = new Dao();
            List departamentos = di.list(new Departamento(), true);
            for (int i = 0; i < departamentos.size(); i++) {
                listDepartamentos.add(new SelectItem(i,
                        ((Departamento) departamentos.get(i)).getDescricao(),
                        Integer.toString(((Departamento) departamentos.get(i)).getId())));
            }
        }
        return listDepartamentos;
    }

    public void setListDepartamentos(List<SelectItem> listDepartamentos) {
        this.listDepartamentos = listDepartamentos;
    }

    public String getDescricaoPesquisa() {
        return descricaoPesquisa;
    }

    public void setDescricaoPesquisa(String descricaoPesquisa) {
        this.descricaoPesquisa = descricaoPesquisa;
    }

    public List<Permissao> getListPermissoes() {
        listPermissoes.clear();
        PermissaoDao db = new PermissaoDao();
        listPermissoes = db.pesquisaTodosAgrupadosPorModulo(Integer.parseInt(listModulos.get(idModulo).getDescription()), SessaoCliente.get().getId());
        return listPermissoes;
    }

    public void setListPermissoes(List<Permissao> listPermissoes) {
        this.listPermissoes = listPermissoes;
    }
}
