package sg.obj;

public class ProvinceDefinition {

    public int id;
    public int r;
    public int g;
    public int b;
    public String type; // land, sea, lake
    public boolean coastal;
    public String terrain; // desert, forest, hills, jungle, marsh, mountain, plains, urban, lakes, ocean
    public int continent;

    public ProvinceDefinition(String definitionEntry) {
        String[] data = definitionEntry.split(";");
        this.id = Integer.parseInt(data[0]);
        this.r = Integer.parseInt(data[1]);
        this.g = Integer.parseInt(data[2]);
        this.b = Integer.parseInt(data[3]);
        type = data[4];
        coastal = Boolean.parseBoolean(data[5]);
        terrain = data[6];
        continent = Integer.parseInt(data[7]);
    }

    public boolean isWaterProvince() {
        return !(type.equals("land"));
    }
}
