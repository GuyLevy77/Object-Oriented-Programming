package ascii_art;

import java.util.*;

public class Algorithms {

    private static final int FIRST_LETTER_IN_ALPHABET = 97;

    private static final String[] morseArray = new String[]{
            ".-", "-...", "-.-.", "-..", ".", "..-.", "--.", "....",
            "..", ".---", "-.-", ".-..", "--", "-.", "---", ".--.", "--.-",
            ".-.", "...", "-", "..-", "...-", ".--", "-..-", "-.--", "--.."};

    /**
     * The function finds the number that repeats itself.
     *
     * @param numList - list of numbers.
     * @return - The number that repeats itself.
     */
    public static int findDuplicate(int[] numList) {
        int res = numList[0];
        int cmp = numList[res];
        res = comparingFunc(numList, res, cmp, true);
        cmp = 0;
        res = comparingFunc(numList, res, cmp, false);
        return res;
    }

    /**
     * Auxiliary function for findDuplicate.
     * @param numList - the input array.
     * @param res - the result.
     * @param cmp - the number we compare to.
     * @param decider - decides which condition we should implement.
     * @return - the result.
     */
    private static int comparingFunc(int[] numList, int res, int cmp, boolean decider) {
        while (res != cmp) {
            res = numList[res];
            if (decider)
                cmp = numList[numList[cmp]];
            else
                cmp = numList[cmp];
        }
        return res;
    }

    /**
     * Gets an array of Strings/words and checks the amount of the unique words in it.
     *
     * @param words - Array of words.
     * @return - Amount of unique words.
     */
    public static int uniqueMorseRepresentations(String[] words) {
        Set<String> morseWordsSet = new HashSet<>();
        Map<Character, String> morseCharMap = new HashMap<>();

        for (int i = 0; i < morseArray.length; i++)
            morseCharMap.put((char) (i + FIRST_LETTER_IN_ALPHABET), morseArray[i]);

        int count = 0;
        for (String word : words) {
            if (checkIfUnique(morseWordsSet, morseCharMap, word))
                count++;
        }
        return count;
    }

    /**
     * The function checks if the current word it gets appear in the morseWordsSet, if it's not we
     * add it.
     *
     * @param morseWordsSet - set of all the words that we already saw.
     * @param morseCharMap  - Map that is represented by key- char, and value - the char in morse code.
     * @param word          - word from the words array.
     * @return - If the current word it gets doesn't appear in the morseWordsSet we return true, otherwise
     * we return false.
     */
    private static boolean checkIfUnique
    (Set<String> morseWordsSet, Map<Character, String> morseCharMap, String word) {

        String wordInMorseForm = "";
        String lowerCaseWord = word.toLowerCase();

        for (int i = 0; i < word.length(); i++)
            wordInMorseForm = wordInMorseForm.concat(morseCharMap.get(lowerCaseWord.charAt(i)));

        if (!(morseWordsSet.contains(wordInMorseForm))) {
            morseWordsSet.add(wordInMorseForm);
            return true;
        }
        return false;
    }
}