package br.com.clinicaintegrada.pessoa;

import br.com.clinicaintegrada.utils.DataHoje;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "pes_classificacao_economica")
@NamedQueries({
    @NamedQuery(name = "ClassificacaoEconomica.pesquisaID", query = "SELECT CE FROM ClassificacaoEconomica AS CE WHERE CE.id = :pid"),
    @NamedQuery(name = "ClassificacaoEconomica.findAll", query = "SELECT CE FROM ClassificacaoEconomica AS CE ORDER BY CE.descricao ASC, CE.salarioMinimoInicial DESC ")
})
public class ClassificacaoEconomica implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "ds_descricao", nullable = false)
    private String descricao;
    @Column(name = "nr_salario_minimo_inicial", columnDefinition = "INTEGER DEFAULT 0", nullable = true)
    private Integer salarioMinimoInicial;
    @Column(name = "nr_salario_minimo_final", columnDefinition = "INTEGER DEFAULT 0", nullable = true)
    private Integer salarioMinimoFinal;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_atualizado")
    private Date atualizado;

    public ClassificacaoEconomica() {
        this.id = -1;
        this.descricao = "";
        this.salarioMinimoInicial = 0;
        this.salarioMinimoFinal = 0;
        this.atualizado = new Date();
    }

    public ClassificacaoEconomica(Integer id, String descricao, Integer salarioMinimoInicial, Integer salarioMinimoFinal, Date atualizado) {
        this.id = id;
        this.descricao = descricao;
        this.salarioMinimoInicial = salarioMinimoInicial;
        this.salarioMinimoFinal = salarioMinimoFinal;
        this.atualizado = atualizado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getSalarioMinimoInicial() {
        return salarioMinimoInicial;
    }

    public void setSalarioMinimoInicial(Integer salarioMinimoInicial) {
        this.salarioMinimoInicial = salarioMinimoInicial;
    }

    public Integer getSalarioMinimoFinal() {
        return salarioMinimoFinal;
    }

    public void setSalarioMinimoFinal(Integer salarioMinimoFinal) {
        this.salarioMinimoFinal = salarioMinimoFinal;
    }

    public Date getAtualizado() {
        return atualizado;
    }

    public void setAtualizado(Date atualizado) {
        this.atualizado = atualizado;
    }

    public String getAtualizadoString() {
        return DataHoje.converteData(atualizado);
    }

    public void setAtualizado(String atualizadoString) {
        this.atualizado = DataHoje.converte(atualizadoString);
    }

}
