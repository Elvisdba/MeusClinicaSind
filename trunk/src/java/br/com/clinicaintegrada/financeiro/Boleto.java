package br.com.clinicaintegrada.financeiro;

import br.com.clinicaintegrada.seguranca.Cliente;
import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "fin_boleto",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_conta_cobranca", "ds_boleto"})
// uniqueConstraints = @UniqueConstraint(columnNames = {"id_conta_cobranca", "nr_ctr_boleto"})
)
public class Boleto implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_conta_cobranca", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private ContaCobranca contaCobranca;
    @Column(name = "nr_boleto")
    private long nrBoleto;
    @Column(name = "ds_boleto", length = 50)
    private String boletoComposto;
    @Column(name = "nr_ctr_boleto", length = 30)
    private String nrCtrBoleto;
    @Column(name = "is_ativo", columnDefinition = "boolean default true")
    private boolean ativo;
    @JoinColumn(name = "id_cliente", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Cliente cliente;

    public Boleto() {
        this.id = -1;
        this.contaCobranca = new ContaCobranca();
        this.nrBoleto = 0;
        this.boletoComposto = "";
        this.nrCtrBoleto = "";
        this.ativo = true;
        this.cliente = new Cliente();
    }

    public Boleto(int id, ContaCobranca contaCobranca, int nrBoleto, String boletoComposto, String nrCtrBoleto, boolean ativo, Cliente cliente) {
        this.id = id;
        this.contaCobranca = contaCobranca;
        this.nrBoleto = nrBoleto;
        this.boletoComposto = boletoComposto;
        this.nrCtrBoleto = nrCtrBoleto;
        this.ativo = ativo;
        this.cliente = cliente;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ContaCobranca getContaCobranca() {
        return contaCobranca;
    }

    public void setContaCobranca(ContaCobranca contaCobranca) {
        this.contaCobranca = contaCobranca;
    }

    public long getNrBoleto() {
        return nrBoleto;
    }

    public void setNrBoleto(int nrBoleto) {
        this.nrBoleto = nrBoleto;
    }

    public String getBoletoComposto() {
        return boletoComposto;
    }

    public void setBoletoComposto(String boletoComposto) {
        this.boletoComposto = boletoComposto;
    }

    public String getNrCtrBoleto() {
        return nrCtrBoleto;
    }

    public void setNrCtrBoleto(String nrCtrBoleto) {
        this.nrCtrBoleto = nrCtrBoleto;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Boleto other = (Boleto) obj;
        return true;
    }

    @Override
    public String toString() {
        return "Boleto{" + "id=" + id + ", contaCobranca=" + contaCobranca + ", nrBoleto=" + nrBoleto + ", boletoComposto=" + boletoComposto + ", nrCtrBoleto=" + nrCtrBoleto + ", ativo=" + ativo + ", cliente=" + cliente + '}';
    }

}
