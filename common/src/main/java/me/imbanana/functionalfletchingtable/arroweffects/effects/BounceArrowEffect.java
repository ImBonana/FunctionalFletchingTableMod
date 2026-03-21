package me.imbanana.functionalfletchingtable.arroweffects.effects;

import me.imbanana.functionalfletchingtable.FunctionalFletchingTableMod;
import me.imbanana.functionalfletchingtable.arroweffects.AbstractArrowEffect;
import me.imbanana.functionalfletchingtable.entities.projectiles.SpecialArrowProjectile;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class BounceArrowEffect extends AbstractArrowEffect {
    private static final int MAX_WALL_BOUNCE = 4;
    private static final double COLL_OFFSET = 0.01;

    private static final ProjectileDeflection DEFLECT_X = (arrow, entity, randomSource) -> {
        Vec3 motion = arrow.getDeltaMovement();

        arrow.setDeltaMovement(motion.multiply(-1, 1, 1));
        arrow.setPos(arrow.position().add(Math.signum(-motion.x) * COLL_OFFSET, 0, 0));
        arrow.needsSync = true;
    };
    private static final ProjectileDeflection DEFLECT_Y = (arrow, entity, randomSource) -> {
        Vec3 motion = arrow.getDeltaMovement();

        arrow.setDeltaMovement(motion.multiply(1, -1, 1));
        arrow.setPos(arrow.position().add(0, Math.signum(-motion.y) * COLL_OFFSET, 0));
        arrow.needsSync = true;
    };
    private static final ProjectileDeflection DEFLECT_Z = (arrow, entity, randomSource) -> {
        Vec3 motion = arrow.getDeltaMovement();

        arrow.setDeltaMovement(arrow.getDeltaMovement().multiply(1, 1, -1));
        arrow.setPos(arrow.position().add(0, 0, Math.signum(-motion.z) * COLL_OFFSET));
        arrow.needsSync = true;
    };



    private int wallBounceCount = 0;

    public BounceArrowEffect(SpecialArrowProjectile projectile) {
        super(projectile);
    }

    @Override
    public ProjectileDeflection hitTargetOrDeflectProjectile(HitResult hitResult) {
        if (wallBounceCount >= MAX_WALL_BOUNCE || hitResult.getType() != HitResult.Type.BLOCK) return ProjectileDeflection.NONE;
        BlockHitResult blockHitResult = (BlockHitResult) hitResult;

        ProjectileDeflection projectileDeflection = ProjectileDeflection.NONE;

        if (blockHitResult.getDirection().getAxis() == Direction.Axis.X) {
            projectileDeflection = DEFLECT_X;
            wallBounceCount += 1;
        }

        if (blockHitResult.getDirection().getAxis() == Direction.Axis.Y) {
            projectileDeflection = DEFLECT_Y;
            wallBounceCount += 1;
        }

        if (blockHitResult.getDirection().getAxis() == Direction.Axis.Z) {
            projectileDeflection = DEFLECT_Z;
            wallBounceCount += 1;
        }

        projectile.deflect(projectileDeflection, null, EntityReference.of(this.projectile.getOwner()), false);

        return projectileDeflection;
    }
}
