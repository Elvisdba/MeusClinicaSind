package br.com.clinicaintegrada.coordenacao;

import br.com.clinicaintegrada.seguranca.Usuario;
import br.com.clinicaintegrada.utils.DataHoje;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.*;
import org.primefaces.event.SelectEvent;

@Entity
@Table(name = "rot_agendamento",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_contrato", "dt_agenda", "ds_hora_agenda"})
)
public class Agendamento implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_contrato", referencedColumnName = "id", nullable = false)
    @OneToOne
    private Contrato contrato;
    @JoinColumn(name = "id_agendador", referencedColumnName = "id", nullable = true)
    @OneToOne
    private Usuario agendador;
    @JoinColumn(name = "id_evento", referencedColumnName = "id", nullable = false)
    @OneToOne
    private Evento evento;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_lancamento", nullable = false)
    private Date dataLancamento;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_agenda", nullable = false)
    private Date dataAgenda;
    @Column(name = "ds_hora_agenda", nullable = false, length = 5)
    private String horaAgenda;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_atendimento", nullable = true)
    private Date dataAtendimento;
    @Column(name = "ds_hora_atendimento", nullable = true, length = 5)
    private String horaAtendimento;
    @Column(name = "ds_obs", length = 200)
    private String observacao;

    public Agendamento() {
        this.id = -1;
        this.contrato = new Contrato();
        this.agendador = null;
        this.evento = new Evento();
        this.dataLancamento = new Date();
        this.dataAgenda = new Date();
        this.horaAgenda = "";
        this.observacao = "";
        this.dataAtendimento = null;
        this.horaAtendimento = "";
    }

    public Agendamento(int id, Contrato contrato, Usuario agendador, Evento evento, Date lancamento, Date agenda, String horaAgenda, String observacao, Date atendimento, String horaAtendimento) {
        this.id = id;
        this.contrato = contrato;
        this.agendador = agendador;
        this.evento = evento;
        this.dataLancamento = lancamento;
        this.dataAgenda = agenda;
        this.horaAgenda = horaAgenda;
        this.observacao = observacao;
        this.dataAtendimento = atendimento;
        this.horaAtendimento = horaAtendimento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Contrato getContrato() {
        return contrato;
    }

    public void setContrato(Contrato contrato) {
        this.contrato = contrato;
    }

    public Usuario getAgendador() {
        return agendador;
    }

    public void setAgendador(Usuario agendador) {
        this.agendador = agendador;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
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

    public Date getDataAgenda() {
        return dataAgenda;
    }

    public void setDataAgenda(Date dataAgenda) {
        this.dataAgenda = dataAgenda;
    }

    public String getDataAgendaString() {
        return DataHoje.converteData(dataAgenda);
    }

    public void setDataAgendaString(String agendaString) {
        this.dataAgenda = DataHoje.converte(agendaString);
    }

    public String getHoraAgenda() {
        return horaAgenda;
    }

    public void setHoraAgenda(String horaAgenda) {
        this.horaAgenda = horaAgenda;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public void setDataAtendimento(Date dataAtendimento) {
        this.dataAtendimento = dataAtendimento;
    }

    public String getHoraAtendimento() {
        return horaAtendimento;
    }

    public void setHoraAtendimento(String horaAtendimento) {
        this.horaAtendimento = horaAtendimento;
    }

    public String getDataAtendimentoString() {
        return DataHoje.converteData(dataAtendimento);
    }

    public void setDataAtendimentoString(String dataAtendimentoString) {
        this.dataAtendimento = DataHoje.converte(dataAtendimentoString);
    }

    public void selectedDataLancamento(SelectEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy");
        this.dataLancamento = DataHoje.converte(format.format(event.getObject()));
    }

    public void selectedDataAgenda(SelectEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy");
        this.dataAgenda = DataHoje.converte(format.format(event.getObject()));
    }

    public void selectedDataAtendimento(SelectEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy");
        this.dataAtendimento = DataHoje.converte(format.format(event.getObject()));
    }

}
