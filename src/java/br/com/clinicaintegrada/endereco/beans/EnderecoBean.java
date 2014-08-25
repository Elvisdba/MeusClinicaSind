package br.com.clinicaintegrada.endereco.beans;

import br.com.clinicaintegrada.endereco.DescricaoEndereco;
import br.com.clinicaintegrada.endereco.Endereco;
import br.com.clinicaintegrada.endereco.Logradouro;
import br.com.clinicaintegrada.endereco.Cidade;
import br.com.clinicaintegrada.endereco.Bairro;
import br.com.clinicaintegrada.endereco.dao.CidadeDao;
import br.com.clinicaintegrada.endereco.dao.EnderecoDao;
import br.com.clinicaintegrada.logSistema.Logger;
import br.com.clinicaintegrada.pessoa.Filial;
import br.com.clinicaintegrada.pessoa.Pessoa;
import br.com.clinicaintegrada.pessoa.PessoaEndereco;
import br.com.clinicaintegrada.pessoa.dao.PessoaEnderecoDao;
import br.com.clinicaintegrada.seguranca.controleUsuario.ChamadaPaginaBean;
import br.com.clinicaintegrada.seguranca.controleUsuario.SessaoCliente;
import br.com.clinicaintegrada.utils.CEPService;
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
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class EnderecoBean implements Serializable {

    private Cidade cidadeBase;
    private Endereco endereco;
    private boolean blDetalhada;
    private boolean pesquisar;
    private boolean btnCadastrar;
    private List<Endereco> listEndereco;
    private List<SelectItem>[] listSelectItem;
    /**
     * <ul>
     * <li>0 - idIndex</li>
     * <li>1 - Cidade</li>
     * <li>2 - Logradouro</li>
     * <li>3 - Estado</li>
     * </ul>
     */
    private Integer[] index;
    private String msgDetalhada;
    private String porPesquisa;

    @PostConstruct
    public void init() {
        endereco = new Endereco();
        cidadeBase = new Cidade();
        msgDetalhada = "";
        blDetalhada = false;
        listEndereco = new ArrayList();
        index = new Integer[4];
        index[0] = -1;
        index[1] = 0;
        index[2] = 0;
        index[3] = 0;
        listSelectItem = new ArrayList[4];
        listSelectItem[0] = new ArrayList<>();
        listSelectItem[1] = new ArrayList<>();
        listSelectItem[2] = new ArrayList<>();
        listSelectItem[3] = new ArrayList<>();
        porPesquisa = "";
        btnCadastrar = false;
        getListCidade();
        getListLogradouro();
    }

    @PreDestroy
    public void destroy() {
        Sessions.remove("enderecoBean");
        Sessions.remove("cidadePesquisa");
        Sessions.remove("bairroPesquisa");
        Sessions.remove("logradouroPesquisa");
        Sessions.remove("descricaoEnderecoPesquisa");
    }

    public void pesquisaCep() {
        porPesquisa = "cep";
        listEndereco.clear();
    }

    public void pesquisaInicial() {
        pesquisar = true;
        porPesquisa = "inicial";
        listEndereco.clear();
    }

    public void pesquisaParcial() {
        pesquisar = true;
        porPesquisa = "parcial";
        listEndereco.clear();
    }

    public synchronized String chamadaEndereco() throws IOException {
        Sessions.remove("enderecoBean");
        if (((ChamadaPaginaBean) Sessions.getObject("chamadaPaginaBean")).getUrlAtual().equals("pesquisaEndereco")) {
            Sessions.put("acessoCadastro", true);
        }
        return ((ChamadaPaginaBean) Sessions.getObject("chamadaPaginaBean")).pagina("endereco");
    }

    public void save() throws Exception {
        DaoInterface di = new Dao();
        EnderecoDao db = new EnderecoDao();
        Logradouro logradouro = (Logradouro) di.find(new Logradouro(), Integer.parseInt(getListLogradouro().get(index[2]).getDescription()));
        endereco.setLogradouro(logradouro);
        if (endereco.getDescricaoEndereco().getId() == -1) {
            Messages.warn("Validação", "O campo Descrição Endereço deve ser preenchido!");
            return;
        }
        if (endereco.getCidade().getId() == -1) {
            Messages.warn("Validação", "O campo Cidade deve ser preenchido!");
            return;
        }
        if (endereco.getBairro().getId() == -1) {
            Messages.warn("Validação", "O campo Bairro deve ser preenchido!");
            return;
        }
        if (endereco.getLogradouro() == null) {
            Messages.warn("Validação", "O campo Logradouro deve ser preenchido!");
            return;
        }
        String cep;
        if (!endereco.getCep().equals("")) {
            cep = endereco.getCep().substring(0, 5);
            cep = cep + endereco.getCep().substring(6, 9);
            endereco.setCep(cep);
        }
        Endereco e;
        if (endereco.getId() == -1) {
            if (SessaoCliente.get().getId() == 1) {
                endereco.setCliente(null);
            } else {
                endereco.setCliente(SessaoCliente.get());
            }
            e = endereco;
        } else {
            e = (Endereco) di.find(new Endereco(), endereco.getId());
        }
        List<Endereco> listend = db.pesquisaEndereco(endereco.getDescricaoEndereco().getId(),
                endereco.getCidade().getId(),
                endereco.getBairro().getId(),
                endereco.getLogradouro().getId());
        if (!listend.isEmpty()) {
            for (int i = 0; i < listend.size(); i++) {
                if (listend.get(i).getCep().equals(endereco.getCep()) && listend.get(i).getFaixa().equals(endereco.getFaixa())) {
                    Messages.warn("Validação", "Endereço já Existente no Sistema!");
                    return;
                }
            }
        }
        Logger log = new Logger();
        di.openTransaction();
        if (endereco.getId() == -1) {
            if (di.save(endereco)) {
                di.commit();
                Messages.info("Sucesso", "Registro inserido");
                log.save(endereco.getId() + " - " + endereco.getLogradouro().getDescricao() + " " + endereco.getDescricaoEndereco().getDescricao() + ", " + endereco.getFaixa() + " - " + endereco.getBairro().getDescricao() + " (" + endereco.getBairro().getId() + ") - " + endereco.getCidade().getCidade() + " (" + endereco.getCidade().getId() + ") - " + endereco.getCidade().getUf());
            } else {
                di.rollback();
                Messages.warn("Erro", "Ao inserir registro!");
            }
        } else {
            if (di.update(endereco)) {
                di.commit();
                String antes = "De: " + e.getId() + " - " + e.getLogradouro().getDescricao() + " " + e.getDescricaoEndereco().getDescricao() + ", " + e.getFaixa() + " - " + e.getBairro().getDescricao() + " (" + e.getBairro().getId() + ") " + e.getCidade().getCidade() + " (" + e.getCidade().getId() + ") - " + e.getCidade().getUf();
                log.update(antes, endereco.getId() + " - " + endereco.getLogradouro().getDescricao() + " " + endereco.getDescricaoEndereco().getDescricao() + ", " + endereco.getFaixa() + " - " + endereco.getBairro().getDescricao() + " (" + endereco.getBairro().getId() + ") " + endereco.getCidade().getCidade() + " (" + endereco.getCidade().getId() + ") - " + endereco.getCidade().getUf());
                Messages.info("Sucesso", "Registro atualizado");
            } else {
                di.rollback();
                Messages.warn("Erro", "Ao atualizar registro!");
            }
        }
        Sessions.put("enderecoPesquisa", endereco);
    }

    public String edit() {
        Endereco e = (Endereco) listEndereco.get(index[0]);
        return edit(e);
    }

    public String edit(Endereco e) {
        endereco = e;
        for (int i = 0; i < (getListLogradouro().size()); i++) {
            if (Integer.parseInt(getListLogradouro().get(i).getDescription()) == endereco.getLogradouro().getId()) {
                index[2] = i;
                break;
            }
        }
        String url = (String) Sessions.getString("urlRetorno");
        Sessions.put("linkClicado", true);
        if (url != null) {
            Sessions.put("enderecoPesquisa", endereco);
            return url;
        }
        return "endereco";
    }

    public void clear() {
        Sessions.remove("enderecoBean");
    }

    public void delete() {
        if (endereco.getId() != -1) {
            DaoInterface di = new Dao();
            di.openTransaction();
            if (di.delete((Endereco) di.find(endereco))) {
                di.commit();
                Messages.info("Sucesso", "Registro excluído");

                Logger log = new Logger();
                log.delete(endereco.getId() + " - " + endereco.getLogradouro().getDescricao() + " " + endereco.getDescricaoEndereco().getDescricao() + ", " + endereco.getFaixa() + " - " + endereco.getBairro().getDescricao() + " (" + endereco.getBairro().getId() + ") " + endereco.getCidade().getCidade() + " (" + endereco.getCidade().getId() + ") - " + endereco.getCidade().getUf());
                clear();
            } else {
                di.rollback();
                Messages.warn("Erro", "Ao excluir registro!");
            }
        }
    }

    public Endereco getEndereco() {
        if (Sessions.exists("simplesPesquisa")) {
            try {
                Bairro bairro = (Bairro) Sessions.getObject("simplesPesquisa");
                endereco.setBairro(bairro);
            } catch (Exception e) {
                DescricaoEndereco descricaoEndereco = (DescricaoEndereco) Sessions.getObject("simplesPesquisa");
                endereco.setDescricaoEndereco(descricaoEndereco);
            }
            Sessions.remove("simplesPesquisa");
        }
        if (Sessions.exists("cidadePesquisa")) {
            endereco.setCidade((Cidade) Sessions.getObject("cidadePesquisa", true));
        }
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public String getMsgDetalhada() {
        return msgDetalhada;
    }

    public void setMsgDetalhada(String msgDetalhada) {
        this.msgDetalhada = msgDetalhada;
    }

    public List<SelectItem> getListLogradouro() {
        if (listSelectItem[2].isEmpty()) {
            Dao dao = new Dao();
            List<Logradouro> logradouros = (List<Logradouro>) dao.list(new Logradouro(), true);
            int j = 0;
            for (int i = 0; i < logradouros.size(); i++) {
                if (logradouros.get(i).getDescricao().toUpperCase().equals("RUA")
                        || logradouros.get(i).getDescricao().toUpperCase().equals("AVENIDA")
                        || logradouros.get(i).getDescricao().toUpperCase().equals("TRAVESSA")
                        || logradouros.get(i).getDescricao().toUpperCase().equals("PRAÇA")
                        || logradouros.get(i).getDescricao().toUpperCase().equals("ALAMEDA")
                        || logradouros.get(i).getDescricao().toUpperCase().equals("RODOVIA")
                        || logradouros.get(i).getDescricao().toUpperCase().equals("ESTRADA")) {
                    listSelectItem[2].add(new SelectItem(j, (String) logradouros.get(i).getDescricao().toUpperCase(), Integer.toString(logradouros.get(i).getId())));
                    if (logradouros.get(i).getDescricao().toUpperCase().equals("RUA")) {
                        index[2] = j;
                    }
                    logradouros.remove(i);
                    j++;
                }
            }
            for (int i = 0; i < logradouros.size(); i++) {
                if (!logradouros.get(i).getDescricao().toUpperCase().equals("RUA")
                        || !logradouros.get(i).getDescricao().toUpperCase().equals("AVENIDA")
                        || !logradouros.get(i).getDescricao().toUpperCase().equals("TRAVESSA")
                        || !logradouros.get(i).getDescricao().toUpperCase().equals("PRAÇA")
                        || !logradouros.get(i).getDescricao().toUpperCase().equals("ALAMEDA")
                        || !logradouros.get(i).getDescricao().toUpperCase().equals("RODOVIA")
                        || !logradouros.get(i).getDescricao().toUpperCase().equals("ESTRADA")) {
                    listSelectItem[2].add(new SelectItem(j, (String) logradouros.get(i).getDescricao(), Integer.toString(logradouros.get(i).getId())));
                    j++;
                }
            }
        }
        return listSelectItem[2];
    }

    public List<SelectItem> getListEstado() {
        if (listSelectItem[3].isEmpty()) {
            listSelectItem[3].add(new SelectItem("AC", "AC", "DF"));
            listSelectItem[3].add(new SelectItem("AL", "AL", "AL"));
            listSelectItem[3].add(new SelectItem("AP", "AP", "AP"));
            listSelectItem[3].add(new SelectItem("AM", "AM", "AM"));
            listSelectItem[3].add(new SelectItem("BA", "BA", "BA"));
            listSelectItem[3].add(new SelectItem("CE", "CE", "CE"));
            listSelectItem[3].add(new SelectItem("DF", "DF", "DF"));
            listSelectItem[3].add(new SelectItem("ES", "ES", "ES"));
            listSelectItem[3].add(new SelectItem("GO", "GO", "GO"));
            listSelectItem[3].add(new SelectItem("MA", "MA", "MA"));
            listSelectItem[3].add(new SelectItem("MT", "MT", "MT"));
            listSelectItem[3].add(new SelectItem("MS", "MS", "MS"));
            listSelectItem[3].add(new SelectItem("MG", "MG", "MG"));
            listSelectItem[3].add(new SelectItem("PA", "PA", "PA"));
            listSelectItem[3].add(new SelectItem("PB", "PB", "PB"));
            listSelectItem[3].add(new SelectItem("PR", "PR", "PR"));
            listSelectItem[3].add(new SelectItem("PE", "PE", "PE"));
            listSelectItem[3].add(new SelectItem("PI", "PI", "PI"));
            listSelectItem[3].add(new SelectItem("RJ", "RJ", "RJ"));
            listSelectItem[3].add(new SelectItem("RN", "RN", "RN"));
            listSelectItem[3].add(new SelectItem("RS", "RS", "RS"));
            listSelectItem[3].add(new SelectItem("RO", "RO", "RO"));
            listSelectItem[3].add(new SelectItem("RR", "RR", "RR"));
            listSelectItem[3].add(new SelectItem("SC", "SC", "SC"));
            listSelectItem[3].add(new SelectItem("SP", "SP", "SP"));
            listSelectItem[3].add(new SelectItem("SE", "SE", "SE"));
            listSelectItem[3].add(new SelectItem("TO", "TO", "TO"));
        }
        return listSelectItem[3];
    }

    public List<SelectItem> getListCidade() {
        if (listSelectItem[1].isEmpty()) {
            PessoaEnderecoDao dbPes = new PessoaEnderecoDao();
            DaoInterface di = new Dao();
            Filial fili = (Filial) di.find(new Filial(), 1);
            if (fili == null) {
                msgDetalhada = "Não existe filial, CRIE uma e "
                        + " vincule o endereço para evitar futuros erros!";
                return new ArrayList();
            }
            if (cidadeBase.getUf().isEmpty()) {
                Pessoa pes = fili.getMatriz().getPessoa();
                List<PessoaEndereco> list = dbPes.pesquisaPessoaEnderecoPorPessoa(pes.getId());
                Cidade cidade;
                if (!list.isEmpty()) {
                    cidade = ((PessoaEndereco) dbPes.pesquisaPessoaEnderecoPorPessoa(pes.getId()).get(0)).getEndereco().getCidade();
                    cidadeBase = cidade;
                } else {
                    cidadeBase = (Cidade) di.find(new Cidade(), 1);
                }
            }

            CidadeDao cidadeDao = new CidadeDao();
            List select = cidadeDao.pesquisaCidadesPorEstado(cidadeBase.getUf());

            for (int i = 0; i < select.size(); i++) {
                listSelectItem[1].add(new SelectItem(i, (String) ((Cidade) select.get(i)).getCidade(), Integer.toString(((Cidade) select.get(i)).getId())));
                if (Integer.parseInt(listSelectItem[1].get(i).getDescription()) == cidadeBase.getId()) {
                    index[1] = i;
                }
            }
        } else {
            DaoInterface di = new Dao();
            cidadeBase = (Cidade) di.find(new Cidade(), Integer.parseInt(listSelectItem[1].get(index[1]).getDescription()));
            for (int i = 0; i < listSelectItem[1].size(); i++) {
                if (Integer.parseInt(listSelectItem[1].get(i).getDescription()) == cidadeBase.getId()) {
                    index[1] = i;
                }
            }
        }
        return listSelectItem[1];
    }

    public List<Endereco> getListEndereco() {
        if (listEndereco.isEmpty()) {
            EnderecoDao enderecoDao = new EnderecoDao();
            Dao dao = new Dao();
            if (porPesquisa.equals("cep")) {
                listEndereco = enderecoDao.pesquisaEnderecoPorCep(endereco.getCep());
                if (listEndereco.isEmpty()) {
                    CEPService cEPService = new CEPService();
                    cEPService.setCep(endereco.getCep());
                    cEPService.procurar();
                    listEndereco = enderecoDao.pesquisaEnderecoPorCep(endereco.getCep());
                }
            } else if (porPesquisa.equals("inicial") && pesquisar) {
                listEndereco = enderecoDao.pesquisaEndereco(cidadeBase.getUf(),
                        ((Cidade) dao.find(new Cidade(), Integer.parseInt(getListCidade().get(index[1]).getDescription()))).getCidade(),
                        ((Logradouro) dao.find(new Logradouro(), Integer.parseInt(getListLogradouro().get(index[2]).getDescription()))).getDescricao(),
                        endereco.getDescricaoEndereco().getDescricao(), "I");
                pesquisar = false;
            } else if (porPesquisa.equals("parcial") && pesquisar) {
                listEndereco = enderecoDao.pesquisaEndereco(cidadeBase.getUf(),
                        ((Cidade) dao.find(new Cidade(), Integer.parseInt(getListCidade().get(index[1]).getDescription()))).getCidade(),
                        ((Logradouro) dao.find(new Logradouro(), Integer.parseInt(getListLogradouro().get(index[2]).getDescription()))).getDescricao(),
                        endereco.getDescricaoEndereco().getDescricao(), "P");
                pesquisar = false;
            }
        }
        return listEndereco;
    }

    public void setListEndereco(List<Endereco> listEndereco) {
        this.listEndereco = listEndereco;
    }

    public boolean isBlDetalhada() {
        if (msgDetalhada.isEmpty()) {
            blDetalhada = false;
        } else {
            blDetalhada = true;
        }
        return blDetalhada;
    }

    public void setBlDetalhada(boolean blDetalhada) {
        this.blDetalhada = blDetalhada;
    }

    public String getPorPesquisa() {
        return porPesquisa;
    }

    public void setPorPesquisa(String porPesquisa) {
        this.porPesquisa = porPesquisa;
    }

    public Cidade getCidadeBase() {
        if (!pesquisar) {
            getListCidade().clear();
            index[1] = 0;
        }
        return cidadeBase;
    }

    public void setCidadeBase(Cidade cidadeBase) {
        this.cidadeBase = cidadeBase;
    }

    public void listenerCadastrar() {
        Sessions.put("cadastrarEndereco", true);
    }

    public boolean isBtnCadastrar() {
        if (Sessions.exists("cadastrarEndereco")) {
            Sessions.remove("cadastrarEndereco");
            btnCadastrar = true;
        }
        return btnCadastrar;
    }

    public void setBtnCadastrar(boolean btnCadastrar) {
        this.btnCadastrar = btnCadastrar;
    }

    public String btnPessoaJuridica() {
        Sessions.put("linkClicado", true);
        return "pessoaJuridica";
    }

    public Integer[] getIndex() {
        return index;
    }

    public void setIndex(Integer[] index) {
        this.index = index;
    }
}
