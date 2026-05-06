package com.ibarnstormer.gbd.block;

import com.ibarnstormer.gbd.config.IConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class AdvancedGuardianBeamTurretBlockEntity extends AbstractGuardianBeamTurretBlockEntity {

    public AdvancedGuardianBeamTurretBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state, IConfig.COMMON.advancedGuardianBeamTurretMaxTargets.get(), IConfig.COMMON.advancedGuardianBeamTurretMaxRange.get(), IConfig.COMMON.advancedGuardianBeamTurretPhysicalDamage.get(), IConfig.COMMON.advancedGuardianBeamTurretMagicDamage.get(), IConfig.COMMON.advancedGuardianBeamTurretIgnoresInvulFrames.get());
    }
}
