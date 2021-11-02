package sg;

import sg.Parsing;
import java.util.ArrayList;

public class ContentScripts {

    public static String getProvincesString(String content) {
        return Parsing.cleanList(Parsing.getBlock(content, "provinces"));
    }

    public static ArrayList<Integer> getProvinces(String content) {
        return Parsing.parseIntList(getProvincesString(content));
    }

}
