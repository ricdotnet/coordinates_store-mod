package dev.ricr;

import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Config {
    public static Logger LOGGER() {
        return LoggerFactory.getLogger(Constants.MOD_ID);
    }

    public static void dropItem(World world, BlockPos pos, Direction direction, ItemStack stack) {
        double x = pos.getX() + 0.5 + direction.getOffsetX() * 0.7;
        double y = pos.getY() + 0.5 + direction.getOffsetY() * 0.7;
        double z = pos.getZ() + 0.5 + direction.getOffsetZ() * 0.7;

        ItemEntity itemEntity = new ItemEntity(world, x, y, z, stack.copy());
        itemEntity.setVelocity(
                direction.getOffsetX() * 0.1,
                direction.getOffsetY() * 0.1 + 0.1, // slight upward motion
                direction.getOffsetZ() * 0.1
        );

        world.spawnEntity(itemEntity);
    }
}
