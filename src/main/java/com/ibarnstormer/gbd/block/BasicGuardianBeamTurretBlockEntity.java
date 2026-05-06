package com.ibarnstormer.gbd.block;

import com.ibarnstormer.gbd.config.IConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BasicGuardianBeamTurretBlockEntity extends AbstractGuardianBeamTurretBlockEntity {

    public BasicGuardianBeamTurretBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state, IConfig.COMMON.basicGuardianBeamTurretMaxTargets.get(), IConfig.COMMON.basicGuardianBeamTurretMaxRange.get(), IConfig.COMMON.basicGuardianBeamTurretPhysicalDamage.get(), IConfig.COMMON.basicGuardianBeamTurretMagicDamage.get(), IConfig.COMMON.basicGuardianBeamTurretIgnoresInvulFrames.get());
    }

}
