package com.ibarnstormer.gbd;

import com.ibarnstormer.gbd.config.IConfig;
import com.ibarnstormer.gbd.datagen.ModLootModifiers;
import com.ibarnstormer.gbd.event.ModEvents;
import com.ibarnstormer.gbd.network.*;
import com.ibarnstormer.gbd.registry.*;
import com.ibarnstormer.gbd.proxies.ClientSetup;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipModifier;
import net.createmod.catnip.lang.FontHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Main.MODID)
public class Main
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "creategbd";
    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MODID).defaultCreativeTab((ResourceKey<CreativeModeTab>) null);

    static {
        REGISTRATE.setTooltipModifierFactory(item -> new ItemDescription.Modifier(item, FontHelper.Palette.STANDARD_CREATE)
                .andThen(TooltipModifier.mapNull(KineticStats.create(item))));
    }
    
    public Main(IEventBus modEventBus, ModContainer modContainer)
    {

        modContainer.registerConfig(ModConfig.Type.COMMON, IConfig.COMMON_SPEC, "create-guardian-beam-defense-common.toml");

        REGISTRATE.registerEventListeners(modEventBus);

        ModArmorMaterials.register(modEventBus);
        ModItems.register();
        ModEntities.init(modEventBus);
        ModBlocks.register();
        ModTileEntities.register();
        ModLootModifiers.register(modEventBus);
        ModAttachmentTypes.init(modEventBus);

        ModGroup.register(modEventBus);
        modEventBus.addListener(ClientSetup::init);
        modEventBus.addListener(this::registerNetworkPayloads);
        NeoForge.EVENT_BUS.register(ModEvents.class);

        // Register ourselves for server and other game events we are interested in
        NeoForge.EVENT_BUS.register(this);
    }

    private void registerNetworkPayloads(final RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");

        registrar.playToServer(ToggleBeamReactorPayload.TYPE, ToggleBeamReactorPayload.STREAM_CODEC, ToggleBeamReactorPayload::handle);
        registrar.playToServer(BeamReactorRayTracePayload.TYPE, BeamReactorRayTracePayload.STREAM_CODEC, BeamReactorRayTracePayload::handle);

        registrar.playToClient(BroadcastBeamReactorSoundPayload.TYPE, BroadcastBeamReactorSoundPayload.STREAM_CODEC, BroadcastBeamReactorSoundPayload::handle);
        registrar.playToClient(BroadcastTurretBeamSoundPayload.TYPE, BroadcastTurretBeamSoundPayload.STREAM_CODEC, BroadcastTurretBeamSoundPayload::handle);
        registrar.playToClient(SyncBeamReactorAttachmentsPayload.TYPE, SyncBeamReactorAttachmentsPayload.STREAM_CODEC, SyncBeamReactorAttachmentsPayload::handle);
    }
    
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        
    }

}
