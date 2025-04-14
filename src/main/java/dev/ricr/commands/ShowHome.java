package dev.ricr.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import dev.ricr.Config;
import dev.ricr.exceptions.HomeNotFoundException;
import dev.ricr.state.Homes;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

public class ShowHome {
    public static void register() {
        Config.LOGGER().info("Registering set_home command");

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("show_home").then(CommandManager.argument("key", StringArgumentType.word()).executes(commandContext -> {
                String key = StringArgumentType.getString(commandContext, "key");

                PlayerEntity player = commandContext.getSource().getPlayer();
                assert player != null;

                String playerName = player.getName().getString();
                String playerUUID = player.getUuid().toString();

                Homes.PlayerHomes playerHomes = Homes.INSTANCE.getPlayerHomes(playerName, playerUUID);
                Vec3d home;

                if ("all".equals(key)) {
                    if (playerHomes == null || playerHomes.homes.isEmpty()) {
                        commandContext.getSource().sendError(Text.literal("You have no homes to show."));
                        return 0;
                    }

                    commandContext.getSource().sendFeedback(() -> Text.literal("Your homes: §4%s".formatted(String.join("§r, §4", playerHomes.homes.keySet()))), false);
                    return 1;
                }

                try {
                    home = playerHomes.getHome(key);
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
