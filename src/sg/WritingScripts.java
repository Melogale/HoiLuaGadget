package sg;

import sg.obj.ProvinceBuildings;
import sg.obj.State;
import sg.obj.VP;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

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

    public static void writeStateContent(File file, State state) {
        StringBuilder concat = new StringBuilder("state = {\n\tid = " + state.id + "\n");
        concat.append("\tname = \"STATE_").append(state.id).append("\"\n");
        concat.append("\tmanpower = ").append(state.manpower).append("\n");
        concat.append("\tstate_category = ").append(state.category).append("\n");
        if(state.impassable) {
            concat.append("\timpassable = yes\n");
        }

        if(state.steel > 0 || state.aluminum > 0 || state.rubber > 0 || state.tungsten > 0 || state.chromium > 0 || state.oil > 0) {
            concat.append("\tresources = {\n");
            if(state.oil > 0) {
                concat.append("\t\toil = ").append(state.oil).append("\n");
            }
            if(state.aluminum > 0) {
                concat.append("\t\taluminium = ").append(state.aluminum).append("\n");
            }
            if(state.rubber > 0) {
                concat.append("\t\trubber = ").append(state.rubber).append("\n");
            }
            if(state.tungsten > 0) {
                concat.append("\t\ttungsten = ").append(state.tungsten).append("\n");
            }
            if(state.steel > 0) {
                concat.append("\t\tsteel = ").append(state.steel).append("\n");
            }
            if(state.chromium > 0) {
                concat.append("\t\tchromium = ").append(state.chromium).append("\n");
            }
            concat.append("\t}\n");
        }

        concat.append("\thistory = {\n");
        concat.append("\t\towner = ").append(state.owner).append("\n");

        List<String> sortedCored = state.cored;
        if(sortedCored != null) {
            if (sortedCored.contains(state.owner)) {
                sortedCored.remove(state.owner);
                Collections.sort(sortedCored);
                sortedCored.add(0, state.owner);
            } else {
                Collections.sort(sortedCored);
            }
            for (String core : sortedCored) {
                concat.append("\t\tadd_core_of = ").append(core).append("\n");
            }
        }
        HashMap<Integer, Integer> vps = state.vps;
        Integer[] keys = new Integer[vps.size()];
        keys = vps.keySet().toArray(keys);
        Arrays.sort(keys);
        for(int vp : keys) {
            concat.append("\t\t" + "victory_points = { ").append(vp).append(" ").append(vps.get(vp)).append(" }\n");
        }
        concat.append("\t\tbuildings = {\n");
        if(state.inf > 0) {
            concat.append("\t\t\tinfrastructure = ").append(state.inf).append("\n");
        }
        if(state.civs > 0) {
            concat.append("\t\t\tindustrial_complex = ").append(state.civs).append("\n");
        }
        if(state.mils > 0) {
            concat.append("\t\t\tarms_factory = ").append(state.mils).append("\n");
        }
        if(state.dockyards > 0) {
            concat.append("\t\t\tdockyard = ").append(state.dockyards).append("\n");
        }
        if(state.airfields > 0) {
            concat.append("\t\t\tair_base = ").append(state.airfields).append("\n");
        }
        if(state.refineries > 0) {
            concat.append("\t\t\tsynthetic_refinery = ").append(state.refineries).append("\n");
        }
        if(state.silos > 0) {
            concat.append("\t\t\tfuel_silo = ").append(state.silos).append("\n");
        }
        if(state.antiairs > 0) {
            concat.append("\t\t\tanti_air_building = ").append(state.antiairs).append("\n");
        }
        if(state.reactors > 0) {
            concat.append("\t\t\tnuclear_reactor = ").append(state.reactors).append("\n");
        }
        if(state.radars > 0) {
            concat.append("\t\t\tradar_station = ").append(state.radars).append("\n");
        }
        if(state.rocketsites > 0) {
            concat.append("\t\t\trocket_site = ").append(state.rocketsites).append("\n");
        }
        for(ProvinceBuildings pb : state.pbuilds) {
            concat.append("\t\t\t").append(pb.province).append(" = {\n");
            if(pb.forts > 0) {
                concat.append("\t\t\t\t" + "bunker = ").append(pb.forts).append("\n");
            }
            if(pb.bunkers > 0) {
                concat.append("\t\t\t\t" + "coastal_bunker = ").append(pb.bunkers).append("\n");
            }
            if(pb.bases > 0) {
                concat.append("\t\t\t\t" + "naval_base = ").append(pb.bases).append("\n");
            }
            concat.append("\t\t\t" + "}\n");

        }
        concat.append("\t\t" + "}\n");
        concat.append("\t}\n");
        concat.append("\tprovinces = {\n");

        boolean first = true;
        for(int province : state.provinces) {
            if(first) {
                concat.append("\t\t");
                first = false;
            } else {
                concat.append(" ");
            }
            concat.append(province);
        }
        concat.append("\n\t}\n");
        concat.append("\tlocal_supplies = ").append(state.supplies).append(".0").append("\n");
        concat.append("}");
        setFileContents(file, concat.toString());
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
