package br.com.clinicaintegrada.seguranca.beans;

import br.com.clinicaintegrada.pessoa.Juridica;
import br.com.clinicaintegrada.pessoa.Pessoa;
import br.com.clinicaintegrada.seguranca.Cliente;
import br.com.clinicaintegrada.seguranca.Registro;
import br.com.clinicaintegrada.seguranca.Rotina;
import br.com.clinicaintegrada.seguranca.SisEmailProtocolo;
import br.com.clinicaintegrada.seguranca.Usuario;
import br.com.clinicaintegrada.seguranca.controleUsuario.SessaoCliente;
import br.com.clinicaintegrada.seguranca.dao.RegistroDao;
import br.com.clinicaintegrada.sistema.Email;
import br.com.clinicaintegrada.sistema.EmailPessoa;
import br.com.clinicaintegrada.utils.AnaliseString;
import br.com.clinicaintegrada.utils.Dao;
import br.com.clinicaintegrada.utils.DaoInterface;
import br.com.clinicaintegrada.utils.DataHoje;
import br.com.clinicaintegrada.utils.Mail;
import br.com.clinicaintegrada.utils.Messages;
import br.com.clinicaintegrada.utils.Sessions;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.component.tabview.Tab;
import org.primefaces.event.TabChangeEvent;

@ManagedBean
@SessionScoped
@SuppressWarnings("unchecked")
public class RegistroBean implements Serializable {

    private Registro registro;
    private String senha;
    private String confirmaSenha;
    private String mensagem;
    private String emailTeste;
    private Integer codigoModulo;
    private Integer idSisEmailProtocolo;
    private List<SelectItem> listaDataVencimento;

    @PostConstruct
    public void init() {
        registro = new Registro();
        senha = "";
        confirmaSenha = "";
        mensagem = "";
        emailTeste = "";
        codigoModulo = 0;
        idSisEmailProtocolo = 0;
        listaDataVencimento = new ArrayList<>();
        if (registro != null) {
            if (registro.getId() == null) {
                RegistroDao registroDao = new RegistroDao();
                registro = (Registro) registroDao.pesquisaRegistroPorCliente(SessaoCliente.get().getId());
                if (registro == null) {
                    registro = new Registro();
                    Dao dao = new Dao();
                    Cliente cliente = (Cliente) SessaoCliente.get();
                    registro.setCliente(cliente);
                    registro.setFilial((Juridica) dao.find(new Juridica(), SessaoCliente.get().getIdJuridica()));
                    registro.setSisEmailProtocolo((SisEmailProtocolo) dao.find(new SisEmailProtocolo(), 1));
                    dao.save(registro, true);
                }
                senha = registro.getSisSenha();
                List<SelectItem> list = getListaSisEmailProtocolo();
                for (int i = 0; i < list.size(); i++) {
                    if (registro.getSisEmailProtocolo().getId() == Integer.parseInt(list.get(i).getDescription())) {
                        idSisEmailProtocolo = i;
                        break;
                    }
                }
            }
        }
    }

    @PreDestroy
    public void destroy() {
        Sessions.remove("registroBean");
    }

    public void save() {
        if (codigoModulo == 0) {
            if (!senha.isEmpty()) {
                if (!senha.equals(confirmaSenha)) {
                    Messages.warn("Validação", "Senhas não correspondem");
                    return;
                }
                registro.setSisSenha(senha);
            }
        }
        Dao dao = new Dao();
        dao.openTransaction();
        registro.setSisEmailProtocolo((SisEmailProtocolo) dao.find(new SisEmailProtocolo(), Integer.parseInt(getListaSisEmailProtocolo().get(idSisEmailProtocolo).getDescription())));
        if (registro.getFilial() == null || registro.getFilial().getId() == null) {
            registro.setFilial((Juridica) dao.find(new Juridica(), SessaoCliente.get().getIdJuridica()));
        }
        if (dao.update(registro)) {
            dao.commit();
            Messages.info("Sucesso", "Registro atualizado");
        } else {
            dao.rollback();
            Messages.warn("Erro", "Ao atualizar registro!");
        }
    }

    public void salvarSemSenha() {
        Dao dao = new Dao();
        dao.openTransaction();
        if (dao.update(registro)) {
            dao.commit();
            Messages.info("Sucesso", "Registro atualizado");
        } else {
            dao.rollback();
            Messages.warn("Erro", "Ao atualizar registro!");
        }
    }

    public boolean validacao() {
        return true;
    }

    public void criarLoginsUsuarios() {
        //PessoaDB db = new PessoaDBToplink();
        List<Pessoa> listaPessoas = new ArrayList(); // = db.pesquisaTodosSemLogin();
        // String login = "", senha = "";
        String senha = "";
        senha = senha + DataHoje.hora().replace(":", "");
        for (int i = 0; i < listaPessoas.size(); i++) {
            try {
                senha = Integer.toString(Integer.parseInt(senha) + Integer.parseInt(senha + "43"));
                senha = getGerarLoginSenhaPessoa(listaPessoas.get(i), senha);
            } catch (NumberFormatException e) {
                mensagem = "Erro ao Gerar!" + e;
            }
        }
        mensagem = "Geração concluída!";
    }

    public Registro getRegistro() {
        return registro;
    }

    public String getGerarLoginSenhaPessoa(Pessoa pessoa, String senhaInicial) {
        senhaInicial = senhaInicial.substring(senhaInicial.length() - 6, senhaInicial.length());
        String nome = AnaliseString.removerAcentos(pessoa.getNome().replace(" ", "X").toUpperCase());
        nome = nome.replace("-", "Y");
        nome = nome.replace(".", "W");
        nome = nome.replace("/", "Z");
        nome = nome.replace("A", "Q");
        nome = nome.replace("E", "R");
        nome = nome.replace("I", "H");
        nome = nome.replace("O", "P");
        nome = nome.replace("U", "M");
        nome = ("JHSRGDQ" + nome) + pessoa.getId();
        String login = nome.substring(nome.length() - 6, nome.length());
        pessoa.setLogin(login);
        pessoa.setSenha(senhaInicial);
        Dao dao = new Dao();
        dao.openTransaction();
        if (dao.update(pessoa)) {
            dao.commit();
        } else {
            dao.rollback();
        }
        return senhaInicial;
    }

    public void setRegistro(Registro registro) {
        this.registro = registro;
    }

    public String getConfirmaSenha() {
        return confirmaSenha;
    }

    public void setConfirmaSenha(String confirmaSenha) {
        this.confirmaSenha = confirmaSenha;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Integer getCodigoModulo() {
        String urlDestino = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURI();
        codigoModulo = 0;
        if (!urlDestino.equals("/Sindical/menuPrincipal.jsf")) {
            if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("idModulo") != null) {
                codigoModulo = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("idModulo");
            }
        }
        return codigoModulo;
    }

    public void setCodigoModulo(Integer codigoModulo) {
        this.codigoModulo = codigoModulo;
    }

    public void limparModulo() {
        Sessions.remove("idModulo");
    }

    public void onChange(TabChangeEvent event) {
        Tab activeTab = event.getTab();
    }

    public List<SelectItem> getListaDataVencimento() {
        if (listaDataVencimento.isEmpty()) {
            for (int i = 1; i <= 31; i++) {
                listaDataVencimento.add(new SelectItem(Integer.toString(i)));
            }
        }
        return listaDataVencimento;
    }

    public void setListaDataVencimento(List<SelectItem> listaDataVencimento) {
        this.listaDataVencimento = listaDataVencimento;
    }

    public Integer getIdSisEmailProtocolo() {
        return idSisEmailProtocolo;
    }

    public void setIdSisEmailProtocolo(Integer idSisEmailProtocolo) {
        this.idSisEmailProtocolo = idSisEmailProtocolo;
    }

    public List<SelectItem> getListaSisEmailProtocolo() {
        List<SelectItem> selectItems = new ArrayList();
        Dao dao = new Dao();
        List<SisEmailProtocolo> seps = (List<SisEmailProtocolo>) dao.list("SisEmailProtocolo");
        for (int i = 0; i < seps.size(); i++) {
            selectItems.add(new SelectItem(i, seps.get(i).getDescricao(), "" + seps.get(i).getId()));
        }
        return selectItems;
    }

    public void enviarEmailTeste() {
        if (emailTeste.isEmpty()) {
            Messages.warn("Validação", "Informar e-mail!");
            return;
        }
        DaoInterface di = new Dao();
        Mail mail = new Mail();
        mail.setEmail(
                new Email(
                        -1,
                        DataHoje.dataHoje(),
                        DataHoje.livre(new Date(), "HH:mm"),
                        (Usuario) Sessions.getObject("sessaoUsuario"),
                        (Rotina) di.find(new Rotina(), 111),
                        null,
                        "Email teste.",
                        "",
                        false,
                        false,
                        null
                )
        );
        List<EmailPessoa> emailPessoas = new ArrayList<>();
        EmailPessoa emailPessoa = new EmailPessoa();
        emailPessoa.setDestinatario(emailTeste);
        emailPessoa.setPessoa(null);
        emailPessoa.setRecebimento(null);
        emailPessoas.add(emailPessoa);
        mail.setEmailPessoas(emailPessoas);
        String[] string = mail.send();
        if (string[0].isEmpty()) {
            Messages.warn("Validação", "Erro ao enviar mensagem!" + string[0]);
        } else {
            Messages.info("Sucesso", "Email enviado com sucesso!");
        }
    }

    public String getEmailTeste() {
        return emailTeste;
    }

    public void setEmailTeste(String emailTeste) {
        this.emailTeste = emailTeste;
    }

}
