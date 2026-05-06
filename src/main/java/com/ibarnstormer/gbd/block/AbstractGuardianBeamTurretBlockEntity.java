package com.ibarnstormer.gbd.block;

import com.ibarnstormer.gbd.Main;
import com.ibarnstormer.gbd.entities.TurretBeamLaserEntity;
import com.ibarnstormer.gbd.network.BroadcastTurretBeamSoundPayload;
import com.ibarnstormer.gbd.registry.ModIcons;
import com.ibarnstormer.gbd.utils.ProtectedScrollOptionBehaviour;
import com.ibarnstormer.gbd.utils.Utils;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.INamedIconOptions;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.utility.CreateLang;
import com.simibubi.create.infrastructure.config.AllConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ambient.AmbientCreature;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class AbstractGuardianBeamTurretBlockEntity extends KineticBlockEntity {

    protected ProtectedScrollOptionBehaviour<LaserTargetingConditions> targetingConditions;
    private final HashMap<LivingEntity, TurretBeamLaserEntity> targetPairs;
    private final int MAP_SIZE;
    private final int MAX_RANGE;
    private final int MOB_DAMAGE;
    private final int MAGIC_DAMAGE;
    private final boolean IGNORE_INVUL_FRAMES;
    private @Nullable UUID ownerUUID;

    protected AbstractGuardianBeamTurretBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state, int maxTargets, int maxRange, int mobDamage, int magicDamage, boolean ignoreInvulFrames) {
        super(typeIn, pos, state);
        targetPairs = new HashMap<>();
        MAP_SIZE = maxTargets;
        MAX_RANGE = maxRange;
        MOB_DAMAGE = mobDamage;
        MAGIC_DAMAGE = magicDamage;
        IGNORE_INVUL_FRAMES = ignoreInvulFrames;
    }

    public boolean isAttacking() {
        return !targetPairs.isEmpty();
    }

    @Override
    public void tick() {
        super.tick();
        BlockPos pos = getBlockPos();
        assert level != null;
        if(targetingConditions.getOwnerUUID() == null && ownerUUID != null) targetingConditions.updateOwner(ownerUUID);

        if(level.getBlockEntity(pos) instanceof AbstractGuardianBeamTurretBlockEntity) {
            BlockState state = level.getBlockState(pos);
            if (Math.abs(getSpeed()) >= AllConfigs.server().kinetics.mediumSpeed.get()) {
                if (!state.getValue(BlockStateProperties.LIT))
                    level.setBlock(pos, state.setValue(BlockStateProperties.LIT, Boolean.TRUE), 2);
                float chargeSpeed = Math.max((190 - Math.round(Math.log(Math.abs(getSpeed())) / Math.log(2)) * 20), 30);
                if (targetPairs.size() < MAP_SIZE) {
                    AABB box = new AABB(pos).inflate(MAX_RANGE);
                    List<LivingEntity> list = level.getEntitiesOfClass(LivingEntity.class, box);

                    Entity owner;
                    if (ownerUUID != null && !level.isClientSide) owner = ((ServerLevel) level).getEntity(ownerUUID);
                    else owner = null;

                    for (LivingEntity target : list) {
                        if (!this.targetPairs.containsKey(target) && !target.isDeadOrDying() && this.parseTargetCriteria(target, this.level) && (Utils.hasLOS(target, pos, this.level) || Utils.posIsPartOfSableAssembly(this.level, pos.getCenter())) && !Utils.isTeammate(owner, target)) {
                            TurretBeamLaserEntity laser = new TurretBeamLaserEntity(this.level, target, this, chargeSpeed, this.MAX_RANGE, this.MAGIC_DAMAGE, this.MOB_DAMAGE, this.IGNORE_INVUL_FRAMES);
                            laser.setPos(((Vec3) Utils.getSableAdjustedPos(level, pos.getCenter())).add(Utils.getSableAdjustedVelocity(level, pos.getCenter()).scale((double) 1 / 7.5)));
                            if (!this.level.isClientSide) this.level.addFreshEntity(laser);
                            this.targetPairs.put(target, laser);
                        }
                        if (targetPairs.size() >= MAP_SIZE) break;
                    }
                }


                Iterator<LivingEntity> targetIterator = targetPairs.keySet().iterator();
                while (targetIterator.hasNext()) {

                    LivingEntity target = targetIterator.next();
                    TurretBeamLaserEntity laser = targetPairs.get(target);
                    laser.setPos(((Vec3) Utils.getSableAdjustedPos(level, pos.getCenter())).add(Utils.getSableAdjustedVelocity(level, pos.getCenter()).scale((double) 1 / 7.5)));
                    if (laser.getTick() % 10 == 0 && level instanceof ServerLevel serverLevel) {
                        PacketDistributor.sendToPlayersNear(serverLevel, null, pos.getX(), pos.getY(), pos.getZ(), 50, new BroadcastTurretBeamSoundPayload(laser.getId()));
                    }

                    if (discardConditions(laser, target, pos.getCenter(), level)) {
                        targetIterator.remove();

                        if (laser.getTick() >= laser.getChargeSpeed() + 1) {
                            if (ownerUUID != null && target.isDeadOrDying() && level instanceof ServerLevel serverLevel) {
                                Entity owner = serverLevel.getEntity(ownerUUID);
                                if (owner instanceof Player player)
                                    player.giveExperiencePoints(target.getExperienceReward(serverLevel, owner));
                            }
                        }
                    }
                }
            } else if (Math.abs(getSpeed()) == 0 || !hasSource()) {
                if (!level.isClientSide && state.getValue(BlockStateProperties.LIT))
                    level.setBlock(pos, state.setValue(BlockStateProperties.LIT, Boolean.FALSE), 2);
                if (!targetPairs.isEmpty()) {
                    targetPairs.clear();
                }
            } else if (!level.isClientSide && state.getValue(BlockStateProperties.LIT))
                level.setBlock(pos, state.setValue(BlockStateProperties.LIT, Boolean.FALSE), 2);
        }
    }

    @Override
    public void remove() {
        super.remove();
        if(!targetPairs.isEmpty()) {
            targetPairs.clear();
        }
    }

    @Override
    public void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        if(ownerUUID != null) compound.putUUID("ownerUUID", ownerUUID);
        super.write(compound, registries, clientPacket);
    }

    @Override
    public void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        if(compound.contains("ownerUUID")) ownerUUID = compound.getUUID("ownerUUID");
        super.read(compound, registries, clientPacket);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        targetingConditions = new ProtectedScrollOptionBehaviour<>(LaserTargetingConditions.class, Component.translatable(Main.MODID + ".turret_beam_targeting_options"), this, new TurretModeSlot(), ownerUUID);
        behaviours.add(targetingConditions);
    }

    public void setOwner(Entity owner) {
        ownerUUID = owner.getUUID();
    }

    public @Nullable UUID getOwnerUUID() {
        return ownerUUID;
    }

    private boolean discardConditions(TurretBeamLaserEntity laser, LivingEntity target, Vec3 center, Level level) {
        return laser.getTick() >= laser.getChargeSpeed() + 5 ||
                laser.isRemoved() ||
                (target.isDeadOrDying() && (!Utils.posIsPartOfSableAssembly(level, center) || target.isRemoved())) ||
                (!Utils.hasLOS(target, BlockPos.containing(Utils.getSableAdjustedPos(level, center)), level) && !Utils.posIsPartOfSableAssembly(level, center));
    }

    private boolean parseTargetCriteria(LivingEntity target, Level level) {
        if(ownerUUID != null && !level.isClientSide) {
            Entity owner = ((ServerLevel) level).getEntity(ownerUUID);
            if(owner == target || target instanceof TamableAnimal tamable && tamable.getOwner() == owner) return false;
        }

        boolean isEnemy = target instanceof Enemy;
        boolean isPassive = target instanceof Animal || target instanceof AmbientCreature || target instanceof AbstractVillager;
        boolean isPlayer = target instanceof Player player && !player.isSpectator() && !player.isCreative();
        switch (targetingConditions.get()) {
            case HOSTILE -> {
                return isEnemy;
            }
            case PASSIVE -> {
                return isPassive;
            }
            case PLAYER -> {
                return isPlayer;
            }
            case HOSTILE_PLAYER -> {
                return isEnemy || isPlayer;
            }
            case HOSTILE_PASSIVE -> {
                return isEnemy || isPassive;
            }
            case PLAYER_PASSIVE -> {
                return isPlayer || isPassive;
            }
            case EVERYTHING -> {
                return !(target instanceof Player) || isPlayer;
            }
            default -> {
                return false;
            }
        }
    }

    public enum LaserTargetingConditions implements INamedIconOptions {
            HOSTILE(ModIcons.I_HOSTILE),
            PASSIVE(ModIcons.I_PASSIVE),
            PLAYER(ModIcons.I_PLAYER),
            HOSTILE_PASSIVE(ModIcons.I_HOSTILE_PASSIVE),
            HOSTILE_PLAYER(ModIcons.I_HOSTILE_PLAYER),
            PLAYER_PASSIVE(ModIcons.I_PLAYER_PASSIVE),
            EVERYTHING(ModIcons.I_EVERYTHING)
        ;

        private final String translationKey;
        private final ModIcons icon;

        LaserTargetingConditions(ModIcons icon) {
            this.icon = icon;
            translationKey = Main.MODID + ".turret.targeting." + CreateLang.asId(name());
        }

        @Override
        public AllIcons getIcon() {
            return icon;
        }

        @Override
        public String getTranslationKey() {
            return translationKey;
        }
    }

}
