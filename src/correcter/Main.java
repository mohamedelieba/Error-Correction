package correcter;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

public class Main {
    static String inputFile = "./send.txt";
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

    public static String textToBinary(String text) {
        String faultyText="";
        Integer numericValue;
        int randomPosition = 0;
        StringBuilder binary;
        for (char ch : text.toCharArray()) {
            numericValue = new Integer(ch);
            binary = new StringBuilder(numericValue.toBinaryString(numericValue));
            randomPosition = random.nextInt(binary.length());
                if (binary.charAt(randomPosition) == '0')
                    binary.setCharAt(randomPosition, '1');
                else
                    binary.setCharAt(randomPosition, '0');
            faultyText+=binaryToText(binary.toString());
        }
        return faultyText;
    }
    public static void introduceErrors(String fileName) throws IOException {
        File file = new File(inputFile);

        String text = new String(Files.readAllBytes(Paths.get(fileName)));
        File output = new  File("received.txt");
        output.createNewFile();
        try(PrintWriter printWriter = new PrintWriter(output)){

            printWriter.print(textToBinary(text));
        }catch(IOException e){
                e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        try {
            introduceErrors(inputFile);
        } catch (IOException e) {
            System.out.println("Cannot read file: " + e.getMessage());
        }
    }
}
