package com.ibarnstormer.gbd.entities;

import com.ibarnstormer.gbd.block.AbstractGuardianBeamTurretBlockEntity;
import com.ibarnstormer.gbd.registry.ModDamageSources;
import com.ibarnstormer.gbd.registry.ModEntities;
import com.ibarnstormer.gbd.utils.Utils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.UUID;

public class TurretBeamLaserEntity extends Entity {

    private static final EntityDataAccessor<Integer> DATA_TICK = SynchedEntityData.defineId(TurretBeamLaserEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> CHARGE_SPEED = SynchedEntityData.defineId(TurretBeamLaserEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> DATA_CACHED_TARGET_ID = SynchedEntityData.defineId(TurretBeamLaserEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> MAX_RANGE = SynchedEntityData.defineId(TurretBeamLaserEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> MAGIC_DAMAGE = SynchedEntityData.defineId(TurretBeamLaserEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> MOB_DAMAGE = SynchedEntityData.defineId(TurretBeamLaserEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> IGNORE_INVUL_FRAMES = SynchedEntityData.defineId(TurretBeamLaserEntity.class, EntityDataSerializers.BOOLEAN);


    @Nullable
    private UUID targetUUID;
    @Nullable
    private Entity cachedTarget;
    @Nullable
    private BlockEntity cachedSource;
    private int clientSideAttackTime;
    private int beamTick;

    protected double lerpX, lerpY, lerpZ;
    protected int lerpSteps;

    public TurretBeamLaserEntity(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    public TurretBeamLaserEntity(Level level, LivingEntity target, BlockEntity source, float chargeSpeed, int maxRange, int magicDamage, int mobDamage, boolean ignoreInvulFrames) {
        super(ModEntities.TURRET_BEAM_LASER_ENTITY.get(), level);
        entityData.set(DATA_CACHED_TARGET_ID, target.getId());
        entityData.set(CHARGE_SPEED, chargeSpeed);
        entityData.set(MAX_RANGE, maxRange);
        entityData.set(MAGIC_DAMAGE, magicDamage);
        entityData.set(MOB_DAMAGE, mobDamage);
        entityData.set(IGNORE_INVUL_FRAMES, ignoreInvulFrames);
        cachedTarget = target;
        cachedSource = source;
        targetUUID = target.getUUID();
        beamTick = 0;
    }

    @Override
    public void tick() {
        super.tick();
        this.noCulling = true;
        // 5 ticks where laser is green
        if(entityData.get(DATA_TICK) < getChargeSpeed() + 5) entityData.set(DATA_TICK, entityData.get(DATA_TICK) + 1);
        this.beamTick++;
        if(level().isClientSide && clientSideAttackTime < getChargeSpeed() + 1) clientSideAttackTime++;
        if(!this.isRemoved()) lerpPosition();

        Entity target = getTarget();
        if(target != null) {
            if(target.level() == this.level() && target.level().dimension() == this.level().dimension()) {
                // Check if beam has LOS with target
                if(cachedSource != null && !Utils.posIsPartOfSableAssembly(level(), cachedSource.getBlockPos().getCenter())) {
                    Vec3 vec3 = new Vec3(target.getX(), target.getY() + target.getBbHeight() * 0.5D, target.getZ());
                    Vec3 vec31 = this.position();
                    Vec3 vec32 = vec31.subtract(vec3);
                    Vec3 vec33 = vec31.subtract(vec32.normalize().scale(1.125));
                    BlockHitResult cast = this.level().clip(new ClipContext(vec33, vec3, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
                    if (cast.getType() == HitResult.Type.BLOCK) {
                        this.discard();
                    }
                }
                double range = this.entityData.get(MAX_RANGE);
                if (this.distanceToSqr(target) > Math.pow(range, 3)) {
                    this.discard();
                }

                // Damage target
                boolean canAttack = entityData.get(IGNORE_INVUL_FRAMES) ? this.getTick() >= this.getChargeSpeed() + 1 : this.getTick() == this.getChargeSpeed() + 1;
                if(canAttack) {
                    if(this.getTick() >= this.getChargeSpeed() + 1) {
                        if(cachedSource != null && cachedSource instanceof AbstractGuardianBeamTurretBlockEntity turret && turret.getOwnerUUID() != null) {
                            Player owner = this.level().getPlayerByUUID(turret.getOwnerUUID());
                            if(owner != null && target instanceof LivingEntity livingEntity) {
                                livingEntity.setLastHurtByPlayer(owner);
                                if(target instanceof INoXPEntity noXPEntity) {
                                    noXPEntity.setExperienceDropAllowance(false);
                                }
                            }
                        }

                        target.hurt(target.damageSources().magic(), entityData.get(MAGIC_DAMAGE));
                        target.hurt(ModDamageSources.of("turret_beam_physical", null, this.level()), entityData.get(MOB_DAMAGE));
                        if(target instanceof LivingEntity livingEntity) {
                            applyKnockback(livingEntity);
                            if(target instanceof INoXPEntity noXPEntity) {
                                noXPEntity.setExperienceDropAllowance(true);
                            }
                        }

                        // Allow other turrets to deal damage sequentially
                        target.invulnerableTime = 0;
                    }
                }

            }
            else {
                this.discard();
            }
        }
        if(entityData.get(DATA_TICK) >= getChargeSpeed() + 5) {
            this.cachedTarget = null;
            this.targetUUID = null;
            this.discard();
        }
    }

    private void applyKnockback(LivingEntity entity) {
        double d0 = this.getX() - entity.getX();

        double d1;
        for(d1 = this.getZ() - entity.getZ(); d0 * d0 + d1 * d1 < 1.0E-4D; d1 = (Math.random() - Math.random()) * 0.01D) {
            d0 = (Math.random() - Math.random()) * 0.01D;
        }

        entity.knockback(0.4F, d0, d1);
    }

    @Nullable
    public Entity getCachedTarget() {
        if(this.level().isClientSide) {
            Entity target = this.level().getEntity(this.entityData.get(DATA_CACHED_TARGET_ID));
            if(target instanceof LivingEntity livingTarget) return livingTarget;
            else return null;
        }
        else {
            return this.level().getEntity(this.entityData.get(DATA_CACHED_TARGET_ID));
        }
    }

    @Nullable
    public Entity getTarget() {
        if (this.cachedTarget != null && !this.cachedTarget.isRemoved()) {
            return this.cachedTarget;
        } else if (this.targetUUID != null && this.level() instanceof ServerLevel) {
            this.cachedTarget = ((ServerLevel)this.level()).getEntity(this.targetUUID);
            return this.cachedTarget;
        } else {
            return null;
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_TICK, 0);
        builder.define(CHARGE_SPEED, 120.0F);
        builder.define(DATA_CACHED_TARGET_ID, 0);
        builder.define(MAX_RANGE, 17);
        builder.define(MAGIC_DAMAGE, 3);
        builder.define(MOB_DAMAGE, 6);
        builder.define(IGNORE_INVUL_FRAMES, false);
    }

    public float getAttackAnimationScale(float p_32813_) {
        return ((float)this.clientSideAttackTime + p_32813_) / getChargeSpeed();
    }

    public int getClientSideAttackTime() {
        return clientSideAttackTime;
    }

    public float getChargeSpeed() {
        return entityData.get(CHARGE_SPEED);
    }

    public int getTick() {
        return this.entityData.get(DATA_TICK);
    }

    public int getBeamTick() {
        return this.beamTick;
    }

    public @Nullable BlockEntity getCachedSource() {
        return this.cachedSource;
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

    @Override
    protected void readAdditionalSaveData(CompoundTag p_20052_) {
        if (p_20052_.hasUUID("Target")) {
            this.targetUUID = p_20052_.getUUID("Target");
        }
    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag p_20139_) {
        if (this.targetUUID != null) {
            p_20139_.putUUID("Target", this.targetUUID);
        }
    }

    public void writeSpawnData(FriendlyByteBuf buffer) {
    }

    public void readSpawnData(FriendlyByteBuf additionalData) {
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
