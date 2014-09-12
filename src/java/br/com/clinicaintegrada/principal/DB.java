package br.com.clinicaintegrada.principal;

import br.com.clinicaintegrada.seguranca.Cliente;
import br.com.clinicaintegrada.utils.Request;
import br.com.clinicaintegrada.utils.Strings;
import br.com.clinicaintegrada.utils.Sessions;
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
                String clienteString = ((Cliente) Sessions.getObject("sessaoCliente")).getIdentifica();
                Cliente cliente = servidor(clienteString);
                try {
                    Map<String, String> properties = new HashMap<>();
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
//        String clienteName = cliente.getIdentifica();
//        if (clienteName.equals("ClinicaIntegradaProducao")) {
//        } else {
//        }

        /**
         *
         * BASE TESTE
         *
         */
        cliente.setCaminhoSistema("ClinicaIntegrada");
        cliente.setPersistence("ClinicaIntegradaProducao");
        cliente.setHost("192.168.1.60");
        cliente.setSenha("989899");
        /**
         *
         * BASE OFICIAL
         *
         */
//        cliente.setCaminhoSistema("ClinicaIntegrada");
//        cliente.setPersistence("ClinicaIntegrada");
//        cliente.setHost("192.168.1.60");
//        cliente.setSenha("989899");
        return cliente;
    }
}