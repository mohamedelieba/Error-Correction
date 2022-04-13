package correcter;

import java.util.Random;
import java.util.Scanner;

public class Main {
    static Random random = new Random();

    public static String introduceErrors(String text) {
        int j = 0;
        String faultyText = "";
        StringBuilder triplet;
        for (int i = 1; i <= text.length(); i++) {
            if (i % 3 == 0) {
                triplet = new StringBuilder(text.substring(j, i));
                triplet.setCharAt(random.nextInt(3), (char) (random.nextInt(122 - 97) + 97));
                j = i;
                faultyText += triplet;
            }
        }
        faultyText += text.substring(j);
        return faultyText;
    }

    public static String correctingText(String str) {
        int k = 0;
        String correctText = "";
        StringBuilder symbol = null;
        for (int i = 1; i <= str.length(); i++) {
            if (i % 3 == 0) {
                symbol = new StringBuilder(str.substring(k, i));
                if (symbol.charAt(0) == symbol.charAt(1))
                    symbol.setCharAt(2, symbol.charAt(0));
                else if (symbol.charAt(0) == symbol.charAt(2))
                    symbol.setCharAt(1, symbol.charAt(0));
                else
                    symbol.setCharAt(0, symbol.charAt(1));
                k = i;
                correctText += symbol.charAt(0);
            }
        }
        return correctText;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String text = scanner.nextLine();
        System.out.println(text);
        String sample = "";
        for (char ch : text.toCharArray()) {
            sample += ch + "" + ch + "" + ch;
        }
        System.out.println(sample);
        String str = introduceErrors(sample);
        System.out.println(str);
        System.out.println(correctingText(str));
    }
}