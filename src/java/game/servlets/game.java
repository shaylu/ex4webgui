/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ws.roulette.RouletteType;
import game.ui.UITable;
import game.Constsants;
import game.util.GameUtils;
import game.util.RouletteService;

/**
 *
 * @author Shay
 */
public class game extends HttpServlet {

    

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
            if (GameUtils.isUserPlaying(request) == true) {
                printGame(request, response, out);
            } else {
                response.sendRedirect("index.html");
            }
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

    private void printGame(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
        printTopHTML(request, response, out);

        RouletteType type;
        UITable creator;
        String rouletteImage;
        
        try {
             type = RouletteService.getRouletteType();
        } catch (Exception e) {
            out.printf("Failed to get roulette type from server, ", e.getMessage());
            return;
        }
        
        creator = new UITable(type);
        rouletteImage = (type == RouletteType.FRENCH) ? "Images/frenchRoulette.gif" : "Images/americanRoulette.gif";

        out.println("<div class='container'>");
        out.println("   <div class='row gap'><button id='getEvents'>Get Events</button> <a href='index.html'>Home</a></div>");

        // ============ GAME AREA ======================
        out.println("<div class='game-area' style='display: none;' data-playername='" + GameUtils.getPlayerName(request) + "' data-roulettetype='" + type.name() + "'>");
        out.println("   <div class='row'><div class='panel-body'><div class='players-div'></div></div></div>");
        out.println("   <div class='row'>");
        out.println("       <div class='panel panel-body'>"
                + "             <div class='table-div inline'>"
                + "             " + creator.createHTML()
                + "              </div>"
                + "              <div id='roulette-container'>"
                + "                <img class='ball' src='Images/ball.png' />"
                + "                <img class='roulette' src='" + rouletteImage + "' />"
                + "              </div>"
                + "         </div><h3 class='timer'></h3>"
                + "     </div>");
        out.println("       <div class='panel panel-body'>"
                + "             <button id='resign'>Resign</button>"
                + "             <div class='coin' data-amount='5'><img src='Images/blue_chip.png' /></div>"
                + "         </div>");
        out.println("   <div class='row'>"
                + "         <textarea id='txtLog'></textarea>"
                + "     </div>");
        out.println("</div>");
        // ============ END GAME AREA ===================

        // ============ SCORE BOARD AREA ================
        out.println("<div class='scoreboard-area' style='display: none;'>");
        out.println("</div>");
        // ============ END SCORE BOARD AREA ============

        out.println("</div>");
        printScripts(request, response, out);
        printBottomHTML(request, response, out);
    }

    private void printTopHTML(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Roulette Game</title>");
        out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("<link href='http://fonts.googleapis.com/css?family=Open+Sans:400,300,700' rel='stylesheet' type='text/css'>");
        out.println("<link href='Content/bootstrap.css' rel='stylesheet' type='text/css'/>");
        out.println("<link href='Content/GameSceneStyleSheet.css' rel='stylesheet' type='text/css'/>");
        out.println("<link href='Content/roulette.css' rel='stylesheet' type='text/css'/>");
        out.println("</head>");
        out.println("<body>");
    }

    private void printScripts(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
        out.println("<script src='Scripts/jquery-2.1.4.js' type='text/javascript'></script>");
        out.println("<script src='Scripts/jquery-ui.min.js' type='text/javascript'></script>");
        out.println("<script src='Scripts/qtransform.js' type='text/javascript'></script>");
        out.println("<script src='Scripts/bootstrap.js' type='text/javascript'></script>");
        out.println("<script src='Scripts/jquery.rotate.1-1.js' type='text/javascript'></script>");
        out.println("<script src='Scripts/GameScene.js' type='text/javascript'></script>");
    }

    private void printBottomHTML(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
        out.println("</body>");
        out.println("</html>");
    }

}
