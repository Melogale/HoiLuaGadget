package sg;

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

    public String trim(String content) {
        return content.trim();
    }

    public static String removeSpaces(String string) {
        return string.replaceAll(" ", "").replaceAll("\t", "");
    }

    public static String cleanList(String string) {
        return string.replaceAll("\t", " ").trim().replaceAll("\\r\\n|\\r|\\n", " ").replaceAll(" +", " ");
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

    /**
     * Returns the value with the given label in content.
     * Returns the value between the first equal sign and the next line end after label.
     * value: string following equals
     */
    public static String getValue(String content, String left) {
        if(content.contains(left)) {
            String after = removeSpaces(beforeWord(afterWord(afterWord(content, "left"), "="), "\n"));
            return after;
        }
        return "";
    }

    public static int getValueInt(String content, String left) {
        String value = getValue(content, left);
        if(value != "") {
            return Integer.parseInt(value);
        }
        return 0;
    }

    /**
     * Returns the block with the given label in content.
     * Returns the first bracket contained code section following label.
     * block: bracket contained string
     */
    public static String getBlock(String content, String label) {
        return beforeWord(afterWord(afterWord(content, label), "{"), "}");
    }
}
