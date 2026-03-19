package me.imbanana.functionalfletchingtable;

import dev.architectury.event.events.client.ClientTooltipEvent;
import me.imbanana.functionalfletchingtable.entities.ModEntityRenderers;
import me.imbanana.functionalfletchingtable.events.ModEvents;
import me.imbanana.functionalfletchingtable.screens.ModScreens;

public class FunctionalFletchingTableModClient {
    public static void initClient() {
        ModScreens.registerClientScreens();
        ModEntityRenderers.registerModEntityRenderers();

        ClientTooltipEvent.ITEM.register(new ModEvents.DataComponentToolTipRender());
    }
}
