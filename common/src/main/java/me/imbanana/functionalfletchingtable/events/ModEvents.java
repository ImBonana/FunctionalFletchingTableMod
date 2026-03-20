package me.imbanana.functionalfletchingtable.events;

import dev.architectury.event.events.client.ClientTooltipEvent;
import dev.architectury.event.events.common.InteractionEvent;
import me.imbanana.functionalfletchingtable.FunctionalFletchingTableMod;
import me.imbanana.functionalfletchingtable.arroweffects.AbstractArrowEffect;
import me.imbanana.functionalfletchingtable.arroweffects.ModArrowEffects;
import me.imbanana.functionalfletchingtable.datacomponents.ModDataComponents;
import me.imbanana.functionalfletchingtable.screens.fletchingtable.FletchingTableMenuProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.block.Blocks;

import java.util.List;

public class ModEvents {
    public static class FletchingTableInteraction implements InteractionEvent.RightClickBlock {
        @Override
        public InteractionResult click(Player player, InteractionHand interactionHand, BlockPos blockPos, Direction direction) {
            if (player.level().getBlockState(blockPos).getBlock() == Blocks.FLETCHING_TABLE) {
                if (player.level().isClientSide()) return InteractionResult.SUCCESS;

                player.openMenu(new FletchingTableMenuProvider(player.level(), blockPos));
                return InteractionResult.SUCCESS_SERVER;
            }

            return InteractionResult.PASS;
        }
    }

    public static class DataComponentToolTipRender implements ClientTooltipEvent.Item {
        private static Component EFFECT_PREFIX = Component.literal("   - ").withStyle(ChatFormatting.GRAY);

        @Override
        public void append(ItemStack stack, List<Component> lines, Item.TooltipContext tooltipContext, TooltipFlag flag) {
            Holder<Item> tipItem = stack.get(ModDataComponents.SPECIAL_ARROW_TIP);
            Holder<Item> shaftItem = stack.get(ModDataComponents.SPECIAL_ARROW_SHAFT);
            Holder<Item> fletchingItem = stack.get(ModDataComponents.SPECIAL_ARROW_FLETCHING);
            Holder<Item> effectItem = stack.get(ModDataComponents.SPECIAL_ARROW_EFFECT);

            if (tipItem != null) {
                lines.add(Component.translatable("tooltip." + FunctionalFletchingTableMod.MOD_ID + ".arrow_effect.tip").withStyle(ChatFormatting.GRAY));
                List<ModArrowEffects.ArrowEffectInfo<? extends AbstractArrowEffect>> arrowEffects = ModArrowEffects.getArrowEffectInfos(ModArrowEffects.ArrowPart.TIP, tipItem.value());

                for (ModArrowEffects.ArrowEffectInfo<? extends AbstractArrowEffect> effectInfo : arrowEffects) {
                    lines.add(EFFECT_PREFIX.copy().append(Component.translatable(effectInfo.identifier().toLanguageKey("arrow_effect.tip"))));
                }
            }

            if (shaftItem != null) {
                lines.add(Component.translatable("tooltip." + FunctionalFletchingTableMod.MOD_ID + ".arrow_effect.shaft").withStyle(ChatFormatting.GRAY));
                List<ModArrowEffects.ArrowEffectInfo<? extends AbstractArrowEffect>> arrowEffects = ModArrowEffects.getArrowEffectInfos(ModArrowEffects.ArrowPart.SHAFT, shaftItem.value());

                for (ModArrowEffects.ArrowEffectInfo<? extends AbstractArrowEffect> effectInfo : arrowEffects) {
                    lines.add(EFFECT_PREFIX.copy().withStyle(ChatFormatting.GRAY).append(Component.translatable(effectInfo.identifier().toLanguageKey("arrow_effect.shaft"))));
                }
            }

            if (fletchingItem != null) {
                lines.add(Component.translatable("tooltip." + FunctionalFletchingTableMod.MOD_ID + ".arrow_effect.fletching").withStyle(ChatFormatting.GRAY));
                List<ModArrowEffects.ArrowEffectInfo<? extends AbstractArrowEffect>> arrowEffects = ModArrowEffects.getArrowEffectInfos(ModArrowEffects.ArrowPart.FLETCHING, fletchingItem.value());

                for (ModArrowEffects.ArrowEffectInfo<? extends AbstractArrowEffect> effectInfo : arrowEffects) {
                    lines.add(EFFECT_PREFIX.copy().withStyle(ChatFormatting.GRAY).append(Component.translatable(effectInfo.identifier().toLanguageKey("arrow_effect.fletching"))));
                }
            }

            if ((effectItem == null || effectItem.value() == Items.AIR) && stack.has(DataComponents.POTION_CONTENTS)) {
                if (stack.has(DataComponents.TOOLTIP_DISPLAY)) {
                    stack.set(DataComponents.TOOLTIP_DISPLAY, stack.get(DataComponents.TOOLTIP_DISPLAY).withHidden(DataComponents.POTION_CONTENTS, true));
                }

                lines.add(Component.translatable("tooltip." + FunctionalFletchingTableMod.MOD_ID + ".arrow_effect.effect").withStyle(ChatFormatting.GRAY));
                stack.get(DataComponents.POTION_CONTENTS).addToTooltip(tooltipContext, component -> lines.add(EFFECT_PREFIX.copy().append(component)), flag, stack);
            } else if (effectItem != null) {
                List<ModArrowEffects.ArrowEffectInfo<? extends AbstractArrowEffect>> arrowEffects = ModArrowEffects.getArrowEffectInfos(ModArrowEffects.ArrowPart.EFFECT, effectItem.value());

                if (!arrowEffects.isEmpty()) {
                    lines.add(Component.translatable("tooltip." + FunctionalFletchingTableMod.MOD_ID + ".arrow_effect.effect").withStyle(ChatFormatting.GRAY));
                }

                for (ModArrowEffects.ArrowEffectInfo<? extends AbstractArrowEffect> effectInfo : arrowEffects) {
                    lines.add(EFFECT_PREFIX.copy().withStyle(ChatFormatting.GRAY).append(Component.translatable(effectInfo.identifier().toLanguageKey("arrow_effect.effect"))));
                }
            }
        }
    }
}
