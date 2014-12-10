package br.com.clinicaintegrada.coordenacao;

import br.com.clinicaintegrada.seguranca.Usuario;
import br.com.clinicaintegrada.utils.DataHoje;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

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
    private Date lancamento;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_agenda", nullable = false)
    private Date agenda;
    @Column(name = "ds_hora_agenda", nullable = false, length = 5)
    private String horaAgenda;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_atendimento", nullable = true)
    private Date atendimento;
    @Column(name = "ds_hora_atendimento", nullable = true, length = 5)
    private String horaAtendimento;
    @Column(name = "ds_obs", length = 200)
    private String observacao;

    public Agendamento() {
        this.id = -1;
        this.contrato = new Contrato();
        this.agendador = null;
        this.evento = new Evento();
        this.lancamento = new Date();
        this.agenda = new Date();
        this.horaAgenda = "";
        this.observacao = "";
        this.atendimento = null;
        this.horaAtendimento = "";
    }

    public Agendamento(int id, Contrato contrato, Usuario agendador, Evento evento, Date lancamento, Date agenda, String horaAgenda, String observacao, Date atendimento, String horaAtendimento) {
        this.id = id;
        this.contrato = contrato;
        this.agendador = agendador;
        this.evento = evento;
        this.lancamento = lancamento;
        this.agenda = agenda;
        this.horaAgenda = horaAgenda;
        this.observacao = observacao;
        this.atendimento = atendimento;
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

    public Date getLancamento() {
        return lancamento;
    }

    public void setLancamento(Date lancamento) {
        this.lancamento = lancamento;
    }

    public String getLancamentoString() {
        return DataHoje.converteData(lancamento);
    }

    public void setLancamentoString(String lancamentoString) {
        this.lancamento = DataHoje.converte(lancamentoString);
    }

    public Date getAgenda() {
        return agenda;
    }

    public void setAgenda(Date agenda) {
        this.agenda = agenda;
    }

    public String getAgendaString() {
        return DataHoje.converteData(agenda);
    }

    public void setAgendaString(String agendaString) {
        this.agenda = DataHoje.converte(agendaString);
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

    public void setAtendimento(Date atendimento) {
        this.atendimento = atendimento;
    }

    public String getHoraAtendimento() {
        return horaAtendimento;
    }

    public void setHoraAtendimento(String horaAtendimento) {
        this.horaAtendimento = horaAtendimento;
    }

    public String getAtendimentoString() {
        return DataHoje.converteData(atendimento);
    }

    public void setAtendimentoString(String atendimentoString) {
        this.atendimento = DataHoje.converte(atendimentoString);
    }

}
