package com.ibarnstormer.gbd.network;

import com.ibarnstormer.gbd.Main;
import com.ibarnstormer.gbd.utils.Utils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SyncBeamReactorAttachmentsPayload(boolean isActive, boolean canToggle, int laserID) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<SyncBeamReactorAttachmentsPayload> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Main.MODID, "sync_beam_reactor_attachments"));

    public static final StreamCodec<FriendlyByteBuf, SyncBeamReactorAttachmentsPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            SyncBeamReactorAttachmentsPayload::isActive,
            ByteBufCodecs.BOOL,
            SyncBeamReactorAttachmentsPayload::canToggle,
            ByteBufCodecs.INT,
            SyncBeamReactorAttachmentsPayload::laserID,
            SyncBeamReactorAttachmentsPayload::new);

    public static void handle(SyncBeamReactorAttachmentsPayload payload, IPayloadContext context) {
        try {
            Player player = context.player();

            Utils.setLaserAllowance(player, payload.canToggle());
            Utils.toggleBeamLaser(player, payload.isActive());
            Utils.setLaserID(player, payload.laserID());
        }
        catch(Exception ignored) {}
    }

    @Override
    public Type<SyncBeamReactorAttachmentsPayload> type() {
        return TYPE;
    }
}
