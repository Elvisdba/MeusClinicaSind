
package br.com.clinicaintegrada.seguranca.controleUsuario;

public class NavigationPoint {

    private String action;
    private String actionMethod;
    private String viewId;

    public NavigationPoint(String action, String actionMethod, String viewId) {
        this.action = action;
        this.actionMethod = actionMethod;
        this.viewId = viewId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getActionMethod() {
        return actionMethod;
    }

    public void setActionMethod(String actionMethod) {
        this.actionMethod = actionMethod;
    }

    public String getViewId() {
        return viewId;
    }

    public void setViewId(String viewId) {
        this.viewId = viewId;
    }
}
