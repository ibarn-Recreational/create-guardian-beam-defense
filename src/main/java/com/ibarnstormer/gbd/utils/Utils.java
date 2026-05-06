package com.ibarnstormer.gbd.utils;

import com.ibarnstormer.gbd.registry.ModAttachmentTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.neoforged.fml.loading.LoadingModList;
import org.jetbrains.annotations.Nullable;

public class Utils {

    public static HitResult rayCast(Entity entity, Minecraft mc, double range) {
        return rayCastRaw(entity, mc.getTimer().getGameTimeDeltaPartialTick(true), range);
    }

    public static HitResult rayCastRaw(Entity entity, float partialTick, double range) {
        Vec3 camPos = new Vec3(entity.getX(), entity.getEyeY(), entity.getZ());
        Vec3 rotation = entity.getViewVector(partialTick);
        Vec3 end = camPos.add(rotation.x * range, rotation.y * range, rotation.z * range);
        return entity.level().clip(new ClipContext(camPos, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity));
    }

    public static boolean hasLOS(LivingEntity target, BlockPos pos, @Nullable Level level) {
        if(level != null && target.level().dimension() == level.dimension()) {
            Vec3 vec3 = new Vec3(target.getX(), target.getY() + target.getBbHeight() * 0.5D, target.getZ());
            Vec3 vec31 = new Vec3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
            Vec3 vec32 = vec31.subtract(vec3);
            Vec3 vec33 = vec31.subtract(vec32.normalize().scale(1.125));
            BlockHitResult cast = level.clip(new ClipContext(vec33, vec3, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, CollisionContext.empty()));
            return cast.getType() != HitResult.Type.BLOCK;
        }
        else return false;
    }

    public static void setLaserAllowance(Player player, boolean b) {
        player.setData(ModAttachmentTypes.CAN_TOGGLE, b);
    }

    public static boolean canActivateLaser(Player player) {
        return player.getData(ModAttachmentTypes.CAN_TOGGLE);
    }

    public static void toggleBeamLaser(Player player, boolean b) {
        player.setData(ModAttachmentTypes.IS_ACTIVE, b);
    }

    public static boolean isBeamLaserEnabled(Player player) {
        return player.getData(ModAttachmentTypes.IS_ACTIVE);
    }

    public static void setLaserID(Player player, int laserID) {
        player.setData(ModAttachmentTypes.LASER_ID, laserID);
    }

    public static int getLaserID(Player player) {
        return player.getData(ModAttachmentTypes.LASER_ID);
    }

    public static boolean isTeammate(@Nullable Entity target, Entity teammate) {
        if(target != null) {
            return target.isAlliedTo(teammate);
        }
        else return false;
    }

    public static Position getSableAdjustedPos(Level level, Position pos) {
        if(LoadingModList.get().getModFileById("sable") != null) {
            return SableUtils.getSableAdjustedPos(level, pos);
        }
        else return pos;
    }

    public static Vec3 getSableAdjustedVelocity(Level level, Position pos) {
        if(LoadingModList.get().getModFileById("sable") != null) {
            return SableUtils.getSableAdjustedVelocity(level, pos);
        }
        else return Vec3.ZERO;
    }

    public static boolean entityIsPartOfSableAssembly(Entity entity) {
        if(LoadingModList.get().getModFileById("sable") != null) {
            return SableUtils.entityIsPartOfSableAssembly(entity);
        }
        else return false;
    }

    public static boolean posIsPartOfSableAssembly(Level level, Position pos) {
        if(LoadingModList.get().getModFileById("sable") != null) {
            return SableUtils.posIsPartOfSableAssembly(level, pos);
        }
        else return false;
    }

    public static float getAttackAnimationScale(int tick, float chargeSpeed, float tickDelta) {
        return ((float)tick + tickDelta) / chargeSpeed;
    }

}
