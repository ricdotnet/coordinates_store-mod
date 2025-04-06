package dev.ricr;

import dev.ricr.commands.SetHome;
import dev.ricr.commands.ShowHome;
import dev.ricr.state.Homes;
import net.fabricmc.api.ModInitializer;

import java.io.File;
import java.io.IOException;

public class CoordinatesStoreMod implements ModInitializer {
    public static final String MOD_ID = "coordinates-store-mod";

    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        Config.LOGGER().info("Setting up coordinates store mod");

        try {
            Homes.init(new File("homes.json"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        new SetHome();
        new ShowHome();
    }
}