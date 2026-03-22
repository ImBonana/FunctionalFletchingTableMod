package me.imbanana.functionalfletchingtable.arroweffects.effects;

import me.imbanana.functionalfletchingtable.arroweffects.AbstractArrowEffect;
import me.imbanana.functionalfletchingtable.entities.projectiles.SpecialArrowProjectile;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.EntityHitResult;

public class FlameArrowEffect extends AbstractArrowEffect {
    public FlameArrowEffect(SpecialArrowProjectile projectile) {
        super(projectile);
    }

    @Override
    public void init() {
        this.projectile.igniteForSeconds(Integer.MAX_VALUE);
    }
}
