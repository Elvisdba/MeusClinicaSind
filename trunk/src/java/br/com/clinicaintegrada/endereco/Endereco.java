package br.com.clinicaintegrada.endereco;

import br.com.clinicaintegrada.seguranca.Cliente;
import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "end_endereco",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_cliente", "", "id_cidade", "id_bairro", "id_logradouro", "id_descricao_endereco"})
)
public class Endereco implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "id_cliente", referencedColumnName = "id", nullable = true)
    @ManyToOne
    private Cliente cliente;
    @JoinColumn(name = "id_cidade", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Cidade cidade;
    @JoinColumn(name = "id_bairro", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Bairro bairro;
    @JoinColumn(name = "id_logradouro", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Logradouro logradouro;
    @JoinColumn(name = "id_descricao_endereco", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private DescricaoEndereco descricaoEndereco;
    @Column(name = "ds_cep", length = 9, nullable = false)
    private String cep;
    @Column(name = "ds_faixa", length = 100, nullable = true)
    private String faixa;

    public Endereco() {
        this.id = -1;
        this.cliente = new Cliente();
        this.cidade = new Cidade();
        this.bairro = new Bairro();
        this.logradouro = new Logradouro();
        this.descricaoEndereco = new DescricaoEndereco();
        this.cep = "";
        this.faixa = "";
    }

    public Endereco(Integer id, Cliente cliente, Cidade cidade, Bairro bairro, Logradouro logradouro, DescricaoEndereco descricaoEndereco, String cep, String faixa) {
        this.id = id;
        this.cliente = cliente;
        this.cidade = cidade;
        this.bairro = bairro;
        this.logradouro = logradouro;
        this.descricaoEndereco = descricaoEndereco;
        this.cep = cep;
        this.faixa = faixa;
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

    public Bairro getBairro() {
        return bairro;
    }

    public void setBairro(Bairro bairro) {
        this.bairro = bairro;
    }

    public Logradouro getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(Logradouro logradouro) {
        this.logradouro = logradouro;
    }

    public DescricaoEndereco getDescricaoEndereco() {
        return descricaoEndereco;
    }

    public void setDescricaoEndereco(DescricaoEndereco descricaoEndereco) {
        this.descricaoEndereco = descricaoEndereco;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getFaixa() {
        return faixa;
    }

    public void setFaixa(String faixa) {
        this.faixa = faixa;
    }

    public String getEnderecoToString() {
        if ((this.getLogradouro().getDescricao().equals(""))
                || (this.getDescricaoEndereco().getDescricao().equals(""))
                || (this.getBairro().getDescricao().equals(""))
                || (this.getCidade().getCidade().equals(""))
                || (this.getCidade().getUf().equals(""))) {
            return "";
        } else {
            return this.getLogradouro().getDescricao() + " "
                    + this.getDescricaoEndereco().getDescricao() + ", "
                    + this.getBairro().getDescricao() + ", "
                    + this.getCidade().getCidade() + "  -  "
                    + this.getCidade().getUf() + "  ";
        }
    }

    public String getEnderecoSimplesToString() {
        if ((this.getLogradouro().getDescricao().equals(""))
                || (this.getDescricaoEndereco().getDescricao().equals(""))) {
            return "";
        } else {
            return this.getLogradouro().getDescricao() + " "
                    + this.getDescricaoEndereco().getDescricao();
        }
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}
