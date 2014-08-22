package br.com.clinicaintegrada.pessoa;

//package br.com.rtools.pessoa;
//
//import br.com.rtools.utilitarios.BaseEntity;
//import java.io.Serializable;
//import javax.persistence.*;
//
//@Entity
//@Table(name = "PES_TIPO_CENTRO_COMERCIAL")
//@NamedQueries({
//    @NamedQuery(name = "TipoCentroComercial.findAll", query = "SELECT TCC FROM TipoCentroComercial AS TCC ORDER BY TCC.descricao ASC "),
//    @NamedQuery(name = "TipoCentroComercial.findName", query = "SELECT TCC FROM TipoCentroComercial AS TCC WHERE UPPER(TCC.descricao) LIKE :pdescricao ORDER BY TCC.descricao ASC ")
//})
//public class TipoCentroComercial implements BaseEntity, Serializable {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;
//    @Column(name = "DS_DESCRICAO", length = 100, unique = true)
//    private String descricao;
//
//    public TipoCentroComercial(int id, String descricao) {
//        this.id = id;
//        this.descricao = descricao;
//    }
//
//    public TipoCentroComercial() {
//        this.id = -1;
//        this.descricao = "";
//    }
//
//    @Override
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public String getDescricao() {
//        return descricao;
//    }
//
//    public void setDescricao(String descricao) {
//        this.descricao = descricao;
//    }
//
//    @Override
//    public int hashCode() {
//        int hash = 7;
//        hash = 17 * hash + this.id;
//        hash = 17 * hash + (this.descricao != null ? this.descricao.hashCode() : 0);
//        return hash;
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (obj == null) {
//            return false;
//        }
//        if (getClass() != obj.getClass()) {
//            return false;
//        }
//        final TipoCentroComercial other = (TipoCentroComercial) obj;
//        if (this.id != other.id) {
//            return false;
//        }
//        if ((this.descricao == null) ? (other.descricao != null) : !this.descricao.equals(other.descricao)) {
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public String toString() {
//        return "TipoCentroComercial{" + "id=" + id + ", descricao=" + descricao + '}';
//    }
//
//}
