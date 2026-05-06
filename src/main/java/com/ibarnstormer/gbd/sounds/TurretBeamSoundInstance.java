package com.ibarnstormer.gbd.sounds;

import com.ibarnstormer.gbd.entities.TurretBeamLaserEntity;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

public class TurretBeamSoundInstance extends AbstractTickableSoundInstance {
    private static final float VOLUME_MIN = 0.0F;
    private static final float VOLUME_SCALE = 1.0F;
    private static final float PITCH_MIN = 0.7F;
    private static final float PITCH_SCALE = 0.5F;
    private final TurretBeamLaserEntity laser;

    public TurretBeamSoundInstance(TurretBeamLaserEntity laser) {
        super(SoundEvents.GUARDIAN_ATTACK, SoundSource.BLOCKS, SoundInstance.createUnseededRandom());
        this.laser = laser;
    }

    @Override
    public void tick() {
        if(laser != null) {
            if(!laser.isRemoved()) {
                this.x = laser.position().x;
                this.y = laser.position().y;
                this.z = laser.position().z;
                float f = laser.getAttackAnimationScale(0.0F);
                this.volume = VOLUME_MIN + VOLUME_SCALE * f * f;
                this.pitch = PITCH_MIN + PITCH_SCALE * f;
            } else this.stop();
        } else this.stop();
    }
}
