package com.ibarnstormer.gbd.network;

import com.ibarnstormer.gbd.Main;
import com.ibarnstormer.gbd.event.ModEvents;
import com.ibarnstormer.gbd.utils.Utils;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.function.Supplier;

public record BeamReactorRayTracePayload(double rayX, double rayY, double rayZ, GameProfile profile) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<BeamReactorRayTracePayload> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Main.MODID, "raycast_beam_laser"));

    public static final StreamCodec<FriendlyByteBuf, BeamReactorRayTracePayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.DOUBLE,
            BeamReactorRayTracePayload::rayX,
            ByteBufCodecs.DOUBLE,
            BeamReactorRayTracePayload::rayY,
            ByteBufCodecs.DOUBLE,
            BeamReactorRayTracePayload::rayZ,
            ByteBufCodecs.GAME_PROFILE,
            BeamReactorRayTracePayload::profile,
            BeamReactorRayTracePayload::new);

    public static void handle(BeamReactorRayTracePayload payload, IPayloadContext context) {
        try {
            Player player = context.player();

            GameProfile profile = payload.profile();

            if(player.getUUID().equals(profile.getId())) ModEvents.handleBeam(player, new Vec3(payload.rayX(), payload.rayY(), payload.rayZ()));
        }
        catch(Exception ignored) {}
    }

    @Override
    public CustomPacketPayload.Type<BeamReactorRayTracePayload> type() {
        return TYPE;
    }

}
