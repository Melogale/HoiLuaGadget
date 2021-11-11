package sg;

import java.io.IOException;

public class Main {

    public static final String hoipath = "D:/Programs/Steam/steamapps/common/Hearts of Iron IV";

    public static void main(String[] args) throws IOException {
        //PatchScripts.printPaths(PatchScripts.keyWordFiles(PatchScripts.allTextFiles(new File(hoipath))));
        System.out.println(ContentScripts.getProvinceBuildings("\n" +
                "state={\n" +
                "\tid=1\n" +
                "\tname=\"STATE_1\" # Corsica\n" +
                "\tmanpower = 322900\n" +
                "\t\n" +
                "\tstate_category = town\n" +
                "\n" +
                "\thistory={\n" +
                "\t\towner = FRA\n" +
                "\t\tvictory_points = { 3838 1 }\n" +
                "\t\tbuildings = {\n" +
                "\t\t\tinfrastructure = 4\n" +
                "\t\t\tindustrial_complex = 1\n" +
                "\t\t\tair_base = 1\n" +
                "\t\t\t3838 = {\n" +
                "\t\t\t\tnaval_base = 3\n" +
                "\t\t\t}\n" +
                "\t\t\t3839 = {\n" +
                "\t\t\t\tnaval_base = 3\n" +
                "\t\t\t}\n" +
                "\t\t\t3840 = {\n" +
                "\t\t\t\tnaval_base = 3\n" +
                "\t\t        coastal_bunker = 2\n" +
                "\t\t\t}\n" +
                "\t\t\t3842 = {\n" +
                "\t\t        coastal_bunker = 2\n" +
                "\t\t\t}\n" +
                "            3842 = {\n" +
                "                naval_base = 2\n" +
                "            }\n" +
                "\t\t}\n" +
                "\t\tadd_core_of = COR\n" +
                "\t\tadd_core_of = FRA\n" +
                "\t}\n" +
                "\n" +
                "\tprovinces={\n" +
                "\t\t3838 9851 11804 \n" +
                "\t}\n" +
                "}\n"));
    }


}