package br.com.clinicaintegrada.fichamedica;

import br.com.clinicaintegrada.coordenacao.*;
import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "ate_atendimento",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_atendimento", "id_avaliacao"})
)
public class AtendimentoAvaliacao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "id_atendimento", referencedColumnName = "id", nullable = false)
    @OneToOne
    private Atendimento atendimento;
    @JoinColumn(name = "id_avaliacao", referencedColumnName = "id", nullable = false)
    @OneToOne
    private Avaliacao avaliacao;
    @Column(name = "ds_historico", length = 500, updatable = true)
    private String historico;

    public AtendimentoAvaliacao() {
        this.id = null;
        this.atendimento = null;
        this.avaliacao = null;
        this.historico = "";
    }

    public AtendimentoAvaliacao(Integer id, Atendimento atendimento, Avaliacao avaliacao, String historico) {
        this.id = id;
        this.atendimento = atendimento;
        this.avaliacao = avaliacao;
        this.historico = historico;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Atendimento getAtendimento() {
        return atendimento;
    }

    public void setAtendimento(Atendimento atendimento) {
        this.atendimento = atendimento;
    }

    public Avaliacao getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Avaliacao avaliacao) {
        this.avaliacao = avaliacao;
    }

    public String getHistorico() {
        return historico;
    }

    public void setHistorico(String historico) {
        this.historico = historico;
    }

    @Override
    public int hashCode() {
        int hash = 3;
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
        final AtendimentoAvaliacao other = (AtendimentoAvaliacao) obj;
        return true;
    }

    @Override
    public String toString() {
        return "AtendimentoAvaliacao{" + "id=" + id + ", atendimento=" + atendimento + ", avaliacao=" + avaliacao + ", historico=" + historico + '}';
    }

}
