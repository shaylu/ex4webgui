/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.util;

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
}
