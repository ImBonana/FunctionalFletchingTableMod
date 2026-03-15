package me.imbanana.functionalfletchingtable.fabric;

import me.imbanana.functionalfletchingtable.FunctionalFletchingTable;
import net.fabricmc.api.ModInitializer;

public final class FunctionalFletchingTableFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        FunctionalFletchingTable.init();
    }
}
