package com.ibarnstormer.gbd.entities;

import com.ibarnstormer.gbd.registry.ModEntities;
import com.ibarnstormer.gbd.utils.Utils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.UUID;

public class BeamReactorLaserEntity extends Entity {

    private static final EntityDataAccessor<Integer> DATA_TICK = SynchedEntityData.defineId(BeamReactorLaserEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_CACHED_OWNER_ID = SynchedEntityData.defineId(BeamReactorLaserEntity.class, EntityDataSerializers.INT);

    @Nullable
    private UUID ownerUUID;
    @Nullable
    private Entity cachedOwner;
    private int clientSideAttackTime;
    private int beamTick;

    //Clientside only
    public Vec3 offsetOld = Vec3.ZERO;
    protected double lerpX, lerpY, lerpZ;
    protected int lerpSteps;

    public BeamReactorLaserEntity(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_TICK, 0);
        builder.define(DATA_CACHED_OWNER_ID, 0);
    }

    public BeamReactorLaserEntity(Level level, Entity owner) {
        super(ModEntities.BEAM_REACTOR_LASER_ENTITY.get(), level);
        entityData.set(DATA_TICK, 0);
        cachedOwner = owner;
        ownerUUID = owner.getUUID();
        entityData.set(DATA_CACHED_OWNER_ID, owner.getId());
        beamTick = 0;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag p_20052_) {
        if (p_20052_.hasUUID("Owner")) {
            this.ownerUUID = p_20052_.getUUID("Owner");
        }
    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag p_20139_) {
        if (this.ownerUUID != null) {
            p_20139_.putUUID("Owner", this.ownerUUID);
        }
    }

    @Override
    public void tick() {
        super.tick();
        this.noCulling = true;
        if(entityData.get(DATA_TICK) < 40) entityData.set(DATA_TICK, entityData.get(DATA_TICK) + 1);
        this.beamTick++;
        if(level().isClientSide && clientSideAttackTime < 41) clientSideAttackTime++;
        if(!level().isClientSide) {
            if(getOwner() instanceof Player player) {
                if(Utils.getLaserID(player) != this.getId()) {
                    this.discard();
                }
            }
        }
        if(!this.isRemoved()) lerpPosition();
        if(this.getOwner() != null) {
            this.setPos(this.getOwner().position().x, this.getOwner().getEyeY() + 0.20, this.getOwner().position().z);
        }
    }

    @Override
    public void lerpTo(double p_20977_, double p_20978_, double p_20979_, float p_20980_, float p_20981_, int p_20982_) {
        this.lerpX = p_20977_;
        this.lerpY = p_20978_;
        this.lerpZ = p_20979_;
        this.lerpSteps = p_20982_;
    }

    public void lerpPosition() {
        if (this.lerpSteps > 0) {
            double d0 = this.getX() + (this.lerpX - this.getX()) / (double)this.lerpSteps;
            double d2 = this.getY() + (this.lerpY - this.getY()) / (double)this.lerpSteps;
            double d4 = this.getZ() + (this.lerpZ - this.getZ()) / (double)this.lerpSteps;
            --this.lerpSteps;
            this.setPos(d0, d2, d4);
            this.setRot(this.getYRot(), this.getXRot());
        }
    }

    public float getAttackAnimationScale(float p_32813_) {
        return ((float)this.clientSideAttackTime + p_32813_) / 40;
    }

    @Nullable
    public Entity getOwner() {
        if (this.cachedOwner != null && !this.cachedOwner.isRemoved()) {
            return this.cachedOwner;
        } else if (this.ownerUUID != null && this.level() instanceof ServerLevel) {
            this.cachedOwner = ((ServerLevel)this.level()).getEntity(this.ownerUUID);
            return this.cachedOwner;
        } else {
            return null;
        }
    }

    public int getTick() {
        return this.entityData.get(DATA_TICK);
    }

    public int getBeamTick() {
        return this.beamTick;
    }

    public int getClientSideAttackTime() {
        return clientSideAttackTime;
    }

    @Nullable
    public Entity getCachedOwner() {
        if(this.level().isClientSide) {
            Entity owner = this.level().getEntity(this.entityData.get(DATA_CACHED_OWNER_ID));
            if(owner instanceof LivingEntity livingOwner) return livingOwner;
            else return null;
        }
        if(this.getOwner() instanceof LivingEntity livingOwner) return livingOwner;
        else return null;
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public boolean hurt(@NotNull DamageSource p_37616_, float p_37617_) {
        return false;
    }

}
