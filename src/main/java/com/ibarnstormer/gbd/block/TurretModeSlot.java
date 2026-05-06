package com.ibarnstormer.gbd.block;

import com.simibubi.create.foundation.blockEntity.behaviour.CenteredSideValueBoxTransform;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class TurretModeSlot extends CenteredSideValueBoxTransform {

    public TurretModeSlot() {
        super((state, d) -> d != state.getValue(BlockStateProperties.FACING).getOpposite());
    }

}
