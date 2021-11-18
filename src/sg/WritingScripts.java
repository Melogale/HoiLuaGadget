package sg;

import sg.obj.State;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class WritingScripts {

    /**
     * label = {
     *     block
     * }
     *
     * variable = value
     */

    public static void writeStateContent(File file, int id, State state) {
        String concat = "state={\n\tid = " + id + "\n\tname = \"STATE_" + id + "\"\n";
        concat += "\tmanpower = " + state.manpower + "\n";
        concat += "\tstate_category = " + state.category + "\n";
        concat += "\thistory={\n";
        concat += "\t\towner = " + state.owner + "\n";
        for(String core : state.cored) {
            concat +=
        }
    }

    public static void writeInLabel(File file, String label, String line) {
        String content = FileScripts.readFile(file);
        String beforeLabel = ParsingScripts.beforeWord(content,label);
        String afterLabel = ParsingScripts.afterWord(content,label);
        String beforeOpening = ParsingScripts.beforeWord(afterLabel,"{");
        String afterOpening = ParsingScripts.afterWord(afterLabel,"{");
        String add = "\n" + line;
        String concat = beforeLabel + label + beforeOpening + "{" + add + afterOpening;
        FileScripts.setFileContents(file, concat);
    }

    public static void setValue(File file, String variable, int value, int tabCount, String heading) {
        setValueString(file, variable, Integer.toString(value), tabCount, heading);
    }

    public static void setValueString(File file, String variable, String value, int tabCount, String heading) {
        String content = FileScripts.readFile(file);
        String tabs = UtilScripts.tabs(tabCount);
        if(content.contains(variable)) {
            String line = ParsingScripts.getLineWith(content, variable);
            String before = ParsingScripts.beforeWord(content, line);
            String after = ParsingScripts.afterWord(content, line);
            String replaced = before + tabs + variable + " = " + value + after;
            FileScripts.setFileContents(file, replaced);
        } else {
            writeInLabel(file, heading, tabs + variable + " = " + value);
        }
    }

    public static void setCategory(File state, String value) {
        setValueString(state, "state_category", value, 1, "state");
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
