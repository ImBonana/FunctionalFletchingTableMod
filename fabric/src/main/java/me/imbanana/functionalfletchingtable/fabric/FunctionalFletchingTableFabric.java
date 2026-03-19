package me.imbanana.functionalfletchingtable.fabric;

import me.imbanana.functionalfletchingtable.FunctionalFletchingTableMod;
import me.imbanana.functionalfletchingtable.entities.ModEntityDataSerializers;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricTrackedDataRegistry;

public final class FunctionalFletchingTableFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        FunctionalFletchingTableMod.init();

        for (ModEntityDataSerializers.EntityDataSerializerRegistryInfo info : ModEntityDataSerializers.getModEntityDataSerializersForRegistry()) {
            FabricTrackedDataRegistry.register(info.identifier(), info.serializerSupplier().get());
        }
    }
}
