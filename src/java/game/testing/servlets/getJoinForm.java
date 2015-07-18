/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.testing.servlets;

import game.util.GameUtils;
import game.util.RouletteService;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ws.roulette.GameDetails;
import ws.roulette.GameStatus;
import ws.roulette.PlayerDetails;
import ws.roulette.RouletteWebService;

/**
 *
 * @author Dell
 */
@WebServlet(name = "getJoinForm", urlPatterns = {"/tests/getJoinForm"})
public class getJoinForm extends HttpServlet {

    public final static String PLAYER_NAME_INPUT = "txtPlayerName";

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
                String res;
                String gameName = request.getParameter("gameName");
                RouletteWebService service = RouletteService.getService(request);

                GameDetails gameDetails = service.getGameDetails(gameName);
                if (gameDetails.getStatus() == GameStatus.WAITING) {
                    if (gameDetails.isLoadedFromXML() == true) {
                        res = getXMLGameJoinForm(service, gameName);
                    } else {
                        res = getNormalJoinForm(gameName);
                    }
                    out.println(res);
                } else {
                    throw new Exception("The game is not waiting for players to join.");
                }
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


    private List<String> getUnusedPlayersNames(RouletteWebService service, String gameName) {
        ArrayList names = new ArrayList();

        try {
            List<PlayerDetails> playersDetails = service.getPlayersDetails(gameName);
            names.addAll(playersDetails.stream().map(x -> x.getName()).collect(Collectors.toList()));
        } catch (Exception e) {
            // nothing
        }

        return names;
    }

    private String getNormalJoinForm(String gameName) {
        String res = "<form name='frmJoinGame' method='post' action='tests/joinGame' data-gamename='" + gameName + "'>";
        res += "<input type='text' id='" + PLAYER_NAME_INPUT + "' name='" + PLAYER_NAME_INPUT + "' />";
        res += "<input type='submit' value='Join'>";
        res += "</form>";
        return res;
    }

    private String getXMLGameJoinForm(RouletteWebService service, String gameName) {
        String res = "<form name='frmJoinGame' method='post' action='tests/joinGame' data-gamename='" + gameName + "'>";
        res += "<select id='" + PLAYER_NAME_INPUT + "' name='" + PLAYER_NAME_INPUT + "'>";
        List<String> playerNames = getUnusedPlayersNames(service, gameName);
        for (String playerName : playerNames) {
            res += "<option value='" + playerName + "'>" + playerName + "</option>";
        }
        res += "</select>";
        res += "<input type='submit' value='Join'>";
        res += "</form>";
        return res;
    }

}
