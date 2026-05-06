package com.ibarnstormer.gbd.block;

import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.SingleAxisRotatingVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.model.Models;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class GuardianBeamTurretVisual extends SingleAxisRotatingVisual<AbstractGuardianBeamTurretBlockEntity> {

    public GuardianBeamTurretVisual(VisualizationContext context, AbstractGuardianBeamTurretBlockEntity blockEntity, float partialTick) {
        super(context, blockEntity, partialTick, correctDirection(blockEntity.getBlockState()), Models.partial(AllPartialModels.SHAFT_HALF));

    }

    private static Direction correctDirection(BlockState state) {
        Direction direction = state.getValue(BlockStateProperties.FACING);

        Direction correctDir = Direction.NORTH;

        switch (direction) {
            case UP:
            case EAST:
            case SOUTH:
            {
                break;
            }
            case DOWN:
            case WEST:
            case NORTH:
            {
                correctDir = Direction.SOUTH;
                break;
            }
        }
        return correctDir;
    }
}
