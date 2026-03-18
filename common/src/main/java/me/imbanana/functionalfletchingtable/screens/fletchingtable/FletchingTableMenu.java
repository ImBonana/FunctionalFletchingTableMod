package me.imbanana.functionalfletchingtable.screens.fletchingtable;

import me.imbanana.functionalfletchingtable.datacomponents.ModDataComponents;
import me.imbanana.functionalfletchingtable.items.ModItems;
import me.imbanana.functionalfletchingtable.screens.ModScreens;
import me.imbanana.functionalfletchingtable.tags.ModItemTags;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.crafting.RecipeAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class FletchingTableMenu extends ItemCombinerMenu {
    private final DataSlot hasRecipeError = DataSlot.standalone();

    public FletchingTableMenu(int i, Inventory inventory, ContainerLevelAccess access) {
        this(i, inventory, access, inventory.player.level());
    }

    public FletchingTableMenu(int i, Inventory inventory) {
        this(i, inventory, ContainerLevelAccess.NULL);
    }

    public FletchingTableMenu(int i, Inventory inventory, ContainerLevelAccess access, Level level) {
        super(ModScreens.FLETCHING_TABLE_MENU_TYPE, i, inventory, access, createInputSlotDefinitions(level.recipeAccess()));

        this.addDataSlot(hasRecipeError).set(1);
    }

    private static ItemCombinerMenuSlotDefinition createInputSlotDefinitions(RecipeAccess recipeAccess) {
        return ItemCombinerMenuSlotDefinition.create()
                .withSlot(0, 71, 32, itemStack -> itemStack.is(ModItemTags.TIP_ITEMS))
                .withSlot(1, 53, 43, itemStack -> itemStack.is(ModItemTags.SHAFT_ITEMS))
                .withSlot(2, 35, 54, itemStack -> itemStack.is(ModItemTags.FLETCHING_ITEMS))
                .withSlot(3, 125, 12, itemStack -> itemStack.is(ModItemTags.ARROW_EFFECT_ITEMS))
                .withResultSlot(4, 125, 43)
                .build();
    }

    @Override
    protected void onTake(Player player, ItemStack itemStack) {
        itemStack.onCraftedBy(player, itemStack.getCount());
        this.resultSlots.awardUsedRecipes(player, this.getRelevantItems());
        this.shrinkStackInSlot(0);
        this.shrinkStackInSlot(1);
        this.shrinkStackInSlot(2);
        this.shrinkStackInSlot(3);
    }

    @Override
    public void createResult() {
        // normal arrow
        ItemStack result = ItemStack.EMPTY;
        ItemStack potionItem = this.inputSlots.getItem(3);
        if (!potionItem.is(Items.LINGERING_POTION) && !potionItem.is(Items.AIR) && !potionItem.is(ModItemTags.ARROW_EFFECT_ITEMS)) return;
        PotionContents potionEffect = potionItem.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);

        ItemStack tipItem = this.inputSlots.getItem(0);
        ItemStack shaftItem = this.inputSlots.getItem(1);
        ItemStack fletchingItem = this.inputSlots.getItem(2);

        if (
                tipItem.is(Items.FLINT)
                && shaftItem.is(Items.STICK)
                && fletchingItem.is(Items.FEATHER)
        ) {
            // Normal Arrows

            if (potionItem.is(Items.LINGERING_POTION)) {
                result = new ItemStack(Items.TIPPED_ARROW, 8);
                result.set(DataComponents.POTION_CONTENTS, potionEffect);
                result.set(DataComponents.POTION_DURATION_SCALE, 0.125f);
            } else if (potionItem.is(Items.GLOWSTONE_DUST)) {
                result = new ItemStack(Items.SPECTRAL_ARROW, 8);
            } else {
                result = new ItemStack(Items.ARROW, 8);
            }
        } else if (
                !tipItem.isEmpty()
                && !shaftItem.isEmpty()
                && !fletchingItem.isEmpty()) {

            // Custom Arrows
            result = new ItemStack(ModItems.SPECIAL_ARROW, 8);
            result.set(ModDataComponents.SPECIAL_ARROW_TIP, tipItem.getItemHolder());
            result.set(ModDataComponents.SPECIAL_ARROW_SHAFT, shaftItem.getItemHolder());
            result.set(ModDataComponents.SPECIAL_ARROW_FLETCHING, fletchingItem.getItemHolder());

            if (!potionItem.isEmpty()) {
                if (potionEffect == PotionContents.EMPTY) {
                    result.set(ModDataComponents.SPECIAL_ARROW_EFFECT, potionItem.getItemHolder());
                } else {
                    result.set(DataComponents.POTION_CONTENTS, potionEffect);
                    result.set(DataComponents.POTION_DURATION_SCALE, 0.125f);
                }
            }
        }


        this.resultSlots.setItem(0, result);
    }

    @Override
    public void slotsChanged(Container container) {
        super.slotsChanged(container);
        if (this.player.level() instanceof ServerLevel) {
            this.hasRecipeError.set(this.getSlot(this.getResultSlot()).hasItem() ? 0 : 1);
        }
    }

    @Override
    protected boolean isValidBlock(BlockState blockState) {
        return blockState.is(Blocks.FLETCHING_TABLE);
    }

    private void shrinkStackInSlot(int i) {
        ItemStack itemStack = this.inputSlots.getItem(i);
        if (!itemStack.isEmpty()) {
            itemStack.shrink(1);
            this.inputSlots.setItem(i, itemStack);
        }
    }

    private List<ItemStack> getRelevantItems() {
        return List.of(this.inputSlots.getItem(0), this.inputSlots.getItem(1), this.inputSlots.getItem(2), this.inputSlots.getItem(3));
    }

    public boolean hasRecipeError() {
        return this.hasRecipeError.get() > 0;
    }
}
