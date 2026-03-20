package me.imbanana.functionalfletchingtable.arroweffects.effects;

import me.imbanana.functionalfletchingtable.arroweffects.AbstractArrowEffect;
import me.imbanana.functionalfletchingtable.entities.projectiles.SpecialArrowProjectile;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class SlownessArrowEffect extends AbstractArrowEffect {
    public SlownessArrowEffect(SpecialArrowProjectile projectile) {
        super(projectile);
    }

    @Override
    public void postHitEntity(LivingEntity livingEntity) {
        livingEntity.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 15 * 20, 2));
    }
}
