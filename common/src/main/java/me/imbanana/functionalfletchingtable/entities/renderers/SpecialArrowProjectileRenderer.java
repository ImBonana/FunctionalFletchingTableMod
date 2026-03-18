package me.imbanana.functionalfletchingtable.entities.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import me.imbanana.functionalfletchingtable.FunctionalFletchingTableMod;
import me.imbanana.functionalfletchingtable.entities.projectiles.SpecialArrowProjectile;
import me.imbanana.functionalfletchingtable.entities.states.SpecialArrowProjectileRenderState;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.object.projectile.ArrowModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;

public class SpecialArrowProjectileRenderer extends EntityRenderer<SpecialArrowProjectile, SpecialArrowProjectileRenderState> {
    private final ArrowModel model;

    public SpecialArrowProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new ArrowModel(context.bakeLayer(ModelLayers.ARROW));
    }

    @Override
    public void submit(SpecialArrowProjectileRenderState arrowRenderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(arrowRenderState.yRot - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(arrowRenderState.xRot));
        if (!arrowRenderState.isInvisible) {
            if (arrowRenderState.tipItem != null) {
                submitNodeCollector.order(1).submitModel(
                        this.model,
                        arrowRenderState,
                        poseStack,
                        RenderTypes.entityCutoutNoCull(FunctionalFletchingTableMod.idOf("textures/entity/projectiles/special_arrow/tip/" + arrowRenderState.tipItem + ".png")),
                        arrowRenderState.lightCoords,
                        OverlayTexture.NO_OVERLAY,
                        arrowRenderState.outlineColor,
                        null
                );
            }

            if (arrowRenderState.shaftItem != null) {
                submitNodeCollector.order(1).submitModel(
                        this.model,
                        arrowRenderState,
                        poseStack,
                        RenderTypes.entityCutoutNoCull(FunctionalFletchingTableMod.idOf("textures/entity/projectiles/special_arrow/shaft/" + arrowRenderState.shaftItem + ".png")),
                        arrowRenderState.lightCoords,
                        OverlayTexture.NO_OVERLAY,
                        arrowRenderState.outlineColor,
                        null
                );
            }

            if (arrowRenderState.fletchingItem != null && !arrowRenderState.isInvisible) {
                submitNodeCollector.order(1).submitModel(
                        this.model,
                        arrowRenderState,
                        poseStack,
                        RenderTypes.entityCutoutNoCull(FunctionalFletchingTableMod.idOf("textures/entity/projectiles/special_arrow/fletching/" + arrowRenderState.fletchingItem + ".png")),
                        arrowRenderState.lightCoords,
                        OverlayTexture.NO_OVERLAY,
                        arrowRenderState.outlineColor,
                        null
                );
            }

            if (arrowRenderState.effectItem != null) {
                submitNodeCollector.order(1).submitModel(
                        this.model,
                        arrowRenderState,
                        poseStack,
                        RenderTypes.entityCutoutNoCull(FunctionalFletchingTableMod.idOf("textures/entity/projectiles/special_arrow/effect/" + arrowRenderState.effectItem + ".png")),
                        arrowRenderState.lightCoords,
                        OverlayTexture.NO_OVERLAY,
                        arrowRenderState.outlineColor,
                        null
                );
            } else if(arrowRenderState.potionColor != -1) {
                submitNodeCollector.order(1).submitModel(
                        this.model,
                        arrowRenderState,
                        poseStack,
                        RenderTypes.entityCutoutNoCull(FunctionalFletchingTableMod.idOf("textures/entity/projectiles/special_arrow/effect/potion_effect.png")),
                        arrowRenderState.lightCoords,
                        OverlayTexture.NO_OVERLAY,
                        arrowRenderState.potionColor,
                        null,
                        arrowRenderState.outlineColor,
                        null
                );
            }
        }
        poseStack.popPose();
        super.submit(arrowRenderState, poseStack, submitNodeCollector, cameraRenderState);
    }

    @Override
    public SpecialArrowProjectileRenderState createRenderState() {
        return new SpecialArrowProjectileRenderState();
    }

    @Override
    public void extractRenderState(SpecialArrowProjectile entity, SpecialArrowProjectileRenderState entityRenderState, float f) {
        super.extractRenderState(entity, entityRenderState, f);
        entityRenderState.tipItem = entity.getSyncedTipItem().isEmpty() ? null : entity.getSyncedTipItem();
        entityRenderState.shaftItem = entity.getSyncedShaftItem().isEmpty() ? null : entity.getSyncedShaftItem();
        entityRenderState.fletchingItem = entity.getSyncedFletchingItem().isEmpty() ? null : entity.getSyncedFletchingItem();
        entityRenderState.effectItem = entity.getSyncedEffectItem().isEmpty() ? null : entity.getSyncedEffectItem();
        entityRenderState.potionColor = entity.getSyncedColor();
        entityRenderState.xRot = entity.getXRot(f);
        entityRenderState.yRot = entity.getYRot(f);
        entityRenderState.shake = entity.shakeTime - f;
    }
}
