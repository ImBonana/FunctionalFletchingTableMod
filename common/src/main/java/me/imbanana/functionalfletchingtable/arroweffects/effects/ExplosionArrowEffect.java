package me.imbanana.functionalfletchingtable.arroweffects.effects;

import me.imbanana.functionalfletchingtable.FunctionalFletchingTableMod;
import me.imbanana.functionalfletchingtable.arroweffects.AbstractArrowEffect;
import me.imbanana.functionalfletchingtable.entities.projectiles.SpecialArrowProjectile;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class ExplosionArrowEffect extends AbstractArrowEffect {
    private final float power;
    private final int fuse;
    private final boolean destructive;

    private int timeUntilBoom = 0;

    public ExplosionArrowEffect(SpecialArrowProjectile projectile, float power, int fuse, boolean destructive) {
        super(projectile);
        this.power = power;
        this.fuse = fuse;
        this.destructive = destructive;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.projectile.isInGround()) {
            FunctionalFletchingTableMod.LOGGER.info(String.valueOf(fuse));
            FunctionalFletchingTableMod.LOGGER.info(String.valueOf(timeUntilBoom));
            if (this.timeUntilBoom < this.fuse) {
                this.timeUntilBoom += 1;
            } else {
                explode();
                this.projectile.discard();
            }
        }
    }

    @Override
    public void hitBlock(BlockHitResult blockHitResult) {
        if (!this.projectile.level().isClientSide() && fuse > 0) {
            this.projectile.level().playSound(null, this.projectile.getX(), this.projectile.getY(), this.projectile.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }

    @Override
    public void hitEntity(EntityHitResult entityHitResult) {
        if (!this.projectile.level().isClientSide()) {
            explode();
            this.projectile.discard();
        }
    }

    private void explode() {
        this.projectile.level()
            .explode(
                this.projectile,
                Explosion.getDefaultDamageSource(this.projectile.level(), this.projectile),
                null,
                this.projectile.getX(),
                this.projectile.getY(0.0625),
                this.projectile.getZ(),
                this.power,
                false,
                this.destructive ? Level.ExplosionInteraction.TNT : Level.ExplosionInteraction.NONE
            );
    }
}
