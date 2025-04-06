package dev.ricr.state;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import dev.ricr.Config;
import dev.ricr.exceptions.HomeNotFoundException;
import dev.ricr.exceptions.HomesFullException;
import net.minecraft.util.math.Vec3d;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

import java.io.File;

public class Homes {

    public static Homes INSTANCE;

    public Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public Map<String, PlayerHomes> playerHomesMap = new HashMap<>();

    public Homes(File homesFile) throws IOException {
        if (!homesFile.exists() && homesFile.createNewFile()) {
            Config.LOGGER().info("Homes file does not exist, creating it");
            this.write(homesFile);
        }

        playerHomesMap = gson.fromJson(new FileReader(homesFile), new TypeToken<Map<String, PlayerHomes>>() {
        }.getType());
    }

    public static void init(File homesFile) throws IOException {
        if (INSTANCE == null) {
            Config.LOGGER().info("Initializing Homes...");

            Homes.INSTANCE = new Homes(homesFile);

            Config.LOGGER().info("Homes initialized!");
            Config.LOGGER().info("There are {} player home sets", Homes.INSTANCE.playerHomesMap.size());
        }
    }

    public PlayerHomes getPlayerHomes(String playerName) {
        if (!playerHomesMap.containsKey(playerName)) {
            playerHomesMap.put(playerName, new PlayerHomes(playerName));
        }

        return playerHomesMap.get(playerName);
    }

    public void write(File homesFile) throws IOException {
        String json = gson.toJson(playerHomesMap, new TypeToken<Map<String, PlayerHomes>>() {
        }.getType());

        FileWriter writer = new FileWriter(homesFile);
        writer.write(json);
        writer.close();
    }

    public static class PlayerHomes {
        public final String playerName;
        public final Map<String, Vec3d> homes;

        public PlayerHomes(String playerName) {
            this.playerName = playerName;
            this.homes = new HashMap<>();
        }

        public void addHome(String name, Vec3d home) throws HomesFullException, IOException {
            Config.LOGGER().info("Adding home {} to {}", name, home);

            if (homes.get(name) == null && homes.size() == 5) {
                throw new HomesFullException("player %s already has 5 homes".formatted(this.playerName));
            }

            homes.put(name, home);

            Config.LOGGER().info("Home {} added to {}.", name, home);

            Homes.INSTANCE.write(new File("homes.json"));
        }

        public Vec3d getHome(String name) throws HomeNotFoundException {
            Vec3d home = homes.get(name);

            if (home == null) {
                throw new HomeNotFoundException("Player %s does not have a home with the name %s".formatted(this.playerName, name));
            }

            return homes.get(name);
        }
    }
}
