package br.com.clinicaintegrada.seguranca.beans;

import br.com.clinicaintegrada.financeiro.Caixa;
import br.com.clinicaintegrada.logSistema.Logger;
import br.com.clinicaintegrada.pessoa.Filial;
import br.com.clinicaintegrada.pessoa.dao.FilialDao;
import br.com.clinicaintegrada.seguranca.Cliente;
import br.com.clinicaintegrada.seguranca.Departamento;
import br.com.clinicaintegrada.seguranca.MacFilial;
import br.com.clinicaintegrada.seguranca.Registro;
import br.com.clinicaintegrada.seguranca.controleUsuario.ChamadaPaginaBean;
import br.com.clinicaintegrada.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.clinicaintegrada.seguranca.controleUsuario.SessaoCliente;
import br.com.clinicaintegrada.seguranca.dao.MacFilialDao;
import br.com.clinicaintegrada.seguranca.dao.RegistroDao;
import br.com.clinicaintegrada.utils.Dao;
import br.com.clinicaintegrada.utils.DaoInterface;
import br.com.clinicaintegrada.utils.Messages;
import br.com.clinicaintegrada.utils.Sessions;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

@ManagedBean
@SessionScoped
public class MacFilialBean implements Serializable {

    private MacFilial macFilial;
    private Integer idFilial;
    private Integer idDepartamento;
    private Integer idCaixa = 0;
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
            Messages.warn("Validação", "Cadastrar filiais!");
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
        Dao dao = new Dao();
        Logger logger = new Logger();
        dao.openTransaction();
        if (dao.delete(macFilial)) {
            logger.delete(
                    "ID: " + macFilial.getId()
                    + " - Filial: (" + macFilial.getFilial().getId() + ") " + macFilial.getFilial().getFilial().getPessoa().getNome()
                    + " - Departamento: (" + macFilial.getDepartamento().getId() + ") " + macFilial.getDepartamento().getDescricao()
                    + " - Mesa: " + macFilial.getMesa()
                    + " - Mac: " + macFilial.getMac()
            );
            dao.commit();
            Messages.info("Sucesso", "Este Registro excluído com sucesso!");
            listaMacs.clear();
        } else {
            dao.rollback();
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

    public Integer getIdFilial() {
        return idFilial;
    }

    public void setIdFilial(Integer idFilial) {
        this.idFilial = idFilial;
    }

    public Integer getIdDepartamento() {
        return idDepartamento;
    }

    public void setIdDepartamento(Integer idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    public List<MacFilial> getListaMacs() {
        if (listaMacs.isEmpty()) {
            MacFilialDao macFilialDao = new MacFilialDao();
            RegistroDao registroDao = new RegistroDao();
            Registro r = registroDao.pesquisaRegistroPorCliente(SessaoCliente.get().getId());
            listaMacs = macFilialDao.pesquisaTodosPorMatriz(r.getFilial().getId());
        }
        return listaMacs;
    }

    public void setListaMacs(List<MacFilial> listaMacs) {
        this.listaMacs = listaMacs;
    }

    public Integer getIdCaixa() {
        return idCaixa;
    }

    public void setIdCaixa(Integer idCaixa) {
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

    public String putFilial(MacFilial mf) throws IOException {
        putFilial(mf, false);
        ChamadaPaginaBean chamadaPaginaBean = (ChamadaPaginaBean) Sessions.getObject("chamadaPaginaBean");
        Sessions.put("linkClicado", true);        
        if(chamadaPaginaBean == null) {
            return "menuPrincipal";
        }
        String link = chamadaPaginaBean.getPenultimoLink();
        return link;
    }

    public void putFilial(MacFilial mf, boolean sair) throws IOException {
        Sessions.remove("acessoFilial");
        Sessions.remove("chamadaPaginaBean");
        ((ControleUsuarioBean) Sessions.getObject("controleUsuarioBean")).setMacFilial(mf);
        ((ControleUsuarioBean) Sessions.getObject("controleUsuarioBean")).setFilial("Filial: ( " + mf.getFilial().getFilial().getPessoa().getNome() + " / " + mf.getDepartamento().getDescricao() + " )");
        Sessions.put("acessoFilial", mf);
        Sessions.put("linkClicado", true);
        if (sair) {
            String retorno = "";
            if (Sessions.exists("sessaoCliente")) {
                retorno = ((Cliente) Sessions.getObject("sessaoCliente")).getIdentifica() + "/";
                if (Sessions.exists("acessoFilial")) {
                    MacFilial mfs = (MacFilial) Sessions.getObject("acessoFilial");
                    retorno += "?filial=" + mfs.getMac();
                }

            }
            FacesContext conext = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) conext.getExternalContext().getSession(false);
            session.invalidate();
            FacesContext.getCurrentInstance().getExternalContext().redirect(retorno);
        }

    }
}
