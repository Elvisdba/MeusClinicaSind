package br.com.clinicaintegrada.financeiro;

import br.com.clinicaintegrada.seguranca.Usuario;
import br.com.clinicaintegrada.utils.DataHoje;
import java.util.Date;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "fin_acordo")
public class Acordo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_usuario", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Usuario usuario;
    @Column(name = "ds_contato", length = 200, nullable = true)
    private String contato;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_data")
    private Date data;

    public Acordo() {
        this.id = -1;
        this.usuario = null;
        this.contato = "";
        this.data = DataHoje.dataHoje();
    }

    public Acordo(int id, Usuario usuario, String contato, String dataString) {
        this.id = id;
        this.usuario = usuario;
        this.contato = contato;
        setDataString(dataString);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date dtData) {
        this.data = dtData;
    }

    public String getDataString() {
        return DataHoje.converteData(data);
    }

    public void setDataString(String dataString) {
        setData(DataHoje.converte(dataString));
    }
}
