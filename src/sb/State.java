package sb;

import java.io.*;
import java.util.ArrayList;

public class State {

    public ArrayList<String> cored;
    public String owner;

    public String content;
    public int manpower;

    public File file;
    public int[] provinces;

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
        this.content = Scripts.readFile(file);
        this.provinces = Scripts.getProvinces(content);
        this.manpower = Scripts.getPop(file);

        this.inf = Scripts.getInf(file);
        this.civs = Scripts.getCivs(file);
        this.mils = Scripts.getMils(file);
        this.dockyards = Scripts.getDockyards(file);
        this.airfields = Scripts.getAirfields(file);
        this.category = Scripts.getCategory(file);

        this.vps = Scripts.getVPs(content);

        this.steel = Scripts.getSteel(content);
        this.aluminum = Scripts.getAluminum(content);
        this.rubber = Scripts.getRubber(content);
        this.tungsten = Scripts.getTungsten(content);
        this.oil = Scripts.getOil(content);
        this.chromium = Scripts.getChromium(content);

    }

    public State(String loc) {
        this(new File(loc));
    }

}
