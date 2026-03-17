package me.imbanana.functionalfletchingtable.fabric;

import me.imbanana.functionalfletchingtable.FunctionalFletchingTableModClient;
import net.fabricmc.api.ClientModInitializer;

public final class FunctionalFletchingTableFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        FunctionalFletchingTableModClient.initClient();
    }
}
