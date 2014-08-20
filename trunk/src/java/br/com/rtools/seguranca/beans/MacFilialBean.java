package br.com.rtools.seguranca.beans;

import br.com.rtools.financeiro.Caixa;
import br.com.rtools.logSistema.Logger;
import br.com.rtools.pessoa.Filial;
import br.com.rtools.pessoa.dao.FilialDao;
import br.com.rtools.seguranca.Departamento;
import br.com.rtools.seguranca.MacFilial;
import br.com.rtools.seguranca.Registro;
import br.com.rtools.seguranca.dao.MacFilialDao;
import br.com.rtools.seguranca.dao.PermissaoUsuarioDao;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DaoInterface;
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
public class MacFilialBean implements Serializable {

    private MacFilial macFilial;
    private int idFilial;
    private int idDepartamento;
    private int idCaixa = 0;
    private List<MacFilial> listaMacs;
    public List<SelectItem> listaFiliais;
    public List<SelectItem> listaDepartamentos;
    private List<SelectItem> listaCaixa;

    @PostConstruct
    public void init() {
        macFilial = new MacFilial();
        idFilial = 0;
        idDepartamento = 0;
        idCaixa = 0;
        listaMacs = new ArrayList<>();
        listaFiliais = new ArrayList<>();
        listaDepartamentos = new ArrayList<>();
        listaCaixa = new ArrayList<>();
    }

    @PreDestroy
    public void destroy() {
        Sessions.remove("macFilialBean");
    }

    public void clear() {
        Sessions.remove("macFilialBean");
    }

    public void add() {
        MacFilialDao macFiliaDao = new MacFilialDao();
        DaoInterface di = new Dao();
        Logger logger = new Logger();
        if (getListaFiliais().isEmpty()) {
            Messages.warn("Validação", "Cadastrar filiais <p:menuitem value=\"Filial\" action=\"#{chamadaPaginaBean.pagina('filial')}\"/>!");
            return;
        }
        Filial filial = (Filial) di.find(new Filial(), Integer.parseInt(getListaFiliais().get(idFilial).getDescription()));
        Departamento departamento = (Departamento) di.find(new Departamento(), Integer.parseInt(getListaDepartamentos().get(idDepartamento).getDescription()));
        if (macFilial.getMac().isEmpty()) {
            Messages.warn("Validação", "Digite um mac válido!");
            return;
        }
        if (!macFilial.getMac().matches("^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$")) {
            Messages.warn("Validação", "Digite um mac válido!");
            return;
        }
        if (macFiliaDao.pesquisaMac(macFilial.getMac()) != null) {
            Messages.warn("Validação", "Este computador ja está registrado!");
            return;
        }
//        Registro registro = (Registro) di.find(new Registro(), 1);
//        if (registro.isSenhaHomologacao() && macFilial.getMesa() <= 0) {
//            Messages.warn("Validação", "O campo mesa é obrigatório devido Senha Homologação em Registro ser verdadeiro");
//            return;
//        }
        macFilial.setDepartamento(departamento);
        macFilial.setFilial(filial);

        if (listaCaixa.get(idCaixa).getDescription().equals("-1")) {
            macFilial.setCaixa(null);
        } else {
            for (int i = 0; i < listaMacs.size(); i++) {
                if (listaMacs.get(i).getCaixa() != null && listaMacs.get(i).getCaixa().getId() == Integer.valueOf(listaCaixa.get(idCaixa).getDescription())) {
                    Messages.warn("Validação", "Já existe uma filial cadastrada para este Caixa");
                    return;
                }
            }
            macFilial.setCaixa((Caixa) di.find(new Caixa(), Integer.valueOf(listaCaixa.get(idCaixa).getDescription())));
        }
        di.openTransaction();
        if (di.save(macFilial)) {
            logger.save(
                    "ID: " + macFilial.getId()
                    + " - Filial: (" + macFilial.getFilial().getId() + ") " + macFilial.getFilial().getFilial().getPessoa().getNome()
                    + " - Departamento: (" + macFilial.getDepartamento().getId() + ") " + macFilial.getDepartamento().getDescricao()
                    + " - Mesa: " + macFilial.getMesa()
                    + " - Mac: " + macFilial.getMac()
            );
            di.commit();
            Messages.info("Salvo", "Este Computador registrado com sucesso!");
            macFilial = new MacFilial();
            listaMacs.clear();
        } else {
            di.rollback();
            Messages.warn("Erro", "Erro ao inserir esse registro!");
        }
    }

    public void delete(MacFilial mf) {
        macFilial = mf;
        DaoInterface di = new Dao();
        Logger logger = new Logger();
        di.openTransaction();
        if (di.delete(macFilial)) {
            logger.delete(
                    "ID: " + macFilial.getId()
                    + " - Filial: (" + macFilial.getFilial().getId() + ") " + macFilial.getFilial().getFilial().getPessoa().getNome()
                    + " - Departamento: (" + macFilial.getDepartamento().getId() + ") " + macFilial.getDepartamento().getDescricao()
                    + " - Mesa: " + macFilial.getMesa()
                    + " - Mac: " + macFilial.getMac()
            );
            di.commit();
            Messages.info("Sucesso", "Este Registro excluído com sucesso!");
            listaMacs.clear();
        } else {
            di.rollback();
            Messages.info("Sucesso", "Erro ao excluir computador!");

        }
        macFilial = new MacFilial();
    }

    public List<SelectItem> getListaFiliais() {
        if (listaFiliais.isEmpty()) {
            FilialDao filialDao = new FilialDao();
            List<Filial> list = (List<Filial>) filialDao.pesquisaTodasCliente();
            for (int i = 0; i < list.size(); i++) {
                listaFiliais.add(new SelectItem(i,
                        list.get(i).getFilial().getPessoa().getDocumento() + " / " + list.get(i).getFilial().getPessoa().getNome(),
                        Integer.toString(list.get(i).getId())));
            }
        }
        return listaFiliais;
    }

    public List<SelectItem> getListaDepartamentos() {
        List<SelectItem> result = new ArrayList<>();
        Dao dao = new Dao();
        List<Departamento> select = (List<Departamento>) dao.list(new Departamento(), true);
        for (int i = 0; i < select.size(); i++) {
            result.add(new SelectItem(i, select.get(i).getDescricao(), Integer.toString(select.get(i).getId())));
        }
        return result;
    }

    public void refreshForm() {

    }

    public MacFilial getMacFilial() {
        return macFilial;
    }

    public void setMacFilial(MacFilial macFilial) {
        this.macFilial = macFilial;
    }

    public int getIdFilial() {
        return idFilial;
    }

    public void setIdFilial(int idFilial) {
        this.idFilial = idFilial;
    }

    public int getIdDepartamento() {
        return idDepartamento;
    }

    public void setIdDepartamento(int idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    public List<MacFilial> getListaMacs() {
        if (listaMacs.isEmpty()) {
            Dao dao = new Dao();
            listaMacs = dao.listByCliente(new MacFilial(), true);
        }
        return listaMacs;
    }

    public void setListaMacs(List<MacFilial> listaMacs) {
        this.listaMacs = listaMacs;
    }

    public int getIdCaixa() {
        return idCaixa;
    }

    public void setIdCaixa(int idCaixa) {
        this.idCaixa = idCaixa;
    }

    public List<SelectItem> getListaCaixa() {
        if (listaCaixa.isEmpty()) {
            DaoInterface di = new Dao();
            List<Caixa> result = di.list(new Caixa());

            listaCaixa.add(new SelectItem(0, "NENHUM CAIXA", "-1"));
            for (int i = 0; i < result.size(); i++) {
                listaCaixa.add(new SelectItem(i + 1,
                        ((String.valueOf(result.get(i).getCaixa()).length() == 1) ? ("0" + String.valueOf(result.get(i).getCaixa())) : result.get(i).getCaixa()) + " - " + result.get(i).getDescricao(),
                        Integer.toString(result.get(i).getId())));
            }
        }
        return listaCaixa;
    }

    public void setListaCaixa(List<SelectItem> listaCaixa) {
        this.listaCaixa = listaCaixa;
    }
}
