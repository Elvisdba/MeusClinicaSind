package br.com.clinicaintegrada.coordenacao;

import br.com.clinicaintegrada.pessoa.Equipe;
import br.com.clinicaintegrada.utils.DataHoje;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "rot_pertence_saida")
public class PertenceSaida implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "id_pertence_entrada", referencedColumnName = "id", nullable = false)
    @OneToOne
    private PertenceEntrada pertenceEntrada;
    @JoinColumn(name = "id_responsavel", referencedColumnName = "id", nullable = false)
    @OneToOne
    private Equipe responsavel;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_saida", nullable = false)
    private Date saida;
    @Column(name = "nr_qtde", nullable = false)
    private Integer quantidade;
    @Column(name = "ds_obs", length = 255)
    private String observacao;

    public PertenceSaida() {
        this.id = -1;
        this.pertenceEntrada = null;
        this.responsavel = null;
        this.saida = new Date();
        this.quantidade = 0;
        this.observacao = "";
    }

    public PertenceSaida(Integer id, PertenceEntrada pertenceEntrada, Equipe responsavel, Date saida, Integer quantidade, String observacao) {
        this.id = id;
        this.pertenceEntrada = pertenceEntrada;
        this.responsavel = responsavel;
        this.saida = saida;
        this.quantidade = quantidade;
        this.observacao = observacao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public PertenceEntrada getPertenceEntrada() {
        return pertenceEntrada;
    }

    public void setPertenceEntrada(PertenceEntrada pertenceEntrada) {
        this.pertenceEntrada = pertenceEntrada;
    }

    public Equipe getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(Equipe responsavel) {
        this.responsavel = responsavel;
    }

    public Date getSaida() {
        return saida;
    }

    public void setSaida(Date saida) {
        this.saida = saida;
    }

    public String getSaidaString() {
        return DataHoje.converteData(saida);
    }

    public void setSaida(String saidaString) {
        this.saida = DataHoje.converte(saidaString);
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public String getQuantidadeString() {
        return "" + quantidade;
    }

    public void setQuantidadeString(String quantidadeString) {
        this.quantidade = Integer.parseInt(quantidadeString);
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    @Override
    public int hashCode() {
        int hash = 5;
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
        final PertenceSaida other = (PertenceSaida) obj;
        return true;
    }

    @Override
    public String toString() {
        return "PertenceEntrada{" + "id=" + id + ", pertenceEntrada=" + pertenceEntrada + ", responsavel=" + responsavel + ", saida=" + saida + ", quantidade=" + quantidade + ", observacao=" + observacao + '}';
    }

}
