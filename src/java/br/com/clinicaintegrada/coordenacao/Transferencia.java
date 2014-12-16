package br.com.clinicaintegrada.coordenacao;

import br.com.clinicaintegrada.pessoa.Equipe;
import br.com.clinicaintegrada.pessoa.Filial;
import br.com.clinicaintegrada.utils.DataHoje;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.*;
import org.primefaces.event.SelectEvent;

@Entity
@Table(name = "rot_transferencia",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"id_contrato", "id_filial_atual", "id_filial_destino", "dt_saida"})
        }
)
public class Transferencia implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "id_contrato", referencedColumnName = "id", nullable = false)
    @OneToOne
    private Contrato contrato;
    @JoinColumn(name = "id_filial_atual", referencedColumnName = "id", nullable = false)
    @OneToOne
    private Filial filialAtual;
    @JoinColumn(name = "id_filial_destino", referencedColumnName = "id", nullable = false)
    @OneToOne
    private Filial filialDestino;
    @JoinColumn(name = "id_equipe", referencedColumnName = "id", nullable = false)
    @OneToOne
    private Equipe equipe;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_lancamento")
    private Date dataLancamento;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_saida")
    private Date dataSaida;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_chedaga")
    private Date dataChegada;
    @Column(name = "ds_hora_saida", nullable = false, length = 5)
    private String horaSaida;
    @Column(name = "ds_hora_chegada", nullable = false, length = 5)
    private String horaChegada;
    @Column(name = "ds_obs", length = 3000)
    private String observacao;

    public Transferencia() {
        this.id = -1;
        this.contrato = null;
        this.filialAtual = null;
        this.filialDestino = null;
        this.equipe = null;
        this.dataSaida = DataHoje.dataHoje();
        this.dataLancamento = DataHoje.dataHoje();
        this.dataChegada = null;
        this.horaSaida = "";
        this.horaChegada = "";
        this.observacao = "";
    }

    public Transferencia(Integer id, Contrato contrato, Filial filialAtual, Filial filialDestino, Equipe equipe, Date dataLancamento, Date dataSaida, Date dataChegada, String horaSaida, String horaChegada, String observacao) {
        this.id = id;
        this.contrato = contrato;
        this.filialAtual = filialAtual;
        this.filialDestino = filialDestino;
        this.equipe = equipe;
        this.dataLancamento = dataLancamento;
        this.dataSaida = dataSaida;
        this.dataChegada = dataChegada;
        this.horaSaida = horaSaida;
        this.horaChegada = horaChegada;
        this.observacao = observacao;
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

    public Filial getFilialAtual() {
        return filialAtual;
    }

    public void setFilialAtual(Filial filialAtual) {
        this.filialAtual = filialAtual;
    }

    public Filial getFilialDestino() {
        return filialDestino;
    }

    public void setFilialDestino(Filial filialDestino) {
        this.filialDestino = filialDestino;
    }

    public Equipe getEquipe() {
        return equipe;
    }

    public void setEquipe(Equipe equipe) {
        this.equipe = equipe;
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

    public void setDataLancamentoString(String dataLancamentoString) {
        this.dataLancamento = DataHoje.converte(dataLancamentoString);
    }

    public Date getDataSaida() {
        return dataSaida;
    }

    public void setDataSaida(Date dataSaida) {
        this.dataSaida = dataSaida;
    }

    public String getDataSaidaString() {
        return DataHoje.converteData(dataSaida);
    }

    public void setDataSaidaString(String dataSaidaString) {
        this.dataSaida = DataHoje.converte(dataSaidaString);
    }

    public Date getDataChegada() {
        return dataChegada;
    }

    public void setDataChegada(Date dataChegada) {
        this.dataChegada = dataChegada;
    }

    public String getDataChegadaString() {
        return DataHoje.converteData(dataChegada);            
    }

    public void setDataChegadaString(String dataChegadaString) {
        this.dataChegada = DataHoje.converte(dataChegadaString);
    }

    public String getHoraSaida() {
        return horaSaida;
    }

    public void setHoraSaida(String horaSaida) {
        this.horaSaida = horaSaida;
    }

    public String getHoraChegada() {
        return horaChegada;
    }

    public void setHoraChegada(String horaChegada) {
        this.horaChegada = horaChegada;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
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
        final Transferencia other = (Transferencia) obj;
        return true;
    }

    @Override
    public String toString() {
        return "Transferencia{" + "id=" + id + ", contrato=" + contrato + ", filialAtual=" + filialAtual + ", filialDestino=" + filialDestino + ", equipe=" + equipe + ", dataLancamento=" + dataLancamento + ", dataSaida=" + dataSaida + ", dataChegada=" + dataChegada + ", horaSaida=" + horaSaida + ", horaChegada=" + horaChegada + ", observacao=" + observacao + '}';
    }

    public void selectedDataSaida(SelectEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy");
        this.dataSaida = DataHoje.converte(format.format(event.getObject()));
    }

    public void selectedDataChegada(SelectEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy");
        this.dataChegada = DataHoje.converte(format.format(event.getObject()));
    }

    public void selectedDataLancamento(SelectEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy");
        this.dataLancamento = DataHoje.converte(format.format(event.getObject()));
    }

}
