package Dictionary;

import java.io.*;
import java.net.URL;
import java.util.Scanner;

public class DictionaryManagement {
    public static final Scanner scanner = new Scanner(System.in);
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

    public static void addNewWord() {
        System.out.printf("Please add a word target to the dictionary: ");
        String newWordTarget = scanner.nextLine();
        System.out.printf("Please add a word explain to the dictionary: ");
        String newWordExplain = scanner.nextLine();

        // kiem tra tu vung them vao da ton tai trong tu dien
        boolean hasExist = false;
        for (int i = 0; i < dictionary.getWords().size(); i++) {
            Word element = dictionary.getWords().get(i);
            if(newWordTarget.equalsIgnoreCase(element.getWord_target())) {
                hasExist = true;
            }
        }

        // neu hasExist = false => them tu vung moi
        if(!hasExist) {
            Word newElement = new Word();
            newElement.setWord_target(newWordTarget);
            newElement.setWord_explain(newWordExplain);

            dictionary.getWords().add(newElement);
            DictionaryCommandline.showAllWords();
            System.out.println("Success !!!");
        } else {
            System.out.println("Failure !!!");

        }
    }

    public static void deleteWord() {
        System.out.printf("Enter the word you want to delete: ");
        String deleteWord = scanner.nextLine();

        int index = -1;
        for (int i = 0; i < dictionary.getWords().size(); i++) {
            Word element = dictionary.getWords().get(i);
            if(deleteWord.equalsIgnoreCase(element.getWord_target())
            || deleteWord.equalsIgnoreCase(element.getWord_explain())) {
                dictionary.getWords().remove(i);
                index = i;
            }
        }

        if(index == -1) {
            System.out.println("Failure !!!");
        } else {
            System.out.println("Success !!!");
        }
    }

    public static void replaceWord() {
        System.out.printf("Enter the word target you want to replace: ");
        String replaceWordTarget = scanner.nextLine();
        System.out.printf("Enter the word explain you want to replace: ");
        String replaceWordExplain = scanner.nextLine();

        int index = -1;
        for (int i = 0; i < dictionary.getWords().size(); i++) {
            Word element = dictionary.getWords().get(i);
            if(replaceWordTarget.equalsIgnoreCase(element.getWord_target())) {
                Word replaceWord = new Word();
                replaceWord.setWord_target(replaceWordTarget);
                replaceWord.setWord_explain(replaceWordExplain);

                dictionary.getWords().set(i, replaceWord);
                index = i;
            }
        }
    }

    public static void dictionaryExportToFile() {

    }

    public static void main(String[] args) throws IOException {
//        insertFromCommandline();
//        insertFromFile();
    }
}
