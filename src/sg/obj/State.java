package sg.obj;

import sg.ContentScripts;
import sg.FileScripts;
import sg.HubMain;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class State {

    public int id;

    public List<String> cored;
    public String owner;

    public String content;
    public int manpower;

    public boolean impassable;

    public File file;
    public ArrayList<Integer> provinces;

    public HashMap<Integer, Integer> vps;
    public ArrayList<ProvinceBuildings> pbuilds;

    public int inf;
    public int civs;
    public int mils;
    public int dockyards;
    public int airfields;

    public int refineries;
    public int reactors;
    public int antiairs;
    public int silos;
    public int radars;
    public int rocketsites;


    public int oil;
    public int aluminum;
    public int rubber;
    public int tungsten;
    public int steel;
    public int chromium;

    public int supplies;


    public String category;

    public State(File file) {
        this.file = file;
        this.content = FileScripts.readFile(file);
        this.id = ContentScripts.getID(content);
        this.provinces = ContentScripts.getProvinces(content);
        Collections.sort(provinces);
        this.manpower = ContentScripts.getPop(content);

        this.inf = ContentScripts.getInf(content);
        this.civs = ContentScripts.getCivs(content);
        this.mils = ContentScripts.getMils(content);
        this.dockyards = ContentScripts.getDockyards(content);
        this.airfields = ContentScripts.getAirfields(content);

        this.refineries = ContentScripts.getRefineries(content);
        this.reactors = ContentScripts.getReactors(content);
        this.antiairs = ContentScripts.getAntiairs(content);
        this.silos = ContentScripts.getSilos(content);
        this.radars = ContentScripts.getRadars(content);
        this.rocketsites = ContentScripts.getRocketSites(content);

        this.category = ContentScripts.getCategory(content);

        this.vps = ContentScripts.getVPs(content);
        this.pbuilds = ContentScripts.getProvinceBuildings(content);
        Collections.sort(pbuilds);

        this.steel = ContentScripts.getSteel(content);
        this.aluminum = ContentScripts.getAluminum(content);
        this.rubber = ContentScripts.getRubber(content);
        this.tungsten = ContentScripts.getTungsten(content);
        this.oil = ContentScripts.getOil(content);
        this.chromium = ContentScripts.getChromium(content);
        this.impassable = content.contains("impassable");

        this.supplies = ContentScripts.getSupplies(content);

        this.cored = ContentScripts.getCored(content);
        this.owner = ContentScripts.getOwner(content);

    }

    public State(ArrayList<Integer> provinces, int manpower, int inf, int civs, int mils, int dockyards, int airfields, int refineries, int reactors, int antiairs, int silos, int radars, int rocketsites, String category, HashMap<Integer, Integer> vps, ArrayList<ProvinceBuildings> pbs, int oil, int aluminum, int rubber, int tungsten, int steel, int chromium, boolean impassable, List<String> cored, String owner, int supplies) {
        this.provinces = provinces;
        Collections.sort(provinces);
        this.manpower = manpower;

        this.inf = inf;
        this.civs = civs;
        this.mils = mils;
        this.dockyards = dockyards;
        this.airfields = airfields;

        this.refineries = refineries;
        this.reactors = reactors;
        this.antiairs = antiairs;
        this.silos = silos;
        this.radars = radars;
        this.rocketsites = rocketsites;

        this.category = category;

        this.vps = vps;
        this.pbuilds = pbs;
        Collections.sort(pbuilds);

        this.steel = steel;
        this.aluminum = aluminum;
        this.rubber = rubber;
        this.tungsten = tungsten;
        this.oil = oil;
        this.chromium = chromium;

        this.impassable = impassable;

        this.supplies = supplies;
        this.owner = owner;
        this.cored = cored;
    }

    public State(String loc) {
        this(new File(loc));
    }


}
