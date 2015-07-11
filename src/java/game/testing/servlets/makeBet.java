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
import java.util.ArrayList;
import java.util.Collections;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.catalina.tribes.util.Arrays;
import server.json.JsonMessage;
import ws.roulette.BetType;
import ws.roulette.RouletteWebService;

/**
 *
 * @author Shay
 */
@WebServlet(name = "makeBet", urlPatterns = {"/tests/makeBet"})
public class makeBet extends HttpServlet {

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
//                {'playername': playername, 'amount': amount, 'betType': betType, 'numbers': numbers}
                
                if (GameUtils.isUserPlaying(request) != true){
                    out.println(new JsonMessage(JsonMessage.Status.Error, "user is not playing."));
                    return;
                }
                
                int amount = Integer.parseInt(request.getParameter("amount"));
                ws.roulette.BetType betType =  ws.roulette.BetType.fromValue(request.getParameter("betType"));
                
                ArrayList<Integer> numbers = new ArrayList<>();
                if (request.getParameter("numbers") != ""){
                    String[] split = request.getParameter("numbers").split(",");
                    for (String num : split) {
                        numbers.add(Integer.parseInt(num));
                    }
                }
                
                RouletteWebService service = RouletteService.getService();
                service.makeBet(amount, betType, numbers, GameUtils.getPlayerID(request));
                
                out.println(new JsonMessage(JsonMessage.Status.Success, ""));
                
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
