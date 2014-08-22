package br.com.clinicaintegrada.coordenacao.beans;

import br.com.clinicaintegrada.coordenacao.Evento;
import br.com.clinicaintegrada.coordenacao.Grade;
import br.com.clinicaintegrada.coordenacao.dao.EventoDao;
import br.com.clinicaintegrada.coordenacao.dao.GradeDao;
import br.com.clinicaintegrada.pessoa.Filial;
import br.com.clinicaintegrada.pessoa.dao.FilialDao;
import br.com.clinicaintegrada.seguranca.controleUsuario.SessaoCliente;
import br.com.clinicaintegrada.sistema.Semana;
import br.com.clinicaintegrada.utils.Dao;
import br.com.clinicaintegrada.utils.DataHoje;
import br.com.clinicaintegrada.utils.Messages;
import br.com.clinicaintegrada.utils.Sessions;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class GradeBean implements Serializable {

    private Grade grade;
    private Semana semana;
    private Filial filial;
    private Evento evento;
    private List<Grade> listGrades;
    private List<Filial> listFiliais;
    private List<Semana> listSemanas;
    private List<Evento> listEventos;
    private boolean blockPanel;

    @PostConstruct
    public void init() {
        grade = new Grade();
        semana = new Semana();
        filial = new Filial();
        evento = new Evento();
        grade.setHoraInicio(DataHoje.livre(new Date(), "HH:mm"));
        grade.setHoraFim(DataHoje.livre(new Date(), "HH:mm"));
        listGrades = new ArrayList<>();
        listFiliais = new ArrayList<>();
        listSemanas = new ArrayList<>();
        listEventos = new ArrayList<>();
        blockPanel = false;
    }

    @PreDestroy
    public void destroy() {
        Sessions.remove("gradeBean");
    }

    public void clear() {
        Sessions.remove("gradeBean");
    }

    public void clear(int tcase) {
        if (tcase == 1) {
            Sessions.remove("gradeBean");
        } else if (tcase == 2) {
            grade = new Grade();
            filial = new Filial();
            evento = new Evento();
            grade.setHoraInicio(DataHoje.livre(new Date(), "HH:mm"));
            grade.setHoraFim(DataHoje.livre(new Date(), "HH:mm"));
            listGrades = new ArrayList<>();
            listFiliais = new ArrayList<>();
            listSemanas = new ArrayList<>();
            listEventos = new ArrayList<>();
        }
    }

    public void save() {
        if (filial == null) {
            Messages.warn("Validação", "Selecionar uma filial");
            return;
        }
        grade.setFilial(filial);
        if (evento == null) {
            Messages.warn("Validação", "Selecionar um evento!");
            return;
        }
        if (grade.getHoraInicio().isEmpty()) {
            Messages.warn("Validação", "Informar a hora de ínicio do evento!");
            return;
        }
        if (grade.getHoraFim().isEmpty()) {
            Messages.warn("Validação", "Informar a hora de fim do evento!");
            return;
        }
        String horaInicioString = grade.getHoraInicio();
        String horaFimString = grade.getHoraFim();
        horaInicioString = horaInicioString.replace(":", "");
        horaFimString = horaFimString.replace(":", "");
        int horaInicio = Integer.parseInt(horaInicioString);
        int horaFim = Integer.parseInt(horaFimString);
        if (horaFim < horaInicio) {
            Messages.warn("Validação", "Horário final deve ser maior ou igual ao horário inicial!");
            return;
        }
        grade.setEvento(evento);
        if (semana == null) {
            Messages.warn("Validação", "Selecionar o dia da semana!");
            return;
        }
        grade.setSemana(semana);
        Dao dao = new Dao();
        if (grade.getId() == -1) {
            grade.setCliente(SessaoCliente.get());
            GradeDao gradeDao = new GradeDao();
            if (gradeDao.existeGradePorCliente(grade.getCliente().getId(), grade.getFilial().getId(), grade.getSemana().getId(), grade.getEvento().getId(), grade.getHoraInicio(), grade.getHoraFim())) {
                Messages.warn("Validação", "Grade já cadastrada");
                return;
            }
            if (dao.save(grade, true)) {
                Messages.info("Sucesso", "Registro inserido");
                clear(2);
            } else {
                Messages.warn("Erro", "Ao inserir registro!");
            }
        } else {
            if (dao.update(grade, true)) {
                Messages.info("Sucesso", "Registro atualizado");
                clear(2);
            } else {
                Messages.warn("Erro", "Ao atualizar registro!");
            }
        }
    }

    public void edit(Grade g) {
        grade = new Grade();
        grade = g;
        filial = grade.getFilial();
        evento = grade.getEvento();
        semana = grade.getSemana();
    }

    public void delete() {
        delete(grade);
    }

    public void delete(Grade g) {
        grade = g;
        Dao dao = new Dao();
        if (dao.delete(grade, true)) {
            Messages.info("Sucesso", "Registro removido");
            clear(2);
        } else {
            Messages.warn("Erro", "Ao remover registro!");
        }
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public List<Filial> getListFiliais() {
        if (listFiliais.isEmpty()) {
            FilialDao filialDao = new FilialDao();
            listFiliais = filialDao.pesquisaTodasCliente();
            if (listFiliais.isEmpty()) {
                Messages.warn("Notificação", "- Cadastrar filiais");
            } else {
                filial = listFiliais.get(0);
            }
        }
        return listFiliais;
    }

    public void setListFiliais(List<Filial> listFiliais) {
        this.listFiliais = listFiliais;
    }

    public List<Semana> getListSemanas() {
        if (listSemanas.isEmpty()) {
            Dao dao = new Dao();
            listSemanas = dao.list(new Semana());
            if (listSemanas.isEmpty()) {
                Messages.warn("Notificação", "- Cadastrar dias da semana");
            } else {
                semana = listSemanas.get(0);
            }
        }
        return listSemanas;
    }

    public void setListSemanas(List<Semana> listSemanas) {
        this.listSemanas = listSemanas;
    }

    public List<Evento> getListEventos() {
        if (listEventos.isEmpty()) {
            EventoDao eventoDao = new EventoDao();
            listEventos = eventoDao.pesquisaTodosPorCliente(SessaoCliente.get().getId());
            if (listEventos.isEmpty()) {
                Messages.warn("Notificação", "- Cadastrar eventos");
            } else {
                evento = listEventos.get(0);
            }
        }
        return listEventos;
    }

    public void setListEventos(List<Evento> listEventos) {
        this.listEventos = listEventos;
    }

    public boolean isBlockPanel() {
        return blockPanel;
    }

    public void setBlockPanel(boolean blockPanel) {
        this.blockPanel = blockPanel;
    }

    public List<Grade> getListGrades() {
        if (listGrades.isEmpty()) {
            GradeDao gradeDao = new GradeDao();
            listGrades = gradeDao.pesquisaTodasGradesPorClienteFilial(SessaoCliente.get().getId(), filial.getId());
        }
        return listGrades;
    }

    public void setListGrades(List<Grade> listGrades) {
        this.listGrades = listGrades;
    }

    public Semana getSemana() {
        return semana;
    }

    public void setSemana(Semana semana) {
        this.semana = semana;
    }

    public Filial getFilial() {
        return filial;
    }

    public void setFilial(Filial filial) {
        this.filial = filial;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

}
