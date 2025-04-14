package dev.ricr.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import dev.ricr.Config;
import dev.ricr.Constants;
import dev.ricr.state.Homes;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

public class Teleport {

    public static void register() {
        Config.LOGGER().info("Registering teleport command");

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("telep").then(CommandManager.argument("key", StringArgumentType.word()).executes(commandContext -> {
                String key = StringArgumentType.getString(commandContext, "key");

                ServerPlayerEntity player = commandContext.getSource().getPlayer();
                assert player != null;

                String playerName = player.getName().getString();
                String playerUUID = player.getUuid().toString();

                if (!player.getInventory().contains(new ItemStack(Items.DIAMOND))) {
                    commandContext.getSource().sendError(Text.literal("You have no diamonds to pay for teleportation!"));
                    return 0;
                }

                Homes.PlayerHomes playerHomes = Homes.INSTANCE.getPlayerHomes(playerName, playerUUID);
                Vec3d home = playerHomes.getHome(key);

                if (home == null) {
                    commandContext.getSource().sendError(Text.literal("You cannot teleport to a home you did not set!"));
                    return 0;
                }

                int holdingDiamondCount = player.getInventory().count(Items.DIAMOND);
                if (holdingDiamondCount < Constants.TP_HOME_DIAMOND_COST) {
                    commandContext.getSource().sendError(Text.literal("You do not have enough diamonds to pay for teleportation!"));
                    return 0;
                }

                if (player instanceof ServerPlayerEntity) {
                    Config.LOGGER().info("Teleporting to {}", playerHomes.getHome(key));
                    player.teleport(commandContext.getSource().getWorld(), home.getX() + 0.5, home.getY() + 0.1, home.getZ() + 0.5, PositionFlag.getFlags(0), player.getYaw(), player.getPitch(), true);

                    player.getInventory().remove((itemStack) -> itemStack.getItem().equals(Items.DIAMOND), Constants.TP_HOME_DIAMOND_COST, player.getInventory());
                }

                return 1;
            })));
        });
    }
}
