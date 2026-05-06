package com.ibarnstormer.gbd.mixin;

import com.ibarnstormer.gbd.entities.INoXPEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin implements INoXPEntity {

    @Unique
    private boolean canDropExperience = true;

    @Override
    public void setExperienceDropAllowance(boolean b) {
        this.canDropExperience = b;
    }

    @Inject(method = "dropExperience", at = @At("HEAD"), cancellable = true)
    private void livingEntity$dropExperience(Entity entity, CallbackInfo ci) {
        if(!this.canDropExperience) ci.cancel();
    }
}
