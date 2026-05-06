package com.ibarnstormer.gbd.registry;

import com.ibarnstormer.gbd.ModGroup;
import com.ibarnstormer.gbd.block.*;
import com.ibarnstormer.gbd.block.AdvancedGuardianBeamTurretBlockEntity;
import com.ibarnstormer.gbd.block.BasicGuardianBeamTurretBlockEntity;
import com.simibubi.create.content.kinetics.base.ShaftVisual;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

import static com.ibarnstormer.gbd.Main.REGISTRATE;

public class ModTileEntities {

    public static final BlockEntityEntry<BasicGuardianBeamTurretBlockEntity> BASIC_LASER_TURRET_TILE = REGISTRATE.blockEntity("basic_laser_turret", BasicGuardianBeamTurretBlockEntity::new)
            .visual(() -> GuardianBeamTurretVisual::new)
            .renderer(() -> GuardianBeamBlockEntityRenderer::new)
            .validBlocks(ModBlocks.BASIC_LASER_TURRET_BLOCK)
            .register();

    public static final BlockEntityEntry<AdvancedGuardianBeamTurretBlockEntity> ADVANCED_LASER_TURRET_TILE = REGISTRATE.blockEntity("advanced_laser_turret", AdvancedGuardianBeamTurretBlockEntity::new)
            .visual(() -> GuardianBeamTurretVisual::new)
            .renderer(() -> GuardianBeamBlockEntityRenderer::new)
            .validBlocks(ModBlocks.ADVANCED_LASER_TURRET_BLOCK)
            .register();

    public static void register() {}
}
