package com.ibarnstormer.gbd.registry;

import com.ibarnstormer.gbd.entities.BeamReactorLaserEntity;
import com.ibarnstormer.gbd.Main;
import com.ibarnstormer.gbd.entities.TurretBeamLaserEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, Main.MODID);

    public static void init(IEventBus modEventBus) {
        ENTITY_TYPES.register(modEventBus);
    }

    public static final DeferredHolder<EntityType<?>, EntityType<BeamReactorLaserEntity>> BEAM_REACTOR_LASER_ENTITY = ENTITY_TYPES.register("beam_laser_entity", () -> EntityType.Builder.<BeamReactorLaserEntity>of(BeamReactorLaserEntity::new, MobCategory.MISC)
            .sized(0.325f,0.325f).setTrackingRange(128).setUpdateInterval(1).setShouldReceiveVelocityUpdates(true).clientTrackingRange(128).build(ResourceLocation.fromNamespaceAndPath(Main.MODID, "beam_laser_entity").toString()));

    public static final DeferredHolder<EntityType<?>, EntityType<TurretBeamLaserEntity>> TURRET_BEAM_LASER_ENTITY = ENTITY_TYPES.register("turret_beam_laser_entity", () -> EntityType.Builder.<TurretBeamLaserEntity>of(TurretBeamLaserEntity::new, MobCategory.MISC)
            .sized(0.325f,0.325f).setTrackingRange(128).setUpdateInterval(1).setShouldReceiveVelocityUpdates(true).clientTrackingRange(128).build(ResourceLocation.fromNamespaceAndPath(Main.MODID, "turret_beam_laser_entity").toString()));


}
