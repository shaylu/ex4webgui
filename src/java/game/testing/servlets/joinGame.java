/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.testing.servlets;

import com.google.gson.Gson;
import game.util.RouletteService;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import server.json.JsonMessage;
import ws.roulette.GameDetails;
import ws.roulette.RouletteWebService;
import game.Constsants;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Shay
 */
@WebServlet(name = "joinGame", urlPatterns = {"/tests/joinGame"})
public class joinGame extends HttpServlet {

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
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            try {
                if (request.getParameter("playerName") == null || request.getParameter("gameName") == null) {
                    out.println(new JsonMessage(JsonMessage.Status.Error, "parameters missing."));
                    return;
                }

                String playerName = request.getParameter("playerName");
                String gameName = request.getParameter("gameName");

                HttpSession session = request.getSession(true);
                if (session.getAttribute(Constsants.SESSION_PLAYER_NAME) != null) {
                    out.println(new JsonMessage(JsonMessage.Status.Error, "Player already in game."));
                    return;
                }

                RouletteWebService service = RouletteService.getService(request);
                int id = service.joinGame(gameName, playerName);
                session.setAttribute(Constsants.SESSION_PLAYER_NAME, playerName);
                session.setAttribute(Constsants.SESSION_PLAYER_ID, id);
                session.setAttribute(Constsants.SESSION_GAME_NAME, gameName);

                out.println(new JsonMessage(JsonMessage.Status.Success, "Player joined a game."));
            } catch (Exception e) {
                out.println(new JsonMessage(JsonMessage.Status.Error, e.getMessage()));
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
