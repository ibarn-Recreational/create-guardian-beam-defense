package com.ibarnstormer.gbd.registry;

import com.ibarnstormer.gbd.block.AdvancedGuardianBeamTurretBlock;
import com.ibarnstormer.gbd.block.BasicGuardianBeamTurretBlock;
import com.ibarnstormer.gbd.block.GuardianBeamTurretMovementBehaviour;
import com.simibubi.create.api.behaviour.movement.MovementBehaviour;
import com.simibubi.create.api.stress.BlockStressValues;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;
import static com.ibarnstormer.gbd.Main.REGISTRATE;

public class ModBlocks {

    public static final BlockEntry<BasicGuardianBeamTurretBlock> BASIC_LASER_TURRET_BLOCK = REGISTRATE.block("basic_laser_turret", BasicGuardianBeamTurretBlock::new)
            .initialProperties(() -> Blocks.PRISMARINE)
            .blockstate(BlockStateGen.directionalBlockProvider(true))
            .properties(BlockBehaviour.Properties::noOcclusion)
            .properties(p -> p.strength(600.0F, 1200.0F).lightLevel((b) -> b.getValue(BlockStateProperties.LIT) ? 8 : 0))
            .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
            .transform(pickaxeOnly())
            .onRegister(MovementBehaviour.movementBehaviour(new GuardianBeamTurretMovementBehaviour()))
            .item()
            .transform(customItemModel())
            .onRegister((block) -> BlockStressValues.IMPACTS.register(block, () -> 4))
            .register();

    public static final BlockEntry<AdvancedGuardianBeamTurretBlock> ADVANCED_LASER_TURRET_BLOCK = REGISTRATE.block("advanced_laser_turret", AdvancedGuardianBeamTurretBlock::new)
            .initialProperties(() -> Blocks.DARK_PRISMARINE)
            .blockstate(BlockStateGen.directionalBlockProvider(true))
            .properties(BlockBehaviour.Properties::noOcclusion)
            .properties(p -> p.strength(600.0F, 1200.0F).lightLevel((b) -> b.getValue(BlockStateProperties.LIT) ? 8 : 0))
            .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
            .transform(pickaxeOnly())
            .onRegister(MovementBehaviour.movementBehaviour(new GuardianBeamTurretMovementBehaviour()))
            .item()
            .properties(p -> p.rarity(Rarity.UNCOMMON))
            .transform(customItemModel())
            .onRegister((block) -> BlockStressValues.IMPACTS.register(block, () -> 8))
            .register();

    public static void register() {}

}
