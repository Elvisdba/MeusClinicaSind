package br.com.clinicaintegrada.questionario;

import br.com.clinicaintegrada.pessoa.Equipe;
import br.com.clinicaintegrada.pessoa.Pessoa;
import br.com.clinicaintegrada.utils.DataHoje;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "que_resposta_lote",
        uniqueConstraints = @UniqueConstraint(columnNames = {"dt_lancamento", "id_pessoa", "id_questionario"})
)
public class RespostaLote implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_lancamento", nullable = false)
    private Date lancamento;
    @JoinColumn(name = "id_equipe", referencedColumnName = "id", nullable = false)
    @OneToOne
    private Equipe equipe;
    @JoinColumn(name = "id_pessoa", referencedColumnName = "id", nullable = false)
    @OneToOne
    private Pessoa pessoa;
    @JoinColumn(name = "id_questionario", referencedColumnName = "id", nullable = false)
    @OneToOne
    private Questionario questionario;
    @Column(name = "ds_hora", length = 255)
    private String hora;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "dt_atualizacao", nullable = true)
    private Date atualizacao;

    public RespostaLote() {
        this.id = null;
        this.lancamento = DataHoje.dataHoje();
        this.equipe = null;
        this.pessoa = null;
        this.questionario = null;
        this.hora = DataHoje.hora();
        this.atualizacao = null;
    }

    public RespostaLote(Integer id, Date lancamento, Equipe equipe, Pessoa pessoa, Questionario questionario, String hora, Date atualizacao) {
        this.id = id;
        this.lancamento = lancamento;
        this.equipe = equipe;
        this.pessoa = pessoa;
        this.questionario = questionario;
        this.hora = hora;
        this.atualizacao = atualizacao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Equipe getEquipe() {
        return equipe;
    }

    public void setEquipe(Equipe equipe) {
        this.equipe = equipe;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public Questionario getQuestionario() {
        return questionario;
    }

    public void setQuestionario(Questionario questionario) {
        this.questionario = questionario;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public Date getAtualizacao() {
        return atualizacao;
    }

    public void setAtualizacao(Date atualizacao) {
        this.atualizacao = atualizacao;
    }

    public String getAtualizacaoString() {
        return DataHoje.converteData(atualizacao);
    }

    public void setAtualizacaoString(String atualizacaoString) {
        this.atualizacao = DataHoje.converte(atualizacaoString);
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
        final RespostaLote other = (RespostaLote) obj;
        return true;
    }

    @Override
    public String toString() {
        return "RespostaLote{" + "id=" + id + ", lancamento=" + lancamento + ", equipe=" + equipe + ", pessoa=" + pessoa + ", questionario=" + questionario + ", hora=" + hora + ", atualizacao=" + atualizacao + '}';
    }

}
