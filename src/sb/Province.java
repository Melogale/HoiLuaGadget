package sb;

public class Province {

    public int id;
    public int pop;

    public String stateName;

    public int inf;
    public int civs;
    public int mils;
    public int dockyards;
    public int airfields;

    public String category;

    public Province(int id, int pop, int inf, int civs, int mils, int dockyards, int airfields, String category) {
        this.id = id;
        this.pop = pop;
        this.inf = inf;
        this.civs = civs;
        this.mils = mils;
        this.dockyards = dockyards;
        this.airfields = airfields;
        this.category = category;
    }

    public Province(int id, String stateName) {
        this(id, 0, 0, 0, 0, 0, 0, "town");
        this.stateName = stateName;
    }

    public Province(int id, int pop) {
        this(id, pop, 0, 0, 0, 0, 0, "town");
    }
}
