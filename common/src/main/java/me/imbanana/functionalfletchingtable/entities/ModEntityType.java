package me.imbanana.functionalfletchingtable.entities;

import me.imbanana.functionalfletchingtable.FunctionalFletchingTableMod;
import me.imbanana.functionalfletchingtable.entities.projectiles.SpecialArrowProjectile;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class ModEntityType {
    public static final EntityType<SpecialArrowProjectile> SPECIAL_ARROW = register(
            "special_arrow",
            EntityType.Builder.<SpecialArrowProjectile>of(SpecialArrowProjectile::new, MobCategory.MISC)
                    .noLootTable()
                    .sized(0.5f, 0.5f)
                    .eyeHeight(0.13f)
                    .clientTrackingRange(4)
                    .updateInterval(20)
    );

    private static <T extends Entity> EntityType<T> register(String path, EntityType.Builder<T> builder) {
        ResourceKey<EntityType<?>> resourceKey = ResourceKey.create(Registries.ENTITY_TYPE, FunctionalFletchingTableMod.idOf(path));
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, resourceKey, builder.build(resourceKey));
    }

    public static void registerModEntities() {
        FunctionalFletchingTableMod.LOGGER.info("Registering Mod Entities");
    }
}
