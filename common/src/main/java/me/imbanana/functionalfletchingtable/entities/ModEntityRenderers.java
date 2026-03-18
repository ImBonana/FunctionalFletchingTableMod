package me.imbanana.functionalfletchingtable.entities;

import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import me.imbanana.functionalfletchingtable.FunctionalFletchingTableMod;
import me.imbanana.functionalfletchingtable.entities.renderers.SpecialArrowProjectileRenderer;

public class ModEntityRenderers {
    public static void registerModEntityRenderers() {
        FunctionalFletchingTableMod.LOGGER.info("Registering mod entity renderers");
        EntityRendererRegistry.register(() -> ModEntityType.SPECIAL_ARROW, SpecialArrowProjectileRenderer::new);
    }
}
