package dev.ricr.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import dev.ricr.Config;
import dev.ricr.Constants;
import dev.ricr.exceptions.HomesFullException;
import dev.ricr.state.Homes;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

import java.io.IOException;
import java.util.Objects;

public class SetHome {

    public static void register() {
        Config.LOGGER().info("Registering set_home command");

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("set_home").then(CommandManager.argument("key", StringArgumentType.word()).executes(commandContext -> {
                String key = StringArgumentType.getString(commandContext, "key");

                if ("all".equals(key)) {
                    commandContext.getSource().sendError(Text.literal("That home name is not allowed"));
                    return 0;
                }

                PlayerEntity player = commandContext.getSource().getPlayer();
                assert player != null;

                String playerName = player.getName().getString();
                String playerUUID = player.getUuid().toString();

                int expPoints = player.experienceLevel;
                if (expPoints < Constants.SET_HOME_XP_COST) {
                    commandContext.getSource().sendError(Text.literal("You need at least " + Constants.SET_HOME_XP_COST + " experience to set a home"));
                    return 0;
                }

                Vec3d entityPos = Objects.requireNonNull(commandContext.getSource().getEntity()).getPos();

                Config.LOGGER().info("Home {} with coordinates x {}, y {} and z {} set.", key, entityPos.x, entityPos.y, entityPos.z);
                try {
                    Homes.INSTANCE.getPlayerHomes(playerName, playerUUID).addHome(key, entityPos);
                } catch (HomesFullException e) {
                    commandContext.getSource().sendFeedback(() -> Text.literal("You already have 5 homes. Use §2/set_home <existing_home>§r to replace one of your existing homes"), false);
                    return 0;
                } catch (IOException e) {
                    // ignore
                }

                commandContext.getSource().sendFeedback(() -> Text.literal("Home §4%s§r has been set. Use §2/show_home %s§r to see its coordinates".formatted(key, key)), false);
                // Cost 3 exp points to save coordinates
                player.addExperienceLevels(-Constants.SET_HOME_XP_COST);

                return 1;
            })));
        });
    }
}
