package sg;

import sg.obj.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Array;
import java.util.*;

public class HubFresh {

    public static ArrayList<State> statesFromFiles(File[] files) {
        ArrayList<State> states = new ArrayList<>();
        for (File file : files) {
            states.add(new State(file));
        }
        return states;
    }

    public static HashMap<Integer, ProvinceDefinition> getProvinceDefinitionsFromFile(File file) {
        HashMap<Integer, ProvinceDefinition> definitions = new HashMap<>();
        String content = FileScripts.readFile(file);
        String[] entries = content.split("\n");
        for (String entry : entries) {
            ProvinceDefinition definition = new ProvinceDefinition(entry);
            int id = definition.id;
            definitions.put(id, definition);
        }
        return definitions;
    }

    public static int keyOfLargestDouble(HashMap<Integer, Double> map) {
        Map.Entry<Integer, Double> maxEntry = null;

        for(Map.Entry<Integer, Double> entry : map.entrySet()) {
            if(maxEntry == null || entry.getValue() > maxEntry.getValue()) {
                maxEntry = entry;
            }
        }
        return maxEntry.getKey();
    }

    public static int keyOfSmallestDouble(HashMap<Integer, Double> map) {
        Map.Entry<Integer, Double> minEntry = null;

        for(Map.Entry<Integer, Double> entry : map.entrySet()) {
            if(minEntry == null || entry.getValue() < minEntry.getValue()) {
                minEntry = entry;
            }
        }
        return minEntry.getKey();
    }

    public static HashMap<Integer, Integer> distributeIntegersByWeight(int totalCount, HashMap<Integer, Integer> weights) {
        HashMap<Integer, Integer> weightedValuesRounded = new HashMap<>();
        HashMap<Integer, Double> roundDifferences = new HashMap<>();
        int totalWeight = 0;
        for (int weight : weights.values()) {
            totalWeight += weight;
        }
        double totalWeightDouble = totalWeight;
        double totalCountDouble = totalCount;

        int totalDistributed = 0;

        for(int id : weights.keySet()) {
            int weight = weights.get(id);
            double weightDouble = weight;
            double weightedValue = totalCountDouble * weightDouble / totalWeightDouble;
            int weightedValueRounded = (int) Math.round(weightedValue);

            totalDistributed += weightedValueRounded;
            weightedValuesRounded.put(id, weightedValueRounded);

            double roundDifference = weightedValue - (double) weightedValueRounded ;

            roundDifferences.put(id, roundDifference);

        }
        while(totalDistributed != totalCount) {
            if (totalDistributed > totalCount) {
                int key = keyOfSmallestDouble(roundDifferences);

                int previousRoundedValue = weightedValuesRounded.get(key);
                weightedValuesRounded.put(key, previousRoundedValue - 1);

                double previousRoundDifference = roundDifferences.get(key);
                roundDifferences.put(key, previousRoundDifference + 1);

                totalDistributed -= 1;

                // to round back down: largest negative dif: lowest dif

            } else { // totalDistributed < totalCount
                int key = keyOfLargestDouble(roundDifferences);

                int previousRoundedValue = weightedValuesRounded.get(key);
                weightedValuesRounded.put(key, previousRoundedValue + 1);

                double previousRoundDifference = roundDifferences.get(key);
                roundDifferences.put(key, previousRoundDifference - 1);
                totalDistributed += 1;
                // to round back up: largest positive dif: highest dif

            }
        }
        return weightedValuesRounded;
    }

    public static void stateTest(String outDirectory) {
        File[] oldFiles = FileScripts.getDirectoryFiles(Paths.HOI_STATES);
        System.out.println("Loaded old files.");

        ArrayList<State> oldStates = statesFromFiles(oldFiles);
        System.out.println("Loaded old states.");

        for(State old : oldStates) {
            File newFile = FileScripts.createFile(outDirectory + "/" + old.id + ".txt");
            WritingScripts.writeStateContent(newFile, old);
            System.out.println("File made.");
        }
    }
    public static ArrayList<State> sortStatesByID(ArrayList<State> states) {
        states.sort(new Comparator<State>() {
            @Override
            public int compare(State o1, State o2) {
                return o1.id - o2.id;
            }
        });
        return states;
    }

    public static HashMap<String, SimpleColor> colors() {
        HashMap<String, SimpleColor> map = new HashMap<>();
        try (Scanner sc = new Scanner(new File(Paths.DIV_COLORS))) {
            int line = 0;
            String curTag = null;
            while (sc.hasNextLine()) {
                String next = sc.nextLine();
                line++;
                if ((line - 1) % 4 == 0) {
                    curTag = ParsingScripts.beforeWord(next, " =");
                    System.out.println("TAG: " + curTag);
                }
                if ((line - 2) % 4 == 0) {
                    String color = ParsingScripts.beforeWord(ParsingScripts.afterWord(next, "rgb { "), " }");
                    String[] vals = color.split(" ");
                    if (vals.length != 3) {
                        System.out.println("Error! Not 3 length color.");
                    } else {
                        map.put(curTag, new SimpleColor(Integer.parseInt(vals[0]), Integer.parseInt(vals[1]), Integer.parseInt(vals[2])));
                    }
                    System.out.println("COLOR for " + curTag + ": " + color);
                }
            }
        } catch (IOException e) {
            System.out.println("Error replacing in file.");
        }
        return map;
    }

    public static void updateAllColors() {

    }

    public static void makeCountries() throws IOException {
        HashMap<String, SimpleColor> colors = colors();

        File colorFile = new File(Paths.DESKTOP_FOLDER + "/colors.txt");
        colorFile.createNewFile();
        try (Scanner sc = new Scanner(new File(Paths.CLR_TAGS)); FileWriter colorWriter = new FileWriter(colorFile);) {
            while (sc.hasNextLine()) {
                String next = sc.nextLine();
                if (next.contains("\"")) {
                    String tag = next.substring(0, 3);
                    SimpleColor colorO = colors.get(tag);

                    if(colorO == null) {
                        System.out.println(tag + " has no color.");
                    } else {
                        String color = colorO.toString();
                        colorWriter.write(tag + " = {\n\tcolor = rgb { " + color + " }\n\tcolor_ui = rgb { " + color + " }\n}\n");

                        String filePath = ParsingScripts.beforeWord(ParsingScripts.afterWord(next,"\""), "\"");
                        File countryFile = new File(Paths.DESKTOP_FOLDER + "/" + filePath);
                        //System.out.println(Paths.DESKTOP_FOLDER + "/" + filePath);
                        FileWriter countryWriter = new FileWriter(countryFile);
                        System.out.println(Paths.DIV_PATH + "/common/" + filePath);
                        File oldFile = new File(Paths.CLR_PATH + "/common/" + filePath);
                        String content = FileScripts.readFile(oldFile);
                        String[] contentSplit = content.split("\n");
                        countryWriter.write(contentSplit[0] + "\n");
                        countryWriter.write(contentSplit[1] + "\n");
                        countryWriter.write("color = { " + color + " }");
                        countryWriter.close();
                    }
                } else {
                    colorWriter.write(next + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //public static int findValidCapital(String tag, List<State> states) {
    //    for(State state : states) {
    //
    //    }
    //}

    public static void makeHistoryCountries() throws FileNotFoundException {
        File[] newFiles = FileScripts.getDirectoryFiles(Paths.CLR_STATES);
        ArrayList<State> newStates = statesFromFiles(newFiles);
        try (Scanner sc = new Scanner(new File(Paths.CLR_TAGS))) {
            while (sc.hasNextLine()) {
                String next = sc.nextLine();
                if (next.contains("\"")) {
                    String tag = next.substring(0, 3);
                    String countryName = ParsingScripts.afterWord(ParsingScripts.beforeWord(ParsingScripts.afterWord(next, "\""), "\""), "countries/");
                    String fileName = tag + " - " + countryName;
                    File file = new File(Paths.DESKTOP_FOLDER + "/history/" + fileName);
                    FileWriter writer = new FileWriter(file);
                    writer.write("capital = 1\n" +
                            "\n" +
                            "set_technology = {\n" +
                            "\tinfantry_weapons = 1\n" +
                            "\ttech_support = 1\n" +
                            "\ttech_recon = 1\n" +
                            "}\n" +
                            "\n" +
                            "set_politics = {\n" +
                            "\truling_party = neutrality\n" +
                            "\tlast_election = \"1934.6.13\"\n" +
                            "\telection_frequency = 24\n" +
                            "\telections_allowed = no\n" +
                            "}\n" +
                            "\n" +
                            "set_popularities = {\n" +
                            "\tdemocratic = 25\n" +
                            "\tfascism = 25\n" +
                            "\tcommunism = 25\n" +
                            "\tneutrality = 25\n" +
                            "}\n" +
                            "\n" +
                            "set_convoys = 5");
                    writer.close();
                }

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

        /**
     *  For each state in oldDirectory, splits up all the content of the states among its provinces.
     *  In newDirectory, gets the provinces of the states, and adds up the content given to them from the old states.
     *  In finalDirectory, places the final content-filled versions of the new states.
     */
    public static void provincialRepopulation(String outDirectory) {
        File[] oldFiles = FileScripts.getDirectoryFiles(Paths.HOI_STATES);
        System.out.println("Loaded old files.");
        File[] newFiles = FileScripts.getDirectoryFiles(Paths.DIV_STATES);
        System.out.println("Loaded new files.");
        File definitionsFile = FileScripts.getFile(Paths.HOI_DEFINITIONS_CSV);
        System.out.println("Loaded definitions files.");

        ArrayList<State> oldStates = statesFromFiles(oldFiles);
        System.out.println("Loaded old states.");
        ArrayList<State> newStates = statesFromFiles(newFiles);
        System.out.println("Loaded new states.");
        HashMap<Integer, ProvinceDefinition> definitions = getProvinceDefinitionsFromFile(definitionsFile);
        System.out.println("Created definitions.");

        HashMap<Integer, ProvinceContent> populatedProvinces = new HashMap<>();

        // desert, forest, hills, jungle, marsh, mountain, plains, urban, lakes, ocean
        // 1       4       3      2       2      2         4       10     0      0
        HashMap<String, Integer> terrainWeights = new HashMap<>();
        terrainWeights.put("desert", 1);
        terrainWeights.put("forest", 4);
        terrainWeights.put("hills", 3);
        terrainWeights.put("jungle", 2);
        terrainWeights.put("marsh", 2);
        terrainWeights.put("mountain", 2);
        terrainWeights.put("plains", 4);
        terrainWeights.put("urban", 7);
        terrainWeights.put("lakes", 0);
        terrainWeights.put("ocean", 0);

        HashMap<String, Integer> categorySlots = new HashMap<>();
        categorySlots.put("megalopolis", 12);
        categorySlots.put("metropolis", 10);
        categorySlots.put("large_city", 8);
        categorySlots.put("city", 6);
        categorySlots.put("large_town", 5);
        categorySlots.put("town", 4);
        categorySlots.put("rural", 2);
        categorySlots.put("pastoral", 1);
        categorySlots.put("small_island", 1);
        categorySlots.put("enclave", 0);
        categorySlots.put("tiny_island", 0);
        categorySlots.put("wasteland", 0);

        for (State oldState : oldStates) {
            System.out.println("Organizing old state: " + oldState.id);
            ArrayList<Integer> stateProvinces = oldState.provinces;

            int manpower = oldState.manpower;
            int inf = oldState.inf;
            int civs = oldState.civs;
            int mils = oldState.mils;
            int dockyards = oldState.dockyards;
            int airfields = oldState.airfields;
            int steel = oldState.steel;
            int aluminum = oldState.aluminum;
            int rubber = oldState.rubber;
            int tungsten = oldState.tungsten;
            int chromium = oldState.chromium;
            int oil = oldState.oil;

            ArrayList<ProvinceBuildings> oldPBs = oldState.pbuilds;

            int supplies = oldState.supplies;
            String category = oldState.category;
            HashMap<Integer, Integer> vps = oldState.vps;

            HashMap<Integer, Integer> developmentWeights = new HashMap<>();
            HashMap<Integer, Integer> maxWeights = new HashMap<>();
            HashMap<Integer, Integer> coastalWeights = new HashMap<>();
            HashMap<Integer, Integer> evenWeights = new HashMap<>();

            // inf and supply averaged across states

            // Calculate weights for: manpower
            // development weights
            // weights based on terrain, whether its coastal, and its vp points
            for (int id : stateProvinces) {
                ProvinceDefinition definition = definitions.get(id);
                int terrainWeight = terrainWeights.get(definition.terrain);
                int coastalWeight = (definition.coastal ? 2 : 1);
                int vpWeight = vps.getOrDefault(id, 1);
                int weight = (int) Math.ceil((double)(terrainWeight * coastalWeight * vpWeight) / 4);
                developmentWeights.put(id, weight);
            }

            // Calculate weights for: nothing right now
            // max weights
            // 0 for everything but the single province with the highest development weight
            Map.Entry<Integer, Integer> maxEntry = null;
            for (Map.Entry<Integer, Integer> entry : developmentWeights.entrySet()) {
                maxWeights.put(entry.getKey(), 0);
                if (maxEntry == null || entry.getValue() > maxEntry.getValue()) {
                    maxEntry = entry;
                }
            }
            assert maxEntry != null;
            maxWeights.put(maxEntry.getKey(), 1);



            // Calculate weights for: steel, aluminum, rubber, tungsten, chromium, oil
            // even weights, civs, mils, airfields
            // every province with a weight of 1
            for (int id : stateProvinces) {
                evenWeights.put(id, 1);
            }

            // Calculate weights for: dockyards
            // coastal weights
            // development weights but 0 for all that aren't coastal
            for (int id : stateProvinces) {
                ProvinceDefinition definition = definitions.get(id);
                //int weight = developmentWeights.get(id) * (definition.coastal ? 1 : 0);
                int weight = definition.coastal ? 1 : 0;
                coastalWeights.put(id, weight);
            }

            // DEVELOPMENT WEIGHTS

            HashMap<Integer, Integer> manpowerCounts = distributeIntegersByWeight(manpower, developmentWeights);
            HashMap<Integer, Integer> civCounts = distributeIntegersByWeight(civs, evenWeights);
            HashMap<Integer, Integer> milCounts = distributeIntegersByWeight(mils, evenWeights);
            // MAX WEIGHTS
            HashMap<Integer, Integer> airfieldCounts = distributeIntegersByWeight(airfields, evenWeights);
            // COASTAL WEIGHTS
            HashMap<Integer, Integer> dockCounts = distributeIntegersByWeight(dockyards, coastalWeights);
            // EVEN WEIGHTS
            HashMap<Integer, Integer> steelCounts = distributeIntegersByWeight(steel, evenWeights);
            HashMap<Integer, Integer> aluminumCounts = distributeIntegersByWeight(aluminum, evenWeights);
            HashMap<Integer, Integer> rubberCounts = distributeIntegersByWeight(rubber, evenWeights);
            HashMap<Integer, Integer> tungstenCounts = distributeIntegersByWeight(tungsten, evenWeights);
            HashMap<Integer, Integer> chromiumCounts = distributeIntegersByWeight(chromium, evenWeights);
            HashMap<Integer, Integer> oilCounts = distributeIntegersByWeight(oil, evenWeights);

            for (int id : stateProvinces) {
                ProvinceContent content = new ProvinceContent(id);
                content.pop = manpowerCounts.get(id);
                content.civs = civCounts.get(id);
                content.mils = milCounts.get(id);
                content.airfields = airfieldCounts.get(id);
                content.dockyards = dockCounts.get(id);
                content.steel = steelCounts.get(id);
                content.aluminum = aluminumCounts.get(id);
                content.rubber = rubberCounts.get(id);
                content.tungsten = tungstenCounts.get(id);
                content.chromium = chromiumCounts.get(id);
                content.oil = oilCounts.get(id);
                content.category = category;
                content.vpPoints = vps.getOrDefault(id, 0);
                content.supplies = oldState.supplies;
                content.inf = oldState.inf;

                populatedProvinces.put(id, content);
            }

            for(ProvinceBuildings pb : oldPBs) {
                int id = pb.province;
                populatedProvinces.get(id).dockbases = pb.bases;
            }
        }
        int finalID = 1;

        newStates = sortStatesByID(newStates);
        for (State newState : newStates) {
            //System.out.println("Checking new state: " + newState.id);
            ArrayList<Integer> provinces = newState.provinces;

            if(provinces.size() == 1 && provinces.get(0) == 1) {
                System.out.println("Skipping state " + newState.id + ". Dead sea empty state.");

                continue;

            }
            //System.out.println("Creating state. " + "Unpopulated state " + newState.id + " will correspond to populated state " + finalID + ".");

            int infSum = 0;
            int infAvg;

            int supplySum = 0;
            int supplyAvg;

            int popSum = 0;
            int civSum = 0;
            int milSum = 0;
            int dockSum = 0;
            int airSum = 0;

            int refineries = 0;
            int reactors = 0;
            int antiairs = 0;
            int silos = 0;
            int radars = 0;
            int rocketsites = 0;

            String categoryMode;
            HashMap<Integer, Integer> vpsAll = new HashMap<>();
            ArrayList<ProvinceBuildings> pbs = new ArrayList<>();

            int oilSum = 0;
            int aluminumSum = 0;
            int rubberSum = 0;
            int tungstenSum = 0;
            int steelSum = 0;
            int chromiumSum = 0;

            boolean impassable = newState.impassable;
            List<String> cored = newState.cored;
            String owner = newState.owner;


            ArrayList<String> categories = new ArrayList<>();

            for (int id : provinces) {
                ProvinceContent content = populatedProvinces.get(id);
                popSum += content.pop;
                infSum += content.inf;
                supplySum += content.supplies;
                civSum += content.civs;
                milSum += content.mils;
                dockSum += content.dockyards;
                airSum += content.airfields;
                categories.add(content.category);
                if (content.vpPoints > 0) {
                    vpsAll.put(id, content.vpPoints);
                }
                if (content.dockbases > 0) {
                    pbs.add(new ProvinceBuildings(id, content.dockbases, 0 ,0));
                }
                oilSum += content.oil;
                aluminumSum += content.aluminum;
                rubberSum += content.rubber;
                tungstenSum += content.tungsten;
                steelSum += content.steel;
                chromiumSum += content.chromium;

            }
            infAvg = infSum / provinces.size();
            supplyAvg = supplySum / provinces.size();

            //System.out.println(infSum + " " + infAvg);
            categoryMode = UtilScripts.getMostCommonString(categories).replace("\"", "");

            // for testing
            //cored = new ArrayList<>();
           // owner = "ENG";

            if(airSum > 10) {
                airSum = 10;
                System.out.println("Too much air!");
            }

            if(milSum > 20) {
                System.out.println("Too many mils");
                milSum = 20;
            }

            if(civSum > 20) {
                System.out.println("Too many civs");
                civSum = 20;
            }

            int slotBuildTotal = civSum + milSum + dockSum;
            //System.out.println(slotBuildTotal);
            int max = categorySlots.get(categoryMode);
            if (!cored.contains(owner)) {
                max /= 2;
            }
            if(slotBuildTotal > max) {
                double factorOff = (double) max / slotBuildTotal;
                civSum = (int) (civSum * factorOff);
                milSum = (int) (milSum * factorOff);
                dockSum = (int) (dockSum * factorOff);
                System.out.println("More than category allows for state " + finalID + "!");

            }


            State finalState = new State(provinces, popSum, infAvg, civSum, milSum, dockSum, airSum, refineries, reactors, antiairs, silos, radars, rocketsites, categoryMode, vpsAll, pbs, oilSum, aluminumSum, rubberSum, tungstenSum, steelSum, chromiumSum, impassable, cored, owner, supplyAvg);

            finalState.id = finalID;
            finalID += 1;

            File newFile = FileScripts.createFile(outDirectory + "/" + finalState.id + ".txt");
            WritingScripts.writeStateContent(newFile, finalState);
            //System.out.println("File made.");

        }
    }
}

