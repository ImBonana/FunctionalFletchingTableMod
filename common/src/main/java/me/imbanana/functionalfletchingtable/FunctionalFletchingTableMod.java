package me.imbanana.functionalfletchingtable;

import dev.architectury.event.events.common.InteractionEvent;
import me.imbanana.functionalfletchingtable.arroweffects.ModArrowEffects;
import me.imbanana.functionalfletchingtable.datacomponents.ModDataComponents;
import me.imbanana.functionalfletchingtable.entities.ModEntityType;
import me.imbanana.functionalfletchingtable.events.ModEvents;
import me.imbanana.functionalfletchingtable.items.ModItems;
import me.imbanana.functionalfletchingtable.screens.ModScreens;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class FunctionalFletchingTableMod {
    public static final String MOD_ID = "functional_fletching_table";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static void init() {
        ModDataComponents.registerModDataComponents();
        ModItems.registerModItems();
        ModEntityType.registerModEntities();
        ModScreens.registerModScreens();
        ModArrowEffects.registerModArrowEffects();

        InteractionEvent.RIGHT_CLICK_BLOCK.register(new ModEvents.FletchingTableInteraction());
    }

    public static Identifier idOf(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}
