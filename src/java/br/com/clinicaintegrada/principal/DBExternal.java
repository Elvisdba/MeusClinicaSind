package br.com.clinicaintegrada.principal;

import br.com.clinicaintegrada.seguranca.controleUsuario.SessaoCliente;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DBExternal {

    private Statement statment;
    private String url = "192.168.1.160";
    private String port = "5434";
    private String database = "";
    private String user = "postgres";
    private String password = "989899";

    public Connection getConnection() {
        try {
            String dataBase = "";
            if(database.isEmpty()) {
                if (SessaoCliente.get().getIdentifica().equals("ClinicaIntegradaProducao")) {
                    dataBase = "ClinicaIntegradaProducao";
                } else {
                    dataBase = SessaoCliente.get().getPersistence();
                }                
            }
            String uri = "jdbc:postgresql://" + this.url + ":" + port + "/" + dataBase;
            Properties props = new Properties();
            props.setProperty("user", user);
            props.setProperty("password", password);
            //props.setProperty("ssl", "true");
            Connection conn = DriverManager.getConnection(uri, props);
            return conn;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public Statement getStatment() throws SQLException {
        statment = getConnection().createStatement();
        return statment;
    }

    public void setStatment(Statement statment) {
        this.statment = statment;
    }

    public void closeStatment() throws SQLException {
        getConnection().close();
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
