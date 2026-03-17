package me.imbanana.functionalfletchingtable.neoforge;

import me.imbanana.functionalfletchingtable.FunctionalFletchingTableMod;
import me.imbanana.functionalfletchingtable.FunctionalFletchingTableModClient;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(value = FunctionalFletchingTableMod.MOD_ID, dist = Dist.CLIENT)
public class FunctionalFletchingTableNeoForgeClient {
    public FunctionalFletchingTableNeoForgeClient(IEventBus modBus) {
        FunctionalFletchingTableModClient.initClient();
    }
}
