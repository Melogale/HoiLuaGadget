package sg;

import sg.FileScripts;
import sg.HubMain;
import sg.obj.ProvinceDefinition;
import sg.obj.State;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.stream.Stream;

public class HubPaint {


    public static final Color waterColor = new Color(132, 246, 250);

    public static HashMap<Integer, ProvinceDefinition> grabProvinceDefinitions(String location) {
        HashMap<Integer, ProvinceDefinition> map = new HashMap<Integer, ProvinceDefinition>();
        String definitions = FileScripts.readFile(location);
        String[] definitionEntries = definitions.split("\n");
        for(String definitionEntry : definitionEntries) {
            ProvinceDefinition def = new ProvinceDefinition(definitionEntry);
            map.put(def.id, def);
        }
        return map;
    }

    public static ArrayList<State> loadStatesFromDirectory(String dir) {
        ArrayList<State> states = new ArrayList<State>();
        File[] files = FileScripts.getDirectoryFiles(dir);
        for(File file : files) {
            states.add(new State(file));
        }
        return states;
    }

    public static void diffcheck(String path1, String path2) {
        File file1 = FileScripts.getFile(path1);
        File file2 = FileScripts.getFile(path2);

        BufferedImage img1;
        BufferedImage img2;
        try {
            img1 = ImageIO.read(file1);
            img2 = ImageIO.read(file2);

        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        for (int x = 0; x < img1.getWidth(); x++) {
            for (int y = 0; y < img1.getHeight(); y++) {
                int clr1 = img1.getRGB(x, y);
                int clr2 = img2.getRGB(x, y);

                img1.setRGB(x, y, clr1 == clr2 ? Color.BLACK.getRGB() : Color.PINK.getRGB());
            }
        }

        File output = new File("outputdiff.png");
        try {
            ImageIO.write(img1, "png", output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void outputStateMap() {
        ArrayList<State> states = loadStatesFromDirectory(Paths.DIV_STATES);
        HashMap<Integer, ProvinceDefinition> definitions = grabProvinceDefinitions(Paths.HOI_DEFINITIONS_CSV);
        HashMap<Integer, Integer> provinceStates = new HashMap<Integer, Integer>();
        HashMap<Integer, Integer> colorProvinces = new HashMap<Integer, Integer>();

        for(State state : states) {
            ArrayList<Integer> provinces = state.provinces;
            for(int province : provinces) {
                if(!definitions.get(province).isWaterProvince()) {
                    provinceStates.put(province, state.id);
                } else {
                    provinceStates.put(province, 0);
                }
            }
        }

        for(int province : definitions.keySet()) {
            ProvinceDefinition definition = definitions.get(province);
            int color = new Color(definition.r, definition.g, definition.b).getRGB();

            colorProvinces.put(color, province);
            if(!provinceStates.containsKey(province)) {
                provinceStates.put(province, 0);
            }
        }

        /////
        for (int state: provinceStates.keySet()) {
            int value = provinceStates.get(state);
            //System.out.println(state + " province has state " + value + ".");
        }
        /////

        /////
        for (int state: colorProvinces.keySet()) {
            int value = colorProvinces.get(state);
            //System.out.println(state + " color has province " + value + ".");
        }
        /////

        HashMap<Integer, Color> stateColors = new HashMap<Integer, Color>();

        for(int state : provinceStates.values()) {
            Random r = new Random();
            stateColors.put(state, new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256)));
        }

        provinceStates.put(0, 0);
        stateColors.put(0, waterColor);




        File provinces = FileScripts.getFile(Paths.DIV_PROVINCES_BMP);

        BufferedImage img;
        try {
            img = ImageIO.read(provinces);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                int clr = img.getRGB(x, y);

                if(x == 1800 && y == 1800) {
                    //System.out.println();
                }
                Color newColor = stateColors.get(provinceStates.get(colorProvinces.get(clr)));
                if(newColor != null) {
                    img.setRGB(x, y, newColor.getRGB());
                } else {
                    System.out.println("NULL!");
                }
            }
            //System.out.println("Done column " + x + ".");
        }

        File output = new File("output.png");
        try {
            ImageIO.write(img, "png", output);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
