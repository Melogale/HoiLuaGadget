package sg;

import java.io.IOException;

public class Main {

    public static final String hoipath = "D:/Programs/Steam/steamapps/common/Hearts of Iron IV";

    public static void main(String[] args) throws IOException {
        //PatchScripts.printPaths(PatchScripts.keyWordFiles(PatchScripts.allTextFiles(new File(hoipath))));
        System.out.println(HubMain.coastalProvinces());
    }


}