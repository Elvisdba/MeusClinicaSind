package br.com.clinicaintegrada.questionario;

import br.com.clinicaintegrada.pessoa.Pessoa;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "que_resposta",
        uniqueConstraints = @UniqueConstraint(columnNames = {"dt_lancamento", "id_pessoa", "id_resposta_fixa"})
)
public class Resposta implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_lancamento", nullable = false)
    private Date lancamento;
    @JoinColumn(name = "id_pessoa", referencedColumnName = "id", nullable = false)
    @OneToOne
    private Pessoa pessoa;
    @JoinColumn(name = "id_resposta_fixa", referencedColumnName = "id", nullable = false)
    @OneToOne
    private RespostaFixa respostaFixa;
    @Column(name = "ds_descricao", length = 255)
    private String descricao;

    public Resposta() {
        this.id = null;
        this.lancamento = null;
        this.pessoa = null;
        this.respostaFixa = null;
        this.descricao = null;
    }

    public Resposta(Integer id, Date lancamento, Pessoa pessoa, RespostaFixa respostaFixa, String descricao) {
        this.id = id;
        this.lancamento = lancamento;
        this.pessoa = pessoa;
        this.respostaFixa = respostaFixa;
        this.descricao = descricao;
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

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public RespostaFixa getRespostaFixa() {
        return respostaFixa;
    }

    public void setRespostaFixa(RespostaFixa respostaFixa) {
        this.respostaFixa = respostaFixa;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
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
        final Resposta other = (Resposta) obj;
        return true;
    }

    @Override
    public String toString() {
        return "Resposta{" + "id=" + id + ", lancamento=" + lancamento + ", pessoa=" + pessoa + ", respostaFixa=" + respostaFixa + ", descricao=" + descricao + '}';
    }

}
