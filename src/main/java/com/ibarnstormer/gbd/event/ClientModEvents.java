package com.ibarnstormer.gbd.event;

import com.ibarnstormer.gbd.Main;
import com.ibarnstormer.gbd.client.keybinds.KeyBindings;
import com.ibarnstormer.gbd.config.IConfig;
import com.ibarnstormer.gbd.entities.BeamReactorLaserEntity;
import com.ibarnstormer.gbd.entities.TurretBeamLaserEntity;
import com.ibarnstormer.gbd.network.BeamReactorRayTracePayload;
import com.ibarnstormer.gbd.network.ToggleBeamReactorPayload;
import com.ibarnstormer.gbd.sounds.BeamReactorSoundInstance;
import com.ibarnstormer.gbd.sounds.TurretBeamSoundInstance;
import com.ibarnstormer.gbd.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Position;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.HitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

public class ClientModEvents {

    @EventBusSubscriber(modid = Main.MODID, value = Dist.CLIENT)
    public static class ClientForgeEvents {
        @SubscribeEvent
        public static void onKeyInput(final InputEvent.Key event) {
            if(event.getKey() == KeyBindings.BEAM_REACTOR_TOGGLE.getKey().getValue() && event.getAction() == 1) {
                handleKeyPress(event.getKey());
            }
        }

        @SubscribeEvent
        public static void playerTick(PlayerTickEvent.Post event) {
            Player player = event.getEntity();

            // If we are firing the laser
            if(Utils.isBeamLaserEnabled(player) && Utils.canActivateLaser(player)) {
                HitResult ray = Utils.rayCast(player, Minecraft.getInstance(), (double) IConfig.COMMON.beamReactorHelmetMaxRange.get());
                Position adjustedPos = Utils.getSableAdjustedPos(player.level(), ray.getLocation());

                PacketDistributor.sendToServer(new BeamReactorRayTracePayload(adjustedPos.x(), adjustedPos.y(), adjustedPos.z(), player.getGameProfile()));
            }
        }

        private static void handleKeyPress(int key) {
            if(key == KeyBindings.BEAM_REACTOR_TOGGLE.getKey().getValue() && Minecraft.getInstance().screen == null) {
                LocalPlayer clientPlayer = Minecraft.getInstance().player;

                assert clientPlayer != null;
                PacketDistributor.sendToServer(new ToggleBeamReactorPayload(!Utils.isBeamLaserEnabled(clientPlayer), clientPlayer.getGameProfile()));
            }
        }


        public static void handleBeamReactorSound(Entity sourcePlayer, Entity beam) {
            Minecraft.getInstance().getSoundManager().play(new BeamReactorSoundInstance((Player) sourcePlayer, (BeamReactorLaserEntity) beam));
        }

        public static void handleTurretBeamSound(Entity laser) {
            Minecraft.getInstance().getSoundManager().play(new TurretBeamSoundInstance((TurretBeamLaserEntity) laser));
        }

    }

    @EventBusSubscriber(modid = Main.MODID, value = Dist.CLIENT)
    public static class ClientModBusEvents {
        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            event.register(KeyBindings.BEAM_REACTOR_TOGGLE);
        }
    }

}
