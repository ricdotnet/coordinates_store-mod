package dev.ricr.items;

import dev.ricr.Config;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;

public class ModItems {
    public static void registerModItems() {
        Config.LOGGER().info("Registering Mod Items");

        Item hardenedLavaItem = HardenedLavaItem.register(Item::new, new Item.Settings());
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register((itemGroup) -> itemGroup.add(hardenedLavaItem));    }

}
