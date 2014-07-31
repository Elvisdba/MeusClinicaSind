package br.com.rtools.principal;

import br.com.rtools.seguranca.Cliente;
import br.com.rtools.utilitarios.Request;
import br.com.rtools.utilitarios.Strings;
import br.com.rtools.utilitarios.Sessions;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import oracle.toplink.essentials.config.CacheType;
import oracle.toplink.essentials.config.TopLinkProperties;
import oracle.toplink.essentials.ejb.cmp3.EntityManagerFactoryProvider;

public class DB {

    private EntityManager entidade;
    private Statement statement;

    public EntityManager getEntityManager() {
        if (entidade == null) {
            if (!Sessions.exists("conexao")) {
                //String clienteString = ((Cliente) Sessions.getObject("sessaoCliente")).getIdentifica();
                Cliente cliente = servidor("ClinicaIntegrada");
                try {
                    Map<String, String> properties = new HashMap<String, String>();
                    String jdbc_user = TopLinkProperties.JDBC_USER;
                    properties.put(TopLinkProperties.CACHE_TYPE_DEFAULT, CacheType.Full);
                    properties.put(jdbc_user, "postgres");
                    properties.put(TopLinkProperties.TRANSACTION_TYPE, "RESOURCE_LOCAL");
                    properties.put(TopLinkProperties.JDBC_DRIVER, "org.postgresql.Driver");
                    properties.put(TopLinkProperties.JDBC_PASSWORD, cliente.getSenha());
                    properties.put(TopLinkProperties.JDBC_URL, "jdbc:postgresql://" + cliente.getHost() + ":5432/" + cliente.getPersistence());
                    EntityManagerFactory emf = Persistence.createEntityManagerFactory(cliente.getPersistence(), properties);
                    String createTable = Strings.converterNullToString(Request.getParam("createTable"));
                    if (createTable.equals("criar")) {
                        properties.put(EntityManagerFactoryProvider.DDL_GENERATION, EntityManagerFactoryProvider.CREATE_ONLY);
                    }
                    entidade = emf.createEntityManager();
                    Sessions.put("conexao", emf);
                } catch (Exception e) {
                    return null;
                }
            } else {
                try {
                    EntityManagerFactory emf = (EntityManagerFactory) Sessions.getObject("conexao");
                    entidade = emf.createEntityManager();
                } catch (Exception e) {
                    return null;
                }
            }
        }
        return entidade;
    }

    public Cliente servidor(String clienteString) {
        Cliente cliente = new Cliente();
        String clienteName = cliente.getIdentifica();
        if (clienteName.equals("Definir")) {
            cliente.setCaminhoSistema(clienteString);
            cliente.setPersistence(clienteString);
            cliente.setHost("192.168.1.102");
            cliente.setSenha("r#@tools");
        } else if (clienteName.equals("Rtools")) {
            cliente.setCaminhoSistema(clienteString);
            cliente.setPersistence(clienteString);
            cliente.setHost("192.168.1.102");
            cliente.setSenha("r#@tools");
        } else {
            clienteString = "ClinicaIntegrada";
            cliente.setHost("192.168.1.60");
            cliente.setSenha("989899");
//            } else {
//                if (cliente.equals("ServidoresRP")) {
//                    configuracao.setHost("localhost");
//                    configuracao.setSenha("989899");
//                }
//            }
            cliente.setCaminhoSistema(clienteString);
            cliente.setPersistence(clienteString);
        }
        return cliente;
    }
}
