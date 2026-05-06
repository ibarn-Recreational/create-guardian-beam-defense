package com.ibarnstormer.gbd.network;

import com.ibarnstormer.gbd.Main;
import com.ibarnstormer.gbd.utils.Utils;
import com.mojang.authlib.GameProfile;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ToggleBeamReactorPayload(boolean toggle, GameProfile profile) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<ToggleBeamReactorPayload> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Main.MODID, "toggle_beam_laser"));

    public static final StreamCodec<FriendlyByteBuf, ToggleBeamReactorPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            ToggleBeamReactorPayload::toggle,
            ByteBufCodecs.GAME_PROFILE,
            ToggleBeamReactorPayload::profile,
            ToggleBeamReactorPayload::new);


    public static void handle(ToggleBeamReactorPayload payload, IPayloadContext context) {
        try {
            Player player = context.player();

            boolean toggle = payload.toggle;
            GameProfile profile = payload.profile();

            if(Utils.canActivateLaser(player) && player.getUUID().equals(profile.getId())) Utils.toggleBeamLaser(player, toggle);
        }
        catch(Exception ignored) {}
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
