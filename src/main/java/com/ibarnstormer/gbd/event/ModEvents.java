package com.ibarnstormer.gbd.event;

import com.ibarnstormer.gbd.Main;
import com.ibarnstormer.gbd.config.IConfig;
import com.ibarnstormer.gbd.entities.BeamReactorLaserEntity;
import com.ibarnstormer.gbd.items.BeamReactorHelmet;
import com.ibarnstormer.gbd.network.BroadcastBeamReactorSoundPayload;
import com.ibarnstormer.gbd.network.SyncBeamReactorAttachmentsPayload;
import com.ibarnstormer.gbd.utils.Utils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.*;

@EventBusSubscriber(modid = Main.MODID)
public class ModEvents {

    @SubscribeEvent
    public static void playerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        // If player is wearing the reactor, allow player to toggle laser beam ability
        Utils.setLaserAllowance(player, player.getInventory().getArmor(3).getItem() instanceof BeamReactorHelmet);

        if((!Utils.isBeamLaserEnabled(player) && Utils.canActivateLaser(player)) || (Utils.isBeamLaserEnabled(player) && !Utils.canActivateLaser(player))) {
            if(player.level() instanceof ServerLevel serverLevel) {
                Entity beam = serverLevel.getEntity(Utils.getLaserID(player));
                if(beam != null) {
                    beam.discard();
                }
                Utils.setLaserID(player, 0);
            }
        }
        if(player instanceof ServerPlayer serverPlayer) {
            PacketDistributor.sendToPlayer(serverPlayer, new SyncBeamReactorAttachmentsPayload(Utils.isBeamLaserEnabled(serverPlayer), Utils.canActivateLaser(serverPlayer), Utils.getLaserID(serverPlayer)));
        }
    }

    public static void handleBeam(Player player, Vec3 ray) {
        if(player.level() instanceof ServerLevel serverLevel) {
            Entity beam = serverLevel.getEntity(Utils.getLaserID(player));
            if (beam == null) {
                BeamReactorLaserEntity newBeam = new BeamReactorLaserEntity(player.level(), player);
                newBeam.setPos(player.getX(), player.getEyeY() + 0.20, player.getZ());
                Utils.setLaserID(player, newBeam.getId());
                serverLevel.addFreshEntity(newBeam);
            }
            else {
                if (beam instanceof BeamReactorLaserEntity laser) {
                    if (laser.getTick() >= 40) {
                        Vec3 vec3d = new Vec3(player.getX(), player.getY() + player.getBbHeight(), player.getZ());
                        HashSet<LivingEntity> damagedEntities = new HashSet<>();
                        AABB box = new AABB(Math.min(player.getX(), ray.x), Math.min(player.getY() + player.getBbHeight(), ray.y), Math.min(player.getZ(), ray.z), Math.max(player.getX(), ray.x), Math.max(player.getY() + player.getBbHeight(), ray.y), Math.max(player.getZ(), ray.z)).inflate(1, 1, 1);
                        List<LivingEntity> list = player.level().getEntitiesOfClass(LivingEntity.class, box);
                        for (LivingEntity entity : list) {
                            if (entity != player && !damagedEntities.contains(entity) && !entity.getPassengers().contains(player)) {
                                float f = entity.getPickRadius() + 0.15f;
                                AABB box1 = entity.getBoundingBox().inflate(f, f, f);
                                Optional<Vec3> hit = box1.clip(vec3d, ray);
                                if (box1.contains(vec3d) || hit.isPresent()) {
                                    double attackDamageScaler = 0;
                                    if(IConfig.COMMON.beamReactorHelmetScalesDamage.get()) {
                                        AttributeInstance damageAttribute = player.getAttribute(Attributes.ATTACK_DAMAGE);
                                        if (damageAttribute != null) {
                                            double base = damageAttribute.getValue();
                                            List<AttributeModifier> modifiers = player.getItemInHand(InteractionHand.MAIN_HAND).getAttributeModifiers().modifiers().stream().filter(e -> e.attribute() == Attributes.ATTACK_DAMAGE && e.slot() == EquipmentSlotGroup.MAINHAND).map(ItemAttributeModifiers.Entry::modifier).toList();
                                            for (AttributeModifier m : modifiers) base -= m.amount();
                                            attackDamageScaler = base - 1;
                                        }
                                    }
                                    entity.hurt(entity.damageSources().indirectMagic(player, player), (float) (IConfig.COMMON.beamReactorHelmetMagicDamage.get() + (attackDamageScaler / 2)));
                                    entity.hurt(entity.damageSources().playerAttack(player), (float) (IConfig.COMMON.beamReactorHelmetPhysicalDamage.get() + attackDamageScaler));
                                    if(IConfig.COMMON.beamReactorHelmetIgnoresInvulFrames.get()) entity.invulnerableTime = 0;
                                    damagedEntities.add(entity);
                                }
                            }
                        }
                        damagedEntities.clear();
                        if (player.tickCount % 20 == 0) {
                            ItemStack reactorStack = player.getInventory().getArmor(3);
                            reactorStack.hurtAndBreak(1, player, EquipmentSlot.HEAD);
                        }
                    }
                    if (player.tickCount % 10 == 0) {
                        PacketDistributor.sendToPlayersNear(serverLevel, null, player.getX(), player.getY(), player.getZ(), 32, new BroadcastBeamReactorSoundPayload(player.getId(), beam.getId()));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.StartTracking event) {
        syncPlayerData(event.getEntity(), false);
    }

    @SubscribeEvent
    public static void onPlayerDisconnect(PlayerEvent.PlayerLoggedOutEvent event) {
        syncPlayerData(event.getEntity(), true);
    }

    @SubscribeEvent
    public static void onPlayerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        syncPlayerData(event.getEntity(), true);
    }

    public static void syncPlayerData(Player player, boolean deactivate) {
        Utils.toggleBeamLaser(player, !deactivate && Utils.isBeamLaserEnabled(player));
        Utils.setLaserAllowance(player, Utils.canActivateLaser(player));
        if(deactivate && player.level() instanceof ServerLevel serverLevel) {
            Entity beam = serverLevel.getEntity(Utils.getLaserID(player));
            if(beam != null) {
                beam.discard();
                Utils.setLaserID(player, 0);
            }
        }
    }

}
