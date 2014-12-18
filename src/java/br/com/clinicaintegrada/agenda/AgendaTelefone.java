package br.com.clinicaintegrada.agenda;

import br.com.clinicaintegrada.utils.BaseEntity;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.*;

@Entity
@Table(name = "age_telefone",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_cliente", "id_tipo_telefone", "ds_telefone"})
)
public class AgendaTelefone implements BaseEntity, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "id_agenda", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Agenda agenda;
    @JoinColumn(name = "id_tipo_telefone", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private TipoTelefone tipoTelefone;
    @Column(name = "ds_ddi", length = 5)
    private String ddi;
    @Column(name = "ds_ddd", length = 2)
    private String ddd;
    @Column(name = "ds_telefone", length = 20)
    private String telefone;
    @Column(name = "ds_contato", length = 50)
    private String contato;

    public AgendaTelefone() {
        this.id = -1;
        this.agenda = new Agenda();
        this.tipoTelefone = new TipoTelefone();
        this.ddi = "55";
        this.ddd = "";
        this.telefone = "";
        this.contato = "";
    }

    public AgendaTelefone(Integer id, Agenda agenda, TipoTelefone tipoTelefone, String ddi, String ddd, String telefone, String contato) {
        this.id = id;
        this.agenda = agenda;
        this.tipoTelefone = tipoTelefone;
        this.ddi = ddi;
        this.ddd = ddd;
        this.telefone = telefone;
        this.contato = contato;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Agenda getAgenda() {
        return agenda;
    }

    public void setAgenda(Agenda agenda) {
        this.agenda = agenda;
    }

    public TipoTelefone getTipoTelefone() {
        return tipoTelefone;
    }

    public void setTipoTelefone(TipoTelefone tipoTelefone) {
        this.tipoTelefone = tipoTelefone;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public String getDdi() {
        return ddi;
    }

    public void setDdi(String ddi) {
        this.ddi = ddi;
    }

    public String getDdd() {
        return ddd;
    }

    public void setDdd(String ddd) {
        this.ddd = ddd;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + this.id;
        hash = 53 * hash + Objects.hashCode(this.agenda);
        hash = 53 * hash + Objects.hashCode(this.tipoTelefone);
        hash = 53 * hash + Objects.hashCode(this.ddi);
        hash = 53 * hash + Objects.hashCode(this.ddd);
        hash = 53 * hash + Objects.hashCode(this.telefone);
        hash = 53 * hash + Objects.hashCode(this.contato);
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
        final AgendaTelefone other = (AgendaTelefone) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.agenda, other.agenda)) {
            return false;
        }
        if (!Objects.equals(this.tipoTelefone, other.tipoTelefone)) {
            return false;
        }
        if (!Objects.equals(this.ddi, other.ddi)) {
            return false;
        }
        if (!Objects.equals(this.ddd, other.ddd)) {
            return false;
        }
        if (!Objects.equals(this.telefone, other.telefone)) {
            return false;
        }
        if (!Objects.equals(this.contato, other.contato)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "AgendaTelefone{" + "id=" + id + ", agenda=" + agenda + ", tipoTelefone=" + tipoTelefone + ", ddi=" + ddi + ", ddd=" + ddd + ", telefone=" + telefone + ", contato=" + contato + '}';
    }

}
