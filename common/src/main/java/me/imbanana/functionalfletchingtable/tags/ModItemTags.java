package me.imbanana.functionalfletchingtable.tags;

import me.imbanana.functionalfletchingtable.FunctionalFletchingTableMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModItemTags {
    public static final TagKey<Item> SHAFT_ITEMS = TagKey.create(Registries.ITEM, FunctionalFletchingTableMod.idOf("fletching_table/shaft_items"));
    public static final TagKey<Item> TIP_ITEMS = TagKey.create(Registries.ITEM, FunctionalFletchingTableMod.idOf("fletching_table/tip_items"));
    public static final TagKey<Item> FLETCHING_ITEMS = TagKey.create(Registries.ITEM, FunctionalFletchingTableMod.idOf("fletching_table/fletching_items"));

}
