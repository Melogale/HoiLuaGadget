package sb;

import java.io.*;

public class State {

    public String content;
    public int manpower;

    public File file;
    public int[] provinces;

    public int inf;
    public int civs;
    public int mils;
    public int dockyards;
    public int airfields;

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

    }

    public State(String loc) {
        this(new File(loc));
    }



}
