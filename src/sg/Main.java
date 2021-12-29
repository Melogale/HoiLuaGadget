package sg;

import sg.obj.State;

import java.io.File;
import java.io.IOException;

public class Main {

    public static final String hoipath = "D:/Programs/Steam/steamapps/common/Hearts of Iron IV";

    public static void main(String[] args) throws IOException {
        HubPatch.copyAndMark();
    }


}