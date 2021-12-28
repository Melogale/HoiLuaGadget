package sg;

import java.util.ArrayList;
import java.util.LinkedList;

public class ParsingScripts {

    /**
     * label = {
     *     block
     * }
     *
     * variable = value
     */

    public static ArrayList<Integer> findIndexesOf(String content, String word) {
        ArrayList<Integer> indexes = new ArrayList<Integer>();
        int index = content.indexOf(word);
        while (index >= 0) {
            indexes.add(index);
            index = content.indexOf(word, index + 1);
        }
        return indexes;
    }

    public static ArrayList<Integer> parseIntList(String string) {
        String[] tokens = string.split(" ");
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < tokens.length; i++) {
            if(!tokens[i].equals("")) {
                if (!tokens[i].contains(".")) {
                    list.add(Integer.parseInt(tokens[i]));
                } else {
                    list.add((int) Double.parseDouble(tokens[i]));
                }
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
     * Returns everything in content after the first appearance of substring word.
     * If word doesn't appear in content, simply returns content in its entirety.
     */
    public static String afterWord(String content, String word) {
        int index = content.indexOf(word);
        return content.substring(index == -1 ? 0 : index + word.length());
    }

    /**
     * Returns everything in content after the final appearance of substring word.
     * If word doesn't appear in content, simply returns content in its entirety.
     */
    public static String afterLastWord(String content, String word) {
        ArrayList<Integer> indexes = findIndexesOf(content, word);
        int index = indexes.size() != 0 ? indexes.get(indexes.size() - 1) : -1;
        return content.substring(index == -1 ? 0 : index + word.length());
    }

    /**
     * Returns everything in content before the first appearance of substring word.
     * If word doesn't appear in content, simply returns content in its entirety.
     */
    public static String beforeWord(String content, String word) {
        int index = content.indexOf(word);
        return content.substring(0, index == -1 ? 0 : index);
    }

    /**
     * Returns everything in content before the final appearance of substring word.
     * If word doesn't appear in content, simply returns content in its entirety.
     */
    public static String beforeLastWord(String content, String word) {
        ArrayList<Integer> indexes = findIndexesOf(content, word);
        int index = indexes.size() != 0 ? indexes.get(indexes.size() - 1) : -1;
        // substring = [i, j)
        return content.substring(0, index == -1 ? 0 : index);
    }
    /**
     * Returns the value with the given label in content.
     * Returns the value between the first equal sign and the next line end after label.
     * value: string following equals
     */
    public static String getValue(String content, String variable) {
        //System.out.println("CONTENT START:");
        //System.out.println(content);
        //System.out.println(":CONTENT END");
        //System.out.println("VARIABLE: " + variable);
        if(content.contains(variable)) {
            String after = removeSpaces(beforeWord(afterWord(afterWord(content, variable), "="), "\n"));
            return after;
        }
        return "";
    }

    public static int getValueInt(String content, String variable) {
        String value = getValue(content, variable);
        if(value != "") {
            //System.out.println("Value: " + value);
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

    /**
     * Returns the whole of the first line that contains the given string.
     */
    public static String getLineWith(String content, String with) {
        String[] lines = content.split("\n");
        for(int i = 0; i < lines.length; i++) {
            if(lines[i].contains(with)) {
                return lines[i];
            }
        }
        return "";
    }

    public static String findLabelWith(String content, String with) {
        if(content.contains(with)) {
            String beforeWith = beforeWord(content, with);
            String beforeOpening = beforeLastWord(beforeWith, "{");
            String beforeEquals = beforeLastWord(beforeOpening, "=");
            String line = afterLastWord(beforeEquals, "\n");
            return line.trim();
        }
        return "";
    }
}
