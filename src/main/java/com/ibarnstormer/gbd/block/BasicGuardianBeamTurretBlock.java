package com.ibarnstormer.gbd.block;

import com.ibarnstormer.gbd.registry.ModTileEntities;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class BasicGuardianBeamTurretBlock extends AbstractGuardianBeamTurretBlock implements IBE<BasicGuardianBeamTurretBlockEntity> {

    public BasicGuardianBeamTurretBlock(Properties properties) {
        super(properties);
    }

    @Override
    public Class<BasicGuardianBeamTurretBlockEntity> getBlockEntityClass() {
        return BasicGuardianBeamTurretBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends BasicGuardianBeamTurretBlockEntity> getBlockEntityType() {
        return ModTileEntities.BASIC_LASER_TURRET_TILE.get();
    }

    @Override
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, @Nullable LivingEntity livingEntity, ItemStack stack) {
        super.setPlacedBy(level, blockPos, blockState, livingEntity, stack);
        withBlockEntityDo(level, blockPos, be -> {
            if(livingEntity != null) be.setOwner(livingEntity);
        });
    }
}
