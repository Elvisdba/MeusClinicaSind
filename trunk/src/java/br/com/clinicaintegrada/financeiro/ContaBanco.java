package br.com.clinicaintegrada.financeiro;

import br.com.clinicaintegrada.endereco.Cidade;
import br.com.clinicaintegrada.pessoa.Filial;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "fin_conta_banco",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_banco", "ds_agencia", "ds_conta"})
)
public class ContaBanco implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_banco", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Banco banco;
    @Column(name = "ds_agencia", length = 100, nullable = false)
    private String agencia;
    @Column(name = "ds_conta", length = 100, nullable = false)
    private String conta;
    @JoinColumn(name = "id_cidade", referencedColumnName = "id", nullable = false)
    @OneToOne
    private Cidade cidade;
    @Column(name = "nr_ultimo_cheque", length = 38, nullable = false)
    private int uCheque;
    @JoinColumn(name = "id_filial", referencedColumnName = "id", nullable = true)
    @OneToOne
    private Filial filial;

    public ContaBanco() {
        this.id = -1;
        this.banco = new Banco();
        this.agencia = "";
        this.conta = "";
        this.cidade = new Cidade();
        this.uCheque = 0;
        this.filial = new Filial();
    }

    public ContaBanco(int id, Banco banco, String agencia, String conta, Cidade cidade, int uCheque, Filial filial) {
        this.id = id;
        this.banco = banco;
        this.agencia = agencia;
        this.conta = conta;
        this.cidade = cidade;
        this.uCheque = uCheque;
        this.filial = filial;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Banco getBanco() {
        return banco;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
    }

    public String getAgencia() {
        return agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public String getConta() {
        return conta;
    }

    public void setConta(String conta) {
        this.conta = conta;
    }

    public Cidade getCidade() {
        return cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }

    public int getUCheque() {
        return uCheque;
    }

    public void setUCheque(int uCheque) {
        this.uCheque = uCheque;
    }

    public Filial getFilial() {
        return filial;
    }

    public void setFilial(Filial filial) {
        this.filial = filial;
    }
}
