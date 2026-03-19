package me.imbanana.functionalfletchingtable.neoforge;

import me.imbanana.functionalfletchingtable.FunctionalFletchingTableMod;
import me.imbanana.functionalfletchingtable.entities.ModEntityDataSerializers;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

@Mod(FunctionalFletchingTableMod.MOD_ID)
public final class FunctionalFletchingTableNeoForge {
    private static final DeferredRegister<EntityDataSerializer<?>> ENTITY_DATA_SERIALIZERS = DeferredRegister.create(NeoForgeRegistries.ENTITY_DATA_SERIALIZERS, FunctionalFletchingTableMod.MOD_ID);

    public FunctionalFletchingTableNeoForge(IEventBus modBus, ModContainer container) {
        FunctionalFletchingTableMod.init();

        for (ModEntityDataSerializers.EntityDataSerializerRegistryInfo info : ModEntityDataSerializers.getModEntityDataSerializersForRegistry()) {
            ENTITY_DATA_SERIALIZERS.register(info.identifier().getPath(), info.serializerSupplier());
        }

        ENTITY_DATA_SERIALIZERS.register(modBus);
    }
}
