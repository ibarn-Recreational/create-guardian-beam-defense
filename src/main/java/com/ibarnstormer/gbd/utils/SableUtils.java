package com.ibarnstormer.gbd.utils;

import dev.ryanhcode.sable.companion.SableCompanion;
import net.minecraft.core.Position;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class SableUtils {

    public static Vec3 getSableAdjustedVelocity(Level level, Position pos) {
        return SableCompanion.INSTANCE.getVelocity(level, pos);
    }

    public static Position getSableAdjustedPos(Level level, Position pos) {
        return SableCompanion.INSTANCE.projectOutOfSubLevel(level, pos);
    }

    public static boolean entityIsPartOfSableAssembly(Entity entity) {
        return SableCompanion.INSTANCE.isInPlotGrid(entity);
    }

    public static boolean posIsPartOfSableAssembly(Level level, Position pos) {
        return SableCompanion.INSTANCE.isInPlotGrid(level, pos);
    }


}
