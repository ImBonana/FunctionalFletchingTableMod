package me.imbanana.functionalfletchingtable.screens.fletchingtable;

import me.imbanana.functionalfletchingtable.FunctionalFletchingTableMod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

public class FletchingTableScreen extends ItemCombinerScreen<FletchingTableMenu> {
    private static final Identifier ERROR_SPRITE = FunctionalFletchingTableMod.idOf("container/fletching_table/error");
    private static final Identifier GUI_TEXTURE = FunctionalFletchingTableMod.idOf("textures/gui/container/fletching_table.png");
    public static final Identifier FLETCHING_PLACEHOLDER_SPRITE = FunctionalFletchingTableMod.idOf("container/slot/feather");
    public static final Identifier SHAFT_PLACEHOLDER_SPRITE = FunctionalFletchingTableMod.idOf("container/slot/stick");
    public static final Identifier TIP_PLACEHOLDER_SPRITE = FunctionalFletchingTableMod.idOf("container/slot/flint");
    public static final Identifier EFFECT_PLACEHOLDER_SPRITE = FunctionalFletchingTableMod.idOf("container/slot/lingering_potion");


    public FletchingTableScreen(FletchingTableMenu itemCombinerMenu, Inventory inventory, Component component) {
        super(itemCombinerMenu, inventory, component, GUI_TEXTURE);
    }

    @Override
    protected void renderErrorIcon(GuiGraphics guiGraphics, int i, int j) {
        if (this.menu.hasRecipeError()) {
            guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, ERROR_SPRITE, i + 92, j + 41, 28, 21);
        }
    }

    @Override
    protected void renderSlot(GuiGraphics guiGraphics, Slot slot, int i, int j) {
        super.renderSlot(guiGraphics, slot, i, j);

        Identifier texture = getSlotPlaceholderTexture(slot.index);
        if (slot.getItem().isEmpty() && slot.isActive() && texture != null) {
            guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, texture, slot.x, slot.y, 16, 16);
        }
    }

    private Identifier getSlotPlaceholderTexture(int id) {
        return switch (id) {
            case 0 -> TIP_PLACEHOLDER_SPRITE;
            case 1 -> SHAFT_PLACEHOLDER_SPRITE;
            case 2 -> FLETCHING_PLACEHOLDER_SPRITE;
            case 3 -> EFFECT_PLACEHOLDER_SPRITE;

            default -> null;
        };
    }
}
