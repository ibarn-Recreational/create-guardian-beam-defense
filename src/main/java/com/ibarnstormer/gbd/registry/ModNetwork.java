package com.ibarnstormer.gbd.registry;

import com.ibarnstormer.gbd.network.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

public class ModNetwork {

    /*
    public static void sendToPlayer(ServerPlayer sender, IModPacket packet) {
        ModChannel.send(PacketDistributor.PLAYER.with(() -> sender), packet);
    }

    public static void sendPacketToServer(IModPacket packet) {
        ModChannel.send(PacketDistributor.SERVER.noArg(), packet);
    }

    public static void sendToNearbyPlayers(ServerPlayer sender, IModPacket packet, double radius, boolean excludeSender) {
        ModChannel.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(excludeSender ? sender : null, sender.getX(), sender.getY(), sender.getZ(), radius, sender.level().dimension())), packet);
    }

    public static void sendToNearbyPlayersRaw(Vec3 pos, IModPacket packet, double radius, Level level) {
        ModChannel.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(null, pos.x, pos.y, pos.z, radius, level.dimension())), packet);
    }
    */


}
