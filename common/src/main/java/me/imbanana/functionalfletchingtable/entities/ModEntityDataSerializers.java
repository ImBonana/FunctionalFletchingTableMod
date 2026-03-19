package me.imbanana.functionalfletchingtable.entities;

import me.imbanana.functionalfletchingtable.FunctionalFletchingTableMod;
import me.imbanana.functionalfletchingtable.arroweffects.AbstractArrowEffect;
import me.imbanana.functionalfletchingtable.arroweffects.ModArrowEffects;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.resources.Identifier;

import java.util.List;
import java.util.function.Supplier;

public class ModEntityDataSerializers {
    public static final EntityDataSerializer<List<ModArrowEffects.ArrowEffectInfo<? extends AbstractArrowEffect>>> ARROW_EFFECT_INFO = EntityDataSerializer.forValueType(ModArrowEffects.ArrowEffectInfo.LIST_STREAM_CODEC);

    public static List<EntityDataSerializerRegistryInfo> getModEntityDataSerializersForRegistry() {
        FunctionalFletchingTableMod.LOGGER.info("Registering Mod Entity Data Serializers");

        return List.of(
                new EntityDataSerializerRegistryInfo(FunctionalFletchingTableMod.idOf("arrow_effect_info"), () -> ARROW_EFFECT_INFO)
        );
    }

    public record EntityDataSerializerRegistryInfo(Identifier identifier, Supplier<EntityDataSerializer<?>> serializerSupplier) { }
}
