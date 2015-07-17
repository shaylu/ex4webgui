/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.testing.servlets;

import game.util.RouletteService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import static java.util.Arrays.stream;
import static java.util.stream.StreamSupport.stream;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import server.json.JsonMessage;
import ws.roulette.RouletteType;
import ws.roulette.RouletteWebService;

/**
 *
 * @author Shay
 */
@WebServlet(name = "createGameFromXML", urlPatterns = {"/tests/createGameFromXML"})
@MultipartConfig
public class createGameFromXML extends HttpServlet {

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

            Part filePart = request.getPart("file");
            String data = "";
            BufferedReader reader = new BufferedReader(new InputStreamReader(filePart.getInputStream()));
            for (String line; (line = reader.readLine()) != null;) {
                data += line;
            }

            try {
                RouletteWebService service = RouletteService.getService();
                service.createGameFromXML(data);
                response.sendRedirect("../create.html?status=Sucess&message=Created A Game!");
            } catch (Exception e) {
                response.sendRedirect("../create.html?status=Error&message=" + e.getMessage());
            }
//            /* TODO output your page here. You may use following sample code. */
//            BufferedReader reader = new BufferedReader(new InputStreamReader(request.getParameter("file")));
//            for (String line; (line = reader.readLine()) != null;) {
//                System.out.println(line);
//            }     
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
