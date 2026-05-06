package com.ibarnstormer.gbd.block;

import com.ibarnstormer.gbd.entities.TurretBeamLaserEntity;
import com.simibubi.create.api.behaviour.movement.MovementBehaviour;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;

import java.util.*;

public class GuardianBeamTurretMovementBehaviour implements MovementBehaviour {

    public GuardianBeamTurretMovementBehaviour() {
    }

    @Override
    public void tick(MovementContext context) {
        MovementBehaviour.super.tick(context);
        if(!context.world.isClientSide())
            this.visitNewPosition(context, BlockPos.containing(context.position.x, context.position.y, context.position.z));
    }

    public void visitNewPosition(MovementContext context, BlockPos pos) {
        MovementBehaviour.super.visitNewPosition(context, pos);
        Contraption contraption = context.contraption;
        if (contraption.entity != null) {
            List<TurretBeamLaserEntity> lasers = context.world.getEntitiesOfClass(TurretBeamLaserEntity.class, new AABB(pos).inflate(0.5));
            for (TurretBeamLaserEntity laser : lasers) {
                laser.teleportTo(context.position.x, context.position.y, context.position.z);
            }
        }
    }

}
