package sg;

import java.util.ArrayList;
import java.util.Collections;

public class UtilScripts {

    public static String getMostCommonString(ArrayList<String> searched) {
        int record = 0;
        String recordHolder = "";
        for(int i = 0; i < searched.size(); i++) {
            int cur = Collections.frequency(searched, searched.get(i));
            if(cur > record) {
                record = cur;
                recordHolder = searched.get(i);
            }
        }
        return recordHolder;
    }

    public static String tabs(int c) {
        String tabs = "";
        for(int i = 0; i < c; i++) {
            tabs += "\t";
        }
        return tabs;
    }

    public static class Tuple {
        public int x;
        public int y;
        public Tuple(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

}
