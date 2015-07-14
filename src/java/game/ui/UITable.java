/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.ui;

import java.util.Arrays;
import ws.roulette.BetType;
import ws.roulette.RouletteType;

/**
 *
 * @author Shay
 */
public class UITable {

    public static final String COLUMN_CLASS = "column";

    RouletteType type;

    public UITable(RouletteType type) {
        this.type = type;
    }

    public String createHTML() {
        String res = "";

        res += "<div class='game-table'>";

        //zeros
        res += "<div class='" + COLUMN_CLASS + "'>";
        {
            res += createZerosGroup();
        }
        res += "</div>";
        
        // numbers and extras
        res += "<div class='" + COLUMN_CLASS + "'>";
        {
            res += "<div>";
            {
                res += createNumbersGroup();
            }
            res += "</div>";
            res += "<div>";
            {
                res += createExtraButtonsGroup();
            }
            res += "</div>";
        }
        res += "</div>";
        
        res += "</div>";

        return res;
    }

    private String createZerosGroup() {
        if (type == RouletteType.FRENCH) {
            return createFrenchZerosGroup();
        } else {
            return createAmericanZerosGroup();
        }
    }

    private String createFrenchZerosGroup() {
        String res = "";
        UIHotSpot hotspot;

        res += "<div class='" + COLUMN_CLASS + "'>";

        {

            hotspot = new UIHotSpot(UIHotSpot.HotSpotType.HorizontalSpacer, "", UIHotSpot.HotSpotColor.Transparent, null, null);
            res += hotspot.createHotSpotDiv();

            hotspot = new UIHotSpot(UIHotSpot.HotSpotType.LargeNumber, "0", UIHotSpot.HotSpotColor.Green, BetType.STRAIGHT, Arrays.asList(0));
            res += hotspot.createHotSpotDiv();

            hotspot = new UIHotSpot(UIHotSpot.HotSpotType.HorizontalSpacer, "", UIHotSpot.HotSpotColor.Transparent, null, null);
            res += hotspot.createHotSpotDiv();
        }

        res += "</div>";
        res += "<div class='" + COLUMN_CLASS + "'>";

        {
            hotspot = new UIHotSpot(UIHotSpot.HotSpotType.CornerSpacer, "", UIHotSpot.HotSpotColor.Transparent, null, null);
            res += hotspot.createHotSpotDiv();

            hotspot = new UIHotSpot(UIHotSpot.HotSpotType.VericalSpacer, "", UIHotSpot.HotSpotColor.Transparent, BetType.SPLIT, Arrays.asList(0, 3));
            res += hotspot.createHotSpotDiv();

            hotspot = new UIHotSpot(UIHotSpot.HotSpotType.CornerSpacer, "", UIHotSpot.HotSpotColor.Transparent, BetType.TRIO, Arrays.asList(0, 2, 3));
            res += hotspot.createHotSpotDiv();

            hotspot = new UIHotSpot(UIHotSpot.HotSpotType.VericalSpacer, "", UIHotSpot.HotSpotColor.Transparent, BetType.SPLIT, Arrays.asList(0, 2));
            res += hotspot.createHotSpotDiv();

            hotspot = new UIHotSpot(UIHotSpot.HotSpotType.CornerSpacer, "", UIHotSpot.HotSpotColor.Transparent, BetType.TRIO, Arrays.asList(0, 1, 2));
            res += hotspot.createHotSpotDiv();

            hotspot = new UIHotSpot(UIHotSpot.HotSpotType.VericalSpacer, "", UIHotSpot.HotSpotColor.Transparent, BetType.SPLIT, Arrays.asList(0, 1));
            res += hotspot.createHotSpotDiv();

            hotspot = new UIHotSpot(UIHotSpot.HotSpotType.CornerSpacer, "", UIHotSpot.HotSpotColor.Transparent, null, null);
            res += hotspot.createHotSpotDiv();
        }

        res += "</div>";

        return res;
    }

    private String createAmericanZerosGroup() {
        String res = "";
        UIHotSpot hotspot;

        res += "<div class='" + COLUMN_CLASS + "'>";

        {
            hotspot = new UIHotSpot(UIHotSpot.HotSpotType.HorizontalSpacer, "", UIHotSpot.HotSpotColor.Transparent, null, null);
            res += hotspot.createHotSpotDiv();

            hotspot = new UIHotSpot(UIHotSpot.HotSpotType.MediumNumber, "00", UIHotSpot.HotSpotColor.Green, BetType.STRAIGHT, Arrays.asList(37));
            res += hotspot.createHotSpotDiv();

            hotspot = new UIHotSpot(UIHotSpot.HotSpotType.HorizontalSpacer, "", UIHotSpot.HotSpotColor.Transparent, BetType.SPLIT, Arrays.asList(0, 37));
            res += hotspot.createHotSpotDiv();

            hotspot = new UIHotSpot(UIHotSpot.HotSpotType.MediumNumber, "0", UIHotSpot.HotSpotColor.Green, BetType.STRAIGHT, Arrays.asList(0));
            res += hotspot.createHotSpotDiv();

            hotspot = new UIHotSpot(UIHotSpot.HotSpotType.HorizontalSpacer, "", UIHotSpot.HotSpotColor.Transparent, null, null);
            res += hotspot.createHotSpotDiv();
        }

        res += "</div>";
        res += "<div class='" + COLUMN_CLASS + "'>";

        {
            hotspot = new UIHotSpot(UIHotSpot.HotSpotType.CornerSpacer, "", UIHotSpot.HotSpotColor.Transparent, null, null);
            res += hotspot.createHotSpotDiv();

            hotspot = new UIHotSpot(UIHotSpot.HotSpotType.VericalSpacer, "", UIHotSpot.HotSpotColor.Transparent, BetType.SPLIT, Arrays.asList(3, 37));
            res += hotspot.createHotSpotDiv();

            hotspot = new UIHotSpot(UIHotSpot.HotSpotType.CornerSpacer, "", UIHotSpot.HotSpotColor.Transparent, BetType.BASKET, Arrays.asList(2, 3, 37));
            res += hotspot.createHotSpotDiv();

            hotspot = new UIHotSpot(UIHotSpot.HotSpotType.SmallVerticalSpacer, "", UIHotSpot.HotSpotColor.Transparent, BetType.SPLIT, Arrays.asList(2, 37));
            res += hotspot.createHotSpotDiv();

            hotspot = new UIHotSpot(UIHotSpot.HotSpotType.CornerSpacer, "", UIHotSpot.HotSpotColor.Transparent, BetType.BASKET, Arrays.asList(0, 2, 37));
            res += hotspot.createHotSpotDiv();

            hotspot = new UIHotSpot(UIHotSpot.HotSpotType.SmallVerticalSpacer, "", UIHotSpot.HotSpotColor.Transparent, BetType.SPLIT, Arrays.asList(0, 2));
            res += hotspot.createHotSpotDiv();

            hotspot = new UIHotSpot(UIHotSpot.HotSpotType.CornerSpacer, "", UIHotSpot.HotSpotColor.Transparent, BetType.BASKET, Arrays.asList(0, 1, 2));
            res += hotspot.createHotSpotDiv();

            hotspot = new UIHotSpot(UIHotSpot.HotSpotType.VericalSpacer, "", UIHotSpot.HotSpotColor.Transparent, BetType.SPLIT, Arrays.asList(0, 1));
            res += hotspot.createHotSpotDiv();

            hotspot = new UIHotSpot(UIHotSpot.HotSpotType.CornerSpacer, "", UIHotSpot.HotSpotColor.Transparent, null, null);
            res += hotspot.createHotSpotDiv();
        }

        res += "</div>";

        return res;
    }

    private String createNumbersGroup() {
        String res = "";
        UIHotSpot hotspot;

        int num = 0;

        // all columns but the last one
        for (int i = 0; i < 11; i++) {
            int top = num + 3, middle = num + 2, bottom = num + 1;
            int top_right = top + 3, middle_right = middle + 3, bottom_right = bottom + 3;

            res += "<div class='" + COLUMN_CLASS + "'>";
            {
                hotspot = new UIHotSpot(UIHotSpot.HotSpotType.HorizontalSpacer, "", UIHotSpot.HotSpotColor.Transparent, BetType.STREET, Arrays.asList(bottom, middle, top));
                res += hotspot.createHotSpotDiv();

                hotspot = new UIHotSpot(UIHotSpot.HotSpotType.SmallNumber, String.valueOf(top), UIHotSpot.HotSpotColor.get(top), BetType.STRAIGHT, Arrays.asList(top));
                res += hotspot.createHotSpotDiv();

                hotspot = new UIHotSpot(UIHotSpot.HotSpotType.HorizontalSpacer, "", UIHotSpot.HotSpotColor.Transparent, BetType.SPLIT, Arrays.asList(top, middle));
                res += hotspot.createHotSpotDiv();

                hotspot = new UIHotSpot(UIHotSpot.HotSpotType.SmallNumber, String.valueOf(middle), UIHotSpot.HotSpotColor.get(middle), BetType.STRAIGHT, Arrays.asList(middle));
                res += hotspot.createHotSpotDiv();

                hotspot = new UIHotSpot(UIHotSpot.HotSpotType.HorizontalSpacer, "", UIHotSpot.HotSpotColor.Transparent, BetType.SPLIT, Arrays.asList(middle, bottom));
                res += hotspot.createHotSpotDiv();

                hotspot = new UIHotSpot(UIHotSpot.HotSpotType.SmallNumber, String.valueOf(bottom), UIHotSpot.HotSpotColor.get(bottom), BetType.STRAIGHT, Arrays.asList(bottom));
                res += hotspot.createHotSpotDiv();

                hotspot = new UIHotSpot(UIHotSpot.HotSpotType.HorizontalSpacer, "", UIHotSpot.HotSpotColor.Transparent, BetType.STREET, Arrays.asList(bottom, middle, top));
                res += hotspot.createHotSpotDiv();
            }
            res += "</div>";
            res += "<div class='" + COLUMN_CLASS + "'>";
            {
                hotspot = new UIHotSpot(UIHotSpot.HotSpotType.CornerSpacer, "", UIHotSpot.HotSpotColor.Transparent, BetType.SIX_LINE, Arrays.asList(bottom, middle, top, bottom_right, middle_right, top_right));
                res += hotspot.createHotSpotDiv();

                hotspot = new UIHotSpot(UIHotSpot.HotSpotType.VericalSpacer, "", UIHotSpot.HotSpotColor.Transparent, BetType.SPLIT, Arrays.asList(top, top_right));
                res += hotspot.createHotSpotDiv();

                hotspot = new UIHotSpot(UIHotSpot.HotSpotType.CornerSpacer, "", UIHotSpot.HotSpotColor.Transparent, BetType.CORNER, Arrays.asList(middle, top, middle_right, top_right));
                res += hotspot.createHotSpotDiv();

                hotspot = new UIHotSpot(UIHotSpot.HotSpotType.VericalSpacer, "", UIHotSpot.HotSpotColor.Transparent, BetType.SPLIT, Arrays.asList(middle, middle_right));
                res += hotspot.createHotSpotDiv();

                hotspot = new UIHotSpot(UIHotSpot.HotSpotType.CornerSpacer, "", UIHotSpot.HotSpotColor.Transparent, BetType.CORNER, Arrays.asList(bottom, middle, bottom_right, middle_right));
                res += hotspot.createHotSpotDiv();

                hotspot = new UIHotSpot(UIHotSpot.HotSpotType.VericalSpacer, "", UIHotSpot.HotSpotColor.Transparent, BetType.SPLIT, Arrays.asList(bottom, bottom_right));
                res += hotspot.createHotSpotDiv();

                hotspot = new UIHotSpot(UIHotSpot.HotSpotType.CornerSpacer, "", UIHotSpot.HotSpotColor.Transparent, BetType.SIX_LINE, Arrays.asList(bottom, middle, top, bottom_right, middle_right, top_right));
                res += hotspot.createHotSpotDiv();
            }
            res += "</div>";

            num += 3;
        }

        // last numbers column
        int top = num + 3, middle = num + 2, bottom = num + 1;
        res += "<div class='" + COLUMN_CLASS + "'>";
        {
            hotspot = new UIHotSpot(UIHotSpot.HotSpotType.HorizontalSpacer, "", UIHotSpot.HotSpotColor.Transparent, BetType.STREET, Arrays.asList(bottom, middle, top));
            res += hotspot.createHotSpotDiv();

            hotspot = new UIHotSpot(UIHotSpot.HotSpotType.SmallNumber, String.valueOf(top), UIHotSpot.HotSpotColor.get(top), BetType.STRAIGHT, Arrays.asList(top));
            res += hotspot.createHotSpotDiv();

            hotspot = new UIHotSpot(UIHotSpot.HotSpotType.HorizontalSpacer, "", UIHotSpot.HotSpotColor.Transparent, BetType.SPLIT, Arrays.asList(top, middle));
            res += hotspot.createHotSpotDiv();

            hotspot = new UIHotSpot(UIHotSpot.HotSpotType.SmallNumber, String.valueOf(middle), UIHotSpot.HotSpotColor.get(middle), BetType.STRAIGHT, Arrays.asList(middle));
            res += hotspot.createHotSpotDiv();

            hotspot = new UIHotSpot(UIHotSpot.HotSpotType.HorizontalSpacer, "", UIHotSpot.HotSpotColor.Transparent, BetType.SPLIT, Arrays.asList(middle, bottom));
            res += hotspot.createHotSpotDiv();

            hotspot = new UIHotSpot(UIHotSpot.HotSpotType.SmallNumber, String.valueOf(bottom), UIHotSpot.HotSpotColor.get(bottom), BetType.STRAIGHT, Arrays.asList(bottom));
            res += hotspot.createHotSpotDiv();

            hotspot = new UIHotSpot(UIHotSpot.HotSpotType.HorizontalSpacer, "", UIHotSpot.HotSpotColor.Transparent, BetType.STREET, Arrays.asList(bottom, middle, top));
            res += hotspot.createHotSpotDiv();
        }
        res += "</div>";
        res += "<div class='" + COLUMN_CLASS + "'>";
        {
            hotspot = new UIHotSpot(UIHotSpot.HotSpotType.CornerSpacer, "", UIHotSpot.HotSpotColor.Transparent, null, null);
            res += hotspot.createHotSpotDiv();

            hotspot = new UIHotSpot(UIHotSpot.HotSpotType.VericalSpacer, "", UIHotSpot.HotSpotColor.Transparent, null, null);
            res += hotspot.createHotSpotDiv();

            hotspot = new UIHotSpot(UIHotSpot.HotSpotType.CornerSpacer, "", UIHotSpot.HotSpotColor.Transparent, null, null);
            res += hotspot.createHotSpotDiv();

            hotspot = new UIHotSpot(UIHotSpot.HotSpotType.VericalSpacer, "", UIHotSpot.HotSpotColor.Transparent, null, null);
            res += hotspot.createHotSpotDiv();

            hotspot = new UIHotSpot(UIHotSpot.HotSpotType.CornerSpacer, "", UIHotSpot.HotSpotColor.Transparent, null, null);
            res += hotspot.createHotSpotDiv();

            hotspot = new UIHotSpot(UIHotSpot.HotSpotType.VericalSpacer, "", UIHotSpot.HotSpotColor.Transparent, null, null);
            res += hotspot.createHotSpotDiv();

            hotspot = new UIHotSpot(UIHotSpot.HotSpotType.CornerSpacer, "", UIHotSpot.HotSpotColor.Transparent, null, null);
            res += hotspot.createHotSpotDiv();
        }
        res += "</div>";

        // column bets
        res += "<div class='" + COLUMN_CLASS + "'>";
        {
            hotspot = new UIHotSpot(UIHotSpot.HotSpotType.HorizontalSpacer, "", UIHotSpot.HotSpotColor.Transparent, null, null);
            res += hotspot.createHotSpotDiv();

            hotspot = new UIHotSpot(UIHotSpot.HotSpotType.SmallNumber, "", UIHotSpot.HotSpotColor.Green, BetType.COLUMN_3, null);
            res += hotspot.createHotSpotDiv();

            hotspot = new UIHotSpot(UIHotSpot.HotSpotType.HorizontalSpacer, "", UIHotSpot.HotSpotColor.Transparent, null, null);
            res += hotspot.createHotSpotDiv();

            hotspot = new UIHotSpot(UIHotSpot.HotSpotType.SmallNumber, "", UIHotSpot.HotSpotColor.Green, BetType.COLUMN_2, null);
            res += hotspot.createHotSpotDiv();

            hotspot = new UIHotSpot(UIHotSpot.HotSpotType.HorizontalSpacer, "", UIHotSpot.HotSpotColor.Transparent, null, null);
            res += hotspot.createHotSpotDiv();

            hotspot = new UIHotSpot(UIHotSpot.HotSpotType.SmallNumber, "", UIHotSpot.HotSpotColor.Green, BetType.COLUMN_1, null);
            res += hotspot.createHotSpotDiv();

            hotspot = new UIHotSpot(UIHotSpot.HotSpotType.HorizontalSpacer, "", UIHotSpot.HotSpotColor.Transparent, null, null);
            res += hotspot.createHotSpotDiv();
        }
        res += "</div>";

        return res;
    }

    private String createExtraButtonsGroup() {
        String res = "";
        UIHotSpot hotspot;

        res += "<div class='" + COLUMN_CLASS + "'>";
        {
            res += "<div class='ExtraButtons'>";
            {
                hotspot = new UIHotSpot(UIHotSpot.HotSpotType.LargeHorizontalNumber, "1st 12", UIHotSpot.HotSpotColor.Green, BetType.PREMIERE_DOUZAINE, null);
                res += hotspot.createHotSpotDiv();

                hotspot = new UIHotSpot(UIHotSpot.HotSpotType.VericalSpacer, "", UIHotSpot.HotSpotColor.Transparent, null, null);
                res += hotspot.createHotSpotDiv();

                hotspot = new UIHotSpot(UIHotSpot.HotSpotType.LargeHorizontalNumber, "2nd 12", UIHotSpot.HotSpotColor.Green, BetType.MOYENNE_DOUZAINE, null);
                res += hotspot.createHotSpotDiv();

                hotspot = new UIHotSpot(UIHotSpot.HotSpotType.VericalSpacer, "", UIHotSpot.HotSpotColor.Transparent, null, null);
                res += hotspot.createHotSpotDiv();

                hotspot = new UIHotSpot(UIHotSpot.HotSpotType.LargeHorizontalNumber, "3rd 12", UIHotSpot.HotSpotColor.Green, BetType.DERNIERE_DOUZAINE, null);
                res += hotspot.createHotSpotDiv();
            }
            res += "</div>";

            res += "<div class='ExtraButtons'>";
            {
                hotspot = new UIHotSpot(UIHotSpot.HotSpotType.MediumHorizontalNumber, "1 to 18", UIHotSpot.HotSpotColor.Green, BetType.MANQUE, null);
                res += hotspot.createHotSpotDiv();

                hotspot = new UIHotSpot(UIHotSpot.HotSpotType.VericalSpacer, "", UIHotSpot.HotSpotColor.Transparent, null, null);
                res += hotspot.createHotSpotDiv();

                hotspot = new UIHotSpot(UIHotSpot.HotSpotType.MediumHorizontalNumber, "Even", UIHotSpot.HotSpotColor.Green, BetType.PAIR, null);
                res += hotspot.createHotSpotDiv();

                hotspot = new UIHotSpot(UIHotSpot.HotSpotType.VericalSpacer, "", UIHotSpot.HotSpotColor.Transparent, null, null);
                res += hotspot.createHotSpotDiv();

                hotspot = new UIHotSpot(UIHotSpot.HotSpotType.MediumHorizontalNumber, "Red", UIHotSpot.HotSpotColor.Red, BetType.ROUGE, null);
                res += hotspot.createHotSpotDiv();

                hotspot = new UIHotSpot(UIHotSpot.HotSpotType.VericalSpacer, "", UIHotSpot.HotSpotColor.Transparent, null, null);
                res += hotspot.createHotSpotDiv();

                hotspot = new UIHotSpot(UIHotSpot.HotSpotType.MediumHorizontalNumber, "Black", UIHotSpot.HotSpotColor.Black, BetType.NOIR, null);
                res += hotspot.createHotSpotDiv();

                hotspot = new UIHotSpot(UIHotSpot.HotSpotType.VericalSpacer, "", UIHotSpot.HotSpotColor.Transparent, null, null);
                res += hotspot.createHotSpotDiv();

                hotspot = new UIHotSpot(UIHotSpot.HotSpotType.MediumHorizontalNumber, "Odd", UIHotSpot.HotSpotColor.Green, BetType.IMPAIR, null);
                res += hotspot.createHotSpotDiv();

                hotspot = new UIHotSpot(UIHotSpot.HotSpotType.VericalSpacer, "", UIHotSpot.HotSpotColor.Transparent, null, null);
                res += hotspot.createHotSpotDiv();

                hotspot = new UIHotSpot(UIHotSpot.HotSpotType.MediumHorizontalNumber, "19 to 36", UIHotSpot.HotSpotColor.Green, BetType.PASSE, null);
                res += hotspot.createHotSpotDiv();

                hotspot = new UIHotSpot(UIHotSpot.HotSpotType.VericalSpacer, "", UIHotSpot.HotSpotColor.Transparent, null, null);
                res += hotspot.createHotSpotDiv();
            }
            res += "</div>";
        }
        res += "</div>";

        return res;
    }

}
