package sg;

import java.io.IOException;

public class Main {

    public static final String hoipath = "D:/Programs/Steam/steamapps/common/Hearts of Iron IV";
    public static final String divergencestatesfolderpath = "C:/Users/zacha/Documents/Paradox Interactive/Hearts of Iron IV/mod/Divergences/history/states";
    public static final String basestratpath = hoipath + "/map/strategicregions";
    public static final String basestratpathlaptop = "C:/Program Files (x86)/Steam/steamapps/common/Hearts of Iron IV/map/strategicregions";

    public static void main(String[] args) throws IOException {
        //PatchScripts.printPaths(PatchScripts.keyWordFiles(PatchScripts.allTextFiles(new File(hoipath))));
        //HubMain.findEmptyStates(statesfolderpath);
        HubMain.generateStrategicRegions(basestratpathlaptop, divergencestatesfolderpath, "");

    }
}