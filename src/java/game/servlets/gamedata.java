/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.servlets;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import server.json.JsonMessage;
import ws.roulette.RouletteWebServiceService;
import game.Constsants;
import java.net.URL;
import game.util.RouletteService;
import java.util.ArrayList;
import java.util.List;
import ws.roulette.Event;
import ws.roulette.GameDetails;
import ws.roulette.PlayerDetails;
import ws.roulette.RouletteWebService;

/**
 *
 * @author Dell
 */
@WebServlet(name = "gamedata", urlPatterns = {"/gamedata"})
public class gamedata extends HttpServlet {

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
        response.setContentType("application/json; charset=UTF-8");

        String json;

        try (PrintWriter out = response.getWriter()) {
            try {
                String type = request.getParameter("type").toString();
                System.out.println("type: " + type);
                String res = "";
                Gson gson = new Gson();
                switch (type) {
                    case "gameDetails":
                        GameDetails gameDetails = getGameDetails();
                        if (gameDetails != null) {
                            json = gson.toJson(gameDetails);
                        } else {
                            json = new JsonMessage(JsonMessage.Status.Error, "Game isn't initialized yet.").toString();
                        }
                        out.println(json);
                        break;
                    case "events":
                        int playerID = (int) request.getSession(true).getAttribute(Constsants.SESSION_PLAYER_ID);
                        int lastProccessedID = Integer.parseInt(request.getParameter("lastID"));
                        List<Event> events = getEvents(lastProccessedID, playerID);
                        json = gson.toJson(events);
                        out.println(json);
                        break;
                    case "players":
                        List<PlayerDetails> playersDetails = getPlayersDetails();
                        break;
                    default:
                        out.print(new JsonMessage(JsonMessage.Status.Error, "No return data was given."));
                        break;
                }
            } catch (Exception e) {
                out.print(new JsonMessage(JsonMessage.Status.Error, e.getMessage()));
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

    private GameDetails getGameDetails() {

        try {
            RouletteWebService service = RouletteService.getService();
            GameDetails gameDetails = service.getGameDetails("");
            return gameDetails;
        } catch (Exception e) {
            return null;
        }
    }

    private List<Event> getEvents(int lastProccessedID, int playerID) {
        try {
            RouletteWebService service = RouletteService.getService();
            return service.getEvents(lastProccessedID, playerID);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private List<PlayerDetails> getPlayersDetails() {
        try {
            RouletteWebService service = RouletteService.getService();
            return service.getPlayersDetails("");
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
