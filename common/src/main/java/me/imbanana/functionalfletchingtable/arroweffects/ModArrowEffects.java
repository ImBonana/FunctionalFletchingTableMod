package me.imbanana.functionalfletchingtable.arroweffects;

import io.netty.buffer.ByteBuf;
import me.imbanana.functionalfletchingtable.FunctionalFletchingTableMod;
import me.imbanana.functionalfletchingtable.arroweffects.effects.UnderwaterArrowEffect;
import me.imbanana.functionalfletchingtable.entities.projectiles.SpecialArrowProjectile;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ModArrowEffects {
    private static final List<ArrowEffectInfo<? extends AbstractArrowEffect>> ARROW_EFFECTS = new ArrayList<>();

    public static final ArrowEffectInfo<UnderwaterArrowEffect> UNDERWATER = registerArrowEffect("underwater", ArrowPart.TIP, Items.PRISMARINE_SHARD, UnderwaterArrowEffect::new);

    private static <T extends AbstractArrowEffect> ArrowEffectInfo<T> registerArrowEffect(String path, ArrowPart arrowPart, Item item, Function<SpecialArrowProjectile, T> factory) {
        return registerArrowEffect(path, new ArrowPart[] { arrowPart }, item, factory);
    }

    private static <T extends AbstractArrowEffect> ArrowEffectInfo<T> registerArrowEffect(String path, ArrowPart[] arrowParts, Item item, Function<SpecialArrowProjectile, T> factory) {
        ArrowEffectInfo<T> effectInfo = new ArrowEffectInfo<>(FunctionalFletchingTableMod.idOf(path), arrowParts, item, factory);

        if (ARROW_EFFECTS.stream().anyMatch(arrowEffectInfo -> arrowEffectInfo.identifier.equals(effectInfo.identifier)))
            throw new RuntimeException("Failed to register arrow effect, Duplicate identifier. (%s)".formatted(effectInfo.identifier.toString()));

        ARROW_EFFECTS.add(effectInfo);
        return effectInfo;
    }

    public static List<? extends AbstractArrowEffect> createArrowAllEffectsFromItem(ArrowPart arrowPart, Item item, SpecialArrowProjectile projectile) {
        if (!hasEffects(arrowPart, item)) throw new RuntimeException("Item '%s' does not have any special effect for '%s' arrow part!".formatted(item.toString(), arrowPart.name()));
        return getArrowEffectInfos(arrowPart, item).stream().map(arrowEffectInfo -> arrowEffectInfo.createEffect(projectile)).toList();
    }

    public static boolean hasEffects(ArrowPart arrowPart, Item item) {
        return !getArrowEffectInfos(arrowPart, item).isEmpty();
    }

    public static ArrowEffectInfo<? extends AbstractArrowEffect> getArrowEffectInfo(Identifier identifier) {
        for (ArrowEffectInfo<? extends AbstractArrowEffect> arrowEffectInfo : ARROW_EFFECTS) {
            if (arrowEffectInfo.identifier.equals(identifier)) {
                return arrowEffectInfo;
            }
        }

        return null;
    }

    public static List<ArrowEffectInfo<? extends AbstractArrowEffect>> getArrowEffectInfos(ArrowPart arrowPart, Item item) {
        return ARROW_EFFECTS.stream().filter(arrowEffectInfo -> arrowEffectInfo.item == item && arrowEffectInfo.canApplyOnPart(arrowPart)).toList();
    }

    public static void registerModArrowEffects() {
        FunctionalFletchingTableMod.LOGGER.info("Registering Mod Arrow Effects");
    }

    public record ArrowEffectInfo<T extends AbstractArrowEffect>(Identifier identifier, ArrowPart[] arrowParts, Item item, Function<SpecialArrowProjectile, T> factory) {
        public static final StreamCodec<ByteBuf, ArrowEffectInfo<? extends AbstractArrowEffect>> STREAM_CODEC = new StreamCodec<>() {
            @Override
            public ArrowEffectInfo<? extends AbstractArrowEffect> decode(ByteBuf byteBuf) {
                return ModArrowEffects.getArrowEffectInfo(Identifier.STREAM_CODEC.decode(byteBuf));
            }

            @Override
            public void encode(ByteBuf byteBuf, ArrowEffectInfo arrowEffect) {
                Identifier.STREAM_CODEC.encode(byteBuf, arrowEffect.identifier());
            }
        };

        public static final StreamCodec<ByteBuf, List<ArrowEffectInfo<? extends AbstractArrowEffect>>> LIST_STREAM_CODEC = new StreamCodec<ByteBuf, List<ArrowEffectInfo<? extends AbstractArrowEffect>>>() {
            @Override
            public List<ArrowEffectInfo<? extends AbstractArrowEffect>> decode(ByteBuf byteBuf) {
                int size = byteBuf.readInt();
                List<ArrowEffectInfo<? extends AbstractArrowEffect>> list = new ArrayList<>();

                for (int i = 0; i < size; i++) {
                    list.add(ArrowEffectInfo.STREAM_CODEC.decode(byteBuf));
                }

                return list;
            }

            @Override
            public void encode(ByteBuf byteBuf, List<ArrowEffectInfo<? extends AbstractArrowEffect>> effectInfos) {
                byteBuf.writeInt(effectInfos.size());

                for (ArrowEffectInfo<? extends AbstractArrowEffect> info : effectInfos) {
                    ArrowEffectInfo.STREAM_CODEC.encode(byteBuf, info);
                }
            }
        };

        public T createEffect(SpecialArrowProjectile projectile) {
            return this.factory.apply(projectile);
        }

        public boolean canApplyOnPart(ArrowPart arrowPart) {
            for (ArrowPart validArrowPart : arrowParts) {
                if (validArrowPart == arrowPart) {
                    return true;
                }
            }

            return false;
        }
    }

    public enum ArrowPart {
        TIP,
        SHAFT,
        FLETCHING,
        EFFECT
    }
}
