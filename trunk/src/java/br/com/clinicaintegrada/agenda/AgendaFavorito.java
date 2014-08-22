package br.com.clinicaintegrada.agenda;

import br.com.clinicaintegrada.seguranca.Usuario;
import br.com.clinicaintegrada.utils.BaseEntity;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "age_favorito",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_agenda", "id_usuario"})
)
public class AgendaFavorito implements Serializable, BaseEntity {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_agenda", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Agenda agenda;
    @JoinColumn(name = "id_usuario", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Usuario usuario;

    public AgendaFavorito() {
        this.id = -1;
        this.agenda = new Agenda();
        this.usuario = new Usuario();
    }

    public AgendaFavorito(int id, Agenda agenda, Usuario usuario) {
        this.id = id;
        this.agenda = agenda;
        this.usuario = usuario;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Agenda getAgenda() {
        return agenda;
    }

    public void setAgenda(Agenda agenda) {
        this.agenda = agenda;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + this.id;
        hash = 97 * hash + Objects.hashCode(this.agenda);
        hash = 97 * hash + Objects.hashCode(this.usuario);
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
        final AgendaFavorito other = (AgendaFavorito) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.agenda, other.agenda)) {
            return false;
        }
        if (!Objects.equals(this.usuario, other.usuario)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "AgendaFavorito{" + "id=" + id + ", agenda=" + agenda + ", usuario=" + usuario + '}';
    }
}
