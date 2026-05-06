package com.ibarnstormer.gbd.utils;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.INamedIconOptions;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollOptionBehaviour;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ProtectedScrollOptionBehaviour<E extends Enum<E> & INamedIconOptions> extends ScrollOptionBehaviour<E> {

    private @Nullable UUID ownerUUID;

    public ProtectedScrollOptionBehaviour(Class<E> enum_, Component label, SmartBlockEntity be, ValueBoxTransform slot, @Nullable UUID ownerUUID) {
        super(enum_, label, be, slot);
        this.ownerUUID = ownerUUID;
    }

    @Override
    public void setValueSettings(Player player, ValueSettings valueSetting, boolean ctrlDown) {
        if((ownerUUID != null && player.level() instanceof ServerLevel server && server.getEntity(ownerUUID) != player && !player.isCreative()) || (ownerUUID == null && !player.isCreative())) return;
        super.setValueSettings(player, valueSetting, ctrlDown);
    }

    public void updateOwner(UUID ownerUUID) {
        this.ownerUUID = ownerUUID;
    }

    public @Nullable UUID getOwnerUUID(){
        return ownerUUID;
    }
}
