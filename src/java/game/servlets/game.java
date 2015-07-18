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
import java.util.List;
import ws.roulette.GameDetails;
import ws.roulette.GameDoesNotExists_Exception;
import ws.roulette.PlayerDetails;
import ws.roulette.RouletteWebService;

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
            try {
                if (GameUtils.isUserPlaying(request) == true) {
                    out.println(getGameHTML(request));
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

    private String getGameHTML(HttpServletRequest request) throws Exception {
        String gameName = GameUtils.getGameName(request);
        String playerName = GameUtils.getPlayerName(request);
        RouletteWebService service = RouletteService.getService(request);
        GameDetails gameDetails = service.getGameDetails(gameName);
        RouletteType rouletteType = gameDetails.getRouletteType();

        String table = new UITable(rouletteType).createHTML();
        String roulette = createRoulette(rouletteType);
        String timer = "<span id='lblTimer'></span>";
        String resignButton = "<button id='btnResign'>Resign</button>";
        String finishBettingButton = "<button id='btnFinishBetting'>Finish Betting</button>";
        String chips = createChipsArea();
        String quitButton = "<button id='btnQuit'>Quit</button>";
        String log = "<textarea id='txtLog'></textarea>";

        String HTML = "";
        HTML += getTopHTML();
        HTML += "  <div class='wrapper'>"
                + "<div class='container'>"
                + "     <div class='waiting-area'>Waiting for game to start...</div>"
                + "     <div class='game-area' style='display: none' data-playername='" + playerName + "' data-roulettetype='" + rouletteType + "' data-gamename='" + gameName + "'>"
                + "         <div class='row'>"
                + "             <div class='panel panel-default'>"
                + "                 <div class='panel-heading players'>"
                + "                 <!--PlayersPanel-->"
                + "                 </div>"
                + "             </div>"
                + "         </div>"
                + "         <div class='row game-table-area'>"
                + "             " + table
                + "             " + roulette
                + "         </div>"
                + "         <div class='row'>"
                + "             <div>"
                + "                 <div class='panel-body game-controls-area'>"
                + "                     " + timer
                + "                     " + chips
                + "                     " + finishBettingButton
                + "                     " + resignButton
                + "                     " + quitButton
                + "                 </div>"
                + "             </div>"
                + "         </div>"
                + "         <div class='row'>"
                + "             <div class='panel panel-default'>"
                + "                 <div class='panel-body'>"
                + "                 " + log
                + "                 </div>"
                + "             </div>"
                + "         </div>"
                + "     </div>"
                + " </div>"
                + "</div>";
        HTML += getScriptsHTML();
        HTML += getFooterHTML();

        return HTML;
    }

    private String createRoulette(RouletteType rouletteType) {
        String rouletteImage = (rouletteType == RouletteType.FRENCH) ? "frenchRoulette.gif" : "americanRoulette.gif";
        return "<div class='roulette-container'><span><img class='roulette' src='Images/" + rouletteImage + "' /><img class='ball' src='Images/ball.png' /></span></div>";
    }

    private String createChipsArea() {
        String res = "";

        res += "<div class='coins'>";
        res += "<div class='coin-box'><div class='coin' data-amount='1'><img src='Images/green_chip.png' /></div><div class='coin-description'>1</div></div>";
        res += "<div class='coin-box'><div class='coin' data-amount='5'><img src='Images/blue_chip.png' /></div><div class='coin-description'>5</div></div>";
        res += "<div class='coin-box'><div class='coin' data-amount='10'><img src='Images/red_chip.png' /></div><div class='coin-description'>10</div></div>";
        res += "<div class='coin-box'><div class='coin' data-amount='25'><img src='Images/black_chip.png' /></div><div class='coin-description'>25</div></div>";
        res += "</div>";

        return res;
    }

    private String getTopHTML() {
        String res = "";
        res += "<html>"
                + "<head>"
                + "<title>Roulette Game</title>"
                + "<meta name='viewport' content='width=device-width, initial-scale=1.0'>"
                + "<link href='http://fonts.googleapis.com/css?family=PT+Sans' rel='stylesheet' type='text/css'>"
                + "<link href='Content/bootstrap.css' rel='stylesheet' type='text/css'/>"
                + "<link href='Content/GameSceneStyleSheet.css' rel='stylesheet' type='text/css'/>"
                + "<link href='Content/roulette.css' rel='stylesheet' type='text/css'/>"
                + "</head>"
                + "<body>";

        return res;
    }

    private String getScriptsHTML() {
        String res = "";
        res += "<script src='Scripts/jquery-2.1.4.js' type='text/javascript'></script>";
        res += "<script src='Scripts/jquery-ui.min.js' type='text/javascript'></script>";
        res += "<script src='Scripts/jquery.transit.min.js' type='text/javascript'></script>";
        res += "<script src='Scripts/bootstrap.js' type='text/javascript'></script>";
        res += "<script src='Scripts/jquery.rotate.1-1.js' type='text/javascript'></script>";
        res += "<script src='Scripts/GameScene.js' type='text/javascript'></script>";
        return res;
    }

    private String getFooterHTML() {
        String res = "";
        res += "   </body>"
                + "</html>";

        return res;
    }
}
