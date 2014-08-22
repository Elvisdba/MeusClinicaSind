package br.com.clinicaintegrada.pessoa;

import br.com.clinicaintegrada.endereco.Endereco;
import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "pes_pessoa_endereco",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_endereco", "id_tipo_endereco", "id_pessoa"})
)
public class PessoaEndereco implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_endereco", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Endereco endereco;
    @JoinColumn(name = "id_tipo_endereco", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private TipoEndereco tipoEndereco;
    @JoinColumn(name = "id_pessoa", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Pessoa pessoa;
    @Column(name = "ds_numero", length = 30, nullable = false)
    private String numero;
    @Column(name = "ds_complemento", length = 50, nullable = true)
    private String complemento;

    public PessoaEndereco() {
        this.id = -1;
        this.endereco = new Endereco();
        this.tipoEndereco = new TipoEndereco();
        this.pessoa = new Pessoa();
        this.numero = "";
        this.complemento = "";
    }

    public PessoaEndereco(int id, Endereco endereco, TipoEndereco tipoEndereco, Pessoa pessoa, String numero, String complemento) {
        this.id = id;
        this.endereco = endereco;
        this.tipoEndereco = tipoEndereco;
        this.pessoa = pessoa;
        this.numero = numero;
        this.complemento = complemento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public TipoEndereco getTipoEndereco() {
        return tipoEndereco;
    }

    public void setTipoEndereco(TipoEndereco tipoEndereco) {
        this.tipoEndereco = tipoEndereco;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }
}
