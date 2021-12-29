package sg;

import sg.obj.*;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
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

    public static ArrayList<VP> getAllVanillaVPs() {
        ArrayList<VP> vps = new ArrayList<VP>();
        for(int i = 1; i <= 806; i++) {
            vps.addAll(ContentScripts.getVPs(FileScripts.readFile(FileScripts.findFileFromStart("states", i))));
        }
        return vps;
    }



    public static void allocateVPs(String outputDir) throws IOException {
        ArrayList<VP> vps = getAllVanillaVPs();
        ArrayList<Integer> done = newStatesWithVPs();
        ArrayList<State> newstates = loadNewStates();

        ArrayList<StatedVP> statedVPs = new ArrayList<StatedVP>();

        for(int i = 0; i < vps.size(); i++) {
            VP curVP = vps.get(i);

            for(int j = 0; j < newstates.size(); j++) {
                State curState = newstates.get(j);
                boolean rightState = false;

                for(int k = 0; k < curState.provinces.size(); k++) {
                    int curProvince = curState.provinces.get(k);
                    if(curProvince == curVP.province) {
                        rightState = true;
                    }
                }

                if(rightState) {
                    statedVPs.add(new StatedVP(curVP, curState.file));
                }

            }

        }

        for(int i = 0; i < statedVPs.size(); i++) {
            StatedVP curVP = statedVPs.get(i);
            File newFile = new File(outputDir + "/" + curVP.newstate.getName());
            if(!Files.exists(newFile.toPath())) {
                newFile.createNewFile();
                FileScripts.copyFileUsingStream(curVP.newstate, newFile);
            }
            String content = FileScripts.readFile(newFile);
            String beforeHistory = ParsingScripts.beforeWord(content,"history");
            String afterHistory = ParsingScripts.afterWord(content,"history");
            String beforeOpening = ParsingScripts.beforeWord(afterHistory,"{");
            String afterOpening = ParsingScripts.afterWord(afterHistory,"{");
            String vp = "\n\t\tvictory_points = {\n\t\t\t" + curVP.province + " " + curVP.value + "\n\t\t}\n";
            String concat = beforeHistory + "history" + beforeOpening + "{" + vp + afterOpening;
            PrintWriter writer = new PrintWriter(newFile);
            writer.print(concat);
            writer.close();
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
        File file = new File("definition.csv");
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

    /**
     *  For each state in oldDirectory, splits up all the content of the states among its provinces.
     *  In newDirectory, gets the provinces of the states, and adds up the content given to them from the old states.
     *  In finalDirectory, places the final content-filled versions of the new states.
     */
    // CONTENT ALLOCATION VARIABLES
    public static final ArrayList<Integer> coastalProvinces = coastalProvinces();

    public static void provinicalRepopulation(String oldDirectory, String newDirectory, String finalDirectory) {
        File[] oldFiles = FileScripts.getDirectoryFiles(oldDirectory);
        File[] newFiles = FileScripts.getDirectoryFiles(newDirectory);



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