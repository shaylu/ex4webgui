/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.servlets;

import com.sun.corba.se.spi.presentation.rmi.StubAdapter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Shay
 */
public class connect extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<link href=\"Content/bootstrap.css\" rel=\"stylesheet\" />");
            out.println("<title>Servlet connect</title>");            
            out.println("</head>");
            out.println("<body>");
            
            if (game.utill.SessionUtils.isUserConnected(request))
            {
                response.sendRedirect("chatroom.html");
            }
            else if (request.getAttribute(game.models.Consts.Connections.WSDL_URL) != null){
                try {
                    
                } catch (Exception e) {
                }
            }
            else {
                showConnectForm(out);
            }
            
            
            out.println("<script src=\"Scripts/jquery-2.1.4.js\"></script>");
            out.println("<script src=\"Scripts/jquery-ui-1.11.4.js\"></script>");
            out.println("<script src=\"Scripts/bootstrap.js\"></script>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private boolean isUserConnected(HttpServletRequest request) {
        return game.utill.SessionUtils.isUserConnected(request);
    }

    private void showConnectForm(PrintWriter out) {
        out.println("<form method='GET' action='connect'>");
        out.println("<div class=\"container\">"
                + "        <div class=\"row well well-lg\">"
                + "            <div class=\"text-center\">"
                + "               <h1>Connect</h1>"
                + "                <div class=\"input-group\">"
                + "                    <input type=\"text\" class=\"form-control\" placeholder=\"WSDL Address\">"
                + "                    <span class=\"input-group-btn\">"
                + "                        <button class=\"btn btn-default\" type=\"submit\">Connect!</button>"
                + "                    </span>"
                + "                </div>"
                + "            </div>"
                + "        </div>"
                + "    </div>");
        out.println("</form>");
    }

}
