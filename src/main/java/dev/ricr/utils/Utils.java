package dev.ricr.utils;

import net.minecraft.world.World;

public class Utils {

    public static boolean isOverworld(World world) {
        return world.getRegistryKey() == World.OVERWORLD;
    }

}
