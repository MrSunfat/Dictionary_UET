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

    public static void merge(Dictionary wordList, int l, int m, int r) {
        // tim kich thuoc 2 mang con
        int n1 = m - l + 1;
        int n2 = r - m;
        // tao mang tam
        Dictionary L = new Dictionary(n1);
        Dictionary R = new Dictionary(n2);
        // cop dl -> mang tam
        for (int i = 0; i < n1; i++) {
            L.getWords().set(i, new Word(wordList.getWords().get(l+i).getWord_target(),
                    wordList.getWords().get(l+i).getWord_explain()));
        }
        for (int j = 0; j < n2; j++) {
            R.getWords().set(j, new Word(wordList.getWords().get(m+1+j).getWord_target(),
                    wordList.getWords().get(m+1+j).getWord_explain()));
        }

        int i = 0, j = 0;
        int k = l;
        while (i < n1 && j < n2) {
            if (R.getWords().get(j).getWord_target().compareTo(L.getWords().get(i).getWord_target())
                    >= 0) {
                wordList.getWords().set(k, new Word(L.getWords().get(i).getWord_target(),
                        L.getWords().get(i).getWord_explain()));
                i++;
            } else {
                wordList.getWords().set(k, new Word(R.getWords().get(j).getWord_target(),
                        R.getWords().get(j).getWord_explain()));
                j++;
            }
            k++;
        }
        // vet cac pt con lai
        while (i < n1) {
            wordList.getWords().set(k, new Word(L.getWords().get(i).getWord_target(),
                    L.getWords().get(i).getWord_explain()));
            i++;
            k++;
        }
        while (j < n2) {
            wordList.getWords().set(k, new Word(R.getWords().get(j).getWord_target(),
                    R.getWords().get(j).getWord_explain()));
            j++;
            k++;
        }
    }

    public static void sortWord(Dictionary wordList, int l, int r){
        if (l < r) {
            int mid = l + (r - l) / 2;
            sortWord(wordList, l, mid);
            sortWord(wordList, mid+1, r);
            merge(wordList, l, mid, r);
        }
    }

    public static int binarySearch(Dictionary wordList, int l, int r, String wordSearch) {
        ArrayList<Word> words = wordList.getWords();
        if (r >= l) {
            int mid = l + (r - l) / 2;

            if (words.get(mid).getWord_target().equalsIgnoreCase(wordSearch)) {
                return mid;
            }

            if (words.get(mid).getWord_target().compareToIgnoreCase(wordSearch) > 0) {
                return binarySearch(wordList, l, mid - 1, wordSearch);
            }
            return binarySearch(wordList, mid + 1, r, wordSearch);
        }

        return -1;
    }
}
