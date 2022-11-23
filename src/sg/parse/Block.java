package sg.parse;

import java.util.ArrayList;
import java.util.HashMap;

public class Block extends Element {

    private ArrayList<Element> list;
    private HashMap<String, Element> map;

    public Block(ArrayList<Element> list) {
        super("block");
        this.list = list;
        for(Element element : list) {
            map.put(element.getLabel(), element);
        }
    }

    public ArrayList<Element> getList() {
        return list;
    }

    public HashMap<String, Element> getMap() {
        return map;
    }

    public String asList() {
        StringBuilder all = new StringBuilder();
        for(Element item : list) {
            all.append(item.toString()).append("\n");
        }
        return all.toString();
    }

    @Override
    public String toString() {
        String all = "{\n";
        all += asList();
        all += "}";
        return all;
    }

}
