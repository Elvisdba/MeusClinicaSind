//package br.com.clinicaintegrada.seguranca.controleUsuario;
//
//import javax.faces.application.NavigationHandler;
//import javax.faces.context.FacesContext;
//
//public class SemanticNavigationHandler extends NavigationHandler {
//
//    private NavigationHandler handler;
//
//    @Override
//    public void handleNavigation(FacesContext fc, String string, String string1) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    enum Action {
//
//        refresh, reTurn, forward, backward, login, exitSession,
//        success, accessDeny, nothing
//    }
//    private String currentAction;
//
//    public SemanticNavigationHandler(NavigationHandler handler) {
//        super();
//        this.handler = handler;
//    }
//
//    @Override
//    public void handleNavigation(FacesContext fc, String actionMethod, String actionName) {
//        Action action;
//        NavigationPoint point;
//        currentAction = actionName;
//        SessionHandler sessionHandler = (SessionHandler) AppHandler.getObjectById("sessionHandler");
////    action = defineNavigationAction(sessionHandler);
//        switch (action) {
////    case login: 
////      point = new NavigationPoint(actionName, actionMethod, fc.getViewRoot().getViewId()); 
////      sessionHandler.pushReturnPoint(point);
////      break;
////    case reTurn:
////      point = sessionHandler.popReturnPoint();
////      currentAction = point.getAction();
////      actionMethod = point.getActionMethod();
////      fc.getViewRoot().setViewId(point.getViewId());
////      break;      
////    case exitSession:
////      AppHandler.endSession();
////      break;
////    case refresh:
////      currentAction = null;
////      break;
////    case forward:
////      point = new NavigationPoint(actionName, actionMethod, fc.getViewRoot().getViewId()); 
////      sessionHandler.pushTrailPoint(point);
////      break;
//            case backward:
//                point = sessionHandler.popTrailPoint();
//                currentAction = point.getAction();
//                actionMethod = point.getActionMethod();
//                fc.getViewRoot().setViewId(point.getViewId());
//                break;
//        }
//        handler.handleNavigation(fc, actionMethod, currentAction);
//    }
//
//    public Action defineNavigationAction(SessionHandler sessionHandler) {
//        if (currentAction != null) {
//            if (!sessionHandler.isReturnPointStackEmpty()
//                    && currentAction.equals(Action.success.name())) {
//                return Action.reTurn;
//            }
////      if (currentAction.indexOf("access@") > -1) {
////        if (sessionHandler.isLogin()) {
////          int atPosition = currentAction.indexOf("@");
////          String action = currentAction.substring(0, atPosition);
////          String entity = currentAction.substring(atPosition+1);
////          if (!sessionHandler.isActionPermitted(entity, action)) {
////            currentAction = Action.accessDeny.name();
////          }
////          return Action.nothing;
////        }
////        else 
////          currentAction = Action.login.name();
////      }
//            try {
//                return Action.valueOf(currentAction);
//            } catch (Exception ex) {
//            }
//        }
//        return Action.nothing;
//    }
//}
