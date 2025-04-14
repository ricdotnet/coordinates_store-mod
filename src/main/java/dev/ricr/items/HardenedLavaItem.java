package dev.ricr.items;

import dev.ricr.Config;
import dev.ricr.Constants;
import net.fabricmc.fabric.api.registry.FuelRegistryEvents;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class HardenedLavaItem {

    public static Item register(Function<Item.Settings, Item> itemFactory, Item.Settings settings) {
        Config.LOGGER().info("Registering HardenedLavaItem");

        RegistryKey<Item> itemRegistryKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Constants.MOD_ID, "hardened_lava"));
        Item hardenedLavaItem = itemFactory.apply(settings.registryKey(itemRegistryKey));
        Registry.register(Registries.ITEM, itemRegistryKey, hardenedLavaItem);

        FuelRegistryEvents.BUILD.register(((builder, context) -> {
            builder.add(hardenedLavaItem, 20 * 60 * 5); // 5 minutes - 20 (ticks) * 60 (seconds) * 5 (minutes)
        }));

        return hardenedLavaItem;
    }

}
