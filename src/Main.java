import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import sector.ForestSector;
import sector.SectorInterface;
import sector.SectorState;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {

    private static final String CONFIG_PATH = "./params/config.json";

    // X_SIZE -> l ; Y_SIZE -> h ; NB_MAX_STEP -> to limit infinite loop ; CHANCE_TO_IGNITE -> from 0 to 1
    private static int X_SIZE, Y_SIZE, NB_MAX_STEP;
    private static double CHANCE_TO_IGNITE;
    private static List<SectorInterface> sectorInFires = new ArrayList<>();
    private static List<List<Integer>> initialFire = new ArrayList<>();
    private static List<List<SectorInterface>> forest;

    public static void main(String[] args) {

        if (!initParam()) return;
        initForest();
        fillNeighbors();

        for (List<Integer> coords : initialFire) {
            SectorInterface sector = forest.get(coords.get(1)).get(coords.get(0));
            if (sector.ignite()) sectorInFires.add(sector);
        }

        System.out.println("initial step");
        printForestColor(forest);
        loop();
    }

    /**
     * print state label of the entire forest
     *
     * @param forest the forest
     */
    public static void printForest(List<List<SectorInterface>> forest) {
        forest.forEach(row -> {
            String rowStr = row.stream().map(sectorInterface -> sectorInterface.getState().label).collect(Collectors.joining(" "));
            System.out.println(rowStr);
        });
    }

    /**
     * print state label of the entire forest with color code
     *
     * @param forest the forest
     */
    public static void printForestColor(List<List<SectorInterface>> forest) {
        forest.forEach(row -> {
            String rowStr = row.stream().map(sectorInterface -> sectorInterface.getState().GetColorLabel()).collect(Collectors.joining(" "));
            System.out.println(rowStr);
        });
    }

    /**
     * print position of each sector in the forest (for debug)
     *
     * @param forest the forest
     */
    public static void printSectorPosition(List<List<SectorInterface>> forest) {
        forest.forEach(row -> {
            String rowStr = row.stream().map(sectorInterface -> sectorInterface.getXPosition() + "-" + sectorInterface.getYPosition())
                    .collect(Collectors.joining(" "));
            System.out.println(rowStr);
        });
    }

    /**
     * initialize parameters from file CONFIG_PATH. Check the validity (out of bounds, number of parameters, file existence, ...)
     *
     * @return False if something is invalid, True otherwise
     */
    public static boolean initParam() {
        try {
            File f = new File(CONFIG_PATH);
            if (f.exists()) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode json = objectMapper.readTree(f);

                // Check if config file is complete
                if (json.has("x_size") && json.has("y_size") && json.has("nb_max_step")
                        && json.has("chance_to_ignite") && json.has("initials_fires")) {
                    X_SIZE = json.get("x_size").asInt();
                    Y_SIZE = json.get("y_size").asInt();
                    NB_MAX_STEP = json.get("nb_max_step").asInt();
                    CHANCE_TO_IGNITE = json.get("chance_to_ignite").asDouble();

                    JsonNode coordsArray = json.get("initials_fires");
                    // check if there is at least one fire
                    if (coordsArray.isEmpty()) {
                        System.out.println("no initial fire");
                        return false;
                    }
                    for (int i = 0; i < coordsArray.size(); i++) {
                        JsonNode coords = coordsArray.get(i);
                        // check if every coordinate is a couple and if they are inside the limits
                        if (coords.size() != 2
                                || coords.get(0).asInt() >= X_SIZE || coords.get(0).asInt() < 0
                                || coords.get(1).asInt() >= Y_SIZE || coords.get(1).asInt() < 0) {
                            System.out.println("invalid coords for initial fire");
                            return false;
                        }
                        initialFire.add(Arrays.asList(coords.get(0).asInt(), coords.get(1).asInt()));
                    }

                } else {
                    System.out.println("missing configs");
                    return false;
                }
            } else {
                System.out.println("no config file");
                return false;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * init forest grid with size en default sector
     */
    public static void initForest() {
        forest = IntStream.range(0, Y_SIZE).
                mapToObj(y -> IntStream.range(0, X_SIZE)
                        .<SectorInterface>mapToObj(x -> new ForestSector(x, y, SectorState.NORMAL))
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

    /**
     * set neighbors for each sector
     */
    public static void fillNeighbors() {
        for (int x = 0; x < X_SIZE; x++) {
            for (int y = 0; y < Y_SIZE; y++) {
                List<SectorInterface> toAdd = new ArrayList<>();
                if (x > 0) toAdd.add(forest.get(y).get(x - 1)); // left
                if (x < X_SIZE - 1) toAdd.add(forest.get(y).get(x + 1)); // right
                if (y > 0) toAdd.add(forest.get(y - 1).get(x)); // up
                if (y < Y_SIZE - 1) toAdd.add(forest.get(y + 1).get(x)); // down
                forest.get(y).get(x).addNeighbors(toAdd);
            }
        }
    }

    /**
     * main loop
     * each iteration = 1 step
     */
    public static void loop() {
        for (int step = 0; step < NB_MAX_STEP; step++) {
            List<SectorInterface> newFire = new ArrayList<>();
            for (SectorInterface sectorInFire : sectorInFires) {
                // if extinguish failed, sector is still in fire for next step
                if (!sectorInFire.extinguish()) newFire.add(sectorInFire);
                for (SectorInterface neighbor : sectorInFire.getNeighbors()) {
                    if (neighbor.canBeIgnite() && Math.random() < CHANCE_TO_IGNITE) {
                        if (neighbor.ignite()) newFire.add(neighbor);
                    }
                }
            }
            System.out.println("--------------------------------");
            System.out.println("step : " + step);
            printForestColor(forest);
            sectorInFires = newFire;
            if (sectorInFires.isEmpty()) break;
        }
    }
}