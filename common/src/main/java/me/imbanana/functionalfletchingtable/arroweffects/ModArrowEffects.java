package me.imbanana.functionalfletchingtable.arroweffects;

import me.imbanana.functionalfletchingtable.entities.projectiles.SpecialArrowProjectile;
import net.minecraft.world.item.Item;

import java.util.HashMap;
import java.util.function.Function;

public class ModArrowEffects {
    private static final HashMap<Item, Function<SpecialArrowProjectile, ? extends AbstractArrowEffect>> ARROW_EFFECTS = new HashMap<>();

    private static <T extends AbstractArrowEffect> void registerArrowEffect(Item item, Function<SpecialArrowProjectile, T> factory) {
        ARROW_EFFECTS.put(item, factory);
    }

    public static AbstractArrowEffect createArrowEffect(Item item, SpecialArrowProjectile projectile) {
        if (!hasEffect(item)) throw new RuntimeException("Item '%s' does not have any special effect!".formatted(item.toString()));
        return ARROW_EFFECTS.get(item).apply(projectile);
    }

    public static boolean hasEffect(Item item) {
        return ARROW_EFFECTS.containsKey(item);
    }

    public static void registerModArrowEffects() {
        // Register all here
    }
}
