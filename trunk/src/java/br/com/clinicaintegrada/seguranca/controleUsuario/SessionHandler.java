package br.com.clinicaintegrada.seguranca.controleUsuario;

import java.util.Date;
import java.util.Stack;

public class SessionHandler {

    private String moduleId;

    private Stack returnPointStack;
    private Stack trailPointStack;

    public SessionHandler() {
        returnPointStack = new Stack();
        trailPointStack = new Stack();
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getModuleId() {
        return moduleId;
    }

    public synchronized Date getCurrentDate() {
        return new Date();
    }

    public void pushReturnPoint(NavigationPoint point) {
        returnPointStack.push(point);
    }

    public boolean isReturnPointStackEmpty() {
        return returnPointStack.empty();
    }

    public void pushTrailPoint(NavigationPoint point) {
        trailPointStack.push(point);
    }

    public boolean isTrailPointStackEmpty() {
        return trailPointStack.empty();
    }
}
