package sb;

import java.util.ArrayList;
import java.util.LinkedList;

public class Parsing {

    public static ArrayList<Integer> parseIntList(String string) {
        String[] tokens = string.split(" ");
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < tokens.length; i++) {
            if(!tokens[i].contains(".")) {
                list.add(Integer.parseInt(tokens[i]));
            } else {
                list.add((int) Double.parseDouble(tokens[i]));
            }
        }
        return list;
    }

    public static String removeSpaces(String string) {
        return string.replaceAll(" ", "").replaceAll("\t", "");
    }

    public static String cleanList(String string) {
        return string.replaceAll("\t", " ").trim().replaceAll("\\r\\n|\\r|\\n", " ").replaceAll(" +", " ");

    }

    /**
     * Returns the block with the given label in content.
     *
     */
    public static String getBlock(String content, String label) {
        return beforeWord(afterWord(afterWord(content, label), "{"), "}");
    }

    /**
     * Returns everything in content after the substring word.
     * If word doesn't appear in content, simply returns content in its entirety.
     */
    public static String afterWord(String content, String word) {
        int index = content.indexOf(word);
        return content.substring(index == -1 ? 0 : index + word.length());
    }

    /**
     * Returns everything in content before the substring word.
     * If word doesn't appear in content, simply returns content in its entirety.
     */
    public static String beforeWord(String content, String word) {
        int index = content.indexOf(word);
        return content.substring(0, index == -1 ? 0 : index);
    }
}
