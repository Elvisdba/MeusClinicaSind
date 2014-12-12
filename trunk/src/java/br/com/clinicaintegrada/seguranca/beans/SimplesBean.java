package br.com.clinicaintegrada.seguranca.beans;

import br.com.clinicaintegrada.seguranca.Nivel;
import br.com.clinicaintegrada.seguranca.Modulo;
import br.com.clinicaintegrada.seguranca.SegEvento;
import br.com.clinicaintegrada.seguranca.Cliente;
import br.com.clinicaintegrada.seguranca.Rotina;
import br.com.clinicaintegrada.seguranca.Departamento;
import br.com.clinicaintegrada.administrativo.TipoDesligamento;
import br.com.clinicaintegrada.administrativo.TipoInternacao;
import br.com.clinicaintegrada.agenda.GrupoAgenda;
import br.com.clinicaintegrada.coordenacao.FuncaoEscala;
import br.com.clinicaintegrada.coordenacao.GrupoEvento;
import br.com.clinicaintegrada.coordenacao.PertenceGrupo;
import br.com.clinicaintegrada.coordenacao.TipoNotificacao;
import br.com.clinicaintegrada.endereco.Bairro;
import br.com.clinicaintegrada.endereco.DescricaoEndereco;
import br.com.clinicaintegrada.endereco.Logradouro;
import br.com.clinicaintegrada.logSistema.Logger;
import br.com.clinicaintegrada.pessoa.GrauParentesco;
import br.com.clinicaintegrada.pessoa.Nacionalidade;
import br.com.clinicaintegrada.pessoa.TipoAtendimento;
import br.com.clinicaintegrada.pessoa.TipoCadastro;
import br.com.clinicaintegrada.pessoa.TipoDocumento;
import br.com.clinicaintegrada.pessoa.TipoEndereco;
import br.com.clinicaintegrada.seguranca.controleUsuario.SessaoCliente;
import br.com.clinicaintegrada.seguranca.dao.RotinaDao;
import br.com.clinicaintegrada.sistema.Cor;
import br.com.clinicaintegrada.utils.Dao;
import br.com.clinicaintegrada.utils.DaoInterface;
import br.com.clinicaintegrada.utils.Messages;
import br.com.clinicaintegrada.utils.Sessions;
import br.com.clinicaintegrada.utils.Tables;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.persistence.Query;

@ManagedBean
@SessionScoped
@SuppressWarnings("unchecked")
public class SimplesBean implements Serializable {

    private Rotina rotina;
    private Object objeto;
    private List<SelectItem> listaRotinaCombo;
    private final List listImports = new ArrayList();
    private List<Rotina> listaRotina;
    private List lista;
    private String nomeRotina;
    private String pesquisaLista;
    private String mensagem;
    private String descricao;
    private Cliente cliente;
    private String[] sessoes;
    private int id;
    private int idRotina;

    public SimplesBean() {
        String a = "";
        rotina = new Rotina();
        idRotina = 0;
        listaRotinaCombo = new ArrayList<>();
        listaRotina = new ArrayList<>();
        nomeRotina = "";
        pesquisaLista = "";
        mensagem = "";
        descricao = "";
        sessoes = null;
        lista = new ArrayList();
        objeto = null;
        id = -1;
        cliente = new Cliente();
    }

    public List<SelectItem> getListaRotinaCombo() {
        int i = 0;
        RotinaDao db = new RotinaDao();
        if (listaRotinaCombo.isEmpty()) {
            listaRotina = db.pesquisaTodosSimples();
            while (i < getListaRotina().size()) {
                listaRotinaCombo.add(new SelectItem(i, getListaRotina().get(i).getRotina()));
                i++;
            }
        }
        return listaRotinaCombo;
    }

    public void save() {
        Dao dao = new Dao();
        Logger log = new Logger();
        log.setCadastroSimples(true);
        if (sessoes != null) {
            if (descricao.equals("")) {
                mensagem = "Informar descrição!";
                Messages.warn("Erro", mensagem);
                return;
            }
            if (id == -1) {
                cliente = SessaoCliente.get();
                converteObjeto(sessoes[0]);
                int t = objeto.getClass().getDeclaredFields().length;
                if (t == 2) {
                    if (dao.existDescriptionInField(objeto.getClass().getSimpleName(), "descricao", descricao)) {
                        Messages.warn("Validação", "Descrição já existe " + nomeRotina + " !");
                        return;
                    }
                    if (dao.save(objeto, true)) {
                        editaObjeto(objeto);
                        log.save("ID: " + id + " - DESCRICAO: " + descricao);
                        Messages.info("Sucesso", "Registro inserido");
                        descricao = "";
                        objeto = null;
                        lista.clear();
                        id = -1;
                    } else {
                        Messages.warn("Erro", "Ao inserir registro " + nomeRotina + " ");
                    }
                } else {
                    if (dao.save(objeto, true)) {
                        editaObjeto(objeto);
                        log.save("ID: " + id + " - Cliente: " + cliente.getId() + " - DESCRICAO: " + descricao);
                        Messages.info("Sucesso", "Registro inserido");
                        descricao = "";
                        objeto = null;
                        lista.clear();
                        id = -1;
                    } else {
                        Messages.warn("Erro", "Ao inserir registro " + nomeRotina + " ");
                    }
                }
            } else {
                Object o = dao.find(objeto);
                atualizaObjeto(sessoes[0]);
                if (dao.update(objeto, true)) {
                    log.update(o.toString(), "ID: " + id + " - DESCRICAO: " + descricao);
                    Messages.info("Sucesso", "Registro atualizado com sucesso");
                    lista.clear();
                } else {
                    Messages.warn("Erro", "Erro ao atualizar registro " + nomeRotina + " ");
                }
            }
        } else {
            Messages.warn("Erro", "Não há tipo de cadastro definido!");
        }
        cliente = new Cliente();
    }

    public String edit(Object o) {
        objeto = o;
        editaObjeto(objeto);
        Sessions.put("linkClicado", true);
        if (Sessions.exists("urlRetorno") && !((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno")).substring(0, 4).equals("menu")) {
            Sessions.put("simplesPesquisa", objeto);
            return (String) Sessions.getString("urlRetorno");
        }
        return null;
    }

    public void delete(Object o) {
        DaoInterface di = new Dao();
        Logger log = new Logger();
        log.setCadastroSimples(true);
        objeto = o;
        editaObjeto(objeto);
        if (!di.delete(objeto, true)) {
            Messages.warn("Erro", "Ao excluir registro");
            Messages.warn("Erro", mensagem);
        } else {
            log.delete("ID: " + id + " - DESCRICAO: " + descricao);
            Messages.info("Sucesso", "Registro excluído");
            lista.clear();
            objeto = null;
            id = -1;
            descricao = "";
        }
    }

    public void clear() {
        rotina = new Rotina();
        mensagem = "";
        id = -1;
        objeto = null;
        descricao = "";
        cliente = new Cliente();
    }

    public String limpar() {
        rotina = new Rotina();
        mensagem = "";
        objeto = null;
        descricao = "";
        return "simples";
    }

    public void setListaRotinaCombo(List<SelectItem> listaRotinaCombo) {
        this.listaRotinaCombo = listaRotinaCombo;
    }

    public List<Rotina> getListaRotina() {
        return listaRotina;
    }

    public void setListaRotina(List<Rotina> listaRotina) {
        this.listaRotina = listaRotina;
    }

    public int getIdRotina() {
        return idRotina;
    }

    public void setIdRotina(int idRotina) {
        this.idRotina = idRotina;
    }

    public Rotina getRotina() {
        return rotina;
    }

    public void setRotina(Rotina rotina) {
        this.rotina = rotina;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getNomeRotina() {
        if (sessoes != null) {
            nomeRotina = sessoes[1];
        }
        return nomeRotina;
    }

    public void setNomeRotina(String nomeRotina) {
        this.nomeRotina = nomeRotina;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String[] getSessoes() {
        if (Sessions.exists("cadastroSimples")) {
            sessoes = (String[]) Sessions.getStringVector("cadastroSimples");
            if (Sessions.exists("chamadaPaginaSimples")) {
                Sessions.remove("chamadaPaginaSimples");
            }
            Sessions.put("chamadaPaginaSimples", sessoes);
            Sessions.remove("cadastroSimples");
        }
        return sessoes;
    }

    public void setSessoes(String[] sessoes) {
        this.sessoes = sessoes;
    }

    public void converteObjeto(String tipo) {
        objeto = convertToObject(tipo);
    }

    public Object convertToObject(String tipo) {
//        for (int i = 0; i < getListImports().size(); i++) {
//           try {
//                String o = getListImports().get(i).toString()+"."+sessoes[0];
//                Class cls = Class.forName(o);
//                Class partypes[] = new Class[2];
//                Method m[] = cls.getDeclaredMethods();
//                partypes[0] = (Class) m[0].getGenericReturnType();
//                partypes[1] = (Class) m[2].getGenericReturnType();
//                Constructor ct = cls.getConstructor(partypes);
//                Object arglist[] = new Object[2];
//                arglist[0] = -1;
//                arglist[1] = descricao;
//                objeto = ct.newInstance(arglist);
//                if (objeto != null) {
//                    break;
//                }
//            } catch (Exception e) {
//                 return null;
//            }
//        }
        Object o = null;
        switch (tipo) {
            case "TipoEndereco":
                o = (TipoEndereco) new TipoEndereco(id, descricao);
                break;
            case "TipoDocumento":
                o = (TipoDocumento) new TipoDocumento(id, descricao);
                break;
            case "Evento":
                o = (SegEvento) new SegEvento(id, descricao);
                break;
            case "Modulo":
                o = (Modulo) new Modulo(id, descricao);
                break;
            case "Departamento":
                o = (Departamento) new Departamento(id, descricao);
                break;
            case "Nivel":
                o = (Nivel) new Nivel(id, descricao);
                break;
            case "Nacionalidade":
                o = (Nacionalidade) new Nacionalidade(id, descricao);
                break;
            case "Cor":
                o = (Cor) new Cor(id, descricao);
                break;
            case "TipoCadastro":
                o = (TipoCadastro) new TipoCadastro(id, descricao);
                break;
            case "GrauParentesco":
                o = (GrauParentesco) new GrauParentesco(id, descricao);
                break;
            case "TipoDesligamento":
                o = (TipoDesligamento) new TipoDesligamento(id, descricao);
                break;
            case "TipoInternacao":
                o = (TipoInternacao) new TipoInternacao(id, descricao);
                break;
            case "TipoAtendimento":
                o = (TipoAtendimento) new TipoAtendimento(id, descricao);
                break;
            case "Logradouro":
                o = (Logradouro) new Logradouro(id, descricao);
                break;
            case "GrupoEvento":
                o = (GrupoEvento) new GrupoEvento(id, descricao);
                break;
            case "TipoNotificacao":
                o = (TipoNotificacao) new TipoNotificacao(id, descricao);
                break;

            // SIMPLES COM ID CLIENTE
            case "Bairro":
                o = (Bairro) new Bairro(id, cliente, descricao);
                break;
            case "DescricaoEndereco":
                o = (DescricaoEndereco) new DescricaoEndereco(id, cliente, descricao);
                break;
            case "FuncaoEscala":
                o = (FuncaoEscala) new FuncaoEscala(id, cliente, descricao);
                break;
            case "GrupoAgenda":
                o = (GrupoAgenda) new GrupoAgenda(id, cliente, descricao);
                break;
            case "PertenceGrupo":
                o = (PertenceGrupo) new PertenceGrupo(id, cliente, descricao);
                break;
        }
        return o;
    }

    public void atualizaObjeto(String tipo) {
        switch (tipo) {
            case "Bairro":
                ((Bairro) objeto).setDescricao(descricao);
                break;
            case "Logradouro":
                ((Logradouro) objeto).setDescricao(descricao);
                break;
            case "DescricaoEndereco":
                ((DescricaoEndereco) objeto).setDescricao(descricao);
                break;
            case "TipoEndereco":
                ((TipoEndereco) objeto).setDescricao(descricao);
                break;
            case "TipoDocumento":
                ((TipoDocumento) objeto).setDescricao(descricao);
                break;
            case "GrupoAgenda":
                ((GrupoAgenda) objeto).setDescricao(descricao);
                break;
            case "Evento":
                ((SegEvento) objeto).setDescricao(descricao);
                break;
            case "Modulo":
                ((Modulo) objeto).setDescricao(descricao);
                break;
            case "Departamento":
                ((Departamento) objeto).setDescricao(descricao);
                break;
            case "Nivel":
                ((Nivel) objeto).setDescricao(descricao);
                break;
            case "Cor":
                ((Cor) objeto).setDescricao(descricao);
                break;
            case "Nacionalidade":
                ((Nacionalidade) objeto).setDescricao(descricao);
                break;
            case "TipoCadastro":
                ((TipoCadastro) objeto).setDescricao(descricao);
                break;
            case "GrauParentesco":
                ((GrauParentesco) objeto).setDescricao(descricao);
                break;
            case "TipoDesligamento":
                ((TipoDesligamento) objeto).setDescricao(descricao);
                break;
            case "TipoInternacao":
                ((TipoInternacao) objeto).setDescricao(descricao);
                break;
            case "FuncaoEscala":
                ((FuncaoEscala) objeto).setDescricao(descricao);
                break;
            case "GrupoEvento":
                ((GrupoEvento) objeto).setDescricao(descricao);
                break;
            case "TipoAtendimento":
                ((TipoAtendimento) objeto).setDescricao(descricao);
                break;
            case "PertenceGrupo":
                ((PertenceGrupo) objeto).setDescricao(descricao);
                break;
            case "TipoNotificacao":
                ((TipoNotificacao) objeto).setDescricao(descricao);
                break;
        }
    }

    public void editaObjeto(Object obj) {
        switch (obj.getClass().getSimpleName()) {
            case "Bairro":
                cliente = ((Bairro) objeto).getCliente();
                descricao = ((Bairro) obj).getDescricao();
                id = ((Bairro) objeto).getId();
                break;
            case "Logradouro":
                descricao = ((Logradouro) obj).getDescricao();
                id = ((Logradouro) objeto).getId();
                break;
            case "DescricaoEndereco":
                cliente = ((DescricaoEndereco) objeto).getCliente();
                descricao = ((DescricaoEndereco) obj).getDescricao();
                id = ((DescricaoEndereco) objeto).getId();
                break;
            case "TipoEndereco":
                descricao = ((TipoEndereco) obj).getDescricao();
                id = ((TipoEndereco) objeto).getId();
                break;
            case "TipoDocumento":
                descricao = ((TipoDocumento) obj).getDescricao();
                id = ((TipoDocumento) objeto).getId();
                break;
            case "Evento":
                descricao = ((SegEvento) obj).getDescricao();
                id = ((SegEvento) objeto).getId();
                break;
            case "Modulo":
                descricao = ((Modulo) obj).getDescricao();
                id = ((Modulo) objeto).getId();
                break;
            case "Departamento":
                descricao = ((Departamento) obj).getDescricao();
                id = ((Departamento) objeto).getId();
                break;
            case "Nivel":
                descricao = ((Nivel) obj).getDescricao();
                id = ((Nivel) objeto).getId();
                break;
            case "Cor":
                descricao = ((Cor) obj).getDescricao();
                id = ((Cor) objeto).getId();
                break;
            case "Nacionalidade":
                descricao = ((Nacionalidade) obj).getDescricao();
                id = ((Nacionalidade) objeto).getId();
                break;
            case "TipoCadastro":
                descricao = ((TipoCadastro) obj).getDescricao();
                id = ((TipoCadastro) objeto).getId();
                break;
            case "GrauParentesco":
                descricao = ((GrauParentesco) obj).getDescricao();
                id = ((GrauParentesco) objeto).getId();
                break;
            case "TipoDesligamento":
                descricao = ((TipoDesligamento) obj).getDescricao();
                id = ((TipoDesligamento) objeto).getId();
                break;
            case "TipoInternacao":
                descricao = ((TipoInternacao) obj).getDescricao();
                id = ((TipoInternacao) objeto).getId();
                break;
            case "TipoAtendimento":
                descricao = ((TipoAtendimento) obj).getDescricao();
                id = ((TipoAtendimento) objeto).getId();
                break;
            case "GrupoEvento":
                descricao = ((GrupoEvento) obj).getDescricao();
                id = ((GrupoEvento) objeto).getId();
                break;
            case "TipoNotificacao":
                descricao = ((TipoNotificacao) obj).getDescricao();
                id = ((TipoNotificacao) objeto).getId();
                break;

            // SIMPLES COM GRUPO EVENTO    
            case "FuncaoEscala":
                descricao = ((FuncaoEscala) obj).getDescricao();
                id = ((FuncaoEscala) objeto).getId();
                cliente = ((FuncaoEscala) objeto).getCliente();
                break;
            case "GrupoAgenda":
                descricao = ((GrupoAgenda) obj).getDescricao();
                id = ((GrupoAgenda) objeto).getId();
                cliente = ((GrupoAgenda) objeto).getCliente();
                break;
            case "PertenceGrupo":
                descricao = ((PertenceGrupo) obj).getDescricao();
                id = ((PertenceGrupo) objeto).getId();
                cliente = ((PertenceGrupo) objeto).getCliente();
                break;
        }
        Dao dao = new Dao();
        objeto = dao.rebind(objeto);
    }

    public boolean comparaObjeto(Object obj) {
        switch (obj.getClass().getSimpleName()) {
            case "Bairro":
                if (((Bairro) obj).getDescricao().contains(pesquisaLista)) {
                    return true;
                }
                break;
            case "Logradouro":
                if (((Logradouro) obj).getDescricao().contains(pesquisaLista)) {
                    return true;
                }
                break;
            case "DescricaoEndereco":
                if (((DescricaoEndereco) obj).getDescricao().contains(pesquisaLista)) {
                    return true;
                }
                break;
            case "TipoEndereco":
                if (((TipoEndereco) obj).getDescricao().contains(pesquisaLista)) {
                    return true;
                }
                break;
            case "TipoDocumento":
                if (((TipoDocumento) obj).getDescricao().contains(pesquisaLista)) {
                    return true;
                }
                break;
            case "GrupoAgenda":
                if (((GrupoAgenda) obj).getDescricao().contains(pesquisaLista)) {
                    return true;
                }
                break;
            case "Evento":
                if (((SegEvento) obj).getDescricao().contains(pesquisaLista)) {
                    return true;
                }
                break;
            case "Modulo":
                if (((Modulo) obj).getDescricao().contains(pesquisaLista)) {
                    return true;
                }
                break;
            case "Departamento":
                if (((Departamento) obj).getDescricao().contains(pesquisaLista)) {
                    return true;
                }
                break;
            case "Nivel":
                if (((Nivel) obj).getDescricao().contains(pesquisaLista)) {
                    return true;
                }
                break;
            case "Cor":
                if (((Cor) obj).getDescricao().contains(pesquisaLista)) {
                    return true;
                }
                break;
            case "Nacionalidade":
                if (((Nacionalidade) obj).getDescricao().contains(pesquisaLista)) {
                    return true;
                }
                break;
            case "TipoCadastro":
                if (((TipoCadastro) obj).getDescricao().contains(pesquisaLista)) {
                    return true;
                }
                break;
            case "GrauParentesco":
                if (((GrauParentesco) obj).getDescricao().contains(pesquisaLista)) {
                    return true;
                }
                break;
            case "Combustivel":
                if (((GrauParentesco) obj).getDescricao().contains(pesquisaLista)) {
                    return true;
                }
                break;
            case "TipoDesligamento":
                if (((TipoDesligamento) obj).getDescricao().contains(pesquisaLista)) {
                    return true;
                }
                break;
            case "TipoInternacao":
                if (((TipoInternacao) obj).getDescricao().contains(pesquisaLista)) {
                    return true;
                }
                break;
            case "FuncaoEscala":
                if (((FuncaoEscala) obj).getDescricao().contains(pesquisaLista)) {
                    return true;
                }
                break;
            case "GrupoEvento":
                if (((GrupoEvento) obj).getDescricao().contains(pesquisaLista)) {
                    return true;
                }
                break;
            case "TipoAtendimento":
                if (((TipoAtendimento) obj).getDescricao().contains(pesquisaLista)) {
                    return true;
                }
                break;
            case "PertenceGrupo":
                if (((PertenceGrupo) obj).getDescricao().contains(pesquisaLista)) {
                    return true;
                }
                break;
            case "TipoNotificacao":
                if (((TipoNotificacao) obj).getDescricao().contains(pesquisaLista)) {
                    return true;
                }
                break;
        }
        return false;
    }

    public void limpaLista() {
        // lista.clear();
    }

    public synchronized List getLista() throws ClassNotFoundException {
        if (sessoes != null) {
            if (!pesquisaLista.isEmpty()) {
                Object o = (Object) convertToObject(sessoes[0]);
                String tableName = Tables.name(o);
                Dao dao = new Dao();
                if (o == null) {
                    return new ArrayList();
                }
                String queryString;
                int t = o.getClass().getDeclaredFields().length;
                if (t == 2) {
                    queryString = " SELECT * FROM " + tableName + " WHERE translate(upper(ds_descricao)) LIKE '%" + descricao.toUpperCase() + "%'";
                } else {
                    queryString = " SELECT * FROM " + tableName + " WHERE translate(upper(ds_descricao)) LIKE '%" + descricao.toUpperCase() + "%' AND id_cliente = " + SessaoCliente.get().getId();
                }
                try {
                    Query query = dao.getEntityManager().createNativeQuery(queryString, o.getClass());
                    query.setMaxResults(1000);
                    lista = query.getResultList();
                    if (!lista.isEmpty()) {
                        return lista;
                    }
                } catch (Exception e) {
                    java.util.logging.Logger.getLogger(SimplesBean.class.getName()).log(Level.SEVERE, null, e);

                }
            }
            return new ArrayList();
        }
        return lista;
    }

    public void setLista(List lista) {
        this.lista = lista;
    }

    public String getPesquisaLista() {
        return pesquisaLista;
    }

    public void setPesquisaLista(String pesquisaLista) {
        this.pesquisaLista = pesquisaLista;
    }

    public String pacoteDaClasse(String classe) {
        List<String> list_class = new ArrayList();
        list_class.add("br.com.clinicaintegrada.coordenacao");
        list_class.add("br.com.clinicaintegrada.agenda");
        list_class.add("br.com.clinicaintegrada.endereco");
        list_class.add("br.com.clinicaintegrada.financeiro");
        list_class.add("br.com.clinicaintegrada.pessoa");
        list_class.add("br.com.clinicaintegrada.relatorios");
        list_class.add("br.com.clinicaintegrada.seguranca");
        list_class.add("br.com.clinicaintegrada.sistema");
        list_class.add("br.com.clinicaintegrada.suporte");
        list_class.add("br.com.clinicaintegrada.administrativo");
        list_class.add("br.com.clinicaintegrada.movimento");
        list_class.add("br.com.clinicaintegrada.movimento");
        for (String list_clas : list_class) {
            try {
                Class.forName(list_clas + "." + classe);
                return list_clas + "." + classe;
            } catch (ClassNotFoundException e) {
                //my class isn't there!
            }
        }
        return "";
    }
}
//                Class cls = Class.forName(sessoes[0]);
//
//                Class partypes[] = new Class[2];
//                Method m[] = cls.getDeclaredMethods();
//                partypes[0] = (Class) m[0].getGenericReturnType();
//                partypes[1] = (Class) m[2].getGenericReturnType();
//
//                Constructor ct = cls.getConstructor(partypes);
//
//                Object arglist[] = new Object[2];
//
//                arglist[0] = -1;
//                arglist[1] = descricao;
//
//                objeto = ct.newInstance(arglist);
