package com.ibarnstormer.gbd.sounds;

import com.ibarnstormer.gbd.entities.BeamReactorLaserEntity;
import com.ibarnstormer.gbd.utils.Utils;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;

public class BeamReactorSoundInstance extends AbstractTickableSoundInstance {
    private static final float VOLUME_MIN = 0.0F;
    private static final float VOLUME_SCALE = 1.0F;
    private static final float PITCH_MIN = 0.7F;
    private static final float PITCH_SCALE = 0.5F;
    private final Player player;
    private final BeamReactorLaserEntity beam;

    public BeamReactorSoundInstance(Player player, BeamReactorLaserEntity beam) {
        super(SoundEvents.GUARDIAN_ATTACK, SoundSource.PLAYERS, SoundInstance.createUnseededRandom());
        this.player = player;
        this.beam = beam;
        this.attenuation = SoundInstance.Attenuation.NONE;
        this.looping = false;
        this.delay = 0;
    }

    @Override
    public void tick() {
        if(player != null && beam != null) {
            if (Utils.isBeamLaserEnabled(player) && Utils.canActivateLaser(player) && !beam.isRemoved() && player.isAlive()) {
                this.x = this.player.getX();
                this.y = this.player.getY();
                this.z = this.player.getZ();
                float f = this.beam.getAttackAnimationScale(0.0F);
                this.volume = VOLUME_MIN + VOLUME_SCALE * f * f;
                this.pitch = PITCH_MIN + PITCH_SCALE * f;
            } else this.stop();
        }
        else this.stop();
    }
}
