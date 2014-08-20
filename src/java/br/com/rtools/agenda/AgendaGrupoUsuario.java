package br.com.rtools.agenda;

import br.com.rtools.seguranca.Cliente;
import br.com.rtools.seguranca.Usuario;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "age_grupo_usuario",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_cliente", "id_grupo_agenda", "id_usuario"})
)
@NamedQueries({
    @NamedQuery(name = "AgendaGrupoUsuario.findGrupoAgenda", query = "SELECT AGU FROM AgendaGrupoUsuario AS AGU WHERE AGU.grupoAgenda.id = :p1 AND AGU.cliente.id = :p2 ORDER BY AGU.grupoAgenda.descricao ASC, AGU.usuario.login ASC"),
    @NamedQuery(name = "AgendaGrupoUsuario.existGrupoUsuario", query = "SELECT AGU FROM AgendaGrupoUsuario AS AGU WHERE AGU.grupoAgenda.id = :p1 AND AGU.usuario.id = :p2 AND AGU.cliente.id = :p3")
})
public class AgendaGrupoUsuario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_cliente", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Cliente cliente;
    @JoinColumn(name = "id_grupo_agenda", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private GrupoAgenda grupoAgenda;
    @JoinColumn(name = "id_usuario", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Usuario usuario;

    public AgendaGrupoUsuario() {
        this.id = -1;
        this.grupoAgenda = new GrupoAgenda();
        this.usuario = new Usuario();
    }

    public AgendaGrupoUsuario(int id, GrupoAgenda grupoAgenda, Usuario usuario) {
        this.id = id;
        this.grupoAgenda = grupoAgenda;
        this.usuario = usuario;
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

    public GrupoAgenda getGrupoAgenda() {
        return grupoAgenda;
    }

    public void setGrupoAgenda(GrupoAgenda grupoAgenda) {
        this.grupoAgenda = grupoAgenda;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + this.id;
        hash = 17 * hash + Objects.hashCode(this.cliente);
        hash = 17 * hash + Objects.hashCode(this.grupoAgenda);
        hash = 17 * hash + Objects.hashCode(this.usuario);
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
        final AgendaGrupoUsuario other = (AgendaGrupoUsuario) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.cliente, other.cliente)) {
            return false;
        }
        if (!Objects.equals(this.grupoAgenda, other.grupoAgenda)) {
            return false;
        }
        if (!Objects.equals(this.usuario, other.usuario)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "AgendaGrupoUsuario{" + "id=" + id + ", cliente=" + cliente + ", grupoAgenda=" + grupoAgenda + ", usuario=" + usuario + '}';
    }

}
