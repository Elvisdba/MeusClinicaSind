package br.com.clinicaintegrada.financeiro.beans;

import br.com.clinicaintegrada.financeiro.*;
import br.com.clinicaintegrada.financeiro.dao.ContaBancoDao;
import br.com.clinicaintegrada.financeiro.dao.PlanoDao;
import br.com.clinicaintegrada.financeiro.list.ListPlanoConta;
import br.com.clinicaintegrada.seguranca.controleUsuario.SessaoCliente;
import br.com.clinicaintegrada.utils.Dao;
import br.com.clinicaintegrada.utils.Messages;
import br.com.clinicaintegrada.utils.Sessions;
import br.com.clinicaintegrada.utils.PF;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class PlanoBean {

    private Plano plano;
    private Plano2 plano2;
    private Plano3 plano3;
    private Plano4 plano4;
    private Plano5 plano5;
    private int indexRow;
    private List<ListPlanoConta> listaPlanoContas;
    private boolean limpar;
    private int idPlanoConta;
    private List<SelectItem> listaContaBanco;
    private List<Plano5> listaPlanoContasPorPesquisa;
    private String descPesquisa;
    private String porPesquisa;
    private String comoPesquisa;
    private int selectedPlano;
    private int deletePlano;
    private Boolean[] visible;
    private String textNewPlano;

    @PostConstruct
    public void init() {
        plano = new Plano();
        plano2 = new Plano2();
        plano3 = new Plano3();
        plano4 = new Plano4();
        plano5 = new Plano5();
        visible = new Boolean[5];
        visible[0] = true;
        visible[1] = false;
        visible[2] = false;
        visible[3] = false;
        visible[4] = false;
        deletePlano = 1;
        textNewPlano = "Plano 1";
        indexRow = -1;
        listaPlanoContas = new ArrayList<>();
        limpar = false;
        idPlanoConta = 0;
        listaContaBanco = new ArrayList();
        listaPlanoContasPorPesquisa = new ArrayList();
        descPesquisa = "";
        porPesquisa = "conta";
        comoPesquisa = "";
        selectedPlano = 1;
    }

    @PreDestroy
    public void destroy() {
        Sessions.remove("planoBean");
        Sessions.remove("planoPesquisa");
    }

    public void clear() {
        Sessions.remove("planoBean");
    }

    public void save(int tcase) {
        Dao dao = new Dao();
        dao.openTransaction();
        if (tcase == 1) {
            if (!savePlano(dao)) {
                dao.rollback();
                return;
            }
        }

        if (tcase == 2) {
            if (!savePlano2(dao)) {
                dao.rollback();
                return;
            }
        }

        if (tcase == 3) {
            if (!savePlano3(dao)) {
                dao.rollback();
                return;
            }
        }

        if (tcase == 4) {
            if (!savePlano4(dao)) {
                dao.rollback();
                return;
            }
        }

        if (tcase == 5) {
            if (!savePlano5(dao)) {
                dao.rollback();
                return;
            }
        }

        dao.commit();
        listaPlanoContas.clear();
    }

    public void delete() {
        Dao dao = new Dao();
        dao.openTransaction();

        if (deletePlano == 1) {
            if (!deletePlano(dao)) {
                dao.rollback();
                return;
            }
        }

        if (deletePlano == 2) {
            if (!deletePlano2(dao)) {
                dao.rollback();
                return;
            }
        }

        if (deletePlano == 3) {
            if (!deletePlano3(dao)) {
                dao.rollback();
                return;
            }
        }

        if (deletePlano == 4) {
            if (!deletePlano4(dao)) {
                dao.rollback();
                return;
            }
        }

        if (deletePlano == 5) {
            if (!deletePlano5(dao)) {
                dao.rollback();
                return;
            }
        }

        listaPlanoContas.clear();
        dao.commit();
    }

    public int getSelectedPlano() {
        return selectedPlano;
    }

    public void setSelectedPlano(int selectedPlano) {
        this.selectedPlano = selectedPlano;
        listaPlanoContas.clear();
        if (selectedPlano == 1) {
            visible[0] = true;
            visible[1] = false;
            visible[2] = false;
            visible[3] = false;
            visible[4] = false;
            textNewPlano = "Plano 2";
        } else if (selectedPlano == 2) {
            visible[0] = true;
            visible[1] = true;
            visible[2] = false;
            visible[3] = false;
            visible[4] = false;
            textNewPlano = "Plano 3";
        } else if (selectedPlano == 3) {
            visible[0] = true;
            visible[1] = true;
            visible[2] = true;
            visible[3] = false;
            visible[4] = false;
            textNewPlano = "Plano 4";
        } else if (selectedPlano == 4) {
            visible[0] = true;
            visible[1] = true;
            visible[2] = true;
            visible[3] = true;
            visible[4] = false;
            textNewPlano = "Plano 5";
        } else if (selectedPlano == 5) {
            visible[0] = true;
            visible[1] = true;
            visible[2] = true;
            visible[3] = true;
            visible[4] = true;
            textNewPlano = "";

        }
    }

    public void edit(Object o) {
        String className = o.getClass().getSimpleName();
        if (className.equals("Plano") && selectedPlano == 1) {
            plano = (Plano) o;
            PF.openDialog("dlg_plano1");
            PF.update("form_plano:i_panel_plano1");
        } else if (className.equals("Plano2") && selectedPlano == 2) {
            plano2 = (Plano2) o;
            PF.openDialog("dlg_plano2");
            PF.update("form_plano:i_panel_plano2");
        } else if (className.equals("Plano3") && selectedPlano == 3) {
            plano3 = (Plano3) o;
            PF.openDialog("dlg_plano3");
            PF.update("form_plano:i_panel_plano3");
        } else if (className.equals("Plano4") && selectedPlano == 4) {
            plano4 = (Plano4) o;
            PF.openDialog("dlg_plano4");
            PF.update("form_plano:i_panel_plano4");
        } else if (className.equals("Plano5") && selectedPlano == 5) {
            plano5 = (Plano5) o;
            PF.openDialog("dlg_plano5");
            PF.update("form_plano:i_panel_plano5");
        }
    }

    public String link(Plano5 p5) {
        String url = (String) Sessions.getString("urlRetorno");
        Sessions.put("linkClicado", true);
        plano5 = p5;
        Sessions.put("pesquisaPlano", p5);
        return url;
    }

    public boolean savePlano(Dao dao) {
        if (plano.getId() == -1) {
            plano.setCliente(SessaoCliente.get());
            if (dao.save(plano)) {
                Messages.info("Sucesso", "Plano 1 adicionado");
            } else {
                Messages.warn("Erro", "Ao adicionar plano 1!");
                return false;
            }
        } else {
            if (dao.update(plano)) {
                Messages.info("Sucesso", "Plano 1 atualizado");
            } else {
                Messages.warn("Erro", "Ao atualizar plano 1!");
                return false;
            }
        }
        return true;
    }

    public boolean deletePlano(Dao dao) {
        if (plano.getId() != -1) {
            if (dao.delete((Plano) dao.find(new Plano(), plano.getId()))) {
                Messages.info("Sucesso", "Plano 1 removido");
            } else {
                Messages.warn("Erro", "Ao deletar plano 1");
                return false;
            }
        }
        plano = new Plano();
        return true;
    }

    public boolean savePlano2(Dao dao) {
        if (plano2.getId() == -1) {
            plano2.setNumero(plano.getNumero() + "." + plano2.getNumero());
            if (dao.save(plano2)) {
                Messages.info("Sucesso", "Plano 1 adicionado");
            } else {
                Messages.warn("Erro", "Ao inserir plano 2!");
                return false;
            }
        } else {
            if (dao.update(plano2)) {
                Messages.info("Sucesso", "Plano 2 atualizado");
            } else {
                Messages.warn("Erro", "Ao atualizad plano 2!");
                return false;
            }
        }
        //plano2 = new Plano2();
        return true;
    }

    public boolean deletePlano2(Dao dao) {
        if (plano2.getId() != -1) {
            if (dao.delete(plano2)) {
                Messages.info("Sucesso", "Plano 2 removido");
            } else {
                Messages.warn("Erro", "Ao deletar plano 2!");
                return false;
            }
        }
        plano2 = new Plano2();
        return true;
    }

    public boolean savePlano3(Dao dao) {
        if (plano3.getId() == -1) {
            plano3.setNumero(plano2.getNumero() + "." + plano3.getNumero());
            if (dao.save(plano3)) {
                Messages.info("Sucesso", "Plano 3 adicionado");
            } else {
                Messages.warn("Erro", "Ao inserir plano 3!");
                return false;
            }
        } else {
            if (dao.update(plano3)) {
                Messages.info("Sucesso", "Plano 3 atualizado");
            } else {
                Messages.warn("Erro", "Ao atualizad plano 3!");
                return false;
            }
        }
        return true;
    }

    public boolean deletePlano3(Dao dao) {
        if (plano3.getId() != -1) {
            if (dao.delete(plano3)) {
                Messages.info("Sucesso", "Plano 3 removido");
            } else {
                Messages.warn("Erro", "Ao deletar plano 3!");
                return false;
            }
        }
        plano3 = new Plano3();
        return true;
    }

    public boolean savePlano4(Dao dao) {
        if (plano4.getId() == -1) {
            plano4.setNumero(plano3.getNumero() + "." + plano4.getNumero());
            if (dao.save(plano4)) {
                Messages.info("Sucesso", "Plano 4 adicionado");
            } else {
                Messages.warn("Erro", "Ao inserir plano 4!");
                return false;
            }
        } else {
            if (dao.update(plano4)) {
                Messages.info("Sucesso", "Plano 4 atualizado");
            } else {
                Messages.warn("Erro", "Ao atualizad plano 4!");
                return false;
            }

        }
        return true;
    }

    public boolean deletePlano4(Dao dao) {
        if (plano4.getId() != -1) {
            if (dao.delete(plano4)) {
                Messages.info("Sucesso", "Plano 4 removido");
            } else {
                Messages.warn("Erro", "Ao deletar plano 4!");
                return false;
            }
        }
        plano4 = new Plano4();
        return true;
    }

    public boolean savePlano5(Dao dao) {
        if (plano5.getId() == -1) {
            plano5.setContaBanco(null);
            if (dao.save(plano5)) {
                Messages.info("Sucesso", "Plano 5 adicionado");
            } else {
                Messages.warn("Erro", "Ao inserir plano 5!");
                return false;
            }
        } else {
            if (dao.update(plano5)) {
                Messages.warn("Sucesso", "Plano 5 atualizado");
            } else {
                Messages.warn("Erro", "Ao atualizar plano 5!");
                return false;
            }
        }
        return true;
    }

    public boolean deletePlano5(Dao dao) {
        if (plano5.getId() != -1) {
            if (dao.delete(plano5)) {
                Messages.info("Sucesso", "Plano 5 removido");
            } else {
                Messages.warn("Erro", "Ao deletar plano 5!");
                return false;
            }
        }
        plano5 = new Plano5();
        return true;
    }

    public String addPlano1() {
        if (selectedPlano == 1) {
            plano = new Plano();
            PF.openDialog("dlg_plano1");
        }
        return null;
    }

    public void addNew(int indexRow) {
        if (selectedPlano == 1) {
            plano2 = new Plano2();
            plano = (Plano) listaPlanoContas.get(indexRow).getPlano5().getPlano4().getPlano3().getPlano2().getPlano();
            plano2.setPlano(plano);
            PF.openDialog("dlg_plano2");
        } else if (selectedPlano == 2) {
            plano3 = new Plano3();
            plano2 = (Plano2) listaPlanoContas.get(indexRow).getPlano5().getPlano4().getPlano3().getPlano2();
            plano3.setPlano2(plano2);
            PF.openDialog("dlg_plano3");
        } else if (selectedPlano == 3) {
            plano4 = new Plano4();
            plano3 = (Plano3) listaPlanoContas.get(indexRow).getPlano5().getPlano4().getPlano3();
            plano4.setPlano3(plano3);
            PF.openDialog("dlg_plano4");
        } else if (selectedPlano == 4) {
            plano5 = new Plano5();
            plano4 = (Plano4) listaPlanoContas.get(indexRow).getPlano5().getPlano4();
            plano5.setPlano4(plano4);
            PF.openDialog("dlg_plano5");
        } else if (selectedPlano == 5) {
            plano5 = (Plano5) listaPlanoContas.get(indexRow).getPlano5();
        }
    }

    public List<ListPlanoConta> getListaPlanoContas() {
        PlanoDao planoDao = new PlanoDao();
        List listaAuxiliar;
        ListPlanoConta listPlanoConta = new ListPlanoConta();
        Plano5 p5 = new Plano5();
        if (listaPlanoContas.isEmpty()) {
            if (selectedPlano == 1) {
                listaAuxiliar = planoDao.findPlanosByClass("Plano", SessaoCliente.get().getId());
                for (Object listaAuxiliar1 : listaAuxiliar) {
                    p5.getPlano4().getPlano3().getPlano2().setPlano((Plano) listaAuxiliar1);
                    listPlanoConta.configuraPlano(p5, "Plano 1", "i_panel_plano1", "plano1", "dlg_plano1");
                    listaPlanoContas.add(listPlanoConta);
                    listPlanoConta = new ListPlanoConta();
                    p5 = new Plano5();
                }

            } else if (selectedPlano == 2) {
                listaAuxiliar = planoDao.findPlanosByClass("Plano2", SessaoCliente.get().getId());
                plano5.getPlano4().getPlano3().getPlano2().setPlano(plano);
                for (Object listaAuxiliar1 : listaAuxiliar) {
                    p5.getPlano4().getPlano3().setPlano2((Plano2) listaAuxiliar1);
                    listPlanoConta.configuraPlano(p5, "Plano 2", "i_panel_plano2", "plano2", "dlg_plano2");
                    listaPlanoContas.add(listPlanoConta);
                    listPlanoConta = new ListPlanoConta();
                    p5 = new Plano5();
                }

            } else if (selectedPlano == 3) {
                listaAuxiliar = planoDao.findPlanosByClass("Plano3", SessaoCliente.get().getId());
                for (Object listaAuxiliar1 : listaAuxiliar) {
                    p5.getPlano4().setPlano3((Plano3) listaAuxiliar1);
                    listPlanoConta.configuraPlano(p5, "Plano 3", "i_panel_plano3", "plano3", "dlg_plano3");
                    listaPlanoContas.add(listPlanoConta);
                    listPlanoConta = new ListPlanoConta();
                    p5 = new Plano5();
                }
            } else if (selectedPlano == 4) {
                listaAuxiliar = planoDao.findPlanosByClass("Plano4", SessaoCliente.get().getId());
                for (Object listaAuxiliar1 : listaAuxiliar) {
                    p5.setPlano4((Plano4) listaAuxiliar1);
                    listPlanoConta.configuraPlano(p5, "Plano 4", "i_panel_plano5", "plano4", "dlg_plano4");
                    listaPlanoContas.add(listPlanoConta);
                    listPlanoConta = new ListPlanoConta();
                    p5 = new Plano5();
                }
            } else if (selectedPlano == 5) {
                listaAuxiliar = planoDao.findPlanosByClass("Plano5", SessaoCliente.get().getId());
                for (Object listaAuxiliar1 : listaAuxiliar) {
                    p5 = (Plano5) listaAuxiliar1;
                    listPlanoConta.configuraPlano(p5, "Plano 5", "i_panel_plano5", "plano5", "dlg_plano5");
                    listaPlanoContas.add(listPlanoConta);
                    listPlanoConta = new ListPlanoConta();
                    p5 = new Plano5();
                }
            }
        }
        return listaPlanoContas;
    }

    public void setListaPlanoContas(List<ListPlanoConta> listaPlanoContas) {
        this.listaPlanoContas = listaPlanoContas;
    }

    public void acaoPesquisaInicial() {
        listaPlanoContasPorPesquisa.clear();
        comoPesquisa = "I";
    }

    public void acaoPesquisaParcial() {
        listaPlanoContasPorPesquisa.clear();
        comoPesquisa = "P";
    }

    public Plano getPlano() {
        return plano;
    }

    public void setPlano(Plano plano) {
        this.plano = plano;
    }

    public Plano2 getPlano2() {
        return plano2;
    }

    public void setPlano2(Plano2 plano2) {
        this.plano2 = plano2;
    }

    public Plano3 getPlano3() {
        return plano3;
    }

    public void setPlano3(Plano3 plano3) {
        this.plano3 = plano3;
    }

    public Plano4 getPlano4() {
        return plano4;
    }

    public void setPlano4(Plano4 plano4) {
        this.plano4 = plano4;
    }

    public Plano5 getPlano5() {
        return plano5;
    }

    public void setPlano5(Plano5 plano5) {
        this.plano5 = plano5;
    }

    public int getIndexRow() {
        return indexRow;
    }

    public void setIndexRow(int indexRow) {
        this.indexRow = indexRow;
    }

    public boolean isLimpar() {
        return limpar;
    }

    public void setLimpar(boolean limpar) {
        this.limpar = limpar;
    }

    public List<SelectItem> getListaContaBanco() {
        ContaBancoDao contaBancoDao = new ContaBancoDao();
        if (listaContaBanco.isEmpty()) {
            List list = contaBancoDao.findAllByCliente(SessaoCliente.get().getId());
            for (int i = 0; i < list.size(); i++) {
                listaContaBanco.add(new SelectItem(i, (String) ((ContaBanco) list.get(i)).getBanco().getBanco() + " - " + ((ContaBanco) list.get(i)).getAgencia() + " - " + ((ContaBanco) list.get(i)).getConta(), Integer.toString(((ContaBanco) list.get(i)).getId())));
            }
        }
        return listaContaBanco;
    }

    public void setListaContaBanco(List<SelectItem> listaContaBanco) {
        this.listaContaBanco = listaContaBanco;
    }

    public int getIdPlanoConta() {
        return idPlanoConta;
    }

    public void setIdPlanoConta(int idPlanoConta) {
        this.idPlanoConta = idPlanoConta;
    }

    public String getDescPesquisa() {
        return descPesquisa;
    }

    public void setDescPesquisa(String descPesquisa) {
        this.descPesquisa = descPesquisa;
    }

    public String getPorPesquisa() {
        return porPesquisa;
    }

    public void setPorPesquisa(String porPesquisa) {
        this.porPesquisa = porPesquisa;
    }

    public String getComoPesquisa() {
        return comoPesquisa;
    }

    public void setComoPesquisa(String comoPesquisa) {
        this.comoPesquisa = comoPesquisa;
    }

    public List<Plano5> getListaPlanoPorPesquisa() {
        if (listaPlanoContasPorPesquisa.isEmpty()) {
            PlanoDao planoDao = new PlanoDao();
            String tipoPlano = "Plano5";
            if (selectedPlano == 1) {
                tipoPlano = "Plano";
            } else if (selectedPlano == 2) {
                tipoPlano = "Plano2";
            } else if (selectedPlano == 3) {
                tipoPlano = "Plano3";
            } else if (selectedPlano == 4) {
                tipoPlano = "Plano4";
            } else if (selectedPlano == 5) {
                tipoPlano = "Plano5";
            }
            if (descPesquisa.equals("")) {
                listaPlanoContasPorPesquisa = new ArrayList();
            } else {
                listaPlanoContasPorPesquisa = planoDao.findByPlano(descPesquisa, porPesquisa, comoPesquisa, "fin_plano5", SessaoCliente.get().getId());
            }
        }

        return listaPlanoContasPorPesquisa;
    }

    public void setListaPlanoPorPesquisa(List<Plano5> listaPlanoContasPorPesquisa) {
        this.listaPlanoContasPorPesquisa = listaPlanoContasPorPesquisa;
    }

    public Boolean[] getVisible() {
        return visible;
    }

    public void setVisible(Boolean[] visible) {
        this.visible = visible;
    }

    public String getTextNewPlano() {
        return textNewPlano;
    }

    public void setTextNewPlano(String textNewPlano) {
        this.textNewPlano = textNewPlano;
    }

    public void selectedDeletePlano(int tcase) {
        deletePlano = tcase;
    }
}
