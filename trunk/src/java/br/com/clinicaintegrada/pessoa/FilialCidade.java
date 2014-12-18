package br.com.clinicaintegrada.pessoa;

import br.com.clinicaintegrada.endereco.Cidade;
import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "pes_filial_cidade")
public class FilialCidade implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "id_cidade", referencedColumnName = "id", nullable = true)
    @ManyToOne
    private Cidade cidade;
    @JoinColumn(name = "id_filial", referencedColumnName = "id", nullable = true)
    @ManyToOne
    private Filial filial;

    public FilialCidade() {
        this.id = -1;
        this.cidade = new Cidade();
        this.filial = new Filial();
    }

    public FilialCidade(Integer id, Cidade cidade, Filial filial) {
        this.id = id;
        this.cidade = cidade;
        this.filial = filial;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Cidade getCidade() {
        return cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }

    public Filial getFilial() {
        return filial;
    }

    public void setFilial(Filial filial) {
        this.filial = filial;
    }
}
