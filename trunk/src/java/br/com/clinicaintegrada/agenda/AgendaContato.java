package br.com.clinicaintegrada.agenda;

import br.com.clinicaintegrada.utils.BaseEntity;
import br.com.clinicaintegrada.utils.DataHoje;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import javax.persistence.*;
import org.primefaces.event.SelectEvent;

@Entity
@Table(name = "age_contato",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_agenda", "ds_contato"})
)
@NamedQueries({
    @NamedQuery(name = "AgendaContato.findByAgenda", query = "SELECT AC FROM AgendaContato AS AC WHERE AC.agenda.id = :p1 ORDER BY AC.departamento ASC, AC.contato ASC, AC.nascimento ASC")
})
public class AgendaContato implements BaseEntity, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "id_agenda", referencedColumnName = "id")
    @ManyToOne
    private Agenda agenda;
    @Column(name = "ds_contato", length = 100)
    private String contato;
    @Column(name = "ds_email1", length = 255, nullable = true)
    private String email1;
    @Column(name = "ds_email2", length = 255, nullable = true)
    private String email2;
    @Column(name = "ds_departamento", length = 100, nullable = true)
    private String departamento;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_nascimento")
    private Date nascimento;
    @Column(name = "is_notifica_aniversario", columnDefinition = "boolean default false")
    private boolean notificaAniversario;

    public AgendaContato() {
        this.id = -1;
        this.agenda = new Agenda();
        this.contato = "";
        this.email1 = "";
        this.email2 = "";
        this.departamento = "";
        this.nascimento = new Date();
        this.notificaAniversario = false;
    }

    public AgendaContato(Integer id, Agenda agenda, String contato, String email1, String email2, String departamento, Date nascimento, Boolean notificaAniversario) {
        this.id = id;
        this.agenda = agenda;
        this.contato = contato;
        this.email1 = email1;
        this.email2 = email2;
        this.departamento = departamento;
        this.nascimento = nascimento;
        this.notificaAniversario = notificaAniversario;
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

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public String getEmail1() {
        return email1;
    }

    public void setEmail1(String email1) {
        this.email1 = email1;
    }

    public String getEmail2() {
        return email2;
    }

    public void setEmail2(String email2) {
        this.email2 = email2;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public Date getNascimento() {
        return nascimento;
    }

    public void setNascimento(Date nascimento) {
        this.nascimento = nascimento;
    }

    public boolean getNotificaAniversario() {
        return notificaAniversario;
    }

    public void setNotificaAniversario(boolean notificaAniversario) {
        this.notificaAniversario = notificaAniversario;
    }

    public String getNascimentoString() {
        return DataHoje.converteData(nascimento);
    }

    public void setNascimentoString(String nascimentoString) {
        this.nascimento = DataHoje.converte(nascimentoString);
    }

    public void selecionaDataNascimento(SelectEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy");
        this.nascimento = DataHoje.converte(format.format(event.getObject()));
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.id);
        hash = 53 * hash + Objects.hashCode(this.agenda);
        hash = 53 * hash + Objects.hashCode(this.contato);
        hash = 53 * hash + Objects.hashCode(this.email1);
        hash = 53 * hash + Objects.hashCode(this.email2);
        hash = 53 * hash + Objects.hashCode(this.departamento);
        hash = 53 * hash + Objects.hashCode(this.nascimento);
        hash = 53 * hash + (this.notificaAniversario ? 1 : 0);
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
        final AgendaContato other = (AgendaContato) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.agenda, other.agenda)) {
            return false;
        }
        if (!Objects.equals(this.contato, other.contato)) {
            return false;
        }
        if (!Objects.equals(this.email1, other.email1)) {
            return false;
        }
        if (!Objects.equals(this.email2, other.email2)) {
            return false;
        }
        if (!Objects.equals(this.departamento, other.departamento)) {
            return false;
        }
        if (!Objects.equals(this.nascimento, other.nascimento)) {
            return false;
        }
        if (this.notificaAniversario != other.notificaAniversario) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "AgendaContato{" + "id=" + id + ", agenda=" + agenda + ", contato=" + contato + ", email1=" + email1 + ", email2=" + email2 + ", departamento=" + departamento + ", nascimento=" + nascimento + ", notificaAniversario=" + notificaAniversario + '}';
    }

}
