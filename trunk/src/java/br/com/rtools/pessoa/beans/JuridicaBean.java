package br.com.rtools.pessoa.beans;

import br.com.rtools.endereco.Endereco;
import br.com.rtools.logSistema.Logger;
import br.com.rtools.pessoa.*;
import br.com.rtools.pessoa.dao.CnaeDao;
import br.com.rtools.pessoa.dao.JuridicaDao;
import br.com.rtools.pessoa.dao.JuridicaReceitaDao;
import br.com.rtools.pessoa.dao.PessoaEnderecoDao;
import br.com.rtools.seguranca.controleUsuario.SessaoCliente;
import br.com.rtools.utilitarios.*;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;
import org.primefaces.component.tabview.TabView;
import org.primefaces.event.TabChangeEvent;

@ManagedBean
@SessionScoped
public class JuridicaBean implements Serializable {

    private Juridica juridica;
    private JuridicaReceita juridicaReceita;
    private PessoaEndereco pessoaEndereco;
    private Endereco endereco;
    private boolean marcar;
    private boolean alterarEnd;
    private boolean endComercial;
    private boolean carregaEnvios;
    private boolean renderAtivoInativo;
    private List listEnd;
    private List listEn;
    private List<Cnae> listCnaes;
    private List<Juridica> listJuridica;
    private List<SelectItem> listTipoDocumento;
    private List<SelectItem> listPorte;
    private String atualiza;
    private String enderecoCompleto;
    private String enderecoDeletado;
    private String descPesquisa;
    private String porPesquisa;
    private String comoPesquisa;
    private String descPesquisaCnae;
    private String porPesquisaCnae;
    private String comoPesquisaCnae;
    private String msgDocumento;
    private String maskCnae;
    private String mask;
    private String log;
    private String desc;
    private String cid;
    private String uf;
    private String cnaeContribuinte;
    private String enderecoCobranca;
    private String strSimpleEndereco;
    private String renderNovoEndereco;
    private String renderEndereco;
    private String hiddePessoaEndereco;
    private String renderChkEndereco;
    private String colorContri;
    private String numDocumento;
    private int indicaTab;
    private int idTipoDocumento;
    private int idPorte;
    private int idIndex;
    private int idIndexCnae;
    private int idIndexEndereco;

    @PostConstruct
    public void init() {
        juridica = new Juridica();
        juridicaReceita = new JuridicaReceita();
        pessoaEndereco = new PessoaEndereco();
        endereco = new Endereco();
        indicaTab = 0;
        enderecoCompleto = "";
        enderecoDeletado = null;
        descPesquisa = "";
        porPesquisa = "nome";
        comoPesquisa = "";
        descPesquisaCnae = "";
        porPesquisaCnae = "cnae";
        comoPesquisaCnae = "";
        msgDocumento = "";
        cnaeContribuinte = " sem cnae! ";
        enderecoCobranca = "";
        strSimpleEndereco = "";
        renderNovoEndereco = "false";
        renderEndereco = "false";
        hiddePessoaEndereco = "true";
        renderChkEndereco = "false";
        colorContri = "red";
        numDocumento = "";
        idTipoDocumento = 1;
        idPorte = 0;
        idIndex = -1;
        idIndexCnae = -1;
        idIndexEndereco = -1;
        alterarEnd = false;
        endComercial = false;
        renderAtivoInativo = false;
        listEnd = new ArrayList<>();
        listEn = new ArrayList<>();
        listJuridica = new ArrayList<>();
        listTipoDocumento = new ArrayList<SelectItem>();
        listPorte = new ArrayList<SelectItem>();
        atualiza = "";
    }

    @PreDestroy
    public void destroy() {
        Sessions.remove("juridicaBean");
        Sessions.remove("juridicaPesquisa");
        Sessions.remove("enderecoPesquisa");
        Sessions.remove("enderecoNum");
        Sessions.remove("enderecoComp");
        Sessions.remove("cnaePesquisa");
    }

    public void clear() {
        Sessions.remove("juridicaBean");
    }

    public void clear(String type) {
        if (type.equals("contabilidade")) {
            juridica = new Juridica();
            pessoaEndereco = new PessoaEndereco();
            marcar = false;
            alterarEnd = false;
            renderChkEndereco = "false";
            cnaeContribuinte = " sem cnae! ";
            colorContri = "red";
            renderEndereco = "false";
            renderNovoEndereco = "false";
            hiddePessoaEndereco = "true";
            msgDocumento = "";
            listEnd = new ArrayList();
            idTipoDocumento = 1;
            idPorte = 0;
            enderecoCompleto = "";
            Sessions.remove("enderecoPesquisa");
            Sessions.remove("enderecoNum");
            Sessions.remove("enderecoComp");
        }
    }

    public void pesquisaCnpj() {
        if (juridica.getId() != -1) {
            return;
        }

        if (juridica.getPessoa().getDocumento().isEmpty()) {
            return;
        }

        String documento = AnaliseString.extrairNumeros(juridica.getPessoa().getDocumento());

        if (!validaTipoDocumento(2, documento)) {
            msgDocumento = "Documento inválido!";
            Messages.warn("Erro", "Documento Inválido!");
            return;
        }
        JuridicaDao juridicaDao = new JuridicaDao();
        List listDocumento = juridicaDao.pesquisaJuridicaPorDocumento(juridica.getPessoa().getDocumento());
        for (int i = 0; i < listDocumento.size(); i++) {
            if (!listDocumento.isEmpty()) {
                Messages.warn("Erro", "Empresa já esta cadastrada no Sistema!");
                return;
            }
        }

        JuridicaReceitaDao juridicaReceitaDao = new JuridicaReceitaDao();

        juridicaReceita = juridicaReceitaDao.pesquisaJuridicaReceitaPorDocumento(documento);
        if (juridicaReceita == null) {
            juridicaReceita = new JuridicaReceita();
        }
        if (juridicaReceita.getPessoa() != null && juridicaReceita.getPessoa().getId() != -1) {
            Messages.warn("Erro", "Pessoa já cadastrada no Sistema!");
            return;
        }
        CnaeDao cnaeDao = new CnaeDao();
        Dao dao = new Dao();
        if (juridicaReceita.getId() == -1) {
            try {
                System.loadLibrary("knu");
            } catch (Exception | UnsatisfiedLinkError e) {
                System.out.println(e.getMessage() + " Erro Carregar Lib ");
                Messages.warn("Erro", "Consulta temporárimente indisponível!");
                return;
            }

            knu.ReceitaCNPJ resultado = knu.knu.receitaCNPJ(documento);

            if (resultado.getCod_erro() != 0) {
                Messages.warn("Erro", resultado.getDesc_erro());
                return;
            }

            if (resultado.getNome_empresarial().isEmpty()) {
                Messages.warn("Erro", "Erro ao pesquisar na Receita!");
                return;
            }

            if (resultado.getSituacao_cadastral().equals("BAIXADA")) {
                Messages.warn("Erro", "Erro ao pesquisar na Receita!");
                return;
            }

            juridicaReceita.setNome(resultado.getNome_empresarial());
            juridicaReceita.setFantasia(resultado.getNome_empresarial());
            juridicaReceita.setDocumento(documento);
            juridicaReceita.setCep(resultado.getCep());
            juridicaReceita.setDescricaoEndereco(resultado.getLogradouro());
            juridicaReceita.setBairro(resultado.getBairro());
            juridicaReceita.setComplemento(resultado.getComplemento());
            juridicaReceita.setNumero(resultado.getNumero());
            juridicaReceita.setCnae(resultado.getAtividade_principal());
            juridicaReceita.setPessoa(null);
            juridicaReceita.setStatus(resultado.getSituacao_cadastral());

            dao.openTransaction();

            if (!dao.save(juridicaReceita)) {
                Messages.warn("Erro", "Erro ao Salvar pesquisa!");
                dao.rollback();
                return;
            }

            dao.commit();

            juridica.getPessoa().setNome(juridicaReceita.getNome());
            juridica.setFantasia(juridicaReceita.getNome());

            String result[] = juridicaReceita.getCnae().split(" ");

            List<Cnae> listac = cnaeDao.pesquisaCnae(result[0], "cnae", "I");

            if (listac.isEmpty()) {
                Messages.warn("Erro", "Erro ao pesquisar CNAE");
                return;
            }
            //retornaCnaeReceita(listac.get(0));

            PessoaEnderecoDao pessoaEnderecoDao = new PessoaEnderecoDao();

            String cep = juridicaReceita.getCep();
            cep = cep.replace(".", "").replace("-", "");

            String descricao[] = AnaliseString.removerAcentos(resultado.getLogradouro()).split(" ");
            String bairros[] = AnaliseString.removerAcentos(resultado.getBairro()).split(" ");

            endereco = pessoaEnderecoDao.enderecoReceita(cep, descricao, bairros);

            if (endereco != null) {
                List<TipoEndereco> tipoEnderecos = (List<TipoEndereco>) dao.find("TipoEndereco", new int[]{2, 3, 4, 5});
                for (int i = 0; i < tipoEnderecos.size(); i++) {
                    pessoaEndereco.setEndereco(endereco);
                    pessoaEndereco.setTipoEndereco((TipoEndereco) tipoEnderecos.get(i));
                    pessoaEndereco.setPessoa(juridica.getPessoa());
                    pessoaEndereco.setNumero(juridicaReceita.getNumero());
                    pessoaEndereco.setComplemento(juridicaReceita.getComplemento());
                    listEnd.add(pessoaEndereco);

                    pessoaEndereco = new PessoaEndereco();
                }
            } else {
                String msg = "Endereço não encontrado no Sistema - CEP: " + resultado.getCep() + " DESC: " + resultado.getLogradouro() + " BAIRRO: " + resultado.getBairro();
                Messages.warn("Erro", msg);
            }
        } else {
            juridica.getPessoa().setNome(juridicaReceita.getNome());
            juridica.setFantasia(juridicaReceita.getNome());

            String result[] = juridicaReceita.getCnae().split(" ");

            List<Cnae> listac = cnaeDao.pesquisaCnae(result[0], "cnae", "I");

            if (listac.isEmpty()) {
                Messages.warn("Erro", "Erro ao pesquisar CNAE");
                return;
            }
            //retornaCnaeReceita(listac.get(0));

            if (juridicaReceita.getStatus().equals("BAIXADA")) {
                Messages.warn("Erro", "Esta empresa esta INATIVA na receita!");
            }

            PessoaEnderecoDao pessoaEnderecoDao = new PessoaEnderecoDao();

            String cep = juridicaReceita.getCep();
            cep = cep.replace(".", "").replace("-", "");

            String descricao[] = AnaliseString.removerAcentos(juridicaReceita.getDescricaoEndereco()).split(" ");
            String bairros[] = AnaliseString.removerAcentos(juridicaReceita.getBairro()).split(" ");

            endereco = pessoaEnderecoDao.enderecoReceita(cep, descricao, bairros);

            if (endereco != null) {
                List<TipoEndereco> tipoEnderecos = (List<TipoEndereco>) dao.find("TipoEndereco", new int[]{2, 3, 4, 5});
                for (int i = 0; i < tipoEnderecos.size(); i++) {
                    pessoaEndereco.setEndereco(endereco);
                    pessoaEndereco.setTipoEndereco((TipoEndereco) tipoEnderecos.get(i));
                    pessoaEndereco.setPessoa(juridica.getPessoa());
                    pessoaEndereco.setNumero(juridicaReceita.getNumero());
                    pessoaEndereco.setComplemento(juridicaReceita.getComplemento());
                    listEnd.add(pessoaEndereco);

                    pessoaEndereco = new PessoaEndereco();
                }
            } else {
                String msg = "Endereço não encontrado no Sistema - CEP: " + juridicaReceita.getCep() + " DESC: " + juridicaReceita.getDescricaoEndereco() + " BAIRRO: " + juridicaReceita.getBairro();
                Messages.warn("Erro", msg);
            }
        }
    }

//    public void pesquisaCnpjXML(){
//        if (juridica.getId() != -1){
//            return;
//        }
//        
//        if (juridica.getPessoa().getDocumento().isEmpty()){
//            return;
//        }
//        
//        if (receita){
//            return;
//        }
//        
//        try{
//            SSLUtil.acceptSSL();
//            URL url = new URL("https://c.knu.com.br/webservice");  
//            URLConnection conn = url.openConnection();  
//            conn.setDoOutput(true);
//            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());  
//            wr.write(
//                    "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n" +
//                    "<dados>\n" +
//                    "    <usuario>lucasprogramatecno@gmail.com</usuario>\n" +
//                    "    <senha>lucasjava13</senha>\n" +
//                    "    <funcao>receitaCNPJ</funcao>\n" +
//                    "    <param>"+AnaliseString.extrairNumeros(juridica.getPessoa().getDocumento())+"</param>\n" +
//                    "    <retorno>1</retorno>\n" +
//                    "</dados>"
//            );
//            
//            wr.flush();  
//            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));  
//            String line;  
//            while ((line = rd.readLine()) != null) {
//                if (line.contains("<atividade_principal>")){
//                    //juridica.getPessoa().setNome(line.substring(22, line.indexOf("</atividade_principal>")));
//                    //juridica.setFantasia(juridica.getPessoa().getNome());
//                }
//                if (line.contains("<nome_empresarial>")){
//                    juridica.getPessoa().setNome(line.substring(20, line.indexOf("</nome_empresarial>")));
//                    juridica.setFantasia(juridica.getPessoa().getNome());
//                }
//            }  
//            receita = true;
//            wr.close();  
//            rd.close();
//            }catch(Exception ex){
//                ex.printStackTrace();
//            }
//        }
    public void accordion(TabChangeEvent event) {
        indicaTab = ((TabView) event.getComponent()).getActiveIndex();
    }

    public void pesquisaDocumento() {
        JuridicaDao juridicaDao = new JuridicaDao();
        if (!juridica.getPessoa().getDocumento().isEmpty()) {
            List<Juridica> lista = juridicaDao.pesquisaJuridicaPorDocumento(juridica.getPessoa().getDocumento());
            if (!lista.isEmpty()) {
                Messages.warn("Validação", "Esse documento já existe para: " + lista.get(0).getPessoa().getNome());
                PF.update("form_pessoa_juridica:");
            }
        }
    }

    public String getInadimplente() {
        if (juridica.getId() != -1) {
            JuridicaDao juridicaDao = new JuridicaDao();
            int[] in = juridicaDao.listaInadimplencia(juridica.getPessoa().getId());

            if (in[0] > 0 && in[1] > 0) {
                return "Esta empresa está inadimplente em " + in[0] + " mes(es) e com " + in[1] + " movimento(s) em atraso.";
            }
        }
        return "";
    }

    public String getEnderecoCobranca() {
        PessoaEndereco ende = null;
        String strCompl;
        if (!listEnd.isEmpty()) {
            ende = (PessoaEndereco) listEnd.get(0);
        }

        if (ende != null) {
            if (ende.getComplemento() == null || ende.getComplemento().isEmpty()) {
                strCompl = " ";
            } else {
                strCompl = " ( " + ende.getComplemento() + " ) ";
            }
            enderecoCobranca = ende.getEndereco().getLogradouro().getDescricao() + " "
                    + ende.getEndereco().getDescricaoEndereco().getDescricao() + ", " + ende.getNumero() + " " + ende.getEndereco().getBairro().getDescricao() + ","
                    + strCompl + ende.getEndereco().getCidade().getCidade() + " - " + ende.getEndereco().getCidade().getUf() + " - " + AnaliseString.mascaraCep(ende.getEndereco().getCep());
        } else {
            if (alterarEnd) {
                getListEnderecos();
            } else {
                enderecoCobranca = "NENHUM";
            }
        }
        return enderecoCobranca;
    }

    public void setEnderecoCobranca(String enderecoCobranca) {
        this.enderecoCobranca = enderecoCobranca;
    }

    public String getDtAtivacao() {
        String dt = "";
        return dt;
    }

    public String save() {
        Dao dao = new Dao();
        JuridicaDao juridicaDao = new JuridicaDao();
        Pessoa pessoa = getJuridica().getPessoa();
        List listDocumento;
        if (listEnd.isEmpty() || pessoa.getId() == -1) {
            addPessoaEndereco();
        }
        juridica.setPorte((Porte) dao.find(new Porte(), Integer.parseInt(getListPorte().get(idPorte).getDescription())));
        dao.openTransaction();
        juridica.getPessoa().setTipoDocumento((TipoDocumento) dao.find(new TipoDocumento(), Integer.parseInt(((SelectItem) getListTipoDocumento().get(idTipoDocumento)).getDescription())));
        if (pessoa.getTipoDocumento().getId() != 4 && pessoa.getDocumento().isEmpty()) {
            Messages.warn("Validação", "Informar o número do documento!");
            return null;
        }
        if (juridica.getId() == -1) {
            if (juridica.getPessoa().getNome().isEmpty()) {
                Messages.warn("Validação", "O campo nome não pode ser nulo!");
                return null;
            }

            if (Integer.parseInt(((SelectItem) getListTipoDocumento().get(idTipoDocumento)).getDescription()) == 4) {
                pessoa.setDocumento("0");
            } else {
                listDocumento = juridicaDao.pesquisaJuridicaPorDocumento(juridica.getPessoa().getDocumento());
                for (int i = 0; i < listDocumento.size(); i++) {
                    if (!listDocumento.isEmpty()) {
                        Messages.warn("Validação", "Empresa já existente no Sistema!");
                        return null;
                    }
                }
            }

            if (!validaTipoDocumento(Integer.parseInt(getListTipoDocumento().get(idTipoDocumento).getDescription()), juridica.getPessoa().getDocumento())) {
                Messages.warn("Validação", "Documento Invalido!");
                return null;
            }

            if (listEnd.isEmpty()) {
                Messages.warn("Validação", "Cadastro não pode ser salvo sem Endereço!");
                return null;
            }

            if (juridica.getPessoa().getId() == -1) {
                pessoa.setCliente(SessaoCliente.get());
                dao.save(pessoa);
                juridica.setPessoa(pessoa);
                if (juridica.getCnae().getId() == -1) {
                    juridica.setCnae(null);
                }

                if (juridicaReceita.getId() != -1) {
                    juridicaReceita.setPessoa(pessoa);
                    dao.update(juridicaReceita);
                }

                gerarLoginSenhaPessoa(juridica.getPessoa(), dao);
                if (dao.save(juridica)) {
                    Messages.info("Sucesso", "Cadastro salvo!");
                    dao.commit();
                    Logger novoLog = new Logger();
                    novoLog.save("ID: " + juridica.getId() + " - Pessoa: (" + juridica.getPessoa().getId() + ") " + juridica.getPessoa().getNome() + " - Abertura" + juridica.getAbertura() + " - Fechamento" + juridica.getAbertura() + " - I.E.: " + juridica.getInscricaoEstadual() + " - Insc. Mun.: " + juridica.getInscricaoMunicipal() + " - Responsável: " + juridica.getResponsavel());
                    Sessions.put("juridicaPesquisa", juridica);
                } else {
                    Messages.error("Erro", "Erro ao Salvar Dados!");
                    dao.rollback();
                    return null;
                }
            }
        } else {
            if (juridica.getPessoa().getNome().isEmpty()) {
                Messages.warn("Validação", "O campo nome não pode ser nulo!");
                return null;
            }

            if (Integer.parseInt(((SelectItem) getListTipoDocumento().get(idTipoDocumento)).getDescription()) == 4) {
                juridica.getPessoa().setDocumento("0");
            } else {
                listDocumento = juridicaDao.pesquisaJuridicaPorDocumento(juridica.getPessoa().getDocumento());
                for (int i = 0; i < listDocumento.size(); i++) {
                    if (!listDocumento.isEmpty() && ((Juridica) listDocumento.get(i)).getId() != juridica.getId()) {
                        Messages.warn("Validação", "Empresa já existente no Sistema!");
                        return null;
                    }
                }
                juridica.getPessoa().setTipoDocumento((TipoDocumento) dao.find(new TipoDocumento(), Integer.parseInt(((SelectItem) getListTipoDocumento().get(idTipoDocumento)).getDescription())));
            }
            if (!validaTipoDocumento(Integer.parseInt(getListTipoDocumento().get(idTipoDocumento).getDescription()), juridica.getPessoa().getDocumento())) {
                Messages.warn("Validação", "Documento Invalido!");
                return null;
            }
            addPessoaEndereco();
            if ((juridica.getPessoa().getLogin()) == null && (juridica.getPessoa().getSenha()) == null) {
                gerarLoginSenhaPessoa(juridica.getPessoa(), dao);
            }
            Juridica jur = (Juridica) dao.find(new Juridica(), juridica.getId());
            String beforeUpdate = "ID: " + jur.getId() + " - Pessoa: (" + jur.getPessoa().getId() + ") " + jur.getPessoa().getNome() + " - Abertura: " + jur.getAbertura() + " - Fechamento: " + jur.getAbertura() + " - I.E.: " + jur.getInscricaoEstadual() + " - Insc. Mun.: " + jur.getInscricaoMunicipal() + " - Responsável: " + jur.getResponsavel();
            dao.update(juridica.getPessoa());
            if (dao.update(juridica)) {

                if (juridica.getContabilidade() != null && juridica.getContabilidade().getId() != -1) {
                    dao.update(juridica.getContabilidade());
                }

                Messages.info("Sucesso", "Cadastro atualizado com Sucesso!");
                dao.commit();
                Logger novoLog = new Logger();
                novoLog.update(beforeUpdate, "ID: " + juridica.getId() + " - Pessoa: (" + juridica.getPessoa().getId() + ") " + juridica.getPessoa().getNome() + " - Abertura: " + juridica.getAbertura() + " - Fechamento: " + juridica.getAbertura() + " - I.E.: " + juridica.getInscricaoEstadual() + " - Insc. Mun.: " + juridica.getInscricaoMunicipal() + " - Responsável: " + juridica.getResponsavel());
                Sessions.put("juridicaPesquisa", juridica);
            } else {
                dao.rollback();
                Messages.error("Erro", "Erro ao atualizar Cadastro!");
            }
        }
        savePessoaEndereco();

        return null;
    }

    public String novo() {
//        juridica = new Juridica();
//        contabilidade = new Juridica();
//        pessoaEndereco = new PessoaEndereco();
//        convencao = new Convencao();
//        marcar = false;
//        alterarEnd = false;
//        renderChkEndereco = "false";
//        cnaeContribuinte = " sem cnae! ";
//        colorContri = "red";
//        renderEndereco = "false";
//        renderNovoEndereco = "false";
//        hiddePessoaEndereco = "true";
//        msgDocumento = "";
//        listEnd = new ArrayList();
//        idTipoDocumento = 1;
//        idPorte = 0;
//        listaContribuintesInativos.clear();
//        setEnderecoCompleto("");
//        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("pessoaComplementoBean");
//        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("enderecoPesquisa");
//        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("enderecoNum");
//        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("enderecoComp");
//        listaEmpresasPertencentes.clear();
        Sessions.put("juridicaBean", new JuridicaBean());
        return "pessoaJuridica";
    }

    public void novoGenerico() {
        juridica = new Juridica();
        pessoaEndereco = new PessoaEndereco();
        marcar = false;
        alterarEnd = false;
        renderChkEndereco = "false";
        cnaeContribuinte = " sem cnae! ";
        colorContri = "red";
        renderEndereco = "false";
        renderNovoEndereco = "false";
        hiddePessoaEndereco = "true";
        msgDocumento = "";
        listEnd = new ArrayList();
        idTipoDocumento = 1;
        idPorte = 0;
        setEnderecoCompleto("");
        Sessions.remove("enderecoPesquisa");
        Sessions.remove("enderecoNum");
        Sessions.remove("enderecoComp");
    }

    public String delete() {
        Dao dao = new Dao();
        PessoaEnderecoDao pessoaEnderecoDao = new PessoaEnderecoDao();
        if (juridica.getId() == -1) {
            Messages.warn("Validação", "Pesquise uma empresa para ser excluída!");
            return null;
        }
        List<PessoaEndereco> listEndereco = pessoaEnderecoDao.pesquisaPessoaEnderecoPorPessoa(juridica.getPessoa().getId());
        dao.openTransaction();
        if (!listEndereco.isEmpty()) {
            for (int i = 0; i < listEndereco.size(); i++) {
                if (!dao.delete(listEndereco.get(i))) {
                    Messages.error("Erro", "Erro ao excluir uma Pessoa Endereço!");
                    dao.rollback();
                    return null;
                }
            }
        }

        // ------------------------------------------------------------------------------------------------------
        if (!dao.delete(juridica)) {
            Messages.error("Erro", "Erro ao excluir Jurídica!");
            dao.rollback();
            return null;
        }

        JuridicaReceitaDao juridicaReceitaDao = new JuridicaReceitaDao();
        String documento = AnaliseString.extrairNumeros(juridica.getPessoa().getDocumento());
        JuridicaReceita jr = juridicaReceitaDao.pesquisaJuridicaReceitaPorDocumento(documento);

        if (jr != null) {
            if (!dao.delete(jr)) {
                Messages.error("Erro", "Erro ao excluir Pesquisa da Receita!");
                dao.rollback();
                return null;
            }
        }
        // -------------------------------------------------------------------------------------------------

        if (!dao.delete(juridica.getPessoa())) {
            Messages.error("Erro", "Erro ao excluir Pessoa!");
            dao.rollback();
            return null;
        }
        Messages.info("Sucesso", "Cadastro excluido com sucesso!");
        Logger novoLog = new Logger();
        novoLog.delete("ID: " + juridica.getId() + " - Pessoa: (" + juridica.getPessoa().getId() + ") " + juridica.getPessoa().getNome() + " - Abertura" + juridica.getAbertura() + " - Fechamento" + juridica.getAbertura() + " - I.E.: " + juridica.getInscricaoEstadual() + " - Insc. Mun.: " + juridica.getInscricaoMunicipal() + " - Responsável: " + juridica.getResponsavel());
        dao.commit();
        novoGenerico();
        return null;
    }

    public String edit(Juridica j) {
        juridica = j;
        String url = (String) Sessions.getString("urlRetorno");
        Sessions.put("linkClicado", true);
        descPesquisa = "";
        porPesquisa = "nome";
        comoPesquisa = "";
        if (!getListTipoDocumento().isEmpty()) {
            for (int o = 0; o < listTipoDocumento.size(); o++) {
                if (Integer.parseInt(listTipoDocumento.get(o).getDescription()) == juridica.getPessoa().getTipoDocumento().getId()) {
                    idTipoDocumento = o;
                }
            }
        }

        if (!getListPorte().isEmpty()) {
            for (int o = 0; o < listPorte.size(); o++) {
                if (Integer.parseInt(listPorte.get(o).getDescription()) == juridica.getPorte().getId()) {
                    idPorte = o;
                }
            }
        }

        if (url != null) {
            Sessions.put("juridicaPesquisa", juridica);
            if (juridica.getContabilidade() == null) {
                renderChkEndereco = "false";
            } else {
                renderChkEndereco = "true";
            }
            renderNovoEndereco = "false";
            renderEndereco = "false";
            alterarEnd = true;
            listEnd = new ArrayList();
            enderecoCobranca = "NENHUM";
            getListEnderecos();
            return url;
        }
        return "pessoaJuridica";
    }

    public void addPessoaEndereco() {
        Dao dao = new Dao();
        PessoaEnderecoDao pessoaEnderecoDao = new PessoaEnderecoDao();
        endereco = new Endereco();
        String num;
        String comp;
        int i = 0;
        Sessions.put("enderecoNum", pessoaEndereco.getNumero());
        Sessions.put("enderecoComp", pessoaEndereco.getComplemento());
        List<TipoEndereco> tipoEnderecos = (List<TipoEndereco>) dao.find("TipoEndereco", new int[]{2, 3, 4, 5});
        endereco = (Endereco) Sessions.getObject("enderecoPesquisa");
        if (endereco != null) {
            if (!alterarEnd) {
                num = (String) Sessions.getString("enderecoNum");
                comp = (String) Sessions.getString("enderecoComp");
                while (i < tipoEnderecos.size()) {
                    pessoaEndereco.setEndereco(endereco);
                    pessoaEndereco.setTipoEndereco((TipoEndereco) tipoEnderecos.get(i));
                    pessoaEndereco.setPessoa(juridica.getPessoa());
                    pessoaEndereco.setNumero(num);
                    pessoaEndereco.setComplemento(comp);
                    listEnd.add(pessoaEndereco);
                    i++;
                    pessoaEndereco = new PessoaEndereco();
                }
            } else {
                num = (String) Sessions.getString("enderecoNum");
                comp = (String) Sessions.getString("enderecoComp");
                if (!listEnd.isEmpty() && pessoaEndereco.getTipoEndereco().getId() == 2) {

                    if (pessoaEndereco.getId() != -1) {
                        // PessoaEndereco pessoaEndeAnt = new PessoaEndereco();

                        PessoaEndereco pessoaEndeAnt = pessoaEnderecoDao.pesquisaPessoaEnderecoPorPessoaTipo(pessoaEndereco.getPessoa().getId(), 2);
                        ((PessoaEndereco) listEnd.get(0)).setTipoEndereco((TipoEndereco) tipoEnderecos.get(0));
                        ((PessoaEndereco) listEnd.get(0)).setEndereco(endereco);
                        ((PessoaEndereco) listEnd.get(0)).setComplemento(pessoaEndereco.getComplemento());
                        ((PessoaEndereco) listEnd.get(0)).setNumero(pessoaEndereco.getNumero());
                        for (int u = 1; u < listEnd.size(); u++) {
                            if (comparaEndereco(pessoaEndeAnt, (PessoaEndereco) listEnd.get(u))) {
                                ((PessoaEndereco) listEnd.get(u)).setTipoEndereco((TipoEndereco) tipoEnderecos.get(u));
                                ((PessoaEndereco) listEnd.get(u)).setEndereco(endereco);
                                ((PessoaEndereco) listEnd.get(u)).setComplemento(pessoaEndereco.getComplemento());
                                ((PessoaEndereco) listEnd.get(u)).setNumero(pessoaEndereco.getNumero());
                            }
                        }
                        endComercial = true;
                    } else {
                        listEnd = new ArrayList();
                        for (int u = 0; u < tipoEnderecos.size(); u++) {
                            pessoaEndereco.setEndereco(endereco);
                            pessoaEndereco.setTipoEndereco((TipoEndereco) tipoEnderecos.get(u));
                            pessoaEndereco.setPessoa(juridica.getPessoa());
                            pessoaEndereco.setNumero(num);
                            pessoaEndereco.setComplemento(comp);
                            listEnd.add(pessoaEndereco);
                            pessoaEndereco = new PessoaEndereco();
                        }
                    }
                } else {
                    if (!listEnd.isEmpty()) {
                        pessoaEndereco.setEndereco(endereco);
                        pessoaEndereco.setPessoa(juridica.getPessoa());
                        pessoaEndereco.setNumero(num);
                        pessoaEndereco.setComplemento(comp);
                        ((PessoaEndereco) listEnd.get(idIndexEndereco)).setEndereco(endereco);
                        ((PessoaEndereco) listEnd.get(idIndexEndereco)).setComplemento(pessoaEndereco.getComplemento());
                        ((PessoaEndereco) listEnd.get(idIndexEndereco)).setNumero(pessoaEndereco.getNumero());
                    } else {
                        int j = 0;
                        while (j < tipoEnderecos.size()) {
                            pessoaEndereco.setEndereco(endereco);
                            pessoaEndereco.setTipoEndereco((TipoEndereco) tipoEnderecos.get(j));
                            pessoaEndereco.setPessoa(juridica.getPessoa());
                            pessoaEndereco.setNumero(num);
                            pessoaEndereco.setComplemento(comp);
                            listEnd.add(pessoaEndereco);
                            j++;
                            pessoaEndereco = new PessoaEndereco();
                        }
                    }
                }
                alterarEnd = false;
            }
            renderEndereco = "true";
            renderNovoEndereco = "false";
        }
        setEnderecoCompleto("");
        Sessions.remove("enderecoPesquisa");
        Sessions.remove("enderecoNum");
        Sessions.remove("enderecoComp");
    }

    public boolean comparaEndereco(PessoaEndereco pessoaEnde1, PessoaEndereco pessoaEnde2) {
        boolean compara;
        if (pessoaEnde1 != null && pessoaEnde2 != null) {
            if (pessoaEnde1.getComplemento() == null || pessoaEnde2.getComplemento() == null) {
                pessoaEnde1.setComplemento("");
                pessoaEnde2.setComplemento("");
            }
            if ((pessoaEnde1.getEndereco().getId() == pessoaEnde2.getEndereco().getId()
                    && pessoaEnde1.getNumero().equals(pessoaEnde2.getNumero())
                    && pessoaEnde1.getComplemento().equals(pessoaEnde2.getComplemento()))) {
                compara = true;
            } else {
                compara = false;
            }
        } else {
            compara = false;
        }
        return compara;
    }

    public List<PessoaEndereco> getListEnderecos() {
        String strCompl;
        if (!getPesquisaPessoaEnderecoPorPessoa().isEmpty() && alterarEnd && listEnd.isEmpty()) {
            listEnd = getPesquisaPessoaEnderecoPorPessoa();
            PessoaEndereco pesEn = (PessoaEndereco) (listEnd.get(1));
            if (pesEn.getComplemento() == null || pesEn.getComplemento().isEmpty()) {
                strCompl = " ";
            } else {
                strCompl = " ( " + pesEn.getComplemento() + " ) ";
            }
            enderecoCobranca = pesEn.getEndereco().getLogradouro().getDescricao() + " "
                    + pesEn.getEndereco().getDescricaoEndereco().getDescricao() + ", " + pesEn.getNumero() + " " + pesEn.getEndereco().getBairro().getDescricao() + ","
                    + strCompl + pesEn.getEndereco().getCidade().getCidade() + " - " + pesEn.getEndereco().getCidade().getUf() + " - " + AnaliseString.mascaraCep(pesEn.getEndereco().getCep());
        }
        return listEnd;
    }

    public List getPesquisaPessoaEnderecoPorPessoa() {
        PessoaEnderecoDao pessoaEnderecoDao = new PessoaEnderecoDao();
        return pessoaEnderecoDao.pesquisaPessoaEnderecoPorPessoa(juridica.getPessoa().getId());
    }

    public void editPessoaEndereco(PessoaEndereco linha, int index) {
        pessoaEndereco = linha;
        idIndexEndereco = index;
        Sessions.put("enderecoPesquisa", pessoaEndereco.getEndereco());
        log = pessoaEndereco.getEndereco().getLogradouro().getDescricao();
        desc = pessoaEndereco.getEndereco().getDescricaoEndereco().getDescricao();
        cid = pessoaEndereco.getEndereco().getCidade().getCidade();
        uf = pessoaEndereco.getEndereco().getCidade().getUf();
        setEnderecoCompleto(log + " " + desc + ", " + cid + " - " + uf);
        renderEndereco = "false";
        renderNovoEndereco = "true";
        alterarEnd = true;
    }

    public void showPessoaEndereco() {
        if (listEnd.isEmpty()) {
            renderEndereco = "false";
            renderNovoEndereco = "true";
            pessoaEndereco = new PessoaEndereco();
            listEnd = new ArrayList();
        } else {
            renderEndereco = "true";
            renderNovoEndereco = "false";
        }
    }

    public void savePessoaEndereco() {
        //VERIFICAR ENDERECO CONTABILIDADE
        verificarEndContabilidade();
        if (juridica.getId() != -1) {
            Dao dao = new Dao();
            if (getPesquisaPessoaEnderecoPorPessoa().isEmpty()) {
                for (int i = 0; i < listEnd.size(); i++) {
                    pessoaEndereco = (PessoaEndereco) listEnd.get(i);
                    dao.openTransaction();
                    if (dao.save(pessoaEndereco)) {
                        dao.commit();
                    } else {
                        dao.rollback();
                        Messages.warn("Erro", "Ao inserir endereço!");

                    }
                    pessoaEndereco = new PessoaEndereco();
                }
            } else {
                if (endComercial) {
                    for (int o = 0; o < listEnd.size(); o++) {
                        dao.openTransaction();
                        if (dao.update((PessoaEndereco) listEnd.get(o))) {
                            dao.commit();
                        } else {
                            dao.rollback();
                        }
                    }
                    endComercial = false;
                } else {
                    if (pessoaEndereco.getTipoEndereco().getId() == 3) {
                    }
                    for (int o = 0; o < listEnd.size(); o++) {
                        dao.openTransaction();
                        if (dao.update((PessoaEndereco) listEnd.get(o))) {
                            dao.commit();
                        } else {
                            dao.rollback();
                        }
                    }
                }

            }
            pessoaEndereco = new PessoaEndereco();
        }
    }

    public void verificarEndContabilidade() {
        PessoaEnderecoDao pessoaEnderecoDao = new PessoaEnderecoDao();
        if (juridica.getId() != -1) {
            if (juridica.isCobrancaEscritorio() && (juridica.getContabilidade() != null && juridica.getContabilidade().getId() != -1)) {
                PessoaEndereco pesEndCon = pessoaEnderecoDao.pesquisaPessoaEnderecoPorPessoaTipo(juridica.getContabilidade().getPessoa().getId(), 3);
                if ((!listEnd.isEmpty()) && pesEndCon != null) {
                    pessoaEndereco = (PessoaEndereco) listEnd.get(1);
                    pessoaEndereco.setComplemento(pesEndCon.getComplemento());
                    pessoaEndereco.setNumero(pesEndCon.getNumero());
                    endereco = pesEndCon.getEndereco();
                    pessoaEndereco.setEndereco(endereco);
                    listEnd.set(1, pessoaEndereco);
                }
            } else if (juridica != null) {
                if (juridica.getContabilidade() != null) {
                    if (comparaEndereco((PessoaEndereco) listEnd.get(1), pessoaEnderecoDao.pesquisaPessoaEnderecoPorPessoaTipo(juridica.getContabilidade().getPessoa().getId(), 3))) {
                        PessoaEndereco pesEndCon = pessoaEnderecoDao.pesquisaPessoaEnderecoPorPessoaTipo(juridica.getPessoa().getId(), 2);
                        if ((!listEnd.isEmpty()) && pesEndCon != null && !endComercial) {
                            pessoaEndereco = (PessoaEndereco) listEnd.get(1);
                            pessoaEndereco.setComplemento(pesEndCon.getComplemento());
                            pessoaEndereco.setNumero(pesEndCon.getNumero());
                            endereco = pesEndCon.getEndereco();
                            pessoaEndereco.setEndereco(endereco);
                            listEnd.set(1, pessoaEndereco);
                        }
                    }
                }
            }
        }
    }

    public String voltarEndereco() {
        indicaTab = 0;
        return "pessoaJuridica";
    }

    public boolean getHabilitar() {
        if (juridica.getPessoa().getId() == -1) {
            return true;
        } else {
            return false;
        }
    }

    public String removePessoaEndereco() {
        if (pessoaEndereco.getId() != -1) {
            Dao dao = new Dao();
            if (dao.delete(pessoaEndereco)) {
                dao.commit();
            } else {
                dao.rollback();
            }
        }
        pessoaEndereco = new PessoaEndereco();
        setEnderecoCompleto("");
        return "pessoaJuridica";
    }

    public void acaoPesquisaInicial() {
        comoPesquisa = "I";
        listJuridica.clear();
    }

    public void acaoPesquisaParcial() {
        comoPesquisa = "P";
        listJuridica.clear();
    }

    public String getMask() {
        int i = Integer.parseInt(getListTipoDocumento().get(idTipoDocumento).getDescription());
        // 1 cpf, 2 cnpj, 3 cei, 4 nenhum
        if (i == 1) {
            return Mask.getMascara("cpf");
        }
        if (i == 2) {
            return Mask.getMascara("cnpj");
        }
        if (i == 3) {
            //mask = "99.999.99999/99";
            mask = "";
        }
        if (i == 4) {
            mask = "";
        }
        return mask;
    }

    public List<SelectItem> getListTipoDocumento() {
        if (listTipoDocumento.isEmpty()) {
            Dao dao = new Dao();
            List<TipoDocumento> list = (List<TipoDocumento>) dao.list(new TipoDocumento());
            for (int i = 0; i < list.size(); i++) {
                listTipoDocumento.add(new SelectItem(i, (String) (list.get(i)).getDescricao(), Integer.toString((list.get(i)).getId())));
            }
        }
        return listTipoDocumento;
    }

    public void setListTipoDocumento(List<SelectItem> listTipoDocumento) {
        this.listTipoDocumento = listTipoDocumento;
    }

    public String getRetornarEnderecoAmbos() {
        if (Sessions.exists("enderecoPesquisa")) {
            log = ((Endereco) Sessions.getObject("enderecoPesquisa")).getLogradouro().getDescricao();
            desc = ((Endereco) Sessions.getObject("enderecoPesquisa")).getDescricaoEndereco().getDescricao();
            cid = ((Endereco) Sessions.getObject("enderecoPesquisa")).getCidade().getCidade();
            uf = ((Endereco) Sessions.getObject("enderecoPesquisa")).getCidade().getUf();
            setEnderecoCompleto(log + " " + desc + ", " + cid + " - " + uf);
        }
        return enderecoCompleto;
    }

    public String btnExcluirContabilidadePertencente() {
        if (juridica.getId() != -1) {
            //chkEndContabilidade = false;
            savePessoaEndereco();
            juridica.setContabilidade(null);
            juridica.setEmailEscritorio(false);
        } else {
            juridica.setContabilidade(null);
            juridica.setEmailEscritorio(false);
        }
        return "pessoaJuridica";
    }

    public String pesquisarPessoaJuridicaGeracaoCadastrar() {
        Sessions.put("urlRetorno", "processamentoIndividual");
        return "pessoaJuridica";
    }

    public boolean validaTipoDocumento(int idDoc, String docS) {
        // 1 cpf, 2 cnpj, 3 cei, 4 nenhum
        //String documento = "";
        String documento = docS.replace(".", "").replace("/", "").replace("-", "");

        boolean ye = false;
        if (idDoc == 1) {
            ye = ValidDocuments.isValidoCPF(documento);
        }
        if (idDoc == 2) {
            ye = ValidDocuments.isValidoCNPJ(documento);
        }
        if (idDoc == 3) {
            //ye = ValidaDocumentos.isValidoCEI(documento);
            ye = true;
        }
        if (idDoc == 4) {
            ye = true;
        }

        return ye;
    }

    public String linkDaReceita() {
        if (juridica != null) {
            int i = 0;
            String documento = "";
            String docLaco = juridica.getPessoa().getDocumento();
            if (validaTipoDocumento(2, docLaco)) {
                while (i < docLaco.length()) {
                    String as = docLaco.substring(i, i + 1);
                    if (!as.equals(".") && !as.equals("-") && !as.equals("/")) {
                        documento = documento + as;
                    }
                    i++;
                }
                Clipboard copia = Toolkit.getDefaultToolkit().getSystemClipboard();
                StringSelection selection = new StringSelection(documento);
                copia.setContents(selection, null);
            } else {
                Clipboard copia = Toolkit.getDefaultToolkit().getSystemClipboard();
                StringSelection selection = new StringSelection("Ação Inválida!");
                copia.setContents(selection, null);
            }
        }
        return null;
    }

    public void enviarEmail() {
//        Dao salvarAcumuladoDB = new Dao();
//        EnvioEmailsDB dbE = new EnvioEmailsDBToplink();
//        if (juridica.getId() != -1) {
//            String msg = EnviarEmail.EnviarEmail((Registro) salvarAcumuladoDB.pesquisaCodigo(1, "Registro"), juridica);
//            if (msg.equals("Enviado com Sucesso. Confira email cadastrado!")) {
//                dbE.insert(envioEmails);
//                Messages.warn("Erro", "Enviado com Sucesso. Confira email cadastrado!");
//            }
//        } else {
//            Messages.warn("Erro", "Pesquisar uma Empresa para envio!");
//        }
    }

    public String enviarEmailParaTodos() {
//        Dao salvarAcumuladoDB = new Dao();
//        JuridicaDao juridicaDB = new JuridicaDao();
//        List<Juridica> juridicas = juridicaDB.pesquisaJuridicaComEmail();
//        Registro reg = (Registro) salvarAcumuladoDB.pesquisaCodigo(1, "Registro");
//        msgConfirma = EnviarEmail.EnviarEmailAutomatico(reg, juridicas);
        return null;
    }

    public boolean isRenEnviarEmail() {
        if (juridica.getId() == 1) {
            return false;
        } else {
            return true;
        }
    }

    public void gerarLoginSenhaPessoa(Pessoa pessoa, Dao dao) {
        String login = "", senha = "", nome = "";
        senha = senha + DataHoje.hora().replace(":", "");
        senha = Integer.toString(Integer.parseInt(senha) + Integer.parseInt(senha + "43"));
        senha = senha.substring(senha.length() - 6, senha.length());
        nome = AnaliseString.removerAcentos(pessoa.getNome().replace(" ", "X").toUpperCase());
        nome = nome.replace("-", "Y");
        nome = nome.replace(".", "W");
        nome = nome.replace("/", "Z");
        nome = nome.replace("A", "Q");
        nome = nome.replace("E", "R");
        nome = nome.replace("I", "H");
        nome = nome.replace("O", "P");
        nome = nome.replace("U", "M");
        nome = ("JHSRGDQ" + nome) + pessoa.getId();
        login = nome.substring(nome.length() - 6, nome.length());
        pessoa.setLogin(login);
        pessoa.setSenha(senha);
        dao.update(pessoa);
    }

    public void atualizaEnvioEmails() {
        if (juridica.getId() != -1) {
//            envioEmails = new EnvioEmails();
//            envioEmails.setEmail(juridica.getPessoa().getEmail1());
//            envioEmails.setHistorico("Envio de Login e senha para Contribuinte.");
//            envioEmails.setOperacao("LOGIN");
//            envioEmails.setPessoa(juridica.getPessoa());
//            envioEmails.setDtEnvio(DataHoje.dataHoje());
            carregaEnvios = true;
            listEn = new ArrayList();
        }
    }

    public Juridica getJuridica() {
        if (juridica.getFantasia().isEmpty() || juridica.getFantasia() == null) {
            juridica.setFantasia(juridica.getPessoa().getNome());
        }
        if (Sessions.exists("cnaePesquisa")) {
            juridica.setCnae((Cnae) Sessions.getObject("cnaePesquisa", true));
        }
        return juridica;
    }

    public void setJuridica(Juridica juridica) {
        this.juridica = juridica;
    }

    public boolean getMarcar() {
        return marcar;
    }

    public void setMarcar(boolean marcar) {
        this.marcar = marcar;
    }

    public int getIdTipoDocumento() {
        return idTipoDocumento;
    }

    public void setIdTipoDocumento(int idTipoDocumento) {
        this.idTipoDocumento = idTipoDocumento;
    }

    public String getCnaeContribuinte() {
        return cnaeContribuinte;
    }

    public void setCnaeContribuinte(String cnaeContribuinte) {
        this.cnaeContribuinte = cnaeContribuinte;
    }

    public String getStrSimpleEndereco() {
        if (juridica.getId() == -1) {
            strSimpleEndereco = "Adicionar Endereço";
        } else {
            strSimpleEndereco = "Mais Endereço";
        }
        return strSimpleEndereco;
    }

    public void setStrSimpleEndereco(String strSimpleEndereco) {
        this.strSimpleEndereco = strSimpleEndereco;
    }

    public String getAtualiza() {
        if (atualiza.isEmpty()) {
            atualiza = "Agora ";
        } else {
            atualiza += "Funcionou!!";
        }
        return atualiza;
    }

    public void setAtualiza(String atualiza) {
        this.atualiza = atualiza;
    }

    public List<Juridica> getListJuridica() {
        if (listJuridica.isEmpty()) {
            JuridicaDao juridicaDao = new JuridicaDao();
            listJuridica = juridicaDao.pesquisaPessoaJuridica(descPesquisa, porPesquisa, comoPesquisa);
        }
        return listJuridica;
    }

    public List<SelectItem> getListPorte() {
        if (listPorte.isEmpty()) {
            List<Porte> select = new Dao().list(new Porte());
            for (int i = 0; i < select.size(); i++) {
                listPorte.add(new SelectItem(i, select.get(i).getDescricao(), Integer.toString(select.get(i).getId())));
            }
        }
        return listPorte;
    }

    public void setListPorte(List<SelectItem> listPorte) {
        this.listPorte = listPorte;
    }

    public String limparCampoPesquisa() {
        setDescPesquisa("");
        return null;
    }

    public void clearCnae() {
        if (juridica.getId() != -1) {
            juridica.setCnae(null);
        }
    }

    public String getMascaraPesquisaJuridica() {
        return Mask.getMascaraPesquisa(porPesquisa, true);
    }

    public JuridicaReceita getJuridicaReceita() {
        return juridicaReceita;
    }

    public void setJuridicaReceita(JuridicaReceita juridicaReceita) {
        this.juridicaReceita = juridicaReceita;
    }

    public PessoaEndereco getPessoaEndereco() {
        return pessoaEndereco;
    }

    public void setPessoaEndereco(PessoaEndereco pessoaEndereco) {
        this.pessoaEndereco = pessoaEndereco;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public String getEnderecoCompleto() {
        return enderecoCompleto;
    }

    public void setEnderecoCompleto(String enderecoCompleto) {
        this.enderecoCompleto = enderecoCompleto;
    }

    public String getEnderecoDeletado() {
        return enderecoDeletado;
    }

    public void setEnderecoDeletado(String enderecoDeletado) {
        this.enderecoDeletado = enderecoDeletado;
    }

    public String getDescPesquisa() {
        return descPesquisa;
    }

    public void setDescPesquisa(String descPesquisa) {
        this.descPesquisa = descPesquisa;
    }

    public String getPorPesquisa() {
        return porPesquisa;
    }

    public void setPorPesquisa(String porPesquisa) {
        this.porPesquisa = porPesquisa;
    }

    public String getComoPesquisa() {
        return comoPesquisa;
    }

    public void setComoPesquisa(String comoPesquisa) {
        this.comoPesquisa = comoPesquisa;
    }

    public String getDescPesquisaCnae() {
        return descPesquisaCnae;
    }

    public void setDescPesquisaCnae(String descPesquisaCnae) {
        this.descPesquisaCnae = descPesquisaCnae;
    }

    public String getPorPesquisaCnae() {
        return porPesquisaCnae;
    }

    public void setPorPesquisaCnae(String porPesquisaCnae) {
        this.porPesquisaCnae = porPesquisaCnae;
    }

    public String getComoPesquisaCnae() {
        return comoPesquisaCnae;
    }

    public void setComoPesquisaCnae(String comoPesquisaCnae) {
        this.comoPesquisaCnae = comoPesquisaCnae;
    }

    public String getMsgDocumento() {
        return msgDocumento;
    }

    public void setMsgDocumento(String msgDocumento) {
        this.msgDocumento = msgDocumento;
    }

    public String getMaskCnae() {
        return maskCnae;
    }

    public void setMaskCnae(String maskCnae) {
        this.maskCnae = maskCnae;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getRenderNovoEndereco() {
        return renderNovoEndereco;
    }

    public void setRendeNovoEndereco(String renderNovoEndereco) {
        this.renderNovoEndereco = renderNovoEndereco;
    }

    public String getRenderEndereco() {
        return renderEndereco;
    }

    public void setRenderEndereco(String renderEndereco) {
        this.renderEndereco = renderEndereco;
    }

    public String getHiddePessoaEndereco() {
        return hiddePessoaEndereco;
    }

    public void setHiddePessoaEndereco(String hiddePessoaEndereco) {
        this.hiddePessoaEndereco = hiddePessoaEndereco;
    }

    public String getRenderChkEndereco() {
        return renderChkEndereco;
    }

    public void setRendeChkEndereco(String renderChkEndereco) {
        this.renderChkEndereco = renderChkEndereco;
    }

    public String getColorContri() {
        return colorContri;
    }

    public void setColorContri(String colorContri) {
        this.colorContri = colorContri;
    }

    public String getNumDocumento() {
        return numDocumento;
    }

    public void setNumDocumento(String numDocumento) {
        this.numDocumento = numDocumento;
    }

    public int getIndicaTab() {
        return indicaTab;
    }

    public void setIndicaTab(int indicaTab) {
        this.indicaTab = indicaTab;
    }

    public int getIdPorte() {
        return idPorte;
    }

    public void setIdPorte(int idPorte) {
        this.idPorte = idPorte;
    }

    public int getIdIndex() {
        return idIndex;
    }

    public void setIdIndex(int idIndex) {
        this.idIndex = idIndex;
    }

    public int getIdIndexCnae() {
        return idIndexCnae;
    }

    public void setIdIndexCnae(int idIndexCnae) {
        this.idIndexCnae = idIndexCnae;
    }

    public int getIdIndexEndereco() {
        return idIndexEndereco;
    }

    public void setIdIndexEndereco(int idIndexEndereco) {
        this.idIndexEndereco = idIndexEndereco;
    }

    public boolean isMarcar() {
        return marcar;
    }

    public boolean isAlterarEnd() {
        return alterarEnd;
    }

    public void setAlterarEnd(boolean alterarEnd) {
        this.alterarEnd = alterarEnd;
    }

    public boolean isEndComercial() {
        return endComercial;
    }

    public void setEndComercial(boolean endComercial) {
        this.endComercial = endComercial;
    }

    public boolean isCarregaEnvios() {
        return carregaEnvios;
    }

    public void setCarregaEnvios(boolean carregaEnvios) {
        this.carregaEnvios = carregaEnvios;
    }

    public boolean isRenderAtivoInativo() {
        return renderAtivoInativo;
    }

    public void setRenderAtivoInativo(boolean renderAtivoInativo) {
        this.renderAtivoInativo = renderAtivoInativo;
    }

    public List getListEnd() {
        return listEnd;
    }

    public void setListEnd(List listEnd) {
        this.listEnd = listEnd;
    }

    public List getListEn() {
        return listEn;
    }

    public void setListEn(List listEn) {
        this.listEn = listEn;
    }

    public void setListJuridica(List<Juridica> listJuridica) {
        this.listJuridica = listJuridica;
    }
}
