package br.com.clinicaintegrada.fichamedica;

import br.com.clinicaintegrada.coordenacao.*;
import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "ate_atendimento")
public class Atendimento implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "id_agendamento", referencedColumnName = "id", unique = true, nullable = false)
    @OneToOne
    private Agendamento agendamento;
    @Column(name = "ds_historico", length = 200, updatable = true)
    private String historico;

    public Atendimento() {
        this.id = null;
        this.agendamento = null;
        this.historico = "";
    }

    public Atendimento(Integer id, Agendamento agendamento, String historico) {
        this.id = id;
        this.agendamento = agendamento;
        this.historico = historico;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Agendamento getAgendamento() {
        return agendamento;
    }

    public void setAgendamento(Agendamento agendamento) {
        this.agendamento = agendamento;
    }

    public String getHistorico() {
        return historico;
    }

    public void setHistorico(String historico) {
        this.historico = historico;
    }
}
