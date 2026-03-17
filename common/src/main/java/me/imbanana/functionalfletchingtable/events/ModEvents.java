package me.imbanana.functionalfletchingtable.events;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.event.events.common.InteractionEvent;
import me.imbanana.functionalfletchingtable.FunctionalFletchingTableMod;
import me.imbanana.functionalfletchingtable.FunctionalFletchingTableModClient;
import me.imbanana.functionalfletchingtable.screens.fletchingtable.FletchingTableMenuProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

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
}
