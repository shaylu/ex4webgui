/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.util;

import java.net.URL;
import game.Constsants;
import java.net.MalformedURLException;
import ws.roulette.RouletteType;
import ws.roulette.RouletteWebService;
import ws.roulette.RouletteWebServiceService;

/**
 *
 * @author Dell
 */
public class RouletteService {
    public static RouletteWebService getService() throws MalformedURLException{
        URL url = new URL(Constsants.SERVICE_URL);
        RouletteWebServiceService service = new RouletteWebServiceService(url);
        
        return service.getRouletteWebServicePort();
    }
    
    public static RouletteType getRouletteType(String gameName) throws Exception {
        RouletteWebService service = getService();
        RouletteType rouletteType = service.getGameDetails(gameName).getRouletteType();
        return rouletteType;
    }
}
