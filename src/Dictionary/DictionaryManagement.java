package Dictionary;

import java.io.*;
import java.net.URL;
import java.util.Scanner;

public class DictionaryManagement {
    private static final Scanner scanner = new Scanner(System.in);
    private static Dictionary dictionary = new Dictionary();

    public static Dictionary getDictionary() {
        return dictionary;
    }

    public static void insertFromCommandline() {

        // nhap so luong tu vung
        int numberOfWords = Integer.parseInt(scanner.nextLine());

        // khoi tao san kieu Dictionary.Word cho tat ca pt thuoc dictionary
        dictionary = new Dictionary(numberOfWords);

        // nhap tu vung + nghia cua no
        for(int i = 0; i < numberOfWords; i++) {
            Word element = dictionary.getWords().get(i);

            element.setWord_target(scanner.nextLine());
            element.setWord_explain(scanner.nextLine());
        }
    }

    public static void insertFromFile() {
        // lay duong dan tuong doi cua file dictionaries.txt
        URL dictionaryURL = DictionaryManagement.class.getResource("../dictionaries.txt");

        try {
            // lay dl tu dictionaries.txt ra
            FileInputStream fileInputStream = new FileInputStream(dictionaryURL.getPath());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));

            // doc tung dong trong dictionaries.txt
            String line = bufferedReader.readLine();

            while (line != null) {
                // tach tu vung, nghia
                String[] wordsInLine = line.split("\\t");

                // tao 1 Dictionary.Word de luu tu vung, nghia
                Word element = new Word();
                element.setWord_target(wordsInLine[0]);
                element.setWord_explain(wordsInLine[1]);

                // them Dictionary.Word vao tu dien
                dictionary.getWords().add(element);

                // tiep tuc doc dong tiep theo
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void dictionaryLookup() {
        int numberOfWords = dictionary.getWords().size();
        System.out.printf("Enter the word you want to find: ");

        // tu can tim
        String searchWord = scanner.nextLine();
        // kiem tra tu can tim cho trong tu dien
        boolean hasExist = false;

        for (int i = 0; i < numberOfWords; i++) {
            Word element = dictionary.getWords().get(i);

            if(searchWord.equalsIgnoreCase(element.getWord_target())) {
                System.out.println("The meaning of that word in Vietnamese is "
                        + element.getWord_explain());
                hasExist = true;
            }
        }

        if(!hasExist) {
            System.out.println("Sorry, we couldn't find it in the dictionary !");
        }
    }

    public static void main(String[] args) throws IOException {
//        insertFromCommandline();
//        insertFromFile();
    }
}
