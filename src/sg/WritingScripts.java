package sg;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class WritingScripts {

    public static void writeLineUnder(File file, String label, String line) {
        String content = FileScripts.readFile(file);
        String beforeHistory = ParsingScripts.beforeWord(content,label);
        String afterHistory = ParsingScripts.afterWord(content,label);
        String beforeOpening = ParsingScripts.beforeWord(afterHistory,"{");
        String afterOpening = ParsingScripts.afterWord(afterHistory,"{");
        String add = "\n" + line;
        String concat = beforeHistory + label + beforeOpening + "{" + add + afterOpening;
        try {
            PrintWriter writer = new PrintWriter(file);
            writer.print(concat);
            writer.close();
        } catch (FileNotFoundException e) {
            System.out.println(file.getName() + " not found!");
        }
    }

    public static void setValue(File file, String label, int value, int tabCount, String heading) {
        setStringValue(file, label, Integer.toString(value), tabCount, heading);
    }

    public static void setStringValue(File file, String label, String value, int tabCount, String heading) {
        String content = FileScripts.readFile(file);
        String tabs = UtilScripts.tabs(tabCount);
        if(content.contains(label)) {
            String line = ParsingScripts.getLineWith(content, label);
            String before = ParsingScripts.beforeWord(content, line);
            String after = ParsingScripts.afterWord(content, line);
            String replaced = before + tabs + label + " = " + value + after;
            FileScripts.setFileContents(file, replaced);
        } else {
            writeLineUnder(file, heading, tabs + label + " = " + value);
        }
    }

    public static void setCategory(File state, String value) {
        setStringValue(state, "state_category", value, 1, "state");
    }
    public static void setPop(File state, int value) {
        setValue(state, "manpower", value, 1, "state");
    }
    public static void setInf(File state, int value) {
        setValue(state, "infrastructure", value, 3, "buildings");
    }
    public static void setCivs(File state, int value) {
        setValue(state, "industrial_complex", value, 3, "buildings");
    }
    public static void setMils(File state, int value) {
        setValue(state, "arms_factory", value, 3, "buildings");
    }
    public static void setAirfields(File state, int value) {
        setValue(state, "air_base", value, 3, "buildings");
    }
    public static void setDockyards(File state, int value) {
        setValue(state, "dockyard", value, 3, "buildings");
    }
}
