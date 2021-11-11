package sg.obj;

public class ProvinceBuildings {

    // naval_base
    // coastal_bunker

    public int province;
    public int bases;
    public int bunkers;

    public ProvinceBuildings(int province, int bases, int bunkers) {
        this.province = province;
        this.bases = bases;
        this.bunkers = bunkers;

    }

    @Override
    public String toString() {
        return province + ":\nbases: " + bases + "\nbunkers: " + bunkers + "\n";
    }

}
