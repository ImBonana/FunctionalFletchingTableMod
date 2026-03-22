package me.imbanana.functionalfletchingtable.arroweffects;

import me.imbanana.functionalfletchingtable.entities.projectiles.SpecialArrowProjectile;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public abstract class AbstractArrowEffect {
    protected final SpecialArrowProjectile projectile;

    public AbstractArrowEffect(SpecialArrowProjectile projectile) {
        this.projectile = projectile;
    }

    public void init() {

    }

    public void onRemoval(Entity.RemovalReason removalReason) {

    }

    public void tick() {

    }

    public void hitBlock(BlockHitResult blockHitResult) {

    }

    public ProjectileDeflection hitTargetOrDeflectProjectile(HitResult hitResult) {
        return ProjectileDeflection.NONE;
    }

    public void hitEntity(EntityHitResult entityHitResult) {

    }

    public void postHitEntity(LivingEntity livingEntity) {

    }

    public int getEntityPierceBonus() {
        return 0;
    }

    public double getBaseDamageBonus() {
        return 0;
    }

    public float initialSpeedModifierBonus() {
        return 0;
    }

    public float getWaterInertiaBonus() {
        return 0;
    }
}
