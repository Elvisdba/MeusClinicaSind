package br.com.clinicaintegrada.sistema.beans;

import br.com.clinicaintegrada.pessoa.Juridica;
import br.com.clinicaintegrada.seguranca.Cliente;
import br.com.clinicaintegrada.seguranca.Usuario;
import br.com.clinicaintegrada.seguranca.dao.ClienteDao;
import br.com.clinicaintegrada.utils.Dao;
import br.com.clinicaintegrada.utils.DaoInterface;
import br.com.clinicaintegrada.utils.Messages;
import br.com.clinicaintegrada.utils.Sessions;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.FileUploadEvent;

@ManagedBean
@SessionScoped
@SuppressWarnings("unchecked")
public class ClienteBean implements Serializable {

    private List<Cliente> listCliente;
    private Cliente cliente;
    private String descricaoPesquisa;
    private Juridica juridica;
    private Usuario usuario;

    @PostConstruct
    public void init() {
        listCliente = new ArrayList<>();
        cliente = new Cliente();
        descricaoPesquisa = "";
        juridica = new Juridica();
        usuario = new Usuario();
    }

    @PreDestroy
    public void destroy() {
        Sessions.remove("clienteBean");
        Sessions.remove("clientePesquisa");
        Sessions.remove("juridicaPesquisa");
    }

    public void clear() {
        Sessions.remove("clienteBean");
    }

    public void save() {

        DaoInterface di = new Dao();

        cliente.setJuridica(juridica.getId());

        if (cliente.getIdJuridica() == -1) {
            Messages.info("Validação", "Pesquisar pessoa jurídica!");
            return;
        }
        if (cliente.getIdentifica().equals("")) {
            Messages.info("Validação", "Informar o identificador do cliente, deve ser único!");
            return;
        }

        if (getCliente().getId() == -1) {
            ClienteDao clienteDao = new ClienteDao();
            if (clienteDao.existeIdentificador(cliente)) {
                Messages.info("Validação", "Identificador já existe!");
                return;
            }

            if (clienteDao.existeIdentificadorPessoa(cliente)) {
                Messages.info("Validação", "Identificador já existe para essa pessoa!");
                return;
            }
            di.openTransaction();
            if (di.save(cliente)) {
                di.commit();
                Messages.info("Sucesso", "Registro inserido");
            } else {
                di.rollback();
                Messages.warn("Erro", "Ao inserir registro!");
            }
        } else {
            di.openTransaction();
            if (di.update(cliente)) {
                di.commit();
                Messages.info("Sucesso", "Registro atualizado");
            } else {
                di.rollback();
                Messages.warn("Erro", "Ao atualizar registro!");
            }
        }
    }

    public void delete() {
        DaoInterface di = new Dao();
        di.openTransaction();
        if (getCliente().getId() != -1) {
            if (di.delete((Cliente) di.find(cliente))) {
                di.commit();
                cliente = new Cliente();
                Messages.info("Sucesso", "Registro excluído");
            } else {
                di.commit();
                Messages.warn("Erro", "Ao excluir registro!");
            }
        }
    }

    public String edit(Object o) {
        Dao dao = new Dao();
        cliente = (Cliente) dao.rebind(o);
        Sessions.put("linkClicado", true);
        Sessions.put("clientePesquisa", cliente);
        return (String) Sessions.getString("urlRetorno");
    }

    public List<Cliente> getListCliente() {
        if (listCliente.isEmpty()) {
            if (!descricaoPesquisa.equals("")) {
//                ClienteDao clienteDao = new ClienteDo();
//                listCliente = (List<Cliente>) clienteDao.listCliente(descricaoPesquisa);
            } else {
                DaoInterface di = new Dao();
                listCliente = (List<Cliente>) di.list("Cliente");
            }
        }
        return listCliente;
    }

    public void limparListCliente() {
        listCliente.clear();
    }

    public void setListaCliente(List<Cliente> listCliente) {
        this.listCliente = listCliente;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getDescricaoPesquisa() {
        return descricaoPesquisa;
    }

    public void setDescricaoPesquisa(String descricaoPesquisa) {
        this.descricaoPesquisa = descricaoPesquisa;
    }

    public Juridica getJuridica() {
        if (Sessions.exists("juridicaPesquisa")) {
            juridica = (Juridica) Sessions.getObject("juridicaPesquisa", true);
        }
        return juridica;
    }

    public void setJuridica(Juridica juridica) {
        this.juridica = juridica;
    }

    public void upload(FileUploadEvent event) {
//        ClienteUpload cu = new ClienteUpload();
//        cu.setArquivo(event.getFile().getFileName());
//        cu.setDiretorio("Imagens");
//        //cu.setArquivo("l");
//        cu.setSubstituir(true);
//        cu.setRenomear("LogoCliente" + ".png");
//        cu.setEvent(event);
//        Upload.enviar(cu, true);
    }

    public String juridicaString(int id) {
        Dao dao = new Dao();
        Juridica j = (Juridica) dao.find(new Juridica(), id);
        return j.getPessoa().getNome() + " - CNPJ: " + j.getPessoa().getDocumento();
    }

    public Usuario getUsuario() {
        usuario = (Usuario) Sessions.getObject("sessaoUsuario");
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
