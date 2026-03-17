package me.imbanana.functionalfletchingtable.screens;

import dev.architectury.registry.client.gui.MenuScreenRegistry;
import me.imbanana.functionalfletchingtable.FunctionalFletchingTableMod;
import me.imbanana.functionalfletchingtable.screens.fletchingtable.FletchingTableScreen;
import me.imbanana.functionalfletchingtable.screens.fletchingtable.FletchingTableMenu;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class ModScreens {
    public static final MenuType<FletchingTableMenu> FLETCHING_TABLE_MENU_TYPE = registerMenuType("fletching_table", FletchingTableMenu::new);


    private static <T extends AbstractContainerMenu> MenuType<T> registerMenuType(String path, MenuType.MenuSupplier<T> menu) {
        return Registry.register(BuiltInRegistries.MENU, FunctionalFletchingTableMod.idOf(path), new MenuType<>(menu, FeatureFlags.VANILLA_SET));
    }

    public static void registerModScreens() {
        FunctionalFletchingTableMod.LOGGER.info("Creating Functional Fletching Table Mod screens");
    }

    public static void registerClientScreens() {
        MenuScreenRegistry.registerScreenFactory(ModScreens.FLETCHING_TABLE_MENU_TYPE, FletchingTableScreen::new);
    }
}