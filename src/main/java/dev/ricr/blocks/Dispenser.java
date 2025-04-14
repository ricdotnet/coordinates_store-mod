package dev.ricr.blocks;

import dev.ricr.Config;
import net.minecraft.block.*;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class Dispenser {
    public Dispenser() {
        DispenserBehavior oldBehavior = DispenserBlock.BEHAVIORS.get(Items.BUCKET);

        DispenserBlock.registerBehavior(Items.BUCKET, new DispenserBehavior() {
            @Override
            public ItemStack dispense(BlockPointer pointer, ItemStack stack) {
                ServerWorld world = pointer.world();
                BlockPos dispenserPos = pointer.pos();
                Direction facing = pointer.state().get(DispenserBlock.FACING);
                BlockPos inFrontPos = dispenserPos.offset(facing);

                BlockState blockInFrontState = world.getBlockState(inFrontPos);
                Block blockInFront = blockInFrontState.getBlock();

                ItemStack lavaBucket = new ItemStack(Items.LAVA_BUCKET);

                if (blockInFront instanceof LavaCauldronBlock) {
                    if (((LavaCauldronBlock) blockInFront).isFull(blockInFrontState)) {
                        stack.decrement(1);

                        world.setBlockState(inFrontPos, Blocks.CAULDRON.getDefaultState());

                        if (!pointer.blockEntity().addToFirstFreeSlot(lavaBucket).isEmpty()) {
                            Config.dropItem(world, dispenserPos, facing, lavaBucket);
                        }

                        return stack;
                    }
                }

                return oldBehavior.dispense(pointer, stack);
            }
        });
    }
}
