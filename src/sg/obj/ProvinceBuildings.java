package sg.obj;

public class ProvinceBuildings implements Comparable {

    // naval_base
    // coastal_bunker

    public int province;

    public int bases;
    public int bunkers;
    public int forts;

    public ProvinceBuildings(int province, int bases, int bunkers, int forts) {
        this.province = province;

        this.bases = bases;
        this.bunkers = bunkers;
        this.forts = forts;

    }

    @Override
    public String toString() {
        return province + ":\nbases: " + bases + "\nbunkers: " + bunkers + "\nforts: " + forts + "\n";
    }

    @Override
    public int compareTo(Object o) {
        return this.province -((ProvinceBuildings)o).province;
    }
}
