package Dictionary;

public class DictionaryCommandline {
    public static void showAllWords() {
        // lay ra tu vung tu Dictionary.DictionaryManagement
        Dictionary dictionary = DictionaryManagement.getDictionary();

        // lay ra so luong tu vung tu Dictionary.DictionaryManagement
        int numberOfWords = dictionary.getWords().size();

        System.out.printf("No\t\t| English\t\t\t\t\t| Vietnames\n");
        for (int i = 0; i < numberOfWords; i++) {
            Word element = dictionary.getWords().get(i);

            System.out.printf("%-5d\t| %-20s\t\t| %s\n", i+1,
                    element.getWord_target(), element.getWord_explain());
        }
    }

    public static void dictionaryBasic() {
        DictionaryManagement.insertFromCommandline();
        showAllWords();
    }

    public static void dictionaryAdvanced() {
        DictionaryManagement.insertFromFile();
        showAllWords();
        DictionaryManagement.dictionaryLookup();
    }

    public static void dictionarySearcher() {
        System.out.printf("Enter the word you want to find: ");
        String searchWord = DictionaryManagement.scanner.nextLine();

        // lay ra tu vung tu Dictionary.DictionaryManagement
        Dictionary dictionary = DictionaryManagement.getDictionary();

        System.out.printf("No\t\t| English\t\t\t\t\t| Vietnames\n");
        for (int i = 0; i < dictionary.getWords().size(); i++) {
            Word element = dictionary.getWords().get(i);

            // kiem tra searchWord co ton tai trong element.getWord_target()
            // va kiem tra element.getWord_target() co bat dau = searchWord
            if(element.getWord_target().toUpperCase().contains(searchWord.toUpperCase())
            && element.getWord_target().toUpperCase().indexOf(searchWord.toUpperCase()) == 0) {
                System.out.printf("%-5d\t| %-20s\t\t| %s\n", i+1,
                        element.getWord_target(), element.getWord_explain());
            }
        }
    }

    public static void main(String[] args) {
//        dictionaryBasic();
        dictionaryAdvanced();
//        dictionarySearcher();
    }
}
