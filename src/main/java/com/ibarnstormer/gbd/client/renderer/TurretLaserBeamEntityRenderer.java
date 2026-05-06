package com.ibarnstormer.gbd.client.renderer;

import com.ibarnstormer.gbd.block.AbstractGuardianBeamTurretBlockEntity;
import com.ibarnstormer.gbd.entities.TurretBeamLaserEntity;
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
import net.minecraft.core.Position;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class TurretLaserBeamEntityRenderer extends EntityRenderer<TurretBeamLaserEntity> {

    private static final ResourceLocation GUARDIAN_BEAM_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/guardian_beam.png");
    private static final RenderType BEAM_RENDER_TYPE = RenderType.entityCutoutNoCull(GUARDIAN_BEAM_LOCATION);

    public TurretLaserBeamEntityRenderer(EntityRendererProvider.Context p_174008_) {
        super(p_174008_);
    }

    @Override
    public ResourceLocation getTextureLocation(TurretBeamLaserEntity p_114482_) {
        return null;
    }

    protected int getBlockLightLevel(TurretBeamLaserEntity p_114496_, BlockPos p_114497_) {
        return 15;
    }

    protected int getSkyLightLevel(TurretBeamLaserEntity p_114509_, BlockPos p_114510_) {
        return 15;
    }

    private Vec3 getPosition(Entity p_114803_, double p_114804_, float p_114805_) {
        double d0 = p_114803_.xo + (p_114803_.position().x - p_114803_.xo) * p_114805_;
        double d1 = (p_114803_.yo + (p_114803_.position().y - p_114803_.yo) * p_114805_) + p_114804_;
        double d2 = p_114803_.zo + (p_114803_.position().z - p_114803_.zo) * p_114805_;
        return new Vec3(d0, d1, d2);
    }

    public void render(TurretBeamLaserEntity beam, float p_114830_, float p_114831_, PoseStack p_114832_, MultiBufferSource p_114833_, int p_114834_) {
        super.render(beam, p_114830_, p_114831_, p_114832_, p_114833_, p_114834_);
        Entity target = beam.getCachedTarget();
        if(target != null && beam.getClientSideAttackTime() > 1) {
            float f = beam.getAttackAnimationScale(p_114831_);
            float f1 = (float)beam.getBeamTick() + p_114831_;
            float f2 = f1 * 0.5F % 1.0F;
            p_114832_.pushPose();
            p_114832_.translate(0.0D, 0.0D, 0.0D);

            Vec3 vec3 = this.getPosition(target, (double)target.getBbHeight() * 0.5D, p_114831_);
            Vec3 vec31 = this.getPosition(beam, 0.0D, p_114831_);
            Vec3 vec32 = vec3.subtract(vec31);
            float f4 = (float)(vec32.length() + 1.0D);
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

}
