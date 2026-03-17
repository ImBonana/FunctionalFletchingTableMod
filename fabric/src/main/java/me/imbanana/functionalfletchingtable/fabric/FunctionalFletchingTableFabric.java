package me.imbanana.functionalfletchingtable.fabric;

import me.imbanana.functionalfletchingtable.FunctionalFletchingTableMod;
import net.fabricmc.api.ModInitializer;

public final class FunctionalFletchingTableFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        FunctionalFletchingTableMod.init();
    }
}
