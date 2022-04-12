package correcter;

import java.util.Random;
import java.util.Scanner;

public class Main {
    static Random random = new Random();

    public static String introduceErrors(String text) {
        int j = 0;
        String triplet = "";
        String faultyText = "";

        for (int i = 1; i <= text.length(); i++) {
            if (i % 3 == 0) {
                triplet = text.substring(j, i);
                triplet = triplet.replace(triplet.charAt(random.nextInt(3)), (char) (random.nextInt(122 - 97) + 97));
                j = i;
                faultyText += triplet;
            }
        }
        faultyText += text.substring(j);
        return faultyText;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String text = scanner.nextLine();
        System.out.println(introduceErrors(text));
    }
}
