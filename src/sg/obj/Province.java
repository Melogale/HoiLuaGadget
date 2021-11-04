package sg.obj;

public class Province {

    public int id;
    public int pop;

    public String stateName;

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

    public Province(int id, int pop, int inf, int civs, int mils, int dockyards, int airfields, String category, int steel, int aluminum, int rubber, int tungsten, int chromium, int oil) {
        this.id = id;
        this.pop = pop;
        this.inf = inf;
        this.civs = civs;
        this.mils = mils;
        this.dockyards = dockyards;
        this.airfields = airfields;
        this.category = category;
        this.steel = steel;
        this.aluminum = aluminum;
        this.rubber = rubber;
        this.tungsten = tungsten;
        this.chromium = chromium;
        this.oil = oil;
    }

    public Province(int id, int pop, int inf, int civs, int mils, int dockyards, int airfields, String category) {
        this(id, pop, inf, civs, mils, dockyards, airfields, category, 0, 0, 0, 0, 0, 0);

    }

    public Province(int id, String stateName) {
        this(id, 0, 0, 0, 0, 0, 0, "town");
        this.stateName = stateName;
    }

    public Province(int id, int pop) {
        this(id, pop, 0, 0, 0, 0, 0, "town");
    }
}
