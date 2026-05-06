package com.ibarnstormer.gbd.proxies;

import com.ibarnstormer.gbd.client.renderer.BeamReactorLaserEntityRenderer;
import com.ibarnstormer.gbd.Main;
import com.ibarnstormer.gbd.client.renderer.TurretLaserBeamEntityRenderer;
import com.ibarnstormer.gbd.event.ModEvents;
import com.ibarnstormer.gbd.registry.ModEntities;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.common.NeoForge;

@EventBusSubscriber(modid = Main.MODID, value = Dist.CLIENT)
public class ClientSetup {

    @SubscribeEvent
    public static void init(final FMLClientSetupEvent event) {
        EntityRenderers.register(ModEntities.BEAM_REACTOR_LASER_ENTITY.get(), BeamReactorLaserEntityRenderer::new);
        EntityRenderers.register(ModEntities.TURRET_BEAM_LASER_ENTITY.get(), TurretLaserBeamEntityRenderer::new);

        NeoForge.EVENT_BUS.register(ModEvents.class);
    }

}
