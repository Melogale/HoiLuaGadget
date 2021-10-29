package sb;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Main {

    public static final String hoipath = "D:/Programs/Steam/steamapps/common/Hearts of Iron IV";

    public static void main(String[] args) throws IOException {
        PatchScripts.printPaths(PatchScripts.keyWordFiles(PatchScripts.allTextFiles(new File(hoipath))));

    }


}