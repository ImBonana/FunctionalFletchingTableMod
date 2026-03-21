package me.imbanana.functionalfletchingtable.entities.projectiles;

import me.imbanana.functionalfletchingtable.FunctionalFletchingTableMod;
import me.imbanana.functionalfletchingtable.arroweffects.AbstractArrowEffect;
import me.imbanana.functionalfletchingtable.arroweffects.ModArrowEffects;
import me.imbanana.functionalfletchingtable.datacomponents.ModDataComponents;
import me.imbanana.functionalfletchingtable.entities.ModEntityDataSerializers;
import me.imbanana.functionalfletchingtable.entities.ModEntityType;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
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
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class SpecialArrowProjectile extends AbstractArrow {
    private static final int EXPOSED_POTION_DECAY_TIME = 600;
    private static final int NO_EFFECT_COLOR = -1;
    private static final EntityDataAccessor<Integer> ID_EFFECT_COLOR = SynchedEntityData.defineId(SpecialArrowProjectile.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<String> ID_TIP_ITEM = SynchedEntityData.defineId(SpecialArrowProjectile.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<String> ID_SHAFT_ITEM = SynchedEntityData.defineId(SpecialArrowProjectile.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<String> ID_FLETCHING_ITEM = SynchedEntityData.defineId(SpecialArrowProjectile.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<String> ID_EFFECT_ITEM = SynchedEntityData.defineId(SpecialArrowProjectile.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<List<ModArrowEffects.ArrowEffectInfo<? extends AbstractArrowEffect>>> ID_ARROW_EFFECTS = SynchedEntityData.defineId(SpecialArrowProjectile.class, ModEntityDataSerializers.ARROW_EFFECT_INFO);
    private static final byte EVENT_POTION_PUFF = 0;

    private List<AbstractArrowEffect> arrowEffects = new ArrayList<>();

    public SpecialArrowProjectile(EntityType<? extends SpecialArrowProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public SpecialArrowProjectile(Level level, double x, double y, double z, ItemStack itemStack, ItemStack weapon) {
        super(ModEntityType.SPECIAL_ARROW, x, y, z, level, itemStack, weapon);
        this.updateTexture();
        this.createArrowEffects();

        this.executeArrowEffectMethod(AbstractArrowEffect::init);
        this.setBaseDamage(this.executeArrowEffectMethodWithResult(AbstractArrowEffect::getBaseDamageBonus, Double::sum, 2.0));
    }

    public SpecialArrowProjectile(Level level, LivingEntity livingEntity, ItemStack itemStack, ItemStack weapon) {
        super(ModEntityType.SPECIAL_ARROW, livingEntity, level, itemStack, weapon);
        this.updateTexture();
        this.createArrowEffects();

        this.executeArrowEffectMethod(AbstractArrowEffect::init);
        this.setBaseDamage(this.executeArrowEffectMethodWithResult(AbstractArrowEffect::getBaseDamageBonus, Double::sum, 2.0));

    }

    @Override
    public void onRemoval(RemovalReason removalReason) {
        super.onRemoval(removalReason);
        this.executeArrowEffectMethod(effect -> effect.onRemoval(removalReason));
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        boolean shouldContinue = this.executeArrowEffectMethodCancelable(effect -> effect.hitBlock(blockHitResult));

        if (shouldContinue) {
            super.onHitBlock(blockHitResult);
        } else {
            BlockState blockState = this.level().getBlockState(blockHitResult.getBlockPos());
            blockState.onProjectileHit(this.level(), blockState, blockHitResult, this);
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);
        this.executeArrowEffectMethod(effect -> effect.hitEntity(entityHitResult));
    }

    @Override
    public byte getPierceLevel() {
        return (byte) ((int) super.getPierceLevel() + this.executeArrowEffectMethodWithResult(AbstractArrowEffect::getEntityPierceBonus, Integer::sum, 0));
    }

    @Override
    public Vec3 getMovementToShoot(double d, double e, double f, float g, float h) {
        return super.getMovementToShoot(d, e, f, g + this.executeArrowEffectMethodWithResult(AbstractArrowEffect::initialSpeedModifierBonus, Float::sum, 0f), h);
    }

    @Override
    protected float getWaterInertia() {
        return this.executeArrowEffectMethodWithResult(AbstractArrowEffect::getWaterInertiaBonus, Float::sum, super.getWaterInertia());
    }

    @Override
    protected ProjectileDeflection hitTargetOrDeflectSelf(HitResult hitResult) {
        ProjectileDeflection finalDeflection = this.executeArrowEffectMethodWithResult(arrowEffect -> arrowEffect.hitTargetOrDeflectProjectile(hitResult), (projectileDeflection, projectileDeflection2) -> projectileDeflection == ProjectileDeflection.NONE ? projectileDeflection2 : projectileDeflection, ProjectileDeflection.NONE);
        return finalDeflection == ProjectileDeflection.NONE ? super.hitTargetOrDeflectSelf(hitResult) : finalDeflection;
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

    private void createArrowEffects() {
        List<ModArrowEffects.ArrowEffectInfo<? extends AbstractArrowEffect>> effectInfos = new ArrayList<>();

        effectInfos.addAll(this.getArrowEffectInfoFromDataComponent(ModDataComponents.SPECIAL_ARROW_TIP, ModArrowEffects.ArrowPart.TIP));
        effectInfos.addAll(this.getArrowEffectInfoFromDataComponent(ModDataComponents.SPECIAL_ARROW_SHAFT, ModArrowEffects.ArrowPart.SHAFT));
        effectInfos.addAll(this.getArrowEffectInfoFromDataComponent(ModDataComponents.SPECIAL_ARROW_FLETCHING, ModArrowEffects.ArrowPart.FLETCHING));
        effectInfos.addAll(this.getArrowEffectInfoFromDataComponent(ModDataComponents.SPECIAL_ARROW_EFFECT, ModArrowEffects.ArrowPart.EFFECT));

        this.entityData.set(ID_ARROW_EFFECTS, effectInfos);
    }

    private List<ModArrowEffects.ArrowEffectInfo<? extends AbstractArrowEffect>> getArrowEffectInfoFromDataComponent(DataComponentType<Holder<Item>> dataComponentType, ModArrowEffects.ArrowPart arrowPart) {
        Item item = this.getPickupItemStackOrigin().
                getOrDefault(dataComponentType, BuiltInRegistries.ITEM.wrapAsHolder(Items.AIR))
                .value();

        if (ModArrowEffects.hasEffects(arrowPart, item)) {
            return ModArrowEffects.getArrowEffectInfos(arrowPart, item);
        } else if (item != Items.AIR) {
            FunctionalFletchingTableMod.LOGGER.warn("Unable to find arrow effect for %s".formatted(item.toString()));
        }

        return List.of();
    }

    private void executeArrowEffectMethod(Consumer<AbstractArrowEffect> consumer) {
        for (AbstractArrowEffect arrowEffect : this.arrowEffects) {
            consumer.accept(arrowEffect);
        }
    }

    private boolean executeArrowEffectMethodCancelable(Predicate<AbstractArrowEffect> predicate) {
        return arrowEffects.stream().allMatch(predicate);
    }

    private <T> T executeArrowEffectMethodWithResult(Function<AbstractArrowEffect, T> function, BiFunction<T, T, T> op, T initialValue) {
        T currentValue = initialValue;

        for (AbstractArrowEffect arrowEffect : this.arrowEffects) {
            currentValue = op.apply(currentValue, function.apply(arrowEffect));
        }

        return currentValue;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ID_EFFECT_COLOR, NO_EFFECT_COLOR);
        builder.define(ID_TIP_ITEM, "");
        builder.define(ID_SHAFT_ITEM, "");
        builder.define(ID_FLETCHING_ITEM, "");
        builder.define(ID_EFFECT_ITEM, "");
        builder.define(ID_ARROW_EFFECTS, List.of());
    }

    @Override
    protected void doPostHurtEffects(LivingEntity livingEntity) {
        super.doPostHurtEffects(livingEntity);
        Entity entity = this.getEffectSource();
        PotionContents potionContents = this.getPotionContents();
        float f = this.getPotionDurationScale();
        potionContents.forEachEffect(mobEffectInstance -> livingEntity.addEffect(mobEffectInstance, entity), f);
        this.executeArrowEffectMethod(arrowEffect -> arrowEffect.postHitEntity(livingEntity));
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

        this.executeArrowEffectMethod(AbstractArrowEffect::tick);
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

    public List<ModArrowEffects.ArrowEffectInfo<? extends AbstractArrowEffect>> getSyncedArrowEffectInfos() {
        return this.entityData.get(ID_ARROW_EFFECTS);
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

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> entityDataAccessor) {
        super.onSyncedDataUpdated(entityDataAccessor);

        if (entityDataAccessor.equals(ID_ARROW_EFFECTS)) {
            this.arrowEffects.clear();

            this.arrowEffects.addAll(this.getSyncedArrowEffectInfos().stream().map(arrowEffectInfo -> arrowEffectInfo.createEffect(this)).toList());
        }
    }
}
