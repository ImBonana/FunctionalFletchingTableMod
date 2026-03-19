package me.imbanana.functionalfletchingtable.screens.fletchingtable;

import me.imbanana.functionalfletchingtable.FunctionalFletchingTableMod;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

public record FletchingTableMenuProvider(Level level, BlockPos blockPos) implements MenuProvider {

    @Override
    public Component getDisplayName() {
        return Component.translatable("container." + FunctionalFletchingTableMod.MOD_ID + ".fletching_table");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new FletchingTableMenu(i, inventory, ContainerLevelAccess.create(level, blockPos));
    }
}
