package Dictionary;

import java.util.ArrayList;

public class Dictionary {
    private ArrayList<Word> words = new ArrayList<Word>();

    public ArrayList<Word> getWords() {
        return words;
    }

    public Dictionary() {
    }

    // Dat kieu Dictionary.Word cho cac pt trong words[]
    public Dictionary(int numberOfWords) {
        for(int i = 0; i < numberOfWords; i++) {
            words.add(new Word());
        }
    }

    public void sortWord(){
        for (int i = 0;i<words.size();i++)
            for (int j = i+1;j<words.size();j++)
                if (words.get(i).getWord_target().compareTo(words.get(j).getWord_target())>0) {
                    Word temp = words.get(i);
                    words.set(i,words.get(j));
                    words.set(j,temp);

                }
    }
}
