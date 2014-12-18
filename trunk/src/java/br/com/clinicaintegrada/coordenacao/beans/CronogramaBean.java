package br.com.clinicaintegrada.coordenacao.beans;

import br.com.clinicaintegrada.coordenacao.Agendamento;
import br.com.clinicaintegrada.coordenacao.Evento;
import br.com.clinicaintegrada.coordenacao.Cronograma;
import br.com.clinicaintegrada.coordenacao.dao.EventoDao;
import br.com.clinicaintegrada.coordenacao.dao.CronogramaDao;
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
public class CronogramaBean implements Serializable {

    private Cronograma grade;
    private Semana semana;
    private Filial filial;
    private Evento evento;
    private List<Cronograma> listCronogramas;
    private List<Filial> listFiliais;
    private List<Semana> listSemanas;
    private List<Evento> listEventos;
    private Boolean blockPanel;
    private Boolean isSemana;

    @PostConstruct
    public void init() {
        grade = new Cronograma();
        semana = new Semana();
        filial = new Filial();
        evento = new Evento();
        grade.setHoraInicio(DataHoje.livre(new Date(), "HH:mm"));
        grade.setHoraFim(DataHoje.livre(new Date(), "HH:mm"));
        listCronogramas = new ArrayList<>();
        listFiliais = new ArrayList<>();
        listSemanas = new ArrayList<>();
        listEventos = new ArrayList<>();
        blockPanel = false;
        isSemana = true;
    }

    @PreDestroy
    public void destroy() {
        Sessions.remove("cronogramaBean");
    }

    public void clear() {
        Sessions.remove("cronogramaBean");
    }

    public void clear(int tcase) {
        if (tcase == 1) {
            Sessions.remove("cronogramaBean");
        } else if (tcase == 2) {
            grade = new Cronograma();
            filial = new Filial();
            evento = new Evento();
            grade.setHoraInicio(DataHoje.livre(new Date(), "HH:mm"));
            grade.setHoraFim(DataHoje.livre(new Date(), "HH:mm"));
            listCronogramas = new ArrayList<>();
            listFiliais = new ArrayList<>();
            listSemanas = new ArrayList<>();
            listEventos = new ArrayList<>();
            isSemana = true;
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
        if (isSemana) {
            if (semana == null) {
                Messages.warn("Validação", "Selecionar o dia da semana!");
                return;
            }
            grade.setSemana(semana);
            grade.setDataEvento(null);
        } else {
            grade.setSemana(null);
            semana = null;
        }
        Dao dao = new Dao();
        if (grade.getId() == -1) {
            grade.setCliente(SessaoCliente.get());
            CronogramaDao cronogramaDao = new CronogramaDao();
            if (cronogramaDao.existeCronogramaPorCliente(grade.getCliente().getId(), grade.getFilial().getId(), grade.getSemana().getId(), grade.getEvento().getId(), grade.getHoraInicio(), grade.getHoraFim())) {
                Messages.warn("Validação", "Cronograma já cadastrada");
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

    public void edit(Object o) {
        grade = new Cronograma();
        Dao dao = new Dao();
        grade = (Cronograma) dao.rebind(o);
        if (grade.getSemana() != null) {
            isSemana = true;
            semana = grade.getSemana();
        } else {
            isSemana = false;
        }
        filial = grade.getFilial();
        evento = grade.getEvento();
    }

    public void delete() {
        delete(grade);
    }

    public void delete(Cronograma g) {
        grade = g;
        Dao dao = new Dao();
        if (dao.delete(grade, true)) {
            Messages.info("Sucesso", "Registro removido");
            clear(2);
        } else {
            Messages.warn("Erro", "Ao remover registro!");
        }
    }

    public Cronograma getCronograma() {
        return grade;
    }

    public void setCronograma(Cronograma grade) {
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
            listEventos = eventoDao.findAll();
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

    public Boolean getBlockPanel() {
        return blockPanel;
    }

    public void setBlockPanel(Boolean blockPanel) {
        this.blockPanel = blockPanel;
    }

    public List<Cronograma> getListCronogramas() {
        if (listCronogramas.isEmpty()) {
            CronogramaDao cronogramaDao = new CronogramaDao();
            listCronogramas = cronogramaDao.pesquisaTodasCronogramasPorClienteFilial(SessaoCliente.get().getId(), filial.getId());
        }
        return listCronogramas;
    }

    public void setListCronogramas(List<Cronograma> listCronogramas) {
        this.listCronogramas = listCronogramas;
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

    public Boolean getIsSemana() {
        return isSemana;
    }

    public void setIsSemana(Boolean isSemana) {
        this.isSemana = isSemana;
    }

}
