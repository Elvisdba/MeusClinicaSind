package br.com.clinicaintegrada.financeiro;

import br.com.clinicaintegrada.seguranca.Cliente;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "fin_plano",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_cliente", "ds_numero"})
)
public class Plano implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "ds_numero", length = 10, nullable = false)
    private String numero;
    @Column(name = "ds_conta", length = 200, nullable = false)
    private String conta;
    @Column(name = "ds_acesso", length = 10)
    private String acesso;
    @Column(name = "ds_classificador", length = 20)
    private String classificador;
    @JoinColumn(name = "id_cliente", referencedColumnName = "id", nullable = false)
    @OneToOne
    private Cliente cliente;

    public Plano() {
        this.id = -1;
        this.numero = "";
        this.conta = "";
        this.acesso = "";
        this.classificador = "";
        this.cliente = new Cliente();
    }

    public Plano(int id, String numero, String conta, String acesso, String classificador, Cliente cliente) {
        this.id = id;
        this.numero = numero;
        this.conta = conta;
        this.acesso = acesso;
        this.classificador = classificador;
        this.cliente = cliente;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getConta() {
        return conta;
    }

    public void setConta(String conta) {
        this.conta = conta;
    }

    public String getAcesso() {
        return acesso;
    }

    public void setAcesso(String acesso) {
        this.acesso = acesso;
    }

    public String getClassificador() {
        return classificador;
    }

    public void setClassificador(String classificador) {
        this.classificador = classificador;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}
