package br.com.rtools.coordenacao;

import br.com.rtools.seguranca.Cliente;
import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "rot_evento",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_cliente", "id_grupo_evento", "ds_descricao"})
)
@NamedQuery(name = "Evento.findAll", query = "SELECT Eve FROM Evento AS Eve ORDER BY Eve.descricao ASC ")
public class Evento implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_cliente", referencedColumnName = "id", nullable = false)
    @OneToOne
    private Cliente cliente;
    @JoinColumn(name = "id_grupo_evento", referencedColumnName = "id", nullable = false)
    @OneToOne
    private GrupoEvento grupoEvento;
    @Column(name = "ds_descricao", length = 30, nullable = true)
    private String descricao;
    @Column(name = "ds_sigla", length = 30, nullable = true)
    private String sigla;
    @Column(name = "is_web", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean web;

    public Evento() {
        this.id = -1;
        this.cliente = new Cliente();
        this.grupoEvento = new GrupoEvento();
        this.descricao = "";
        this.sigla = "";
        this.web = false;
    }

    public Evento(int id, Cliente cliente, GrupoEvento grupoEvento, String descricao, String sigla, boolean web) {
        this.id = id;
        this.cliente = cliente;
        this.grupoEvento = grupoEvento;
        this.descricao = descricao;
        this.sigla = sigla;
        this.web = web;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public GrupoEvento getGrupoEvento() {
        return grupoEvento;
    }

    public void setGrupoEvento(GrupoEvento grupoEvento) {
        this.grupoEvento = grupoEvento;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public boolean isWeb() {
        return web;
    }

    public void setWeb(boolean web) {
        this.web = web;
    }

}