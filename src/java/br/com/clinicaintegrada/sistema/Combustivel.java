package br.com.clinicaintegrada.sistema;

import br.com.clinicaintegrada.utils.Moeda;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.*;

@Entity
@Table(name = "sis_combustivel")
@NamedQueries({
    @NamedQuery(name = "Combustivel.findAll", query = "SELECT TC FROM Combustivel AS TC ORDER BY TC.descricao ASC "),    
    @NamedQuery(name = "Combustivel.findName", query = "SELECT TC FROM Combustivel AS TC WHERE UPPER(TC.descricao) LIKE :pdescricao ORDER BY TC.descricao ASC ")
})
public class Combustivel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "ds_descricao", length = 25, unique = true)
    private String descricao;
    @Column(name = "nr_valor_litro")
    private float valorLitro;

    public Combustivel() {
        this.id = -1;
        this.descricao = "";
        this.valorLitro = 0;
    }

    public Combustivel(int id, String descricao, float valorLitro) {
        this.id = id;
        this.descricao = descricao;
        this.valorLitro = valorLitro;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        hash = 23 * hash + this.id;
        hash = 23 * hash + Objects.hashCode(this.descricao);
        hash = 23 * hash + Float.floatToIntBits(this.valorLitro);
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
        final Combustivel other = (Combustivel) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.descricao, other.descricao)) {
            return false;
        }
        if (Float.floatToIntBits(this.valorLitro) != Float.floatToIntBits(other.valorLitro)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Combustivel{" + "id=" + id + ", descricao=" + descricao + ", valorLitro=" + valorLitro + '}';
    }

    public float getValorLitro() {
        return valorLitro;
    }

    public void setValorLitro(float valorLitro) {
        this.valorLitro = valorLitro;
    }

    public String getValorLitroString() {
        return Moeda.converteR$Float(valorLitro);
    }

    public void setValorLitroString(String valorLitroString) {
        this.valorLitro = Moeda.converteUS$(valorLitroString);
    }

}
