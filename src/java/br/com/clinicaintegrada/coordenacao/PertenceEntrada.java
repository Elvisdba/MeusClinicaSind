package br.com.clinicaintegrada.coordenacao;

import br.com.clinicaintegrada.pessoa.Equipe;
import br.com.clinicaintegrada.utils.DataHoje;
import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "rot_pertence_entrada",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_contrato", "ds_descricao"})
)
public class PertenceEntrada implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "id_contrato", referencedColumnName = "id", nullable = false)
    @OneToOne
    private Contrato contrato;
    @JoinColumn(name = "id_responsavel", referencedColumnName = "id", nullable = false)
    @OneToOne
    private Equipe responsavel;
    @JoinColumn(name = "id_pertence_grupo", referencedColumnName = "id", nullable = true)
    @OneToOne
    private PertenceGrupo pertenceGrupo;
    @Column(name = "ds_descricao", length = 255, nullable = false)
    private String descricao;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_entrada", nullable = false)
    private Date entrada;
    @Column(name = "nr_qtde", nullable = false)
    private Integer quantidade;
    @Column(name = "ds_obs", length = 255)
    private String observacao;

    public PertenceEntrada() {
        this.id = -1;
        this.contrato = null;
        this.responsavel = null;
        this.pertenceGrupo = null;
        this.descricao = "";
        this.entrada = new Date();
        this.quantidade = 0;
        this.observacao = "";
    }

    public PertenceEntrada(Integer id, Contrato contrato, Equipe responsavel, PertenceGrupo pertenceGrupo, String descricao, Date entrada, Integer quantidade, String observacao) {
        this.id = id;
        this.contrato = contrato;
        this.responsavel = responsavel;
        this.pertenceGrupo = pertenceGrupo;
        this.descricao = descricao;
        this.entrada = entrada;
        this.quantidade = quantidade;
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

    public Equipe getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(Equipe responsavel) {
        this.responsavel = responsavel;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getEntrada() {
        return entrada;
    }

    public void setEntrada(Date entrada) {
        this.entrada = entrada;
    }

    public String getEntradaString() {
        return DataHoje.converteData(entrada);
    }

    public void setEntradaString(String entradaString) {
        this.entrada = DataHoje.converte(entradaString);
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
        final PertenceEntrada other = (PertenceEntrada) obj;
        return true;
    }

    @Override
    public String toString() {
        return "PertenceEntrada{" + "id=" + id + ", contrato=" + contrato + ", responsavel=" + responsavel + ", descricao=" + descricao + ", entrada=" + entrada + ", quantidade=" + quantidade + ", observacao=" + observacao + '}';
    }

    public PertenceGrupo getPertenceGrupo() {
        return pertenceGrupo;
    }

    public void setPertenceGrupo(PertenceGrupo pertenceGrupo) {
        this.pertenceGrupo = pertenceGrupo;
    }

}
