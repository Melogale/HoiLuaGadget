package sb;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

public class Scripts {

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
            newstates.add(new State(findFileFromStart("newstates", i)));
        }
        return newstates;
    }
    public static ArrayList<State> loadOldStates() {
        ArrayList<State> newstates = new ArrayList<State>();
        for(int i = 1; i <= OLDCOUNT; i++) {
            newstates.add(new State(findFileFromStart("states", i)));
        }
        return newstates;
    }

    public static void printAllStratProvinces() {
        for(int i = 1; i <= 210; i++) {
            StrategicRegion strat = new StrategicRegion(findFileFromStart("strategicregions", i));
            System.out.print(strat.provinces + " ");
        }

    }


    public static void printAllStateProvinces() {
        ArrayList<State> states = loadOldStates();
        for(int i = 0; i < states.size(); i++) {
            State current = states.get(i);
            for(int j = 0; j < current.provinces.length; j++) {
                System.out.print(current.provinces[j] + " ");
            }
        }
    }

    public static ArrayList<VP> getAllVanillaVPs() {
        ArrayList<VP> vps = new ArrayList<VP>();
        for(int i = 1; i <= 806; i++) {
            vps.addAll(getVPs(readFile(findFileFromStart("states", i))));
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

                for(int k = 0; k < curState.provinces.length; k++) {
                    int curProvince = curState.provinces[k];
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
                copyFileUsingStream(curVP.newstate, newFile);
            }
            String content = readFile(newFile);
            String beforeHistory = beforeWord(content,"history");
            String afterHistory = afterWord(content,"history");
            String beforeOpening = beforeWord(afterHistory,"{");
            String afterOpening = afterWord(afterHistory,"{");
            String vp = "\n\t\tvictory_points = {\n\t\t\t" + curVP.province + " " + curVP.value + "\n\t\t}\n";
            String concat = beforeHistory + "history" + beforeOpening + "{" + vp + afterOpening;
            PrintWriter writer = new PrintWriter(newFile);
            writer.print(concat);
            writer.close();
        }

    }

    public static String removeSpaces(String string) {
        return string.replaceAll(" ", "").replaceAll("\t", "");
    }

    public static String cleanList(String string) {
        return string.replaceAll("\t", " ").trim().replaceAll("\\r\\n|\\r|\\n", " ").replaceAll(" +", " ");

    }

    public static int[] parseIntList(String string) {
        String[] tokens = string.split(" ");
        int[] list = new int[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            if(!tokens[i].contains(".")) {
                list[i] = Integer.parseInt(tokens[i]);
            } else {
                list[i] = (int) Double.parseDouble(tokens[i]);
            }
        }
        return list;
    }

    public static int[] getProvinces(String content) {
        return parseIntList(getProvinceList(content));
    }


    public static File findFileFromStart(String folder, String start) {
        File dir = new File(folder);
        File[] foundFiles = dir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.startsWith(start);
            }
        });
        return foundFiles[0];
    }

    public static File[] getDirectoryFiles(String folder) {
        File dir = new File(folder);
        File[] foundFiles = dir.listFiles();
        return foundFiles;
    }

    public static File findFileFromStart(String folder, int start) {
        return findFileFromStart(folder, Integer.toString(start));
    }




    public static String readFile(String loc) {
        return readFile(file(loc));
    }

    public static String readFile(File file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = null;
            StringBuilder stringBuilder = new StringBuilder();
            String ls = System.getProperty("line.separator");
            try {
                while((line = reader.readLine()) != null) {
                    if(line.contains("#")) {
                        line = beforeWord(line, "#");
                    }
                    stringBuilder.append(line);
                    stringBuilder.append(ls);
                }

                return stringBuilder.toString();
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
        return "";
    }

    public static ArrayList<String> readLoc() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File("oldloc/state_names_l_english.yml")));
            String line = null;
            ArrayList<String> names = new ArrayList<String>();
            try {
                while((line = reader.readLine()) != null) {
                    if(line.contains("#")) {
                        line = beforeWord(line, "#");
                    }
                    line = afterWord(line, ":");
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



    public static String getLuaSection(String content, String section) {
        return beforeWord(afterWord(afterWord(content, section), "{"), "}");
    }

    public static String getProvinceList(String content) {
        return cleanList(getLuaSection(content, "provinces"));

    }

    public static String afterWord(String input, String word) {
        int index = input.indexOf(word);
        return input.substring(index == -1 ? 0 : index + word.length());
    }

    public static String beforeWord(String input, String word) {
        int index = input.indexOf(word);
        return input.substring(0, index == -1 ? 0 : index);
    }

    public static ArrayList<VP> getVPs(String content) {
        String section = "victory_points";
        ArrayList<Integer> indexes = new ArrayList<Integer>();
        int index = content.indexOf(section);
        while(index >= 0) {
            indexes.add(index);
            index = content.indexOf(section, index + section.length());
        }
        ArrayList<VP> vps = new ArrayList<VP>();
        for(int i = 0; i < indexes.size(); i++) {
            int[] vp = parseIntList(
                    cleanList(
                            beforeWord(
                                    afterWord(
                                            content.substring(
                                                    indexes.get(i) + section.length()
                                            ),
                                            "{"),
                                    "}")
                    )
            );
            vps.add(new VP(vp[0], vp[1]));
        }
        return vps;
    }

    public static int getEqualsInt(String content, String left) {
        String steel = getEqualsValue(content, left);
        if(steel != "") {
            return Integer.parseInt(steel);
        }
        return 0;
    }

    public static int getSteel(String content) {
        return getEqualsInt(content, "steel");
    }

    public static int getOil(String content) {
        return getEqualsInt(content, "oil");
    }

    public static int getChromium(String content) {
        return getEqualsInt(content, "chromium");
    }

    public static int getAluminum(String content) {
        return getEqualsInt(content, "aluminium");
    }

    public static int getTungsten(String content) {
        return getEqualsInt(content, "tungsten");
    }

    public static int getRubber(String content) {
        return getEqualsInt(content, "rubber");
    }

    public static String getEqualsValue(String content, String left) {
        if(content.indexOf(left) != -1) {
            String after = removeSpaces(beforeWord(afterWord(afterWord(content, "left"), "="), "\n"));
            return after;
        }
        return "";
    }

    public static ArrayList<Integer> newStatesWithVPs() {
        ArrayList<Integer> has = new ArrayList<Integer>();
        for(int i = 1001; i <= 2018; i++) {
            if(getVPs(readFile(findFileFromStart("newstates", i))).size() > 0) {
                has.add(i);
            }
        }
        return has;
    }

    public static File clone(File orig, String newDirectory) {
        File newFile = new File(newDirectory + "/" + orig.getName());
        try {
            newFile.createNewFile();
            copyFileUsingStream(orig, newFile);
        } catch (IOException e) {
            System.out.println("Clone failed");
        }
        return newFile;
    }

    private static void copyFileUsingStream(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }
    }

    public static void replace(File file, String oldLine, String newLine) throws IOException {
        Scanner sc = new Scanner(file);
        StringBuffer buffer = new StringBuffer();
        while (sc.hasNextLine()) {
            buffer.append(sc.nextLine()+System.lineSeparator());
        }
        String fileContents = buffer.toString();
        sc.close();
        fileContents = fileContents.replaceAll(oldLine, newLine);
        FileWriter writer = new FileWriter(file);
        writer.append(fileContents);
        writer.flush();
    }

    public static String statesToProvinces(String states) throws IOException {
        String[] tokens = states.split(" ");
        String provinces = "";
        for (String token : tokens) {
            String curPro = getProvinceList("states/" + token + ".txt");
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


    public static String getLineWith(String content, String with) {
        String[] lines = content.split("\n");
        for(int i = 0; i < lines.length; i++) {
            if(lines[i].contains(with)) {
                return lines[i];
            }
        }
        return "";
    }

    public static File file(String loc) {
        return new File(loc);
    }

    public static int getValue(File file, String label) {
        String content = readFile(file);
        if(content.contains(label)) {
            String after = afterWord(readFile(file), label);
            String before = after.split("\n")[0];
            String man = cleanList(before).replaceAll("=", "").trim();
            int val = Integer.parseInt(man);
            return val;
        }
        return 0;
    }

    public static String getStringValue(File file, String label) {
        String content = readFile(file);
        if(content.contains(label)) {
            String after = afterWord(readFile(file), label);
            String before = after.split("\n")[0];
            String man = cleanList(before).replaceAll("=", "").trim();
            return man;
        }
        return "";
    }

    public static String tabs(int c) {
        String tabs = "";
        for(int i = 0; i < c; i++) {
            tabs += "\t";
        }
        return tabs;
    }

    public static void writeLineUnder(File file, String label, String line) {
        String content = readFile(file);
        String beforeHistory = beforeWord(content,label);
        String afterHistory = afterWord(content,label);
        String beforeOpening = beforeWord(afterHistory,"{");
        String afterOpening = afterWord(afterHistory,"{");
        String add = "\n" + line;
        String concat = beforeHistory + label + beforeOpening + "{" + add + afterOpening;
        try {
            PrintWriter writer = new PrintWriter(file);
            writer.print(concat);
            writer.close();
        } catch (FileNotFoundException e) {
            System.out.println(file.getName() + " not found!");
        }
    }


    public static void setValue(File file, String label, int value, int tabCount, String heading) {
        setStringValue(file, label, Integer.toString(value), tabCount, heading);
    }

    public static void setStringValue(File file, String label, String value, int tabCount, String heading) {
        String content = readFile(file);
        String tabs = tabs(tabCount);
        if(content.contains(label)) {
            String line = getLineWith(content, label);
            String before = beforeWord(content, line);
            String after = afterWord(content, line);
            String replaced = before + tabs + label + " = " + value + after;
            setFileContents(file, replaced);
        } else {
            writeLineUnder(file, heading, tabs + label + " = " + value);
        }
    }
    // GETTERS

    public static String getCategory(File state) {
        return getStringValue(state, "state_category");
    }

    public static int getPop(File state) {
        return getValue(state, "manpower");
    }

    public static int getInf(File state) {
        return getValue(state, "infrastructure");
    }
    public static int getCivs(File state) {
        return getValue(state, "industrial_complex");
    }
    public static int getMils(File state) {
        return getValue(state, "arms_factory");
    }
    public static int getAirfields(File state) {
        return getValue(state, "air_base");
    }
    public static int getDockyards(File state) {
        return getValue(state, "dockyard");
    }
    public static int getID(File state) {
        return getValue(state, "id");
    }
    // SETTERS

    public static void setCategory(File state, String value) {
        setStringValue(state, "state_category", value, 1, "state");
    }
    public static void setPop(File state, int value) {
        setValue(state, "manpower", value, 1, "state");
    }
    public static void setInf(File state, int value) {
        setValue(state, "infrastructure", value, 3, "buildings");
    }
    public static void setCivs(File state, int value) {
        setValue(state, "industrial_complex", value, 3, "buildings");
    }
    public static void setMils(File state, int value) {
        setValue(state, "arms_factory", value, 3, "buildings");
    }
    public static void setAirfields(File state, int value) {
        setValue(state, "air_base", value, 3, "buildings");
    }
    public static void setDockyards(File state, int value) {
        setValue(state, "dockyard", value, 3, "buildings");
    }


    public static ArrayList<Integer> onePopNewStates() {
        ArrayList<Integer> undone = new ArrayList<Integer>();
        for(int i = 1001; i <= NEWCOUNT; i++) {
            int pop = getPop(findFileFromStart("newstates", i));
            if (pop == 1) {
                undone.add(i);
            }
        }
        return undone;
    }

    public static void setFileContents(File file, String contents) {
        try {
            PrintWriter writer = new PrintWriter(file);
            writer.print(contents);
            writer.close();
        } catch (FileNotFoundException f) {
            System.out.println(file.getName() + " not found!");
        }
    }

    public static Province getProvinceFromID(ArrayList<Province> list, int id) {
        for(int i = 0; i < list.size(); i++) {
            Province c = list.get(i);
            if(c.id == id) {
                return c;
            }
        }
        return null;
    }

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
                        line = beforeWord(line, "#");
                    }
                    if(line.contains("true")) {
                        coastal.add(Integer.parseInt(beforeWord(line, ";")));
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

        ArrayList<Province> provinces = new ArrayList<Province>();

        for(int i = 0; i < olds.size(); i++) {
            State cur = olds.get(i);
            int pc = cur.provinces.length;
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
                provinces.add(new Province(cur.provinces[j], mp / pc, inf, civs, mils, dockyards, airfields, category));
            }
        }

        ArrayList<State> news = loadNewStates();
        for(int i = 0; i < news.size(); i++) {
            State curs = news.get(i);
            int[] provs = curs.provinces;
            int pop = 0;
            int infTotal = 0;
            int civs = 0;
            int mils = 0;
            int dockyards = 0;
            int airfields = 0;

            ArrayList<String> categories = new ArrayList<String>();

            File newFile = clone(curs.file, stateDir);
            String newName = newFile.getName();
            int newID = Integer.parseInt(newName.substring(0, newName.length() - 4));
            if(dothese.contains(newID)) {
                for(int j = 0; j < provs.length; j++) {
                    Province cprov = getProvinceFromID(provinces, provs[j]);
                    if(cprov == null) {
                        System.out.println("Province " + provs[j] + " in state " + curs.file.getName());
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
                int inf = infTotal / provs.length;
                System.out.println("Processing state " + newID + ".");
                setPop(newFile, pop);
                setInf(newFile, inf);
                setCivs(newFile, civs);
                setMils(newFile, mils);
                //setDockyards(newFile, dockyards);
                setAirfields(newFile, airfields);
                setCategory(newFile, getMostCommonString(categories));
            } else {
                System.out.println("Skipping state " + newID + ".");
            }

        }

    }

    public static void genLoc(String locFileName) throws IOException {
        ArrayList<State> olds = loadOldStates();

        ArrayList<Province> provinces = new ArrayList<Province>();

        ArrayList<String> names = readLoc();

        File newFile = new File(locFileName);
        newFile.createNewFile();


        String string = "";

        for(int i = 0; i < olds.size(); i++) {
            State cur = olds.get(i);
            for(int j = 0; j < cur.provinces.length; j++) {
                provinces.add(new Province(cur.provinces[j], names.get(i)));
            }
        }

        ArrayList<State> news = loadNewStates();
        for(int i = 0; i < news.size(); i++) {
            State curs = news.get(i);
            int[] provs = curs.provinces;

            ArrayList<String> newNames = new ArrayList<String>();

            String newName = curs.file.getName();

            int newID = Integer.parseInt(newName.substring(0, newName.length() - 4));

            for(int j = 0; j < provs.length; j++) {
                Province cprov = getProvinceFromID(provinces, provs[j]);
                if(cprov == null) {
                    System.out.println("Province " + provs[j] + " in state " + curs.file.getName());
                } else {
                    newNames.add(cprov.stateName);
                }
            }
            string = string + " STATE_" + newID + ":" + getMostCommonString(newNames) + "\n";

        }
        setFileContents(newFile, string);

    }



}
