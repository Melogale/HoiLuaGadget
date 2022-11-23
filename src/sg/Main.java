package sg;

import sg.obj.SimpleColor;

import java.io.IOException;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) throws IOException {
        //HubFresh.stateTest(Paths.DESKTOP_FOLDER);
        //HubPaint.diffcheck(Paths.DIV_PROVINCES_BMP, Paths.HOI_PROVINCES_BMP);
        //HubPaint.outputStateMap();
        //HubFresh.provincialRepopulation(Paths.DESKTOP_FOLDER + "/States");
        //HashMap<String, SimpleColor> map = HubFresh.colors();
        //System.out.println(map);
        //System.out.println(map.get("ENG"));
        //HubFresh.makeHistoryCountries();
        HubFresh.makeCountries();

        //HubMain.generateStrategicRegions(Paths.HOI_STRATS, Paths.DIV_STATES, Paths.DESKTOP_FOLDER + "/strats");
    }
}