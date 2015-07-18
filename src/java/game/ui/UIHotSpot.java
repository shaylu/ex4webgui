/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.ui;

import java.util.Arrays;
import java.util.List;
import ws.roulette.BetType;

/**
 *
 * @author Shay
 */
public class UIHotSpot {

    private String numbersArrayToString(List<Integer> numbers) {
        if (numbers == null){
            return "";
        }
        
        String res = "";
        for (int i = 0; i < numbers.size(); i++) {
            res += numbers.get(i);
            res += ",";
        }

        if (!res.equals("")) {
            res = res.substring(0, res.length() - 1);
        }

        return res;
    }

    public enum HotSpotColor {

        Red, Black, Green, Transparent;

        public static final List<Integer> reds = Arrays.asList(1, 3, 5, 7, 9, 12, 14, 16, 18, 19, 21, 23, 25, 27, 30, 32, 34, 36);
        public static final List<Integer> blacks = Arrays.asList(2, 4, 6, 8, 10, 11, 13, 15, 17, 20, 22, 24, 26, 28, 29, 31, 33, 34, 35);
        public static final List<Integer> greens = Arrays.asList(0, 37);

        public static HotSpotColor get(int number) {
            if (arrayContains(greens, number)) {
                return Green;
            } else if (arrayContains(reds, number)) {
                return Red;
            } else if (arrayContains(blacks, number)) {
                return Black;
            } else {
                return Transparent;
            }
        }

        private static boolean arrayContains(List<Integer> array, int winningNumber) {
            return array.stream().filter(x -> x == winningNumber).findFirst().isPresent();
        }

    }

    public enum HotSpotType {

        SmallNumber, MediumNumber, LargeNumber, VericalSpacer, HorizontalSpacer, CornerSpacer, MediumHorizontalSpacer, LargeHorizontalSpacer, LargeHorizontalNumber, MediumHorizontalNumber, SmallVerticalSpacer;
    }

    public static final String HOTSPOT_CLASS = "hotspot";

    String text;
    HotSpotColor color;
    HotSpotType type;

    BetType betType;
    List<Integer> numbers;

    public UIHotSpot(HotSpotType type, String text, HotSpotColor color, BetType betType, List<Integer> numbers) {
        this.text = text;
        this.color = color;
        this.betType = betType;
        this.numbers = numbers;
        this.type = type;
    }

    public String createHotSpotDiv() {
        String res = "";
        if (betType != null) {
            res = "<div class='" + HOTSPOT_CLASS + " " + type.name() + " " + color.name() + "' data-betType='" + betType.name() + "' data-numbers='" + numbersArrayToString(numbers) + "'>" + text + "</div>";
        } else {
            res = "<div class='" + HOTSPOT_CLASS + " " + type.name() + " " + color.name() + "'>" + text + "</div>";
        }

        return res;
    }

}
