package com.ibarnstormer.gbd.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.contraptions.bearing.BearingBlock;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import dev.engine_room.flywheel.api.visualization.VisualizationManager;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.Objects;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.FACING;

public class GuardianBeamBlockEntityRenderer<T extends AbstractGuardianBeamTurretBlockEntity> extends KineticBlockEntityRenderer<T> {

    public GuardianBeamBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(T be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        super.renderSafe(be, partialTicks, ms, buffer, light, overlay);
        if (VisualizationManager.supportsVisualization(be.getLevel())) return;

        Direction direction = getRenderedBlockState(be).getValue(FACING);
        SuperByteBuffer shaftHalf = CachedBuffers.partialFacing(AllPartialModels.SHAFT_HALF, be.getBlockState(), direction.getOpposite());
        int lightBehind = LevelRenderer.getLightColor(Objects.requireNonNull(be.getLevel()), be.getBlockPos().relative(direction.getOpposite()));

        standardKineticRotationTransform(shaftHalf, be, lightBehind).renderInto(ms, buffer.getBuffer(RenderType.cutoutMipped()));
    }

    @Override
    protected SuperByteBuffer getRotatedModel(AbstractGuardianBeamTurretBlockEntity be, BlockState state) {
        return CachedBuffers.partialFacing(AllPartialModels.SHAFT_HALF, state, state
                .getValue(AbstractGuardianBeamTurretBlock.FACING)
                .getOpposite());
    }
}
