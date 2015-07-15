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
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ws.roulette.Event;
import ws.roulette.PlayerDetails;
import ws.roulette.RouletteWebService;

/**
 *
 * @author Dell
 */
@WebServlet(name = "getScoreBoard", urlPatterns = {"/tests/getScoreBoard"})
public class getScoreBoard extends HttpServlet {

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
            try {
                if (GameUtils.isUserPlaying(request) == true) {
                    out.println(getScoreBoardHTML(request));
                } else {
                    response.sendRedirect("index.html");
                }
            } catch (Exception e) {
                out.println("Ooops, something went wrong, " + e.getMessage());
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

    private String getScoreBoardHTML(HttpServletRequest request) throws Exception {
        RouletteWebService service = RouletteService.getService();
        List<PlayerDetails> playersDetails = service.getPlayersDetails(GameUtils.getGameName(request));
        PlayerDetails[] sorted = playersDetails.stream().sorted((p1, p2) -> Integer.compare(p2.getMoney(),p1.getMoney())).toArray(PlayerDetails[]::new);
        
        String res = "";
        res += "<div class='score-board'>";
        res += "<div class='results'><h3>Score Board</h3>";

        for (int i = 0; i < sorted.length; i++) {
            PlayerDetails player = sorted[i];
            res += "<div class='result'>(" + (i + 1) + ")&#09;" + player.getName() + ": " + player.getMoney() +"</div>";
        }
        
        res += "</div>";
        res += "<div><button class='home-button'>Home</button></div>";
        res += "</div>";
        
        return res;
    }
}
