package br.com.clinicaintegrada.coordenacao;

import br.com.clinicaintegrada.pessoa.Equipe;
import br.com.clinicaintegrada.utils.DataHoje;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.*;
import org.primefaces.event.SelectEvent;

@Entity
@Table(name = "rot_notificacao",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"id_contrato", "id_equipe", "id_tipo_notificacao", "dt_lancamento"})}
)
public class Notificacao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "id_contrato", referencedColumnName = "id")
    @OneToOne
    private Contrato contrato;
    @JoinColumn(name = "id_equipe", referencedColumnName = "id")
    @OneToOne
    private Equipe equipe;
    @JoinColumn(name = "id_tipo_notificacao", referencedColumnName = "id", nullable = false)
    @OneToOne
    private TipoNotificacao tipoNotificacao;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_lancamento", nullable = false)
    private Date dataLancamento;
    @Column(name = "ds_motivo", length = 200)
    private String motivo;

    public Notificacao() {
        this.id = -1;
        this.contrato = null;
        this.equipe = null;
        this.dataLancamento = new Date();
        this.tipoNotificacao = null;
        this.motivo = "";
    }

    public Notificacao(Integer id, Contrato contrato, Equipe equipe, TipoNotificacao tipoNotificacao, Date dataLancamento, String motivo) {
        this.id = id;
        this.contrato = contrato;
        this.equipe = equipe;
        this.tipoNotificacao = tipoNotificacao;
        this.dataLancamento = dataLancamento;
        this.motivo = motivo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Contrato getContrato() {
        return contrato;
    }

    public void setContrato(Contrato contrato) {
        this.contrato = contrato;
    }

    public Date getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(Date dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    public String getDataLancamentoString() {
        return DataHoje.converteData(dataLancamento);
    }

    public void setDataLancamentoString(String lancamentoString) {
        this.dataLancamento = DataHoje.converte(lancamentoString);
    }

    public Equipe getEquipe() {
        return equipe;
    }

    public void setEquipe(Equipe equipe) {
        this.equipe = equipe;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public void selectedDataLancamento(SelectEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy");
        this.dataLancamento = DataHoje.converte(format.format(event.getObject()));
    }

    public TipoNotificacao getTipoNotificacao() {
        return tipoNotificacao;
    }

    public void setTipoNotificacao(TipoNotificacao tipoNotificacao) {
        this.tipoNotificacao = tipoNotificacao;
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
        final Notificacao other = (Notificacao) obj;
        return true;
    }

    @Override
    public String toString() {
        return "Notificacao{" + "id=" + id + ", contrato=" + contrato + ", equipe=" + equipe + ", tipoNotificacao=" + tipoNotificacao + ", dataLancamento=" + dataLancamento + ", motivo=" + motivo + '}';
    }

}
