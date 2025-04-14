package dev.ricr.enchantment.effect;

import com.mojang.serialization.MapCodec;
import dev.ricr.Config;
import dev.ricr.Constants;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ModEnchantmentEffects {
    public static final RegistryKey<Enchantment> BREAKING = of("breaking");
    public static MapCodec<BreakingEnchantmentEffect> BREAKING_EFFECT = register("breaking_effect", BreakingEnchantmentEffect.CODEC);

    private static RegistryKey<Enchantment> of(String path) {
        Identifier id = Identifier.of(Constants.MOD_ID, path);
        return RegistryKey.of(RegistryKeys.ENCHANTMENT, id);
    }

    private static <T extends EnchantmentEntityEffect> MapCodec<T> register(String id, MapCodec<T> codec) {
        return Registry.register(Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE, Identifier.of(Constants.MOD_ID, id), codec);
    }

    public static void registerModEnchantmentEffects() {
        Config.LOGGER().info("Registering EnchantmentEffects for" + Constants.MOD_ID);
    }
}