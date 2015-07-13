/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.testing.servlets;

import com.google.gson.Gson;
import game.util.GameUtils;
import game.util.RouletteService;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import server.json.JsonMessage;
import ws.roulette.GameDetails;
import ws.roulette.PlayerDetails;
import ws.roulette.RouletteWebService;

/**
 *
 * @author Dell
 */
@WebServlet(name = "getPlayersPanel", urlPatterns = {"/tests/getPlayersPanel"})
public class getPlayersPanel extends HttpServlet {

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
            try {
                if (request.getParameter("gameName") == null)
                    throw new Exception("game name not given.");
                
                String gameName = request.getParameter("gameName");
                RouletteWebService service = RouletteService.getService();
                String res = "";
                List<PlayerDetails> playersDetails = service.getPlayersDetails(gameName);
                for (PlayerDetails playersDetail : playersDetails) {
                    String playerName = playersDetail.getName();
                    String playerMoney = String.valueOf(playersDetail.getMoney());
                    String playerClass = (playerName == GameUtils.getPlayerName(request)) ? "player current" : "player";
                    res += "<div class='" + playerClass + "' name='" + playerName +"'><div class='playerName'>" + playerName +"</div><div class='playerMoney'>" + playerMoney +"</div></div>";
                }
                out.println(res);
            } catch (Exception e) {
                out.println("Oops, something went wrong, " + e.getMessage());
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

}
