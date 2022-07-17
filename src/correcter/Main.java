package correcter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;

public class Main {
    static Random random = new Random();

    public static char binaryToText(String binary) {
        int decimal = 0;
        for (int i = 0; i < binary.length(); i++) {
            if (binary.charAt(i) == '1') {
                decimal += Math.pow(2, (binary.length() - i - 1));
            }
        }
        return (char) decimal;
    }

    public static String t2B(String text) {
        int num = 0;
        StringBuilder binary = new StringBuilder();
        for (char ch : text.toCharArray()) {
            num = ch;
            binary.append(String.format("%8s", Long.toString(num, 2)).replace(' ', '0'));
        }
        return binary.toString();
    }

    public static String introduceErrors(String binaryStr) {
        StringBuilder singleByte;
        int j = 0;
        int randomPosition = 0;
        String faultyText = "";
        for (int i = 1; i < binaryStr.length() + 1; i++) {
            if (i % 8 == 0) {
                singleByte = new StringBuilder(binaryStr.substring(j, i));
                randomPosition = random.nextInt(singleByte.length());
                if (singleByte.charAt(randomPosition) == '0')
                    singleByte.setCharAt(randomPosition, '1');
                else
                    singleByte.setCharAt(randomPosition, '0');
                j = i;
                faultyText += singleByte;
            }
        }
        return faultyText;
    }

    public static String encode(String binaryStr) {
        int j = 0;
        String encodedText = "";
        StringBuilder triplet;
        for (int i = 1; i <= binaryStr.length(); i++) {
            if (i % 3 == 0) {
                triplet = new StringBuilder(binaryStr.substring(j, i));
                triplet.append(triplet.charAt(0) - '0' ^ triplet.charAt(1) - '0' ^ triplet.charAt(2) - '0');
                j = i;
                encodedText += triplet;
            }
        }
        encodedText += binaryStr.substring(j);
        if (encodedText.length() % 8 != 0) {
            int len = encodedText.length() + (encodedText.length() - (encodedText.length() - (encodedText.length() % 8)));
            encodedText = String.format("%-" + len + "s", encodedText).replace(' ', '0');
        }
        String text = "";
        for (char ch : encodedText.toCharArray())
            text += ch + "" + ch;
        return text;
    }

    public static String decode(String faultyMessage) {
        int j = 0;
        String decodedText = "";
        StringBuilder singleByte = null;
        char singleChar;
        for (int i = 1; i <= faultyMessage.length(); i++) {
            if (i % 8 == 0) {
                singleByte = new StringBuilder(faultyMessage.substring(j, i));
                if (singleByte.toString().toCharArray()[0] != singleByte.toString().toCharArray()[1]) {
                    singleChar = (char) (singleByte.charAt(2) ^ singleByte.charAt(4) ^ singleByte.charAt(6));
                    singleByte.replace(0, 2, singleChar + "" + singleChar);
                } else if (singleByte.toString().toCharArray()[2] != singleByte.toString().toCharArray()[3]) {
                    singleChar = (char) (singleByte.charAt(0) ^ singleByte.charAt(4) ^ singleByte.charAt(6));
                    singleByte.replace(2, 4, singleChar + "" + singleChar);

                } else if (singleByte.toString().toCharArray()[4] != singleByte.toString().toCharArray()[5]) {
                    singleChar = (char) (singleByte.charAt(0) ^ singleByte.charAt(2) ^ singleByte.charAt(6));
                    singleByte.replace(4, 6, singleChar + "" + singleChar);
                } else {
                    singleChar = (char) (singleByte.charAt(0) ^ singleByte.charAt(2) ^ singleByte.charAt(4));
                    singleByte.replace(6, 8, singleChar + "" + singleChar);
                }
                j = i;
                decodedText += singleByte;
            }
        }

        StringBuilder originalMessage = new StringBuilder();
        for (int k = 0; k < decodedText.length(); k++){
            if (k%2==0){
                originalMessage.append(decodedText.charAt(k));
            }
        }
        int k = 0;
        String fullText = "";
        for (int i = 1; i < originalMessage.length()+1; i++){
            StringBuilder sample;

            if(i%4==0){
                sample = new StringBuilder(originalMessage.substring(k,i));
                sample.replace(3,4,"");
                k = i;
                fullText += sample;
            }
        }
        String message = "";
        k = 0;
        char ch;
        for (int i = 1; i < fullText.length()+1; i++){
            StringBuilder sample;
            if(i%8==0){
                sample = new StringBuilder(fullText.substring(k,i));
                ch = binaryToText(String.valueOf(sample));
                message+=ch;
                k = i;
            }
        }
        return message;
    }

    public static void main(String[] args) {
        String inputFile;
        File input;
        File output;
        String text;
        Scanner scanner = new Scanner(System.in);
//        for (String arg : args) {
        String mode = scanner.next();
        if (mode.equals("encode")) {
            inputFile = "./send.txt";
            input = new File(inputFile);
            text = "";
            try (Scanner fileScanner = new Scanner(input)) {
                while (fileScanner.hasNext()) {
                    text = fileScanner.nextLine();
                }
            } catch (FileNotFoundException e) {
                System.out.println("No file found: " + inputFile);
            }

            output = new File("encoded.txt");
            if (!output.exists()) {
                try {
                    output.createNewFile();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            try (PrintWriter printWriter = new PrintWriter(output)) {
                printWriter.print(encode(t2B(text)));
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (mode.equals("send")) {
            inputFile = "./encoded.txt";
            input = new File(inputFile);
            text = "";
            try (Scanner fileScanner = new Scanner(input)) {
                while (fileScanner.hasNext()) {
                    text = fileScanner.nextLine();
                }
            } catch (FileNotFoundException e) {
                System.out.println("No file found: " + inputFile);
            }

            output = new File("received.txt");
            if (!output.exists()) {
                try {
                    output.createNewFile();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            try (PrintWriter printWriter = new PrintWriter(output)) {
                printWriter.print(introduceErrors(text));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (mode.equals("decode")) {
            inputFile = "./received.txt";
            input = new File(inputFile);
            text = "";
            try (Scanner fileScanner = new Scanner(input)) {
                while (fileScanner.hasNext()) {
                    text = fileScanner.nextLine();
                }
            } catch (FileNotFoundException e) {
                System.out.println("No file found: " + inputFile);
            }

            output = new File("decoded.txt");
            if (!output.exists()) {
                try {
                    output.createNewFile();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            try (PrintWriter printWriter = new PrintWriter(output)) {
                printWriter.print(decode(text));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        }
    }
}
