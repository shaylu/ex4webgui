/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.util;

import java.net.URL;
import game.Constsants;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Scanner;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ws.roulette.RouletteType;
import ws.roulette.RouletteWebService;
import ws.roulette.RouletteWebServiceService;

/**
 *
 * @author Dell
 */
public class RouletteService {
    public static String wsdlURL;
    
    public static String getAbsolutePathOfResource(String resouce) {
        URL url = RouletteService.class.getResource(resouce);
        return url != null ? url.getPath() : "?";
    }
//
//    public static String getResouceContent(String resource) {
//        StringBuilder result = new StringBuilder();
//        try (InputStream stream = RouletteService.class.getResourceAsStream(resource)) {
//            Scanner scanner = new Scanner(stream, "UTF-8");
//            while (scanner.hasNextLine()){
//                result.append(scanner.nextLine()).append("\n\r");
//            }
//        } catch (Exception exception){
//            return "Error: Failed to read file!";
//        }
//        return result.toString();
//    }
    
    public static RouletteWebService getService(HttpServletRequest request) throws MalformedURLException, SAXException, IOException, Exception{
        
        if (wsdlURL == null){
            loadWsdlUrl(request);
        }
        
        URL url = new URL(wsdlURL);
        RouletteWebServiceService service = new RouletteWebServiceService(url);
        
        return service.getRouletteWebServicePort();
    }
    
    public static RouletteType getRouletteType(String gameName, HttpServletRequest request) throws Exception {
        RouletteWebService service = getService(request);
        RouletteType rouletteType = service.getGameDetails(gameName).getRouletteType();
        return rouletteType;
    }

    private static void loadWsdlUrl(HttpServletRequest request) throws ParserConfigurationException, SAXException, IOException, Exception {
        
        URL xml = request.getServletContext().getResource(Constsants.CONFIG_XML_PATH);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	Document doc = dBuilder.parse(xml.openStream());
        NodeList nodes = doc.getDocumentElement().getElementsByTagName("WsdlURL");
        if (nodes.item(0) != null){
            Node item = nodes.item(0);
            String url = item.getTextContent();
            wsdlURL = url;
        }
        else {
            throw new Exception("No elements found.");
        }
    }
}
