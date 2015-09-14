package br.com.clinicaintegrada.questionario;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "que_resposta",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_lote", "id_resposta_fixa"})
)
public class Resposta implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "id_resposta_lote", referencedColumnName = "id", nullable = false)
    @OneToOne
    private RespostaLote respostaLote;
    @JoinColumn(name = "id_resposta_fixa", referencedColumnName = "id", nullable = false)
    @OneToOne
    private RespostaFixa respostaFixa;
    @Column(name = "ds_descricao", length = 255)
    private String descricao;

    public Resposta() {
        this.id = null;
        this.respostaLote = null;
        this.respostaFixa = null;
        this.descricao = "";
    }

    public Resposta(Integer id, RespostaLote respostaLote, RespostaFixa respostaFixa, String descricao) {
        this.id = id;
        this.respostaLote = respostaLote;
        this.respostaFixa = respostaFixa;
        this.descricao = descricao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public RespostaLote getRespostaLote() {
        return respostaLote;
    }

    public void setRespostaLote(RespostaLote respostaLote) {
        this.respostaLote = respostaLote;
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
        final Resposta other = (Resposta) obj;
        return true;
    }

    @Override
    public String toString() {
        return "Resposta{" + "id=" + id + ", respostaLote=" + respostaLote + ", respostaFixa=" + respostaFixa + ", descricao=" + descricao + '}';
    }

}
