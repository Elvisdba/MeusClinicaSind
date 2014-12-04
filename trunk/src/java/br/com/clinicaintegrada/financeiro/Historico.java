package br.com.clinicaintegrada.financeiro;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "fin_historico")
public class Historico implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_movimento", referencedColumnName = "id", nullable = false)
    @OneToOne
    private Movimento movimento;
    @Column(name = "ds_historico", length = 2000, nullable = false)
    private String historico;
    @Column(name = "ds_complemento", length = 2000, nullable = true)
    private String complemento;

    public Historico() {
        this.id = -1;
        this.movimento = new Movimento();
        this.historico = "";
        this.complemento = "";
    }

    public Historico(int id, Movimento movimento, String historico, String complemento) {
        this.id = id;
        this.movimento = movimento;
        this.historico = historico;
        this.complemento = complemento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Movimento getMovimento() {
        return movimento;
    }

    public void setMovimento(Movimento movimento) {
        this.movimento = movimento;
    }

    public String getHistorico() {
        return historico;
    }

    public void setHistorico(String historico) {
        this.historico = historico;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
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
        final Historico other = (Historico) obj;
        return true;
    }

    @Override
    public String toString() {
        return "Historico{" + "id=" + id + ", movimento=" + movimento + ", historico=" + historico + ", complemento=" + complemento + '}';
    }

}
