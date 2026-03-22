package me.imbanana.functionalfletchingtable.arroweffects.effects;

import me.imbanana.functionalfletchingtable.arroweffects.AbstractArrowEffect;
import me.imbanana.functionalfletchingtable.entities.projectiles.SpecialArrowProjectile;

public class PiercingArrowEffect extends AbstractArrowEffect {
    private final int piercingBonus;

    public PiercingArrowEffect(SpecialArrowProjectile projectile, int piercingBonus) {
        super(projectile);
        this.piercingBonus = piercingBonus;
    }

    @Override
    public int getEntityPierceBonus() {
        return piercingBonus;
    }
}
