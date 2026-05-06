package com.ibarnstormer.gbd.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class IConfig {

    public static final ModConfigSpec COMMON_SPEC;
    public static final Common COMMON;

    static {
        final Pair<Common, ModConfigSpec> pair = new ModConfigSpec.Builder().configure(Common::new);
        COMMON_SPEC = pair.getRight();
        COMMON = pair.getLeft();
    }

    public static class Common {

        public final ModConfigSpec.BooleanValue
            basicGuardianBeamTurretIgnoresInvulFrames,
            advancedGuardianBeamTurretIgnoresInvulFrames,
            beamReactorHelmetScalesDamage,
            beamReactorHelmetIgnoresInvulFrames;

        public final ModConfigSpec.IntValue
            basicGuardianBeamTurretPhysicalDamage,
            basicGuardianBeamTurretMagicDamage,
            basicGuardianBeamTurretMaxRange,
            basicGuardianBeamTurretMaxTargets,
            advancedGuardianBeamTurretPhysicalDamage,
            advancedGuardianBeamTurretMagicDamage,
            advancedGuardianBeamTurretMaxRange,
            advancedGuardianBeamTurretMaxTargets,
            beamReactorHelmetPhysicalDamage,
            beamReactorHelmetMagicDamage,
            beamReactorHelmetMaxRange;

        public Common(ModConfigSpec.Builder builder) {
            builder.comment("Create Guardian Beam Defense common configuration settings").push("Basic Guardian Beam Turret Settings");

            basicGuardianBeamTurretPhysicalDamage = builder.comment("The amount of 'mob' damage the basic turret inflicts (Default: 6)").worldRestart()
                    .defineInRange("Basic Guardian Beam Turret Physical Damage", 6, 1, Integer.MAX_VALUE);

            basicGuardianBeamTurretMagicDamage = builder.comment("The amount of 'magic' damage the basic turret inflicts (Default: 3)").worldRestart()
                    .defineInRange("Basic Guardian Beam Turret Magic Damage", 3, 1, Integer.MAX_VALUE);

            basicGuardianBeamTurretMaxRange = builder.comment("The maximum range of entities that the basic turret can target (Default: 17)").worldRestart()
                    .defineInRange("Basic Guardian Beam Turret Maximum Range", 17, 1, 30);

            basicGuardianBeamTurretMaxTargets = builder.comment("The maximum amount of entities that the basic turret can target at the same time (Default: 1)").worldRestart()
                    .defineInRange("Basic Guardian Beam Turret Maximum Targets", 1, 1, 20);

            basicGuardianBeamTurretIgnoresInvulFrames = builder.comment("Whether or not the basic turret ignores invulnerability frames (Default: false)").worldRestart()
                    .define("Basic Guardian Beam Turret Ignores Invulnerability Frames", false);

            builder.pop().push("Advanced Guardian Beam Turret Settings");

            advancedGuardianBeamTurretPhysicalDamage = builder.comment("The amount of 'mob' damage the advanced turret inflicts (Default: 6)").worldRestart()
                    .defineInRange("Advanced Guardian Beam Turret Physical Damage", 6, 1, Integer.MAX_VALUE);

            advancedGuardianBeamTurretMagicDamage = builder.comment("The amount of 'magic' damage the advanced turret inflicts (Default: 3)").worldRestart()
                    .defineInRange("Advanced Guardian Beam Turret Magic Damage", 3, 1, Integer.MAX_VALUE);

            advancedGuardianBeamTurretMaxRange = builder.comment("The maximum range of entities that the advanced turret can target (Default: 17)").worldRestart()
                    .defineInRange("Advanced Guardian Beam Turret Maximum Range", 17, 1, 30);

            advancedGuardianBeamTurretMaxTargets = builder.comment("The maximum amount of entities that the basic turret can target at the same time (Default: 10)").worldRestart()
                    .defineInRange("Advanced Guardian Beam Turret Maximum Targets", 10, 1, 20);

            advancedGuardianBeamTurretIgnoresInvulFrames = builder.comment("Whether or not the advanced turret ignores invulnerability frames (Default: false)").worldRestart()
                    .define("Advanced Guardian Beam Turret Ignores Invulnerability Frames", false);

            builder.pop().push("Beam Reactor Helmet Settings");

            beamReactorHelmetPhysicalDamage = builder.comment("The amount of 'player' damage the beam reactor helmet's laser inflicts (Default: 8)").worldRestart()
                    .defineInRange("Beam Reactor Helmet Physical Damage", 8, 1, Integer.MAX_VALUE);

            beamReactorHelmetMagicDamage = builder.comment("The amount of 'magic' damage the beam reactor helmet's laser inflicts (Default: 4)").worldRestart()
                    .defineInRange("Beam Reactor Helmet Magic Damage", 4, 1, Integer.MAX_VALUE);

            beamReactorHelmetMaxRange = builder.comment("The maximum range of the beam reactor helmet's laser (Default: 15)").worldRestart()
                    .defineInRange("Beam Reactor Helmet Maximum Range", 15, 1, 30);

            beamReactorHelmetScalesDamage = builder.comment("Whether or not the beam reactor helmet's laser damage increases based on the player's base attack damage attribute (e.g. when a player drinks a strength potion) (Default: true)").worldRestart()
                    .define("Beam Reactor Helmet Scales Damage", true);

            beamReactorHelmetIgnoresInvulFrames = builder.comment("Whether or not the beam reactor helmet's laser ignores invulnerability frames (makes the helmet very OP and broken lol) (Default: false)").worldRestart()
                    .define("Beam Reactor Helmet Ignores Invulnerability Frames", false);

            builder.pop();

        }

    }

}
