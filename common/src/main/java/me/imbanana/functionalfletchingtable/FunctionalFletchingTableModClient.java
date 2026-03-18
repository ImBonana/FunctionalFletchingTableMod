package me.imbanana.functionalfletchingtable;

import me.imbanana.functionalfletchingtable.entities.ModEntityRenderers;
import me.imbanana.functionalfletchingtable.screens.ModScreens;

public class FunctionalFletchingTableModClient {
    public static void initClient() {
        ModScreens.registerClientScreens();
        ModEntityRenderers.registerModEntityRenderers();
    }
}
