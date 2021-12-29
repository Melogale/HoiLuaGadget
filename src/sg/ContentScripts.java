package sg;

import sg.obj.ProvinceBuildings;
import sg.obj.VP;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

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
    public static int getRefineries(String content) {
        return ParsingScripts.getValueInt(content, "synthetic_refinery");
    }
    public static int getReactors(String content) {
        return ParsingScripts.getValueInt(content, "nuclear_reactor");
    }
    public static int getAntiairs(String content) {
        return ParsingScripts.getValueInt(content, "anti_air_building");
    }
    public static int getSilos(String content) {
        return ParsingScripts.getValueInt(content, "fuel_silo");
    }
    public static int getRadars(String content) {
        return ParsingScripts.getValueInt(content, "radar_station");
    }
    public static int getRocketSites(String content) {
        return ParsingScripts.getValueInt(content, "rocket_site");
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

    public static ArrayList<ProvinceBuildings> getProvinceBuildings(String content) {
        String base = "naval_base";
        String bunker = "coastal_bunker";
        String fort = "bunker";
        ArrayList<Integer> bases = ParsingScripts.findIndexesOf(content, base);
        ArrayList<Integer> bunkers = ParsingScripts.findIndexesOf(content,  bunker);
        ArrayList<Integer> forts = ParsingScripts.findIndexesOf(content, fort);

        HashMap<Integer, ProvinceBuildings> data = new HashMap<Integer, ProvinceBuildings>();

        for(int i = 0; i < bases.size(); i++) {
            String temp = content.substring(i == 0 ? 0 : bases.get(i - 1) + base.length(), bases.get(i) + base.length());
            String label = ParsingScripts.findLabelWith(temp, base);
            int province = Integer.parseInt(label.trim());
            String onward = content.substring(bases.get(i));
            if(data.containsKey(province)) {
                data.get(province).bases += ParsingScripts.getValueInt(onward, base);
            } else {
                data.put(province, new ProvinceBuildings(province, ParsingScripts.getValueInt(onward, base), 0, 0));
            }
        }

        for(int i = 0; i < bunkers.size(); i++) {
            String temp = content.substring(i == 0 ? 0 : bunkers.get(i - 1) + bunker.length(), bunkers.get(i) + bunker.length());
            String label = ParsingScripts.findLabelWith(temp, bunker);
            int province = Integer.parseInt(label.trim());
            String onward = content.substring(bunkers.get(i));
            if(data.containsKey(province)) {
                data.get(province).bunkers += ParsingScripts.getValueInt(onward, bunker);
            } else {
                data.put(province, new ProvinceBuildings(province, 0, ParsingScripts.getValueInt(onward, bunker), 0));
            }
        }

        for(int i = 0; i < forts.size(); i++) {
            String temp = content.substring(i == 0 ? 0 : forts.get(i - 1) + fort.length(), forts.get(i) + fort.length());
            String label = ParsingScripts.findLabelWith(temp, fort);
            int province = Integer.parseInt(label.trim());
            String onward = content.substring(forts.get(i));
            if(data.containsKey(province)) {
                data.get(province).forts += ParsingScripts.getValueInt(onward, fort);
            } else {
                data.put(province, new ProvinceBuildings(province, 0, 0, ParsingScripts.getValueInt(onward, fort)));
            }
        }

        ArrayList<ProvinceBuildings> list = new ArrayList<ProvinceBuildings>();
        Collection<ProvinceBuildings> col = data.values();
        for(ProvinceBuildings i : col) {
            list.add(i);
        }

        return list;

    }

}
