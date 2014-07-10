package br.com.rtools.seguranca.beans;

import br.com.rtools.agenda.GrupoAgenda;
import br.com.rtools.pessoa.Nacionalidade;
import br.com.rtools.endereco.Bairro;
import br.com.rtools.endereco.DescricaoEndereco;
import br.com.rtools.endereco.Logradouro;
import br.com.rtools.logSistema.Logger;
import br.com.rtools.pessoa.TipoDocumento;
import br.com.rtools.pessoa.TipoEndereco;
import br.com.rtools.seguranca.*;
import br.com.rtools.seguranca.dao.RotinaDao;
import br.com.rtools.sistema.Cor;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DaoInterface;
import br.com.rtools.utilitarios.Messages;
import br.com.rtools.utilitarios.Sessions;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
@SuppressWarnings("unchecked")
public class SimplesBean implements Serializable {

    private Rotina rotina;
    private Object objeto;
    private List<SelectItem> listaRotinaCombo;
    private List listImports = new ArrayList();
    private List<Rotina> listaRotina;
    private List lista;
    private String nomeRotina;
    private String pesquisaLista;
    private String mensagem;
    private String descricao;
    private String[] sessoes;
    private int id;
    private int idRotina;

    public SimplesBean() {
        rotina = new Rotina();
        idRotina = 0;
        listaRotinaCombo = new ArrayList<SelectItem>();
        listaRotina = new ArrayList<Rotina>();
        nomeRotina = "";
        pesquisaLista = "";
        mensagem = "";
        descricao = "";
        sessoes = null;
        lista = new ArrayList();
        objeto = null;
        id = -1;
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
                converteObjeto(sessoes[0]);
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

        if (tipo.equals("Bairro")) {
            objeto = (Bairro) new Bairro(id, descricao);
        } else if (tipo.equals("Logradouro")) {
            objeto = (Logradouro) new Logradouro(id, descricao);
        } else if (tipo.equals("DescricaoEndereco")) {
            objeto = (DescricaoEndereco) new DescricaoEndereco(id, descricao);
        } else if (tipo.equals("TipoEndereco")) {
            objeto = (TipoEndereco) new TipoEndereco(id, descricao);
        } else if (tipo.equals("TipoDocumento")) {
            objeto = (TipoDocumento) new TipoDocumento(id, descricao);
        } else if (tipo.equals("GrupoAgenda")) {
            objeto = (GrupoAgenda) new GrupoAgenda(id, descricao);
        } else if (tipo.equals("Evento")) {
            objeto = (Evento) new Evento(id, descricao);
        } else if (tipo.equals("Modulo")) {
            objeto = (Modulo) new Modulo(id, descricao);
        } else if (tipo.equals("Departamento")) {
            objeto = (Departamento) new Departamento(id, descricao);
        } else if (tipo.equals("Nivel")) {
            objeto = (Nivel) new Nivel(id, descricao);
        } else if (tipo.equals("Nacionalidade")) {
            objeto = (Nacionalidade) new Nacionalidade(id, descricao);
        } else if (tipo.equals("Cor")) {
            objeto = (Cor) new Cor(id, descricao);
        }
    }

    public void atualizaObjeto(String tipo) {
        if (tipo.equals("Bairro")) {
            ((Bairro) objeto).setDescricao(descricao);
        } else if (tipo.equals("Logradouro")) {
            ((Logradouro) objeto).setDescricao(descricao);
        } else if (tipo.equals("DescricaoEndereco")) {
            ((DescricaoEndereco) objeto).setDescricao(descricao);
        } else if (tipo.equals("TipoEndereco")) {
            ((TipoEndereco) objeto).setDescricao(descricao);
        } else if (tipo.equals("TipoDocumento")) {
            ((TipoDocumento) objeto).setDescricao(descricao);
        } else if (tipo.equals("GrupoAgenda")) {
            ((GrupoAgenda) objeto).setDescricao(descricao);
        } else if (tipo.equals("Evento")) {
            ((Evento) objeto).setDescricao(descricao);
        } else if (tipo.equals("Modulo")) {
            ((Modulo) objeto).setDescricao(descricao);
        } else if (tipo.equals("Departamento")) {
            ((Departamento) objeto).setDescricao(descricao);
        } else if (tipo.equals("Nivel")) {
            ((Nivel) objeto).setDescricao(descricao);
        } else if (tipo.equals("Cor")) {
            ((Cor) objeto).setDescricao(descricao);
        } else if (tipo.equals("Nacionalidade")) {
            ((Nacionalidade) objeto).setDescricao(descricao);
        }
    }

    public void editaObjeto(Object obj) {
        if (obj.getClass().getSimpleName().equals("Bairro")) {
            descricao = ((Bairro) obj).getDescricao();
            id = ((Bairro) objeto).getId();
        } else if (obj.getClass().getSimpleName().equals("Logradouro")) {
            descricao = ((Logradouro) obj).getDescricao();
            id = ((Logradouro) objeto).getId();
        } else if (obj.getClass().getSimpleName().equals("DescricaoEndereco")) {
            descricao = ((DescricaoEndereco) obj).getDescricao();
            id = ((DescricaoEndereco) objeto).getId();
        } else if (obj.getClass().getSimpleName().equals("TipoEndereco")) {
            descricao = ((TipoEndereco) obj).getDescricao();
            id = ((TipoEndereco) objeto).getId();
        } else if (obj.getClass().getSimpleName().equals("TipoDocumento")) {
            descricao = ((TipoDocumento) obj).getDescricao();
            id = ((TipoDocumento) objeto).getId();
        } else if (obj.getClass().getSimpleName().equals("GrupoAgenda")) {
            descricao = ((GrupoAgenda) obj).getDescricao();
            id = ((GrupoAgenda) objeto).getId();
        } else if (obj.getClass().getSimpleName().equals("Evento")) {
            descricao = ((Evento) obj).getDescricao();
            id = ((Evento) objeto).getId();
        } else if (obj.getClass().getSimpleName().equals("Modulo")) {
            descricao = ((Modulo) obj).getDescricao();
            id = ((Modulo) objeto).getId();
        } else if (obj.getClass().getSimpleName().equals("Departamento")) {
            descricao = ((Departamento) obj).getDescricao();
            id = ((Departamento) objeto).getId();
        } else if (obj.getClass().getSimpleName().equals("Nivel")) {
            descricao = ((Nivel) obj).getDescricao();
            id = ((Nivel) objeto).getId();
        } else if (obj.getClass().getSimpleName().equals("Cor")) {
            descricao = ((Cor) obj).getDescricao();
            id = ((Cor) objeto).getId();
        } else if (obj.getClass().getSimpleName().equals("Nacionalidade")) {
            descricao = ((Nacionalidade) obj).getDescricao();
            id = ((Nacionalidade) objeto).getId();
        }
        Dao dao = new Dao();
        objeto = dao.rebind(objeto);
    }

    public boolean comparaObjeto(Object obj) {
        if (obj.getClass().getSimpleName().equals("Bairro")) {
            if (((Bairro) obj).getDescricao().contains(pesquisaLista)) {
                return true;
            }
        } else if (obj.getClass().getSimpleName().equals("Logradouro")) {
            if (((Logradouro) obj).getDescricao().contains(pesquisaLista)) {
                return true;
            }
        } else if (obj.getClass().getSimpleName().equals("DescricaoEndereco")) {
            if (((DescricaoEndereco) obj).getDescricao().contains(pesquisaLista)) {
                return true;
            }
        } else if (obj.getClass().getSimpleName().equals("TipoEndereco")) {
            if (((TipoEndereco) obj).getDescricao().contains(pesquisaLista)) {
                return true;
            }
        } else if (obj.getClass().getSimpleName().equals("TipoDocumento")) {
            if (((TipoDocumento) obj).getDescricao().contains(pesquisaLista)) {
                return true;
            }
        } else if (obj.getClass().getSimpleName().equals("GrupoAgenda")) {
            if (((GrupoAgenda) obj).getDescricao().contains(pesquisaLista)) {
                return true;
            }
        } else if (obj.getClass().getSimpleName().equals("Evento")) {
            if (((Evento) obj).getDescricao().contains(pesquisaLista)) {
                return true;
            }
        } else if (obj.getClass().getSimpleName().equals("Modulo")) {
            if (((Modulo) obj).getDescricao().contains(pesquisaLista)) {
                return true;
            }
        } else if (obj.getClass().getSimpleName().equals("Departamento")) {
            if (((Departamento) obj).getDescricao().contains(pesquisaLista)) {
                return true;
            }
        } else if (obj.getClass().getSimpleName().equals("Nivel")) {
            if (((Nivel) obj).getDescricao().contains(pesquisaLista)) {
                return true;
            }
        } else if (obj.getClass().getSimpleName().equals("Cor")) {
            if (((Cor) obj).getDescricao().contains(pesquisaLista)) {
                return true;
            }
        } else if (obj.getClass().getSimpleName().equals("Nacionalidade")) {
            if (((Nacionalidade) obj).getDescricao().contains(pesquisaLista)) {
                return true;
            }
        }
        return false;
    }

    public void limpaLista() {
        // lista.clear();
    }

    public synchronized List getLista() {
        if (sessoes != null) {
            Dao dao = new Dao();
            if (!pesquisaLista.isEmpty()) {
                lista = dao.findByDescription(sessoes[0], pesquisaLista, "p");
            }
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
