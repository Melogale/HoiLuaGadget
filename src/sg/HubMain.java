package sg;

import sg.obj.*;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Random;

public class HubMain {

    public static final int NEWCOUNT = 2026;
    public static final int OLDCOUNT = 806;

    public static ArrayList<Integer> statesSoFar = new ArrayList<Integer>();

    public static void checkForDuplicates(String stateIDs) {
        String[] tokens = stateIDs.split(" ");
        for (String token : tokens) {
            int parsed = Integer.parseInt(token);
            if(statesSoFar.contains(parsed)) {
                System.out.println("Warning! State ID " + parsed + " has been used multiple times!");
            } else {
                statesSoFar.add(parsed);
            }
        }
    }

    public static void generateStrategicRegions(String baseStratPath, String newStates, String whereToPut) {
        File strats[] = FileScripts.getDirectoryFiles(baseStratPath);
        File states[] = FileScripts.getDirectoryFiles(newStates);

        ArrayList<Integer> seaProvinces = seaProvinces();



        Hashtable<Integer, Integer> provincestate = new Hashtable<Integer, Integer>();

        // state, statehighestcount
        Hashtable<Integer, Integer> stateMax = new Hashtable<Integer, Integer>();
        // state id, strat id
        Hashtable<Integer, Integer> stateStrat = new Hashtable<Integer, Integer>();
        //int finalState = -1;
        for(File state : states) {
            //System.out.println(state.getName());
            String content = FileScripts.readFile(state);
            ArrayList<Integer> provinces = ContentScripts.getProvinces(content);
            int id = ContentScripts.getID(content);
            //finalState = id;
            for(int i : provinces) {
                provincestate.put(i, id);
            }
            stateMax.put(id, 0);
            stateStrat.put(id, -1);
        }
        /*
        for(File strat : strats) {
            System.out.println(strat.getName());
            String content = FileScripts.readFile(strat);

            if(content.contains("naval")) {
                ArrayList<Integer> provinces = ContentScripts.getProvinces(content);
                int id = finalState + ContentScripts.getID(content);
                for(int i : provinces) {
                    provincestate.put(i, finalState + )
                }
            }

        }
        */
        // FOR WATER PROVINCES THAT AREN'T IN STATES
        Hashtable<Integer, ArrayList<Integer>> stratExtraProvinces = new Hashtable<Integer, ArrayList<Integer>>();
        int highestStrat = 0;
        for(File strat : strats) {

            String content = FileScripts.readFile(strat);
            Hashtable<Integer, Integer> stateprovinceCounter = new Hashtable<Integer, Integer>();
            ArrayList<Integer> provinces = ContentScripts.getProvinces(content);
            //if(Collections.disjoint(seaProvinces, provinces)) {
            //System.out.println(strat.getName());
            int id = ContentScripts.getID(content);
            if(id > highestStrat) {
                highestStrat = id;
            }
            for (int i : provinces) {
                if(provincestate.containsKey(i)) {
                    int state = provincestate.get(i);
                    int value = stateprovinceCounter.containsKey(state) ? stateprovinceCounter.get(state) + 1 : 1;
                    stateprovinceCounter.put(state, value);
                } else {
                    ArrayList<Integer> extra = stratExtraProvinces.containsKey(id) ? stratExtraProvinces.get(id) : new ArrayList<Integer>();
                    extra.add(i);
                    stratExtraProvinces.put(id, extra);
                }
            }
            for (int state : stateprovinceCounter.keySet()) {
                int count = stateprovinceCounter.get(state);
                if (stateMax.get(state) < count) {
                    stateStrat.put(state, id);
                    stateMax.put(state, count);
                }
            }
            //}
        }

        System.out.println("\nDONE WITH STRATS\n");
        // strat id, states
        Hashtable<Integer, ArrayList<Integer>> stratStates = new Hashtable<Integer, ArrayList<Integer>>();
        for(int state : stateStrat.keySet()) {
            int strat = stateStrat.get(state);
            ArrayList<Integer> value = stratStates.containsKey(strat) ? stratStates.get(strat) : new ArrayList<Integer>();
            value.add(state);
            //System.out.println(stratStates);
            //System.out.println(strat);
            //System.out.println(value);
            stratStates.put(strat, value);
        }
        /*
        for(int i = -1; i < highestStrat; i++) {
            System.out.print(i + ":");
            if(stratStates.containsKey(i)) {
                System.out.print(" " + stratStates.get(i));
            }
            if(stratExtraProvinces.containsKey(i)) {
                System.out.print(" (WATER): " + stratExtraProvinces.get(i));
            }
            System.out.print("\n");
        }
        */
        Hashtable<Integer, File> stateFiles = new Hashtable<Integer, File>();
        for(File state : states) {
            String content = FileScripts.readFile(state);
            int id = ContentScripts.getID(content);

            stateFiles.put(id, state);

        }

        for(File strat: strats) {
            String content = FileScripts.readFile(strat);

            String  oldProvinces = ContentScripts.getProvincesString(content);
            int id = ContentScripts.getID(content);

            String newProvinces = "";
            if(stratStates.containsKey(id)) {
                ArrayList<Integer> np = stratStates.get(id);

                for(int i : np) {
                    newProvinces += "# State " + i + "\n\t\t";
                    File state = stateFiles.get(i);

                    ArrayList<Integer> stateProvinces = ContentScripts.getProvinces(FileScripts.readFile(state));
                    for(int j = 0; j < stateProvinces.size(); j++) {
                        int cp = stateProvinces.get(j);
                        newProvinces += cp + (j == stateProvinces.size() - 1 ? "" : " ");
                    }
                    newProvinces += "\n\t\t";
                }
                newProvinces = newProvinces.substring(0, newProvinces.length() - 3);
            }
            if(stratExtraProvinces.containsKey(id)) {
                ArrayList<Integer> extraProvinces = stratExtraProvinces.get(id);
                newProvinces += "\n\t\t# Water Provinces\n\t\t";
                for(int j = 0; j < extraProvinces.size(); j++) {
                    int cp = extraProvinces.get(j);
                    newProvinces += cp + (j == extraProvinces.size() - 1 ? "" : " ");
                }
            }
            File newFile = FileScripts.createFile(whereToPut + "/" + id + ".txt");
            WritingScripts.setFileContents(newFile, content);
            FileScripts.replace(newFile, oldProvinces, newProvinces);

        }
    }

    public static void findEmptyStates(String directory) {
        File files[] = FileScripts.getDirectoryFiles(directory);
        for(File file : files) {
            if(file.getName().endsWith(".txt")) {
                String content = FileScripts.readFile(file);
                ArrayList<Integer> provinces = ContentScripts.getProvinces(content);
                if(provinces.size() == 0) {
                    System.out.println(file.getName());
                }
            }
        }
    }


    public static ArrayList<State> loadNewStates() {
        ArrayList<State> newstates = new ArrayList<State>();
        for(int i = 1001; i <= NEWCOUNT; i++) {
            newstates.add(new State(FileScripts.findFileFromStart("newstates", i)));
        }
        return newstates;
    }
    public static ArrayList<State> loadOldStates() {
        ArrayList<State> newstates = new ArrayList<State>();
        for(int i = 1; i <= OLDCOUNT; i++) {
            newstates.add(new State(FileScripts.findFileFromStart("states", i)));
        }
        return newstates;
    }

    public static void printAllStratProvinces() {
        for(int i = 1; i <= 210; i++) {
            StrategicRegion strat = new StrategicRegion(FileScripts.findFileFromStart("strategicregions", i));
            System.out.print(strat.provinces + " ");
        }

    }


    public static void printAllStateProvinces() {
        ArrayList<State> states = loadOldStates();
        for(int i = 0; i < states.size(); i++) {
            State current = states.get(i);
            for(int j = 0; j < current.provinces.size(); j++) {
                System.out.print(current.provinces.get(j) + " ");
            }
        }
    }




    public static ArrayList<String> readLoc() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File("oldloc/state_names_l_english.yml")));
            String line = null;
            ArrayList<String> names = new ArrayList<String>();
            try {
                while((line = reader.readLine()) != null) {
                    if(line.contains("#")) {
                        line = ParsingScripts.beforeWord(line, "#");
                    }
                    line = ParsingScripts.afterWord(line, ":");
                    names.add(line);
                }

                return names;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                reader.close();
            }
        } catch(FileNotFoundException f) {
            System.out.println("Loc not found.");
        } catch(IOException e) {
            System.out.println("Loc yielded an IO exception.");
        }
        return null;
    }

    public static ArrayList<Integer> newStatesWithVPs() {
        ArrayList<Integer> has = new ArrayList<Integer>();
        for(int i = 1001; i <= 2018; i++) {
            if(ContentScripts.getVPs(FileScripts.readFile(FileScripts.findFileFromStart("newstates", i))).size() > 0) {
                has.add(i);
            }
        }
        return has;
    }

    public static String statesToProvinces(String states) throws IOException {
        String[] tokens = states.split(" ");
        String provinces = "";
        for (String token : tokens) {
            String curPro = ContentScripts.getProvincesString("states/" + token + ".txt");
            provinces += curPro + " ";
        }
        return provinces.trim();
    }

    public static void randomColors() {
        for(int i = 0; i < 30; i++) {
            Random random = new Random();
            System.out.println(random.nextInt(200) + " " + random.nextInt(200) + " " + random.nextInt(200));

        }
    }

    public static ArrayList<Integer> onePopNewStates() {
        ArrayList<Integer> undone = new ArrayList<Integer>();
        for(int i = 1001; i <= NEWCOUNT; i++) {
            int pop = ContentScripts.getPop(FileScripts.readFile(FileScripts.findFileFromStart("newstates", i)));
            if (pop == 1) {
                undone.add(i);
            }
        }
        return undone;
    }

    public static ProvinceContent getProvinceFromID(ArrayList<ProvinceContent> list, int id) {
        for(int i = 0; i < list.size(); i++) {
            ProvinceContent c = list.get(i);
            if(c.id == id) {
                return c;
            }
        }
        return null;
    }

    public static ArrayList<Integer> coastalProvinces() {
        File file = new File(Paths.HOI_DEFINITIONS_CSV);
        ArrayList<Integer> coastal = new ArrayList<Integer>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = null;
            StringBuilder stringBuilder = new StringBuilder();
            String ls = System.getProperty("line.separator");
            try {
                while((line = reader.readLine()) != null) {
                    if(line.contains("#")) {
                        line = ParsingScripts.beforeWord(line, "#");
                    }
                    if(line.contains("true")) {
                        coastal.add(Integer.parseInt(ParsingScripts.beforeWord(line, ";")));
                    }
                    stringBuilder.append(line);
                    stringBuilder.append(ls);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                reader.close();
            }
        } catch(FileNotFoundException f) {
            System.out.println(file.getName() + " not found.");
        } catch(IOException e) {
            System.out.println(file.getName() + " yielded an IO exception.");
        }
        return coastal;
    }

    public static ArrayList<Integer> seaProvinces() {
        File file = new File("definition.csv");
        ArrayList<Integer> sea = new ArrayList<Integer>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = null;
            StringBuilder stringBuilder = new StringBuilder();
            String ls = System.getProperty("line.separator");
            try {
                while((line = reader.readLine()) != null) {
                    if(line.contains("#")) {
                        line = ParsingScripts.beforeWord(line, "#");
                    }
                    if(line.contains("sea")) {
                        sea.add(Integer.parseInt(ParsingScripts.beforeWord(line, ";")));
                    }
                    stringBuilder.append(line);
                    stringBuilder.append(ls);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                reader.close();
            }
        } catch(FileNotFoundException f) {
            System.out.println(file.getName() + " not found.");
        } catch(IOException e) {
            System.out.println(file.getName() + " yielded an IO exception.");
        }
        return sea;
    }

    public static void populateStates(String stateDir) {
        ArrayList<State> olds = loadOldStates();

        ArrayList<Integer> dothese = onePopNewStates();
        ArrayList<Integer> coastal = coastalProvinces();

        ArrayList<ProvinceContent> provinces = new ArrayList<ProvinceContent>();

        for(int i = 0; i < olds.size(); i++) {
            State cur = olds.get(i);
            int pc = cur.provinces.size();
            int mp = cur.manpower;


            for(int j = 0; j < pc; j++) {
                int inf = cur.inf;
                int civs = 0;
                int mils = 0;
                int dockyards = 0;
                int airfields = 0;
                String category = cur.category;

                if(j == pc / 2) {
                    civs = cur.civs;
                    mils = cur.mils;
                    airfields = cur.airfields;
                }
                if(coastal.contains(j)) {
                    dockyards = cur.dockyards;
                }
                provinces.add(new ProvinceContent(cur.provinces.get(j), mp / pc, inf, civs, mils, dockyards, airfields, category));
            }
        }

        ArrayList<State> news = loadNewStates();
        for(int i = 0; i < news.size(); i++) {
            State curs = news.get(i);
            ArrayList<Integer> provs = curs.provinces;
            int pop = 0;
            int infTotal = 0;
            int civs = 0;
            int mils = 0;
            int dockyards = 0;
            int airfields = 0;

            ArrayList<String> categories = new ArrayList<String>();

            File newFile = FileScripts.clone(curs.file, stateDir);
            String newName = newFile.getName();
            int newID = Integer.parseInt(newName.substring(0, newName.length() - 4));
            if(dothese.contains(newID)) {
                for(int j = 0; j < provs.size(); j++) {
                    ProvinceContent cprov = getProvinceFromID(provinces, provs.get(j));
                    if(cprov == null) {
                        System.out.println("Province " + provs.get(j) + " in state " + curs.file.getName());
                    } else {
                        pop += cprov.pop;
                        infTotal += cprov.inf;
                        civs += cprov.civs;
                        mils += cprov.mils;
                        dockyards += cprov.dockyards;
                        airfields += cprov.airfields;
                        categories.add(cprov.category);
                    }
                }
                int inf = infTotal / provs.size();
                System.out.println("Processing state " + newID + ".");
                WritingScripts.setPop(newFile, pop);
                WritingScripts.setInf(newFile, inf);
                WritingScripts.setCivs(newFile, civs);
                WritingScripts.setMils(newFile, mils);
                //setDockyards(newFile, dockyards);
                WritingScripts.setAirfields(newFile, airfields);
                WritingScripts.setCategory(newFile, UtilScripts.getMostCommonString(categories));
            } else {
                System.out.println("Skipping state " + newID + ".");
            }

        }

    }



    public static void genLoc(String locFileName) throws IOException {
        ArrayList<State> olds = loadOldStates();

        ArrayList<ProvinceContent> provinces = new ArrayList<ProvinceContent>();

        ArrayList<String> names = readLoc();

        File newFile = new File(locFileName);
        newFile.createNewFile();


        String string = "";

        for(int i = 0; i < olds.size(); i++) {
            State cur = olds.get(i);
            for(int j = 0; j < cur.provinces.size(); j++) {
                provinces.add(new ProvinceContent(cur.provinces.get(j), names.get(i)));
            }
        }

        ArrayList<State> news = loadNewStates();
        for(int i = 0; i < news.size(); i++) {
            State curs = news.get(i);
            ArrayList<Integer> provs = curs.provinces;

            ArrayList<String> newNames = new ArrayList<String>();

            String newName = curs.file.getName();

            int newID = Integer.parseInt(newName.substring(0, newName.length() - 4));

            for(int j = 0; j < provs.size(); j++) {
                ProvinceContent cprov = getProvinceFromID(provinces, provs.get(j));
                if(cprov == null) {
                    System.out.println("Province " + provs.get(j) + " in state " + curs.file.getName());
                } else {
                    newNames.add(cprov.stateName);
                }
            }
            string = string + " STATE_" + newID + ":" + UtilScripts.getMostCommonString(newNames) + "\n";
        }
        WritingScripts.setFileContents(newFile, string);
    }
}