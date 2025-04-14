package dev.ricr;

import dev.ricr.commands.SetHome;
import dev.ricr.commands.ShowHome;
import dev.ricr.commands.Teleport;
import dev.ricr.enchantment.effect.ModEnchantmentEffects;
import dev.ricr.state.Homes;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelBasedValue;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

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

        // register enchantments
        ModEnchantmentEffects.registerModEnchantmentEffects();

        PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
            ItemStack heldItem = player.getMainHandStack();

            if (heldItem.hasEnchantments()) {
                RegistryKey<Enchantment> key = RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(Constants.MOD_ID, "breaking"));

                heldItem.getEnchantments().getEnchantmentEntries().forEach(entry -> {
                    if (entry.toString().contains(key.getValue().toString())) {
                        System.out.println(heldItem.getEnchantments().getLevel(entry.getKey()));
                    }
                });

//                if (level > 0) {
//                    // Apply your enchantment effect here
//                    // For example: drop extra XP, damage tool more, change block drops, etc.
//
//                    // You can use your EnchantmentEffectComponent system here too
//                    float effectValue = EnchantmentLevelBasedValue.linear(0.4f, 0.2f).getValue(level);
//
//                    System.out.println(effectValue);
//                    System.out.println("Block broken with level " + level);
//
//                    // Apply custom effect logic based on `effectValue`
//                }
            }
        });

//        ModItems.registerModItems();
    }
}