package me.imbanana.functionalfletchingtable.entities.projectiles;

import me.imbanana.functionalfletchingtable.FunctionalFletchingTableMod;
import me.imbanana.functionalfletchingtable.datacomponents.ModDataComponents;
import me.imbanana.functionalfletchingtable.entities.ModEntityType;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class SpecialArrowProjectile extends AbstractArrow {
    private static final int EXPOSED_POTION_DECAY_TIME = 600;
    private static final int NO_EFFECT_COLOR = -1;
    private static final EntityDataAccessor<Integer> ID_EFFECT_COLOR = SynchedEntityData.defineId(SpecialArrowProjectile.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<String> ID_TIP_ITEM = SynchedEntityData.defineId(SpecialArrowProjectile.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<String> ID_SHAFT_ITEM = SynchedEntityData.defineId(SpecialArrowProjectile.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<String> ID_FLETCHING_ITEM = SynchedEntityData.defineId(SpecialArrowProjectile.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<String> ID_EFFECT_ITEM = SynchedEntityData.defineId(SpecialArrowProjectile.class, EntityDataSerializers.STRING);
    private static final byte EVENT_POTION_PUFF = 0;

    public SpecialArrowProjectile(EntityType<? extends SpecialArrowProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public SpecialArrowProjectile(Level level, double x, double y, double z, ItemStack itemStack, ItemStack weapon) {
        super(ModEntityType.SPECIAL_ARROW, x, y, z, level, itemStack, weapon);
        this.updateTexture();
    }

    public SpecialArrowProjectile(Level level, LivingEntity livingEntity, ItemStack itemStack, ItemStack weapon) {
        super(ModEntityType.SPECIAL_ARROW, livingEntity, level, itemStack, weapon);
        this.updateTexture();
    }

    private PotionContents getPotionContents() {
        return this.getPickupItemStackOrigin().getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
    }

    private float getPotionDurationScale() {
        return this.getPickupItemStackOrigin().getOrDefault(DataComponents.POTION_DURATION_SCALE, 1.0F);
    }

    private String getTipItem() {
        return this.getPickupItemStackOrigin().
                getOrDefault(ModDataComponents.SPECIAL_ARROW_TIP, BuiltInRegistries.ITEM.wrapAsHolder(Items.AIR))
                .unwrapKey()
                .map(itemResourceKey -> itemResourceKey.identifier().getPath())
                .orElse("");
    }

    private String getShaftItem() {
        return this.getPickupItemStackOrigin().
                getOrDefault(ModDataComponents.SPECIAL_ARROW_SHAFT, BuiltInRegistries.ITEM.wrapAsHolder(Items.AIR))
                .unwrapKey()
                .map(itemResourceKey -> itemResourceKey.identifier().getPath())
                .orElse("");
    }

    private String getFletchingItem() {
        return this.getPickupItemStackOrigin().
                getOrDefault(ModDataComponents.SPECIAL_ARROW_FLETCHING, BuiltInRegistries.ITEM.wrapAsHolder(Items.AIR))
                .unwrapKey()
                .map(itemResourceKey -> itemResourceKey.identifier().getPath())
                .orElse("");
    }

    private String getEffectItem() {
        Holder<Item> effectItemHolder = this.getPickupItemStackOrigin().getOrDefault(
                ModDataComponents.SPECIAL_ARROW_EFFECT,
                BuiltInRegistries.ITEM.wrapAsHolder(Items.AIR)
        );

        if (effectItemHolder.value() == Items.AIR) return "";

        return effectItemHolder.unwrapKey()
                .map(itemResourceKey -> itemResourceKey.identifier().getPath())
                .orElse("");
    }

    @Override
    protected void setPickupItemStack(ItemStack itemStack) {
        super.setPickupItemStack(itemStack);
        this.updateTexture();
    }

    private void updateTexture() {
        PotionContents potionContents = this.getPotionContents();
        this.entityData.set(ID_EFFECT_COLOR, potionContents.equals(PotionContents.EMPTY) ? NO_EFFECT_COLOR : potionContents.getColor());
        this.entityData.set(ID_TIP_ITEM, this.getTipItem());
        this.entityData.set(ID_SHAFT_ITEM, this.getShaftItem());
        this.entityData.set(ID_FLETCHING_ITEM, this.getFletchingItem());
        this.entityData.set(ID_EFFECT_ITEM, this.getEffectItem());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ID_EFFECT_COLOR, NO_EFFECT_COLOR);
        builder.define(ID_TIP_ITEM, "");
        builder.define(ID_SHAFT_ITEM, "");
        builder.define(ID_FLETCHING_ITEM, "");
        builder.define(ID_EFFECT_ITEM, "");
    }

    @Override
    protected void doPostHurtEffects(LivingEntity livingEntity) {
        super.doPostHurtEffects(livingEntity);
        Entity entity = this.getEffectSource();
        PotionContents potionContents = this.getPotionContents();
        float f = this.getPotionDurationScale();
        potionContents.forEachEffect(mobEffectInstance -> livingEntity.addEffect(mobEffectInstance, entity), f);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            if (this.isInGround()) {
                if (this.inGroundTime % 5 == 0) {
                    this.makeParticle(1);
                }
            } else {
                this.makeParticle(2);
            }
        } else if (this.isInGround() && this.inGroundTime != 0 && !this.getPotionContents().equals(PotionContents.EMPTY) && this.inGroundTime >= EXPOSED_POTION_DECAY_TIME) {
            this.level().broadcastEntityEvent(this, EVENT_POTION_PUFF);
            ItemStack itemStack = this.getPickupItem();
            itemStack.remove(DataComponents.POTION_DURATION_SCALE);
            itemStack.remove(DataComponents.POTION_CONTENTS);
            this.setPickupItemStack(itemStack);
        }
    }

    private void makeParticle(int i) {
        int j = this.getSyncedColor();
        if (j != NO_EFFECT_COLOR && i > 0) {
            for (int k = 0; k < i; k++) {
                this.level()
                        .addParticle(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, j), this.getRandomX(0.5), this.getRandomY(), this.getRandomZ(0.5), 0.0, 0.0, 0.0);
            }
        }
    }

    public int getSyncedColor() {
        return this.entityData.get(ID_EFFECT_COLOR);
    }

    public String getSyncedTipItem() {
        return this.entityData.get(ID_TIP_ITEM);
    }

    public String getSyncedShaftItem() {
        return this.entityData.get(ID_SHAFT_ITEM);
    }

    public String getSyncedFletchingItem() {
        return this.entityData.get(ID_FLETCHING_ITEM);
    }

    public String getSyncedEffectItem() {
        return this.entityData.get(ID_EFFECT_ITEM);
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(Items.ARROW);
    }

    @Override
    public void handleEntityEvent(byte b) {
        if (b == EVENT_POTION_PUFF) {
            int i = this.getSyncedColor();
            if (i != NO_EFFECT_COLOR) {
                float f = (i >> 16 & 0xFF) / 255.0F;
                float g = (i >> 8 & 0xFF) / 255.0F;
                float h = (i >> 0 & 0xFF) / 255.0F;

                for (int j = 0; j < 20; j++) {
                    this.level()
                            .addParticle(
                                    ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, f, g, h), this.getRandomX(0.5), this.getRandomY(), this.getRandomZ(0.5), 0.0, 0.0, 0.0
                            );
                }
            }
        } else {
            super.handleEntityEvent(b);
        }
    }
}
