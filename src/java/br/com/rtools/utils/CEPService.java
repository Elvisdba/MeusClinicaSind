package br.com.clinicaintegrada.utils;

import br.com.clinicaintegrada.endereco.Bairro;
import br.com.clinicaintegrada.endereco.CepAlias;
import br.com.clinicaintegrada.endereco.Cidade;
import br.com.clinicaintegrada.endereco.DescricaoEndereco;
import br.com.clinicaintegrada.endereco.Endereco;
import br.com.clinicaintegrada.endereco.Logradouro;
import br.com.clinicaintegrada.endereco.dao.BairroDao;
import br.com.clinicaintegrada.endereco.dao.CidadeDao;
import br.com.clinicaintegrada.endereco.dao.DescricaoEnderecoDao;
import br.com.clinicaintegrada.endereco.dao.EnderecoDao;
import br.com.clinicaintegrada.endereco.dao.LogradouroDao;
import br.com.clinicaintegrada.seguranca.controleUsuario.SessaoCliente;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.Annotations;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

public class CEPService {

    private String cep = "";
    private String cepMemoria = "";
    private Endereco endereco = new Endereco();

    /**
     * http://www.republicavirtual.com.br/cep/exemplos.php
     *
     */
    public void procurar() {
        EnderecoDao enderecoDao = new EnderecoDao();
        CidadeDao cidadeDao = new CidadeDao();
        List<Endereco> listaEnderecos = (List<Endereco>) enderecoDao.pesquisaEnderecoPorCep(cep);
        if (listaEnderecos.isEmpty()) {
            if (cepMemoria.equals(cep)) {
                return;
            }
            if (cepMemoria.equals("")) {
                cepMemoria = cep;
            }
            cep = cep.replace("-", "");
            String urlString = "http://cep.republicavirtual.com.br/web_cep.php?cep=" + cep + "&formato=query_string";
            // os parametros a serem enviados
            Properties parameters = new Properties();
            parameters.setProperty("cep", cep);
            parameters.setProperty("formato", "xml");
            Iterator i = parameters.keySet().iterator();
            int counter = 0;
            while (i.hasNext()) {
                String name = (String) i.next();
                String value = parameters.getProperty(name);
                urlString += (++counter == 1 ? "?" : "&") + name + "=" + value;
            }
            try {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Request-Method", "GET");
                connection.setDoInput(true);
                connection.setDoOutput(false);
                connection.connect();
                StringBuffer newData;
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    newData = new StringBuffer();
                    String s = "";
                    while (null != ((s = br.readLine()))) {
                        newData.append(s);
                    }
                }
                XStream xstream = new XStream(new DomDriver());
                Annotations.configureAliases(xstream, CepAlias.class);
                xstream.alias("webservicecep", CepAlias.class);
                CepAlias cepAlias = (CepAlias) xstream.fromXML(newData.toString());
                Dao dao = new Dao();
                Cidade cidade = cidadeDao.pesquisaCidadePorEstadoCidade(cepAlias.getUf(), cepAlias.getCidade());
                if (cidade == null) {
                    cidade = new Cidade();
                    cidade.setCidade(cepAlias.getCidade());
                    cidade.setUf(cepAlias.getUf());
                    dao.save(cidade, true);
                }
                LogradouroDao logradouroDao = new LogradouroDao();
                Logradouro logradouro = logradouroDao.pesquisaLogradouroPorDescricao(cepAlias.getTipo_logradouro());
                if (logradouro == null) {
                    logradouro = new Logradouro();
                    logradouro.setDescricao(cepAlias.getTipo_logradouro());
                    dao.save(logradouro, true);
                }
                BairroDao bairroDao = new BairroDao();
                Bairro bairro = bairroDao.pesquisaBairroPorDescricaoCliente(cepAlias.getBairro(), SessaoCliente.get().getId());
                if (bairro == null) {
                    bairro = new Bairro();
                    bairro.setDescricao(cepAlias.getBairro());
                    bairro.setCliente(null);
                    dao.save(bairro, true);
                }
                DescricaoEnderecoDao descricaoEnderecoDao = new DescricaoEnderecoDao();
                DescricaoEndereco descricaoEndereco = descricaoEnderecoDao.pesquisaDescricaoEnderecoPorDescricaoCliente(cepAlias.getLogradouro(), SessaoCliente.get().getId());
                if (descricaoEndereco == null) {
                    descricaoEndereco = new DescricaoEndereco();
                    descricaoEndereco.setDescricao(cepAlias.getLogradouro());
                    descricaoEndereco.setCliente(null);
                    dao.save(descricaoEndereco, true);
                }
                endereco = new Endereco();
                endereco.setCep(cep);
                endereco.setBairro(bairro);
                endereco.setCidade(cidade);
                endereco.setDescricaoEndereco(descricaoEndereco);
                endereco.setLogradouro(logradouro);
                List list = enderecoDao.pesquisaEndereco(endereco.getDescricaoEndereco().getId(), endereco.getCidade().getId(), endereco.getBairro().getId(), endereco.getLogradouro().getId());
                if (list.isEmpty()) {
                    if (SessaoCliente.get().getId() == 1) {
                        endereco.setCliente(null);
                    } else {
                        endereco.setCliente(SessaoCliente.get());
                    }
                    dao.save(endereco, true);
                } else {
                    endereco = new Endereco();
                    list.clear();
                }
            } catch (IOException e) {
            }
        } else {
            endereco = new Endereco();
            if (listaEnderecos.size() == 1) {
                endereco = listaEnderecos.get(0);
            } else {
                endereco.setBairro(listaEnderecos.get(0).getBairro());
            }
        }
        cep = "";
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getCepMemoria() {
        return cepMemoria;
    }

    public void setCepMemoria(String cepMemoria) {
        this.cepMemoria = cepMemoria;
    }
}
