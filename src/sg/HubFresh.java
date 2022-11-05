package sg;

import sg.obj.*;

import java.io.File;
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
        terrainWeights.put("urban", 10);
        terrainWeights.put("lakes", 0);
        terrainWeights.put("ocean", 0);

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
            String category = oldState.category;
            HashMap<Integer, Integer> vps = oldState.vps;

            HashMap<Integer, Integer> developmentWeights = new HashMap<>();
            HashMap<Integer, Integer> maxWeights = new HashMap<>();
            HashMap<Integer, Integer> coastalWeights = new HashMap<>();
            HashMap<Integer, Integer> evenWeights = new HashMap<>();

            // Calculate weights for: manpower, inf, civs, mils
            for (int id : stateProvinces) {
                ProvinceDefinition definition = definitions.get(id);
                int terrainWeight = terrainWeights.get(definition.terrain);
                int coastalWeight = (definition.coastal ? 2 : 1);
                int vpWeight = (vps.containsKey(id) ? vps.get(id) / 2 : 1);
                int weight = terrainWeight * coastalWeight * vpWeight;
                developmentWeights.put(id, weight);
            }

            // Calculate weights for: airfields
            Map.Entry<Integer, Integer> maxEntry = null;
            for (Map.Entry<Integer, Integer> entry : developmentWeights.entrySet()) {
                maxWeights.put(entry.getKey(), 0);
                if (maxEntry == null || entry.getValue() > maxEntry.getValue()) {
                    maxEntry = entry;
                }
            }
            maxWeights.put(maxEntry.getKey(), 1);

            // Calculate weights for: dockyards
            for (int id : stateProvinces) {
                ProvinceDefinition definition = definitions.get(id);
                int weight = terrainWeights.get(definition.terrain) * (definition.coastal ? 2 : 0) * (vps.containsKey(id) ? vps.get(id) / 2 : 1);
                coastalWeights.put(id, weight);
            }

            // Calculate weights for: steel, aluminum, rubber, tungsten, chromium, oil
            for (int id : stateProvinces) {
                evenWeights.put(id, 1);
            }

            // DEVELOPMENT WEIGHTS
            HashMap<Integer, Integer> manpowerCounts = distributeIntegersByWeight(manpower, developmentWeights);
            HashMap<Integer, Integer> infCounts = distributeIntegersByWeight(inf, developmentWeights);
            HashMap<Integer, Integer> civCounts = distributeIntegersByWeight(civs, developmentWeights);
            HashMap<Integer, Integer> milCounts = distributeIntegersByWeight(mils, developmentWeights);
            // MAX WEIGHTS
            HashMap<Integer, Integer> airfieldCounts = distributeIntegersByWeight(airfields, maxWeights);
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
                content.inf = infCounts.get(id);
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
                content.vpPoints = vps.containsKey(id) ? vps.get(id) : 0;
                populatedProvinces.put(id, content);
            }
        }

        for (State newState : newStates) {
            System.out.println("Populating new state: " + newState.id);
            ArrayList<Integer> provinces = newState.provinces;

            int popSum = 0;
            int infAvg = 0;
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
            ArrayList<String> cored = newState.cored;
            String owner = newState.owner;
            System.out.println(owner);

            int infSum = 0;
            ArrayList<String> categories = new ArrayList<>();

            for (int id : provinces) {
                ProvinceContent content = populatedProvinces.get(id);
                popSum += content.pop;
                infSum += content.inf;
                civSum += content.civs;
                milSum += content.mils;
                dockSum += content.dockyards;
                airSum += content.airfields;
                categories.add(content.category);
                if (content.vpPoints > 0) {
                    vpsAll.put(id, content.vpPoints);
                }
                oilSum = content.oil;
                aluminumSum = content.aluminum;
                rubberSum = content.rubber;
                tungstenSum = content.tungsten;
                steelSum = 0;
                chromiumSum = 0;

            }

            infAvg = infSum / provinces.size();
            categoryMode = UtilScripts.getMostCommonString(categories);

            State finalState = new State(provinces, popSum, infAvg, civSum, milSum, dockSum, airSum, refineries, reactors, antiairs, silos, radars, rocketsites, categoryMode, vpsAll, pbs, oilSum, aluminumSum, rubberSum, tungstenSum, steelSum, chromiumSum, impassable, cored, owner);

            File newFile = FileScripts.createFile(outDirectory + "/" + newState.id + ".txt");
            WritingScripts.writeStateContent(newFile, finalState);

        }
    }
}

