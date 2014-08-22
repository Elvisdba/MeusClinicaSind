package br.com.clinicaintegrada.pessoa.beans;

import br.com.clinicaintegrada.utils.ValidDocuments;
import br.com.clinicaintegrada.utils.Diretorio;
import br.com.clinicaintegrada.utils.Mask;
import br.com.clinicaintegrada.utils.Dao;
import br.com.clinicaintegrada.utils.Sessions;
import br.com.clinicaintegrada.utils.Upload;
import br.com.clinicaintegrada.utils.PhotoCam;
import br.com.clinicaintegrada.utils.PF;
import br.com.clinicaintegrada.utils.AnaliseString;
import br.com.clinicaintegrada.utils.DataHoje;
import br.com.clinicaintegrada.utils.Messages;
import br.com.clinicaintegrada.pessoa.dao.PessoaProfissaoDao;
import br.com.clinicaintegrada.pessoa.dao.PessoaEmpresaDao;
import br.com.clinicaintegrada.pessoa.dao.PessoaEnderecoDao;
import br.com.clinicaintegrada.pessoa.dao.DependenteDao;
import br.com.clinicaintegrada.pessoa.dao.FisicaDao;
import br.com.clinicaintegrada.pessoa.Dependente;
import br.com.clinicaintegrada.pessoa.Juridica;
import br.com.clinicaintegrada.pessoa.PessoaEndereco;
import br.com.clinicaintegrada.pessoa.TipoDocumento;
import br.com.clinicaintegrada.pessoa.PessoaEmpresa;
import br.com.clinicaintegrada.pessoa.Filial;
import br.com.clinicaintegrada.pessoa.TipoEndereco;
import br.com.clinicaintegrada.pessoa.TipoCadastro;
import br.com.clinicaintegrada.pessoa.GrauParentesco;
import br.com.clinicaintegrada.pessoa.Fisica;
import br.com.clinicaintegrada.pessoa.PessoaProfissao;
import br.com.clinicaintegrada.pessoa.Profissao;
import br.com.clinicaintegrada.pessoa.Pessoa;
import br.com.clinicaintegrada.endereco.Cidade;
import br.com.clinicaintegrada.endereco.Endereco;
import br.com.clinicaintegrada.logSistema.Logger;
import br.com.clinicaintegrada.pessoa.list.ListFisicaPessoaEmpresa;
import br.com.clinicaintegrada.seguranca.Usuario;
import br.com.clinicaintegrada.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.clinicaintegrada.seguranca.controleUsuario.SessaoCliente;
import br.com.clinicaintegrada.sistema.ConfiguracaoUpload;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;
import javax.servlet.ServletContext;
import javax.servlet.http.Part;
import org.primefaces.component.accordionpanel.AccordionPanel;
import org.primefaces.component.tabview.TabView;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CaptureEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.TabChangeEvent;

@ManagedBean
@SessionScoped
public class FisicaBean extends PesquisarProfissaoBean implements Serializable {

    private Fisica fisica;
    private Usuario usuario;
    private Endereco enderecox;
    private Dependente dependente;
    private PessoaEndereco pessoaEndereco;
    private PessoaEmpresa pessoaEmpresa;
    private PessoaProfissao pessoaProfissao;
    private Part file;
    private String indicaTab;
    private String enderecoCompleto;
    private String descPesquisa;
    private String porPesquisa;
    private String comoPesquisa;
    private String masc;
    private String maxl;
    private String enderecoCobranca;
    private String renAbreEnd;
    private String msgSocio;
    private String lblSocio;
    private String pesquisaPor;
    private String tipo;
    private String tipoSocio;
    private String indexNovoEndereco;
    private String fotoPerfil;
    private String fotoArquivo;
    private String fotoTempPerfil;
    private String fotoTempArquivo;
    private String cliente;
    private String numero;
    private String complemento;
    private String fileContent;
    private String[] imagensTipo;
    private List itens;
    private List<ListFisicaPessoaEmpresa> listPessoa;
    private List<Fisica> listPessoaFisica;
    private List<PessoaEndereco> listPessoaEndereco;
    private List<PessoaEmpresa> listPessoaEmpresa;
    private List<Dependente> listDependente;
    private List<SelectItem> listProfissoes;
    private List<SelectItem> listTipoCadastro;
    private List<SelectItem> listGrauParentesco;
    private int idPais;
    private int idProfissao;
    private int idTipoCadastro;
    private int idIndexEndereco;
    private int idIndexFisica;
    private int idIndexPessoaEmp;
    private int indexPessoaFisica;
    private int index_endereco;
    private int idGrauParentesco;
    private boolean alterarEnd;
    private boolean endResidencial;
    private boolean fotoTemp;
    private boolean renderJuridicaPesquisa;
    private boolean readyOnlineNaturalidade;
    private boolean disabledNaturalidade;
    private boolean visibleEditarEndereco;
    private boolean chkDependente;

    @PostConstruct
    public void init() {
        fisica = new Fisica();
        usuario = new Usuario();
        enderecox = new Endereco();
        pessoaEndereco = new PessoaEndereco();
        pessoaProfissao = new PessoaProfissao();
        pessoaEmpresa = new PessoaEmpresa();
        dependente = new Dependente();
        dependente.setNascimentoString("");
        file = null;
        indicaTab = "pessoal";
        enderecoCompleto = "";
        descPesquisa = "";
        porPesquisa = "nome";
        comoPesquisa = "";
        masc = "";
        maxl = "";
        enderecoCobranca = "";
        renAbreEnd = "true";
        msgSocio = "";
        lblSocio = "";
        pesquisaPor = "";
        tipo = "";
        tipoSocio = "";
        indexNovoEndereco = "";
        fotoPerfil = "";
        fotoArquivo = "";
        fotoTempPerfil = "";
        fotoTempArquivo = "";
        cliente = "";
        numero = "";
        complemento = "";
        fileContent = "";
        imagensTipo = new String[]{"jpg", "jpeg", "png", "gif"};
        itens = new ArrayList();
        listDependente = new ArrayList<>();
        listPessoa = new ArrayList<>();
        listPessoaFisica = new ArrayList<>();
        listPessoaEmpresa = new ArrayList<>();
        listPessoaEndereco = new ArrayList<>();
        listProfissoes = new ArrayList<>();
        listTipoCadastro = new ArrayList<>();
        listGrauParentesco = new ArrayList<>();
        idPais = 11;
        idProfissao = 0;
        idTipoCadastro = 0;
        idIndexEndereco = 0;
        idIndexFisica = 0;
        idIndexPessoaEmp = 0;
        indexPessoaFisica = 0;
        index_endereco = 0;
        idGrauParentesco = 0;
        alterarEnd = false;
        endResidencial = false;
        fotoTemp = false;
        renderJuridicaPesquisa = false;
        readyOnlineNaturalidade = true;
        disabledNaturalidade = false;
        visibleEditarEndereco = false;
        chkDependente = false;

    }

    @PreDestroy
    public void destroy() {
        Sessions.remove("fisicaBean");
        Sessions.remove("fisicaPesquisa");
        Sessions.remove("juridicaPesquisa");
        Sessions.remove("enderecoPesquisa");

    }

    public void clear() {
        Sessions.remove("fisicaBean");
    }

    public String getEnderecoCobranca() {
        for (PessoaEndereco pe : listPessoaEndereco) {
            String complementoString = " ";
            if (pe.getTipoEndereco().getId() == 3) {
                if (!pe.getComplemento().isEmpty()) {
                    complementoString = " ( " + pe.getComplemento() + " ) ";
                }

                return enderecoCobranca = pe.getEndereco().getLogradouro().getDescricao() + " "
                        + pe.getEndereco().getDescricaoEndereco().getDescricao() + ", " + pe.getNumero() + " " + pe.getEndereco().getBairro().getDescricao() + ","
                        + complementoString + pe.getEndereco().getCidade().getCidade() + " - " + pe.getEndereco().getCidade().getUf() + " - " + AnaliseString.mascaraCep(pe.getEndereco().getCep());
            }
        }
        return enderecoCobranca;
    }

    public void save() {
        Logger logger = new Logger();
        FisicaDao db = new FisicaDao();
        Pessoa pessoa = fisica.getPessoa();
        List listDocumento;
        if ((listPessoaEndereco.isEmpty() || pessoa.getId() == -1) && enderecox.getId() != -1) {
            adicionarEnderecos();
        }
        boolean sucesso = false;
        Dao dao = new Dao();
        if (fisica.getPessoa().getNome().equals("")) {
            Messages.warn("Validação", "Informar nome da pessoa!");
            return;
        }
        if (fisica.getSexo().equals("")) {
            Messages.warn("Validação", "Informar o sexo!");
            return;
        }
        if (fisica.getNascimento().isEmpty() || fisica.getNascimento().length() < 10) {
            Messages.warn("Validação", "Data de nascimento esta inválida!");
            return;
        } else {
            if (!DataHoje.isDataValida(fisica.getNascimento())) {
                Messages.warn("Validação", "Data de nascimento esta inválida!");
                return;
            }
        }
        //fisica.setTipoCadastro((TipoCadastro) dao.find(new TipoCadastro(), Integer.parseInt(getListTipoCadastro().get(getIdTipoCadastro()).getDescription())));
        fisica.setTipoCadastro(null);
        if ((fisica.getPessoa().getId() == -1) && (fisica.getId() == -1)) {
            fisica.getPessoa().setTipoDocumento((TipoDocumento) dao.find(new TipoDocumento(), 1));
            if (!db.pesquisaFisicaPorNomeNascimentoRG(fisica.getPessoa().getNome(), fisica.getDtNascimento(), fisica.getRg()).isEmpty()) {
                Messages.warn("Validação", "Esta pessoa já esta cadastrada!");
                return;
            }
            if (pessoa.getDocumento().equals("") || pessoa.getDocumento().equals("0")) {
                pessoa.setDocumento("0");
            } else {
                if (!ValidDocuments.isValidoCPF(AnaliseString.extrairNumeros(fisica.getPessoa().getDocumento()))) {
                    Messages.warn("Validação", "Documento Invalido!");
                    return;
                }
                listDocumento = db.pesquisaFisicaPorDocumento(fisica.getPessoa().getDocumento());
                if (!listDocumento.isEmpty()) {
                    Messages.warn("Validação", "Documento já existente!");
                    return;
                }
            }
            dao.openTransaction();
            fisica.getPessoa().setCliente(SessaoCliente.get());
            if (dao.save(pessoa)) {
                fisica.setNacionalidade(getListaPaises().get(idPais).getLabel());
                fisica.setPessoa(pessoa);
                if (dao.save(fisica)) {
                    Sessions.put("fisicaPesquisa", fisica);
                    Messages.info("Sucesso", "Cadastro inserido");
                    logger.save("ID " + fisica.getId()
                            + " - Pessoa: " + fisica.getPessoa().getId()
                            + " - Nome: " + fisica.getPessoa().getNome()
                            + " - Nascimento: " + fisica.getNascimento()
                            + " - CPF: " + fisica.getPessoa().getDocumento()
                            + " - RG: " + fisica.getRg());
                    dao.commit();
                    sucesso = true;
                } else {
                    dao.rollback();
                    Messages.warn("Erro", "Erro ao salvar cadastro: Pessoa Física");
                }
            } else {
                dao.rollback();
                Messages.warn("Erro", "Erro ao salvar cadastro: Pessoa");
            }
        } else {
            Fisica f = (Fisica) dao.find(new Fisica(), fisica.getId());
            String beforeUpdate = " De: ID - " + fisica.getId()
                    + " Nome: " + f.getPessoa().getNome() + " - "
                    + " Nascimento: " + f.getNascimento() + " - "
                    + " CPF: " + f.getPessoa().getDocumento() + " - "
                    + " RG: " + f.getRg();
            if (fisica.getPessoa().getDocumento().equals("") || fisica.getPessoa().getDocumento().equals("0")) {
                fisica.getPessoa().setDocumento("0");
            } else {
                if (!ValidDocuments.isValidoCPF(AnaliseString.extrairNumeros(fisica.getPessoa().getDocumento()))) {
                    Messages.warn("Validação", "Documento inválido!");
                    return;
                }
                listDocumento = db.pesquisaFisicaPorDocumento(fisica.getPessoa().getDocumento());
                for (Object listDocumento1 : listDocumento) {
                    if (!listDocumento.isEmpty() && ((Fisica) listDocumento1).getId() != fisica.getId()) {
                        Messages.warn("Validação", "Documento já existente!");
                        return;
                    }
                }
            }
            List<Fisica> list = db.pesquisaFisicaPorNomeNascimentoRG(fisica.getPessoa().getNome(), fisica.getDtNascimento(), fisica.getRg());
            if (!list.isEmpty()) {
                for (Fisica f1 : list) {
                    if (f1.getId() != fisica.getId()) {
                        Messages.warn("Validação", "Esta pessoa já esta cadastrada!");
                        return;
                    }
                }
            }
            fisica.setNacionalidade(getListaPaises().get(idPais).getLabel());
            dao.openTransaction();
            if (dao.update(fisica.getPessoa())) {
                logger.update(beforeUpdate,
                        " Nome: " + fisica.getPessoa().getNome() + " - "
                        + " Nascimento: " + f.getNascimento() + " - "
                        + " CPF: " + fisica.getPessoa().getDocumento() + " - "
                        + " RG: " + fisica.getRg());
            } else {
                dao.rollback();
                return;
            }

            if (dao.update(fisica)) {
                Sessions.put("fisicaPesquisa", fisica);
                Messages.info("Sucesso", "Cadastro atualizado");
                sucesso = true;
                dao.commit();
            } else {
                dao.rollback();
                Messages.warn("Erro", "Erro ao atualizar cadastro: Pessoa Física");
            }
        }
        savePessoaEndereco();
        savePessoaEmpresa();
        savePessoaProfissao();
        saveDependentes();
        limparCamposData();
        if (sucesso) {
            salvarImagem();
        }
    }

    public void saveDependentes() {
        if (fisica.getId() != -1 && !listDependente.isEmpty() && listDependente.get(0).getPessoa().getId() == -1) {
            Dao dao = new Dao();
            for (Dependente d : listDependente) {
                d.setPessoa(fisica.getPessoa());
                dao.save(d, true);
            }
            listDependente.clear();
        }
    }

    public void savePessoaEndereco() {
        Dao dao = new Dao();
        if (fisica.getId() != -1) {
            if (!listPessoaEndereco.isEmpty()) {
                dao.openTransaction();
                for (PessoaEndereco pe : listPessoaEndereco) {
                    if (pe.getId() == -1) {
                        if (!dao.save(pe)) {
                            Messages.warn("Erro", "Não foi possivel SALVAR endereço!");
                            dao.rollback();
                            return;
                        }
                    } else {
                        if (!dao.update(pe)) {
                            Messages.warn("Erro", "Não foi possivel ALTERAR endereço!");
                            dao.rollback();
                            return;
                        }

                    }
                }
                dao.commit();
            }

        }
    }

    public void savePessoaProfissao() {
        Dao dao = new Dao();
        if (!listProfissoes.isEmpty()) {
            pessoaProfissao.setProfissao((Profissao) dao.find((Profissao) new Profissao(), Integer.parseInt(listProfissoes.get(idProfissao).getDescription())));
            dao.openTransaction();
            if (fisica.getId() == -1) {
                pessoaProfissao = new PessoaProfissao();
                pessoaProfissao.setFisica(fisica);
                if (dao.save(pessoaProfissao)) {
                    dao.commit();
                } else {
                    dao.rollback();
                }
            } else {
                if (pessoaProfissao.getId() == -1) {
                    pessoaProfissao.setFisica(fisica);
                    if (!dao.save(pessoaProfissao)) {
                        dao.rollback();
                        return;
                    }
                } else {
                    if (!dao.update(pessoaProfissao)) {
                        dao.rollback();
                        return;
                    }
                }
                dao.commit();
            }
        }
    }

    public void limpaFoto() {
        FacesContext context = FacesContext.getCurrentInstance();
        File fExiste = new File(((ServletContext) context.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/Fotos/fotoTemp.jpg"));
        if (fExiste.exists()) {
            fExiste.delete();
        }
    }

    public String novoOK() {
        if (fisica.getId() == -1) {
            Sessions.put("fisicaBean", new FisicaBean());
        }
        return "pessoaFisica";
    }

    public void delete() {
        if (fisica.getId() != -1) {
            PessoaEnderecoDao peDao = new PessoaEnderecoDao();
            List<PessoaEndereco> listaEndereco = peDao.pesquisaPessoaEnderecoPorPessoa(fisica.getPessoa().getId());
            Dao dao = new Dao();
            dao.openTransaction();
            // EXCLUI ENDEREÇO -----------------
            if (!listaEndereco.isEmpty()) {
                for (PessoaEndereco listaEndereco1 : listaEndereco) {
                    if (!dao.delete((PessoaEndereco) dao.find(new PessoaEndereco(), listaEndereco1.getId()))) {
                        dao.rollback();
                        Messages.warn("Erro", "Não foi possivel excluir o(s) endereço(s)!");
                        return;
                    }
                }
            }
            PessoaProfissaoDao daoPP = new PessoaProfissaoDao();
            PessoaProfissao pp = daoPP.pesquisaPessoaProfissaoPorFisica(fisica.getId());
            // EXCLUI PROFISSÃO -----------------
            if (pp.getId() != -1) {
                if (!dao.delete((PessoaProfissao) dao.find(new PessoaProfissao(), pp.getId()))) {
                    dao.rollback();
                    Messages.warn("Erro", "Erro ao excluir profissão!");
                    return;
                }
            }
            // EXCLUI PESSOA EMPRESA ------------
            PessoaEmpresaDao pessoaEmpresaDao = new PessoaEmpresaDao();
            List<PessoaEmpresa> listaPessoaEmp = pessoaEmpresaDao.listaEmpresasPorFisica(fisica.getId());
            if (!listaPessoaEmp.isEmpty()) {
                for (PessoaEmpresa listaPessoaEmp1 : listaPessoaEmp) {
                    if (!dao.delete(listaPessoaEmp1)) {
                        dao.rollback();
                        Messages.warn("Erro", "Erro ao pessoas empresa!");
                        return;
                    }
                }
            }
            if (!dao.delete(fisica)) {
                dao.rollback();
                Messages.warn("Erro", "Física não pode ser excluída!");
                return;
            }
            for (int i = 0; i < listDependente.size(); i++) {
                if (listDependente.get(i).getId() == -1) {
                    listDependente.remove(i);
                } else {
                    if (!dao.delete(listDependente.get(i))) {
                        Messages.warn("Erro", "Ao remover dependente!");
                        dao.rollback();
                        return;
                    }
                }

                break;
            }
            if (!dao.delete(fisica.getPessoa())) {
                dao.rollback();
                Messages.warn("Erro", "Cadastro Pessoa não pode ser excluída!");
                return;
            }
            dao.commit();
            removeImagem();
            Logger logger = new Logger();
            logger.delete("ID: " + fisica.getId() + " - Pessoa: " + fisica.getPessoa().getId() + " - Nascimento: " + fisica.getNascimento() + " - Nome: " + fisica.getPessoa().getNome() + " - CPF: " + fisica.getPessoa().getDocumento() + " - RG: " + fisica.getRg());
            clear();
            clearDependente();
            listDependente.clear();
            Messages.info("Sucesso", "Registro excluído");
        } else {
            Messages.warn("Validação", "Pesquise uma pessoa física para ser excluída!");
        }
    }

    public String edit(Fisica f) {
        if (fisica.getId() != f.getId()) {
            FacesContext context = FacesContext.getCurrentInstance();
            File fExiste = new File(((ServletContext) context.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/Fotos/fotoTemp.jpg"));
            if (fExiste.exists()) {
                fExiste.delete();
            }
        }
        PessoaEmpresaDao peDao = new PessoaEmpresaDao();
        PessoaProfissaoDao ppDao = new PessoaProfissaoDao();
        fisica = f;
        Sessions.put("fisicaPesquisa", fisica);
        String url = (String) Sessions.getString("urlRetorno");
        descPesquisa = "";
        porPesquisa = "nome";
        comoPesquisa = "";
        alterarEnd = true;
        listPessoa = new ArrayList();
        msgSocio = "";
        pessoaEmpresa = (PessoaEmpresa) peDao.pesquisaPessoaEmpresaPorFisica(fisica.getId());
        if (pessoaEmpresa == null) {
            pessoaEmpresa = new PessoaEmpresa();
        }
        if (pessoaEmpresa.getId() != -1) {
            profissao = pessoaEmpresa.getFuncao();
            Sessions.put("juridicaPesquisa", pessoaEmpresa.getJuridica());
            renderJuridicaPesquisa = true;
        } else {
            Sessions.remove("juridicaPesquisa");
            profissao = new Profissao();
            renderJuridicaPesquisa = false;
        }

        pessoaProfissao = ppDao.pesquisaPessoaProfissaoPorFisica(fisica.getId());
        if (pessoaProfissao.getId() != -1) {
            for (int i = 0; i < listProfissoes.size(); i++) {
                if (Integer.valueOf(listProfissoes.get(i).getDescription()) == pessoaProfissao.getProfissao().getId()) {
                    idProfissao = i;
                    break;
                }
            }
        }
        if (fisica.getTipoCadastro() != null) {
            for (int i = 0; i < listTipoCadastro.size(); i++) {
                if (Integer.valueOf(listTipoCadastro.get(i).getDescription()) == fisica.getTipoCadastro().getId()) {
                    idTipoCadastro = i;
                    break;
                }
            }
        }
        showImagemFisica();
        Sessions.put("linkClicado", true);
        listDependente.clear();
        return url;
    }

    public void showImagemFisica() {
        String caminhoTemp = "/Cliente/" + getCliente() + "/Imagens/Fotos/";
        String arquivo = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath(caminhoTemp);
        for (String imagensTipo1 : imagensTipo) {
            File f = new File(arquivo + "/" + fisica.getPessoa().getId() + "." + imagensTipo1);
            if (f.exists()) {
                fotoPerfil = caminhoTemp + "/" + fisica.getPessoa().getId() + "." + imagensTipo1;
                fotoTempPerfil = "";
                break;
            } else {
                fotoPerfil = "";
                fotoTempPerfil = "";
            }
        }
    }

    public void existePessoaDocumento() {
        if (!fisica.getPessoa().getDocumento().isEmpty() && !fisica.getPessoa().getDocumento().equals("___.___.___-__") && fisica.getId() == -1) {
            if (!ValidDocuments.isValidoCPF(AnaliseString.extrairNumeros(fisica.getPessoa().getDocumento()))) {
                Messages.warn("Validação", "Documento (CPF) inválido! " + fisica.getPessoa().getDocumento());
                PF.update("form_pessoa_fisica:i_tabview_fisica:id_valida_documento: " + fisica.getPessoa().getDocumento());
                fisica.getPessoa().setDocumento("");
                return;
            }
            FisicaDao db = new FisicaDao();
            List lista = db.pesquisaFisicaPorDocumento(fisica.getPessoa().getDocumento());
            if (!lista.isEmpty()) {
                String x = editarFisicaParametro((Fisica) lista.get(0));
                PF.update("form_pessoa_fisica:i_panel_pessoa_fisica");
                showImagemFisica();
            }
        }
    }

    public String editarFisicaParametro(Fisica fis) {
        PessoaEmpresaDao peDao = new PessoaEmpresaDao();
        fisica = fis;
        Sessions.put("fisicaPesquisa", fisica);
        String url = (String) Sessions.getString("urlRetorno");
        descPesquisa = "";
        porPesquisa = "nome";
        comoPesquisa = "";
        alterarEnd = true;
        pessoaEmpresa = (PessoaEmpresa) peDao.pesquisaPessoaEmpresaPorFisica(fisica.getId());
        if (pessoaEmpresa == null) {
            pessoaEmpresa = new PessoaEmpresa();
        }
        if (pessoaEmpresa.getId() != -1) {
            profissao = pessoaEmpresa.getFuncao();
            Sessions.put("juridicaPesquisa", pessoaEmpresa.getJuridica());
            renderJuridicaPesquisa = true;
        } else {
            Sessions.remove("juridicaPesquisa");
            profissao = new Profissao();
            renderJuridicaPesquisa = false;
        }
        Sessions.put("linkClicado", true);
        return url;
    }

    /**
     * TIPO ENDERECO PESSOA FÍSICA {1,3,4}
     */
    public void alterarEndereco() {
        visibleEditarEndereco = false;
        enderecox = new Endereco();
        for (PessoaEndereco pe : listPessoaEndereco) {

        }
    }

    public void adicionarEnderecos() {
        Dao dao = new Dao();
        List<TipoEndereco> tipoEnderecos = (List<TipoEndereco>) dao.find("TipoEndereco", new int[]{1, 3, 4});
        if (enderecox.getId() != -1) {
            listPessoaEndereco.clear();
            for (TipoEndereco tipoEndereco : tipoEnderecos) {
                listPessoaEndereco.add(new PessoaEndereco(-1, enderecox, tipoEndereco, fisica.getPessoa(), numero, complemento));
            }
        }

        enderecox = new Endereco();
        if (1 == 1) {
            return;
        }
        // COMPARA ENDERECOS
        // comparaEndereco(pessoaEndeAnt, (PessoaEndereco) listaEnd.get(u))
        Endereco endereco = new Endereco();

        //GenericaSessions.put("enderecoNum", pessoaEndereco.getNumero());
        //GenericaSessions.put("enderecoComp", pessoaEndereco.getComplemento());
    }

    public void editarPessoaEndereco(PessoaEndereco pessoaEnderecox, int index) {
        pessoaEndereco = pessoaEnderecox;
        visibleEditarEndereco = true;
    }

    public String deletePessoaEndereco() {
        if (pessoaEndereco.getId() != -1) {
            Dao dao = new Dao();
            if (dao.delete(pessoaEndereco, true)) {
                pessoaEndereco = new PessoaEndereco();
                setEnderecoCompleto("");
            }
        }
        return "pessoaFisica";
    }

    public void savePessoaEmpresa() {
        Dao dao = new Dao();
        if (fisica.getId() != -1 && pessoaEmpresa.getJuridica().getId() != -1) {
            pessoaEmpresa.setFisica(fisica);
            pessoaEmpresa.setAvisoTrabalhado(false);
            if (pessoaEmpresa.getDtAdmissao() != null && pessoaEmpresa.getDtDemissao() != null) {
                int dataAdmissao = DataHoje.converteDataParaInteger(pessoaEmpresa.getAdmissao());
                int dataDemissao = DataHoje.converteDataParaInteger(pessoaEmpresa.getDemissao());
                if (dataDemissao <= dataAdmissao) {
                    Messages.warn("Validação", "Data de demissão deve ser maior que data de admissão!");
                    pessoaEmpresa.setDemissao(null);
                    return;
                }
            }
            if (pessoaEmpresa.getDtAdmissao() == null) {
                Messages.warn("Validação", "Informar data de admissão!");
                return;
            }
            if (profissao.getProfissao().equals("") || profissao.getProfissao() == null) {
                Messages.warn("Validação", "Pesquise uma função!");
                return;
            }
            pessoaEmpresa.setFuncao(profissao);
            if (pessoaEmpresa.getDemissao() != null && !pessoaEmpresa.getDemissao().equals("")) {
                if (DataHoje.converteDataParaInteger(pessoaEmpresa.getDemissao())
                        > DataHoje.converteDataParaInteger(DataHoje.data())) {
                    Messages.warn("Validação", "Data de Demissão maior que atual!");
                    return;
                }
            }
            if (pessoaEmpresa.getId() == -1) {
                dao.save(pessoaEmpresa, true);
            } else {
                dao.update(pessoaEmpresa, true);
            }
            if (pessoaEmpresa.getDemissao() != null && !pessoaEmpresa.getDemissao().equals("")) {
                pessoaEmpresa = new PessoaEmpresa();
                profissao = new Profissao();
                Sessions.remove("juridicaPesquisa");
                renderJuridicaPesquisa = false;
            }
        }
    }

    public void acaoPesquisaInicial() {
        comoPesquisa = "I";
        listPessoa.clear();
        listPessoaFisica.clear();
    }

    public void acaoPesquisaParcial() {
        comoPesquisa = "P";
        listPessoa.clear();
        listPessoaFisica.clear();
    }

    public List<ListFisicaPessoaEmpresa> getListPessoa() {
        if (listPessoa.isEmpty()) {
            FisicaDao db = new FisicaDao();
            PessoaEmpresaDao dbEmp = new PessoaEmpresaDao();
            List<Fisica> list = db.pesquisaPessoaFisica(descPesquisa, porPesquisa, comoPesquisa);
            for (Fisica f : list) {
                listPessoa.add(new ListFisicaPessoaEmpresa(f, (PessoaEmpresa) dbEmp.pesquisaPessoaEmpresaPorFisica(f.getId())));
            }
        }
        return listPessoa;
    }

    public void setListPessoa(List<ListFisicaPessoaEmpresa> listPessoa) {
        this.listPessoa = listPessoa;
    }

    public String getMask() {
        String mask = porPesquisa;
        if (mask.equals("documento")) {
            mask = "cpf";
        }
        return Mask.getMascaraPesquisa(mask, true);
    }

    public String getColocarMaxlenghtPesquisa() {
        maxl = "50";
        if (porPesquisa.equals("cpf")) {
            maxl = "14";
        }
        return maxl;
    }

    public boolean comparaEndereco(PessoaEndereco pessoaEnde1, PessoaEndereco pessoaEnde2) {
        boolean compara;
        if (pessoaEnde1 != null && pessoaEnde2 != null) {
            compara = (pessoaEnde1.getEndereco().getId() == pessoaEnde2.getEndereco().getId()
                    && pessoaEnde1.getNumero().equals(pessoaEnde2.getNumero())
                    && pessoaEnde1.getComplemento().equals(pessoaEnde2.getComplemento()));
        } else {
            compara = false;
        }
        return compara;
    }

    public List<SelectItem> getListaPaises() {
        // String[] lista = new String[]{};
        String[] lista = new String[]{
            "Africana(o)",
            "Afegã(o)",
            "Alemã(o)",
            "Americana(o)",
            "Angolana(o)",
            "Argelina(o)",
            "Argentina(o)",
            "Asiática(o)",
            "Australiana(o)",
            "Belga(o)",
            "Boliviana(o)",
            "Brasileira(o)",
            "Canadense(o)",
            "Canadiana(o)",
            "Chilena(o)",
            "Chinesa(o)",
            "Colombiana(o)",
            "Cubana(o)",
            "Da Nova Zelândia(o)",
            "Dinamarquesa(o)",
            "Egípcia(o)",
            "Equatoriana(o)",
            "Espanha(o)",
            "Espanhola(o)",
            "Europeu(o)",
            "Finlandesa(o)",
            "Francesa(o)",
            "Grega(o)",
            "Haitiana(o)",
            "Holandesa(o)",
            "Hondurenha(o)",
            "Hungara(o)",
            "Indiana(o)",
            "Inglesa(o)",
            "Iraneana(o)",
            "Iraquiana(o)",
            "Italiana(o)",
            "Jamaicana(o)",
            "Japonesa(o)",
            "Marroquina(o)",
            "Mexicana(o)",
            "Norte Americana(o)",
            "Norueguesa(o)",
            "Paquistanesa(o)",
            "Paraguaia(o)",
            "Peruana(o)",
            "Polaca(o)",
            "Portuguesa(o)",
            "Queniana(o)",
            "Russa(o)",
            "Sueca(o)",
            "Suiça(o)",
            "Sul-Africana(o)",
            "Sul-Coreana(o)",
            "Turca(o)",
            "Uraguaia(o)",
            "Venezuelana(o)"};
        List<SelectItem> selectPais = new ArrayList<>();
        int i = 0;
        while (i < lista.length) {
            selectPais.add(new SelectItem(i, lista[i], String.valueOf(i)));
            i++;
        }
        if (fisica.getId() != -1) {
            for (i = 0; i < selectPais.size(); i++) {
                if (selectPais.get(i).getLabel().equals(fisica.getNacionalidade())) {
                    idPais = i;
                }
            }
        }
        return selectPais;
    }

    public List<SelectItem> getListProfissoes() {
        listProfissoes.clear();
        //if (listProfissoes.isEmpty()) {
        Dao dao = new Dao();
        List<Profissao> lista = (List<Profissao>) dao.list(new Profissao(), true);
        for (int i = 0; i < lista.size(); i++) {
            listProfissoes.add(new SelectItem(i, lista.get(i).getProfissao(), "" + lista.get(i).getId()));
        }
        //}
        return listProfissoes;
    }

    public List<SelectItem> getListTipoCadastro() {
        if (listTipoCadastro.isEmpty()) {
            Dao dao = new Dao();
            List<TipoCadastro> lista = (List<TipoCadastro>) dao.list(new TipoCadastro(), true);
            for (int i = 0; i < lista.size(); i++) {
                listTipoCadastro.add(new SelectItem(i, lista.get(i).getDescricao(), "" + lista.get(i).getId()));
            }
        }
        return listTipoCadastro;
    }

    public void setListTipoCadastro(List<SelectItem> listTipoCadastro) {
        this.listTipoCadastro = listTipoCadastro;
    }

    public String getCidadeNaturalidade() {
        String nat;
        if (idPais != 11) {
            readyOnlineNaturalidade = false;
            disabledNaturalidade = true;
            nat = "";
            return nat;
        } else {
            readyOnlineNaturalidade = true;
            disabledNaturalidade = false;
        }
        Cidade cidade;
        if (Sessions.exists("cidadePesquisa")) {
            cidade = (Cidade) Sessions.getObject("cidadePesquisa", true);
            nat = cidade.getCidade();
            nat = nat + " - " + cidade.getUf();
            fisica.setNaturalidade(nat);
        }

        if (!fisica.getNaturalidade().isEmpty()) {
            nat = fisica.getNaturalidade();
            return nat;
        }

        if (fisica.getId() == -1 || fisica.getNaturalidade().isEmpty()) {
            PessoaEnderecoDao pessoaEnderecoDao = new PessoaEnderecoDao();
            Dao dao = new Dao();
            Filial f = (Filial) dao.find(new Filial(), 1);
            if (f != null) {
                Pessoa pes = f.getMatriz().getPessoa();
                if (pes.getId() != -1) {
                    List<PessoaEndereco> list = pessoaEnderecoDao.pesquisaPessoaEnderecoPorPessoa(pes.getId());
                    if (!list.isEmpty()) {
                        cidade = ((PessoaEndereco) pessoaEnderecoDao.pesquisaPessoaEnderecoPorPessoa(pes.getId()).get(0)).getEndereco().getCidade();
                    } else {
                        cidade = (Cidade) dao.find(new Cidade(), 1);
                    }
                    nat = cidade.getCidade();
                    nat = nat + " - " + cidade.getUf();
                    nat = nat + " <<<";
                    fisica.setNaturalidade(nat);
                    return nat;
                }
            }
        }
        return null;
    }

    public String getPessoaImagem() {
        FacesContext context = FacesContext.getCurrentInstance();
        File files = new File(((ServletContext) context.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/Fotos/"));
        File fExiste = new File(((ServletContext) context.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/Fotos/fotoTemp.jpg"));
        File listFile[] = files.listFiles();
        String nome;
        String caminho;
        //temFoto = false;
        if (fExiste.exists() && fisica.getDataFoto().isEmpty()) {
            fotoTemp = true;
        } else if (fExiste.exists()) {
            fotoTemp = true;
        }
        if (fotoTemp) {
            nome = "fotoTemp";
        } else {
            nome = "semFoto";
        }
        int numArq = listFile.length;
        for (int i = 0; i < numArq; i++) {
            String n = listFile[i].getName();
            for (int o = 0; o < n.length(); o++) {
                if (n.substring(o, o + 1).equals(".")) {
                    n = listFile[i].getName().substring(0, o);
                }
            }
            try {
                if (!fotoTemp) {
                    if (Integer.parseInt(n) == fisica.getPessoa().getId()) {
                        nome = n;
                        fotoTemp = false;
                        caminho = ((ServletContext) context.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/Fotos/fotoTemp.jpg");
                        File fl = new File(caminho);
                        fl.delete();
                        break;
                    }
                } else {
                    fotoTemp = false;
                    break;
                }
            } catch (NumberFormatException e) {
            }
        }
        return nome + ".jpg";
    }

    public void salvarImagem() {
        if (!Diretorio.criar("Imagens/Fotos/")) {
            return;
        }
        String arquivo = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + getCliente() + "/Imagens/Fotos/");
        boolean error = false;
        if (!fotoTempPerfil.equals("")) {
            File des = new File(arquivo + "/" + fisica.getPessoa().getId() + ".png");
            if (des.exists()) {
                des.delete();
            }
            File src = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath(fotoTempPerfil));
            boolean rename = src.renameTo(des);
            fotoPerfil = "/Cliente/" + getCliente() + "/Imagens/Fotos/" + fisica.getPessoa().getId() + ".png";
            fotoTempPerfil = "";

            if (!rename) {
                error = true;
            }
        }
        if (!error) {
            File f = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + getCliente() + "/temp/foto/" + getUsuario().getId()));
            boolean delete = f.delete();
        }
    }

    public void excluirImagemSozinha() {
        FacesContext context = FacesContext.getCurrentInstance();
        String caminho = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/Fotos/fotoTemp.jpg");
        try {
            File fl = new File(caminho);
            if (fl.exists()) {
                fl.delete();
            } else if (fisica.getId() != -1) {
                caminho = ((ServletContext) context.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/Fotos/" + String.valueOf(fisica.getPessoa().getId()) + ".jpg");
                fl = new File(caminho);
                fl.delete();
                Dao dao = new Dao();
                fisica.setDataFoto("");
                dao.update(fisica, true);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void removeImagem() {
        try {
            File fl = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente" + ControleUsuarioBean.getCliente() + "/Imagens/Fotos/" + String.valueOf(fisica.getPessoa().getId()) + ".png"));
            if (fl.exists()) {
                fl.delete();
            }
        } catch (Exception e) {
            // System.out.println(e);
        }
    }

    public String removeEmpresaAnterior(PessoaEmpresa pe) {
        Dao dao = new Dao();
        if (dao.delete(pe, true)) {
            Messages.info("Sucesso", "Empresa removida com sucesso");
        } else {
            Messages.warn("Erro", "Não foi possível remover esta empresa!");
        }
        listPessoaEmpresa.clear();
        return null;
    }

    public void removeJuridicaPesquisada() {
        if (pessoaEmpresa.getId() != -1) {
            Dao dao = new Dao();
            dao.openTransaction();
            if (dao.delete(pessoaEmpresa)) {
                dao.commit();
            } else {
                dao.rollback();
            }
        }
        Sessions.remove("juridicaPesquisa");
        pessoaEmpresa = new PessoaEmpresa();
        profissao = new Profissao();
        renderJuridicaPesquisa = false;
        PF.update("form_pessoa_fisica:i_panel_pessoa_fisica");
    }

    public String hojeRecadastro() {
        fisica.setRecadastro(DataHoje.data());
        return null;
    }

    public List getItens() {
        return itens;
    }

    public void setItens(List itens) {
        this.itens = itens;
    }

    public void setComoPesquisa(String comoPesquisa) {
        this.comoPesquisa = comoPesquisa;
    }

    public String getComoPesquisa() {
        return comoPesquisa;
    }

    public void setPorPesquisa(String porPesquisa) {
        this.porPesquisa = porPesquisa;
    }

    public String getDescPesquisa() {
        return descPesquisa;
    }

    public String getPorPesquisa() {
        return porPesquisa;
    }

    public void setDescPesquisa(String descPesquisa) {
        this.descPesquisa = descPesquisa;
    }

    public PessoaProfissao getPessoaProfissao() {
        return pessoaProfissao;
    }

    public void setPessoaProfissao(PessoaProfissao pessoaProfissao) {
        this.pessoaProfissao = pessoaProfissao;
    }

    public PessoaEndereco getPessoaEndereco() {
        return pessoaEndereco;
    }

    public void setPessoaEndereco(PessoaEndereco pessoaEndereco) {
        this.pessoaEndereco = pessoaEndereco;
    }

    public String getEnderecoCompleto() {
        return enderecoCompleto;
    }

    public void setEnderecoCompleto(String enderecoCompleto) {
        this.enderecoCompleto = enderecoCompleto;
    }

    public void setIndicaTab(String indicaTab) {
        this.indicaTab = indicaTab;
    }

    public String getIndicaTab() {
        return indicaTab;
    }

    public Fisica getFisica() {
        if (fisica.getId() == -1) {
            tipo = "novo";
        } else {
            tipo = "naonovo";
        }
        return fisica;
    }

    public void setFisica(Fisica fisica) {
        this.fisica = fisica;
    }

    public String getRenAbreEnd() {
        return renAbreEnd;
    }

    public void setRenAbreEnd(String renAbreEnd) {
        this.renAbreEnd = renAbreEnd;
    }

    public int getIdPais() {
        return idPais;
    }

    public void setIdPais(int idPais) {
        this.idPais = idPais;
    }

    public int getIdProfissao() {
        return idProfissao;
    }

    public void setIdProfissao(int idProfissao) {
        this.idProfissao = idProfissao;
    }

    public PessoaEmpresa getPessoaEmpresa() {
        if (Sessions.exists("juridicaPesquisa")) {
            pessoaEmpresa.setJuridica((Juridica) Sessions.getObject("juridicaPesquisa"));
            renderJuridicaPesquisa = true;
        }

        return pessoaEmpresa;
    }

    public void setPessoaEmpresa(PessoaEmpresa pessoaEmpresa) {
        this.pessoaEmpresa = pessoaEmpresa;
    }

    public boolean isRenderJuridicaPesquisa() {
        return renderJuridicaPesquisa;
    }

    public void setRenderJuridicaPesquisa(boolean renderJuridicaPesquisa) {
        this.renderJuridicaPesquisa = renderJuridicaPesquisa;
    }

    public int getIdIndexEndereco() {
        return idIndexEndereco;
    }

    public void setIdIndexEndereco(int idIndexEndereco) {
        this.idIndexEndereco = idIndexEndereco;
    }

    public int getIdIndexFisica() {
        return idIndexFisica;
    }

    public void setIdIndexFisica(int idIndexFisica) {
        this.idIndexFisica = idIndexFisica;
    }

    public List<PessoaEmpresa> getListPessoaEmpresa() {
        PessoaEmpresaDao db = new PessoaEmpresaDao();
        if (fisica.getId() != -1) {
            listPessoaEmpresa = db.listaPessoaEmpresaPorFisica(fisica.getId());
        }
        return listPessoaEmpresa;
    }

    public void setListPessoaEmpresa(List<PessoaEmpresa> listPessoaEmpresa) {
        this.listPessoaEmpresa = listPessoaEmpresa;
    }

    public int getIdIndexPessoaEmp() {
        return idIndexPessoaEmp;
    }

    public void setIdIndexPessoaEmp(int idIndexPessoaEmp) {
        this.idIndexPessoaEmp = idIndexPessoaEmp;
    }

    public String getPesquisaPor() {
        return pesquisaPor;
    }

    public void setPesquisaPor(String pesquisaPor) {
        this.pesquisaPor = pesquisaPor;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void limparCamposData() {
        if (pessoaEmpresa.getId() != -1) {
            if (pessoaEmpresa.getJuridica().getId() != -1) {
                if (!pessoaEmpresa.getDemissao().equals("")) {
                    pessoaEmpresa.setAdmissao("");
                    pessoaEmpresa.setDemissao("");
                    pessoaEmpresa = new PessoaEmpresa();
                }
            }
        }
    }

    public void upload() {
        try {
            fileContent = new Scanner(file.getInputStream()).useDelimiter("\\A").next();
        } catch (IOException e) {
            // Error handling
        }
    }

    public Part getFile() {
        return file;
    }

    public void setFile(Part file) {
        this.file = file;
    }

    public String getFileContent() {
        return fileContent;
    }

    public void setFileContent(String fileContent) {
        this.fileContent = fileContent;
    }

    public void validateFile(FacesContext ctx, UIComponent comp, Object value) {
        List<FacesMessage> msgs = new ArrayList<>();
        Part files = (Part) value;
        if (files.getSize() > 1024) {
            msgs.add(new FacesMessage("file too big"));
        }
        if (!"text/plain".equals(files.getContentType())) {
            msgs.add(new FacesMessage("not a text file"));
        }
        if (!msgs.isEmpty()) {
            throw new ValidatorException(msgs);
        }
    }

    public String getMascaraPesquisaFisica() {
        return Mask.getMascaraPesquisa(porPesquisa, true);
    }

    public List<Fisica> getListPessoaFisica() {
        if (listPessoaFisica.isEmpty()) {
            FisicaDao dao = new FisicaDao();
            listPessoaFisica = dao.pesquisaPessoaFisica(descPesquisa, porPesquisa, comoPesquisa);
        }
        return listPessoaFisica;
    }

    public void setLisaPessoaFisica(List<Fisica> listPessoaFisica) {
        this.listPessoaFisica = listPessoaFisica;
    }

    public String pessoaEmpresaString(Fisica f) {
        String pessoaEmpresaString = "";
        PessoaEmpresaDao pessoaEmpresaDB = new PessoaEmpresaDao();
        PessoaEmpresa pe = (PessoaEmpresa) pessoaEmpresaDB.pesquisaPessoaEmpresaPorFisica(f.getId());
        if (pe != null) {
            if (pe.getId() != -1) {
                pessoaEmpresaString = pe.getJuridica().getPessoa().getNome();
            }
        }
        return (pessoaEmpresaString.isEmpty()) ? "SEM EMPRESA" : pessoaEmpresaString;
    }

    public void novoEndereco(TabChangeEvent event) {
        indexNovoEndereco = ((AccordionPanel) event.getComponent()).getActiveIndex();
    }

    public void accordion(TabChangeEvent event) {
        indexPessoaFisica = ((TabView) event.getComponent()).getActiveIndex();
    }

    public String getIndexNovoEndereco() {
        return indexNovoEndereco;
    }

    public void setIndexNovoEndereco(String indexNovoEndereco) {
        this.indexNovoEndereco = indexNovoEndereco;
    }

    public int getIndexPessoaFisica() {
        return indexPessoaFisica;
    }

    public void setIndexPessoaFisica(int indexPessoaFisica) {
        this.indexPessoaFisica = indexPessoaFisica;
    }

    public String getFotoPerfil() {
        if (fisica.getId() != -1) {
            // TEM FOTO MAS NO BANCO ESTA FALSE == ALTERA PARA TRUE NO BANCO
            Dao dao = new Dao();
            if (!fotoPerfil.isEmpty() && fisica.getDataFoto().isEmpty()) {
                fisica.setDataFoto(DataHoje.data());
                dao.update(fisica, true);
                return fotoPerfil;
            }

            // TEM FOTO E NO BANCO ESTA TRUE == PERMANECE DO JEITO QUE ESTA
            // NÃO TEM FOTO E NO BANCO ESTA FALSE = PERMANECE DO JEITO QUE ESTA
            // NÃO TEM FOTO E NO BANCO ESTA TRUE = ALTERA PARA FALSE NO BANCO
            if (fotoPerfil.isEmpty() && !fisica.getDataFoto().isEmpty()) {
                fisica.setDataFoto("");
                dao.update(fisica, true);
            }
        }
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public String getFotoTempPerfil() {
        return fotoTempPerfil;
    }

    public void setFotoTempPerfil(String fotoTempPerfil) {
        this.fotoTempPerfil = fotoTempPerfil;
    }

    public String getCliente() {
        if (cliente.equals("")) {
            if (Sessions.exists("sessaoCliente")) {
                return SessaoCliente.get().getIdentifica();
            }
        }
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public Usuario getUsuario() {
        if (Sessions.exists("sessaoUsuario")) {
            usuario = (Usuario) Sessions.getObject("sessaoUsuario");
        }
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void capturar(CaptureEvent captureEvent) {
        String fotoTempCaminho = "foto/" + getUsuario().getId();
        if (PhotoCam.oncapture(captureEvent, "perfil", fotoTempCaminho, true)) {
            File f = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + getCliente() + "/temp/" + fotoTempCaminho + "/perfil.png"));
            if (f.exists()) {
                fotoTempPerfil = "/Cliente/" + getCliente() + "/temp/" + fotoTempCaminho + "/perfil.png";
                fotoPerfil = "";
            } else {
                fotoTempPerfil = "";
            }
        }
        RequestContext.getCurrentInstance().update(":form_pessoa_fisica");
        RequestContext.getCurrentInstance().execute("dgl_captura.hide();");
    }

    public void upload(FileUploadEvent event) {
        String fotoTempCaminho = "foto/" + getUsuario().getId();
        File f = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + getCliente() + "/temp/" + fotoTempCaminho + "/perfil.png"));
        if (f.exists()) {
            boolean delete = f.delete();
        } else {
            fotoTempPerfil = "";
        }
        ConfiguracaoUpload cu = new ConfiguracaoUpload();
        cu.setArquivo(event.getFile().getFileName());
        cu.setDiretorio("temp/foto/" + getUsuario().getId());
        cu.setArquivo("perfil.png");
        cu.setSubstituir(true);
        cu.setRenomear("perfil.png");
        cu.setEvent(event);
        if (Upload.enviar(cu, true)) {
            fotoTempPerfil = "/Cliente/" + getCliente() + "/temp/" + fotoTempCaminho + "/perfil.png";
            fotoPerfil = "";
        } else {
            fotoTempPerfil = "";
            fotoPerfil = "";
        }
        RequestContext.getCurrentInstance().update(":form_pessoa_fisica");

    }

    public void apagarImagem() {
        boolean sucesso = false;
        if (!fotoTempPerfil.equals("")) {
            File f = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + getCliente() + "/temp/foto/" + getUsuario().getId() + "/perfil.png"));
            sucesso = f.delete();
        } else {
            if (fisica.getId() != -1) {
                File f = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + getCliente() + "/Imagens/Fotos/" + fisica.getPessoa().getId() + ".png"));
                sucesso = f.delete();
            }
        }
        if (sucesso) {
            fotoTempPerfil = "";
            fotoPerfil = "";
            RequestContext.getCurrentInstance().update(":form_pessoa_fisica");
        }
    }

    public void validaPIS() {
        Messages.warn("Validação", "Número do PIS inválido!");
        ValidDocuments.isValidoPIS(fisica.getPis());
    }

    public boolean isReadyOnlineNaturalidade() {
        return readyOnlineNaturalidade;
    }

    public void setReadyOnlineNaturalidade(boolean readyOnlineNaturalidade) {
        this.readyOnlineNaturalidade = readyOnlineNaturalidade;
    }

    public boolean isDisabledNaturalidade() {
        return disabledNaturalidade;
    }

    public void setDisabledNaturalidade(boolean disabledNaturalidad) {
        this.disabledNaturalidade = disabledNaturalidad;
    }

    public String[] getImagensTipo() {
        return imagensTipo;
    }

    public void setImagensTipo(String[] imagensTipo) {
        this.imagensTipo = imagensTipo;
    }

    public Endereco getEnderecox() {
        if (Sessions.getObject("enderecoPesquisa") != null) {
            enderecox = (Endereco) Sessions.getObject("enderecoPesquisa");

            enderecoCompleto = enderecox.getLogradouro().getDescricao() + " "
                    + enderecox.getDescricaoEndereco().getDescricao() + ", "
                    + enderecox.getCidade().getCidade() + " - "
                    + enderecox.getCidade().getUf();

            Sessions.remove("enderecoPesquisa");

            if (visibleEditarEndereco) {
                pessoaEndereco.setEndereco(enderecox);
            }
        }
        return enderecox;
    }

    public void setEnderecox(Endereco enderecox) {
        this.enderecox = enderecox;
    }

    public List<PessoaEndereco> getListPessoaEndereco() {
        if (fisica.getId() != -1 && listPessoaEndereco.isEmpty()) {
            PessoaEnderecoDao pessoaEnderecoDao = new PessoaEnderecoDao();
            listPessoaEndereco = pessoaEnderecoDao.pesquisaPessoaEnderecoPorPessoa(fisica.getPessoa().getId());
        }
        return listPessoaEndereco;
    }

    public void setListPessoaEndereco(List<PessoaEndereco> listPessoaEndereco) {
        this.listPessoaEndereco = listPessoaEndereco;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public boolean isVisibleEditarEndereco() {
        return visibleEditarEndereco;
    }

    public void setVisibleEditarEndereco(boolean visibleEditarEndereco) {
        this.visibleEditarEndereco = visibleEditarEndereco;
    }

    public boolean isChkDependente() {
        return chkDependente;
    }

    public void setChkDependente(boolean chkDependente) {
        this.chkDependente = chkDependente;
    }

    public int getIdTipoCadastro() {
        return idTipoCadastro;
    }

    public void setIdTipoCadastro(int idTipoCadastro) {
        this.idTipoCadastro = idTipoCadastro;
    }

    public Dependente getDependente() {
        return dependente;
    }

    public void setDependente(Dependente dependente) {
        this.dependente = dependente;
    }

    public List<SelectItem> getListGrauParentesco() {
        if (listGrauParentesco.isEmpty()) {
            Dao dao = new Dao();
            List<GrauParentesco> list = dao.list(new GrauParentesco(), true);
            int i = 0;
            listGrauParentesco.add(new SelectItem(i, "Selecionar", "-1"));
            for (i = 0; i < list.size(); i++) {
                listGrauParentesco.add(new SelectItem(i + 1, list.get(i).getDescricao(), "" + list.get(i).getId()));
            }
        }
        return listGrauParentesco;
    }

    public void setListGrauParentesco(List<SelectItem> listGrauParentesco) {
        this.listGrauParentesco = listGrauParentesco;
    }

    public int getIdGrauParentesco() {
        return idGrauParentesco;
    }

    public void setIdGrauParentesco(int idGrauParentesco) {
        this.idGrauParentesco = idGrauParentesco;
    }

    public void clearDependente() {
        dependente = new Dependente();
        dependente.setNascimentoString("");
        idGrauParentesco = 0;
        listGrauParentesco.clear();
    }

    public void addDependente() {
        if (dependente.getNome().isEmpty()) {
            Messages.warn("Validaçao", "Informa nome!");
            return;
        }
        Dao dao = new Dao();
        if (idGrauParentesco == 0) {
            Messages.warn("Validaçao", "Informa o grau de parentesco!");
            return;
        }
        if (dependente.getSexo() == null) {
            Messages.warn("Validaçao", "Informa o sexo!");
            return;
        }
        dependente.setGrauParentesco((GrauParentesco) dao.find(new GrauParentesco(), Integer.parseInt(listGrauParentesco.get(idGrauParentesco).getDescription())));
        if (fisica.getId() == -1) {
            listDependente.add(dependente);
            clearDependente();
        } else {
            if (dependente.getId() == -1) {
                dependente.setPessoa(fisica.getPessoa());
                if (dao.save(dependente, true)) {
                    Messages.info("Sucesso", "Dependente adicionado");
                    clearDependente();
                    listDependente.clear();
                } else {
                    Messages.warn("Erro", "Ao adicionar dependente!");
                }
            } else {
                if (dao.update(dependente, true)) {
                    Messages.info("Sucesso", "Dependente atualizado");
                    clearDependente();
                    listDependente.clear();
                } else {
                    Messages.warn("Erro", "Ao adicionar dependente!");
                }
            }
        }
    }

    public void removeDependente(int index) {
        for (int i = 0; i < listDependente.size(); i++) {
            if (i == index) {
                if (listDependente.get(i).getId() == -1) {
                    listDependente.remove(i);
                } else {
                    Dao dao = new Dao();
                    if (dao.delete(listDependente.get(index), true)) {
                        Messages.info("Sucesso", "Dependente removido");
                        clearDependente();
                    } else {
                        Messages.warn("Erro", "Ao remover dependente!");
                    }
                }
                listDependente.clear();
                break;
            }
        }
    }

    public List<Dependente> getListDependente() {
        if (fisica.getId() != -1) {
            if (listDependente.isEmpty()) {
                DependenteDao dependenteDao = new DependenteDao();
                listDependente = dependenteDao.pesquisaDependentesPorPessoa(fisica.getPessoa().getId());
            }
        }
        return listDependente;
    }

    public void setListDependente(List<Dependente> listDependente) {
        this.listDependente = listDependente;
    }

}
