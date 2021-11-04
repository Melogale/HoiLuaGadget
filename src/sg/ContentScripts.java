package sg;

import sg.obj.VP;

import java.util.ArrayList;

public class ContentScripts {

    public static String getProvincesString(String content) {
        return ParsingScripts.cleanList(ParsingScripts.getBlock(content, "provinces"));
    }

    public static ArrayList<Integer> getProvinces(String content) {
        return ParsingScripts.parseIntList(getProvincesString(content));
    }

    public static int getSteel(String content) {
        return ParsingScripts.getValueInt(content, "steel");
    }

    public static int getOil(String content) {
        return ParsingScripts.getValueInt(content, "oil");
    }

    public static int getChromium(String content) {
        return ParsingScripts.getValueInt(content, "chromium");
    }

    public static int getAluminum(String content) {
        return ParsingScripts.getValueInt(content, "aluminium");
    }

    public static int getTungsten(String content) {
        return ParsingScripts.getValueInt(content, "tungsten");
    }

    public static int getRubber(String content) {
        return ParsingScripts.getValueInt(content, "rubber");
    }

    public static String getCategory(String content) {
        return ParsingScripts.getValue(content, "state_category");
    }

    public static int getPop(String content) {
        return ParsingScripts.getValueInt(content, "manpower");
    }

    public static int getInf(String content) {
        return ParsingScripts.getValueInt(content, "infrastructure");
    }
    public static int getCivs(String content) {
        return ParsingScripts.getValueInt(content, "industrial_complex");
    }
    public static int getMils(String content) {
        return ParsingScripts.getValueInt(content, "arms_factory");
    }
    public static int getAirfields(String content) {
        return ParsingScripts.getValueInt(content, "air_base");
    }
    public static int getDockyards(String content) {
        return ParsingScripts.getValueInt(content, "dockyard");
    }
    public static int getID(String content) {
        return ParsingScripts.getValueInt(content, "id");
    }

    public static ArrayList<VP> getVPs(String content) {
        String section = "victory_points";
        ArrayList<Integer> indexes = new ArrayList<Integer>();
        int index = content.indexOf(section);
        while(index >= 0) {
            indexes.add(index);
            index = content.indexOf(section, index + section.length());
        }
        ArrayList<VP> vps = new ArrayList<VP>();
        for(int i = 0; i < indexes.size(); i++) {
            ArrayList<Integer> vp = ParsingScripts.parseIntList(ParsingScripts.cleanList(ParsingScripts.beforeWord(ParsingScripts.afterWord(content.substring(indexes.get(i) + section.length()), "{"), "}")));
            vps.add(new VP(vp.get(0), vp.get(1)));
        }
        return vps;
    }

}
