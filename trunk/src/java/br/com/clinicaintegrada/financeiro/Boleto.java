package br.com.clinicaintegrada.financeiro;

import br.com.clinicaintegrada.seguranca.Cliente;
import java.io.Serializable;
import java.util.Date;
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
    private Integer id;
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
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_vencimento")
    private Date dtVencimento;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_vencimento_original")
    private Date dtVencimentoOriginal;
    @Column(name = "ds_mensagem", length = 1000)
    private String mensagem;

    public Boleto() {
        this.id = -1;
        this.contaCobranca = new ContaCobranca();
        this.nrBoleto = 0;
        this.boletoComposto = "";
        this.nrCtrBoleto = "";
        this.ativo = true;
        this.cliente = new Cliente();
        this.dtVencimento = null;
        this.dtVencimentoOriginal = null;
        this.mensagem = null;
    }

    public Boleto(Integer id, ContaCobranca contaCobranca, int nrBoleto, String boletoComposto, String nrCtrBoleto, boolean ativo, Cliente cliente, Date dtVencimento, Date dtVencimentoOriginal, String mensagem) {
        this.id = id;
        this.contaCobranca = contaCobranca;
        this.nrBoleto = nrBoleto;
        this.boletoComposto = boletoComposto;
        this.nrCtrBoleto = nrCtrBoleto;
        this.ativo = ativo;
        this.cliente = cliente;
        this.dtVencimento = dtVencimento;
        this.dtVencimentoOriginal = dtVencimentoOriginal;
        this.mensagem = mensagem;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public void setNrBoleto(long nrBoleto) {
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

    public Date getDtVencimento() {
        return dtVencimento;
    }

    public void setDtVencimento(Date dtVencimento) {
        this.dtVencimento = dtVencimento;
    }

    public Date getDtVencimentoOriginal() {
        return dtVencimentoOriginal;
    }

    public void setDtVencimentoOriginal(Date dtVencimentoOriginal) {
        this.dtVencimentoOriginal = dtVencimentoOriginal;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

}
