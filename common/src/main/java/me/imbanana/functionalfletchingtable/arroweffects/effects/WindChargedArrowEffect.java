package me.imbanana.functionalfletchingtable.arroweffects.effects;

import me.imbanana.functionalfletchingtable.arroweffects.AbstractArrowEffect;
import me.imbanana.functionalfletchingtable.entities.projectiles.SpecialArrowProjectile;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.entity.projectile.hurtingprojectile.windcharge.AbstractWindCharge;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class WindChargedArrowEffect extends AbstractArrowEffect {
    public WindChargedArrowEffect(SpecialArrowProjectile projectile) {
        super(projectile);
    }

    @Override
    public boolean hitBlock(BlockHitResult blockHitResult) {
        if (!projectile.level().isClientSide()) {
            explode();
        }

        return true;
    }

    @Override
    public void hitEntity(EntityHitResult entityHitResult) {
        if (!projectile.level().isClientSide()) {
            explode();
        }
    }

    private void explode() {
        double d = projectile.getX();
        double e = projectile.getY() + projectile.getBbHeight() / 2.0F;
        double f = projectile.getZ();
        float g = 3.0F + projectile.getRandom().nextFloat() * 2.0F;

        projectile.level().explode(
                projectile,
                null,
                AbstractWindCharge.EXPLOSION_DAMAGE_CALCULATOR,
                d,
                e,
                f,
                g,
                false,
                Level.ExplosionInteraction.TRIGGER,
                ParticleTypes.GUST_EMITTER_SMALL,
                ParticleTypes.GUST_EMITTER_LARGE,
                WeightedList.of(),
                SoundEvents.BREEZE_WIND_CHARGE_BURST
        );
    }
}
