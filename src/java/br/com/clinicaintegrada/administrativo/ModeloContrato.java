package br.com.clinicaintegrada.administrativo;

import br.com.clinicaintegrada.seguranca.Cliente;
import br.com.clinicaintegrada.seguranca.Modulo;
import br.com.clinicaintegrada.utils.DataHoje;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "ctr_modelo_contrato")
@NamedQueries({
    @NamedQuery(name = "ModeloContrato.findAll", query = "SELECT MC FROM ModeloContrato AS MC ORDER BY MC.dataCadastro DESC, MC.dataAtualizado DESC, MC.titulo ASC "),
    @NamedQuery(name = "ModeloContrato.findName", query = "SELECT MC FROM ModeloContrato AS MC WHERE UPPER(MC.titulo) LIKE :pdescricao ORDER BY MC.dataCadastro DESC, MC.dataAtualizado DESC, MC.titulo ASC ")
})
public class ModeloContrato implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "ds_titulo", length = 100)
    private String titulo;
    @Column(name = "ds_descricao", length = 15000)
    private String descricao;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_cadastro")
    private Date dataCadastro;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_atualizado")
    private Date dataAtualizado;
    @JoinColumn(name = "id_modulo", referencedColumnName = "id")
    @ManyToOne
    private Modulo modulo;
    @JoinColumn(name = "id_cliente", referencedColumnName = "id")
    @ManyToOne
    private Cliente cliente;

    public ModeloContrato() {
        this.id = -1;
        this.titulo = "";
        this.descricao = "";
        this.dataCadastro = new Date();
        this.dataAtualizado = new Date();
        this.modulo = new Modulo();
        this.cliente = new Cliente();
    }

    public ModeloContrato(Integer id, String titulo, String descricao, String dataCadastro, String dataAtualizado, Modulo modulo, Cliente cliente) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        setDataCadastro(dataCadastro);
        setDataAtualizado(dataAtualizado);
        this.modulo = modulo;
        this.cliente = cliente;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDataCadastro() {
        return DataHoje.converteData(dataCadastro);
    }

    public void setDataCadastro(String dataCadastro) {
        this.dataCadastro = DataHoje.converte(dataCadastro);
    }

    public String getDataAtualizado() {
        return DataHoje.converteData(dataAtualizado);
    }

    public void setDataAtualizado(String dataAtualizado) {
        this.dataAtualizado = DataHoje.converte(dataAtualizado);
    }

    public Modulo getModulo() {
        return modulo;
    }

    public void setModulo(Modulo modulo) {
        this.modulo = modulo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}
