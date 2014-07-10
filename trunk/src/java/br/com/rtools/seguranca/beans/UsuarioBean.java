package br.com.rtools.seguranca.beans;

import br.com.rtools.logSistema.Logger;
import br.com.rtools.pessoa.Fisica;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.seguranca.Cliente;
import br.com.rtools.seguranca.Departamento;
import br.com.rtools.seguranca.Evento;
import br.com.rtools.seguranca.MacFilial;
import br.com.rtools.seguranca.Modulo;
import br.com.rtools.seguranca.Nivel;
import br.com.rtools.seguranca.Permissao;
import br.com.rtools.seguranca.PermissaoDepartamento;
import br.com.rtools.seguranca.PermissaoUsuario;
import br.com.rtools.seguranca.Rotina;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.seguranca.UsuarioAcesso;
import br.com.rtools.seguranca.controleUsuario.SessaoCliente;
import br.com.rtools.seguranca.dao.ModuloDao;
import br.com.rtools.seguranca.dao.PermissaoDao;
import br.com.rtools.seguranca.dao.PermissaoDepartamentoDao;
import br.com.rtools.seguranca.dao.PermissaoUsuarioDao;
import br.com.rtools.seguranca.dao.RotinaDao;
import br.com.rtools.seguranca.dao.UsuarioAcessoDao;
import br.com.rtools.seguranca.dao.UsuarioDao;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DaoInterface;
import br.com.rtools.utilitarios.Messages;
import br.com.rtools.utilitarios.Sessions;
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
@SuppressWarnings("unchecked")
public class UsuarioBean implements Serializable {

    private Usuario usuario;
    private List<PermissaoUsuario> listPermissaoUsuario;
    private List<Usuario> listUsuario;
    private List<UsuarioAcesso> listUsuarioAcesso;
    private List<SelectItem> listModulos;
    private List<SelectItem> listRotinas;
    private List<SelectItem> listEventos;
    private List<SelectItem> listDepartamentos;
    private List<SelectItem> listNiveis;
    private String confirmaSenha;
    private String descricaoPesquisa;
    private String mensagem;
    private String senhaNova;
    private String senhaAntiga;
    private String userLogado;
    private boolean adicionado;
    private boolean disSenha;
    private boolean disNovaSenha;
    private boolean disStrSenha;
    private boolean filtrarPorModulo;
    private boolean filtrarPorRotina;
    private boolean filtrarPorEvento;
    private boolean filtrarUsuarioAtivo;
    private boolean usuarioMaster;
    private int idDepartamento;
    private int idEvento;
    private int idModulo;
    private int idNivel;
    private int idRotina;

    @PostConstruct
    public void init() {
        usuario = new Usuario();
        if (((Usuario) Sessions.getObject("sessaoUsuario")).getId() == 1) {
            usuarioMaster = true;
        }
        listPermissaoUsuario = new ArrayList();
        listUsuario = new ArrayList();
        listUsuarioAcesso = new ArrayList();
        listModulos = new ArrayList<SelectItem>();
        listRotinas = new ArrayList<SelectItem>();
        listEventos = new ArrayList<SelectItem>();
        listDepartamentos = new ArrayList<SelectItem>();
        listNiveis = new ArrayList<SelectItem>();
        confirmaSenha = "";
        descricaoPesquisa = "";
        mensagem = "";
        senhaNova = "";
        senhaAntiga = "";
        userLogado = "";
        adicionado = false;
        disSenha = false;
        disNovaSenha = false;
        disStrSenha = true;
        filtrarPorModulo = false;
        filtrarPorRotina = false;
        filtrarPorEvento = false;
        filtrarUsuarioAtivo = true;
        idDepartamento = 0;
        idEvento = 0;
        idModulo = 0;
        idNivel = 0;
        idRotina = 0;
    }

    @PreDestroy
    public void destroy() {
        Sessions.remove("usuarioBean");
        Sessions.remove("usuarioPesquisa");
        Sessions.remove("pessoaPesquisa");
    }

    public void clear() {
        Sessions.remove("usuarioBean");
    }

    public void save() {
        DaoInterface di = new Dao();
        di.openTransaction();

        UsuarioDao db = new UsuarioDao();

        if (usuario.getLogin().equals("")) {
            mensagem = "Campo login não pode ser nulo!";
            return;
        }

        if (usuario.getSenha().equals("")) {
            mensagem = "Campo senha não pode ser nulo!";
            return;
        }
        if (usuario.getSenha().length() > 6) {
            mensagem = "A senha deve ter no máximo 6 Caracteres!";
            return;
        }
        Logger logger = new Logger();
        if (usuario.getId() == -1) {
            if (usuario.getCliente() == null || usuario.getCliente().getId() == -1) {
                usuario.setCliente(SessaoCliente.get());
            }
            if (usuario.getPessoa().getId() == -1) {
                mensagem = "Pesquise um nome de Usuário disponível!";
                return;
            }

            if (!usuario.getSenha().equals(confirmaSenha)) {
                mensagem = "Senhas não correspondem!";
                return;
            }
            if (db.pesquisaLogin(usuario.getLogin(), usuario.getPessoa().getId()).isEmpty()) {
                if (di.save(usuario)) {
                    di.commit();
                    mensagem = "Login e senha salvos com Sucesso!";
                    logger.save("ID: " + usuario.getId() + " - Pessoa: " + usuario.getPessoa().getId() + " - " + usuario.getPessoa().getNome() + " - Login: " + usuario.getLogin() + " - Ativo: " + usuario.getAtivo());
                } else {
                    di.rollback();
                    mensagem = "Erro ao Salvar Login e Senha!";
                }
            } else {
                di.rollback();
                mensagem = "Este login já existe no Sistema.";
            }
        } else {
            Usuario user = (Usuario) di.find(usuario);
            if (disNovaSenha) {
                if (user.getSenha().equals(getSenhaAntiga()) && !usuario.getSenha().equals("")) {
                } else {
                    usuario.setSenha(user.getSenha());
                    mensagem = "Senha Incompativel!";
                }
            } else {
                usuario.setSenha(user.getSenha());
            }
            Usuario usu = (Usuario) di.find(usuario);
            String beforeUpdate = "ID: " + usu.getId() + " - Pessoa: (" + usu.getPessoa().getId() + ") " + usu.getPessoa().getNome() + " - Login: " + usu.getLogin() + " - Ativo: " + usu.getAtivo();
            if (di.update(usuario)) {
                di.commit();
                mensagem = "Login e senha salvos com Sucesso!";
                logger.update(beforeUpdate, "ID: " + usuario.getId() + " - Pessoa: (" + usuario.getPessoa().getId() + ") " + usuario.getPessoa().getNome() + " - Login: " + usuario.getLogin() + " - Ativo: " + usuario.getAtivo());
            } else {
                di.rollback();
                mensagem = "Erro ao atualizar Usuario!";
            }
        }
    }

    public String addUsuarioPermissao() {
        if (usuario.getId() == -1) {
            return null;
        }
        PermissaoUsuario pu = new PermissaoUsuario();
        DaoInterface di = new Dao();
        pu.setDepartamento((Departamento) di.find(new Departamento(), Integer.valueOf(listDepartamentos.get(idDepartamento).getDescription())));
        pu.setNivel((Nivel) di.find(new Nivel(), Integer.valueOf(listNiveis.get(idNivel).getDescription())));
        pu.setUsuario(usuario);
        pu.setCliente(SessaoCliente.get());
        PermissaoUsuarioDao permissaoUsuarioDB = new PermissaoUsuarioDao();
        if (permissaoUsuarioDB.existePermissaoUsuario(pu)) {
            Messages.warn("Sistema", "Permissão já existe");
            return null;
        }
        di.openTransaction();
        if (di.save(pu)) {
            di.commit();
            Logger logger = new Logger();
            logger.save("Permissão Usuário - ID: " + pu.getId() + " - Usuário (" + pu.getUsuario().getId() + ") " + pu.getUsuario().getLogin() + " - Departamento (" + pu.getDepartamento().getId() + ") " + pu.getDepartamento().getDescricao() + " - Nível (" + pu.getNivel().getId() + ") " + pu.getNivel().getDescricao());
            Messages.info("Sucesso", "Permissão adicionada");
        } else {
            di.rollback();
            Messages.warn("Erro", "Erro ao adicionar essa permissão!");
        }
        idDepartamento = 0;
        idNivel = 0;
        return null;
    }

    public String removePermissaoUsuario(PermissaoUsuario pu) {
        if (pu.getId() != -1) {
            DaoInterface di = new Dao();
            pu = (PermissaoUsuario) di.find(pu);
            di.openTransaction();
            if (di.delete(pu)) {
                di.commit();
                Logger logger = new Logger();
                logger.delete("Permissão Usuário - ID: " + pu.getId() + " - Usuário (" + pu.getUsuario().getId() + ") " + pu.getUsuario().getLogin() + " - Departamento (" + pu.getDepartamento().getId() + ") " + pu.getDepartamento().getDescricao() + " - Nível (" + pu.getNivel().getId() + ") " + pu.getNivel().getDescricao());
                Messages.info("Sucesso", "Permissão removida");
            } else {
                di.rollback();
                Messages.warn("Erro", "Erro ao Excluír permissão!");
                return null;
            }
        }
        return null;
    }

    public boolean removePermissoes(DaoInterface di) {
        PermissaoUsuarioDao permissaoUsuarioDao = new PermissaoUsuarioDao();
        PermissaoDepartamentoDao permissaoDepartamentoDao = new PermissaoDepartamentoDao();
        UsuarioAcessoDao usuarioAcessoDao = new UsuarioAcessoDao();
        for (int i = 0; i < listPermissaoUsuario.size(); i++) {
            PermissaoUsuario perUsuario = listPermissaoUsuario.get(i);
            perUsuario = permissaoUsuarioDao.pesquisaPermissaoUsuario(perUsuario.getUsuario().getId(), perUsuario.getDepartamento().getId(), perUsuario.getNivel().getId(), SessaoCliente.get().getId());
            if (perUsuario == null) {
                di.rollback();
                return false;
            }
            List<PermissaoDepartamento> lista = permissaoDepartamentoDao.pesquisaPermissaoDepartamento(perUsuario.getDepartamento().getId(), perUsuario.getNivel().getId(), SessaoCliente.get().getId());
            for (int w = 0; w < lista.size(); w++) {
                UsuarioAcesso ua = usuarioAcessoDao.pesquisaUsuarioAcesso(perUsuario.getUsuario().getId(), lista.get(w).getPermissao().getId(), SessaoCliente.get().getId());
                if (ua == null) {
                    di.rollback();
                    return false;
                }
                if (!di.delete(ua)) {
                    di.rollback();
                    return false;
                }
            }
            if (!di.delete(perUsuario)) {
                di.rollback();
                return false;
            } else {
                return true;
            }
        }
        return true;
    }

    public void delete() {
        DaoInterface di = new Dao();
        di.openTransaction();
        if (usuario.getId() != -1) {
            if (!removePermissoes(di)) {
                mensagem = "Erro ao excluir permissões!";
                return;
            }

            if (!di.delete(usuario)) {
                di.rollback();
                mensagem = "Login não pode ser excluido!";
            } else {
                di.commit();
                mensagem = "Login excluido com sucesso!";
                Logger logger = new Logger();
                logger.delete("ID: " + usuario.getId() + " - Pessoa: " + usuario.getPessoa().getId() + " - " + usuario.getPessoa().getNome() + " - Login: " + usuario.getLogin());
                destroy();
            }
        }
    }

    public String edit(Usuario usu) {
        usuario = usu;
        Sessions.put("pessoaPesquisa", usuario.getPessoa());
        Sessions.put("usuarioPesquisa", usuario);
        Sessions.put("linkClicado", true);
        return (String) Sessions.getString("urlRetorno");
    }

    public List<Usuario> getListUsuario() {
        if (listUsuario.isEmpty()) {
            UsuarioDao usuarioDao = new UsuarioDao();
            if (descricaoPesquisa.isEmpty()) {
                listUsuario = usuarioDao.pesquisaTodosPorCliente(SessaoCliente.get().getId());
            } else {
                listUsuario = usuarioDao.pesquisaTodosPorDescricao(descricaoPesquisa, SessaoCliente.get().getId());
            }
            List<Usuario> list = new ArrayList<Usuario>();
            if (filtrarUsuarioAtivo) {
                for (int i = 0; i < listUsuario.size(); i++) {
                    if (listUsuario.get(i).getAtivo()) {
                        list.add(listUsuario.get(i));
                    }
                }
                listUsuario.clear();
                listUsuario = list;
            }
        }
        return listUsuario;
    }

    public void limparListaUsuario() {
        listUsuario.clear();
    }

    public List<SelectItem> getListNiveis() {
        if (listNiveis.isEmpty()) {
            DaoInterface di = new Dao();
            List niveis = di.list(new Nivel(), true);
            if (!niveis.isEmpty()) {
                for (int i = 0; i < niveis.size(); i++) {
                    listNiveis.add(new SelectItem(i,
                            ((Nivel) niveis.get(i)).getDescricao(),
                            Integer.toString(((Nivel) niveis.get(i)).getId()))
                    );
                }
            }
        }
        return listNiveis;
    }

    public List<SelectItem> getListDepartamentos() {
        if (listDepartamentos.isEmpty()) {
            DaoInterface di = new Dao();
            List departamentos = di.list(new Departamento(), true);
            for (int i = 0; i < departamentos.size(); i++) {
                listDepartamentos.add(new SelectItem(i,
                        ((Departamento) departamentos.get(i)).getDescricao(),
                        Integer.toString(((Departamento) departamentos.get(i)).getId()))
                );
            }
        }
        return listDepartamentos;
    }

    public void sairSistema() throws IOException {
        String retorno = "";
        if (Sessions.exists("sessaoCliente")) {
            retorno = ((Cliente) Sessions.getObject("sessaoCliente")).getIdentifica() + "/";
            if (Sessions.exists("acessoFilial")) {
                MacFilial macFilial = (MacFilial) Sessions.getObject("acessoFilial");
                retorno += "?filial=" + macFilial.getMac();
            }

        }
        /*String sair;
         if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario") != null){
         FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("sessaoUsuario");
         sair = "index";
         }else sair = "";
         return sair;*/
        //Contexto da Aplicação
        FacesContext conext = FacesContext.getCurrentInstance();
        //Verifica a sessao e a grava na variavel
        HttpSession session = (HttpSession) conext.getExternalContext().getSession(false);
        //Fecha/Destroi sessao
        session.invalidate();
        FacesContext.getCurrentInstance().getExternalContext().redirect(retorno);
    }

    public String sairSuporteWeb() {
        /*String sair;
         if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario") != null){
         FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("sessaoUsuario");
         sair = "index";
         }else sair = "";
         return sair;*/
        //Contexto da Aplicação
        FacesContext conext = FacesContext.getCurrentInstance();
        //Verifica a sessao e a grava na variavel
        HttpSession session = (HttpSession) conext.getExternalContext().getSession(false);
        //Fecha/Destroi sessao
        session.invalidate();
        return "indexSuporte";
    }

    public boolean isDisSenha() {
        if (usuario.getId() == -1) {
            disSenha = true;
        } else {
            disSenha = false;
        }
        return disSenha;
    }

    public void setDisSenha(boolean disSenha) {
        this.disSenha = disSenha;
    }

    public boolean isDisStrSenha() {
        if (usuario.getId() == -1) {
            disStrSenha = false;
        } else {
            disStrSenha = true;
        }
        return disStrSenha;
    }

    public void setDisStrSenha(boolean disStrSenha) {
        this.disStrSenha = disStrSenha;
    }

    public void habilitaNovaSenha() {
        if (!disNovaSenha) {
            disNovaSenha = true;
        } else {
            disNovaSenha = false;
        }
    }

    public boolean isDisNovaSenha() {
        return disNovaSenha;
    }

    public void setDisNovaSenha(boolean disNovaSenha) {
        this.disNovaSenha = disNovaSenha;
    }

    public String getSenhaAntiga() {
        return senhaAntiga;
    }

    public void setSenhaAntiga(String senhaAntiga) {
        this.senhaAntiga = senhaAntiga;
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

    public Usuario getUsuario() {
        if (Sessions.exists("pessoaPesquisa")) {
            usuario.setPessoa((Pessoa) Sessions.getObject("pessoaPesquisa", true));
        } else if (Sessions.exists("fisicaPesquisa")) {
            usuario.setPessoa(((Fisica) Sessions.getObject("fisicaPesquisa", true)).getPessoa());
        } else if (Sessions.exists("clientePesquisa")) {
            Dao dao = new Dao();
            Cliente cliente = (Cliente) Sessions.getObject("clientePesquisa", true);
            usuario.setPessoa(((Juridica) dao.find(new Juridica(), cliente.getIdJuridica())).getPessoa());
            if (usuario.getPessoa().getId() != 1) {
                usuario.setLogin(cliente.getIdentifica());
                usuario.setCliente(cliente);
                usuario.setAdministrador(true);
            } else {
                usuario = new Usuario();
            }
        }
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getUserLogado() {
        return userLogado;
    }

    public void setUserLogado(String userLogado) {
        this.userLogado = userLogado;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getConfirmaSenha() {
        return confirmaSenha;
    }

    public void setConfirmaSenha(String confirmaSenha) {
        this.confirmaSenha = confirmaSenha;
    }

    public List<PermissaoUsuario> getListPermissaoUsuario() {
        PermissaoUsuarioDao db = new PermissaoUsuarioDao();
        if (usuario.getId() != -1 && !adicionado) {
            listPermissaoUsuario = db.pesquisaListaPermissaoPorUsuario(usuario.getId(), SessaoCliente.get().getId());
        }
        return listPermissaoUsuario;
    }

    public void setListPermissaoUsuario(List<PermissaoUsuario> listaPermissaoUsuario) {
        this.listPermissaoUsuario = listaPermissaoUsuario;
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

    public List<SelectItem> getListModulos() {
        if (listModulos.isEmpty()) {
            ModuloDao moduloDao = new ModuloDao();
            List<Modulo> modulos = moduloDao.listaModuloPermissaoAgrupado(SessaoCliente.get().getId());
            for (int i = 0; i < modulos.size(); i++) {
                listModulos.add(new SelectItem(i,
                        modulos.get(i).getDescricao(),
                        Integer.toString(modulos.get(i).getId())));
            }
            listRotinas.clear();
        }
        return listModulos;
    }

    public void setListModulos(List<SelectItem> listModulos) {
        this.listModulos = listModulos;
    }

    public List<SelectItem> getListRotinas() {
        if (listRotinas.isEmpty() && !listModulos.isEmpty()) {
            int idM = Integer.parseInt(listModulos.get(idModulo).getDescription());
            RotinaDao rotinaDao = new RotinaDao();
            List<Rotina> rotinas = rotinaDao.listaRotinaPermissaoAgrupado(idM, SessaoCliente.get().getId());
            idRotina = 0;
            for (int i = 0; i < rotinas.size(); i++) {
                listRotinas.add(
                        new SelectItem(
                                i,
                                rotinas.get(i).getRotina(),
                                Integer.toString(rotinas.get(i).getId())));
            }
            listEventos.clear();
        }
        return listRotinas;
    }

    public void setListRotinas(List<SelectItem> listRotinas) {
        this.listRotinas = listRotinas;
    }

    public List<SelectItem> getListEventos() {
        if (listEventos.isEmpty() && !listRotinas.isEmpty() && !listModulos.isEmpty()) {
            PermissaoDao db = new PermissaoDao();
            int idM = Integer.parseInt(listModulos.get(idModulo).getDescription());
            int idR = Integer.parseInt(listRotinas.get(idRotina).getDescription());
            List<Evento> eventos = db.listaEventoPermissaoAgrupado(idM, idR);
            listEventos.clear();
            for (int i = 0; i < eventos.size(); i++) {
                listEventos.add(new SelectItem(i, eventos.get(i).getDescricao(), Integer.toString(eventos.get(i).getId())));
            }
        }
        return listEventos;
    }

    public void setListEventos(List<SelectItem> listEventos) {
        this.listEventos = listEventos;
    }

    public void addUsuarioAcesso() {
        UsuarioAcessoDao usuarioAcessoDao = new UsuarioAcessoDao();
        PermissaoDao permissaoDao = new PermissaoDao();
        int idM = Integer.parseInt(listModulos.get(idModulo).getDescription());
        int idR = Integer.parseInt(listRotinas.get(idRotina).getDescription());
        int idE = Integer.parseInt(listEventos.get(idEvento).getDescription());
        int idC = SessaoCliente.get().getId();
        if (usuario.getId() != -1) {
            if (((UsuarioAcesso) (usuarioAcessoDao.pesquisaUsuarioAcessoModuloRotinaEvento(usuario.getId(), idM, idR, idE, idC))) == null) {
                Permissao permissao = permissaoDao.pesquisaPermissaoModuloRotinaEvento(idM, idR, idE, idC);
                UsuarioAcesso usuarioAcesso = new UsuarioAcesso();
                DaoInterface di = new Dao();
                usuarioAcesso.setUsuario(usuario);
                usuarioAcesso.setPermissao(permissao);
                usuarioAcesso.setCliente(SessaoCliente.get());
                di.openTransaction();
                usuarioAcesso.setPermite(true);
                if (di.save(usuarioAcesso)) {
                    di.commit();
                    Logger logger = new Logger();
                    logger.save("Usuário Acesso - ID: " + usuarioAcesso.getId() + " - Usuário (" + usuarioAcesso.getUsuario().getId() + ") " + usuarioAcesso.getUsuario().getLogin() + " - Permissão (" + usuarioAcesso.getPermissao().getId() + ") [Módulo: " + usuarioAcesso.getPermissao().getModulo().getDescricao() + " - Rotina: " + usuarioAcesso.getPermissao().getRotina().getRotina() + " - Evento: " + usuarioAcesso.getPermissao().getEvento().getDescricao() + "]");
                    Messages.info("Sucesso", "Permissão adicionada");
                } else {
                    di.rollback();
                    Messages.warn("Erro", "Erro ao adicionar permissão!");
                }
            } else {
                Messages.warn("Sistema", "Permissão já existe!");
            }
        }
        listUsuarioAcesso.clear();
    }

    public List<UsuarioAcesso> getListUsuarioAcesso() {
        listUsuarioAcesso.clear();
        if (usuario.getId() != -1) {
            UsuarioAcessoDao usuarioAcessoDao = new UsuarioAcessoDao();
            int idM = 0;
            int idR = 0;
            int idE = 0;
            if (filtrarPorModulo) {
                idM = Integer.parseInt(listModulos.get(idModulo).getDescription());
            }
            if (filtrarPorRotina) {
                idR = Integer.parseInt(listRotinas.get(idRotina).getDescription());
            }
            if (filtrarPorEvento) {
                idE = Integer.parseInt(listEventos.get(idEvento).getDescription());
            }
            listUsuarioAcesso = usuarioAcessoDao.listaUsuarioAcesso(usuario.getId(), idM, idR, idE, SessaoCliente.get().getId());
            if (filtrarPorModulo || filtrarPorRotina || filtrarPorEvento) {
            }
        }
        return listUsuarioAcesso;
    }

    public void setListUsuarioAcesso(List<UsuarioAcesso> listUsuarioAcesso) {
        this.listUsuarioAcesso = listUsuarioAcesso;
    }

    public void limparListaUsuarioAcesso() {
        limparListaUsuarioAcessox();
        listModulos.clear();
        getListModulos();
        getListRotinas();
        getListEventos();
    }

    public void limparListaUsuarioAcessox() {
        listUsuarioAcesso.clear();
    }

    public void updateUsuarioAcesso(UsuarioAcesso ua) {
        if (ua.getId() == -1) {
            return;
        }
        DaoInterface di = new Dao();
        di.openTransaction();
        Logger logger = new Logger();
        String beforeUpdate = "Usuário Acesso - ID: " + ua.getId() + " - Usuário (" + ua.getUsuario().getId() + ") " + ua.getUsuario().getLogin() + " - Permissão (" + ua.getPermissao().getId() + ") [Módulo: " + ua.getPermissao().getModulo().getDescricao() + " - Rotina: " + ua.getPermissao().getRotina().getRotina() + " - Evento: " + ua.getPermissao().getEvento().getDescricao() + "] - Permite:" + ua.isPermite();
        if (ua.isPermite()) {
            ua.setPermite(false);
        } else {
            ua.setPermite(true);
        }
        if (di.update(ua)) {
            logger.update(beforeUpdate, "Usuário Acesso - ID: " + ua.getId() + " - Usuário (" + ua.getUsuario().getId() + ") " + ua.getUsuario().getLogin() + " - Permissão (" + ua.getPermissao().getId() + ") [Módulo: " + ua.getPermissao().getModulo().getDescricao() + " - Rotina: " + ua.getPermissao().getRotina().getRotina() + " - Evento: " + ua.getPermissao().getEvento().getDescricao() + "] - Permite:" + ua.isPermite());
            di.commit();
            Messages.info("Sucesso", "Permissão de acesso atualizada");
            listUsuarioAcesso.clear();
        } else {
            Messages.warn("Erro", "Falha ao atualizar essa permisão!");
            di.rollback();
        }
    }

    public void removeUsuarioAcesso(UsuarioAcesso ua) {
        if (ua.getId() == -1) {
            return;
        }
        DaoInterface di = new Dao();
        di.openTransaction();
        if (di.delete(ua)) {
            di.commit();
            Logger logger = new Logger();
            logger.delete("Usuário Acesso - ID: " + ua.getId() + " - Usuário (" + ua.getUsuario().getId() + ") " + ua.getUsuario().getLogin() + " - Permissão (" + ua.getPermissao().getId() + ") [Módulo: " + ua.getPermissao().getModulo().getDescricao() + " - Rotina: " + ua.getPermissao().getRotina().getRotina() + " - Evento: " + ua.getPermissao().getEvento().getDescricao() + "]");
            listUsuarioAcesso.clear();
            Messages.info("Sucesso", "Permissão removida");
        } else {
            di.rollback();
            Messages.warn("Erro", "Erro ao remover permissão!");
        }

    }

    public boolean isFiltrarPorModulo() {
        return filtrarPorModulo;
    }

    public void setFiltrarPorModulo(boolean filtrarPorModulo) {
        this.filtrarPorModulo = filtrarPorModulo;
    }

    public boolean isFiltrarPorRotina() {
        return filtrarPorRotina;
    }

    public void setFiltrarPorRotina(boolean filtrarPorRotina) {
        this.filtrarPorRotina = filtrarPorRotina;
    }

    public boolean isFiltrarPorEvento() {
        return filtrarPorEvento;
    }

    public void setFiltrarPorEvento(boolean filtrarPorEvento) {
        this.filtrarPorEvento = filtrarPorEvento;
    }

    public String getDescricaoPesquisa() {
        return descricaoPesquisa;
    }

    public void setDescricaoPesquisa(String descricaoPesquisa) {
        this.descricaoPesquisa = descricaoPesquisa;
    }

    public String getUsuarioPerfil() {
        if (Sessions.exists("sessaoUsuario")) {
            usuario = (Usuario) Sessions.getObject("sessaoUsuario");
        }
        return null;
    }

    public void salvarSenhaUsuarioPerfil() {
        if (usuario.getId() != -1) {
            if (usuario.getPessoa().getId() == 1) {
                mensagem = "Não é possível alterar a senha do administrador!";
                Messages.warn("Validação", mensagem);
                return;
            }
            if (getSenhaNova().equals("")) {
                mensagem = "Campo senha não pode ser nulo!";
                Messages.warn("Validação", mensagem);
                return;
            }
            if (getSenhaNova().length() > 6) {
                mensagem = "A senha deve ter no máximo 6 Caracteres!";
                Messages.warn("Validação", mensagem);
                return;
            }
            DaoInterface di = new Dao();
            Usuario user = (Usuario) di.find(new Usuario(), usuario.getId());
            if (!user.getSenha().equals(senhaAntiga)) {
                mensagem = "Senha antiga incompativel!";
                Messages.warn("Erro", mensagem);
                return;
            }
            usuario.setSenha(senhaNova);
            di.openTransaction();
            if (di.update(usuario)) {
                di.commit();
                setSenhaNova("");
                setSenhaAntiga("");
                mensagem = "Senha alterada";
                Messages.info("Sucesso", mensagem);
                Logger logger = new Logger();
                logger.update("", "ID: " + usuario.getId() + " - Pessoa: " + usuario.getPessoa().getId() + " - " + usuario.getPessoa().getNome() + " - Login: " + usuario.getLogin() + " - Ativo: " + usuario.getAtivo());
            } else {
                di.rollback();
                mensagem = "Não foi possível atualizar essa senha!";
            }
        }
    }

    public String getSenhaNova() {
        return senhaNova;
    }

    public void setSenhaNova(String senhaNova) {
        this.senhaNova = senhaNova;
    }

    public boolean isFiltrarUsuarioAtivo() {
        return filtrarUsuarioAtivo;
    }

    public void setFiltrarUsuarioAtivo(boolean filtrarUsuarioAtivo) {
        this.filtrarUsuarioAtivo = filtrarUsuarioAtivo;
    }

    public boolean isUsuarioMaster() {
        return usuarioMaster;
    }

    public void setUsuarioMaster(boolean usuarioMaster) {
        this.usuarioMaster = usuarioMaster;
    }
}
