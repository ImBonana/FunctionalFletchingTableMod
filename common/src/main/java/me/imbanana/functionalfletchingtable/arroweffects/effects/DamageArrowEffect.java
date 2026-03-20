package me.imbanana.functionalfletchingtable.arroweffects.effects;

import me.imbanana.functionalfletchingtable.arroweffects.AbstractArrowEffect;
import me.imbanana.functionalfletchingtable.entities.projectiles.SpecialArrowProjectile;

public class DamageArrowEffect extends AbstractArrowEffect {
    private final int damageBonus;

    public DamageArrowEffect(SpecialArrowProjectile projectile, int damageBonus) {
        super(projectile);

        this.damageBonus = damageBonus;
    }

    @Override
    public double getBaseDamageBonus() {
        return damageBonus;
    }
}
