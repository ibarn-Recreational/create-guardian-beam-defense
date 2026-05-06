package com.ibarnstormer.gbd;

import com.ibarnstormer.gbd.registry.ModItems;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber(modid = Main.MODID)
public class ModGroup {

    private static final DeferredRegister<CreativeModeTab> TAB_REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Main.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> INSTANCE = TAB_REGISTRY.register("main",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.GUARDIAN_BEAM_CAPACITOR.get()))
                    .title(Component.translatable(Main.MODID + ".itemGroup.creategbd"))
                    .build());

    public static void register(IEventBus modbus) {
        TAB_REGISTRY.register(modbus);
    }

    @SubscribeEvent
    private static void onBuildCreative(BuildCreativeModeTabContentsEvent event) {
        if(event.getTabKey() == INSTANCE.getKey()) {
            CreateRegistrate registrate = Main.REGISTRATE;

            for(RegistryEntry<Item, Item> entry : registrate.getAll(Registries.ITEM)) {
                Item item = entry.get();
                if(item == Items.AIR) continue;
                event.accept(item.asItem());
            }
        }
    }

}
