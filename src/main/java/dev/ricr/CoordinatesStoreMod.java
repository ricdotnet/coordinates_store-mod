package dev.ricr;

import dev.ricr.commands.SetHome;
import dev.ricr.commands.ShowHome;
import dev.ricr.commands.Teleport;
import dev.ricr.state.Homes;
import net.fabricmc.api.ModInitializer;

import java.io.File;
import java.io.IOException;

public class CoordinatesStoreMod implements ModInitializer {
    @Override
    public void onInitialize() {
        Config.LOGGER().info("Setting up coordinates store mod");

        try {
            Homes.init(new File("homes.json"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // register commands
        SetHome.register();
        ShowHome.register();
        Teleport.register();
    }
}