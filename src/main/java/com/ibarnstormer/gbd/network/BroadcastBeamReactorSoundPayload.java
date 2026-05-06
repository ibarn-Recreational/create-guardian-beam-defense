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

public record BroadcastBeamReactorSoundPayload(int playerID, int beamID) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<BroadcastBeamReactorSoundPayload> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Main.MODID, "broadcast_beam_reactor_sound"));

    public static final StreamCodec<FriendlyByteBuf, BroadcastBeamReactorSoundPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            BroadcastBeamReactorSoundPayload::playerID,
            ByteBufCodecs.INT,
            BroadcastBeamReactorSoundPayload::beamID,
            BroadcastBeamReactorSoundPayload::new);

    public static void handle(BroadcastBeamReactorSoundPayload payload, IPayloadContext context) {
        try {
            if(Minecraft.getInstance().level != null) {
                Entity sourcePlayer = Minecraft.getInstance().level.getEntity(payload.playerID());
                Entity beam = Minecraft.getInstance().level.getEntity(payload.beamID());
                if (Minecraft.getInstance().player != null) {
                    ClientModEvents.ClientForgeEvents.handleBeamReactorSound(sourcePlayer, beam);
                }
            }
        }
        catch(Exception ignored) {}
    }

    @Override
    public Type<BroadcastBeamReactorSoundPayload> type() {
        return TYPE;
    }
}
