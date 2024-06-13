/*
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 6/13/2024 6:39 AM
@Last Modified 6/13/2024 6:39 AM
Version 1.0
*/

import java.util.HashMap;
import java.util.Map;

public class NumberToWords {

    private static final Map<Integer, String> unitsMap = new HashMap<>();
    private static final Map<Integer, String> tensMap = new HashMap<>();

    static {
        unitsMap.put(1, "satu");
        unitsMap.put(2, "dua");
        unitsMap.put(3, "tiga");
        unitsMap.put(4, "empat");
        unitsMap.put(5, "lima");
        unitsMap.put(6, "enam");
        unitsMap.put(7, "tujuh");
        unitsMap.put(8, "delapan");
        unitsMap.put(9, "sembilan");
        unitsMap.put(10, "sepuluh");
        unitsMap.put(11, "sebelas");
        unitsMap.put(12, "dua belas");
        unitsMap.put(13, "tiga belas");
        unitsMap.put(14, "empat belas");
        unitsMap.put(15, "lima belas");
        unitsMap.put(16, "enam belas");
        unitsMap.put(17, "tujuh belas");
        unitsMap.put(18, "delapan belas");
        unitsMap.put(19, "sembilan belas");

        tensMap.put(2, "dua puluh");
        tensMap.put(3, "tiga puluh");
        tensMap.put(4, "empat puluh");
        tensMap.put(5, "lima puluh");
        tensMap.put(6, "enam puluh");
        tensMap.put(7, "tujuh puluh");
        tensMap.put(8, "delapan puluh");
        tensMap.put(9, "sembilan puluh");
    }

    public static String convert(int number) {
        if (number == 0) {
            return "nol";
        }

        return convertNumberToWords(number).trim();
    }

    private static String convertNumberToWords(int number) {
        if (number < 20) {
            return unitsMap.get(number);
        }

        if (number < 100) {
            return tensMap.get(number / 10) + " " + (number % 10 != 0 ? unitsMap.get(number % 10) : "");
        }

        if (number < 1000) {
            return (number / 100 == 1 ? "seratus" : unitsMap.get(number / 100) + " ratus") + " " + (number % 100 != 0 ? convertNumberToWords(number % 100) : "");
        }

        if (number < 1000000) {
            return (number / 1000 == 1 ? "seribu" : convertNumberToWords(number / 1000) + " ribu") + " " + (number % 1000 != 0 ? convertNumberToWords(number % 1000) : "");
        }

        if (number < 1000000000) {
            return convertNumberToWords(number / 1000000) + " juta" + " " + (number % 1000000 != 0 ? convertNumberToWords(number % 1000000) : "");
        }

        return "";
    }

    public static void main(String[] args) {
        int[] testNumbers = {0, 1, 15, 21, 105, 123, 1000, 123000, 1000000, 123456789};

        for (int num : testNumbers) {
            System.out.println(num + " -> " + convert(num));
        }
    }
}
