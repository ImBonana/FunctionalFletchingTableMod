package me.imbanana.functionalfletchingtable.datacomponents;

import me.imbanana.functionalfletchingtable.FunctionalFletchingTableMod;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

import java.util.function.UnaryOperator;

public class ModDataComponents {

    public static final DataComponentType<Holder<Item>> SPECIAL_ARROW_TIP = register(
            "special_arrow_tip",
            builder -> builder.persistent(Item.CODEC).networkSynchronized(Item.STREAM_CODEC)
    );

    public static final DataComponentType<Holder<Item>> SPECIAL_ARROW_SHAFT = register(
            "special_arrow_shaft",
            builder -> builder.persistent(Item.CODEC).networkSynchronized(Item.STREAM_CODEC)
    );

    public static final DataComponentType<Holder<Item>> SPECIAL_ARROW_FLETCHING = register(
            "special_arrow_fletching",
            builder -> builder.persistent(Item.CODEC).networkSynchronized(Item.STREAM_CODEC)
    );

    public static final DataComponentType<Holder<Item>> SPECIAL_ARROW_EFFECT = register(
            "special_arrow_effect",
            builder -> builder.persistent(Item.CODEC).networkSynchronized(Item.STREAM_CODEC)
    );

    private static <T> DataComponentType<T> register(String path, UnaryOperator<DataComponentType.Builder<T>> unaryOperator) {
        return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, FunctionalFletchingTableMod.idOf(path), unaryOperator.apply(DataComponentType.builder()).build());
    }

    public static void registerModDataComponents() {
        FunctionalFletchingTableMod.LOGGER.info("Registering Mod Data Components");
    }
}
