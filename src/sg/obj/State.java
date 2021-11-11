package sg.obj;

import sg.ContentScripts;
import sg.FileScripts;
import sg.HubMain;

import java.io.*;
import java.util.ArrayList;

public class State {

    public ArrayList<String> cored;
    public String owner;

    public String content;
    public int manpower;

    public File file;
    public ArrayList<Integer> provinces;

    public ArrayList<VP> vps;

    public int inf;
    public int civs;
    public int mils;
    public int dockyards;
    public int airfields;

    public int steel;
    public int aluminum;
    public int rubber;
    public int tungsten;
    public int chromium;
    public int oil;

    public String category;

    public State(File file) {
        this.file = file;
        this.content = FileScripts.readFile(file);

        this.provinces = ContentScripts.getProvinces(content);
        this.manpower = ContentScripts.getPop(content);

        this.inf = ContentScripts.getInf(content);
        this.civs = ContentScripts.getCivs(content);
        this.mils = ContentScripts.getMils(content);
        this.dockyards = ContentScripts.getDockyards(content);
        this.airfields = ContentScripts.getAirfields(content);
        this.category = ContentScripts.getCategory(content);

        this.vps = ContentScripts.getVPs(content);

        this.steel = ContentScripts.getSteel(content);
        this.aluminum = ContentScripts.getAluminum(content);
        this.rubber = ContentScripts.getRubber(content);
        this.tungsten = ContentScripts.getTungsten(content);
        this.oil = ContentScripts.getOil(content);
        this.chromium = ContentScripts.getChromium(content);

    }

    public State(String loc) {
        this(new File(loc));
    }

}
