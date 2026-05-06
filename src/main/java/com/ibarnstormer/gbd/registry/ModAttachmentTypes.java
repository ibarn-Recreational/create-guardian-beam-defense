package com.ibarnstormer.gbd.registry;

import com.ibarnstormer.gbd.Main;
import com.mojang.serialization.Codec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.UUID;
import java.util.function.Supplier;

public class ModAttachmentTypes {

    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, Main.MODID);

    public static final Supplier<AttachmentType<Boolean>> IS_ACTIVE = ATTACHMENT_TYPES.register(
            "is_laser_active", () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL).build());

    public static final Supplier<AttachmentType<Boolean>> CAN_TOGGLE = ATTACHMENT_TYPES.register(
            "can_toggle_laser", () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL).build());

    public static final Supplier<AttachmentType<Integer>> LASER_ID = ATTACHMENT_TYPES.register(
            "laser_id", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).build());

    public static void init(IEventBus bus) {
        ATTACHMENT_TYPES.register(bus);
    }
}
