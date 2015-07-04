/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.utill;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Shay
 */
public class SessionUtils {
    public static boolean isUserConnected(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(game.models.Consts.Connections.WSDL_URL) : null;
        return sessionAttribute != null;
    }
    
    public static String getWsdlURL(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(game.models.Consts.Connections.WSDL_URL) : null;
        
        if (sessionAttribute != null)
            return sessionAttribute.toString();
        else
            return null;
    }
    
    public static void saveConnection(HttpServletRequest request){
        request.getSession(true).setAttribute(game.models.Consts.Connections.WSDL_URL, request.getAttribute(game.models.Consts.Connections.WSDL_URL));
    }
    
    public static void disconnect(HttpServletRequest request){
        request.getSession(false).removeAttribute(game.models.Consts.Connections.WSDL_URL);
    }
}
