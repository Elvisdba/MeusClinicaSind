package br.com.clinicaintegrada.financeiro;

import br.com.clinicaintegrada.utils.DataHoje;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "fin_mensagem_boleto")
public class MensagemBoleto implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_servicos", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Servicos servicos;
    @JoinColumn(name = "id_tipo_servico", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private TipoServico tipoServico;
    @Column(name = "ds_mensagem", length = 2000, nullable = false)
    private String mensagem;
    @Column(name = "ds_mensagem_compensacao", length = 2000, nullable = false)
    private String mensagemCompensacao;
    @Column(name = "ds_referencia", length = 7, nullable = true)
    private String referencia;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_vencimento")
    private Date vencimento;

    public MensagemBoleto() {
        this.id = -1;
        this.servicos = null;
        this.tipoServico = null;
        this.mensagem = "";
        this.mensagemCompensacao = "";
        this.referencia = "";
        this.vencimento = new Date();
    }

    public MensagemBoleto(int id, Servicos servicos, TipoServico tipoServico, String mensagem, String mensagemCompensacao, String referencia, Date vencimento) {
        this.id = id;
        this.servicos = servicos;
        this.tipoServico = tipoServico;
        this.mensagem = mensagem;
        this.mensagemCompensacao = mensagemCompensacao;
        this.referencia = referencia;
        this.vencimento = vencimento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Servicos getServicos() {
        return servicos;
    }

    public void setServicos(Servicos servicos) {
        this.servicos = servicos;
    }

    public TipoServico getTipoServico() {
        return tipoServico;
    }

    public void setTipoServico(TipoServico tipoServico) {
        this.tipoServico = tipoServico;
    }

    public String getMensagemCompensacao() {
        return mensagemCompensacao;
    }

    public void setMensagemCompensacao(String mensagemCompensacao) {
        this.mensagemCompensacao = mensagemCompensacao;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public Date getVencimento() {
        return vencimento;
    }

    public void setVencimento(Date dtVencimento) {
        this.vencimento = dtVencimento;
    }

    public String getVencimentoString() {
        return DataHoje.converteData(vencimento);
    }

    public void setVencimentoString(String vencimento) {
        this.vencimento = DataHoje.converte(vencimento);
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
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
        final MensagemBoleto other = (MensagemBoleto) obj;
        return true;
    }

    @Override
    public String toString() {
        return "MensagemBoleto{" + "id=" + id + ", servicos=" + servicos + ", tipoServico=" + tipoServico + ", mensagem=" + mensagem + ", mensagemCompensacao=" + mensagemCompensacao + ", referencia=" + referencia + ", vencimento=" + vencimento + '}';
    }

}
