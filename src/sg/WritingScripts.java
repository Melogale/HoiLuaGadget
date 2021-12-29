package sg;

import sg.obj.ProvinceBuildings;
import sg.obj.State;
import sg.obj.VP;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

public class WritingScripts {

    /**
     * label = {
     *     block
     * }
     *
     * variable = value
     */

    /**
     * Set the contents of a file.
     */
    public static void setFileContents(File file, String contents) {
        try {
            PrintWriter writer = new PrintWriter(file);
            writer.print(contents);
            writer.close();
        } catch (FileNotFoundException f) {
            System.out.println(file.getName() + " not found!");
        }
    }

    public static void writeStateContent(File file, int id, State state) {
        String concat = "state = {\n\tid = " + id + "\n";
        concat += "\tname = \"STATE_" + id + "\"\n";
        concat += "\tmanpower = " + state.manpower + "\n";
        concat += "\tstate_category = " + state.category + "\n";
        if(state.impassable) {
            concat += "\timpassable = yes\n";
        }

        if(state.steel > 0 || state.aluminum > 0 || state.rubber > 0 || state.tungsten > 0 || state.chromium > 0 || state.oil > 0) {
            concat += "\tresources = {\n";
            if(state.oil > 0) {
                concat += "oil = " + state.oil + "\n";
            }
            if(state.aluminum > 0) {
                concat += "aluminium = " + state.aluminum + "\n";
            }
            if(state.rubber > 0) {
                concat += "rubber = " + state.rubber + "\n";
            }
            if(state.tungsten > 0) {
                concat += "tungsten = " + state.tungsten + "\n";
            }
            if(state.steel > 0) {
                concat += "steel = " + state.steel + "\n";
            }
            if(state.chromium > 0) {
                concat += "chromium = " + state.chromium + "\n";
            }
        }

        concat += "\thistory = {\n";
        concat += "\t\towner = " + state.owner + "\n";

        ArrayList<String> sortedCored = state.cored;
        if(sortedCored != null) {
            if (sortedCored.contains(state.owner)) {
                sortedCored.remove(state.owner);
                Collections.sort(sortedCored);
                sortedCored.add(0, state.owner);
            } else {
                Collections.sort(sortedCored);
            }
            for (String core : sortedCored) {
                concat += "\t\tadd_core_of = " + core + "\n";
            }
        }
        for(VP vp : state.vps) {
            concat += "\t\t" +  "victory_points = { " + vp.province + " " + vp.value + " }\n";
        }
        concat += "\t\tbuildings = {\n";
        if(state.inf > 0) {
            concat += "\t\t\tinfrastructure = " + state.inf + "\n";
        }
        if(state.civs > 0) {
            concat += "\t\t\tindustrial_complex = " + state.civs + "\n";
        }
        if(state.mils > 0) {
            concat += "\t\t\tarms_factory = " + state.mils + "\n";
        }
        if(state.dockyards > 0) {
            concat += "\t\t\tdockyard = " + state.dockyards + "\n";
        }
        if(state.airfields > 0) {
            concat += "\t\t\tair_base = " + state.airfields + "\n";
        }
        if(state.refineries > 0) {
            concat += "\t\t\tsynthetic_refinery = " + state.refineries + "\n";
        }
        if(state.silos > 0) {
            concat += "\t\t\tfuel_silo = " + state.silos + "\n";
        }
        if(state.antiairs > 0) {
            concat += "\t\t\tanti_air_building = " + state.antiairs + "\n";
        }
        if(state.reactors > 0) {
            concat += "\t\t\tnuclear_reactor = " + state.reactors + "\n";
        }
        if(state.radars > 0) {
            concat += "\t\t\tradar_station = " + state.radars + "\n";
        }
        if(state.rocketsites > 0) {
            concat += "\t\t\trocket_site = " + state.rocketsites + "\n";
        }
        for(ProvinceBuildings pb : state.pbuilds) {
            concat += "\t\t\t" + pb.province + " = {\n";
            if(pb.forts > 0) {
                concat += "\t\t\t\t" + "bunker = " + pb.forts +"\n";
            }
            if(pb.bunkers > 0) {
                concat += "\t\t\t\t" + "coastal_bunker = " + pb.bunkers +"\n";
            }
            if(pb.bases > 0) {
                concat += "\t\t\t\t" + "naval_base = " + pb.bases +"\n";
            }
            concat += "\t\t\t" + "}\n";

        }
        concat += "\t\t" + "}\n";
        concat += "\t}\n";
        concat += "\tprovinces = {\n";

        boolean first = true;
        for(int province : state.provinces) {
            if(first) {
                concat += "\t\t";
                first = false;
            } else {
                concat += " ";
            }
            concat += province;
        }
        concat+= "\n\t}\n";
        concat += "}";
        setFileContents(file, concat);
    }

    public static void writeInLabel(File file, String label, String line) {
        String content = FileScripts.readFile(file);
        String beforeLabel = ParsingScripts.beforeWord(content,label);
        String afterLabel = ParsingScripts.afterWord(content,label);
        String beforeOpening = ParsingScripts.beforeWord(afterLabel,"{");
        String afterOpening = ParsingScripts.afterWord(afterLabel,"{");
        String add = "\n" + line;
        String concat = beforeLabel + label + beforeOpening + "{" + add + afterOpening;

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
            setFileContents(file, replaced);
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
