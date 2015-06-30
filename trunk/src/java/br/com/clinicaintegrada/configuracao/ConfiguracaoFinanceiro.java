package br.com.clinicaintegrada.configuracao;

import br.com.clinicaintegrada.seguranca.Cliente;
import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "conf_financeiro")
public class ConfiguracaoFinanceiro implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "id_cliente", referencedColumnName = "id", nullable = false, unique = true)
    @OneToOne
    private Cliente cliente;
    @Column(name = "ds_mensagem_boleto", length = 1000)
    private String mensagemBoleto;

    public ConfiguracaoFinanceiro() {
        this.id = null;
        this.cliente = null;
        this.mensagemBoleto = "";
    }

    public ConfiguracaoFinanceiro(Integer id, Cliente cliente, String mensagemBoleto) {
        this.id = id;
        this.cliente = cliente;
        this.mensagemBoleto = mensagemBoleto;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getMensagemBoleto() {
        return mensagemBoleto;
    }

    public void setMensagemBoleto(String mensagemBoleto) {
        this.mensagemBoleto = mensagemBoleto;
    }

}
