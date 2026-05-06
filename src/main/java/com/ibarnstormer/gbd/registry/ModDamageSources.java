package com.ibarnstormer.gbd.registry;

import com.ibarnstormer.gbd.Main;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.Optional;

public class ModDamageSources {

    public static DamageSource of(String id, @Nullable Entity attacker, Level world) {
        ResourceKey<DamageType> type = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(Main.MODID, id));
        Optional<Registry<DamageType>> registry = world.registryAccess().registry(Registries.DAMAGE_TYPE);
        if(registry.isPresent()) return new DamageSource(registry.get().getHolderOrThrow(type), attacker);
        else throw new NullPointerException("Provided damage id does not exist or cannot be found.");
    }

}
