package com.ibarnstormer.gbd.registry;

import com.ibarnstormer.gbd.items.BeamReactorHelmet;
import com.ibarnstormer.gbd.items.ElderGuardianBeamCapacitor;
import com.ibarnstormer.gbd.items.GuardianBeamCapacitor;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Rarity;

import static com.ibarnstormer.gbd.Main.REGISTRATE;

public class ModItems {

    public static final ItemEntry<BeamReactorHelmet> BEAM_REACTOR_HELMET = REGISTRATE.item("beam_reactor_helmet", BeamReactorHelmet::new)
            .properties(p -> p.rarity(Rarity.UNCOMMON).stacksTo(1).durability(ArmorItem.Type.HELMET.getDurability(41)).fireResistant())
            .register();

    public static final ItemEntry<GuardianBeamCapacitor> GUARDIAN_BEAM_CAPACITOR = REGISTRATE.item("guardian_beam_capacitor", GuardianBeamCapacitor::new)
            .register();

    public static final ItemEntry<ElderGuardianBeamCapacitor> ELDER_GUARDIAN_BEAM_CAPACITOR = REGISTRATE.item("elder_guardian_beam_capacitor", ElderGuardianBeamCapacitor::new)
            .properties(p -> p.rarity(Rarity.UNCOMMON))
            .register();

    public static void register() {}
}
