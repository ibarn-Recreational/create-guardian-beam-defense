package com.ibarnstormer.gbd.client.renderer;

import com.ibarnstormer.gbd.config.IConfig;
import com.ibarnstormer.gbd.entities.BeamReactorLaserEntity;
import com.ibarnstormer.gbd.utils.Utils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class BeamReactorLaserEntityRenderer extends EntityRenderer<BeamReactorLaserEntity> {

    private static final ResourceLocation GUARDIAN_BEAM_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/guardian_beam.png");
    private static final RenderType BEAM_RENDER_TYPE = RenderType.entityCutoutNoCull(GUARDIAN_BEAM_LOCATION);

    public BeamReactorLaserEntityRenderer(EntityRendererProvider.Context p_174008_) {
        super(p_174008_);
    }

    protected int getBlockLightLevel(BeamReactorLaserEntity p_114496_, BlockPos p_114497_) {
        return 15;
    }

    protected int getSkyLightLevel(BeamReactorLaserEntity p_114509_, BlockPos p_114510_) {
        return 15;
    }

    private Vec3 lerpedOffset(BeamReactorLaserEntity p_114803_, float p_114805_, Vec3 offset) {
        double d0 = (p_114803_.offsetOld.x + (offset.x - p_114803_.offsetOld.x) * p_114805_);
        double d1 = (p_114803_.offsetOld.y + (offset.y - p_114803_.offsetOld.y) * p_114805_);
        double d2 = (p_114803_.offsetOld.z + (offset.z - p_114803_.offsetOld.z) * p_114805_);
        return new Vec3(d0, d1, d2);
    }

    private Vec3 getUnlerpedPos(Entity p_114803_, Vec3 offset) {
        double d0 = p_114803_.getX() + offset.x;
        double d1 = p_114803_.getY() + offset.y;
        double d2 = p_114803_.getZ() + offset.z;
        return new Vec3(d0, d1, d2);
    }

    private Vec3 lerpPos(Entity p_114803_, float p_114805_, boolean atEyes) {
        double d0 = Mth.lerp(p_114805_, p_114803_.xOld, p_114803_.position().x);
        double d1 = Mth.lerp(p_114805_, atEyes ? p_114803_.yOld + p_114803_.getEyeHeight() + 0.20 : p_114803_.yOld, atEyes ? p_114803_.getEyeY() + 0.20 : p_114803_.position().y);
        double d2 = Mth.lerp(p_114805_, p_114803_.zOld, p_114803_.position().z);
        return new Vec3(d0, d1, d2);
    }

    // Basically from guardian renderer
    public void render(BeamReactorLaserEntity beam, float p_114830_, float p_114831_, PoseStack p_114832_, MultiBufferSource p_114833_, int p_114834_) {
        super.render(beam, p_114830_, p_114831_, p_114832_, p_114833_, p_114834_);
        Entity owner = beam.getCachedOwner();
        if(owner != null && beam.getClientSideAttackTime() > 1) {
            float f = beam.getAttackAnimationScale(p_114831_);
            float f1 = (float)beam.getBeamTick() + p_114831_;
            float f2 = f1 * 0.5F % 1.0F;
            p_114832_.pushPose();

            Vec3 lerpPosBeam = lerpPos(beam, p_114831_, false).reverse();
            Vec3 lerpPosOwner = lerpPos(owner, p_114831_, true);
            p_114832_.translate(lerpPosBeam.x + lerpPosOwner.x, lerpPosBeam.y + lerpPosOwner.y, lerpPosBeam.z + lerpPosOwner.z);

            float ownerClampedYHeadRot = owner.getYHeadRot() % 360;
            float xOffset = (float) (Math.cos((ownerClampedYHeadRot - 90) * (Math.PI / 180)) * 0.4F);
            float yOffset = (float) (Math.sin((owner.getXRot() + 90) * (Math.PI / 180)) - 1) * 0.2F;
            float zOffset = (float) (Math.sin((ownerClampedYHeadRot - 90) * (Math.PI / 180)) * 0.4F);

            Vec3 newOffset = new Vec3(xOffset * (owner.getXRot() / 90), yOffset, zOffset * (owner.getXRot() / 90));
            Vec3 ownerOffset = lerpedOffset(beam, p_114831_, newOffset);
            beam.offsetOld = ownerOffset;
            Vec3 vec3 = (Vec3) Utils.getSableAdjustedPos(beam.level(), Utils.rayCastRaw(owner, p_114831_, (double) IConfig.COMMON.beamReactorHelmetMaxRange.get()).getLocation());
            Vec3 vec31 = this.getUnlerpedPos(owner, new Vec3(ownerOffset.x, ownerOffset.y + owner.getEyeHeight() + 0.2, ownerOffset.z));
            Vec3 vec32 = vec3.subtract(vec31);
            p_114832_.translate(-ownerOffset.x, ownerOffset.y, -ownerOffset.z);
            float f4 = (float)(vec32.length());
            vec32 = vec32.normalize();
            float f5 = (float)Math.acos(vec32.y);
            float f6 = (float)Math.atan2(vec32.z, vec32.x);
            p_114832_.mulPose(Axis.YP.rotationDegrees((((float)Math.PI / 2F) - f6) * (180F / (float)Math.PI)));
            p_114832_.mulPose(Axis.XP.rotationDegrees(f5 * (180F / (float)Math.PI)));
            float f7 = f1 * 0.05F * -1.5F;
            float f8 = f * f;
            int j = 64 + (int)(f8 * 191.0F);
            int k = 32 + (int)(f8 * 191.0F);
            int l = 128 - (int)(f8 * 64.0F);
            float f11 = Mth.cos(f7 + 2.3561945F) * 0.282F;
            float f12 = Mth.sin(f7 + 2.3561945F) * 0.282F;
            float f13 = Mth.cos(f7 + ((float)Math.PI / 4F)) * 0.282F;
            float f14 = Mth.sin(f7 + ((float)Math.PI / 4F)) * 0.282F;
            float f15 = Mth.cos(f7 + 3.926991F) * 0.282F;
            float f16 = Mth.sin(f7 + 3.926991F) * 0.282F;
            float f17 = Mth.cos(f7 + 5.4977875F) * 0.282F;
            float f18 = Mth.sin(f7 + 5.4977875F) * 0.282F;
            float f19 = Mth.cos(f7 + (float)Math.PI) * 0.2F;
            float f20 = Mth.sin(f7 + (float)Math.PI) * 0.2F;
            float f21 = Mth.cos(f7 + 0.0F) * 0.2F;
            float f22 = Mth.sin(f7 + 0.0F) * 0.2F;
            float f23 = Mth.cos(f7 + ((float)Math.PI / 2F)) * 0.2F;
            float f24 = Mth.sin(f7 + ((float)Math.PI / 2F)) * 0.2F;
            float f25 = Mth.cos(f7 + ((float)Math.PI * 1.5F)) * 0.2F;
            float f26 = Mth.sin(f7 + ((float)Math.PI * 1.5F)) * 0.2F;
            float f29 = -1.0F + f2;
            float f30 = f4 * 2.5F + f29;
            VertexConsumer vertexconsumer = p_114833_.getBuffer(BEAM_RENDER_TYPE);
            PoseStack.Pose posestack$pose = p_114832_.last();
            Matrix4f matrix4f = posestack$pose.pose();
            Matrix3f matrix3f = posestack$pose.normal();
            vertex(vertexconsumer, matrix4f, matrix3f, f19, f4, f20, j, k, l, 0.4999F, f30);
            vertex(vertexconsumer, matrix4f, matrix3f, f19, 0.0F, f20, j, k, l, 0.4999F, f29);
            vertex(vertexconsumer, matrix4f, matrix3f, f21, 0.0F, f22, j, k, l, 0.0F, f29);
            vertex(vertexconsumer, matrix4f, matrix3f, f21, f4, f22, j, k, l, 0.0F, f30);
            vertex(vertexconsumer, matrix4f, matrix3f, f23, f4, f24, j, k, l, 0.4999F, f30);
            vertex(vertexconsumer, matrix4f, matrix3f, f23, 0.0F, f24, j, k, l, 0.4999F, f29);
            vertex(vertexconsumer, matrix4f, matrix3f, f25, 0.0F, f26, j, k, l, 0.0F, f29);
            vertex(vertexconsumer, matrix4f, matrix3f, f25, f4, f26, j, k, l, 0.0F, f30);
            float f31 = 0.0F;
            if (beam.tickCount % 2 == 0) {
                f31 = 0.5F;
            }

            vertex(vertexconsumer, matrix4f, matrix3f, f11, f4, f12, j, k, l, 0.5F, f31 + 0.5F);
            vertex(vertexconsumer, matrix4f, matrix3f, f13, f4, f14, j, k, l, 1.0F, f31 + 0.5F);
            vertex(vertexconsumer, matrix4f, matrix3f, f17, f4, f18, j, k, l, 1.0F, f31);
            vertex(vertexconsumer, matrix4f, matrix3f, f15, f4, f16, j, k, l, 0.5F, f31);
            p_114832_.popPose();
        }
    }

    private static void vertex(VertexConsumer p_114842_, Matrix4f p_114843_, Matrix3f p_114844_, float p_114845_, float p_114846_, float p_114847_, int p_114848_, int p_114849_, int p_114850_, float p_114851_, float p_114852_) {
        p_114842_.addVertex(p_114843_, p_114845_, p_114846_, p_114847_).setColor(p_114848_, p_114849_, p_114850_, 255).setUv(p_114851_, p_114852_).setOverlay(OverlayTexture.NO_OVERLAY).setLight(15728880).setNormal(0.0F, 1.0F, 0.0F);
    }


    @Override
    public ResourceLocation getTextureLocation(BeamReactorLaserEntity p_114482_) {
        return null;
    }
}
