package br.com.clinicaintegrada.utils;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@ViewScoped
public class PeopleController {

    private People people;

    public PeopleController() {
        people = new People();
    }

    public void save() {
        //pessoaDao.save(people);
        people = new People();
    }

    public void edit() {
        Dao dao = new Dao();
        Integer id = Integer.parseInt((String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id"));
        people = (People) dao.find(new People(), 1);
    }

    public void delete() {
        // pega o parametro passado no link
        Integer id = Integer.parseInt((String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id"));
        //pessoaDao.delete(id);
    }

    public People getPeople() {
        return people;
    }

    public void setPeople(People people) {
        this.people = people;
    }
}
