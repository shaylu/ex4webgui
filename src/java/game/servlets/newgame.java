/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.servlets;

import game.Constsants;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import server.json.JsonMessage;
import ws.roulette.RouletteType;
import ws.roulette.RouletteWebService;
import ws.roulette.RouletteWebServiceService;

/**
 *
 * @author Dell
 */
@WebServlet(name = "newgame", urlPatterns = {"/newgame"})
public class newgame extends HttpServlet {
    
    public static final String GAME_NAME_FIELD = "txtGameName";
    public static final String ROULETTE_TYPE_FIELD = "rdRouletteType";
    public static final String HUMAN_PLAYERS_FIELD = "cmbHumanPlayers";
    public static final String COMP_PLAYERS_FIELD = "cmbComputerPlayers";
    public static final String INIT_MONEY_FIELD = "txtAmountOfMoney";
    public static final String MIN_BETS_FIELD = "txtMinBetsPerPlayer";
    public static final String MAX_BETS_FIELD = "txtMaxBetsPerPlayer";
    public static final String XML_FILE_FIELD = "fileXMLFile";

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
//        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            createGameFromSettings(request, response, out);

//            if (request.getParameter("action") != null) {
//                String action = request.getParameter("action");
//                switch (action) {
//                    case "GameFromSettings":
//                        createGameFromSettings(request, response, out);
//                        break;
//                }
//            }

            /* TODO output your page here. You may use following sample code. */
        }
    }

    private void createGameFromSettings(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
//        response.setContentType("application/json");

        JsonMessage message;
        String name;
        RouletteType type;
        int humanPlayers, computerPlayers, initMoney, minBets, maxBets, id;

        try {
            name = request.getParameter(GAME_NAME_FIELD);
            type = RouletteType.fromValue(request.getParameter(ROULETTE_TYPE_FIELD));
            humanPlayers = Integer.parseInt(request.getParameter(HUMAN_PLAYERS_FIELD));
            computerPlayers = Integer.parseInt(request.getParameter(COMP_PLAYERS_FIELD));
            initMoney = Integer.parseInt(request.getParameter(INIT_MONEY_FIELD));
            minBets = Integer.parseInt(request.getParameter(MIN_BETS_FIELD));
            maxBets = Integer.parseInt(request.getParameter(MAX_BETS_FIELD));
        } catch (Exception e) {
            message = new JsonMessage(JsonMessage.Status.Error, "Failed to parse user settings.");
            response.setContentType("application/json; charset=UTF-8");
            out.println(message);
            return;
        }

        try {
            URL url = new URL(Constsants.SERVICE_URL);
            RouletteWebServiceService service = new RouletteWebServiceService(url);
            RouletteWebService context = service.getRouletteWebServicePort();
            context.createGame(computerPlayers, humanPlayers, initMoney, minBets, maxBets, name, type);
        } catch (Exception e) {
            message = new JsonMessage(JsonMessage.Status.Error, e.getMessage());
            response.setContentType("application/json; charset=UTF-8");
            out.println(message);
            return;
        }

        response.setContentType("application/json; charset=UTF-8");
         message = new JsonMessage(JsonMessage.Status.Success, "Successfully created new game.");
        out.println(message);
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
