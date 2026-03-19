package me.imbanana.functionalfletchingtable.arroweffects.effects;

import me.imbanana.functionalfletchingtable.arroweffects.AbstractArrowEffect;
import me.imbanana.functionalfletchingtable.entities.projectiles.SpecialArrowProjectile;

public class UnderwaterArrowEffect extends AbstractArrowEffect {
    public UnderwaterArrowEffect(SpecialArrowProjectile projectile) {
        super(projectile);
    }

    @Override
    public float getWaterInertiaBonus() {
        return 0.6f; // vanilla default is 0.6f
    }
}
