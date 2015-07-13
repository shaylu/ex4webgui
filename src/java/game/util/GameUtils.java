/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.util;

import game.Constsants;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import server.json.JsonMessage;
import ws.roulette.GameDetails;
import ws.roulette.GameStatus;

/**
 *
 * @author Dell
 */
public class GameUtils {

    public static boolean canJoinGame() {
        boolean res = true;
        try {
            GameDetails gameDetails = RouletteService.getService().getGameDetails("");
            if (gameDetails == null) {
                res = false;
            } else if (gameDetails.getStatus() == GameStatus.ACTIVE || gameDetails.getStatus() == GameStatus.FINISHED) {
                res = false;
            }
        } catch (Exception e) {
            res = false;
        }

        return res;
    }

    public static boolean isUserPlaying(HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        if (session.getAttribute(Constsants.SESSION_PLAYER_NAME) != null && session.getAttribute(Constsants.SESSION_PLAYER_ID) != null) {
            return true;
        } else {
            return false;
        }
    }

    public static String getPlayerName(HttpServletRequest request) {
        HttpSession session = request.getSession();
        return session.getAttribute(Constsants.SESSION_PLAYER_NAME).toString();
    }

    public static int getPlayerID(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session != null && session.getAttribute(Constsants.SESSION_PLAYER_ID) != null) {
            return Integer.parseInt(session.getAttribute(Constsants.SESSION_PLAYER_ID).toString());
        } else {
            return 0;
        }
    }
    
    public static String getGameName(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        if (session.getAttribute(Constsants.SESSION_GAME_NAME) != null){
            return session.getAttribute(Constsants.SESSION_GAME_NAME).toString();
        }
        else {
            throw new Exception("player is not signed to a game.");
        }
    }

    public static void leaveGame(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute(Constsants.SESSION_PLAYER_NAME, null);
        session.setAttribute(Constsants.SESSION_PLAYER_ID, null);
        session.setAttribute(Constsants.SESSION_GAME_NAME, null);
    }
}
