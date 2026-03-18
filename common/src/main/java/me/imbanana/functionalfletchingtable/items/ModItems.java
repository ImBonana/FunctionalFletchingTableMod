package me.imbanana.functionalfletchingtable.items;

import me.imbanana.functionalfletchingtable.FunctionalFletchingTableMod;
import me.imbanana.functionalfletchingtable.datacomponents.ModDataComponents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.function.Function;

public class ModItems {
    public static final SpecialArrowItem SPECIAL_ARROW = register(
            "special_arrow",
            SpecialArrowItem::new,
            new Item.Properties()
                    .component(ModDataComponents.SPECIAL_ARROW_TIP, BuiltInRegistries.ITEM.wrapAsHolder(Items.AIR))
                    .component(ModDataComponents.SPECIAL_ARROW_SHAFT, BuiltInRegistries.ITEM.wrapAsHolder(Items.AIR))
                    .component(ModDataComponents.SPECIAL_ARROW_FLETCHING, BuiltInRegistries.ITEM.wrapAsHolder(Items.AIR))
                    .component(ModDataComponents.SPECIAL_ARROW_EFFECT, BuiltInRegistries.ITEM.wrapAsHolder(Items.AIR))
                    .arch$tab(CreativeModeTabs.COMBAT)
    );

    private static <T extends Item> T register(String path, Function<Item.Properties, T> itemFactory, Item.Properties settings) {
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, FunctionalFletchingTableMod.idOf(path));

        T item = itemFactory.apply(settings.setId(itemKey));

        Registry.register(BuiltInRegistries.ITEM, itemKey, item);

        return item;
    }

    public static void registerModItems() {
        FunctionalFletchingTableMod.LOGGER.info("Registering Mod Items");
    }
}
