package br.com.clinicaintegrada.sistema.beans;

import br.com.clinicaintegrada.logSistema.Logger;
import br.com.clinicaintegrada.seguranca.Usuario;
import br.com.clinicaintegrada.seguranca.controleUsuario.SessaoCliente;
import br.com.clinicaintegrada.sistema.Combustivel;
import br.com.clinicaintegrada.sistema.Veiculo;
import br.com.clinicaintegrada.sistema.dao.VeiculoDao;
import br.com.clinicaintegrada.utils.Dao;
import br.com.clinicaintegrada.utils.Messages;
import br.com.clinicaintegrada.utils.Sessions;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class VeiculoBean implements Serializable {

    private Veiculo veiculo;
    private List<Veiculo> listVeiculo;
    private List<SelectItem> listCombustivel;
    private int idCombustivel;

    @PostConstruct
    public void init() {
        veiculo = new Veiculo();
        listVeiculo = new ArrayList<>();
        listCombustivel = new ArrayList<>();
        setIdCombustivel(0);
    }

    @PreDestroy
    public void destroy() {
        Sessions.remove("veiculoBean");
        Sessions.remove("veiculoPesquisa");
    }

    public void clear() {
        Sessions.remove("veiculoBean");
    }

    public void save() {
        Dao dao = new Dao();
        if (listCombustivel.isEmpty()) {
            Messages.warn("Validação", "Cadastrar combustíveis!");
            return;
        }
        veiculo.setCombustivel((Combustivel) dao.find(new Combustivel(), Integer.parseInt(listCombustivel.get(idCombustivel).getDescription())));
        if (veiculo.getDescricao().isEmpty()) {
            Messages.warn("Validação", "Informar nome do veículo (Modelo/Ano/Marca) (Ex. Fusca/2014/Volkswagen)!");
            return;
        }
        if (veiculo.getPlaca().isEmpty()) {
            Messages.warn("Validação", "Informar a placa do veículo!");
            return;
        }
        if (veiculo.getKmLitro() <= 0) {
            Messages.warn("Validação", "Informar a quantidade de quilometros por litro!");
            return;
        }
        Logger logger = new Logger();
        if (veiculo.getId() == -1) {
            VeiculoDao veiculoDao = new VeiculoDao();
            if (veiculoDao.existeVeiculo(veiculo.getDescricao(), veiculo.getPlaca(), veiculo.getCombustivel().getId(), SessaoCliente.get().getId())) {
                Messages.warn("Validação", "Veículo já cadastrado!");
                return;
            }
            veiculo.setCliente(SessaoCliente.get());
            if (dao.save(veiculo, true)) {
                logger.save(
                        " ID: " + veiculo.getId()
                        + " - Descrição: [" + veiculo.getDescricao()
                        + " - Placa: " + veiculo.getPlaca()
                        + " - KM / LITRO: " + veiculo.getKmLitroString()
                );
                clear();
                Messages.info("Sucesso", "Registro adicionado");
            } else {
                Messages.warn("Erro", "Erro ao inserir registro!");
            }
        } else {
            Veiculo v = (Veiculo) dao.find(veiculo);
            String beforeUpdate = ""
                    + " ID: " + v.getId()
                    + " - Descrição: [" + v.getDescricao()
                    + " - Placa: " + v.getPlaca()
                    + " - KM / LITRO: " + v.getKmLitroString();
            if (dao.update(veiculo, true)) {
                logger.update(beforeUpdate,
                        " ID: " + veiculo.getId()
                        + " - Descrição: [" + veiculo.getDescricao()
                        + " - Placa: " + veiculo.getPlaca()
                        + " - KM / LITRO: " + veiculo.getKmLitroString()
                );
                clear();
                Messages.info("Sucesso", "Registro adicionado");
            } else {
                Messages.warn("Erro", "Erro ao inserir registro!");
            }
        }
    }

    public void delete(Veiculo v) {
        veiculo = v;
        Dao dao = new Dao();
        Logger logger = new Logger();
        if (veiculo.getId() != -1) {
            if (dao.delete(veiculo, true)) {
                logger.delete(
                        " ID: " + veiculo.getId()
                        + " - Descrição: [" + veiculo.getDescricao()
                        + " - Placa: " + veiculo.getPlaca()
                        + " - KM / LITRO: " + veiculo.getKmLitroString()
                );
                clear();
                Messages.info("Sucesso", "Registro removido");
            } else {
                Messages.warn("Erro", "Erro ao remover registro!");
            }
        }
    }

    public void edit(Object o) {
        Dao dao = new Dao();
        veiculo = (Veiculo) dao.rebind(o);
        for (int i = 0; i < listCombustivel.size(); i++) {
            if (veiculo.getCombustivel().getId() == Integer.parseInt(listCombustivel.get(i).getDescription())) {
                idCombustivel = i;
                break;
            }
        }
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public List<Veiculo> getListVeiculo() {
        if (listVeiculo.isEmpty()) {
            VeiculoDao veiculoDao = new VeiculoDao();
            listVeiculo = (List<Veiculo>) veiculoDao.pesquisaVeiculosPorCliente(SessaoCliente.get().getId());
        }
        return listVeiculo;
    }

    public void setListVeiculo(List<Veiculo> listVeiculo) {
        this.listVeiculo = listVeiculo;
    }

    public List<SelectItem> getListCombustivel() {
        if (listCombustivel.isEmpty()) {
            Dao dao = new Dao();
            List<Combustivel> list = (List<Combustivel>) dao.list(new Combustivel(), true);
            for (int i = 0; i < list.size(); i++) {
                listCombustivel.add(new SelectItem(i, list.get(i).getDescricao() + " - Valor litro: (R$) " + list.get(i).getValorLitroString(), "" + list.get(i).getId()));
            }
        }
        return listCombustivel;
    }

    public void setListCombustivel(List<SelectItem> listCombustivel) {
        this.listCombustivel = listCombustivel;
    }

    public int getIdCombustivel() {
        return idCombustivel;
    }

    public void setIdCombustivel(int idCombustivel) {
        this.idCombustivel = idCombustivel;
    }

}
