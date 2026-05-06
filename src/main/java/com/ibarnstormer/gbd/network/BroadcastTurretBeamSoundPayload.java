package com.ibarnstormer.gbd.network;

import com.ibarnstormer.gbd.Main;
import com.ibarnstormer.gbd.event.ClientModEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record BroadcastTurretBeamSoundPayload(int laserID) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<BroadcastTurretBeamSoundPayload> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Main.MODID, "broadcast_beam_turret_sound"));

    public static final StreamCodec<FriendlyByteBuf, BroadcastTurretBeamSoundPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            BroadcastTurretBeamSoundPayload::laserID,
            BroadcastTurretBeamSoundPayload::new);


    public static void handle(BroadcastTurretBeamSoundPayload payload, IPayloadContext context) {
        try {
            if(Minecraft.getInstance().level != null) {
                if (Minecraft.getInstance().player != null) {
                    Entity beam = Minecraft.getInstance().level.getEntity(payload.laserID());
                    ClientModEvents.ClientForgeEvents.handleTurretBeamSound(beam);
                }
            }
        }
        catch(Exception ignored) {}
    }

    @Override
    public Type<BroadcastTurretBeamSoundPayload> type() {
        return TYPE;
    }

}
