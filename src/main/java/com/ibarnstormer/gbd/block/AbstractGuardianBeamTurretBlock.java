package com.ibarnstormer.gbd.block;

import com.ibarnstormer.gbd.Main;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

import java.util.UUID;

public abstract class AbstractGuardianBeamTurretBlock extends DirectionalKineticBlock implements IWrenchable {

    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    protected AbstractGuardianBeamTurretBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(LIT, Boolean.FALSE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face == state.getValue(FACING).getOpposite();
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(FACING).getOpposite().getAxis();
    }

    @Override
    public SpeedLevel getMinimumRequiredSpeedLevel() {
        return SpeedLevel.MEDIUM;
    }

    @Override
    public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        if(world instanceof ServerLevel server && player != null && !player.isCreative()) {
            BlockEntity be = context.getLevel().getBlockEntity(pos);
            if(be instanceof AbstractGuardianBeamTurretBlockEntity turret) {
                UUID ownerUUID = turret.getOwnerUUID();
                if((ownerUUID != null && server.getEntity(ownerUUID) != player) || (ownerUUID == null && !player.isCreative())) {
                    player.displayClientMessage(Component.translatable(Main.MODID + ".turret.wrench_error"), true);
                    return InteractionResult.FAIL;
                }
            }
        }
        return super.onSneakWrenched(state, context);
    }

}
