import java.util.ArrayList;

public class Dictionary {
    private ArrayList<Word> words = new ArrayList<Word>();

    public ArrayList<Word> getWords() {
        return words;
    }

    public Dictionary() {
    }

    // Dat kieu Word cho cac pt trong words[]
    public Dictionary(int numberOfWords) {
        for(int i = 0; i < numberOfWords; i++) {
            words.add(new Word());
        }
    }
}
