package dev.ricr.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import dev.ricr.Config;
import dev.ricr.exceptions.HomeNotFoundException;
import dev.ricr.state.Homes;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

public class ShowHome {
    static {
        Config.LOGGER().info("Registering set_home command");

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("show_home").then(CommandManager.argument("key", StringArgumentType.word()).executes(commandContext -> {
                String key = StringArgumentType.getString(commandContext, "key");
                String playerName = commandContext.getSource().getName();

                Vec3d home;

                try {
                    home = Homes.INSTANCE.getPlayerHomes(playerName).getHome(key);
                } catch (HomeNotFoundException e) {
                    commandContext.getSource().sendFeedback(() -> Text.literal("Home §4%s§r not found! Use §2/set_home <name>§r to set a home.".formatted(key)), false);
                    return 0;
                }

                commandContext.getSource().sendFeedback(() -> Text.literal("§4%s§r §6XYZ:§r %.0f %.0f %.0f".formatted(key, home.x, home.y, home.z)), false);

                return 1;
            })));
        });
    }
}
