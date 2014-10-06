package br.com.clinicaintegrada.financeiro;

import br.com.clinicaintegrada.sistema.Mes;
import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "fin_indice_mensal",
        uniqueConstraints = @UniqueConstraint(columnNames = {"nr_ano", "nr_mes", "nr_valor", "id_indice"})
)
public class IndiceMensal implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "nr_ano", length = 4, nullable = false)
    private int ano;
    @JoinColumn(name = "id_mes", referencedColumnName = "id", nullable = false)
    @OneToOne
    private Mes mes;
    @Column(name = "nr_valor", nullable = false)
    private float valor;
    @JoinColumn(name = "id_indice", referencedColumnName = "id", nullable = false)
    @OneToOne
    private Indice indice;

    public IndiceMensal() {
        this.id = -1;
        this.ano = -1;
        this.mes = new Mes();
        this.valor = 0;
    }

    public IndiceMensal(int id, int ano, Mes mes, float valor) {
        this.id = id;
        this.ano = ano;
        this.mes = mes;
        this.valor = valor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public Mes getMes() {
        return mes;
    }

    public void setMes(Mes mes) {
        this.mes = mes;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public Indice getIndice() {
        return indice;
    }

    public void setIndice(Indice indice) {
        this.indice = indice;
    }
}
