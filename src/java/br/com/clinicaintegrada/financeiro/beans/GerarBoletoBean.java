package br.com.clinicaintegrada.financeiro.beans;

import br.com.clinicaintegrada.financeiro.LoteBoleto;
import br.com.clinicaintegrada.financeiro.dao.FinanceiroDao;
import br.com.clinicaintegrada.pessoa.Pessoa;
import br.com.clinicaintegrada.utils.DataHoje;
import br.com.clinicaintegrada.utils.Messages;
import br.com.clinicaintegrada.utils.Sessions;
import br.com.clinicaintegrada.utils.Dao;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class GerarBoletoBean {

    private Pessoa pessoa;
    private List listData;
    private List listServicoSemCobranca;
    private List<Pessoa> listPessoa;
    private List<LoteBoleto> listGerados;
    private LoteBoleto[] selectedGerados;
    private boolean imprimeVerso;

    private String ano;
    private String mes;


    public GerarBoletoBean() {
        pessoa = new Pessoa();
        listPessoa = new ArrayList();
        listGerados = new ArrayList<>();
        imprimeVerso = true;
        selectedGerados = null;

        ano = DataHoje.DataToArrayString(DataHoje.data())[2];
        mes = DataHoje.DataToArrayString(DataHoje.data())[1];
        listData = new ArrayList();
        getListServicoSemCobranca();
    }

    public void gerarTodos() {
        if (!listServicoSemCobranca.isEmpty()) {
            Messages.warn("Atenção", "Não é possível gerar mensalidade, verifique os Serviços e Conta Cobrança!");
            return;
        }

        Dao dao = new Dao();
        dao.openTransaction();

        if (listData.isEmpty()) {
            if (dao.executeQuery("select func_geramensalidades(null, '" + mes + "/" + ano + "')")) {
                dao.commit();
                listGerados.clear();
                Messages.info("Sucesso", "Geração de Mensalidades concluída!");
            } else {
                dao.rollback();
                Messages.warn("Erro", "Erro ao gerar Mensalidades!");
            }
        } else {
            for (Object listDatax : listData) {
                String vencto = listDatax.toString().substring(0, 2) + "/" + listDatax.toString().substring(3, 7);
                if (dao.executeQuery("select func_geramensalidades(null, '" + vencto + "')")) {
                    listGerados.clear();
                    Messages.info("Sucesso", "Geração de Mensalidades " + vencto + " concluída!");
                } else {
                    dao.rollback();
                    Messages.warn("Erro", "Erro ao gerar Mensalidades!");
                    return;
                }
            }

            dao.commit();
        }
    }

    public void gerarLista() {
        if (!listServicoSemCobranca.isEmpty()) {
            Messages.warn("Atenção", "Não é possível gerar mensalidade, verifique os Serviços e Conta Cobrança!");
            return;
        }

        Dao dao = new Dao();
        dao.openTransaction();
        boolean erro = false;

        for (Pessoa pe : listPessoa) {
            if (listData.isEmpty()) {
                if (dao.executeQuery("select func_geramensalidades(" + pe.getId() + ", '" + mes + "/" + ano + "')")) {
                    Messages.info("Sucesso", "Geração de Mensalidades concluída!");
                } else {
                    erro = true;
                    Messages.warn("Erro", "Erro ao gerar Mensalidades!");
                }
            } else {
                for (Object listDatax : listData) {
                    String vencto = listDatax.toString().substring(0, 2) + "/" + listDatax.toString().substring(3, 7);
                    if (dao.executeQuery("select func_geramensalidades(" + pe.getId() + ", '" + vencto + "')")) {
                        Messages.info("Sucesso", "Geração de Mensalidades " + vencto + " concluída!");
                    } else {
                        erro = true;
                        Messages.warn("Erro", "Erro ao gerar Mensalidades!");
                    }
                }
            }
        }

        if (erro) {
            dao.rollback();
        } else {
            dao.commit();
            listGerados.clear();
        }
    }

    public void addPessoa() {
        listPessoa.add(pessoa);
        pessoa = new Pessoa();
    }

    public void removePessoa(int index) {
        listPessoa.remove(index);
    }

    public void addData() {
        if (!listData.isEmpty()) {
            boolean existe = false;
            for (int i = 0; i < listData.size(); i++) {
                if (listData.get(i).toString().equals(mes + "/" + ano)) {
                    existe = true;
                }
            }
            if (!existe) {
                listData.add(mes + "/" + ano);
            }

        } else {
            listData.add(mes + "/" + ano);
        }
    }

    public void removeData(int index) {
        listData.remove(index);
    }

    public void removePessoa() {
        pessoa = new Pessoa();
    }

    public Pessoa getPessoa() {
        if (Sessions.exists("pessoaPesquisa")) {
            pessoa = (Pessoa) Sessions.getObject("pessoaPesquisa");
            Sessions.remove("pessoaPesquisa");
        }
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public List<Pessoa> getListPessoa() {
        return listPessoa;
    }

    public void setListPessoa(List<Pessoa> listPessoa) {
        this.listPessoa = listPessoa;
    }

    public List<LoteBoleto> getListGerados() {
        if (listGerados.isEmpty()) {
            listGerados = (new Dao()).list(new LoteBoleto());
        }
        return listGerados;
    }

    public void setListGerados(List<LoteBoleto> listGerados) {
        this.listGerados = listGerados;
    }

    public boolean isImprimeVerso() {
        return imprimeVerso;
    }

    public void setImprimeVerso(boolean imprimeVerso) {
        this.imprimeVerso = imprimeVerso;
    }

    public String getAno() {
        if (ano.length() != 4) {
            ano = DataHoje.DataToArrayString(DataHoje.data())[2];
        }
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public List getListaData() {
        return listData;
    }

    public void setListData(List listData) {
        this.listData = listData;
    }

    public List getListServicoSemCobranca() {
        if (listServicoSemCobranca.isEmpty()) {
            FinanceiroDao financeiroDao = new FinanceiroDao();

            listServicoSemCobranca = financeiroDao.listServicosSemCobranca();

            if (!listServicoSemCobranca.isEmpty()) {
                Messages.fatal("Atenção", "Não é possível gerar mensalidades sem antes definir Conta Cobrança para os seguintes Serviços:");
//                for (List row : listServicoSemCobranca) {
//                    Messages.info("Serviço / Tipo: ", row.get(1).toString() + " - " + row.get(3).toString());
//                }
            }
        }
        return listServicoSemCobranca;
    }

    public void setListaServicoSemCobranca(List listServicoSemCobranca) {
        this.listServicoSemCobranca = listServicoSemCobranca;
    }

    public LoteBoleto[] getSelectedGerados() {
        return selectedGerados;
    }

    public void setSelectedGerados(LoteBoleto[] selectedGerados) {
        this.selectedGerados = selectedGerados;
    }
}
