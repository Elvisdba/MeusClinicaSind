package br.com.rtools.agenda;

import br.com.rtools.seguranca.Usuario;
import java.io.Serializable;
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
public class AgendaFavorito implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    public String toString() {
        return "AgendaFavorito{" + "id=" + id + ", agenda=" + agenda + ", usuario=" + usuario + '}';
    }
}
