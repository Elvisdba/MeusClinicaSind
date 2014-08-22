package br.com.clinicaintegrada.coordenacao;

import br.com.clinicaintegrada.pessoa.Filial;
import br.com.clinicaintegrada.seguranca.Cliente;
import br.com.clinicaintegrada.sistema.Semana;
import br.com.clinicaintegrada.utils.DataHoje;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.*;
import org.primefaces.event.SelectEvent;

@Entity
@Table(name = "rot_grade",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_cliente", "id_grupo_evento", "ds_descricao"})
)
// @NamedQuery(name = "Grade.findAll", query = "SELECT G FROM Grade AS G ORDER BY G.descricao ASC ")
public class Grade implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_cliente", referencedColumnName = "id", nullable = false)
    @OneToOne
    private Cliente cliente;
    @JoinColumn(name = "id_evento", referencedColumnName = "id", nullable = false)
    @OneToOne
    private Evento evento;
    @JoinColumn(name = "id_filial", referencedColumnName = "id", nullable = false)
    @OneToOne
    private Filial filial;
    @JoinColumn(name = "id_semana", referencedColumnName = "id", nullable = false)
    @OneToOne
    private Semana semana;
    @Column(name = "ds_hora_inicio", length = 5, nullable = false)
    private String horaInicio;
    @Column(name = "ds_hora_fim", length = 5, nullable = false)
    private String horaFim;
    @Column(name = "ds_obs", length = 200, nullable = true)
    private String observacao;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_evento", nullable = true)
    private Date dataEvento;

    public Grade() {
        this.id = -1;
        this.cliente = new Cliente();
        this.evento = new Evento();
        this.filial = new Filial();
        this.semana = new Semana();
        this.horaInicio = "";
        this.horaFim = "";
        this.observacao = "";
        this.dataEvento = new Date();
    }

    public Grade(int id, Cliente cliente, Evento evento, Filial filial, Semana semana, String horaInicio, String horaFim, String observacao, Date dataEvento) {
        this.id = id;
        this.cliente = cliente;
        this.evento = evento;
        this.filial = filial;
        this.semana = semana;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
        this.observacao = observacao;
        this.dataEvento = dataEvento;
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

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public Filial getFilial() {
        return filial;
    }

    public void setFilial(Filial filial) {
        this.filial = filial;
    }

    public Semana getSemana() {
        return semana;
    }

    public void setSemana(Semana semana) {
        this.semana = semana;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFim() {
        return horaFim;
    }

    public void setHoraFim(String horaFim) {
        this.horaFim = horaFim;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Date getDataEvento() {
        return dataEvento;
    }

    public void setDataEvento(Date dataEvento) {
        this.dataEvento = dataEvento;
    }

    public String getDataEventoString() {
        return DataHoje.converteData(dataEvento);
    }

    public void setDataEventoString(String dataEventoString) {
        this.dataEvento = DataHoje.converte(dataEventoString);
    }

    public void selectedDataEvento(SelectEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy");
        this.dataEvento = DataHoje.converte(format.format(event.getObject()));
    }

//    public void selectedHoraInicio(SelectEvent event) {
//        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
//        this.horaInicio = (DataHoje.converte(format.format(event.getObject()))).toString();
//    }
//
//    public void selectedHoraFim(SelectEvent event) {
//        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
//        this.horaFim = (DataHoje.converte(format.format(event.getObject()))).toString();
//    }

}
